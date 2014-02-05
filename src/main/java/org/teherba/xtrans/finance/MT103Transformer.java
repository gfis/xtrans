/*  Transforms the S.W.I.F.T. FIN MT103 message type
    @(#) $Id: MT103Transformer.java 566 2010-10-19 16:32:04Z gfis $
    2007-10-11, Dr. Georg Fischer: copied from MT940Transformer.java
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
import  org.apache.log4j.Logger;

/** Transformer for the SWIFT FIN MT103 message type.
 *  Examples for an MT103:
 *  <pre>
{1:F01BICFOOYYAXXX8682497001}{2:O1030803051028ESPBESMMAXXX54237368340510280803N}{3:{113:NOMF}{108:0510280086100047}{119:STP}}{4:
:20:D051025EUR100047
:13C:/RNCTIME/123123123+0000
:23B:CRED
:32A:051028EUR724297,95
:33B:EUR724297,95
:50A:ALLFESMMXXX
:53A:NORTESMMXXX
:57A:BICFOOYYXXX
:59:/ES0123456789012345671234
MS MULTIGESTION CONSERVADOR FIMF
:70:REDEMPTS. TRADEDATE 2005-10-25
/123123123: MS MULTIGESTION CONSER
:71A:SHA
-}{5:{MAC:766F7678}{CHK:45718FA95A25}}
 *  </pre>
 *	All tags on top  level must be uppercase, 
 * 	tag for substructures  must be lowercase (inserted by filter).
 *
 *  @author Dr. Georg Fischer
 */
public class MT103Transformer extends SWIFTTransformer { 
    public final static String CVSID = "@(#) $Id: MT103Transformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** No-args constructor
     */
    public MT103Transformer() {
        super();
        setFormatCodes("mt103");
        setDescription("SWIFT FIN MT103 message (Payment)");
    } // Constructor
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(MT103Transformer.class.getName());
	} // initialize
	
    /*==================*/
    /* Filter for MT103 */
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
		if (type.equals("103")) {
		/*
			System.out.println("dir=" + dir + ", type=" + type + ", tag=" + tag 
					+ ", lineTag=" + lineTag + ", lineCount=" + lineCount);
		*/
			int iline;
			if (false) {
			} else if (tag.startsWith("F20" )) { // reference
			} else if (tag.startsWith("F23" )) { // direction: CRED
			} else if (tag.startsWith("F32A")) { // date, ccy, amt
				fireDate(value.substring(0, 6));
				fireSimpleElement("ccy", value.substring(6, 9));
				fireAmt(value.substring(9   ));
				value = "";
			} else if (tag.equals    ("F33B") || tag.equals("71F")) { // ccy, amt
				fireSimpleElement("ccy"   , value.substring(0, 3));
				fireAmt(value.substring(3   ));
				value = "";
			} else if (tag.startsWith("F50K") 
					|| tag.startsWith("F57D")
					|| tag.equals    ("F59" ) ) 
			{ // acct, adr[1..n]
				fireSimpleElement("acct", value);
				iline = 1;
				while (iline < lines.size()) {
					fireEmptyElement(NEWLINE_TAG);
					fireLineBreak();
					fireSimpleElement("adrLine", lines.get(iline ++));
				} // while iline
				value = "";
			} else if (tag.startsWith("F52A")) { // BIC
			} else if (tag.startsWith("F53A")) { // BIC
			} else if (tag.startsWith("F57A")) { // BIC
			} else if (tag.equals    ("F70" )) { // inf[1..n]
				fireSimpleElement("inf", value);
				iline = 1;
				while (iline < lines.size()) {
					fireEmptyElement(NEWLINE_TAG);
					fireLineBreak();
					fireSimpleElement("inf", lines.get(iline ++));
				} // while iline
				value = "";
			} else if (tag.equals    ("F71A")) { // ChrgBrTp
			}
		} // type = 103
    	return value; // transparent implementation
    } // filter      

} // MT103Transformer
