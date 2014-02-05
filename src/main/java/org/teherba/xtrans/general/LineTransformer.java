/*  Transforms text lines to/from XML
    @(#) $Id: LineTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2006-10-04: multiple line grouping
    2006-09-20, Dr. Georg Fischer
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
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Simple transformer for lines in a character file.
 *  Creates an XML table with rows and cells. 
 *  Each line in the input file goes into one cell,
 *  where <em>n</em> cells fill one row. 
 *  The number of lines in a cell is a configurable parameter (default 1).
 *  @author Dr. Georg Fischer
 */
public class LineTransformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: LineTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** Constructor.
     */
    public LineTransformer() {
        super();
        setFormatCodes("line");
        setDescription("character file consisting of lines");
        setFileExtensions("txt");
    } // Constructor
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(LineTransformer.class.getName());
        putEntityReplacements();
	} // initialize

    /** Data element tag */
    private static final String DATA_TAG    = "td";
    /** Root element tag */
    private static final String ROOT_TAG    = "line";
    /** Table element tag */
    private static final String TABLE_TAG   = "table";
    /** Row element tag */
    private static final String ROW_TAG     = "tr";
    /** Info element tag */
    private static final String NEWLINE_TAG = "n";

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
        	putEntityReplacements();
            int group = getIntOption("group", 1); // so many lines = cells in one table row
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireStartElement(TABLE_TAG, toAttribute("group", String.valueOf(group)));
            fireLineBreak();
            String line;
            int count = 0;
            BufferedReader buffReader = new BufferedReader(charReader);
            while ((line = buffReader.readLine()) != null) {
                if (count == 0) { // start a new row
                    fireStartElement(ROW_TAG); // start of next row
                }
                fireStartElement(DATA_TAG);
                fireCharacters(replaceNoSource(line));
                fireEndElement(DATA_TAG);
                count ++;
                if (count == group) { // start a new row
                    count = 0;
                    fireEndElement(ROW_TAG);
                    fireLineBreak();
                } else {
                    fireLineBreak();
                    fireEmptyElement(NEWLINE_TAG);
                }
            } // while not EOF
            if (count != 0) { // force end of row
                fireEndElement(ROW_TAG);
                fireLineBreak();
            }
            buffReader.close();
            fireEndElement(TABLE_TAG);
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

    /** state of parsing, 0 = uninteresting, 1 = in interesting element */
    private int state;
    
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        state = 0; // uninteresting
    }
    
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
        if (qName.equals(DATA_TAG)) {
            state = 1; // in interesting element
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
        if (qName.equals(DATA_TAG)) {
            charWriter.println();
            state = 0; // in interesting element
        }
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int length) {
        if (state == 1) { // interesting
            charWriter.print(replaceInResult(new String(ch, start, length)));
        }
    } // characters

} // LineTransformer
