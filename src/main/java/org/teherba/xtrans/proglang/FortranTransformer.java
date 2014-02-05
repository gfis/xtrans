/*  Transforms Fortran (77) program files
    @(#) $Id: FortranTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2010-06-09: collapse string and comment tags to one with mode attribute
	2008-01-21, Georg Fischer: copied from PL1Transformer
	
	from http://fortranwiki.org/fortran/show/Keywords :
FORTRAN 77: access, assign, backspace, blank, block, call, close, common, 
continue, data, dimension, direct, do, else, endif, enddo, end, entry, eof, 
equivalence, err, exist, external, file, fmt, form, format, formatted, function, 
goto, if, implicit, include, inquire, intrinsic, iostat, logical, named, namelist, 
nextrec, number, open, opened, parameter, pause, print, program, read, rec, recl, 
return, rewind, sequential, status, stop, subroutine, then, type, unformatted, unit, write, save.

Fortran 90
The following keywords were added in Fortran 90: allocate, allocatable, case, contains, 
cycle, deallocate, elsewhere, exit, interface, intent, module, only, operator, optional, 
pointer, private, procedure, public, result, recursive, select, sequence, target, use, while, where.

Fortran 95
The following keywords were added in Fortran 95: elemental, forall, pure.

Fortran 2003
The following features were added in Fortran 2003.
Keywords: abstract, associate, class, decimal, decorate, delegate, encoding, 
endfile, enum, enumerator, extends, extensible, flush, generic, iomsg, import, 
move_alloc, nextrec, non_overridable, pass, pending, reference, round, sign, static, typealias.
Attributes: asynchronous, bind, protected, volatile.
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
import  java.util.regex.Matcher;
import  java.util.regex.Pattern;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Transformer for programs in the Fortran (FORTRAN IV, Fortran 77) language.
 *  The source files are column oriented:
 *	<pre>
 *  column  1     space or "C" = comment, "D" = debugging code line
 *	columns 1-5	  numeric statement label
 *	column  6	  non-space indicates a continuation line
 *	columns 7-72  source statements
 *	columns 73-80 card sequence number
 *	</pre>
 *	<p>
 *	This module cannot (yet) handle:
 *	<ul>
 *	<li>the "nH..." string denotation</li>
 *	<li>spaces in keywords and identifiers</li>
 *	<li>identifiers which clash with keywords</li>
 *	</ul>
 *  @see ProgLangTransformer for a general description of the
 *  the generated elements.
 *  @author Dr. Georg Fischer
 */
