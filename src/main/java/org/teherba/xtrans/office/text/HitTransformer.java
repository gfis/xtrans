/*  (Siemens resp. InterFace) Hit text processing system
    @(#) $Id: HitTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2008-03-25, Dr. Georg Fischer
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

package org.teherba.xtrans.office.text;
import  org.teherba.xtrans.ByteTransformer;
import  org.teherba.xtrans.ISO6937Map;
import  org.xml.sax.Attributes;
import  org.xml.sax.SAXException;
import  org.apache.log4j.Logger;

/** Transformer for the (Siemens/Sinix and InterFace) Hit text processing system.
 *  Hit ran on various Unix systems, especially Siemens MX series.
 *  The character set is ISO 6937 resp. CCITT T.61.
 *  The file has a header and lines terminated by EOS = '\x00',
 *  which may be of type text (0x0d), ruler (0x80) or markup (0x10).
 *  C.f. Hit Programmer's Manual V4.0, pp. 509 - 521
 *  (Interface Computer GmbH, April 1990).
 *  <p>
 *  Ruler lines consist of pairs of bytes, which are represented
 *  by a <em>wx</em> XML element and a hexadecimal attribute.
 *  In the header, non-printable bytes are likewise represented
 *  by a <em>bx</em> XML element.
 *  <p>
 *  Text lines start with a count of leading spaces, followed by a
 *  sequence of ISO 6937 characters which are interspersed by
 *  control codes for formatting. These are represented by their HTML
 *  equivalents. The first control code switches the feature on,
 *  while the second switches it off again.
 *  <p>
 *  Example dump of a Hit file:
<pre>
     0: ff 68 49 74 99 bf 34 30  30 31             ff fa  ,hIt,?4001....,z
    10:             80  8  8 30   a 30  f 30 14 30 19 30  ....,..0.0.0.0.0
    20: 1e 30 23 30 28 31 37 31  3c 31 41 31 46 31 4b     .0#0(171&lt;1A1F1K.
    30: 80  8  8 32  8 30  a 32   f 32 14 32 19 32 1e 32  ,..2.0.2.2.2.2.2
    40: 23 33 37 33 3c 33 41 33  46 31 4b     d  b 44 69  #373&lt;3A3F1K...Di
    50: 65 73 20 69 73 74 20 65  69 6e 20 44 6f 6b 75 6d  es ist ein Dokum
    60: 65 6e 74 20 6d 69 74 20  64 65 6d 20 4e 61 6d 65  ent mit dem Name
    70: 6e 20 22 6d 6f 64 75 6c  61 74 69 6f 6e 22 2e     n "modulation"..
    80:  d  1     d  b 44 69 65  73 20 69 73 74 20 65 69  .....Dies ist ei
    90: 6e 65 20 41 62 73 61 74  7a 6d 61 72 6b 65 3a 20  ne Absatzmarke:
    a0: 1f    80  8  8 32  8 32   b 30  f 32 14 32 19 32  ..,..2.2.0.2.2.2
    b0: 1e 32 23 33 37 33 3c 33  41 33 46 31 4b     d  1  .2#373&lt;3A3F1K...
    c0:     d  b 31 2e 20 20 20  45 69 6e 20 65 69 6e 67  ...1.   Ein eing
    d0: 65 72 c8 75 63 6b 74 65  73 20 5a 65 69 6c 65 6e  erHucktes Zeilen
    e0: 6c 69 6e 65 61 6c 1f      d 10 1f    80  8  8 32  lineal......,..2
    f0:  8 30  a 32 14 32 19 32  1e 32 23 33 37 33 3c 33  .0.2.2.2.2#373&lt;3

   100: 41 33 46 31 4b     d  1     10  1 3c 4f 3a 31 2e  A3F1K......&lt;O:1.
   110: 35 3e     d  b 45 69 6e  65 20 53 74 65 75 65 72  5&gt;...Eine Steuer
   120: 7a 65 69 6c 65 20 22 5a  65 69 6c 65 6e 61 62 73  zeile "Zeilenabs
   130: 74 61 6e 64 22 20 22 31  2c 35 22     d  1    10  tand" "1,5".....
   140:  1 3c 4f 3a 31 2e 30 3e      d  1     d  b 55 6e  .&lt;O:1.0&gt;......Un
   150: 64 20 53 6f 6e 64 65 72  7a 65 69 63 68 65 6e 3a  d Sonderzeichen:
   160: 1f     d  b 1f     d  b  a7 a0 31 39 33 32 a0 41  ........' 1932 A
   170: 62 73 2e 49 49 a0 42 47  42 1f     d  b 1f     d  bs.II BGB.......
   180:  b  2 46 65 74 74 73 63  68 72 69 66 74 20  2  3  ..Fettschrift ..
   190: 20 75 6e 74 65 72 73 74  72 69 63 68 65 6e 20 67   unterstrichen g
   1a0: 65 73 63 68 72 69 65 62  65 6e                    eschrieben......
   1b0:                                                   ................
</pre>
 *  @author Dr. Georg Fischer
 */
