/*  Abstract base class for all programming language transformers
    @(#) $Id: ProgLangTransformer.java 801 2011-09-12 06:16:01Z gfis $
    2017-05-28: javadoc 1.8
    2010-06-16: tokenStack (successor of tagStack)
    2010-06-09: collapse string and comment tags to one with mode attribute; se and ee
    2010-06-05: stra > sta, str > stq, strb > stb, sem > op
    2009-12-22: treat ( [ { parentheses as operators because of possible nesting errors
    2009-12-16: toUpperCase ids, keywords/verbs if caseIndependant; forceUpperCase; (G+R married 10 years ago)
    2008-09-22: 'testWord' had reverse condition
    2008-01-23: restructured, processLine isolated
    2007-12-04: copied from program/ProgramTransformer -> generalized scanner
    2007-10-29, Georg Fischer: extracted from JavaTransformer
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
import  org.teherba.xtrans.CharTransformer;
import  org.teherba.xtrans.NestedLineReader;
import  org.teherba.xtrans.parse.Token;
import  java.io.BufferedReader;
import  java.util.HashSet;
import  java.util.Stack;
import  java.util.regex.Matcher;
import  java.util.regex.Pattern;
import  org.xml.sax.Attributes;
import  org.xml.sax.helpers.AttributesImpl;
import  org.apache.log4j.Logger;

/** Abstract transformer for various programming languages.
 *  This implementation works mainly unaltered for C and Java languages,
 *  but it is also prepared for more column oriented languages like Cobol or Fortran.
 *  <p>
 *  The following XML elements are generated:
 *  <ul>
 *  <li>&lt;nl y="0"&gt; -
 *  For the precise location of all source language elements,
 *  there is a newline element at the <em>beginning</em> of each line,
 *  with the line number ("y") starting at 0.
 *  All other elements have an "sp" attribute for the number of spaces <em>before</em> the element.
 *  Closing parentheses are prefixed by a dummy &lt;sp/&gt; element which indicates the
 *  preceeding number of spaces.
 *  Tabs outside of strings are (not yet) expanded according to the "tab" option setting.
 *  </li>
 *  <li>&lt;kw/&gt;   - language keywords, reserved words</li>
 *  <li>&lt;vb/&gt;   - verbs = language keywords which start a statement (e.g. in Cobol)</li>
 *  <li>&lt;id/&gt;   - identifiers, type names etc.</li>
 *  <li>&lt;num/&gt;  - decimal numbers</li>
 *  <li>&lt;op/&gt;   - operators like "&gt;&gt;=", "++"</li>
 *  <li>&lt;cmt&gt;   - normal comment starting with slash asterisk</li>
 *  <li>&lt;lec&gt;   - line end comment starting with slash slash</li>
 *  <li>&lt;doc&gt;   - documentation comment starting with slash asterisk asterisk</li>
 *  <li>&lt;curl&gt;   - nesting with curly  brackets "{  }"</li>
 *  <li>&lt;sqr&gt;   - nesting with square brackets "[  ]"</li>
 *  <li>&lt;rnd&gt;   - nesting with round  brackets "(  )"</li>
 *  <li>&lt;concat&gt;   - string concatenation (for continued string values in Cobol)
 *  <li>&lt;chr/&gt;  - single character denotation, either
 *    <ul>
 *    <li>printable in "p" attribute</li>
 *    <li>control (backslash escaped) "b" attribute</li>
 *    <li>code in "h" attribute (up to 2 hex digits)</li>
 *    <li>code in "u" attribute (up to 4 hex digits)</li>
 *    </ul>
 *  <li>
 *  <li>&lt;str&gt;  - string of characters which were enclosed in quotes,
 *      with entities replaced for XML</li>
 *  <li>... some more elements which are described in the *_TAG constants</li>
 *  </ul>
 *  The representations of the comment and parentheses delimiter strings
 *  are set appropriately for each programming language. Also, the behaviour of
 *  quoting, lower/uppercase relevance, inner quotes, left and right margins
 *  is set individually for each language. All heavy-weight analysis is done
 *  in this class, while the language specific classes should only configure it
 *  and handle special exceptions.
 *  @author Dr. Georg Fischer
 */
public abstract class ProgLangTransformer extends CharTransformer {
    public final static String CVSID = "@(#) $Id: ProgLangTransformer.java 801 2011-09-12 06:16:01Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** Root element tag */
    public static final String ROOT_TAG             = "program";
    /** Begin of program tag (for {@link TokenTransformer} */
    public static final String START_ELEMENT        = "se";
    /** End of program tag (for {@link TokenTransformer}) */
    public static final String END_ELEMENT          = "ee";
    /** Tag for character content (for {@link TokenTransformer}), max length 64, multiple if longer, no tabs or line breaks */
    public static final String CHARACTERS           = "ca";
    /** Backslash Newline element tag */
    public static final String BACKSLASH_NL_TAG     = "bnl";
    /** Character element tag for single character denotation with "b", "h", "p" or "u" attribute */
    public static final String CHAR_TAG             = "chr";
    /** Normal Comment element tag for C comment starting with slash asterisk (with {@link #MODE_ATTR} = cm, dc, le) */
    public static final String COMMENT_TAG          = "cmt";
    /** String concatenation operator (for continued string values in Cobol) */
    public static final String CONCAT_TAG           = "concat";
    /** Identifier element tag for identifiers, type names etc.*/
    public static final String IDENTIFIER_TAG       = "id";
    /** Keyword element tag for language keywords, reserved words (eventually with {@link #MODE_ATTR}={@link #VERB_MODE})*/
    public static final String KEYWORD_TAG          = "kw";
    /** Label element tag */
    public static final String LABEL_TAG            = "lab";
    /* Newline element tag */
    //  public static final String NEWLINE_TAG          = "n";
    /** Element indicating that the current line will be continued with the following line */
    public static final String NEXT_CONTINUE_TAG    = "nc";
    /** Element indicating that the current line is a continuation of the previous line */
    public static final String PREV_CONTINUE_TAG    = "pc";
    /** Number element tag for simple decimal numbers (without fractions) */
    public static final String NUMBER_TAG           = "num";
    /** Operator element tag for operators like "&gt;&gt;=", "++" etc. */
    public static final String OPERATOR_TAG         = "op";
    /** Preprocessor or pragma instruction element tag */
    public static final String PRAGMA_TAG           = "pragma";

    /* The following 3 are not used because of possible nesting errors
    -** Curly bracket "{" element tag *-
    public static final String CURLY_TAG            = "curl";
    -** Round bracket "(" element tag *-
    public static final String ROUND_TAG            = "rnd";
    -** Square bracket "[" element tag *-
    public static final String SQUARE_TAG           = "sqr";
    */

    /** Statement terminator tag */
    public static final String SEMI_TAG             = "sem";
    /** Generalized string element tag (with {@link #MODE_ATTR} = ap, bt, dq) */
    public static final String STRING_TAG           = "str";
    /** Tag for whitespace (with {@link #MODE_ATTR} = newline/nl, prefix/pr, postfix/po, spaces/sp, tabs/tb) */
    public static final String WHITESPACE_TAG       = "ws" ;

    /** name for mode attribute */
    public static final String MODE_ATTR            = "m";
    /** name for line number attribute */
    public static final String LINE_NO_ATTR         = "n"; // offset from top
    /** name for prefix attribute */
    public static final String PRE_ATTR             = "l"; // left  margin text
    /** name for postfix attribute */
    public static final String POST_ATTR            = "r"; // right margin text
    /** name for space attribute */
    public static final String SP_ATTR              = "s";
    /** name for value attribute */
    public static final String VAL_ATTR             = "v";

    /** value for the mode attribute of a single (apostrophe) quoted string */
    public static final String ASTRING_MODE         = "ap";
    /** value for the mode attribute of a backticks           quoted string */
    public static final String BSTRING_MODE         = "bt";
    /** value for the mode attribute of a double              quoted string */
    public static final String DSTRING_MODE         = "dq";

