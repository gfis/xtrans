/*  Transforms a reduced/compressed infoSet representation to/from XML. 
    Caution, this file is UTF-8 encoded: äöüÄÖÜß
    @(#) $Id: SiXMLTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2007-09-05, Dr. Georg Fischer
    
    Caution, not yet tested or working at all!
    c.f. <a href="http://www.cs.le.ac.uk/SiXML/">University of Leicester</a>
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

package org.teherba.xtrans.general;
import  org.teherba.xtrans.CharTransformer;
import  java.io.BufferedReader;
import	java.util.Stack;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Transforms a simplified XML notation of an infoset to/from ordinary XML. 
 *  Ordinary XML can transparently be transformed into SiXML and vice versa. 
 *	Entities, CDATA, DTD declarations, namespaces and comments
 *	are not processed, however.
 *	The transformer maintains a stack of the element nesting structure, 
 *	and tries to avoid repetitive element names, attributes and values.
 *	<p>
 *	SiXML has the following shortcuts:
 *	<ol>
 *	<li>Attribute names can be omitted if the attribute was in the same position as in the previous,
 *	identically named element (="value" is kept).</li>
 *	<li>Attribute values can also be omitted if the attribute's value was the same as in the previous,
 *	identically named element (only a "=" is kept).</li>
 *	<li>Trailing "=" characters can be omitted.</li>
 *	<li>All end elements tag are replaced by "&lt;/&gt;"</li>
 *	<li>An identical (leaf) element value and the end element tag are replaced by "&gt;" only.</li>
 *	</ol>
 *	<p>
 *  @author Dr. Georg Fischer
 */
