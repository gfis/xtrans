<%--
	JSP include with common trailer for documentation pages
	@(#) $Id: seeAlso.inc 9 2008-09-05 05:21:15Z gfis $
	2008-06-01: Dr. Georg Fischer 
--%>
<%
	String root = "../";
	String docs = "./";
	if (",index,metaInf,xslTrans,".indexOf("," + callingPage + ",") >= 0) {
		root = "./";
		docs = "docs/";
	}
%>
<p>
	See also:
    <ul>
<% if (! callingPage.equals("index")) { %>
		<li><a href="<%= root %>index.jsp"><strong>xtrans</strong> main page</a></li>
<% } %>
<% if (! callingPage.equals("xslTrans")) { %>
		<li><a href="<%= root %>xslTrans.jsp"><strong>XSL</strong></a><strong> T</strong>ransformation Applications</li>
<% } %>
    	<li><strong><a href="<%= docs %>api/index.html">API</a></strong> javadocs</li>
<% if (! callingPage.equals("design")) { %>
		<li>General <a href="<%= docs %>design.jsp">design principles</a></li>
<% } %>
<%--
    	<li>Listings of the <a href="<%= docs %>xtrans.jar.lst">Jar</a> and 
                            <a href="<%= docs %>xtrans.war.lst">War</a> files</li>
--%>
<% if (! callingPage.equals("documentation")) { %>
		<li><a href="<%= docs %>documentation.jsp">Overview of Packages and Classes</a></li>
<% } %>
<% if (! callingPage.equals("developer")) { %>
		<li><a href="<%= docs %>developer.jsp">Developer Hints</a></li>
<% } %>
<% if (! callingPage.equals("bugs")) { %>
		<li><a href="<%= docs %>bugs.jsp">Drawbacks, Bugs and possible Improvements</a></li>
<% } %>
		<li>
			<a href="<%= root %>metaInf.jsp?view=manifest">Manifest</a>, 
			<a href="<%= root %>metaInf.jsp?view=license" >License</a>, 
			<a href="<%= root %>metaInf.jsp?view=notice"  >References</a>
		</li>
    </ul>
</p>
<p>
	Questions, remarks to: <a href="mailto:punctum@punctum.com">Dr. Georg Fischer</a>
</p>
