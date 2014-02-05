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
    Shows a track of coordinates as SVG path
    @(#) $Id: track.xsl 9 2008-09-05 05:21:15Z gfis $
    2007-08-26, Georg Fischer
-->
<xsl:stylesheet version="1.0" 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns="http://www.w3.org/2000/svg"
		>
    <xsl:output method="xml" />
	
	<xsl:variable name="xmax"   select="-8-0.5" />
	<xsl:variable name="xmin"   select="-3+0.5" />
	<xsl:variable name="ymax"   select="43" />
	<xsl:variable name="ymin"   select="49" />
	
	<xsl:variable name="width"  select="400" />
	<xsl:variable name="height" select="600" />
    
    <xsl:template match="coordinates">
		<svg>
			<xsl:attribute name="width">
				<xsl:value-of select="$width" />
			</xsl:attribute>
			<xsl:attribute name="height">
				<xsl:value-of select="$height" />
			</xsl:attribute>
			
			<!-- first the track of all coordinates -->
    		<g style="stroke:red; stroke-width:1; stroke-linejoin:round; fill:none" >
				<path>
					<xsl:attribute name="d">
						<xsl:for-each select="dim5">
							<xsl:choose>
								<xsl:when test="position() = 1">
									<xsl:value-of select="'M'" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="'L'" />
								</xsl:otherwise>
					        </xsl:choose>
							<xsl:call-template name="xmap">
            			        <xsl:with-param name="x" select="@x"/>
							</xsl:call-template>
							<xsl:value-of select="','" />
							<xsl:call-template name="ymap">
            			        <xsl:with-param name="y" select="@y"/>
							</xsl:call-template>
						</xsl:for-each>
					</xsl:attribute>
				</path>
			</g>

			<!-- now the markers for pauses -->
			<xsl:for-each select="dim5">
				<xsl:choose>
					<xsl:when test="@dt &gt; 120 or @dt &lt; 0">
						<circle r="10" style="stroke:green; stroke-width:1; fill:none">
							<xsl:attribute name="cx">
								<xsl:call-template name="xmap">
    	        			        <xsl:with-param name="x" select="@x"/>
								</xsl:call-template>
							</xsl:attribute>
							<xsl:attribute name="cy">
								<xsl:call-template name="ymap">
    	        			        <xsl:with-param name="y" select="@y"/>
								</xsl:call-template>
							</xsl:attribute>
						</circle>
						<text style="font-size: 8pt; font-family:sans-serif">
							<xsl:attribute name="x">
								<xsl:call-template name="xmap">
    	        			        <xsl:with-param name="x" select="@x"/>
								</xsl:call-template>
							</xsl:attribute>
							<xsl:attribute name="y">
								<xsl:call-template name="ymap">
    	        			        <xsl:with-param name="y" select="@y"/>
								</xsl:call-template>
							</xsl:attribute>
							<xsl:text>&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;</xsl:text>
							<xsl:value-of select="concat(substring(@t, 12), ' ', @dt, 's')" />
						</text>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
			    </xsl:choose>
			</xsl:for-each>
			
		</svg>
    </xsl:template>

    <xsl:template name="xmap">
        <xsl:param name="x" />
       	<xsl:value-of select="(($x - $xmin) div ($xmax - $xmin)) * $width" />
    </xsl:template>

    <xsl:template name="ymap">
        <xsl:param name="y" />
       	<xsl:value-of select="(($y - $ymin) div ($ymax - $ymin)) * $height"/>
    </xsl:template>

</xsl:stylesheet>
