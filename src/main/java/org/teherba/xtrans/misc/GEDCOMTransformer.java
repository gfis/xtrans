/*  Transformer for GEDCOM genealogical data communication file format.
    Caution, must be stored in UTF-8: ÄÖÜß
    @(#) $Id: GEDCOMTransformer.java 689 2011-05-03 18:01:49Z gfis $
    2011-05-03: implement serializer
    2007-06-22, Dr. Georg Fischer 
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

package org.teherba.xtrans.misc;
import  org.teherba.xtrans.CharTransformer;
import  java.io.BufferedReader;
import  java.util.regex.Pattern;
import  java.util.regex.Matcher;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Transformer for GEDCOM genealogical data communication file format.
 *  Simliar to: 
 *  http://homepage.ntlworld.com/michael.h.kay/gedml/index.html. Maintained by Michael H. Kay. 2-April-2002 or later. 
 *  See also the page for GEDCOM XML on http://xml.coverpages.org/genealogy.html:
 *  http://www.gendex.com/gedcom55/55gctoc.htm - GEDCOM 5.5 reference
 *  Example for input file:
<pre>
0 @SUBM@ SUBM
0 @I001@ INDI
1 NAME Georg /Fischer/
1 SEX M
1 BIRT
2 DATE 29 Apr 1944
2 PLAC Waldshut
1 ADDR Rotteckring 19
2 CONT
2 CONT Kenzingen
2 CONT
2 CONT 79341
2 CONT de
2 PHON 07641 913016, +49 175 160 7788
1 FAMS @F001@
1 FAMS @F003@
1 FAMC @F002@
0 @I002@ INDI
1 NAME Veronika Sabine Waltraut /Fischer/
1 SEX F
1 BIRT
2 DATE 8 Aug 1988
2 PLAC Kassel
1 FAMC @F001@
0 @I003@ INDI
1 NAME Ulrich Erwin Matthias /Fischer/
1 SEX M
1 BIRT
2 DATE 9 Sep 1999
2 PLAC Fulda
1 FAMC @F001@
0 @I004@ INDI
1 NAME Friedrich Wilhelm /Fischer/
1 SEX M
1 BIRT
2 DATE 8 Aug 1907
2 PLAC Freiburg
1 DEAT
2 DATE 11 Aug 1992
2 PLAC Waldshut
1 FAMS @F002@
1 FAMC @F004@
0 @I005@ INDI
1 NAME Dorothea Teherba /Ritter/
1 SEX F
1 BIRT
2 DATE 7 Feb 1910
2 PLAC Oranienburg
1 DEAT
2 DATE 8 Apr 1985
2 PLAC Berlin
1 FAMS @F002@
1 FAMC @F010@
</pre>
 *  @author Dr. Georg Fischer
 */
