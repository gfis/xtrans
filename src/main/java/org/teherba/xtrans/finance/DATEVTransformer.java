/*  Transformer for DATEV accounting data files
    @(#) $Id: DATEVTransformer.java 566 2010-10-19 16:32:04Z gfis $
    Caution, this file contains UTF-8 encoded characters: äöüÄÖÜß
    2010-07-13: 'attrs' in 'generate' renamed to 'alist'
	2007-09-26: another sample file had no "f", "g" delimiters - totally revised
    2007-03-30: more SAX handler methods
    2006-09-19: Georg Fischer
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
import  org.teherba.xtrans.ByteRecord;
import  org.teherba.xtrans.ByteTransformer;
import	java.util.ArrayList;
import	java.util.HashMap;
import  org.xml.sax.Attributes;
import  org.xml.sax.SAXException;
import  org.apache.log4j.Logger;

/** Transforms DATEV accounting data files to/from XML.
 *  DE001 files consist of binary blocks of size 256 (0x100),
 *  filled with variable length accounting records, which in turn
 *	consist of a series of fields each terminated by a letter a - h, o, y and z.
 *	These letters must be in increasing order for an accounting record.
 *	After 'e' or 'g' there is a text field enclosed in 0x1e and 0x1c.
 *  The records are not spanned across block boundaries, but blocks
 *	are filled with nil bytes instead. 
 *	Example:
 *  <pre>
hex="1d 18 31 20 20 20 31 31 47 46 30 30 37 33 31 39" str="..1   11GF007319"
hex="36 31 33 30 38 30 30 30 30 30 30 31 30 31 30 31" str="6130800000010101"
hex="30 31 33 31 31 32 30 31 30 30 31 20 20 20 20 20" str="01311201001     "
hex="20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20" str="                "
hex="20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 79" str="               y"
hex="2b 32 34 36 38 30 39 34 61 30 30 30 39 30 30 30" str="+2468094a0009000"
hex="62 30 30 30 30 30 31 63 30 30 30 30 30 30 64 30" str="b000001c000000d0"
hex="31 30 31 65 30 31 38 31 30 66 30 30 30 30 67 30" str="101e01810f0000g0"
hex="30 30 30 1e 53 61 6c 64 6f 76 6f 72 74 72 61 67" str="000.Saldovortrag"
hex="1c 6f 32 79 2b 33 31 35 32 35 38 61 30 30 30 31" str=".o2y+315258a0001"
hex="36 30 30 62 30 30 30 30 30 32 63 30 30 30 30 30" str="600b000002c00000"
hex="30 64 30 31 30 31 65 30 39 30 30 30 66 30 30 30" str="0d0101e09000f000"
hex="30 67 30 30 30 30 1e 53 61 6c 64 6f 76 6f 72 74" str="0g0000.Saldovort"
hex="72 61 67 1c 6f 32 79  0  0  0  0  0  0  0  0  0" str="rag.o2y........."
hex=" 0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0" str="................"
hex=" 0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0" str="................"
                                                                            
hex="2d 33 31 35 32 35 38 61 32 30 30 31 36 30 30 62" str="-315258a2001600b"
hex="30 30 30 30 30 32 63 30 30 30 30 30 30 64 30 31" str="000002c000000d01"
hex="30 31 65 30 39 30 30 30 66 30 30 30 30 67 30 30" str="01e09000f0000g00"
hex="30 30 1e 3c 53 74 6f 72 6e 6f 3e 20 53 61 6c 64" str="00.<Storno> Sald"
hex="6f 76 6f 72 74 72 61 67 1c 6f 32 79 2b 36 32 34" str="ovortrag.o2y+624"
                                                                            
                                                                            
hex="2b 31 32 30 36 34 61 30 30 31 31 30 30 32 62 30" str="+12064a0011002b0"
hex="30 30 30 35 35 63 30 30 30 30 30 30 64 33 30 31" str="00055c000000d301"
hex="32 65 30 31 36 30 30 66 30 30 30 30 67 30 30 30" str="2e01600f0000g000"
hex="30 1e 41 2e 76 2e 48 6f 64 65 6e 62 65 72 67 20" str="0.A.v.Hagendorf "
hex="23 31 36 1c 6f 31 79 2d 31 61 32 30 37 30 30 30" str="#16.o1y-1a207000"
hex="33 62 30 30 31 30 30 39 63 30 30 30 30 30 30 64" str="3b001009c000000d"
hex="33 30 31 32 65 30 36 38 35 35 66 30 30 30 30 67" str="3012e06855f0000g"
hex="30 30 30 30 1e 52 75 6e 64 75 6e 67 73 64 69 66" str="0000.Rundungsdif"
hex="66 65 72 65 6e 7a 1c 6f 31 79 2b 31 61 30 30 30" str="ferenz.o1y+1a000"
hex="36 38 35 35 62 30 30 30 31 38 31 63 30 30 30 30" str="6855b000181c0000"
hex="30 30 64 33 31 31 32 65 30 31 38 30 30 66 30 30" str="00d3112e01800f00"
hex="30 30 67 30 30 30 30 1e 52 75 6e 64 75 6e 67 20" str="00g0000.Rundung "
hex="45 75 72 6f 2f 44 4d 1c 6f 31 79 78 33 30 39 37" str="Euro/DM.o1yx3097"
hex="30 32 36 39 33 79 7a  0  0  0  0  0  0  0  0  0" str="02693yz........."
hex=" 0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0" str="................"
hex=" 0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0" str="................"

resulting XML output:

&lt;datev&gt;
&lt;buch belnr=  "1" dat= "101" betr="+2468094" soll=   "9000" hab=   "1810" d="0" g="0" h="0" text="Saldovortrag" /&gt;
&lt;buch belnr=  "2" dat= "101" betr= "+315258" soll=   "1600" hab=   "9000" d="0" g="0" h="0" text="Saldovortrag" /&gt;
&lt;buch belnr=  "2" dat= "101" betr= "-315258" soll="2001600" hab=   "9000" d="0" g="0" h="0" text="<Storno&gt; Saldovortrag" /&gt;
&lt;buch belnr= "11" dat= "201" betr=   "+6240" soll=   "1810" hab=   "6430" d="0" g="0" h="0" text="Rundfunk" /&gt;
&lt;buch belnr= "21" dat= "301" betr= "+240000" soll=   "1810" hab=   "6310" d="0" g="0" h="0" text="Miete Perla 01-03/2001" /&gt;
&lt;buch belnr=  "1" dat= "901" betr=   "+7424" soll= "901600" hab=   "6815" d="0" g="0" h="0" text="Handsender" /&gt;
&lt;buch belnr=  "2" dat= "901" betr=  "+24190" soll= "901600" hab=    "670" d="0" g="0" h="0" text="Lautsprecher+Grafiktabletts" /&gt;
...

 *  </pre>
 *  @author Dr. Georg Fischer
 */
