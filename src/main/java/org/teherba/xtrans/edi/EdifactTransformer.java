/*  Transforms UN/Edifact messages
    @(#) $Id: EdifactTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2010-06-23: initialize -> setEditSeparators
    2008-07-07, Dr. Georg Fischer: copied from SWIFTTransformer
    caution, encoded in UTF-8: äöüÄÖÜß
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

package org.teherba.xtrans.edi;
import  org.teherba.xtrans.CharTransformer;
import  java.io.BufferedReader;
import	java.util.ArrayList;
import  java.util.Stack;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Transformer for UN/Edifact messages as described in
 *  ISO 9735 Electronic data interchange for administration, commerce and transport (EDIFACT).
 *	This class only handles the raw syntax of UN Edifact messages and the separator 
 *	characters. The higher level structures of groups, messages and all code lists
 *	must be treated separately.
 *	<p>
 *	Segments are transformed to elements with the 3-character (uppercase) name as XML tag,
 *	and they start on a new line.
 *	All other elements have lowercase XML tags. 
 *	Data elements are converted to numbered &lt;d<em>i</em>&gt; elements.
 *	Component data elements also become numbered &lt;c<em>i</em>&gt;, <em>i</em>=1,2,3... .
 *	<p>
 *	Whitespace characters (new lines) are normally not used in Edifact. 
 *	If present, they are represented by an empty &lt;n/&gt; element.
 *	<p>
 *  Example (from FINSTA D.96A dl_tkicch_finsta131.pdf, Swiss Interbank Clearing):
 *  <pre>
UNA:+.? '
UNH+12345+FINSTA:D:96A:UN' Message No. 12345
BGM+54+4711+9' Legal statement No. 4711
DTM+137:19961010:102' Date of message 10.10.1996
LIN+1' Start of first B-Level
FII+AS+6789-987654.32B+BANKCHZZXXX:25:5' Account owners account number and bank identification
RFF+ADP:CH-4712/1996' Bank reference: Reference number / Year
MOA+315:12000:CHF' Opening balance of the account : 12’000 CHF
DTM+171:19961001:102' Opening balance date 01.10.1996
MOA+343:14500:CHF' Closing balance of the account : 14’500 CHF
DTM+171:19961009:102' Closing balance date 09.10.1996
MOA+344:14000:CHF' Value date balance 14’000 CHF
DTM+171:19961003:102' Value date balance date 03.10.1996
MOA+344:15000:CHF' Value date balance 15’000 CHF
DTM+171:19961004:102' Value date balance date 04.10.1996
MOA+344:14500:CHF' Value date balance 14’500 CHF
DTM+171:19961007:102' Value date balance date 07.10.1996
SEQ+13+1' Start of first C-Level, reporting item details separately (transaction details not included).
RFF+PQ:3456' Customer reference number "3456“ assigned to a payment.
RFF+AIK:98762' Bank’s individual transaction reference number "98762“
DTM+209:19961003:102' Value date 03.10.1996
BUS++DO+1+ZZZ' Domestic clean payment
MOA+348:2000:CHF::4' Booked credit amount on the account equals 2’000 CHF
SEQ+13+2' Start of second C-Level, reporting item details separately (transaction details not included).
RFF+PQ:54321' Customer reference number „54321“ assigned to a payment.
RFF+AIK:987644' Bank’s individual transaction reference number "98744“
DTM+209:19961004:102' Value date 04.10.1996
BUS++DO+1+ZZZ' Domestic clean payment
MOA+348:1000:CHF::4' Booked credit amount on the account equals 1’000 CHF
SEQ+11+3' Start of third C-Level, reporting item details included (transaction details included).
RFF+AEK:2000' Payment order number "2000“
RFF+AIK:98755' Bank’s individual transaction reference number "98755“
DTM+209:19961007:102' Value date 07.10.1996
BUS++DO+1+ZZZ' Domestic clean payment
MOA+348:-500:CHF::4' Booked debit amount on the account equals -500 CHF
FTX+ADS+++VORSCHUSS: MONAT 10.1999' Information to account owner
UNT+36+12345' Message No. 12345 with 35 segments*  
</pre>
 *  @author Dr. Georg Fischer
 */
