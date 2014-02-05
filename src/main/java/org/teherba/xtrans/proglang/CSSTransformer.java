/*  Transforms CSS source files
    @(#) $Id: CSSTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2008-01-18, Georg Fischer: copied from JavaTransformer
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

package org.teherba.xtrans.proglang;
import  org.teherba.xtrans.proglang.ProgLangTransformer;
import  java.util.regex.Pattern;
import  org.apache.log4j.Logger;

/** Transformer for Cascaded Style Sheets in HTML, as defined by the 
 *	<a href="http://www.w3c.org">W3C consortium</a>.
 *  See {@link ProgLangTransformer} for a general description of the
 *	the generated elements.
 *  @author Dr. Georg Fischer
 */
public class CSSTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: CSSTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = Logger.getLogger(CSSTransformer.class.getName());;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public CSSTransformer() {
        super();
        setFormatCodes		("css");
        setDescription		("Cascaded Style Sheet");
        setFileExtensions	("css");
        setMimeType			("text/css");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables
	 *  common to generator and serializer.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        caseIndependant = false;
	} // initialize
	
    /** Prepares the class.
     *  Does all heavy-weight initialization.
     */
    protected void prepareGenerator() {
        super.prepareGenerator();
		identifierPattern = Pattern.compile("[\\w\\_\\-]+");
        keywords = new String[] { "()"
		, "abstract"
        };
        storeWords();
    } // prepareGenerator

    //////////////////////
    // SAX event generator
    //////////////////////

    /////////////////////////
    // SAX content handler //
    /////////////////////////

} // class CSSTransformer
