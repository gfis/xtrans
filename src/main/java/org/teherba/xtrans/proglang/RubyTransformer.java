/*  Transforms Ruby program source files
    @(#) $Id: RubyTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2008-05-16, Georg Fischer: copied from JavaScriptTransformer
    
    Caution, no valid turn-around test yet
*/
/*
 * Copyright 2008 Dr. Georg Fischer <punctum at punctum dot kom>
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

/** Transformer for programs in the 
 *	<a xref="http://">Ruby</a> 
 *	language.
 *  See {@link ProgLangTransformer} for a general description of the
 *  the generated elements.
 *  @author Dr. Georg Fischer
 */
public class RubyTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: RubyTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = Logger.getLogger(RubyTransformer.class.getName());;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public RubyTransformer() {
        super();
        setFormatCodes("ruby");
        setDescription("Ruby Programming Language");
        setFileExtensions("rb");
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
        bothStringTypes = false; // true;
	    lineEndComment  = "#";
        escapeCode      = LANG_RUBY;
	} // initialize
	
    /** Prepares the class.
     *  Does all heavy-weight initialization.
     */
    protected void prepareGenerator() {
        super.prepareGenerator();
        operatorPattern    = Pattern.compile
                ("\\,|\\.|[\\+\\-\\*\\>\\<\\&\\|\\^\\%\\$\\=\\:\\?\\!\\~\\\\\\@\\`]+");
                // no slash! because it may start a comment
        keywords = new String[] { "()"
		, "BEGIN"
		, "END"
		, "__FILE__"
		, "__LINE__"
		, "alias"
		, "and"
		, "begin"
		, "break"
		, "case"
		, "class"
		, "def"
		, "defined?"
		, "do"
		, "else"
		, "elsif"
		, "end"
		, "ensure"
		, "false"
		, "for"
		, "if"
		, "in"
		, "module"
		, "next"
		, "nil"
		, "not"
		, "or"
		, "redo"
		, "rescue"
		, "retry"
		, "return"
		, "self"
		, "super"
		, "then"
		, "true"
		, "undef"
		, "unless"
		, "until"
		, "when"
		, "while"
		, "yield"
		};
        storeWords();
    } // prepareGenerator

    //////////////////////
    // SAX event generator
    //////////////////////

    /////////////////////////
    // SAX content handler //
    /////////////////////////

} // class RubyTransformer