public class HitTransformer extends ByteTransformer {
    public final static String CVSID = "@(#) $Id: HitTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** Upper bound for input buffer */
    private static final int MAX_BUF = 4096;

    /** Root element tag */
    private static final String ROOT_TAG        = "hit";
    /** Byte element tag */
    private static final String BYTE_TAG        = "bx";
    /** Header element tag */
    private static final String HEAD_TAG        = "header";
    /** Markup element tag */
    private static final String MARKUP_TAG      = "markup";
    /** Ruler element tag */
    private static final String RULER_TAG       = "ruler";
    /** Proportional text line element tag */
    private static final String PROP_TEXT_TAG   = "propText";
    /** Text line element tag */
    private static final String TEXT_TAG        = "text";
    /** Word element tag */
    private static final String WORD_TAG        = "wx";
    /** Attribute name for leading spaces */
    private static final String SPACE_ATTR      = "sp";

    /** byte which indicates the end of string */
    private static final char EOS = 0;

    /** Element which denotes a line break, for readability/reconstruction only */
    private static final String NEWLINE_TAG     = "n";
    /** Element tag for hard hyphen */
    private static final String HARD_HYPHEN_TAG = "hhy";
    /** Element tag for soft hyphen */
    private static final String SOFT_HYPHEN_TAG = "shy";

    /** Table of ISO 6937 character mappings */
    private ISO6937Map isoMap;

    /** number of logical line (terminated by EOS) */
    private int lineNo;

    /** initial accent for 2-byte accented ISO 6937 character */
    private char accent;

    /** tag for a text line */
    private String lineTag;

    /** buffer for values in input stream */
    private StringBuffer content;

    /* 2-byte pair from ruler line */
    private int rulerPair;

    /** Buffer for a portion of the input file */
    private byte[] byteBuffer;

    /** state of finite automaton */
    private  int  state;

    /** values of <em>state</em> */
    private static final int IN_0F          =  1;
    private static final int IN_7F          =  2;
    private static final int IN_7F_LL       =  3;
    private static final int IN_HARD_HYPHEN =  4;
    private static final int IN_HEADER      =  5;
    private static final int IN_HYPHEN      =  6;
    private static final int IN_ISO         =  7;
    private static final int IN_MARKUP      =  8;
    private static final int IN_RULER       =  9;
    private static final int IN_RULER_LOW   = 10;
    private static final int IN_RULER_HIGH  = 11;
    private static final int IN_SHARP       = 12;
    private static final int IN_SOFT_HYPHEN = 13;
    private static final int IN_START       = 14;
    private static final int IN_TEXT        = 15;
    private static final int IN_TEXT_LINE   = 16;

