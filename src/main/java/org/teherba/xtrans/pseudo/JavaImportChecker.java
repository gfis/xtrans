/*  Serializer which checks import statements in a Java porgram
    @(#) $Id: ProgLangTransformer.java 801 2011-09-12 06:16:01Z gfis $
    2016-10-11, Georg Fischer: copied from ProgLangTransformer
*/
/*
 * Copyright 2016 Dr. Georg Fischer <punctum at punctum dot kom>
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
import  org.teherba.xtrans.proglang.ProgLangTransformer;
import  java.util.TreeMap;
import  java.util.Iterator;
import  org.xml.sax.Attributes;
import  org.apache.log4j.Logger;

/** A serializer which reads XMLized Java source files and processes them in 3 phases:
 *  <ol>
 *  <li>Look for import statements of classes (starting with an uppercase letter).</li>
 *  <li>Look for uses of class identifieres (starting with an uppercase letter).</li>
 *  <li>Notify of superfluous imports, and of missing ones (in the same package).</li>
 *  </ol>
 *  This program cannot generate XML.
 *  @author Dr. Georg Fischer
 */
public class JavaImportChecker extends ProgLangTransformer {
    public final static String CVSID = "@(#) $Id: ProgLangTransformer.java 801 2011-09-12 06:16:01Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** No-args Constructor.
     */
    public JavaImportChecker() {
        super();
        setFormatCodes("jimp");
        setDescription("check Java imports");
        setFileExtensions("txt");
    } // Constructor()

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = Logger.getLogger(JavaImportChecker.class.getName());
    } // initialize

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        log.warn("xtrans.JavaImportChecker cannot generate XML");
        boolean result = false;
        return  result;
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** Maps Java class names to one of:
     *  <ul>
     *  <li>imp  - was imported</li>
     *  <li>use  - was used, but not imported</li>
     *  <li>both - was imported and used</li>
     *  </ul>
     */
    private TreeMap<String, String> occurs;

    /** Phases for the checker */
    private static enum Phase
            { PHASE_IMPORT  // during import declarations
            , PHASE_CLASS   // directly behind 1st "class" keyword
            , PHASE_ENUM    // directly behind "enum" keyword
            , PHASE_USE     // in class body
            };
    /** current phase of the checker */
    private Phase phase;
    
    /** Name of the class to be checked */
    private String subjectClass;

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        occurs = new TreeMap<String, String>();
        phase = Phase.PHASE_IMPORT;
        subjectClass = "(unknown)";
    } // startDocument

    /** List of usual classes in package java.lang */
    private static final String JAVA_LANG =
        ",Boolean,Character,Class,ClassLoader,Double,Exception,IllegalArgumentException,Integer,Long"
        + ",NumberFormatException,Object,Package,String,StringBuffer,System,Throwable,";

    /** Receive notification of the end of the document.
     */
    public void endDocument() {
    	charWriter.println(subjectClass);
        Iterator<String> iter = occurs.keySet().iterator();
        while (iter.hasNext()) {
            String key   = iter.next();
            if (JAVA_LANG.indexOf("," + key + ",") < 0) { // not in java.lang
                String value = occurs.get(key);
                if (! value.equals("both")) {
                    charWriter.println("  " + value +  " only:\t" +  key);
                }
            } // not in java.lang
        } // while iter
    } // endDocument

    /** Whether the name is a camel-case class name and not a final constant
     *  @param name name to be checked
     *  @return whether the name contains a mixture of uppercase and lowercase letters
     */
    private boolean checkCamel(String name) {
        return name.matches("[A-Z]+[a-z].*");
    } // checkCamel

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
        String value = null; // of an attribute
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        if (false) {
        } else if (qName.equals(IDENTIFIER_TAG)) {
            value = attrs.getValue(VAL_ATTR);
            if (value != null) {
                switch (phase) {
                    case PHASE_IMPORT:
                        if (checkCamel(value)) {
                            occurs.put(value, "import");
                        } // camel
                        break;
                    case PHASE_CLASS: // identifier is class name
                        subjectClass = value;
                        occurs.put(value, "both"); // will not be shown
                        phase = Phase.PHASE_USE;
                        break;
                    case PHASE_ENUM: // identifier is enum name
                        subjectClass = value;
                        occurs.put(value, "both"); // will not be shown
                        phase = Phase.PHASE_USE;
                        break;
                    default:
                    case PHASE_USE:
                        if (checkCamel(value)) {
                            String stored = occurs.get(value);
                            if (stored != null) {
                                if (stored.equals("import")) {
                                    occurs.put(value, "both");
                                }
                            } else {
                                occurs.put(value, "use");
                            }
                        } // camel
                        break;
                } // switch phase
            } // value != null
        } else if (qName.equals(KEYWORD_TAG )) {
            value = attrs.getValue(VAL_ATTR);
            if (value != null) {
                switch (phase) {
                    case PHASE_IMPORT:
                        if (false) {
                        } else if (value.equals("class")) {
                            phase = Phase.PHASE_CLASS;
                        } else if (value.equals("enum")) {
                            phase = Phase.PHASE_ENUM;
                        }
                        break;
                    default:
                        break;
                } // switch phase
            } // value != null
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
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int length) {
    } // characters
    
