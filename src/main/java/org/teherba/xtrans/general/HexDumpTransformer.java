/*  Transforms to/from a hexdump of a (binary) file
    @(#) $Id: HexDumpTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2007-03-30: more SAX handler methods
    2006-09-20, Dr. Georg Fischer <punctum@punctum.com>
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
import  org.teherba.xtrans.ByteTransformer;
import  org.teherba.xtrans.ByteRecord;
import  org.xml.sax.Attributes;
import  org.xml.sax.SAXException;
import  org.apache.log4j.Logger;

/** Transformer for the hexdump of a (binary) file. 
 *  Read the binary file into portions of 16 bytes,
 *  and generate one XML element for each such portion.
 *  This class may be useful for test outputs, and for the
 *  editing of binary files via a manipulation of the XML representation.
 *  Attributes are:
 *  <ul>
 *  <li>the offset in the file</li>
 *  <li>hexadecimal representation of 16 bytes (with "00" replaced by "..")</li>
 *  <li>followed by the same bytes as human readable characters where possible ("-" otherwise)</li>
 *  </ul>
 *  Parameters are the <em>width</em> (default 16), and
 *  the <em>code</em> for the human readable representation 
 *  which can be <em>ascii</em> or <em>ebcdic</em>. 
 *  Unprintable and XML-metacharacters are replaced by "-" in the <em>str</em> attribute.
 *  <p />
 *  The serialization from XML to binary data does not regard the offsets -
 *  only the bytes represented by the <em>hex</em> attributes are written one
 *  after the other.
 *  <p />
 *  Example for the output:
 * <pre>
&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;hexdump width="16" ebcdic="false"&gt;
&lt;data off="0x0"  hex="1d 18 31 20 20 20 31 31 47 46 30 30 37 33 31 39" str="--1   11GF007319"/&gt;
&lt;data off="0x10" hex="36 31 33 30 38 30 30 30 30 30 30 31 30 31 30 31" str="6130800000010101"/&gt;
&lt;data off="0x20" hex="30 31 33 31 31 32 30 31 30 30 31 20 20 20 20 20" str="01311201001     "/&gt;
&lt;data off="0x30" hex="20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20" str="                "/&gt;
&lt;data off="0x40" hex="20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 79" str="               y"/&gt;
&lt;data off="0x50" hex="2b 32 34 36 38 30 39 34 61 30 30 30 39 30 30 30" str="+2468094a0009000"/&gt;
&lt;data off="0x60" hex="62 30 30 30 30 30 31 63 30 30 30 30 30 30 64 30" str="b000001c000000d0"/&gt;
&lt;data off="0x70" hex="31 30 31 65 30 31 38 31 30 66 30 30 30 30 67 30" str="101e01810f0000g0"/&gt;
&lt;data off="0x80" hex="30 30 30 1e 53 61 6c 64 6f 76 6f 72 74 72 61 67" str="000-Saldovortrag"/&gt;
&lt;data off="0x90" hex="1c 6f 32 79 2b 33 31 35 32 35 38 61 30 30 30 31" str="-o2y+315258a0001"/&gt;
&lt;data off="0xa0" hex="36 30 30 62 30 30 30 30 30 32 63 30 30 30 30 30" str="600b000002c00000"/&gt;
&lt;data off="0xb0" hex="30 64 30 31 30 31 65 30 39 30 30 30 66 30 30 30" str="0d0101e09000f000"/&gt;
&lt;data off="0xc0" hex="30 67 30 30 30 30 1e 53 61 6c 64 6f 76 6f 72 74" str="0g0000-Saldovort"/&gt;
&lt;data off="0xd0" hex="72 61 67 1c 6f 32 79 .. .. .. .. .. .. .. .. .." str="rag-o2y---------"/&gt;
&lt;data off="0xe0" hex=".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .." str="----------------"/&gt;
&lt;data off="0xf0" hex=".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .." str="----------------"/&gt;

&lt;data off="0x100"    hex="2d 33 31 35 32 35 38 61 32 30 30 31 36 30 30 62" str="-315258a2001600b"/&gt;
 * ...
 *  </pre>
 *  @author Dr. Georg Fischer
 */
