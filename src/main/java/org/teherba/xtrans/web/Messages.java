/*  Messages.java - Static help texts and other language specific messages for Xtrans
 *  @(#) $Id: 57d01d0860aef0c2f2783647be70c3c381710c86 $
 *  2016-10-14: views alll lowercase
 *  2016-09-07: Dr. Georg Fischer: copied from Xtool
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
import  org.teherba.common.web.BasePage;
import  java.io.Serializable;

/** Language specific message texts and formatting for Xtrans's user interface.
 *  Apart from the language specific processing is found in the JSPs (in dbat/web),
 *  all internationalization of the Java source code is assembled in this class.
 *  Currently implemented natural languages (denoted by 2-letter ISO <em>country</em> codes) are:
 *  <ul>
 *  <li>en - English</li>
 *  <li>de - German</li>
 *  </ul>
 *  <p />
 *  All methods in this class are not stateful, and therefore are
 *  <em>static</em> for easier activation.
 *  @author Dr. Georg Fischer
 */
public class Messages implements Serializable {
    public final static String CVSID = "@(#) $Id: 57d01d0860aef0c2f2783647be70c3c381710c86 $";

    /** No-args Constructor
     */
    public Messages() {
    } // Constructor
    
    /** Sets the application-specific error message texts
     *  @param basePage reference to the hash for message texts
     */
    public static void addMessageTexts(BasePage basePage) {
        String appLink = "<a href=\"servlet?view=index\">" + basePage.getAppName() + "</a>";
        //--------
        basePage.add("en", "001", appLink);
        //--------
        String laux = basePage.LANG_AUX;  // pseudo language code for links to auxiliary information
        int imess   = basePage.START_AUX; // start of messages    for links to auxiliary information
        String
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"main\"        href=\"servlet?view=index\">");
        basePage.add("en", smess, "{parm}Xtrans</a> Main Page");
        basePage.add("de", smess, "{parm}Xtrans</a>-Startseite");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"xsltrans\"    href=\"servlet?view=xsltrans\">");
        basePage.add("en", smess, "{parm}XSL</a> Transformation Pipeline");
        basePage.add("de", smess, "{parm}Xtrans</a>-Transformationskette");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"packlist\"    href=\"servlet?view=packlist\">");
        basePage.add("en", smess, "{parm}List of Transformers</a>");
        basePage.add("de", smess, "{parm}Liste der Transformer</a>");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"wiki\"        href=\"http://www.teherba.org/index.php/Xtrans\" target=\"_new\">");
        basePage.add("en", smess, "{parm}Wiki</a> Documentation");
        basePage.add("de", smess, "{parm}Wiki</a>-Dokumentation");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"github\"      href=\"https://github.com/gfis/xtrans\" target=\"_new\">");
        basePage.add("en", smess, "{parm}Git Repository</a>");
        basePage.add("de", smess, "{parm}Git Repository</a>");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"api\"         href=\"docs/api/index.html\">");
        basePage.add("en", smess, "{parm}Java API</a> Documentation");
        basePage.add("de", smess, "{parm}Java API</a>-Dokumentation");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"manifest\"    href=\"servlet?view=manifest\">");
        basePage.add("en", smess, "{parm}Manifest</a>");
        basePage.add("de", smess, "{parm}Manifest</a>");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"license\"     href=\"servlet?view=license\">");
        basePage.add("en", smess, "{parm}License</a>");
        basePage.add("de", smess, "{parm}Lizenz</a>");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"notice\"      href=\"servlet?view=notice\">");
        basePage.add("en", smess, "{parm}References</a>");
        basePage.add("de", smess, "{parm}Referenzen</a>");
        //--------
        basePage.add("en", "401", "Invalid {parm} <em>{par2}</em>");
        basePage.add("en", "407", "Either input file or text area must be specified");
        //--------
    } // addMessageTexts

    //================
    // Main method
    //================

    /** Test driver 
     *  @param args language code: "en", "de"
     */
    public static void main(String[] args) {
        Messages help = new Messages();
        System.out.println("no messages");
    } // main

} // Messages
