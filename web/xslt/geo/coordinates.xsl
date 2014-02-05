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
    Extracts x,y,z coordinates and dateTime from NMEA raw XML results
    @(#) $Id: coordinates.xsl 9 2008-09-05 05:21:15Z gfis $
    2007-08-25, Georg Fischer
-->
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" />
    
    <xsl:template match="nmea">
		<coordinates>
        <xsl:text>&#10;</xsl:text>
        <xsl:for-each select=".">
        	<xsl:apply-templates select="GGA" />
        </xsl:for-each>
        </coordinates>
    </xsl:template>

    <xsl:template match="GGA">
		<dim5>
			<xsl:attribute name="x">
				<!-- +/- 0..90 degrees with decimal fraction, 0 = equator, +90 = north pole -->
				<xsl:call-template name="coordinate">
                    <xsl:with-param name="value" select="./long"/>
                    <xsl:with-param name="len"   select="3"/>
				</xsl:call-template>
			</xsl:attribute>
			<xsl:attribute name="y">
				<!-- +/- 0..180 degrees with decimal fraction, 0 = Greenwich, - = East -->
				<xsl:call-template name="coordinate">
                    <xsl:with-param name="value" select="./latt"/>
                    <xsl:with-param name="len"   select="2"/>
				</xsl:call-template>
			</xsl:attribute>
			<xsl:attribute name="z"><!-- Altitude in meters with decimal fraction -->
				<xsl:value-of select="format-number(substring(./alti, 2), '#000.0')" />
			</xsl:attribute>
			<xsl:attribute name="t"><!-- ISO dateTime: 2007-08-25T09:50:00Z -->
				<xsl:variable name="date" select="./following-sibling::RMC[1]/date" />
				<xsl:value-of select="concat('20', substring($date, 5, 2), '-', substring($date, 3, 2), '-' ,substring($date, 1, 2))" />
				<xsl:value-of select="concat('T', substring(time, 1, 2), ':', substring(time, 3, 2), ':' ,substring(time, 5, 2), 'Z')" />
			</xsl:attribute>
			<xsl:attribute name="v"><!-- speed in km/h; was in knots = 0.51444444 m/s -->
				<xsl:variable name="speed" select="./following-sibling::RMC[1]/sped" />
				<xsl:value-of select="format-number($speed * 0.51444444 * 3600 div 1000, '##0.00')" />
			</xsl:attribute>
			<xsl:attribute name="dt"><!-- delta t in seconds -->
				<xsl:variable name="time2" select="concat(./following-sibling::GGA[1]/time, '000000')" />
				<xsl:value-of select="substring($time2, 1, 2) * 3600 + substring($time2, 3, 2) * 60 + substring($time2, 5, 2)
					- (substring(time, 1, 2) * 3600 + substring(time, 3, 2) * 60 + substring(time, 5, 2))" />
			</xsl:attribute>
			<xsl:attribute name="w"><!-- course in degrees, 0 = north, 90 = east, 180 = south, 270 = west -->
				<xsl:value-of select="format-number(./following-sibling::RMC[1]/curs, '#000.0')" />
			</xsl:attribute>
			<xsl:attribute name="d"><!-- distance in m -->
				<xsl:variable name="x2" select="concat(./following-sibling::GGA[1]/long, '00000000')" />
				<xsl:variable name="y2" select="concat(./following-sibling::GGA[1]/latt, '00000000')" />
				<xsl:value-of select="format-number(0, '#000.0')" />
			</xsl:attribute>
        </dim5>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

    <xsl:template name="coordinate">
        <xsl:param name="value" />
        <xsl:param name="len" />
        <xsl:choose><!-- sign -->
            <xsl:when test="substring($value, 1, 1) = 'S' or substring($value, 1, 1) = 'E'">
                <xsl:value-of select="'-'" />
            </xsl:when>
        </xsl:choose>
        <xsl:variable name="temp">
        	<xsl:value-of select="round((substring($value, 2, $len) + substring($value, 2 + $len, 7) div 60.0 + 0.00000051) * 1000000) div 1000000" />
        </xsl:variable>
        <xsl:value-of select="format-number($temp, '##0.000000')" />
    </xsl:template>

</xsl:stylesheet>
