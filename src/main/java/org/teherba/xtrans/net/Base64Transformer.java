/*  Transforms to/from a Base64 encoding of a (binary) file
    @(#) $Id: Base64Transformer.java 566 2010-10-19 16:32:04Z gfis $
    2006-09-22, Georg Fischer: copied from HexDumpTransformer
    2010-06-30: problem with spaces in Base64 string, solved in BaseTransformer.base64ToBytes
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

package org.teherba.xtrans.net;
import  org.teherba.xtrans.ByteRecord;
import  org.teherba.xtrans.ByteTransformer;
import  java.lang.System; // arraycopy
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/**
 *  Transformer for a Base64 encoding of a (binary) file
 *  Base64 encoding is described in 
 *  <a href="http://tools.ietf.org/html/rfc2045#section-6.8">RFC 2045, section 6.8</a> 
 *  Encodes portions of 57 bytes from any octet stream into 76 characters 
 *  from an ASCII subset using 65 characters (letters in upper and lower case, digits,
 *  "+", "/" and the (65th) special pad character "=". One trailing "=" 
 *  indicates that the last encoded value had 16 bit, while "==" indicates
 *  that it had 8 bits.
 *  </p><p>
 *  For the transformation from XML to binary, the Base64 encoded content
 *  of the &lt;td&gt; element may consist of several lines, which
 *  are trimmed (leading and trailing whitespace is removed) and 
 *  concatenated.
 *  </p><p>
 *  The following string is used to encode 3 octets (3*8 = 24 bit) 
 *  in 4 characters (6 bit each):
 *  <code>char[] char64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="</code>
 *  </p><p>
 *  Example: end of a ZIP file in an email attachment
 *  <pre>
UEsBAhQAFAAIAAgA0ZPMKsLy8Qs7AAAAOwAAAAoAAAAAAAAAAAAAAAAAtOQEAHNvbF9lcy5iYXRQ
SwECFAAUAAgACADBk8wqelPO/HEAAAB+AAAABgAAAAAAAAAAAAAAAAAn5QQAc29sLmpzUEsBAhQA
FAAIAAgAzJPMKsSKbDZyAAAAgQAAAAkAAAAAAAAAAAAAAAAAzOUEAHNvbF9lbi5qc1BLAQIUABQA
CAAIANaTzCrKd2UwcgAAAIEAAAAJAAAAAAAAAAAAAAAAAHXmBABzb2xfZXMuanNQSwECFAAUAAgA
CADlk8wq19uZGUMAAABDAAAABgAAAAAAAAAAAAAAAAAe5wQAc29sLnNoUEsBAhQAFAAIAAgA6ZPM
KtdYVRZGAAAARgAAAAkAAAAAAAAAAAAAAAAAlecEAHNvbF9lbi5zaFBLAQIUABQACAAIAO2TzCoO
NFN1RgAAAEYAAAAJAAAAAAAAAAAAAAAAABLoBABzb2xfZXMuc2hQSwECCgAKAAAAAAATla4qAAAA
AAAAAAAAAAAAJgAAAAAAAAAAAAAAAACP6AQAYWJlbHBzb2Z0L2NhcmRnYW1lcy9Tb2xTdWl0ZS94
bWxSdWxlcy9QSwECFAAUAAgACABdvrgqBKSaBQACAADVBwAALgAAAAAAAAAAAAAAAADT6AQAYWJl
bHBzb2Z0L2NhcmRnYW1lcy9Tb2xTdWl0ZS94bWxSdWxlcy9qc29sLmR0ZFBLAQIUABQACAAIAPa7
uioTwzWo3QYAALxAAAA4AAAAAAAAAAAAAAAAAC/rBABhYmVscHNvZnQvY2FyZGdhbWVzL1NvbFN1
aXRlL3htbFJ1bGVzL3NvbGl0YWlyZXNfMV8wLnhtbFBLBQYAAAAADwAPALUDAABy8gQAAAA=

------=_NextPart_000_0002_01C10A67.9FDF3800--
 
 *  </pre>
 *  @author Dr. Georg Fischer
 */
public class Base64Transformer extends ByteTransformer { 
    public final static String CVSID = "@(#) $Id: Base64Transformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** maximum number of output lines to be printed without tag */
    private static final int MAX_LINE = 10;
    /** number of data octets to be encoded in one line */
    private int width; 
    
    /** Root element tag */
    private static final String ROOT_TAG    = "base64";
    /** Root element tag */
    private static final String TABLE_TAG   = "table";
    /** Data element tag */
    private static final String DATA_TAG    = "td";
    
    /** No-args Constructor
     */
    public Base64Transformer() {
        super();
        setFormatCodes("base64");
        setDescription("Base64 encoding of a binary file");
        setFileExtensions("base64");
    } // Constructor()
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(Base64Transformer.class.getName());
        width = getIntOption("width", 57); // = 19*3, yields 19*4 = 76 encoded characters
	} // initialize
    
    /** record for the specific format */
    private ByteRecord byteRecord;

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
            byteRecord = new ByteRecord(width);
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireStartElement(TABLE_TAG, toAttribute("width", String.valueOf(width)));
            fireStartElement(DATA_TAG);
            fireLineBreak();
            int count = 0;
            int len;
            while ((len = byteRecord.read(byteReader)) >= 0) {
                fireCharacters(bytesToBase64(byteRecord.getBuffer(), len));
                fireLineBreak();
                count ++;
                if (count >= MAX_LINE) {
                    count = 0;
                    fireEndElement  (DATA_TAG);
                    fireStartElement(DATA_TAG);
                    fireLineBreak();
                }
            } // while reading input file
            fireEndElement(DATA_TAG);
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

    /** state of parsing, 0 = uninteresting, 1 = in interesting element */
    private int state;

    /** buffer for the assembly of a Base64 string */
    private StringBuffer buffer;
    /** record for the specific format */
    private ByteRecord saxRecord;
        
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
    } // startDocument
    
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
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs) {
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        if (false) {
        } else if (qName.equals(ROOT_TAG)) {
            buffer = new StringBuffer(MAX_LINE * width);
            state = 0; // uninteresting
        } else if (qName.equals(DATA_TAG)) {
            state = 1; // in interesting element
            buffer.setLength(0);
        }
    } // startElement
    
    /** Receive notification of the end of an element.
     *  Looks for the element which contains encoded strings lines.
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
        if (false) {
        } else if (qName.equals(DATA_TAG)) {
            state = 0; // in interesting element
            byte [] octets = base64ToBytes(buffer.toString());
            int len = octets.length;
            saxRecord = new ByteRecord(len);
            System.arraycopy(octets, 0, saxRecord.getBuffer(), 0, len);
            saxRecord.write(byteWriter, len);
        }
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int length) {
        if (state == 1) { // interesting
            buffer.append((new String(ch, start, length)).trim());
        }
    } // characters
    
} // Base64Tranformer
