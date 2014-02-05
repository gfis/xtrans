/*  Properties of a token (terminal, start tag, end tag) to be read by the parser
    @(#) $Id: Token.java 608 2010-12-12 11:52:01Z gfis $
    2010-07-21: seqNo int again
	2010-06-28: tokenNo -> sortId
    2010-06-09: copied from Item.java
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
package org.teherba.xtrans.parse;
import  org.teherba.xtrans.BaseTransformer;
import  org.teherba.xtrans.CharTransformer;
import  org.teherba.xtrans.proglang.ProgLangTransformer;
import  java.io.PrintWriter;
import  java.sql.ResultSet;
import  java.text.SimpleDateFormat;
import  java.util.regex.Pattern;
import  org.xml.sax.Attributes;
import  org.xml.sax.ContentHandler;

/** Bean which stores all properties of a token 
 *	(terminal, whitespace, comment, XML start tag, XML end tag) 
 *	to be read by the parser. Tokens are - like XML - a representation of
 *	programming language elements which allows for the identical
 *	reproduction of a source file including all whitespace. They
 *  are generated and serialized by <em>TokenTransformer</em>,
 *	and their string representation can easily be loaded into or retrieved from 
 *	an SQL database table.
 *  @author Dr. Georg Fischer
 */
public class Token implements Cloneable {
    public final static String CVSID = "@(#) $Id: Token.java 608 2010-12-12 11:52:01Z gfis $";

    /** Separator for serialization */
    public static final String SEPARATOR = "\t";
	/** Pattern for splitting of raw load format lines */
    private static final Pattern SPLITTER = Pattern.compile(SEPARATOR);

    /** name or other identification of underlying program */
    protected String programName;
    /** Sort identification, unique in the entire program.
     *	Normally this would be a sequential (hex) number of width 5, 
     *  but for inserted tokens there are more digits appended,
     *  and the sorting is in lexicographical order.
     */
    protected int seqNo;
/*
	public String intToSeqNo(int tokenNo) {
		String result = Integer.toHexString(tokenNo);
		if (result.length() < 5) {
			result = "00000000".substring(0, 5 - result.length()) + result; // fill with leading zeroes
		}
		return result;
	} // intToSeqNo
*/	
    /** XML tag name for terminals (kw, id, num, op and so on), or se, ee */
    protected String tag;
    /** Mode, subtype for strings and comments */
    protected String mode;
    /** Number of spaces before the token */
    protected int 	 spacesBefore;
    /** Value: specific operator string, identifier characters, number digits, keyword characters, or XML element name */
    protected String val;

	/* categories of tags */
	/** Terminal symbols: id, kw, op and so on */
	public static final int TERMINAL 	= 1;
	/** Nonterminal symbols: start and end tags */
	public static final int NONTERMINAL = 2;
	/** Comments and whitespace */
	public static final int IGNORABLE   = 3;
	/*  Character content */
	// public static final int CONTENT     = 4;
	
    /** No-args Constructor
     */
    public Token() {
    	tag             = "";
    	spacesBefore 	= 0;
    	mode 			= "";
    	val				= "";
    } // Constructor()

    /** Constructor with most important fields
     *	@param tag XML tag for the token if it corresponds to an empty XML element, 
     *	START_ELEMENT, CHARACTERS or END_ELEMENT otherwise
     *	@param spacesBefore number of spaces before the token
     *	@param mode variant of the token
     */
    public Token(String tag, int spacesBefore, String mode) {
    	this.tag            = tag;
    	this.spacesBefore 	= spacesBefore;
    	this.mode 			= mode;
    	this.val			= "";
    } // Constructor(3, tag)

    /** Constructs from a SAX event (an XML tag and other SAX parser fields).
     *	The value is not yet filled.
     *	@param isStart whether to construct from a start tag
     *  @param qName the qualified name, but with any prefix already removed 
     *	@param attrs attributes of the XML event
     */
    public Token(boolean isStart, String qName, Attributes attrs) {
    	this();
    	fillSAXEvent(isStart, qName, attrs);
    } // Constructor(3,qName)

