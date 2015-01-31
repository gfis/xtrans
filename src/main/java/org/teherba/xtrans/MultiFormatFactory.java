/*  Selects the applicable transformer - all implemented formats
    @(#) $Id: MultiFormatFactory.java 967 2012-08-29 18:22:10Z gfis $
    2012-08-29: ExifGenerator
    2011-10-04: TeX
    2011-04-05, Georg Fischer: derived from XtransFactory

    Usage:
        java -cp dist/xtrans.jar org.teherba.xtrans.MultiFormatFactory
    Output files:
        src/main/java/org/teherba/xtrans/.../package.html
*/
/*
 * Copyright 2011 Dr. Georg Fischer <punctum at punctum dot kom>
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
import  org.teherba.xtrans.XMLTransformer;
import  org.teherba.xtrans.config.IniTransformer;
import  org.teherba.xtrans.config.MakefileTransformer;
import  org.teherba.xtrans.config.ManifestTransformer;
import  org.teherba.xtrans.config.PropertiesTransformer;
import  org.teherba.xtrans.database.DatabaseTransformer;
import  org.teherba.xtrans.edi.EdifactTransformer;
import  org.teherba.xtrans.edi.X12Transformer;
import  org.teherba.xtrans.finance.AEB43Transformer;
import  org.teherba.xtrans.finance.DATEVTransformer;
import  org.teherba.xtrans.finance.DTATransformer;
import  org.teherba.xtrans.finance.DTA2Transformer;
import  org.teherba.xtrans.finance.MT103Transformer;
import  org.teherba.xtrans.finance.MT940Transformer;
import  org.teherba.xtrans.finance.SWIFTTransformer;
import  org.teherba.xtrans.general.Col1Transformer;
import  org.teherba.xtrans.general.ColumnTransformer;
import  org.teherba.xtrans.general.HexDumpTransformer;
import  org.teherba.xtrans.general.JSONTransformer;
import  org.teherba.xtrans.general.LineTransformer;
import  org.teherba.xtrans.general.PYXTransformer;
import  org.teherba.xtrans.general.SeparatedTransformer;
import  org.teherba.xtrans.general.SiXMLTransformer; // not yet
import  org.teherba.xtrans.geo.NMEATransformer;
import  org.teherba.xtrans.grammar.ExtraTransformer;
import  org.teherba.xtrans.grammar.YACCTransformer;
import  org.teherba.xtrans.image.raster.ExifGenerator;
import  org.teherba.xtrans.image.vector.WMFTransformer;
import  org.teherba.xtrans.misc.GEDCOMTransformer;
import  org.teherba.xtrans.misc.MorseCodeTransformer;
import  org.teherba.xtrans.net.Base64Transformer;
import  org.teherba.xtrans.net.LDIFTransformer;
import  org.teherba.xtrans.net.QuotedPrintableTransformer;
import  org.teherba.xtrans.net.URITransformer;
import  org.teherba.xtrans.office.data.DBaseTransformer;
import  org.teherba.xtrans.office.data.DIFTransformer;
import  org.teherba.xtrans.office.text.HitTransformer;
import  org.teherba.xtrans.office.text.RichTextTransformer;
import  org.teherba.xtrans.office.text.TeXTransformer;
import  org.teherba.xtrans.organizer.ICalendarTransformer;
import  org.teherba.xtrans.organizer.VCardTransformer;
import  org.teherba.xtrans.parse.ParseFilter;
import  org.teherba.xtrans.proglang.CTransformer;
import  org.teherba.xtrans.proglang.CobolTransformer;
import  org.teherba.xtrans.proglang.CppTransformer;
import  org.teherba.xtrans.proglang.CSSTransformer;
import  org.teherba.xtrans.proglang.FortranTransformer;
import  org.teherba.xtrans.proglang.JavaTransformer;
import  org.teherba.xtrans.proglang.JavaScriptTransformer;
import  org.teherba.xtrans.proglang.JCLTransformer;
import  org.teherba.xtrans.proglang.PascalTransformer;
import  org.teherba.xtrans.proglang.PL1Transformer;
import  org.teherba.xtrans.proglang.PostScriptTransformer;
import  org.teherba.xtrans.proglang.ProgramSerializer;
import  org.teherba.xtrans.proglang.REXXTransformer;
import  org.teherba.xtrans.proglang.RubyTransformer;
import  org.teherba.xtrans.proglang.SQLTransformer;
import  org.teherba.xtrans.proglang.SQLPrettyFilter;
import  org.teherba.xtrans.proglang.TokenTransformer;
import  org.teherba.xtrans.proglang.VisualBasicTransformer;
import  org.teherba.xtrans.pseudo.CountingSerializer;
import  org.teherba.xtrans.pseudo.FileTreeGenerator;
import  org.teherba.xtrans.pseudo.LevelFilter;
import  org.teherba.xtrans.pseudo.MailSerializer;
import  org.teherba.xtrans.pseudo.SequenceGenerator;
import  org.teherba.xtrans.pseudo.SystemGenerator;
import  java.io.File;
import  java.io.PrintWriter;
import  java.util.HashMap;
import  java.util.Iterator;
import  org.apache.log4j.Logger;

/** Selects a specific transformer, and iterates over the descriptions
 *  of all transformers and their codes.
 *  <p>
 *  The {@link #main} method of this
 *  class generates package description files in all subdirectories
 *  for the Javadoc API documentation.
 *  @author Dr. Georg Fischer
 */
