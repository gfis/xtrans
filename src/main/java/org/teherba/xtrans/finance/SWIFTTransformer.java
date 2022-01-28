/*  Transforms S.W.I.F.T. FIN message types (MTxxx)
    @(#) $Id: SWIFTTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2008-06-11: multiline 4th parameter of 'filter'
    2007-09-28: renamed with uppercase SWIFT
    2007-03-14: 'parseSWIFTLine' renamed from 'parse' <= collision with XMLReader interface
    2006-09-23, Dr. Georg Fischer
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

package org.teherba.xtrans.finance;
import  org.teherba.xtrans.CharTransformer;
import  java.io.BufferedReader;
import  java.util.ArrayList;
import  java.util.Stack;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transformer for SWIFT FIN message types (MTxxx).
 *  The messages transferred by the <em>Society for
 *  Worldwide International Financial Transfers (<a href="http://www.swift.com">S.W.I.F.T.</a>)</em>
 *  consist of 5 rather freely formatted blocks (enclosed in curly brackets), where block 4
 *  contains a series of fields whose tags consist of
 *  two digits, optionally followed by a letter, and which are enclosed by colons.
 *  <p>
 *  The direction (I = input, O = output as seen by SWIFT) and the message type (xxx = 3 digits) are
 *  coded at the start of block 2.
 *  Blocks 3 and 5 may have (even nested) substructures also enclosed in curly brackets.
 *  Block 5 carries checksums and is normally computed and appended before the message transfer.
 *  <p>
 *  This transformer can convert to XML and back transparently without option "-iso".
 *  If this option is set to "true", the field values are converted to ISO 20022 format
 *  where reasonable, and it is not reasonable to convert that XML back to MT format.
 *  <p>
 *  In any case, the order of the subelements is always the linear order of the MT message.
 *  Block elements start with "B", and SWIFT tags are prefixed "F".
 *  ZKA subfield numbers are prefixed with "Z".
 *  All tags on top  level must be uppercase,
 *  tag for substructures  must be lowercase (inserted by 'filter').
 *  <p>
 *  Examples for an MT103:
 *  <pre>
{1:F01BICFOOYYAXXX8682497001}{2:O1030803051028ESPBESMMAXXX54237368340510280803N}{3:{113:NOMF}{108:0510280086100047}{119:STP}}{4:
:20:D051025EUR100047
:13C:/RNCTIME/123123123+0000
:23B:CRED
:32A:051028EUR724297,95
:33B:EUR724297,95
:50A:ALLFESMMXXX
:53A:NORTESMMXXX
:57A:BICFOOYYXXX
:59:/ES0123456789012345671234
MS MULTIGESTION CONSERVADOR FIMF
:70:REDEMPTS. TRADEDATE 2005-10-25
/123123123: MS MULTIGESTION CONSER
:71A:SHA
-}{5:{MAC:766F7678}{CHK:45718FA95A25}}

{1:F01OWHBDEFFAXXX9999999999}{2:I940 N}{4:
:20:GL0205020003
:25:50320000/0202264016
:28C:00088/00001
:60F:C020502EUR101755,28
:62F:C020502EUR101755,28
:64:C020502EUR101755,28
-}
 *  </pre>
 *  @author Dr. Georg Fischer
 */
public class SWIFTTransformer extends CharTransformer {
    public final static String CVSID = "@(#) $Id: SWIFTTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    /** counter for successive lines for same tag */
    protected int lineCount;
    /** tag for which lines are counted */
    protected String lineTag;
    /** whether to generate values for ISO 20022 */
    protected boolean toISO20022;
    /** system dependant newline string */
    protected String newline;

