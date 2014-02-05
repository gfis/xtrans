/*  Filter which reformats ("pretty prints") SQL statements
    @(#) $Id: SQLPrettyFilter.java 605 2010-12-09 05:38:27Z gfis $
	2010-12-06: Dr. Georg Fischer: copied from 'LevelFilter'
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
import  org.teherba.xtrans.parse.Token;
import  org.teherba.xtrans.proglang.ProgLangTransformer;
import  java.util.HashMap;
import  org.xml.sax.Attributes;
import  org.xml.sax.ContentHandler;
import  org.xml.sax.helpers.AttributesImpl;
import  org.apache.log4j.Logger;

/** Pseudo transformer which accepts a SAX stream of events,  
 *  and removes and adds whitespace such that the resulting SQL code is
 *	more pleasant for the human reader.
 *  <p>
 *  The following formatting rules are used:
 *	<ul>
 *	<li>All input whitespace (outside of strings) is replaced by 1 space, or finally removed.</li>
 *	<li>Resulting lines have a maximum length of 72 characters.</li>
 *	<li>Keywords "select", "from", "where" , "order", "group", "union", "left", "right", "outer", "and", "with",
 *		"create", "drop", "update", "insert", "delete", "commit" 
 *		start a new line.</li>
 *	<li>"," (outside a function call) starts or ends a line, depending on parameter <em>-nlcomma="before|after|none"</em>.</li>
 *	<li>"(" (outside a function call) starts a new line.</li>
 *	<li>")" (outside a function call) starts and ends a new line.</li>
 *	<li>Keywords, identifiers, numbers and strings are separated by a single space.</li>
 *	<li>Arithmetic ("+", "-", "*", "/") and relational operators ("="; "&gt;=" etc.) are surrounded by a single space.</li>
 *	<li>"." has no surrounding spaces.</li>
 *	<li>";" is followed by a newline.</li>
 *	<li>All other operators are followed by a single space.</li>
 *	</ul>
 *  Example for a source text:
<pre>
				select 
				    '1' as sort1
				,   op.adropno as sort2
				,   op.adropno
				,   op.adrwsid 
				,	op.adropjn
				,   jo.jobschenv as jobschenv
				,   op.adropdes 
				,   'gry' as style
				,	'ohne Vorl.'  as adrepext 
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
public class SQLPrettyFilter extends ProgLangTransformer { 
    public final static String CVSID = "@(#) $Id: SQLPrettyFilter.java 605 2010-12-09 05:38:27Z gfis $";

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
        log.warn("xtrans.SQLPrettyFilter can serialize to XML only");
        boolean result = false;
        return  result;
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** nesting level in side function calls */
    private int funcLevel;
    /** indenting level */
    private int indentLevel;
    /** indent amount (number of spaces to be added / subtracted for next level) */
    private int indentAmount;
    /** state for space insertion */
    private State spaceState;
    /** number of spaces before the current element */
    private int spaceCount;
    /** state for newline insertion */
    private State newlineState;
    /** maximum length of output lines */
    private int maxLength;
    /** current length of line assembled so far */
    private int curLength;
    /** current output line number */
    private int lineNo;
    /** previous {@link org.teherba.xtrans.parse.Token Token} which is already output */
    private Token oldToken;
    /** current  {@link org.teherba.xtrans.parse.Token Token} whose spacing must be determined */
    private Token newToken;
    /** Map for simple replacements in source strings */
    private HashMap/*<1.5*/<String, Character>/*1.5>*/ keywordMap;
    /** keywords which cause a preceeding newline, separated by dots */
    String keywords;
    /** Enumeration for state values */
    private static enum State 
    	{ ALWAYS
    	, NEVER
    	, ON_DEMAND
    	} ;
    
    /** Receive notification of the beginning of the document.
	 *  Initialize stateful variables.
     */
    public void startDocument() {
        try {
			super.startDocument();
			oldToken = new Token();
	        keywordMap   = new HashMap/*<1.5*/<String, Character>/*1.5>*/(32);
	        keywordMap.put("select"	, new Character('n'));
	        keywordMap.put("from"	, new Character('n'));
	        keywordMap.put("where"	, new Character('n'));
	        keywordMap.put("order"	, new Character('n'));
	        keywordMap.put("group"	, new Character('n'));
	        keywordMap.put("having"	, new Character('n'));
	        keywordMap.put("union"	, new Character('n'));
	        keywordMap.put("left"	, new Character('n'));
	        keywordMap.put("right"	, new Character('n'));
	        keywordMap.put("outer"	, new Character('n'));
	        keywordMap.put("and"	, new Character('n'));
	        keywordMap.put("with"	, new Character('n'));
	        keywordMap.put("create"	, new Character('n'));
	        keywordMap.put("drop"	, new Character('n'));
	        keywordMap.put("update"	, new Character('n'));
	        keywordMap.put("insert"	, new Character('n'));
	        keywordMap.put("delete"	, new Character('n'));
	        keywordMap.put("commit"	, new Character('n'));
	        lineNo       = 0;
			funcLevel 	 = 0;
			indentLevel  = 0;
			indentAmount = 4;
			newlineState = State.NEVER;
			spaceState   = State.NEVER;
			spaceCount   = 0;
			maxLength    = 72;
			curLength    = 0;
			keywords 	 = ".select.from.where.order.group.having.union.left.right.outer.and.with" 
						+  ".create.drop.update.insert.delete.commit";
			filterHandler.startDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // startDocument

    /** Tests whether the line would become longer than the maximum,
     *	and inserts a newline/indent sequence in that case.
     *	@param len length (&gt; 0) of the next token (identifier, keyword, space etc) to be output
	 *  or =0 for unconditional new line
     *	@result whether a newline/indent sequence was inserted
     */
    protected boolean optionalNewLine(int len) {
    	boolean result = false;
    	try {
	    	if (len == 0 || curLength + len >= maxLength) { // insert newline/indent
				result = true;
				lineNo ++;
				filterHandler.startElement("", "", WHITESPACE_TAG, toAttributes(new String[] 
						{ MODE_ATTR,  NEWLINE_MODE
						, VAL_ATTR,   String.valueOf(lineNo)
						}));
				// new line
				filterHandler.endElement("", "", WHITESPACE_TAG);
				spaceCount = indentLevel * indentAmount;
				curLength = spaceCount + len;
	    	} else {
	    		curLength += len;
	    	}
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    	return result;
    } // optionalNewLine
    
    /** Receive notification of the end of the document.
     */
    public void endDocument() {
        try {
			filterHandler.endDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // endDocument
    
    /** Receive notification of the start of an element.
     *  Insert or remove spacing elements or attributes.
     *  @param uri The Namespace URI, or the empty string if the element has no Namespace URI 
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix), 
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix), 
     *  or the empty string if qualified names are not available.
     *  @param attrs the attributes attached to the element. 
     *  If there are no attributes, it shall be an empty Attributes object.
     */
    public void startElementDeprecated(String uri, String localName, String qName, Attributes attrs) {
        try {
        	AttributesImpl newAttrs = new AttributesImpl(attrs);
        	int spIndex		= newAttrs.getIndex(SP_ATTR);
        	int valIndex	= newAttrs.getIndex(VAL_ATTR);
        	int modeIndex	= newAttrs.getIndex(MODE_ATTR);
        	if (spIndex >= 0) {
        		newAttrs.setAttribute(spIndex, "", SP_ATTR, SP_ATTR, "CDATA", "1");
        		optionalNewLine(1);
        	} // spacesBefore
        	String value = "";
        	if (valIndex >= 0) {
        		value = newAttrs.getValue(valIndex);
        		optionalNewLine(value.length());
        	} // value
            if (false) {
            } else if (qName.equals(ROOT_TAG		)) { // ignore
            } else if (qName.equals(COMMENT_TAG		)) { 
            } else if (qName.equals(IDENTIFIER_TAG	) || qName.equals(NUMBER_TAG) ) { 
            } else if (qName.equals(KEYWORD_TAG		) && valIndex >= 0) { 
				value = value.toLowerCase();
        		if (keywords.indexOf("." + value) >= 0) { // found 
        			indentLevel --;
					if (indentLevel < 0) {
						indentLevel = 0;
					}
        			optionalNewLine(0); // unconditional
					if (value.equals("and")) {
						spaceCount += indentAmount; // one more
					}
        			if (spIndex >= 0) {
	        			newAttrs.setAttribute(spIndex, "", SP_ATTR, SP_ATTR, "CDATA", String.valueOf(spaceCount));
					} else {
	        			newAttrs.addAttribute(         "", SP_ATTR, SP_ATTR, "CDATA", String.valueOf(spaceCount));
					}
        			indentLevel ++;
        		} // found
			} else if (qName.equals(OPERATOR_TAG	) && valIndex >= 0) {
				if (false) {
				} else if (value.equals(",")) {
	        		optionalNewLine(0); // unconditional
        			if (spIndex >= 0) {
	        			newAttrs.setAttribute(spIndex, "", SP_ATTR, SP_ATTR, "CDATA", String.valueOf(spaceCount));
					} else {
	        			newAttrs.addAttribute(         "", SP_ATTR, SP_ATTR, "CDATA", String.valueOf(spaceCount));
					}
				} else if (value.equals("(")) {
					indentLevel += 2;
				} else if (value.equals(")")) {
					indentLevel -= 2;
	        		optionalNewLine(0); // unconditional
        			if (spIndex >= 0) {
	        			newAttrs.setAttribute(spIndex, "", SP_ATTR, SP_ATTR, "CDATA", String.valueOf(spaceCount));
					} else {
	        			newAttrs.addAttribute(         "", SP_ATTR, SP_ATTR, "CDATA", String.valueOf(spaceCount));
					}
				}
            } else if (qName.equals(WHITESPACE_TAG	)) { 
            	if (modeIndex >= 0) { // "tb" or "nl" mode - replace it by 1 space
            		newAttrs.removeAttribute(modeIndex);
            		valIndex = newAttrs.getIndex(VAL_ATTR);
            		newAttrs.removeAttribute(valIndex);
            		newAttrs.addAttribute("", SP_ATTR, SP_ATTR, "CDATA", "1");
            	} // with mode
            	// WHITESPACE
			} else { // copy 
			}
			filterHandler.startElement(uri, localName, qName, newAttrs);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // startElementDeprecated
    
    /** Receive notification of the start of an element.
     *  Insert or remove spacing elements or attributes.
     *  @param uri The Namespace URI, or the empty string if the element has no Namespace URI 
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix), 
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix), 
     *  or the empty string if qualified names are not available.
     *  @param attrs the attributes attached to the element. 
     *  If there are no attributes, it shall be an empty Attributes object.
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs) {
        newToken = new Token(true, qName, attrs);
        newToken.fillNonTerminal(true, qName);
    } // startElement
    
    /** Receive notification of the end of an element.
     *  Decrease the level.
     *  @param uri the Namespace URI, or the empty string if the element has no Namespace URI 
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix), 
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix), 
     *  or the empty string if qualified names are not available.
     */
    public void endElement(String uri, String localName, String qName) {
    	int spacesBefore = newToken.getSpacesBefore();
    	if (spacesBefore > 1) {
    		newToken.setSpacesBefore(1);
    	}
    	if (! newToken.getTag().equals(ProgLangTransformer.WHITESPACE_TAG)) {
			newToken.fireContent(filterHandler, uri, localName);
		}
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int length) {
        try {
	        if (length == 0) {
    	       // ignore
        	} else {
	        	if (ch[length - 1] == '\n') {
    	    		length --;
        		}
				String value = (new String(ch, start, length))
        	        //	.replaceAll("&"   , "&amp;" )
            	    //	.replaceAll("\\\"", "&quot;")
                	//	.replaceAll("\\\'", "&apos;")
                    //	.replaceAll("<", "&lt;")
                	//	.replaceAll(">", "&gt;")
						;
        		filterHandler.characters(ch, start, length);
				// newToken.setVal(value);
			} // length >= 0
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // characters

} // SQLPrettyFilter
