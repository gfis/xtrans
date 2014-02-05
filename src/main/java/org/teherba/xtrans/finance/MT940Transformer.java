/*  Transforms the S.W.I.F.T. FIN MT940 message type
    @(#) $Id: MT940Transformer.java 566 2010-10-19 16:32:04Z gfis $
    2007-09-28, Dr. Georg Fischer: copied from SWIFTTransformer.java
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

package org.teherba.xtrans.finance;
import  org.teherba.xtrans.finance.SWIFTTransformer;
import	java.util.ArrayList;
import  java.util.regex.Matcher;
import  java.util.regex.Pattern;
import  org.apache.log4j.Logger;

/** Transformer for the SWIFT FIN MT940 (statement) message type.
 *  Examples for an MT940:
 *  <pre>
{1:F01OWHBDEFFAXXX9999999999}{2:I940 N}{4:
:20:FTAMIMGE
:25:68050101/195101015600
:28:1/1
:60F:C900214DEM0,
:61:900212D54,72NMSC602070012248//97186/034
:86:999DTA/REF./NR. 0602070012248 FIRMA FREUEDICH GMBH U.CO
SEI-GLUECKLICH-STR.1 298 84 02
 TEXT DATUM DM NETTO
:61:900212D8247,53NMSC195101018000//97153/074
:86:999ACMS W/195 1010180 00
:61:900214D65,94NCHK4295339691//95186/033
:86:999 602080032863 BSE/NR 0000003014/REF30726020872
820070050 20070050000100050040000
:61:900214D702,24NCHK4296339599//95186/033
:86:999 602080039334 BSE/NR 0008906519/REF31837232235
776020070 76020070000100050040000
:61:900214D26168,94NMSC195101018000//97153/074
:86:999ACMS W/195 1010180 00
:61:900214D12223,43NMSC195101018000//97153/074
:86:999ACMS W/195 1010180 00
:61:900214D44736,10NMSC195101018000//97153/074
:86:999ACMS W/195 1010180 00
:61:900214D145109,11NMSC195101018000//97153/074
:86:999ACMS W/195 1010180 00
:61:900212C18,32NTRF672070095529//97261/039
:86:999DTA/REF./NR. 0672070095529 KURT FELIX  9999 SONSTWO   
FA.IRGENDWAS 9999 SONSTWO    7995000000107009731251000500400
00
:61:900212C248,29NTRF672070095528//97261/039
:86:999DTA/REF./NR. 0672070095528 AUTOFABRIK,  TRABANTER STR.
 100, OPELHEIM GUMMIREIFEN-DIENSTE  GMBH,
 790500510000236907035100050040000
:61:900212C1296,50NTRF672070095530//97261/039
:86:999DTA/REF./NR. 0672070095530 AUTO HANS-OTTO INHABER EMIL
9998 SIEGESSTADT-GEWINNEN   GUMMI-REIFENDIENSTE   GMBH
 775518609411000095036200050040000
:61:900212C1850,64NTRF672070095531//97261/039
:86:999DTA/REF./NR. 0672070095531 AUGUST AUGUSTIN POSTFACH 99
99  1111 JUBELHAUSEN 1   FA.GUMMI
 773600109480000035126100050040000
:61:900212C4888,50NTRF672070098321//97261/039
:86:999DTA/REF./NR. 0672070098321 FA. GUTE ARBEIT KG SCHOENDO
RF GUMMIREIFEN-DIENST GMBH 7905005100999208020151000500400
00
:62M:D900214DEM229005,76
:64:D900214DEM229005,76
-}
 *  </pre>
 *	All tags on top  level must be uppercase, 
 * 	tags for substructures must be lowercase (inserted by <em>filter</em>).
 *
 *  @author Dr. Georg Fischer
 */
