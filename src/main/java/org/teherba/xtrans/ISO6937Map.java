/*  ISO6937Generator.java - generates the code mappings for the ISO 6937 character set
 *  @(#) $Id: ISO6937Map.java 9 2008-09-05 05:21:15Z gfis $
 *	2008-03-06, Georg Fischer
 *
 *  Caution, this file must be saved as UTF-8: äöüÄÖÜß
 *
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

package org.teherba.xtrans;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Generates the code mappings for the ISO 6937 character set.
 *  The code tables stem from the article
 *	"ISO/IEC 6937" in <a href="http://en.wikipedia.org">Wikipedia</a>, the free encyclopedia.
 */

public class ISO6937Map {
    public final static String CVSID = "@(#) $Id: ISO6937Map.java 9 2008-09-05 05:21:15Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /**	Initializes mappings for the ISO 6937 character set.
     */
    public ISO6937Map() {
        log = LogManager.getLogger(ISO6937Map.class.getName());
    	setAccents();
    	setCodes();
    } // Constructor 0

    /** list of valid unaccented letters for each accent */
    private String[] letters  = new String[16];
    /** list of corresponding accented letters for each accent */
    private String[] accents = new String[16];

	/** Sets the mappings for all accented letters
	 *	@param descr description of the accent
	 *	@param startByte byte that indicates the start of an accented letter byte pair, corresponds 
	 *	with the accent
	 *	@param unaccented sequence of letters which may be accented with this accent
	 *	@param accented corresponding sequence of accented letters
	 */
	private void setAccent(String descr, int startByte, String unaccented, String accented) {
		int ind = startByte - 0xc0;
		letters[ind] = unaccented;
		accents[ind] = accented;
	} // setAccent

	/** Sets the mappings for all accented letters
	 */
	private void setAccents() {
		setAccent("Grave  			", 0xc1, "AEIOUaeiou"				, "ÀÈÌÒÙàèìòù"              );
		setAccent("Acute  			", 0xc2, "ACEILNORSUYZaceilnorsuyz"	, "ÁĆÉÍĹŃÓŔŚÚÝŹáćéíĺńóŕśúýź");
		setAccent("Circumflex		", 0xc3, "ACEGHIJOSUWYaceghijosuwy"	, "ÂĈÊĜĤÎĴÔŜÛŴŶâĉêĝĥîĵôŝûŵŷ");
		setAccent("Tilde  			", 0xc4, "AINOUainou"				, "ÃĨÑÕŨãĩñõũ"              );
		setAccent("Macron 			", 0xc5, "AEIOUaeiou"				, "ĀĒĪŌŪāēīōū"              );
		setAccent("Breve  			", 0xc6, "AGUagu"					, "ĂĞŬăğŭ"                  );
		setAccent("Dot    			", 0xc7, "CEGIZcegiz"				, "ĊĖĠİŻċėġıż"              );
		setAccent("Umlaut/Diaresis  ", 0xc8, "AEIOUYaeiouy"				, "ÄËÏÖÜŸäëïöüÿ"            );
		setAccent("Ring   			", 0xca, "AUau"						, "ÅŮåů"                    );
		setAccent("Cedilla			", 0xcb, "CGKLNRSTcgklnrst"			, "ÇĢĶĻŅŖŞŢçģķļņŗşţ"        );
		setAccent("Double Acute		", 0xcd, "OUou"						, "ŐŰőű"                    );
		setAccent("Ogonek 			", 0xce, "AEIUaeiu"					, "ĄĘĮŲąęįų"                );
		setAccent("Caron  			", 0xcf, "CDELNRSTZcdelnrstz"		, "ČĎĚĽŇŘŠŤŽčďěľňřšťž"		);
	} // setAccents

    /** Gets the Unicode character for a pair of (accent, letter).
     *	@param accent accent which should be placed on the following letter
     *	@param letter letter to be accented
     *	@return resulting letter
     */
    public char getAccentedChar(char accent, char letter) {
    	char result = '?';
    	int ind = accent - 0xc0;
    	if (letters[ind] == null) {
    		return 'µ';
    	}
    	int pos = letters[ind].indexOf(letter);
    	if (pos >= 0) {
    		result = accents[ind].charAt(pos);
    	} else {
    		log.error("invalid letter " + letter + " behind accent " + accent);
    	}
		return result;
    } // getAccentedChar

	/** Array bound for both character tables */
	private static final int MAX_TABLE = 256;
    /** Maps from ISO 6937 characters to Unicode accented letters and special symbols */
    private char[] unicode;
    /** The inverse of <em>unicode</em>: maps from Unicode to ISO 6937 characters  */
    private char[] isocode;
    
