<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- 
	@(#) $Id: developer.jsp 9 2008-09-05 05:21:15Z gfis $
	caution, must be UTF-8 encoded: äöüÄÖÜß 
-->
<!--
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
-->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Hints for Developers</title>
    <link rel="stylesheet" type="text/css" href="stylesheet.css">
</head>

<body>
    <h2>Hints for Developers</h2>
    <p>
		<a href="../index.jsp"><strong>xtrans</strong></a> 
    	currently processes only a limited set of formats. 
    </p>
    <p>
    You are encouraged to:
    <ul>
    <li>play with the format transformer classes,</li>
    <li><a href="mailto:punctum@punctum.com">email</a> any suggestions for improvement,</li>
    <li>contribute patches for corrections,</li>
    <li>contribute new transformer classes.</li>
    </ul>
    </p>
    <h4>Coding conventions</h4>
    <p>
    Please try to remain close to the current programming style:
    <ul>
    <li>Write Javadoc comments before all methods and public members.</li>
    <li>Note that the Java sources are compiled with UTF-8 source encoding:
<pre>
    &lt;javac  srcdir="${src.home}" destdir="${build.classes}" listfiles="yes"
            encoding="utf8"
            source="1.4" target="1.4"
            debug="${javac.debug}" debuglevel="${javac.debuglevel}"&gt;
</pre>
    Determine the proper accents and non-ASCII characters, and write them
    in Unicode in the Java source files. 
    Use an Unicode enabled editor that handles 
    UTF-8 properly; write some Unicode characters in the header comment 
    such that the editor can detect the UTF-8 encoding.</li>
    <li>Use reliable sources for the format definition like 
    <a href="http://tools.ietf.org/html">RFCs</a> or ISO standards,
    and document them in the Javadoc header of the class.
    </li>
    </ul>
    </p>
    <h4>Reversibility</h4>
    <p>
    The transformers should try to serialize XML to exactly the same 
    specific format
    from which they are able to generate XML. The <em>test</em> Ant targets
    perform a "generate - serialize - binary compare" sequence to check the
    reversibility of the transformation. 
    </p><p>
    Some formats don't have a well-defined canonical representation. 
    In JCL, for example, the line breaks and the spaces for field separation
    are lost in the XML representation, and cannot exactly be reproduced
    by the serializer. In these cases, subsequent "generate - serialize" sequences
    should finally produce an identical result.
    </p>
    <h4>Future Extensions</h4>
    <p>
    <ul>
    	<li>more text processing formats:
    		<ul>
    			<li>(La)TeX - similiar to RTF</li>
    			<li>dot instruction oriented formats: IBM DCF, nroff, troff, perldoc</li>
    			<li>binary formats like IBM DCA/RFT, Siemens Hit, WordPerfect</li>
    			<li>common tagset for text processing features</li>
    		</ul>
    	</li>
    	<li>raster image processing formats:
    		<ul>
    			<li>TIFF</li>
    			<li>EXIF - at least the header</li>
    			<li>GIF, BMP etc.</li>
    		</ul>
    	</li>
    	<li>vector image processing formats with target SVG:
    		<ul>
    			<li>WMF</li>
    			<li>Flash?</li>
    			<li>RTF DO, AmiPro, WordPerfect Graphics ...</li>
    		</ul>
    	</li>
    	<li>ZIP file tree pseudo transformer</li>
    </ul>
	<% String callingPage = "developer"; %>
   	<%@include file="../seeAlso.inc" %>
</body>
</html>
