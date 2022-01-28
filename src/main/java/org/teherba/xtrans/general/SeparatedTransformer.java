/*  Transforms text lines with fields separated or delimited by some character string
    @(#) $Id: SeparatedTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2008-02-13: Java 1.5 types
    2006-11-04, Dr. Georg Fischer: Dresden -> Frankfurt -> Kenzingen
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
import  java.util.HashMap;
import  java.util.Iterator;
import  java.util.regex.Pattern;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transformer for lines with fields separated or delimited by character strings.
 *  Creates an XML &lt;table&gt; with &lt;tr&gt; rows and &lt;td&gt;cells,
 *  optionally preceeded by a line with &lt;th&gt; fields for labels (field names).
 *  The following options can be specified, or may be guessed from the input:
 *  <ul>
 *  <li>the separator/delimiter string (e.g. ",", ";", "&amp;#x9;" = tab, "&amp;#x20;" = whitespace)</li>
 *  <li>whether the line is surrounded by the separator on:
 *      <ul>
 *          <li>the left side</li>
 *          <li>the right side</li>
 *          <li>neither side</li>
 *          <li>both sides</li>
 *      </ul>
 *  <li>whether the first line contains labels = field names</li>
 *  <li>whether the field values may be surrounded by quotes (&quot;) or apostrophes (&amp;apos;)
 *  <li>whether to remove whitespace on both ends of the fields (trim the fields)</li>
 *  </ul>
 *  @author Dr. Georg Fischer
 */
public class SeparatedTransformer extends CharTransformer {
    public final static String CVSID = "@(#) $Id: SeparatedTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** separator resp. delimiter string */
    private String separator;
    /** whether the first line contains labels = field names */
    private boolean labels;
    /** indicator for surrounding: "none", "right", "left" or "both" sides */
    private String surround;

    /** number of lines processed so far */
    private int lineCount;

    /** greater than any real column number */
    private static final int HIGH_VALUE = 0xffffff;
    /** lengths for fields, [0] is 1 and last is HIGH_VALUE */
    private int[] sublen;

