/*  Transformer for NMEA genealogical data communication file format.
    Caution, must be stored in UTF-8: äöüÄÖÜß
    @(#) $Id: NMEATransformer.java 566 2010-10-19 16:32:04Z gfis $
	2007-08-25: units and N/E before numerical values
    2007-07-02, Dr. Georg Fischer 
    
    NMEA -> XML only
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

package org.teherba.xtrans.geo;
import  org.teherba.xtrans.CharTransformer;
import  java.io.BufferedReader;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Transformer for NMEA-0183 standard data (NMEA = National Marine Electronics Association)
 *  for measurements read from the Global Positioning System (GPS).
 *  See the NMEA FAQ at <a href="http://vancouver-webpages.com/peter">http://vancouver-webpages.com/peter</a>.
 *  Example for input file from a Sony CP1 device:
<pre>
&#64;Sonygps/ver1.0/wgs-84
$GPGGA,064138,4811.4104,N,00746.5211,E,1,05,03.3,00236.2,M,047.9,M,,*4F
$GPGSA,A,3,11,13,17,20,23,,,,,,,,06.5,03.3,05.5*06
$GPGSV,2,1,05,20,80,024,50,23,51,193,51,13,22,205,46,11,48,152,52*76
$GPGSV,2,2,05,17,41,271,51,,,,,,,,,,,,*4F
$GPRMC,064138,A,4811.4104,N,00746.5211,E,000.0,000.0,010707,,,A*76
$GPVTG,000.0,T,,M,000.0,N,000.0,K,A*0D
$GPGGA,064153,4811.4137,N,00746.5236,E,1,05,03.3,00219.1,M,047.9,M,,*49
$GPGSA,A,3,11,13,17,20,23,,,,,,,,06.5,03.3,05.5*06
$GPGSV,2,1,05,11,48,152,52,13,22,205,45,17,41,271,51,20,80,025,49*75
$GPGSV,2,2,05,23,51,193,52,,,,,,,,,,,,*45
$GPRMC,064153,A,4811.4137,N,00746.5236,E,000.0,000.0,010707,,,A*7E
$GPVTG,000.0,T,,M,000.0,N,000.0,K,A*0D

Only GGA and RMC sentences are interpreted at the moment.
They are transformed into the following XML:

&lt;nmea&gt;
&lt;GGA&gt;
&lt;time&gt;084724&lt;/time&gt;
&lt;latt&gt;N4811.3356&lt;/latt&gt;
&lt;long&gt;E00746.4177&lt;/long&gt;
&lt;fixq&gt;1&lt;/fixq&gt;
&lt;nsat&gt;03&lt;/nsat&gt;
&lt;hdil&gt;05.5&lt;/hdil&gt;
&lt;alti&gt;M00000.0&lt;/alti&gt;
&lt;hgeo&gt;M047.9&lt;/hgeo&gt;
&lt;/GGA&gt;
&lt;RMC&gt;
&lt;time&gt;084724&lt;/time&gt;
&lt;warn&gt;A&lt;/warn&gt;
&lt;latt&gt;N4811.3356&lt;/latt&gt;
&lt;long&gt;E00746.4177&lt;/long&gt;
&lt;sped&gt;000.0&lt;/sped&gt;
&lt;curs&gt;319.3&lt;/curs&gt;
&lt;date&gt;180807&lt;/date&gt;
&lt;magv/&gt;
&lt;/RMC&gt;
...
</pre>
 *  @author Dr. Georg Fischer
 */