public class HexDumpTransformer extends ByteTransformer { 
    public final static String CVSID = "@(#) $Id: HexDumpTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** Constructor
     */
    public HexDumpTransformer() {
        super();
        setFormatCodes("hexdump");
        setDescription("Hexadecimal Dump");
        setFileExtensions("hex");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(HexDumpTransformer.class.getName());
	} // initialize

    /** Root element tag */
    private static final String ROOT_TAG    = "hexdump";
    /** Table element tag */
    private static final String TABLE_TAG   = "table";
    /** Data element tag */
    private static final String DATA_TAG    = "data";
    
    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
            boolean isEBCDIC = getOption("code", "ascii").toLowerCase().startsWith("e") ; 
            int width = getIntOption("width", 16); // print so many bytes on one line
            ByteRecord genByteRecord = new ByteRecord(width);
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireStartElement(TABLE_TAG, toAttributes(new String[]
                    { "width", String.valueOf(width)
                    , "code" , (isEBCDIC ? "ebcdic" : "ascii")
                    }));
            fireLineBreak();
            StringBuffer attrList = new StringBuffer(128);
            int offset = 0; // file position
            int len;
            while ((len = genByteRecord.read(byteReader)) >= 0) {
                // read and process a record from the input file
                attrList.setLength(0);
                String text = isEBCDIC
                        ? genByteRecord.getEBCDICString(0, len)
                        : genByteRecord.getString      (0, len)
                        ;
                int start = 0;
                while (start < text.length()) {
                    char ch = text.charAt(start);
                    if (ch < 0x20 || ch == '"' || ch >= 0x7f || ch == '<' || ch == '>' || ch == '&') {
                        ch = '-';
                    }
                    attrList.append(ch);
                    start ++;
                } // while
                fireEmptyElement(DATA_TAG, toAttributes(new String[]
                        { "off", Integer.toHexString(offset)
                        , "hex", genByteRecord.dump(0, len).replaceAll(" 0", "..")
                        , "str", attrList.toString()
                        }));
                fireLineBreak();
                offset += width;
                if (offset % 0x100 == 0) { // additional blank line
                    fireLineBreak();
                }
            } // while reading input file
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

    /** buffer for the assembly of a binary byte sequence */
    private byte[] serBuffer;
    /** record for the specific format */
    protected ByteRecord serByteRecord;
        
    /** Receive notification of the start of an element.
     *  Looks for the element which contains encoded strings.
     *  @param uri The Namespace URI, or the empty string if the element has no Namespace URI 
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix), 
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix), 
     *  or the empty string if qualified names are not available.
     *  @param attrs the attributes attached to the element. 
     *  If there are no attributes, it shall be an empty Attributes object.
     *  @throws NumberFormatException for invalid hex digits
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs) {
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        if (false) {
        } else if (qName.equals(TABLE_TAG)) {
            int width = 16;
            try {
                width = Integer.parseInt(attrs.getValue("width"));
            } catch (Exception exc) {
            }
            serByteRecord = new ByteRecord(width);
            serBuffer = serByteRecord.getBuffer();
        } else if (qName.equals(DATA_TAG)) {
            String hexdump = (attrs.getValue("hex")).replaceAll("\\.\\.", "00");
            int index = 0;
            int pos   = 0;
            while (pos < hexdump.length()) {
                int value = 0;
                try {
                    value = Integer.parseInt(hexdump.substring(pos, pos+2), 16);
                } catch (NumberFormatException exc) {
                    log.error("off=" + attrs.getValue("off")
                            + " hex=\"" + hexdump + "\""
                            + " index=" + index + " pos=" + pos
                            );
                    throw exc;
                }
                serBuffer[index ++] = (byte) value;
                pos += 3;
            } // while pos
            serByteRecord.write(byteWriter, index);
        }
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
    } // endElement

    /** Receive notification of character data inside an element.
     *  These are ignored - all content is transported with attributes.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array. 
     *  @throws SAXException - any SAX exception, 
     *  possibly wrapping another exception
     */
    public void characters(char[] ch, int start, int len) 
            throws SAXException {
    } // characters

} // HexDumpTransformer
