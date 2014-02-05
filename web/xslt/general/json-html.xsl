<?xml version="1.0"  encoding="UTF-8"?>
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
<!--
    Transforms unified "field" elements with "id=" attributes
    to different elements with names concatenated from "F" and the "id=" attribute.
    @(#) $Id: json-html.xsl 9 2008-09-05 05:21:15Z gfis $
    2006-10-04, Dr. Georg Fischer
	
	The grammar is as follows:
	 *  <pre>
	 *  json    = object | array | string | number | token.
	 *  object  = "{" node/"," "}".
	 *	node    = key ":" value.
	 *  key     = string.
	 *	value   = object | array | string | number | token.
	 *  array   = "[" element/"," "]".
	 *	element = object | array | string | number | token
	 *  token   = "true" | "false" | "null".
	 *  </pre>
-->
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes" />
    
    <xsl:template match="json">
    	<html>
    		<head>
				<style>
					<xsl:text>
					.tba { color:black ; background-color: moccasin  ; 
						 ; border-width:1 ; border-style:solid ; border-color:black ; margin:2 }
					.tbo { color:black ; background-color: palegreen 
						 ; border-width:1 ; border-style:solid ; border-color:black ; margin:2 }
					.ola { vertical-align:top ; list-style-type:decimal     } /* IE6 ignores top */
					.olo { vertical-align:top ; list-style-type:lower-alpha }
					.tkw { color:black ; background-color: lavender
					     ; font-family:'Lucida Console',Courier,monospace ; font-weight:bold }
					</xsl:text>
				</style>
    		</head>
    		<body style="font-family:Arial,Helvetica,sans-serif">
       			<xsl:apply-templates select="obj|arr|str|num|null|true|false" />
        	</body>
		</html>
		<!-- json -->
    </xsl:template>

    <xsl:template match="obj">
    	<table class="tbo">
    		<tr>
    			<td>
    				<ol class="olo">
       					<xsl:apply-templates select="node" />
					</ol>
				</td>
			</tr>
		</table>
		<!-- obj -->
    </xsl:template>

    <xsl:template match="node">
		<li>
    		<strong>
        		<xsl:apply-templates select="key" />
		        <xsl:value-of select="' : '" />
	        </strong>
       		<xsl:apply-templates select="val|arr|obj" />
       	</li>
		<!-- node -->
    </xsl:template>

    <xsl:template match="val">
 		<xsl:apply-templates select="obj|arr|str|num|true|false|null" />
    </xsl:template>

    <xsl:template match="str">
    	<span class="tkw"><xsl:value-of select="string()" /></span>
    </xsl:template>

    <xsl:template match="num">
    	<span class="tkw"><xsl:value-of select="string()" /></span>
    </xsl:template>

    <xsl:template match="null|true|false">
    	<em class="tkw"><xsl:value-of select="name()" /></em>
    </xsl:template>

    <xsl:template match="arr">
    	<table class="tba">
    		<tr>
    			<td>
    				<ol class="ola">
			       		<xsl:apply-templates select="elem" />
					</ol>
				</td>
			</tr>
		</table>
    </xsl:template>

    <xsl:template match="elem">
		<li>
       		<xsl:apply-templates select="obj|arr|str|num|true|false|null" />
       	</li>
    </xsl:template>

</xsl:stylesheet>
