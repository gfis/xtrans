/*  Pseudo transformer which generates elements
    for system properties, the environment, the current time etc.
    @(#) $Id: SystemGenerator.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2016-10-13: less imports
    2008-02-13: Java 1.5 types
    2008-02-02: character sets
    2007-12-18, Georg Fischer: copied from CountingSerializer
*/
/*
 * Copyright 2006 Dr. Georg Fischer <dr dot georg dot fischer at gmail dot kom>
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
import  java.net.InetAddress;
import  java.net.NetworkInterface;
import  java.nio.charset.Charset;
import  java.util.Date;
import  java.util.Enumeration;
import  java.util.Iterator;
import  java.util.Locale;
import  java.util.Map;
import  java.util.Properties;
import  java.util.TreeSet;
import  java.text.SimpleDateFormat;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Pseudo transformer which generates elements
 *  for the system properties, the environment,
 *  different pathes, the current date and time, etc.
 *  By default, only the time related elements are output.
 *  Each type of information is encoded by a specific bit
 *  in the integer option <em>mask</em> as follows:
 *  <table><caption>Mask Bits</caption>
 *  <tr><td>  1</td><td>system properties in the JVM</td></tr>
 *  <tr><td>  2</td><td>environment variables of the operating system</td></tr>
 *  <tr><td>  4</td><td>pathes</td></tr>
 *  <tr><td>  8</td><td>current date and time (default)</td></tr>
 *  <tr><td> 16</td><td>character sets supported by the JVM</td></tr>
 *  <tr><td> 32</td><td>available JDBC drivers and their properties</td></tr>
 *  </table>
 *  <p>
 *  There is no round-trip identity.
 *  As a convenience, the serializer writes the XML elements like properties.
 *  @author Dr. Georg Fischer
 */
public class SystemGenerator extends CharTransformer {
    public final static String CVSID = "@(#) $Id: SystemGenerator.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** Root element tag */
    private static final String ROOT_TAG    = "system";
    /** Time element tag */
    private static final String TIME_TAG    = "time";

    /** mask which indicates what elements to show:
     *  <ul>
     *  <li>bit 0 = properties</li>
     *  <li>bit 1 = environment</li>
     *  <li>bit 2 = current time</li>
     *  </ul>
     */
    private int bitMask;

