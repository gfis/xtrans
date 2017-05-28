/*  Transformer for a representation of tokens in programs which
    is ready to be loaded into a database table
    @(#) $Id: TokenTransformer.java 607 2010-12-11 21:19:25Z gfis $
    2017-05-28: javadoc 1.8
    2016-10-13: less imports
    2010-06-09: collapse string and comment tags to one with mode attribute; se and ee
    2009-12-31: sprec, groupid
    2009-12-18, Dr. Georg Fischer: copied from pseudo/TokenTransformer.java
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
import  java.io.BufferedReader;
import  java.util.regex.Pattern;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Transformer for a representation of tokens in programs which
 *  is ready to be loaded into a database table.
 *  This format represents all features of the XML produced by
 *  {@link ProgLangTransformer} and its derived classes.
 *  <p>
 *  The tests check for identical results of 2 round trips:
 *  <ul>
 *  <li>language - XML   - language</li>
 *  <li>language - token - language</li>
 *  </ul>
 *  The output is a tab-separated text file with the following columns:
 *  <ul>
 *  <li>programName (optional): program name or other identification of the program</li>
 *  <li>seqNo (optional): sequential element number in the program</li>
 *  <li>tag: element name (start tag): kw, op, num ...</li>
 *  <li>mode: further detail of the element type, for comments, strings, whitespace</li>
 *  <li>spacesBefore: number of spaces before the element</li>
 *  <li>val: main element attribute, content (identifier, number, operator ...)</li>
 *  </ul>
 *  The output file will normally be loaded into a database table.
 *  @author Dr. Georg Fischer
 */
public class TokenTransformer extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: TokenTransformer.java 607 2010-12-11 21:19:25Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** maximum number of files / formats / encodings */
    private static final int MAX_FILE = 2;

    /** Constructor.
     */
    public TokenTransformer() {
        super();
        setFormatCodes("token");
        setDescription("Transformer for Parser Tokens");
        setFileExtensions("tsv");
    } // Constructor

    /** Initializes the (quasi-constant) global structures and variables
     *  common to generator and serializer.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = Logger.getLogger(TokenTransformer.class.getName());
    } // initialize

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
            programName = getOption("name", "");
            String line;
            BufferedReader buffReader = new BufferedReader(charReader);
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            Pattern splitter = Pattern.compile(Token.SEPARATOR);
            int lineCount = 0;
            while ((line = buffReader.readLine()) != null) {
                lineCount ++;
                if (programName.length() > 0 && line.indexOf("{$name}") >= 0) {
                    line = line.replaceAll("\\{\\$name\\}", programName);
                }
                Token token = new Token(line);
                token.fire(this);
            } // while not EOF
            buffReader.close();
            fireEndElement(ROOT_TAG);
            fireLineBreak();
            fireEndDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return  result;
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** Upper bound for character buffer */
    protected static final int MAX_BUF = 4096;
    /** buffer for portions of the input elements */
    protected StringBuffer saxBuffer;
    /** name of input program (commandline parameter) */
    protected String programName;
    /** column separator in output file, default "\t" */
    protected String separator;
    /** whether to output in sortable format (with programName and tokenNo at start of each line) */
    protected boolean sortable;
    /** current line number */
    protected int saxLineNo;
    /** current index (element number) in line */
    protected int saxTokenNo;
    /** current token (not yet finished, without the value), has no nested elements */
    protected Token saxToken;

    /** Prints {@link #saxToken} to the output file.
     */
    protected void printSaxToken() {
        saxToken.setProgramName(programName);
        saxToken.setSeqNo(saxTokenNo ++);
        charWriter.println(replaceInResult(saxToken.toString(sortable)));
    } // printSaxToken

    /** Outputs any accumulated character content, and clears the value
     *  in {@link #saxToken}.
     */
    protected void flushSaxToken() {
        if (saxToken.getVal() != null
                && saxToken.getVal().length() > 0
                && saxToken.getTag().equals(ProgLangTransformer.CHARACTERS)) {
            printSaxToken();
            saxToken.setVal("");
        } // if != null
    } // flushSaxToken

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        try {
            super.startDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        saxBuffer   = new StringBuffer(MAX_BUF); // a rather long portion
        programName = getOption("name", "");
        separator   = getOption("sep", "\t");
        sortable    = ! getOption("sort", "no").toLowerCase().startsWith("n");
        saxLineNo   = 0;
        saxTokenNo  = 1;
        saxToken    = new Token("dummy", 0, ""); // is always refilled/reused
    } // startDocument

    /** Receive notification of the end of the document.
     */
    public void endDocument() {
        flushSaxToken();
    /*
        charWriter.close();
    */
    } // endDocument

    /** Receive notification of the start of an element.
     *  Looks for the element which contains raw lines.
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
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        flushSaxToken();
        saxToken.fillSAXEvent(true, qName, attrs);
        saxToken.fillNonTerminal(true, qName);
        switch (Token.getCategory(qName)) {
            default:
            case Token.TERMINAL:
            case Token.IGNORABLE:
                // wait for val to be filled by characters, output token in endElement
                break;
            case Token.NONTERMINAL:
                if (saxToken.size() > 0) {
                    printSaxToken();
                }
                saxToken.fill(ProgLangTransformer.CHARACTERS, 0, "");
                break;
        } // switch category
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
        switch (Token.getCategory(qName)) {
            default:
            case Token.TERMINAL:
            case Token.IGNORABLE:
                // eventually 'val' has been filled in 'characters'
                printSaxToken();
                break;
            case Token.NONTERMINAL:
                flushSaxToken();
                saxToken.fillSAXEvent(false, qName, null);
                saxToken.fillNonTerminal(false, qName);
                printSaxToken();
                break;
        } // switch category
        saxToken.fill(ProgLangTransformer.CHARACTERS, 0, "");
    } // endElement

    /** Receive notification of the end of an entity
     *  @param name name - The name of the entity. If it is a parameter entity,
     *  the name will begin with '%', and if it is the external DTD subset, it will be "[dtd]".
     */
    public void endEntity(String name) {
        try {
            saxToken.setVal(saxToken.getVal() + "&" + name + ";");
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // endEntity

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int length) {
        if (length == 0) {
           // ignore
        } else {
            if (ch[length - 1] == '\n') {
                length --;
            }
            flushSaxToken();
            String value = (new String(ch, start, length))
                    .replaceAll("&"   , "&amp;" )
                    .replaceAll("\\\"", "&quot;")
                    .replaceAll("\\\'", "&apos;")
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    ;
            if (programName.length() > 0 && value.indexOf(programName) >= 0) {
                value = value.replaceAll(programName, "{\\$name}");
            }
            saxToken.setVal(value);
        } // length >= 0
    } // characters

} // TokenTransformer
