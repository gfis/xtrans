/*  Pseudo transformer which serializes an XML representation of a program; 
	cannot generate XML
    @(#) $Id: ProgramSerializer.java 668 2011-04-06 06:43:17Z gfis $
    2011-04-06: without processArgs, main
    2010-06-09: collapse string and comment tags to one with mode attribute; se and ee
    2009-12-31: sprec, groupid
    2009-12-18, Dr. Georg Fischer: copied from pseudo/ProgramSerializer.java
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
package org.teherba.xtrans.proglang;
import  org.teherba.xtrans.XtransFactory;
import  org.teherba.xtrans.parse.Token;
import  org.teherba.xtrans.proglang.ProgLangTransformer;
import  java.io.BufferedReader;
import  java.util.Iterator;
import  java.util.regex.Pattern;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/**	Pseudo transformer which serializes an XML representation of a program; 
 *	cannot generate XML. The input contains a few different XML elements, as
 *	generated and described by {@link ProgLangTransformer}.
 *	The output is a tab-separated text file with the following columns:
 *	<ul>
 *	<li>progname: program name or other identification of the program</li>
 *	<li>lineno:   original line number</li>
 *	<li>elemno:   sequential element number in the line</li>
 *	<li>spacesBefore: number of spaces before the element</li>
 *	<li>groupid:  identification of the statement group in the program</li>
 *	<li>elemtype: element name (start tag): kw, op, num ...</li>
 *	<li>elemval:  main element attribute, content (identifier, number, operator ...)</li>
 *	</ul>
 *	Unimportant elements (the ones for line numbering, pre- and postfixes, and comments)
 *	are suppressed and not output.
 *	<p />
 *	The output file will normally be loaded into a database table.
 *  @author Dr. Georg Fischer
 */
public class ProgramSerializer extends ProgLangTransformer { 
    public final static String CVSID = "@(#) $Id: ProgramSerializer.java 668 2011-04-06 06:43:17Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** maximum number of files / formats / encodings */
    private static final int MAX_FILE = 2;
	
    /** Constructor.
     */
    public ProgramSerializer() {
        super();
        setFormatCodes("progser");
        setDescription("serializer for programs");
        setFileExtensions("tsv");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables
	 *  common to generator and serializer.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(ProgramSerializer.class.getName());
	} // initialize
	
    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
            String line;
            BufferedReader buffReader = new BufferedReader(charReader);
            fireStartDocument(); 
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            Pattern splitter = Pattern.compile(Token.SEPARATOR);
            int lineCount = 0;
            while ((line = buffReader.readLine()) != null) {
                lineCount ++;
                Token token = new Token(line);
				token.fire(this);
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

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** Upper bound for character buffer */
    protected static final int MAX_BUF = 4096;
    /** buffer for portions of the input elements */
    protected StringBuffer saxBuffer;
    /** name of input program (commandline parameter) */
    protected String programName;
    /** column separator in output file, default "\t" */
    protected String separator;
    /** identification of the statement group in the program */
    protected String groupId;
    /** current line number */
    protected int saxLineNo;
    /** current index (element number) in line */
    protected int saxTokenNo;
	/** current token (not yet finished, without the value), has no nested elements */
	private Token saxToken;
    
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        saxBuffer   = new StringBuffer(MAX_BUF); // a rather long portion
		programName = getOption("name", "");
		separator   = getOption("sep", "\t");
		groupId     = "P1";
		saxLineNo   = 0;
		saxTokenNo  = 0;
		try {
		} catch (Exception exc) {
		    log.error(exc.getMessage(), exc);
		}
    } // startDocument

    /** Receive notification of the end of the document.
     */
    public void endDocument() {
		try {
		} catch (Exception exc) {
		    log.error(exc.getMessage(), exc);
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
		saxToken = new Token(true, qName, attrs, programName, saxTokenNo);
		switch (Token.getCategory(qName)) {
			default:
			case Token.TERMINAL:
				break;
			case Token.IGNORABLE:
				break;
			case Token.NONTERMINAL:
	       		charWriter.println(saxToken.toString());
				break;
		} // switch category
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
		switch (Token.getCategory(qName)) {
			default:
			case Token.TERMINAL:
			case Token.IGNORABLE:
				break;
			case Token.NONTERMINAL:
				saxToken = new Token(false, qName, null, programName, saxTokenNo);
				break;
		} // switch category
   		charWriter.println(saxToken.toString());
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int length) {
        if (length == 0) {
           // ignore
        } else {
			String value = new String(ch, start, length);
    	    if (value.indexOf(programName) >= 0) {
        		value = value.replaceAll(programName, "{\\$name}");
	   	    }
            saxToken.setVal(value);
		}
    } // characters

} // ProgramSerializer
