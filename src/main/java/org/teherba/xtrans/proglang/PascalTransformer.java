/*  Transforms (Turbo) Pascal program files
    @(#) $Id: PascalTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2010-06-08: collapse string and comment tags to one with type attribute
    2008-01-21, Georg Fischer: copied from JavaTransformer
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
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transformer for programs in (Turbo) Pascal
 *  See {@link ProgLangTransformer} for a general description of the
 *  the generated elements.
 *  @author Dr. Georg Fischer
 */
public class PascalTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: PascalTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = LogManager.getLogger(PascalTransformer.class.getName());;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public PascalTransformer() {
        super();
        setFormatCodes("pascal,pas,tp");
        setDescription("(Turbo) Pascal Programming Language");
        setFileExtensions("pas,tpu");
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
        caseIndependant = false;
        doubleInnerApos = true;
        pascalComments  = true;
        escapeCode      = LANG_PASCAL;
	} // initialize
	
    /** Prepares the class.
     *  Does all heavy-weight initialization.
     */
    protected void prepareGenerator() {
        super.prepareGenerator();
        keywords = new String[] { "()"
		, "and"
        , "array"
        , "begin"
        , "case"
        , "const"
        , "div"
        , "do"
        , "downto"
        , "else"
        , "end"
        , "file"
        , "for"
        , "function"
        , "goto"
        , "if"
        , "in"
        , "label"
        , "mod"
        , "nil"
        , "not"
        , "of"
        , "or"
        , "packed"
        , "procedure"
        , "program"
        , "record"
        , "repeat"
        , "set"
        , "shl"     // Turbo Pascal
        , "shr"     // Turbo Pascal
        , "string"  // Turbo Pascal
        , "then"
        , "to"
        , "type"
        , "until"
        , "var"
        , "while"
        , "with"
        , "xor"     // Turbo Pascal
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

	/** Writes the start of a target comment.
	 *  @param qName the qualified name (with prefix),
	 *  or the empty string if qualified names are not available.
	 *  @param attrs the attributes attached to the element.
	 *  If there are no attributes, it shall be an empty Attributes object.
	 */
	protected void writeCommentStart(String qName, Attributes attrs) {
		commentModeAttr = attrs.getValue(MODE_ATTR);
		if (qName.equals(COMMENT_TAG   )) {
			if (false) {
			} else if (commentModeAttr == null 
					|| commentModeAttr.equals(COMMENT_MODE))  {
	            saxBuffer.append      (PAS_COMMENT_START );
	        } else if (commentModeAttr.equals(DOCUMENT_MODE)) {
	            saxBuffer.append      (PAS_COMMENT2_START);
	        } 
		}
	} // writeCommentStart

	/** Writes the end   of a target comment.
	 *  @param qName tag which specifies the subtype of the comment
	 */
	protected void writeCommentEnd  (String qName) {
		if (qName.equals(COMMENT_TAG  )) {
			if (false) {
			} else if (commentModeAttr == null 
					|| commentModeAttr.equals(COMMENT_MODE)) {
	            saxBuffer.append      (PAS_COMMENT_END   );
	        } else if (commentModeAttr.equals(DOCUMENT_MODE)) {
	            saxBuffer.append      (PAS_COMMENT2_END  );
	        } 
		}
	} // writeCommentEnd

} // class PascalTransformer
