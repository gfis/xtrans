/*  Transforms LDIF (LDAP Data Interchange Format)
    used for example in addressbooks of Mozilla Thunderbird
    Caution, must be stored as UTF-8 (äöüÄÖÜß)
    @(#) $Id: LDIFTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2007-10-12: don't output ignorable whitespace space (around <url>),
    2007-10-12: and expand/shrink entities in comments
    2006-11-04, Dr. Georg Fischer
*/
/*
 * Copyright 2006 Dr. Georg Fischer <punctum at punctum dot kom>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.teherba.xtrans.net;
import  org.teherba.xtrans.CharTransformer;
import  java.io.BufferedReader;
import  java.nio.charset.Charset;
import  java.nio.charset.CharsetEncoder;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transformer for LDIF (LDAP Data Interchange Format)
 *  used for example in addressbooks of Mozilla Thunderbird.
 *  LDIF is an ASCII-based format for the description of entries in
 *  an LDAP directory, and is described in
 *  <a href="http://www.ietf.org/rfc/rfc2849.txt">RFC 2849</a>.
 *  The file consists of blocks separated by empty lines.
 *  Each block has lines which start with a key, a colon, and the value
 *  which in turn may consist of a comma separated list of "subkey=subvalue" pairs.
 *  A double colon indicates a Base64 encoded value.
 *  Example:
 * <pre>
dn: cn=Georg Fischer,mail=punctum@punctum.com
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
objectclass: mozillaAbPersonAlpha
givenName: Georg
sn: Fischer
cn: Georg Fischer
mail: punctum@punctum.com
modifytimestamp: 0Z

dn: mail=vvd-info@vwfsag.de
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
objectclass: mozillaAbPersonAlpha
mail: vvd-info@vwfsag.de
modifytimestamp: 0Z

dn:: Y249QnVjaGhhbmRsdW5nIELDvGNoZXJ3dXJtLG1haWw9YnVlY2hlci53dXJtQHQtb25saW5lLmRl
-----&gt; translates to:
dn: cn=Buchhandlung Bücherwurm,mail=buecher.wurm@t-online.de
objectclass: top
objectclass: person
objectclass: organizationalPerson
objectclass: inetOrgPerson
objectclass: mozillaAbPersonAlpha
givenName: Buchhandlung
sn:: QsO8Y2hlcnd1cm0=
cn:: QnVjaGhhbmRsdW5nIELDvGNoZXJ3dXJt
mail: buecher.wurm@t-online.de
modifytimestamp: 0Z
 *  </pre>
 *  @author Dr. Georg Fischer
 */
public class LDIFTransformer extends CharTransformer {
    public final static String CVSID = "@(#) $Id: LDIFTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** the delimiter for keywords */
    private static String COLON = ":";

    /** Tag for the whole address book  */
    private static final String ROOT_TAG    = "root";
    /** Tag for one address book entry */
    private static final String ENTRY_TAG   = "entry";
    /** Tag for comments */
    private static final String COMMENT_TAG = "comment";
    /** Tag for end of mod-spec */
    private static final String EOMS_TAG    = "mod-spec-";
    /** Tag for URLs */
    private static final String URL_TAG     = "url";

