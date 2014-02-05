/*  Transforms text lines with characters into their
    Morse code (sequences of "." and "-") wrapped into an XML table
    caution, must be stored in UTF-8: ÄÖÜß
    @(#) $Id: MorseCodeTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2006-11-22, Dr. Georg Fischer
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
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/**	Transformer for simple text to be encoded in Morse code.
 *  Creates an XML &lt;table&gt; with &lt;tr&gt; rows for words 
 *  and &lt;td&gt;cells for characters. 
 *  <p />
 *  Because a lot of ASCII characters are missing (they are 
 *  replaced by '?'), and because of the conversion to uppercase,
 *  a forward-backward transformation will normally not
 *  exactly reproduce the input file.
 *  @author Dr. Georg Fischer
 */
public class MorseCodeTransformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: MorseCodeTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** number of lines processed so far */
    private int lineCount;

    /** current position in output string */
    private int colPos;

    /** buffer for output line */
    private StringBuffer lineBuffer;
    /** current column number */
    private int linePos;

    /** No-args Constructor.
     */
    public MorseCodeTransformer() {
        super();
        setFormatCodes("morse");
        setDescription("Morse Code");
        setFileExtensions("txt");
    } // Constructor
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
     *  The code table is from <a href="http://de.wikipedia.org">de.Wikipedia.org</a>,
     *  entry <em>Morsecode</em>.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(MorseCodeTransformer.class.getName());
        putReplacementMap("A",  ".-"        );
        putReplacementMap("B",  "-..."      );
        putReplacementMap("C",  "-.-."      );
        putReplacementMap("D",  "-.."       );
        putReplacementMap("E",  "."         );
        putReplacementMap("F",  "..-."      );
        putReplacementMap("G",  "--."       );
        putReplacementMap("H",  "...."      );
        putReplacementMap("I",  ".."        );
        putReplacementMap("J",  ".---"      );
        putReplacementMap("K",  "-.-"       );
        putReplacementMap("L",  ".-.."      );
        putReplacementMap("M",  "--"        );
        putReplacementMap("N",  "-."        );
        putReplacementMap("O",  "---"       );
        putReplacementMap("P",  ".--."      );
        putReplacementMap("Q",  "--.-"      );
        putReplacementMap("R",  ".-."       );
        putReplacementMap("S",  "..."       );
        putReplacementMap("T",  "-"         );
        putReplacementMap("U",  "..-"       );
        putReplacementMap("V",  "...-"      );
        putReplacementMap("W",  ".--"       );
        putReplacementMap("X",  "-..-"      );
        putReplacementMap("Y",  "-.--"      );
        putReplacementMap("Z",  "--.."      );
        putReplacementMap("1",  ".----"     );
        putReplacementMap("2",  "..---"     );
        putReplacementMap("3",  "...--"     );
        putReplacementMap("4",  "....-"     );
        putReplacementMap("5",  "....."     );
        putReplacementMap("6",  "-...."     );
        putReplacementMap("7",  "--..."     );
        putReplacementMap("8",  "---.."     );
        putReplacementMap("9",  "----."     );
        putReplacementMap("0",  "-----"     );
    /*
    */
        putReplacementMap("À", ".--.-"     );
        putReplacementMap("Å", ".--.-"     );
        putReplacementMap("Ä", ".-.-"      );
        putReplacementMap("È", ".-..-"     );
        putReplacementMap("É", "..-.."     );
        putReplacementMap("Ö", "---."      );
        putReplacementMap("Ü", "..--"      );
        putReplacementMap("ß", "...--.."   );
        putReplacementMap("CH", "----"      );
        putReplacementMap("Ñ", "--.--"     );
    /*
    */
        putReplacementMap(".",  ".-.-.-"    );
        putReplacementMap(",",  "--..--"    );
        putReplacementMap(":",  "---..."    );
        putReplacementMap(";",  "-.-.-."    );
        putReplacementMap("?",  "..--.."    );
        putReplacementMap("-",  "-....-"    );
        putReplacementMap("(",  "-.--."     );
        putReplacementMap(")",  "-.--.-"    );
        putReplacementMap("'",  ".----."    );
        putReplacementMap("=",  "-...-"     );
        putReplacementMap("+",  ".-.-."     );
        putReplacementMap("/",  "-..-."     );
        putReplacementMap("@",  ".--.-."    );
    /*
        putReplacementMap("KA", "-.-.-"     );
        putReplacementMap("BT", "-...-"     );
        putReplacementMap("AR", ".-.-."     );
        putReplacementMap("VE", "...-."     );
        putReplacementMap("SK", "...-.-"    );
        putReplacementMap("SOS",    "...---..."     );
        putReplacementMap("IRRUNG", "........"      );
    */
	} // initialize

    /** Data element tag */
    private static final String DATA_TAG    = "td";
    /** Root element tag */
    private static final String ROOT_TAG    = "morse";
    /** Row element tag */
    private static final String ROW_TAG     = "tr";
    /** Info element tag */
    private static final String NEWLINE_TAG = "n";

    /** Encodes some text to Morse Code
     *  @param text string to be encoded
     *  @return the Morse Code representation wrapped into XML
     */
    private String encode(String text) {
        lineBuffer = new StringBuffer(296);
        int pos = 0;
        while (pos < text.length()) {
            char ch = Character.toUpperCase(text.charAt(pos ++));
            if (ch <= ' ') { // whitespace or control
                fireEndElement(ROW_TAG);
                fireLineBreak();
                fireStartElement(ROW_TAG);
            } else { // non-whitespace
                String morse = getSourceReplacement(String.valueOf(ch));
                if (morse == null) {
                    morse = getSourceReplacement("?");
                }
                fireStartElement(DATA_TAG, toAttribute("ch" 
                        , text.substring(pos - 1, pos)                      
                                .replaceAll("&", "&amp;")
                                .replaceAll("<", "&lt;")
                                .replaceAll(">", "&gt;")
                                .replaceAll("\'", "&apos;")
                                .replaceAll("\"", "&quot;")
                        ));
                fireCharacters(morse);
                fireEndElement(DATA_TAG);
            }       
        } // while pos
        return lineBuffer.toString();
    } // encode
    
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
            BufferedReader buffReader = new BufferedReader(charReader);
            while ((line = buffReader.readLine()) != null) {
                fireStartElement(ROW_TAG);
                encode  (line);
                fireEmptyElement(NEWLINE_TAG);
                fireEndElement  (ROW_TAG);
                fireLineBreak();
                fireLineBreak();
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
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** currently opened element */
    private String elem;
    
    /** Decodes the Morse Code for a character 
     *  @param morse string to be encoded
     *  @return the corresponding character
     */
    private String decode(String morse) {
        String result = getResultReplacement(morse);
        if (result == null) {
            result = "?";
        }
        return result;
    } // decode
    
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        lineCount = 0;
        lineBuffer = new StringBuffer(32); 
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
        if (false) {
        } else if (qName.equals(DATA_TAG )) {
            lineBuffer.setLength(0);
        } else if (qName.equals(ROOT_TAG)) {
        } else if (qName.equals(ROW_TAG  )) {
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
        } else if (qName.equals(DATA_TAG )) { // now finish this field 
            charWriter.print(decode(lineBuffer.toString()));
        } else if (qName.equals(ROW_TAG  )) { // word boundary
            if (linePos > 0) {
                charWriter.print(" ");
                linePos ++;
            }
        } else if (qName.equals(NEWLINE_TAG)) { // now output this row
            charWriter.println();
            linePos = 0;
        } // ignore unknown elements
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int len) {
        if (elem .equals(DATA_TAG)) {
            lineBuffer.append(new String(ch, start, len));
            linePos += len;
        } // else ignore characters in unknown elements
    } // characters
} // MorseCodeTransformer