	/** Sets the mappings for 6 columns of the code table
	 *	@param row the lower nibble of the code table index
	 *	@param cols the characters which occupy columns A through F of the
	 *	code table
	 */
	private void setCode(int row, char[] cols) {
		int ind = 0;
		int code = 0xa0 + row;
		while (ind < 6) {
			if (cols[ind] != '#') {
				unicode[code] = cols[ind];
				// isocode[cols[ind]] = (char) (code & 0xff);
			}
			ind ++;
			code += 0x10;
		} // while ind	
	} // setCode
	
	/** Sets the mappings for various special characters.
	 *  This code table is part of the table for Unicode characters
	 *  128 to 255. Irrelevant positions are filled with '#'.
	 */
	private void setCodes() {
		unicode = new char[MAX_TABLE];
		isocode = new char[MAX_TABLE];
    	int ind = 0;
    	while (ind < MAX_TABLE) { 
    		isocode[ind] = (char) 0; // preset table with nils
    		unicode[ind] = (char) 0; // preset table with nils
			ind ++;
    	} // while ind
		//        _             0xa_    0xb_    0xc_    0xd_    0xe_    0xf_
		setCode(0x0, new char[] {'#',	'°', 	'#',    '―', 	'Ω', 	'ĸ'});
		setCode(0x1, new char[] {'¡',   '±', 	'#', 	'¹', 	'Æ', 	'æ'});
		setCode(0x2, new char[] {'¢', 	'²', 	'#', 	'®', 	'Đ', 	'đ'});
		setCode(0x3, new char[] {'£',   '³', 	'#', 	'©', 	'ª', 	'ð'});
		setCode(0x4, new char[] {'#',   '×', 	'#',   	'™', 	'Ħ', 	'ħ'});
		setCode(0x5, new char[] {'¥', 	'µ', 	'#', 	'♪', 	'#',    'ı'});
		setCode(0x6, new char[] {'#',   '¶', 	'#',	'¬', 	'Ĳ', 	'ĳ'});
		setCode(0x7, new char[] {'§',   '·', 	'#', 	'¦', 	'Ŀ', 	'ŀ'});
		setCode(0x8, new char[] {'¤', 	'÷', 	'#',    '#',    'Ł', 	'ł'});
		setCode(0x9, new char[] {'‘', 	'’', 	'#',    '#',    'Ø', 	'ø'});
		setCode(0xa, new char[] {'“', 	'”', 	'#',    '#',    'Œ', 	'œ'});
		setCode(0xb, new char[] {'«', 	'»', 	'#',    '#',    'º', 	'ß'});
		setCode(0xc, new char[] {'←', 	'¼', 	'#',    '⅛', 	'þ', 	'Þ'});
		setCode(0xd, new char[] {'↑', 	'½', 	'#', 	'⅜', 	'Ŧ', 	'ŧ'});
		setCode(0xe, new char[] {'→', 	'¾', 	'#', 	'⅝', 	'Ŋ', 	'ŋ'});
		setCode(0xf, new char[] {'↓', 	'¿', 	'#', 	'⅞', 	'ŉ', 	'#'});
	} // setCodes
	
    /** Gets the Unicode character for a pair of (accent, letter) 
     *	@param letter letter to be accented
     *	@return resulting letter
     */
    public char getUnicode(char letter) {
    	char result = '?';
    	if (letter < 256) {
	    	char code = unicode[letter];
	    	if (code != 0) {
	    		result = code;
	    	} 
	    }
	    return result;
    } // getUnicode

    /** Gets the ISO 6937 character for a Unicode character, if a 
     *  one-to-one byte mapping exists, or nil otherwise
     *	@param code the Unicode character to be mapped
     *	@return resulting ISO 6937 character, 
     *	or nil if the mapping is not possible
     */
    public char getIsocode(char code) {
    	char result = 0x00;
    	boolean found = false;
        char ind = 0xa0;
	    if (result == 0) { 
	    	while (! found && ind < 0x100) {
	    		if (code == unicode[ind]) {
	    			found = true;
	    			result = ind;
	    		}
	    		ind ++;
	    	} // while ind
	    }
	    if (result == 0) { // not found in codes < 256 - look among accented lists
	    	ind = 1;
	    	found = false;
	    	while (! found && ind < 16) {
	    		int pos = accents[ind].indexOf(code);
	    		if (pos >= 0) {
	    			found = true;
	    			result = (char) (((0xc0 | ind) << 8) | letters[ind].charAt(pos));
	    		} 
	    		ind ++;
	    	} // while ind
	    } // not < 256
	    return result;
    } // getIsocode

} // ISO6937Map