public class GEDCOMTransformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: GEDCOMTransformer.java 689 2011-05-03 18:01:49Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** current position in output string */
    private int colPos;
	
	/** maximum number of nested keywords */
	private static final int MAX_STACK = 16;
	/** stack of nested GEDCOM keywords */
	private String[] stack;
	/** top of stack */
	private int top;
	/** pattern for GEDCOM lines */
	private Pattern gedpat;

    /** No-args Constructor.
     */
    public GEDCOMTransformer() {
        super();
        setFormatCodes("gedcom");
        setDescription("Genealogical Data Communication");
        setFileExtensions("ged");
    } // Constructor()
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(GEDCOMTransformer.class.getName());
        stack = new String[MAX_STACK];
        top = -1;
        gedpat = Pattern.compile("(\\d+)\\s+([\\@\\w]+)\\s*(.*)");
	} // initialize

    /** Root element tag */
    private static final String ROOT_TAG    = "gedcom";
    /** <em>id</em> attribute */
    private static final String ID_ATTR     = "id";
    /** <em>ref</em> attribute */
    private static final String REF_ATTR    = "ref";

    /** Decodes a GEDCOM line. 
     *	The nesting level in column 1 is represented by the proper nesting of XML elements (SAX events).
     *  @param line GEDCOM line to be decoded
	 *  Example:
	 <pre>
0 @I003@ INDI
1 NAME Ulrich Erwin Matthias /Fischer/
1 SEX M
1 BIRT
2 DATE 9 Sep 1984
2 PLAC München
1 FAMC @F001@
		</pre>
     */
    private void decode(String line) {
    	Matcher matcher = gedpat.matcher(line);
    	if (matcher.matches()) {
    		int level = 0;
    		try {
    			level = Integer.parseInt(matcher.group(1));
    		} catch (Exception exc) {
    			// cannot occur because of pattern
    		} 
   			if (level < MAX_STACK) {
 				String keyWord = matcher.group(2);
				String value   = matcher.group(3);
				String id      = "";
				log.debug(level + " " + keyWord + ";" + value + ";");
				if (keyWord.startsWith("@")) {
					id = keyWord.replaceAll("@", "");
					keyWord = value;
					value = "";
				}
    			while (top >= level) { // pop = close previous elements
    				fireEndElement(stack[top --]);
    				fireLineBreak();
    			} // while popping
    			top = level;
    			stack[top] = keyWord;
    			if (id.length() > 0) { // keyword has an id prefix
	    			fireStartElement(keyWord, toAttributes(new String[] { ID_ATTR, id }));
	    		} else { // no id
					if (value != null && value.startsWith("@")) { // value is a reference
						fireStartElement(keyWord, toAttributes(new String[] { REF_ATTR, value.replaceAll("@", "") }));
					} else {
		    			fireStartElement(keyWord);
		    			if (value != null && value.length() > 0) {
		    				fireCharacters(value);
		    			}
		    		} // normal value
	    		} // no id
    		} else {
    			log.error("level > " + MAX_STACK + ": " + level);
    		} 
    	} else {
    		log.error("non-matching GEDCOM line: " + line);
    	} 
    } // decode
    
    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            String line;
            int count = 0;
			top = -1;
            BufferedReader buffReader = new BufferedReader(charReader);
            while ((line = buffReader.readLine()) != null) {
                decode(line);
                count ++;
            } // while not EOF
			while (top >= 0) { // pop = close previous elements
    			fireEndElement(stack[top --]);
    			fireLineBreak();
    		} // while popping
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

    /** buffer for output line */
    private StringBuffer lineBuffer;
    /** current column number */
    private int linePos;
	/** current nesting level */
	private int saxLevel;
	/** whether an element was opened: 
	   -1 = at start of document, 
	    2 = directly after start of element, 
	    1 = later after start, 
	    0 after end of element 
	*/
	private int charState;

    /** currently opened element */
    private String elem;
    
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        lineBuffer = new StringBuffer(128); 
        saxLevel = 0;
        charState = -1;
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
        if (charState >= 0 && lineBuffer.length() > 0) {
        	charWriter.println(lineBuffer.toString());
        	lineBuffer.setLength(0);
        }
        if (false) {
        } else if (qName.equals(ROOT_TAG)) {
			// ignore
        } else { // variable GEDCOM keyword
	        charState = 2;
        	lineBuffer.append(String.valueOf(saxLevel));
        	String id = attrs.getValue(ID_ATTR);
        	if (id != null) {
	        	lineBuffer.append(" @");
    	    	lineBuffer.append(id);
    	    	lineBuffer.append('@');
        	}
        	lineBuffer.append(' ');
        	lineBuffer.append(qName.toUpperCase());
        	String ref = attrs.getValue(REF_ATTR);
        	if (ref != null) {
	        	lineBuffer.append(" @");
    	    	lineBuffer.append(ref);
    	    	lineBuffer.append('@');
        	}
        	saxLevel ++;
        } // else ignore unknown elements
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
        elem = ""; // no characters allowed outside <td> ... </td>
        if (false) {
        } else if (qName.equals(ROOT_TAG )) { 
        	charWriter.println(lineBuffer.toString());
        } else { // variable GEDCOM keyword
            saxLevel --;
        } 
        charState = 0;
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int len) {
    	if (charState > 0) {
    		if (charState == 2) {
    			lineBuffer.append(' ');
    		}
    		charState = 1;
        	lineBuffer.append(new String(ch, start, len));
    	}
    } // characters
    
} // GEDCOMTransformer