    /** No-args constructor
     */
    public SWIFTTransformer() {
        super();
        setFormatCodes("swift");
        setDescription("SWIFT FIN message (MTnnn)");
    } // Constructor

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log         = LogManager.getLogger(SWIFTTransformer.class.getName());
        newline     = System.getProperty("line.separator");
    } // initialize

    /** Generates an element from an MT amount
     *  subfield with the resulting value in MT or MX representation.
     *  @param value amount with decimal comma, and 0/1/n digits behind the comma
     */
    protected void fireAmt(String value) {
        if (toISO20022) {
            value = value.replaceAll(",", ".");
            int dotPos = value.indexOf('.');
            if (dotPos < 0) {
                value += ".00";
            } else {
                int diff = value.length() - dotPos;
                if (diff <= 2) {
                    value += "000".substring(0, 3 - diff);
                }
            }
        }
        fireSimpleElement("amt", value);
    } // fireAmt

    /** Generates an element from an MT credit/debit indicator
     *  subfield with the resulting value in MT or MX representation.
     *  @param value "C" or "D"
     */
    protected void fireSign(String value) {
        if (toISO20022) {
            if (false) {
            } else if (value.equals("C")) {
                value = "CRDT";
            } else if (value.equals("D")) {
                value = "DBIT";
            }
        }
        fireSimpleElement("sign", value);
    } // fireSign

    /** Generates an element from an MT date.
     *  The century (19 or 20) is guessed from the year with changeover at 1980.
     *  subfield with the resulting value in MT or MX representation.
     *  @param value date of the form YYMMDD
     */
    protected void fireDate(String value) {
        if (toISO20022) {
            String year   = value.substring( 0, 2);
            value = ((year.compareTo("79") > 0) ? "19" : "20") + year
                    + "-" + value.substring( 2, 4)
                    + "-" + value.substring( 4, 6)
                    ;
        }
        fireSimpleElement("yymmdd", value);
    } // fireDate

    /** Generates an element from an MT date
     *  subfield with the resulting value in MT or MX representation.
     *  The year of the short date is guessed from the long date.
     *  The century (19 or 20) is guessed from the year with changeover at 1980.
     *  @param shortDate date of the form MMDD
     *  @param longDate  date of the form YYMMDD
     *  which is within a 5 month range around the short date
     */
    protected void fireShortDate(String shortDate, String longDate) {
        if (toISO20022) {
            try {
                int year = Integer.parseInt(longDate .substring(0, 2));
                int lmon = Integer.parseInt(longDate .substring(2, 4)); // month from long  date
                int smon = Integer.parseInt(shortDate.substring(0, 2)); // month from short date
                if (lmon == smon) { // same year - take it simply
                } else if (lmon > smon) {
                    if (lmon - smon <= 5) { // same year
                    } else { // smon in next year
                        year ++;
                        if (year >= 100) {
                            year = 0;
                        }
                    }
                } else { // smon > lmon
                    if (smon - lmon <= 5) { // same year
                    } else { // smon in previous year
                        year --;
                        if (year < 0) {
                            year = 99;
                        }
                    }
                }
                year += (year > 79 ? 1900 : 2000);
                shortDate = String.valueOf(year)
                        + "-" + String.valueOf(smon + 100).substring(1) // force leading zero
                        + "-" + shortDate.substring(2, 4); // DD
            } catch (Exception exc) {
                // non-digits in date, ignore, leave date as it is
            }
        }
        fireSimpleElement("mmdd", shortDate);
    } // fireShortDate

    /** Generates an element from an MT timestamp (:13D:) of the form YYMMDDhh:mm+hhmm.
     *  subfield with the resulting value in MT or MX representation.
     *  The century (19 or 20) is guessed from the year with changeover at 1980.
     *  @param value timestamp of the form 07062905:30+0200
     */
    protected void fireTimestamp(String value) {
        if (toISO20022) {
            try {
                // :13D:07062905:30+0200
                //      012345678901234567
                String year   = value.substring( 0, 2);
                value = ((year.compareTo("79") > 0) ? "19" : "20") + year
                        + "-" + value.substring( 2, 4)
                        + "-" + value.substring( 4, 6)
                        + "T" + value.substring( 6,14)
                        + ":" + value.substring(14,16)
                        ;
            } catch (Exception exc) {
            }
        }
        fireSimpleElement("time", value);
    } // fireTimestamp

    /** Root element tag */
    protected static final String   ROOT_TAG    = "SWIFT-FIN";
    /** XML tag for block elements */
    protected static final String   BLOCK_TAG   = "B";
    /** XML tag for field elements */
    protected static final String   FIELD_TAG   = "F";
    /** XML tag for ZKA special subfields in :86: */
    protected static final String   ZKA_TAG     = "Z";
    /** XML tag for which suppresses the output of its (nested) character content */
    protected static final String   VOID_TAG    = "VOID";
    /** XML tag for a single message of some type (e.g. MT940) */
    protected static final String   MESSAGE_TAG = "message";
    /** XML tag for unspecified block contents */
    protected static final String   REST_TAG    = "rest";
    /** XML tag for message type */
    protected static final String   TYPE_TAG    = "mt";
    /** XML attribute name for message type */
    protected static final String   TYPE_ATTR   = "type";
    /** XML tag for direction of transfer */
    protected static final String   DIR_TAG     = "dir";
    /** XML tag for variant of a field - not used at present */
    protected static final String   VAR_TAG     = "var";
    /** XML tag for explicit new line */
    protected static final String   NEWLINE_TAG = "N";

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        toISO20022  = ! getOption("iso", "false").startsWith("f");
        state       = IN_LBRACE;
        content     = new StringBuffer(296);
        clearLines();
        stack       = new Stack/*<1.5*/<String>/*1.5>*/();
        messageType = "";
        messageDir  = "";
        level       = 0;
        inFirstOfB4 = false;
        lineCount   = 0;
        lineTag     = "";
        mainBlockTag= "";
        try {
            fireStartDocument();
            fireStartRoot   (ROOT_TAG);
            fireLineBreak   ();
            String line;
            BufferedReader buffReader = new BufferedReader(charReader);
            while ((line = buffReader.readLine()) != null) {
                parseSWIFTLine(line + "\n");
            } // while lines
            terminate();
            fireEndElement  (ROOT_TAG);
            fireLineBreak   ();
            fireEndDocument ();
            fireLineBreak   ();
            buffReader.close();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return  result;
    } // generate

    /*==================*/
    /* SWIFT FIN parser */
    /*==================*/

    /** Stack for nested tags */
    private Stack/*<1.5*/<String>/*1.5>*/ stack = new Stack/*<1.5*/<String>/*1.5>*/();
    /** Level of nested "{...}" structures: 0, 1, 2 ... */
    private int level;
    /** Current state of the parser */
    private int state;
    /** Whether in first field of block 4 */
    private boolean inFirstOfB4;
    /** current main block tag (B1..B5) */
    private String mainBlockTag;
    /** Buffer for one line of field currently parsed */
    private StringBuffer content;
    /** Buffer for all lines of field currently parsed */
    private ArrayList/*<1.5*/<String>/*1.5>*/ lines;
    /** SWIFT FIN message number, e.g. 940 for MT940 */
    private String messageType;
    /** direction of the message: I or O as seen from SWIFT */
    private String messageDir;

    /** Parser states enumeration */
    private static final int    IN_LBRACE   = 0;
    private static final int    IN_B4_FIELD = 1;
    private static final int    IN_BLOCK    = 2;
    private static final int    IN_NEWLINE  = 3;
    private static final int    IN_ID       = 4;
    /** Codes for debug output of states */
    private static final String[] stateDebug = new String[]
            { "LB"
            , "TX"
            , "BL"
            , "NL"
            , "ID"
            };
    /** Relevant input characters to be scanned */
    protected static final char   CH_LBRACE   = '{';
    protected static final char   CH_RBRACE   = '}';
    protected static final char   CH_COLON    = ':';
    protected static final char   CH_DASH     = '-';
    protected static final char   CH_NEWLINE  = '\n';
    protected static final char   CH_QMARK    = '?';
    protected static final char   CH_SLASH    = '/';

    /** Appends a character to the <em>content</em> buffer,
     *  eventually encodes an entity before
     *  @param ch character to be appended
     */
    private void append(char ch) {
        switch (ch) {
            case '&':   content.append("&amp;");    break;
            case '"':   content.append("&quot;");   break;
            case '<':   content.append("&lt;");     break;
            case '>':   content.append("&gt;");     break;
            default:    content.append(ch);         break;
        } // switch ch
    } // append ch

    /** Appends a string to the <em>content</em> buffer,
     *  does not encode an entity before
     *  @param str string to be appended
     */
    private void append(String str) {
        content.append(str);
    } // append str

    /** Clears the <em>lines</em> buffer.
     */
    private void clearLines() {
        lines = new ArrayList/*<1.5*/<String>/*1.5>*/(10); // 10 continuation lines are ok
    } // clearLines

    /** Starts an XML element, take its name from the
     *  <em>content</em> buffer and clear that buffer;
     *  prefixes the name with a letter if it starts with a digit,
     *  and extracts a trailing variant letter
     */
    private void openElement() {
        String tag = content.toString();
        String variant = null;
        switch (state) {
            case IN_LBRACE:
                if (tag.length() > 0 && Character.isDigit(tag.charAt(0))) {
                    tag = BLOCK_TAG + tag;
                }
                break;
            default:
                if (tag.length() > 0 && Character.isDigit(tag.charAt(0))) {
                    if (tag.length() > 2) {
                        variant = tag.substring(2);
                        tag = tag.substring(0, 2);
                    }
                    tag = FIELD_TAG + tag;
                }
                break;
        } // switch state
        if (variant != null) {
            tag += variant;
        }
        fireStartElement(tag);
        stack.push(tag);
        content.setLength(0);
    } // openElement

    /** Splits a block's content into subfields.
     *  @return false if the surrounding element (top on stack) is no block element &lt;Bn&gt;
     */
    private boolean blockStructure() {
        boolean result = false;
        String tag = (String) stack.peek();
        if (tag.length() == 2 && tag.startsWith(BLOCK_TAG)) {
            result = true;
            mainBlockTag = tag;
            switch (tag.charAt(1)) {
                case '1':
                    String value = (content.toString());
                    fireCharacters(value.substring(0, 3));
                    fireSimpleElement("bic12", value.substring(3, 15));
                    fireCharacters(value.substring(15));
                    break;
                case '2':
                    fireStartElement( DIR_TAG);
                    messageDir  = content.substring(0, 1);
                    fireCharacters  (messageDir );
                    fireEndElement  ( DIR_TAG);
                    fireStartElement(TYPE_TAG);
                    messageType = content.substring(1, 4);
                    fireCharacters  (messageType);
                    fireEndElement  (TYPE_TAG);
                    fireStartElement(REST_TAG);
                    fireCharacters  (content.substring(4   ));
                    fireEndElement  (REST_TAG);
                    break;
                default: // '3', '4' , '5'
                    fireCharacters  (content.toString());
                    break;
            } // switch
        } // if block
        return result;
    } // blockStructure

    /** Writes the <em>content</em> buffer
     *  to the XML output file; clears the buffer
     */
    private void emit() {
        if (! stack.empty() && blockStructure()) {
        } else { // no block element
        /*
            String tag = stack.size() > 0 ? (String) stack.peek() : "";
            if (tag.equals(NEWLINE_TAG)) {
                // ignore
            } else if (! tag.equals(lineTag)) {
                lineTag   = tag;
                lineCount = 1;
            } else {
                lineCount ++;
            }
            fireCharacters(filter(messageDir, messageType, tag, lines));
            if (mainBlockTag.equals("B4")) {
                lines.add(content.toString());
            } else {
            }
        */
            fireCharacters(content.toString());
        }
        content.setLength(0);
    } // emit

    /** Pseudo-abstract method for further substructuring of a field
     *  in a derived class, with an array of continuation lines
     *  @param type type of the SWIFT FIN message, e.g. 940
     *  @param dir message transfer direction as seen from SWIFT: "I" or "O"
     *  @param tag field designator, e.g. "B3", "F20"
     *  @param lines content of the field to be substructured,
     *  with continuation lines in following array elements
     *  @return unchanged value or the empty string if substructure was already emitted
     */
    protected String filter(String dir, String type, String tag, ArrayList/*<1.5*/<String>/*1.5>*/ lines) {
        // transparent implementation: concatenation of all lines, separated by newline
        StringBuffer result = new StringBuffer(1024);
        int iline = 0;
        int len = lines.size();
        fireCharacters(lines.get(iline ++));
        while (iline < len) {
            fireEmptyElement(NEWLINE_TAG);
            fireCharacters(lines.get(iline));
            iline ++;
        } // while
        return result.toString();
    } // filter

    /** Ends an XML element
     */
    private void closeElement() {
        String tag = (String) stack.pop();
        if (lines.size() > 0) {
            fireCharacters(filter(messageDir, messageType, tag, lines));
        }
        clearLines();
        fireEndElement(tag);
        if (state != IN_LBRACE || tag.length() <= 2) {
            fireLineBreak();
        }
    } // closeElement

    /** Parses a line with SWIFT blocks and/or tags, and
     *  emits XML elements as necessary
     *  @param line line with blocks and tags
     */
    private void parseSWIFTLine(String line) {
        int pos = 0;
        if (line.startsWith("{1")) { // determine and open message type
            pos = line.indexOf("{2:");
            //                  {2:I940...
            //                  012345678
            if (pos >= 0 && pos <= line.length() - 7) {
                // extract message type from block 2
                fireStartElement(MESSAGE_TAG, toAttribute(TYPE_ATTR, line.substring(pos + 4, pos + 7)));
            } else {
                fireStartElement(MESSAGE_TAG);
            }
            // fireLineBreak();
        } // start message type

        pos = 0;
        while (pos < line.length()) {
            char ch = line.charAt(pos ++);
            // fireCharacters("[" + stateDebug[state] + "]");
            switch (state) {

                case IN_LBRACE:
                    switch(ch) {
                        case CH_LBRACE:
                            emit();
                            level ++;
                            break;
                        case CH_COLON:
                            openElement();
                            if (level == 1 && ((String) stack.peek()).equals(BLOCK_TAG + "4")) {
                                state = IN_NEWLINE;
                                inFirstOfB4 = true;
                                fireLineBreak();
                            } else {
                                state = IN_BLOCK;
                            }
                            break;
                        case CH_RBRACE:
                            emit(); closeElement();
                            level --;
                            break;
                        default:
                            append(ch);
                            break;
                    } // switch ch
                    break;

                case IN_BLOCK: // colon is not recognized
                    switch(ch) {
                        case CH_LBRACE:
                            // should not happen
                            emit(); // closeElement();
                            level ++;
                            state = IN_LBRACE;
                            break;
                        case CH_RBRACE:
                            state = IN_LBRACE;
                            emit(); closeElement();
                            level --;
                            break;
                        default:
                            append(ch);
                            break;
                    } // switch ch
                    break;

                case IN_B4_FIELD: // content of block 4
                    switch(ch) {
                        case CH_NEWLINE:
                            state = IN_NEWLINE; // delay output of a newline
                            lines.add(content.toString());
                            content.setLength(0);
                            break;
                        default:
                            append(ch);
                            break;
                    } // switch ch
                    break;

                case IN_NEWLINE:
                    switch(ch) {
                        case CH_NEWLINE: // 2nd newline - ignore it
                            // state = IN_NEWLINE; // delay output of a newline
                            break;
                        case CH_COLON:
                            state = IN_ID;
                            if (! inFirstOfB4) {
                                emit(); closeElement(); // terminate old field
                            }
                            inFirstOfB4 = false;
                            break;
                        case CH_DASH:
                            emit(); closeElement();
                            state = IN_LBRACE;
                            break;
                        default:
                            if (! toISO20022) {
                            /*
                                emit(); fireEmptyElement(NEWLINE_TAG);
                                fireLineBreak();
                            */
                            }
                            state = IN_B4_FIELD;
                            append(ch);
                            break;
                    } // switch ch
                    break;

                case IN_ID:
                    switch(ch) {
                        case CH_NEWLINE: // error: newline in ID
                            state = IN_NEWLINE; // delay output of a newline
                            break;
                        case CH_COLON:
                            state = IN_B4_FIELD;
                            openElement();
                            lineCount = 0; // 'filter' will start with 1 if same tag
                            break;
                        default:
                            append(ch);
                            break;
                    } // switch ch
                    break;

                default:
                    log.error("invalid state " + state);
                    break;
            } // switch state
        } // while consuming characters

        if (line.startsWith("-}")) { // close message type
            fireEndElement(MESSAGE_TAG);
            fireLineBreak();
        } // close message type
    } // parseSWIFTLine

    /** Pseudo-abstract method: terminates the parsing process
     *  and emits all remaining XML elements
     */
    protected void terminate() {
    } // terminate

    /*=======================*/
    /* SWIFT FIN SAX Handler */
    /*=======================*/

    /** current block: B1, B2, B3, B4 (payload) or B5 (trailer) */
    private String currentBlock;

    /** buffer for the assembly of a Base64 string */
    private StringBuffer saxBuffer;

    /** Inhibits the output of character content inside a VOID element. */
    protected boolean inhibitChars;

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        currentBlock = "";
        inhibitChars = false;
    } // startDocument

    /** Receive notification of the end of the document.
     */
    public void endDocument() {
        // charWriter.println();
    } // endDocument

    /** Receive notification of the start of an element.
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
        } else if (qName.compareTo(BLOCK_TAG + "1") >= 0
                && qName.compareTo(BLOCK_TAG + "5") <= 0) { // start of block 1..5
            currentBlock = qName;
            charWriter.print(CH_LBRACE);
            charWriter.print(qName.substring(1));
            charWriter.print(CH_COLON);
            if (qName.equals(BLOCK_TAG + "4")) {
                charWriter.println();
            }
            currentBlock = qName;
        } else if (qName.startsWith(BLOCK_TAG)) {
            charWriter.print(CH_LBRACE);
            charWriter.print(qName.substring(1));
            charWriter.print(CH_COLON);
        } else if (qName.startsWith(FIELD_TAG)) {
            charWriter.print(CH_COLON);
            charWriter.print(qName.substring(1));
            String var = attrs.getValue(VAR_TAG);
            if (var != null) {
                charWriter.print(var);
            }
            charWriter.print(CH_COLON);
        } else if (qName.startsWith(ZKA_TAG)) {
            charWriter.print(CH_QMARK);
            charWriter.print(qName.substring(1));
        } else if (qName.equals(NEWLINE_TAG)) {
            charWriter.println();
        } else if (qName.equals(ROOT_TAG)) {
            saxBuffer = new StringBuffer(128);
        } else if (qName.equals(VOID_TAG)) {
            inhibitChars = true;
        } else if (qName.equals(DIR_TAG)
                || qName.equals(MESSAGE_TAG)
                || qName.equals(TYPE_TAG)
                || qName.equals(REST_TAG)) {
            saxBuffer = new StringBuffer(128);
        } else if (! Character.isLowerCase(qName.charAt(0))) { // first letter is uppercase - on top level
            charWriter.print(CH_LBRACE);
            charWriter.print(qName);
            charWriter.print(CH_COLON);
        } else { // lowercase - substructure to be ignored
        }
    } // startElement

    /** Receive notification of the end of an element.
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
        if (false) {
        } else if (qName.compareTo(BLOCK_TAG + "1") >= 0
                && qName.compareTo(BLOCK_TAG + "5") <= 0) { // end of block
            if (qName.equals(BLOCK_TAG + "4")) {
                charWriter.print(CH_DASH);
            }
            charWriter.print(CH_RBRACE);
            currentBlock = "";
        } else if (qName.startsWith(BLOCK_TAG)) {
            charWriter.print(CH_RBRACE);
        } else if (qName.equals(DIR_TAG      )) {
        } else if (qName.startsWith(FIELD_TAG)) {
            charWriter.println();
        } else if (qName.equals(MESSAGE_TAG  )) {
            charWriter.println();
        } else if (qName.equals(NEWLINE_TAG  )) {
            // already done in startElement
        } else if (qName.equals(REST_TAG     )) {
        } else if (qName.equals(ROOT_TAG     )) {
        } else if (qName.equals(TYPE_TAG     )) {
        } else if (qName.startsWith(ZKA_TAG  )) {
        } else if (qName.equals(VOID_TAG)) {
            inhibitChars = false;
        } else if (! Character.isLowerCase(qName.charAt(0))) { // first letter is uppercase - on top level
            charWriter.print(CH_RBRACE);
        } else { // lowercase - substructure to be ignored
        }
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int length) {
        if (! inhibitChars) {
            if (length > 0 && ch[start] == '\n') { // ignore
                start ++;
                length --;
            }
            if (currentBlock.length() > 0) {
                charWriter.print((new String(ch, start, length)).replaceAll(newline, ""));
            }
            if (false && length > 0 && ch[0] != '\n') { // interesting
                // charWriter.print((new String(ch, start, length)).trim());
                charWriter.print(("x" + (new String(ch, start, length))).trim().substring(1)); // trim right only
            }
        } // inhibitChars
    } // characters

} // SWIFTTransformer
