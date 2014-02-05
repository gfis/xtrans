/*  Transforms SQL statements  
    @(#) $Id: SQLTransformer.java 611 2010-12-12 19:14:56Z gfis $
    2010-06-09: collapse string and comment tags to one with mode attribute
    2010-05-31: many more keywords from SQL99
    2010-05-21: WITH, CALL
    2008-06-30: more keywords
    2008-01-23, Georg Fischer: copied from CSSTransformer
    
    Still to do:
    - What about the '<>' comparision operator?
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
import  org.apache.log4j.Logger;

/** Transformer for the Structured Query Language (SQL) used in relational databases
 *	like DB2, Oracle, MySQL etc.
 *  See {@link ProgLangTransformer} for a general description of the
 *	the generated elements.
 *  @author Dr. Georg Fischer
 */
public class SQLTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: SQLTransformer.java 611 2010-12-12 19:14:56Z gfis $";

    /** log4j logger (category) */
    private static Logger log = Logger.getLogger(SQLTransformer.class.getName());

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public SQLTransformer() {
        super();
        setFormatCodes("sql");
        setDescription("Structured Query Language");
        setFileExtensions("sql,ddl");
    } // Constructor

	/** Initializes the (quasi-constant) global structures and variables
	 *  common to generator and serializer.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        lineEndComment  = "--";
        doubleInnerApos = true;
        caseIndependant = true;
        bothStringTypes = true; // for mySQL (?)
		escapeCode      = LANG_PL1;
	} // initialize
	
    /** Prepares the class.
     *  Does all heavy-weight initialization.
     */
    protected void prepareGenerator() {
        super.prepareGenerator();
        identifierPattern  = Pattern.compile
                ("[\\w\\_]+");
        numberPattern      = Pattern.compile
                ("\\d+"); 
        // reserved in SQL99, from http://www.sql.org/sql-database/postgresql/manual/sql-keywords-appendix.html
        keywords = new String[] { "()" 
		, "absolute"
		, "action"
		, "add"
		, "admin"
		, "after"
		, "aggregate"
		, "alias"
		, "all"
		, "allocate"
		, "alter"
		, "and"
		, "any"
		, "are"
		, "array"
		, "as"
		, "asc"
		, "assertion"
		, "at"
		, "authorization"
		, "before"
		, "begin"
		, "binary"
		, "bit"
		, "blob"
		, "boolean"
		, "both"
		, "breadth"
		, "by"
		, "call"
		, "cascade"
		, "cascaded"
		, "case"
		, "cast"
		, "catalog"
		, "char"
		, "character"
		, "check"
		, "class"
		, "clob"
		, "close"
		, "collate"
		, "collation"
		, "column"
		, "commit"
		, "completion"
		, "connect"
		, "connection"
		, "constraint"
		, "constraints"
		, "constructor"
		, "continue"
		, "corresponding"
		, "create"
		, "cross"
		, "cube"
		, "current"
		, "current_date"
		, "current_path"
		, "current_role"
		, "current_time"
		, "current_timestamp"
		, "current_user"
		, "cursor"
		, "cycle"
		, "data"
		, "date"
		, "day"
		, "deallocate"
		, "dec"
		, "decimal"
		, "declare"
		, "default"
		, "deferrable"
		, "deferred"
		, "delete"
		, "depth"
		, "deref"
		, "desc"
		, "describe"
		, "descriptor"
		, "destroy"
		, "destructor"
		, "deterministic"
		, "diagnostics"
		, "dictionary"
		, "disconnect"
		, "distinct"
		, "domain"
		, "double"
		, "drop"
		, "dynamic"
		, "each"
		, "else"
		, "end"
		, "end-exec"
		, "equals"
		, "escape"
		, "every"
		, "except"
		, "exception"
		, "exec"
		, "execute"
		, "exists"
		, "external"
		, "false"
		, "fetch"
		, "first"
		, "float"
		, "for"
		, "foreign"
		, "found"
		, "free"
		, "from"
		, "full"
		, "function"
		, "general"
		, "get"
		, "global"
		, "go"
		, "goto"
		, "grant"
		, "group"
		, "grouping"
		, "having"
		, "host"
		, "hour"
		, "identity"
		, "ignore"
		, "immediate"
		, "in"
		, "indicator"
		, "initialize"
		, "initially"
		, "inner"
		, "inout"
		, "input"
		, "insert"
		, "int"
		, "integer"
		, "intersect"
		, "interval"
		, "into"
		, "is"
		, "isolation"
		, "iterate"
		, "join"
		, "key"
		, "language"
		, "large"
		, "last"
		, "lateral"
		, "leading"
		, "left"
		, "less"
		, "level"
		, "like"
		, "limit"
		, "local"
		, "localtime"
		, "localtimestamp"
		, "locator"
		, "map"
		, "match"
		, "minute"
		, "modifies"
		, "modify"
		, "module"
		, "month"
		, "names"
		, "national"
		, "natural"
		, "nchar"
		, "nclob"
		, "new"
		, "next"
		, "no"
		, "none"
		, "not"
		, "null"
		, "numeric"
		, "object"
		, "of"
		, "off"
		, "old"
		, "on"
		, "only"
		, "open"
		, "operation"
		, "option"
		, "or"
		, "order"
		, "ordinality"
		, "out"
		, "outer"
		, "output"
		, "pad"
		, "parameter"
		, "parameters"
		, "partial"
		, "path"
		, "postfix"
		, "precision"
		, "prefix"
		, "preorder"
		, "prepare"
		, "preserve"
		, "primary"
		, "prior"
		, "privileges"
		, "procedure"
		, "public"
		, "read"
		, "reads"
		, "real"
		, "recursive"
		, "ref"
		, "references"
		, "referencing"
		, "relative"
		, "restrict"
		, "result"
		, "return"
		, "returns"
		, "revoke"
		, "right"
		, "role"
		, "rollback"
		, "rollup"
		, "routine"
		, "row"
		, "rows"
		, "savepoint"
		, "schema"
		, "scope"
		, "scroll"
		, "search"
		, "second"
		, "section"
		, "select"
		, "sequence"
		, "session"
		, "session_user"
		, "set"
		, "sets"
		, "size"
		, "smallint"
		, "some"
		, "space"
		, "specific"
		, "specifictype"
		, "sql"
		, "sqlexception"
		, "sqlstate"
		, "sqlwarning"
		, "start"
		, "state"
		, "statement"
		, "static"
		, "structure"
		, "system_user"
		, "table"
		, "temporary"
		, "terminate"
		, "than"
		, "then"
		, "time"
		, "timestamp"
		, "timezone_hour"
		, "timezone_minute"
		, "to"
		, "trailing"
		, "transaction"
		, "translation"
		, "treat"
		, "trigger"
		, "true"
		, "under"
		, "union"
		, "unique"
		, "unknown"
		, "unnest"
		, "update"
		, "usage"
		, "user"
		, "using"
		, "value"
		, "values"
		, "varchar"
		, "variable"
		, "varying"
		, "view"
		, "when"
		, "whenever"
		, "where"
		, "with"
		, "without"
		, "work"
		, "write"
		, "year"
		, "zone"
		        
        , "longvarbinary"
        , "longvarchar"
        , "numeric"
        , "varbinary"
        , "varchar2"
        };
        storeWords();
    } // prepareGenerator

    //////////////////////
    // SAX event generator
    //////////////////////

    /** Matches all punctuation and operator elements apart from parentheses, ";", "," and ".".
     *  This special implementation cares for SQL's line end comment ("--").
     *  @param line input line containing the start of the operator at position <em>linePos</em>
     *  @param linePos position where the operator starts
     *  @return position behind the element, - 1
     */
    protected int matchOperator(String line, int linePos, int trap) {
        int pos2 = linePos + 1; // position behind the element just recognized
        Matcher matcher = operatorPattern.matcher(line.substring(linePos));
        if (matcher.lookingAt()) {
            pos2 = linePos + matcher.end();
            String oper = line.substring(linePos, pos2);
            if (! oper.startsWith(lineEndComment)) {
            	fireEmptyElement(OPERATOR_TAG, spaceAndValAttr(oper));
            } else { // line end comment starts here
	            int pos1 = linePos + lineEndComment.length();
		        fireStartElement(COMMENT_TAG, spaceAndModeAttr(LINE_END_MODE));
        	    fireCharacters(replaceNoSource(line.substring(pos1)));
            	fireEndElement(COMMENT_TAG);
            	pos2 = trap; // skip to end of line
            } // LEC
        } else {
            log.error("unknown operator in line " + (lineNo + 1)
                    + " at column " + (linePos + 1) + ": " + line.substring(linePos));
        }
        return pos2 - 1; // 1 before the end
    } // matchOperator

    /////////////////////////
    // SAX content handler //
    /////////////////////////

} // class SQLTransformer