    /** Constructs from a SAX event (an XML tag and other SAX parser fields).
     *	The value is not yet filled.
     *	@param isStart whether to construct from a start tag
     *  @param qName the qualified name, but with any prefix already removed 
     *	@param attrs attributes of the XML event
     *  @param programName name of the input program
     *	@param seqNo number of this token in the program, must already be incremented by the caller
     */
    public Token(boolean isStart, String qName, Attributes attrs, String programName, int seqNo) {
    	this();
    	fill(isStart, qName, attrs, programName, seqNo);
    } // Constructor(5)

    /** Constructs from raw load format line, which can consist of 6 or 4 fields.
     *  In this file line representation 
     *  the first two fields (programName and seqNo) may be omitted,
     *	since they can be attached from outside. 
     *	@param line text line with tab separated properties 
     *	programName, seqNo (both optional), tag, mode, spacesBefore, val.
     */
    public Token(String line) {
    	this();
        String fields[] = SPLITTER.split(line, -1); // do not discard trailing empty strings
		int icol = 0;
    	if (fields.length > 4) {
	        programName =       fields[icol ++] ;
    	    seqNo       = toInt(fields[icol ++]);
    	} // 6 fields
        tag         	=       fields[icol ++] ;
        mode        	=       fields[icol ++] ;
        spacesBefore	= toInt(fields[icol ++]);
        if (icol < fields.length) {
        	val     	=       fields[icol ++];
        	// c.f. 'toString', where these replacements originate
       		val	= ProgLangTransformer.replaceEntities(val); 
    	} else {
    		val = "";
    	}
        
        switch (getCategory(tag)) {
        	default:
        	case TERMINAL:
        	case IGNORABLE:
        		break;
	        case NONTERMINAL:
				break;
		} // switch category
    } // Constructor(line)

    /** Clone an existing Token.
     */
    public Token clone() {
		Token result = new Token(this.tag, this.spacesBefore, this.mode);
		result.setVal(this.val);
		return result;
    } // clone()

	/** Converts a string to integer, and return 0 for any problem 
	 *	@param str string to be converted
	 *	@return integer value or 0
	 */
	private int toInt(String str) {
		int result = 0;
		try {
			result = Integer.parseInt(str);
		} catch (Exception exc) {
			// ignore, return 0
		}
		return result;
	} // toInt
	
	/** Gets the length of the value &gt;= 0.
	 *	@return integer length or 0
	 */
	public int size() {
		int result = 0;
		if (this.val != null) {
			result = this.val.length();
		}
		return result;
	} // size
	
	/** Determines the category of a tag as an integer
	 *	such that it can be used in a 'switch' statement.
	 *	@param qName name of the tag to be caqtegorized
	 *	@return one of (NON)TERMINAL, IGNORABLE
	 */
	public static int getCategory(String qName) {
		int result = TERMINAL;
        if (false 
        		|| qName.equals(ProgLangTransformer.CHARACTERS    		)
        		|| qName.equals(ProgLangTransformer.CHAR_TAG      		)
        		|| qName.equals(ProgLangTransformer.IDENTIFIER_TAG		)
        		|| qName.equals(ProgLangTransformer.KEYWORD_TAG   		)
        		|| qName.equals(ProgLangTransformer.LABEL_TAG   		)
        		|| qName.equals(ProgLangTransformer.NEXT_CONTINUE_TAG	)
        		|| qName.equals(ProgLangTransformer.NUMBER_TAG    		)
        		|| qName.equals(ProgLangTransformer.PREV_CONTINUE_TAG	)
        		|| qName.equals(ProgLangTransformer.OPERATOR_TAG  		)
        		|| qName.equals(ProgLangTransformer.SEMI_TAG      		)
        		) { // terminal
        	result = TERMINAL;
        } else if (false // whitespace
        		|| qName.equals(ProgLangTransformer.WHITESPACE_TAG)
        		) {	// ignore whitespace
        	result = IGNORABLE;
		} else { // nonterminal
			result = NONTERMINAL;
		} // nonterminal
		return result; 
    } // getCategory

