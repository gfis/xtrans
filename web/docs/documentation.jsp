<%--
    @(#) $Id: documentation.jsp 668 2011-04-06 06:43:17Z gfis $
    2011-04-06: MultiFormatFactory
    2007-04-12: error for iter element [0], which is null
    2007-02-14: refactored to org.teherba.xtrans
    2006-10-13: copied from numword
    uses Java Reflection
--%>
<%@page import="org.teherba.xtrans.BaseTransformer"%>
<%@page import="org.teherba.xtrans.MultiFormatFactory"%>
<%@page import="org.teherba.xtrans.XtransFactory"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.lang.Class"%>
<% response.setContentType("text/html; charset=UTF-8"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>xtrans Documentation</title>
    <link rel="stylesheet" type="text/css" href="../stylesheet.css" />
</head>
<body>
<h2>Overview</h2>
<p>
<a href="../index.jsp"><strong>xtrans</strong></a> 
transforms binary or text files to and from XML.
</p>

<h4>Implemented Formats</h4>
<table border="0" cellpadding="2" cellspacing="2" bgcolor="lavender">
  <tr>
    <td valign="top"><strong>Package</strong></td>
    <td valign="top"><strong>Format</strong></td>
    <td valign="top"><strong>Type</strong></td>
    <td valign="top"><strong>Class Name</strong></td>
    <td valign="top"><strong>Description</strong></td>
  </tr>
<%
    String CVSID = "@(#) $Id: documentation.jsp 668 2011-04-06 06:43:17Z gfis $";
    String appName = "xtrans";

	// keep the following code in sync with XtransFactory.main
    XtransFactory factory = new MultiFormatFactory();
    Iterator iter = factory.getIterator();
    String oldPackage = "";
    iter.next(); // skip over element [0] which is null
    while (iter.hasNext()) {
        BaseTransformer trans = (BaseTransformer) iter.next();  
        String name = trans.getClass().getName();
        int pos = name.indexOf(appName + ".");
        name = name.substring(pos + appName.length() + 1);
        pos = name.lastIndexOf(".");
        String packageName = name.substring(0, pos);
        name = name.substring(pos + 1);
        String superName = trans.getClass().getSuperclass().getName();
        pos = superName.indexOf(appName + ".");
        superName = superName.substring(pos + appName.length() + 1, pos + appName.length() + 1 + 4);
%>
  <tr>
    <td valign="top"><%= packageName.equals(oldPackage) ? "&nbsp;" : packageName %></td>
    <td valign="top"><strong><%= trans.getFirstFormatCode() %></strong></td>
    <td valign="top"><%= superName %></td>
    <td valign="top"><%= "<a href=\"" + "api/org/teherba/" 
                        + appName + "/" + packageName + "/" 
                        + name + ".html#skip-navbar_top\">" + name + "</a>" %></td>
    <td valign="top"><%= trans.getDescription() %></td>
  </tr>
<%
        oldPackage = packageName;
    } // while iter.hasNext()
%>
</table>
	<% String callingPage = "documentation"; %>
   	<%@include file="../seeAlso.inc" %>
</body>
</html>
