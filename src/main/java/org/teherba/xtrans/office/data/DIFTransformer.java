/*  (Navy) DIF - Data Interchange Format
    @(#) $Id: DIFTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2006-12-28, Dr. Georg Fischer: adapted from pt/data/pddif.c
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

package org.teherba.xtrans.office.data;
import  org.teherba.xtrans.CharTransformer;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/**
 *  Transformer for (Navy) DIF - Data Interchange Format.
 *  Current limitations: XML very close to DIF, no representation of 
 *  a usual spreadsheet table.
 *  Example:
<pre>
TABLE
0,1
"punctum"
VECTORS
0,2
""
TUPLES
0,32767
""
LABEL
1,1
"plz"
LABEL
2,1
"city"
SIZE
2,20
""
DATA
0,0
""
-1,0
BOT
0,79801
""
1,0
"Freiburg"
-1,0
BOT
0,79341
V
1,0
"Kenzingen"
-1,0
EOD
</pre>
 *  @author Dr. Georg Fischer
 */
public class DIFTransformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: DIFTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** Upper bound for character buffer */
    private static final int MAX_BUF = 4096;

    /** Root element tag */
    private static final String ROOT_TAG    = "dif";
    /** Element tag for topics */
    private static final String TOPIC_TAG   = "topic";
    /** Element tag for vectors */
    private static final String VECTOR_TAG  = "vector";
    /** Element tag for text strings */
    private static final String NUM_TAG     = "num";
    /** Element tag for topics */
    private static final String STR_TAG     = "str";
    /** Comment element tag */
    private static final String COMMENT_TAG = "comment";
    /** Data element tag */
    private static final String DATA_TAG    = "data";
    /** Group element tag */
    private static final String GROUP_TAG   = "group";

    /** No-args Constructor.
     */
    public DIFTransformer() {
        super();
        setFormatCodes("dif");
        setDescription("DIF - Data Interchange Format");
        setFileExtensions("dif");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = LogManager.getLogger(DIFTransformer.class.getName());
        charBuffer = new char[MAX_BUF];
        content = new StringBuffer(MAX_BUF);
	} // initialize

    /** buffer for values in input stream */
    private StringBuffer content;
    
    /** Buffer for a portion of the input file */
    private char[] charBuffer;

    /** current input field */
    private  int  ifield; 

    /** state of finite automaton */
    private  int  state; 
    /** last global state of input processing */
    private  int  lastState;
    
    /** values of <em>state</em> */
    private static final int START_HEADER   = 1;
    private static final int START_TEXT     = 2;
    private static final int IN_TOPIC       = 3;
    private static final int IN_VEC         = 4;
    private static final int IN_NUMVAL      = 5;
    private static final int IN_QUOTE_INIT  = 6;
    private static final int IN_STRVAL      = 7;
    private static final int IN_QUOTE_TERM  = 8;
    private static final int IN_TEXT        = 9;

    /** topic (1st line of a quadruple) */
    private String topic;
    /** vector number (1st value in the 2nd line of a quadruple) */
    private String vector;
    /** numeric value (2nd value in the 2nd line of a quadruple) */
    private String numValue;
    /** string value (3rd line of a quadruple) */
    private String strValue;
    
    /** current XML element built during scanning process */
    private StringBuffer element;
    /** label of a statement */
    private String label;
    /** name of a parameter */
    private String parmName;
    
    /** Evaluates a (declarative) quadruple
     */
    private  void evalQuadruple () {
        state = IN_TOPIC;
        fireStartElement (GROUP_TAG);
        fireSimpleElement(TOPIC_TAG , topic   );
        fireSimpleElement(VECTOR_TAG, vector  );
        fireSimpleElement(NUM_TAG   , numValue);
        fireStartElement (STR_TAG  );
        fireCharacters   (strValue );
        fireEndElement   (STR_TAG  );
        fireEndElement   (GROUP_TAG);
        fireLineBreak ();
        if (false) {
        } else if (topic.compareTo("TABLE"          ) == 0) {
        } else if (topic.compareTo("VECTORS"        ) == 0) {
        } else if (topic.compareTo("LABEL"          ) == 0) {
        } else if (topic.compareTo("SIZE"           ) == 0) {
        } else if (topic.compareTo("TUPLES"         ) == 0) {
        } else if (topic.compareTo("DATA"           ) == 0) {
            state = START_TEXT; // start and rows of fields 
        } else if (topic.compareTo("UNITS"          ) == 0) {
        } else if (topic.compareTo("DISPLAYUNITS"   ) == 0) {
        } else if (topic.compareTo("TRUELENGTH"     ) == 0) {
        } else if (topic.compareTo("MINORSTART"     ) == 0) {
        } else if (topic.compareTo("MAJORSTART"     ) == 0) {
        } else if (topic.compareTo("PERIODICITY"    ) == 0) {
        } else if (topic.compareTo("COMMENT"        ) == 0) {
        } else {
            log.error("unknown keyword " + topic);
        } // 
    } // evalQuadruple 
    
    /** Evaluates a (data cell) triple
     */
    private  void evalTriple () {
        state = IN_VEC;
        fireStartElement (DATA_TAG );
        fireSimpleElement(VECTOR_TAG, vector  );
        fireSimpleElement(NUM_TAG   , numValue);
        fireStartElement (STR_TAG  );
        fireCharacters   (strValue );
        fireEndElement   (STR_TAG  );
        fireEndElement   (DATA_TAG );
        fireLineBreak ();
    } // evalTriple 

    /** Processes a portion of the input file (one line)
     *  @param start offset where to start/resume scanning
     *  @param trap  offset behind last character to be processed
     *  @return offset behind last character which was processed
     */
    private int processInput(int start, int trap) {
        char ch; // current character to be processed
        boolean readOff; // whether current character should be consumed
        content.setLength(0);
        element.setLength(0);
        int ibuf = start;
        while (ibuf < trap) { // process all characters 
            readOff = true;
            ch = charBuffer[ibuf];
            switch (state) {
                case START_HEADER:
                    readOff = false;
                    lastState = state;
                    state = IN_TOPIC;
                    content.setLength(0);
                    break;
                case START_TEXT:
                    readOff = false;
                    lastState = state;
                    state = IN_VEC;
                    content.setLength(0);
                    break;
                case IN_TOPIC:
                    switch (ch) {
                        case '\n': // record end in MS-DOS and Unix 
                            topic = content.toString();
                            state = IN_VEC;
                            content.setLength(0);
                            break;
                        case '\r': // ignore carriage return 
                        case '\u001a': // ignore end of file 
                        case ' ':  // ignore space 
                            break;
                        default:
                            content.append(ch);
                            break;
                    } // switch ch
                    break;
                case IN_VEC:
                    switch (ch) {
                        case '\n': // record end in MS-DOS and Unix 
                        case ',':
                            vector = content.toString();
                            state = IN_NUMVAL;
                            content.setLength(0);
                            break;
                        case '\r': // ignore carriage return 
                        case '\u001a': // ignore end of file 
                        case ' ':  // ignore space 
                            break;
                        default:
                            content.append(ch);
                            break;
                    } // switch ch
                    break;
                case IN_NUMVAL:
                    switch (ch) {
                        case '\n': // record end in MS-DOS and Unix 
                            numValue = content.toString();
                            state = IN_QUOTE_INIT;
                            content.setLength(0);
                            break;
                        case '\r': // ignore carriage return 
                        case '\u001a': // ignore end of file 
                        case ' ':  // ignore space 
                            break;
                        default:
                            content.append(ch);
                            break;
                    } // switch ch
                    break;
                case IN_QUOTE_INIT:
                    if (ch == '"') {
                        state = IN_STRVAL;
                    } else { // no quote -> value indicator 
                        state = IN_STRVAL;
                        readOff = false;
                    }
                    break;
                case IN_STRVAL:
                    switch (ch) {
                        case '\n': // record end in MS-DOS and Unix 
                            readOff = false;
                            state = IN_QUOTE_TERM;
                            break;
                        case '"':
                            state = IN_QUOTE_TERM;
                        case '\u001a': // ignore end of file 
                        case '\r':
                            break;
                        default:
                            content.append (ch);
                            break;
                    } // switch ch
                    break;
                case IN_QUOTE_TERM:
                    switch (ch) {
                        case '\n': // record end in MS-DOS and Unix 
                            strValue = content.toString();
                            if (lastState == START_HEADER) {
                                evalQuadruple ();
                            } else { // lastState == START_TEXT 
                                evalTriple ();
                            }
                            content.setLength(0);
                            break;
                        case '\u001a': // ignore end of file 
                        case '\r':
                        default: // ignore others 
                            break;
                    } // switch ch
                    break;
                default:
                    break;
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
        element = new StringBuffer(64);
        putEntityReplacements();
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            state = START_HEADER; 
            lastState = state;
            while ((len = charReader.read(charBuffer, 0, MAX_BUF)) >= 0) {
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
    private StringBuffer saxBuffer;

    /** currently opened element */
    private String elem;
    /** current vector number (-1 for artificial cells) */
    private String vectorNumber;
    /** whether in group (in DIF header) */
    private boolean inGroup;
    
    /** Fill a string with spaces and print it
     *  @param str string to be printed
     */
    public void putLine(String str) {
        saxBuffer.setLength(0);
        saxBuffer.append(str);
        putLine();
    } // putLine
    
    /** Fill the line with spaces and print it
     */
    public void putLine() {
        charWriter.println(saxBuffer.toString());
        saxBuffer.setLength(0);
    } // putLine
    
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        saxBuffer = new StringBuffer(MAX_BUF); // a rather long line
        elem = "";
        vectorNumber = "0";
        inGroup = false;
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
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs) {
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        elem = qName;
        saxBuffer.setLength(0);
        if (false) {
        } else if (qName.equals(GROUP_TAG   )) { 
        	inGroup = true;
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
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        elem = ""; 
        if (false) {
        } else if (qName.equals(GROUP_TAG   )) { 
        	inGroup = false;
        } else if (qName.equals(ROOT_TAG    )) { 
        } else if (qName.equals(TOPIC_TAG   )) { 
            charWriter.println(saxBuffer.toString());
            saxBuffer.setLength(0);
        } else if (qName.equals(VECTOR_TAG  )) { 
            vectorNumber = saxBuffer.toString();
            saxBuffer.append(",");
            charWriter.print(saxBuffer.toString());
            saxBuffer.setLength(0);
        } else if (qName.equals(NUM_TAG     )) { 
            charWriter.println(saxBuffer.toString());
            saxBuffer.setLength(0);
        } else if (qName.equals(STR_TAG     )) { 
            if (inGroup || vectorNumber.equals("1")) { // no number, no artificial cell
                saxBuffer.append("\"");
                charWriter.print("\"");
            }
            charWriter.println(saxBuffer.toString());
            saxBuffer.setLength(0);
        } // else ignore unknown elements
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int len) {
        if  (   elem.equals(TOPIC_TAG   ) 
            ||  elem.equals(VECTOR_TAG  )
            ||  elem.equals(NUM_TAG     )
            ||  elem.equals(STR_TAG     )
            )
        {
            String text = new String(ch, start, len);
            saxBuffer.append(text);
        } // else ignore characters in unknown elements
    } // characters
} // DIFTransformer
