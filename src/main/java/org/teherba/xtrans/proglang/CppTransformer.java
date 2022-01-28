/*  Transforms C++ program source files
    @(#) $Id: CppTransformer.java 566 2010-10-19 16:32:04Z gfis $
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
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transformer for programs in the C++ programming language.
 *  See {@link ProgLangTransformer} for a general description of the
 *  the generated elements.
 *  @author Dr. Georg Fischer
 */
public class CppTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: CppTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = LogManager.getLogger(CppTransformer.class.getName());;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public CppTransformer() {
        super();
        setFormatCodes("cpp");
        setDescription("C++ Programming Language");
        setFileExtensions("c++,cpp");
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
		, "#error"
		, "#if"
		, "#ifdef"
		, "#ifndef"
		, "#include"
		, "#include_next"
		, "#line"
		, "#pragma"
		, "#undef"
		, "auto"
		, "bool"
		, "break"
		, "case"
		, "catch"
		, "char"
		, "class"
		, "const"
		, "const_cast"
		, "continue"
		, "default"
		, "defined"
		, "delete"
		, "do"
		, "double"
		, "dynamic_cast"
		, "else"
		, "enum"
		, "explicit"
		, "export"
		, "extern"
		, "false"
		, "float"
		, "for"
		, "friend"
		, "goto"
		, "if"
		, "inline"
		, "int"
		, "long"
		, "mutable"
		, "namespace"
		, "new"
		, "operator"
		, "private"
		, "protected"
		, "public"
		, "register"
		, "reinterpret_cast"
		, "return"
		, "short"
		, "signed"
		, "sizeof"
		, "static"
		, "static_cast"
		, "struct"
		, "switch"
		, "template"
		, "this"
		, "throw"
		, "true"
		, "try"
		, "typedef"
		, "typeid"
		, "typename"
		, "union"
		, "unsigned"
		, "using"
		, "virtual"
		, "void"
		, "volatile"
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

} // class CppTransformer
