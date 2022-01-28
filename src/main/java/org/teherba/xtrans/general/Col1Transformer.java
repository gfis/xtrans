/*  Transforms text lines to/from XML. 
    Lines start with a letter/word followed by whitespace, or whitespace only.
    The letters/words indicate the resulting XML tag, while whitespace
    appends the line to the content of the current XML element.
    @(#) $Id: Col1Transformer.java 566 2010-10-19 16:32:04Z gfis $
    2007-06-04, Dr. Georg Fischer: copied from LineTransformer
    Caution, this file is UTF-8 encoded: äöüÄÖÜß
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
import  java.util.regex.Matcher;
import  java.util.regex.Pattern;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transforms text lines to/from XML. 
 *  Lines start with a letter/word followed by whitespace, or whitespace only.
 *  The letters/words indicate the resulting XML tag, while whitespace
 *  appends the line to the content of the current XML element.
 *	The first word defines the boundary for <em>td</em> elements.
 *	The transformation from XML to this format does not reproduce Windows
 *	newlines with CR.
 *	<p>
 *  Example:
 *  <pre>
h Windbeutel
i 125 g Butter
i 125 g Mehl
i 250 ml Wasser
i 4 Eier
i 1 EL feiner Zucker
i 1 Prise Salz
r Das Wasser mit Zucker, Salz und Butter zum Kochen bringen.
  Das gesiebte Mehl hineinstreuen und die Masse so lange rühren,
  bis sie sich vom Topf löst. Den Teig abkühlen lassen, dann
  die Eier nacheinander darunterschlagen. Mit einem Löffel
  oder Dressierbeutel nicht zu große Teighäufchen auf ein
  gebuttertes und dünn mit Mehl bepudertes Blech setzen
  (in Abständen von ca. 10 cm).
r 15-20 Min. bei starker Hitze goldgelb backen. Nicht zu früh
  nachsehen, da das Gebäck gegen Zugluft empfindlich ist und
  leicht zusammenfällt!
r Der gebackene Windbeutel wird zu 3/4 aufgeschnitten und mit
  Sahne gefüllt.
n Ich habe zum Backen 45 Min. gebraucht, da nach meinen Erfahrungen
  die "starke" Hitze, wie angegeben, zu dunkel backt, wenn nicht
  sogar schwarz und verbrannt.
  
resulting XML:

&lt;td&gt;
&lt;h&gt;Windbeutel&lt;/h&gt;
&lt;i&gt;125 g Butter&lt;/i&gt;
&lt;i&gt;125 g Mehl&lt;/i&gt;
&lt;i&gt;250 ml Wasser&lt;/i&gt;
&lt;i&gt;4 Eier&lt;/i&gt;
&lt;i&gt;1 EL feiner Zucker&lt;/i&gt;
&lt;i&gt;1 Prise Salz&lt;/i&gt;
&lt;r&gt;Das Wasser mit Zucker, Salz und Butter zum Kochen bringen.
Das gesiebte Mehl hineinstreuen und die Masse so lange rühren,
bis sie sich vom Topf löst. Den Teig abkühlen lassen, dann
die Eier nacheinander darunterschlagen. Mit einem Löffel
oder Dressierbeutel nicht zu große Teighäufchen auf ein
gebuttertes und dünn mit Mehl bepudertes Blech setzen
(in Abständen von ca. 10 cm).&lt;/r&gt;
&lt;r&gt;15-20 Min. bei starker Hitze goldgelb backen. Nicht zu früh
nachsehen, da das Gebäck gegen Zugluft empfindlich ist und
leicht zusammenfällt!&lt;/r&gt;
&lt;r&gt;Der gebackene Windbeutel wird zu 3/4 aufgeschnitten und mit
Sahne gefüllt.&lt;/r&gt;
&lt;n&gt;Ich habe zum Backen 45 Min. gebraucht, da nach meinen Erfahrungen
die "starke" Hitze, wie angegeben, zu dunkel backt, wenn nicht
sogar schwarz und verbrannt.
&lt;/n&gt;
&lt;/td&gt;
</pre>
 *  @author Dr. Georg Fischer
 */
public class Col1Transformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: Col1Transformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** No-args Constructor.
     */
    public Col1Transformer() {
        super();
        setFormatCodes("col1");
        setDescription("tags in column 1 of text lines");
        setFileExtensions("txt");
    } // Constructor()
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = LogManager.getLogger(Col1Transformer.class.getName());
        putEntityReplacements();
	} // initialize

    /** Root element tag */
    private static final String ROOT_TAG    = "root";
    /** Table element tag */
    private static final String TABLE_TAG   = "table";
    /** Data element tag */
    private static final String DATA_TAG    = "td";

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        String currentTag = "";
        String firstWord = "";
        try {
            Pattern patt = Pattern.compile("((\\w*)(\\s+))"); // a word (maybe empty) and whitespace (non-empty)
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireStartElement(TABLE_TAG);
            fireLineBreak();
            String line;
            int count = 0;
            BufferedReader buffReader = new BufferedReader(charReader);
            while ((line = buffReader.readLine()) != null) {
            	count ++;
                Matcher matcher = patt.matcher(line);
                if (matcher.lookingAt()) { // word-whitespace matches at the beginning of 'line'
                    String word = matcher.group(2);
					int matchLen = matcher.group(1).length();
                    if (word.length() > 0) { // non-empty word
                        if (count > 1) { // not first line
                            fireEndElement(currentTag);
                            fireLineBreak();
	                        if (word.equals(firstWord)) {
   		                    	fireEndElement(DATA_TAG);
        	                    fireLineBreak();
            	            	fireStartElement(DATA_TAG);
                	            fireLineBreak();
       	            	    } else { 
       	            	    }
                        } else { // count == 1 - first line
                        	firstWord = word;
           	            	fireStartElement(DATA_TAG);
               	            fireLineBreak();
                        }
                        currentTag = word;
                        fireStartElement(currentTag); // start of next row
                    } else { // leading whitespace: continue current element
                        fireLineBreak();    
                    }
                    // now emit the rest of the line
                    fireCharacters(line.substring(matchLen));
                } else { // no leading word-whitespace, e.g. leading punctuation: continue current element
                    fireLineBreak();    
                    fireCharacters(line);
                }
            } // while not EOF
            if (! currentTag.equals("")) {
                fireEndElement(currentTag);
                fireLineBreak();
               	fireEndElement(DATA_TAG);
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
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** currently opened element */
    private String elem;
    
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
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
        } else if (qName.equals(DATA_TAG )) {
        } else if (qName.equals(ROOT_TAG )) {
        } else if (qName.equals(TABLE_TAG)) {
        } else {
            charWriter.print(qName + " ");
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
        elem = qName; // no characters allowed outside <td> ... </td>
        if (false) {
        } else if (qName.equals(DATA_TAG )) {
        } else if (qName.equals(ROOT_TAG )) {
        } else if (qName.equals(TABLE_TAG)) {
        } else {
        	elem = "";
        }
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int length) {
        if (false) {
        } else if (elem.equals(DATA_TAG )) {
        } else if (elem.equals(ROOT_TAG )) {
        } else if (elem.equals(TABLE_TAG)) {
        } else {
	    	String chars = replaceInResult(new String(ch, start, length));
	    	if (chars.matches("\\s+")) {
	        	charWriter.print(chars);
	    	} else {
	        	charWriter.print(chars.replaceAll("\\n", "\n  "));
	    	}
        }
    } // characters

} // Col1Transformer
