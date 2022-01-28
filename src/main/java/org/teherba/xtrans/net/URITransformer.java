/*  Transformer for files with one URI per line.
    caution, must be stored as UTF-8 (äöüÄÖÜß)
    @(#) $Id: URITransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2006-11-23, Dr. Georg Fischer
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
import  org.teherba.xtrans.net.URIWrapper;
import  java.io.BufferedReader;
import  java.net.URISyntaxException;
import  java.util.ArrayList;
import  java.util.HashMap;
import  java.util.Iterator;
import  java.util.regex.Pattern;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transformer for files with one Uniform Resource Identifier (URI) per line.
 *  The URIs are represented by an XML structure with elements for the individual
 *  components of the URI.
 *  @author Dr. Georg Fischer
 */
public class URITransformer extends CharTransformer {
    public final static String CVSID = "@(#) $Id: URITransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** No-args Constructor.
     */
    public URITransformer() {
        super();
        setFormatCodes("uri");
        setDescription("Universal Resource Identifiers on single lines");
        setFileExtensions("uri,txt");
    } // Constructor()

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = LogManager.getLogger(URITransformer.class.getName());
        putEntityReplacements();
    } // initialize

    /** Tag for the root element */
    private static final String ROOT_TAG  = "uris";
    /** Tag for one URI */
    private static final String ENTRY_TAG = "uri";
    /** Tag for unknown scheme */
    private static final String UNDEF_TAG = "undef";

    /** Number of lines read so far */
    protected int lineCount;

    /** tag for mediatype */
    private static final String MEDIA_TAG = "mediatype";

    /** Generates XML for the "data" URI scheme described in RFC 2397.
     *  The syntax is
     *  <pre>
     *  data:[&lt;mediatype&gt;][;base64],&lt;data&gt;
     *  </pre>
     *  mediatype defaults to "text/plain;charset=US-ASCII",
     *  where "text/plain" can be omitted
     *  @param uri the URI parsed by general rules
     */
    protected void generateData(URIWrapper uri) {
        fireSimpleElement("unparsed", uri.getSchemeSpecificPart());
    } // generateData

    /** Generates XML for the "mailto"
     *  URI scheme described in RFC 2368.
     *  @param uri the URI parsed by general rules
     */
    protected void generateMailto(URIWrapper uri) {
        fireSimpleElement("unparsed", uri.getSchemeSpecificPart());
    } // generateMailto

    /** Generates XML for the "tag"
     *  URI schemes described in RFC 4151 and
     *  www.taguri.org
     *  @param uri the URI parsed by general rules
     */
    protected void generateTag(URIWrapper uri) {
        fireSimpleElement("unparsed", uri.getSchemeSpecificPart());
        generateURL(uri);
    } // generateTag

    /** Generates XML for the "tel", "fax" and "modem"
     *  URI schemes described in RFC xxxx.
     *  @param uri the URI parsed by general rules
     */
    protected void generateTel(URIWrapper uri) {
        fireSimpleElement("unparsed", uri.getSchemeSpecificPart());
    } // generateTel

    /** Generates XML for the following URL schemes:
     *  <ul>
     *  <li>ftp</li>
     *  <li>file</li>
     *  <li>gopher</li>
     *  <li>http, https</li>
     *  </ul>
     *  @param uri the URI parsed by general rules
     */
    protected void generateURL(URIWrapper uri) {
        fireSimpleElement("userInfo"    , uri.getUserInfo   ());
        fireSimpleElement("host"        , uri.getHost       ());
        String port = String.valueOf(uri.getPort());
        fireSimpleElement("port"        , (port.startsWith("-") ? null : port));
        fireSimpleElement("path"        , uri.getPath       ());
    //  fireSimpleElement("query"       , uri.getQuery      ());
        boolean first = true;
        HashMap params = uri.getParams();
        Iterator iter = params.keySet().iterator();
        while (iter.hasNext()) {
            if (first) { // only if there is any parameter
                first = false;
                fireStartElement("query");
            }
            String key   = (String) iter.next();
            Object obj = params.get(key);
            if (obj != null) {
                ArrayList al = (ArrayList) obj;
                int ial = 0;
                while (ial < al.size()) {
                    String value = (String) al.get(ial ++); // only th efirst
                    fireStartElement("param", toAttribute("id", replaceInSource(key)));
                    fireCharacters (replaceInSource(value));
                    fireEndElement  ("param");
                } // while ial
            } // obj != null
        } // while iter
        if (! first) {
            fireEndElement("query");
        }
        fireSimpleElement("fragment"    , uri.getFragment   ());
    } // generateURL

    /** Generates XML for the "urn"
     *  URI scheme described in RFC xxxx.
     *  @param uri the URI parsed by general rules
     */
    protected void generateURN(URIWrapper uri) {
        fireSimpleElement("unparsed", uri.getSchemeSpecificPart());
    } // generateURN

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
            lineCount = 0;
            BufferedReader buffReader = new BufferedReader(charReader);
            Pattern schemePattern = Pattern.compile("(\\w+):");
            while ((line = buffReader.readLine()) != null) {
                lineCount ++;
                line = line.trim(); // remove surrounding spaces
                if (line.startsWith("<") && line.endsWith(">")) {
                    line = line.substring(1, line.length() - 1); // remove "<" and ">"
                }
                if (line.startsWith("#") || line.length() == 0) {
                    // ignore comments and empty lines
                } else {
                    try {
                        URIWrapper uri = new URIWrapper(line);
                        String tag = uri.getScheme();
                        if (tag == null) {
                            tag = "relative";
                        }
                        // fireStartElement(ENTRY_TAG);
                        fireStartElement(tag);
                        if (false) {
                        } else if (tag.equals("data"    )) { generateURL    (uri);
                        } else if (tag.equals("fax"     )) { generateURL    (uri);
                        } else if (tag.equals("file"    )) { generateURL    (uri);
                        } else if (tag.equals("ftp"     )) { generateURL    (uri);
                        } else if (tag.equals("go"      )) { generateURL    (uri);
                        } else if (tag.equals("gopher"  )) { generateURL    (uri);
                        } else if (tag.equals("http"    )) { generateURL    (uri);
                        } else if (tag.equals("https"   )) { generateURL    (uri);
                    //  } else if (tag.equals("jndi"    )) { generateURN    (uri);
                        } else if (tag.equals("mailto"  )) { generateURL    (uri);
                        } else if (tag.equals("modem"   )) { generateURL    (uri);
                        } else if (tag.equals("relative")) { generateURL    (uri);
                        } else if (tag.equals("rmi"     )) { generateURN    (uri);
                        } else if (tag.equals("tag"     )) { generateTag    (uri);
                        } else if (tag.equals("tel"     )) { generateURL    (uri);
                        } else if (tag.equals("urn"     )) { generateURL    (uri);
                        } else {
                        }
                        fireEndElement  (tag);
                        // fireEndElement  (ENTRY_TAG);
                        fireLineBreak();
                    } catch (URISyntaxException exc) {
                        fireStartElement(UNDEF_TAG, toAttribute("error", exc.getReason()));
                        fireCharacters (replaceInSource(line));
                        fireEndElement  (UNDEF_TAG);
                        fireLineBreak();
                    } // catch URISyntax
                } // non-comment
            } // while not EOF
            buffReader.close();
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

    /** currently opened element */
    private String elem;

    /** buffer for successive calls of method <em>characters</em> */
    private StringBuffer serBuffer;
    /** number of current line */
    private int serLineCount;

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        serLineCount = 0;
        serBuffer = new StringBuffer(296);
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
        } else { // all format specific elements
            if (serBuffer.length() == 0) {
                charWriter.println();
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
        serBuffer.append(new String(ch, start, len));
    } // characters

} // URITransformer