public class DATEVTransformer extends ByteTransformer {
    public final static String CVSID = "@(#) $Id: DATEVTransformer.java 566 2010-10-19 16:32:04Z gfis $";
    
    /** log4j logger (category) */
    private Logger log;
    
    /** Block size */
    private static final int BLOCK_SIZE = 256; // block size, several logical records in 1 block

    /** Inner class for the definition of a DATEV field */
    private class DATEVField {
        /** delimiting character (at the end of the field) */
        char delim;
        /** attribute name */
        String attrName;
        /** field width in bytes */
        int width;
        /** whether to remove leading zeroes */
        boolean trimZeroes;
        
        /** Constructor
         *  @param delim delimiting character (at the end of the field) 
         *  @param attrName attribute name 
         *  @param width field width in bytes 
         *  @param trimZeroes whether to remove leading zeroes
         */
        public DATEVField(char delim, String attrName, int width, boolean trimZeroes) {
            // log.debug("DATEVField(" + delim + "," + attrName + "," + width + "," + trimZeroes + ")");
            this.delim      = delim;
            this.attrName   = attrName;
            this.width      = width;
            this.trimZeroes = trimZeroes;
        }
    } // inner class DATEVField
    
    /** capacity for attributes */
    private static final int CAP_ATTR = 16;
    /** maps a field delimiter to the field definition */
    private HashMap/*<1.5*/<Character, DATEVField>/*1.5>*/ delimMap;
    /** maps an attribute name to the field definition */
    private HashMap/*<1.5*/<String, DATEVField>/*1.5>*/ attrMap;
    /** Root element tag */
    private static final String ROOT_TAG = "datev";
    /** Data element tag */
    private static final String DATA_TAG = "data";
    /** Header element tag */
    private static final String HEAD_TAG = "head";
    /** Footer element tag */
    private static final String FOOT_TAG = "foot";
    
