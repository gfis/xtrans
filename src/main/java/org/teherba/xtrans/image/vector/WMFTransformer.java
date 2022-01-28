/*  Transformer for WMF vector graphic files
    @(#) $Id: WMFTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2008-03-21: Georg Fischer
*/
/*
 * Copyright 2008 Dr. Georg Fischer <punctum at punctum dot kom>
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

package org.teherba.xtrans.image.vector;
import  org.teherba.xtrans.ByteTransformer;
import  org.xml.sax.Attributes;
import  org.xml.sax.SAXException;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transforms WMF (Windows Meta Format) vector graphic files to/from XML.
 *  See
 *  <ul>
 *  <li>Windows SDK, Programmer's Reference, Vol. 4: Resources</li>
 *  <li>"GFF Format Summary Microsoft Windows Metafile.htm"</li>
 *  </ul>
 *  <p>
 *  All (numerical) values are represented in the following attributes:
 *  <table><caption>Numerical Attributes</caption>
 *  <tr><td>w="int"</td><td>word (2 bytes) with a hexadecimal value</td></tr>
 *  <tr><td>rgb="int"</td><td>hexadecimal R-G-B color values (3 bytes)</td></tr>
 *  </table>
 *  The element values are descriptive, but are not used by the SAX handler.
 *  <p>
 *  There are several variations of the WMF header:
 *  <ul>
 *  <li>standard with a 18 byte header</li>
 *  <li>Aldus placeable with a 22 byte header before the standard</li>
 *  <li>Clipboard       with an 8 byte header before the standard</li>
 *  <li>.emf = enhanced WMF with an 80 byte header record, starting with 0x00000001</li>
 *  </ul>
 *  <p>
 *  Frequency of tags:
 *  <pre>
      2 0000
      5 0102
      1 0103
      1 0104
     24 0106
     46 012d
     13 012e
      8 01f0
      5 0201
      5 0209
      1 020b
      1 020c
     43 02fa
      2 02fb
     13 02fc
      7 0324
      4 041b
      5 0538
 *  </pre>
 *  Example:
 *  <pre>
     0: d7 cd c6 9a       e6 f2  d3 f2 61  d 4e  d e8  3  WMF...frSra.N.h.
    10:             e3 54  1      9        3 4d  3        ....cT......M...
    20:  5     6  1               4           3  1  8     ................
    30:  5           b  2               5           c  2  ................
    40:  1     1     4            3  1  8     5           ................
    50:  b  2              5            c  2  1     1     ................
    60:  5           c  2 80 3e  80 3e  5           b  2  .......&gt;.&gt;......
    70:              4            6  1  1     7           ................
    80: fc  2       ff ff ff            4          2d  1  |..........-.
    90:        9          fa  2   5                ff ff  ......z.......
    a0: ff    22     4           2d  1  1     4           .".....-.......
    b0:  6  1  1     e           24  3  5    47    47     ........$...G.G.
    c0: 39 3e 47    39 3e 39 3e  47    39 3e 47    47     9&gt;G.9&gt;9&gt;G.9&gt;G.G.
    d0:  9          fa  2                                 ....z...........
    e0: 22     4          2d  1   2     4          2d  1  ".....-.......-.
    f0:        7          fc  2                           ......|.........

   100:  4          2d  1  3      4          2d  1  1     ....-.......-...
   110:  4           6  1  1     18          24  3  a     ............$...
   120: 80 3e 47    39 3e        47          47    8e     .&gt;G.9&gt;..G...G...
   130: 39 3e 8e    f2 3d 47     80 3e 47    80 3e        9&gt;..r=G..&gt;G..&gt;..
   140: 39 3e       80 3e 47      4          2d  1  2     9&gt;...&gt;G.....-...
   150:  4          2d  1         4          2d  1  3     ....-.......-...
   160:  4          2d  1  1      4           6  1  1     ....-...........
   170: 18          24  3  a     39 3e 80 3e 80 3e 39 3e  ....$...9&gt;.&gt;.&gt;9&gt;
   180: 80 3e 47    f2 3d 47     f2 3d 39 3e 39 3e f2 3d  .&gt;G.r=G.r=9&gt;9&gt;r=
   190: 39 3e 80 3e 80 3e 80 3e  80 3e 39 3e 39 3e 80 3e  9&gt;.&gt;.&gt;.&gt;.&gt;9&gt;9&gt;.&gt;
   1a0:  4          2d  1  2      4          2d  1        ....-.......-...
   1b0:  4          2d  1  3      4          2d  1  1     ....-.......-...
   1c0:  4           6  1  1     18          24  3  a     ............$...
   1d0:       39 3e 47    80 3e  39 3e 80 3e 39 3e f2 3d  ..9&gt;G..&gt;9&gt;.&gt;9&gt;r=
   1e0: 47    f2 3d 8e    39 3e        39 3e       80 3e  G.r=..9&gt;..9&gt;...&gt;
   1f0: 47    80 3e       39 3e   4          2d  1  2     G..&gt;..9&gt;....-...

   200:  4          2d  1         4          2d  1  3     ....-.......-...
   210:  4          2d  1  1      4           6  1  1     ....-...........
   220: 18          24  3  a     47                47     ....$...G.....G.
   230:       39 3e 8e    39 3e  8e    47    47    8e     ..9&gt;..9&gt;..G.G...
   240: 47                             47    47           G.........G.G...
   250:  4          2d  1  2      4          2d  1        ....-.......-...
   260:  7          fc  2           7a 33           4     ....|....z3.....
   270:       2d  1  4     4           2d  1  1     4     ..-.......-.....
   280:        6  1  1     e           24  3  5    c0  1  ..........$...@.
   290: be  1 bd 3c be  1 bd 3c  bb 3c c0  1 bb 3c c0  1  &gt;.=&lt;&gt;.=&lt;;&lt;@.;&lt;@.
   2a0: be  1  4          2d  1   2     4          2d  1  &gt;.....-.......-.
 *  </pre>
 *  @author Dr. Georg Fischer
 */
