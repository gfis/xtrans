#!/usr/bin/make

# @(#) $Id: makefile 470 2010-06-08 19:26:56Z gfis $
JAVA=java -cp dist/xtrans.jar
JAR=java -jar dist/xtrans.jar
DBAT=$(JAVA) org.teherba.dbat.Dbat -e UTF-8 

all:
#------------------------------------
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
