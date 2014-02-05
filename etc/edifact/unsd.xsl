<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <!--UNSD,07A: Segment specifications-->
  <xsl:template name="UNH">
    <code>UNH</code>
    <title>HEADER</title>
    <function>
      <n>header.</n>
    </function>
    <element>
      <child name="3164" seq="030">
        <n>CITY NAME</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <!--UNH-->
  </xsl:template>
  <xsl:template name="UNT">
    <code>UNT</code>
    <title>HEADER</title>
    <function>
      <n>header.</n>
    </function>
    <element>
      <child name="3164" seq="030">
        <n>CITY NAME</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <!--UNT-->
  </xsl:template>
</xsl:stylesheet>
