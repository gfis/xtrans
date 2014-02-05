<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:S2SCTIcf="urn:S2SCTIcf:xsd:SCTIcfBlkCredTrf" 
		xmlns:sw4="urn:iso:std:iso:20022:tech:xsd:pacs.004.001.01" 
		xmlns:sw6="urn:iso:std:iso:20022:tech:xsd:pacs.006.001.01" 
		xmlns:sw8="urn:iso:std:iso:20022:tech:xsd:pacs.008.001.01" 
		>
<!--
	SEPA-Testnachrichten nachbehandeln:
	- Anzahl der pacs.008, pacs.006 und pacs.004 berechnen
	- Anzahl der Transaktionen berechnen
	- Summe der Betraege berechnen
	Copyright (c) 2007 Commerzbank AG
	@(#) $Id: DTA.check.xsl 9 2008-09-05 05:21:15Z gfis $
    2007-07-12, Dr. Georg Fischer: round((x + 0.0005) * 100) div 100
	2007-07-02, Michael Bruening : Änderung w/ neuen EBA-Schema (S2SCT geloescht)
    2007-06-12, Dr. Georg Fischer: ZIT A 7.5.1
    
    Aufruf: xalan -i 4 -o outputfile inputfile patch.xsl
-->
	<xsl:output method="xml" indent="yes" />
	<xsl:strip-space elements="*"/>
	<xsl:param name="comment" /><!-- yes/no, default=yes -->

	<!-- pacs.008 zaehlen -->
  	<xsl:template match="S2SCTIcf:NumCTBlk">
  		<S2SCTIcf:NumCTBlk>
  			<xsl:value-of select="count(./following-sibling::S2SCTIcf:pacs.008.001.01)" />
  			<xsl:call-template name="computed" />
  		</S2SCTIcf:NumCTBlk>
	</xsl:template>

	<!-- pacs.006 zaehlen -->
  	<xsl:template match="S2SCTIcf:NumRFCBlk">
  		<S2SCTIcf:NumRFCBlk>
  			<xsl:value-of select="count(./following-sibling::S2SCTIcf:pacs.006.001.01)" />
  			<xsl:call-template name="computed" />
  		</S2SCTIcf:NumRFCBlk>
	</xsl:template>

	<!-- pacs.004 zaehlen -->
  	<xsl:template match="S2SCTIcf:NumRFRBlk">
  		<S2SCTIcf:NumRFRBlk>
  			<xsl:value-of select="count(./following-sibling::S2SCTIcf:pacs.004.001.01)" />
  			<xsl:call-template name="computed" />
  		</S2SCTIcf:NumRFRBlk>
	</xsl:template>

	<!-- Transaktionen in pacs.008 zaehlen -->
  	<xsl:template match="sw8:GrpHdr/sw8:NbOfTxs">
  		<sw8:NbOfTxs>
  			<xsl:value-of select="count(../following-sibling::sw8:CdtTrfTxInf)" />
  			<xsl:call-template name="computed" />
  		</sw8:NbOfTxs>
	</xsl:template>

	<!-- Betraege in pacs.008 summieren -->
  	<xsl:template match="sw8:GrpHdr/sw8:TtlIntrBkSttlmAmt">
  		<sw8:TtlIntrBkSttlmAmt Ccy="EUR">
  			<xsl:value-of select="round((sum(../following-sibling::sw8:CdtTrfTxInf/sw8:IntrBkSttlmAmt) + 0.0005) * 100) div 100" />
  			<xsl:call-template name="computed" />
  		</sw8:TtlIntrBkSttlmAmt>
	</xsl:template>

	<!-- Transaktionen in pacs.006 zaehlen (nur EBA) -->
  	<xsl:template match="sw6:GrpHdr/sw6:NbOfTxs">
  		<sw6:NbOfTxs>
  			<xsl:value-of select="count(../following-sibling::sw6:TxInf)" />
  			<xsl:call-template name="computed" />
  		</sw6:NbOfTxs>
	</xsl:template>

	<!-- Betraege in pacs.006 summieren -->
  	<xsl:template match="sw6:GrpHdr/sw6:CtrlSum">
  		<sw6:CtrlSum>
  			<xsl:value-of select="round((sum(../following-sibling::sw6:TxInf/sw6:OrgnlIntrBkSttlmAmt) + 0.0005) * 100) div 100" />
  			<xsl:call-template name="computed" />
  		</sw6:CtrlSum>
	</xsl:template>

	<!-- Transaktionen in pacs.004 zaehlen -->
  	<xsl:template match="sw4:GrpHdr/sw4:NbOfTxs">
  		<sw4:NbOfTxs>
  			<xsl:value-of select="count(../following-sibling::sw4:TxInf)" />
  			<xsl:call-template name="computed" />
  		</sw4:NbOfTxs>
	</xsl:template>

	<!-- Betraege in pacs.004 summieren -->
  	<xsl:template match="sw4:GrpHdr/sw4:TtlRtrdIntrBkSttlmAmt">
  		<sw4:TtlRtrdIntrBkSttlmAmt Ccy="EUR">
  			<xsl:value-of select="round((sum(../following-sibling::sw4:TxInf/sw4:RtrdIntrBkSttlmAmt) + 0.0005) * 100) div 100" />
  			<xsl:call-template name="computed" />
  		</sw4:TtlRtrdIntrBkSttlmAmt>
	</xsl:template>

	<!-- alle anderen unveraendert kopieren -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template name="computed">
		<xsl:if test="$comment != 'no'">
  			<xsl:comment>computed</xsl:comment>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
