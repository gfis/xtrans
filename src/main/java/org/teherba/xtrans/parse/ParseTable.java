/*  Maps pairs of (state, token) to parser actions
    @(#) $Id: ParseTable.java 798 2011-09-10 15:30:05Z gfis $
    2016-10-13: less imports
    2010-06-04: copied from churchcal/ParseTable.java
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
import  org.teherba.xtrans.parse.Item;
import  org.teherba.xtrans.parse.Production;
import  org.teherba.xtrans.parse.Token;
import  org.teherba.xtrans.parse.Transformation;
import  org.teherba.dbat.Dbat; // load grammar from DB table
import  org.teherba.dbat.Configuration; // load grammar from DB table
import  java.sql.Connection;
import  java.sql.PreparedStatement;
import  java.sql.ResultSet;
import  java.util.ArrayList; // prodNo -> production
import  java.util.HashMap; // (state, item) -> (parserAction, nextInfo)
import  java.util.Stack;
import  org.apache.log4j.Logger;

/** Maps from the parser's state and an input token to a 
 *	parser action and some additional info:
 *	<ul>
 *	<li>for a shift action: the successor state</li>
 *	<li>for a reduction: the {@link Production production} which was applied</li>
 *	<li>for accept: nothing</li>
 *	</ul>
 *	Besides an array for the productions, this class stores the parser's state
 *	table in a hashmap of {@link Item}s, which have the properties of {@link Token}s 
 *	which are relevant for the parser's decision.
 *	<p>
 *	Besides the bean access methods, there is the {@link #delta} method which
 *	is used in the parser's loop, and a {@link #load} method which reads 
 *	the grammar resp. the parse table from a database table.
 *  @author Dr. Georg Fischer
 */
public class ParseTable {
    public final static String CVSID = "@(#) $Id: ParseTable.java 798 2011-09-10 15:30:05Z gfis $";
    /** log4j logger (category) */
    private Logger log;

	/** Table which maps from (state, item) to (action, nextInfo) */
	private HashMap  /*<1.5*/<String, Item>/*1.5>*/ items;
	/** Array which maps a production number to a production */
	private ArrayList/*<1.5*/<Production>/*1.5>*/ productions;
	/** Array with sublists of transformation rules */
	private ArrayList/*<1.5*/<Transformation>/*1.5>*/ transformations;
	
	/** current state of the push-down parser algorithm */
	private int state;
	/** Stack of items accumulated so far for open nonterminals */
	private Stack/*<1.5*/<Item>/*1.5>*/ stack;
		
    /** No-args Constructor
     */
    public ParseTable() {
        log = Logger.getLogger(ParseTable.class.getName());
    	items 		= new HashMap  /*<1.5*/<String, Item>/*1.5>*/(256);
        productions = new ArrayList/*<1.5*/<Production      >/*1.5>*/(256);
        productions.add(null); // ignore element [0] = production for axiom = bop program eop;
    } // Constructor()

    /** Loads the parser tables from database tables.
     *  @param grammarName name of the grammar which is to be used for parsing
     */
    public String load(String grammarName) {
    	String message = "loaded ";
        try {
        	Dbat dbat = new Dbat();
        	dbat.initialize(Configuration.CLI_CALL);
            Connection con = dbat.getConfiguration().openConnection();
	        ResultSet resultSet = null;
			int rowCount = 0;
			
			Item item = new Item();
            PreparedStatement itemStmt  = con.prepareStatement(item  .getSelectStatement());
            itemStmt.clearParameters();
            itemStmt.setString(1, grammarName);
            resultSet = itemStmt.executeQuery();
            rowCount = 0;
            while (resultSet.next()) { 
            	item = new Item();
            	item.fill(resultSet);
            	items.put(item.key(), item);
	            rowCount ++;
            } // while fetching
            resultSet.close();
            itemStmt.close();
            con.commit();
			message = String.valueOf(rowCount) + " items " + message;

			Production prod = new Production();
            PreparedStatement prodStmt  = con.prepareStatement(prod.getSelectStatement());
            prodStmt.clearParameters();
            prodStmt.setString(1, grammarName);
            resultSet = prodStmt.executeQuery();
            rowCount = 0;
            while (resultSet.next()) { 
				prod = new Production();
				prod.fill(resultSet);
				productions.add(prod.key(), prod);
	            rowCount ++;
            } // while fetching
            resultSet.close();
            prodStmt.close();
            con.commit();
			message = "ParseTable: " + String.valueOf(rowCount) + " productions, " + message;

			Transformation trans = new Transformation();
            PreparedStatement transStmt  = con.prepareStatement(trans.getSelectStatement());
            transStmt.clearParameters();
            transStmt.setString(1, grammarName);
            resultSet = transStmt.executeQuery();
            rowCount = 0;
            while (resultSet.next()) { 
				trans = new Transformation();
				trans.fill(resultSet);
				transformations.add(trans.key(), trans);
	            rowCount ++;
            } // while fetching
            resultSet.close();
            transStmt.close();
            con.commit();
			message = "ParseTable: " + String.valueOf(rowCount) + " transformations, " + message;

			dbat.getConfiguration().closeConnection();

        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        } 
		return message + " from grammar \"" + grammarName + "\"";
    } // load

	/** Initializes the {@link #state} and the {@link #stack}.
	 */
	public void initParser() {
		stack = new Stack/*<1.5*/<Item>/*1.5>*/();
        state = 1; // initial state
	} // initParser

	/** Performs the elementary mapping of the push-down parser
	 *	from a pair of ({@link #state}, token) to the next parser action 
	 *	and corresponding additional information.
	 *	Adjusts the {@link #state} and the {@link #stack}.
	 *	@param token next input token to be fed into the parsing process.
	 */
	public Item delta(Token token) {
		Item result = null;
		return result;
	} // delta

    /** Main program, prints SQL statements for the creation and/or filling
     *  of the database tables.
     *  <pre>
     *  usage:\tjava org.teherba.xtrans.ParseTable [-c|-ci|-i]
     *  </pre>
     */
    public static void main(String args[]) {
    	Item 			item  = new Item		 	();
    	Production  	prod  = new Production		();
    	Transformation	trans = new Transformation	();
        int iarg = 0;
        if (iarg >= args.length) {
            System.err.println("usage:\tjava org.teherba.xtrans.parse.ParseTable [-c|-ci|-i]\n");
        } else {
            String options = args[iarg ++];
            if (options.indexOf("c") >= 0) { // print DDL
            	System.out.print(item .getDDL());
            	System.out.print(prod .getDDL());
            	System.out.print(trans.getDDL());
            } // c
            if (options.indexOf("i") >= 0) { // print INSERT statements for "ident" grammar
				// not yet implemented
            } // i
        } // >= 1 argument
    } // main
    
} // ParseTable
