/*  Superclass for Transformers for Organizer formats
    @(#) $Id: OrganizerTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2024-12-26: deprecations
    2007-10-14: reanimated - ok for VCard and ICalendar
    2006-11-04, Dr. Georg Fischer

    caution, must be stored as UTF-8 (äöüÄÖÜß)
*/
/*
 * Copyright 2006 Dr. Georg Fischer <dr dot georg dot fischer at gmail dot kom>
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

package org.teherba.xtrans.organizer;
import  org.teherba.xtrans.CharTransformer;
import  java.io.BufferedReader;
import  java.util.ArrayList;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** This class provides common features of transformers for some
 *  related formats like vCard or iCalendar. These features include:
 *  <ul>
 *  <li>lines with "parameter:value" elements</li>
 *  <li>parameter lists separated by ";"</li>
 *  <li>value lists separated by ","</li>
 *  <li>nested "BEGIN:element" and "END:element" bracketing
 *  <li>line folding</li>
 *  <li>Base 64 and/or Quoted Printable encoding</li>
 *  </ul>
 *  Example:
 *  <pre>
BEGIN:vCard
VERSION:3.0
FN:Tim Howes
ORG:Netscape Communications Corp.
ADR;TYPE=WORK:;;501 E. Middlefield Rd.;Mountain View;
 CA; 94043;U.S.A.
TEL;TYPE=VOICE,MSG,WORK:+1-415-937-3419
TEL;TYPE=FAX,WORK:+1-415-528-4164
EMAIL;TYPE=INTERNET:howes@netscape.com
END:vCard
 *  </pre>
 *  @author Dr. Georg Fischer
 */
public class OrganizerTransformer extends CharTransformer {
    public final static String CVSID = "@(#) $Id: OrganizerTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    protected Logger log;

    /** Tag for the whole address book entry */
    protected static final String ROOT_TAG    = "Document";
    /** Tag for one address book entry */
    protected static final String NEWLINE_TAG = "n";
    /** offsets of starts of continuation lines */
    protected ArrayList/*<1.5*/<Integer>/*1.5>*/ offsets;

    /** No-args Constructor.
     */
    public OrganizerTransformer() {
        super();
    } // Constructor

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = LogManager.getLogger(OrganizerTransformer.class.getName());
    } // initialize

    /** buffer for folded lines */
    protected StringBuffer foldBuffer;

    /** Number of lines read so far */
    protected int lineCount;

    /** Generates all elements derived from one logical line
     *  contained in <em>foldBuffer</em>
     *  (maybe folded from several physical lines).
     */
    private void evaluateFoldedLine() {
        String line = foldBuffer.toString();
        // System.out.println("line: " + line);
        if (line.length() > 0) {
            int pos = line.indexOf(":");
            if (pos < 0) {
                log.warn("no keyword in line " + (lineCount - 1));
                fireEmptyElement("error", toAttribute("text", "no keyword in line " + (lineCount - 1)));
                fireLineBreak();
            } else {
                String [] pairs = line.substring(0, pos).split(";"); // tag(;attribute[=value])*
                pos ++; // skip colon
                ArrayList/*<1.5*/<String>/*1.5>*/ keyValues = new ArrayList/*<1.5*/<String>/*1.5>*/(16);
                int ipair = 0;
                String tag = pairs[ipair ++];
                while (ipair < pairs.length) {
                    String [] pair = pairs[ipair].split("=");
                    keyValues.add(pair[0]);
                    if (pair.length > 1) {
                        keyValues.add(pair[1]);
                    } else {
                        keyValues.add("");
                    }
                    ipair ++;
                } // while ipair
                fireStartElement(tag, toAttributes(keyValues));
                int ioff = 0;
                int offset = pos;
                while (ioff < offsets.size()) {
                    int noff = ((Integer) offsets.get(ioff)).intValue();
                    fireCharacters(line.substring(offset, noff));
                    fireEmptyElement(NEWLINE_TAG);
                    offset = noff;
                    ioff ++;
                } // while ioff
                if (offset < line.length()) {
                    fireCharacters(line.substring(offset));
                }
                fireEndElement(tag);
                fireLineBreak();
            } // line with keyword
        } // non-empty line
        offsets = new ArrayList/*<1.5*/<Integer>/*1.5>*/(16);
        foldBuffer.setLength(0); // start over
    } // evalFoldedLine

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        foldBuffer = new StringBuffer(2048); // not too small because of BAS64 encoded values
        offsets = new ArrayList/*<1.5*/<Integer>/*1.5>*/(16);
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            String line; // physical
            lineCount = 0;
            BufferedReader buffReader = new BufferedReader(charReader);
            while ((line = buffReader.readLine()) != null) {
                lineCount ++;
                if (false) {
                } else if (line.length() == 0) {
                    evaluateFoldedLine();
                } else if (line.startsWith(" ")) { // continuation, logical line was folded
                    offsets.add(Integer.valueOf(foldBuffer.length()));
                    foldBuffer.append(line.substring(1)); // without the space
                } else { // start of new logical line
                    evaluateFoldedLine();
                    foldBuffer.append(line);
                }
            } // while not EOF
            buffReader.close();
            evaluateFoldedLine();
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

    /** buffer for output line */
    private StringBuffer serBuffer;

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        serBuffer = new StringBuffer(296); // not too long (max. 76 anyway?)
    }

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
        if (false) {
        } else if (qName.equals(ROOT_TAG   )) {
        } else if (qName.equals(NEWLINE_TAG)) {
            charWriter.println(serBuffer.toString());
            serBuffer.setLength(0);
            charWriter.print(" ");
        } else { // all format specific elements
            serBuffer.setLength(0);
            charWriter.print(qName);
            int nattr = attrs.getLength();
            int iattr = 0;
            while (iattr < nattr) {
                charWriter.print(";" + attrs.getQName(iattr));
                String value = attrs.getValue(iattr);
                if (value != null) {
                    charWriter.print("=" + value);
                }
                iattr ++;
            } // while iattr
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
        if (false) {
        } else if (qName.equals(ROOT_TAG   )) {
        } else if (qName.equals(NEWLINE_TAG)) {
        } else { // all format specific elements
           charWriter.println(serBuffer.toString());
        }
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

} // OrganizerTransformer