    /** Constructor.
     */
    public SystemGenerator() {
        super();
        setFormatCodes("system");
        setDescription("show system information");
        setFileExtensions("txt");
    } // Constructor

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = LogManager.getLogger(SystemGenerator.class.getName());
    } // initialize

    /** separator string for pathes */
    private String pathSeparator;

    /** Splits a path-like string into component directories, and lists them as separate elements.
     *  @param path string with a path-like list of directories
     *  @param element enclosing element tag
     *  @param elem tag for a single directory
     */
    private void structurePath(Object path, String element, String elem) {
        try {
            String pathStr = (String) path;
            if (pathStr == null) {
                pathStr = "";
            }
            pushXML(element, toAttribute("sep", pathSeparator));
            fireLineBreak();
            String[] dirs = pathStr.split(pathSeparator);
            int idir = 0;
            while (idir < dirs.length) {
                fireSimpleElement(elem, dirs[idir]);
                fireLineBreak();
                idir ++;
            } // while idir
            popXML();
            fireLineBreak();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        } // try
    } // structurePath

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        bitMask = getIntOption("mask", 0x0008); // default: times only; don't show too much on website
        try {
            TreeSet/*<1.5*/<String>/*1.5>*/ keys = null;
            String key      = null;
            String value    = null;
            Iterator iter   = null;

            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();

            int bit = 1; // current bit to be tested: 1, 2, 4, 8, ...
            if ((bitMask & bit) != 0) { // properties
                pushXML("properties", toAttribute("mask", String.valueOf(bit)));
                fireLineBreak();
                Properties props = System.getProperties();
                Enumeration enm = props.propertyNames();
                keys = new TreeSet/*<1.5*/<String>/*1.5>*/(); // sort them
                while (enm.hasMoreElements()) {
                    keys.add((String) enm.nextElement());
                } // while enum
                iter = keys.iterator();
                while (iter.hasNext()) {
                    key   = (String) iter.next();
                    value = (String) props.get(key);
                    pushXML("prop", toAttribute("key", key));
                    fireCharacters(value);
                    popXML();
                    fireLineBreak();
                } // while iter
                popXML(); // properties
                fireLineBreak();
            } // properties

            bit *= 2;
            if ((bitMask & bit) != 0) { // environment
                pushXML("environment", toAttribute("mask", String.valueOf(bit)));
                fireLineBreak();
                Map/*<1.5*/<String, String>/*1.5>*/ env = System.getenv();
                keys = new TreeSet/*<1.5*/<String>/*1.5>*/(env.keySet());
                iter = keys.iterator();
                while (iter.hasNext()) {
                    key   = (String) iter.next();
                    value = (String) env.get(key);
                    pushXML("env", toAttribute("key", key));
                    fireCharacters(value);
                    popXML();
                    fireLineBreak();
                } // while iter
                popXML(); // environment
                fireLineBreak();
            } // environment

            bit *= 2;
            if ((bitMask & bit) != 0) { // pathes
                pathSeparator = (String) System.getProperty("path.separator");
                if (pathSeparator == null) {
                    pathSeparator = ":"; // default Unix
                }
                pushXML("pathes", toAttribute("mask", String.valueOf(bit)));
                fireLineBreak();
                structurePath(System.getProperty("java.class.path"      ), "java.class.path"        , "dir");
                structurePath(System.getProperty("java.library.path"    ), "java.library.path"      , "dir");
                structurePath(System.getProperty("sun.boot.class.path"  ), "sun.boot.class.path"    , "dir");
                structurePath(System.getenv     ("Path"                 ), "env.Path"               , "dir");
                popXML(); // pathes
                fireLineBreak();
            } // pathes

            bit *= 2;
            if ((bitMask & bit) != 0) { // times
                java.util.Date dtime = new java.util.Date();
                pushXML("times", toAttribute("mask", String.valueOf(bit)));
                fireLineBreak();
                fireSimpleElement("era"               , (new SimpleDateFormat("G"    , Locale.US      )).format(dtime));
                fireSimpleElement("era.de"            , (new SimpleDateFormat("G"    , Locale.GERMAN  )).format(dtime));
                fireLineBreak();
                fireSimpleElement("iso.date"          , (new SimpleDateFormat("yyyy-MM-dd"            )).format(dtime));
                fireLineBreak();
                fireSimpleElement("iso.date.time.utc" , (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'SSSZ")).format(dtime));
                fireLineBreak();
                fireSimpleElement("iso.date.time.zone", (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'SSSz")).format(dtime));
                fireLineBreak();
                fireSimpleElement("time.zone.name"    , (new SimpleDateFormat("zzzzz"                 )).format(dtime));
                fireLineBreak();
                fireSimpleElement("year"              , (new SimpleDateFormat("yyyy"                  )).format(dtime));
                fireLineBreak();
                fireSimpleElement("month"             , (new SimpleDateFormat("MM"                    )).format(dtime));
                fireSimpleElement("month.name"        , (new SimpleDateFormat("MMMMM", Locale.US      )).format(dtime));
                fireSimpleElement("month.name.de"     , (new SimpleDateFormat("MMMMM", Locale.GERMAN  )).format(dtime));
                fireLineBreak();
                fireSimpleElement("day"               , (new SimpleDateFormat("dd"                    )).format(dtime));
                fireSimpleElement("day.year"          , (new SimpleDateFormat("DDD"                   )).format(dtime));
                fireLineBreak();
                fireSimpleElement("day.week"          , (new SimpleDateFormat("F"                     )).format(dtime));
                fireSimpleElement("day.week.name"     , (new SimpleDateFormat("EEEEE", Locale.US      )).format(dtime));
                fireSimpleElement("day.week.name.de"  , (new SimpleDateFormat("EEEEE", Locale.GERMAN  )).format(dtime));
                fireLineBreak();
                fireSimpleElement("week.year"         , (new SimpleDateFormat("w"                     )).format(dtime));
                fireSimpleElement("week.month"        , (new SimpleDateFormat("W"                     )).format(dtime));
                fireLineBreak();
                popXML(); // times
                fireLineBreak();
            } // times

            bit *= 2;
            if ((bitMask & bit) != 0) { // character sets
                iter = Charset.availableCharsets().keySet().iterator();
                pushXML("charsets", toAttribute("mask", String.valueOf(bit)));
                fireLineBreak();
                while (iter.hasNext()) {
                    String charset = (String) iter.next();
                    fireSimpleElement("charset", charset);
                    fireLineBreak();
                } // while charsets
                popXML(); // charsets
                fireLineBreak();
            } // character sets

        /*  JDBC driver detection will not work, one must specify the driver's name and the URL
            bit *= 2;
            if ((bitMask & bit) != 0) { // JDBC drivers
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Enumeration drivers = DriverManager.getDrivers();
                pushXML("JDBCdrivers", toAttribute("mask", String.valueOf(bit)));
                fireLineBreak();
                while (drivers.hasMoreElements()) {
                    Driver driver = (Driver) drivers.nextElement();
                    pushXML("driver", toAttributes(new String[]
                            { "major", String.valueOf(driver.getMajorVersion())
                            , "minor", String.valueOf(driver.getMinorVersion())
                            , "jdcbCompliant", String.valueOf(driver.jdbcCompliant())
                            } ));
                    fireLineBreak();
                    // DriverPropertyInfo[] infos = driver.getPropertyInfo("", null);

                    popXML(); // JDBCdrivers
                    fireLineBreak();
                } // while drivers
                popXML(); // JDBCdrivers
                fireLineBreak();
            } // JDBC drivers
        */
            bit *= 2;
            if ((bitMask & bit) != 0) { // network
                Enumeration/*<1.5*/<NetworkInterface>/*1.5>*/ interfaces = NetworkInterface.getNetworkInterfaces();
                pushXML("network-interfaces", toAttribute("mask", String.valueOf(bit)));
                fireLineBreak();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface iface = (NetworkInterface) interfaces.nextElement();
                    pushXML("interface", toAttributes(new String[]
                            { "displayName" , iface.getName         ()
                    //      , "name"        , iface.getDisplayName  ()
                            , "toString"    , iface.toString        ()
                            } ));
                    fireLineBreak();
                    Enumeration/*<1.5*/<InetAddress>/*1.5>*/ addrs = iface.getInetAddresses();
                    while (addrs.hasMoreElements()) {
                        InetAddress addr = (InetAddress) addrs.nextElement();
                        fireSimpleElement("addr", addr.toString());
                        fireLineBreak();
                    } // while addrs
                    popXML();
                    fireLineBreak();
                } // while interfaces
                popXML();
                fireLineBreak();
            } // network

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

    /** number of characters in direct content */
    private int charCount;

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        charWriter.print("SystemGenerator.serialize");
    } // startDocument

    /** Receive notification of the end of the document.
     */
    public void endDocument() {
        charWriter.println();
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
        charWriter.print(newline + qName);
        String key = attrs.getValue("key");
        if (key != null) {
            charWriter.print("." + key);
        }
        charWriter.print("=");
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
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int length) {
        String text = new String(ch, start, length).replaceAll("\\r?\\n", "");
        if (! text.matches("\\s+")) {
            charWriter.print(text);
        }
    } // characters

} // SystemGenerator
