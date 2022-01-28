/*  Donald Knuth's TeX typesetting system, and LaTeX
    @(#) $Id: TeXTransformer.java 812 2011-10-04 07:01:28Z gfis $
    2011-10-04, Dr. Georg Fischer

	not yet working
*/
/*
 * Copyright 2011 Dr. Georg Fischer <punctum at punctum dot kom>
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
import  org.teherba.xtrans.CharTransformer;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/**
 *  Transformer for Donald Knuth's TeX typesetting system, and LaTeX
 *  Examples:
<pre>
\chapter{Einleitung}
\label{Einleitung}
\emph{Baustelle}\\
1971 war das Geburtsjahr des  BMW 3.0 CSL (E9)...
\cite{Oldtimer2010}\\

\cleardoublepage

\begin{wrapfigure}{r}{0.4\textwidth}
  \begin{center}
    \includegraphics[width=0.38\textwidth]{Grafiken/Verfahrensschritte.pdf}
  \end{center}
  \caption{Flussdiagramm zur Bestimmung der optischen Qualität}
  \label{fig:Flussdiagramm}
\end{wrapfigure}

\section{Zielsetzung}
Um eine Parameteroptimierung durchführen zu können 
</pre>
 *  @author Dr. Georg Fischer
 */
public class TeXTransformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: TeXTransformer.java 812 2011-10-04 07:01:28Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** Upper bound for character buffer */
    private static final int MAX_BUF = 4096;

    /** Root element tag */
    private static final String ROOT_TAG    	= "tex";
    /** Element which denotes a line break, for readability/reconstruction only */
    private static final String NEWLINE_TAG 	= "n";
    /** Group element tag */
    private static final String GROUP_TAG   	= "group";

    /** Element tag for hard space */
    private static final String HARD_SPACE_TAG  = "xnbsp";
    /** Element tag for hard hyphen */
    private static final String HARD_HYPHEN_TAG = "xhhy";
    /** Element tag for soft hyphen */
    private static final String SOFT_HYPHEN_TAG = "xshy";

	/** whether the tag is optional, i.e. is prefixed with "\\*" */
	private boolean withStar;
	
    /** buffer for values in input stream */
    private StringBuffer content;
    
    /** Buffer for a portion of the input file */
    private char[] charBuffer;

    /** state of finite automaton */
    private  int  state; 
    
    /** values of <em>state</em> */
    private static final int IN_BACKSLASH   = 3;
    private static final int IN_HEX1        = 4;
    private static final int IN_HEX2        = 5;
    private static final int IN_NUMBER      = 2;
    private static final int IN_TAG         = 1;
    private static final int IN_TEXT        = 9;
    private static final int IN_QUOTE_INIT  = 6;
    private static final int IN_STRVAL      = 7;
    private static final int IN_QUOTE_TERM  = 8;

    /** current RTF tag */
    private StringBuffer texTag;
    /** numerical value of current RTF tag */
    private StringBuffer texNumber;
    /** sign of numerical value of current RTF tag */
    private String texSign;
    /** 2 hexadecimal digits behind \\' */
    private int hexCode;
    /** nesting level of groups */
    private int groupLevel;
    
    /** No-args Constructor.
     */
    public TeXTransformer() {
        super();
        setFormatCodes		("tex");
        setDescription		("TeX, LaTeX - Typesetting System");
        setFileExtensions	("tex");
        setMimeType			("text/tex");
    } // Constructor
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = LogManager.getLogger(TeXTransformer.class.getName());
	} // initialize

    /** Emits document text, and writes its characters
     */
    private void evaluateContent() {
    	fireCharacters(content.toString());
    	content.setLength(0);
	} // evaluateContent
	
    /** Starts a group
     */
    private void startTeXGroup() {
		evaluateContent();
    	groupLevel ++;
		fireStartElement(GROUP_TAG);
	} // startTeXGroup
	
    /** Ends   a group
     */
    private void   endTeXGroup() {
		evaluateContent();
		fireEndElement  (GROUP_TAG);
		fireLineBreak();
    	groupLevel --;
	} //   endTeXGroup
	
    /** Evaluates a TeX tag, and writes an empty element for it
     */
    private void evaluateTeXTag() {
    	String value = texSign.toString() + texNumber.toString();
    	if (value.length() == 0) {
    		if (withStar) {
	    		fireEmptyElement(texTag.toString(), toAttribute("s", "*"));
	    	} else {
	    		fireEmptyElement(texTag.toString());
			}
    	} else { // with value
    		if (withStar) {
	    		fireEmptyElement(texTag.toString(), toAttributes(new String[] 
	    				{ "s", "*"
	    				, "n", value
	    				}));
	    	} else {
	    		fireEmptyElement(texTag.toString(), toAttribute("n", value));
			}
    	}
    	withStar = false;
    	texTag.setLength(0);
    	texNumber.setLength(0);
    	texSign = "";
	} // evaluateTeXTag
	
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
            ch = charBuffer[ibuf];
            switch (state) {

                case IN_TEXT:
                    switch (ch) {
                        case '\u001a': // ignore end of file 
                        case '\r': // ignore CR
                        	break;
                        case '\n': // record end in MS-DOS and Unix 
							evaluateContent();
                        	fireEmptyElement(NEWLINE_TAG);
                        	break;
                        case '{': // start of a substructure
                        	startTeXGroup();
                        	break;
                        case '}': // end   of a substructure
                        	endTeXGroup();
                        	break;
						case '\\': 
							state = IN_BACKSLASH;
							texTag.setLength(0);
							break;
                        default:
                            content.append(ch);
                            break;
                    } // switch ch
                    break; // IN_TEXT

                case IN_BACKSLASH:
                    switch (ch) {
                        case '\u001a': // ignore end of file 
                        case '\r': // ignore CR
                        	break;
                        case '\n': // record end in MS-DOS and Unix 
							evaluateContent();
                        	fireEmptyElement(NEWLINE_TAG);
                        	break;
                        case '*':
                        	state = IN_TEXT;
							evaluateContent();
                        	withStar = true;
                        	break;
                        case '{':
                        case '}':
                        case '\\':
                        	state = IN_TEXT;
                        	content.append(ch);
                        	break;
                        case '~': // hard space
							state = IN_TEXT;
							evaluateContent();
							fireEmptyElement(HARD_SPACE_TAG);
							break;
                        case '-': // soft hyphen
							state = IN_TEXT;
							evaluateContent();
							fireEmptyElement(SOFT_HYPHEN_TAG);
							break;
                        case '_': // hard hyphen
							state = IN_TEXT;
							evaluateContent();
							fireEmptyElement(HARD_HYPHEN_TAG);
							break;
                         case '\'': // start of hexcode
							state = IN_HEX1;
							hexCode = 0;
							break;
                        default:
                        	if (Character.isLetter(ch)) {
                        		state = IN_TAG;
								evaluateContent();
                            	texTag.append(ch);
                            } else { // ignore backslash
                            	state = IN_TEXT;
                            	content.append(ch);
                            }
                            break;
                    } // switch ch
                    break; // IN_BACKSLASH

                case IN_HEX1:
                	state = IN_HEX2;
                	hexCode = Character.digit(ch, 16);
                	if (hexCode < 0) {
                		log.error("invalid hexcode " + hexCode);
                		hexCode = 0;
					}
                	break; // IN_HEX1
                case IN_HEX2:
                	state = IN_TEXT;
                	int hex2 = Character.digit(ch, 16);
                	if (hexCode < 0) {
                		log.error("invalid hexcode " + hexCode);
						hexCode = (hexCode << 4);
					} else {
						hexCode = (hexCode << 4) | hex2;
					}
					if (hexCode >= 0x20) {
						content.append((char) hexCode);
					} else { // reproduce original (strange code)
						content.append("\\'" + (Integer.toHexString(0x100 + hexCode)).substring(1));
					}
                	break; // IN_HEX2

				case IN_TAG:
					switch (ch) {
                        case '\u001a': // ignore end of file 
                        case '\r': // ignore CR
                        	break;
                        case '\n': // record end in MS-DOS and Unix 
							evaluateTeXTag();
                        	fireEmptyElement(NEWLINE_TAG);
							state = IN_TEXT;
							// ignore that newline
                        	break;
						case '-':
							state = IN_NUMBER;
							texSign = "-";
							texNumber.setLength(0);
							break;
						case ' ':
							evaluateTeXTag();
							state = IN_TEXT;
							// ignore that space
							break;
						default:
							if (false) {
							} else if (Character.isLetter(ch)) {
								texTag.append(ch);
							} else if (Character.isDigit (ch)) {
								state = IN_NUMBER;
								texSign = "";
								texNumber.setLength(0);
								texNumber.append(ch);
							} else {
								evaluateTeXTag();
								state = IN_TEXT;
								readOff = false;
							}
							break;
					} // switch ch
					break; // IN_TAG
				
				case IN_NUMBER:
                    switch (ch) {
                        case '\u001a': // ignore end of file 
                        case '\r': // ignore CR
                        	break;
                        case '\n': // record end in MS-DOS and Unix 
							evaluateTeXTag();
                        	fireEmptyElement(NEWLINE_TAG);
							state = IN_TEXT;
							// ignore that newline
                        	break;
						case ' ':
							evaluateTeXTag();
							state = IN_TEXT;
							// ignore that space
							break;
						default:
							if (Character.isDigit (ch)) {
								texNumber.append(ch);
							} else {
								evaluateTeXTag();
								state = IN_TEXT;
								readOff = false;
							}
							break;
					} // switch ch
                    break; // IN_NUMBER
                    
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
        charBuffer = new char[MAX_BUF];
        content = new StringBuffer(MAX_BUF);
        texTag  = new StringBuffer(32);
		withStar  = false;
		texSign   = "";
		texNumber = new StringBuffer(16);
		groupLevel = 0;
        // element = new StringBuffer(64);
        putEntityReplacements();
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            state = IN_TEXT; 
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
    
    /** whether a space must be inserted after an RTF tag */
    private boolean insertSpace;

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        saxBuffer = new StringBuffer(MAX_BUF); // a rather long line
        elem = "";
        insertSpace = false;
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
        } else if (qName.equals(ROOT_TAG		)) { 
        	insertSpace = false;
        } else if (qName.equals(NEWLINE_TAG		)) { 
        	charWriter.println();
        	insertSpace = false;
        } else if (qName.equals(HARD_SPACE_TAG	)) { 
        	charWriter.print("\\~");
        	insertSpace = false;
        } else if (qName.equals(HARD_HYPHEN_TAG	)) { 
        	charWriter.print("\\_");
        	insertSpace = false;
        } else if (qName.equals(SOFT_HYPHEN_TAG	)) { 
        	charWriter.print("\\-");
        	insertSpace = false;
        } else if (qName.equals(GROUP_TAG		)) { 
        	charWriter.print("{");
        	insertSpace = false;
        } else { // some texTag
        	insertSpace = true;
        	String star = attrs.getValue("s");
        	if (star != null) { // indicate that the texTag was an extension
        		charWriter.print("\\*");
        	}
			charWriter.print("\\" + qName);
        	String value = attrs.getValue("n");
        	if (value != null) { // append the numerical value, perhaps with "-" sign
        		charWriter.print(value);
        	}
        } // some texTag
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
        } else if (qName.equals(ROOT_TAG		)) { 
        	insertSpace = false;
        } else if (qName.equals(GROUP_TAG		)) { 
        	charWriter.print("}");
        	insertSpace = false;
        } else { 
        	// all other elements are empty - ignore their end tags
        }
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int len) {
    	if (true) {
            saxBuffer.setLength(0);
            int pos = 0;
            while (pos < len) {
            	char chx = ch[start + pos];
            	if (chx == '\n') {
            		// ignore
            	} else if (chx > '\u007f') {
					saxBuffer.append("\\'");
					saxBuffer.append((Integer.toHexString(0x100 + chx)).substring(1));
            	} else {
            		saxBuffer.append(chx);
            	}
            	pos ++;
            } // while pos
    
            if (saxBuffer.length() > 0) {
	    		if (insertSpace) {
    				charWriter.print(" ");
    			}
   				insertSpace = false;
	            charWriter.print(saxBuffer.toString());
	        } // no newlines
        } // else ignore characters in unknown elements
    } // characters
    
} // T