    /** Gets DROP/CREATE statements for the corresponding database table,
     *  which always has 6 columns, while in the file line representation 
     *  the first two (programName and seqNo) may be omitted,
     *	since they can be attached from outside. 
     *  @return data definition SQL statements
     */
    public String getDDL() {
        return    "-- @(#) $Id: Token.java 608 2010-12-12 11:52:01Z gfis $\n"
        		+ "-- generated by " + Token.class.getName() + " on " 
        				+ (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")).format(new java.util.Date()) + "\n"
        		+ "DROP   TABLE tokens;\n"
        		+ "CREATE TABLE tokens\n"
        		+ "\t( programName\tVARCHAR(64) NOT NULL\n"
        		+ "\t, seqNo\tINT NOT NULL\n"
        		+ "\t, tag\tVARCHAR( 8) NOT NULL\n"
        		+ "\t, mode\tVARCHAR( 8)\n"
        		+ "\t, spacesBefore\tINT\n"
        		+ "\t, val\tVARCHAR(256)\n"
				+ "\t, PRIMARY KEY(programName, seqNo)\n"
				+ "\t);\n"        		
				+ "COMMIT;\n";
    } // getDDL

    /** Gets a SELECT statement for all properties (quasi-static).
     *  @return something like "SELECT * from items where program_name = ?"
     */
    public String getSelectStatement() {
        return "SELECT" 
        		+ "  programName"
        		+ ", seqNo"
        		+ ", tag"
        		+ ", mode"
        		+ ", spacesBefore"
        		+ ", val"
        		+ "  FROM tokens WHERE programName = ?"
        		+ "  ORDER BY "
        		+ "  programName"
        		+ ", seqNo"
        		;
    } // getSelectStatement

    /** Fills the token from a SAX event (an XML tag).
     *	The value is not yet filled.
     *	@param isStart whether to construct from a start tag
     *  @param qName the qualified name, but with any prefix already removed 
     *	@param attrs attributes of the XML event
     */
    public void fillSAXEvent(boolean isStart, String qName, Attributes attrs) {
    	this.tag = qName;
    	if (attrs != null) {
			String nsp  = attrs.getValue(ProgLangTransformer.SP_ATTR);
			spacesBefore = 0;
	        if (nsp != null) {
	        	try {
	        		spacesBefore = Integer.parseInt(nsp);
	        	} catch (Exception exc) {
	        		// is still = 0
	        	} 
	        } // s= attribute present
			mode        = attrs.getValue(ProgLangTransformer.MODE_ATTR);
			if (mode == null) {
				mode = "";
			}
			val         = attrs.getValue(ProgLangTransformer.VAL_ATTR);
			if (val == null) {
				val = "";
			}
		} // attrs != null
    } // fillSAXEvent(isStart, qName, attrs)

    /** Modify the token if it is a nonterminal.
     *	@param isStart whether to construct from a start tag
     *  @param qName the qualified name, but with any prefix already removed 
     */
    public void fillNonTerminal(boolean isStart, String qName) {
        switch (getCategory(qName)) {
        	default:
        	case TERMINAL:
        	case IGNORABLE:
	        	break;
	        case NONTERMINAL:
				tag = (isStart ? ProgLangTransformer.START_ELEMENT : ProgLangTransformer.END_ELEMENT);
				val = qName; 
				break;
		} // switch category
    } // fillNonTerminal(isStart, qName)

    /** Fills the token from a SAX event (an XML tag and other SAX parser fields).
     *	The value is not yet filled.
     *	@param isStart whether to construct from a start tag
     *  @param qName the qualified name, but with any prefix already removed 
     *	@param attrs attributes of the XML event
     *  @param programName name of the input program
     *	@param seqNo number of this token in the program, must already be incremented by the caller
     */
    public void fill(boolean isStart, String qName, Attributes attrs, String programName, int seqNo) {
    	fillSAXEvent(isStart, qName, attrs);
    	fillNonTerminal(isStart, qName);
    	this.programName	= programName;
    	this.seqNo		    = seqNo;
    } // fill(5, qName)

    /** Fills the token with the most important fields
     *	@param tag XML tag for the token if it corresponds to an empty XML element, 
     *	START_ELEMENT, CHARACTERS or END_ELEMENT otherwise
     *	@param spacesBefore number of spaces before the token
     *	@param mode variant of the token
     */
    public void fill(String tag, int spacesBefore, String mode) {
    	this.programName    = "";
    	this.seqNo          = 0;
    	this.tag            = tag;
    	this.mode 			= mode;
    	this.spacesBefore	= spacesBefore;
    	this.val			= "";
    } // fill(3, tag)

    /** Fills the token with the most important fields
     *	@param tag XML tag for the token if it corresponds to an empty XML element, 
     *	START_ELEMENT, CHARACTERS or END_ELEMENT otherwise
     *	@param spacesBefore number of spaces before the token
     *	@param mode variant of the token
     *  @param programName name of the input program
     *	@param seqNo number of this token in the program, must already be incremented by the caller
     */
    public void fill(String tag, int spacesBefore, String mode, String programName, int seqNo) {
    	this.programName    = programName;
    	this.seqNo          = seqNo;
    	this.tag            = tag;
    	this.mode 			= mode;
    	this.spacesBefore	= spacesBefore;
    	this.val			= "";
    } // fill(5, tag)

    /** Stores one entry fetched from the database table
     *  @param resultSet result set containing the next row fetched from the database table
     */
    public void fill(ResultSet resultSet) {
        try {
			int icol = 0;
           	programName = resultSet.getString(++ icol);
            seqNo       = resultSet.getInt   (++ icol);
            tag         = resultSet.getString(++ icol);
            mode        = resultSet.getString(++ icol);
            spacesBefore= resultSet.getInt   (++ icol);
            val         = resultSet.getString(++ icol);
        } catch (Exception exc) { // should never happen
            System.err.println(exc.getMessage());
        } 
    } // fill1
            
    /** Generates for some parameter transformer.
     *	@param transformer a class instance which generates SAX events
     */
    public void fire(BaseTransformer transformer) {
    	if (false) {
    	} else if (false
        		||  tag.equals(ProgLangTransformer.CHAR_TAG      		)
        		||  tag.equals(ProgLangTransformer.IDENTIFIER_TAG		)
        		||  tag.equals(ProgLangTransformer.KEYWORD_TAG   		)
        		||  tag.equals(ProgLangTransformer.LABEL_TAG   		)
        		||  tag.equals(ProgLangTransformer.NEXT_CONTINUE_TAG	)
        		||  tag.equals(ProgLangTransformer.NUMBER_TAG    		)
        		||  tag.equals(ProgLangTransformer.PREV_CONTINUE_TAG	)
        		||  tag.equals(ProgLangTransformer.OPERATOR_TAG  		)
        		||  tag.equals(ProgLangTransformer.SEMI_TAG      		)
        		) {
	        transformer.fireEmptyElement(tag, transformer.toAttributes(new String[]
    	    		{ (spacesBefore  > 0 ? ProgLangTransformer.SP_ATTR   : null), String.valueOf(spacesBefore)
        			, (mode.length() > 0 ? ProgLangTransformer.MODE_ATTR : null), mode
        			, ProgLangTransformer.VAL_ATTR                              , val
        			}));
        } else if (	tag.equals(ProgLangTransformer.START_ELEMENT 		)) {
	        transformer.fireStartElement(val, transformer.toAttributes(new String[] 
    	    		{ (spacesBefore  > 0 ? ProgLangTransformer.SP_ATTR   : null), String.valueOf(spacesBefore)
        			, (mode.length() > 0 ? ProgLangTransformer.MODE_ATTR : null), mode
        			}));
        } else if (	tag.equals(ProgLangTransformer.END_ELEMENT 		    )) {
	        transformer.fireEndElement(val);
        } else if (	tag.equals(ProgLangTransformer.CHARACTERS 		    )) {
		  	transformer.fireCharacters(transformer.replaceNoSource(this.val));
        } else if (	tag.equals(ProgLangTransformer.WHITESPACE_TAG 		)) {
			transformer.fireEmptyElement(tag, transformer.toAttributes(new String[] 
    	    		{ (spacesBefore  > 0 ? ProgLangTransformer.SP_ATTR   : null), String.valueOf(spacesBefore)
        			, ProgLangTransformer.MODE_ATTR, mode
       				, ProgLangTransformer.VAL_ATTR,  val
       				}));
		} else { 
			// unknown tag - ignore it
		} 
    } // fire

    /** Generates for some parameter ContentHandler.
     *	@param handler a class instance which generates SAX events
     *  @param uri the Namespace URI, or the empty string if the element has no Namespace URI 
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix), 
     */
    public void fireContent(ContentHandler handler, String uri, String localName) {
		try {
	    	if (false) {
	    	} else if (false
	        		||  tag.equals(ProgLangTransformer.CHAR_TAG      		)
	        		||  tag.equals(ProgLangTransformer.IDENTIFIER_TAG		)
	        		||  tag.equals(ProgLangTransformer.KEYWORD_TAG   		)
	        		||  tag.equals(ProgLangTransformer.LABEL_TAG   		)
	        		||  tag.equals(ProgLangTransformer.NEXT_CONTINUE_TAG	)
	        		||  tag.equals(ProgLangTransformer.NUMBER_TAG    		)
	        		||  tag.equals(ProgLangTransformer.PREV_CONTINUE_TAG	)
	        		||  tag.equals(ProgLangTransformer.OPERATOR_TAG  		)
	        		||  tag.equals(ProgLangTransformer.SEMI_TAG      		)
	        		) {
		        handler.startElement(uri, localName, tag, ProgLangTransformer.attributesArray(new String[]
	    	    		{ (spacesBefore  > 0 ? ProgLangTransformer.SP_ATTR   : null), String.valueOf(spacesBefore)
	        			, (mode.length() > 0 ? ProgLangTransformer.MODE_ATTR : null), mode
	        			, ProgLangTransformer.VAL_ATTR                              , val
	        			}));
	        	handler.endElement(uri, localName, tag);
	        } else if (	tag.equals(ProgLangTransformer.START_ELEMENT 		)) {
		        handler.startElement(uri, localName, val, ProgLangTransformer.attributesArray(new String[] 
	    	    		{ (spacesBefore  > 0 ? ProgLangTransformer.SP_ATTR   : null), String.valueOf(spacesBefore)
	        			, (mode.length() > 0 ? ProgLangTransformer.MODE_ATTR : null), mode
	        			}));
	        } else if (	tag.equals(ProgLangTransformer.END_ELEMENT 		    )) {
		        handler.endElement(uri, localName, val);
	        } else if (	tag.equals(ProgLangTransformer.CHARACTERS 		    )) {
	            handler.characters(val.toCharArray(), 0, val.length());
	        } else if (	tag.equals(ProgLangTransformer.WHITESPACE_TAG 		)) {
		        handler.startElement(uri, localName, tag, ProgLangTransformer.attributesArray(new String[]
	    	    		{ (spacesBefore  > 0 ? ProgLangTransformer.SP_ATTR   : null), String.valueOf(spacesBefore)
	        			, ProgLangTransformer.MODE_ATTR, mode
	       				, ProgLangTransformer.VAL_ATTR,  val
	       				}));
	        	handler.endElement(uri, localName, tag);
			} else { 
				// unknown tag - ignore it
			} 
        } catch (Exception exc) { // should never happen
            System.err.println(exc.getMessage());
        } 
    } // fireContent

    /** Return a string representation of a token, with optional 
     *  sort fields
	 *  @param sortable whether programName and seqNo are prefixed to the line
     *  @return all properties concatenated by the {@link #SEPARATOR}.
     */
    public String toString(boolean sortable) {
    	StringBuffer result = new StringBuffer(256);
    	if (sortable) {
    		result
    			.append(programName)	.append(SEPARATOR)
    			.append(seqNo)			.append(SEPARATOR)
    			;
    	} // sortable
    	result
				.append(tag)			.append(SEPARATOR)
				.append(mode)			.append(SEPARATOR)
    			.append(spacesBefore)	.append(SEPARATOR)
    	      	.append(val // c.f. 'Token(line)', where these replacements are undone
    	      			.replaceAll("\\t", "&#x9;")
    	      			)
    	        ;
    	return result.toString();
    } // toString

    /** Return a string representation without sorting fields 
     *  @return all properties concatenated by the {@link #SEPARATOR}.
     */
    public String toString() {
    	return toString(false);
    } // toString

    /** Separator for key components */
    public static final String SEP = ";";
    
    /** Gets the key for this item
     *  @return concatenation of key components
     */
    public String key() {
        return programName + SEP + String.valueOf(seqNo);
    } // key
    
    //-----------------------------------------------------
    // Bean getters and setters
    
    /** Gets the mode (subtype) of the token.
     *  @return ap, bt, dq for strings, doc, lec for comments
     */
    public String getMode() {
        return mode;
    } // getMode

    /** Sets the mode (subtype) of the token.
     *  @param mode ap, bt, dq for strings, doc, lec for comments
     */
    public void   setMode(String mode) {
        this.mode = mode;
    } // setMode
    
    /** Gets the name of the input program.
     *  @return name of the input program.
     */
    public String getProgramName() {
        return programName;
    } // getProgramName

    /** Sets the name of the input program..
     *  @param programName name of the input program.
     */
    public void   setProgramName(String programName) {
        this.programName = programName;
    } // setProgramName
    
    /** Gets the number of spaces before the token 
     *  @return number of spaces before the token
     */
    public int    getSpacesBefore() {
        return spacesBefore;
    } // getSpacesBefore

    /** Sets the number of spaces before the token
     *  @param spacesBefore number of spaces before the token
     */
    public void   setSpacesBefore(int spacesBefore) {
        this.spacesBefore = spacesBefore;
    } // setSpacesBefore

    /** Gets the tag of the token.
     *  @return kw, id, num, op, se, ee etc.
     */
    public String getTag() {
        return tag;
    } // getTag

    /** Sets the tag of the token.
     *  @param tag  kw, id, num, op, se, ee etc.
     */
    public void   setTag(String tag) {
        this.tag = tag;
    } // setTag

    /** Gets the sort identification 
     *  @return sequential sort identification in the current program 
     */
    public int  getSeqNo() {
        return seqNo;
    } // getSeqNo

    /** Sets the sort identification
     *  @param seqNo sequential sort identification in the current program
     */
    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    } // setSeqNo

    /** Gets value of the token.
     *  @return specific operator string, identifier characters, number digits, keyword characters 
     */
    public String getVal() {
        return val;
    } // getVal

    /** Sets value of the token.
     *  @param val specific operator string, identifier characters, number digits, keyword characters 
     */
    public void   setVal(String val) {
        this.val = val;
    } // setVal
    
//------------------------------------------------------
    /** Main program, prints the DDL for a Token SQL table
     *  @param args Arguments - not used
     */
    public static void main(String args[]) {
    	System.out.print((new Token()).getDDL());
    } // main

} // Token