    /** Defines a DATEV field and places proper references into the
     *	mapping arrays.
     *  @param delim delimiting character (at the end of the field) 
     *  @param attrName attribute name 
     *  @param width field width in bytes 
     *  @param trimZeroes whether to remove leading zeroes
     */
    private void defineField(char delim, String attrName, int width, boolean trimZeroes) {
		DATEVField field = new DATEVField(delim, attrName, width, trimZeroes);
		delimMap.put(new Character(delim), field);
		attrMap .put(attrName            , field);
    } // defineField
    
    /** String of 64 spaces for padding; all width specifications below must be smaller than its length */
    private static final String SPACES = "                                                                ";
    
    /**	No-args Constructor
     */
    public DATEVTransformer() {
        super();
        setFormatCodes("datev");
        setDescription("DATEV accounting file (DE001)");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(DATEVTransformer.class.getName());
    	delimMap = new HashMap/*<1.5*/<Character, DATEVField>/*1.5>*/(16);
    	attrMap  = new HashMap/*<1.5*/<String   , DATEVField>/*1.5>*/(16);
        defineField('a'        , "amt"         , 0, true ); 
        defineField('b'        , "debt"        , 7, true );
        defineField('c'        , "nr"          , 6, true );
        defineField('d'        , "nr"          , 6, true );
        defineField('e'        , "date"        , 4, false);
        defineField('f'        , "cred"        , 5, true );
        defineField('g'        , "g"           , 4, true );
        defineField('\u001e'   , "cred"        , 4, true );
        defineField('\u001c'   , "text"        , 0, false);
        defineField('o'        , "o"           , 1, false);
        defineField('x'        , "checksum"    , 0, false);
        defineField('y'        , "y"           , 0, false);
        defineField('z'        , "z"           , 0, false);

        putEntityReplacements(); // "
        putReplacementMap("\""      , "&quot"   ); // "
        putReplacementMap("\u0084"  , "ä"      ); // ae
        putReplacementMap("\u0094"  , "ö"      ); // oe
        putReplacementMap("\u0081"  , "ü"      ); // ue
        putReplacementMap("\u008e"  , "Ä"      ); // Ae
        putReplacementMap("\u0099"  , "Ö"      ); // Oe
        putReplacementMap("\u009a"  , "Ü"      ); // Ue
        putReplacementMap("\u00e1"  , "ß"      ); // ss
	} // initialize
	
    /*===========================*/
    /* Generator for SAX events  */
    /*===========================*/

    /** Block with several accounting records */
    private ByteRecord blockRecord;
    /** sum of amounts ("x" field in last block) */
    private long controlSum;
    /** array for attributes corresponding to DATEV field definitions */
    private ArrayList/*<1.5*/<String>/*1.5>*/ alist;
    
    /** Evaluates the file header (80 bytes) and writes a
     *  <em>head</em> element. Example for a header:
     *  <pre>
        hex="1d 18 31 20 20 20 31 31 47 46 30 30 37 33 31 39" str="..1   11GF007319"
        hex="36 31 33 30 38 30 30 30 30 30 30 31 30 31 30 31" str="6130800000010101"
        hex="30 31 33 31 31 32 30 31 30 30 31 20 20 20 20 20" str="01311201001     "
        hex="20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20" str="                "
        hex="20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 79" str="               y"
     *  </pre>
     */
    private void evalHeader() {
    	blockRecord.setPosition(0);
        fireEmptyElement(HEAD_TAG, toAttributes(new String[] 
                { "part1"   , blockRecord.getString(0x02,  8)
                , "mandant" , blockRecord.getString(0x0a,  7)
                , "part2"   , blockRecord.getString(0x11, 26) 
                }));
        fireLineBreak();
    }
    
    /** Removes leading characters from a string, but keeps the last (zero).
     *  @param ch character to be removed
     *	@param value string to be modified
     *  @return modified string
     */
    public String trimLeading(char ch, String value) {
    	int pos = 0;
    	while (pos < value.length() - 1 && value.charAt(pos) == ch) {
    		pos ++;
    	}
    	return value.substring(pos);
    } // trimLeading
    
    /** state when a number is expected */
    private static final int IN_NUMBER = 1;
    /** state when a string is expected */
    private static final int IN_STRING = 2;
    /** state at the end of an accounting record */
    private static final int BLOCK_FILLER = 3;
    /** state at the end of the file, while in checksum */
    private static final int IN_CHECKSUM = 4;
    
