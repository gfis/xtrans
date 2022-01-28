/*  Transforms any (binary) file to the
    an XML representation of the Quoted-Printable format
    of RFC 2045, Section 6.7, and vice versa
    @(#) $Id: QuotedPrintableTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2006-11-20, Dr. Georg Fischer <punctum@punctum.com>
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
import  org.teherba.xtrans.ByteRecord;
import  org.teherba.xtrans.ByteTransformer;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transformer which converts any (binary) file to an XML representation
 *  of the "Quoted Printable" format, and back.
 *  Parallel to Base64, the Quoted Printable format is described in
 *  <a href="http://tools.ietf.org/html/rfc2045#section-6.7">RFC 2045, section 6.7</a>.
 *  The transformer generates an XML &lt;table&gt; with &lt;tr&gt; rows that
 *  can be thought of being separated by hard linebreaks (CR/LF).
 *  Each row contains one or more &lt;td&gt; cells, where all but the last
 *  cell in a row have a soft linebreak ("=" + CR/LF) at the end.
 *  All cell contents are no longer than 76 characters.
 *  The normal Quoted Printable format can be obtained from the XML
 *  by a subsequent serialization with <em>LineTransformer</em>.
 *  The encoding parameter is irrelevant - the tranformation
 *  always converts between binary and US-ASCII.
 *  <p>
 *  Verbatim from RFC 2045:
 *  <pre>
   In this encoding, octets are to be represented as determined by the
   following rules:

    (1)   (General 8bit representation) Any octet, except a CR or
          LF that is part of a CRLF line break of the canonical
          (standard) form of the data being encoded, may be
          represented by an "=" followed by a two digit
          hexadecimal representation of the octet's value.  The
          digits of the hexadecimal alphabet, for this purpose,
          are "0123456789ABCDEF".  Uppercase letters must be
          used; lowercase letters are not allowed.  Thus, for
          example, the decimal value 12 (US-ASCII form feed) can
          be represented by "=0C", and the decimal value 61 (US-
          ASCII EQUAL SIGN) can be represented by "=3D".  This
          rule must be followed except when the following rules
          allow an alternative encoding.

    (2)   (Literal representation) Octets with decimal values of
          33 through 60 inclusive, and 62 through 126, inclusive,
          MAY be represented as the US-ASCII characters which
          correspond to those octets (EXCLAMATION POINT through
          LESS THAN, and GREATER THAN through TILDE,
          respectively).

    (3)   (White Space) Octets with values of 9 and 32 MAY be
          represented as US-ASCII TAB (HT) and SPACE characters,
          respectively, but MUST NOT be so represented at the end
          of an encoded line.  Any TAB (HT) or SPACE characters
          on an encoded line MUST thus be followed on that line
          by a printable character.  In particular, an "=" at the
          end of an encoded line, indicating a soft line break
          (see rule #5) may follow one or more TAB (HT) or SPACE
          characters.  It follows that an octet with decimal
          value 9 or 32 appearing at the end of an encoded line
          must be represented according to Rule #1.  This rule is
          necessary because some MTAs (Message Transport Agents,
          programs which transport messages from one user to
          another, or perform a portion of such transfers) are
          known to pad lines of text with SPACEs, and others are
          known to remove "white space" characters from the end
          of a line.  Therefore, when decoding a Quoted-Printable
          body, any trailing white space on a line must be
          deleted, as it will necessarily have been added by
          intermediate transport agents.

    (4)   (Line Breaks) A line break in a text body, represented
          as a CRLF sequence in the text canonical form, must be
          represented by a (RFC 822) line break, which is also a
          CRLF sequence, in the Quoted-Printable encoding.  Since
          the canonical representation of media types other than
          text do not generally include the representation of
          line breaks as CRLF sequences, no hard line breaks
          (i.e. line breaks that are intended to be meaningful
          and to be displayed to the user) can occur in the
          quoted-printable encoding of such types.  Sequences
          like "=0D", "=0A", "=0A=0D" and "=0D=0A" will routinely
          appear in non-text data represented in quoted-
          printable, of course.

          Note that many implementations may elect to encode the
          local representation of various content types directly
          rather than converting to canonical form first,
          encoding, and then converting back to local
          representation.  In particular, this may apply to plain
          text material on systems that use newline conventions
          other than a CRLF terminator sequence.  Such an
          implementation optimization is permissible, but only
          when the combined canonicalization-encoding step is
          equivalent to performing the three steps separately.

    (5)   (Soft Line Breaks) The Quoted-Printable encoding
          REQUIRES that encoded lines be no more than 76
          characters long.  If longer lines are to be encoded
          with the Quoted-Printable encoding, "soft" line breaks
          must be used.  An equal sign as the last character on a
          encoded line indicates such a non-significant ("soft")
          line break in the encoded text.
 *  </pre>
 *  @author Dr. Georg Fischer
 */
