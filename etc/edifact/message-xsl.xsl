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
    Generates a complete XML description of an EDIFACT message definition
    @(#) $Id: message-xsl.xsl 9 2008-09-05 05:21:15Z gfis $
    2008-07-18, Dr. Georg Fischer
    
    Activation: xalan -i 4 -o outputfile edxsl.xml edsd-xsl.xsl > edsd.xsl
-->
<xsl:stylesheet version="1.0" 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:xalan="http://xml.apache.org/xalan"
		> 
    <xsl:output method="xml" indent="yes" xalan:indent-amount="2" encoding="UTF-8" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="root">
    	<xsl:element name="xsl:stylesheet">
			<xsl:attribute name="version">
				<xsl:value-of select="1.0" />
			</xsl:attribute>
	    	<xsl:element name="xsl:include">
				<xsl:attribute name="href">
					<xsl:value-of select="'unsd.xsl'" />
				</xsl:attribute>
	   	    </xsl:element>
	    	<xsl:element name="xsl:include">
				<xsl:attribute name="href">
					<xsl:value-of select="'edsd.xsl'" />
				</xsl:attribute>
	   	    </xsl:element>
		    <xsl:element name="xsl:output">
				<xsl:attribute name="method">
					<xsl:value-of select="'xml'" />
				</xsl:attribute>
				<xsl:attribute name="indent">
					<xsl:value-of select="'yes'" />
				</xsl:attribute>
				<xsl:attribute name="xalan:indent-amount">
					<xsl:value-of select="'2'" />
				</xsl:attribute>
				<xsl:attribute name="encoding">
					<xsl:value-of select="'UTF-8'" />
				</xsl:attribute>
		    </xsl:element>
		    <xsl:element name="xsl:strip-space">
				<xsl:attribute name="elements">
					<xsl:value-of select="'*'" />
				</xsl:attribute>
		    </xsl:element>
   	    	<xsl:apply-templates select="message" />
   	    </xsl:element>
    </xsl:template>

    <xsl:template match="message">
		<xsl:element name="xsl:template">
			<xsl:attribute name="match">
				<xsl:value-of select="'message'" />
			</xsl:attribute>
			<xsl:element name="message">
				<xsl:attribute name="name">
					<xsl:value-of select="@name" />
				</xsl:attribute>
				<xsl:attribute name="issue">
					<xsl:value-of select="@issue" />
				</xsl:attribute>
				<xsl:comment>
					<xsl:value-of select="concat(@name, ',', @issue,  ': ', title)" />
		    	</xsl:comment>
		    	<xsl:copy-of select="title" />
	    	  	<xsl:apply-templates select="group|segm" />
				<xsl:comment>
					<xsl:value-of select="@name" />
		    	</xsl:comment>
			</xsl:element>
      	</xsl:element>
    </xsl:template><!-- message -->

    <xsl:template match="group">
		<group>
			<xsl:attribute name="name">
				<xsl:value-of select="@name" />
			</xsl:attribute>
        	<xsl:copy-of select="min|max|seq|n" />
           	<xsl:apply-templates select="segm|group" />
        </group>
    </xsl:template><!-- group -->

    <xsl:template match="segm">
		<segm>
			<xsl:attribute name="name">
				<xsl:value-of select="@name" />
			</xsl:attribute>
        	<xsl:copy-of select="min|max|seq|n" />
        	<xsl:element name="xsl:call-template">
				<xsl:attribute name="name">
					<xsl:value-of select="@name" />
				</xsl:attribute>
        	</xsl:element>
		</segm>
    </xsl:template><!-- segm -->

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>
<!--
            <xsl:value-of select="node()" />
            <xsl:apply-templates select="@*|node()" />
-->
</xsl:stylesheet>
