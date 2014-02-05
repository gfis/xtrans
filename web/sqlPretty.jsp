<?xml version="1.0" encoding="UTF-8"?>
<%--
	Parameters for one XSLT transformation
    @(#) $Id: sqlPretty.jsp 670 2011-04-06 16:42:46Z gfis $
	2011-04-06: no BaseTransformer
    2008-09-23: omit empty lines    
    2008-06-02: Georg Fischer
	caution, must be UTF-8 encoded: äöüÄÖÜß 
--%>
<%@page import="java.util.Iterator"%>
<% response.setContentType("text/html; charset=UTF-8"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--
 * Copyright 2008 Dr. Georg Fischer <punctum at punctum dot kom>
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
    <link rel="stylesheet" type="text/css" href="stylesheet.css" />
    <title>SQL-Formatierer</title>
	<script type="text/javascript">
	</script>
</head>
<%
    String CVSID = "@(#) $Id: sqlPretty.jsp 670 2011-04-06 16:42:46Z gfis $";
    String[] optPipeLine = new String [] // keep in sync with following String array
            { "" // this is skipped below
            , "- -sql -filter sqlpretty -sql -"
            } ;
     String[] enPipeLine = new String [] 
            { "" // this is skipped below
            , "MT940 -> camt.052"
            } ;
    String[] optEnc = new String [] 
            { "ISO-8859-1"  // 0
            , "UTF-8"       // 1
            } ;
    String[] enEnc = new String [] 
            { "ISO-8859-1"  // 0
            , "UTF-8"       // 1
            } ;
    Object 
    field = session.getAttribute("pipeLine" );  String pipeLine     = (field != null && ! field.equals("")) ? (String) field : optPipeLine[1];
    field = session.getAttribute("enc1"     );  String enc1         = (field != null && ! field.equals("")) ? (String) field : optEnc[0];
    field = session.getAttribute("enc2"     );  String enc2         = (field != null && ! field.equals("")) ? (String) field : optEnc[0];
    field = session.getAttribute("nsp"      );  String namespace    = (field != null) ? (String) field : "";
    field = session.getAttribute("opt"      );  String options      = (field != null) ? (String) field : "";
    field = session.getAttribute("infile"   );  String infile       = (field != null) ? (String) field : "";
    field = session.getAttribute("intext"   );  String intext       = (field != null) ? (String) field : "";
    int index = 0;
    String border = "0";
%>
<body>
    <!--
    pipeLine="<%= pipeLine %>", namespace="<%= namespace %>", options="<%= options %>" 
    -->
    
    <h2>xtrans SQL-Formatierer</h2>
    <form action="xtransservlet" method="post" enctype="multipart/form-data">
        <input type = "hidden" name="view" value="sqlPretty" />
        <input type = "hidden" name="pipeLine" value="<%= optPipeLine[1] %>" />
        <input type = "hidden" name="enc2" value="<%= enc1 %>" />
        <table cellpadding="8" border="<%= border %>">
            <tr valign="top">
                <td>
                    <table border="<%= border %>">
                    	<tr>
						<td>
		                    <table border="<%= border %>">
								<tr>
									<td colspan="3">
	    	    	            		Optionen<br />
	        		            		<input name="opt" maxsize="80" size="40" value="<%= options %>" />
	        	    	    	    </td>
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
	    	                	</tr>
	    	            	</table>
    	    	        </td>
        		    	</tr>
        		    	<tr>
        		    		<td colspan="3">
			                    <table border="<%= border %>">
			                    	<tr>
			                    		<td>
					                    Eingabedatei<br />
        		    			        <input name="infile" type="file" style="font-family: Courier, monospace" 
                		 		        		maxsize="512" size="80" value="<%= infile %>"/> 
                		 		        &nbsp;&nbsp;<em><strong>oder</strong></em>
					                    <br />Quelltext<br />
					                    <textarea name="intext" cols="80" rows="10"><%= intext %></textarea>
                   						<br /><input type="submit" value="Formatieren" />
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
