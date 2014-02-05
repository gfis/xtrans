<?xml version="1.0" encoding="UTF-8"?>
<!--
    Transforms a record structure definition into a W3C schema (.xsd)
    @(#) $Id: genW3CSchema.xsl 9 2008-09-05 05:21:15Z gfis $
    2013-12-29: tabs to 4 spaces
    2008-06-24, Dr. Georg Fischer: copied from genRecord.xsl; äöüÄÖÜß
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
        xmlns:xsl ="http://www.w3.org/1999/XSL/Transform"
        xmlns:xs  ="http://www.w3.org/2001/XMLSchema"
        xmlns:rec ="http://www.teherba.org/2006/xtrans/Record"
        xmlns:date="http://exslt.org/dates-and-times"
        xmlns:func="http://exslt.org/functions"
        extension-element-prefixes="func date"
        >
    <xsl:output method="xml" indent="yes" />
    <xsl:strip-space elements="*" /><!-- remove whitespace in all nodes -->

<!-- Transformation of the root element -->

    <xsl:template match="rec:record">
        <xs:schema xmlns="http://www.teherba.org/2006/xtrans/Record"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                targetNamespace="http://www.teherba.org/2006/xtrans/Record"
                elementFormDefault="qualified"
                attributeFormDefault="unqualified"
                >
            <xs:element name="records" type="RecordsType"/>
            <xs:complexType name="RecordsType">
                <xs:sequence>
                    <xs:element minOccurs="1" maxOccurs="unbounded">
                        <xsl:attribute name="name">
                            <xsl:value-of select="@name" />
                        </xsl:attribute>
                        <xs:annotation>
                            <xs:documentation><xsl:value-of select="@rem" /></xs:documentation>
                        </xs:annotation>
                        <xs:complexType>
                            <!-- evaluate all substructures -->
<!--
                            <xsl:for-each select="rec:field|rec:choice|rec:array">
-->
                            <xsl:for-each select="rec:choice">
                                <xsl:apply-templates select="." />
                            </xsl:for-each>
                            <xsl:for-each select="rec:array">
                                <xsl:apply-templates select="." />
                            </xsl:for-each>
                            <xsl:for-each select="rec:field">
                                <xsl:apply-templates select="." />
                            </xsl:for-each>
                        </xs:complexType>
                    </xs:element>
                </xs:sequence>
            </xs:complexType>
        </xs:schema>
    </xsl:template><!-- rec:record -->

    <xsl:template match="rec:choice">
        <xs:sequence>
            <xs:choice>
                <xsl:for-each select="rec:variant">
                    <xsl:apply-templates select="." />
                </xsl:for-each>
            </xs:choice>
        </xs:sequence>
    </xsl:template><!-- rec:choice -->

    <xsl:template match="rec:variant">
        <xs:element>
            <xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute>
            <xs:annotation>
                <xs:documentation><xsl:value-of select="@rem" /></xs:documentation>
            </xs:annotation>
            <xs:complexType>

                <xs:sequence>
                    <xsl:for-each select="rec:array">
                        <xsl:apply-templates select="." />
                    </xsl:for-each>
                </xs:sequence>
<!--
-->
                <xsl:for-each select="rec:field|rec:choice">
                    <xsl:apply-templates select="." />
                </xsl:for-each>
            </xs:complexType>
        </xs:element>
    </xsl:template><!-- rec:variant -->

    <xsl:template match="rec:array">
        <xs:element minOccurs="0">
            <xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute>
            <xsl:attribute name="maxOccurs"><xsl:value-of select="@occurs" /></xsl:attribute>
            <xs:complexType>
                <!-- Generate field access code for the array element -->
                <xsl:for-each select="rec:field">
                    <xsl:apply-templates select="." />
                </xsl:for-each>
                <xs:attribute name="index">
                     <xs:annotation>
                        <xs:documentation><xsl:value-of select="concat('array index in [0..', @occurs - 1, ']')" /></xs:documentation>
                    </xs:annotation>
                    <xs:simpleType>
                        <xs:restriction base="xs:unsignedInt">
                            <xs:maxExclusive>
                                <xsl:attribute name="value"><xsl:value-of select="@occurs" /></xsl:attribute>
                            </xs:maxExclusive>
                        </xs:restriction>
                    </xs:simpleType>
                </xs:attribute>
            </xs:complexType>
        </xs:element>
    </xsl:template><!-- rec:array -->

    <xsl:template match="rec:field">
        <xs:attribute>
            <xsl:attribute name="name"><xsl:value-of select="@name" /></xsl:attribute>
            <xs:annotation>
                <xs:documentation><xsl:value-of select="@rem" /></xs:documentation>
            </xs:annotation>
            <xs:simpleType>
                <xs:restriction>
                    <xsl:choose>
                        <xsl:when test="@type = 'num' or @type = 'binary' or @type='packed' or @type='unsignedpacked'">
                            <xsl:attribute name="base">
                                <xsl:value-of select="'xs:string'" />
                            </xsl:attribute>
                            <xs:minLength value="0"/>
                            <xs:maxLength>
                                <xsl:attribute name="value">
                                    <xsl:choose>
                                        <xsl:when test="@type = 'binary'">
                                            <xsl:value-of select="@len * 3"/>
                                        </xsl:when>
                                        <xsl:when test="@type = 'packed'">
                                            <xsl:value-of select="@len * 2 + 2"/>
                                        </xsl:when>
                                        <xsl:when test="@type = 'unsignedpacked'">
                                            <xsl:value-of select="@len * 2"/>
                                        </xsl:when>
                                        <xsl:otherwise><!-- num -->
                                            <xsl:value-of select="@len"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:attribute>
                            </xs:maxLength>
                            <xs:pattern>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="concat('[0-9]*', '')"/>
                                </xsl:attribute>
                            </xs:pattern>
                        </xsl:when>
                        <xsl:otherwise><!-- string, char, ebcdic -->
                            <xsl:attribute name="base">
                                <xsl:value-of select="'xs:string'" />
                            </xsl:attribute>
                            <xs:minLength value="0"/>
                            <xs:maxLength>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="@len"/>
                                </xsl:attribute>
                            </xs:maxLength>
                        </xsl:otherwise>
                    </xsl:choose>
                </xs:restriction>
            </xs:simpleType>
        </xs:attribute>
    </xsl:template><!-- field -->

</xsl:stylesheet>
