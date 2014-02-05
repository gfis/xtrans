/*  Transforms C++ program source files
    @(#) $Id: CTransformer.java 566 2010-10-19 16:32:04Z gfis $
	2008-01-21: keywords only
    2007-10-19, Georg Fischer: copied from ExtraTransformer

    Caution, for the test case to work this source file must be stored
    - in UTF-8
    - with all tabs expanded to spaces
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
import  org.apache.log4j.Logger;

/** Transformer for programs in "The C Programming Language" as described by Kernighan and Ritchie.
 *  See {@link ProgLangTransformer} for a general description of the
 *  the generated elements.
 *  @author Dr. Georg Fischer
 */
public class CTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: CTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = Logger.getLogger(CTransformer.class.getName());;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public CTransformer() {
        super();
        setFormatCodes("c");
        setDescription("C Programming Language");
        setFileExtensions("c,h");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables
	 *  common to generator and serializer.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        minColumn   = 0; // take whole line for source text
        maxColumn   = HIGH_COLUMN;
        caseIndependant = false;
        doubleInnerApos = false;
	} // initialize
	
    /** Prepares the class.
     *  Does all heavy-weight initialization.
     */
    protected void prepareGenerator() {
        super.prepareGenerator();
        keywords = new String[] { "()"
		, "#define"
		, "#elif"
		, "#else"
		, "#endif"
		, "#if"
		, "#ifdef"
		, "#ifndef"
		, "#include"
		, "#undef"
		, "auto"
		, "break"
		, "case"
		, "char"
		, "continue"
		, "default"
		, "do"
		, "double"
		, "else"
		, "entry"
		, "extern"
		, "float"
		, "for"
		, "goto"
		, "if"
		, "int"
		, "long"
		, "register"
		, "return"
		, "short"
		, "sizeof"
		, "static"
		, "struct"
		, "switch"
		, "typedef"
		, "union"
		, "unsigned"
		, "while"
        };
        putEntityReplacements();
        storeWords();
    } // prepareGenerator

    //////////////////////
    // SAX event generator
    //////////////////////

    /////////////////////////
    // SAX content handler //
    /////////////////////////

} // class CTransformer
