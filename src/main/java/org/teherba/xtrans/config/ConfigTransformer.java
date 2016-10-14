/*  Superclass for Transformers for Configuration formats
    @(#) $Id: ConfigTransformer.java 801 2011-09-12 06:16:01Z gfis $
    2010-10-19: unchecked additions; totally untested again, did 530 work???
    2010-07-27, Dr. Georg Fischer: copied from OrganizerTransformer
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
package org.teherba.xtrans.config;
import  org.teherba.xtrans.CharTransformer;
import  java.io.BufferedReader;
import  java.util.regex.Matcher;
import  java.util.regex.Pattern;
import  org.xml.sax.Attributes;
import  org.xml.sax.helpers.AttributesImpl;
import  org.apache.log4j.Logger;

/** This class provides common features of transformers for
 *  Unix configuration, property, manifest, ini, makefiles
 *  and similiar file formats. The files contain:
 *  <ul>
 *  <li>lines with key/value pairs</li>
 *  <li>comments</li>
 *  <li>empty lines</li>
 *  <li>optional section headings</li>
 *  <li>continuation lines resp. folded lines</li>
 *  </ul>
 *  The file may be divided into sections with headings,
 *  which are transformed into XML &lt;table&gt; elements with names.
 *  Each key/value pair is transformed into a &lt;tr&gt; table row
 *  with a &lt;th&gt; element for the key and a &lt;td&gt; element for the value.
 *  Spaces are preserved with <em>s</em> attributes.
 *  Newlines and tabs are represented by separate XML elements.
 *  Example:
 *  <pre>
; see http://cocoon.apache.org
[.ShellClassInfo]
IconFile=src\documentation\images\cocoon.ico
IconIndex=0
ConfirmFileOp=0
InfoTip=Apache Cocoon, the XML application framework!
 *  </pre>
 *  See also:
 *  <ul>
 *  <li><a href="http://en.wikipedia.org/wiki/INI_file">http://en.wikipedia.org/wiki/INI_file</a></li>
 *  <li><a href="http://java.sun.com/javase/6/docs/technotes/guides/jar/jar.html#JAR Manifest">http://java.sun.com/javase/6/docs/technotes/guides/jar/jar.html#JAR Manifest</a></li>
 *  </ul>
 *  @author Dr. Georg Fischer
 */
public class ConfigTransformer extends CharTransformer {
    public final static String CVSID = "@(#) $Id: ConfigTransformer.java 801 2011-09-12 06:16:01Z gfis $";

    /** log4j logger (category) */
    protected Logger log;

    /** Tag for the whole configuration document;
     *  <em>DATA, HEAD, ROW, TABLE</em> tags are inherited from
     *  {@link org.teherba.xtrans.BaseTransformer}
     */
    protected static final String ROOT_TAG          = "configuration";
    /** Tag for an empty line */
    protected static final String NEWLINE_TAG       = "n";
    /** Comment element tag */
    protected static final String COMMENT_TAG       = "co";

    /** name of attribute for line end comment character (only set if not first in array) */
    protected static final String LEC_ATTR          = "lec";
    /** name of space attribute (only set if not 0) */
    protected static final String SP_ATTR           = "s";
    /** name of whitespace attribute (only set if not the empty string) */
    protected static final String WS_ATTR           = "w";
    /** name of separator attribute (only set if not default) */
    protected static final String SEP_ATTR          = "sep";
    /** name of space before separator attribute (only set if not 0) */
    protected static final String SEP_SP_ATTR       = "seps";
    /** name of table name attribute (only set for section headings) */
    protected static final String NAME_ATTR         = "name";
    /** name of table's sequential number attribute (only set if table is not named) */
    protected static final String SEQ_ATTR          = "seq";

    /** Maximum line length after which a line is "folded", that is immediately broken
     *  and continued with a leading space on the next line (default = 0: no hard folding)
     */
    protected int       hardFoldLength;
    /** Separator for key/value pairs, usually ":" or "=" */
    protected String    pairSeparator;
    /** Continuation marker at the end of a line which is broken up, "\\" for Unix */
    protected String    nextContinue;
    /** Line end comment indicator, for example "#" */
    protected String[]  lineEndComment;
    /** number of spaces before a word */
    protected int       spaceBefore;

