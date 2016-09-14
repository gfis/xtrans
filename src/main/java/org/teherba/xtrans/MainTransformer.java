/*  Commandline tool which transforms various file formats to and from XML.
 *  @(#) $Id: MainTransformer.java 966 2012-08-29 07:06:07Z gfis $
 *  2016-09-14: MultiFormatFactory back to dynamic XtransFactory
 *  2011-04-06: configure, process methods moved to XtransFactory
 *  2010-07-09: XtransPipe incorporated
 *  2010-06-01: do not close System.out if processing commands from -f, close at the end only
 *  2007-11-05: also with LexicalHandler
 *  2007-04-04: renamed from 'Transformer'
 *  2006-10-12: copied from gramword
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
import  org.teherba.xtrans.BaseTransformer;
import  org.teherba.xtrans.XtransFactory;
import  java.io.BufferedReader;
import  java.io.FileReader;
import  java.io.InputStreamReader;
import  java.util.ArrayList;
import  java.util.Calendar;
import  java.util.Iterator;
import  org.apache.log4j.Logger;

/** This program is a filter which reads a file in some foreign format,
 *  converts it to XML, feeds that into one or more XSLT transformations
 *  (which may be supplied as stylesheet source files or as translets), 
 *  and/or filter classes from this package,
 *  and finally serializes the output to a file in the same or some different 
 *  foreign format. 
 *  <p>
 *  The main method reads processing parameter (formats, filenames, filters,
 *  options) from the command line, or from lines in an input file (behind -f).
 *  @author Dr. Georg Fischer
 */
public class MainTransformer { 
    public final static String CVSID = "@(#) $Id: MainTransformer.java 966 2012-08-29 07:06:07Z gfis $";

    /** log4j logger (category) */
    public Logger log;
    
    /** Factory delivering transformers for different input and output file formats */
    private XtransFactory factory;

    /** Names of input and output file, or null for STDIN/STDOUT. */
    // private String[] fileNames;
    /** Input reader, generates SAX events */
    // private BaseTransformer generator;
    /** Output writer, consumes SAX events */
    // private BaseTransformer serializer;
    /** Factory for SAX XSLT transformers and translets */
    // private static SAXTransformerFactory saxFactory;
    /** real path to web context */
    // private String realPath;
    
    /** Constructor
     */
    public MainTransformer() {
        log = Logger.getLogger(MainTransformer.class.getName());
        factory = new XtransFactory();
        // System.out.println(factory.toString());
    } // Constructor 0

    /** Close all remaining open files, especially STDOUT
     */
    public void closeFiles() {
        factory.closeFiles();
    } // closeFiles()

    /** Sets the real path to the context of the web application 
     *  @param path path to be set
     */
    public void setRealPath(String path) {
        factory.setRealPath(path);
    } // setRealPath
    
    /** Gets the generator for further configuration,
     *  for the redirection of the input reader to an uploaded file 
     *  @return producer of XML 
     */
    public BaseTransformer getGenerator() {
        return factory.getGenerator();
    } // getGenerator

    /** Gets the serializer for further configuration,
     *  for the redirection of the output reader to the servlet's response
     *  @return consumer of XML 
     */
    public BaseTransformer getSerializer() {
        return factory.getSerializer();
    } // getSerializer

    /** Runs an XML generator, a series of XSLT transformations
     *  (either with a stylesheet or a translet) and an XML serializer.
     */
    public void process() {
        factory.process();
    } // process

    /** Processes the commandline arguments and calls the applicable transformer
     *  @param args commandline arguments as strings
     */
    public void processArgs(String args[]) {
        try {
            factory.createPipeLine(args);
            if (false) { // measure execution time
                long startTime = Calendar.getInstance().getTime().getTime();
                factory.process();
                long endTime   = Calendar.getInstance().getTime().getTime();
                System.err.println("Xtrans: " + (endTime - startTime) + " ms");
            } else {
                factory.process();
            }
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }   
    } // processArgs 

    /** Main program, processes the commandline arguments
     *  @param args arguments; see {@link XtransFactory#createPipeLine} for a description
     *  of a sequence of arguments. For the special case "-f filename",
     *  the arguments are read and processed line by line from the specified file.
     */
    public static void main(String args[]) {
        MainTransformer mainTransformer = new MainTransformer();
        mainTransformer.setRealPath("");
        if (args.length == 2 && args[0].equals("-f")) {
            String fileName = args[1];
            String line = null; // current line from text file
            try {
                /* We better read all commands in advance and store them in an array,
                   since -f may refer to STDIN = System.in, and this will
                   cause problems if the pipe also refers to System.in (and closes it).
                */
                BufferedReader lineReader = new BufferedReader
                        ( (fileName == null || fileName.length() <= 0 || fileName.equals("-"))
                        ? new InputStreamReader(System.in)
                        : new FileReader(fileName) 
                        );
                ArrayList<String> list = new  ArrayList<String>(64);
                while ((line = lineReader.readLine()) != null) { // read lines
                    list.add(line);
                } // while ! eof
                lineReader.close();
                
                Iterator<String> iter = list.iterator();
                while (iter.hasNext()) { // process lines
                    line = (String) iter.next();
                    System.err.println("xtrans.MainTransformer " + line);
                    mainTransformer.processArgs(line.split("\\s+"));
                } // while iter
            } catch (Exception exc) {
                mainTransformer.log.error(exc.getMessage(), exc);
            } // try
        } else {
            mainTransformer.processArgs(args);
        }
        mainTransformer.closeFiles();
    } // main

} // MainTransformer