public class WMFTransformer extends ByteTransformer {
    public final static String CVSID = "@(#) $Id: WMFTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** Root element tag */
    private static final String ROOT_TAG   = "wmf";
    /** Header element tag */
    private static final String HEAD_TAG   = "header";
    /** Record element tag */
    private static final String RECORD_TAG = "record";

    /** No-args Constructor
     */
    public WMFTransformer() {
        super();
        setFormatCodes  ("wmf");
        setDescription  ("Windows Meta File");
        setMimeType     ("image/wmffile");
    } // Constructor

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = LogManager.getLogger(WMFTransformer.class.getName());
    } // initialize

    /*===========================*/
    /* Generator for SAX events  */
    /*===========================*/

    /** Buffer for input bytes */
    private byte [] buffer;
    /** Indicator for end of file */
    private int readLen;
    /** Relative byte offset */
    private int offset;

    /** Reads the input buffer, and then
     *  gets the integer value of up to 4 LSB bytes from the buffer.
     *  @param len number of bytes to be consumed: 1 - 4
     *  @return integer value of the bytes read (least significant first)
     */
    private int getLSB(int len) {
        int value = 0;
        offset += len;
        try {
            readLen = byteReader.read(buffer, 0, len);
            while (len > 0) {
                value = (value << 8) | (buffer[-- len] & 0xff);
            } // while len
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return value;
    } // getLSB

    /** Reads the input buffer, and then
     *  gets the value of a word (2 LSB bytes) from the buffer.
     *  @return integer value of 2 bytes (least significant first)
     */
    private int getWord() {
        return getLSB(2);
    } // getWord

    /** Reads the input buffer, and then
     *  gets the value for an R-G-B color (4 LSB bytes) from the buffer.
     *  @return integer value of the color
     */
    private int getColor() {
        return getLSB(4);
    } // getColor

    /** Reads the input buffer, and then
     *  gets the value for a double word (4 LSB bytes) from the buffer.
     *  @return integer value of the double word
     */
    private int getDouble() {
        return getLSB(4);
    } // getDouble

    /** Creates an empty element from the next 2 input bytes.
     *  @param tag element's tag
     *  @return integer value of the 2 LSB bytes
     */
    private int fireInt(String tag) {
        int value = getLSB(2);
        fireEmptyElement(tag, toAttribute("w" , Integer.toString(value)));
        return value;
    } // fireInt

    /** Creates an empty element from the next 2 input bytes.
     *  @param tag element's tag
     *  @return integer value of the 2 LSB bytes
     */
    private int fireWord(String tag) {
        int value = getLSB(2);
        fireEmptyElement(tag, toAttributes(new String[]
            { "ofs", Integer.toHexString(offset)
            , "wx" , Integer.toHexString(value)
            }));
        return value;
    } // fireWord

    /** Creates an empty element from the next 4 input bytes.
     *  @return integer value of the R-G-B color
     */
    private int fireColor() {
        int value = getLSB(4);
        fireEmptyElement("color", toAttribute("dx", Integer.toHexString(value)));
        return value;
    } // fireColor

    /** Creates an empty element from the next 2 input bytes.
     *  @param tag element's tag
     *  @return integer value of the 2 LSB bytes
     */
    private int fireDouble(String tag) {
        int value = getLSB(4);
        fireEmptyElement(tag, toAttribute("dx", Integer.toHexString(value)));
        return value;
    } // fireDouble

    /** magic number at the start of a WMF file */
    private static final int MAGIC_WMF = 0x9ac6cdd7;
    /** additional length of an Aldus placebale WMF */
    private static final int PLACEABLE_LEN   = 22;
    /** standard header length */
    private static final int STD_HEADER_LEN  = 18;
    /** length of an EMF header */
    private static final int EMF_HEADER_LEN  = 80;

    /** Evaluates the file header and writes a
     *  <em>header</em> element.
     */
    private void evalHeader() {
        int start = offset;
        pushXML(HEAD_TAG);
        int headerLen = STD_HEADER_LEN;
        int magic = getDouble();
        if (magic == MAGIC_WMF) {
            headerLen += PLACEABLE_LEN;
        }
        fireLineBreak();
        fireEmptyElement("magic", toAttribute("dx", Integer.toHexString(magic)));
        fireLineBreak();
        fireWord("dummy"); // 0
        fireLineBreak();
        fireWord("x1");
        fireWord("y1");
        fireLineBreak();
        fireWord("x2");
        fireWord("y2");
        fireLineBreak();
        fireWord("unit");
        fireLineBreak();
        while (readLen >= 0 && offset - start < headerLen) {
            fireWord("word");
            fireLineBreak();
        } // while in header
        popXML(); // header
        fireLineBreak();
    } // evalHeader

    /** Evaluates a graphics function record and writes the corresponding elements.
     *  @param recordLen length of the record in 2-byte words, including the length and function code
     *  @param funct code for the specific graphical function
     */
    private void evalRecord(int recordLen, int funct) {
        int start = offset;
        fireStartElement(RECORD_TAG, toAttribute("dx", Integer.toHexString(recordLen)));
        fireLineBreak();
        Attributes attrs = toAttribute("wx", Integer.toHexString(funct));
        switch (funct) {
            case 0x0102:
                pushXML("SetBkMode", attrs);
                break;
            case 0x0103:
                pushXML("SetMapMode", attrs);
                break;
            case 0x0104:
                pushXML("SetROP2", attrs);
                break;
            case 0x0106:
                pushXML("SetPolyFillMode", attrs);
                break;
            case 0x012d:
                pushXML("SelectObject", attrs);
                break;
            case 0x012e:
                pushXML("SetTextAlign", attrs);
                break;
            case 0x01f0:
                pushXML("DeleteObject", attrs);
                break;
            case 0x0201:
                pushXML("SetBkColor", attrs);
                fireColor();
                break;
            case 0x0209:
                pushXML("SetTextColor", attrs);
                fireColor();
                break;
            case 0x020b:
                pushXML("SetWindowOrg", attrs);
                fireInt("y");
                fireInt("x");
                break;
            case 0x020c:
                pushXML("SetWindowExt", attrs);
                fireInt("y");
                fireInt("x");
                break;
            case 0x02fa:
                pushXML("CreatePenIndirect", attrs);
                fireWord("style");
                fireInt ("thick");
                fireColor();
                break;
            case 0x02fb:
                pushXML("CreateFontIndirect", attrs);
                break;
            case 0x02fc:
                pushXML("CreateBrushIndirect", attrs);
                fireWord("style");
                fireColor();
                fireWord("hatch");
                break;
            case 0x0324:
                pushXML("Polygon", attrs);
                int npoint = fireInt("npoint");
                fireLineBreak();
                while (npoint > 0) {
                    fireInt("x");
                    fireInt("y");
                    fireLineBreak();
                    npoint --;
                } // while npoint
                break;
            case 0x0325:
                pushXML("Polyline", attrs);
                npoint = fireInt("npoint");
                fireLineBreak();
                while (npoint > 0) {
                    fireInt("x");
                    fireInt("y");
                    fireLineBreak();
                    npoint --;
                } // while npoint
                break;
            case 0x041b:
                pushXML("Rectangle", attrs);
                fireInt("x1");
                fireInt("y1");
                fireInt("x2");
                fireInt("y2");
                fireLineBreak();
                break;
            case 0x0538:
                pushXML("PolyPolygon", attrs);
                int npoly  = fireInt("npoly");
                while (npoly > 0) {
                    npoint = fireInt("npoint");
                    fireLineBreak();
                    while (npoint > 0) {
                        fireInt("x");
                        fireInt("y");
                        fireLineBreak();
                        npoint --;
                    } // while npoint
                    npoly --;
                } // while npoly
                break;
            default:
                pushXML("unknownFunction", attrs);
                break;
        } // switch function
        recordLen *= 2;
        recordLen -= 6; // recordLen and funct are already read off
        while (readLen >= 0 && offset - start < recordLen) {
            fireWord("word");
            fireLineBreak();
        } // while in header
        popXML();
        fireLineBreak();
        fireEndElement(RECORD_TAG);
        fireLineBreak();
    } // evalRecord

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        buffer = new byte[8];
        readLen = 0;
        offset = 0;
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            evalHeader();
            while (readLen > 0) {
                int recordLen = getDouble(); // in 2-byte words
                int function  = getLSB(2);
                if (readLen > 0) {
                    evalRecord(recordLen, function);
                }
            } // while reading input file
            fireEndElement(ROOT_TAG);
            fireEndDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return  result;
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** Receive notification of the start of an element.
     *  @param uri The Namespace URI, or the empty string if the element has no Namespace URI
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix),
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix),
     *  or the empty string if qualified names are not available.
     *  @param attrs the attributes attached to the element.
     *  If there are no attributes, it shall be an empty Attributes object.
     *  @throws SAXException for SAX errors
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs)
            throws SAXException {
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        try {
            int iattr = attrs.getLength();
            while (iattr > 0) {
                iattr --;
                String name  = attrs.getLocalName(iattr);
                String value = attrs.getValue    (iattr);
                if (false) {
                } else if (name.equals("wx"))  {
                    putLSB(value, 2, true);
                } else if (name.equals("cx"))  {
                    putLSB(value, 4, true);
                } else if (name.equals("dx"))  {
                    putLSB(value, 4, true);
                } else if (name.equals("w" ))  {
                    putLSB(value, 2, false);
                } else if (name.equals("ofs")) {
                    // ignore
                } else {
                    log.error("unknown attribute " + name);
                }
            } // while iattr
        } catch (Exception exc) {
            throw new SAXException(exc.getMessage());
        }
    } // startElement

} // WMFTransformer