    /** value of the mode attribute for a normal comment starting with slash asterisk */
    public static final String COMMENT_MODE         = "cm";
    /** value of the mode attribute for a documentation comment starting with slash asterisk asterisk*/
    public static final String DOCUMENT_MODE        = "dc";
    /** value of the mode attribute for a line end comment starting with slash slash */
    public static final String LINE_END_MODE        = "le";

    /** value for the mode attribute of newline whitespace */
    public static final String NEWLINE_MODE         = "nl";
    /** value for the mode attribute of line prefixes */
    public static final String PREFIX_MODE          = "pr";
    /** value for the mode attribute of line postfixes */
    public static final String POSTFIX_MODE         = "po";
    /** value for the mode attribute of spaces */
    public static final String SPACE_MODE           = "sp";
    /** value for the mode attribute of tabs */
    public static final String TAB_MODE             = "tb";

    /** value of the mode attribute of language keywords which start a statement (e.g. in Cobol) */
    public static final String VERB_MODE            = "vb";

    /** string for start of comment */
    protected static final String COMMENT_START     = "/*";
    /** string for start of comment (Pascal) */
    protected static final String PAS_COMMENT_START = "{";
    /** string for start of comment (Pascal) */
    protected static final String PAS_COMMENT2_START= "(*";
    /** string for start of preprocessor instruction */
    protected static final String PRAGMA_START      = "#";  // for C
    /** string for end   of comment */
    protected static final String COMMENT_END       = "*/";
    /** string for end   of comment (Pascal) */
    protected static final String PAS_COMMENT_END   = "}";
    /** string for end   of document comment */
    protected static final String DOCUMENT_START    = "/*";
    /** string for end   of document comment */
    protected static final String DOCUMENT_END      = "*/";
    /** string for end   of comment (Pascal) */
    protected static final String PAS_COMMENT2_END  = "*)";
    /** string for end   of line comment */
    protected String lineEndComment;
    /** string for end   of preprocessor instruction */
    protected static final String PRAGMA_END        = "";

    /** matching end of current comment opening mark */
    protected String endComment; // "*/" for C, "*)" or "}" for Pascal
    /** matching end of current string  opening mark */
    protected String endQuote; // quote, apostrophe, backtick
    /** alternative quote in string */
    protected String altQuote; // apostrophe for quote and vice versa

    /** code for unlimited line length */
    protected static final int HIGH_COLUMN = 0x7fffff;

    /** code for current programming language */
    protected int language;
    // enumerations for 'language' in alphabetical order, codes in implementation order
    /** code for C language */
    protected static final int LANG_C       =  5;
    /** code for Cobol language */
    protected static final int LANG_COBOL   = 11;
    /** code for C++ language */
    protected static final int LANG_CPP     =  6;
    /** code for the Cascaded Style Sheet language */
    protected static final int LANG_CSS     =  8;
    /** code for Java language */
    protected static final int LANG_JAVA    =  1;
    /** code for JavaScript language */
    protected static final int LANG_JS      =  4;
    /** code for Pascal language */
    protected static final int LANG_PASCAL  =  7;
    /** code for Perl language */
    protected static final int LANG_PERL    = 12;
    /** code for PL/1 language */
    protected static final int LANG_PL1     =  2;
    /** code for REXX language */
    protected static final int LANG_REXX    =  3;
    /** code for Ruby language */
    protected static final int LANG_RUBY    = 13;
    /** code for SQL language */
    protected static final int LANG_SQL     =  9;
    /** code for unspecified language */
    protected static final int LANG_UNDEF   =  0;
    /** code for VisualBasic language */
    protected static final int LANG_VBA     = 10;

    /** Gets the language.
     *  @return language code
     */
    protected int getLanguage() {
        return language;
    } // getLanguage

    /** Sets the language.
     *  @param language language code
     */
    protected void setLanguage(int language) {
        this.language = language;
    } // setLanguage

    // features of a language which cannot be static final here, but which are fixed per language
    /** Minimum column for source lines, end of prefix */
    protected int minColumn;
    /** Maximum column for source lines, start of postfix or line number field */
    protected int maxColumn;
    /** whether letter case is irrelevant for identifiers, keywords and verbs in this language */
    protected boolean caseIndependant;
    /** whether identifiers, keywords, verbs are forced to uppercase (if <em>caseIndependant</em>) */
    protected boolean forceUpperCase;
    /** whether strings with apostrophes and with quotes are allowed */
    protected boolean bothStringTypes;
    /** whether inner string delimiters must be duplicated */
    protected boolean doubleInnerApos;
    /** whether the language uses Pascal's comment conventions */
    protected boolean pascalComments;
    /** language code denoting the convention for character escapes */
    protected int escapeCode;