    /** Pattern for separator matching */
    protected Pattern   wordPunct;
    /** Pattern for line splitting */
    protected Pattern   splitPattern;

    /** No-args Constructor.
     */
    public ConfigTransformer() {
        super();
        setFormatCodes("config");
        setDescription("Configuration file");
        setFileExtensions("conf,cfg,ini");
    } // Constructor

    /** sequential number of current table: 1, 2 ... */
    protected int tableSeq;
    /** name of the section/table, or null if none */
    protected String sectionName;
    /** buffer for folded lines */
    protected StringBuffer foldBuffer;
    /** Number of lines read so far */
    protected int lineCount;
    /** whether there is a row   which must be terminated */
    protected boolean rowOpen;
    /** whether there is a table which must be terminated */
    protected boolean tableOpen;

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = Logger.getLogger(ConfigTransformer.class.getName());
        // Unix-like defaults:
        pairSeparator   = getOption("sep"   , "=" );
        nextContinue    = getOption("cont"  , "\\");
        lineEndComment  = new String[] { getOption("comment", "#" ) };
        hardFoldLength  = getIntOption("fold", 0   ); // no hard folding
        wordPunct       = Pattern.compile("([^=\\:]+)([=\\:])");
        splitPattern    = Pattern.compile("(\\s+|" + Pattern.quote(pairSeparator) + ")");
    } // initialize

    /** Constructs an attribute for a number of spaces to be inserted before the element.
     */
    protected Attributes spaceAttribute(int spaceCount) {
        AttributesImpl attrs = new AttributesImpl();
        if (spaceCount > 0) {
            attrs.addAttribute("", SP_ATTR, SP_ATTR, "CDATA", Integer.toString(spaceCount));
        }
        return attrs;
    } // spaceAttribute

    /** Prepares the input line and find any separator.
     *  @param line line to be prepared
     *  @return position of a separator, or -1 if none was found
     */
    protected int analyzeLine(String line) {
        line = line.replaceAll("\\t", " ");
        return line.indexOf(pairSeparator);
    } // analyzeLine

    /** split the line into at most so many parts */
    protected static final int MAX_PARTS = 8;
    /** Whether the key/value pair separator was found so far */
    protected boolean separatorFound;

    /** Tries to split the input line into the following subsequences:
     *  <ol>
     *  <li>leading whitespace (maybe empty)</li>
     *  <li>key(word)<br /></li>
     *
     *  <li>whitespace before the separator (maybe empty)</li>
     *  <li>separator(if present, empty othewise)<br /></li>
     *
     *  <li>whitespace before the value (maybe empty)</li>
     *  <li>value (maybe empty, not including any continuation character)<br /></li>
     *
     *  <li>whitespace before the continuation sequence (if present)</li>
     *  <li>continuation sequence (if present, missing otherwise)</li>
     *  </ol>
     *  @param line line to be splitted
     *  @return a string array with 3 - 4 pairs (whitespace, content)
     */
    protected String[] splitLine(String line) {
        separatorFound = false;
        String whitespace = "";
        String [] parts = new String[MAX_PARTS]; // whitespaces or separator
        int    [] ends  = new int   [MAX_PARTS]; // 1st position behind parts[ipart]

        int wordStart = 0;
        int npart = 0;
        boolean busy = true;
        Matcher matcher = splitPattern.matcher(line);
        while (busy && npart < MAX_PARTS) {
            if (matcher.find()) {
                int wsStart = matcher.start();
                String part = matcher.group();
                switch (npart) {
                    case 0:
                        if (part.equals(pairSeparator)) { // insert empty key
                            parts[npart ++] = ""; // [0]
                            parts[npart ++] = ""; // [0]
                        }
                        if (wsStart > 0) { // no leading whitespace
                            parts[npart ++] = ""; // [0]
                            parts[npart ++] = line.substring(wordStart, wsStart); // [1]
                        }
                        parts[npart ++] = part; // [2]
                        wordStart = matcher.end();
                        break;
                    case 3:
                        parts[npart ++] = line.substring(wordStart, wsStart); // [3]
                        parts[npart ++] = matcher.group(); // [4]
                        wordStart = matcher.end();
                        break;

                    default:
                        break;
                } // switch npart
                parts[npart] = matcher.group();
                ends [npart] = matcher.end();
                processPart(line, npart, parts, ends);
                npart ++;
            } else {
                busy = false;
            }
        } // while finding
        return parts;
    } // splitLine

    /** Processes one matched part.
     *  @param line  line which is splitted
     *  @param npart number of part (starting at 0)
     *  @param parts whitespaces or separator
     *  @param ends  first position in <em>line</em> behind <em>parts[ipart]</em>
     */
    protected void processPart(String line, int npart, String[] parts, int[] ends) {
        /*
                switch (npart) {
                    case 0:
                        if (wsStart == 0) {
                            if (parts[npart].equals(pairSeparator)) {
                                separatorFound = true;
                                fireEmptyElement(DATA_TAG);
                                fireStartElement(DATA_TAG);
                                fireCharacters(parts[npart]);
                                fireEndElement  (DATA_TAG);
                            } else {
                                startBefore = whitespaceToCode(parts[npart]);
                            }
                            // at the beginning
                        } else { // start > 0
                            fireStartElement(DATA_TAG);
                            fireCharacters(line.substring(0, start));
                            fireEndElement  (DATA_TAG);
                            startBefore = whitespaceToCode(parts[npart]);
                        } // start > 0
                        break;
                    case 1:
                        if (parts[npart].equals(pairSeparator)) {
                            separatorFound = true;
                            fireEmptyElement(DATA_TAG);
                            fireStartElement(DATA_TAG);
                            fireCharacters(parts[npart]);
                            fireEndElement  (DATA_TAG);
                        } else {
                            startBefore = whitespaceToCode(parts[npart]);
                        }
                        break;
                    default:
                        break;
                } // switch npart
        */
    } // processPart

    /** Initializes a table with an optional section heading
     */
    public void initializeTable() {
        if (tableOpen) {
            terminateTable();
        }
        if (true) {
            tableSeq ++;
            AttributesImpl attrs = new AttributesImpl();
            if (sectionName != null) {
                attrs.addAttribute("", NAME_ATTR, NAME_ATTR, "CDATA", sectionName);
                sectionName = null;
            }
            attrs.addAttribute("", SEQ_ATTR, SEQ_ATTR, "CDATA", Integer.toString(tableSeq));
            fireStartElement(TABLE_TAG, attrs);
            fireLineBreak();
            tableOpen = true;
        }
    } // initializeTable

    /** Initializes a row containing a key/value pair,
     *  writes all up to and including the <em>td</em> tag.
     *  @param line physical line with a key at the beginning
     *  @param sepos position of the pair separator in <em>line</em>
     */
    public void initializeRow(String line, int sepos) {
        if (! tableOpen) {
            initializeTable();
        }
        fireStartElement(ROW_TAG);
        String[] parts = splitLine(line);
        AttributesImpl attrs = new AttributesImpl();
        if (parts[0].length() > 0) {
            attrs.addAttribute("", WS_ATTR, WS_ATTR, "CDATA", parts[0]);
        }
        fireStartElement(HEAD_TAG, attrs);

            Pattern wordPunct = Pattern.compile("([^=\\:]+)([=\\:])");
            Matcher matcher = wordPunct.matcher(line);
            if (matcher.lookingAt()) {
                String separator = matcher.group(2);
                sepos = matcher.group(1).length(); // position of "=" or ":"
                if (separator.equals(":")) {
                    terminateRow(true); // start a new table
                }
            } // if lookingAt


        parts = splitLine(parts[1]);

        int prevsp = sepos - 1;
        while (prevsp > 0 && line.charAt(prevsp) == ' ') { // trailing space
            prevsp --;
        } // while trailing space
        if (prevsp >= 0) {
            fireCharacters(line.substring(0, prevsp + 1)); // <th> contents
        }
        fireEndElement(HEAD_TAG);
        int nextsp = sepos + 1;
        while (nextsp < line.length() && line.charAt(nextsp) == ' ') { // leading space
            nextsp ++;
        } // while leading space
        attrs = new AttributesImpl();
        if (false) {
        } else if (sepos < 0) { // special meaning: empty line
            attrs.addAttribute("", SEP_SP_ATTR, SEP_SP_ATTR, "CDATA", Integer.toString(sepos              ));
        } else if (sepos - 1 - prevsp != 0) {
            attrs.addAttribute("", SEP_SP_ATTR, SEP_SP_ATTR, "CDATA", Integer.toString(sepos  - 1 - prevsp));
        }
        if (nextsp - 1 - sepos != 0) {
            attrs.addAttribute("", SP_ATTR,     SP_ATTR, "CDATA", Integer.toString(nextsp - 1 - sepos ));
        }
        if (sepos >= 0 && ! pairSeparator.equals(line.substring(sepos, sepos + 1))) {
            attrs.addAttribute("", SEP_ATTR,    SEP_ATTR, "CDATA", line.substring(sepos, sepos + 1));
        } else if (sepos < 0) {
            attrs.addAttribute("", SEP_ATTR,    SEP_ATTR, "CDATA", "");
        }
        fireStartElement(DATA_TAG, attrs);
        if (nextsp < line.length()) {
            foldBuffer.append(line.substring(nextsp));
        }
        rowOpen = true;
    } // initializeRow

    /** Terminates the row and eventually the table.
     *  @param last whether the row is the last in table
     */
    protected void terminateRow(boolean last) {
        if (rowOpen) {
            fireCharacters(foldBuffer.toString());
            foldBuffer.setLength(0);
            fireEndElement(DATA_TAG);
            fireEndElement(ROW_TAG);
            fireLineBreak();
            rowOpen = false;
        }
        if (last) {
            terminateTable();
        }
    } // terminateRow

    /** Terminates the table.
     */
    protected void terminateTable() {
        if (rowOpen) {
            terminateRow(false); // with 'true' it would become recursive
        }
        if (tableOpen) {
            fireEndElement(TABLE_TAG);
            fireLineBreak();
            tableOpen = false;
        }
    } // terminateRow

    /** Determines whether the line is a section heading.
     *  @param line trimmed current line
     */
    protected boolean isSectionHeading(String line) {
        boolean result = false;
        return result;
    } // isSectionHeading

    /** Determines whether the line is empty and therefore is a section break
     *  and if so, terminates the current table.
     *  @param line current trimmed line
     */
    protected boolean isEmptyLine(String line) {
        boolean result = false;
        if (line.length() == 0) {
            result = true;
            terminateRow(true); // empty line starts a new table (for MANIFEST, for example)
            fireEmptyElement(NEWLINE_TAG);
            fireLineBreak();
        } // length = 0
        return result;
    } // isEmptyLine

    /** Determines whether the line is a comment
     *  and if so, terminates the preceeding row
     *  @param line current line
     */
    protected boolean isComment(String line) {
        boolean result = false;
        String trimmedLine = line.trim();
        int ilec = 0;
        while (! result && ilec < lineEndComment.length) { // try all comment introducers
            String lechar = lineEndComment[ilec ++];
            if (trimmedLine.startsWith(lechar)) {
                terminateRow(false);
                AttributesImpl attrs = new AttributesImpl();
                if (! lechar.equals(lineEndComment[0])) { // not the default comment introducer
                    attrs.addAttribute("", LEC_ATTR, LEC_ATTR, "CDATA", lechar);
                }
                if (! line.startsWith(lechar)) { // some whitespace before
                    attrs.addAttribute("", SP_ATTR, SP_ATTR, "CDATA"
                            , Integer.toString(line.indexOf(lechar))); // assume spaces before
                }
                fireStartElement(COMMENT_TAG, attrs);
                fireCharacters(trimmedLine.substring(lechar.length()));
                fireEndElement(COMMENT_TAG);
                fireLineBreak(); // length = 0
                result = true;
            } // startsWith
        } // while ilec
        return result;
    } // isComment

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        foldBuffer = new StringBuffer(2048); // not too small because of BASE64 encoded values
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            String line; // physical
            lineCount = 0;
            sectionName = null;
            tableSeq = 0;
            tableOpen = false;
            rowOpen   = false;
            BufferedReader buffReader = new BufferedReader(charReader);
            while ((line = buffReader.readLine()) != null) {
                lineCount ++;
                if (false) {
                } else if (hardFoldLength > 0 && line.startsWith(" ")) { // continuation, logical line was folded
                    foldBuffer.append(line.substring(1)); // without the space
                } else if (isComment(line)) {
                } else { // start of new logical line
                    if (isSectionHeading(line.trim())) {
                        terminateRow(true);
                        initializeTable();
                    } else { // normal key/value pair?
                        if (isEmptyLine(line.trim())) {
                        } else {
                            terminateRow(false);
                            int sepos = analyzeLine(line); // prepare the line and find any separator
                            if (sepos >= 0) { // pair separator found
                                initializeRow(line, sepos);
                            } else { // sepos < 0:  whole line goes into <td> element
                                initializeRow(line, sepos);
                            }
                        } // not empty
                    } // normal key/value pair
                } // new logical line
            } // while not EOF
            terminateRow(true);
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

    /** buffer for output line */
    protected StringBuffer saxBuffer;

    /** Output the {@link #saxBuffer}, possibly with folded lines,
     *  and empty it.
     */
    protected void flushSaxBuffer() {
        int saxLen = saxBuffer.length();
        // System.err.println("hardFoldLength=" + hardFoldLength);
        if (hardFoldLength > 0 && saxLen > hardFoldLength) {
            int start = hardFoldLength;
            charWriter.println(saxBuffer.substring(0, start));
            while (saxLen - start > hardFoldLength) {
                charWriter.println(" " + saxBuffer.substring(start, start + hardFoldLength - 1));
                start += hardFoldLength - 1;
            } // while folding
            // if to be folded
            if (start < saxLen) {
                charWriter.println(" " + saxBuffer.substring(start));
            }
        } else {
            charWriter.println(saxBuffer.toString());
        }
        saxBuffer.setLength(0);
    } // flushSaxBuffer

    /*  Prefix the tag with a number of spaces taken from the "sp" attribute
     *  @param attrs the attributes attached to the element.
     */
