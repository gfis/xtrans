/*  Pseudo transformer which counts XML elements
    @(#) $Id: CountingSerializer.java 566 2010-10-19 16:32:04Z gfis $
	2010-06-05: print result line only if count > 0
    2007-08-30: was in package 'general'
    2007-01-03, Dr. Georg Fischer
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

package org.teherba.xtrans.pseudo;
import  org.teherba.xtrans.CharTransformer;
import  java.util.TreeMap;
import  java.util.Iterator;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Pseudo transformer which cannot generate XML, 
 *  but which counts XML elements only.
 *  @author Dr. Georg Fischer
 */
public class CountingSerializer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: CountingSerializer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** No-args Constructor.
     */
    public CountingSerializer() {
        super();
        setFormatCodes("count");
        setDescription("count XML elements");
        setFileExtensions("txt");
    } // Constructor()
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(CountingSerializer.class.getName());
	} // initialize

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        log.warn("xtrans.CountingSerializer cannot generate XML");
        boolean result = false;
        return  result;
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** Map for counters of individual XML elements and length of content characters */
    private TreeMap/*<1.5*/<String, Integer>/*1.5>*/ counters;

    /** number of characters in direct content */
    private int charCount;
    
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        counters = new TreeMap/*<1.5*/<String, Integer>/*1.5>*/();
        charCount = 0;
    } // startDocument

    /** Receive notification of the end of the document.
     */
    public void endDocument() {
        Iterator iter = counters.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            int count = ((Integer) counters.get(key)).intValue();
			if (count > 0) {
	            charWriter.println(key + "\t" + count);
			}
        }
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
        qName += ".elem";
        Object obj = counters.get(qName);
        if (obj != null) { 
            Integer counter = new Integer(((Integer) obj).intValue() + 1);
            counters.put(qName, counter);
        } else {
            counters.put(qName, new Integer(1));
        }
        charCount = 0;
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
        qName += ".char";
        Object obj = counters.get(qName);
        if (obj != null) { 
            Integer counter = new Integer(((Integer) obj).intValue() + charCount);
            counters.put(qName, counter);
        } else {
            counters.put(qName, new Integer(charCount));
        }
        charCount = 0;
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int length) {
        charCount += length;
    } // characters

} // CountingSerializer
