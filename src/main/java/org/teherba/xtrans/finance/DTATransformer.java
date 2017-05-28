/*  Transformer for DTA (German payments exchange file)
    @(#) $Id: DTATransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2008-06-24: warning if some sum mismatch
    2007-09-05: compute E record from C12, C4, C5
    2006-10-11: Georg Fischer
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
import  org.teherba.xtrans.finance.DTARecordBase;
import  org.teherba.xtrans.ByteTransformer;
import  org.xml.sax.Attributes;
import  org.xml.sax.SAXException;
import  org.apache.log4j.Logger;

/** Transforms DTA (German payments exchange) files to/from XML.
 *  The records are defined by an <a href="/xtrans/spec/finance/DTA.spec.xml">XML record specification</a>.
 *  <pre>
XML output:

&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;doc:records xmlns:doc="http://org.punctum.xtrans/2006/Record"&gt;
&lt;doc:DTA rlen="150" l0="0" rtyp="A" &gt;&lt;doc:A A3="GK" A4="050070010" A5="00000000 ...
&lt;doc:DTA rlen="324" l0="0" rtyp="C" &gt;&lt;doc:C C3="050070010" C4="050070010" C5="0 ...
&lt;doc:DTA rlen="324" l0="0" rtyp="C" &gt;&lt;doc:C C3="050070010" C4="050070010" C5="0 ...
...
 *  </pre>
 *  @author Dr. Georg Fischer
 */
public class DTATransformer extends ByteTransformer {
    public final static String CVSID = "@(#) $Id: DTATransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** record for the specific format */
    private DTARecordBase genRecord;

    /** No-args Constructor
     */
    public DTATransformer() {
        super();
        setFormatCodes("dta");
        setDescription("DTA MCV German payments exchange file");
        setFileExtensions("dta");
    } // Constructor

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = Logger.getLogger(DTATransformer.class.getName());
        genRecord = new DTARecordBase();
    } // initialize

    /** DTA element tag */
    private static final String  DTA_TAG = "DTA";

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
            fireStartDocument();
            setNamespace("", genRecord.getNamespaceURI());
            fireStartRoot(genRecord.ROOT_TAG);
            fireLineBreak();
            while (genRecord.readVariable(byteReader)) {
                genRecord.fireElements(this);
                fireLineBreak();
            } // while reading
            fireEndElement(genRecord.ROOT_TAG);
            fireLineBreak();
            fireEndDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return  result;
    } // generate

    /*=======================*/
    /* DTA MCV   SAX Handler */
    /*=======================*/

    /** Sum of C records */
    private long sumCount;
    /** Sum of account numbers */
    private long sumAccount;
    /** Sum of BLZ */
    private long sumBLZ;
    /** Sum of amounts */
    private long sumAmount;
    /** record for the specific format */
    private DTARecordBase serRecord;

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        serRecord = new DTARecordBase();
        sumCount   = 0L;
        sumAccount = 0L;
        sumBLZ     = 0L;
        sumAmount  = 0L;
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
     *  @throws SAXException for SAX errors
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs)
            throws SAXException {
        // System.out.println("start " + localName + ":" + qName + ", namespace=" + namespace);
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        try {
            if (false) {
            } else if (qName.equals(namespace + "C")) {
                sumCount   ++;
                sumAccount += Long.parseLong(attrs.getValue("C5" ));
                sumBLZ     += Long.parseLong(attrs.getValue("C4" ));
                sumAmount  += Long.parseLong(attrs.getValue("C12"));
            } else if (qName.equals(namespace + "E")) {
                long e4     = Long.parseLong(attrs.getValue("E4" ));
                long e6     = Long.parseLong(attrs.getValue("E6" ));
                long e7     = Long.parseLong(attrs.getValue("E7" ));
                long e8     = Long.parseLong(attrs.getValue("E8" ));
                if (        sumCount    != e4
                        ||  sumAccount  != e6
                        ||  sumBLZ      != e7
                        ||  sumAmount   != e8) { // some mismatch
                    log.warn("sum mismatch: computed are"
                            +  " E4=" + sumCount
                            + ", E6=" + sumAccount
                            + ", E7=" + sumBLZ
                            + ", E8=" + sumAmount
                            );
                } // some mismatch
            }
            serRecord.startElement(uri, localName, qName, attrs);
        } catch (Exception exc) {
            log.error("error in startElement ", exc);
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
        try {
            if (false) {
            } else if (qName.equals(namespace + "E")) {
                serRecord.setE6(Long.toString(sumAccount ));
                serRecord.setE7(Long.toString(sumBLZ     ));
                serRecord.setE8(Long.toString(sumAmount  ));
            } else if (qName.equals(namespace + DTA_TAG)) {
                serRecord.writeVariable(byteWriter);
            } else {
            }
        } catch (Exception exc) {
            log.error("error in startElement ", exc);
        }
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

} // DTATransformer
