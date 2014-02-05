/*  Transforms PL/1 program files
    @(#) $Id: PL1Transformer.java 566 2010-10-19 16:32:04Z gfis $
    2010-06-23: DCL too
    2008-09-22: BIN and CHAR are keywords, too
	2008-01-21: reduced to ProgLangTransformer
    2007-10-29, Georg Fischer: copied from JavaTransformer
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

/** Transformer for programs in IBM's "programming language number one", PL/1.
 *  @see ProgLangTransformer for a general description of the
 *  the generated elements.
 *  @author Dr. Georg Fischer
 */
public class PL1Transformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: PL1Transformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private static Logger log = Logger.getLogger(PL1Transformer.class.getName());;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public PL1Transformer() {
        super();
        setFormatCodes("pl1,pli");
        setDescription("PL/1 Programming Language");
        setFileExtensions("pl1,pli");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables
	 *  common to generator and serializer.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        minColumn   =  1;
        maxColumn   = 72;
        bothStringTypes = true;
    	caseIndependant = true;
        doubleInnerApos = true;
        escapeCode      = LANG_PL1;
	} // initialize
	
    /** Prepares the generator.
     *  Does all heavy-weight initialization.
     */
    protected void prepareGenerator() {
		super.prepareGenerator();
        keywords = new String[] { "()"
        , "A"
        , "ACTIVATE"
        , "ALIGNED"
        , "ALLOCATE"
        , "AREA"
        , "ASSEMBLER"
        , "ATTENTION"
        , "AUTOMATIC"
        , "B"
        , "BACKWARDS"
        , "BASED"
        , "BEGIN"
        , "BIN"
        , "BINARY"
        , "BIT"
        , "BUFFERED"
        , "BUILTIN"
        , "BY"
        , "C"
        , "CALL"
        , "CHAR"
        , "CHARACTER"
        , "CHARGRAPHIC"
        , "CHECK"
        , "CLOSE"
        , "COBOL"
        , "COLUMN"
        , "COMPLETION"
        , "COMPLEX"
        , "CONDITION"
        , "CONNECTED"
        , "CONTROLLED"
        , "CONVERSION"
        , "COPY"
        , "DATA"
        , "DCL"
        , "DEACTIVATE"
        , "DECIMAL"
        , "DECLARE"
        , "DEFAULT"
        , "DEFINED"
        , "DELAY"
        , "DELETE"
        , "DESCRIPTORS"
        , "DIRECT"
        , "DISPLAY"
        , "DO"
        , "E"
        , "EDIT"
        , "ELSE"
        , "END"
        , "ENDFILE"
        , "ENDPAGE"
        , "ENTRY"
        , "ENTRYADDR"
        , "ENVIRONMENT"
        , "ERROR"
        , "EVENT"
        , "EXCLUSIVE"
        , "EXIT"
        , "EXTERNAL"
        , "F"
        , "FETCH"
        , "FILE"
        , "FINISH"
        , "FIXED"
        , "FIXEDOVERFLOW"
        , "FLOAT"
        , "FORMAT"
        , "FORTRAN"
        , "FREE"
        , "FROM"
        , "G"
        , "GENERIC"
        , "GET"
        , "GET-STRING"
        , "GO"
        , "GOTO"
        , "GO_TO"
        , "GRAPHIC"
        , "IF"
        , "IGNORE"
        , "IMAG"
        , "IN"
        , "INCLUDE"
        , "INITIAL"
        , "INPUT"
        , "INTER"
        , "INTERNAL"
        , "INTO"
        , "IRREDUCIBLE"
        , "ITEM"
        , "KEY"
        , "KEYED"
        , "KEYFROM"
        , "KEYTO"
        , "LABEL"
        , "LEAVE"
        , "LIKE"
        , "LINE"
        , "LINESIZE"
        , "LIST"
        , "LOCATE"
        , "MAIN"
        , "NAME"
        , "NOCHARGRAPHIC"
        , "NOEXECOPS"
        , "NOLOCK"
        , "NOMAP"
        , "NOMAPIN"
        , "NOMAPOUT"
        , "NOPRINT"
        , "NORESCAN"
        , "NOTE"
        , "OFFSET"
        , "ON"
        , "ONCHAR"
        , "ONSOURCE"
        , "OPEN"
        , "OPTIONS"
        , "ORDER"
        , "OTHERWISE"
        , "OUTPUT"
        , "OVERFLOW"
        , "P"
        , "PAGE"
        , "PAGESIZE"
        , "PENDING"
        , "PICTURE"
        , "POINTER"
        , "POSITION"
        , "PRINT"
        , "PRIORITY"
        , "PROCEDURE"
        , "PUT"
        , "PUT-STRING"
        , "R"
        , "RANGE"
        , "READ"
        , "REAL"
        , "RECORD"
        , "RECURSIVE"
        , "REDUCIBLE"
        , "REENTRANT"
        , "REFER"
        , "RELEASE"
        , "REORDER"
        , "REPEAT"
        , "REPLY"
        , "REREAD"
        , "RESCAN"
        , "RETCODE"
        , "RETURN"
        , "RETURNS"
        , "REVERT"
        , "REWRITE_FILE"
        , "SELECT"
        , "SEQUENTIAL"
        , "SET"
        , "SIGNAL"
        , "SIZE"
        , "SKIP"
        , "SNAP"
        , "STATEMENT"
        , "STATIC"
        , "STATUS"
        , "STOP"
        , "STREAM"
        , "STRING"
        , "STRINGRANGE"
        , "STRINGSIZE"
        , "SUBSCRIPTRANGE"
        , "SUBSTR"
        , "SYSTEM"
        , "TASK"
        , "THEN"
        , "TITLE"
        , "TO"
        , "TRANSIENT"
        , "TRANSMIT"
        , "UNALIGNED"
        , "UNBUFFERED"
        , "UNDEFINEDFILE"
        , "UNDERFLOW"
        , "UNLOCK"
        , "UNSPEC"
        , "UNTIL"
        , "UPDATE"
        , "VALUE"
        , "VARIABLE"
        , "VARYING"
        , "WAIT"
        , "WHEN"
        , "WHILE"
        , "WRITE"
        , "X"
        , "ZERODIVIDE"
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

} // class PL1Transformer
