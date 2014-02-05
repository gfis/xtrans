/*  Transforms text lines with columns of fixed width to/from XML
    @(#) $Id: ColumnTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2006-11-03, Dr. Georg Fischer: Frankfurt -> Jena -> Dresden
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

package org.teherba.xtrans.general;
import  org.teherba.xtrans.CharTransformer;
import  java.io.BufferedReader;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** Transformer for lines with fields of fixed width.
 *  Creates an XML &lt;table&gt; with &lt;tr&gt; rows and &lt;td&gt;cells. 
 *  The input file should not contain tab characters, and the lines should
 *  be left and right trimmed.
 *  Whitespace at the beginning and at the end of XML output fields is trimmed.
 *  Column starting positions begin to count at 1,
 *  and are taken from parameter <em>columns</em>, 
 *  or they can be guessed automatically.
 *  @author Dr. Georg Fischer
 */
public class ColumnTransformer extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: ColumnTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** comma separated list of (1-based) column start positions */
    private String columns;
    
    /** lengths for fields, [0] is 1 and last is HIGH_VALUE */
    private int[] sublen;
    
    /** number of lines processed so far */
    private int lineCount;

    /** greater than any real column number */
    private static final int HIGH_VALUE = 0xffffff;
    
    /** No-args Constructor.
     */
    public ColumnTransformer() {
        super();
        setFormatCodes("column");
        setDescription("character file with fields of fixed width");
        setFileExtensions("txt");
    } // Constructor()
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(ColumnTransformer.class.getName());
        columns = "";
	} // initialize

    /** Data element tag */
    private static final String DATA_TAG    = "td";
    /** Root element tag */
    private static final String ROOT_TAG    = "column";
    /** Table element tag */
    private static final String TABLE_TAG   = "table";
    /** Row element tag */
    private static final String ROW_TAG     = "tr";
    /** Info element tag */
    private static final String INFO_TAG    = "info";

    /** Reads some number of lines from the input 
     *  and tries to detect positions (counted from 1) 
     *  where columns may start.
     *  @param buffReader Reader for the input file, , is marked and reset
     *  @return comma separated list of ascending column start positions
     *  (counting from 1)
     */
    private String guessColumns(BufferedReader buffReader) {
        final int MAX_CHARS = 16384; // about 200 lines of 80 characters
        final int MAX_COLS  = 256;   // columns behind that limit cannot be guessed
        StringBuffer result = new StringBuffer(128);
        result.append("1");
        int charCount = 0;
        try {
            if (buffReader.markSupported()) {
                buffReader.mark(MAX_CHARS);
                int counts[] = new int[MAX_COLS]; // counts the non-space characters in that column
                for (int icount = 0; icount < MAX_COLS; icount ++) {
                    counts[icount] = 0;
                } // for icount (1)
                String line = null;
                lineCount = 0;
                while (charCount < MAX_CHARS && (line = buffReader.readLine()) != null) {
                    int len = line.length();
                    lineCount ++;
                    charCount += len + 2; // maybe CR + LF
                    if (len >= MAX_COLS) {
                        len = MAX_COLS - 1;
                    }
                    for (int icount = 0; icount < len; icount ++) {
                        if (line.charAt(icount) != ' ') {
                            counts[icount + 1] ++;
                        }
                    } // for icount (2)
                } // while guessing
                buffReader.reset();
                
                // now evaluate the counts 
                // column 1 is predefined
                int threshold = lineCount >> 1; 
                for (int icount = 2; icount < MAX_COLS; icount ++) {
                    if  (   counts[icount] >= 8 && counts[icount - 1] == 0
                        ||  counts[icount] - counts[icount - 1] > threshold
                        ) 
                    {
                        result.append(",");
                        result.append(Integer.toString(icount));
                    }
                } // for icount (3)
            } // if markSupported
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        // log.debug("guessColumns.result=" + result.toString());
        return result.toString();
    } // guessColumns

    /** Converts a comma separated list of ascending (1-based) column numbers
     *  to an array of substring lengths
     *  @param columns comma separated list of ascending (1-based) column numbers
     *  @return array of substring lengths
     */
    private int[] col1ToSublen(String columns) {
        String cols[] = columns.split(",");
        int ncol = cols.length;
        int sublen[] = new int[ncol + 1]; // want to use them in 'substring'
        int oldCol = 1;
        int icol = 1;
        while (icol < ncol) {
            int col = HIGH_VALUE; // sorry, but bad parameters will skip to end of line
            try {
                col = Integer.parseInt(cols[icol]); 
            } catch (Exception exc) {
            }
            sublen[icol] = col - oldCol;
            if (sublen[icol] <= 0) {
                sublen[icol] = HIGH_VALUE; // was not strictly ascending - skip to end of line
            }
            // log.debug("sublen[" + icol + "]=" + sublen[icol]);
            oldCol = col;
            icol ++;
        } // while icol;
        sublen[icol ++] = HIGH_VALUE; // bigger than any real column number
        return sublen;
    } // col1ToSublen

    /** Gets all options from the environment (commandline or form),
     *  and stores them in local variables
     *  @param buffReader reader to be used for examination the head of the input file
     */
    private void getAllOptions(BufferedReader buffReader) {
        columns = getOption("columns", ""); // so many lines = cells in one table row
        if (columns.length() == 0 && buffReader != null) {
            columns = guessColumns(buffReader);
        }
        sublen = col1ToSublen(columns); // want to use them in 'substring'
    } // getAllOptions

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
            String line;
            BufferedReader buffReader = new BufferedReader(charReader);
            getAllOptions(buffReader);
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireStartElement(TABLE_TAG, toAttribute("columns", String.valueOf(columns)));
            fireLineBreak();
            lineCount = 0;
            while ((line = buffReader.readLine()) != null) {
                fireStartElement(ROW_TAG); // start of next row
                int icol = 1;
                int pos = 0;
                int ncol = sublen.length - 1;
                while (icol <= ncol) { // "<=" since there are ncol+1 'sublen' elements
                    int newPos = pos + sublen[icol];
                    if (newPos > line.length()) {
                        newPos = line.length();
                    }
                    fireStartElement(DATA_TAG);
                    if (pos < line.length()) {
                        fireCharacters (line.substring(pos, newPos)
                                .trim()
                                .replaceAll("&", "&amp;")
                                .replaceAll("<", "&lt;")
                                .replaceAll(">", "&gt;")
                                );
                    }
                    fireEndElement(DATA_TAG);
                    icol ++;
                    pos = newPos;
                } // while icol;

                lineCount ++;
                fireEndElement(ROW_TAG);
                fireLineBreak();
            } // while not EOF
            buffReader.close();
            fireEmptyElement(INFO_TAG, toAttribute("linecount", String.valueOf(lineCount)));
            fireLineBreak();
            fireEndElement(TABLE_TAG);
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

    /** current column number */
    private int saxCol;

    /** current position in output string */
    private int colPos;

    /** buffer for output line */
    private StringBuffer lineBuffer;

    /** currently opened element */
    private String elem;
    
    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        getAllOptions(null);
        lineCount = 0;
        lineBuffer = new StringBuffer(2048); // a rather long line
    } // startDocument
    
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
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        elem = qName;
        if (false) {
        } else if (qName.equals(TABLE_TAG)) {
            String cols = attrs.getValue("columns");
            if (cols != null) {
                columns = cols;
            }
            sublen = col1ToSublen(columns); // want to use them in 'substring'
        } else if (qName.equals(ROW_TAG  )) {
            saxCol = 0;
            colPos = 0;
            lineCount ++;
            lineBuffer.setLength(0);
        } else if (qName.equals(DATA_TAG )) {
            saxCol ++; // starts with 1 for 1st <td> below in 'characters'
        } else if (qName.equals(INFO_TAG )) {
            String temp = attrs.getValue("linecount");
            int nline = -1;
            if (temp != null) {
                try {
                    nline = Integer.parseInt(temp);
                } catch (Exception exc) {
                }
            } 
            if (nline != lineCount) {
                log.warn("actual number of lines (" + lineCount 
                        + ") differs from line count (" + nline
                        + ") stated in <info> element");
            }
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
        elem = ""; // no characters allowed outside <td> ... </td>
        if (false) {
        } else if (qName.equals(DATA_TAG )) { // now finish this field 
            // eventually pad to width
            if (saxCol < sublen.length) {
                colPos += sublen[saxCol];
            }
        } else if (qName.equals(ROW_TAG  )) { // now output this row
            charWriter.println(lineBuffer.toString());
        } // ignore unknown elements
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int len) {
        if (elem .equals(DATA_TAG )) {
            if (colPos < HIGH_VALUE) {
                while (colPos > lineBuffer.length()) {
                    lineBuffer.append(' ');
                } // while padding
            }
            lineBuffer.append(new String(ch, start, len));
        } // else ignore characters in unknown elements
    } // characters
    
} // ColumnTransformer
