/*  Transforms the PostScript printer programming language
    @(#) $Id: PostScriptTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2010-06-09: collapse string and comment tags to one with mode attribute
    2008-02-02, Georg Fischer, to Sassnitz: copied from VisualBasicTransformer
    
    round-trip ok, but not yet implemented:
    - \octal character escapes
    - (text) as specific element
    - %% processing instructions (document comments?)
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

/** Transformer for Adobe's PostScript programming language for printers
 *	like Word, Excel, Access etc.
 *  See {@link ProgLangTransformer} for a general description of the
 *	the generated elements.
 *	Statements normally end with the line, and are continued if the last
 *	character on the line is an underscore.
 *  @author Dr. Georg Fischer
 */
public class PostScriptTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: PostScriptTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = Logger.getLogger(PostScriptTransformer.class.getName());

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public PostScriptTransformer() {
        super();
        setFormatCodes		("ps");
        setDescription		("PostScript (Adobe)");
        setFileExtensions	("ps");
        setMimeType			("application/postscript");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables
	 *  common to generator and serializer.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        bothStringTypes = false;
    	caseIndependant = false;
        doubleInnerApos = false;
        escapeCode      = LANG_VBA; // none
        lineEndComment  = "%";
	} // initialize
	
    /** Prepares the class.
     *  Does all heavy-weight initialization.
     */
    protected void prepareGenerator() {
        super.prepareGenerator();
        identifierPattern  = Pattern.compile
                ("[\\w]+");
        numberPattern      = Pattern.compile
                ("\\d+"); 
        operatorPattern    = Pattern.compile
                ("\\,|\\.|[\\+\\-\\*\\>\\<\\$\\=\\:\\~\\/\\#\\\\]+");
        keywords = new String[] { "()"
        , "def"
        };
        storeWords();
    } // prepareGenerator

    //////////////////////
    // SAX event generator
    //////////////////////

    /** Matches all elements starting with "\'": character denotations, maybe escaped.
     *  This special implementation cares for VisualBasic's line end comment (a single apostrophe).
     *  @param line input line containing the character
     *  @param linePos position where the character denotation starts
     *  @param trap position behind the end of the line
     *  @return position behind the element, - 1
     */
    protected int matchCharacter(String line, int linePos, int trap) {
        int pos2 = linePos + 1; // position behind the element just recognized
        int pos1 = linePos + lineEndComment.length();
        fireStartElement(COMMENT_TAG, spaceAndModeAttr(LINE_END_MODE));
        fireCharacters(replaceNoSource(line.substring(pos1)));
        fireEndElement(COMMENT_TAG);
        pos2 = trap; // skip to end of line
        return pos2 - 1; // 1 before the end
    } // matchCharacter

    /** Matches all punctuation and operator elements apart from parentheses, ";", "," and ".".
     *  @param line input line containing the start of the operator at position <em>linePos</em>
     *  @param linePos position where the operator starts
     *  @return position behind the element, - 1
     */
    protected int matchOperator(String line, int linePos, int trap) {
        int pos2 = linePos + 1; // position behind the element just recognized
		if (line.substring(linePos).startsWith(lineEndComment)) { // line end comment starts here
            int pos1 = linePos + lineEndComment.length();
	        fireStartElement(COMMENT_TAG, spaceAndModeAttr(LINE_END_MODE));
       	    fireCharacters(replaceNoSource(line.substring(pos1)));
           	fireEndElement(COMMENT_TAG);
           	pos2 = trap; // skip to end of line
		} else { // normal operator 
			pos2 = super.matchOperator(line, linePos, trap) + 1;
		}
        return pos2 - 1; // 1 before the end
    } // matchOperator
    

    /////////////////////////
    // SAX content handler //
    /////////////////////////

} // class PostScriptTransformer
