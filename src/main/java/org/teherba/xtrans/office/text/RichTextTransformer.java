/*  Microsoft's Rich Text Format (RTF) used in Word etc.
    @(#) $Id: RichTextTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2007-10-16, Dr. Georg Fischer
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

package org.teherba.xtrans.office.text;
import  org.teherba.xtrans.CharTransformer;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/**
 *  Transformer for Microsoft's Rich Text Format (RTF) used in Word et al.
 *  Examples:
<pre>
{\\rtf1\\ansi
\\*\\do\\dobxcolumn\\dobypara\\dodhgt8194
\\dprect\\dpx599\\dpy167\\dpxsize1728\\dpysize576
\\dpfillfgcr255\\dpfillfgcg255\\dpfillfgcb255
\\dpfillbgcr255\\dpfillbgcg255\\dpfillbgcb255
\\dpfillpat1\\dplinew15\\dplinecor0\\dplinecog0\\dplinecob0
}

{\\rtf1\\ansi\\ansicpg1252\\uc1 \\deff0\\deflang1033\\deflangfe1031{\\fonttbl
{\\f0\\froman\\fcharset0\\fprq2{\\*\\panose 02020603050405020304}Times New Roman;}
{\\f1\\fswiss\\fcharset0\\fprq2{\\*\\panose 020b0604020202020204}Arial;}
{\\f3\\froman\\fcharset2\\fprq2{\\*\\panose 05050102010706020507}Symbol;}
{\\f16\\froman\\fcharset238\\fprq2 Times New Roman CE;}
{\\f17\\froman\\fcharset204\\fprq2 Times New Roman Cyr;}
{\\f19\\froman\\fcharset161\\fprq2 Times New Roman Greek;}
{\\f20\\froman\\fcharset162\\fprq2 Times New Roman Tur;}
{\\f21\\froman\\fcharset186\\fprq2 Times New Roman Baltic;}{
\\f22\\fswiss\\fcharset238\\fprq2 Arial CE;}
{\\f23\\fswiss\\fcharset204\\fprq2 Arial Cyr;}
{\\f25\\fswiss\\fcharset161\\fprq2 Arial Greek;}
{\\f26\\fswiss\\fcharset162\\fprq2 Arial Tur;}
{\\f27\\fswiss\\fcharset186\\fprq2 Arial Baltic;}}
{\\colortbl;
\\red0\\green0\\blue0;
\\red0\\green0\\blue255;
\\red0\\green255\\blue255;
\\red0\\green255\\blue0;
\\red255\\green0\\blue255;
\\red255\\green0\\blue0;
\\red255\\green255\\blue0;
\\red255\\green255\\blue255;
\\red0\\green0\\blue128;
\\red0\\green128\\blue128;
\\red0\\green128\\blue0;
\\red128\\green0\\blue128;
\\red128\\green0\\blue0;
\\red128\\green128\\blue0;
\\red128\\green128\\blue128;
\\red192\\green192\\blue192;}{\\stylesheet{
\\nowidctlpar\\widctlpar\\adjustright \\f1\\lang1031\\cgrid \\snext0 Normal;}{\\*\\cs10 \\additive Default Paragraph Font;}}{\\*\\listtable{\\list\\listtemplateid-1\\listsimple{\\listlevel\\levelnfc0\\leveljc0\\levelfollow0\\levelstartat0\\levelspace0\\levelindent0{\\leveltext
\\'01*;}{\\levelnumbers;}}{\\listname ;}\\listid-2}}{\\*\\listoverridetable{\\listoverride\\listid-2\\listoverridecount1{\\lfolevel\\listoverrideformat{\\listlevel\\levelnfc23\\leveljc0\\levelfollow0\\levelstartat1\\levelold\\levelspace0\\levelindent283{\\leveltext
\\'01\\u-3913 ?;}{\\levelnumbers;}\\f3\\fbias0 \\fi-283\\li283 }}\\ls1}}{\\info{\\title WinWord kennt folgende Zeichenattribute:}{\\author Dr. Georg Fischer}{\\operator Dr. Georg Fischer}{\\creatim\\yr1998\\mo2\\dy16\\hr21\\min25}{\\revtim\\yr1998\\mo2\\dy16\\hr21\\min25}
{\\version2}{\\edmins0}{\\nofpages1}{\\nofwords46}{\\nofchars266}{\\*\\company punctum-transfer}{\\nofcharsws326}{\\vern73}}\\paperw11906\\paperh16838\\margl1417\\margr1417\\margt1417\\margb1134 
\\deftab708\\widowctrl\\ftnbj\\aenddoc\\hyphhotz425\\formshade\\viewkind1\\viewscale79\\viewzk2\\pgbrdrhead\\pgbrdrfoot \\fet0\\sectd \\linex0\\headery709\\footery709\\colsx709\\endnhere\\sectdefaultcl {\\*\\pnseclvl1\\pnucrm\\pnstart1\\pnindent720\\pnhang{\\pntxta .}}
{\\*\\pnseclvl2\\pnucltr\\pnstart1\\pnindent720\\pnhang{\\pntxta .}}{\\*\\pnseclvl3\\pndec\\pnstart1\\pnindent720\\pnhang{\\pntxta .}}{\\*\\pnseclvl4\\pnlcltr\\pnstart1\\pnindent720\\pnhang{\\pntxta )}}{\\*\\pnseclvl5\\pndec\\pnstart1\\pnindent720\\pnhang{\\pntxtb (}{\\pntxta )}}
{\\*\\pnseclvl6\\pnlcltr\\pnstart1\\pnindent720\\pnhang{\\pntxtb (}{\\pntxta )}}{\\*\\pnseclvl7\\pnlcrm\\pnstart1\\pnindent720\\pnhang{\\pntxtb (}{\\pntxta )}}{\\*\\pnseclvl8\\pnlcltr\\pnstart1\\pnindent720\\pnhang{\\pntxtb (}{\\pntxta )}}{\\*\\pnseclvl9
\\pnlcrm\\pnstart1\\pnindent720\\pnhang{\\pntxtb (}{\\pntxta )}}\\pard\\plain \\nowidctlpar\\widctlpar\\adjustright \\f1\\lang1031\\cgrid {WinWord kennt folgende Zeichenattribute:
\\par {\\pntext\\pard\\plain\\f3\\cgrid \\loch\\af3\\dbch\\af0\\hich\\f3 \\'b7\\tab}}\\pard \\fi-283\\li283\\nowidctlpar\\widctlpar{\\*\\pn \\pnlvlblt\\ilvl0\\ls1\\pnrnot0\\pnf3\\pnstart1\\pnindent283\\pnhang{\\pntxtb \\'b7}}\\ls1\\adjustright {\\b Fett}{druck 
\\par {\\pntext\\pard\\plain\\f3\\cgrid \\loch\\af3\\dbch\\af0\\hich\\f3 \\'b7\\tab}}\\pard \\fi-283\\li283\\nowidctlpar\\widctlpar{\\*\\pn \\pnlvlblt\\ilvl0\\ls1\\pnrnot0\\pnf3\\pnstart1\\pnindent283\\pnhang{\\pntxtb \\'b7}}\\ls1\\adjustright {\\i kursive}{ Schrift (italics)
\\par {\\pntext\\pard\\plain\\f3\\cgrid \\loch\\af3\\dbch\\af0\\hich\\f3 \\'b7\\tab}}\\pard \\fi-283\\li283\\nowidctlpar\\widctlpar{\\*\\pn \\pnlvlblt\\ilvl0\\ls1\\pnrnot0\\pnf3\\pnstart1\\pnindent283\\pnhang{\\pntxtb \\'b7}}\\ls1\\adjustright {\\ul Unter}{streichung
\\par {\\pntext\\pard\\plain\\strike\\f3\\cgrid \\loch\\af3\\dbch\\af0\\hich\\f3 \\'b7\\tab}}\\pard \\fi-283\\li283\\nowidctlpar\\widctlpar{\\*\\pn \\pnlvlblt\\ilvl0\\ls1\\pnrnot0\\pnf3\\pnstart1\\pnindent283\\pnhang{\\pntxtb \\'b7}}\\ls1\\adjustright {\\strike durchgestrichen}{
\\par {\\pntext\\pard\\plain\\f3\\cgrid \\loch\\af3\\dbch\\af0\\hich\\f3 \\'b7\\tab}}\\pard \\fi-283\\li283\\nowidctlpar\\widctlpar{\\*\\pn \\pnlvlblt\\ilvl0\\ls1\\pnrnot0\\pnf3\\pnstart1\\pnindent283\\pnhang{\\pntxtb \\'b7}}\\ls1\\adjustright {Schriftart, z.B. Times New Roman
\\par {\\pntext\\pard\\plain\\f3\\cgrid \\loch\\af3\\dbch\\af0\\hich\\f3 \\'b7\\tab}}\\pard \\fi-283\\li283\\nowidctlpar\\widctlpar{\\*\\pn \\pnlvlblt\\ilvl0\\ls1\\pnrnot0\\pnf3\\pnstart1\\pnindent283\\pnhang{\\pntxtb \\'b7}}\\ls1\\adjustright {Schriftgr\\'f6\\'dfe, z.B. }{\\fs40 20 pt}{
\\par {\\pntext\\pard\\plain\\f3\\super\\cgrid \\loch\\af3\\dbch\\af0\\hich\\f3 \\'b7\\tab}}\\pard \\fi-283\\li283\\nowidctlpar\\widctlpar{\\*\\pn \\pnlvlblt\\ilvl0\\ls1\\pnrnot0\\pnf3\\pnstart1\\pnindent283\\pnhang{\\pntxtb \\'b7}}\\ls1\\adjustright {\\super hoch}{- und }{\\sub tief}{gestellt

\\par {\\pntext\\pard\\plain\\scaps\\f3\\cgrid \\loch\\af3\\dbch\\af0\\hich\\f3 \\'b7\\tab}}\\pard \\fi-283\\li283\\nowidctlpar\\widctlpar{\\*\\pn \\pnlvlblt\\ilvl0\\ls1\\pnrnot0\\pnf3\\pnstart1\\pnindent283\\pnhang{\\pntxtb \\'b7}}\\ls1\\adjustright {\\scaps Kapit\\'e4lchen}{
\\par {\\pntext\\pard\\plain\\caps\\f3\\cgrid \\loch\\af3\\dbch\\af0\\hich\\f3 \\'b7\\tab}}\\pard \\fi-283\\li283\\nowidctlpar\\widctlpar{\\*\\pn \\pnlvlblt\\ilvl0\\ls1\\pnrnot0\\pnf3\\pnstart1\\pnindent283\\pnhang{\\pntxtb \\'b7}}\\ls1\\adjustright {\\caps Gro\\'dfschreibung}{
\\par {\\pntext\\pard\\plain\\f3\\cgrid \\loch\\af3\\dbch\\af0\\hich\\f3 \\'b7\\tab}}\\pard \\fi-283\\li283\\nowidctlpar\\widctlpar{\\*\\pn \\pnlvlblt\\ilvl0\\ls1\\pnrnot0\\pnf3\\pnstart1\\pnindent283\\pnhang{\\pntxtb \\'b7}}\\ls1\\adjustright {verborgenen Text (Kommentar, der auf d
em Bildschirm, aber nicht im Ausdruck erscheint)
\\par {\\pntext\\pard\\plain\\f3\\cgrid \\loch\\af3\\dbch\\af0\\hich\\f3 \\'b7\\tab}}\\pard \\fi-283\\li283\\nowidctlpar\\widctlpar{\\*\\pn \\pnlvlblt\\ilvl0\\ls1\\pnrnot0\\pnf3\\pnstart1\\pnindent283\\pnhang{\\pntxtb \\'b7}}\\ls1\\adjustright {Farbe, z.B. }
{\\cf6 rot}{
\\par }\\pard \\nowidctlpar\\widctlpar\\adjustright {
\\par 
\\par }}
</pre>
 *  @author Dr. Georg Fischer
 */
