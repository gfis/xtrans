<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- 
	@(#) $Id: bugs.jsp 9 2008-09-05 05:21:15Z gfis $
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
    <title>Bugs</title>
    <link rel="stylesheet" type="text/css" href="stylesheet.css">
</head>
<body>
    <h2>
		<a href="../index.jsp"><strong>xtrans</strong></a> 
    	Drawbacks, Bugs and possible Improvements
    </h2>
    <table>
    <tr valign="top"><td>General</td>
        <td><ul>
            <li>Though most transformers convert from the raw (specific) format
            to an XMLized representation, there are a few exceptions
            where general binary or text files are converted to the specific format
            which is then wrapped into XML. Examples are Base64, Quoted Prinatble and Morse Code.
            </li>
            <li>
            Most transformers store values in XML elements, but sometimes
            it seemed easier to store them in attributes of elements.
            DTA and Datev are examples for the latter case. 
            </li>
            <li>
            For formats with many different tags (SWIFT for example) 
            the question arises whether
            such tags are syntax or data. These tags can be converted to
            id attributes of a generalized XML "field" element, or a seperate
            element for each such tag can be generated. The SwiftTransformer
            made the latter decision.
            </li>
            </ul></td></tr>
    <tr valign="top"><td>Test</td>
        <td><ul>
            <li>For all formats there are only a few test cases.
            </li>
            </ul></td></tr>
    <tr valign="top"><td>Incomplete<br />Transformers</td>
        <td><ul>
            <li>general.XMLTransformer - insufficient serialization of entities; serializer should be replaced by Apaches's
            <li>general.CountingTransformer - cannot generate, but serializes any XML to a sorted list
                with counts for all elements, and the accumulated length of their direct character content
            </li>
            <li>net.URITransformer - the set of supported schemas is incomplete, and serializing is not implemented.
            </li>
            <li>organizer.LDIFTransformer - not well tested, and serializing is not implemented.
            </li>
            </ul></td></tr>
    </table>
	<% String callingPage = "bugs"; %>
	<%@include file="../seeAlso.inc" %>
</body>
</html>
