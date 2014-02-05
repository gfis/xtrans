#!/usr/bin/perl

# add or remove a sort key from a lien oriented XML file
# @(#) $Id: sortkey.pl 832 2011-11-28 07:55:02Z gfis $
# Copyright (c) 2011 Dr. Georg Fischer
# 2011-11-27: for xtrans -jcl
# 
# Activation:
#	xtrans -jcl job.jcl -xml | perl sortkey.pl -add | sort | perl -sortkey -rem | xtrans -xml -jcl 
#-----------------------------------------------------------------------------
use strict;

	if (scalar(@ARGV) == 0) {
		print <<"GFis";
usage: perl $0 (-add|-rem) [-c] [-p] infile.xml > outfile.key
		-c	remove comments
		-p	replace name of program (from job name[2:6])
GFis
	}
	my $action 		= "add";
	my $no_comment 	= 0;
	my $pgm_replace = 0;
	my $pgm_name 	= "";
	while (scalar(@ARGV) > 0 && ($ARGV[0] =~ m{\A\-})) {
		my $opt = shift(@ARGV);
		if (0) {
		} elsif ($opt =~ m{\A\-a}) {
			$action = "add";
		} elsif ($opt =~ m{\A\-[sr]}) {
			$action = "rem";
		} elsif ($opt =~ m{\A\-c}) {
			$no_comment = 1;
		} elsif ($opt =~ m{\A\-p}) {
			$pgm_replace = 1;
		}
	} # while @ARGV
	# print STDERR "action=$action\n";
	$ARGV[0] =~ m{([^\/\\]+)\Z}; # last word in path
	my $job_name = $1;
	$job_name =~ s{\.\w+\Z}{}; # remove any extension
	$pgm_name = substr($job_name, 1, 5);
	print STDERR "PGM=$pgm_name\n";

	my $stmt_no = 0; # statement number, over jobs etc.
	my $dd_no   = 0; # sequential number in DD statement
	my $label   = ""; # current (previous) label
	my $seq     = "";
	my $op      = "";
	my $key 	= ""; # = stmt_no,label,dd_no
	my $stmt 	= "";
	while (<>) {
		my $line = $_;
		if ($action =~ m{\Aa}) { # add sort key
			# print "add: $line";
			if ($pgm_replace) {
				$line =~ s[$job_name][\{job\}]g;
				$line =~ s[$pgm_name][\{pgm\}]g;
			}
			if (0) {
			} elsif ($line =~ m{\A\<(\w+)}) {
				$stmt  = $1;
				if (0) {
				} elsif ($stmt eq "stmt") {
					if ($line =~ m{ label\=\"(\w+)\"}) {
						$label = $1;
						$seq = 0;
					}
					if ($line =~ m{ seq\=\"(\w+)\"}) {
					#	$seq   = $1;
					}
					if ($line =~ m{ op\=\"(\w+)\"}) {
						$op = $1;
					}
					if ($op eq "EXEC") {
						
					} # EXEC
				} elsif ($stmt eq "data") {
				} elsif ($stmt eq "comment") {
					# $seq = 0;
					# $label = "";
				} else {
					# $seq = 0;
				}
			} else {
				$stmt_no ++;
				$seq = 0;
				$label = "";
				$stmt = "";
			}
			$key = sprintf("%05d,%s,%04d", $stmt_no, $label, $seq);
			if ($no_comment and $stmt eq "comment") {
			} else {
				print "$key=$line";
			}
			if (0) {
			} elsif ($op eq "DD") {
				# $stmt_no = $stmt_no;
			} else { # EXEC, PROC etc.
				$stmt_no ++;
				$seq = 0;
			} 
			$seq ++;
		} else { # remove sort key
			# print "rem: $line";
			$line =~ s{\A[^\=]+\=}{}; # all up to and including "="
			print $line;
		} # remove
	} # while <>  
	
	my ($sec, $min, $hour, $mday, $mon, $year, $wday, $yday, $isdst) = localtime (time); $mon ++; $year += 1900;
	my $iso_today = sprintf("%04d-%02d-%02d", $year, $mon, $mday);
	my $iso_now = $iso_today . sprintf("T%02d:%02d:%02d", $hour, $min, $sec);

__DATA__
<?xml version="1.0" encoding="UTF-8"?>
<jcl>
<stmt label="TCNR52I1" seq="0" op="JOB"><parm id="1"><group level="1"><parm id="2">141605</parm><parm id="3">PERM</parm></group></parm><parm id="4"><text>AS.VERS. KUNDE/PERS</text></parm><parm name="MSGCLASS">1</parm><parm name="MSGLEVEL"><group level="1"><parm id="5">1</parm><parm id="6">1</parm></group></parm></stmt>
<comment>*******************************************************************</comment>
<comment>     LETZTE AENDERUNG WURDE AM 11/06/16 VON RZSX DURCHGEFUEHRT    *</comment>
<comment>                         VERSION 002                              *</comment>
<comment>*******************************************************************</comment>
<comment>        ASYNCHRONE VERSORGUNG KUNDE/PERSON                        *</comment>
<comment>        PERMANENTLÄUFER FAS-AUSLEITUNG                            *</comment>
<comment>*******************************************************************</comment>
<stmt label="CNR52" seq="0" op="EXEC"><parm name="PGM">DFSRRC00</parm><parm name="REGION">32M</parm><parm name="TIME">1440</parm><parm name="PARM"><group level="1"><parm id="1">BMP</parm><parm id="2">CNR52</parm><parm id="3">CNR52</parm><parm id="4"></parm><parm id="5"></parm><parm id="6">N00064</parm><parm id="7">BM</parm><parm id="8"></parm><parm id="9"></parm><parm id="10">0</parm><parm id="11"></parm><parm id="12">05</parm><parm id="13">03</parm><parm id="14">IMSU</parm><parm id="15"></parm><parm id="16">DB2U</parm><parm id="17"></parm><parm id="18"></parm><parm id="19"></parm><parm id="20">10</parm></group></parm></stmt>
<stmt label="ALLOC" seq="0" op="DD"><parm name="DSN">TUPR.TCNR52I1.STCLOCK</parm><parm name="DISP"><group level="1"><parm id="1">MOD</parm><parm id="2">CATLG</parm><parm id="3">CATLG</parm></group></parm><parm name="SPACE"><group level="1"><parm id="4">TRK</parm><parm><group level="2"><parm>1</parm><parm>0</parm></group></parm><parm id="5">RLSE</parm></group></parm><parm name="STORCLAS">PCSTD</parm><parm name="MGMTCLAS">NOHSM</parm><parm name="RECFM">FB</parm><parm name="LRECL">80</parm><parm name="BLKSIZE">0</parm></stmt>
<stmt label="ALLOCSTK" seq="0" op="DD"><parm name="DSN">RZ1.KK.STEULIB(CNR52IP)</parm><parm name="DISP">SHR</parm></stmt>
<stmt seq="1" op="DD"><parm name="DSN">RZ1.KK.STEULIB(CNR52IS)</parm><parm name="DISP">SHR</parm></stmt>
<stmt label="STEPLIB" seq="0" op="DD"><parm name="DSN">SYS3.IMST.SOFTWARE.LOAD</parm><parm name="DISP">SHR</parm></stmt>
<stmt seq="1" op="DD"><parm name="DSN">SYS3.IMST.RESLIB</parm><parm name="DISP">SHR</parm></stmt>
<stmt seq="2" op="DD"><parm name="DSN">SYS4.TEST.PGMLIB</parm><parm name="DISP">SHR</parm></stmt>
<stmt seq="3" op="DD"><parm name="DSN">SYS3.PEV.PGMLIB</parm><parm name="DISP">SHR</parm></stmt>
<stmt seq="4" op="DD"><parm name="DSN">SYS4.TEST2.PGMLIB</parm><parm name="DISP">SHR</parm></stmt>
<stmt seq="5" op="DD"><parm name="DSN">DVK.PEVIMSE.PGMLIB</parm><parm name="DISP">SHR</parm></stmt>
<stmt seq="6" op="DD"><parm name="DSN">MEMO.API.LOAD</parm><parm name="DISP">SHR</parm></stmt>
<stmt label="IMS" seq="0" op="DD"><parm name="DSN">SYS3.IMST.PSBLIB</parm><parm name="DISP">SHR</parm></stmt>
<stmt seq="1" op="DD"><parm name="DSN">SYS3.IMST.DBDLIB</parm><parm name="DISP">SHR</parm></stmt>
<stmt label="PROCLIB" seq="0" op="DD"><parm name="DSN">SYS3.IMT.PARMLIB</parm><parm name="DISP">SHR</parm></stmt>
<stmt label="DFSESL" seq="0" op="DD"><parm name="DSN">SYS3.IMST.RESLIB</parm><parm name="DISP">SHR</parm></stmt>
<stmt seq="1" op="DD"><parm name="DSN">SYS1.DB2.ENTW.DSNLOAD</parm><parm name="DISP">SHR</parm></stmt>
<stmt seq="2" op="DD"><parm name="DSN">SYS1.MQS.ENTW.CSQLOAD</parm><parm name="DISP">SHR</parm></stmt>
<stmt label="APIPARM" seq="0" op="DD"><parm name="DSN">MEMO.API.PARMLIB</parm><parm name="DISP">SHR</parm></stmt>
<stmt label="APIOKLOG" seq="0" op="DD"><parm id="1">DUMMY</parm></stmt>
<stmt label="APIERLOG" seq="0" op="DD"><parm id="1">DUMMY</parm></stmt>
<stmt label="PLIDMMP" seq="0" op="DD"><parm name="SYSOUT">D</parm><parm name="DEST">N1</parm></stmt>
<stmt label="SYSABOUT" seq="0" op="DD"><parm name="SYSOUT">D</parm><parm name="DEST">N1</parm></stmt>
<stmt label="SYSDBOUT" seq="0" op="DD"><parm name="SYSOUT">D</parm><parm name="DEST">N1</parm></stmt>
<stmt label="SYSUDUMP" seq="0" op="DD"><parm name="SYSOUT">D</parm><parm name="DEST">N1</parm></stmt>
<stmt label="STPRCDD" seq="0" op="DD"><parm name="SYSOUT">D</parm><parm name="DEST">N1</parm></stmt>
<stmt label="SYSPRTST" seq="0" op="DD"><parm name="SYSOUT">*</parm></stmt>
<stmt label="SYSPRINT" seq="0" op="DD"><parm name="SYSOUT">*</parm></stmt>
<stmt label="SYSOUT" seq="0" op="DD"><parm name="SYSOUT">*</parm></stmt>
<stmt label="DXBOPT" seq="0" op="DD"><parm id="1">DUMMY</parm></stmt>
<stmt label="IGZSRTCD" seq="0" op="DD"><parm id="1">DUMMY</parm></stmt>
<stmt label="DPKTABLE" seq="0" op="DD"><parm name="DSN">SYS3.IMST.DPKTABLE</parm><parm name="DISP">SHR</parm></stmt>
<stmt label="DFSVSAMP" seq="0" op="DD"><parm name="DSN">RZ1.KK.STEULIB(CI04096)</parm><parm name="DISP">SHR</parm></stmt>
<stmt label="CNKARI01" seq="0" op="DD"><parm id="1">*</parm></stmt>
<data>S01TASK-ID=TCNR52I1T9000</data>
<stmt label="ZKARI1" seq="0" op="DD"><parm id="1">*</parm></stmt>
<data>S01JOB=TCNR52I1,PGM=CNR52   ,PLAN=CNR52</data>
<comment>-------------------------------------------------------------------</comment>
</jcl>
