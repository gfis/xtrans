/*  Transforms YACC (Yet Another Compiler Compiler, or GNU Bison) grammar files 
    @(#) $Id: YACCTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2007-05-14, Georg Fischer: copied from ExtraTransformer
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

package org.teherba.xtrans.grammar;
import  org.teherba.xtrans.grammar.ExtraTransformer;
import  java.io.BufferedReader;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transforms YACC (Yet Another Compiler Compiler, or GNU Bison) grammar files.
 *  Example for a YACC grammar:
<pre>
%token NAME NUMBER
%%
statement: NAME '=' expression
	| expression					{ printf ("= %d\n", $1); }
	;
expression: expression '+' NUMBER	{ $$ = $1 + $3; }
	|	    expression '-' NUMBER	{ $$ = $1 - $3; }
	|		NUMBER					{ $$ = $1; }
	;
%%

</pre>
 *  @author Dr. Georg Fischer
 */
public class YACCTransformer extends ExtraTransformer { 
    public final static String CVSID = "@(#) $Id: YACCTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = LogManager.getLogger(YACCTransformer.class.getName());;
    
    /** No-args Constructor.
     */
    public YACCTransformer() {
        super();
        setFormatCodes("yacc");
        setDescription("Yet Another Compiler Compiler");
        setFileExtensions("y");
    } // Constructor()

	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        putEntityReplacements();
	} // initialize

    
    //////////////////////
    // SAX event generator
    //////////////////////

    /** state of finite automaton */
    private int  state;  
    /** values for <em>state</em> */ 
    private static final int IN_APOS     =  1; // after 1st apostrophe
    private static final int IN_COMMENT  =  2;
    private static final int IN_NAME     =  3;
    private static final int IN_PUNCT    =  4;
    private static final int IN_SEMA     =  5;
    private static final int IN_STRING   =  6; // after 1st double quote
    private static final int IN_BACKSLASH=  7; // after 1st "\\"
    private static final int IN_HEX      =  8;
    private static final int IN_PERCENT  =  9; // after 1st "%"
    private static final int IN_BRACE    = 10; // after 1st "{"
    
    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
    	int noPcPc = 0; // number of "%%" tokens read so far
    	int braceLevel = 0; // nesting level of "{ ... }" inside semantic actions
        boolean result  = true;
        StringBuffer elementBuffer = new StringBuffer(64); // current XML element built during scanning process
        int hexBuffer = 0; // buffer for hex digits behind x or u
        try {
        	putEntityReplacements();
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            int state = IN_PUNCT;
            boolean inLHS    = false; // whether in left hand side of a rule
            boolean inString = false; // whether in string (enclosed in single quotes)
            boolean inTrans  = false; // whether in translation part
            boolean readOff  = true;  // whether current character should be consumed
            int grammarState = 0; // 0 = in prelude, 1 = in grammar, 2 = in postlude
            String line;
            BufferedReader buffReader = new BufferedReader(charReader);
            lineNo = 0;
            while ((line = buffReader.readLine()) != null) {
                lineNo ++;
                int linePos = 0;
                if (line.startsWith("%%")) {
                	grammarState ++;
                	linePos = 2;
	                switch (grammarState) {
	                	case 0: // cannot occur
	                	    break;
	                	case 1:
	                	    break;
	                	case 2:
                            if (inTrans) {
                                fireEndElement(TRANS_TAG);
                            } 
                            fireEndElement(PROD_TAG);
                            fireEndElement(RULE_TAG);
                            fireStartElement(POSTLUDE_TAG);
	                	    break;
	                	default: // cannot occur
	                	    break;
	                } // switch grammarState
                } // leading "%%"

	            switch (grammarState) {
	            	case 0: // cannot occur
	            		fireCharacters(replaceNoSource(line));
	            		fireLineBreak();
	            	    break;
	            	
	            	case 1:
                        char ch; // current character to be processed
                        int trap = line.length();
                        
                        while (linePos < trap) { // process all characters 
                            readOff = true;
                            ch = line.charAt(linePos);
                            switch (state) {
                                
                                case IN_PUNCT:
                                    switch (ch) {
                                        case ' ':
                                        case '\t':
                                            // ignore whitespace
                                            break;
                                        case '/':
                                            if (line.substring(linePos).startsWith("/*")) {
                                                state = IN_COMMENT;
                                                elementBuffer.setLength(0);
                                                linePos ++;
                                            } else {
                                            /*
                                                fireStartElement(ERROR_TAG);
                                                fireCharacters(replaceNoSource(line.substring(linePos)));
                                                fireEndElement(ERROR_TAG);
                                            */
                                                state = IN_PUNCT;
                                            }
                                            break;
                                        case '\'':
                                            inString = true;
                                            elementBuffer.setLength(0);
                                            state = IN_STRING;
                                            break;
                                        case '%':
                                            inLHS = true;
				        					state = IN_PERCENT;
                                            break;
                                        case '#':
                                            state = IN_SEMA;
                                            elementBuffer.setLength(0);
                                            break;
                                        case ';': // end of rule, start new one
                                            if (inTrans) {
                                                fireEndElement(TRANS_TAG);
                                            } 
                                            fireEndElement(PROD_TAG);
                                            fireEndElement(RULE_TAG);
                                            inLHS = true;
                                            break;
                                        case ':': // end of left hand side, start of 1st production
                                            {
                                                fireStartElement(PROD_TAG);
                                                inTrans = false;
                                                inLHS = false;
                                            }
                                            break;
                                        case '|': // start of another production
                                            if (inTrans) {
                                                fireEndElement(TRANS_TAG);
                                            }
                                            fireEndElement(PROD_TAG);
                                            fireStartElement(PROD_TAG);
                                            inTrans = false;
                                            break;
                                        case '{': // start of semantic action(s)
                                            state = IN_BRACE;
                                            break;
                                        default:
                                            if (Character.isLetter(ch)) {
                                                elementBuffer.setLength(0);
                                                elementBuffer.append(ch);
                                                state = IN_NAME;
                                            }
                                            break;
                                    } // switch ch
                                    break;
                                    
                                case IN_PERCENT:
                                    if (ch == '%') {
                                    	noPcPc ++;
                                    	inLHS = true;
                                        if (noPcPc == 2 && ! inLHS) {
                                        } else if (noPcPc == 1) {
                                            elementBuffer.setLength(0);
                                            elementBuffer.append(ch);
                                            fireSimpleElement(SYM_TAG, replaceNoSource(elementBuffer.toString()));
                                            break;
                                        }
                                    } else {
                                    }
                                	state = IN_PUNCT;
                                	break;
                                	
                                case IN_BRACE:
                                    switch (ch) {
                                        case '}':
		                                	state = IN_PUNCT;
		                                	break;
				        				default:
				        					break;
				        			} // switch ch
                                	break;
                                	
                                case IN_COMMENT:
                                    if (line.substring(linePos).startsWith("*/")) {
                                        state = IN_PUNCT;
                                        fireSimpleElement(COMMENT_TAG, replaceNoSource(elementBuffer.toString()));
                                        linePos ++;
                                    } else {
                                        elementBuffer.append(ch);
                                    }
                                    break;
                                    
                                case IN_NAME:
                                    if (Character.isLetterOrDigit(ch) || ch == '_') {
                                        elementBuffer.append(ch);
                                    } else {
                                        readOff = false;
                                        state = IN_PUNCT;
                                        if (inLHS) {
                                            fireStartElement(RULE_TAG);
                                            fireSimpleElement(LEFT_TAG, elementBuffer.toString());
                                        } else {
                                            fireSimpleElement(SYM_TAG,  elementBuffer.toString());
                                        }
                                    }
                                    break;
                                    
                                case IN_SEMA:
                                    if (Character.isDigit(ch)) {
                                        elementBuffer.append(ch);
                                    } else {
                                        readOff = false;
                                        state = IN_PUNCT;
                                        fireSimpleElement(SEMA_TAG, elementBuffer.toString());
                                    }
                                    break;
                                    
                                case IN_STRING:
                                    switch (ch) {
                                        case '\'': // 2nd apostrophe
                                            state = IN_APOS;
                                            break;
                                        case '\\':
                                            state = IN_BACKSLASH;
                                            break;
                                        default:
                                            elementBuffer.append(ch);
                                            break;
                                    }
                                    break; // IN_STRING 
                                    
                                case IN_BACKSLASH:
                                    state = IN_STRING;
                                    switch (ch) {
                                        case '\\':
                                            elementBuffer.append('\\');
                                            break;
                                        case 'f':
                                            elementBuffer.append('\f');
                                            break;
                                        case 'n':
                                            elementBuffer.append('\n');
                                            break;
                                        case 'r':
                                            elementBuffer.append('\r');
                                            break;
                                        case 't':
                                            elementBuffer.append('\t');
                                            break;
                                        case 'u':
                                        case 'x':
                                            hexBuffer = 0;
                                            state = IN_HEX;
                                            break;
                                        default:
                                            elementBuffer.append(ch);
                                            break;
                                    }
                                    break;
                                    
                                case IN_HEX:
                                    int dig = Character.digit(ch, 16);
                                    if (dig >= 0) {
                                        hexBuffer = hexBuffer * 16 + dig;
                                    } else {
                                        state = IN_STRING;
                                        readOff = false;
                                        elementBuffer.append((char) (hexBuffer & 0xffff));
                                    }
                                    break;
                                    
                                case IN_APOS:
                                    switch (ch) {
                                        case '\'':
                                            elementBuffer.append(ch); // append 2nd apostrophe only
                                            state = IN_STRING;
                                            break;
                                        default: // no 2nd apostrophe followed - end of string
                                            readOff = false;
                                            fireSimpleElement(TERM_TAG, elementBuffer.toString());
                                            state = IN_PUNCT;
                                            break;
                                    }
                                    break; // IN_APOS
                                    
                                default:
                                    log.error("invalid state " + state);
                                    break;
                            } // switch state */
                            if (readOff) {
                                linePos ++;
                            }
                        } // while processing characters
                        fireLineBreak();
	            	    break;
	            	    
	            	case 2:
	            		fireCharacters(replaceNoSource(line));
	            		fireLineBreak();
	            	    break;
	            	default: // cannot occur
	            	    break;
	            } // switch grammarState

            } // while not EOF
            buffReader.close();
            fireEndElement(POSTLUDE_TAG);
            fireEndElement(ROOT_TAG);
            fireLineBreak();
            fireEndDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return  result;
    }

    //////////////////////
    // SAX content handler 
    //////////////////////

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        lineBuffer  = new StringBuffer(MAX_BUF); // a rather long line
        String nl = System.getProperty("line.separator");
        openElement = "";
        ruleCount   = 0;
		startGrammarToken = nl + "%%";
		endGrammarToken   = "%%" + nl;
		startRuleToken    = " : ";
		endRuleToken      = nl + ";";
		startProdToken    = "\t| ";
		endProdToken      = "";
		startTransToken   = " { ";
		endTransToken     = " } ";
        charWriter.print("%token ");
    }
    
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
            lineBuffer.setLength(0);
        } else if (qName.equals(COMMENT_TAG)) { 
            lineBuffer.setLength(0);
            lineBuffer.append("/*");
        } else if (qName.equals(RULE_TAG   )) { 
            charWriter.print(lineBuffer.toString());
            if (ruleCount <= 0) {
                lineBuffer.setLength(0);
                lineBuffer.append(startGrammarToken);
            } else {
            }
            ruleCount ++;
            prodCount = 0;
        } else if (qName.equals(LEFT_TAG   )) { 
        } else if (qName.equals(SEMA_TAG   )) { 
            lineBuffer.append("#");
        } else if (qName.equals(SYM_TAG    )) { 
        } else if (qName.equals(TRANS_TAG  )) { 
            lineBuffer.append(startTransToken);
        } else if (qName.equals(PROD_TAG   )) { 
            if (prodCount <= 0) {
                lineBuffer.append(startRuleToken);
            } else {
                charWriter.println(lineBuffer.toString());
                lineBuffer.setLength(0);
                lineBuffer.append(startProdToken);
            }
            prodCount ++;
        } else if (qName.equals(TERM_TAG   )) { 
            lineBuffer.append('\'');
        } // else ignore unknown elements
    }
    
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
            charWriter.println(lineBuffer.toString());
            lineBuffer.setLength(0);
            charWriter.print(endGrammarToken);
        } else if (qName.equals(COMMENT_TAG)) { 
            lineBuffer.append("*/");
            charWriter.println(lineBuffer.toString());
            lineBuffer.setLength(0);
        } else if (qName.equals(RULE_TAG   )) { 
            lineBuffer.append(endRuleToken);
            charWriter.print(lineBuffer.toString());
            lineBuffer.setLength(0);
        } else if (qName.equals(LEFT_TAG   )) { 
        } else if (qName.equals(SEMA_TAG   )) { 
            lineBuffer.append(" ");
        } else if (qName.equals(SYM_TAG    )) { 
            lineBuffer.append(" ");
        } else if (qName.equals(TRANS_TAG  )) {
        	lineBuffer.append(endTransToken); 
        } else if (qName.equals(PROD_TAG   )) { 
        	lineBuffer.append(endProdToken); 
            charWriter.print(lineBuffer.toString());
            lineBuffer.setLength(0);
        } else if (qName.equals(TERM_TAG   )) { 
            lineBuffer.append("\' ");
            charWriter.print(lineBuffer.toString());
            lineBuffer.setLength(0);
        } // else ignore unknown elements
    }
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int len) {
        if  (false) {
        } else if ( openElement.equals(COMMENT_TAG) 
                ||  openElement.equals(SYM_TAG    )
                ||  openElement.equals(LEFT_TAG   )
                ||  openElement.equals(SEMA_TAG   )
                ) {
            String text = new String(ch, start, len);
            lineBuffer.append(new String(ch, start, len));
        } else if ( openElement.equals(TERM_TAG   )) {
            String text = replaceInResult(new String(ch, start, len));
            lineBuffer.append(text.replaceAll("\'","\'\'"));
        } // else ignore whitespace around unknown elements
    }
}