public class NMEATransformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: NMEATransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** No-args Constructor.
     */
    public NMEATransformer() {
        super();
        setFormatCodes("nmea");
        setDescription("NMEA-0183 GPS data for geocoding");
        setFileExtensions("txt");
    } // NMEATransformer
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(NMEATransformer.class.getName());
	} // initialize

    /** Element tag for altitude */
    private static final String ALTI_TAG    = "alti";
    /** Element tag for course */
    private static final String CURS_TAG    = "curs";
    /** Element tag for date */
    private static final String DATE_TAG    = "date";
    /** Element tag for lattitude */
    private static final String LATT_TAG    = "latt";
    /** Element tag for longitude */
    private static final String LONG_TAG    = "long";
    /** Root element tag */
    private static final String ROOT_TAG    = "nmea";
    /** Element tag for speed */
    private static final String SPED_TAG    = "sped";
    /** Element tag for time */
    private static final String TIME_TAG    = "time";

    /** array of values (strings separated by commata) */
    private String[] values;

    /** Decodes a single NMEA value
     *  @param tag tag for surrounding XML element
	 *  @param ival index of element in <em>values</em>
     */
    private void decode1(String tag, int ival) {
		fireStartElement(tag);
		fireCharacters  (values[ival    ]); 			
		fireEndElement  (tag);
	} // decode1

    /** Decodes two consecutive NMEA values
     *  @param tag tag for surrounding XML element
	 *  @param ival index of first element in <em>values</em>
     */
    private void decode2(String tag, int ival) {
		fireStartElement(tag);
		fireCharacters  (values[ival + 1]); 			
		fireCharacters  (values[ival    ]); 			
		fireEndElement  (tag);
	} // decode2

    /** Decodes a lattitude pair of values
	 *  @param ival index of first element in <em>values</em>
     */
    private void decodeLattitude(int ival) {
		fireStartElement(LATT_TAG);
		fireCharacters  (values[ival + 1]); 
		// fireCharacters("0");			
		fireCharacters  (values[ival    ]); 			
		fireEndElement  (LATT_TAG);
	} // decodeLattitude

    /** Decodes a longitude pair of values
	 *  @param ival index of first element in <em>values</em>
     */
    private void decodeLongitude(int ival) {
		fireStartElement(LONG_TAG);
		fireCharacters  (values[ival + 1]); 			
		fireCharacters  (values[ival    ]); 			
		fireEndElement  (LONG_TAG);
	} // decodeLongitude

    /** Decodes a NMEA sentence (line)
     *  @param line NMEA line to be decoded
	 *  Example:
	 <pre>
$GPGGA,064138,4811.4104,N,00746.5211,E,1,05,03.3,00236.2,M,047.9,M,,*4F
$GPGSA,A,3,11,13,17,20,23,,,,,,,,06.5,03.3,05.5*06
$GPGSV,2,1,05,20,80,024,50,23,51,193,51,13,22,205,46,11,48,152,52*76
$GPGSV,2,2,05,17,41,271,51,,,,,,,,,,,,*4F
$GPRMC,064138,A,4811.4104,N,00746.5211,E,000.0,000.0,010707,,,A*76
	</pre>
     */
    private void decodeSentence(String line) {
    	if (line.startsWith("$") && line.length() >= 7) {
    		values = line.split("\\,");
   			int ival = 0;
    		String tag = values[ival ++].substring(3, 6);
			
		    /*  From the FAQ: The (optional) checksum field consists of a "*" and two hex digits
     		 *  representing the exclusive OR of all characters between, but not
    		 *  including, the "$" and "*".
    		 */
    		int computedSum = 0;
    		int starPos = line.indexOf("*");
    		for (int ipos = 1; ipos < starPos; ipos ++) {
    			computedSum ^= line.charAt(ipos);
    		} // for ipos
    		computedSum &= 0xff;

    		int checkSum = -1;
    		if (starPos > 0 && starPos == line.length() - 3) {
    			try {
    				checkSum = Integer.parseInt(line.substring(starPos + 1, starPos + 3), 16) & 0xff;
    			} catch (Exception exc) {
    				// ignore
    			}
    			line = line.substring(0, starPos);
    		} // starPos valid
    					
    		if (checkSum != computedSum) {
    			System.out.println("checksums differ: " + checkSum + " <> " + computedSum);
    		} else { // checksums ok
	    		if (false) {
	    		} else if (tag.equals("GGA")) { // Global Positioning System Fix Data
					// $GPGGA,064138,4811.4104,N,00746.5211,E,1,05,03.3,00236.2,M,047.9,M,,*4F
	    			if (values.length < 15) {
	    				log.error("GGA sentence has less than 15 values");
	    			} else {
		    			fireStartElement(tag);
	    				decode1(TIME_TAG, ival ++);
	    				decodeLattitude(ival ++); ival ++;
	    				decodeLongitude(ival ++); ival ++;
			            fireLineBreak();
	    				decode1("fixq", ival ++);
	    				decode1("nsat", ival ++);
	    				decode1("hdil", ival ++);
	    				decode2(ALTI_TAG, ival ++); ival ++;
	    				decode2("hgeo", ival ++); ival ++;
	    				fireEndElement  (tag);
			            fireLineBreak();
	    			} // GGA
	    		} else if (tag.equals("RMC")) { // Recommended minimum specific GPS/Transit data
					// $GPRMC,064138,A,4811.4104,N,00746.5211,E,000.0,000.0,010707,,,A*76
	    			if (values.length < 12) {
	    				log.error("RMC sentence has less than 12 values");
	    			} else {
		    			fireStartElement(tag);
	    				decode1(TIME_TAG, ival ++);
	    				decode1("warn", ival ++);
	    				decodeLattitude(ival ++); ival ++;
	    				decodeLongitude(ival ++); ival ++;
			            fireLineBreak();
	    				decode1(SPED_TAG, ival ++);
	    				decode1(CURS_TAG, ival ++);
	    				decode1(DATE_TAG, ival ++);
	    				decode2("magv", ival ++); ival ++;
	    				fireEndElement  (tag);
			            fireLineBreak();
	    			} // RMC
	    		} else {
	    			// ignore 
	    		}
	    	} // checksums ok
    	} else { // no $GPtag
    		// ignore "@Sonygps/ver1.0/wgs-84"
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
            BufferedReader buffReader = new BufferedReader(charReader);
            while ((line = buffReader.readLine()) != null) {
                decodeSentence(line);
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

    /** buffer for output line */
    private StringBuffer lineBuffer;
    /** current column number */
    private int linePos;
    /** currently opened element */
    private String elem;
    
    /** Decodes the Morse Code for a character 
     *  @param morse string to be decoded
     *  @return the corresponding character
     */
    private String encode(String morse) {
        String result = getResultReplacement(morse);
        if (result == null) {
            result = "?";
        }
        return result;
    } // encode
    
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        lineBuffer = new StringBuffer(32); 
    } // startDocument
    
    /** Receive notification of the end       of the document.
     */
    public void endDocument() {
    } // endDocument
    
    /** Receive notification of the start of an element.
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
        lineBuffer.setLength(0);
        if (false) {
        } else if (qName.equals(DATE_TAG)) {
        } else if (qName.equals(ROOT_TAG)) {
        } else if (qName.equals(TIME_TAG)) {
        } // else ignore unknown elements
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
        elem = ""; // no characters allowed outside <td> ... </td>
        if (false) {
        } else if (qName.equals(DATE_TAG)) { // now finish this field 
        } else if (qName.equals(TIME_TAG)) { // word boundary
        } // else ignore unknown elements
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int len) {
        lineBuffer.append(new String(ch, start, len));
    } // characters
} // NMEATransformer