/* found in xtrans/src on 2016-10-11:
ByteRecord
  import only:	Attributes
  import only:	Date
  use only:	Long
  use only:	NumberFormatException
  import only:	SAXException
  import only:	Timestamp
BaseTransformer
  use only:	Boolean
  use only:	IllegalArgumentException
  use only:	Long
  import only:	SAXSource
  import only:	SAXTransformerFactory
  import only:	StreamSource
  import only:	Templates
  import only:	TransformerFactory
ICalendarTransformer
VCardTransformer
OrganizerTransformer
ParseTable
  import only:	Iterator
  import only:	ProgLangTransformer
  import only:	Statement
Production
  use only:	Date
ParseFilter
  import only:	Item
  import only:	Iterator
  import only:	Production
  import only:	Transformation
Token
  import only:	CharTransformer
  use only:	Cloneable
  use only:	Date
  import only:	PrintWriter
XMLTransformer
URIWrapper
Base64Transformer
(unknown)
LDIFTransformer
URITransformer
QuotedPrintableTransformer
BeanRecord
  import only:	Attributes
  import only:	Date
  use only:	Long
  use only:	NumberFormatException
  import only:	SAXException
  import only:	Timestamp
DBaseTransformer
  use only:	Long
DIFTransformer
HitTransformer
RichTextTransformer
TeXTransformer
JSONTransformer
Col1Transformer
SiXMLTransformer
LineTransformer
ColumnTransformer
PYXTransformer
HexDumpTransformer
  use only:	NumberFormatException
SeparatedTransformer
LineSplitter
MainTransformer
FileTreeGenerator
  use only:	Date
  use only:	Long
SystemGenerator
  use only:	Date
CountingSerializer
SequenceGenerator
LevelFilter
JavaImportChecker
  use only:	Phase
BaseRecord
  use only:	Long
  use only:	NumberFormatException
TokenTransformer
  import only:	BaseTransformer
  import only:	Iterator
  import only:	XtransFactory
CTransformer
ProgramSerializer
  import only:	Iterator
  import only:	XtransFactory
CppTransformer
JavaTransformer
JavaScriptTransformer
RubyTransformer
PascalTransformer
PostScriptTransformer
CSSTransformer
PL1Transformer
ProgLangTransformer
VisualBasicTransformer
SQLTransformer
CobolTransformer
  import only:	HashSet
SQLPrettyFilter
  import only:	Attributes
  import only:	AttributesImpl
  use only:	State
REXXTransformer
JCLTransformer
FortranTransformer
EdifactTransformer
X12Transformer
GEDCOMTransformer
MorseCodeTransformer
NMEATransformer
GPSBeanBase
  import only:	Date
  use only:	Double
  use only:	Float
XtransFactory
  import only:	XMLReader
IndexPage
  import only:	HttpSession
XtransServlet
  import only:	MainTransformer
  use only:	XslTransPage
PackageListPage
  import only:	HttpSession
Messages
XslTransPage
  import only:	BaseTransformer
  import only:	HttpSession
  use only:	IndexPage
  import only:	Iterator
  import only:	XtransFactory
Field
DTARecordBase
  import only:	Date
  use only:	Long
  import only:	Timestamp
AEB43Transformer
SWIFTTransformer
DATEVTransformer
  use only:	DATEVField
  use only:	Long
DTA2Transformer
  use only:	Long
CODARecordBase
  import only:	Date
  use only:	Long
  import only:	Timestamp
MT940Transformer
AEB43RecordBase
  import only:	Date
  use only:	Long
  import only:	Timestamp
DTA2RecordBase
  import only:	AttributesImpl
  import only:	Date
  use only:	Long
  import only:	Timestamp
CFONBRecordBase
  import only:	Date
  use only:	Long
  import only:	Timestamp
DTATransformer
  use only:	Long
MT103Transformer
CharTransformer
ExifGenerator
  import only:	BufferedInputStream
  import only:	SAXException
WMFTransformer
DatabaseTransformer
  import only:	Charset
IniTransformer
ConfigTransformer
  import only:	ArrayList
PropertiesTransformer
  import only:	Pattern
MakefileTransformer
  import only:	Pattern
ManifestTransformer
CharRecord
  import only:	Attributes
  import only:	Date
  use only:	Long
  use only:	NumberFormatException
  import only:	SAXException
  import only:	Timestamp
ByteTransformer
NestedLineReader
YACCTransformer
ExtraTransformer
ISO6937Map
*/
} // class JavaImportChecker
