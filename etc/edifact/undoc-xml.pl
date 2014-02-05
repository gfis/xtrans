#!/usr/bin/perl

# convert UN/Edifact tabular documentation to XML
# @(#) $Id: undoc-xml.pl 9 2008-09-05 05:21:15Z gfis $
# 2008-07-10, Dr. Georg Fischer
# 
# Activation:
#	perl undoc2xml.pl docfile > xmlfile
#
# The particular document basename (e.g. EDCD)
# and the issue (year + version, e.g. 07A) are derived from 'docfile'.
# The following inputfiles are converted to XML:
#   EDCD - Composite specifications
#   EDED - Data element specifications
#   EDSD - Segment specifications
#   message types like FINSTA, PAYMUL etc.
# See also: ISO/TS20625:2002
#------------------------------------------------------
#
# Copyright 2008 Dr. Georg Fischer <punctum at punctum dot kom>
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
use strict;

	my $indent = "";
	my @stack = ();
	my $docfile = shift(@ARGV);
	$docfile =~ m[(\w+)\.(\w+)\Z];
	my $base  = uc($1);
	my $issue = uc($2);
	if ($base =~ m[\A(\w{6})\_\w\Z]) {
		$base = $1;
	} 
	my ($sec, $min, $hour, $mday, $mon, $year, $wday, $yday, $isdst) = localtime (time);
	$yday += 1; $mon += 1; $year += 1900;
	my $timestamp   = sprintf ("%04d-%02d-%02dT%02d:%02d:%02d", $year, $mon, $mday, $hour, $min, $sec);
	
	print <<'GFis';
<?xml version="1.0" encoding="UTF-8"?>
GFis
	pushXML("root");
	print <<'GFis';