public class FortranTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: FortranTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = Logger.getLogger(FortranTransformer.class.getName());;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public FortranTransformer() {
        super();
        setFormatCodes("fortran,ftn,for,f77");
        setDescription("Fortran Programming Language");
        setFileExtensions("ftn,f,f77");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables
	 *  common to generator and serializer.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        minColumn   =  0;
        maxColumn   = 72;
        bothStringTypes = false;
    	caseIndependant = true;
        doubleInnerApos = true;
        escapeCode      = LANG_PL1;
        lineEndComment = "!";
	} // initialize

    /** Prepares the generator.
     *  Does all heavy-weight initialization.
     */
    protected void prepareGenerator() {
		super.prepareGenerator();
        identifierPattern  = Pattern.compile
                ("[\\w]+");
        operatorPattern    = Pattern.compile
                ("\\,|\\.|[\\+\\-\\*\\>\\<\\$\\=\\:\\~\\/]+");
        keywords = new String[] { "()"
        , "BY"
        , "CALL"
        , "CHARACTER"
        , "CLOSE"
        , "COMMON"
        , "COMPLEX"
        , "CONTINUE"
        , "DATA"
        , "DIMENSION"
        , "DO"
        , "ELSE"
        , "END"
        , "ENTRY"
        , "EQUIVALENCE"
        , "EXIT"
        , "EXTERNAL"
        , "FILE"
        , "FLOAT"
        , "FORMAT"
        , "GO"
        , "GOTO"
        , "IF"
        , "INCLUDE"
        , "INPUT"
        , "INTEGER"
        , "OPEN"
        , "PRINT"
        , "PUNCH"
        , "READ"
        , "REAL"
        , "RETURN"
        , "STOP"
        , "SUBROUTINE"
        , "THEN"
        , "TO"
        , "UNIT"
        , "WRITE"
        };
        putEntityReplacements();
        storeWords();
    } // prepareGenerator

    //////////////////////
    // SAX event generator
    //////////////////////

    /** Tests whether the matched operator is a exclamation mark "!",
     *	and treat it as a line end comment
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
    
    /** Pattern for leading spaces in label */
    private static final Pattern LEAD_SPACE = Pattern.compile("( *)");

	/** length of the label field */
	private static final int LABEL_LEN = 5;
	
    /** Processes the column oriented features of the source line
     */
    protected void processColumns() {
    	if (line.length() > 0) {
	    	char col1 = line.charAt(0);
    		if (false) {
    		
    		// first test for comment
   			} else if (col1 == 'C') { // comment
		        fireStartElement(COMMENT_TAG, spaceAttribute());
        		fireCharacters  (replaceNoSource(line.trim().substring(1)));
        		fireEndElement  (COMMENT_TAG);
        		linePos = trap; // otherwise ignore that source line
   			} else if (col1 == 'D') { // debugging code
		        fireStartElement(COMMENT_TAG, spaceAndModeAttr(DOCUMENT_MODE));
        		fireCharacters  (replaceNoSource(line.trim().substring(1)));
        		fireEndElement  (COMMENT_TAG);
        		linePos = trap; // otherwise ignore that source line
        		
        	// then evaluate label field (columns 1-5)
   			} else if (! line.startsWith("     ")) { // not 5 spaces - contains numeric line label
    			String label = (line.length() > LABEL_LEN) ? line.substring(0, LABEL_LEN) : line;
				Matcher match = LEAD_SPACE.matcher(label);
				match.lookingAt();
				spaceCount = match.group().length();
				label = label.trim();
        		linePos = spaceCount + label.length(); 
		        fireEmptyElement(LABEL_TAG, spaceAndValAttr(label));
    		} else { // 5 leading spaces
    		    linePos = LABEL_LEN;
    		    spaceCount = linePos;
    		} 
    		
    		// test continuation indicator in column 6
    		if (trap >= LABEL_LEN + 1) {
    			String col6 = line.substring(LABEL_LEN, LABEL_LEN + 1);
    			if (! col6.equals(" ")) {
    				spaceCount = LABEL_LEN;
    				fireEmptyElement(PREV_CONTINUE_TAG, spaceAndValAttr(col6));
    				linePos = LABEL_LEN + 1;
    			} // with continuation
    		} // 
    	} // non-empty line
	} // processColumns
	
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
    	modeAttr = attrs.getValue(MODE_ATTR);
        if (qName.equals(COMMENT_TAG   )) {
        	if (false) {
        	} else if (modeAttr == null) {
	        	saxBuffer.append("C");
	        } else if (modeAttr.equals(DOCUMENT_MODE)) {
	        	saxBuffer.append("D");
	        } else if (modeAttr.equals(LINE_END_MODE)) {
	            saxBuffer.append(lineEndComment);
	        } 
        }
    } // writeCommentStart

    /** Writes the end   of a target comment.
     *  @param qName tag which specifies the subtype of the comment
     */
    protected void writeCommentEnd  (String qName) {
        if (false) {
        } else if (qName.equals(COMMENT_TAG )) {
            // output nothing
        }
    } // writeCommentEnd

    /** Writes a label element into columns 1-5.
     *	This implementation appends the label without any column orientation.
     *  @param qName the qualified name (with prefix),
     *  or the empty string if qualified names are not available.
     *  @param attrs the attributes attached to the element.
     *  If there are no attributes, it shall be an empty Attributes object.
     */
    protected void writeLabel(String qName, Attributes attrs) {
    	try {
   			saxBuffer.append(attrs.getValue(VAL_ATTR));
        } catch (Exception exc) {
        	log.error("FortranTransformer.writeLabel", exc);
        }
    } // writeLabel

} // class FortranTransformer