public class EdifactTransformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: EdifactTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    /** whether to generate values for ISO 20022 */
    protected boolean toISO20022;
        
    /** No-args constructor
     */
    public EdifactTransformer() {
        super();
        setFormatCodes("edifact");
        setDescription("UN/Edifact message interchange");
    } // Constructor
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log 		= Logger.getLogger(EdifactTransformer.class.getName());
	} // initialize
	
    /** Root element tag */
    protected static final String ROOT_TAG 				= "interchange";
    /** XML tag for segment elements */
    protected static final String SEPARATORS_ATTR 		= "sep";
    /** XML tag for data element elements */
    protected static final String DATA_TAG				= "d";
    /** XML tag for component data element elements */
    protected static final String COMPONENT_DATA_TAG	= "c";
    /** XML tag for repetition data element elements */
    protected static final String REPETITION_TAG		= "r";
    /** XML tag for explicit new line */
    protected static final String NEWLINE_TAG 			= "n";

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        toISO20022  = ! getOption("iso", "false").startsWith("f");
        try {
            fireStartDocument();
            fireStartRoot   (ROOT_TAG);
            fireLineBreak   ();
            String line;
            BufferedReader buffReader = new BufferedReader(charReader);
            setEdiSeparators();
	        state   			= IN_START;
	        content 			= new StringBuffer(296);
	        stack   			= new Stack/*<1.5*/<Integer>/*1.5>*/();
			segmentTag 			= "";
			dataIndex 			= 0;
			componentDataIndex 	= 0;
			int lineNo = 0;
            while ((line = buffReader.readLine()) != null) {
            	if (lineNo > 0) {
            		fireEmptyElement(NEWLINE_TAG);
            	}
            	lineNo ++;
                parseEdifactLine(line);
            } // while line
            terminate();
            fireEndElement	(ROOT_TAG);
            fireLineBreak   ();
            fireEndDocument ();
            fireLineBreak   ();
            buffReader.close();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return  result;
    } // generate

    /*================*/
    /* Edifact parser */
    /*================*/

    /** Stack for parser states and data element indices */
    private Stack/*<1.5*/<Integer>/*1.5>*/ stack = new Stack/*<1.5*/<Integer>/*1.5>*/();
    /** Buffer for one field currently parsed */
    private StringBuffer content;
    
	/** separator for component data elements, usually ":" */
	protected char componentDataElementSeparator;
	/** separator for data elements, usually "+" */
	protected char dataElementSeparator;
	/** decimal point or comma */
	protected char decimalNotation;
	/** release indicator (escape character), usually "?" */
	protected char releaseIndicator;
	/** reserved for future use, usually " " */
	protected char repetitionSeparator;
	/** segment terminator, usually "\'" */
	protected char segmentTerminator;
	
    /** Current state of the parser */
    private int state;
    /** current segment tag */
    protected String segmentTag;
    /** current index for data elements */
    protected int dataIndex;
    /** current index for component data elements */
    protected int componentDataIndex;
	
    /** Initializes the parser, sets the format separtors
     */
    protected void setEdiSeparators() {
		dataElementSeparator= '+';
		componentDataElementSeparator= ':';
		releaseIndicator    = '?';
		repetitionSeparator = ' ';
		decimalNotation 	= '.';
		segmentTerminator   = '\'';
    } // setEdiSeparators
    
    /** Parser states enumeration */
    private static final int IN_START      				= 0;
    private static final int IN_SEGMENT_HEAD			= 1;
    private static final int IN_RELEASE					= 2;
    private static final int IN_DATA_ELEMENT    		= 3;

    /**	Parses a line with Edifact segments, data elements and component data elements, and
     *  emits XML elements as necessary
     *  @param line source line
     */
    private void parseEdifactLine(String line) {
        int pos = 0;
        while (pos < line.length()) {
            char ch = line.charAt(pos ++);
            // fireCharacters("[" + stateDebug[state] + "]");
        
            switch (state) {
                
				case IN_START: // assume that the entire UNA segment fits into 'line'
					pos --; // was already consumed by 'ch'
					if (line.length() >= 9 && line.substring(pos, pos + 3).equals("UNA")) {
						// UNA:+.? '
						pos += 3;
						componentDataElementSeparator	= line.charAt(pos ++);
						dataElementSeparator 			= line.charAt(pos ++);
						decimalNotation		 			= line.charAt(pos ++);
						releaseIndicator	 			= line.charAt(pos ++);
						repetitionSeparator 			= line.charAt(pos ++);
						segmentTerminator	 			= line.charAt(pos ++);
						fireEmptyElement(line.substring(pos, pos + 3), 
								toAttribute(SEPARATORS_ATTR, replaceInSource(line.substring(pos + 3, pos + 9))));
						fireLineBreak();
					} else { // no UNA, probably ISA
						// start with segment tag below
					} // ISA
					// System.out.println("segment terminator: " + segmentTerminator);
					content.setLength(0);
					state = IN_SEGMENT_HEAD;
					break; // IN_START

				case IN_SEGMENT_HEAD: // behind segment terminator
					if (false) {
					} else if (Character.isLetterOrDigit(ch)) {
						append(ch); // letter in segment tag
					} else if (ch == segmentTerminator) {
						fireEmptyElement(content.toString());
						fireLineBreak();
						content.setLength(0);
						// remain in same state
					} else if (ch == dataElementSeparator) {
						segmentTag = content.toString();
						content.setLength(0);
						pushXML(segmentTag);
						dataIndex = 0;
						componentDataIndex = 0;
						pushXML(DATA_TAG + Integer.toString(++ dataIndex));
						state = IN_DATA_ELEMENT;
					} else if (ch == componentDataElementSeparator) {
						// should not occur
						segmentTag = content.toString();
						content.setLength(0);
						pushXML(segmentTag);
						dataIndex = 0;
						componentDataIndex = 0;
						pushXML(DATA_TAG + Integer.toString(++ dataIndex));
						fireEmptyElement(COMPONENT_DATA_TAG + Integer.toString(++ componentDataIndex));						
						state = IN_DATA_ELEMENT;
					} else if (ch == releaseIndicator) {
						stack.push(new Integer(state));
						state = IN_RELEASE;
					} else if (ch == decimalNotation) {
						if (toISO20022) {
							append('.');
						} else {
							append(ch);
						}
				/*
		            } else if (ch == repetitionSeparator) {
						// ignore
				*/
					} else { 
						// should not occur
						// ignore, remain in same state
						append(ch); // other character in segment tag
					}
					break; // IN_SEGMENT_HEAD
					
				case IN_RELEASE:
					append(ch);
					if (stack.size() <= 0) {
						log.error("stack underflow");
					} else {
						state = ((Integer) stack.pop()).intValue();
					}
					break; // IN_RELEASE
					
				case IN_DATA_ELEMENT:
					if (false) {
					} else if (ch == segmentTerminator) {
						closeDataElement();
						popXML(segmentTag);
						fireLineBreak();
						state = IN_SEGMENT_HEAD;
					} else if (ch == dataElementSeparator) {
						closeDataElement();
						pushXML(DATA_TAG + Integer.toString(++ dataIndex));
						// state = IN_DATA_ELEMENT;
					} else if (ch == componentDataElementSeparator) {
						pushXML(COMPONENT_DATA_TAG + Integer.toString(++ componentDataIndex));
							fireCharacters(content.toString());
							content.setLength(0);
						popXML();
						// state = IN_COMPONENT_DATA_ELEMENT;
					} else if (ch == releaseIndicator) {
						stack.push(new Integer(state));
						state = IN_RELEASE;
					} else if (ch == decimalNotation) {
						if (toISO20022) {
							append('.');
						} else {
							append(ch);
						}
				/*
		            } else if (ch == repetitionSeparator) {
						// ignore
				*/
					} else { 
						append(ch);
					}
					break; // IN_DATA_ELEMENT

                default:
                    log.error("invalid state " + state);
                    break;
            } // switch state
        } // while consuming characters 

    } // parseEdifactLine
    
    /** Closes a data element
     */
    protected void closeDataElement() {
    	if (componentDataIndex > 0) {
			pushXML(COMPONENT_DATA_TAG + Integer.toString(++ componentDataIndex));
				fireCharacters(content.toString());
			popXML();
		} else {
			fireCharacters(content.toString());
		}
		componentDataIndex = 0;
		content.setLength(0);
		popXML(); // di 
    } // closeDataElement
    
    /**	Pseudo-abstract method: terminates the parsing process 
     *	and emits all remaining XML elements
     */
    protected void terminate() {
    } // terminate
    
	/** Generates an element from an MT amount 
	 *	subfield with the resulting value in MT or MX representation.
	 *	@param value amount with decimal comma, and 0/1/n digits behind the comma
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
	 *	subfield with the resulting value in MT or MX representation.
	 *	@param value "C" or "D"
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
	 *	The century (19 or 20) is guessed from the year with changeover at 1980.
	 *	subfield with the resulting value in MT or MX representation.
	 *	@param value date of the form YYMMDD
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
	
	/** Generates an element from an MT timestamp (:13D:) of the form YYMMDDhh:mm+hhmm.
	 *	subfield with the resulting value in MT or MX representation.
	 *	The century (19 or 20) is guessed from the year with changeover at 1980.
	 *	@param value timestamp of the form 07062905:30+0200
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

    /** Pseudo-abstract method for further substructuring of a field
     *	in a derived class, with an array of continuation lines
     *  @param type type of the Edifact FIN message, e.g. 940
     *	@param dir message transfer direction as seen from Edifact: "I" or "O"
     *	@param tag field designator, e.g. "B3", "F20"
     *	@param lines content of the field to be substructured, 
     *	with continuation lines in following array elements
     *	@return unchanged value or the empty string if substructure was already emitted
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
        
    /*=====================*/
    /* Edifact SAX Handler */
    /*=====================*/

    /** buffer for the assembly of a Base64 string */
    private StringBuffer saxBuffer;
    /** Inhibits the output of character content inside a VOID element. */
    protected boolean inhibitChars;
    
	/** separator for component data elements, usually ":" */
	protected char saxComponentDataElementSeparator;
	/** separator for data elements, usually "+" */
	protected char saxDataElementSeparator;
	/** decimal point or comma */
	protected char saxDecimalNotation;
	/** release indicator (escape character), usually "?" */
	protected char saxReleaseIndicator;
	/** reserved for future use, usually " " */
	protected char saxRepetitionSeparator;
	/** segment terminator, usually "\'" */
	protected char saxSegmentTerminator;
	
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
    	inhibitChars = true;
		saxComponentDataElementSeparator	= ':';
		saxDataElementSeparator 			= '+';
		saxDecimalNotation		 			= '.';
		saxReleaseIndicator	 				= '?';
		saxRepetitionSeparator 				= ' ';
		saxSegmentTerminator	 			= '\'';
    } // startDocument
    
    /** Receive notification of the end of the document.
     */
    public void endDocument() {
    	charWriter.println();
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
        } else if (qName.compareTo("aaa") < 0) { // starts with uppercase - is a segment tag
        	charWriter.print(qName);
        	if (qName.equals("UNA")) {
        		String seps = attrs.getValue(SEPARATORS_ATTR);
        		int pos = 0;
				saxComponentDataElementSeparator	= seps.charAt(pos ++);
				saxDataElementSeparator 			= seps.charAt(pos ++);
				saxDecimalNotation		 			= seps.charAt(pos ++);
				saxReleaseIndicator	 				= seps.charAt(pos ++);
				saxRepetitionSeparator 				= seps.charAt(pos ++);
				saxSegmentTerminator	 			= seps.charAt(pos ++);
        		charWriter.print(seps.substring(0, pos - 1)); // not the last - this is written for </UNA>
        	} // UNA
        } else if (qName.startsWith(DATA_TAG) && qName.substring(DATA_TAG.length()).matches("\\d+")) {
        	inhibitChars = false;
			charWriter.print(saxDataElementSeparator);
        } else if (qName.startsWith(COMPONENT_DATA_TAG) && qName.substring(COMPONENT_DATA_TAG.length()).matches("\\d+")) {
        	inhibitChars = false;
			if (! qName.equals(COMPONENT_DATA_TAG + "1")) {
				charWriter.print(saxComponentDataElementSeparator);
			}
        } else if (qName.equals(NEWLINE_TAG	)) {
        	charWriter.println();
        } else if (qName.equals(ROOT_TAG	)) {
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
        } else if (qName.compareTo("aaa") < 0) { // starts with uppercase - is a segment tag
        	charWriter.print(saxSegmentTerminator);
        //	charWriter.println();
        } else if (qName.startsWith(DATA_TAG) && qName.substring(DATA_TAG.length()).matches("\\d+")) {
        	inhibitChars = true;
        } else if (qName.startsWith(COMPONENT_DATA_TAG) && qName.substring(COMPONENT_DATA_TAG.length()).matches("\\d+")) {
        	inhibitChars = true;
        } else if (qName.equals(NEWLINE_TAG	)) {
			// ignore
        } else if (qName.equals(ROOT_TAG	)) {
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
    		charWriter.print(new String(ch, start, length));
    	}
    } // characters

} // EdifactTransformer
