/*  Transformer for AEB43 (Spanish payments exchange file)
    @(#) $Id: AEB43Transformer.java 566 2010-10-19 16:32:04Z gfis $
	2008-06-20: most code moved to CharTransformer
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
import  org.teherba.xtrans.finance.AEB43RecordBase;
import  org.teherba.xtrans.CharTransformer;

/** Transforms AEB43 (Spanish payments exchange) files to/from XML.
 *  The records are defined by an <a href="/xtrans/spec/finance/AEB43.spec.xml">XML record specification</a>.
 *  <pre>
...
 *  </pre>
 *  @author Dr. Georg Fischer
 */
public class AEB43Transformer extends CharTransformer {
    public final static String CVSID = "@(#) $Id: AEB43Transformer.java 566 2010-10-19 16:32:04Z gfis $";
    
    /** No-args Constructor
     */
    public AEB43Transformer() {
        super();
        setFormatCodes("aeb43");
        setDescription("AEB43 Spanish payments exchange file");
        setFileExtensions("aeb");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        // setRecord   (new AEB43RecordBase());
        // setSAXRecord(new AEB43RecordBase());
	} // initialize
	
} // AEB43Transformer
