<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- 
	@(#) $Id: design.jsp 9 2008-09-05 05:21:15Z gfis $
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
    <link rel="stylesheet" type="text/css" href="stylesheet.css">
	<title>Package xtrans</title>
</head>
<body>
<p>
<a href="../index.jsp"><strong>xtrans</strong></a>
is a collection of classes which 
transform binary files (consisting of 8-bit bytes) 
or text files (strings of encoded characters) 
to XML and vice versa.
</p>
<p> 
The package is a bridge from the Java and XML world to 
legacy file formats with 
<ul>
<li>binary, packed decimal or EBCDIC fields, </li>
<li>column oriented record structures,</li>
<li>arrays of fields,</li>
<li>record type indicators and overlaying variants of subrecords,</li>
<li>length fields and so on.</li>
</ul>
</p>
<h4>Applications
</h4>
<p>
The package is a building block for:
<ul>
<li>analysis, checking and extraction programs,</li>
<li>manipulation of files in one format,</li>
<li>general (file) converters between different formats.</li>
</ul>
</p><p>
The package abstracts from the lexical specialities of a format.
It facilitates syntactical analyses and transformations since these tasks
can all be done in a uniform way in the XML world, 
normally by one or more XSLT stylesheets.
</p><p>
The m * n complexity of converting from
m different input formats to n output formats is reduced to m + n 
by the attempt to introduce
an intermediate format, in this case XML (eXtensible Markup Language).
</p><p>
The transformers in this package convert between a specific 
format and XML. Ideally the conversion of a file to XML
and back to the specific format reproduces the original file byte for byte.
Sometimes, there are minor, irrelevant differences like
spacing, suppression of comments etc.
</p><p>
The design of the whole package is such that the generated XML is as
close to the specific format as possible. This may seem unneccessary
when converting to XML, but is very helpful when converting from
XML to the specific format. In general, the sequential order of the
XML elements is essential for the correct serialization to the specific 
format. 
</p><p>
In order to solve the general conversion problem, applications
must still understand the whole syntactical (BUT NOT lexical) structure
AND the semantics
of the source and of the target format.
</p>
<h4>Records
</h4>
<p> 
Binary files are processed with the <em>Byte&lt;name&gt;</em> series of classes, while
text files are processed with the <em>Char&lt;name&gt;</em> series of classes. 
</p><p> 
The two classes <a href="ByteRecord.html">ByteRecord</a> and <a href="CharRecord.html">CharRecord</a>
have an underlying buffer and define 
<ul>
<li>a record with <a href="Field.html">Field</a>s of 
fixed length, name, and some starting position in the record, </li>
<li>access methods (setters and getters) for these fields,</li>
<li>EBCDIC and packed decimal conversion in the case of <a href="ByteRecord.html">ByteRecord</a>,
and all Java defined character encodings in the case of <a href="CharRecord.html">CharRecord</a>,
<li>methods for reading and writing such a record from/to a file, 
<li>methods for XML encoding of a record and corresponding parsing.</li>
</ul>
These two classes were developped mainly for the processing of records in
conventional (legacy) file formats.
<a href="RecordBase.html">RecordBase</a> is the abstract superclass for both.
</p><p> 
As a convenience. there is a third class <a href="BeanRecord.html">BeanRecord</a>
which has no underlying record buffer,
and which allows to define fields (properties) with only a few standard Java types: 
<tt>long, String, java.sql.Date</tt> and <tt>java.sql.Timestamp</tt>. The access
and XML conversion methods are similiar to those of the two record classes based on buffers.
</p><p> 
The four record classes mentioned above share much common code, and are therefore generated
from a file <em>Records.txt</em> 
with the standalone program <a href="LineSplitter.html">LineSplitter</a>.
</p>
<h4>Transformers
</h4>
<p> 
The transformation to and from XML is handled by specific classes derived from
<a href="ByteTransformer.html">ByteTransformer</a>
and <a href="CharTransformer.html">CharTransformer</a> respectively,
which are both derived from <a href="BaseTransformer.html">BaseTransformer</a>.
The main methods in these classes are 
<ul>
<li><a href="BaseTransformer.html#generate()">generate()</a>
which creates an XML file from the foreign file format, and </li>
<li><a href="BaseTransformer.html#serialize()">serialize()</a>
which creates the foreign file format back from an XML file.</li>
</ul>
The latter method uses a standard SAX parser.
</p><p> 
Implementors are encouraged to define both transformation directions wherever
possible, and to test the identical reproduction of the foreign file format
from  XML instances.
</p>
<h4>XML Record Specification
</h4>
<p> 
The <em>record</em> and <em>transformer</em> classes can be used independantly from
each other, but for legacy file formats from z/OS and COBOL, they are normally
combined. Abstraction is further raised by the possibility to define a record
via an XML description. In this case, the specific Java record class derived from 
ByteRecord or CharRecord is completely generated with the aid of the 
XSLT stylesheet <a href="/xtrans/xslt/genBean.xsl">genBean.xsl</a> found in the <tt>etc/xslt</tt> 
subdirectory of the source distribution. The <tt>etc/spec</tt>
contains examples for XML record definitions, among them one for 
the <a href="/xtrans/spec/finance/DTA.spec.xml">German DTA format</a>.
</p><p> 
The XML specification allows to define:
<ul>
<li>general attributes of the record like name, type (Byte, Char, or Bean),
</li>
<li>variants of overlaying subrecords,</li>
<li>arrays of fields, and</li>
<li>fields with name, length, type (num, decimal, ebcdic, string etc.), remark, and optional
offset in the record buffer.
</li>
</ul>
</p><p>
XML generation from the record's fields and SAX parsing is already predefined
in the generated record class. The transformer must still be written manually,
but apart from a very simple standard pattern it will only contain specific operations
like file handling or checksum computation, for example.
</p> 
<h4>SQL CREATE
</h4>
<p> 
For simple tabular record specification there is a possiblity to generate 
an SQL CREATE statement with the <a href="/xtrans/xslt/genBean.xsl">genBean.xsl</a> 
stylesheet also found in <tt>etc/spec</tt>.
</p>
<% String callingPage = "design"; %>
<%@include file="../seeAlso.inc" %>
</body>
</html>
