/*  PackageListPage.java - main web page for Xtrans
 *  @(#) $Id: 57d01d0860aef0c2f2783647be70c3c381710c86 $
 *  2017-05-28: javadoc 1.8
 *  2016-10-13: less imports; skip over [0] = XMLTransformer
 *  2016-09-14: MultiFormatFactory back to dynamic XtransFactory
 *  2016-09-06: without session
 *  2016-08-28: Dr. Georg Fischer: copied from Dbat
 *  2011-04-06: MultiFormatFactory
 *  2008-07-30: svn tests
 *  2008-05-31: with field 'view'
 *  2006-10-13: copied from numword
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
package org.teherba.xtrans.web;
import  org.teherba.xtrans.BaseTransformer;
import  org.teherba.xtrans.XtransFactory;
import  org.teherba.common.web.BasePage;
import  java.lang.Class;
import  java.io.IOException;
import  java.io.PrintWriter;
import  java.io.Serializable;
import  java.util.Iterator;
import  javax.servlet.http.HttpServletRequest;
import  javax.servlet.http.HttpServletResponse;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** RaMath main dialog page
 *  @author Dr. Georg Fischer
 */
public class PackageListPage implements Serializable {
    public final static String CVSID = "@(#) $Id: 57d01d0860aef0c2f2783647be70c3c381710c86 $";
    public final static long serialVersionUID = 19470629;

    /** log4j logger (category) */
    private Logger log;

    /** No-args Constructor
     */
    public PackageListPage() {
        log      = LogManager.getLogger(PackageListPage.class.getName());
    } // Constructor

    /** Output the main dialog page for Xtrans
     *  @param request request with header fields
     *  @param response response with writer
     *  @param basePage refrence to common methods and error messages
     *  @param language 2-letter code en, de etc.
     *  @throws IOException if an IO error occurs
     */
    public void showList(HttpServletRequest request, HttpServletResponse response
            , BasePage basePage
            , String language
            ) throws IOException {
        if (true) { // try {
            PrintWriter out = basePage.writeHeader(request, response, language);
            out.write("<title>" + basePage.getAppName() + " Main Page</title>\n");
            out.write("</head>\n<body>\n");

            out.write("<h2>Overview</h2>\n");
            out.write("<p>\n");
            out.write("<a href=\"servlet?view=index\"><strong>Xtrans</strong></a> ");
            out.write("transforms binary or text files to and from XML.");
            out.write("</p>\n");
            out.write("<h4>Implemented Formats</h4>\n");
            out.write("<table border=\"0\" cellpadding=\"2\" cellspacing=\"2\" bgcolor=\"lavender\">\n");
            out.write("  <tr>\n");
            out.write("    <td valign=\"top\"><strong>Package</strong></td>\n");
            out.write("    <td valign=\"top\"><strong>Format</strong></td>\n");
            out.write("    <td valign=\"top\"><strong>Type</strong></td>\n");
            out.write("    <td valign=\"top\"><strong>Class Name</strong></td>\n");
            out.write("    <td valign=\"top\"><strong>Description</strong></td>\n");
            out.write("  </tr>\n");

            String appName = basePage.getAppName().toLowerCase();
            XtransFactory factory = new XtransFactory();
            Iterator<BaseTransformer> iter = factory.getIterator();
            String oldPackage = "";
            iter.next(); // skip over element [0] which is XMLTransformer (outside any package)
            while (iter.hasNext()) {
                BaseTransformer trans = iter.next();
                String name = trans.getClass().getName();
                // construct http://localhost:8080/xtrans/docs/api/org/teherba/xtrans/proglang/CSSTransformer.html
                //           http://localhost:8080/xtrans/api/org/teherba/Xtrans/herba.xtrans.proglang/CSSTransformer.html#skip-navbar_top
                int pos = name.indexOf(appName + ".");
                name = name.substring(pos + appName.length() + 1);
                pos  = name.lastIndexOf(".");
                String packageName = name.substring(0, pos);
                name = name.substring(pos + 1);
                out.write("  <tr>\n");
                out.write("    <td valign=\"top\">" + (packageName.equals(oldPackage) ? "&nbsp;" : packageName ) + "</td>\n"); // package
                out.write("    <td valign=\"top\"><strong>" + trans.getFirstFormatCode() + "</strong></td>\n"); // format code
                out.write("    <td valign=\"top\">" + (trans.isBinaryFormat() ? "byte" : "char")  + "</td>\n"); // type
                out.write("    <td valign=\"top\">" + "<a href=\"" + "docs/api/org/teherba/"
                                  + appName + "/" + packageName + "/"
                                  + name + ".html#skip-navbar_top\">" + name + "</a>"  + "</td>\n"); // class name
                out.write("    <td valign=\"top\">" + trans.getDescription() + "</td>\n"); // description
                out.write("  </tr>\n");
                oldPackage = packageName;
            } // while iter.hasNext()
            out.write("</table>\n");
            basePage.writeAuxiliaryLinks(language, "packlist");
            basePage.writeTrailer(language, "quest");
    /*
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
    */
        }
    } // showList

    //================
    // Main method
    //================

    /** Test driver
     *  @param args language code: "en", "de"
     */
    public static void main(String[] args) {
        PackageListPage help = new PackageListPage();
        System.out.println("no messages");
    } // main

} // PackageListPage
