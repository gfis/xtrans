/*  Pseudo-abstract class for binary file format transformers
    @(#) $Id: ByteTransformer.java 566 2010-10-19 16:32:04Z gfis $
    Copyright (c) 2006 Dr. Georg Fischer <punctum@punctum.com>
    2010-07-06: super.startDocument()
    2006-09-20: copied from BaseTransformer
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
import  org.teherba.xtrans.BaseTransformer;
import  org.xml.sax.SAXException;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Base class for binary file format transformers 
 *  defining common properties and methods.
 *  @author Dr. Georg Fischer
 */
public abstract class ByteTransformer extends BaseTransformer {
    public final static String CVSID = "@(#) $Id: ByteTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
      
    /** record for the specific format */
    // protected ByteRecord byteRecord;
    
    /** No-args Constructor
     */
    public ByteTransformer() {
        super();
    } // Constructor
    
    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = LogManager.getLogger(ByteTransformer.class.getName());
    } // initialize
    
    /** Tells that this specific format is a binary format
     *  @return false if character format, true for binary
     */
    public boolean isBinaryFormat() {
        setBinaryFormat(true);
        return true;
    } // isBinaryFormat
    
    /** Receive notification of the beginning of the document,
     *  and initialize the outgoing handler for a filter.
     *  @throws SAXException - any SAX exception, 
     *  possibly wrapping another exception
     */
    public void startDocument() 
            throws SAXException {
        try {
            super.startDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // startDocument
    
} // ByteTransformer