    /** Êvaluates a field depending on its trailing delimiter,
     *	and emits appropriate XML tags.
     *  @param delimiter the single non-numeric character behind the field
     *	@param value the numerical (or text) value of the field
     *  @return subsequent state of finite automaton
     */
    private int evaluateField(char delimiter, String value) {
        int result = IN_NUMBER;
        DATEVField field = (DATEVField) delimMap.get(new Character(delimiter));
        switch (delimiter) {
        	case '\u0000':
        		result = BLOCK_FILLER;
        		break;
        	case 'a':
	            long amount = 0;
    	        try {
        	        amount = Long.parseLong(value.startsWith("+") ? value.substring(1) : value);
            	} catch (Exception exc) {
            	}
            	controlSum += amount;
            	// fall thru
        	case 'b':
        	case 'c':
        	case 'd':
        	case 'e':
        	case 'f':
        	case 'g':
			case 'o':
		        alist.add(field.attrName);
				alist.add(field.trimZeroes ? trimLeading('0', value) : value);
        		break;
        	case '\u001e':
		        alist.add(field.attrName);
				alist.add(field.trimZeroes ? trimLeading('0', value) : value);
				result = IN_STRING;
        		break;
        	case '\u001c':
		        alist.add(field.attrName);
        		alist.add(replaceInSource(value));
				break;
        	case 'x':
       			alist = new ArrayList/*<1.5*/<String>/*1.5>*/(CAP_ATTR);
        		result = IN_CHECKSUM;
        		break;
        	case 'y':
		        alist.add(field.attrName);
				alist.add(value);
                fireEmptyElement(DATA_TAG, toAttributes(alist));
                fireLineBreak();
       			alist = new ArrayList/*<1.5*/<String>/*1.5>*/(CAP_ATTR);
        		result = IN_NUMBER;
        		break;
        	case 'z':
        		result = BLOCK_FILLER;
        		break;
			default:
				log.error("invalid delimiter " + delimiter);
				break;
		} // switch delimiter
        return result;
    } // evaluateField
    
    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        char delimiter = '0';
        blockRecord = new ByteRecord(BLOCK_SIZE);
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            int blockNo = 0;
            int len;
            controlSum = 0;
            while ((len = blockRecord.read(byteReader)) >= 0) {
                // read and process a block from the input file
                blockNo ++;
                byte [] block = blockRecord.getBuffer();
                int spos = 0;
                if (blockNo == 1) {
                    evalHeader();
                    spos = 0x50; // start after header in 1st block
                }
				alist = new ArrayList/*<1.5*/<String>/*1.5>*/(CAP_ATTR);
				int state = IN_NUMBER;
				StringBuffer value = new StringBuffer(128);
				char ch = '0';
                while (spos < len) {
           			ch = (char) (block[spos ++] & 0xff);
                	switch (state) {
                		case IN_NUMBER:
                			if (ch >= '+' && ch <= '9') {
                				value.append(ch);
                			} else { // end of field reached
                				state = evaluateField(ch, value.toString());
                				value.setLength(0);
                			}
                			break; // IN_NUMBER
                				
                		case IN_CHECKSUM:
                			if (ch >= '+' && ch <= '9') {
                				value.append(ch);
                			} else { // y - end of field reached
		                        fireEmptyElement(FOOT_TAG, toAttributes(new String[]
        		                        { "sum", value.toString()
                		                , "computed", String.valueOf(controlSum)
                        		        }));
		                        fireLineBreak();
                				value.setLength(0);
                        		state = IN_NUMBER;
                			}
                			break; // IN_CHECKSUM
                				
                		case IN_STRING:
                			if (ch != '\u001c') {
                				value.append(ch);
                			} else { // end of field reached
                				state = evaluateField(ch, value.toString());
                				value.setLength(0);
                			}
                			break; // IN_STRING
                			
                		case BLOCK_FILLER:
                			spos = len; // force reading of next block
                			state = IN_NUMBER;
                			break;
                			
                		default:
                			System.out.println("invalid state " + state);
                			break;
                	} // switch state
                } // while spos
            } // while reading input file
            fireEndElement(ROOT_TAG);
            fireEndDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return  result;
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

	/** computed sum of amounts */
	private long saxSum;
	/** buffer for the assembly of a new block */
	private ByteRecord saxBlock;
	