public class RichTextTransformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: RichTextTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** Upper bound for character buffer */
    private static final int MAX_BUF = 4096;

    /** Root element tag */
    private static final String ROOT_TAG    	= "RichTextFormat";
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
    private StringBuffer rtfTag;
    /** numerical value of current RTF tag */
    private StringBuffer rtfNumber;
    /** sign of numerical value of current RTF tag */
    private String rtfSign;
    /** 2 hexadecimal digits behind \\' */
    private int hexCode;
    /** nesting level of groups, {\\rtf1 = 1 */
    private int groupLevel;
    
    /** No-args Constructor.
     */
    public RichTextTransformer() {
        super();
        setFormatCodes		("rtf");
        setDescription		("RTF - Rich Text Format for MS-Word et al.");
        setFileExtensions	("rtf");
        setMimeType			("text/rtf");
    } // Constructor
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = LogManager.getLogger(RichTextTransformer.class.getName());
	} // initialize

    /** Emits document text, and writes its characters
     */
    private void evaluateContent() {
    	fireCharacters(content.toString());
    	content.setLength(0);
	} // evaluateContent
	
    /** Starts a group
     */
    private void startRTFGroup() {
		evaluateContent();
    	groupLevel ++;
		fireStartElement(GROUP_TAG);
	} // startRTFGroup
	
    /** Ends   a group
     */
    private void   endRTFGroup() {
		evaluateContent();
		fireEndElement  (GROUP_TAG);
		fireLineBreak();
    	groupLevel --;
	} //   endRTFGroup
	
    /** Evaluates an RTF tag, and writes an empty element for it
     */
    private void evaluateRTFTag() {
    	String value = rtfSign.toString() + rtfNumber.toString();
    	if (value.length() == 0) {
    		if (withStar) {
	    		fireEmptyElement(rtfTag.toString(), toAttribute("s", "*"));
	    	} else {
	    		fireEmptyElement(rtfTag.toString());
			}
    	} else { // with value
    		if (withStar) {
	    		fireEmptyElement(rtfTag.toString(), toAttributes(new String[] 
	    				{ "s", "*"
	    				, "n", value
	    				}));
	    	} else {
	    		fireEmptyElement(rtfTag.toString(), toAttribute("n", value));
			}
    	}
    	withStar = false;
    	rtfTag.setLength(0);
    	rtfNumber.setLength(0);
    	rtfSign = "";
	} // evaluateRTFTag
	
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
                        	startRTFGroup();
                        	break;
                        case '}': // end   of a substructure
                        	endRTFGroup();
                        	break;
						case '\\': 
							state = IN_BACKSLASH;
							rtfTag.setLength(0);
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
                            	rtfTag.append(ch);
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
							evaluateRTFTag();
                        	fireEmptyElement(NEWLINE_TAG);
							state = IN_TEXT;
							// ignore that newline
                        	break;
						case '-':
							state = IN_NUMBER;
							rtfSign = "-";
							rtfNumber.setLength(0);
							break;
						case ' ':
							evaluateRTFTag();
							state = IN_TEXT;
							// ignore that space
							break;
						default:
							if (false) {
							} else if (Character.isLetter(ch)) {
								rtfTag.append(ch);
							} else if (Character.isDigit (ch)) {
								state = IN_NUMBER;
								rtfSign = "";
								rtfNumber.setLength(0);
								rtfNumber.append(ch);
							} else {
								evaluateRTFTag();
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
							evaluateRTFTag();
                        	fireEmptyElement(NEWLINE_TAG);
							state = IN_TEXT;
							// ignore that newline
                        	break;
						case ' ':
							evaluateRTFTag();
							state = IN_TEXT;
							// ignore that space
							break;
						default:
							if (Character.isDigit (ch)) {
								rtfNumber.append(ch);
							} else {
								evaluateRTFTag();
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
        rtfTag  = new StringBuffer(32);
		withStar  = false;
		rtfSign   = "";
		rtfNumber = new StringBuffer(16);
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
        } else { // some rtfTag
        	insertSpace = true;
        	String star = attrs.getValue("s");
        	if (star != null) { // indicate that the rtfTag was an extension
        		charWriter.print("\\*");
        	}
			charWriter.print("\\" + qName);
        	String value = attrs.getValue("n");
        	if (value != null) { // append the numerical value, perhaps with "-" sign
        		charWriter.print(value);
        	}
        } // some rtfTag
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
    
} // RichTextTransformer
