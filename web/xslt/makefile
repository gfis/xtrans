#!/usr/bin/make

# @(#) $Id: makefile 9 2008-09-05 05:21:15Z gfis $
# 2006-09-27, Georg Fischer

all:
	xalan -o DtaRecordBean.java Dta.spec.xml genBean.xsl
makexml:
	xsltproc genBean.xsl Dta.spec.xml > DtaRecordBean.java
	