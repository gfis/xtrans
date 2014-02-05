/*  Transforms Cobol program fileENs
    @(#) $Id: CobolTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2010-06-09: collapse string and comment tags to one with mode attribute
	2009-12-22: no verbs, ENVIRONMENT was missing
	2009-12-11: GOBACK, EXEC, END-EXEC, SQL; keyverbs
    2008-03-26: match identifiers starting with a digit
	2008-02-01, Georg Fischer: similiar to FortranTransformer
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
import  java.util.HashSet;
import  java.util.regex.Matcher;
import  java.util.regex.Pattern;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Transformer for programs in COBOL.
 *  The source files are column oriented:
 *	<pre>
 *	columns 1-6	  numeric statement label
 *	column  7	  a "*" indicates a comment line
 *	columns 8-72  source statements
 *	columns 73-80 card sequence number
 *	</pre>
 *  @see ProgLangTransformer for a general description of the
 *  the generated elements.
 *  @author Dr. Georg Fischer
 *
 * From: http://www.cse.ohio-state.edu/~sgomori/314/langref.html#contin
<pre>
Continuation

Statements can be continued over several lines without doing anything more 
than placing the code on separate lines. The compiler will figure it out.

Very long alphanumeric literals can be continued on multiple lines 
by placing a hyphen in column 7 of the continuation lines and 
placing the remainder of the literal there. This continuation 
of the literal requires a beginning single quote. The literal on 
the previous line does not have an ending single quote but 
is considered to extend to column 72.


             MOVE 'THIS IS ONE SERIOUSLY LONG ALPHANUME
      -    'RIC LITERAL' TO WS-STRING.

The hyphen on the 2nd line is in column 7. The single quote 
on that line is in column 12 and the literal continues from there. 
The ending single quote on that line is required. Note that the 
portion of the literal that's on the first line does not have 
an ending quote. If the last 'E' is not in column 72 then it will 
be assumed that all characters between that 'E' and column 72 are 
spaces and will appear in the literal.
</pre>
 */
