/*  Pseudo transformer which generates an XML representation 
	of a nested file directory structure; cannot serialize
    @(#) $Id: FileTreeGenerator.java 967 2012-08-29 18:22:10Z gfis $
    2012-08-29: identical Serializer
    2008-09-05: better linebreaks
    2007-08-30: was in package 'general'
    2007-06-28, Dr. Georg Fischer
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

package org.teherba.xtrans.pseudo;
import  org.teherba.xtrans.CharTransformer;
import  java.io.File;
import  java.io.FileInputStream;
import  java.security.DigestInputStream;
import  java.security.MessageDigest;
import  java.text.SimpleDateFormat;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/**	Pseudo transformer which generates an XML representation 
 *	of a nested file directory structure. Serialization is not possible.
 *  For each file, the name, date and time of last modification, size in bytes
 *	and (with option <em>-md5 true</em>) an MD5 checksum over the file's content
 *	are output. For directories, the date and time, name, and absolute path are output.
 *	Assumes that the directory (file) tree does not contain cycles.
 *  @author Dr. Georg Fischer
 */
public class FileTreeGenerator extends CharTransformer { 
    public final static String CVSID = "@(#) $Id: FileTreeGenerator.java 967 2012-08-29 18:22:10Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** Root element tag */
    private static final String ROOT_TAG   	= "files";
    /** Directory element tag */
    private static final String DIR_TAG     = "dir";
    /** File element tag */
    private static final String FILE_TAG    = "file";

    /** starting directory (file) name */
    private String dirName;
    /** nesting level */
    private int level;
    /** whether to compute an MD5 checksum over the file's content */
    private boolean withMD5;
    
    /** Constructor.
     */
    public FileTreeGenerator() {
        super();
        setFormatCodes("dir");
        setDescription("nested file directory listing");
        setFileExtensions(".");
    } // Constructor()
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        log = Logger.getLogger(FileTreeGenerator.class.getName());
        dirName = null;
        level = 0;
	} // initialize

    /** Readable format for timestamps with milliseconds */
    private static final SimpleDateFormat ISO_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** length of the buffer for reading a file */
    private static final int MAX_BUFFER = 4096;
    
    /** Computes a (MD5) checksum over all bytes in a file 
     *	@param file sum over the bytes in this file
     *	@return checksum as String
     */
    public String computeChecksum(File file) {
    	StringBuffer result = new StringBuffer(64);
    	byte[] buffer = new byte[MAX_BUFFER];
    	try {
    		MessageDigest md = MessageDigest.getInstance("MD5");
    		DigestInputStream digis = new DigestInputStream(new FileInputStream(file), md);
    		int len = MAX_BUFFER;
    		while ((len = digis.read(buffer, 0, len)) > 0) {
    		} // while reading
    		byte[] hash = md.digest();
    		int ihash = 0;
    		while (ihash < hash.length) {
    			result.append(Integer.toHexString((hash[ihash ++] & 0xff) + 0x100).substring(1));
    		}
    	} catch (Exception exc) {
    		log.error(exc.getMessage(), exc);
    	}
    	return result.toString();
    } // computeChecksum
    
    /** Opens some input or output file
     *  @param ifile 0 for source file, 1 for result file
     *  @param fileName name of the file to be opened, or null for stdin/stdout
     *  @return whether the operation was successful
     */
    public boolean openFile(int ifile, String fileName) {
        boolean result = true;
        try {
            // log.debug("openFileTree(" + ifile + ", " + fileName + ");");
            if (ifile <= 0) { // open input
				if (fileName == null) {
					log.error("cannot generate FileTree from System.in");
				} else {
					dirName = fileName;
				}
            } else { // open output
            	super.openFile(ifile, fileName);
            }
        } catch (Exception exc) {
            log.error(exc.getMessage());
            result = false;
        }
        return result;
    } // openFile

    /** Expands one directory (file).
     *  @param file directory to be expanded
     */
    public void expand(File file) {
        try {
        	level ++;
        	if (level > 512) {
        		log.error("directory nesting level > 512");
        		return;
        	}
        	String[] attrs = new String[] 
        	/* 0 */ { "time", ISO_TIMESTAMP_FORMAT.format(new java.util.Date(file.lastModified()))
			/* 2 */	, null	, null // "md5"
   	    	/* 4 */	, null	, null // "size"
    		/* 6 */	, "name", file.getName()
    	    		};
       		int iattr = 2;
    	    if (! file.isDirectory()) {
	        	String md5 = "";
	        	if (withMD5) {
					attrs[iattr ++] = "md5";
					attrs[iattr ++] = computeChecksum(file);
        		} // with MD5
				attrs[iattr ++] = "size";
				attrs[iattr ++] = Long.toString(file.length());
    	    	fireEmptyElement(FILE_TAG, toAttributes(attrs));
    	    } else { // directory
				attrs[iattr ++] = "uri";
				attrs[iattr ++] = file.toURI().toString();
	    	    fireStartElement(DIR_TAG , toAttributes(attrs));
	    	    fireLineBreak();
    	    	File [] files = file.listFiles();
    	    	int ifile = 0;
    	    	while (ifile < files.length) {
    	    		expand(files[ifile ++]);
    	    	} // while files
				fireEndElement  (DIR_TAG);
    	    } // directory	
			fireLineBreak   ();
    	    level --;
        } catch (Exception exc) {
            log.error(exc.getMessage());
        }
    } // expand
    
    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = false;
        withMD5 = ! getOption("md5", "false").startsWith("f");
        try {
	        if (dirName != null) {
	        	fireStartDocument();
	        	fireStartElement(ROOT_TAG);
	    	    fireLineBreak();
    	    	expand(new File(dirName));
    	    	fireEndElement  (ROOT_TAG);
	    	    fireLineBreak();
        	} // dirName != null
        } catch (Exception exc) {
            log.error(exc.getMessage());
            result = false;
        }
        return  result;
    } // generate

        /*==================================*/
    /* SAX handler for XML input        */
	/* The input is repeated unchanged. */
    /*==================================*/

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        try {
			super.startDocument();
			filterHandler.startDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // startDocument

    /** Receive notification of the end of the document.
     *  For a wellformed XML document, the level must be 0 again.
     */
    public void endDocument() {
        try {
			filterHandler.endDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // endDocument
    
    /** Receive notification of the start of an element.
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
        try {
			filterHandler.startElement(uri, localName, qName, attrs);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // startElement
    
    /** Receive notification of the end of an element.
     *  @param uri the Namespace URI, or the empty string if the element has no Namespace URI 
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix), 
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix), 
     *  or the empty string if qualified names are not available.
     */
    public void endElement(String uri, String localName, String qName) {
        try {
			filterHandler.endElement(uri, localName, qName);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // endElement
    
    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array. 
     */
    public void characters(char[] ch, int start, int length) {
        try {
			filterHandler.characters(ch, start, length);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // characters

} // FileTreeGenerator
