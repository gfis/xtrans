/*  Reads lines from a tree of nested include (text) files
    @(#) $Id: NestedLineReader.java 9 2008-09-05 05:21:15Z gfis $
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

package org.teherba.xtrans;
import  java.io.BufferedReader;
import  java.io.FileInputStream;
import  java.nio.channels.Channels;
import  java.nio.channels.ReadableByteChannel;
import  java.util.Stack;
import  org.apache.log4j.Logger;

/** Reads lines from a tree of nested include (text) files.
 *  @author Dr. Georg Fischer
 */
public class NestedLineReader {
    public final static String CVSID = "@(#) $Id: NestedLineReader.java 9 2008-09-05 05:21:15Z gfis $";

    /** log4j logger (category) */
    private static Logger log = Logger.getLogger(NestedLineReader.class.getName());;

    /** maximum nesting level */
    private static final int MAX_NEST = 16;

    /** stack of readersfor the currently open files */
    protected Stack/*<1.5*/<BufferedReader>/*1.5>*/ readerStack;

    /** current (top-most) reader in the stack */
    protected BufferedReader reader;

    /** No-args Constructor.
     */
    public NestedLineReader() {
        readerStack = new Stack/*<1.5*/<BufferedReader>/*1.5>*/();
        reader = null;
    } // Constructor

    /** Constructor which opens an initial source file.
     *  @param fileName file to be opened initially.
     *  @param sourceEncoding encoding of the include file to be opened
     */
    public NestedLineReader(String fileName, String sourceEncoding) {
        this();
        open(fileName, sourceEncoding);
    } // Constructor

    /** Opens a nested include file.
     *  @param fileName file to be opened.
     *  @param sourceEncoding encoding of the include file to be opened
     *  @return whether the file could be opened.
     */
    public boolean open(String fileName, String sourceEncoding) {
        boolean result = true; // assume success
        try {
            ReadableByteChannel channel = (new FileInputStream (fileName)).getChannel();
            reader = new BufferedReader(Channels.newReader(channel, sourceEncoding));
            result = open(reader);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
            result = false;
        }
        return  result;
    } // open

    /** Opens a nested include file.
     *  @param charReader a BufferedReader for a character file, already open
     *  @return whether the file could be opened.
     */
    public boolean open(BufferedReader charReader) {
        boolean result = true; // assume success
        try {
            if (readerStack.size() < MAX_NEST) {
                reader = charReader;
                readerStack.push(reader);
            } else {
                result = false;
                log.error("more than " + MAX_NEST + " nested include files");
            }
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
            result = false;
        }
        return  result;
    } // open

    /** Reads the next line.
     *  @return line read from one of the nested source files,
     *  or <em>null</em> at then end of the outermost file.
     */
    public String readLine() {
        String result = null;
        try {
            result = reader.readLine();
            boolean busy = true;
            while (busy && result == null) {
                reader.close();
                if (readerStack.size() > 1) {
                    reader = readerStack.pop();
                    result = reader.readLine();
                } else { // outermost file is also at EOF
                    busy = false;
                }
            } // while popping
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
            result = null;
        }
        return  result;
    } // readLine

    /** Closes all open files.
     */
    public void close() {
        try {
            while (! readerStack.empty()) {
                reader = readerStack.pop();
                reader.close();
            } // not yet empty
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // close

} // class NestedLineReader