/*
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
*/
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        saxBuffer = new StringBuffer(512);
    } // startDocument

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
        if (false) {
        } else if (qName.equals(ROOT_TAG   )) {
        } else if (qName.equals(TABLE_TAG  )) {
            String name = attrs.getValue(NAME_ATTR);
            if (name != null) { // section name
                charWriter.println("[" + name + "]");
            } // with section name
        } else if (qName.equals(ROW_TAG    )) {
        } else if (qName.equals(HEAD_TAG   )) {
            saxBuffer.append(attrToWhitespace(attrs));
        } else if (qName.equals(DATA_TAG   )) {
            saxBuffer.append(attrToWhitespace(attrs, SEP_SP_ATTR));
            String sep = attrs.getValue(SEP_ATTR);
            if (sep == null) { // attribute not present - take default
                sep = pairSeparator;
            }
            saxBuffer.append(sep);
            saxBuffer.append(attrToWhitespace(attrs));
        } else if (qName.equals(COMMENT_TAG)) {
            String lec = attrs.getValue(LEC_ATTR);
            if (lec == null) {
                lec = lineEndComment[0];
            }
            saxBuffer.append(attrToWhitespace(attrs));
            saxBuffer.append(lec);
        } else if (qName.equals(NEWLINE_TAG)) {
        } else if (false) { // all format specific elements
        }
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
        if (false) {
        } else if (qName.equals(ROOT_TAG   )) {
        } else if (qName.equals(TABLE_TAG  )) {
        } else if (qName.equals(ROW_TAG    )) {
            flushSaxBuffer();
        } else if (qName.equals(HEAD_TAG   )) {
        } else if (qName.equals(DATA_TAG   )) {
        } else if (qName.equals(COMMENT_TAG)) {
            flushSaxBuffer();
        } else if (qName.equals(NEWLINE_TAG)) {
            saxBuffer.setLength(0);
            charWriter.println();
        } else if (false) { // all format specific elements
        }
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int len) {
        String text = new String(ch, start, len);
        if (! text.matches("\\s+")) {
            saxBuffer.append(text);
        }
    } // characters

} // ConfigTransformer