    /** No-args Constructor.
     *  This should be as lightweight as possible.
     */
    public ProgLangTransformer() {
        super();
        // the following apply to the generator AND the serializer
        minColumn       = 0;           // first column of source line
        maxColumn       = HIGH_COLUMN; // quasi infinite
        bothStringTypes = false;
        caseIndependant = false;
        doubleInnerApos = false;
        pascalComments  = false;
        lineEndComment  = "//";
        escapeCode      = LANG_JAVA;
        nextContinue    = null;
        prevContinue    = null;
        setLanguage(LANG_UNDEF);
    } // Constructor

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = Logger.getLogger(ProgLangTransformer.class.getName());
        // log.error("ProgLangTransformer.initialize() was called");
    } // initialize

    //////////////////////
    // SAX event generator
    //////////////////////

    /** List of keywords in this language. */
    protected String[] keywords;
    /** List of all reserved or otherwise special words of the language */
    protected HashSet<String> words;

    /** List of keyverbs in this language. Verbs will typically start a statement (for example in COBOL) */
    protected String[] keyverbs;
    /** List of all reserved or otherwise special verbs of the language */
    protected HashSet<String> verbs;

    /** Stores the special words of the language in a hash set.
     */
    protected void storeWords() {
        words = new HashSet<String>(keywords.length);
        for (int ikey = 1; ikey < keywords.length; ikey ++) { // skip over 1st element
            if (caseIndependant) {
                words.add(keywords[ikey].toUpperCase());
            } else {
                words.add(keywords[ikey].replaceAll("_", "_"));
            }
        } // for ikey
    } // storeWords

    /** Tests whether an identifier is a keyword of the language
     *  @param id identifier to be tested
     *  @return whether the identifier is a keyword of the language
     */
    protected boolean testWord(String id) {
        return words.contains(caseIndependant
                ? id.toUpperCase()
                : id);
    } // testWord

    /** Stores the special verbs of the language in a hash set.
     */
    protected void storeVerbs() {
        verbs = new HashSet<String>(keyverbs.length);
        for (int ikey = 1; ikey < keyverbs.length; ikey ++) { // skip over 1st element
            if (caseIndependant) {
                verbs.add(keyverbs[ikey].toUpperCase());
            } else {
                verbs.add(keyverbs[ikey].replaceAll("_", "_"));
            }
        } // for ikey
    } // storeVerbs

    /** Tests whether an identifier is a verb of the language
     *  @param id identifier to be tested
     *  @return whether the identifier is a verb of the language
     */
    protected boolean testVerb(String id) {
        return verbs.contains(caseIndependant
                ? id.toUpperCase()
                : id);
    } // testVerb

    /** gets the left source margin
     *  @return minimum column number (0 based) for source program text
     */
    protected int getMinColumn() {
        return minColumn;
    } // getMinColumn

    /** Sets the left source margin
     *  @param minColumn minimum column number (0 based) for source program text
     */
    protected void setMinColumn(int minColumn) {
        this.minColumn = minColumn;
    } // setMinColumn

    /** Gets the right source margin
     *  @return maximum column number (0 based) for source program text
     */
    protected int getMaxColumn() {
        return maxColumn;
    } // getMaxColumn

    /** Sets the right source margin
     *  @param maxColumn maximum column number (0 based) for source program text
     */
    protected void setMaxColumn(int maxColumn) {
        this.maxColumn = maxColumn;
    } // setMaxColumn

    /** state after an apostrophe */
    protected static final int IN_APOS          =  1;
    /** state after a backslash */
    protected static final int IN_BACKSLASH     =  2;
    /** state during a multi-line comment */
    protected static final int IN_COMMENT       =  3;
    /** state during a multi-line Pascal comment */
    protected static final int IN_PAS_COMMENT   =  4;
    /** state in an identifier */
    protected static final int IN_IDENTIFIER    =  5;
    /** state in a number (sequence of digits) */
    protected static final int IN_NUMBER        =  6;
    /** state in a string */
    protected static final int IN_QUOTE         =  7;
    /** state in a string */
    protected static final int IN_SINGLE_QUOTE  =  8;
    /** common starting state */
    protected static final int IN_TEXT          =  9;
    /** state after first tab */
    protected static final int IN_TABS          = 10;
    /** state after a "&lt;" */
    protected static final int IN_ANGLE         = 11;

    /** number of spaces before an element */
    protected int spaceCount;
    /** number of successive tab stops (for SP_TAG, spaces come before tabs) */
    protected int tabCount;
    /** common number of spaces before a continued comment line */
    protected String commentIndent;
    /** String which indicates that the following line will continue the current line */
    protected String nextContinue;
    /** String which indicates that the current   line will continue the previous line */
    protected String prevContinue;

    /** stack of tokens */
    protected Stack<Token> tokenStack;

    /** Starts a nested element from a {@link Token}
     *  @param token the token for the element's start
     */
    protected void pushToken(Token token) {
        tokenStack.push(token);
    } // pushToken

    /** Ends a nested Token element
     *  @return closing token
     */
    protected Token popToken() {
        Token result = null;
        if (! tokenStack.isEmpty()) {
            result = (Token) tokenStack.pop();
        } else {
            fireComment("stack underflow error");
        }
        return result;
    } // popToken

    /** Determines the token in the stack's top element
     *  @return top token
     */
    protected Token topToken() {
        Token result = null;
        if (! tokenStack.isEmpty()) {
            result = (Token) tokenStack.peek();
        }
        return result;
    } // topToken

    /** Starts a nested XML element with attributes,
     *  and remembers the first attribute value also on the stack
     *  @param tag the element's tag
     *  @param attrList array of (name,value) pairs for attributes
     */
    protected void pushXML2(String tag, String [] attrList) {
        tagStack.push(tag + "," + attrList[1]);
        fireStartElement(tag, toAttributes(attrList));
    } // pushXML2

    /** Ends a nested XML element,
     *  and cares for stack elements which contain an additional attribute value
     */
    protected void popXML2() {
        if (! tagStack.isEmpty()) {
            String[] top = ((String) tagStack.pop()).split(",");
            fireEndElement(top[0]);
        } else {
            fireComment("stack underflow error");
        }
    } // popXML2

    /** Determines the tag in the stack's top element,
     *  including an additional attribute value
     *  @return top token
     */
    protected String topXML2() {
        String result = "";
        if (! tagStack.isEmpty()) {
            result = (String) tagStack.peek();
        }
        return result;
    } // topXML2

    /** Matches all elements starting with "\'": character denotations, maybe escaped.
     *  @param line input line containing the character
     *  @param linePos position where the character denotation starts
     *  @param trap position behind the end of the line
     *  @return position behind the element, - 1
     */
    protected int matchCharacter(String line, int linePos, int trap) {
        int pos2 = linePos + 1; // position behind the element just recognized
        char ch = ' ';
        Matcher matcher = characterPattern.matcher(line.substring(linePos));
        if (matcher.lookingAt()) {
            pos2 = linePos + matcher.end(); // because substring was matched
            String chars = line.substring(linePos, pos2);
            if (false) {
            } else if (chars.startsWith("\'\\u")) {
                chars = "&#x" + chars.substring(3, 7) + ";";
            } else if (chars.startsWith("\'\\x")) {
                chars = "&#x" + chars.substring(3, 5) + ";";
            } else if (chars.startsWith("\'\\"))  {
                if (chars.length() == 3) { // single backslash
                    chars = chars.substring(1, 2);
                } else {
                    ch = chars.charAt(2);
                    switch (ch) {
                        case 'b' : chars = "\\b";               break;
                        case 'f' : chars = "\\f";               break;
                        case 'n' : chars = "\\n";               break;
                        case 'r' : chars = "\\r";               break;
                        case 't' : chars = "\\t";               break;
                        default  : chars = chars.substring(2, 3); break;
                    } // switch ch
                    ch = chars.charAt(0);
                }
            } else { // unescaped
                chars = chars.substring(1, 2);
            }
            fireEmptyElement(CHAR_TAG, spaceAndValAttr(chars));
        } else {
            log.error("ProgLangTransformer: invalid character denotation in line " + (lineNo + 1)
                    + " at column " + (linePos + 1) + ": " + line.substring(linePos));
        }
        return pos2 - 1; // 1 before the end
    } // matchCharacter

    /** Matches identifiers and keywords starting with a letter.
     *  @param line input line containing the letter
     *  @param linePos position where the letter was found
     *  @param trap position behind the end of the line
     *  @return position behind the element, - 1
     */
    protected int matchIdentifier(String line, int linePos, int trap) {
        int pos2 = linePos + 1; // position behind the element just recognized
        Matcher matcher = identifierPattern.matcher(line.substring(linePos));
        if (matcher.lookingAt()) {
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
        } else {
            log.error("unmatched identifier in line " + (lineNo + 1)
                    + " at column " + (linePos + 1) + ": " + line.substring(linePos));
        }
        return pos2 - 1; // 1 before the end
    } // matchIdentifier

    /** Matches numbers starting with a digit.
     *  @param line input line containing the digit
     *  @param linePos position where the digit was found
     *  @param trap position behind the end of the line
     *  @return position behind the element, - 1
     */
    protected int matchNumber(String line, int linePos, int trap) {
        int pos2 = linePos + 1; // position behind the element just recognized
        Matcher matcher = numberPattern.matcher(line.substring(linePos));
        if (matcher.lookingAt()) {
            pos2 = linePos + matcher.end();
            String number = line.substring(linePos, pos2);
            fireEmptyElement(NUMBER_TAG, spaceAndValAttr(number));
        } else {
            log.error("unmatched number in line " + (lineNo + 1)
                    + " at column " + (linePos + 1) + ": " + line.substring(linePos));
        }
        return pos2 - 1; // 1 before the end
    } // matchNumber

    /** Matches all punctuation and operator elements apart from parentheses, ";", "," and ".".
     *  @param line input line containing the start of the operator at position <em>linePos</em>
     *  @param linePos position where the operator starts
     *  @param trap position behind the end of the line
     *  @return position behind the element, - 1
     */
    protected int matchOperator(String line, int linePos, int trap) {
        int pos2 = linePos + 1; // position behind the element just recognized
        Matcher matcher = operatorPattern.matcher(line.substring(linePos));
        if (matcher.lookingAt()) {
            pos2 = linePos + matcher.end();
            fireEmptyElement(OPERATOR_TAG, spaceAndValAttr(line.substring(linePos, pos2)));
        } else {
            log.error("unknown operator in line " + (lineNo + 1)
                    + " at column " + (linePos + 1) + ": " + line.substring(linePos));
        }
        return pos2 - 1; // 1 before the end
    } // matchOperator

    /** Matches all elements starting with "/": comments or "/=".
     *  @param line input line containing "/" at position <em>linePos</em>
     *  @param linePos position where "/" was found
     *  @param trap position behind the end of the line
     *  @return position behind the matched element, - 1
     */
    protected int matchSlash(String line, int linePos, int trap) {
        int pos1 = linePos + 1; // the position where this element's content starts
        int pos2 = pos1; // position behind the element just recognized
        if (false) {
        } else if (line.startsWith(DOCUMENT_START, linePos)
                    && line.length() > linePos + DOCUMENT_START.length()
                    && ! line.startsWith(COMMENT_END, linePos + DOCUMENT_START.length() - 1)
                    ) { // document comment and not /**/
            pos1 = linePos + DOCUMENT_START.length();
            pos2 = line.indexOf(DOCUMENT_END, pos2);
            if (pos2 >= pos1) { // closing comment on same line
                fireStartElement(COMMENT_TAG, spaceAndModeAttr(DOCUMENT_MODE));
                fireCharacters(replaceInSource(line.substring(pos1, pos2)));
                pos2 += DOCUMENT_END.length(); // on "/" of "*/"
                fireEndElement(COMMENT_TAG);
            } else { // continued on following line(s)
                state = IN_COMMENT;
                commentIndent = SPACES.substring(0, linePos);
                pushXML2(COMMENT_TAG, new String[]
                        { MODE_ATTR, DOCUMENT_MODE
                        , SP_ATTR  , String.valueOf(spaceCount)
                        });
                fireCharacters(replaceInSource(line.substring(pos1)));
                pos2 = trap;
            }
        } else if (line.startsWith(COMMENT_START,  linePos)) { // normal comment
            pos1 = linePos + COMMENT_START.length();
            pos2 = line.indexOf(COMMENT_END, pos2);
            if (pos2 >= pos1) { // closing comment on same line
                fireStartElement(COMMENT_TAG, spaceAttribute());
                fireCharacters(replaceInSource(line.substring(pos1, pos2)));
                pos2 += COMMENT_END.length(); // on "/" of "*/"
                fireEndElement(COMMENT_TAG);
            } else { // continued on following line(s)
                state = IN_COMMENT;
                commentIndent = SPACES.substring(0, linePos);
                pushXML2(COMMENT_TAG, new String[]
                        { MODE_ATTR, COMMENT_MODE
                        , SP_ATTR  , String.valueOf(spaceCount)
                        });
                fireCharacters(replaceInSource(line.substring(pos1)));
                pos2 = trap;
            }
        } else if (line.startsWith(lineEndComment,  linePos)) { // line end comment
            pos1 = linePos + lineEndComment.length();
            fireStartElement(COMMENT_TAG, spaceAndModeAttr(LINE_END_MODE));
            fireCharacters(replaceInSource(line.substring(pos1)));
            fireEndElement(COMMENT_TAG);
            pos2 = trap; // skip to end of line
        } else if (line.startsWith("/=",  linePos)) { // divide by
            fireEmptyElement(OPERATOR_TAG, spaceAndValAttr("/="));
            pos2 = linePos + 2;
        } else { // single "/" = ordinary division
            fireEmptyElement(OPERATOR_TAG, spaceAndValAttr("/" ));
            pos2 = linePos + 1;
        }
        return pos2 - 1; // 1 before the end
    } // matchSlash

    /** Matches all elements starting with "(" - eventually a Pascal comment "(*".
     *  @param line input line containing "(" at position <em>linePos</em>
     *  @param linePos position where "(" was found
     *  @param trap position behind the end of the line
     *  @return position behind the matched element, - 1
     */
    protected int matchParenthesis(String line, int linePos, int trap) {
        int pos1 = linePos + 1; // the position where this element's content starts
        int pos2 = pos1; // position behind the element just recognized
        if (false) {
        } else if (line.startsWith(PAS_COMMENT2_START, linePos)
                    && line.length() > linePos + PAS_COMMENT2_START.length()
                    ) { // document comment and not /**/
            pos1 = linePos + PAS_COMMENT2_START.length();
            pos2 = line.indexOf(PAS_COMMENT2_END, pos2);
            if (pos2 >= pos1) { // closing comment on same line
                fireStartElement(COMMENT_TAG, spaceAndModeAttr(DOCUMENT_MODE));
                fireCharacters(replaceInSource(line.substring(pos1, pos2)));
                pos2 += PAS_COMMENT2_END.length(); // on "/" of "*/"
                fireEndElement(COMMENT_TAG);
            } else { // continued on following line(s)
                state = IN_PAS_COMMENT;
                commentIndent = SPACES.substring(0, linePos);
                pushXML2(COMMENT_TAG, new String[]
                        { MODE_ATTR, DOCUMENT_MODE
                        , SP_ATTR  , String.valueOf(spaceCount)
                        });
                fireCharacters(replaceInSource(line.substring(pos1)));
                pos2 = trap;
            }
        } else if (line.startsWith(PAS_COMMENT_START,  linePos)) { // normal comment
            pos1 = linePos + PAS_COMMENT_START.length();
            pos2 = line.indexOf(PAS_COMMENT_END, pos2);
            if (pos2 >= pos1) { // closing comment on same line
                fireStartElement(COMMENT_TAG, spaceAttribute());
                fireCharacters(replaceInSource(line.substring(pos1, pos2)));
                pos2 += PAS_COMMENT_END.length(); // on "/" of "*/"
                fireEndElement(COMMENT_TAG);
            } else { // continued on following line(s)
                state = IN_PAS_COMMENT;
                commentIndent = SPACES.substring(0, linePos);
                pushXML2(COMMENT_TAG, new String[]
                        { MODE_ATTR, COMMENT_MODE
                        , SP_ATTR  , String.valueOf(spaceCount)
                        });
                fireCharacters(replaceInSource(line.substring(pos1)));
                pos2 = trap;
            }
        } else { // single "(" = ordinary open parenthesis
            // fireStartElement(ROUND_TAG , spaceAttribute());
            fireEmptyElement(OPERATOR_TAG, spaceAndValAttr(line.substring(linePos, linePos + 1)));
            pos2 = linePos + 1;
        }
        return pos2 - 1; // 1 before the end
    } // matchParenthesis

    /** Inserts a number of spaces before a closing element.
     */
    private void optionalSpaces() {
        if (spaceCount > 0) {
            fireEmptyElement(WHITESPACE_TAG, spaceAttribute());
            spaceCount = 0;
        }
    } // optionalSpaces

    /** Constructs an attribute for a number of spaces to be inserted before the element.
     *  @return new attribute
     */
    protected Attributes spaceAttribute() {
        AttributesImpl attrs = new AttributesImpl();
        if (spaceCount > 0) {
            attrs.addAttribute("", SP_ATTR, SP_ATTR, "CDATA", Integer.toString(spaceCount));
            spaceCount = 0;
        }
        return attrs;
    } // spaceAttribute

    /** Constructs an attribute for a number of spaces to be inserted before the element,
     *  and a 2nd value attribute before the element.
     *  @param value value of the "v" attribute
     *  @return new attribute
     */
    protected Attributes spaceAndValAttr(String value) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", VAL_ATTR, VAL_ATTR   , "CDATA", replaceInSource(value)     );
        if (spaceCount > 0) {
            attrs.addAttribute("", SP_ATTR, SP_ATTR, "CDATA", Integer.toString(spaceCount));
            spaceCount = 0;
        }
        return attrs;
    } // spaceAndValAttr

    /** Constructs an attribute for a number of spaces to be inserted before the element,
     *  and a 2nd mode attribute before the element.
     *  @param value value of the "t" attribute (a short string constant like "bt", "ap", "qu")
     *  @return new attribute
     */
    protected Attributes spaceAndModeAttr(String value) {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", MODE_ATTR, MODE_ATTR   , "CDATA", value);
        if (spaceCount > 0) {
            attrs.addAttribute("", SP_ATTR, SP_ATTR, "CDATA", Integer.toString(spaceCount));
            spaceCount = 0;
        }
        return attrs;
    } // spaceAndModeAttr

    /** Pattern for escaped characters */
    protected Pattern characterPattern;
    /** Pattern for identifiers and keywords */
    protected Pattern identifierPattern;
    /** Pattern for numbers */
    protected Pattern numberPattern;
    /** Pattern for all operators, single- or multi-character */
    protected Pattern operatorPattern;

    /** Prepares the generator.
     *  Does all heavy-weight initialization,
     *  especially the call of {@link #storeWords}.
     */
    protected void prepareGenerator() {
        language = LANG_UNDEF; // unspecified so far
        putEntityReplacements();
        characterPattern   = Pattern.compile
                ("\\\'([^\\\\]|\\\\(u[a-fA-F0-9]{4}|x[a-fA-F0-9]{2}|n|r|b|t|f|[^A-Za-z0-9]))\\\'");
                //     unesc.       \u1234          \x12           \n ...     \. etc.
        identifierPattern  = Pattern.compile
                ("[\\w\\_]+");
        numberPattern      = Pattern.compile
                ("0x[0-9a-fA-F]+|\\d+"); // maybe 0x09af
        operatorPattern    = Pattern.compile
                ("\\,|\\.|[\\+\\-\\*\\>\\<\\&\\|\\^\\%\\$\\=\\:\\?\\!\\~\\#\\@\\\\]+");
                // no slash! because it may start a comment
        lineNo  = 0;
        readOff = true;
        state   = IN_TEXT;
        keywords        = new String[] { "()" };
        keyverbs        = new String[] { "()" };
        storeWords();
        storeVerbs();
        forceUpperCase  = ! getOption("uc", "false").startsWith("f");
    } // prepareGenerator

    /** whether to consume the current character in the finite automaton */
    private boolean readOff;
    /** state of finite automaton */
    protected int state;
    /** previous state of finite automaton */
    protected int prevState;
    /** reader for source lines and nested include files */
    protected NestedLineReader reader;
    /** current source line read from input file */
    protected String line;
    /** current position in source line */
    protected int linePos;
    /** position behind the last character in the source line */
    protected int trap;
    /** buffer for short portions of generated XML */
    protected StringBuffer buffer;

    /** Processes one source line.
     *  All characters in the "real" source line are consumed by finite state automaton.
     *  Column oriented prefixes and postfixes were previously handled by {@link #processColumns}.
     *  Uses global <em>state</em>.
     */
    protected void processLine() {
        while (linePos < trap) { // process all characters in the line
            readOff = true;
            char ch = line.charAt(linePos);
            switch (state) {

                case IN_TEXT:
                    switch (ch) {
                        case ' ':
                            spaceCount ++;
                            break;
                        case ';':
                            // fireEmptyElement(SEMI_TAG  , spaceAttribute());
                            fireEmptyElement(OPERATOR_TAG, spaceAndValAttr(line.substring(linePos, linePos + 1)));
                            break;
                        case '{':
                            if (pascalComments) {
                                linePos = matchParenthesis(line, linePos, trap);
                            } else {
                                // fireStartElement(CURLY_TAG , spaceAttribute());
                                fireEmptyElement(OPERATOR_TAG, spaceAndValAttr(line.substring(linePos, linePos + 1)));
                            }
                            break;
                        case '}':
                            optionalSpaces();
                            // fireEndElement  (CURLY_TAG);
                            fireEmptyElement(OPERATOR_TAG, spaceAndValAttr(line.substring(linePos, linePos + 1)));
                            break;
                        case '(':
                            if (pascalComments) {
                                linePos = matchParenthesis(line, linePos, trap);
                            } else {
                                // fireStartElement(ROUND_TAG , spaceAttribute());
                                fireEmptyElement(OPERATOR_TAG, spaceAndValAttr(line.substring(linePos, linePos + 1)));
                            }
                            break;
                        case ')':
                            optionalSpaces();
                            // fireEndElement  (ROUND_TAG);
                            fireEmptyElement(OPERATOR_TAG, spaceAndValAttr(line.substring(linePos, linePos + 1)));
                            break;
                        case '[':
                            // fireStartElement(SQUARE_TAG, spaceAttribute());
                            fireEmptyElement(OPERATOR_TAG, spaceAndValAttr(line.substring(linePos, linePos + 1)));
                            break;
                        case ']':
                            optionalSpaces();
                            // fireEndElement  (SQUARE_TAG);
                            fireEmptyElement(OPERATOR_TAG, spaceAndValAttr(line.substring(linePos, linePos + 1)));
                            break;
                        case '/': // comment or "/="
                            linePos = matchSlash(line, linePos, trap);
                            break; // slash
                        case '#': // maybe line end comment in Perl, Ruby etc.
                            if (lineEndComment.startsWith("#")) {
                                linePos = matchSlash   (line, linePos, trap);
                            } else {
                                linePos = matchOperator(line, linePos, trap);
                            }
                            break; // #
                        case '\\':
                            switch (escapeCode) {
                                case LANG_REXX:
                                    linePos = matchOperator  (line, linePos, trap);
                                    break;
                                case LANG_PL1:
                                case LANG_VBA:
                                    buffer.append(ch);
                                    break;
                                default:
                                    prevState = state;
                                    state = IN_BACKSLASH;
                                    break;
                            } // switch escapeCode
                            break; // \\
                    /* not yet
                        case '<': // type or "<=", "<<"
                            linePos = matchLessThan(line, linePos, trap);
                            break; // lt
                    */
                        case '\t':
                            // expand by "-tab" option ???
                            tabCount = 1;
                            state = IN_TABS;
                            break;
                        case '\'':
                            switch (escapeCode) {
                                case LANG_JAVA:
                                case LANG_VBA:
                                    linePos = matchCharacter(line, linePos, trap );
                                    break;
                                case LANG_PERL:
                                case LANG_RUBY:
                                    state = IN_SINGLE_QUOTE;
                                    fireStartElement(STRING_TAG, spaceAndModeAttr(ASTRING_MODE));
                                    buffer.setLength(0);
                                    break;
                                case LANG_PL1:
                                case LANG_REXX:
                                case LANG_COBOL:
                                default:
                                    state = IN_APOS;
                                    fireStartElement(STRING_TAG, spaceAndModeAttr(ASTRING_MODE));
                                    buffer.setLength(0);
                                    break;
                            } // switch escapeCode
                        /*
                        */
                        /*
                            if (bothStringTypes) {
                                state = IN_APOS;
                                fireStartElement(ASTRING_TAG, spaceAttribute());
                                buffer.setLength(0);
                            } else {
                                linePos = matchCharacter(line, linePos, trap );
                            }
                        */
                            break;
                        case '\"':
                            state = IN_QUOTE;
                            fireStartElement(STRING_TAG, spaceAndModeAttr(DSTRING_MODE));
                            buffer.setLength(0);
                            break;
                        default:
                            if (false) {
                            } else if (Character.isLetter(ch)) {
                                linePos = matchIdentifier(line, linePos, trap);
                            } else if (Character.isDigit(ch)) {
                                linePos = matchNumber    (line, linePos, trap);
                            } else { // punctuation, operator
                                linePos = matchOperator  (line, linePos, trap);
                            }
                            break; // default
                    } // switch ch
                    break; // IN_TEXT

                case IN_COMMENT:
                    { // block for 'pos2'
                        if (line.startsWith(commentIndent, linePos)) {
                            linePos += commentIndent.length();
                        }
                        int pos2 = line.indexOf((topXML2().equals(COMMENT_TAG + "," + DOCUMENT_MODE)
                                ? DOCUMENT_END : COMMENT_END)
                                , linePos);
                        if (pos2 >= linePos) { // end of comment found
                            state = IN_TEXT;
                            fireCharacters(replaceInSource(line.substring(linePos, pos2)));
                            linePos = pos2 + 2;
                            popXML2(); // DOCUMENT_TAG or COMMENT_TAG
                        } else { // comment still continues
                            fireCharacters(replaceInSource(line.substring(linePos)));
                            linePos = trap;
                        }
                    } // block
                    break; // IN_COMMENT

                case IN_PAS_COMMENT:
                    { // block for 'pos2'
                        if (line.startsWith(commentIndent, linePos)) {
                            linePos += commentIndent.length();
                        }
                        int pos2 = line.indexOf((topXML2().equals(COMMENT_TAG + "," + DOCUMENT_MODE)
                                ? PAS_COMMENT2_END : PAS_COMMENT_END)
                                , linePos);
                        if (pos2 >= linePos) { // end of comment found
                            state = IN_TEXT;
                            fireCharacters(replaceInSource(line.substring(linePos, pos2)));
                            linePos = pos2 + 2;
                            popXML2(); // DOCUMENT_TAG or COMMENT_TAG
                        } else { // comment still continues
                            fireCharacters(replaceInSource(line.substring(linePos)));
                            linePos = trap;
                        }
                    } // block
                    break; // IN_PAS_COMMENT

                case IN_APOS:
                    switch (ch) {
                        case '\'': // 2nd apostrophe
                            if (line.startsWith("\'\'", linePos) && doubleInnerApos) {
                                // doubled inner apostrophe
                                linePos ++; // skip over 1st
                                buffer.append(ch); // append 2nd apostrophe
                            } else {
                                state = IN_TEXT;
                                fireCharacters(/* replaceInSource */(buffer.toString()));
                                fireEndElement(STRING_TAG);
                            }
                            break;
                    /*
                        case '\\':
                            prevState = state;
                            state = IN_BACKSLASH;
                            break;
                    */
                        default:
                            buffer.append(ch);
                            break;
                    } // switch ch
                    break; // IN_APOS

                case IN_QUOTE:
                    switch (ch) {
                        case '\"': // 2nd quote
                            state = IN_TEXT;
                            fireCharacters(replaceInSource(buffer.toString()));
                            fireEndElement(STRING_TAG);
                            break;
                        case '\\':
                            switch (escapeCode) {
                                case LANG_REXX:
                                case LANG_PL1:
                                case LANG_VBA:
                                    buffer.append(ch);
                                    break;
                                default:
                                    prevState = state;
                                    state = IN_BACKSLASH;
                                    break;
                            } // switch escapeCode
                            break;
                        default:
                            buffer.append(ch);
                            break;
                    } // switch ch
                    break; // IN_QUOTE

                case IN_SINGLE_QUOTE:
                    switch (ch) {
                        case '\'': // 2nd apostrophe
                            state = IN_TEXT;
                            fireCharacters(replaceInSource(buffer.toString()));
                            fireEndElement(STRING_TAG);
                            break;
                        case '\\':
                            switch (escapeCode) {
                                case LANG_REXX:
                                case LANG_PL1:
                                case LANG_VBA:
                                    buffer.append(ch);
                                    break;
                                default:
                                    prevState = state;
                                    state = IN_BACKSLASH;
                                    break;
                            } // switch escapeCode
                            break;
                        default:
                            buffer.append(ch);
                            break;
                    } // switch ch
                    break; // IN_SINGLE_QUOTE

                case IN_TABS:
                    switch (ch) {
                        case '\t': // next tab
                            tabCount ++;
                            break;
                        default:
                            state = IN_TEXT;
                            readOff = false;
                            fireEmptyElement(WHITESPACE_TAG, toAttributes(new String[]
                                    { MODE_ATTR,  TAB_MODE
                                    , VAL_ATTR,   String.valueOf(tabCount)
                                    , (spaceCount > 0 ? SP_ATTR : ""),    String.valueOf(spaceCount)
                                    }));
                            spaceCount = 0;
                            tabCount   = 0;
                            break;
                    } // switch ch
                    break; // IN_TABS

                case IN_BACKSLASH:
                    state = prevState;
                    if (buffer.length() > 0) { // flush the buffer before any entity
                        fireCharacters(replaceInSource(buffer.toString()));
                        buffer.setLength(0);
                    } // flush
                    switch (ch) {
                        case '\\':
                            buffer.append("\\\\");
                            break;
                        case '\"':
                            fireEntity("quot");
                            break;
                        case '\'':
                            fireEntity("apos");
                            break;
                        case 'b':
                            fireEntity("#x8");
                            break;
                    /*
                        case 'f':
                            fireEntity("#xc");
                            break;
                    */
                        case 'n':
                            fireEntity("#xa");
                            break;
                        case 'r':
                            fireEntity("#xd");
                            break;
                        case 't':
                            fireEntity("#x9");
                            break;
                        case 'u':
                            fireEntity("#x" + line.substring(linePos + 1, linePos + 5));
                            linePos += 4;
                            break;
                        case 'x': // for C, C++
                            fireEntity("#x" + line.substring(linePos + 1, linePos + 3));
                            linePos += 2;
                            break;
                        default:
                            buffer.append("\\");
                            buffer.append(ch);
                            break;
                    } // switch ch
                    break; // IN_BACKSLASH

                default:
                    log.error("invalid state " + state);
                    break;
            } // switch state */
            if (readOff) { // really consume the current character
                linePos ++;
            } // else look again at current character
        } // while processing all characters in the line
        if (state == IN_APOS) { // broken string in COBOL - close it
            state = IN_TEXT;
            fireCharacters(replaceInSource(buffer.toString()));
            fireEndElement(STRING_TAG);
        }
    } // processLine

    /** Processes the column oriented features of the source line
     */
    protected void processColumns() {
        // maybe overriden by column oriented languages
    } // processColumns

    /** Transforms from the specified format to XML by reading lines from
     *  the input file and feeding its characters into a finite automaton.
     *  Protected properties set by the <em>matchXXX</em> methods:
     *  <ul>
     *  <li>lineNo</li>
     *  <li>readOff</li>
     *  <li>state</li>
     *  </ul>
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        // log.info("ProgLangTransformer.generate: forceUpperCase = " + forceUpperCase + ", caseIndependant = " + caseIndependant);
        boolean result  = true;
        buffer = new StringBuffer(64); // current XML element built during scanning process
        try {
            prepareGenerator(); // language specific initialization
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            // fireEmptyElement("parm", toAttribute("lang", getFirstFormatCode()));
            fireLineBreak();
            state     = IN_TEXT; // state of finite automaton
            prevState = state;
            readOff   = true;  // whether current character should be consumed
            lineNo    = 0; // number of current line
            linePos   = 0; // position of the character to be read
            reader = new NestedLineReader();
            reader.open(new BufferedReader(charReader));

            while ((line = reader.readLine()) != null) { // while reading lines
                linePos     = 0;
                // column   = 0;
                spaceCount  = 0;
                tabCount    = 0;
                // each line starts with a NEWLINE element
                // which may have attributes for a prefix and postfix area (e.g. columns 1-6 and 73-80)
                // AttributesImpl attrs = new AttributesImpl();
                // attrs.addAttribute("", LINE_NO_ATTR, LINE_NO_ATTR , "CDATA", Integer.toString(lineNo));
                fireEmptyElement(WHITESPACE_TAG, toAttributes(new String[]
                        { MODE_ATTR, NEWLINE_MODE
                        , VAL_ATTR , String.valueOf(lineNo)
                        }));
                // fireEmptyElement(NEWLINE_TAG, attrs);

                if (minColumn > 0 && line.length() > minColumn) {
                    fireEmptyElement(WHITESPACE_TAG, toAttributes(new String[]
                            { MODE_ATTR, PREFIX_MODE
                            , VAL_ATTR , line.substring(0, minColumn)
                            }));
                    // attrs.addAttribute("", PRE_ATTR, PRE_ATTR , "CDATA", line.substring(0, minColumn));
                    linePos = minColumn; // start scanning behind the prefix
                }
                if (line.length() > maxColumn) {
                    fireEmptyElement(WHITESPACE_TAG, toAttributes(new String[]
                            { MODE_ATTR, POSTFIX_MODE
                            , VAL_ATTR , line.substring(maxColumn)
                            }));
                    // attrs.addAttribute("", POST_ATTR, POST_ATTR, "CDATA", line.substring(maxColumn));
                    line = line.substring(0, maxColumn); // remove the postfix
                }
                trap = line.length(); // position behind the end of the line
                processColumns(); // empty for Java, overridden by column oriented languages
                processLine();    // real source text

                fireLineBreak();
                lineNo ++;
            } // while not EOF
            reader.close();
            fireEndElement(ROOT_TAG);
            fireLineBreak();
            fireEndDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
            result = false;
        }
        return  result;
    } // generate

    /////////////////////////
    // SAX content handler //
    /////////////////////////

    /** buffer for portions of the output line */
    protected StringBuffer saxBuffer;
    /** buffer for the output line */
    protected StringBuffer saxLine;
    /** prefix  at the beginning of <em>saxLine</em> */
    protected String saxPrefix;
    /** postfix at the end of <em>saxLine</em> */
    protected String saxPostfix;
    /** current position in output line */
    protected int saxColumn;
    /** spaces before a continued comment line */
    protected String saxCommentIndent;
    /** Upper bound for character buffer */
    protected static final int MAX_BUF = 4096;
    /** whether in a string */
    protected boolean saxInString;
    /** whether in a comment */
    protected boolean saxInComment;
    /** character which encloses PL/1 string constants */
    protected char saxApos;
    /** character which encloses C string constants */
    protected char saxQuote;
    /** value of the mode attribute of some elements - which cannot be nested */
    protected String modeAttr;
    /** value of the mode attribute of comments - which cannot be nested */
    protected String commentModeAttr;

    /** Writes the contents of the buffer and purges it.
     *  @param replace replacement mask,
     *  bit 2**0: whether to replace entities in result stream,
     *  bit 2**1: whether to escape quotes and apostrophes
     */
    protected void flushBuffer(int replace) {
        // System.out.println(saxBuffer.toString());
        switch (replace) {
            case 0:
            case 2:
            case 1:
            case 3:
                saxLine.append(replaceInResult(saxBuffer.toString()));
                break;
        } // switch replace
        saxBuffer.setLength(0);
    } // flushBuffer

    /** Replaces all non-printable/non-ASCII characters by their
     *  C-language escape (with backslash, unicode if &gt;= 0x80)
     *  @param source string in which characters are replaced
     *  @return traget string with backslash escapes
     */
    public String escapeInResult(String source) {
        StringBuffer result = new StringBuffer(296);
        int pos = 0;
        while (pos < source.length()) {
            char ch = source.charAt(pos ++);
            switch (ch) {
                case '\'':
                    if (saxInString) {
                        if (doubleInnerApos) {
                            result.append("\'\'");
                        } else {
                            if (bothStringTypes) {
                                result.append(ch);
                            } else {
                                result.append("\\\'");
                            }
                        }
                    } else {
                        result.append(ch);
                    }
                    break;
                case '\"':
                    if (saxInString) {
                        if (doubleInnerApos) {
                            result.append("\"\"");
                        } else {
                            if (bothStringTypes) {
                                result.append(ch);
                            } else {
                                result.append("\\\"");
                            }
                        }
                    } else {
                        result.append(ch);
                    }
                    break;
                default:
                    if (ch < 0x20) {
                        switch (ch) { // control character
                            case '\b':
                                result.append("\\b");
                                break;
                            case '\f':
                                result.append("\\f");
                                break;
                            case '\n':
                                result.append("\\n");
                                break;
                            case '\r':
                                result.append("\\r");
                                break;
                            case '\t':
                                if (saxInString) {
                                    result.append("\\t");
                                } else {
                                    result.append(ch);
                                }
                                break;
                           default:
                                break;
                        } // switch control character
                    } else if (ch <  0x80) { // normal ASCII
                        result.append(ch);
                    } else if (ch < 0x100) { // LATIN-1
                        switch (escapeCode) {
                            case LANG_JAVA:
                                result.append("\\u" + (Integer.toHexString(ch + 0x10000)).substring(1));
                                break;
                            default:
                            case LANG_REXX:
                            case LANG_C:
                            case LANG_PL1:
                                result.append(ch);
                                break;
                        } // switch escapeCode
                    } else { // >= 256, Unicode in any case
                        result.append("\\u" + (Integer.toHexString(ch + 0x10000)).substring(1));
                    }
                    break;
            } // switch ch
        } // while pos
        return result.toString();
    } // escapeInResult

    /** Writes the contents of the output line and purges it with the pre- and postfixes.
     *  Fills with spaces if <em>maxColumn</em> is set.
     */
    protected void flushLine() {
        int spaceFill = maxColumn;
        if (saxPrefix != null) {
            saxLine.insert(0, saxPrefix);
        }
        if (saxPostfix != null) { // implies maxColumn < HIGH_COLUMN
            if (saxLine.length() > maxColumn) {
                saxLine.setLength(maxColumn);
            } else {
                saxLine.append(spaces(spaceFill - saxLine.length()));
            }
            saxLine.append(saxPostfix);
        }
        charWriter.println(saxLine.toString());
        saxPrefix  = null;
        saxPostfix = null;
        saxLine.setLength(0);
    } // flushLine

    /** Get the current length of the line to be written to the target.
     *  @return length of line
     */
    public int getSaxLength() {
        return saxLine.length();
    } // getSaxLength

    /** Prefix the tag with a number of spaces taken from the "sp" attribute
     *  @param attrs the attributes attached to the element.
     */
    protected void prefixSpaces(Attributes attrs) {
        String spValue = attrs.getValue(SP_ATTR);
        if (spValue != null) { // with space count
            int spc = 0;
            try {
                spc = Integer.parseInt(spValue);
            } catch (Exception exc) {
            }
            saxBuffer.append(spaces(spc));
        } // with space count
    } // prefixSpaces

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        try {
            super.startDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        putEntityReplacements();
        saxApos     = '\''; // for PL/1, REXX ...
        saxQuote    = '\"'; // for Java, C ...
        saxBuffer   = new StringBuffer(MAX_BUF); // a rather long portion
        saxLine     = new StringBuffer(MAX_BUF); // a rather long line
        saxPrefix   = null;
        saxPostfix  = null;
        saxColumn   = 0;
        saxInString = false;
        saxInComment= false;
        forceUpperCase  = ! getOption("uc", "false").startsWith("f");
    } // startDocument

    /** Receive notification of the end of the document.
     */
    public void endDocument() {
        flushBuffer(0);
        flushLine();
        charWriter.close();
    } // endDocument

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
                saxBuffer.append      (COMMENT_START );
            } else if (commentModeAttr.equals(DOCUMENT_MODE)) {
                saxBuffer.append      (DOCUMENT_START);
            } else if (commentModeAttr.equals(LINE_END_MODE)) {
                saxBuffer.append      (lineEndComment);
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
                saxBuffer.append      (COMMENT_END   );
            } else if (commentModeAttr.equals(DOCUMENT_MODE)) {
                saxBuffer.append      (DOCUMENT_END  );
            } else if (commentModeAttr.equals(LINE_END_MODE)) {
                // output nothing
            }
        }
    } // writeCommentEnd

    /** Writes a label element.
     *  This implementation appends the label without any column orientation.
     *  @param qName the qualified name (with prefix),
     *  or the empty string if qualified names are not available.
     *  @param attrs the attributes attached to the element.
     *  If there are no attributes, it shall be an empty Attributes object.
     */
    protected void writeLabel(String qName, Attributes attrs) {
        saxBuffer.append(attrs.getValue(VAL_ATTR));
    } // writeLabel

    /** Receive notification of the start of an element.
     *  Looks for the element which contains raw lines.
     *  @param uri The Namespace URI, or the empty string if the element has no Namespace URI
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix),
     *  or the empty string if namespace processing is not being performed.
     *  @param qName the qualified name (with prefix),
     *  or the empty string if qualified names are not available.
     *  @param attrs the attributes attached to the element.
     *  If there are no attributes, it shall be an empty Attributes object.
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs) {
        String value = null; // of an attribute
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        prefixSpaces(attrs); // print the leading spaces before the element
        if (false) {
        } else if (qName.equals(BACKSLASH_NL_TAG)) {
            saxBuffer.append('\\');
            flushBuffer(0);
            String lno = attrs.getValue(LINE_NO_ATTR);
            if (lno != null && ! lno.equals("0")) {
                flushLine();
            }
            saxPrefix  = attrs.getValue(PRE_ATTR);
            saxPostfix = attrs.getValue(POST_ATTR);
            if (saxInComment) {
                saxBuffer.append(saxCommentIndent);
            }
        } else if (qName.equals(CHAR_TAG)) {
            String chars = attrs.getValue(VAL_ATTR);
            if (chars != null) {
                saxBuffer.append('\'');
                chars = replaceInResult(chars);
                if (false) {
                } else if (chars.equals("\'")) {
                    chars = "\\\'";
                } else if (chars.equals("\"")) {
                    chars = "\\\"";
                } else if (chars.equals("\\")) {
                    chars = "\\\\";
                } else if (chars.equals("&#x8;")) {
                    chars = "\\b";
                } else if (chars.equals("&#x9;")) {
                    chars = "\\t";
                } else if (chars.equals("&#xa;")) {
                    chars = "\\n";
                } else if (chars.equals("&#12;") || chars.equals("&#xc;")) {
                    chars = "\\f";
                } else if (chars.equals("&#xd;")) {
                    chars = "\\r";
                } else if (chars.startsWith("&#x")) {
                    if (chars.length() > 6) {
                        chars = "\\u" + chars.substring(3, chars.length() - 1);
                    } else {
                        chars = "\\x" + chars.substring(3, chars.length() - 1);
                    }
                }
                saxBuffer.append(chars);
                saxBuffer.append('\'');
            }
        } else if (qName.equals(COMMENT_TAG )) {
            flushBuffer(0);
            saxInComment = true;
            saxCommentIndent = spaces(saxLine.length()); // including the prefixSpaces
            writeCommentStart(qName, attrs);
        } else if (qName.equals(CONCAT_TAG  )) { // for Cobol
            flushBuffer(0);
            writeCommentStart(qName, attrs);
        } else if (qName.equals(IDENTIFIER_TAG)) {
            value = attrs.getValue(VAL_ATTR);
            if (value != null) {
                saxBuffer.append(forceUpperCase && caseIndependant ? value.toUpperCase() : value);
            }
        } else if (qName.equals(KEYWORD_TAG )) {
            // subsumes VERB_MODE also
            value = attrs.getValue(VAL_ATTR);
            if (value != null) {
                saxBuffer.append(forceUpperCase && caseIndependant ? value.toUpperCase() : value);
            }
        } else if (qName.equals(LABEL_TAG   )) {
            flushBuffer(0);
            writeLabel(qName, attrs);
/*
        } else if (qName.equals(NEWLINE_TAG )) {
            flushBuffer(0);
            String lno = attrs.getValue(LINE_NO_ATTR);
            if (lno != null && ! lno.equals("0")) {
                flushLine();
            }
            saxPrefix  = attrs.getValue(PRE_ATTR);
            saxPostfix = attrs.getValue(POST_ATTR);
            if (saxInComment) {
                saxBuffer.append(saxCommentIndent);
            }
*/
        } else if (qName.equals(NEXT_CONTINUE_TAG )) {
            flushBuffer(0);
            String nc = attrs.getValue(VAL_ATTR);
            saxBuffer.append(nc);
        } else if (qName.equals(PREV_CONTINUE_TAG )) {
            flushBuffer(0);
            String pc = attrs.getValue(VAL_ATTR);
            saxBuffer.append(pc);
        } else if (qName.equals(NUMBER_TAG  )) {
            String num = attrs.getValue(VAL_ATTR);
            if (num != null) {
                saxBuffer.append(num);
            }
        } else if (qName.equals(OPERATOR_TAG)) {
            String op = attrs.getValue(VAL_ATTR);
            if (op != null) {
                saxBuffer.append(op);
            }
        } else if (qName.equals(PRAGMA_TAG  )) {
            flushBuffer(0);
            saxBuffer.append(PRAGMA_START);
        } else if (qName.equals(SEMI_TAG    )) {
            saxBuffer.append(';');
        } else if (qName.equals(STRING_TAG  )) {
            modeAttr = attrs.getValue(MODE_ATTR);
            if (false) {
            } else if (modeAttr == null || modeAttr.equals(ASTRING_MODE)) {
                saxBuffer.append(saxApos);
            } else if (modeAttr.equals(DSTRING_MODE)) {
                saxBuffer.append(saxQuote);
            }
            flushBuffer(0);
            saxInString = true;
        } else if (qName.equals(WHITESPACE_TAG   )) {
            // prefixSpaces was already called - do not evaluate that mode
            value    = attrs.getValue(VAL_ATTR ); // number of tabs, margin text, line number
            modeAttr = attrs.getValue(MODE_ATTR);
            if (false) {
            } else if (modeAttr == null || modeAttr.equals(TAB_MODE)) { // TAB_MODE is default
                if (value != null) { // tab count >= 0
                    int ival = 0;
                    try {
                        ival = Integer.parseInt(value);
                    } catch (Exception exc) {
                    }
                    while (ival > 0) { // expand tab count
                        saxBuffer.append('\t');
                        ival --;
                    } // while ival
                } // tab count >= 0
            } else if (modeAttr.equals(NEWLINE_MODE )) {
                flushBuffer(0);
                if (value != null && ! value.equals("0")) { // not at the beginning
                    flushLine();
                }
                if (saxInComment) {
                    saxBuffer.append(saxCommentIndent);
                }
            } else if (modeAttr.equals(PREFIX_MODE )) {
                saxPrefix  = (value != null ? value : "");
            } else if (modeAttr.equals(POSTFIX_MODE)) {
                saxPostfix = (value != null ? value : "");
            }
            // WHITESPACE_TAG
        } // else ignore unknown elements
    } // startElement

    /** Receive notification of the end of an element.
     *  Looks for the element which contains raw lines.
     *  Terminates the line.
     *  @param uri the Namespace URI, or the empty string if the element has no Namespace URI
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix),
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix),
     *  or the empty string if qualified names are not available.
     */
    public void endElement(String uri, String localName, String qName) {
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        if (false) {
        } else if (qName.equals(BACKSLASH_NL_TAG)) {
        } else if (qName.equals(COMMENT_TAG )) {
            flushBuffer(1);
            writeCommentEnd(qName);
            saxInComment = false;
        } else if (qName.equals(PRAGMA_TAG  )) {
            flushBuffer(1);
            saxBuffer.append(PRAGMA_END);
        } else if (qName.equals(STRING_TAG  )) {
            flushBuffer(3);
            saxInString = false;
            if (saxLine.length() < maxColumn) { // no trailing apostrophe in column 73 for Cobol
            }
            if (false) {
            } else if (modeAttr == null || modeAttr.equals(ASTRING_MODE)) {
                saxBuffer.append(saxApos);
            } else if (modeAttr.equals(DSTRING_MODE)) {
                saxBuffer.append(saxQuote);
            }
        } // else ignore unknown elements
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int len) {
        if (! saxInString) {
            if (len > 0 && ch[start + len - 1] == '\n') { // ignore newline between tags
                len --;
            } // last \n
        } // ! saxInString
        saxBuffer.append(escapeInResult(replaceNoResult(new String(ch, start, len))));
    } // characters

} // class ProgLangTransformer
