/*  Filters lines depending on the characters in column 1
    @(#) $Id: LineSplitter.java 9 2008-09-05 05:21:15Z gfis $
    2006-11-02: package xtrans
    2006-10-02, Georg Fischer
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

package org.teherba.xtrans;
import  java.io.BufferedReader;
import  java.io.FileInputStream;
import  java.io.FileOutputStream;
import  java.io.PrintWriter;
import  java.nio.channels.Channels;
import  java.nio.channels.ReadableByteChannel;
import  java.nio.channels.WritableByteChannel;

/** Filters lines depending on the characters in column 1 of the source file. 
 *  The input file lines which should contain either:
 *  <ul>
 *  <li>a lowercase letter, which copies the line if and only if this letter is selected</li>
 *  <li>an uppercase letter, which copies the line for all selections <em>different from</em> 
 *  the corresponding lowercase letter</li>
 *  <li>whitespace, which copies the line according to the previously active mode</li>
 *  <li>a punctuation character, which switches to unconditional line copy mode</li>
 *  </ul>
 *  Example:
 *  <pre>
b   private byte[] buffer;
c   private StringBuffer buffer;
n   private StringBuffer buffer;
b   public ByteRecord(int bsize) {
        buffer = new byte[bsize]; 
c   public CharRecord(int bsize) {
        buffer = new StringBuffer (bsize);
        buffer.setLength(0); // empty at the beginning
n   public BeanRecord(int bsize) {
        buffer = new StringBuffer (bsize);
        buffer.setLength(0); // empty at the beginning
A       log = LogManager.getLogger(ByteRecord.class.getName());
        bufferSize = bsize;
    }
 *  </pre>
 *  @author Dr. Georg Fischer
 */
public class LineSplitter { 
    public final static String CVSID = "@(#) $Id: LineSplitter.java 9 2008-09-05 05:21:15Z gfis $";

    /**	Filters lines depending on the characters in column 1.
     *  @param args commandline arguments: -s char [file1 [file2]
     *  <pre>
     *  no  filename:  convert from stdin to stdout;
     *  one filename:  convert from file1 to stdout;
     *  two filenames: convert from file1 to file2;
     *  -s: character to be selected
     *	</pre>
     */
    public static void main(String args[]) {
        int iarg = 0;
        String nl = System.getProperty("line.separator");
        String enc1 = "ISO-8859-1";
        String enc2 = enc1; // "UTF-8";
        char selector = '=';
        
        while (iarg < args.length && args[iarg].startsWith("-")) { // process options
            String option = args[iarg ++].substring(1);
            if (false) {}
            else if (option.startsWith("s")) {
                if (iarg < args.length) {
                    selector = args[iarg ++].charAt(0);
                } else {
                    selector = '=';
                }
            } // -s
        } // while options
        
        try {
            ReadableByteChannel source = iarg < args.length 
                    ? (new FileInputStream (args[iarg ++])).getChannel()
                    : Channels.newChannel(System.in);
            WritableByteChannel target = iarg < args.length 
                    ? (new FileOutputStream (args[iarg ++])).getChannel()
                    : Channels.newChannel(System.out);
            BufferedReader reader = new BufferedReader(Channels.newReader(source, enc1));
            PrintWriter    writer = new PrintWriter   (Channels.newWriter(target, enc2));

            boolean selected = false;
            char previousCol1 = '='; // punctuation = copy unconditionally
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0) {
                    line = " ";
                }
                int offset = 1; // default: delete column 1
                char col1 = line.charAt(0);
                if (col1 == '\t') {
                    col1 = previousCol1;
                    offset = 0; // tabs will not be deleted
                } else if (col1 == ' ') {
                    col1 = previousCol1;
                } else {
                    previousCol1 = col1;
                }
                if (col1 == selector) { // right prefix
                    writer.println(line.substring(offset));
                    selected = true;
                } else if (Character.isLowerCase(col1)) { 
                    // letter, but wrong prefix
                } else if (Character.isUpperCase(col1)) { 
                    // do not copy if corresponding lowercase letter is selected
                    if (Character.toLowerCase(col1) != selector) {
                        writer.println(line.substring(offset));
                    }
                } else { // digit or punctuation - switch to copy-all mode
                    writer.println(line.substring(offset));
                }
            }
            writer.close();
            reader.close();
        } catch(Exception exc) {
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        }
    } // main
} // LineSplitter