public class MultiFormatFactory extends XtransFactory{
    public final static String CVSID = "@(#) $Id: MultiFormatFactory.java 967 2012-08-29 18:22:10Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    public MultiFormatFactory() {
        super();
        log = Logger.getLogger(MultiFormatFactory.class.getName());
        allTransformers = new BaseTransformer[] { null // since this allows for "," on next source line
                // the order here defines the order in documentation.jsp,
                // should be: "... group by package order by package, name"
                , new XMLTransformer            () // serializer for XML
                // -------
                // general
                , new Col1Transformer           () // tags in column 1 of text lines
                , new ColumnTransformer         () // fields with fixed columns
                , new HexDumpTransformer        () // hexdump of a binary file
                , new JSONTransformer           () // Java Script Object Notation
                , new LineTransformer           () // character file consisting of lines
                , new PYXTransformer            () // line oriented representation of XML
                // , new SiXMLTransformer          () // Simplified XML representation
                , new SeparatedTransformer      () // fields separated or delimited by character strings
                // config
                , new IniTransformer            () // Windows .ini files
                , new MakefileTransformer       () // Unix 'make' utility input files
                , new ManifestTransformer       () // Java MANIFEST.MF files
                , new PropertiesTransformer     () // Java Properties files
                // database
                , new DatabaseTransformer       () // Dbat generator
                // edi
                , new EdifactTransformer        () // UN/Edifact trade interchange messages
                , new X12Transformer            () // ANSI ASC X12 trade interchange messages
                // finance
                , new AEB43Transformer          () // AEB43 (Spanish payments exchange file)
                , new DATEVTransformer          () // DATEV accounting files
                , new DTATransformer            () // DTA (German payments exchange file)
                , new DTA2Transformer           () // DTA2 (German payments exchange file)
                , new MT103Transformer          () // SWIFT MT103 messages
                , new MT940Transformer          () // SWIFT MT940 messages
                , new SWIFTTransformer          () // SWIFT FIN messages
                // geo
                , new NMEATransformer           () // NMEA-0183 standard data for geopositioning
                // grammar
                , new ExtraTransformer          () // EXTRA = Extensible Translator System (Georg Fischer 1980)
                , new YACCTransformer           () // YACC = Yet Another Compiler Compiler, or GNU Bison
                // image.raster
                , new ExifGenerator             () // Exif metadata
                // image.vector
                , new WMFTransformer            () // Windows Meta Format
                // misc
                , new GEDCOMTransformer         () // Genealogical Data Communication
                , new MorseCodeTransformer      () // Morse Code of a text
                // net
                , new Base64Transformer         () // Base64 encoding of a binary file
                , new LDIFTransformer           () // LDIF addressbooks as exported from Mozilla Thunderbird
                , new QuotedPrintableTransformer() // Quoted Printable encoding
                , new URITransformer            () // lines with single URIs
                // office.data
                , new DBaseTransformer          () // (Navy) dBase II, III et al.
                , new DIFTransformer            () // (Navy) DIF - Data Interchange Format
                // office.text
                , new HitTransformer            () // Interface/Siemens Hit V3.1, V4.0
                , new RichTextTransformer       () // RTF - Microsoft's Rich Text Format
                , new TeXTransformer            () // TeX and LaTeX typesetting
                // organizer
                , new ICalendarTransformer      () // iCalendar calendars and schedules
                , new VCardTransformer          () // VCard address/phone book entries
                // parser
                , new ParseFilter               () // parse a SAX stream of events and insert/delete/rearrange production elements
                // programming languages
                , new CTransformer              () // C source programs
                , new CobolTransformer          () // Cobol source programs
                , new CppTransformer            () // C++ source programs
                , new CSSTransformer            () // Cascaded Style Sheets
                , new FortranTransformer        () // Fortran (IV, 77)
                , new JavaTransformer           () // Java source programs
                , new JavaScriptTransformer     () // JavaScript source programs
                , new JCLTransformer            () // IBM z/OS Job Control Language
                , new PascalTransformer         () // (Turbo) Pascal
                , new PL1Transformer            () // PL/1
                , new PostScriptTransformer     () // PostScript (Adobe)
                , new ProgramSerializer         () // table of syntax elements suitable for SQL input
                , new REXXTransformer           () // REXX
                , new RubyTransformer           () // Ruby
                , new SQLPrettyFilter           () // pretty print SQL instructions
                , new SQLTransformer            () // Structured Query Language for relational databases
                , new TokenTransformer          () // Programming language tokens
                , new VisualBasicTransformer    () // Microsoft's Visual Basic (for Applications)
                // pseudo
                , new CountingSerializer        () // counts XML elements and length of direct character content
                , new FileTreeGenerator         () // recursively walks a file/directory tree
                , new LevelFilter               () // add a level attribute to all start element tags
                , new MailSerializer            () // get address, subject and filenames from XMl and send a mail
                , new SequenceGenerator         () // generates a sequence
                , new SystemGenerator           () // show system information
                // --------
                };
    } // Constructor

    /** Maps subpackage names to their descriptions */
    private static HashMap/*<1.5*/<String, String>/*1.5>*/ descMap;

    /** Stores the descriptions of all subpackages.
     */
    private static void storeSubPackages() {
        descMap = new HashMap/*<1.5*/<String, String>/*1.5>*/();
        descMap.put("config"        , "configuration file formats");
        descMap.put("edi"           , "electronic data interchange (business) formats");
        descMap.put("finance"       , "financial data formats (SWIFT et al.)");
        descMap.put("general"       , "general purpose file formats");
        descMap.put("geo"           , "geopositioning data formats");
        descMap.put("grammar"       , "grammar/syntax description languages");
        descMap.put("image"         , "graphics and image file formats");
        descMap.put("image.raster"  , "raster image file formats");
        descMap.put("image.vector"  , "vector image file formats");
        descMap.put("misc"          , "miscellaneous file formats");
        descMap.put("net"           , "Internet standard (RFC) file formats");
        descMap.put("office"        , "file formats for office applications");
        descMap.put("office.data"   , "office table and spreadsheet applications");
        descMap.put("office.text"   , "office text processing applications");
        descMap.put("organizer"     , "organizer (PIM) file formats");
        descMap.put("parse"         , "transforming parser");
        descMap.put("proglang"      , "programming languages");
        descMap.put("pseudo"        , "pseudo files and filters");
    } // storeSubPackages

    /** Main program, writes package descriptions for all subpackages.
     *  The code is taken from web/documentation.jsp.
     *  @param args commandline arguments (none)
     */
    public static void main(String args[]) {
        MultiFormatFactory factory = new MultiFormatFactory();
        Iterator iter = factory.getIterator();
        String appName = "xtrans";
        String oldPackage = "";
        String packageName = "";
        try {
            storeSubPackages();
            PrintWriter out = null;
            iter.next(); // skip over element [0] which is null
            while (iter.hasNext()) {
                BaseTransformer trans = (BaseTransformer) iter.next();
                String name = trans.getClass().getName();
                int pos = name.indexOf(appName + ".");
                name = name.substring(pos + appName.length() + 1);
                pos = name.lastIndexOf(".");
                packageName = name.substring(0, pos);
                name = name.substring(pos + 1);
                String superName = trans.getClass().getSuperclass().getName();
                pos = superName.indexOf(appName + ".");
                superName = superName.substring(pos + appName.length() + 1, pos + appName.length() + 1 + 4);
                if (! packageName.equals(oldPackage)) {
                    if (out != null) {
                        out.println("</table></blockquote></body>");
                        out.println("</html>");
                        out.close();
                    }
                    out = new PrintWriter(new File("src/main/java/org/teherba/"
                            + appName + "/"
                            + packageName.replaceAll("\\.", "/")
                            + "/package.html"));
                    oldPackage = packageName;
                    String desc = descMap.get(packageName);
                    if (desc == null) {
                        desc = "different " + packageName + " file formats";
                    }
                    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"");
                    out.println("  \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
                    out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
                    out.println("<head><!-- generated by MultiFormatFactory.main --></head><body>");
                    out.println("<p>Converters between XML and " + desc + ".\n");
                    out.println("<blockquote><table cellspacing=\"1\" cellpadding=\"0\" summary=\"Description of the various formats\">");
                } // new packageName
                out.print("<tr><td><strong>-" + trans.getFirstFormatCode() + "</strong></td><td>");
                out.print(trans.getDescription());
                out.println("</td></tr>");
            } // while iter.hasNext()
            if (out != null) {
                out.println("</table></blockquote></body>");
                out.println("</html>");
                out.close();
            }
        } catch (Exception exc) {
            System.err.println("package name " + packageName);
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        } // try
    } // main

} // MultiFormatFactory
