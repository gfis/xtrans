/*  Pseudo transformer which generates a sequence of elements.
    @(#) $Id: SequenceGenerator.java 798 2011-09-10 15:30:05Z gfis $
    2009-08-10, Georg Fischer: copied from CountingSerializer
*/
/*
 * Copyright 2009 Dr. Georg Fischer <punctum at punctum dot kom>
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

package org.teherba.xtrans.pseudo;
import  org.teherba.xtrans.CharTransformer;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Pseudo transformer which generates a sequence of elements.
 *  @author Dr. Georg Fischer
 */
public class SequenceGenerator extends CharTransformer {
    public final static String CVSID = "@(#) $Id: SequenceGenerator.java 798 2011-09-10 15:30:05Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** Root element tag */
    private static final String ROOT_TAG    = "sequence";
    /** Element of sequence tag */
    private static final String ELEM_TAG    = "elem";

    /** Constructor.
     */
    public SequenceGenerator() {
        super();
        setFormatCodes("seq");
        setDescription("generate a sequence");
        setFileExtensions("txt");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(SequenceGenerator.class.getName());
	} // initialize

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        int first = getIntOption("first",  1);
        int last  = getIntOption("last" , 16);
        int incr  = (first < last) ? 1 : -1;
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
			boolean busy = true;
            while (busy) {
                busy = first != last;
		        fireSimpleElement(ELEM_TAG, String.valueOf(first));
                fireLineBreak();
		        first += incr;
            } // while 
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

    /** number of characters in direct content */
    private int charCount;

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
    	charWriter.print("SequenceGenerator cannot serialize");
    } // startDocument

    /** Receive notification of the end of the document.
     */
    public void endDocument() {
        charWriter.println();
    } // endDocument

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
        charWriter.print(newline + qName);
        String key = attrs.getValue("key");
        if (key != null) {
        	charWriter.print("." + key);
        }
        charWriter.print("=");
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
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int length) {
    	String text = new String(ch, start, length).replaceAll("\\r?\\n", "");
    	if (! text.matches("\\s+")) {
    		charWriter.print(text);
    	}
    } // characters
    
} // SequenceGenerator
