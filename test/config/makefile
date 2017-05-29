# makesub - makefile for subdirectories of ./hahn
# set THIS outside

TOOL=../../../../../tool/
.SUFFIXES: .des .htm
.des.htm:
	perl $(TOOL)desnatl.pl en $*.des > $*.htm
	perl $(TOOL)prenatl.pl es $*.htm | perl $(TOOL)delnatl.pl es > $*.es.html

$(THIS): $(THIS).htm $(THIS).tab.des
	cp $(THIS).en.html index.html
#         in order to prevent directory browsing

$(THIS).tab.des: $(THIS).dat
	perl $(TOOL)datprep.pl $(THIS).dat | perl $(TOOL)datdes.pl table > $@
$(THIS).lis.des: $(THIS).dat
	perl $(TOOL)datprep.pl $(THIS).dat | perl $(TOOL)datdes.pl list  > $@