public class SiXMLTransformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: SiXMLTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** Non-args Constructor.
     */
    public SiXMLTransformer() {
        super();
        setFormatCodes("6ml");
        setDescription("Simplified XML Notation");
        setFileExtensions("6ml");
    } // constructor

	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(SiXMLTransformer.class.getName());
	} // initialize
	
    /** Root element tag */
    private static final String ROOT_TAG   	= "json";
    /** Object element tag - when key should follow */
    private static final String OBJECT_TAG	= "obj";
    /** Indicator on stack when key is already processed and a value should follow - fictious, not used for XML,
     *	stack is toggled between OBJECT1_TAG and OBJECT2_TAG
     */
    private static final String OBJECT1_TAG = "obj1";
    /** Indicator on stack when key is already processed and a value should follow - fictious, not used for XML,
     *	stack is toggled between OBJECT_TAG and OBJECT2_TAG
     */
    private static final String OBJECT2_TAG = "obj2";
    /** Array element tag */
    private static final String ARRAY_TAG   = "arr";
    /** Indicator on stack when 1st value in an array is already processed */
    private static final String ARRAY2_TAG  = "arr2";
    /** Key element tag */
    private static final String KEY_TAG     = "key";
    /** Value element tag */
    private static final String VALUE_TAG   = "val";
    /** String element tag */
    private static final String STRING_TAG  = "str";
    /** Numbere element tag */
    private static final String NUMBER_TAG  = "num";
    /** Null element tag */
    private static final String NULL_TAG    = "null";
    /** Boolean false element tag */
    private static final String FALSE_TAG   = "false";
    /** Boolean True element tag */
    private static final String TRUE_TAG    = "true";
    
    /** scanner state */
    private int state;
	/** Buffer for tokens: true, false, null, numbers */
	private StringBuffer buffer;
	/** Buffer for Unicode escapes */
	private StringBuffer unicode;
    /** values for <em>state</em> */ 
    private static final int IN_SKIP     = 1;
    private static final int IN_INIT     = 2; 
    private static final int IN_OBJECT   = 3;
    private static final int IN_STRING   = 4;
    private static final int IN_TOKEN    = 5;
    private static final int IN_BACKSLASH= 6;
    private static final int IN_UNICODE  = 7;

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            BufferedReader buffReader = new BufferedReader(charReader);
            String line;
            int count = 0;
			state = IN_SKIP;
			buffer  = new StringBuffer(296);
			int unicode = 0;
			int escapeLen = 0; // length of unicode escape hex string
			boolean readOff = true;
            while ((line = buffReader.readLine()) != null) {
				int linePos = 0;
				while (linePos < line.length()) {				
					char ch = line.charAt(linePos);
					readOff = true;
					switch (state) {
						case IN_SKIP:
							switch (ch) {
								case '\b':
								case '\f':
								case '\n':
								case '\r':
								case '\t':
								case ' ':
									// whitespace - continue skipping
									break;
								case '{':
									pushXML(OBJECT_TAG);
									break;
								case ':':
									tagStack.pop();
									tagStack.push(OBJECT2_TAG);
									break;
								case '}':
									if (topXML().equals(OBJECT2_TAG)) {
										tagStack.pop();
										tagStack.push(OBJECT_TAG);
									}
									popXML (OBJECT_TAG);
									break;
								case '[':
									pushXML(ARRAY_TAG);
									break;
								case ',':
									if (topXML().equals(OBJECT2_TAG)) {
										tagStack.pop();
										tagStack.push(OBJECT_TAG);
									}
									break;
								case ']':
									popXML (ARRAY_TAG);
									break;
								case '"':
									state = IN_STRING;
									buffer.setLength(0);
									if (topXML().equals(OBJECT_TAG)) {
										pushXML(KEY_TAG);
									} else { // OBJECT2_TAG or ARRAY_TAG
										pushXML(STRING_TAG);
									}
									break;
								default: // should be start of "true", "false" or "null", or of a number
									buffer.setLength(0);
									buffer.append(ch);
									state = IN_TOKEN;
									break;
							} // switch ch
							break; // IN_SKIP

						case IN_STRING:
							switch (ch) {
								case '"':
									fireCharacters(buffer.toString());
									state = IN_SKIP;
									popXML(KEY_TAG);
									popXML(STRING_TAG);
									break;
								case '\\':
									state = IN_BACKSLASH;
									break;
								case '<':
									if (getMimeType().equals("text/xml")) {
										buffer.append("&lt;");
									} else {
										buffer.append(ch);
									}
									break;
								case '>':
									if (getMimeType().equals("text/xml")) {
										buffer.append("&gt;");
									} else {
										buffer.append(ch);
									}
									break;
								case '&':
									if (getMimeType().equals("text/xml")) {
										buffer.append("&amp;");
									} else {
										buffer.append(ch);
									}
									break;
								case '\'':
									if (getMimeType().equals("text/xml")) {
										buffer.append("&apos;");
									} else {
										buffer.append(ch);
									}
									break;
								default: // any string character
									buffer.append(ch);
									break;
							} // switch ch
							break; // IN_STRING
							
						case IN_BACKSLASH:
							state = IN_STRING;
							switch (ch) {
								case '"':
									buffer.append(ch);
									break;
								case '\\':
									buffer.append(ch);
									break;
								case '/':
									buffer.append(ch);
									break;
								case 'b':
									buffer.append('\b');
									break;
								case 'f':
									buffer.append('\f');
									break;
								case 'n':
									buffer.append('\n');
									break;
								case 'r':
									buffer.append('\r');
									break;
								case 't':
									buffer.append('\t');
									break;
								case 'u':
									state = IN_UNICODE;
									unicode = 0;
									escapeLen = 0;
									break;
								default: // any other character is invalid
									// take it literally as if it were not escaped
									buffer.append(ch);
									break;
							} // switch ch
							break; // IN_BACKSLASH
							
						case IN_UNICODE:
							escapeLen ++;
							if (escapeLen > 4) {
								state = IN_STRING;
								readOff = false;
								buffer.append((char) (unicode & 0xffff));
							} else if (ch >= '0' && ch <= '9') {
								unicode = unicode * 16 + (ch - '0');
							} else if (ch >= 'A' && ch <= 'F') {
								unicode = unicode * 16 + (ch - 'A' + 10);
							} else if (ch >= 'a' && ch <= 'f') {
								unicode = unicode * 16 + (ch - 'a' + 10);
							} else {
								state = IN_STRING;
								readOff = false;
								buffer.append((char) (unicode & 0xffff));
							}
							break; // IN_UNICODE
							
						case IN_TOKEN:
							switch (ch) {
								case '\b':
								case '\f':
								case '\n':
								case '\r':
								case ' ':
								case ']':
								case '}':
								case ',':
								case ':':
									String value = buffer.toString();
									if (false) {
									} else if (value.equals("true")) {
										fireEmptyElement(TRUE_TAG);
									} else if (value.equals("false")) {
										fireEmptyElement(FALSE_TAG);
									} else if (value.equals("null")) {
										fireEmptyElement(NULL_TAG);
									} else { // NUMBER
										pushXML(NUMBER_TAG);
										fireCharacters(value);
										popXML (NUMBER_TAG);
									}
									readOff = false;
									state = IN_SKIP;
									break;
								default: // continuation of "true", "false" or "null", or of a number
									if (ch >= '+' && ch <= 'z') {
										buffer.append(ch);
									} else { // should not occur
										// ignore silently
									}
									break;
							} // switch ch
							break; // IN_STRING
							
						default:
							log.error("invalid state " + state);
							break;
					} // switch state
					if (readOff) {
						linePos ++;
					}
				} // while linePos
            	count ++;
            } // while not EOF
            buffReader.close();
            fireEndElement(ROOT_TAG);
            fireLineBreak();
            fireEndDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return  result;
    }

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** Stack for elements of the language: objects, keys, values, arrays ... */
    private Stack/*<1.5*/<String>/*1.5>*/ saxStack;
    /** Buffer for values */
    private StringBuffer valueBuffer;
	/** current topmost element */
	private String elem;    
	
    /** Writes the value buffer with proper character escape sequences
     */
    public void writeEscapedString() {
       	charWriter.print  ("\"");
		int pos = 0;
		while (pos < valueBuffer.length()) {
			char ch = valueBuffer.charAt(pos ++);
			switch (ch) {
				case '\b':
					charWriter.print("\\b");
					break;
				case '\f':
					charWriter.print("\\f");
					break;
				case '\n':
					charWriter.print("\\n");
					break;
				case '\r':
					charWriter.print("\\r");
					break;
				case '\t':
					charWriter.print("\\t");
					break;
				case '\\':
					charWriter.print("\\\\");
					break;
				case '/':
					charWriter.print("\\/");
					break;
				case '"':
					charWriter.print("\\\"");
					break;
				case '\u00a0':
					charWriter.print("\\u00a0");
					break;
				default:
					if (ch < 256) {
						charWriter.print(ch);
					} else {
						String hex = Integer.toHexString(ch);
						charWriter.print("\\u" + "0000".substring(0, 4 - hex.length()) + hex);
					}
					break;
			} // switch ch
		} // while pos
       	charWriter.print  ("\"");
    } // writeEscapedString
    
    /** Starts a value in an object sequence
     */
    public void prepareObjectValue() {
		if (saxStack.empty()) {
			// at the very end  - ignore
		} else if (((String) saxStack.peek()).equals(OBJECT_TAG)) {
        	saxStack.pop();
        	saxStack.push(OBJECT1_TAG);
        	charWriter.print  ("{");
		} else if (((String) saxStack.peek()).equals(OBJECT1_TAG)) {
        	saxStack.pop();
        	saxStack.push(OBJECT2_TAG);
        	charWriter.print  (":");
		} else if (((String) saxStack.peek()).equals(OBJECT2_TAG)) {
        	saxStack.pop();
        	saxStack.push(OBJECT1_TAG);
        	charWriter.print  (",");
		}
    } // prepareObjectValue
    
    /** Starts a value in an array sequence
     */
    public void prepareArrayValue() {
		if (saxStack.empty()) {
			// at the very end  - ignore
		} else if (((String) saxStack.peek()).equals(ARRAY_TAG)) {
        	charWriter.print  ("[");
        	saxStack.pop();
        	saxStack.push(ARRAY2_TAG);
		} else if (((String) saxStack.peek()).equals(ARRAY2_TAG)) {
        	charWriter.print  (",");
		}
    } // prepareArrayValue
    
	/** Receive notification of the beginning of the document.
     */
    public void startDocument() {
    	saxStack = new Stack/*<1.5*/<String>/*1.5>*/();
    	valueBuffer = new StringBuffer(256);
    } // startDocument
    
    /** Receive notification of the start of an element.
     *  Looks for the element which contains raw lines.
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
        elem = qName;
        if (false) {
        } else if (elem.equals(STRING_TAG )) {
        } else {
        }
        saxStack.push(elem);
        valueBuffer.setLength(0);
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
        elem = qName;
        saxStack.pop();
		if (false) {
        } else if (elem.equals(ROOT_TAG   )) {
        	// ignore
        } else if (elem.equals(STRING_TAG )) {
        	writeEscapedString();
        	charWriter.println();
        } else {
        }
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int length) {
    	elem = (String) saxStack.peek();
    	String chars = replaceInResult(new String(ch, start, length));
    	valueBuffer.append(chars);
    } // characters

} // SiXMLTransformer