public class MT940Transformer extends SWIFTTransformer { 
    public final static String CVSID = "@(#) $Id: MT940Transformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** No-args constructor
     */
    public MT940Transformer() {
        super();
        setFormatCodes("mt940");
        setDescription("SWIFT FIN MT940 message (Report)");
    } // Constructor
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(MT940Transformer.class.getName());
	} // initialize
	
    /*==================*/
    /* Filter for MT940 */
    /*==================*/

    /** Pseudo-abstract method for further substructuring of a field
     *	in a derived class
     *  @param type type of the SWIFT FIN message, e.g. 940
     *	@param dir message transfer direction as seen from SWIFT: "I" or "O"
     *	@param tag field designator, e.g. "B3", "F20"
     *	@param lines content of the field to be substructured, maybe with several continuation lines
     *	@return unchanged value or the empty string if substructure was already emitted
     */ 
    public String filter(String dir, String type, String tag, ArrayList/*<1.5*/<String>/*1.5>*/ lines) {
		String value = lines.get(0);
		int pos;
		if (type.equals("940")) {
		/*
			System.out.println("dir=" + dir + ", type=" + type + ", tag=" + tag 
					+ ", lineTag=" + lineTag + ", lineCount=" + lineCount);
		*/
			int iline;
			if (false) {
			} else if (tag.startsWith("F13D")) { // creation date, time and timezone :13D:07062905:30+0100
				fireTimestamp(value);
			} else if (tag.startsWith("F20" )) { // reference
			} else if (tag.startsWith("F25" )) { // reference
			} else if (tag.startsWith("F28" )) { // statement number / page number
				String[] nr = value.split("\\/");
				if (nr.length >= 2) {
					fireSimpleElement("reportNo", nr[0]);
					fireCharacters("/");
					fireSimpleElement("pageNo" , nr[1]);
					value = "";
				}
			} else if (tag.startsWith("F60" ) // balances 
					|| tag.startsWith("F62" )
					|| tag.startsWith("F64" )
					|| tag.startsWith("F65" )
					) { // balances
				// :62M:D900214DEM229005,76
				//      01234567890123456789
				fireSign(value.substring(0, 1));
				fireDate(value.substring(1, 7));
				fireSimpleElement("ccy",  value.substring(7, 10));
				fireAmt (value.substring(10));
				value = "";
			} else if (tag.startsWith("F61" )) { // transaction detail
				if (lines.size() >= 1) { // evaluate 1st line
					pos = 0;
					String yymmdd = value.substring(pos,  pos + 6);
					fireDate(yymmdd); // valuta date
					pos += 6;
					if (Character.isDigit(value.charAt(pos))) {
						fireShortDate(value.substring(pos, pos + 4), yymmdd); // booking date
						pos += 4;
					}
					if (value.startsWith("R", pos)) { // reversal indicator
						fireSimpleElement("rvsl", value.substring(pos, pos + 1));
						pos ++;
					} // reversal
					fireSign(value.substring(pos, pos + 1)); // 'C'redit or 'D'ebit
					pos += 1;
					int epos = pos;
					while (epos < value.length() && ! Character.isLetter(value.charAt(epos))) {
						epos ++;
					}
					fireAmt(value.substring(pos, epos));  // amount
					fireLineBreak();
					
					pos = epos;
					if (pos + 4 <= value.length()) {
						fireSimpleElement("txcd1", value.substring(pos + 0, pos + 1));
						fireSimpleElement("txcd2", value.substring(pos + 1, pos + 4));
						pos += 4;
						int slash = value.indexOf("//", pos);
						if (slash >= 0) {
							fireSimpleElement("ref", value.substring(pos, slash));
							fireCharacters("//");
							pos = slash + 2;
							if (pos < value.length()) {
								fireSimpleElement("ref2", value.substring(pos));
							}
						} // with "//"
					}
					value = "";
				} // 1st line
				if (lines.size() >= 2) { // evaluate 2nd line
					fireEmptyElement(NEWLINE_TAG); // separated, not terminated by newlines
					fireLineBreak();
					value = lines.get(1); 
					pos = 0;
					while (pos < value.length() && value.charAt(pos) == '/') {
						if (false) {
						} else if (value.startsWith("/OCMT/", pos)) { // original commitment amount
							pos = fireSubfield61(value, pos);
						} else if (value.startsWith("/CHGS/", pos)) { // charges
							pos = fireSubfield61(value, pos);
						} else {
							fireSimpleElement("msgid", value.substring(pos));
							pos = value.length();
						}
					} // while pos
					value = "";
				} // 2nd line
				// :61:
			} else if (tag.startsWith("F86" )) { // rmt inf, possibly with ZKA subfields
				if (value.indexOf("?00") == 3) { // ZKA structure
					splitZKA(lines);
				    // ZKA structure
				} else { // non-ZKA
					fireSimpleElement("inf", value);
					iline = 1;
					while (iline < lines.size()) {
						fireEmptyElement(NEWLINE_TAG); // separated, not terminated by newlines
						fireLineBreak();
						fireSimpleElement("inf", lines.get(iline ++));
					} // while iline
				} // non-ZKA
				value = ""; 
				// :86:
			} // if F-tag
		} // type = 940
    	return value; // transparent implementation
    } // filter      
    
    /** Extracts a subfield of :61:, and increments the current position.
     *  The subfield has the form /aaaa/cccnnn...n,n/ with code, currency and amount
     *	@param value remaining value of field :61:
	 *  @param pos starting position in <em>value</em>
     *  @return new position behind the extracted subfield
     */
    private int fireSubfield61(String value, int pos) {
    	pushXML(value.substring(pos + 1, pos + 5).toLowerCase());
    	pos += 6;
    	if (pos + 3 <= value.length()) {
    		fireSimpleElement("ccy", value.substring(pos, pos + 3));
    		pos += 3;
    		int slashPos = value.indexOf('/', pos);
    		if (slashPos >= 0) {
    			fireAmt(value.substring(pos, slashPos));
    			pos = slashPos + 1;
    		} // amount present
    	} // currency present
    	popXML();
    	return pos;
	} // fireSubfield61

	/** Matches a ZKA subfield with a number and an optional codeword (2 digits, word+) */
	private static final Pattern ZKA_WORD = Pattern.compile("(\\d\\d)([A-Z]{4}\\+|)");
	
    /** Treats the :86: lines as one long string and
     *	splits them on ?nn into subfields as specified by 
     *	the German "Zentraler Kreditausschuss" (ZKA).
     *	All subfields are output with "Znn" tags and unchanged content.
     *	At this point, the output of content is inhibited for the inverse conversion,
     *	by using a special VOID tag.
     *	Thereafter, the content of ?20..?29,?60..?63 is repeated.
     *	It is broken into structured elements like EREF, KREF etc.,
     *	followed by the concatenation of all unstructured parts.
     *	@param lines content of the :86: field, one element per line
     */
    private void splitZKA(ArrayList/*<1.5*/<String>/*1.5>*/ lines) {
    	StringBuffer ustrdBuffer = new StringBuffer(1024); // all lines concatenated
		StringBuffer strdBuffer = new StringBuffer(1024);
		String sfno; // number of subfield
		String word; // codeword, e.g. "EREF+"
		String text; // rest of subfield behind word or number
		String sep = "@"; // must be different from '?', and any other SWIFT character
    	int iline = 0;
    	int len = lines.size();
    	while (iline < len) {
    		ustrdBuffer.append(lines.get(iline ++));
    	} // while

		String[] subFields = ustrdBuffer.toString().split("\\?");
		ustrdBuffer.setLength(0); // recycle it for concatenation of unstructured parts in ?20..?29 and ?60..?63
		boolean structured = false;
		int isub = 0;
		while (isub < subFields.length) {
			if (isub == 0) { // first subField - not prefixed by "?"
				fireSimpleElement("gvc", subFields[isub]);
				fireLineBreak();
			} else {  // real subField
				Matcher matcher = ZKA_WORD.matcher(subFields[isub]);
				if (matcher.lookingAt()) { // matches ("20", "EREF+") or ("20", "")
					sfno = matcher.group(1);
					word = matcher.group(2); 
					text = subFields[isub].substring(sfno.length() + word.length());
					pushXML(ZKA_TAG + sfno);
						fireCharacters(text);
					popXML();
					fireLineBreak();
					if (sfno.startsWith("2") || sfno.startsWith("6")) {
						if (word.length() > 0) { // with word
							structured = true;
							strdBuffer.append(sep);
							strdBuffer.append(word);
							strdBuffer.append(sep);
							strdBuffer.append(text);
						} else { // without word
							if (structured) {
								strdBuffer.append(text); // continuation, but only once
								structured = false;
							} else {
								ustrdBuffer.append(text);
							}
						} // without word
					} 
				} else { // no ?nn ZKA subfield number - single '?'
					ustrdBuffer.append('?');
					ustrdBuffer.append(subFields[isub]);
				} // no ZKA subfield
			} // real subField
			isub ++;
		} // while isub
		
		// now repeat the content in structured elements
		pushXML(VOID_TAG);
			subFields = strdBuffer.toString().split(sep);
			isub = 1; // there is a leading 'sep' and therefore an empty initial field
			while (isub < subFields.length) {
				word = subFields[isub ++];
				text = subFields[isub ++];
				fireSimpleElement(word.toLowerCase().replaceAll("\\+",""), text);
				fireLineBreak();
			} // while isub
			fireSimpleElement("ustrd", ustrdBuffer.toString());
		popXML(); // VOID_TAG
    } // splitZKA

} // MT940Transformer