    /** No-args Constructor.
     */
    public LDIFTransformer() {
        super();
        setFormatCodes("ldif");
        setDescription("LDAP Data Interchange File");
        setFileExtensions("ldif,ldi");
    } // Constructor()

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = LogManager.getLogger(LDIFTransformer.class.getName());
    } // initialize

    /** buffer for folded lines */
    private StringBuffer foldBuffer = new StringBuffer(512);

    /** Number of lines read so far */
    protected int lineCount;

    /** Generates all elements derived from one logical line
     *  contained in <em>foldBuffer</em>
     *  (maybe folded from several physical lines).
     */
    private void evaluateFoldedLine() {
        String line = foldBuffer.toString();
        if (line.length() == 0) {
        } else if (line.startsWith("#")) { // comment
            fireStartElement(COMMENT_TAG);
            fireCharacters (line.substring(1) // remove "#"
                    .replaceAll("&", "&amp;")
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    );
            fireEndElement(COMMENT_TAG);
            fireLineBreak();
        } else {
            int pos = line.indexOf(":");
            if (pos < 0) {
                log.warn("no keyword in line " + (lineCount - 1));
                fireEmptyElement("error", toAttribute("text", "no keyword in line " + (lineCount - 1)));
                fireLineBreak();
            } else {
                String keyword = line.substring(0, pos ++);
                int semiPos = keyword.indexOf(";"); // whether ";option" strings are appended to the keyword
                if (semiPos < 0) { // no options
                    semiPos = keyword.length(); // for unified call to fireEndElement, below
                    fireStartElement(keyword);
                } else { // with options
                    fireStartElement(keyword.substring(0, semiPos)
                            , toAttributes(((keyword.substring(semiPos + 1) + ";")
                            .replaceAll(";", "=true=")).split("=")));
                            // there is a remaining empty element in the string array returned by 'split'
                            // but 'toAttributes' takes complete pairs only
                }
                while (pos < line.length()) {
                    while (pos < line.length() && line.charAt(pos) == ' ') { // skip FILL
                        pos ++;
                    } // while skipping FILL
                    if (line.startsWith(":", pos)) {
                        pos ++; // skip over ":"
                        // Base64 encoded value follows
                        while (pos < line.length() && line.charAt(pos) == ' ') { // skip FILL
                            pos ++;
                        } // while skipping FILL
                        fireCharacters(base64ToString(line.substring(pos), this.getResultEncoding()));
                    } else if (line.startsWith ("<", pos)) {
                        // URL follows
                        pos ++; // skip over "<"
                        while (pos < line.length() && line.charAt(pos) == ' ') { // skip FILL
                            pos ++;
                        } // while skipping FILL
                        fireStartElement(URL_TAG);
                        fireCharacters(line.substring(pos));
                        fireEndElement(URL_TAG);
                    } else {
                        fireCharacters(line.substring(pos)
                                .replaceAll("&", "&amp;")
                                .replaceAll("<", "&lt;")
                                .replaceAll(">", "&gt;")
                                );
                    }
                    pos = line.length(); // break loop
                } // while in line
                fireEndElement(keyword.substring(0, semiPos));
                fireLineBreak();
            } // line with keyword
        } // no comment
        foldBuffer.setLength(0);
    } // evaluateFoldedLine

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            int state = 0; // outside address, 1 = inside address
            String line; // physical
            foldBuffer.setLength(0);
            lineCount = 0;
            BufferedReader buffReader = new BufferedReader(charReader);
            while ((line = buffReader.readLine()) != null) {
                lineCount ++;
                if (false) {
                } else if (line.length() == 0) {
                    if (lineCount <= 1) {
                        log.warn("file starts with empty line");
                        fireEmptyElement("error", toAttribute("text", "file starts with empty line"));
                        fireLineBreak();
                    }
                    if (state == 1) {
                        evaluateFoldedLine();
                        fireEndElement(ENTRY_TAG);
                        fireLineBreak();
                    }
                    state = 0;
                } else if (line.startsWith("#")) { // (start of) comment
                    if (state == 1) {
                        evaluateFoldedLine();
                    } else if (state == 0) {
                        fireStartElement(ENTRY_TAG);
                        fireLineBreak();
                    }
                    foldBuffer.append(line);
                    state = 1;
                } else if (line.startsWith(" ")) { // continuation, logical line was folded
                    if (lineCount <= 1) {
                        log.warn("file starts with continuation line");
                        fireEmptyElement("error", toAttribute("text", "file starts with continuation line"));
                        fireLineBreak();
                        state = 1;
                    }
                    foldBuffer.append(line.substring(1));
                } else if (line.equals("-")) { // end of mod-spec
                    if (lineCount <= 1) {
                        log.warn("file starts with end of mod-spec");
                        fireEmptyElement("error", toAttribute("text", "file starts with end of mod-spec"));
                        fireLineBreak();
                        state = 1;
                    }
                    evaluateFoldedLine();
                    fireEmptyElement(EOMS_TAG);
                    fireLineBreak();
                } else { // start of new logical line
                    if (line.indexOf(":") < 0) { // continued Base64
                        // below: foldBuffer.append(line);
                    }
                    else if (state == 0) {
                        fireStartElement(ENTRY_TAG);
                        fireLineBreak();
                    } else {
                        evaluateFoldedLine();
                    }
                    state = 1;
                    foldBuffer.append(line);
                }
            } // while not EOF
            buffReader.close();
            if (state == 1) {
                evaluateFoldedLine();
                fireEndElement(ENTRY_TAG);
                fireLineBreak();
            }
            fireEndElement(ROOT_TAG);
            fireLineBreak();
            fireEndDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return  result;
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** line counter for Locator methods */
    private int serLineCount;
    /** buffer for output line */
    private StringBuffer serBuffer;

    /** currently opened element */
    private String elem;

    /** Charset Encoder which tests for US-ASCII */
    private static final CharsetEncoder ASCII_ENCODER = Charset.forName("US-ASCII").newEncoder();

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        serLineCount = 0;
        serBuffer = new StringBuffer(296); // not too long (max. 76 anyway?)
    } // startDocument

    /** Receive notification of the start of an element.
     *  Looks for the element which contains raw lines.
     *  @param uri The Namespace URI, or the empty string if the element has no Namespace URI
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix),
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix),
     *  or the empty string if qualified names are not available.
     *  @param attrs the attributes attached to the element.
     *  If there are no attributes, it shall be an empty Attributes object.
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs) {
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        elem = qName;
        if (false) {
        } else if (qName.equals(ROOT_TAG   )) {
            serLineCount = 0;
        } else if (qName.equals(ENTRY_TAG  )) {
            if (serLineCount > 0) {
                charWriter.println(""); // separator line before next entry
            }
        } else if (qName.equals(COMMENT_TAG)) {
            serBuffer.setLength(0);
        } else if (qName.equals(URL_TAG    )) {
            serBuffer.append("< ");
        } else if (qName.equals(EOMS_TAG   )) {
            serBuffer.append("< ");
        } else { // all format specific elements
            serBuffer.setLength(0);
            charWriter.print(qName);
            int nattr = attrs.getLength();
            int iattr = 0;
            while (iattr < nattr) {
                charWriter.print(";" + attrs.getQName(iattr ++));
            }
            charWriter.print(":");
        }
    } // startElement

    /** Receive notification of the end of an element.
     *  Looks for the element which contains raw lines.
     *  Terminates the line.
     *  @param uri the Namespace URI, or the empty string if the element has no Namespace URI
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix),
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix),
     *  or the empty string if qualified names are not available.
     */
    public void endElement(String uri, String localName, String qName) {
        elem = ""; // no characters allowed outside <td> ... </td>
        if (false) {
        } else if (qName.equals(ROOT_TAG )) {
        } else if (qName.equals(ENTRY_TAG )) {
        } else if (qName.equals(COMMENT_TAG)) {
            charWriter.print("#");
            charWriter.println(serBuffer.toString()
                    .replaceAll("&lt;" , "<")
                    .replaceAll("&gt;" , ">")
                    .replaceAll("&amp;", "&")
                    );
        } else if (qName.equals(EOMS_TAG   )) {
            charWriter.println("-");
        } else if (qName.equals(URL_TAG    )) {
            charWriter.print(serBuffer.toString());
            serBuffer.setLength(0);
        } else { // all format specific elements
            if (serBuffer.length() == 0) {
                charWriter.println();
            } else if ( ASCII_ENCODER.canEncode(serBuffer)
                        &&  serBuffer.indexOf("\u0000") < 0
                        &&  serBuffer.indexOf("\n") < 0
                        &&  serBuffer.indexOf("\r") < 0
                    ) {
                charWriter.println(" "  + serBuffer.toString());
            } else {
                charWriter.print(": ");
                String value = stringToBase64(serBuffer.toString(), getSourceEncoding());
                int MAX_LEN = 76;
                int pos = MAX_LEN - (qName.length() + 3);
                if (pos <= value.length() && pos > 0) {
                    charWriter.println(value.substring(0, pos));
                } else {
                    charWriter.println(value);
                    pos = value.length() + 1;
                }
                while (pos <= value.length() - MAX_LEN) {
                    charWriter.println(" " + value.substring(pos, pos + MAX_LEN));
                    pos += MAX_LEN;
                }

                if    (pos <= value.length()) {
                    charWriter.println(" " + value.substring(pos, value.length()));
                }
            }
        }
        serLineCount ++;
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int len) {
        String text = new String(ch, start, len);
        if (! text.matches("\\s+")) {
            serBuffer.append(text);
        }
    } // characters

} // LDIFTransformer
