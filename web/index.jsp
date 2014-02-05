<?xml version="1.0" encoding="UTF-8"?>
<%--
    Main form for application 'xtrans'
    @(#) $Id: index.jsp 668 2011-04-06 06:43:17Z gfis $
    2011-04-06: MultiFormatFactory
    2008-07-30: svn tests
    2008-05-31: with field 'view'
    2006-10-13: copied from numword
    caution, must be UTF-8 encoded: äöüÄÖÜß 
--%>
<%--
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
--%>
<%@page import="org.teherba.xtrans.BaseTransformer"%>
<%@page import="org.teherba.xtrans.MultiFormatFactory"%> 
<%@page import="org.teherba.xtrans.XtransFactory"%> 
<%@page import="java.util.Iterator"%>
<% response.setContentType("text/html; charset=UTF-8"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="stylesheet.css" />
    <title>xtrans - XML Transformation</title>
    <script type="text/javascript">
    </script>
</head>
<%
    String CVSID = "@(#) $Id: index.jsp 668 2011-04-06 06:43:17Z gfis $";
    XtransFactory factory = new MultiFormatFactory();
    String[] optDir     = new String [] 
            { "to"          // 0
            , "from"        // 1
            } ;
    String[] enDir      = new String [] 
            { "to"          // 0
            , "from"        // 1
            } ;
    String[] optTarget  = new String [] 
            { "xml"         // 0
            } ;
    String[] enTarget   = new String [] 
            { "XML"         // 0
            } ;
    String[] optEnc     = new String [] 
            { "UTF-8"       // 0
            , "ISO-8859-1"  // 1
            } ;
    String[] enEnc      = new String [] 
            { "UTF-8"       // 0
            , "ISO-8859-1"  // 1
            } ;
    Object 
    field = session.getAttribute("format"   );  String format       = (field != null) ? (String) field : "line";
    field = session.getAttribute("target"   );  String target       = (field != null) ? (String) field : "xml";
    field = session.getAttribute("dir"      );  String dir          = (field != null) ? (String) field : "to";
    field = session.getAttribute("enc1"     );  String enc1         = (field != null) ? (String) field : "UTF-8";
    field = session.getAttribute("enc2"     );  String enc2         = (field != null) ? (String) field : "UTF-8";
    field = session.getAttribute("nsp"      );  String namespace    = (field != null) ? (String) field : "";
    field = session.getAttribute("opt"      );  String options      = (field != null) ? (String) field : "";
    field = session.getAttribute("infile"   );  String infile       = (field != null) ? (String) field : "";
    field = session.getAttribute("intext"   );  String intext       = (field != null) ? (String) field : "";
    int index = 0;
    String border = "0";
%>
<body>
    <!--
    dir="<%= dir %>", format="<%= format %>", target="<%= target %>", namespace="<%= namespace %>", options="<%= options %>" 
    -->
    
    <h2>xtrans - Format Transformation to/from XML</h2>
    <form action="xtransservlet" method="post" enctype="multipart/form-data">
        <input type = "hidden" name="view" value="index" />
        <table cellpadding="8" border="<%= border %>">
            <tr valign="top">
                <td rowspan="2"><strong>Format</strong><br />
                    <select name="format" size="<%= factory.getCount() %>">
                    <%
                        Iterator iter = factory.getIterator();
                        while (iter.hasNext()) {
                            BaseTransformer transformer = (BaseTransformer) iter.next();
                            String code = transformer.getFirstFormatCode();
                            out.write("<option value=\"" + code + "\""
                                    + (code.equals(format) ? " selected" : "")
                                    + ">"
                                    + code + " - " + transformer.getDescription() + "</option>\n");
                        } // while iter
                    %>
                    </select>
                </td>
                <td><br />
                    <table border="<%= border %>">
                        <tr>
                        <td>
                            <table border="<%= border %>">
                                <tr>
                                    <td>
                                        <select name="dir" size="<%= optDir.length %>">
                                        <%
                                            index = 0;
                                            while (index < optDir.length) {
                                                out.write("<option value=\"" 
                                                        + optDir[index] + "\""
                                                        + (optDir[index].equals(dir) ? " selected" : "")
                                                        + ">"
                                                        + enDir[index] + "</option>\n");
                                                index ++;
                                            } // while index
                                        %>
                                        </select>
                                    </td>
                                    <td>    
                                        <select name="target" size="<%= optTarget.length %>">
                                        <%
                                            index = 0;
                                            while (index < optTarget.length) {
                                                out.write("<option value=\"" 
                                                        + optTarget[index] + "\""
                                                        + (optTarget[index].equals(target) ? " selected" : "")
                                                        + ">"
                                                        + enTarget[index] + "</option>\n");
                                                index ++;
                                            } // while index
                                        %>
                                        </select>
                                    </td>
                                    <td>    
										&nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3">
                                        Options<br />
                                        <input name="opt" maxsize="80" size="40" value="<%= options %>" />
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3">
                                        Namespace<br /> 
                                        <input name="nsp" maxsize="20" size="40" value="<%= namespace %>" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>Source Encoding<br /> 
                                        <select name="enc1" size="<%= optEnc.length %>">
                                        <%
                                            index = 0;
                                            while (index < optEnc.length) {
                                                out.write("<option value=\"" 
                                                        + optEnc[index] + "\""
                                                        + (optEnc[index].equals(enc1) ? " selected" : "")
                                                        + ">"
                                                        + enEnc[index] + "</option>\n");
                                                index ++;
                                            } // while index
                                        %>
                                        </select>
                                    </td>
                                    <td>Target Encoding<br /> 
                                        <select name="enc2" size="<%= optEnc.length %>">
                                        <%
                                            index = 0;
                                            while (index < optEnc.length) {
                                                out.write("<option value=\"" 
                                                        + optEnc[index] + "\""
                                                        + (optEnc[index].equals(enc2) ? " selected" : "")
                                                        + ">"
                                                        + enEnc[index] + "</option>\n");
                                                index ++;
                                            } // while index
                                        %>
                                        </select>
                                    </td>
                                    <td>    
										&nbsp;
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td>
                            <p />
                            <% String callingPage = "index"; %>
                            <%@include file="seeAlso.inc" %>
                        </td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <table border="<%= border %>">
                                    <tr>
                                        <td>
                                        Name of Input File<br />
                                        <input name="infile" type="file" style="font-family: Courier, monospace" 
                                                maxsize="512" size="80" value="<%= infile %>"/> 
                                        &nbsp;&nbsp;<em><strong>or</strong></em>
                                        <br />Input source text (non-binary formats only)<br />
                                        <textarea name="intext" cols="80" rows="10"><%= intext %></textarea>
                                        <br /><input type="submit" value="Submit" />
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
            
                </td>
            </tr>
        </table><!-- main layout -->
    </form>
</body>
</html>
