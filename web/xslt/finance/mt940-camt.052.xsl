<?xml version="1.0" encoding="UTF-8"?>
<!--
	Converter from a batch of MT940 messages to a bulk of camt.052 documents
	@(#) $Id: mt940-camt.052.xsl 9 2008-09-05 05:21:15Z gfis $
	2008-06-17: validates
	2008-06-02, Dr. Georg Fischer punctum GmbH, D-79341 Kenzingen
	Caution, encoded in UTF-8: äöüÄÖÜßß, though this XSLT stylesheet is pure US-ASCII
	
	Activation: 
		xalan -o camt.052.out.xml mt940.batch camt.052.xsl

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
<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns="urn:iso:std:iso:20022:tech:xsd:camt.052.001.01"
		xmlns:xalan="http://xml.apache.org/xalan"
		xmlns:func="http://exslt.org/functions"
		xmlns:date="http://exslt.org/dates-and-times"
		extension-element-prefixes="func date"
		>
	<xsl:output method="xml" indent="yes" xalan:indent-amount="2" encoding="UTF-8" />
	<xsl:strip-space elements="*" />
	<xsl:param name="seqno" select="4711" />

	<xsl:template match="SWIFT-FIN">
		<Bulk>
			<xsl:apply-templates select="message" />
		</Bulk>
	</xsl:template>

	<xsl:template match="message">
		<Document>
		<BkToCstmrAcctRptV01>
			<GrpHdr>
				<MsgId>
					<xsl:value-of select="B4/F20" />
				</MsgId>
				<CreDtTm><xsl:call-template name="current-time" /></CreDtTm>
				<MsgPgntn>
					<PgNb>1</PgNb><LastPgInd>1</LastPgInd>
				</MsgPgntn>
			</GrpHdr>
			<Rpt>
				<Id>
					<xsl:value-of select="translate(concat(date:date-time(), ''), '+-: T', '')"/>
				</Id>
				<xsl:apply-templates select="B4" />
			</Rpt>
		</BkToCstmrAcctRptV01>
		</Document>
	</xsl:template>

	<xsl:template match="B4">
		<ElctrncSeqNb><xsl:value-of select="$seqno + count(../preceding-sibling::message)" /></ElctrncSeqNb>
        <LglSeqNb>
        	<xsl:value-of select="F28/reportNo" />
        	<xsl:value-of select="F28C/reportNo" />
        </LglSeqNb>
		<CreDtTm>
			<xsl:choose>
				<xsl:when test="string-length(F13D/time) &gt; 0">
					<!-- e.g. :13D:07062905:30+0200 -->
					<xsl:value-of select="F13D/time" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="current-time" />
				</xsl:otherwise>
			</xsl:choose>
		</CreDtTm>
        <!-- FrToDt -->
        <!-- CpyDplctInd: all electronic statements are originals -->
		<Acct>
			<Id>
            	<PrtryAcct>
            		<Id><xsl:value-of select="F25" /></Id>
            	</PrtryAcct>
			</Id>
			<!-- Tp -->
			<xsl:variable name="ccy" select="substring(F25, string-length(F25) - 2, 3)" />
			<xsl:if test="translate($ccy, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', '..........................') = '...'">
				<Ccy><xsl:value-of select="$ccy" /></Ccy>
			</xsl:if>
			<!-- Nm -->
			<!-- Ownr -->
			<!-- Svcr -->
			<xsl:variable name="bic" select="substring(F25, 1, 8)" />
			<xsl:if test="translate($bic, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', '..........................') = '........'">
				<Svcr>
                   <FinInstnId>
						<BIC><xsl:value-of select="$bic" /></BIC>
                   </FinInstnId>
				</Svcr>
			</xsl:if>
		</Acct>
		<!-- RltdAcct -->
		<!-- Intrst -->
		<xsl:apply-templates select="F60F|F60M|F62F|F62M|F64|F65" /><!-- balances -->
		<xsl:apply-templates select="F61" />
	</xsl:template><!-- B4 -->

	<xsl:template match="F60F"><!-- (first) opening balance -->
		<xsl:call-template name="balance">
			<xsl:with-param name="node" select="." />
			<xsl:with-param name="code" select="'PRCD'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="F60M"><!-- intermediary balance -->
		<xsl:call-template name="balance">
			<xsl:with-param name="node" select="." />
			<xsl:with-param name="code" select="'ITBD'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="F62F"><!-- (final) closing balance -->
		<xsl:call-template name="balance">
			<xsl:with-param name="node" select="." />
			<xsl:with-param name="code" select="'CCLB'" />
		</xsl:call-template>
		<xsl:apply-templates select="F86" />
	</xsl:template>

	<xsl:template match="F62M"><!-- (intermediary) closing balance -->
		<xsl:call-template name="balance">
			<xsl:with-param name="node" select="." />
			<xsl:with-param name="code" select="'ITBD'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="F64|F65"><!-- current|future value balance -->
		<xsl:call-template name="balance">
			<xsl:with-param name="node" select="." />
			<xsl:with-param name="code" select="'CCLB'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="F61">
		<Ntry>
			<Amt>
				<xsl:attribute name="Ccy"><xsl:value-of select="concat(../F60F/ccy, ../F60M/ccy)" /></xsl:attribute>
				<xsl:value-of select="amt" />
			</Amt>
			<CdtDbtInd><xsl:value-of select="sign" /></CdtDbtInd>
			<xsl:if test="string-length(rev) &gt; 0">
				<RvslInd><xsl:value-of select="rev" /></RvslInd>
			</xsl:if>
			<Sts>BOOK</Sts>

			<xsl:if test="string-length(mmdd) &gt; 0">
				<BookgDt>
					<Dt><xsl:value-of select="mmdd" /></Dt>
				</BookgDt>
			</xsl:if>
			<xsl:if test="string-length(yymmdd) &gt; 0">
				<ValDt>
					<Dt><xsl:value-of select="mmdd" /></Dt>
				</ValDt>
			</xsl:if>

			<xsl:if test="string-length(msgid) &gt; 0">
				<AcctSvcrRef><xsl:value-of select="msgid" /></AcctSvcrRef>
			</xsl:if>
			
			<!-- Avlbty -->

			<BkTxCd>
			<!-- with SWIFT decision?
			-->
				<Domn><Cd>PMNT</Cd>
					<Fmly><Cd>OTHR</Cd>
					<SubFmlyCd>OTHR</SubFmlyCd>
					</Fmly>
				</Domn>
				<Prtry><!-- SWIFT-TX-Code ?? -->
					<Cd>
						<xsl:if test="string-length(txcd2) &gt; 0">
							<xsl:value-of select="concat(txcd2, '/')" />
						</xsl:if>
						<xsl:if test="string-length(ref2) &gt; 0">
							<xsl:value-of select="ref2" />
						</xsl:if>
					</Cd>
					<Issr>ZKA</Issr>
				</Prtry>
			</BkTxCd>
			
			<xsl:if test="string-length(ocmt) &gt; 0">
				<AmtDtls>
            		<InstdAmt><!--[0..1] AmountAndCurrencyExchangeDetails1 ! Originalbetrag-->
                		<Amt><!--[1..1] CurrencyAndAmount decimal >=0 L18.5 -->
                			<xsl:attribute name="Ccy">
	                			<xsl:value-of select="ocmt/ccy" />
                			</xsl:attribute>
                			<xsl:value-of select="ocmt/amt" />
                		</Amt>
       	     		</InstdAmt>
				</AmtDtls>
			</xsl:if>
			<xsl:if test="string-length(chgs) &gt; 0">
	            <Chrgs><!--[0..n] ChargesInformation3 -->
               		<Amt><!--[1..1] CurrencyAndAmount decimal >=0 L18.5 -->
               			<xsl:attribute name="Ccy">
                			<xsl:value-of select="chgs/ccy" />
               			</xsl:attribute>
               			<xsl:value-of select="chgs/amt" />
               		</Amt>
        	    </Chrgs>
			</xsl:if>
			
			<xsl:variable name="f86" select="following-sibling::F86[1]" />

			<TxDtls>
				<Refs>
					<xsl:if test="string-length(ref2) &gt; 0 and string(ref2) != 'NONREF'">
                        <AcctSvcrRef><xsl:value-of select="ref2" /></AcctSvcrRef><!--[0..1] Max35Text string 1..35 ! 61/8 -->
 					</xsl:if>
					<xsl:if test="string-length(ref)  &gt; 0 and string(ref)  != 'NONREF'">
						<InstrId><xsl:value-of select="ref" /></InstrId><!--[0..1] Max35Text string 1..35 ! 61/7, KREF+-->
					</xsl:if>
					<xsl:if test="string-length($f86/VOID/eref) &gt; 0">
						<EndToEndId><xsl:value-of select="$f86/VOID/eref" /></EndToEndId><!--[0..1] Max35Text string 1..35 ! EREF+ -->
					</xsl:if>
					<xsl:if test="string-length($f86/VOID/mref) &gt; 0">
						<MndtId><xsl:value-of select="$f86/VOID/mref" /></MndtId>
					</xsl:if>
				</Refs>

               	<xsl:choose>
					<xsl:when test="string(sign) = 'CRDT'">
						<xsl:if test="string-length($f86/Z31) &gt; 0">
			                <RltdPties><!--[0..1] TransactionParty1 -->
								<Dbtr>
									<xsl:call-template name="partner-name">
										<xsl:with-param name="node" select="$f86" />
									</xsl:call-template>
								</Dbtr>
								<DbtrAcct>
									<xsl:call-template name="partner-acct">
										<xsl:with-param name="node" select="$f86" />
									</xsl:call-template>
								</DbtrAcct>
							</RltdPties>
						</xsl:if>
						<xsl:if test="string-length($f86/Z30) &gt; 0">
		                    <RltdAgts><!--[0..1] TransactionAgents1 -->
    	                   		<DbtrAgt><!--[0..1] BranchAndFinancialInstitutionIdentification3 -->
    	                        	<FinInstnId><!--[1..1] FinancialInstitutionIdentification5Choice  -->
						               	<xsl:choose>
											<xsl:when test="translate($f86/Z30, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', '..........................') = '........'">
			                                	<BIC><!--[1..1] BICIdentifier string /[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}/ -->
    	    		                        		<xsl:value-of select="$f86/Z30" />
    	            		                	</BIC>
    	            		                </xsl:when>
											<xsl:otherwise>
 				                            	<PrtryId><!--[1..1] GenericIdentification3 -->
    	        		                        	<Id><xsl:value-of select="$f86/Z30" /></Id><!--[1..1] Max35Text string 1..35 -->
    	                        			    </PrtryId>
											</xsl:otherwise>
    	           						</xsl:choose>
									</FinInstnId>
								</DbtrAgt>
							</RltdAgts>
						</xsl:if>
					</xsl:when>
					<xsl:when test="string(sign) = 'DBIT'">
						<xsl:if test="string-length($f86/Z31) &gt; 0">
			                <RltdPties><!--[0..1] TransactionParty1 -->
								<Cdtr>
									<xsl:call-template name="partner-name">
										<xsl:with-param name="node" select="$f86" />
									</xsl:call-template>
								</Cdtr>
								<CdtrAcct>
									<xsl:call-template name="partner-acct">
										<xsl:with-param name="node" select="$f86" />
									</xsl:call-template>
								</CdtrAcct>
							</RltdPties>
						</xsl:if>
						<xsl:if test="string-length($f86/Z30) &gt; 0">
		                    <RltdAgts><!--[0..1] TransactionAgents1 -->
    	                   		<CdtrAgt><!--[0..1] BranchAndFinancialInstitutionIdentification3 -->
    	                        	<FinInstnId><!--[1..1] FinancialInstitutionIdentification5Choice -->
						               	<xsl:choose>
											<xsl:when test="translate($f86/Z30, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', '..........................') = '........'">
			                                	<BIC><!--[1..1] BICIdentifier string /[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}/ -->
    	    		                        		<xsl:value-of select="$f86/Z30" />
    	            		                	</BIC>
    	            		                </xsl:when>
											<xsl:otherwise>
 				                            	<PrtryId><!--[1..1] GenericIdentification3 -->
    	        		                        	<Id><xsl:value-of select="$f86/Z30" /></Id><!--[1..1] Max35Text string 1..35 -->
    	                        			    </PrtryId>
											</xsl:otherwise>
    	           						</xsl:choose>
									</FinInstnId>
								</CdtrAgt>
							</RltdAgts>
						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
               	</xsl:choose>

				<RmtInf>
					<xsl:choose>
						<xsl:when test="string-length($f86/Z00) &gt; 0"><!-- ZKA substructures -->
							<xsl:apply-templates select="$f86/ustrd" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:for-each select="$f86/inf">
								<xsl:if test="string-length(.) &gt; 0">
									<Ustrd>
										<xsl:value-of select="." />
									</Ustrd>
								</xsl:if>
							</xsl:for-each>
						</xsl:otherwise>
					</xsl:choose>
				</RmtInf>

			</TxDtls>
		</Ntry>
	</xsl:template>

	<!-- the information behind all entries -->
	<xsl:template match="F86">
		<AddtlTxInf>
			<xsl:value-of select="translate(string(.), '&#xd;&#xa;', '')" />
		</AddtlTxInf>
	</xsl:template>

<!-- =================== -->
<!-- auxiliary templates -->

	<!-- different types of balances -->
	<xsl:template name="balance">
		<xsl:param name="node"/>
		<xsl:param name="code"/>
		<Bal><xsl:comment><xsl:value-of select="substring(name($node), 2)" /></xsl:comment>
			<Tp><Cd><xsl:value-of select="$code" /></Cd></Tp>
			<xsl:element name="Amt">
				<xsl:attribute name="Ccy"><xsl:value-of select="$node/ccy" /></xsl:attribute>
				<xsl:value-of select="$node/amt" />
			</xsl:element>
			<CdtDbtInd><xsl:value-of select="$node/sign" /></CdtDbtInd>
			<Dt>
				<Dt>
					<xsl:value-of select="$node/yymmdd" />
				</Dt>
			</Dt>
		</Bal>
	</xsl:template><!--balance-->

	<!-- current-time: ISO with timezone -->
	<xsl:template name="current-time">
		<!--2007-10-23T11:56:12+02:00
			1234567890123456789012345-->
		<xsl:value-of select="concat(substring(date:date-time(),1,19), substring(date:date-time(),20))"/>
	</xsl:template><!--current-time-->
	
	<xsl:template name="msgid">
		<xsl:value-of select="concat('4711', '')" />
		<xsl:number />
	</xsl:template><!--msgid-->

	<xsl:template name="partner-name">
		<xsl:param name="node"/>
		<xsl:if test="string-length($node/Z32) &gt; 0 or string-length($node/Z33) &gt; 0">
			<Nm>
				<xsl:value-of select="$node/Z32" />
				<xsl:if test="string-length($node/Z33) &gt; 0">
					<xsl:value-of select="$node/Z33" />
				</xsl:if>
			</Nm>
		</xsl:if>
	</xsl:template><!--partner-name-->

	<xsl:template name="partner-acct">
		<xsl:param name="node"/>
		<xsl:choose>
			<xsl:when test="string-length($node/Z31) &gt; 0">
	        	<Id><!--[1..1] AccountIdentification3Choice -->
					<xsl:choose>
						<xsl:when test="translate(substring($node/Z31,1,2), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', '..........................') = '..'">
	        				<Id><!--[1..1] AccountIdentification3Choice -->
    	            			<IBAN><!--[1..1] IBANIdentifier string /[a-zA-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}/ -->
									<xsl:value-of select="$node/Z31" />
            				    </IBAN>
           					</Id>
						</xsl:when>
						<xsl:otherwise>
                            <PrtryAcct><!--[1..1] SimpleIdentificationInformation2 -->
                                <Id><xsl:value-of select="$node/Z31" /></Id><!--[1..1] Max34Text string 1..34 -->
                            </PrtryAcct>
   						</xsl:otherwise>
					</xsl:choose>
           		</Id>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template><!--partner-acct-->

</xsl:stylesheet>
