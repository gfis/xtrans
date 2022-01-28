/*  Extracts metadata from raster image files
    @(#) $Id: ExifGenerator.java 967 2012-08-29 18:22:10Z gfis $
    2012-08-29, Georg Fischer: copied from pseudo.FileTreeGenerator

    not yet implemented
*/
/*
 * Copyright 2012 Dr. Georg Fischer <punctum at punctum dot kom>
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

package org.teherba.xtrans.image.raster;
import  org.teherba.xtrans.ByteTransformer;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;
/*
import  com.drew.imaging.ImageMetadataReader;
import  com.drew.metadata.Directory;
import  com.drew.metadata.Metadata;
import  com.drew.metadata.Tag;
*/
/** Extracts metadata from raster image files.
 *  This class uses the library <em>libmetadata-extractor-java</em>
 *  which may be installed under Ubuntu, or directly from
 *  <a href="http://code.google.com/p/metadata-extractor/">http://code.google.com/p/metadata-extractor/</a>
 *  The author of that library is Drew Noakes.
 *  @author Dr. Georg Fischer
 */
public class ExifGenerator extends ByteTransformer {
    public final static String CVSID = "@(#) $Id: ExifGenerator.java 967 2012-08-29 18:22:10Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** Root element tag */
    private static final String ROOT_TAG   = "metadata";
    /** Directory element tag */
    private static final String DIR_TAG    = "dir";
    /** Tag element tag */
    private static final String TAG_TAG    = "tag";

    /** No-args Constructor
     */
    public ExifGenerator() {
        super();
        setFormatCodes  ("exif");
        setDescription  ("Exif Metadata");
        setMimeType     ("image/jpeg");
    } // Constructor

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = LogManager.getLogger(ExifGenerator.class.getName());
    } // initialize

    /*===========================*/
    /* Generator for SAX events  */
    /*===========================*/

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        String indent = "    "; // 4 spaces
        try {
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
        /*
            Metadata metadata = ImageMetadataReader.readMetadata((BufferedInputStream) getByteReader(), false);
            for (Directory directory : metadata.getDirectories()) {
                String dirName = directory.getName().replaceAll("\\W+", "-").replaceAll("\\-+\\Z", "");
                fireStartElement(dirName);
                fireLineBreak();
                for (Tag tag : directory.getTags()) {
                    String tagName = tag.getTagName().replaceAll("\\W+", "-").replaceAll("\\-+\\Z", "");
                    fireCharacters(indent);
                    fireStartElement(tagName);
                    fireCharacters(tag.getDescription());
                    fireEndElement(tagName);
                    fireLineBreak();
                } // for tag
                fireEndElement(dirName);
                fireLineBreak();
            } // for directory
        */
            fireEndElement(ROOT_TAG);
            fireLineBreak();
            fireEndDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
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

} // ExifGenerator
