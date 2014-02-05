#!/usr/bin/perl

#
#  Copyright 2006 Dr. Georg Fischer <punctum at punctum dot kom>
# 
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
# 
#       http://www.apache.org/licenses/LICENSE-2.0
# 
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
#------------------------------------------------------------------ 
# example client - short perl program 
# for SOAP access to Transformer service
# @(#) $Id: client.pl 9 2008-09-05 05:21:15Z gfis $
# 2005-08-26: copied from googly.pl
#
# Usage: 
#   perl client.pl language [function [digits]]
#       where   language = de, fr, en, ...
#               function = spell, parse, month, weekday, season
#
# The essential 4 lines in the program below are marked with # <===
#------------------------------------------------------------------ 

use strict;
use utf8;
use SOAP::Lite; # <===
binmode(STDOUT, ":utf8"); # <-- SOAP response is UTF-8, pass it to output
     
    # take up to 3 parameters from the command line
    my $language = shift @ARGV;
    my $function = "spell";
    my $digits   = "";
    if (scalar (@ARGV) > 0) {
        $function = shift @ARGV;
        if (scalar (@ARGV) > 0) {
            $digits = join (" ", @ARGV);
        }
    }
    else {
        print   "usage: perl client.pl language [function [digits]]\n"
            .   "   where   language = de, fr, en ...\n"
            .   "       function = spell, parse, months ...\n"
            ;
    }
         
    # create a new SOAP::Lite instance from the WSDL
    my $path = $0;
    $path =~ s[client\.pl][xtrans.wsdl];
    my $service = SOAP::Lite->service("file:$path"); # <===

    # call XtransService
    my $results = $service->getResponse ($language, $function, $digits); # <===
    print $results, "\n"; # <===