public class QuotedPrintableTransformer extends ByteTransformer {
    public final static String CVSID = "@(#) $Id: QuotedPrintableTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** Data cell element tag */
    private static final String DATA_TAG    = "td";
    /** Root element tag */
    private static final String ROOT_TAG    = "quoted-printable";
    /** Table element tag */
    private static final String TABLE_TAG   = "table";
    /** Row element tag */
    private static final String ROW_TAG     = "tr";

    /** No-args Constructor.
     */
    public QuotedPrintableTransformer() {
        super();
        setFormatCodes("qp,qprint,quoted-printable");
        setDescription("Quoted Printable format (RFC 2045, 6.7)");
        setFileExtensions("txt,qp");
    } // Constructor()

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = LogManager.getLogger(QuotedPrintableTransformer.class.getName());
        // setSourceEncoding("US-ASCII");
        // setResultEncoding("US-ASCII");
    } // initialize

    /** number of data octets to be read from input */
    private int width;

    /** Buffer where encoded lines are built */
    private StringBuffer lineBuffer;

    /** length of byte buffer */
    private static final int MAX_BUF = 4096;

    /** record for the specific format */
    protected ByteRecord sourceRecord;
    /** Buffer for bytes of encoded lines */
    private byte[] sourceBuffer;
    /** current position in <em>sourceBuffer</em> */
    private int sourcePos;

    /** Whether another row was started (1) or not (0) */
    private int parseState;

    /** Writes the line buffer into one XML data cell
     */
    private void putLine() {
        if (parseState == 0) {
            parseState = 1;
            fireStartElement(ROW_TAG); // start of next row
            fireLineBreak();
        }
        fireStartElement(DATA_TAG); // start of next cell
        try {
            fireCharacters (lineBuffer.toString());
        } catch(Exception exc) {
            log.error("unsupported encoding " + getSourceEncoding());
        }
        lineBuffer.setLength(0);
        wasSpace = false;
        fireEndElement  (DATA_TAG);
        fireLineBreak();
    } // putLine

    /** Tests whether an octet's representation with some length (1 or 3)
     *  still fits on the line, possibly, writes
     *  data element (partial line) with a trailing soft linebreak if not,
     *  and finally appends (the representation of) the octet
     *  @param octet octet to be encoded
     *  @param len number of characters to be appended to the line (1 or 3)
     */
    private void testAppend(byte octet, int len) {
        int linePos = lineBuffer.length();
        if (len == 2) { // hard linebreak
            putLine();
        } else {
            if (linePos + len >= width) {
                lineBuffer.append('=');
                putLine();
            } else if (wasSpace) {
                if (false) {
                } else if (lineBuffer.substring(linePos - 3).equals("=20")) {
                    lineBuffer.setLength(linePos - 3);
                    lineBuffer.append(' ');
                } else if (lineBuffer.substring(linePos - 3).equals("=09")) {
                    lineBuffer.setLength(linePos - 3);
                    lineBuffer.append('\t');
                }
                wasSpace = false;
            }
            if (len == 1) { // octet represented by itself
                lineBuffer.append((char) octet);
            } else { // len == 3, encoded representation of the octet
                lineBuffer.append('=');
                String hex = Integer.toHexString(octet & 0xff).toUpperCase();
                if (hex.length() <= 1) {
                    lineBuffer.append('0');
                }
                lineBuffer.append(hex);
                wasSpace = octet == 0x20 || octet == 0x09;
            }
        }
    } // testAppend

    /** enumeration of values for <em>state</em> */
    private static final int IN_TEXT    = 0;
    private static final int IN_SP      = 1;
    private static final int IN_CR      = 2;

    /** Whether the previous character was a space or tab */
    private boolean wasSpace;

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
            width = getIntOption("width", 76); // maximum length of encoded lines
            lineBuffer = new StringBuffer(128); // >= width
            sourceRecord = new ByteRecord(MAX_BUF); // some reasonable input buffer length
            sourceBuffer = sourceRecord.getBuffer();
            lineBuffer.setLength(0);
            wasSpace = false;
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireStartElement(TABLE_TAG, toAttribute("width", String.valueOf(width)));
            fireLineBreak();
            parseState = 0;
            int count = 0;
            int len;
            int state = IN_TEXT;
            while ((len = sourceRecord.read(byteReader)) >= 0) {
                sourcePos = 0;
                while (sourcePos < len) {
                    byte octet = sourceBuffer[sourcePos ++];
                    switch (state) {
                        case IN_TEXT:
                            switch (octet) {
                                case 0x0a: // no CR before - encode always
                                    testAppend(octet, 3);
                                    break;
                                case 0x0d:
                                    state = IN_CR;
                                    break;
                                case 0x09: // tab
                                case (byte) ' ': // ' '
                                    testAppend(octet, 3);
                                    break;
                                case (byte) '=':
                                case (byte) '<':
                                case (byte) '>':
                                case (byte) '&':
                                case (byte) '"':
                                case (byte) '\'': // &apos;
                                    testAppend(octet, 3); // must escape
                                    break;
                                default:
                                    if        (octet < 0x20) {
                                        testAppend(octet, 3);
                                    } else if (octet > 0x7f) {
                                        testAppend(octet, 3);
                                    } else {
                                        testAppend(octet, 1); // all "must-escape" octets were catched by 'switch'
                                    }
                                    break;
                            } // switch octet
                            break;
                        case IN_CR:
                            if (octet == 0x0a) { // CR + LF = hard line break
                                testAppend(octet, 2);
                                fireEndElement(ROW_TAG);
                                fireStartElement(ROW_TAG);
                                parseState = 1;
                            } else {
                                testAppend((byte) 0x0d, 3);
                                sourcePos --; // reread the one behind CR
                            }
                            state = IN_TEXT;
                            break;
                        default:
                            log.error("invalid state " + state);
                            break;
                    } // switch state
                } // while sourcePos
            } // while not EOF
            if (lineBuffer.length() > 0) {
                putLine();
            }
            if (parseState == 1) {
                fireEndElement  (ROW_TAG);
                fireLineBreak();
            }
            fireEndElement(TABLE_TAG);
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

    /** Whether another row was started (1) or not (0) */
    private int saxState;

    /** record for the specific format */
    protected ByteRecord resultRecord;
    /** Buffer for bytes of encoded lines */
    private byte[] resultBuffer;
    /** current position in <em>resultBuffer</em> */
    private int resultPos;

    /** assemble successive calls of 'characters' */
    private StringBuffer saxLineBuffer;
    /** current line number in SAX input */
    private int saxLineCount;

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        resultRecord = new ByteRecord(MAX_BUF); // a reasonable chunk of data
        resultBuffer = resultRecord.getBuffer();
        resultPos = 0;
        saxLineCount = 0;
        saxState = 0;
        saxLineBuffer = new StringBuffer(128);
    } // startDocument

    /** Tests whether this line could fit into the byte buffer,
     *  possibly writes the latter, and then decodes the string
     *  and appends its bytes to the byte buffer,
     */
    private void decode() {
        String softLine = saxLineBuffer.toString();
        saxLineBuffer.setLength(0);
        int decodePos = 0;
        while (decodePos < softLine.length()) {
            char ch = softLine.charAt(decodePos ++);
            if (ch == '=') {
                if (decodePos == softLine.length()) { // soft linebreak at the end - ignore
                } else { // decode hex representation
                    int code = 0;
                    if (decodePos + 2 <= softLine.length()) {
                        try {
                            code = Integer.parseInt(softLine.substring(decodePos, decodePos + 2), 16);
                        } catch (Exception exc) {
                            log.error("invalid =xx escape sequence in <td> element #"
                                    + saxLineCount + "\n" + softLine);
                        }
                        decodePos += 2;
                    } else {
                        log.error("incomplete =xx escape sequence in <td> element #"
                            + saxLineCount + ", decodePos=" + decodePos + "\n" + softLine);
                    }
                    resultBuffer[resultPos ++] = (byte) code;
                }
            } else { // not '='
                resultBuffer[resultPos ++] = (byte) ch;
            }
        } // while decodePos
        resultRecord.write(byteWriter, resultPos);
        resultPos = 0;
    } // decode

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
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs) {
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        elem = qName;
        if (false) {
        } else if (qName.equals(ROW_TAG  )) {
            if (saxState == 1) {
                saxLineBuffer.append("\r\n");
                decode();
            }
            saxState = 0;
        } else if (qName.equals(DATA_TAG )) {
            saxLineCount ++;
        } // else ignore unknown elements
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
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        elem = ""; // no characters allowed outside <td> ... </td>
        if (false) {
        } else if (qName.equals(ROW_TAG  )) { // now output this row
            saxState = 1;
        } else if (qName.equals(DATA_TAG )) { // now finish this field
            decode();
        } else if (qName.equals(ROOT_TAG )) {
        } else if (qName.equals(TABLE_TAG )) {
        } // ignore unknown elements
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int len) {
        if (elem .equals(DATA_TAG )) {
            saxLineBuffer.append(new String(ch, start, len));
        } // else ignore characters in unknown elements
    } // characters
    
} // QuotedPrintableTransformer
