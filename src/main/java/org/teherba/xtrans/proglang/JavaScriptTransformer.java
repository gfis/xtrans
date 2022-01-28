/*  Transforms JavaScript program source files
    @(#) $Id: JavaScriptTransformer.java 566 2010-10-19 16:32:04Z gfis $
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
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transformer for programs in the 
 *	<a href="http://www.ecma-international.org/publications/standards/Ecma-262.htm">ECMAScript</a> 
 *	language, better known as JavaScript and implemented
 *  in most modern Internet browsers (Firefox, Opera, InternetExplorer).
 *  See {@link ProgLangTransformer} for a general description of the
 *  the generated elements.
 *  @author Dr. Georg Fischer
 */
public class JavaScriptTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: JavaScriptTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = LogManager.getLogger(JavaScriptTransformer.class.getName());;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public JavaScriptTransformer() {
        super();
        setFormatCodes("javascript");
        setDescription("JavaScript Programming Language");
        setFileExtensions("js");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables
	 *  common to generator and serializer.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        caseIndependant = false;
        doubleInnerApos = false;
        bothStringTypes = true;
	} // initialize
	

    /** Prepares the class.
     *  Does all heavy-weight initialization.
     */
    protected void prepareGenerator() {
        super.prepareGenerator();
        keywords = new String[] { "()"
		, "abstract"
		, "boolean"
		, "break"
		, "byte"
		, "case"
		, "catch"
		, "char"
		, "class"
		, "const"
		, "continue"
		, "default"
		, "do"
		, "double"
		, "else"
		, "extends"
		, "false"
		, "final"
		, "finally"
		, "float"
		, "for"
		, "function"
		, "goto"
		, "if"
		, "implements"
		, "import"
		, "in"
		, "instanceof "
		, "int"
		, "interface"
		, "long"
		, "native"
		, "new"
		, "null"
		, "package"
		, "private"
		, "protected"
		, "public"
		, "return"
		, "short"
		, "static"
		, "super"
		, "switch"
		, "synchronized"
		, "this"
		, "throw"
		, "throws"
		, "transient"
		, "true"
		, "try"
		, "var"
		, "void"
		, "while"
		, "with"
        };
        storeWords();
    } // prepareGenerator

    //////////////////////
    // SAX event generator
    //////////////////////

    /////////////////////////
    // SAX content handler //
    /////////////////////////

} // class JavaScriptTransformer
