/*  Transforms REXX program files
    @(#) $Id: REXXTransformer.java 566 2010-10-19 16:32:04Z gfis $
	2010-06-17: backslash = not repaired
    2007-01-18, Georg Fischer: copied from JavaTransformer
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
import	org.teherba.xtrans.proglang.ProgLangTransformer;
import  org.apache.log4j.Logger;

/** Transformer for programs in the REXX language 
 *	implemented on IBM z/OS, AIX, AS/400, Windows and (via "Regina") Unix.
 *  See {@link ProgLangTransformer} for a general description of the
 *	the generated elements.
 *	Normally, the line end is also the statement's end. The statement
 *	may be continued on the next line if it ends with "," or "+" (a comment
 *	may still follow, however).
 *  @author Dr. Georg Fischer
 */
public class REXXTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: REXXTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = Logger.getLogger(REXXTransformer.class.getName());;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public REXXTransformer() {
        super();
        setFormatCodes("rexx,rex");
        setDescription("REXX Programming Language");
        setFileExtensions("rexx,rex,rx");
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
        bothStringTypes = true;
        caseIndependant = true;
        doubleInnerApos = false;
		escapeCode      = LANG_REXX;
	} // initialize
	
    /** Prepares the class.
     *  Does all heavy-weight initialization.
     */
    protected void prepareGenerator() {
		super.prepareGenerator();
        keywords = new String[] { "()"
  		, "address" 
  		, "arg"
		, "break"
		, "call"
		, "do"
		, "drop"
		, "echo"
		, "else"
		, "end"
        , "exit"
        , "false"
		, "if"
		, "interpret"
		, "iterate"
		, "leave"
		, "nop"
		, "numeric"
        , "options"
		, "otherwise"
		, "parse"
		, "procedure"
		, "pull"
		, "push"
		, "queue"
        , "return"
		, "say"
		, "select"
		, "shell"
		, "signal"
		, "then"
		, "trace"

		, "true"
		, "upper"
        , "when"
		, "value"
		, "to"
		, "by"
		, "for"
		, "forever"
		, "while"
		, "until"
		, "form"
        , "digits"
		, "fuzz"
		, "scientific"
		, "engineering"
		, "failat"
		, "prompt"
        , "results"
		, "upper"
		, "external"
		, "source"
		, "with"
		, "command"
        , "function"
		, "var"
		, "version"
		, "expose"
		, "on"
		, "off"
        };
        putEntityReplacements();
        storeWords();
    } // prepareGenerator

    /////////////////////////
    // SAX content handler //
    /////////////////////////

} // class REXXTransformer
