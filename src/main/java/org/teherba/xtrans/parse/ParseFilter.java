/*  Filter which parses a SAX event stream for proper 
    element order and nesting, and inserts and deletes elements
    for nonterminals. 
    @(#) $Id: ParseFilter.java 566 2010-10-19 16:32:04Z gfis $
    2010-07-21: a bit more than a compiling stub
    2009-12-18, Dr. Georg Fischer: copied from pseudo/TransParser.java
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
package org.teherba.xtrans.parse;
import  org.teherba.xtrans.parse.Item;
import  org.teherba.xtrans.parse.ParseTable;
import  org.teherba.xtrans.parse.Production;
import  org.teherba.xtrans.parse.Transformation;
import  org.teherba.xtrans.proglang.ProgLangTransformer;
import  java.util.Iterator;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/**	Filter which parses a SAX event stream for proper 
 *  element ordering and nesting, and inserts or deletes start and end events 
 *	for the recognized nonterminals.
 *  The input contains a few different elements as
 *	generated and described by the *_TAG constants in {@link ProgLangTransformer}.
 *	The output event stream can be serialized again back to the
 *	specific programming language.
 *  @author Dr. Georg Fischer
 */
public class ParseFilter extends ProgLangTransformer { 
    public final static String CVSID = "@(#) $Id: ParseFilter.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** Parser table generated from a grammar to be used */
    private ParseTable parseTable;
    
    /** Constructor.
     */
    public ParseFilter() {
        super();
        setFormatCodes("parse");
        setDescription("parser for programs");
        setFileExtensions("xml");
    } // Constructor
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(ParseFilter.class.getName());
	} // initialize

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = false;
		log.warn("xtrans.parse.ParseFilter has no generator");
        return  result;
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** name of the grammar to be used */
    private String grammarName;
          
    /** Receive notification of the beginning of the document.
     *	All grammar specific information for the parsing process 
     *	is read from database tables. 
     *	The key for the grammar is taken from option "-grammar".
     */
    public void startDocument() {
    	super.startDocument(); // ultimately sets 'filterHandler'
        grammarName = getOption("grammar", "ident"); // get the grammar's name from option "-grammar"; default: "ident"
		parseTable = new ParseTable();
        try {
			char[] message = ("<!-- " + parseTable.load(grammarName) + " -->\n").toCharArray();
			filterHandler.startDocument();
			filterHandler.characters(message, 0, message.length);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // startDocument

    /** Receive notification of the end of the document.
     */
    public void endDocument() {
        try {
			filterHandler.endDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
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
        try {
			filterHandler.startElement(uri, localName, qName, attrs);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
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
        try {
			filterHandler.endElement(uri, localName, qName);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int length) {
        try {
			filterHandler.characters(ch, start, length);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // characters

} // ParseFilter