    /** No-args Constructor.
     */
    public SeparatedTransformer() {
        super();
        setFormatCodes("separ,delim,separated,delimited");
        setDescription("file with fields separated or delimited by character strings");
        setFileExtensions("txt");
    } // Constructor

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = LogManager.getLogger(SeparatedTransformer.class.getName());
        separator = ",";
        surround = "none";
        labels = false;
    } // initialize

    /** Root element tag */
    private static final String ROOT_TAG    = "separated";

    /** Reads some number of lines from the input
     *  and tries to detect positions (counted from 1)
     *  where columns may start.
     *  @param buffReader Reader for the input file, is marked and reset
     *  @return comma separated list of ascending column start positions
     *  (counting from 1)
     */
    private String guessSeparator(BufferedReader buffReader) {
        final int MAX_CHARS = 16384; // about 200 lines of 80 characters
        String result = "&#x20;"; // take whitespace if we cannot guess
        int charCount = 0;
        try {
            if (buffReader != null && buffReader.markSupported()) {
                buffReader.mark(MAX_CHARS);
                Pattern nonPunct = Pattern.compile("[^\\p{Punct}]+");
                final int MAX_MAP = 512; // do not store more different separator patterns
                int nmap = 0;
                HashMap/*<1.5*/<String, Integer>/*1.5>*/ hash = new HashMap/*<1.5*/<String, Integer>/*1.5>*/(MAX_MAP);
                String line;
                lineCount = 0;
                while (nmap < MAX_MAP && charCount < MAX_CHARS && (line = buffReader.readLine()) != null) {
                    int len = line.length();
                    String puncts[] = nonPunct.split(line, 0);
                    // log.debug("puncts[0]=" + puncts[0] + ", count=" + puncts.length);
                    for (int ipunct = 0; ipunct < puncts.length; ipunct ++) {
                        if (hash.get(puncts[ipunct]) != null) {
                            hash.put(puncts[ipunct], new Integer(1 + ((Integer) hash.get(puncts[ipunct])).intValue()));
                        } else {
                            hash.put(puncts[ipunct], new Integer(1));
                            nmap ++;
                        }
                    } // for ipunct
                    lineCount ++;
                    charCount += len + 2; // maybe CR + LF
                } // while guessing
                buffReader.reset();

                // now evaluate the counts in the hashmap
                Iterator iter = hash.keySet().iterator();
                int currentMax = 0; // current maximum occurrence of a punctuation string
                String punctMax = "&#x20;";
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    int icount = ((Integer) hash.get(key)).intValue();
                    if (icount > currentMax) {
                        currentMax = icount;
                        punctMax = key;
                        // log.debug("key=" + key + ", icount=" + icount);
                    }
                } // while
                if (currentMax > lineCount) {
                    result = punctMax;
                }
            } // if markSupported
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        // log.debug("guessSeparator.result=" + result);
        return result;
    } // guessSeparator

    /** Reads some number of lines from the input
     *  and tries to detect positions (counted from 1)
     *  where columns may start.
     *  @param buffReader Reader for the input file, is marked and reset
     *  @return comma separated list of ascending column start positions
     *  (counting from 1)
     */
    private String guessSurround(BufferedReader buffReader) {
        final int MAX_CHARS = 512; // about 6 lines of 80 characters
        String result = "none"; // default
        int left = 0;
        int right = 0;
        int charCount = 0;
        try {
            if (buffReader != null && buffReader.markSupported()) {
                buffReader.mark(MAX_CHARS);
                String line;
                lineCount = 0;
                while (charCount < MAX_CHARS && (line = buffReader.readLine()) != null) {
                    int len = line.length();
                    if (line.startsWith(separator)) {
                        left ++;
                    }
                    if (line.endsWith(separator)) {
                        right ++;
                    }
                    lineCount ++;
                    charCount += len + 2; // maybe CR + LF
                } // while guessing
                buffReader.reset();

                // now evaluate the counts
                if (left >= lineCount - 2 && right >= lineCount - 2) {
                    result = "both";
                } else if (left >= lineCount - 2) {
                    result = "left";
                } else if (right >= lineCount - 2) {
                    result = "right";
                } else {
                    result = "none";
                }
            } // if markSupported
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        // log.debug("guessSurround.result=" + result);
        return result;
    } // guessSurround

    /** Gets all options from the environment (commandline or form),
     *  and stores them in local variables
     *  @param buffReader reader to be used for examination the head of the input file
     */
    private void getAllOptions(BufferedReader buffReader) {
        labels    = ! getOption("labels", "false").startsWith("f");
        separator = getOption("separator", "");
        if (separator.length() == 0) {
            separator = guessSeparator(buffReader);
        }
        if (separator.startsWith("&#")) {
            if (separator.equals("&#x20;")) {
                separator = "\\s+";
            } else {
                separator = entityToString1(separator);
            }
        }
        // caution, we need the "naked" separator here
        surround  = getOption("surround", "");
        if (surround.length() == 0) {
            surround = guessSurround(buffReader);
        }
        if (! Character.isLetterOrDigit(separator.charAt(0)) && ! separator.startsWith("\\")) {
            separator = "\\" + separator; // escape since it will become a regular expression pattern
        }
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
            fireStartElement(TABLE_TAG, toAttributes(new String[]
                    { "separator", (separator.length() >= 2 && separator.startsWith("\\")
                            ? separator.substring(1)
                            : separator)
                    , "labels", (labels ? "true" : "false")
                    , "surround", surround
                    }));
            fireLineBreak();

            Pattern splitter = Pattern.compile(separator);
            lineCount = 0;
            while ((line = buffReader.readLine()) != null) {
                lineCount ++;
                String fields[] = splitter.split(line, -1); // do not discard trailing empty strings
                fireStartElement(ROW_TAG); // start of next row
                int icol = (surround.startsWith("l") || surround.startsWith("b")) ? 1 : 0;
                int ncol = fields.length - ((surround.startsWith("r") || surround.startsWith("b")) ? 1 : 0);
                while (icol < ncol) {
                    if (labels && lineCount == 1) {
                        fireStartElement(HEAD_TAG);
                        fireCharacters (fields[icol]
                                .trim()
                                .replaceAll("&", "&amp;")
                                .replaceAll("<", "&lt;")
                                .replaceAll(">", "&gt;")
                                );
                        fireEndElement  (HEAD_TAG);
                    } else {
                        fireStartElement(DATA_TAG);
                        fireCharacters (fields[icol]
                                .trim()
                                .replaceAll("&", "&amp;")
                                .replaceAll("<", "&lt;")
                                .replaceAll(">", "&gt;")
                                );
                        fireEndElement  (DATA_TAG);
                    }
                    icol ++;
                } // while icol;
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
    private int icol;

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
            String
            temp = attrs.getValue("labels");
            if (temp != null && temp.length() > 0) {
                labels = ! temp.startsWith("f");
            }
            temp = attrs.getValue("separator");
            if (temp != null && temp.length() > 0) {
                separator = temp;
            }
            if (separator.length() >= 2 && separator.startsWith("\\")) {
                separator = separator.substring(1); // remove "\\"
            }
            temp = attrs.getValue("surround");
            if (temp != null && temp.length() > 0) {
                surround = temp;
            }
        } else if (qName.equals(ROW_TAG  )) {
            icol = 0;
            lineCount ++;
            lineBuffer.setLength(0);
            if (surround.startsWith("l") || surround.startsWith("b")) {
                lineBuffer.append(separator);
            }
        } else if (qName.equals(DATA_TAG )) {
            icol ++; // starts with 1 for 1st <td> below in 'characters'
            if (icol > 1) {
                lineBuffer.append(separator);
            }
        } else if (qName.equals(HEAD_TAG )) {
            icol ++; // starts with 1 for 1st <td> below in 'characters'
            if (icol > 1) {
                lineBuffer.append(separator);
            }
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
    /*
        } else if (qName.equals(DATA_TAG )) { // now finish this field
        } else if (qName.equals(HEAD_TAG )) { // now finish this field
    */
        } else if (qName.equals(ROW_TAG  )) { // now output this row
            if (surround.startsWith("r") || surround.startsWith("b")) {
                lineBuffer.append(separator);
            }
            charWriter.println(lineBuffer.toString());
        } // ignore unknown elements
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param len the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int len) {
        if (elem .equals(DATA_TAG ) || elem .equals(HEAD_TAG)) {
            lineBuffer.append(new String(ch, start, len));
        } // else ignore characters in unknown elements
    } // characters

} // SeparatedTransformer