    /** Puts a DATEV field with a trailing delimiter into the result
     *  @param attrs attributes of the XML element
     *  @param field DATEV field definition
     *  @return field value plus trailing delimiter
     */
    private String putDATEVField(Attributes attrs, DATEVField field) {
        StringBuffer result = new StringBuffer(128);
        String value = attrs.getValue(field.attrName);
        if (value == null) {
            value = "";
        }
        if (field.trimZeroes) { // numeric
            // log.debug(field.delim + ", result.length=" + result.length() + ", value.length= " + value.length() + ", field.width=" + field.width);
        /* omit padding    
            while (result.length() + value.length() < field.width) {
                result.append('0');
            }
        */
            result.append(value);
        } else { // alphanumeric
            result.append(replaceInResult(value));  
        } // alphanumeric
        result.append(field.delim);

        if (field.delim == 'a') { // amount field
            long amount = 0;
            try {
                amount = Long.parseLong(value.startsWith("+") ? value.substring(1) : value);
            } catch (Exception exc) {
            }
            saxSum += amount;
        }
        return result.toString();
    } // putDATEVField
    
    /** Gets an attribute, and left pads it with some character to a specified length
     *  @param attrs attributes of the XML element
     *  @param name name of the attribute to be accessed
     *  @param width prefix the value with some spaces to make it at least <em>width</em> characters wide
     *  @param pad character to be used for left padding
     */
    private void putAttribute(Attributes attrs, String name, int width, char pad) {
        StringBuffer result = new StringBuffer(32);
        result.setLength(0);  
        String value = attrs.getValue(name);
        if (value == null) {
            value = "";
        }
        while (result.length() + value.length() < width) {
            result.append(pad);
        }
        result.append(value);
        // result.append(name);
        saxBlock.setString(width, result.toString());
    } // putAttribute
    
    /*===================*/
    /* DATEV SAX Handler */
    /*===================*/

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
    	saxSum   = 0;
        saxBlock = new ByteRecord(BLOCK_SIZE);
    } // startDocument

    /** Receive notification of the start of an element.
     *  Pass processing to generated code in <em>DTARecordBase</em>.
     *  @param uri The Namespace URI, or the empty string if the element has no Namespace URI 
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix), 
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix), 
     *  or the empty string if qualified names are not available.
     *  @param attrs the attributes attached to the element. 
     *  If there are no attributes, it shall be an empty Attributes object.
     *  @throws SAX Exception
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs) 
            throws SAXException {
		DATEVField field = null;
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        try {
            if (false) {
            } else if (qName.equals(ROOT_TAG)) {
            } else if (qName.equals(HEAD_TAG)) {
                saxBlock.setPosition(0);
                saxBlock.setString(0, 2, "\u001d\u0018");
                putAttribute(attrs, "part1"   ,  8, '0');
                putAttribute(attrs, "mandant" ,  7, '0');
                putAttribute(attrs, "part2"   , 26, '0');
                saxBlock.fill(' '); // too much
                saxBlock.setString(0x4f, 1, "y"); // filled up to 0x50
            } else if (qName.equals(DATA_TAG)) {
                StringBuffer entry = new StringBuffer(128);
				int nattr = attrs.getLength();
				int iattr = 0;
				while (iattr < nattr) {
					String name  = attrs.getLocalName(iattr);
					field = (DATEVField) attrMap.get(name);
					if (field != null) {
	                    entry.append(putDATEVField(attrs, field));
					} else {
						log.error("unknown attribute " + name);
					}
					iattr ++;
				} // while iattr
                int len = entry.length();
                // log.debug(entry.toString());
                if (saxBlock.getPosition() + len + 20 > BLOCK_SIZE) { // +12 for x...z entry (checksum)
                    // first write current block
                    saxBlock.fill('\u0000');
                    saxBlock.write(byteWriter, BLOCK_SIZE);
                    saxBlock.setPosition(0);
                }
                saxBlock.setString(len, entry.toString());
            } else if (qName.equals(FOOT_TAG)) {
            }
        } catch (Exception exc) {
            throw new SAXException("error in startElement ", exc);
        }
    }

    /** Receive notification of the end of the document.
     */
    public void endDocument() {
        String result = "x" + saxSum + "yz";
        saxBlock.setString(result.length(), result);
        saxBlock.fill('\u0000');
        saxBlock.write(byteWriter, BLOCK_SIZE);
    }
}