    /** No-args Constructor.
     */
    public HitTransformer() {
        super();
        setFormatCodes("hit");
        setDescription("Siemens Hit");
        setFileExtensions("hit");
    } // Constructor

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = Logger.getLogger(HitTransformer.class.getName());
    } // initialize

    /** Emits document text, and writes its characters
     */
    private void fireContent() {
        if (content.length() > 0) {
            fireCharacters(content.toString());
            content.setLength(0);
        }
    } // fireContent

    /** Emits an arbitrary byte as hexadecimal code
     *  @param ch byte to be output
     */
    private void fireByte(char ch) {
        fireContent();
        fireEmptyElement(BYTE_TAG, toAttribute(BYTE_TAG, Integer.toHexString(ch)));
    } // fireByte

    /** Emits an arbitrary word (2 LSB bytes) as hexadecimal code
     *  @param word word to be output
     */
    private void fireWord(int word) {
        fireContent();
        fireEmptyElement(WORD_TAG, toAttribute(WORD_TAG, Integer.toHexString(word)));
    } // fireByte

    /** Processes a portion of the input file (one line)
     *  @param start offset where to start/resume scanning
     *  @param trap  offset behind last character to be processed
     *  @return offset behind last character which was processed
     */
    private int processInput(int start, int trap) {
        char ch; // current character to be processed
        boolean readOff; // whether current character should be consumed
        int ibuf = start;
        while (ibuf < trap) { // process all characters
            readOff = true;
            ch = (char) (byteBuffer[ibuf] & 0xff);
            switch (state) {

                default:
                    log.error("invalid state " + state);
                    break;

                case IN_HEADER: // accumulate 3 header lines
                    if (ch == EOS) {
                        fireByte(ch);
                        if (ibuf >= 15) {
                            lineNo ++;
                            if (lineNo > 3) {
                                state = IN_START;
                                popXML(); // HEAD_TAG
                            }
                            fireLineBreak();
                        }
                    } else if (ch >= 0x20 && ch <= 0x7e) { // normal printable ASCII character
                        content.append(ch);
                    } else {
                        fireByte(ch);
                    }
                    break; // IN_HEADER

                case IN_MARKUP:
                    if (ch == EOS) {
                        fireByte(ch);
                        state = IN_START;
                        popXML(); // markup
                        fireLineBreak();
                    } else if (ch >= 0x20 && ch <= 0x7e) { // normal printable ASCII character
                        content.append(ch);
                    } else {
                        fireByte(ch);
                    }
                    break; // IN_MARKUP

                case IN_START: // start of text line
                    content.setLength(0);
                    switch (ch) {
                        default:
                        case 0x0d: // normal text line
                            lineTag = TEXT_TAG;
                            state = IN_TEXT_LINE;
                            break;
                        case 0x12: // proportional text line
                            lineTag = PROP_TEXT_TAG;
                            state = IN_TEXT_LINE;
                            break;
                        case 0x10: // markup line
                            pushXML("markup");
                            state = IN_MARKUP;
                            break;
                        case 0x80: // ruler line
                            pushXML(RULER_TAG);
                            state = IN_RULER_LOW;
                            break;
                    } // switch ch
                    break; // IN_START

                case IN_RULER_LOW:
                    if (ch == EOS) {
                        // fireByte(ch); // implied by </ruler>
                        state = IN_START;
                        popXML(); // "ruler"
                        fireLineBreak();
                    } else {
                        rulerPair = ch;
                        state = IN_RULER_HIGH;
                    }
                    break; // IN_RULER_LOW

                case IN_RULER_HIGH:
                    rulerPair = rulerPair | (ch << 8);
                    fireWord(rulerPair);
                    state = IN_RULER_LOW;
                    break; // IN_RULER_HIGH

                case IN_TEXT_LINE: // ch == number of leading spaces
                    pushXML(lineTag, toAttribute(SPACE_ATTR, Integer.toString(ch)));
                    state = IN_TEXT;
                    break; // IN_TEXT_LINE

                case IN_TEXT:
                    switch (ch) {
                        case EOS: // EOS
                            lineNo ++;
                            fireContent();
                            popXML(); // text, propText
                            fireLineBreak();
                            state = IN_START;
                            break;
                        case 0x02: // bold
                            fireContent();
                            fireEmptyElement("b");
                            break;
                        case 0x03: // underline
                            fireContent();
                            fireEmptyElement("u");
                            break;
                        case 0x04: // higher
                            fireContent();
                            fireEmptyElement("sup");
                            break;
                        case 0x05: // lower
                            fireContent();
                            fireEmptyElement("sub");
                            break;
                        case 0x06: // italic
                            fireContent();
                            fireEmptyElement("i");
                            break;
                        case 0x07: // syllable (optional) hyphen
                            // state = IN_SOFT_HYPHEN;
                            // should be behind next char, but this is difficult to remove during SAX parsing
                            fireContent();
                            fireEmptyElement("shy");
                            break;
                        case 0x08: // backspace
                            // ignore
                            break;
                        case 0x09: // tab
                            fireContent();
                            fireEmptyElement("tab");
                            break;
                        case 0x13: // strike through
                            fireContent();
                            fireEmptyElement("strike");
                            break;
                        case 0x1f: // next line end is end of paragraph
                            fireContent();
                            fireEmptyElement("para");
                            break;
                        case 0xa0: // hard space
                            fireContent();
                            fireEmptyElement("nbsp");
                            break;
                        // case 0xc0: // hyphen at EOL
                        default:
                            if (false) {
                            } else if (ch >= 0x20 && ch <= 0x7e) { // normal printable ASCII character
                                content.append(ch);
                            } else if (ch >= 0x80 && ch <= 0x8a) { // box character
                            } else if (ch >= 0xc1 && ch <= 0xcf) { // accent for following character
                                accent = ch;
                                state = IN_ISO;
                            } else if (ch >= 0xa0 && ch <= 0xff && ch != 0xc0) { // accent for following character
                                content.append(isoMap.getUnicode(ch));
                            } else {
                                fireByte(ch);
                            }
                            break;
                    } // switch ch
                    break; // IN_TEXT

                case IN_SOFT_HYPHEN:
                    content.append(ch);
                    fireContent();
                    fireEmptyElement("shy");
                    state = IN_TEXT;
                    break; // IN_SOFT_HYPHEN

                case IN_HYPHEN:
                    // assume ch == EOS
                    content.append('-');
                    state = IN_TEXT;
                    break; // IN_HYPHEN

                case IN_ISO:
                    content.append(isoMap.getAccentedChar(accent, ch));
                    state = IN_TEXT;
                    break; // IN_ISO

            } // switch state
            if (readOff) {
                ibuf ++;
            }
        } // while processing
        return ibuf; // new 'start'
    } // processInput

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        int len; // length read from 'charReader'
        isoMap = new ISO6937Map();
        byteBuffer = new byte[MAX_BUF];
        lineNo = 0;
        content = new StringBuffer(MAX_BUF);

        putEntityReplacements();
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            state = IN_HEADER;
            pushXML(HEAD_TAG);
            fireLineBreak();
            while ((len = byteReader.read(byteBuffer, 0, MAX_BUF)) >= 0) {
                len = processInput(0, len);
            } // while reading
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
    private byte[] saxBuffer;
    /** current position in <em>saxBuffer</em> */
    private int saxPos;

    /** Table of ISO 6937 character mappings */
    private ISO6937Map saxIsoMap;

    /** currently opened element */
    private String elem;

    /** Terminate and write a logical line
     */
    public void flushLine() {
        try {
            byteWriter.write(saxBuffer, 0, saxPos);
            saxPos = 0;
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // flushLine

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        saxIsoMap = new ISO6937Map();
        saxBuffer = new byte[MAX_BUF]; // a rather long line
        saxPos = 0;
        elem = "";
    } // startDocument

    /** Receive notification of the start of an element.
     *  Looks for the element which contains raw lines.
     *  @param uri The Namespace URI, or the empty string if the element has no Namespace URI
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix),
     *  or the empty string if namespace processing is not being performed.
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
        elem = qName;
        try {
            if (false) {
            } else if (qName.equals(ROOT_TAG        )) {
                // ignore
            } else if (qName.equals(BYTE_TAG        )) {
                flushLine();
                putLSB(attrs.getValue(BYTE_TAG  ), 1, true);
            } else if (qName.equals(HEAD_TAG        )) {
            } else if (qName.equals(MARKUP_TAG      )) {
                byteWriter.write(0x10);
            } else if (qName.equals(PROP_TEXT_TAG   )) {
                byteWriter.write(0x12);
                putLSB(attrs.getValue(SPACE_ATTR), 1, false);
            } else if (qName.equals(RULER_TAG       )) {
                byteWriter.write(0x80);
            } else if (qName.equals(TEXT_TAG        )) {
                byteWriter.write(0x0d);
                putLSB(attrs.getValue(SPACE_ATTR), 1, false);
            } else if (qName.equals(WORD_TAG        )) {
                flushLine();
                putLSB(attrs.getValue(WORD_TAG  ), 2, true);
            } else if (qName.equals("b"         )) {
                saxBuffer[saxPos ++] = 0x02;
            } else if (qName.equals("u"         )) {
                saxBuffer[saxPos ++] = 0x03;
            } else if (qName.equals("sup"       )) {
                saxBuffer[saxPos ++] = 0x04;
            } else if (qName.equals("sub"       )) {
                saxBuffer[saxPos ++] = 0x05;
            } else if (qName.equals("i"         )) {
                saxBuffer[saxPos ++] = 0x06;
            } else if (qName.equals("shy"       )) {
                if (false) { // problems with 2-byte ISO 6937 accented characters
                saxBuffer[saxPos] = saxBuffer[saxPos - 1];
                saxBuffer[saxPos - 1] = 0x07; // insert before last character
                saxPos ++;
                } else {
                saxBuffer[saxPos ++] = 0x07;
                }
            } else if (qName.equals("tab"           )) {
                saxBuffer[saxPos ++] = 0x08;
            } else if (qName.equals("strike"        )) {
                saxBuffer[saxPos ++] = 0x13;
             } else if (qName.equals("para"         )) {
                saxBuffer[saxPos ++] = 0x1f;
            } else if (qName.equals("nbsp"          )) {
                saxBuffer[saxPos ++] = (byte) 0xa0;
            } else {
            }
        } catch (Exception exc) {
            throw new SAXException(exc.getMessage());
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
     *  @throws SAXException for SAX errors
     */
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        elem = "";
        try {
            if (false) {
            } else if (qName.equals(ROOT_TAG        )) {
                flushLine();
            } else if (qName.equals(HEAD_TAG        )) {
                flushLine();
            } else if (qName.equals(MARKUP_TAG      )) {
                flushLine();
                byteWriter.write(EOS);
            } else if (qName.equals(PROP_TEXT_TAG   )) {
                flushLine();
                byteWriter.write(EOS);
            } else if (qName.equals(RULER_TAG       )) {
                flushLine();
                byteWriter.write(EOS);
            } else if (qName.equals(TEXT_TAG        )) {
                flushLine();
                byteWriter.write(EOS);
            } else {
                // all other elements are empty - ignore their end tags
            }
        } catch (Exception exc) {
            throw new SAXException(exc.getMessage());
        }
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array.
     *  @throws SAXException for SAX errors
     */
    public void characters(char[] ch, int start, int len)
            throws SAXException {
        try {
            if (true) { // inside HEAD_TAG, TEXT_TAG and PROP_TEXT_TAG
                int pos = 0;
                while (pos < len) {
                    char chx = ch[start ++];
                    if (chx == '\n' || chx == '\r') {
                        // ignore
                    } else if (chx >= 0x20 && chx <= 0x7e) { // normal printable ASCII character
                        saxBuffer[saxPos ++] = (byte) chx;
                    } else if (chx >= 0x80) { // accented or from table
                        int value = saxIsoMap.getIsocode(chx);
                        if (value == 0) {
                            saxBuffer[saxPos ++] = (byte) '?';
                        } else if (value >= 0x100) {
                            saxBuffer[saxPos ++] = (byte) (value >> 8);
                            saxBuffer[saxPos ++] = (byte) (value & 0xff);
                        } else {
                            saxBuffer[saxPos ++] = (byte) (value & 0xff);
                        }
                    } else {
                        saxBuffer[saxPos ++] = (byte) chx;
                    }
                    pos ++;
                } // while pos
            } // else ignore characters in unknown elements
        } catch (Exception exc) {
            throw new SAXException(exc.getMessage());
        }
    } // characters

} // HitTransformer
