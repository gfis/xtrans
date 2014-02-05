<?xml version="1.0"  encoding="UTF-8"?>
<!--
    Extract names and gender from a GEDCOM file
    @(#) $Id: prenom_sex.xsl 9 2008-09-05 05:21:15Z gfis $
    2007-06-22, Dr. Georg Fischer
    
    Activation: xalan -i 4 -o outputfile inputfile prenom_sex.xsl
-->
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" indent="yes" encoding="ISO-8859-1" />
    <xsl:strip-space elements="*"/>
    
    <xsl:template match="INDI">
       <xsl:choose>
            <xsl:when test="name() = 'INDI'">
		    	<xsl:value-of select="concat(SEX, ' ', NAME, '&#10;')" />
            </xsl:when>
            <xsl:otherwise>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="FAM">
    </xsl:template>
</xsl:stylesheet>
