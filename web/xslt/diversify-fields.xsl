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
    @(#) $Id: diversify-fields.xsl 9 2008-09-05 05:21:15Z gfis $
    2006-10-04, Dr. Georg Fischer
-->
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes" />
    
    <xsl:template match="*|@*|comment()">
        <xsl:choose>
            <xsl:when test="name() = 'field'">
                <xsl:element name="{concat('F', @id)}">
                    <xsl:for-each select="@*">
                        <xsl:if test="name() != 'id'">
                            <xsl:attribute name="{name()}">
                                <xsl:value-of select="." />
                            </xsl:attribute>
                        </xsl:if>
                    </xsl:for-each>
                    <xsl:apply-templates select="*|text()|comment()" />
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="@*|node()|comment()" />
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
