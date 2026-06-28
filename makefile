#!/usr/bin/make

# @(#) $Id: makefile 470 2010-06-08 19:26:56Z gfis $
# 2016-09-12: Georg Fischer: copied from common

DIFF=diff -y --suppress-common-lines --width=160
DIFF=diff -w -rs -C0
SRC=src/main/java/org/teherba/xtrans
TOMC=/var/lib/tomcat/webapps/xtrans
TOMC=c:/var/lib/tomcat/webapps/xtrans
TESTDIR=test
# the following can be overriden outside for single or subset tests,
# for example make regression TEST=U%
TEST="%"
# for Windows, SUDO should be empty
SUDO=
JAVA=java -cp dist/xtrans.jar
JAR=java -jar dist/xtrans.jar
DBAT=$(JAVA) org.teherba.dbat.Dbat -e UTF-8

all: regression
#-------------------------------------------------------------------
# Perform a regression test
regression:
	java -classpath "dist/xtrans.jar" \
			org.teherba.common.RegressionTester $(TESTDIR)/all.tests $(TEST) 2>&1 \
	| tee $(TESTDIR)/regression.log
	grep FAILED $(TESTDIR)/regression.log

#	java -Dlog4j.debug
#----
# Recreate all testcases which failed (i.e. remove xxx.prev.tst)
# Handle with care!
# Failing testcases are turned into "passed" and are manifested by this target!
recreate: recr1 regr2
recr0:
	grep -E '> FAILED' $(TESTDIR)/regression*.log | cut -f 3 -d ' ' | xargs -l -ißß echo rm -v test/ßß.prev.tst
recr1:
	grep -E '> FAILED' $(TESTDIR)/regression*.log | cut -f 3 -d ' ' | xargs -l -ißß rm -v test/ßß.prev.tst
regr2:
	make regression TEST=$(TEST) > x.tmp
#--------------------------------------------------
# test whether all defined tests in all.tests have *.prev.tst results and vice versa
check_tests:
	grep -E "^TEST" $(TESTDIR)/all.tests | cut -b 6-8 | sort | uniq -c > $(TESTDIR)/tests_formal.tmp
	ls -1 $(TESTDIR)/*.prev.tst          | cut -b 6-8 | sort | uniq -c > $(TESTDIR)/tests_actual.tmp
	diff -y --suppress-common-lines --width=32 $(TESTDIR)/tests_formal.tmp $(TESTDIR)/tests_actual.tmp
#---------------------------------------------------
jfind:
	find src -iname "*.java" | xargs -l grep -H $(JF)
rmbak:
	find src -iname "*.bak"  | xargs -l rm -v
#---------------------------------------------------
# some XSLT transformations
ged:
	xalan test/geo/fischer17.ged.xml etc/xslt/prenom_sex.xsl | sort | less
java:
	$(JAR) -java -xml test/proglang/test.java test/proglang/test.java.xml
hameau: hameau1 hameau2
hameau1:
	xsltproc etc/xslt/coordinates.xsl test/geo/hameau.txt.xml  > test/geo/hameau.dim5.xml
hameau2:
	xsltproc etc/xslt/track.xsl       test/geo/hameau.dim5.xml > test/geo/hameau.trk.svg
#-------------------------------------
# parsing
tables: create_tables \
	dummy_tables
create_tables:
	$(JAVA) org.teherba.xtrans.parse.ParseTable -c \
				> etc/sql/tables.create.sql
	$(DBAT) -f	  etc/sql/tables.create.sql
dummy_tables:
	cut -f 1-6 test/parse/items.dummy.raw 		| $(DBAT) -r items
	cut -f 1-4 test/parse/productions.dummy.raw	| $(DBAT) -r productions
parse:
	$(JAR) -sql test/proglang/sqltest.sql -parse -grammar dummy
ident_tables:
	cut -f 1-6 | tr -d "?" | test/parse/items.ident.raw 		| $(DBAT) -r items
	cut -f 1-4 | tr -d "?" | test/parse/productions.ident.raw	| $(DBAT) -r productions
