/*  XslTransPage.java - main web page for Xtrans
 *  @(#) $Id: 57d01d0860aef0c2f2783647be70c3c381710c86 $
 *  2016-09-07, Dr. Georg Fischer: adopted from xslTrans.jsp
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
import  java.io.PrintWriter;
import  java.io.Serializable;
import  java.util.Iterator;
import  javax.servlet.http.HttpServletRequest;
import  javax.servlet.http.HttpServletResponse;
import  javax.servlet.http.HttpSession;
import  org.apache.log4j.Logger;

/** RaMath main dialog page
 *  @author Dr. Georg Fischer
 */
public class XslTransPage implements Serializable {
    public final static String CVSID = "@(#) $Id: 57d01d0860aef0c2f2783647be70c3c381710c86 $";
    public final static long serialVersionUID = 19470629;

    /** log4j logger (category) */
    private Logger log;

    /** No-args Constructor
     */
    public XslTransPage() {
        log      = Logger.getLogger(XslTransPage.class.getName());
    } // Constructor

    /** Output the main dialog page for Xtrans
     *  @param request request with header fields
     *  @param response response with writer
     *  @param basePage refrence to common methods and error messages
     *  @param language 2-letter code en, de etc.
     */
    public void dialog(HttpServletRequest request, HttpServletResponse response
            , BasePage basePage
            , String language
            , String pipeLine
            , String options
            , String namespace
            , String enc1
            , String enc2
            , String infile
            , String intext
            ) {
        try {
            PrintWriter out = basePage.writeHeader(request, response, language);
            out.write("<title>" + basePage.getAppName() + " Main Page</title>\n");
            out.write("</head>\n<body>\n");
            String[] optPipeLine = new String [] // keep in sync with following String array
                    { "" // this is skipped below
                    , "- -mt940 -xsl finance/mt940-camt.052.xsl -xml -"
                    } ;
            String[] enPipeLine = new String []
                    { "" // this is skipped below
                    , "MT940 -> camt.052"
                    } ;
            String border = "0";
            int index = 0;
            out.write("<!-- pipeLine=\"" + pipeLine + "\", namespace=\"" + namespace + "\", options=\"" + options +" -->\n");
            out.write("<h2>xtrans XSLT Pipeline Applications</h2>\n");
            out.write("<form action=\"servlet\" method=\"post\" enctype=\"multipart/form-data\">\n");
            out.write("    <input type = \"hidden\" name=\"view\" value=\"xslTrans2\" />\n");
            out.write("    <table cellpadding=\"8\" border=\"" + border + "\">\n");
            out.write("        <tr valign=\"top\">\n");
            out.write("            <td rowspan=\"2\"><strong>Pipeline Application</strong><br />\n");
            out.write("                <select name=\"pipeLine\" size=\"" + optPipeLine.length + "\">\n");
                                       index = 1; // skip dummy entry [0]
                                       while (index < optPipeLine.length) {
                                           out.write("<option value=\""
                                                   + optPipeLine[index] + "\""
                                                   + (optPipeLine[index].equals(pipeLine) ? " selected" : "")
                                                   + ">"
                                                   + enPipeLine[index] + "</option>\n");
                                           index ++;
                                       } // while index
            out.write("                </select>\n");
            out.write("            </td>\n");

            out.write("            <td>\n");
                                       IndexPage.writeFormOptions(basePage, out, language, ""
                                               , options, namespace, enc1, enc2, infile, intext);
            out.write("            </td>\n");
            out.write("        </tr>\n");
            out.write("    </table><!-- main layout -->\n");
            out.write("</form>\n");

            basePage.writeAuxiliaryLinks(language, "xsltrans");
            basePage.writeTrailer(language, "quest");
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // dialog

    //================
    // Main method
    //================

    /** Test driver
     *  @param args language code: "en", "de"
     */
    public static void main(String[] args) {
        XslTransPage help = new XslTransPage();
        System.out.println("no messages");
    } // main

} // XslTransPage
