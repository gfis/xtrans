/*  Filter which reformats ("pretty prints") SQL statements
    @(#) $Id: SQLPrettyFilter.java 616 2010-12-20 16:51:18Z gfis $
    2016-10-13: less imports
    2010-12-06: Dr. Georg Fischer: copied from 'LevelFilter'
*/
/*
 * Copyright 2010 Dr. Georg Fischer <punctum at punctum dot kom>
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
import  org.teherba.xtrans.parse.Token;
import  org.teherba.xtrans.BaseTransformer;
import  org.teherba.xtrans.proglang.TokenTransformer;
import  java.util.HashMap;
import  org.apache.log4j.Logger;

/** Pseudo transformer which accepts a SAX stream of events,
 *  and removes and adds whitespace such that the resulting SQL code is
 *  more pleasant for the human reader.
 *  <p>
 *  The following formatting rules are used:
 *  <ul>
 *  <li>All input whitespace (outside of strings) is replaced by 1 space, or finally removed.</li>
 *  <li>Resulting lines have a maximum length of 72 characters.</li>
 *  <li>Keywords "select", "from", "where" , "order", "group", "union", "left", "right", "outer", "and", "with",
 *      "create", "drop", "update", "insert", "delete", "commit"
 *      start a new line.</li>
 *  <li>"," (outside a function call) starts or ends a line, depending on parameter <em>-nlcomma="before|after|none"</em>.</li>
 *  <li>"(" (outside a function call) starts a new line.</li>
 *  <li>")" (outside a function call) starts and ends a new line.</li>
 *  <li>Keywords, identifiers, numbers and strings are separated by a single space.</li>
 *  <li>Arithmetic ("+", "-", "*", "/") and relational operators ("="; "&gt;=" etc.) are surrounded by a single space.</li>
 *  <li>"." has no surrounding spaces.</li>
 *  <li>";" is followed by a newline.</li>
 *  <li>All other operators are followed by a single space.</li>
 *  </ul>
 *  Example for a source text:
<pre>
                select
                    '1' as sort1
                ,   op.adropno as sort2
                ,   op.adropno
                ,   op.adrwsid
                ,   op.adropjn
                ,   jo.jobschenv as jobschenv
                ,   op.adropdes
                ,   'gry' as style
                ,   'ohne Vorl.'  as adrepext
                from XXRTOP op
                left join XXRTJOB jo on jo.jobmemb = op.adropjn and jo.xxrdatinfo = op.xxrdatinfo
                where op.adrid  = '<parm name="ADRID" />'
                  and op.xxrdatinfo = '<parm name="PLEX" />'
                  and not exists
                  ( select d2.adropead, d2.adrep_opno
                    from XXRTDEP d2
                    where  d2.adrid = '<parm name="ADRID" />'
                      and  d2.xxrdatinfo = '<parm name="PLEX" />'
                      and  d2.adropno = op.adropno
                  )
</pre>
 *  @author Dr. Georg Fischer
 */