<!-- 
	Edifact codelist derived from UN/Edifact tabular documentation 
	with doc2xml.pl: @(#) $Id: undoc-xml.pl 9 2008-09-05 05:21:15Z gfis $
GFis
	print <<"GFis";
	automatically generated at $timestamp, do not edit here!
-->
GFis

	open(DOC, "<", $docfile) || die "cannot read $docfile";
	my $line1 = <DOC>; # read first line to get the title
	$line1 =~ m[\A[\d\.]+\s+(.+)];
	my $title = $1;
	$title =~ s[\s+\Z][]; # remove trailing whitespace
	
	undef $/; # read rest of docfile with slurp mode
	my $content = <DOC>;
	close(DOC);
	my $iblock = 0;
	my $block;
	foreach $block (split(/\-\-\-\-+\r?\n/, $content)) {
		$iblock ++;
		if ($base =~ m[\A(\w{6})\Z]) {
			& eval_message($iblock, $block);
		} elsif ($iblock == 1) { # initial elements
			print "$indent<base>$base</base><issue>$issue</issue>\n";
			print "$indent<title>$title</title>\n";
		} elsif ($iblock >= 2) { # EDxy
			if ($iblock > 2) {
				popXML(); # entry
			}
			pushXML("entry");
			foreach my $part (split (/\r?\n\r?\n/, $block)) {
				$part =~ s[\A\r?\n][];
				$part =~ s[\r?\n\Z][];
				# print "===================\n$part\n";
				if (0) {
				} elsif ($base eq "EDCD") {
					& eval_edcd($part);
				} elsif ($base eq "EDED") {
					& eval_eded($part);
				} elsif ($base eq "EDSD") {
					& eval_edsd($part);
				} else {
					die "unknown base $base";
				}
			} # foreach part	
		} # $iblock >= 2
	} # foreach block

	popXML(); # entry
	popXML(); # root
	exit;
	
	my $edcd_sample = <<'GFis';
----------------------------------------------------------------------

       C008 MONETARY AMOUNT FUNCTION DETAIL

       Desc: To provide the detail of a monetary amount function.

010    5105  Monetary amount function detail
             description code                          C      an..17
020    1131  Code list identification code             C      an..17
030    3055  Code list responsible agency code         C      an..3
040    5104  Monetary amount function detail
             description                               C      an..70

----------------------------------------------------------------------
GFis
sub eval_edcd {
	my $part = shift(@_);
	if (0) {
	} elsif ($part =~ m[\A(\s\s\s+[ X\*\+\|]\s+Desc\:)]i) { # Desc:
		$part = substr($part, length($1));
		pushXML("desc");
		map {
			s[\A\s+][];
			print "$indent<n>$_</n>\n";
		} split (/\r?\n/, $part);
		popXML(); # desc
	} elsif ($part =~ m[\A(\s\s\s+[ X\*\+\|]\s+Note\:)]i) { # Note:
		$part = substr($part, length($1));
		pushXML("note");
		map {
			s[\A\s+][];
			print "$indent<n>$_</n>\n";
		} split (/\r?\n/, $part);
		popXML(); # note
	} elsif ($part =~ m[\A(\d+)\s*[ X\*\+\|]\s+(\w+)\s+]) { # entry with line number
		my @lines = split (/\r?\n/, $part);
		my $iline = 0;
		while ($iline < scalar(@lines)) {
			$_ = $lines[$iline];
			my $head = substr($_,  0, 13);
			my $line = substr($_, 13, 42);
			my $tail = substr($_, 55);
			$head =~ m[\A(\d*)\s*[ X\*\+\|]\s+(\w*)];
			my $seq = $1;
			my $id  = $2;
			if ($seq ne "") {
				pushXML("child", "name=\"$id\" seq=\"$seq\"");
			}
			$line =~ s[\A\s+][];
			$line =~ s[\&][\&amp;]g;
			$line =~ s[\s+\Z][];
			print "$indent<n>$line</n>\n";
			if ($tail =~ m[\A\s*([CMSA])\s*(\d*)\s+([an0-9\.]*)]) {
				my $min = $1;
				my $max = $2;
				my ($typ, $len) = split(/\.\./, $3);
				$min =~ tr[CM][01];
				print "$indent<min>$min</min>";
				if (length($max) >= 1) {
					print "<max>$max</max>";
				}
				print "\n";
				print "$indent<type>$typ</type><length>$len</length>\n";
				popXML(); # child
			}
			$iline ++;
		} # while $iline
	} elsif ($part =~ m[\A\s\s\s+([ X\*\+\|])\s+(\w+)\s*([\w\s]*)]) { # title
		my $code = $2;
		my $title = $3;
		print "$indent<code>$code</code><title>$title</title>\n";
	} else {
		print "<!-- unknown part: $part -->\n";
	}
} # eval_edcd

	my $eded_sample = <<'GFis';
----------------------------------------------------------------------

     1131  Code list identification code                           [C]

     Desc: Code identifying a user or association maintained code
           list.

     Repr: an..17

     Note: 
           1 The codes for this data element are provided by the
           code list responsible agency defined in data element
           3055.

0123456789012345678901234567890123456789012345678901234567890123456789
----------------------------------------------------------------------
GFis
sub eval_eded {
	my $part = shift(@_);
	if (0) {
	} elsif ($part =~ m[\A(\s\s\s+[ X\*\+\|]\s+Desc\:)]i) { # Desc:
		$part = substr($part, length($1));
		pushXML("desc");
		map {
			s[\A\s+][];
			print "$indent<n>$_</n>\n";
		} split (/\r?\n/, $part);
		popXML(); # desc
	} elsif ($part =~ m[\A(\s\s\s+[ X\*\+\|]\s+Note\:)]i) { # Note:
		$part = substr($part, length($1));
		pushXML("note");
		map {
			s[\A\s+][];
			print "$indent<n>$_</n>\n";
		} split (/\r?\n/, $part);
		popXML(); # note
	} elsif ($part =~ m[\A(\s\s\s+[ X\*\+\|]\s+Repr\:)\s+([an0-9\.]+)]i) { # Repr:
		my ($typ, $len) = split(/\.\./, $2);
		print "$indent<type>$typ</type><length>$len</length>\n";
	} elsif ($part =~ m[\A[ X\*\+\|]\s+(\d+)\s+(.+)]i) { # entry with code number
		my $code = $1;
		my $title = $2;
		$title =~ s{\s*\[[BCI]\]\s*\Z}{}; # remove whether batch, interactive or common use
		print "$indent<code>$code</code><title>$title</title>\n";
	} elsif ($part =~ m[\A\s\s\s+([ X\*\+\|])\s+(\w+)\s*([\w\s]*)]) { # title
		my $code = $2;
		my $title = $3;
		print "$indent<code>$code</code><title>$title</title>\n";
	} else {
		print "<!-- unknown part: $part -->\n";
	}
} # eval_eded

	my $edsd_sample = <<'GFis';
----------------------------------------------------------------------

       APP  APPLICABILITY

       Function: To specify the applicability.

010    9051 APPLICABILITY CODE QUALIFIER               C    1 an..3

020    C973 APPLICABILITY TYPE                         C    1
       9049  Applicability type description code       C      an..3
       1131  Code list identification code             C      an..17
       3055  Code list responsible agency code         C      an..3
       9048  Applicability type description            C      an..35

0123456789012345678901234567890123456789012345678901234567890123456789
----------------------------------------------------------------------
GFis
sub eval_edsd {
	my $part = shift(@_);
	if (0) {
	} elsif ($part =~ m[\A(\s\s\s+[ X\*\+\|]\s+Function\:)]i) { # Desc:
		$part = substr($part, length($1));
		pushXML("function");
		map {
			s[\A\s+][];
			print "$indent<n>$_</n>\n";
		} split (/\r?\n/, $part);
		popXML(); # function
	} elsif ($part =~ m[\A(\d+\s*[ X\*\+\|]\s+(\w+)\s+)]) { # entry with line number
		my $code = $2;
		my @lines = split (/\r?\n/, $part);
		my $iline = 0;
		my $first_tag = "child";
		if ($code =~ m[\A\d]) {
			pushXML("element");
		} else {
			pushXML("composite");
			$first_tag = "data";
		}
		while ($iline < scalar(@lines)) {
			$_ = $lines[$iline];
			my $head = substr($_,  0, 12);
			my $line = substr($_, 12, 43);
			my $tail = substr($_, 55);
			my $seq = "";
			my $id  = "";
			if ($head =~ m[\A(\d*)\s*[ X\*\+\|]\s+(\w+)]) {
				$seq = $1;
				$id  = $2;
			}
			pushXML($first_tag, "name=\"$id\" seq=\"$seq\"");
			$first_tag = "child";
			$line =~ s[\A\s+][];
			$line =~ s[\&][\&amp;]g;
			$line =~ s[\s+\Z][];
			print "$indent<n>$line</n>\n";
			if ($tail =~ m[\A\s*([CMSA])\s+(\d*)\s*([an0-9\.]*)]) {
				my $min = $1;
				my $max = $2;
				my ($typ, $len) = split(/\.\./, $3);
				$min =~ tr[CM][01];
				print "$indent<min>$min</min>";
				if (length($max) >= 1) {
					print "<max>$max</max>";
				}
				print "\n";
				print "$indent<type>$typ</type><length>$len</length>\n";
			}
			popXML(); # child
			$iline ++;
		} # while $iline
		popXML(); # element or composite
	} elsif ($part =~ m[\A\s\s\s+([ X\*\+\|])\s+(\w+)\s*([\w\s]*)]) { # title
		my $code = $2;
		my $title = $3;
		print "$indent<code>$code</code><title>$title</title>\n";
	} else {
		print "<!-- unknown part: $part -->\n";
	}
} # eval_edsd

	my $message_sample = <<'GFis';
----------------------------------------------------------------------
...
4.3.1  Segment table

Pos    Tag Name                                      S   R

0010   UNH Message header                            M   1     
0020   BGM Beginning of message                      M   1     
0030   DTM Date/time/period                          M   1     

0040       ----- Segment group 1  ------------------ C   1-----------+
0050   RFF Reference                                 M   1           |
0060   DTM Date/time/period                          C   1-----------+

0070       ----- Segment group 2  ------------------ C   5-----------+
0080   FII Financial institution information         M   1           |
0090   CTA Contact information                       C   1           |
0100   COM Communication contact                     C   5-----------+

0110       ----- Segment group 3  ------------------ C   3-----------+
0120   NAD Name and address                          M   1           |
0130   CTA Contact information                       C   1           |
0140   COM Communication contact                     C   5-----------+

0150       ----- Segment group 4  ------------------ M   9999--------+
0160   LIN Line item                                 M   1           |
0170   FII Financial institution information         M   1           |
0180   RFF Reference                                 M   1           |
0190   FTX Free text                                 C   1           |
                                                                     |
0200       ----- Segment group 5  ------------------ M   99---------+|
0210   MOA Monetary amount                           M   1          ||
0220   DTM Date/time/period                          C   1----------+|
                                                                     |
0230       ----- Segment group 6  ------------------ C   9999-------+|
0240   SEQ Sequence details                          M   1          ||
0250   RFF Reference                                 M   5          ||
0260   DTM Date/time/period                          M   2          ||
0270   BUS Business function                         M   1          ||
0280   MOA Monetary amount                           M   1          ||
0290   FTX Free text                                 C   1----------++
0300   CNT Control total                             C   5     

0310       ----- Segment group 7  ------------------ C   5-----------+
0320   AUT Authentication result                     M   1           |
0330   DTM Date/time/period                          C   1-----------+
0340   UNT Message trailer                           M   1     

01234567890123456789012345678901234567890123456789012345678901234567890123456789
GFis
sub eval_message {
	my ($iblock, $block) = @_;
	my @lines = split (/\r?\n/, $block);
	my $iline = 0;
	my $skip = 1;
	while ($iline < scalar(@lines)) {
		$_ = $lines[$iline ++];
		if ($skip == 1) {
			# print "mess.$iline.$iblock: $_\n";
			if (0) {
			} elsif ($iblock == 1 && $iline == 4) {
				$title = $_;
				$title =~ s[\A\s+][];
				$title =~ s[\s+\Z][];
			} elsif (m[\APos\s\s+Tag\s+Name\s+]) {
				$skip = 0;
				pushXML("message", "name=\"$base\" issue=\"$issue\"");
				print "$indent<title>$title</title>\n";
			}
			next;
		} 
		if (m[\A\s*\|*\s*\Z]) {
			# skip empty lines
		} else { # numbered line
			my $head = substr($_,  0, 11);
			my $line = substr($_, 11, 42);
			my $tail = substr($_, 53);
			$head =~ m[\A(\d{4})[ X\*\+\|]{3}([ \w]+)];
			my $seq  = $1;
			my $name = $2;
			$name =~ s[\s][]g;
			$line =~ s[\A\s+][];
			$line =~ s[\&][\&amp;]g;
			$line =~ s[\s+\Z][];
			$tail =~ m[\A\s*([CMSA])\s+(\d*)\s*([\-\+\|]*)];
			my $min = $1;
			my $max = $2;
			my $bracket = $3;
			$min =~ tr[CM][01];
			if (0) {
			} elsif ($name =~ m[\A\Z]) { # ----- Segment group 5  ------------------ M   99---------+|
				# open group
				$line =~ m[group\s+(\d+)];
				$name = "$1";
				pushXML("group", "name=\"$name\"");
				print "$indent<min>$min</min>";
				if (length($max) >= 1) {
					print "<max>$max</max>";
				}
				print "<seq>$seq</seq>\n";
			} else { # ordinary segment
				pushXML("segm", "name=\"$name\"");
				print "$indent<min>$min</min>";
				if (length($max) >= 1) {
					print "<max>$max</max>";
				}
				print "<seq>$seq</seq>\n";
				print "$indent<n>$line</n>\n";
				popXML(); # $name
				my $level = ($bracket =~ s[\+][]g); # count plus signs
				while ($level > 0) { 
					popXML(); # close some segment group
					$level --;
				} # while $level
			} # ordinary segment
		} # numbered line
	} # while $iline
	if ($skip == 0) {
		# popXML(); # message
	} 
} # eval_message

#--------------------------------------------------------
sub pushXML {
	my ($tag, $attrs) = @_;
	push (@stack, $tag);
	print "$indent<$tag" . (length($attrs) > 0 ? " $attrs" : "") . ">\n";
	$indent .= "  ";
} # pushXML

sub popXML {
	my $tag = pop(@stack);
	$indent = substr($indent, 2);
	print "$indent</$tag>\n";
} # popXML

__DATA__
