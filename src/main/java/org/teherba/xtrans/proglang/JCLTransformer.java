/*  Transforms IBM z/OS Job Control Language (JCL) to and from XML
    @(#) $Id: JCLTransformer.java 833 2011-11-28 20:47:14Z gfis $
    2011-11-28: continuation of parameters before column 16
	2011-11-25: more attributes to help normalization
    2007-03-21: separation of generator and content handler checked
    2007-03-15: use 'fire...' methods in generator
    2006-12-14, Dr. Georg Fischer: adapted from pljcl.c
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
package org.teherba.xtrans.proglang;
import  org.teherba.xtrans.CharTransformer;
import  java.io.BufferedReader;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Transformer for IBM's z/OS Job Control Language (JCL). 
 *  The file must already be translated to ASCII.
 *  JCL has very few main statements:
 *  <code>DD, ELSE, ENDIF, EXEC, IF, JOB, PEND, PROC</code>.
 *  Example for a job (translate all "#" to "*"):
<pre>
//RZFPA    JOB (200,300,'Georg Fischer'),
//         NOTIFY=&SYSUID,MSGCLASS=H
/# ROUTE P
//STEP1    EXEC PGM=IEFBR14
//DD1      DD   DSN=RZFP.TEST.CNTL,DISP=(NEW,CATLG,DELETE),
//              SPACE=(CYL,(1,1)),DCB=(RECFM=FB)
//         DD   DSN=AC8Y.TEST.CLIST
//SYSIN    DD   *
this is inline data
more inline data
/#
//# THIS IS A COMMENT
//STEP2    EXEC PROC1
//# ANOTHER COMMENT
//STEP3    EXEC PGM=DFSORT
//SORTIN   DD   DSN=RZFP.TEST.CLIST(SF1)
//
//# should not be shown
//STEP4    EXEC NOSHOW
/#

This is converted to the following XML:

<jcl>
    <stmt label="RZFPA" op="JOB">
        <parm>
            <group>
                <parm>200</parm>
                <parm>300</parm>
                <parm>Georg Fischer</parm>
            </group>
        </parm>
        <parm name="NOTIFY">&amp;SYSUID</parm>
        <parm name="MSGCLASS">H</parm>
    </stmt>
    <eof> ROUTE P</eof>

    <!-- ... -->

    <stmt label="SYSIN" op="DD">
        <parm>*</parm>
    </stmt>
    <data>this is inline data</data>
    <data>more inline data</data>
    <eof />
    <comment> THIS IS A COMMENT</comment>

    <!-- ... -->
    
</jcl>

</pre>
 *  @author Dr. Georg Fischer
 */
