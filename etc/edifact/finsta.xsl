<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1">
  <xsl:include href="unsd.xsl"/>
  <xsl:include href="edsd.xsl"/>
  <xsl:output method="xml" indent="yes" xmlns:xalan="http://xml.apache.org/xalan" xalan:indent-amount="2" encoding="UTF-8"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="message">
    <message name="FINSTA" issue="07A">
      <!--FINSTA,07A: Financial statement of an account message-->
      <title>Financial statement of an account message</title>
      <segm xmlns:xalan="http://xml.apache.org/xalan" name="UNH">
        <min>1</min>
        <max>1</max>
        <seq>0010</seq>
        <n>Message header</n>
        <xsl:call-template name="UNH"/>
      </segm>
      <segm xmlns:xalan="http://xml.apache.org/xalan" name="BGM">
        <min>1</min>
        <max>1</max>
        <seq>0020</seq>
        <n>Beginning of message</n>
        <xsl:call-template name="BGM"/>
      </segm>
      <segm xmlns:xalan="http://xml.apache.org/xalan" name="DTM">
        <min>1</min>
        <max>1</max>
        <seq>0030</seq>
        <n>Date/time/period</n>
        <xsl:call-template name="DTM"/>
      </segm>
      <group xmlns:xalan="http://xml.apache.org/xalan" name="1">
        <min>0</min>
        <max>1</max>
        <seq>0040</seq>
        <segm name="RFF">
          <min>1</min>
          <max>1</max>
          <seq>0050</seq>
          <n>Reference</n>
          <xsl:call-template name="RFF"/>
        </segm>
        <segm name="DTM">
          <min>0</min>
          <max>1</max>
          <seq>0060</seq>
          <n>Date/time/period</n>
          <xsl:call-template name="DTM"/>
        </segm>
      </group>
      <group xmlns:xalan="http://xml.apache.org/xalan" name="2">
        <min>0</min>
        <max>5</max>
        <seq>0070</seq>
        <segm name="FII">
          <min>1</min>
          <max>1</max>
          <seq>0080</seq>
          <n>Financial institution information</n>
          <xsl:call-template name="FII"/>
        </segm>
        <segm name="CTA">
          <min>0</min>
          <max>1</max>
          <seq>0090</seq>
          <n>Contact information</n>
          <xsl:call-template name="CTA"/>
        </segm>
        <segm name="COM">
          <min>0</min>
          <max>5</max>
          <seq>0100</seq>
          <n>Communication contact</n>
          <xsl:call-template name="COM"/>
        </segm>
      </group>
      <group xmlns:xalan="http://xml.apache.org/xalan" name="3">
        <min>0</min>
        <max>3</max>
        <seq>0110</seq>
        <segm name="NAD">
          <min>1</min>
          <max>1</max>
          <seq>0120</seq>
          <n>Name and address</n>
          <xsl:call-template name="NAD"/>
        </segm>
        <segm name="CTA">
          <min>0</min>
          <max>1</max>
          <seq>0130</seq>
          <n>Contact information</n>
          <xsl:call-template name="CTA"/>
        </segm>
        <segm name="COM">
          <min>0</min>
          <max>5</max>
          <seq>0140</seq>
          <n>Communication contact</n>
          <xsl:call-template name="COM"/>
        </segm>
      </group>
      <group xmlns:xalan="http://xml.apache.org/xalan" name="4">
        <min>1</min>
        <max>9999</max>
        <seq>0150</seq>
        <segm name="LIN">
          <min>1</min>
          <max>1</max>
          <seq>0160</seq>
          <n>Line item</n>
          <xsl:call-template name="LIN"/>
        </segm>
        <segm name="FII">
          <min>1</min>
          <max>1</max>
          <seq>0170</seq>
          <n>Financial institution information</n>
          <xsl:call-template name="FII"/>
        </segm>
        <segm name="RFF">
          <min>1</min>
          <max>1</max>
          <seq>0180</seq>
          <n>Reference</n>
          <xsl:call-template name="RFF"/>
        </segm>
        <segm name="FTX">
          <min>0</min>
          <max>1</max>
          <seq>0190</seq>
          <n>Free text</n>
          <xsl:call-template name="FTX"/>
        </segm>
        <group name="5">
          <min>1</min>
          <max>99</max>
          <seq>0200</seq>
          <segm name="MOA">
            <min>1</min>
            <max>1</max>
            <seq>0210</seq>
            <n>Monetary amount</n>
            <xsl:call-template name="MOA"/>
          </segm>
          <segm name="DTM">
            <min>0</min>
            <max>1</max>
            <seq>0220</seq>
            <n>Date/time/period</n>
            <xsl:call-template name="DTM"/>
          </segm>
        </group>
        <group name="6">
          <min>0</min>
          <max>9999</max>
          <seq>0230</seq>
          <segm name="SEQ">
            <min>1</min>
            <max>1</max>
            <seq>0240</seq>
            <n>Sequence details</n>
            <xsl:call-template name="SEQ"/>
          </segm>
          <segm name="RFF">
            <min>1</min>
            <max>5</max>
            <seq>0250</seq>
            <n>Reference</n>
            <xsl:call-template name="RFF"/>
          </segm>
          <segm name="DTM">
            <min>1</min>
            <max>2</max>
            <seq>0260</seq>
            <n>Date/time/period</n>
            <xsl:call-template name="DTM"/>
          </segm>
          <segm name="BUS">
            <min>1</min>
            <max>1</max>
            <seq>0270</seq>
            <n>Business function</n>
            <xsl:call-template name="BUS"/>
          </segm>
          <segm name="MOA">
            <min>1</min>
            <max>1</max>
            <seq>0280</seq>
            <n>Monetary amount</n>
            <xsl:call-template name="MOA"/>
          </segm>
          <segm name="FTX">
            <min>0</min>
            <max>1</max>
            <seq>0290</seq>
            <n>Free text</n>
            <xsl:call-template name="FTX"/>
          </segm>
        </group>
      </group>
      <segm xmlns:xalan="http://xml.apache.org/xalan" name="CNT">
        <min>0</min>
        <max>5</max>
        <seq>0300</seq>
        <n>Control total</n>
        <xsl:call-template name="CNT"/>
      </segm>
      <group xmlns:xalan="http://xml.apache.org/xalan" name="7">
        <min>0</min>
        <max>5</max>
        <seq>0310</seq>
        <segm name="AUT">
          <min>1</min>
          <max>1</max>
          <seq>0320</seq>
          <n>Authentication result</n>
          <xsl:call-template name="AUT"/>
        </segm>
        <segm name="DTM">
          <min>0</min>
          <max>1</max>
          <seq>0330</seq>
          <n>Date/time/period</n>
          <xsl:call-template name="DTM"/>
        </segm>
      </group>
      <segm xmlns:xalan="http://xml.apache.org/xalan" name="UNT">
        <min>1</min>
        <max>1</max>
        <seq>0340</seq>
        <n>Message trailer</n>
        <xsl:call-template name="UNT"/>
      </segm>
    </message>
  </xsl:template>
</xsl:stylesheet>