public class CobolTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: CobolTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = Logger.getLogger(CobolTransformer.class.getName());;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public CobolTransformer() {
        super();
        setFormatCodes("cobol,cob,cbl");
        setDescription("Cobol Programming Language");
        setFileExtensions("cob");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables
	 *  common to generator and serializer.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        minColumn   =  6;
        maxColumn   = 72;
        bothStringTypes = true;
    	caseIndependant = true;
        doubleInnerApos = true;
        escapeCode      = LANG_COBOL;
        setLanguage(LANG_COBOL);
        // lineEndComment = "!";
	} // initialize
	
    /** Prepares the generator.
     *  A valid field name is from 1 to 30 characters in length; 
     *	contains only the letters A-Z, the digits 0-9 and the hyphen; 
     *	contains at least one letter; does not begin or end with a hyphen; 
     *	and is not a COBOL reserved word.
     *	The keywords are derived from <a href="http://www.emacswiki.org/cgi-bin/wiki/cobol-mode.el">emacswiki</a>.
     *  Does all heavy-weight initialization.
     */
    protected void prepareGenerator() {
		super.prepareGenerator();
        identifierPattern  = Pattern.compile
                ("[0-9\\-]*[A-Za-z][A-Za-z0-9\\-\\_]*"); // contains at least 1 letter
        operatorPattern    = Pattern.compile
                ("\\,|\\.|[\\+\\-\\*\\>\\<\\$\\=\\:\\~\\/\\#\\@\\^]+"); // ^= in EXEC SQL
        keywords = new String[] { "()"
			, "ACCEPT"
			, "ACCESS"
			, "ACOS"
			, "ADD"
			, "ADDRESS"
			, "ADVANCING"
			, "AFTER"
			, "ALL"
			, "ALPHABET"
			, "ALPHABETIC"
			, "ALPHABETIC-LOWER"
			, "ALPHABETIC-UPPER"
			, "ALPHANUMERIC"
			, "ALPHANUMERIC-EDITED"
			, "ALSO"
			, "ALTER"
			, "ALTERNATE"
			, "AND"
			, "ANNUITY"
			, "ANY"
			, "APPROXIMATE"
			, "AREA"
			, "AREAS"
			, "ASCENDING"
			, "ASIN"
			, "ASSIGN"
			, "AT"
			, "ATAN"
			, "AUTHOR"
			, "BEFORE"
			, "BINARY"
			, "BLANK"
			, "BLOCK"
			, "BOTTOM"
			, "BY"
			, "CALL"
			, "CANCEL"
			, "CD"
			, "CF"
			, "CH"
			, "CHAR"
			, "CHARACTER"
			, "CHARACTER-SET"
			, "CHARACTERS"
			, "CHECKPOINT"
			, "CLASS"
			, "CLOCK-UNITS"
			, "CLOSE"
			, "COBOL"
			, "CODE"
			, "CODE-SET"
			, "COLLATING"
			, "COLUMN"
			, "COMMA"
			, "COMMON"
			, "COMMUNICATION"
			, "COMP"
			, "COMP-3"
			, "COMP-5"
			, "COMPUTATIONAL"
			, "COMPUTATIONAL-3"
			, "COMPUTATIONAL-5"
			, "COMPUTE"
			, "CONFIGURATION"
			, "CONTAINS"
			, "CONTENT"
			, "CONTINUE"
			, "CONTROL"
			, "CONTROLS"
			, "CONVERTING"
			, "COPY"
			, "CORR"
			, "CORRESPONDING"
			, "COS"
			, "COUNT"
			, "CURRENCY"
			, "CURRENT-DATE"
			, "DATA"
			, "DATE"
			, "DATE-COMPILED"
			, "DATE-OF-INTEGER"
			, "DATE-WRITTEN"
			, "DAY"
			, "DAY-OF-INTEGER"
			, "DAY-OF-WEEK"
			, "DE"
			, "DEBUG-CONTENTS"
			, "DEBUG-ITEM"
			, "DEBUG-LINE"
			, "DEBUG-NAME"
			, "DEBUG-SUB-1"
			, "DEBUG-SUB-2"
			, "DEBUG-SUB-3"
			, "DEBUGGING"
			, "DECIMAL-POINT"
			, "DECLARATIVES"
			, "DELETE"
			, "DELIMITED"
			, "DELIMITER"
			, "DEPENDING"
			, "DESCENDING"
			, "DESTINATION"
			, "DETAIL"
			, "DISABLE"
			, "DISPLAY"
			, "DIVIDE"
			, "DIVISION"
			, "DOWN"
			, "DUPLICATES"
			, "DYNAMIC"
			, "EGI"
			, "ELSE"
			, "EMI"
			, "ENABLE"
			, "END"
			, "END-ADD"
			, "END-COMPUTE"
			, "END-DELETE"
			, "END-DIVIDE"
			, "END-EVALUATE"
			, "END-EXEC"
			, "END-IF"
			, "END-MULTIPLY"
			, "END-OF-PAGE"
			, "END-PERFORM"
			, "END-READ"
			, "END-RECEIVE"
			, "END-RETURN"
			, "END-REWRITE"
			, "END-SEARCH"
			, "END-START"
			, "END-STRING"
			, "END-SUBTRACT"
			, "END-UNSTRING"
			, "END-WRITE"
			, "ENTER"
			, "ENVIRONMENT"
			, "EOP"
			, "EQUAL"
			, "ERROR"
			, "ESI"
			, "EVALUATE"
			, "EVERY"
			, "EXCEPTION"
			, "EXCLUSIVE"
			, "EXEC"
			, "EXIT"
			, "EXTEND"
			, "EXTENDED-STORAGE"
			, "EXTERNAL"
			, "FACTORIAL"
			, "FALSE"
			, "FD"
			, "FILE"
			, "FILE-CONTROL"
			, "FILLER"
			, "FINAL"
			, "FIRST"
			, "FOOTING"
			, "FOR"
			, "FROM"
			, "FUNCTION"
			, "GENERATE"
			, "GENERIC"
			, "GIVING"
			, "GLOBAL"
			, "GO"
			, "GOBACK"
			, "GREATER"
			, "GROUP"
			, "GUARDIAN-ERR"
			, "HEADING"
			, "HIGH-VALUE"
			, "HIGH-VALUES"
			, "I-O"
			, "I-O-CONTROL"
			, "IDENTIFICATION"
			, "IF"
			, "IN"
			, "INDEX"
			, "INDEXED"
			, "INDICATE"
			, "INITIAL"
			, "INITIALIZE"
			, "INITIATE"
			, "INPUT"
			, "INPUT-OUTPUT"
			, "INSPECT"
			, "INSTALLATION"
			, "INTEGER"
			, "INTEGER-OF-DATE"
			, "INTEGER-OF-DAY"
			, "INTEGER-PART"
			, "INTO"
			, "INVALID"
			, "IS"
			, "JUST"
			, "JUSTIFIED"
			, "KEY"
			, "LABEL"
			, "LAST"
			, "LEADING"
			, "LEFT"
			, "LENGTH"
			, "LESS"
			, "LIMIT"
			, "LIMITS"
			, "LINAGE"
			, "LINAGE-COUNTER"
			, "LINE"
			, "LINE-COUNTER"
			, "LINKAGE"
			, "LOCK"
			, "LOCKFILE"
			, "LOG"
			, "LOG10"
			, "LOW-VALUE"
			, "LOW-VALUES"
			, "LOWER-CASE"
			, "MAX"
			, "MEAN"
			, "MEDIAN"
			, "MEMORY"
			, "MERGE"
			, "MESSAGE"
			, "MIDRANGE"
			, "MIN"
			, "MOD"
			, "MODE"
			, "MODULES"
			, "MOVE"
			, "MULTIPLE"
			, "MULTIPLY"
			, "NATIVE"
			, "NEGATIVE"
			, "NEXT"
			, "NO"
			, "NOT"
			, "NULL"
			, "NULLS"
			, "NUMBER"
			, "NUMERIC"
			, "NUMERIC-EDITED"
			, "NUMVAL"
			, "NUMVAL-C"
			, "OBJECT-COMPUTER"
			, "OCCURS"
			, "OF"
			, "OFF"
			, "OMITTED"
			, "ON"
			, "OPEN"
			, "OPTIONAL"
			, "OR"
			, "ORD"
			, "ORD-MAX"
			, "ORD-MIN"
			, "ORDER"
			, "ORGANIZATION"
			, "OTHER"
			, "OUTPUT"
			, "OVERFLOW"
			, "PACKED-DECIMAL"
			, "PADDING"
			, "PAGE"
			, "PAGE-COUNTER"
			, "PERFORM"
			, "PF"
			, "PH"
			, "PIC"
			, "PICTURE"
			, "PLUS"
			, "POINTER"
			, "POSITION"
			, "POSITIVE"
			, "PRESENT-VALUE"
			, "PRINTING"
			, "PROCEDURE"
			, "PROCEDURES"
			, "PROCEED"
			, "PROGRAM"
			, "PROGRAM-ID"
			, "PROGRAM-STATUS"
			, "PROGRAM-STATUS-1"
			, "PROGRAM-STATUS-2"
			, "PROMPT"
			, "PROTECTED"
			, "PURGE"
			, "QUEUE"
			, "QUOTE"
			, "QUOTES"
			, "RANDOM"
			, "RANGE"
			, "RD"
			, "READ"
			, "RECEIVE"
			, "RECEIVE-CONTROL"
			, "RECORD"
			, "RECORDS"
			, "REDEFINES"
			, "REEL"
			, "REFERENCE"
			, "REFERENCES"
			, "RELATIVE"
			, "RELEASE"
			, "REM"
			, "REMAINDER"
			, "REMOVAL"
			, "RENAMES"
			, "REPLACE"
			, "REPLACING"
			, "REPLY"
			, "REPORT"
			, "REPORTING"
			, "REPORTS"
			, "RERUN"
			, "RESERVE"
			, "RESET"
			, "RETURN"
			, "REVERSE"
			, "REVERSED"
			, "REWIND"
			, "REWRITE"
			, "RF"
			, "RH"
			, "RIGHT"
			, "ROUNDED"
			, "RUN"
			, "SAME"
			, "SD"
			, "SEARCH"
			, "SECTION"
			, "SECURITY"
			, "SEGMENT"
			, "SEGMENT-LIMIT"
			, "SELECT"
			, "SEND"
			, "SENTENCE"
			, "SEPARATE"
			, "SEQUENCE"
			, "SEQUENTIAL"
			, "SET"
			, "SHARED"
			, "SIGN"
			, "SIN"
			, "SIZE"
			, "SORT"
			, "SORT-MERGE"
			, "SOURCE"
			, "SOURCE-COMPUTER"
			, "SPACE"
			, "SPACES"
			, "SPECIAL-NAMES"
			, "SQL"
			, "SQRT"
			, "STANDARD"
			, "STANDARD-1"
			, "STANDARD-2"
			, "STANDARD-DEVIATION"
			, "START"
			, "STARTBACKUP"
			, "STATUS"
			, "STOP"
			, "STRING"
			, "SUB-QUEUE-1"
			, "SUB-QUEUE-2"
			, "SUB-QUEUE-3"
			, "SUBTRACT"
			, "SUM"
			, "SUPPRESS"
			, "SYMBOLIC"
			, "SYNC"
			, "SYNCDEPTH"
			, "SYNCHRONIZED"
			, "TABLE"
			, "TAL"
			, "TALLYING"
			, "TAN"
			, "TAPE"
			, "TERMINAL"
			, "TERMINATE"
			, "TEST"
			, "TEXT"
			, "THAN"
			, "THEN"
			, "THROUGH"
			, "THRU"
			, "TIME"
			, "TIMES"
			, "TO"
			, "TOP"
			, "TRAILING"
			, "TRUE"
			, "TYPE"
			, "UNIT"
			, "UNLOCK"
			, "UNLOCKFILE"
			, "UNLOCKRECORD"
			, "UNSTRING"
			, "UNTIL"
			, "UP"
			, "UPON"
			, "UPPER-CASE"
			, "USAGE"
			, "USE"
			, "USING"
			, "VALUE"
			, "VALUES"
			, "VARIANCE"
			, "VARYING"
			, "WHEN"
			, "WHEN-COMPILED"
			, "WITH"
			, "WORDS"
			, "WORKING-STORAGE"
			, "WRITE"
			, "ZERO"
			, "ZEROES"
        };
        putEntityReplacements();
        storeWords();
	/*        
        keyverbs = new String[] { "()"
			, "ACCEPT"
			, "ADD"
			, "ALTER"
			, "ASSIGN"
			, "CALL"
			, "CANCEL"
			, "CLOSE"
			, "COMPUTE"
			, "CONTINUE"
			, "COPY"
			, "DELETE"
			, "DISABLE"
			, "DISPLAY"
			, "DIVIDE"
			, "ELSE"
			, "ENABLE"
			, "ENTER"
			, "EVALUATE"
			, "EXEC"
			, "EXIT"
			, "GENERATE"
			, "GO"
			, "GOBACK"
			, "IF"
			, "INITIALIZE"
			, "INITIATE"
			, "INSPECT"
			, "MERGE"
			, "MOVE"
			, "MULTIPLY"
			, "OPEN"
			, "PERFORM"
			, "PROCEED"
			, "READ"
			, "RECEIVE"
			, "RETURN"
			, "SEARCH"
			, "SELECT"
			, "SEND"
			, "SET"
			, "SORT"
			, "SORT-MERGE"
			, "STOP"
			, "SUBTRACT"
			, "UNSTRING"
			, "USE"
			, "WHEN"
			, "WRITE"
        };
		storeVerbs();
	*/
        // log.info("CobolTransformer.prepareGenerator: forceUpperCase = " + forceUpperCase + ", caseIndependant = " + caseIndependant);
    } // prepareGenerator

    /** Matches numbers starting with a digit, or also matches identifiers, 
     *	since identifiers may start with a digit.
     *  @param line input line containing the digit
     *  @param linePos position where the digit was found
     *  @param trap position behind the end of the line
     *  @return position behind the element, - 1
     */
    protected int matchNumber(String line, int linePos, int trap) {
        int pos2 = linePos + 1; // position behind the element just recognized
        Matcher matcher = identifierPattern.matcher(line.substring(linePos));
        if (matcher.lookingAt()) { // code from ProgLangTransformer.matchIdentifier:
            pos2 = linePos + matcher.end();
            String id = line.substring(linePos, pos2);
            if (forceUpperCase && caseIndependant) {
            	id = id.toUpperCase();
            }
			if (false) {
		/*
            } else if (testVerb(id)) { // key verb of the language
                fireEmptyElement(VERB_TAG      , spaceAndValAttr(id));
        */
            } else if (testWord(id)) { // other keyword of the language
                fireEmptyElement(KEYWORD_TAG   , spaceAndValAttr(id));
            } else { // normal identifier
                fireEmptyElement(IDENTIFIER_TAG, spaceAndValAttr(id));
            }
        } else { // no identifier - normal code from ProgLangTranformer.matchNumber
        	matcher = numberPattern.matcher(line.substring(linePos));
	        if (matcher.lookingAt()) {
	            pos2 = linePos + matcher.end();
	            String number = line.substring(linePos, pos2);
	            fireEmptyElement(NUMBER_TAG, spaceAndValAttr(number));
	        } else {
	            log.error("unmatched number in line " + (lineNo + 1)
	                    + " at column " + (linePos + 1) + ": " + line.substring(linePos));
	        }
	    } // if no identifier
        return pos2 - 1; // 1 before the end
    } // matchNumber

    /** Processes the column oriented features of the source line
     */
    protected void processColumns() {
    	if (line.length() > minColumn) {
	    	char col7 = line.charAt(minColumn);
	    	String endTag = null;
    		if (false) { // tests for comment
   			} else if (col7 == '*') { // comment
		        fireStartElement(COMMENT_TAG, spaceAttribute());
		        endTag =		 COMMENT_TAG;
   			} else if (col7 == 'D') { // debugging code - treat as comment
                fireStartElement(COMMENT_TAG, spaceAndModeAttr(DOCUMENT_MODE));
		        endTag =		 COMMENT_TAG;
   			} else if (col7 == '-') { // continued string
		        fireStartElement(CONCAT_TAG  , spaceAttribute());
		        fireEndElement  (CONCAT_TAG);
		        linePos = minColumn + 1; // consume '-'
   			} else if (col7 != ' ') { // for example '/'
		        fireStartElement(COMMENT_TAG , spaceAndValAttr(line.substring(minColumn, minColumn + 1)));
		        endTag =		 COMMENT_TAG;
    		} 
    		if (endTag != null) {
    			fireCharacters  (replaceNoSource(line.substring(minColumn).trim().substring(1)));
        		fireEndElement  (endTag);
        		linePos = trap; // otherwise ignore that source line
    		}
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
        if (false) {
        } else if (qName.equals(COMMENT_TAG )) {
        	if (false) {
        	} else if (modeAttr == null) {
	        	String col7 = attrs.getValue(VAL_ATTR);
    	    	if (col7 == null) {
        			saxBuffer.append("*");
        		} else {
        			saxBuffer.append(col7);
        		}
	            // saxBuffer.append   (COMMENT_START );
	        } else if (modeAttr.equals(DOCUMENT_MODE)) {
	            saxBuffer.append("D");
	        } else if (modeAttr.equals(LINE_END_MODE)) {
	            saxBuffer.append(lineEndComment);
	        } 
        } else if (qName.equals(CONCAT_TAG  )) {
            saxBuffer.append('-');
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

} // class CobolTransformer
