<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!--
    Tests the checksums in DTA record type E, and repairs them if necessary
    @(#) $Id: DTA.repair.xsl 9 2008-09-05 05:21:15Z gfis $
    2008-06-24, Dr. Georg Fischer
	
	Activation:
    	xalan -i 4 -o dta.out.xml dta.in.xml DTA.repair.xsl
	
-->
<!--
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
-->
<xsl:stylesheet version="1.0" 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:r="http://www.teherba.org/2006/xtrans/Record"
		>
	<xsl:output method="xml" indent="yes" />
	<xsl:strip-space elements="*"/>

  	<xsl:template match="r:records">
  		<r:records>
			<xsl:apply-templates select="r:DTA" />
		</r:records>
	</xsl:template>

  	<xsl:template match="r:DTA">
  		<r:DTA>
			<xsl:attribute name="rlen"><xsl:value-of select="@rlen" /></xsl:attribute>
			<xsl:attribute name="l0"  ><xsl:value-of select="@l0"   /></xsl:attribute>
			<xsl:attribute name="rtyp"><xsl:value-of select="@rtyp" /></xsl:attribute>
			<xsl:apply-templates select="r:A|r:C|r:E" />
		</r:DTA>
	</xsl:template>

  	<xsl:template match="r:E">
    	<xsl:comment>
    		<xsl:value-of select="concat(' E4=&quot;', @E4 + 0, '&quot; E6=&quot;', @E6 + 0, '&quot; E7=&quot;', @E7 + 0, '&quot; E8=&quot;', @E8 + 0, '&quot; were stored')" />
    	</xsl:comment>
		<r:E>
			<xsl:attribute name="E4">
  				<xsl:value-of select="count(../preceding-sibling::r:DTA[@rtyp='C'])" />
			</xsl:attribute>
			<xsl:attribute name="E6">
  				<xsl:value-of select=  "sum(../preceding-sibling::r:DTA[@rtyp='C']/r:C/@C5)" />
			</xsl:attribute>
			<xsl:attribute name="E7">
  				<xsl:value-of select=  "sum(../preceding-sibling::r:DTA[@rtyp='C']/r:C/@C4)" />
			</xsl:attribute>
			<xsl:attribute name="E8">
  				<xsl:value-of select=  "sum(../preceding-sibling::r:DTA[@rtyp='C']/r:C/@C12)" />
			</xsl:attribute>
		</r:E>
	</xsl:template>

	<!-- copy all others transparently -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
