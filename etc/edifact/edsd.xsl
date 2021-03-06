<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1">
  <xsl:output method="xml" indent="yes" xmlns:xalan="http://xml.apache.org/xalan" xalan:indent-amount="2" encoding="UTF-8"/>
  <xsl:strip-space elements="*"/>
  <!--EDSD,07A: Segment specifications-->
  <xsl:template name="ADR">
    <code>ADR</code>
    <title>ADDRESS</title>
    <function>
      <n>To specify an address.</n>
    </function>
    <composite>
      <data name="C817" seq="010">
        <n>ADDRESS USAGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3299" seq="">
        <n>Address purpose code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3131" seq="">
        <n>Address type code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3475" seq="">
        <n>Address status code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C090" seq="020">
        <n>ADDRESS DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3477" seq="">
        <n>Address format code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3286" seq="">
        <n>Address component description</n>
        <min>1</min>
        <type>an</type>
        <length>70</length>
      </child>
      <child name="3286" seq="">
        <n>Address component description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
      <child name="3286" seq="">
        <n>Address component description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
      <child name="3286" seq="">
        <n>Address component description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
      <child name="3286" seq="">
        <n>Address component description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <element>
      <child name="3164" seq="030">
        <n>CITY NAME</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="3251" seq="040">
        <n>POSTAL IDENTIFICATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>17</length>
      </child>
    </element>
    <element>
      <child name="3207" seq="050">
        <n>COUNTRY IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C819" seq="060">
        <n>COUNTRY SUBDIVISION DETAILS</n>
        <min>0</min>
        <max>5</max>
        <type/>
        <length/>
      </data>
      <child name="3229" seq="">
        <n>Country subdivision identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3228" seq="">
        <n>Country subdivision name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C517" seq="070">
        <n>LOCATION IDENTIFICATION</n>
        <min>0</min>
        <max>5</max>
        <type/>
        <length/>
      </data>
      <child name="3225" seq="">
        <n>Location identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3224" seq="">
        <n>Location name</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <!--ADR-->
  </xsl:template>
  <xsl:template name="AGR">
    <code>AGR</code>
    <title>AGREEMENT IDENTIFICATION</title>
    <function>
      <n>To specify the agreement details.</n>
    </function>
    <composite>
      <data name="C543" seq="010">
        <n>AGREEMENT TYPE IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7431" seq="">
        <n>Agreement type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7433" seq="">
        <n>Agreement type description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7434" seq="">
        <n>Agreement type description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <element>
      <child name="9419" seq="020">
        <n>SERVICE LAYER CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--AGR-->
  </xsl:template>
  <xsl:template name="AJT">
    <code>AJT</code>
    <title>ADJUSTMENT DETAILS</title>
    <function>
      <n>To identify the reason for an adjustment.</n>
    </function>
    <element>
      <child name="4465" seq="010">
        <n>ADJUSTMENT REASON DESCRIPTION CODE</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1082" seq="020">
        <n>LINE ITEM IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>6</length>
      </child>
    </element>
    <!--AJT-->
  </xsl:template>
  <xsl:template name="ALC">
    <code>ALC</code>
    <title>ALLOWANCE OR CHARGE</title>
    <function>
      <n>To identify allowance or charge details.</n>
    </function>
    <element>
      <child name="5463" seq="010">
        <n>ALLOWANCE OR CHARGE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C552" seq="020">
        <n>ALLOWANCE/CHARGE INFORMATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1230" seq="">
        <n>Allowance or charge identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="5189" seq="">
        <n>Allowance or charge identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4471" seq="030">
        <n>SETTLEMENT MEANS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1227" seq="040">
        <n>CALCULATION SEQUENCE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C214" seq="050">
        <n>SPECIAL SERVICES IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7161" seq="">
        <n>Special service description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7160" seq="">
        <n>Special service description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7160" seq="">
        <n>Special service description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--ALC-->
  </xsl:template>
  <xsl:template name="ALI">
    <code>ALI</code>
    <title>ADDITIONAL INFORMATION</title>
    <function>
      <n>To indicate that special conditions due to the</n>
      <n>origin, customs preference, fiscal or commercial</n>
      <n>factors are applicable.</n>
    </function>
    <element>
      <child name="3239" seq="010">
        <n>COUNTRY OF ORIGIN IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9213" seq="020">
        <n>DUTY REGIME TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4183" seq="030">
        <n>SPECIAL CONDITION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4183" seq="040">
        <n>SPECIAL CONDITION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4183" seq="050">
        <n>SPECIAL CONDITION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4183" seq="060">
        <n>SPECIAL CONDITION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4183" seq="070">
        <n>SPECIAL CONDITION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--ALI-->
  </xsl:template>
  <xsl:template name="APP">
    <code>APP</code>
    <title>APPLICABILITY</title>
    <function>
      <n>To specify the applicability.</n>
    </function>
    <element>
      <child name="9051" seq="010">
        <n>APPLICABILITY CODE QUALIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C973" seq="020">
        <n>APPLICABILITY TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9049" seq="">
        <n>Applicability type description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9048" seq="">
        <n>Applicability type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--APP-->
  </xsl:template>
  <xsl:template name="APR">
    <code>APR</code>
    <title>ADDITIONAL PRICE INFORMATION</title>
    <function>
      <n>To provide information concerning pricing</n>
      <n>related to class of trade, price multiplier, and</n>
      <n>reason for change.</n>
    </function>
    <element>
      <child name="4043" seq="010">
        <n>TRADE CLASS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C138" seq="020">
        <n>PRICE MULTIPLIER INFORMATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5394" seq="">
        <n>Price multiplier rate</n>
        <min>1</min>
        <type>n</type>
        <length>12</length>
      </child>
      <child name="5393" seq="">
        <n>Price multiplier type code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C960" seq="030">
        <n>REASON FOR CHANGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4295" seq="">
        <n>Change reason description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4294" seq="">
        <n>Change reason description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--APR-->
  </xsl:template>
  <xsl:template name="ARD">
    <code>ARD</code>
    <title>MONETARY AMOUNT FUNCTION</title>
    <function>
      <n>To provide details of the function of a monetary</n>
      <n>amount.</n>
    </function>
    <composite>
      <data name="C549" seq="010">
        <n>MONETARY AMOUNT FUNCTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5007" seq="">
        <n>Monetary amount function description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5006" seq="">
        <n>Monetary amount function description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C008" seq="020">
        <n>MONETARY AMOUNT FUNCTION DETAIL</n>
        <min>0</min>
        <max>8</max>
        <type/>
        <length/>
      </data>
      <child name="5105" seq="">
        <n>Monetary amount function detail</n>
      </child>
      <child name="" seq="">
        <n>description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5104" seq="">
        <n>Monetary amount function detail</n>
      </child>
      <child name="" seq="">
        <n>description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <!--ARD-->
  </xsl:template>
  <xsl:template name="ARR">
    <code>ARR</code>
    <title>ARRAY INFORMATION</title>
    <function>
      <n>To contain the data in an array.</n>
    </function>
    <composite>
      <data name="C778" seq="010">
        <n>POSITION IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7164" seq="">
        <n>Hierarchical structure level identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1050" seq="">
        <n>Sequence position identifier</n>
        <min>0</min>
        <type>an</type>
        <length>10</length>
      </child>
    </composite>
    <composite>
      <data name="C770" seq="020">
        <n>ARRAY CELL DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9424" seq="">
        <n>Array cell data description</n>
        <min>0</min>
        <type>an</type>
        <length>512</length>
      </child>
    </composite>
    <code>Note</code>
    <title/>
    <!--ARR-->
  </xsl:template>
  <xsl:template name="ASI">
    <code>ASI</code>
    <title>ARRAY STRUCTURE IDENTIFICATION</title>
    <function>
      <n>To identify the structure of an array.</n>
    </function>
    <composite>
      <data name="C779" seq="010">
        <n>ARRAY STRUCTURE IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9428" seq="">
        <n>Array cell structure identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C082" seq="020">
        <n>PARTY IDENTIFICATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3039" seq="">
        <n>Party identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4405" seq="030">
        <n>STATUS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="040">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--ASI-->
  </xsl:template>
  <xsl:template name="ATT">
    <code>ATT</code>
    <title>ATTRIBUTE</title>
    <function>
      <n>To identify a specific attribute.</n>
    </function>
    <element>
      <child name="9017" seq="010">
        <n>ATTRIBUTE FUNCTION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C955" seq="020">
        <n>ATTRIBUTE TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9021" seq="">
        <n>Attribute type description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9020" seq="">
        <n>Attribute type description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C956" seq="030">
        <n>ATTRIBUTE DETAIL</n>
        <min>0</min>
        <max>5</max>
        <type/>
        <length/>
      </data>
      <child name="9019" seq="">
        <n>Attribute description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9018" seq="">
        <n>Attribute description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <!--ATT-->
  </xsl:template>
  <xsl:template name="AUT">
    <code>AUT</code>
    <title>AUTHENTICATION RESULT</title>
    <function>
      <n>To specify results of the application of an</n>
      <n>authentication procedure.</n>
    </function>
    <element>
      <child name="9280" seq="010">
        <n>VALIDATION RESULT TEXT</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="9282" seq="020">
        <n>VALIDATION KEY IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <!--AUT-->
  </xsl:template>
  <xsl:template name="BAS">
    <code>BAS</code>
    <title>BASIS</title>
    <function>
      <n>To describe the foundation or starting point.</n>
    </function>
    <element>
      <child name="9045" seq="010">
        <n>BASIS CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C974" seq="020">
        <n>BASIS TYPE</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9047" seq="">
        <n>Basis type description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9046" seq="">
        <n>Basis type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--BAS-->
  </xsl:template>
  <xsl:template name="BGM">
    <code>BGM</code>
    <title>BEGINNING OF MESSAGE</title>
    <function>
      <n>To indicate the type and function of a message</n>
      <n>and to transmit the identifying number.</n>
    </function>
    <composite>
      <data name="C002" seq="010">
        <n>DOCUMENT/MESSAGE NAME</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1001" seq="">
        <n>Document name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1000" seq="">
        <n>Document name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C106" seq="020">
        <n>DOCUMENT/MESSAGE IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1004" seq="">
        <n>Document identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1056" seq="">
        <n>Version identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1060" seq="">
        <n>Revision identifier</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
    </composite>
    <element>
      <child name="1225" seq="030">
        <n>MESSAGE FUNCTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4343" seq="040">
        <n>RESPONSE TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--BGM-->
  </xsl:template>
  <xsl:template name="BII">
    <code>BII</code>
    <title>STRUCTURE IDENTIFICATION</title>
    <function>
      <n>A segment used to convey an indexing structure</n>
      <n>mechanism which identifies the positioning of a</n>
      <n>group or item.</n>
    </function>
    <element>
      <child name="7429" seq="010">
        <n>INDEXING STRUCTURE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C045" seq="020">
        <n>BILL LEVEL IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7436" seq="">
        <n>Level one identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="7438" seq="">
        <n>Level two identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="7440" seq="">
        <n>Level three identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="7442" seq="">
        <n>Level four identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="7444" seq="">
        <n>Level five identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="7446" seq="">
        <n>Level six identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
    </composite>
    <element>
      <child name="7140" seq="030">
        <n>ITEM IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <!--BII-->
  </xsl:template>
  <xsl:template name="BUS">
    <code>BUS</code>
    <title>BUSINESS FUNCTION</title>
    <function>
      <n>To provide information related to the processing</n>
      <n>and purpose of a financial message.</n>
    </function>
    <composite>
      <data name="C521" seq="010">
        <n>BUSINESS FUNCTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4027" seq="">
        <n>Business function type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4025" seq="">
        <n>Business function code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4022" seq="">
        <n>Business description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <element>
      <child name="3279" seq="020">
        <n>GEOGRAPHIC AREA CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4487" seq="030">
        <n>FINANCIAL TRANSACTION TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C551" seq="040">
        <n>BANK OPERATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4383" seq="">
        <n>Bank operation code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4463" seq="050">
        <n>INTRA-COMPANY PAYMENT INDICATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--BUS-->
  </xsl:template>
  <xsl:template name="CAV">
    <code>CAV</code>
    <title>CHARACTERISTIC VALUE</title>
    <function>
      <n>To provide the value of a characteristic.</n>
    </function>
    <composite>
      <data name="C889" seq="010">
        <n>CHARACTERISTIC VALUE</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7111" seq="">
        <n>Characteristic value description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7110" seq="">
        <n>Characteristic value description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7110" seq="">
        <n>Characteristic value description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--CAV-->
  </xsl:template>
  <xsl:template name="CCD">
    <code>CCD</code>
    <title>CREDIT COVER DETAILS</title>
    <function>
      <n>To request a credit cover, reply to that request</n>
      <n>and disclose the reason for the reply.</n>
    </function>
    <element>
      <child name="4505" seq="010">
        <n>CREDIT COVER REQUEST TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4507" seq="020">
        <n>CREDIT COVER RESPONSE TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4509" seq="030">
        <n>CREDIT COVER RESPONSE REASON CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--CCD-->
  </xsl:template>
  <xsl:template name="CCI">
    <code>CCI</code>
    <title>CHARACTERISTIC</title>
    <function>
      <n>To identify and describe a specific</n>
      <n>characteristic and its relevance for subsequent</n>
      <n>business processes.</n>
    </function>
    <element>
      <child name="7059" seq="010">
        <n>CLASS TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C502" seq="020">
        <n>MEASUREMENT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6313" seq="">
        <n>Measured attribute code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6321" seq="">
        <n>Measurement significance code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6155" seq="">
        <n>Non-discrete measurement name code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="6154" seq="">
        <n>Non-discrete measurement name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C240" seq="030">
        <n>CHARACTERISTIC DESCRIPTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7037" seq="">
        <n>Characteristic description code</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7036" seq="">
        <n>Characteristic description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7036" seq="">
        <n>Characteristic description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="4051" seq="040">
        <n>CHARACTERISTIC RELEVANCE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--CCI-->
  </xsl:template>
  <xsl:template name="CDI">
    <code>CDI</code>
    <title>PHYSICAL OR LOGICAL STATE</title>
    <function>
      <n>To describe a physical or logical state.</n>
    </function>
    <element>
      <child name="7001" seq="010">
        <n>PHYSICAL OR LOGICAL STATE TYPE CODE</n>
      </child>
      <child name="" seq="">
        <n>QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C564" seq="020">
        <n>PHYSICAL OR LOGICAL STATE INFORMATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7007" seq="">
        <n>Physical or logical state description</n>
      </child>
      <child name="" seq="">
        <n>code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7006" seq="">
        <n>Physical or logical state description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <!--CDI-->
  </xsl:template>
  <xsl:template name="CDS">
    <code>CDS</code>
    <title>CODE SET IDENTIFICATION</title>
    <function>
      <n>To identify a code set and to give its class and</n>
      <n>maintenance operation.</n>
    </function>
    <composite>
      <data name="C702" seq="010">
        <n>CODE SET IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9150" seq="">
        <n>Simple data element tag identifier</n>
        <min>0</min>
        <type>an</type>
        <length>4</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="1507" seq="020">
        <n>DESIGNATED CLASS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="030">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--CDS-->
  </xsl:template>
  <xsl:template name="CDV">
    <code>CDV</code>
    <title>CODE VALUE DEFINITION</title>
    <function>
      <n>To provide information related to a code value.</n>
    </function>
    <element>
      <child name="9426" seq="010">
        <n>CODE VALUE TEXT</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="9434" seq="020">
        <n>CODE NAME</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>70</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="030">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9453" seq="040">
        <n>CODE VALUE SOURCE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="7299" seq="050">
        <n>REQUIREMENT DESIGNATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--CDV-->
  </xsl:template>
  <xsl:template name="CED">
    <code>CED</code>
    <title>COMPUTER ENVIRONMENT DETAILS</title>
    <function>
      <n>To give a precise definition of all necessary</n>
      <n>elements belonging to the configuration of a</n>
      <n>computer system like hardware, firmware,</n>
      <n>operating system, communication (VANS, network</n>
      <n>type, protocol, format) and application</n>
      <n>software.</n>
    </function>
    <element>
      <child name="1501" seq="010">
        <n>COMPUTER ENVIRONMENT DETAILS CODE</n>
      </child>
      <child name="" seq="">
        <n>QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C079" seq="020">
        <n>COMPUTER ENVIRONMENT IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1511" seq="">
        <n>Computer environment name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1510" seq="">
        <n>Computer environment name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1056" seq="">
        <n>Version identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1058" seq="">
        <n>Release identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="9448" seq="030">
        <n>FILE GENERATION COMMAND NAME</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <!--CED-->
  </xsl:template>
  <xsl:template name="CIN">
    <code>CIN</code>
    <title>CLINICAL INFORMATION</title>
    <function>
      <n>To describe an item of clinical information.</n>
    </function>
    <element>
      <child name="6415" seq="010">
        <n>CLINICAL INFORMATION TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C836" seq="020">
        <n>CLINICAL INFORMATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6413" seq="">
        <n>Clinical information description</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6412" seq="">
        <n>Clinical information description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C837" seq="030">
        <n>CERTAINTY DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4049" seq="">
        <n>Certainty description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4048" seq="">
        <n>Certainty description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--CIN-->
  </xsl:template>
  <xsl:template name="CLA">
    <code>CLA</code>
    <title>CLAUSE IDENTIFICATION</title>
    <function>
      <n>To identify a clause in a treaty, law and/or</n>
      <n>contract.</n>
    </function>
    <element>
      <child name="4059" seq="010">
        <n>CLAUSE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C970" seq="020">
        <n>CLAUSE NAME</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4069" seq="">
        <n>Clause name code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4068" seq="">
        <n>Clause name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <!--CLA-->
  </xsl:template>
  <xsl:template name="CLI">
    <code>CLI</code>
    <title>CLINICAL INTERVENTION</title>
    <function>
      <n>To specify a clinical intervention such as</n>
      <n>treatments and investigations.</n>
    </function>
    <element>
      <child name="9441" seq="010">
        <n>CLINICAL INTERVENTION TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C828" seq="020">
        <n>CLINICAL INTERVENTION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9437" seq="">
        <n>Clinical intervention description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9436" seq="">
        <n>Clinical intervention description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <!--CLI-->
  </xsl:template>
  <xsl:template name="CMP">
    <code>CMP</code>
    <title>COMPOSITE DATA ELEMENT IDENTIFICATION</title>
    <function>
      <n>To identify a composite data element and to give</n>
      <n>its class and maintenance operation.</n>
    </function>
    <element>
      <child name="9146" seq="010">
        <n>COMPOSITE DATA ELEMENT TAG IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>4</length>
      </child>
    </element>
    <element>
      <child name="1507" seq="020">
        <n>DESIGNATED CLASS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="030">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--CMP-->
  </xsl:template>
  <xsl:template name="CNI">
    <code>CNI</code>
    <title>CONSIGNMENT INFORMATION</title>
    <function>
      <n>To identify one consignment.</n>
    </function>
    <element>
      <child name="1490" seq="010">
        <n>CONSOLIDATION ITEM NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>5</length>
      </child>
    </element>
    <composite>
      <data name="C503" seq="020">
        <n>DOCUMENT/MESSAGE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1004" seq="">
        <n>Document identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1373" seq="">
        <n>Document status code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1366" seq="">
        <n>Document source description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
      <child name="3453" seq="">
        <n>Language name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1056" seq="">
        <n>Version identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1060" seq="">
        <n>Revision identifier</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
    </composite>
    <element>
      <child name="1312" seq="030">
        <n>CONSIGNMENT LOAD SEQUENCE IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>4</length>
      </child>
    </element>
    <!--CNI-->
  </xsl:template>
  <xsl:template name="CNT">
    <code>CNT</code>
    <title>CONTROL TOTAL</title>
    <function>
      <n>To provide control total.</n>
    </function>
    <composite>
      <data name="C270" seq="010">
        <n>CONTROL</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6069" seq="">
        <n>Control total type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6066" seq="">
        <n>Control total quantity</n>
        <min>1</min>
        <type>n</type>
        <length>18</length>
      </child>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
    </composite>
    <!--CNT-->
  </xsl:template>
  <xsl:template name="COD">
    <code>COD</code>
    <title>COMPONENT DETAILS</title>
    <function>
      <n>To provide component details of an object (e.g.</n>
      <n>product, container) such as its type and the</n>
      <n>material of which it is composed.</n>
    </function>
    <composite>
      <data name="C823" seq="010">
        <n>TYPE OF UNIT/COMPONENT</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7505" seq="">
        <n>Unit or component type description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7504" seq="">
        <n>Unit or component type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C824" seq="020">
        <n>COMPONENT MATERIAL</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7507" seq="">
        <n>Component material description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7506" seq="">
        <n>Component material description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--COD-->
  </xsl:template>
  <xsl:template name="COM">
    <code>COM</code>
    <title>COMMUNICATION CONTACT</title>
    <function>
      <n>To identify a communication number of a</n>
      <n>department or a person to whom communication</n>
      <n>should be directed.</n>
    </function>
    <composite>
      <data name="C076" seq="010">
        <n>COMMUNICATION CONTACT</n>
        <min>1</min>
        <max>3</max>
        <type/>
        <length/>
      </data>
      <child name="3148" seq="">
        <n>Communication address identifier</n>
        <min>1</min>
        <type>an</type>
        <length>512</length>
      </child>
      <child name="3155" seq="">
        <n>Communication means type code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--COM-->
  </xsl:template>
  <xsl:template name="COT">
    <code>COT</code>
    <title>CONTRIBUTION DETAILS</title>
    <function>
      <n>To specify details about membership</n>
      <n>contributions.</n>
    </function>
    <element>
      <child name="5047" seq="010">
        <n>CONTRIBUTION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C953" seq="020">
        <n>CONTRIBUTION TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5049" seq="">
        <n>Contribution type description code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5048" seq="">
        <n>Contribution type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C522" seq="030">
        <n>INSTRUCTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4403" seq="">
        <n>Instruction type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4401" seq="">
        <n>Instruction description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4400" seq="">
        <n>Instruction description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C203" seq="040">
        <n>RATE/TARIFF CLASS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5243" seq="">
        <n>Rate or tariff class description code</n>
        <min>1</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5242" seq="">
        <n>Rate or tariff class description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="5275" seq="">
        <n>Supplementary rate or tariff code</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5275" seq="">
        <n>Supplementary rate or tariff code</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C960" seq="050">
        <n>REASON FOR CHANGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4295" seq="">
        <n>Change reason description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4294" seq="">
        <n>Change reason description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--COT-->
  </xsl:template>
  <xsl:template name="CPI">
    <code>CPI</code>
    <title>CHARGE PAYMENT INSTRUCTIONS</title>
    <function>
      <n>To identify a charge.</n>
    </function>
    <composite>
      <data name="C229" seq="010">
        <n>CHARGE CATEGORY</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5237" seq="">
        <n>Charge category code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C231" seq="020">
        <n>METHOD OF PAYMENT</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4215" seq="">
        <n>Transport charges payment method code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4237" seq="030">
        <n>PAYMENT ARRANGEMENT CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--CPI-->
  </xsl:template>
  <xsl:template name="CPS">
    <code>CPS</code>
    <title>CONSIGNMENT PACKING SEQUENCE</title>
    <function>
      <n>To identify the sequence in which physical</n>
      <n>packing is presented in the consignment, and</n>
      <n>optionally to identify the hierarchical</n>
      <n>relationship between packing layers.</n>
    </function>
    <element>
      <child name="7164" seq="010">
        <n>HIERARCHICAL STRUCTURE LEVEL IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="7166" seq="020">
        <n>HIERARCHICAL STRUCTURE PARENT IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="7075" seq="030">
        <n>PACKAGING LEVEL CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--CPS-->
  </xsl:template>
  <xsl:template name="CPT">
    <code>CPT</code>
    <title>ACCOUNT IDENTIFICATION</title>
    <function>
      <n>To provide account identification information.</n>
    </function>
    <element>
      <child name="4437" seq="010">
        <n>ACCOUNT TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C593" seq="020">
        <n>ACCOUNT IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1147" seq="">
        <n>Account identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1148" seq="">
        <n>Account abbreviated name</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1146" seq="">
        <n>Account name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1146" seq="">
        <n>Account name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="6345" seq="">
        <n>Currency identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--CPT-->
  </xsl:template>
  <xsl:template name="CST">
    <code>CST</code>
    <title>CUSTOMS STATUS OF GOODS</title>
    <function>
      <n>To specify goods in terms of customs identities,</n>
      <n>status and intended use.</n>
    </function>
    <element>
      <child name="1496" seq="010">
        <n>GOODS ITEM NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>5</length>
      </child>
    </element>
    <composite>
      <data name="C246" seq="020">
        <n>CUSTOMS IDENTITY CODES</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7361" seq="">
        <n>Customs goods identifier</n>
        <min>1</min>
        <type>an</type>
        <length>18</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C246" seq="030">
        <n>CUSTOMS IDENTITY CODES</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7361" seq="">
        <n>Customs goods identifier</n>
        <min>1</min>
        <type>an</type>
        <length>18</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C246" seq="040">
        <n>CUSTOMS IDENTITY CODES</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7361" seq="">
        <n>Customs goods identifier</n>
        <min>1</min>
        <type>an</type>
        <length>18</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C246" seq="050">
        <n>CUSTOMS IDENTITY CODES</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7361" seq="">
        <n>Customs goods identifier</n>
        <min>1</min>
        <type>an</type>
        <length>18</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C246" seq="060">
        <n>CUSTOMS IDENTITY CODES</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7361" seq="">
        <n>Customs goods identifier</n>
        <min>1</min>
        <type>an</type>
        <length>18</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--CST-->
  </xsl:template>
  <xsl:template name="CTA">
    <code>CTA</code>
    <title>CONTACT INFORMATION</title>
    <function>
      <n>To identify a person or a department to whom</n>
      <n>communication should be directed.</n>
    </function>
    <element>
      <child name="3139" seq="010">
        <n>CONTACT FUNCTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C056" seq="020">
        <n>CONTACT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3413" seq="">
        <n>Contact identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3412" seq="">
        <n>Contact name</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <!--CTA-->
  </xsl:template>
  <xsl:template name="CUX">
    <code>CUX</code>
    <title>CURRENCIES</title>
    <function>
      <n>To specify currencies used in the transaction</n>
      <n>and relevant details for the rate of exchange.</n>
    </function>
    <composite>
      <data name="C504" seq="010">
        <n>CURRENCY DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6347" seq="">
        <n>Currency usage code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6345" seq="">
        <n>Currency identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6343" seq="">
        <n>Currency type code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6348" seq="">
        <n>Currency rate</n>
        <min>0</min>
        <type>n</type>
        <length>4</length>
      </child>
    </composite>
    <composite>
      <data name="C504" seq="020">
        <n>CURRENCY DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6347" seq="">
        <n>Currency usage code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6345" seq="">
        <n>Currency identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6343" seq="">
        <n>Currency type code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6348" seq="">
        <n>Currency rate</n>
        <min>0</min>
        <type>n</type>
        <length>4</length>
      </child>
    </composite>
    <element>
      <child name="5402" seq="030">
        <n>CURRENCY EXCHANGE RATE</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>12</length>
      </child>
    </element>
    <element>
      <child name="6341" seq="040">
        <n>EXCHANGE RATE CURRENCY MARKET IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--CUX-->
  </xsl:template>
  <xsl:template name="DAM">
    <code>DAM</code>
    <title>DAMAGE</title>
    <function>
      <n>To specify damage including action taken.</n>
    </function>
    <element>
      <child name="7493" seq="010">
        <n>DAMAGE DETAILS CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C821" seq="020">
        <n>TYPE OF DAMAGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7501" seq="">
        <n>Damage type description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7500" seq="">
        <n>Damage type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C822" seq="030">
        <n>DAMAGE AREA</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7503" seq="">
        <n>Damage area description code</n>
        <min>0</min>
        <type>an</type>
        <length>4</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7502" seq="">
        <n>Damage area description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C825" seq="040">
        <n>DAMAGE SEVERITY</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7509" seq="">
        <n>Damage severity description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7508" seq="">
        <n>Damage severity description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C826" seq="050">
        <n>ACTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1229" seq="">
        <n>Action code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1228" seq="">
        <n>Action description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--DAM-->
  </xsl:template>
  <xsl:template name="DFN">
    <code>DFN</code>
    <title>DEFINITION FUNCTION</title>
    <function>
      <n>To specify a definition function.</n>
    </function>
    <element>
      <child name="9023" seq="010">
        <n>DEFINITION FUNCTION CODE</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9025" seq="020">
        <n>DEFINITION EXTENT CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4519" seq="030">
        <n>DEFINITION IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <!--DFN-->
  </xsl:template>
  <xsl:template name="DGS">
    <code>DGS</code>
    <title>DANGEROUS GOODS</title>
    <function>
      <n>To identify dangerous goods.</n>
    </function>
    <element>
      <child name="8273" seq="010">
        <n>DANGEROUS GOODS REGULATIONS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C205" seq="020">
        <n>HAZARD CODE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8351" seq="">
        <n>Hazard identification code</n>
        <min>1</min>
        <type>an</type>
        <length>7</length>
      </child>
      <child name="8078" seq="">
        <n>Additional hazard classification</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an</type>
        <length>7</length>
      </child>
      <child name="8092" seq="">
        <n>Hazard code version identifier</n>
        <min>0</min>
        <type>an</type>
        <length>10</length>
      </child>
    </composite>
    <composite>
      <data name="C234" seq="030">
        <n>UNDG INFORMATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7124" seq="">
        <n>United Nations Dangerous Goods (UNDG)</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>n4</type>
        <length/>
      </child>
      <child name="7088" seq="">
        <n>Dangerous goods flashpoint description</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
    </composite>
    <composite>
      <data name="C223" seq="040">
        <n>DANGEROUS GOODS SHIPMENT FLASHPOINT</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7106" seq="">
        <n>Shipment flashpoint degree</n>
        <min>0</min>
        <type>n3</type>
        <length/>
      </child>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
    </composite>
    <element>
      <child name="8339" seq="050">
        <n>PACKAGING DANGER LEVEL CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="8364" seq="060">
        <n>EMERGENCY PROCEDURE FOR SHIPS IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>6</length>
      </child>
    </element>
    <element>
      <child name="8410" seq="070">
        <n>HAZARD MEDICAL FIRST AID GUIDE IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>4</length>
      </child>
    </element>
    <element>
      <child name="8126" seq="080">
        <n>TRANSPORT EMERGENCY CARD IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>10</length>
      </child>
    </element>
    <composite>
      <data name="C235" seq="090">
        <n>HAZARD IDENTIFICATION PLACARD DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8158" seq="">
        <n>Orange hazard placard upper part</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an</type>
        <length>4</length>
      </child>
      <child name="8186" seq="">
        <n>Orange hazard placard lower part</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an4</type>
        <length/>
      </child>
    </composite>
    <composite>
      <data name="C236" seq="100">
        <n>DANGEROUS GOODS LABEL</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8246" seq="">
        <n>Dangerous goods marking identifier</n>
        <min>0</min>
        <type>an</type>
        <length>4</length>
      </child>
      <child name="8246" seq="">
        <n>Dangerous goods marking identifier</n>
        <min>0</min>
        <type>an</type>
        <length>4</length>
      </child>
      <child name="8246" seq="">
        <n>Dangerous goods marking identifier</n>
        <min>0</min>
        <type>an</type>
        <length>4</length>
      </child>
    </composite>
    <element>
      <child name="8255" seq="110">
        <n>PACKING INSTRUCTION TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="8179" seq="120">
        <n>TRANSPORT MEANS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>8</length>
      </child>
    </element>
    <element>
      <child name="8211" seq="130">
        <n>HAZARDOUS CARGO TRANSPORT AUTHORISATION</n>
      </child>
      <child name="" seq="">
        <n>CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--DGS-->
  </xsl:template>
  <xsl:template name="DII">
    <code>DII</code>
    <title>DIRECTORY IDENTIFICATION</title>
    <function>
      <n>To identify a directory and to give its release,</n>
      <n>status, controlling agency, language and</n>
      <n>maintenance operation.</n>
    </function>
    <element>
      <child name="1056" seq="010">
        <n>VERSION IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>9</length>
      </child>
    </element>
    <element>
      <child name="1058" seq="020">
        <n>RELEASE IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>9</length>
      </child>
    </element>
    <element>
      <child name="9148" seq="030">
        <n>DIRECTORY STATUS IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1476" seq="040">
        <n>CONTROLLING AGENCY IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>2</length>
      </child>
    </element>
    <element>
      <child name="3453" seq="050">
        <n>LANGUAGE NAME CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="060">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--DII-->
  </xsl:template>
  <xsl:template name="DIM">
    <code>DIM</code>
    <title>DIMENSIONS</title>
    <function>
      <n>To specify dimensions.</n>
    </function>
    <element>
      <child name="6145" seq="010">
        <n>DIMENSION TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C211" seq="020">
        <n>DIMENSIONS</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>1</min>
        <type>an</type>
        <length>8</length>
      </child>
      <child name="6168" seq="">
        <n>Length measure</n>
        <min>0</min>
        <type>n</type>
        <length>15</length>
      </child>
      <child name="6140" seq="">
        <n>Width measure</n>
        <min>0</min>
        <type>n</type>
        <length>15</length>
      </child>
      <child name="6008" seq="">
        <n>Height measure</n>
        <min>0</min>
        <type>n</type>
        <length>15</length>
      </child>
    </composite>
    <!--DIM-->
  </xsl:template>
  <xsl:template name="DLI">
    <code>DLI</code>
    <title>DOCUMENT LINE IDENTIFICATION</title>
    <function>
      <n>To specify the processing mode of a specific</n>
      <n>line within a referenced document.</n>
    </function>
    <element>
      <child name="1073" seq="010">
        <n>DOCUMENT LINE ACTION CODE</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1082" seq="020">
        <n>LINE ITEM IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>6</length>
      </child>
    </element>
    <!--DLI-->
  </xsl:template>
  <xsl:template name="DLM">
    <code>DLM</code>
    <title>DELIVERY LIMITATIONS</title>
    <function>
      <n>To specify limitations on deliveries.</n>
    </function>
    <element>
      <child name="4455" seq="010">
        <n>BACK ORDER ARRANGEMENT TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C522" seq="020">
        <n>INSTRUCTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4403" seq="">
        <n>Instruction type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4401" seq="">
        <n>Instruction description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4400" seq="">
        <n>Instruction description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C214" seq="030">
        <n>SPECIAL SERVICES IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7161" seq="">
        <n>Special service description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7160" seq="">
        <n>Special service description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7160" seq="">
        <n>Special service description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="4457" seq="040">
        <n>SUBSTITUTION CONDITION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--DLM-->
  </xsl:template>
  <xsl:template name="DMS">
    <code>DMS</code>
    <title>DOCUMENT</title>
    <function>
      <n>To specify summary information relating to the</n>
      <n>document/message.</n>
    </function>
    <composite>
      <data name="C106" seq="010">
        <n>DOCUMENT/MESSAGE IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1004" seq="">
        <n>Document identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1056" seq="">
        <n>Version identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1060" seq="">
        <n>Revision identifier</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
    </composite>
    <composite>
      <data name="C002" seq="020">
        <n>DOCUMENT/MESSAGE NAME</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1001" seq="">
        <n>Document name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1000" seq="">
        <n>Document name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="7240" seq="030">
        <n>ITEM TOTAL QUANTITY</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>15</length>
      </child>
    </element>
    <!--DMS-->
  </xsl:template>
  <xsl:template name="DOC">
    <code>DOC</code>
    <title>DOCUMENT</title>
    <function>
      <n>To identify documents and details directly</n>
      <n>related to it.</n>
    </function>
    <composite>
      <data name="C002" seq="010">
        <n>DOCUMENT/MESSAGE NAME</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1001" seq="">
        <n>Document name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1000" seq="">
        <n>Document name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C503" seq="020">
        <n>DOCUMENT/MESSAGE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1004" seq="">
        <n>Document identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1373" seq="">
        <n>Document status code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1366" seq="">
        <n>Document source description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
      <child name="3453" seq="">
        <n>Language name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1056" seq="">
        <n>Version identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1060" seq="">
        <n>Revision identifier</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
    </composite>
    <element>
      <child name="3153" seq="030">
        <n>COMMUNICATION MEDIUM TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1220" seq="040">
        <n>DOCUMENT COPIES REQUIRED QUANTITY</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>2</length>
      </child>
    </element>
    <element>
      <child name="1218" seq="050">
        <n>DOCUMENT ORIGINALS REQUIRED QUANTITY</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>2</length>
      </child>
    </element>
    <!--DOC-->
  </xsl:template>
  <xsl:template name="DRD">
    <code>DRD</code>
    <title>DATA REPRESENTATION DETAILS</title>
    <function>
      <n>To specify the details of the data</n>
      <n>representation.</n>
    </function>
    <element>
      <child name="7512" seq="010">
        <n>STRUCTURE COMPONENT IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="7515" seq="020">
        <n>STRUCTURE TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9169" seq="030">
        <n>DATA REPRESENTATION TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="6174" seq="040">
        <n>SIZE MEASURE</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>15</length>
      </child>
    </element>
    <!--DRD-->
  </xsl:template>
  <xsl:template name="DSG">
    <code>DSG</code>
    <title>DOSAGE ADMINISTRATION</title>
    <function>
      <n>To communicate how dose(s) are administered.</n>
    </function>
    <element>
      <child name="6085" seq="010">
        <n>DOSAGE ADMINISTRATION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C838" seq="020">
        <n>DOSAGE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6083" seq="">
        <n>Dosage description identifier</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6082" seq="">
        <n>Dosage description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <!--DSG-->
  </xsl:template>
  <xsl:template name="DSI">
    <code>DSI</code>
    <title>DATA SET IDENTIFICATION</title>
    <function>
      <n>To identify a data set.</n>
    </function>
    <composite>
      <data name="C782" seq="010">
        <n>DATA SET IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1520" seq="">
        <n>Data set identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C082" seq="020">
        <n>PARTY IDENTIFICATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3039" seq="">
        <n>Party identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4405" seq="030">
        <n>STATUS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C286" seq="040">
        <n>SEQUENCE INFORMATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1050" seq="">
        <n>Sequence position identifier</n>
        <min>1</min>
        <type>an</type>
        <length>10</length>
      </child>
      <child name="1159" seq="">
        <n>Sequence identifier source code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="1060" seq="050">
        <n>REVISION IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>6</length>
      </child>
    </element>
    <!--DSI-->
  </xsl:template>
  <xsl:template name="DTM">
    <code>DTM</code>
    <title>DATE</title>
    <function>
      <n>To specify date, and/or time, or period.</n>
    </function>
    <composite>
      <data name="C507" seq="010">
        <n>DATE/TIME/PERIOD</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="2005" seq="">
        <n>Date or time or period function code</n>
      </child>
      <child name="" seq="">
        <n>qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="2380" seq="">
        <n>Date or time or period text</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="2379" seq="">
        <n>Date or time or period format code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--DTM-->
  </xsl:template>
  <xsl:template name="EDT">
    <code>EDT</code>
    <title>EDITING DETAILS</title>
    <function>
      <n>To specify editing details.</n>
    </function>
    <element>
      <child name="6178" seq="010">
        <n>EDIT FIELD LENGTH MEASURE</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9026" seq="020">
        <n>EDIT MASK FORMAT IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="9031" seq="030">
        <n>EDIT MASK REPRESENTATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4447" seq="040">
        <n>FREE TEXT FORMAT CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--EDT-->
  </xsl:template>
  <xsl:template name="EFI">
    <code>EFI</code>
    <title>EXTERNAL FILE LINK IDENTIFICATION</title>
    <function>
      <n>To specify the link of one non-EDIFACT external</n>
      <n>file to an EDIFACT message.</n>
    </function>
    <composite>
      <data name="C077" seq="010">
        <n>FILE IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1508" seq="">
        <n>File name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7008" seq="">
        <n>Item description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <composite>
      <data name="C099" seq="020">
        <n>FILE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1516" seq="">
        <n>File format name</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1056" seq="">
        <n>Version identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1503" seq="">
        <n>Data format description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1502" seq="">
        <n>Data format description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="1050" seq="030">
        <n>SEQUENCE POSITION IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>10</length>
      </child>
    </element>
    <element>
      <child name="9450" seq="040">
        <n>FILE COMPRESSION TECHNIQUE NAME</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <!--EFI-->
  </xsl:template>
  <xsl:template name="ELM">
    <code>ELM</code>
    <title>SIMPLE DATA ELEMENT DETAILS</title>
    <function>
      <n>To identify a simple data element and give</n>
      <n>related details.</n>
    </function>
    <element>
      <child name="9150" seq="010">
        <n>SIMPLE DATA ELEMENT TAG IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>4</length>
      </child>
    </element>
    <element>
      <child name="9153" seq="020">
        <n>SIMPLE DATA ELEMENT CHARACTER</n>
      </child>
      <child name="" seq="">
        <n>REPRESENTATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9155" seq="030">
        <n>LENGTH TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9156" seq="040">
        <n>SIMPLE DATA ELEMENT MAXIMUM LENGTH MEASURE</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9158" seq="050">
        <n>SIMPLE DATA ELEMENT MINIMUM LENGTH MEASURE</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9161" seq="060">
        <n>CODE SET INDICATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1507" seq="070">
        <n>DESIGNATED CLASS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="080">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="6432" seq="090">
        <n>SIGNIFICANT DIGITS QUANTITY</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>2</length>
      </child>
    </element>
    <!--ELM-->
  </xsl:template>
  <xsl:template name="ELU">
    <code>ELU</code>
    <title>DATA ELEMENT USAGE DETAILS</title>
    <function>
      <n>To specify the usage of a data element.</n>
    </function>
    <element>
      <child name="9162" seq="010">
        <n>DATA ELEMENT TAG IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>4</length>
      </child>
    </element>
    <element>
      <child name="7299" seq="020">
        <n>REQUIREMENT DESIGNATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1050" seq="030">
        <n>SEQUENCE POSITION IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>10</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="040">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="6176" seq="050">
        <n>OCCURRENCES MAXIMUM NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>7</length>
      </child>
    </element>
    <element>
      <child name="9453" seq="060">
        <n>CODE VALUE SOURCE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9285" seq="070">
        <n>VALIDATION CRITERIA CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9175" seq="080">
        <n>DATA ELEMENT USAGE TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--ELU-->
  </xsl:template>
  <xsl:template name="ELV">
    <code>ELV</code>
    <title>ELEMENT VALUE DEFINITION</title>
    <function>
      <n>To define an element value.</n>
    </function>
    <element>
      <child name="9029" seq="010">
        <n>VALUE DEFINITION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9422" seq="020">
        <n>VALUE TEXT</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>512</length>
      </child>
    </element>
    <element>
      <child name="7299" seq="030">
        <n>REQUIREMENT DESIGNATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="040">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--ELV-->
  </xsl:template>
  <xsl:template name="EMP">
    <code>EMP</code>
    <title>EMPLOYMENT DETAILS</title>
    <function>
      <n>To specify employment details.</n>
    </function>
    <element>
      <child name="9003" seq="010">
        <n>EMPLOYMENT DETAILS CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C948" seq="020">
        <n>EMPLOYMENT CATEGORY</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9005" seq="">
        <n>Employment category description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9004" seq="">
        <n>Employment category description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C951" seq="030">
        <n>OCCUPATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9009" seq="">
        <n>Occupation description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9008" seq="">
        <n>Occupation description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="9008" seq="">
        <n>Occupation description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C950" seq="040">
        <n>QUALIFICATION CLASSIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9007" seq="">
        <n>Qualification classification description</n>
      </child>
      <child name="" seq="">
        <n>code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9006" seq="">
        <n>Qualification classification description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="9006" seq="">
        <n>Qualification classification description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="3480" seq="050">
        <n>PERSON JOB TITLE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="9035" seq="060">
        <n>QUALIFICATION APPLICATION AREA CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--EMP-->
  </xsl:template>
  <xsl:template name="EQA">
    <code>EQA</code>
    <title>ATTACHED EQUIPMENT</title>
    <function>
      <n>To specify attached or related equipment.</n>
    </function>
    <element>
      <child name="8053" seq="010">
        <n>EQUIPMENT TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C237" seq="020">
        <n>EQUIPMENT IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8260" seq="">
        <n>Equipment identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3207" seq="">
        <n>Country identifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--EQA-->
  </xsl:template>
  <xsl:template name="EQD">
    <code>EQD</code>
    <title>EQUIPMENT DETAILS</title>
    <function>
      <n>To identify a unit of equipment.</n>
    </function>
    <element>
      <child name="8053" seq="010">
        <n>EQUIPMENT TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C237" seq="020">
        <n>EQUIPMENT IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8260" seq="">
        <n>Equipment identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3207" seq="">
        <n>Country identifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C224" seq="030">
        <n>EQUIPMENT SIZE AND TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8155" seq="">
        <n>Equipment size and type description code</n>
        <min>0</min>
        <type>an</type>
        <length>10</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="8154" seq="">
        <n>Equipment size and type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="8077" seq="040">
        <n>EQUIPMENT SUPPLIER CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="8249" seq="050">
        <n>EQUIPMENT STATUS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="8169" seq="060">
        <n>FULL OR EMPTY INDICATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4233" seq="070">
        <n>MARKING INSTRUCTIONS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--EQD-->
  </xsl:template>
  <xsl:template name="EQN">
    <code>EQN</code>
    <title>NUMBER OF UNITS</title>
    <function>
      <n>To specify the number of units.</n>
    </function>
    <composite>
      <data name="C523" seq="010">
        <n>NUMBER OF UNIT DETAILS</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6350" seq="">
        <n>Units quantity</n>
        <min>0</min>
        <type>n</type>
        <length>15</length>
      </child>
      <child name="6353" seq="">
        <n>Unit type code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--EQN-->
  </xsl:template>
  <xsl:template name="ERC">
    <code>ERC</code>
    <title>APPLICATION ERROR INFORMATION</title>
    <function>
      <n>To identify the type of application error within</n>
      <n>a message.</n>
    </function>
    <composite>
      <data name="C901" seq="010">
        <n>APPLICATION ERROR DETAIL</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9321" seq="">
        <n>Application error code</n>
        <min>1</min>
        <type>an</type>
        <length>8</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--ERC-->
  </xsl:template>
  <xsl:template name="ERP">
    <code>ERP</code>
    <title>ERROR POINT DETAILS</title>
    <function>
      <n>A segment to identify the location of an</n>
      <n>application error within a message.</n>
    </function>
    <composite>
      <data name="C701" seq="010">
        <n>ERROR POINT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1049" seq="">
        <n>Message section code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1052" seq="">
        <n>Message item identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1054" seq="">
        <n>Message sub-item identifier</n>
        <min>0</min>
        <type>n</type>
        <length>6</length>
      </child>
    </composite>
    <composite>
      <data name="C853" seq="020">
        <n>ERROR SEGMENT POINT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9166" seq="">
        <n>Segment tag identifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1050" seq="">
        <n>Sequence position identifier</n>
        <min>0</min>
        <type>an</type>
        <length>10</length>
      </child>
      <child name="1159" seq="">
        <n>Sequence identifier source code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--ERP-->
  </xsl:template>
  <xsl:template name="EVE">
    <code>EVE</code>
    <title>EVENT</title>
    <function>
      <n>To specify details about events.</n>
    </function>
    <element>
      <child name="9635" seq="010">
        <n>EVENT DETAILS CODE QUALIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C004" seq="020">
        <n>EVENT CATEGORY</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9637" seq="">
        <n>Event category description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9636" seq="">
        <n>Event category description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C030" seq="030">
        <n>EVENT TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9171" seq="">
        <n>Event type description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9170" seq="">
        <n>Event type description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C063" seq="040">
        <n>EVENT IDENTIFICATION</n>
        <min>0</min>
        <max>5</max>
        <type/>
        <length/>
      </data>
      <child name="9173" seq="">
        <n>Event description code</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9172" seq="">
        <n>Event</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <element>
      <child name="1229" seq="050">
        <n>ACTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--EVE-->
  </xsl:template>
  <xsl:template name="FCA">
    <code>FCA</code>
    <title>FINANCIAL CHARGES ALLOCATION</title>
    <function>
      <n>Description of allocation of charges.</n>
    </function>
    <element>
      <child name="4471" seq="010">
        <n>SETTLEMENT MEANS CODE</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C878" seq="020">
        <n>CHARGE/ALLOWANCE ACCOUNT</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3434" seq="">
        <n>Institution branch identifier</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3194" seq="">
        <n>Account holder identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="6345" seq="">
        <n>Currency identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--FCA-->
  </xsl:template>
  <xsl:template name="FII">
    <code>FII</code>
    <title>FINANCIAL INSTITUTION INFORMATION</title>
    <function>
      <n>To identify an account and a related financial</n>
      <n>institution.</n>
    </function>
    <element>
      <child name="3035" seq="010">
        <n>PARTY FUNCTION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C078" seq="020">
        <n>ACCOUNT HOLDER IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3194" seq="">
        <n>Account holder identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3192" seq="">
        <n>Account holder name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3192" seq="">
        <n>Account holder name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="6345" seq="">
        <n>Currency identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C088" seq="030">
        <n>INSTITUTION IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3433" seq="">
        <n>Institution name code</n>
        <min>0</min>
        <type>an</type>
        <length>11</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3434" seq="">
        <n>Institution branch identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3432" seq="">
        <n>Institution name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
      <child name="3436" seq="">
        <n>Institution branch location name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <element>
      <child name="3207" seq="040">
        <n>COUNTRY IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--FII-->
  </xsl:template>
  <xsl:template name="FNS">
    <code>FNS</code>
    <title>FOOTNOTE SET</title>
    <function>
      <n>To identify a set of footnotes.</n>
    </function>
    <composite>
      <data name="C783" seq="010">
        <n>FOOTNOTE SET IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9430" seq="">
        <n>Footnote set identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C082" seq="020">
        <n>PARTY IDENTIFICATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3039" seq="">
        <n>Party identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4405" seq="030">
        <n>STATUS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="040">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--FNS-->
  </xsl:template>
  <xsl:template name="FNT">
    <code>FNT</code>
    <title>FOOTNOTE</title>
    <function>
      <n>To identify a footnote.</n>
    </function>
    <composite>
      <data name="C784" seq="010">
        <n>FOOTNOTE IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9432" seq="">
        <n>Footnote identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C082" seq="020">
        <n>PARTY IDENTIFICATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3039" seq="">
        <n>Party identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4405" seq="030">
        <n>STATUS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="040">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--FNT-->
  </xsl:template>
  <xsl:template name="FOR">
    <code>FOR</code>
    <title>FORMULA</title>
    <function>
      <n>To identify a formula.</n>
    </function>
    <element>
      <child name="9501" seq="010">
        <n>FORMULA TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="7402" seq="020">
        <n>OBJECT IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="9502" seq="030">
        <n>FORMULA NAME</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="4440" seq="040">
        <n>FREE TEXT</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>512</length>
      </child>
    </element>
    <composite>
      <data name="C961" seq="050">
        <n>FORMULA COMPLEXITY</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9505" seq="">
        <n>Formula complexity code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--FOR-->
  </xsl:template>
  <xsl:template name="FSQ">
    <code>FSQ</code>
    <title>FORMULA SEQUENCE</title>
    <function>
      <n>To provide a single operation within the</n>
      <n>sequence of operations of a formula.</n>
    </function>
    <element>
      <child name="9507" seq="010">
        <n>FORMULA SEQUENCE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="9509" seq="020">
        <n>FORMULA SEQUENCE OPERAND CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>17</length>
      </child>
    </element>
    <element>
      <child name="1050" seq="030">
        <n>SEQUENCE POSITION IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>10</length>
      </child>
    </element>
    <element>
      <child name="9510" seq="040">
        <n>FORMULA SEQUENCE NAME</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="4440" seq="050">
        <n>FREE TEXT</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>512</length>
      </child>
    </element>
    <!--FSQ-->
  </xsl:template>
  <xsl:template name="FTX">
    <code>FTX</code>
    <title>FREE TEXT</title>
    <function>
      <n>To provide free form or coded text information.</n>
    </function>
    <element>
      <child name="4451" seq="010">
        <n>TEXT SUBJECT CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4453" seq="020">
        <n>FREE TEXT FUNCTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C107" seq="030">
        <n>TEXT REFERENCE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4441" seq="">
        <n>Free text description code</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C108" seq="040">
        <n>TEXT LITERAL</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4440" seq="">
        <n>Free text</n>
        <min>1</min>
        <type>an</type>
        <length>512</length>
      </child>
      <child name="4440" seq="">
        <n>Free text</n>
        <min>0</min>
        <type>an</type>
        <length>512</length>
      </child>
      <child name="4440" seq="">
        <n>Free text</n>
        <min>0</min>
        <type>an</type>
        <length>512</length>
      </child>
      <child name="4440" seq="">
        <n>Free text</n>
        <min>0</min>
        <type>an</type>
        <length>512</length>
      </child>
      <child name="4440" seq="">
        <n>Free text</n>
        <min>0</min>
        <type>an</type>
        <length>512</length>
      </child>
    </composite>
    <element>
      <child name="3453" seq="050">
        <n>LANGUAGE NAME CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4447" seq="060">
        <n>FREE TEXT FORMAT CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--FTX-->
  </xsl:template>
  <xsl:template name="GDS">
    <code>GDS</code>
    <title>NATURE OF CARGO</title>
    <function>
      <n>To indicate the type of cargo as a general</n>
      <n>classification.</n>
    </function>
    <composite>
      <data name="C703" seq="010">
        <n>NATURE OF CARGO</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7085" seq="">
        <n>Cargo type classification code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--GDS-->
  </xsl:template>
  <xsl:template name="GEI">
    <code>GEI</code>
    <title>PROCESSING INFORMATION</title>
    <function>
      <n>To identify processing information.</n>
    </function>
    <element>
      <child name="9649" seq="010">
        <n>PROCESSING INFORMATION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C012" seq="020">
        <n>PROCESSING INDICATOR</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7365" seq="">
        <n>Processing indicator description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7364" seq="">
        <n>Processing indicator description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="7187" seq="030">
        <n>PROCESS TYPE DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>17</length>
      </child>
    </element>
    <!--GEI-->
  </xsl:template>
  <xsl:template name="GID">
    <code>GID</code>
    <title>GOODS ITEM DETAILS</title>
    <function>
      <n>To indicate totals of a goods item.</n>
    </function>
    <element>
      <child name="1496" seq="010">
        <n>GOODS ITEM NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>5</length>
      </child>
    </element>
    <composite>
      <data name="C213" seq="020">
        <n>NUMBER AND TYPE OF PACKAGES</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7224" seq="">
        <n>Package quantity</n>
        <min>0</min>
        <type>n</type>
        <length>8</length>
      </child>
      <child name="7065" seq="">
        <n>Package type description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7064" seq="">
        <n>Type of packages</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7233" seq="">
        <n>Packaging related description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C213" seq="030">
        <n>NUMBER AND TYPE OF PACKAGES</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7224" seq="">
        <n>Package quantity</n>
        <min>0</min>
        <type>n</type>
        <length>8</length>
      </child>
      <child name="7065" seq="">
        <n>Package type description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7064" seq="">
        <n>Type of packages</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7233" seq="">
        <n>Packaging related description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C213" seq="040">
        <n>NUMBER AND TYPE OF PACKAGES</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7224" seq="">
        <n>Package quantity</n>
        <min>0</min>
        <type>n</type>
        <length>8</length>
      </child>
      <child name="7065" seq="">
        <n>Package type description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7064" seq="">
        <n>Type of packages</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7233" seq="">
        <n>Packaging related description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C213" seq="050">
        <n>NUMBER AND TYPE OF PACKAGES</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7224" seq="">
        <n>Package quantity</n>
        <min>0</min>
        <type>n</type>
        <length>8</length>
      </child>
      <child name="7065" seq="">
        <n>Package type description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7064" seq="">
        <n>Type of packages</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7233" seq="">
        <n>Packaging related description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C213" seq="060">
        <n>NUMBER AND TYPE OF PACKAGES</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7224" seq="">
        <n>Package quantity</n>
        <min>0</min>
        <type>n</type>
        <length>8</length>
      </child>
      <child name="7065" seq="">
        <n>Package type description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7064" seq="">
        <n>Type of packages</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7233" seq="">
        <n>Packaging related description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--GID-->
  </xsl:template>
  <xsl:template name="GIN">
    <code>GIN</code>
    <title>GOODS IDENTITY NUMBER</title>
    <function>
      <n>To give specific identification numbers, either</n>
      <n>as single numbers or ranges.</n>
    </function>
    <element>
      <child name="7405" seq="010">
        <n>OBJECT IDENTIFICATION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C208" seq="020">
        <n>IDENTITY NUMBER RANGE</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C208" seq="030">
        <n>IDENTITY NUMBER RANGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C208" seq="040">
        <n>IDENTITY NUMBER RANGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C208" seq="050">
        <n>IDENTITY NUMBER RANGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C208" seq="060">
        <n>IDENTITY NUMBER RANGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--GIN-->
  </xsl:template>
  <xsl:template name="GIR">
    <code>GIR</code>
    <title>RELATED IDENTIFICATION NUMBERS</title>
    <function>
      <n>To specify a related set of identification</n>
      <n>numbers.</n>
    </function>
    <element>
      <child name="7297" seq="010">
        <n>SET TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C206" seq="020">
        <n>IDENTIFICATION NUMBER</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4405" seq="">
        <n>Status description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C206" seq="030">
        <n>IDENTIFICATION NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4405" seq="">
        <n>Status description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C206" seq="040">
        <n>IDENTIFICATION NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4405" seq="">
        <n>Status description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C206" seq="050">
        <n>IDENTIFICATION NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4405" seq="">
        <n>Status description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C206" seq="060">
        <n>IDENTIFICATION NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4405" seq="">
        <n>Status description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--GIR-->
  </xsl:template>
  <xsl:template name="GOR">
    <code>GOR</code>
    <title>GOVERNMENTAL REQUIREMENTS</title>
    <function>
      <n>To indicate the requirement for a specific</n>
      <n>governmental action and/or procedure or which</n>
      <n>specific procedure is valid for a specific part</n>
      <n>of the transport.</n>
    </function>
    <element>
      <child name="8323" seq="010">
        <n>TRANSPORT MOVEMENT CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C232" seq="020">
        <n>GOVERNMENT ACTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9415" seq="">
        <n>Government agency identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9411" seq="">
        <n>Government involvement code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9417" seq="">
        <n>Government action code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9353" seq="">
        <n>Government procedure code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C232" seq="030">
        <n>GOVERNMENT ACTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9415" seq="">
        <n>Government agency identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9411" seq="">
        <n>Government involvement code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9417" seq="">
        <n>Government action code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9353" seq="">
        <n>Government procedure code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C232" seq="040">
        <n>GOVERNMENT ACTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9415" seq="">
        <n>Government agency identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9411" seq="">
        <n>Government involvement code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9417" seq="">
        <n>Government action code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9353" seq="">
        <n>Government procedure code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C232" seq="050">
        <n>GOVERNMENT ACTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9415" seq="">
        <n>Government agency identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9411" seq="">
        <n>Government involvement code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9417" seq="">
        <n>Government action code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9353" seq="">
        <n>Government procedure code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--GOR-->
  </xsl:template>
  <xsl:template name="GPO">
    <code>GPO</code>
    <title>GEOGRAPHICAL POSITION</title>
    <function>
      <n>To specify a geographical position.</n>
    </function>
    <element>
      <child name="6029" seq="010">
        <n>GEOGRAPHICAL POSITION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="6000" seq="020">
        <n>LATITUDE DEGREE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>10</length>
      </child>
    </element>
    <element>
      <child name="6002" seq="030">
        <n>LONGITUDE DEGREE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>11</length>
      </child>
    </element>
    <element>
      <child name="6096" seq="040">
        <n>ALTITUDE</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>18</length>
      </child>
    </element>
    <!--GPO-->
  </xsl:template>
  <xsl:template name="GRU">
    <code>GRU</code>
    <title>SEGMENT GROUP USAGE DETAILS</title>
    <function>
      <n>To specify the usage of a segment group within a</n>
      <n>message type structure and its maintenance</n>
      <n>operation.</n>
    </function>
    <element>
      <child name="9164" seq="010">
        <n>GROUP IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>4</length>
      </child>
    </element>
    <element>
      <child name="7299" seq="020">
        <n>REQUIREMENT DESIGNATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="6176" seq="030">
        <n>OCCURRENCES MAXIMUM NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>7</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="040">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1050" seq="050">
        <n>SEQUENCE POSITION IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>10</length>
      </child>
    </element>
    <!--GRU-->
  </xsl:template>
  <xsl:template name="HAN">
    <code>HAN</code>
    <title>HANDLING INSTRUCTIONS</title>
    <function>
      <n>To specify handling and where necessary, notify</n>
      <n>hazards.</n>
    </function>
    <composite>
      <data name="C524" seq="010">
        <n>HANDLING INSTRUCTIONS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4079" seq="">
        <n>Handling instruction description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4078" seq="">
        <n>Handling instruction description</n>
        <min>0</min>
        <type>an</type>
        <length>512</length>
      </child>
    </composite>
    <composite>
      <data name="C218" seq="020">
        <n>HAZARDOUS MATERIAL</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7419" seq="">
        <n>Hazardous material category name code</n>
        <min>0</min>
        <type>an</type>
        <length>7</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7418" seq="">
        <n>Hazardous material category name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--HAN-->
  </xsl:template>
  <xsl:template name="HYN">
    <code>HYN</code>
    <title>HIERARCHY INFORMATION</title>
    <function>
      <n>A segment to identify hierarchical connections</n>
      <n>from a given item to a higher or lower levelled</n>
      <n>item or to identify dependencies among the</n>
      <n>content of hierarchically related groups of</n>
      <n>data.</n>
    </function>
    <element>
      <child name="7173" seq="010">
        <n>HIERARCHY OBJECT CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="7171" seq="020">
        <n>HIERARCHICAL STRUCTURE RELATIONSHIP CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1229" seq="030">
        <n>ACTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C212" seq="040">
        <n>ITEM NUMBER IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7140" seq="">
        <n>Item identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7143" seq="">
        <n>Item type identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="7166" seq="050">
        <n>HIERARCHICAL STRUCTURE PARENT IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <!--HYN-->
  </xsl:template>
  <xsl:template name="ICD">
    <code>ICD</code>
    <title>INSURANCE COVER DESCRIPTION</title>
    <function>
      <n>To describe the insurance cover.</n>
    </function>
    <composite>
      <data name="C330" seq="010">
        <n>INSURANCE COVER TYPE</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4497" seq="">
        <n>Insurance cover type code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C331" seq="020">
        <n>INSURANCE COVER DETAILS</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4495" seq="">
        <n>Insurance cover description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4494" seq="">
        <n>Insurance cover description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="4494" seq="">
        <n>Insurance cover description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--ICD-->
  </xsl:template>
  <xsl:template name="IDE">
    <code>IDE</code>
    <title>IDENTITY</title>
    <function>
      <n>To identify an object.</n>
    </function>
    <element>
      <child name="7495" seq="010">
        <n>OBJECT TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C206" seq="020">
        <n>IDENTIFICATION NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4405" seq="">
        <n>Status description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C082" seq="030">
        <n>PARTY IDENTIFICATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3039" seq="">
        <n>Party identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4405" seq="040">
        <n>STATUS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1222" seq="050">
        <n>CONFIGURATION LEVEL NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>2</length>
      </child>
    </element>
    <composite>
      <data name="C778" seq="060">
        <n>POSITION IDENTIFICATION</n>
        <min>0</min>
        <max>99</max>
        <type/>
        <length/>
      </data>
      <child name="7164" seq="">
        <n>Hierarchical structure level identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1050" seq="">
        <n>Sequence position identifier</n>
        <min>0</min>
        <type>an</type>
        <length>10</length>
      </child>
    </composite>
    <composite>
      <data name="C240" seq="070">
        <n>CHARACTERISTIC DESCRIPTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7037" seq="">
        <n>Characteristic description code</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7036" seq="">
        <n>Characteristic description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7036" seq="">
        <n>Characteristic description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--IDE-->
  </xsl:template>
  <xsl:template name="IFD">
    <code>IFD</code>
    <title>INFORMATION DETAIL</title>
    <function>
      <n>To specify details about items of information.</n>
    </function>
    <element>
      <child name="9633" seq="010">
        <n>INFORMATION DETAILS CODE QUALIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C009" seq="020">
        <n>INFORMATION CATEGORY</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9615" seq="">
        <n>Information category description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9614" seq="">
        <n>Information category description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C010" seq="030">
        <n>INFORMATION TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4473" seq="">
        <n>Information type code</n>
        <min>0</min>
        <type>an</type>
        <length>4</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4472" seq="">
        <n>Information type</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C011" seq="040">
        <n>INFORMATION DETAIL</n>
        <min>0</min>
        <max>5</max>
        <type/>
        <length/>
      </data>
      <child name="9617" seq="">
        <n>Information detail description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9616" seq="">
        <n>Information detail description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <element>
      <child name="4405" seq="050">
        <n>STATUS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--IFD-->
  </xsl:template>
  <xsl:template name="IHC">
    <code>IHC</code>
    <title>PERSON CHARACTERISTIC</title>
    <function>
      <n>To specify characteristics of a person such as</n>
      <n>ethnic origin.</n>
    </function>
    <element>
      <child name="3289" seq="010">
        <n>PERSON CHARACTERISTIC CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C818" seq="020">
        <n>PERSON INHERITED CHARACTERISTIC DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3311" seq="">
        <n>Inherited characteristic description code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3310" seq="">
        <n>Inherited characteristic description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <!--IHC-->
  </xsl:template>
  <xsl:template name="IMD">
    <code>IMD</code>
    <title>ITEM DESCRIPTION</title>
    <function>
      <n>To describe an item in either an industry or</n>
      <n>free format.</n>
    </function>
    <element>
      <child name="7077" seq="010">
        <n>DESCRIPTION FORMAT CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C272" seq="020">
        <n>ITEM CHARACTERISTIC</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7081" seq="">
        <n>Item characteristic code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C273" seq="030">
        <n>ITEM DESCRIPTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7009" seq="">
        <n>Item description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7008" seq="">
        <n>Item description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
      <child name="7008" seq="">
        <n>Item description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
      <child name="3453" seq="">
        <n>Language name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="7383" seq="040">
        <n>SURFACE OR LAYER CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--IMD-->
  </xsl:template>
  <xsl:template name="IND">
    <code>IND</code>
    <title>INDEX DETAILS</title>
    <function>
      <n>To specify an index.</n>
    </function>
    <composite>
      <data name="C545" seq="010">
        <n>INDEX IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5013" seq="">
        <n>Index code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5027" seq="">
        <n>Index type identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C546" seq="020">
        <n>INDEX VALUE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5030" seq="">
        <n>Index text</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="5039" seq="">
        <n>Index representation code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--IND-->
  </xsl:template>
  <xsl:template name="INP">
    <code>INP</code>
    <title>PARTIES AND INSTRUCTION</title>
    <function>
      <n>To specify parties to an instruction, the</n>
      <n>instruction, or both.</n>
    </function>
    <composite>
      <data name="C849" seq="010">
        <n>PARTIES TO INSTRUCTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3301" seq="">
        <n>Enacting party identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3285" seq="">
        <n>Instruction receiving party identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C522" seq="020">
        <n>INSTRUCTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4403" seq="">
        <n>Instruction type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4401" seq="">
        <n>Instruction description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4400" seq="">
        <n>Instruction description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C850" seq="030">
        <n>STATUS OF INSTRUCTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4405" seq="">
        <n>Status description code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3036" seq="">
        <n>Party name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="1229" seq="040">
        <n>ACTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--INP-->
  </xsl:template>
  <xsl:template name="INV">
    <code>INV</code>
    <title>INVENTORY MANAGEMENT RELATED DETAILS</title>
    <function>
      <n>To provide the different information related to</n>
      <n>the inventory management functions and needed to</n>
      <n>process properly the inventory movements and the</n>
      <n>inventory balances.</n>
    </function>
    <element>
      <child name="4501" seq="010">
        <n>INVENTORY MOVEMENT DIRECTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="7491" seq="020">
        <n>INVENTORY TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4499" seq="030">
        <n>INVENTORY MOVEMENT REASON CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4503" seq="040">
        <n>INVENTORY BALANCE METHOD CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C522" seq="050">
        <n>INSTRUCTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4403" seq="">
        <n>Instruction type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4401" seq="">
        <n>Instruction description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4400" seq="">
        <n>Instruction description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--INV-->
  </xsl:template>
  <xsl:template name="IRQ">
    <code>IRQ</code>
    <title>INFORMATION REQUIRED</title>
    <function>
      <n>To indicate which information is requested in a</n>
      <n>responding message.</n>
    </function>
    <composite>
      <data name="C333" seq="010">
        <n>INFORMATION REQUEST</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4511" seq="">
        <n>Requested information description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4510" seq="">
        <n>Requested information description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--IRQ-->
  </xsl:template>
  <xsl:template name="LAN">
    <code>LAN</code>
    <title>LANGUAGE</title>
    <function>
      <n>To specify a language.</n>
    </function>
    <element>
      <child name="3455" seq="010">
        <n>LANGUAGE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C508" seq="020">
        <n>LANGUAGE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3453" seq="">
        <n>Language name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3452" seq="">
        <n>Language name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--LAN-->
  </xsl:template>
  <xsl:template name="LIN">
    <code>LIN</code>
    <title>LINE ITEM</title>
    <function>
      <n>To identify a line item and configuration.</n>
    </function>
    <element>
      <child name="1082" seq="010">
        <n>LINE ITEM IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>6</length>
      </child>
    </element>
    <element>
      <child name="1229" seq="020">
        <n>ACTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C212" seq="030">
        <n>ITEM NUMBER IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7140" seq="">
        <n>Item identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7143" seq="">
        <n>Item type identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C829" seq="040">
        <n>SUB-LINE INFORMATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5495" seq="">
        <n>Sub-line indicator code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1082" seq="">
        <n>Line item identifier</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
    </composite>
    <element>
      <child name="1222" seq="050">
        <n>CONFIGURATION LEVEL NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>2</length>
      </child>
    </element>
    <element>
      <child name="7083" seq="060">
        <n>CONFIGURATION OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--LIN-->
  </xsl:template>
  <xsl:template name="LOC">
    <code>LOC</code>
    <title>PLACE</title>
    <function>
      <n>To identify a place or a location and/or related</n>
      <n>locations.</n>
    </function>
    <element>
      <child name="3227" seq="010">
        <n>LOCATION FUNCTION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C517" seq="020">
        <n>LOCATION IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3225" seq="">
        <n>Location identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3224" seq="">
        <n>Location name</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <composite>
      <data name="C519" seq="030">
        <n>RELATED LOCATION ONE IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3223" seq="">
        <n>First related location identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3222" seq="">
        <n>First related location name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C553" seq="040">
        <n>RELATED LOCATION TWO IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3233" seq="">
        <n>Second related location identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3232" seq="">
        <n>Second related location name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <element>
      <child name="5479" seq="050">
        <n>RELATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--LOC-->
  </xsl:template>
  <xsl:template name="MEA">
    <code>MEA</code>
    <title>MEASUREMENTS</title>
    <function>
      <n>To specify physical measurements, including</n>
      <n>dimension tolerances, weights and counts.</n>
    </function>
    <element>
      <child name="6311" seq="010">
        <n>MEASUREMENT PURPOSE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C502" seq="020">
        <n>MEASUREMENT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6313" seq="">
        <n>Measured attribute code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6321" seq="">
        <n>Measurement significance code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6155" seq="">
        <n>Non-discrete measurement name code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="6154" seq="">
        <n>Non-discrete measurement name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C174" seq="030">
        <n>VALUE/RANGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>1</min>
        <type>an</type>
        <length>8</length>
      </child>
      <child name="6314" seq="">
        <n>Measure</n>
        <min>0</min>
        <type>an</type>
        <length>18</length>
      </child>
      <child name="6162" seq="">
        <n>Range minimum quantity</n>
        <min>0</min>
        <type>n</type>
        <length>18</length>
      </child>
      <child name="6152" seq="">
        <n>Range maximum quantity</n>
        <min>0</min>
        <type>n</type>
        <length>18</length>
      </child>
      <child name="6432" seq="">
        <n>Significant digits quantity</n>
        <min>0</min>
        <type>n</type>
        <length>2</length>
      </child>
    </composite>
    <element>
      <child name="7383" seq="040">
        <n>SURFACE OR LAYER CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--MEA-->
  </xsl:template>
  <xsl:template name="MEM">
    <code>MEM</code>
    <title>MEMBERSHIP DETAILS</title>
    <function>
      <n>To specify details about membership.</n>
    </function>
    <element>
      <child name="7449" seq="010">
        <n>MEMBERSHIP TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C942" seq="020">
        <n>MEMBERSHIP CATEGORY</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7451" seq="">
        <n>Membership category description code</n>
        <min>1</min>
        <type>an</type>
        <length>4</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7450" seq="">
        <n>Membership category description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C944" seq="030">
        <n>MEMBERSHIP STATUS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7453" seq="">
        <n>Membership status description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7452" seq="">
        <n>Membership status description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C945" seq="040">
        <n>MEMBERSHIP LEVEL</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7455" seq="">
        <n>Membership level code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7457" seq="">
        <n>Membership level description code</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7456" seq="">
        <n>Membership level description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C203" seq="050">
        <n>RATE/TARIFF CLASS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5243" seq="">
        <n>Rate or tariff class description code</n>
        <min>1</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5242" seq="">
        <n>Rate or tariff class description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="5275" seq="">
        <n>Supplementary rate or tariff code</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5275" seq="">
        <n>Supplementary rate or tariff code</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C960" seq="060">
        <n>REASON FOR CHANGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4295" seq="">
        <n>Change reason description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4294" seq="">
        <n>Change reason description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--MEM-->
  </xsl:template>
  <xsl:template name="MKS">
    <code>MKS</code>
    <title>MARKET</title>
    <function>
      <n>To specify to which market and/or through which</n>
      <n>sales distribution channel and/or for which end-</n>
      <n>use the sales of product/service have been made</n>
      <n>or are given as forecast.</n>
    </function>
    <element>
      <child name="7293" seq="010">
        <n>SECTOR AREA IDENTIFICATION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C332" seq="020">
        <n>SALES CHANNEL IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3496" seq="">
        <n>Sales channel identifier</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="1229" seq="030">
        <n>ACTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--MKS-->
  </xsl:template>
  <xsl:template name="MOA">
    <code>MOA</code>
    <title>MONETARY AMOUNT</title>
    <function>
      <n>To specify a monetary amount.</n>
    </function>
    <composite>
      <data name="C516" seq="010">
        <n>MONETARY AMOUNT</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5025" seq="">
        <n>Monetary amount type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5004" seq="">
        <n>Monetary amount</n>
        <min>0</min>
        <type>n</type>
        <length>35</length>
      </child>
      <child name="6345" seq="">
        <n>Currency identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6343" seq="">
        <n>Currency type code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4405" seq="">
        <n>Status description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--MOA-->
  </xsl:template>
  <xsl:template name="MSG">
    <code>MSG</code>
    <title>MESSAGE TYPE IDENTIFICATION</title>
    <function>
      <n>To identify a message type and to give its class</n>
      <n>and maintenance operation.</n>
    </function>
    <composite>
      <data name="C709" seq="010">
        <n>MESSAGE IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1003" seq="">
        <n>Message type code</n>
        <min>1</min>
        <type>an</type>
        <length>6</length>
      </child>
      <child name="1056" seq="">
        <n>Version identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1058" seq="">
        <n>Release identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1476" seq="">
        <n>Controlling agency identifier</n>
        <min>0</min>
        <type>an</type>
        <length>2</length>
      </child>
      <child name="1523" seq="">
        <n>Message implementation identification</n>
      </child>
      <child name="" seq="">
        <n>code</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
      <child name="1060" seq="">
        <n>Revision identifier</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
      <child name="1373" seq="">
        <n>Document status code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="1507" seq="020">
        <n>DESIGNATED CLASS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="030">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C941" seq="040">
        <n>RELATIONSHIP</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9143" seq="">
        <n>Relationship description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9142" seq="">
        <n>Relationship description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--MSG-->
  </xsl:template>
  <xsl:template name="MTD">
    <code>MTD</code>
    <title>MAINTENANCE OPERATION DETAILS</title>
    <function>
      <n>To identify a maintenance operation and its</n>
      <n>responsible parties.</n>
    </function>
    <element>
      <child name="7495" seq="010">
        <n>OBJECT TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="020">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="3005" seq="030">
        <n>MAINTENANCE OPERATION OPERATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="3009" seq="040">
        <n>MAINTENANCE OPERATION PAYER CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--MTD-->
  </xsl:template>
  <xsl:template name="NAD">
    <code>NAD</code>
    <title>NAME AND ADDRESS</title>
    <function>
      <n>To specify the name/address and their related</n>
      <n>function, either by C082 only and/or</n>
      <n>unstructured by C058 or structured by C080 thru</n>
      <n>3207.</n>
    </function>
    <element>
      <child name="3035" seq="010">
        <n>PARTY FUNCTION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C082" seq="020">
        <n>PARTY IDENTIFICATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3039" seq="">
        <n>Party identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C058" seq="030">
        <n>NAME AND ADDRESS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3124" seq="">
        <n>Name and address description</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3124" seq="">
        <n>Name and address description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3124" seq="">
        <n>Name and address description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3124" seq="">
        <n>Name and address description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3124" seq="">
        <n>Name and address description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C080" seq="040">
        <n>PARTY NAME</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3036" seq="">
        <n>Party name</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3036" seq="">
        <n>Party name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3036" seq="">
        <n>Party name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3036" seq="">
        <n>Party name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3036" seq="">
        <n>Party name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3045" seq="">
        <n>Party name format code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C059" seq="050">
        <n>STREET</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3042" seq="">
        <n>Street and number or post office box</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3042" seq="">
        <n>Street and number or post office box</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3042" seq="">
        <n>Street and number or post office box</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="3042" seq="">
        <n>Street and number or post office box</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="3164" seq="060">
        <n>CITY NAME</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <composite>
      <data name="C819" seq="070">
        <n>COUNTRY SUBDIVISION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3229" seq="">
        <n>Country subdivision identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3228" seq="">
        <n>Country subdivision name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <element>
      <child name="3251" seq="080">
        <n>POSTAL IDENTIFICATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>17</length>
      </child>
    </element>
    <element>
      <child name="3207" seq="090">
        <n>COUNTRY IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--NAD-->
  </xsl:template>
  <xsl:template name="NAT">
    <code>NAT</code>
    <title>NATIONALITY</title>
    <function>
      <n>To specify a nationality.</n>
    </function>
    <element>
      <child name="3493" seq="010">
        <n>NATIONALITY CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C042" seq="020">
        <n>NATIONALITY DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3293" seq="">
        <n>Nationality name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3292" seq="">
        <n>Nationality name</n>
        <min>0</min>
        <type>a</type>
        <length>35</length>
      </child>
    </composite>
    <!--NAT-->
  </xsl:template>
  <xsl:template name="PAC">
    <code>PAC</code>
    <title>PACKAGE</title>
    <function>
      <n>To describe the number and type of</n>
      <n>packages/physical units.</n>
    </function>
    <element>
      <child name="7224" seq="010">
        <n>PACKAGE QUANTITY</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>8</length>
      </child>
    </element>
    <composite>
      <data name="C531" seq="020">
        <n>PACKAGING DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7075" seq="">
        <n>Packaging level code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7233" seq="">
        <n>Packaging related description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7073" seq="">
        <n>Packaging terms and conditions code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C202" seq="030">
        <n>PACKAGE TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7065" seq="">
        <n>Package type description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7064" seq="">
        <n>Type of packages</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C402" seq="040">
        <n>PACKAGE TYPE IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7077" seq="">
        <n>Description format code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7064" seq="">
        <n>Type of packages</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7143" seq="">
        <n>Item type identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7064" seq="">
        <n>Type of packages</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7143" seq="">
        <n>Item type identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C532" seq="050">
        <n>RETURNABLE PACKAGE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8395" seq="">
        <n>Returnable package freight payment</n>
      </child>
      <child name="" seq="">
        <n>responsibility code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="8393" seq="">
        <n>Returnable package load contents code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--PAC-->
  </xsl:template>
  <xsl:template name="PAI">
    <code>PAI</code>
    <title>PAYMENT INSTRUCTIONS</title>
    <function>
      <n>To specify the instructions for payment.</n>
    </function>
    <composite>
      <data name="C534" seq="010">
        <n>PAYMENT INSTRUCTION DETAILS</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4439" seq="">
        <n>Payment conditions code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4431" seq="">
        <n>Payment guarantee means code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4461" seq="">
        <n>Payment means code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4435" seq="">
        <n>Payment channel code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--PAI-->
  </xsl:template>
  <xsl:template name="PAS">
    <code>PAS</code>
    <title>ATTENDANCE</title>
    <function>
      <n>To specify attendance information relating to an</n>
      <n>individual.</n>
    </function>
    <element>
      <child name="9443" seq="010">
        <n>ATTENDANCE TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C839" seq="020">
        <n>ATTENDEE CATEGORY</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7459" seq="">
        <n>Attendee category description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7458" seq="">
        <n>Attendee category description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C840" seq="030">
        <n>ATTENDANCE ADMISSION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9445" seq="">
        <n>Admission type description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9444" seq="">
        <n>Admission type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C841" seq="040">
        <n>ATTENDANCE DISCHARGE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9447" seq="">
        <n>Discharge type description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9446" seq="">
        <n>Discharge type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--PAS-->
  </xsl:template>
  <xsl:template name="PCC">
    <code>PCC</code>
    <title>PREMIUM CALCULATION COMPONENT DETAILS</title>
    <function>
      <n>To identify the component affecting a premium</n>
      <n>calculation and the value category of the</n>
      <n>component.</n>
    </function>
    <composite>
      <data name="C820" seq="010">
        <n>PREMIUM CALCULATION COMPONENT</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4521" seq="">
        <n>Premium calculation component identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4522" seq="020">
        <n>PREMIUM CALCULATION COMPONENT VALUE</n>
      </child>
      <child name="" seq="">
        <n>CATEGORY IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <!--PCC-->
  </xsl:template>
  <xsl:template name="PCD">
    <code>PCD</code>
    <title>PERCENTAGE DETAILS</title>
    <function>
      <n>To specify percentage information.</n>
    </function>
    <composite>
      <data name="C501" seq="010">
        <n>PERCENTAGE DETAILS</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5245" seq="">
        <n>Percentage type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5482" seq="">
        <n>Percentage</n>
        <min>0</min>
        <type>n</type>
        <length>10</length>
      </child>
      <child name="5249" seq="">
        <n>Percentage basis identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4405" seq="020">
        <n>STATUS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--PCD-->
  </xsl:template>
  <xsl:template name="PCI">
    <code>PCI</code>
    <title>PACKAGE IDENTIFICATION</title>
    <function>
      <n>To specify markings and labels on individual</n>
      <n>packages or physical units.</n>
    </function>
    <element>
      <child name="4233" seq="010">
        <n>MARKING INSTRUCTIONS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C210" seq="020">
        <n>MARKS &amp; LABELS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7102" seq="">
        <n>Shipping marks description</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7102" seq="">
        <n>Shipping marks description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7102" seq="">
        <n>Shipping marks description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7102" seq="">
        <n>Shipping marks description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7102" seq="">
        <n>Shipping marks description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7102" seq="">
        <n>Shipping marks description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7102" seq="">
        <n>Shipping marks description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7102" seq="">
        <n>Shipping marks description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7102" seq="">
        <n>Shipping marks description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7102" seq="">
        <n>Shipping marks description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="8169" seq="030">
        <n>FULL OR EMPTY INDICATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C827" seq="040">
        <n>TYPE OF MARKING</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7511" seq="">
        <n>Marking type code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--PCI-->
  </xsl:template>
  <xsl:template name="PDI">
    <code>PDI</code>
    <title>PERSON DEMOGRAPHIC INFORMATION</title>
    <function>
      <n>To specify items of person demographic</n>
      <n>information.</n>
    </function>
    <element>
      <child name="3499" seq="010">
        <n>GENDER CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C085" seq="020">
        <n>MARITAL STATUS DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3479" seq="">
        <n>Marital status description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3478" seq="">
        <n>Marital status description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C101" seq="030">
        <n>RELIGION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3483" seq="">
        <n>Religion name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3482" seq="">
        <n>Religion name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--PDI-->
  </xsl:template>
  <xsl:template name="PER">
    <code>PER</code>
    <title>PERIOD RELATED DETAILS</title>
    <function>
      <n>Specification of details relating to a period.</n>
    </function>
    <element>
      <child name="2023" seq="010">
        <n>PERIOD TYPE CODE QUALIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C977" seq="020">
        <n>PERIOD DETAIL</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="2119" seq="">
        <n>Period detail description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="2118" seq="">
        <n>Period detail description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--PER-->
  </xsl:template>
  <xsl:template name="PGI">
    <code>PGI</code>
    <title>PRODUCT GROUP INFORMATION</title>
    <function>
      <n>To indicate the group in which a product</n>
      <n>belongs.</n>
    </function>
    <element>
      <child name="5379" seq="010">
        <n>PRODUCT GROUP TYPE CODE</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C288" seq="020">
        <n>PRODUCT GROUP</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5389" seq="">
        <n>Product group name code</n>
        <min>0</min>
        <type>an</type>
        <length>25</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5388" seq="">
        <n>Product group name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--PGI-->
  </xsl:template>
  <xsl:template name="PIA">
    <code>PIA</code>
    <title>ADDITIONAL PRODUCT ID</title>
    <function>
      <n>To specify additional or substitutional item</n>
      <n>identification codes.</n>
    </function>
    <element>
      <child name="4347" seq="010">
        <n>PRODUCT IDENTIFIER CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C212" seq="020">
        <n>ITEM NUMBER IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7140" seq="">
        <n>Item identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7143" seq="">
        <n>Item type identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C212" seq="030">
        <n>ITEM NUMBER IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7140" seq="">
        <n>Item identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7143" seq="">
        <n>Item type identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C212" seq="040">
        <n>ITEM NUMBER IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7140" seq="">
        <n>Item identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7143" seq="">
        <n>Item type identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C212" seq="050">
        <n>ITEM NUMBER IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7140" seq="">
        <n>Item identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7143" seq="">
        <n>Item type identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C212" seq="060">
        <n>ITEM NUMBER IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7140" seq="">
        <n>Item identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7143" seq="">
        <n>Item type identification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--PIA-->
  </xsl:template>
  <xsl:template name="PNA">
    <code>PNA</code>
    <title>PARTY IDENTIFICATION</title>
    <function>
      <n>To specify information necessary to establish</n>
      <n>the identity of a party.</n>
    </function>
    <element>
      <child name="3035" seq="010">
        <n>PARTY FUNCTION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C206" seq="020">
        <n>IDENTIFICATION NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4405" seq="">
        <n>Status description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C082" seq="030">
        <n>PARTY IDENTIFICATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3039" seq="">
        <n>Party identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="3403" seq="040">
        <n>NAME TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="3397" seq="050">
        <n>NAME STATUS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C816" seq="060">
        <n>NAME COMPONENT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3405" seq="">
        <n>Name component type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3398" seq="">
        <n>Name component description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
      <child name="3401" seq="">
        <n>Name component usage code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3295" seq="">
        <n>Name original alphabet code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C816" seq="070">
        <n>NAME COMPONENT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3405" seq="">
        <n>Name component type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3398" seq="">
        <n>Name component description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
      <child name="3401" seq="">
        <n>Name component usage code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3295" seq="">
        <n>Name original alphabet code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C816" seq="080">
        <n>NAME COMPONENT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3405" seq="">
        <n>Name component type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3398" seq="">
        <n>Name component description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
      <child name="3401" seq="">
        <n>Name component usage code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3295" seq="">
        <n>Name original alphabet code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C816" seq="090">
        <n>NAME COMPONENT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3405" seq="">
        <n>Name component type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3398" seq="">
        <n>Name component description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
      <child name="3401" seq="">
        <n>Name component usage code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3295" seq="">
        <n>Name original alphabet code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C816" seq="100">
        <n>NAME COMPONENT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3405" seq="">
        <n>Name component type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3398" seq="">
        <n>Name component description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
      <child name="3401" seq="">
        <n>Name component usage code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3295" seq="">
        <n>Name original alphabet code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="1229" seq="110">
        <n>ACTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--PNA-->
  </xsl:template>
  <xsl:template name="POC">
    <code>POC</code>
    <title>PURPOSE OF CONVEYANCE CALL</title>
    <function>
      <n>To specify the purpose of the call of the</n>
      <n>conveyance.</n>
    </function>
    <composite>
      <data name="C525" seq="010">
        <n>PURPOSE OF CONVEYANCE CALL</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8025" seq="">
        <n>Conveyance call purpose description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="8024" seq="">
        <n>Conveyance call purpose description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--POC-->
  </xsl:template>
  <xsl:template name="PRC">
    <code>PRC</code>
    <title>PROCESS IDENTIFICATION</title>
    <function>
      <n>To identify a process.</n>
    </function>
    <composite>
      <data name="C242" seq="010">
        <n>PROCESS TYPE AND DESCRIPTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7187" seq="">
        <n>Process type description code</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7186" seq="">
        <n>Process type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7186" seq="">
        <n>Process type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C830" seq="020">
        <n>PROCESS IDENTIFICATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7191" seq="">
        <n>Process description code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7190" seq="">
        <n>Process description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <!--PRC-->
  </xsl:template>
  <xsl:template name="PRI">
    <code>PRI</code>
    <title>PRICE DETAILS</title>
    <function>
      <n>To specify price information.</n>
    </function>
    <composite>
      <data name="C509" seq="010">
        <n>PRICE INFORMATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5125" seq="">
        <n>Price code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5118" seq="">
        <n>Price amount</n>
        <min>0</min>
        <type>n</type>
        <length>15</length>
      </child>
      <child name="5375" seq="">
        <n>Price type code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5387" seq="">
        <n>Price specification code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5284" seq="">
        <n>Unit price basis quantity</n>
        <min>0</min>
        <type>n</type>
        <length>9</length>
      </child>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
    </composite>
    <element>
      <child name="5213" seq="020">
        <n>SUB-LINE ITEM PRICE CHANGE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--PRI-->
  </xsl:template>
  <xsl:template name="PRV">
    <code>PRV</code>
    <title>PROVISO DETAILS</title>
    <function>
      <n>Details regarding the stipulation or limitation</n>
      <n>in a document.</n>
    </function>
    <element>
      <child name="4071" seq="010">
        <n>PROVISO CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C971" seq="020">
        <n>PROVISO TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4073" seq="">
        <n>Proviso type description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4072" seq="">
        <n>Proviso type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C972" seq="030">
        <n>PROVISO CALCULATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4075" seq="">
        <n>Proviso calculation description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4074" seq="">
        <n>Proviso calculation description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--PRV-->
  </xsl:template>
  <xsl:template name="PSD">
    <code>PSD</code>
    <title>PHYSICAL SAMPLE DESCRIPTION</title>
    <function>
      <n>To define the physical sample parameters</n>
      <n>associated with a test, resulting in discrete</n>
      <n>measurements.</n>
    </function>
    <element>
      <child name="4407" seq="010">
        <n>SAMPLE PROCESS STEP CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="7039" seq="020">
        <n>SAMPLE SELECTION METHOD CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C526" seq="030">
        <n>FREQUENCY DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6071" seq="">
        <n>Frequency code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6072" seq="">
        <n>Frequency rate</n>
        <min>0</min>
        <type>n</type>
        <length>9</length>
      </child>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
    </composite>
    <element>
      <child name="7045" seq="040">
        <n>SAMPLE STATE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="7047" seq="050">
        <n>SAMPLE DIRECTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C514" seq="060">
        <n>SAMPLE LOCATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3237" seq="">
        <n>Sample location description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3236" seq="">
        <n>Sample location description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C514" seq="070">
        <n>SAMPLE LOCATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3237" seq="">
        <n>Sample location description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3236" seq="">
        <n>Sample location description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C514" seq="080">
        <n>SAMPLE LOCATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3237" seq="">
        <n>Sample location description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3236" seq="">
        <n>Sample location description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--PSD-->
  </xsl:template>
  <xsl:template name="PTY">
    <code>PTY</code>
    <title>PRIORITY</title>
    <function>
      <n>The segment is used to communicate priority</n>
      <n>information.</n>
    </function>
    <element>
      <child name="4035" seq="010">
        <n>PRIORITY TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C585" seq="020">
        <n>PRIORITY DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4037" seq="">
        <n>Priority description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4036" seq="">
        <n>Priority description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--PTY-->
  </xsl:template>
  <xsl:template name="PYT">
    <code>PYT</code>
    <title>PAYMENT TERMS</title>
    <function>
      <n>To specify the terms of payment.</n>
    </function>
    <element>
      <child name="4279" seq="010">
        <n>PAYMENT TERMS TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C019" seq="020">
        <n>PAYMENT TERMS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4277" seq="">
        <n>Payment terms description identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4276" seq="">
        <n>Payment terms description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="2475" seq="030">
        <n>EVENT TIME REFERENCE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="2009" seq="040">
        <n>TERMS TIME RELATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="2151" seq="050">
        <n>PERIOD TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="2152" seq="060">
        <n>PERIOD COUNT QUANTITY</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>3</length>
      </child>
    </element>
    <!--PYT-->
  </xsl:template>
  <xsl:template name="QRS">
    <code>QRS</code>
    <title>QUERY AND RESPONSE</title>
    <function>
      <n>To provide a declaration in the form of a coded</n>
      <n>question and response.</n>
    </function>
    <element>
      <child name="7293" seq="010">
        <n>SECTOR AREA IDENTIFICATION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C811" seq="020">
        <n>QUESTION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4057" seq="">
        <n>Question description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4056" seq="">
        <n>Question description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <composite>
      <data name="C812" seq="030">
        <n>RESPONSE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4345" seq="">
        <n>Response description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4344" seq="">
        <n>Response description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <!--QRS-->
  </xsl:template>
  <xsl:template name="QTY">
    <code>QTY</code>
    <title>QUANTITY</title>
    <function>
      <n>To specify a pertinent quantity.</n>
    </function>
    <composite>
      <data name="C186" seq="010">
        <n>QUANTITY DETAILS</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6063" seq="">
        <n>Quantity type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6060" seq="">
        <n>Quantity</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
    </composite>
    <!--QTY-->
  </xsl:template>
  <xsl:template name="QUA">
    <code>QUA</code>
    <title>QUALIFICATION</title>
    <function>
      <n>To specify the qualification of a person.</n>
    </function>
    <element>
      <child name="9037" seq="010">
        <n>QUALIFICATION TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C950" seq="020">
        <n>QUALIFICATION CLASSIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9007" seq="">
        <n>Qualification classification description</n>
      </child>
      <child name="" seq="">
        <n>code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9006" seq="">
        <n>Qualification classification description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="9006" seq="">
        <n>Qualification classification description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--QUA-->
  </xsl:template>
  <xsl:template name="QVR">
    <code>QVR</code>
    <title>QUANTITY VARIANCES</title>
    <function>
      <n>To specify item details relating to quantity</n>
      <n>variances.</n>
    </function>
    <composite>
      <data name="C279" seq="010">
        <n>QUANTITY DIFFERENCE INFORMATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6064" seq="">
        <n>Variance quantity</n>
        <min>1</min>
        <type>n</type>
        <length>15</length>
      </child>
      <child name="6063" seq="">
        <n>Quantity type code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4221" seq="020">
        <n>DISCREPANCY NATURE IDENTIFICATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C960" seq="030">
        <n>REASON FOR CHANGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4295" seq="">
        <n>Change reason description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4294" seq="">
        <n>Change reason description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--QVR-->
  </xsl:template>
  <xsl:template name="RCS">
    <code>RCS</code>
    <title>REQUIREMENTS AND CONDITIONS</title>
    <function>
      <n>To specify sector/subject requirements and</n>
      <n>conditions.</n>
    </function>
    <element>
      <child name="7293" seq="010">
        <n>SECTOR AREA IDENTIFICATION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C550" seq="020">
        <n>REQUIREMENT/CONDITION IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7295" seq="">
        <n>Requirement or condition description</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7294" seq="">
        <n>Requirement or condition description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="1229" seq="030">
        <n>ACTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="3207" seq="040">
        <n>COUNTRY IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--RCS-->
  </xsl:template>
  <xsl:template name="REL">
    <code>REL</code>
    <title>RELATIONSHIP</title>
    <function>
      <n>To identify relationships between objects.</n>
    </function>
    <element>
      <child name="9141" seq="010">
        <n>RELATIONSHIP TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C941" seq="020">
        <n>RELATIONSHIP</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9143" seq="">
        <n>Relationship description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9142" seq="">
        <n>Relationship description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--REL-->
  </xsl:template>
  <xsl:template name="RFF">
    <code>RFF</code>
    <title>REFERENCE</title>
    <function>
      <n>To specify a reference.</n>
    </function>
    <composite>
      <data name="C506" seq="010">
        <n>REFERENCE</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1153" seq="">
        <n>Reference code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1154" seq="">
        <n>Reference identifier</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
      <child name="1156" seq="">
        <n>Document line identifier</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
      <child name="1056" seq="">
        <n>Version identifier</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1060" seq="">
        <n>Revision identifier</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
    </composite>
    <!--RFF-->
  </xsl:template>
  <xsl:template name="RJL">
    <code>RJL</code>
    <title>ACCOUNTING JOURNAL IDENTIFICATION</title>
    <function>
      <n>To identify an accounting journal.</n>
    </function>
    <composite>
      <data name="C595" seq="010">
        <n>ACCOUNTING JOURNAL IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1171" seq="">
        <n>Accounting journal identifier</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1170" seq="">
        <n>Accounting journal name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C596" seq="020">
        <n>ACCOUNTING ENTRY TYPE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4475" seq="">
        <n>Accounting entry type name code</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4474" seq="">
        <n>Accounting entry type name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--RJL-->
  </xsl:template>
  <xsl:template name="RNG">
    <code>RNG</code>
    <title>RANGE DETAILS</title>
    <function>
      <n>To identify a range.</n>
    </function>
    <element>
      <child name="6167" seq="010">
        <n>RANGE TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C280" seq="020">
        <n>RANGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>1</min>
        <type>an</type>
        <length>8</length>
      </child>
      <child name="6162" seq="">
        <n>Range minimum quantity</n>
        <min>0</min>
        <type>n</type>
        <length>18</length>
      </child>
      <child name="6152" seq="">
        <n>Range maximum quantity</n>
        <min>0</min>
        <type>n</type>
        <length>18</length>
      </child>
    </composite>
    <!--RNG-->
  </xsl:template>
  <xsl:template name="ROD">
    <code>ROD</code>
    <title>RISK OBJECT TYPE</title>
    <function>
      <n>To identify a type of object at risk.</n>
    </function>
    <composite>
      <data name="C851" seq="010">
        <n>RISK OBJECT TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7179" seq="">
        <n>Risk object type identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C852" seq="020">
        <n>RISK OBJECT SUB-TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7177" seq="">
        <n>Risk object sub-type description</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7176" seq="">
        <n>Risk object sub-type description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <!--ROD-->
  </xsl:template>
  <xsl:template name="RSL">
    <code>RSL</code>
    <title>RESULT</title>
    <function>
      <n>To specify a discrete or non-discrete result as</n>
      <n>a value or value range.</n>
    </function>
    <element>
      <child name="6087" seq="010">
        <n>RESULT VALUE TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="6077" seq="020">
        <n>RESULT REPRESENTATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C831" seq="030">
        <n>RESULT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6314" seq="">
        <n>Measure</n>
        <min>0</min>
        <type>an</type>
        <length>18</length>
      </child>
      <child name="6321" seq="">
        <n>Measurement significance code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6155" seq="">
        <n>Non-discrete measurement name code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6154" seq="">
        <n>Non-discrete measurement name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C831" seq="040">
        <n>RESULT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6314" seq="">
        <n>Measure</n>
        <min>0</min>
        <type>an</type>
        <length>18</length>
      </child>
      <child name="6321" seq="">
        <n>Measurement significance code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6155" seq="">
        <n>Non-discrete measurement name code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6154" seq="">
        <n>Non-discrete measurement name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C848" seq="050">
        <n>MEASUREMENT UNIT DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6410" seq="">
        <n>Measurement unit name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="6079" seq="060">
        <n>RESULT NORMALCY CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--RSL-->
  </xsl:template>
  <xsl:template name="RTE">
    <code>RTE</code>
    <title>RATE DETAILS</title>
    <function>
      <n>To specify rate information.</n>
    </function>
    <composite>
      <data name="C128" seq="010">
        <n>RATE DETAILS</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5419" seq="">
        <n>Rate type code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5420" seq="">
        <n>Unit price basis rate</n>
        <min>1</min>
        <type>n</type>
        <length>15</length>
      </child>
      <child name="5284" seq="">
        <n>Unit price basis quantity</n>
        <min>0</min>
        <type>n</type>
        <length>9</length>
      </child>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
    </composite>
    <element>
      <child name="4405" seq="020">
        <n>STATUS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--RTE-->
  </xsl:template>
  <xsl:template name="SAL">
    <code>SAL</code>
    <title>REMUNERATION TYPE IDENTIFICATION</title>
    <function>
      <n>Identification of a remuneration type.</n>
    </function>
    <composite>
      <data name="C049" seq="010">
        <n>REMUNERATION TYPE IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5315" seq="">
        <n>Remuneration type name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5314" seq="">
        <n>Remuneration type name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="5314" seq="">
        <n>Remuneration type name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--SAL-->
  </xsl:template>
  <xsl:template name="SCC">
    <code>SCC</code>
    <title>SCHEDULING CONDITIONS</title>
    <function>
      <n>To specify scheduling conditions.</n>
    </function>
    <element>
      <child name="4017" seq="010">
        <n>DELIVERY PLAN COMMITMENT LEVEL CODE</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4493" seq="020">
        <n>DELIVERY INSTRUCTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C329" seq="030">
        <n>PATTERN DESCRIPTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="2013" seq="">
        <n>Frequency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="2015" seq="">
        <n>Despatch pattern code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="2017" seq="">
        <n>Despatch pattern timing code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--SCC-->
  </xsl:template>
  <xsl:template name="SCD">
    <code>SCD</code>
    <title>STRUCTURE COMPONENT DEFINITION</title>
    <function>
      <n>To specify a component of a data structure (e.g.</n>
      <n>an array or table).</n>
    </function>
    <element>
      <child name="7497" seq="010">
        <n>STRUCTURE COMPONENT FUNCTION CODE</n>
      </child>
      <child name="" seq="">
        <n>QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C786" seq="020">
        <n>STRUCTURE COMPONENT IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7512" seq="">
        <n>Structure component identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C082" seq="030">
        <n>PARTY IDENTIFICATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3039" seq="">
        <n>Party identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4405" seq="040">
        <n>STATUS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1222" seq="050">
        <n>CONFIGURATION LEVEL NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>2</length>
      </child>
    </element>
    <composite>
      <data name="C778" seq="060">
        <n>POSITION IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7164" seq="">
        <n>Hierarchical structure level identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1050" seq="">
        <n>Sequence position identifier</n>
        <min>0</min>
        <type>an</type>
        <length>10</length>
      </child>
    </composite>
    <composite>
      <data name="C240" seq="070">
        <n>CHARACTERISTIC DESCRIPTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7037" seq="">
        <n>Characteristic description code</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7036" seq="">
        <n>Characteristic description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7036" seq="">
        <n>Characteristic description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--SCD-->
  </xsl:template>
  <xsl:template name="SEG">
    <code>SEG</code>
    <title>SEGMENT IDENTIFICATION</title>
    <function>
      <n>To identify a segment and give its class and</n>
      <n>maintenance operation.</n>
    </function>
    <element>
      <child name="9166" seq="010">
        <n>SEGMENT TAG IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1507" seq="020">
        <n>DESIGNATED CLASS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="030">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--SEG-->
  </xsl:template>
  <xsl:template name="SEL">
    <code>SEL</code>
    <title>SEAL NUMBER</title>
    <function>
      <n>To specify the seal number or a range of seal</n>
      <n>numbers.</n>
    </function>
    <element>
      <child name="9308" seq="010">
        <n>TRANSPORT UNIT SEAL IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <composite>
      <data name="C215" seq="020">
        <n>SEAL ISSUER</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9303" seq="">
        <n>Sealing party name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9302" seq="">
        <n>Sealing party name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="4517" seq="030">
        <n>SEAL CONDITION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C208" seq="040">
        <n>IDENTITY NUMBER RANGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7402" seq="">
        <n>Object identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="4525" seq="050">
        <n>SEAL TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--SEL-->
  </xsl:template>
  <xsl:template name="SEQ">
    <code>SEQ</code>
    <title>SEQUENCE DETAILS</title>
    <function>
      <n>To provide details relating to the sequence.</n>
    </function>
    <element>
      <child name="1229" seq="010">
        <n>ACTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C286" seq="020">
        <n>SEQUENCE INFORMATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1050" seq="">
        <n>Sequence position identifier</n>
        <min>1</min>
        <type>an</type>
        <length>10</length>
      </child>
      <child name="1159" seq="">
        <n>Sequence identifier source code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--SEQ-->
  </xsl:template>
  <xsl:template name="SFI">
    <code>SFI</code>
    <title>SAFETY INFORMATION</title>
    <function>
      <n>To identify regulatory safety information.</n>
    </function>
    <element>
      <child name="7164" seq="010">
        <n>HIERARCHICAL STRUCTURE LEVEL IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <composite>
      <data name="C814" seq="020">
        <n>SAFETY SECTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4046" seq="">
        <n>Safety section number</n>
        <min>1</min>
        <type>n</type>
        <length>2</length>
      </child>
      <child name="4044" seq="">
        <n>Safety section name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <composite>
      <data name="C815" seq="030">
        <n>ADDITIONAL SAFETY INFORMATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4039" seq="">
        <n>Additional safety information description</n>
      </child>
      <child name="" seq="">
        <n>code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4038" seq="">
        <n>Additional safety information description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="4513" seq="040">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--SFI-->
  </xsl:template>
  <xsl:template name="SGP">
    <code>SGP</code>
    <title>SPLIT GOODS PLACEMENT</title>
    <function>
      <n>To specify the placement of goods in relation to</n>
      <n>equipment.</n>
    </function>
    <composite>
      <data name="C237" seq="010">
        <n>EQUIPMENT IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8260" seq="">
        <n>Equipment identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3207" seq="">
        <n>Country identifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="7224" seq="020">
        <n>PACKAGE QUANTITY</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>8</length>
      </child>
    </element>
    <!--SGP-->
  </xsl:template>
  <xsl:template name="SGU">
    <code>SGU</code>
    <title>SEGMENT USAGE DETAILS</title>
    <function>
      <n>To specify the details of the usage of a segment</n>
      <n>within a message type structure.</n>
    </function>
    <element>
      <child name="9166" seq="010">
        <n>SEGMENT TAG IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="7299" seq="020">
        <n>REQUIREMENT DESIGNATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="6176" seq="030">
        <n>OCCURRENCES MAXIMUM NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>7</length>
      </child>
    </element>
    <element>
      <child name="7168" seq="040">
        <n>LEVEL NUMBER</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1050" seq="050">
        <n>SEQUENCE POSITION IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>10</length>
      </child>
    </element>
    <element>
      <child name="1049" seq="060">
        <n>MESSAGE SECTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="070">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--SGU-->
  </xsl:template>
  <xsl:template name="SPR">
    <code>SPR</code>
    <title>ORGANISATION CLASSIFICATION DETAILS</title>
    <function>
      <n>To provide classification details relating to</n>
      <n>the activities of an organisation.</n>
    </function>
    <element>
      <child name="7293" seq="010">
        <n>SECTOR AREA IDENTIFICATION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="3079" seq="020">
        <n>ORGANISATION CLASSIFICATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C844" seq="030">
        <n>ORGANISATION CLASSIFICATION DETAIL</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3083" seq="">
        <n>Organisational class name code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3082" seq="">
        <n>Organisational class name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <!--SPR-->
  </xsl:template>
  <xsl:template name="SPS">
    <code>SPS</code>
    <title>SAMPLING PARAMETERS FOR SUMMARY STATISTICS</title>
    <function>
      <n>To define the sampling parameters associated</n>
      <n>with summary statistics reported.</n>
    </function>
    <composite>
      <data name="C526" seq="010">
        <n>FREQUENCY DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6071" seq="">
        <n>Frequency code qualifier</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6072" seq="">
        <n>Frequency rate</n>
        <min>0</min>
        <type>n</type>
        <length>9</length>
      </child>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
    </composite>
    <element>
      <child name="6074" seq="020">
        <n>CONFIDENCE PERCENT</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>6</length>
      </child>
    </element>
    <composite>
      <data name="C512" seq="030">
        <n>SIZE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6173" seq="">
        <n>Size type code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6174" seq="">
        <n>Size measure</n>
        <min>0</min>
        <type>n</type>
        <length>15</length>
      </child>
    </composite>
    <composite>
      <data name="C512" seq="040">
        <n>SIZE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6173" seq="">
        <n>Size type code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6174" seq="">
        <n>Size measure</n>
        <min>0</min>
        <type>n</type>
        <length>15</length>
      </child>
    </composite>
    <composite>
      <data name="C512" seq="050">
        <n>SIZE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6173" seq="">
        <n>Size type code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6174" seq="">
        <n>Size measure</n>
        <min>0</min>
        <type>n</type>
        <length>15</length>
      </child>
    </composite>
    <composite>
      <data name="C512" seq="060">
        <n>SIZE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6173" seq="">
        <n>Size type code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6174" seq="">
        <n>Size measure</n>
        <min>0</min>
        <type>n</type>
        <length>15</length>
      </child>
    </composite>
    <composite>
      <data name="C512" seq="070">
        <n>SIZE DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6173" seq="">
        <n>Size type code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6174" seq="">
        <n>Size measure</n>
        <min>0</min>
        <type>n</type>
        <length>15</length>
      </child>
    </composite>
    <!--SPS-->
  </xsl:template>
  <xsl:template name="STA">
    <code>STA</code>
    <title>STATISTICS</title>
    <function>
      <n>To transmit summary statistics related to a</n>
      <n>specified collection of test result values.</n>
    </function>
    <element>
      <child name="6331" seq="010">
        <n>STATISTIC TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C527" seq="020">
        <n>STATISTICAL DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6314" seq="">
        <n>Measure</n>
        <min>0</min>
        <type>an</type>
        <length>18</length>
      </child>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
      <child name="6313" seq="">
        <n>Measured attribute code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="6321" seq="">
        <n>Measurement significance code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--STA-->
  </xsl:template>
  <xsl:template name="STC">
    <code>STC</code>
    <title>STATISTICAL CONCEPT</title>
    <function>
      <n>To specify a statistical concept.</n>
    </function>
    <composite>
      <data name="C785" seq="010">
        <n>STATISTICAL CONCEPT IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6434" seq="">
        <n>Statistical concept identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C082" seq="020">
        <n>PARTY IDENTIFICATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3039" seq="">
        <n>Party identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4405" seq="030">
        <n>STATUS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4513" seq="040">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--STC-->
  </xsl:template>
  <xsl:template name="STG">
    <code>STG</code>
    <title>STAGES</title>
    <function>
      <n>To provide information related to the kind of</n>
      <n>stage in a process, the number of stages and the</n>
      <n>actual stage.</n>
    </function>
    <element>
      <child name="9421" seq="010">
        <n>PROCESS STAGE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="6426" seq="020">
        <n>PROCESS STAGES QUANTITY</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>2</length>
      </child>
    </element>
    <element>
      <child name="6428" seq="030">
        <n>PROCESS STAGES ACTUAL QUANTITY</n>
        <min>0</min>
        <max>1</max>
        <type>n</type>
        <length>2</length>
      </child>
    </element>
    <!--STG-->
  </xsl:template>
  <xsl:template name="STS">
    <code>STS</code>
    <title>STATUS</title>
    <function>
      <n>To specify the status of an object or service,</n>
      <n>including its category and the reason(s) for the</n>
      <n>status.</n>
    </function>
    <composite>
      <data name="C601" seq="010">
        <n>STATUS CATEGORY</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9015" seq="">
        <n>Status category code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C555" seq="020">
        <n>STATUS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4405" seq="">
        <n>Status description code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4404" seq="">
        <n>Status description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C556" seq="030">
        <n>STATUS REASON</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9013" seq="">
        <n>Status reason description code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9012" seq="">
        <n>Status reason description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <composite>
      <data name="C556" seq="040">
        <n>STATUS REASON</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9013" seq="">
        <n>Status reason description code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9012" seq="">
        <n>Status reason description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <composite>
      <data name="C556" seq="050">
        <n>STATUS REASON</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9013" seq="">
        <n>Status reason description code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9012" seq="">
        <n>Status reason description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <composite>
      <data name="C556" seq="060">
        <n>STATUS REASON</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9013" seq="">
        <n>Status reason description code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9012" seq="">
        <n>Status reason description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <composite>
      <data name="C556" seq="070">
        <n>STATUS REASON</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="9013" seq="">
        <n>Status reason description code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="9012" seq="">
        <n>Status reason description</n>
        <min>0</min>
        <type>an</type>
        <length>256</length>
      </child>
    </composite>
    <!--STS-->
  </xsl:template>
  <xsl:template name="TAX">
    <code>TAX</code>
    <title>DUTY</title>
    <function>
      <n>To specify relevant duty/tax/fee information.</n>
    </function>
    <element>
      <child name="5283" seq="010">
        <n>DUTY OR TAX OR FEE FUNCTION CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C241" seq="020">
        <n>DUTY/TAX/FEE TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5153" seq="">
        <n>Duty or tax or fee type name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5152" seq="">
        <n>Duty or tax or fee type name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C533" seq="030">
        <n>DUTY/TAX/FEE ACCOUNT DETAIL</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5289" seq="">
        <n>Duty or tax or fee account code</n>
        <min>1</min>
        <type>an</type>
        <length>6</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="5286" seq="040">
        <n>DUTY OR TAX OR FEE ASSESSMENT BASIS</n>
      </child>
      <child name="" seq="">
        <n>QUANTITY</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>15</length>
      </child>
    </element>
    <composite>
      <data name="C243" seq="050">
        <n>DUTY/TAX/FEE DETAIL</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5279" seq="">
        <n>Duty or tax or fee rate code</n>
        <min>0</min>
        <type>an</type>
        <length>7</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5278" seq="">
        <n>Duty or tax or fee rate</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="5273" seq="">
        <n>Duty or tax or fee rate basis code</n>
        <min>0</min>
        <type>an</type>
        <length>12</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="5305" seq="060">
        <n>DUTY OR TAX OR FEE CATEGORY CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="3446" seq="070">
        <n>PARTY TAX IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>20</length>
      </child>
    </element>
    <element>
      <child name="1227" seq="080">
        <n>CALCULATION SEQUENCE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="5307" seq="090">
        <n>TAX OR DUTY OR FEE PAYMENT DUE DATE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--TAX-->
  </xsl:template>
  <xsl:template name="TCC">
    <code>TCC</code>
    <title>CHARGE</title>
    <function>
      <n>To specify charges.</n>
    </function>
    <composite>
      <data name="C200" seq="010">
        <n>CHARGE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8023" seq="">
        <n>Freight and other charges description</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="8022" seq="">
        <n>Freight and other charges description</n>
        <min>0</min>
        <type>an</type>
        <length>26</length>
      </child>
      <child name="4237" seq="">
        <n>Payment arrangement code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7140" seq="">
        <n>Item identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <composite>
      <data name="C203" seq="020">
        <n>RATE/TARIFF CLASS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5243" seq="">
        <n>Rate or tariff class description code</n>
        <min>1</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5242" seq="">
        <n>Rate or tariff class description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="5275" seq="">
        <n>Supplementary rate or tariff code</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="5275" seq="">
        <n>Supplementary rate or tariff code</n>
        <min>0</min>
        <type>an</type>
        <length>6</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C528" seq="030">
        <n>COMMODITY/RATE DETAIL</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7357" seq="">
        <n>Commodity identification code</n>
        <min>0</min>
        <type>an</type>
        <length>18</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C554" seq="040">
        <n>RATE/TARIFF CLASS DETAIL</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="5243" seq="">
        <n>Rate or tariff class description code</n>
        <min>0</min>
        <type>an</type>
        <length>9</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--TCC-->
  </xsl:template>
  <xsl:template name="TDT">
    <code>TDT</code>
    <title>TRANSPORT INFORMATION</title>
    <function>
      <n>To specify information regarding the transport</n>
      <n>such as mode of transport, means of transport,</n>
      <n>its conveyance reference number and the</n>
      <n>identification of the means of transport.</n>
    </function>
    <element>
      <child name="8051" seq="010">
        <n>TRANSPORT STAGE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="8028" seq="020">
        <n>MEANS OF TRANSPORT JOURNEY IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>17</length>
      </child>
    </element>
    <composite>
      <data name="C220" seq="030">
        <n>MODE OF TRANSPORT</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8067" seq="">
        <n>Transport mode name code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="8066" seq="">
        <n>Transport mode name</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
    </composite>
    <composite>
      <data name="C001" seq="040">
        <n>TRANSPORT MEANS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8179" seq="">
        <n>Transport means description code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="8178" seq="">
        <n>Transport means description</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
    </composite>
    <composite>
      <data name="C040" seq="050">
        <n>CARRIER</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3127" seq="">
        <n>Carrier identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="3126" seq="">
        <n>Carrier name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="8101" seq="060">
        <n>TRANSIT DIRECTION INDICATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C401" seq="070">
        <n>EXCESS TRANSPORTATION INFORMATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8457" seq="">
        <n>Excess transportation reason code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="8459" seq="">
        <n>Excess transportation responsibility code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7130" seq="">
        <n>Customer shipment authorisation</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
    </composite>
    <composite>
      <data name="C222" seq="080">
        <n>TRANSPORT IDENTIFICATION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8213" seq="">
        <n>Transport means identification name</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="8212" seq="">
        <n>Transport means identification name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
      <child name="8453" seq="">
        <n>Transport means nationality code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="8281" seq="090">
        <n>TRANSPORT MEANS OWNERSHIP INDICATOR CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <code>Dependency</code>
    <title>note</title>
    <!--TDT-->
  </xsl:template>
  <xsl:template name="TEM">
    <code>TEM</code>
    <title>TEST METHOD</title>
    <function>
      <n>To describe the nature of the test performed.</n>
    </function>
    <composite>
      <data name="C244" seq="010">
        <n>TEST METHOD</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4415" seq="">
        <n>Test method identifier</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4416" seq="">
        <n>Test description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <element>
      <child name="4419" seq="020">
        <n>TEST ADMINISTRATION METHOD CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="3077" seq="030">
        <n>TEST MEDIUM CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="6311" seq="040">
        <n>MEASUREMENT PURPOSE CODE QUALIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="7188" seq="050">
        <n>TEST METHOD REVISION IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>30</length>
      </child>
    </element>
    <composite>
      <data name="C515" seq="060">
        <n>TEST REASON</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4425" seq="">
        <n>Test reason name code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4424" seq="">
        <n>Test reason name</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <!--TEM-->
  </xsl:template>
  <xsl:template name="TMD">
    <code>TMD</code>
    <title>TRANSPORT MOVEMENT DETAILS</title>
    <function>
      <n>To specify operational transport movement</n>
      <n>details for a goods item or equipment (which may</n>
      <n>differ from the contractual conditions).</n>
    </function>
    <composite>
      <data name="C219" seq="010">
        <n>MOVEMENT TYPE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8335" seq="">
        <n>Movement type description code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="8334" seq="">
        <n>Movement type description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="8332" seq="020">
        <n>EQUIPMENT PLAN DESCRIPTION</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>26</length>
      </child>
    </element>
    <element>
      <child name="8341" seq="030">
        <n>HAULAGE ARRANGEMENTS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--TMD-->
  </xsl:template>
  <xsl:template name="TMP">
    <code>TMP</code>
    <title>TEMPERATURE</title>
    <function>
      <n>To specify the temperature setting.</n>
    </function>
    <element>
      <child name="6245" seq="010">
        <n>TEMPERATURE TYPE CODE QUALIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C239" seq="020">
        <n>TEMPERATURE SETTING</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="6246" seq="">
        <n>Temperature degree</n>
        <min>0</min>
        <type>n</type>
        <length>15</length>
      </child>
      <child name="6411" seq="">
        <n>Measurement unit code</n>
        <min>0</min>
        <type>an</type>
        <length>8</length>
      </child>
    </composite>
    <!--TMP-->
  </xsl:template>
  <xsl:template name="TOD">
    <code>TOD</code>
    <title>TERMS OF DELIVERY OR TRANSPORT</title>
    <function>
      <n>To specify terms of delivery or transport.</n>
    </function>
    <element>
      <child name="4055" seq="010">
        <n>DELIVERY OR TRANSPORT TERMS FUNCTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="4215" seq="020">
        <n>TRANSPORT CHARGES PAYMENT METHOD CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C100" seq="030">
        <n>TERMS OF DELIVERY OR TRANSPORT</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4053" seq="">
        <n>Delivery or transport terms description</n>
      </child>
      <child name="" seq="">
        <n>code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="4052" seq="">
        <n>Delivery or transport terms description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
      <child name="4052" seq="">
        <n>Delivery or transport terms description</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
    </composite>
    <!--TOD-->
  </xsl:template>
  <xsl:template name="TPL">
    <code>TPL</code>
    <title>TRANSPORT PLACEMENT</title>
    <function>
      <n>To specify placement of goods or equipment in</n>
      <n>relation to the transport used. The segment</n>
      <n>serves as a pointer to the TDT segment group.</n>
    </function>
    <composite>
      <data name="C222" seq="010">
        <n>TRANSPORT IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="8213" seq="">
        <n>Transport means identification name</n>
      </child>
      <child name="" seq="">
        <n>identifier</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="8212" seq="">
        <n>Transport means identification name</n>
        <min>0</min>
        <type>an</type>
        <length>70</length>
      </child>
      <child name="8453" seq="">
        <n>Transport means nationality code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--TPL-->
  </xsl:template>
  <xsl:template name="TRU">
    <code>TRU</code>
    <title>TECHNICAL RULES</title>
    <function>
      <n>A segment specifying technical rules.</n>
    </function>
    <element>
      <child name="7402" seq="010">
        <n>OBJECT IDENTIFIER</n>
        <min>1</min>
        <max>1</max>
        <type>an</type>
        <length>35</length>
      </child>
    </element>
    <element>
      <child name="1056" seq="020">
        <n>VERSION IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>9</length>
      </child>
    </element>
    <element>
      <child name="1058" seq="030">
        <n>RELEASE IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>9</length>
      </child>
    </element>
    <element>
      <child name="7175" seq="040">
        <n>RULE PART IDENTIFIER</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>7</length>
      </child>
    </element>
    <element>
      <child name="3055" seq="050">
        <n>CODE LIST RESPONSIBLE AGENCY CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--TRU-->
  </xsl:template>
  <xsl:template name="TSR">
    <code>TSR</code>
    <title>TRANSPORT SERVICE REQUIREMENTS</title>
    <function>
      <n>To specify the contract and carriage conditions</n>
      <n>and service and priority requirements for the</n>
      <n>transport.</n>
    </function>
    <composite>
      <data name="C536" seq="010">
        <n>CONTRACT AND CARRIAGE CONDITION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4065" seq="">
        <n>Contract and carriage condition code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C233" seq="020">
        <n>SERVICE</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7273" seq="">
        <n>Service requirement code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7273" seq="">
        <n>Service requirement code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C537" seq="030">
        <n>TRANSPORT PRIORITY</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="4219" seq="">
        <n>Transport service priority code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C703" seq="040">
        <n>NATURE OF CARGO</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7085" seq="">
        <n>Cargo type classification code</n>
        <min>1</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <!--TSR-->
  </xsl:template>
  <xsl:template name="VLI">
    <code>VLI</code>
    <title>VALUE LIST IDENTIFICATION</title>
    <function>
      <n>To identify a coded or non coded value list.</n>
    </function>
    <composite>
      <data name="C780" seq="010">
        <n>VALUE LIST IDENTIFICATION</n>
        <min>1</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="1518" seq="">
        <n>Value list identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7405" seq="">
        <n>Object identification code qualifier</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <composite>
      <data name="C082" seq="020">
        <n>PARTY IDENTIFICATION DETAILS</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="3039" seq="">
        <n>Party identifier</n>
        <min>1</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
    </composite>
    <element>
      <child name="4405" seq="030">
        <n>STATUS DESCRIPTION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1514" seq="040">
        <n>VALUE LIST NAME</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>70</length>
      </child>
    </element>
    <element>
      <child name="1507" seq="050">
        <n>DESIGNATED CLASS CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <element>
      <child name="1505" seq="060">
        <n>VALUE LIST TYPE CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <composite>
      <data name="C240" seq="070">
        <n>CHARACTERISTIC DESCRIPTION</n>
        <min>0</min>
        <max>1</max>
        <type/>
        <length/>
      </data>
      <child name="7037" seq="">
        <n>Characteristic description code</n>
        <min>1</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="1131" seq="">
        <n>Code list identification code</n>
        <min>0</min>
        <type>an</type>
        <length>17</length>
      </child>
      <child name="3055" seq="">
        <n>Code list responsible agency code</n>
        <min>0</min>
        <type>an</type>
        <length>3</length>
      </child>
      <child name="7036" seq="">
        <n>Characteristic description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
      <child name="7036" seq="">
        <n>Characteristic description</n>
        <min>0</min>
        <type>an</type>
        <length>35</length>
      </child>
    </composite>
    <element>
      <child name="4513" seq="080">
        <n>MAINTENANCE OPERATION CODE</n>
        <min>0</min>
        <max>1</max>
        <type>an</type>
        <length>3</length>
      </child>
    </element>
    <!--VLI-->
  </xsl:template>
</xsl:stylesheet>
