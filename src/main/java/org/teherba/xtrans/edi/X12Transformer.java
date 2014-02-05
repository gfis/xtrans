/*  Transforms ANSI ASC X12 trade interchange messages
    @(#) $Id: X12Transformer.java 566 2010-10-19 16:32:04Z gfis $
    2010-06-23: initialize -> setEditSeparators
    2008-07-10, Dr. Georg Fischer: derived from EdifactTransformer
    caution, encoded in UTF-8: äöüÄÖÜß
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

package org.teherba.xtrans.edi;
import  org.teherba.xtrans.edi.EdifactTransformer;
import	java.util.ArrayList;
import  org.apache.log4j.Logger;

/** Transformer for ANSI ASC X12 trade interchange messages.
 *	This class only handles the raw syntax of X12 messages and the separator 
 *	characters. The higher level structures of groups, messages and all code lists
 *	must be treated separately.
 *	<p>
 *	Segments are transformed to elements with the 3-character (uppercase) name as XML tag,
 *	and they start on a new line.
 *	All other elements have lowercase XML tags. 
 *	Data elements are converted to numbered &lt;d<em>i</em>&gt; elements.
 *	Component data elements also become numbered &lt;c<em>i</em>&gt;, <em>i</em>=1,2,3... .
 *	<p>
 *  Example:
 *  <pre>
ISA*00*          *00*          *ZZ*SUBMITTERS ID  *ZZ*RECEIVERS ID   *010122*1253*U*00401*000000905*1*T*:~
GS*HC*SenderID*ReceiverID*20010122*1310*1*X*004010X098~
ST*837*0021~
BHT*0019*00*0123*19981015*1023*RP~
REF*87*004010X098D~
NM1*41*2*JAMES A SMITH, M.D.*****46*TGJ23~
PER*IC*LINDA*TE*8015552222*EX*231~
NM1*40*2*ABC CLEARINGHOUSE*****46*66783JJT~
HL*1**20*1~
NM1*85*2*JAMES A SMITH, M.D.*****24*587654321~
N3*234 Seaway St~
N4*Miami*FL*33111~
REF*1C*99983000~
HL*2*1*22*0~
SBR*S*18***15****MB~
NM1*IL*1*SMITH*TED****MI*000221111A~
N3*236 N MAIN ST~
N4*MIAMI*FL*33413~
DMG*D8*19230501*M~
NM1*PR*2*MEDICARE-BLUE SHIELD OF NORTH DAKOT*****PI*741234~
N2*A~
CLM*26462967*100***11::1*Y*A*Y*Y*C~
DTP*431*D8*19981003~
REF*D9*17312345600006351~
HI*BK:73314*BF:80010~
NM1*82*1*KILDARE*BEN****34*112233334~
PRV*PE*ZZ*203BF0100Y~
SBR*P*32***C1****CI~
CAS*PR*1*21.89**3*15~
AMT*D*12.15~
AMT*F2*26.89~
DMG*D8*19430501*F~
OI***Y*B**Y~
NM1*IL*1*SMITH*JANE****MI*JS00111223333~
N3*236 N MAIN ST~
N4*MIAMI*FL*33111~
NM1*PR*2*KEY INSURANCE COMPANY*****PI*999996666~
DTP*573*D8*19990101~
LX*1~
SV1*HC:99213*45*UN*1***1**N~
DTP*472*D8*19981003~
LX*2~
SV1*HC:99214*55*UN*1***1**N~
DTP*472*D8*19981003~
SE*43*0021~
GE*1*1~
IEA*1*000000905~
</pre>
 *  @author Dr. Georg Fischer
 */
public class X12Transformer extends EdifactTransformer { 
    public final static String CVSID = "@(#) $Id: X12Transformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    /** system.dependant newline string */
    protected String newline;
        
    /** No-args constructor
     */
    public X12Transformer() {
        super();
        setFormatCodes("x12");
        setDescription("ANSI ASC X12 message interchange");
    } // Constructor
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(X12Transformer.class.getName());
	} // initialize
	
    /*================*/
    /* Edifact parser */
    /*================*/

    /** Initializes the parser, sets the format separtors
     */
    protected void setEdiSeparators() {
		dataElementSeparator= '*';
		componentDataElementSeparator= ':';
		releaseIndicator    = '\\';
		repetitionSeparator = ' ';
		decimalNotation 	= '.';
		segmentTerminator   = '~';
    } // setEdiSeparators
    
    /** Pseudo-abstract method for further substructuring of a field
     *	in a derived class, with an array of continuation lines
     *  @param type type of the Edifact FIN message, e.g. 940
     *	@param dir message transfer direction as seen from Edifact: "I" or "O"
     *	@param tag field designator, e.g. "B3", "F20"
     *	@param lines content of the field to be substructured, 
     *	with continuation lines in following array elements
     *	@return unchanged value or the empty string if substructure was already emitted
     */ 
    protected String filter(String dir, String type, String tag, ArrayList/*<1.5*/<String>/*1.5>*/ lines) {
    	// transparent implementation: concatenation of all lines, separated by newline
    	StringBuffer result = new StringBuffer(1024);
    	int iline = 0;
    	int len = lines.size();
   		fireCharacters(lines.get(iline ++));
    	while (iline < len) {
    		fireEmptyElement(NEWLINE_TAG);
	   		fireCharacters(lines.get(iline));
	    	iline ++;
    	} // while
    	return result.toString();
    } // filter
        
    /*=====================*/
    /* Edifact SAX Handler */
    /*=====================*/

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
    	inhibitChars = true;
		saxComponentDataElementSeparator	= ':';
		saxDataElementSeparator 			= '*';
		saxDecimalNotation		 			= '.';
		saxReleaseIndicator	 				= '\\';
		saxRepetitionSeparator 				= ' ';
		saxSegmentTerminator	 			= '~';
    } // startDocument
    
} // X12Transformer
