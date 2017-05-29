/*  Transforms Java program source files
    @(#) $Id: JavaTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-29: \r problem
    2007-10-19, Georg Fischer: copied from ExtraTransformer
    Still to do:
    - '\0' leads to an error, '\u0000' is ok
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

/** Transformer for Sun's programming language Java
 *  which is implemented on a wide range of systems, among them
 *  Unix (Linux, Sun Solaris, IBM AIX), IBM z/OS, Microsoft Windows,
 *  Apple MacOS.
 *  See {@link ProgLangTransformer} for a general description of the
 *  the generated elements.
 *  <p>
 *  A test example is this source file.
 *  @author Dr. Georg Fischer
 */
public class JavaTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: JavaTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log = Logger.getLogger(JavaTransformer.class.getName());;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public JavaTransformer() {
        super();
        setFormatCodes("java");
        setDescription("Java Programming Language");
        setFileExtensions("java");
        // the following apply to the generator AND the serializer
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
    } // initialize

    /** Prepares the generator.
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
        , "if"
        , "implements"
        , "import"
        , "instanceof"
        , "int"
        , "interface"
        , "length"
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
        , "threadsafe"
        , "throw"
        , "throws"
        , "transient"
        , "true"
        , "try"
        , "void"
        , "while"
        };
        storeWords();
    } // prepareGenerator

    //////////////////////
    // SAX event generator
    //////////////////////

    /** Does nothing but to use several rare features of the language for test purposes.
     */
    protected void rareFeatureTest() {
        /**/ // a minimal, but non-document comment
        // char ch1 = '\x20';      // \\x escape
        String asciiEscapes = "\n\r\t";
        char ch2 = '\u00a0';    // \\u escape
        String str2 = "\u00a0\u00a0";    // \\u escapes
    } // rareFeatureTest

    /////////////////////////
    // SAX content handler //
    /////////////////////////

} // class JavaTransformer