public class JCLTransformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: JCLTransformer.java 833 2011-11-28 20:47:14Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** length of a JCL line */
    private static final int MAX_LINE = 72;
    /** Upper bound for character buffer */
    private static final int MAX_BUF = 4096;

    /** Root element tag */
    private static final String ROOT_TAG    = "jcl";
    /** Comment element tag */
    private static final String COMMENT_TAG = "comment";
    /** Data element tag */
    private static final String DATA_TAG    = "data";
    /** End of Job element tag */
    private static final String EOJ_TAG     = "eoj";
    /** End of Data element tag */
    private static final String EOF_TAG     = "eof";
    /** Group element tag */
    private static final String GROUP_TAG   = "group";
    /** Parameter element tag */
    private static final String PARM_TAG    = "parm";
    /** Statement element tag */
    private static final String STMT_TAG    = "stmt";
    /** Element tag for text strings */
    private static final String TEXT_TAG    = "text";

    /** No-args Constructor.
     */
    public JCLTransformer() {
        super();
        setFormatCodes("jcl");
        setDescription("IBM z/OS Job Control Language");
        setFileExtensions("jcl");
    } // Constructor()
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(JCLTransformer.class.getName());
	} // initialize
	
    //////////////////////
    // SAX event generator
    //////////////////////

    /** Buffer for strings in operands */
    private StringBuffer operBuffer;
    
    /** state of finite automaton */
    private int  operState;  
    /** values for <em>operState</em> */ 
    private static final int IN_NAME     =  1;
    private static final int IN_OPERAND  =  2;
    private static final int IN_STRING   =  3;
    private static final int IN_APOS     =  4;
    private static final int IN_COMMENT  =  5;
    /** type of a range of several lines */
    private int lineState;
    /** values for <em>lineState</em> */ 
    private static final int UNKNOWN     = 21; // at the beginning and after complete statements
    private static final int STATEMENT   = 22; // after //
    private static final int STATEMENT_2 = 23; // continued statement (previous had trailing ",")
    /** current JCL line  with indexes 0..71 = columns 1..72 */
    String line;
        
    /** level for nested parentheses */
    private int groupLevel;
    /** whether there was a trailing comma */
    private boolean trailingComma;

    /** true during and after string (within apostrophes) recognition */
    private boolean isString;
    /** true after an opening (GDG or member) parenthesis in a DSN */
    private boolean innerOpen;

    /** current XML element built during scanning process */
    private StringBuffer elementBuffer;
    /** name of a parameter */
    private Attributes parmAttrs;
    /** current number of positional parameter */
    private int parmNo;
    /** label of current statement, if present, or of preceeding labeled statement if not */
    private String stmtLabel;
    /** sequential number statement, 0 if labelled, 1, 2, 3 and so on for following unlabelled statements */
    private int stmtSeq;
    
    /** Starts the XML for a parameter 
     */
    private void startParm() {
    	if (true) {
	       	if (parmAttrs == null && groupLevel <= 1) { // has no name
    	   		parmNo ++;
       			parmAttrs = toAttribute("id", String.valueOf(parmNo));
       		} // not a named parameter
       	}
        pushXML(PARM_TAG, parmAttrs);
        parmAttrs = null;
        elementBuffer.setLength(0);
    } // startParm
    
    /** Terminates the XML for a parameter 
     */
    private void endParm() {
        if (! topXML().equals(PARM_TAG)) {
	       	if (parmAttrs == null && groupLevel <= 1) { // has no name
    	   		parmNo ++;
       			parmAttrs = toAttribute("id", String.valueOf(parmNo));
       		} // not a named parameter
            pushXML(PARM_TAG, parmAttrs);
            parmAttrs = null;
        }
        if (isString) {
            isString = false;
            // log.debug(elementBuffer.toString());
            fireSimpleElement(TEXT_TAG, elementBuffer.toString());
        } else {
            fireCharacters(replaceNoSource(elementBuffer.toString()));
        }
        elementBuffer.setLength(0);
        // fireComment("popend");
        popXML(); // parm
    } // endParm
    
    /** Processes the buffer for operands, which has a trailing space
     *  for proper termination. A typical string in the buffer would be:
     <pre>
         "DSN=PRGF.TEST.CNTL,DISP=(NEW,CATLG,DELETE),SPACE=(CYL,(1,1)),DCB=(RECFM=FB) "
     </pre>
     */
    private void processOperands() {
        char ch; // current character to be processed
        boolean readOff; // whether current character should be consumed
        // log.debug(operBuffer.toString());
        int trap = operBuffer.length();
        operState = IN_NAME;
        isString = false;
        innerOpen = false;
        elementBuffer.setLength(0);
        parmAttrs = null;
        int ibuf = 0;
        while (ibuf < trap) { // process all characters 
            readOff = true;
            ch = operBuffer.charAt(ibuf);
            switch (operState) {
                
                case IN_NAME:
                    switch (ch) {
                        case '=':
                            parmAttrs = toAttribute("name", elementBuffer.toString());
                            elementBuffer.setLength(0);
                            break;
                        case '(':
                            if (elementBuffer.length() == 0) {
                                groupLevel ++;
                                startParm();
                                pushXML(GROUP_TAG, toAttribute("level", String.valueOf(groupLevel)));
                            } else { // open at the end of a DSN etc. - member, generation
                                innerOpen = true;
                                elementBuffer.append(ch);
                            }
                            break;
                        case '\t':
                            ch = ' '; 
                            // fall through
                        case ' ':
                            // fall through
                        case ',':
                            endParm();
                            break;
                        case ')':
                            if (innerOpen) {
                                elementBuffer.append(ch);
                                innerOpen = false;
                            } else {
                                endParm();
                                groupLevel --;
                                // fireComment("popname");
                                popXML(); // group
                                // fireLineBreak();
                            }
                            break;
                        case '\'':
                            isString = true;
                            elementBuffer.setLength(0);
                            operState = IN_STRING;
                            break;
                        case '\u001a':
                        case '{':
                        	elementBuffer.append("Ä");
                        	break;
                        case '|':
                        	elementBuffer.append("Ö");
                        	break;
                        case '}':
                        	elementBuffer.append("Ü");
                        	break;
                        default:
                            elementBuffer.append(ch);
                            break;
                    } // switch ch */
                    break;  
                    
                case IN_STRING:
                    switch (ch) {
                        case '\'':
                            operState = IN_APOS;
                            break;
                        case '\u001a':
                        case '{':
                        	elementBuffer.append("Ä");
                        	break;
                        case '|':
                        	elementBuffer.append("Ö");
                        	break;
                        case '}':
                        	elementBuffer.append("Ü");
                        	break;
                        default:
                            elementBuffer.append(ch);
                            break;
                    }
                    break; // IN_STRING 
                    
                case IN_APOS:
                    switch (ch) {
                        case '\'':
                            elementBuffer.append(ch); // append 2nd apostrophe only
                            operState = IN_STRING;
                            break;
                        default: // no 2nd apostrophe followed - end of string
                            readOff = false;
                            operState = IN_NAME;
                            break;
                    }
                    break; // IN_APOS
                    
                default:
                    log.error("invalid operState " + operState);
                    break;
            } // switch operState */
            
            if (readOff) {
                ibuf ++;
            }
        } // while processing characters
        while (! tagStack.isEmpty()) {
            tagStack.pop();
        }
    } // processOperands
               
	/** Scans over blanks, the operands, blanks, any line end comment, and any continuation indicator
	 *	@param pos the current position (on first blank)
	 *	@param len maximum length of the line
	 */
	private void scanOperands(int pos, int len) {
        while (pos < len && line.charAt(pos) == ' ') {
            pos ++; // search 1st non-blank
        } // while searching for operands
        int pos2 = pos + 1;
        while (pos2 < len && line.charAt(pos2) != ' ') {
            pos2 ++; // search 1st blank
        } // while searching thru operands
        operBuffer.append(line.substring(pos, pos2));
        if (line.charAt(pos2 - 1) != ',' && (pos2 < MAX_LINE || line.charAt(pos2 - 1) == ' ')) { // no continuation
            operBuffer.append(' '); // break process loop
            processOperands();
            fireEndElement(STMT_TAG);
	        fireLineBreak();
            lineState = UNKNOWN;
        } else {
            lineState = STATEMENT_2;
        }
	} // scanOperands
	
    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result  = true;
        elementBuffer   = new StringBuffer(64);
        operBuffer      = new StringBuffer(MAX_BUF);
        int pos; // temporary position in 'line'
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            lineState = UNKNOWN;
	        parmNo    = 0; // start counting of positional parameters
            operBuffer.setLength(0);
            BufferedReader buffReader = new BufferedReader(charReader);
            lineNo = 0;
            while ((line = buffReader.readLine()) != null) {
                lineNo ++;
                int len = line.length();
                if (len > MAX_LINE) {
                    len = MAX_LINE;
                }
                while (len > 1 && line.charAt(len - 1) == ' ') {
                    len --; // trim trailing spaces
                } // while trimming
                // log.debug("len=" + len + ", " + line.substring(0, len));
                
                if (false) {
                } else if (line.startsWith("//*")) { // comment
                    fireSimpleElement(COMMENT_TAG, line.substring(3).replaceAll("\u001a","Ä"));
	                fireLineBreak();
                } else if (line.startsWith("/*") ) { // EOF
                    fireStartElement(EOF_TAG);
                    fireCharacters(line.substring(2, len));
                    fireEndElement  (EOF_TAG);
	                fireLineBreak();
                } else if (line.equals    ("//") ) { // EOJ
                    fireEmptyElement(EOJ_TAG);
	                fireLineBreak();
                } else if (line.startsWith("//") ) { // statement
                    switch(lineState) {
                        case STATEMENT_2: // continuation
                            pos = 2;
                            scanOperands(pos, len);
                            break;
                        default: // start of new statement
					        parmNo    = 0; // start counting of positional parameters
                            pos = 2;
                            while (pos < len && line.charAt(pos) != ' ') {
                                pos ++; // search 1st blank
                            } // while searching thru label
                            String label = line.substring(2, pos);
                            while (pos < len && line.charAt(pos) == ' ') {
                                pos ++; // search 1st non-blank 
                            } // while searching for op
                            int opPos = pos;
                            while (pos < len && line.charAt(pos) != ' ') {
                                pos ++; // search 1st blank
                            } // while searching thru op
                            String op = line.substring(opPos, pos);
                            if (label.length() > 0) {
                            	stmtSeq = 0;
	           					stmtLabel = label;
	                            pushXML(STMT_TAG, toAttributes(new String[]
    	                                { "label", stmtLabel
    	                                , "seq"  , String.valueOf(stmtSeq)
        	                            , "op"   , op
            	                        }));
                            } else { // without label
                            	stmtSeq ++;
	                            pushXML(STMT_TAG, toAttributes(new String[]
    	                                { "seq"  , String.valueOf(stmtSeq)
    	                        //      , "label", stmtLabel
    	                                , "op"   , op
            	                        }));
                            } // without label
					        operBuffer.setLength(0);
                            scanOperands(pos, len);
                            break;
                    } // switch lineState
                } else { // data line
                    switch(lineState) {
                        default: // start of new data section
                            fireSimpleElement(DATA_TAG, line.substring(0, len));
			                fireLineBreak();
                            break;
                    } // switch lineState
                }
                // log.debug("operBuffer=\"" + operBuffer.toString() + "\"");
                // fireLineBreak();
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

    //////////////////////
    // SAX content handler 
    //////////////////////

    /** position of last comma in operands portion in <em>lineBuffer</em> */
    private int lastCommaPos;

    /** true during and after string recognition */
    private boolean maybeString;

    /** buffer for output line */
    private StringBuffer lineBuffer;

    /** currently opened element */
    private String openElement;
    /** previous element */
    private String previousElement;
    
    /** Fill a string with spaces and print it
     *  @param str string to be printed
     */
    public void putLine(String str) {
        lineBuffer.setLength(0);
        lineBuffer.append(str);
        putLine();
    } // putLine
    
    /** Fill the line with spaces and print it
     */
    public void putLine() {
        while (lineBuffer.length() < MAX_LINE) {
            lineBuffer.append(' ');
        } // while padding
        charWriter.println(lineBuffer.toString());
        lineBuffer.setLength(0);
    } // putLine(0)
    
    /** Test for a line break, and generate one with a continuation line if necessary
     */
    public void breakLine() {
        if (lineBuffer.length() > MAX_LINE) {
            // print and continue on next line
            if (lastCommaPos < 0) {
                log.error("long operand string without comma");
            } else {
                String cont = lineBuffer.substring(lastCommaPos + 1);
                lineBuffer.delete(lastCommaPos + 1, lineBuffer.length());
                putLine();
                lineBuffer.append("// ");
                lineBuffer.append(cont);
                lastCommaPos = lineBuffer.length() - 1;
            }
        } else {
            lastCommaPos = lineBuffer.length() - 1;
        }
	} // breakLine
	
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        lineBuffer = new StringBuffer(MAX_BUF); // a rather long line
        openElement = "";
        previousElement = "";
        maybeString = false;
        lastCommaPos = -1; // invalid
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
        openElement = qName;
        if (false) {
        } else if (qName.equals(ROOT_TAG   )) { 
        } else if (qName.equals(COMMENT_TAG)) { 
            lineBuffer.setLength(0);
            lineBuffer.append("//*");
        } else if (qName.equals(DATA_TAG   )) { 
            lineBuffer.setLength(0);
        } else if (qName.equals(EOF_TAG    )) { 
            lineBuffer.setLength(0);
            lineBuffer.append("/*");
        } else if (qName.equals(EOJ_TAG    )) { 
            lineBuffer.setLength(0);
            lineBuffer.append("//");
        } else if (qName.equals(GROUP_TAG  )) { 
            lineBuffer.append("(");
            previousElement = "";
        } else if (qName.equals(PARM_TAG   )) { 
            if (previousElement.equals(PARM_TAG)) {
                lineBuffer.append(',');
				breakLine();
            }
            String name = attrs.getValue("name");
            if (name != null) {
                lineBuffer.append(name);
                lineBuffer.append('=');
            }
        } else if (qName.equals(STMT_TAG   )) { 
            //        1         2         3         4         
            //2345678901234567890123456789012345678901234567890123456789
            //LABEL678 OP345 PARM=VALUE,
            lineBuffer.setLength(0);
            lineBuffer.append("//");
            String label = attrs.getValue("label");
            if (label == null) {
                label = "";
            }
            lineBuffer.append(label);
            lineBuffer.append(' ');
            while (lineBuffer.length() < 11) {
                lineBuffer.append(' ');
            }
            String op = attrs.getValue("op");
            if (op != null) {
                lineBuffer.append(op);
            }
            lineBuffer.append(' ');
        /* not necessary
            while (lineBuffer.length() < 16) {
                lineBuffer.append(' ');
            }
        */
        } else if (qName.equals(TEXT_TAG   )) { 
            maybeString = true;
            lineBuffer.append('\'');
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
        openElement = ""; 
        if (false) {
        } else if (qName.equals(ROOT_TAG   )) { 
        } else if (qName.equals(COMMENT_TAG)) { 
            putLine();
        } else if (qName.equals(DATA_TAG   )) { 
            putLine();
        } else if (qName.equals(EOF_TAG    )) { 
            putLine();
        } else if (qName.equals(EOJ_TAG    )) { 
            putLine();
        } else if (qName.equals(GROUP_TAG  )) { 
            lineBuffer.append(")");
        } else if (qName.equals(PARM_TAG   )) { 
        } else if (qName.equals(STMT_TAG   )) { 
			breakLine();
            putLine();
        } else if (qName.equals(TEXT_TAG   )) { 
            lineBuffer.append('\'');
            maybeString = false;
        } // else ignore unknown elements
        previousElement = qName;
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int len) {
        if (false) {
        } else if ( openElement.equals(COMMENT_TAG) 
                ||  openElement.equals(DATA_TAG   )
                ||  openElement.equals(EOF_TAG    )
                ||  openElement.equals(TEXT_TAG   )
                ) {
            String text = (new String(ch, start, len));
            lineBuffer.append(maybeString ? text.replaceAll("\'","\'\'") : text);
        } else if ( openElement.equals(GROUP_TAG  )
                ||  openElement.equals(PARM_TAG   )
                ) {
            String text = (new String(ch, start, len)).trim();
            lineBuffer.append(maybeString ? text.replaceAll("\'","\'\'") : text);
        } // else ignore characters in unknown elements
    } // characters

} // JCLTransformer