public class SQLPrettyFilter extends TokenTransformer {
    public final static String CVSID = "@(#) $Id: SQLPrettyFilter.java 616 2010-12-20 16:51:18Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** Constructor.
     */
    public SQLPrettyFilter() {
        super();
        setFormatCodes("sqlpretty");
        setDescription("pretty print SQL");
        setFileExtensions("xml");
    } // Constructor

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = Logger.getLogger(SQLPrettyFilter.class.getName());
    } // initialize

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        log.warn("xtrans.SQLPrettyFilter can only be used as a filter");
        boolean result = false;
        return  result;
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*=========================== */

    /** nesting level in side function calls */
    private int funcLevel;
    /** indenting level */
    private int indentLevel;
    /** indent amount (number of spaces to be added / subtracted for next level) */
    private int indentAmount;
    /** state for space insertion */
    private State spaceState;
    /** number of spaces before the current element */
    private int spacesBefore;
    /** state for newline insertion */
    private State newlineState;
    /** current length of line assembled so far */
    private int curLength;
    /** current output line number */
    private int lineNo;
    /** the parent transformer */
    private BaseTransformer filterTransformer;
    /** previous {@link org.teherba.xtrans.parse.Token Token} which is already output */
    private Token oldToken;
    /* saxToken is the current token */
    /** Map for simple replacements in source strings */
    private HashMap/*<1.5*/<String, Character>/*1.5>*/ keywordMap;
    /** Enumeration for state values */
    private static enum State
        { NONE
        , NORMAL
        , PENDING
        , PREVIOUS
        } ;

    /** Receive notification of the beginning of the document.
     *  Initialize stateful variables.
     */
    public void startDocument() {
        try {
            super.startDocument();
            filterTransformer = (BaseTransformer) filterHandler;
            oldToken = new Token();
        /*
            filterTransformer.setMinColumn(0);
            filterTransformer.setMaxColumn(72);
        */
            keywordMap   = new HashMap/*<1.5*/<String, Character>/*1.5>*/(32);
            keywordMap.put("SELECT" , new Character('n'));
            keywordMap.put("FROM"   , new Character('n'));
            keywordMap.put("WHERE"  , new Character('n'));
            keywordMap.put("ORDER"  , new Character('n'));
            keywordMap.put("GROUP"  , new Character('n'));
            keywordMap.put("HAVING" , new Character('n'));
            keywordMap.put("UNION"  , new Character('n'));
            keywordMap.put("LEFT"   , new Character('n'));
            keywordMap.put("RIGHT"  , new Character('n'));
            keywordMap.put("OUTER"  , new Character('n'));
            keywordMap.put("AND"    , new Character('n'));
            keywordMap.put("WITH"   , new Character('n'));
            keywordMap.put("CREATE" , new Character('n'));
            keywordMap.put("DROP"   , new Character('n'));
            keywordMap.put("UPDATE" , new Character('n'));
            keywordMap.put("INSERT" , new Character('n'));
            keywordMap.put("DELETE" , new Character('n'));
            keywordMap.put("CASE"   , new Character('s'));
            keywordMap.put("WHEN"   , new Character('b'));
            keywordMap.put("ELSE"   , new Character('b'));
            keywordMap.put("END"    , new Character('e'));
            keywordMap.put("COMMIT" , new Character('n'));
            lineNo       = 0;
            funcLevel    = 0;
            indentLevel  = 0;
            indentAmount = 4;
            newlineState = State.NORMAL;
            spaceState   = State.NONE;
            setMinColumn( 8);
            setMaxColumn(72);
            spacesBefore = getMinColumn();
            curLength    = 0;
            filterTransformer.startDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // startDocument

    /** Receive notification of the end of the document.
     */
    public void endDocument() {
        try {
            super.endDocument();
            filterTransformer.endDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // endDocument

    /** Inserts a newline/indent sequence.
     */
    protected void insertNewLine() {
        try {
            if (true || newlineState == State.NONE) {
                lineNo ++;
                filterTransformer.startElement("", "", WHITESPACE_TAG, toAttributes(new String[]
                        { MODE_ATTR,  NEWLINE_MODE
                        , VAL_ATTR,   String.valueOf(lineNo)
                        }));
                filterTransformer.endElement("", "", WHITESPACE_TAG);
            } // != NONE
            spacesBefore = getMinColumn() + indentLevel * indentAmount;
            curLength = 0;
            newlineState = State.NORMAL;
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // insertNewLine

    /** Inserts the current {@link TokenTransformer#saxToken}
     */
    protected void insertToken() {
        try {
            if (newlineState == State.PENDING) {
                insertNewLine();
            }
            String tag = saxToken.getTag();
            if (false) {
            } else if (tag.equals(START_ELEMENT )) {
            } else if (tag.equals(END_ELEMENT   )) {
            } else { // CHARACTERS, WHITESPACE_TAG, IDENTIFIER_TAG etc.
                int len = saxToken.getVal().length();
                if (curLength + spacesBefore + len >= getMaxColumn()) {
                     insertNewLine();
                } else {
                    curLength += spacesBefore + len;
                }
            }
            saxToken.setSpacesBefore(spacesBefore);
            saxToken.fireContent(filterTransformer, "", "");
            spacesBefore = 0;
            oldToken = saxToken.clone();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // insertToken

    /** Increases the amount of indenting space.
     */
    protected void pushIndent() {
        indentLevel ++;
    } // pushIndent

    /** Decreases the amount of indenting space.
     */
    protected void popIndent() {
        indentLevel --;
        if (indentLevel < 0) {
            indentLevel = 0;
        }
    } // popIndent

    /** Determines the amount of pleasant whitespace between the {@link #oldToken}
     *  and the {@link TokenTransformer#saxToken}, and outputs the oldToken together with that whitespace.
     */
    protected void printSaxToken() {
        try {
            String tag = saxToken.getTag();
            if (tag.equals(WHITESPACE_TAG)) { // tab or newline
                if (false) {
                } else if (oldToken.getTag().equals(START_ELEMENT)  && oldToken.getVal().equals(ROOT_TAG    )) {
                    // ignore WHITESPACE (NL)
                } else { // replace by 1 space
                    if (curLength != 0) {
                        spacesBefore = 1;
                    }
                }
                // and ignore saxToken
            } else {
                spacesBefore += saxToken.getSpacesBefore();
                if (spacesBefore > 1 && curLength != 0) {
                    spacesBefore = 1;
                }
                if (false) {
                } else if (tag.equals(OPERATOR_TAG)) {
                    String operator = saxToken.getVal();
                    if (false) {
                    } else if (operator.equals(",")) {
                        if (funcLevel == 0) {
                            insertNewLine();
                        }
                        insertToken();
                        spacesBefore += 1;
                    } else if (operator.equals("(")) {
                        if (funcLevel > 0) {
                            funcLevel ++;
                            insertToken();
                        } else if (oldToken.getTag().equals(IDENTIFIER_TAG)) {
                            funcLevel = 1;
                            insertToken();
                        } else {
                            insertToken();
                            pushIndent();
                        }
                    } else if (operator.equals(")")) {
                        if (funcLevel > 0) {
                            funcLevel --;
                        } else {
                            // popIndent();
                            popIndent();
                            insertNewLine();
                        }
                        insertToken();
                    } else {
                        insertToken();
                    }
                    // OPERATOR_TAG
                } else if (tag.equals(KEYWORD_TAG)) {
                    String keyword = saxToken.getVal().toUpperCase();
                    saxToken.setVal(keyword); // upperCased
                    Character ch = keywordMap.get(keyword);
                    if (ch != null) {
                        switch(ch.charValue()) {
                            case 'n':
                                popIndent();
                                insertNewLine();
                                pushIndent();
                                // spacesBefore = indentLevel * indentAmount;
                                break;
                            case 'b':
                                if (keyword.equals("WHEN") && oldToken.getVal().equals("CASE")) {
                                } else {
                                    insertNewLine();
                                }
                                break;
                            case 's':
                                pushIndent();
                                break;
                            case 'e':
                                popIndent();
                                break;
                            default:
                                break;
                        } // switch
                    } // ch != null
                    if (false) {
                    } else if (keyword.equals("AND")) {
                        saxToken.setVal("  "  + keyword);
                    } else if (keyword.equals("OR")) {
                        saxToken.setVal("   " + keyword);
                    }
                    insertToken();
                    // KEYWORD
                } else { // any other ..._TAG
                    insertToken();
                }

                // post-processing
                if (false) {
                } else if (tag.equals(END_ELEMENT) && saxToken.getVal().equals(COMMENT_TAG)) {
                    newlineState = State.PENDING;
                    // COMMENT
                } else {
                }
            } // saxToken != WHITESPACE
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // printSaxToken

} // SQLPrettyFilter
