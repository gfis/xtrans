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
# close <img> elements
# 2006-11-22, Dr. Georg Fischer
use locale;

my $letter;
my $code;
while (<DATA>) {
    if (m/\<td\>([^\<]+)\</) {
        $letter = $1;
    } elsif (m/\<td\>/ && m/alt=\"([^\"]+)\"/) {
        $code = $1;
        $code =~ s/\\cdot/\./g;
        $code =~ s/\\(begin|end)\{matrix\}//g;
        $code =~ s/\s//g;
        print "\t\tputup(\"$letter\",\t\"$code\"\t\t);\n";
    }
}
__DATA__
<?xml version="1.0" encoding="ISO-8859-1"?>
<html>
<table class="hintergrundfarbe1 rahmenfarbe1" style="border-style: solid; border-width: 1px; margin: 1em 1em 1em 0pt; border-collapse: collapse; empty-cells: show;" border="2" cellpadding="4" cellspacing="0" rules="all">
<tbody><tr class="hintergrundfarbe6">
<td colspan="2">Lateinische Buchstaben</td>

</tr>
<tr class="hintergrundfarbe6">
<td><b>Buchstabe</b></td>
<td><b>Code</b></td>
</tr>
<tr>
<td>A</td>
<td><img class="tex" src="Morsecode-Dateien/81eda3661331f0dc094c2ce3616553a9.png" alt="\cdot -"></td>
</tr>
<tr>
<td>B</td>
<td><img class="tex" src="Morsecode-Dateien/13ce539c03f1bd0406c828097f2fcc3c.png" alt="- \cdot \cdot \cdot"></td>
</tr>

<tr>
<td>C</td>
<td><img class="tex" src="Morsecode-Dateien/873d96dec8b8a9098f9331bc2c3c8367.png" alt="- \cdot - \cdot"></td>
</tr>
<tr>
<td>D</td>
<td><img class="tex" src="Morsecode-Dateien/9088890eb5680315048d92b181b97e7a.png" alt="- \cdot \cdot"></td>
</tr>
<tr>
<td>E</td>
<td><img class="tex" src="Morsecode-Dateien/36f8ae4c86b69d52d037a6802d91cc4a.png" alt="\cdot"></td>
</tr>
<tr>
<td>F</td>

<td><img class="tex" src="Morsecode-Dateien/563c2ae1733285a0c33e9095d458cbad.png" alt="\cdot \cdot - \cdot"></td>
</tr>
<tr>
<td>G</td>
<td><img class="tex" src="Morsecode-Dateien/641616981236afb88617cf44f4715a73.png" alt="- - \cdot"></td>
</tr>
<tr>
<td>H</td>
<td><img class="tex" src="Morsecode-Dateien/c311f7097f52e98740594c1ae876c9a8.png" alt="\cdot \cdot \cdot \cdot"></td>
</tr>
<tr>
<td>I</td>
<td><img class="tex" src="Morsecode-Dateien/a5c13314525d07bc439dd614f17f739c.png" alt="\cdot \cdot"></td>
</tr>

<tr>
<td>J</td>
<td><img class="tex" src="Morsecode-Dateien/c1bcca114c1eab8a44b748b6b4ba4072.png" alt="\cdot - - -"></td>
</tr>
<tr>
<td>K</td>
<td><img class="tex" src="Morsecode-Dateien/06fe3e88c45f73781ca2c4c15912f197.png" alt="- \cdot -"></td>
</tr>
<tr>
<td>L</td>
<td><img class="tex" src="Morsecode-Dateien/273af353b69607fa999dceba73845397.png" alt="\cdot - \cdot \cdot"></td>
</tr>
<tr>
<td>M</td>

<td><img class="tex" src="Morsecode-Dateien/4992b182f7f6e0c18ae1183a1df51495.png" alt="\begin{matrix} - - \end{matrix}"></td>
</tr>
<tr>
<td>N</td>
<td><img class="tex" src="Morsecode-Dateien/0d41b8afc911f0381d00662eaaf84279.png" alt="- \cdot"></td>
</tr>
<tr>
<td>O</td>
<td><img class="tex" src="Morsecode-Dateien/27aadbbff1277f68add80a130520a840.png" alt="\begin{matrix} - - - \end{matrix}"></td>
</tr>
<tr>
<td>P</td>
<td><img class="tex" src="Morsecode-Dateien/2d8fbf92192fe3bd538d532954bb99d0.png" alt="\cdot - - \cdot"></td>
</tr>

<tr>
<td>Q</td>
<td><img class="tex" src="Morsecode-Dateien/b2aa46b926356dffeb1eff7cf0dae1cd.png" alt="- - \cdot -"></td>
</tr>
<tr>
<td>R</td>
<td><img class="tex" src="Morsecode-Dateien/61722953e253355fed79880eefc2b1f4.png" alt="\cdot - \cdot"></td>
</tr>
<tr>
<td>S</td>
<td><img class="tex" src="Morsecode-Dateien/f96217d18c60c280226bac50e510092b.png" alt="\cdot \cdot \cdot"></td>
</tr>
<tr>
<td>T</td>

<td><img class="tex" src="Morsecode-Dateien/102b3cf9b9c37bb8526b0c4fae990724.png" alt="\begin{matrix} - \end{matrix}"></td>
</tr>
<tr>
<td>U</td>
<td><img class="tex" src="Morsecode-Dateien/d53cef2815a631da1cdab9e9e28081db.png" alt="\cdot \cdot -"></td>
</tr>
<tr>
<td>V</td>
<td><img class="tex" src="Morsecode-Dateien/26c933b2d7f564187b40010985c1a31f.png" alt="\cdot \cdot \cdot  -"></td>
</tr>
<tr>
<td>W</td>
<td><img class="tex" src="Morsecode-Dateien/f173e04d05b03b06bf8be5394ca9a7ab.png" alt="\cdot - -"></td>
</tr>

<tr>
<td>X</td>
<td><img class="tex" src="Morsecode-Dateien/19407e0018d43cd7673e1d5a3199ebf0.png" alt="- \cdot \cdot -"></td>
</tr>
<tr>
<td>Y</td>
<td><img class="tex" src="Morsecode-Dateien/5fdd2292368c511783d50f52082bc58b.png" alt="- \cdot - -"></td>
</tr>
<tr>
<td>Z</td>
<td><img class="tex" src="Morsecode-Dateien/3482d46a4fd4b45f29f5da15ac16a3f2.png" alt="- - \cdot \cdot"></td>
</tr>
</tbody></table>
</th>

<td></td>
<th valign="top">
<table class="hintergrundfarbe1 rahmenfarbe1" style="border-style: solid; border-width: 1px; margin: 1em 1em 1em 0pt; border-collapse: collapse; empty-cells: show;" border="2" cellpadding="4" cellspacing="0" rules="all">
<tbody><tr class="hintergrundfarbe6">
<td colspan="2">Zahlen</td>
</tr>
<tr class="hintergrundfarbe6">
<td><b>Zahl</b></td>
<td><b>Code</b></td>
</tr>
<tr>
<td>1</td>
<td><img class="tex" src="Morsecode-Dateien/ca2c1bfcc68fb0bebd0cc8333dc1a7ab.png" alt="\cdot - - - -"></td>

</tr>
<tr>
<td>2</td>
<td><img class="tex" src="Morsecode-Dateien/cb99f364f0d78faedaab46c8d251994a.png" alt="\cdot \cdot - - -"></td>
</tr>
<tr>
<td>3</td>
<td><img class="tex" src="Morsecode-Dateien/d0ef0cd2820b5778c03661f88d643e57.png" alt="\cdot \cdot \cdot - -"></td>
</tr>
<tr>
<td>4</td>
<td><img class="tex" src="Morsecode-Dateien/6329ef82e275478d72fce509a6e33b52.png" alt="\cdot \cdot \cdot \cdot -"></td>
</tr>
<tr>

<td>5</td>
<td><img class="tex" src="Morsecode-Dateien/f79481393d8a6906653b4067641b6840.png" alt="\cdot \cdot \cdot \cdot \cdot"></td>
</tr>
<tr>
<td>6</td>
<td><img class="tex" src="Morsecode-Dateien/9eead421f6ccd8453a0eaa090e644897.png" alt="- \cdot \cdot \cdot \cdot"></td>
</tr>
<tr>
<td>7</td>
<td><img class="tex" src="Morsecode-Dateien/744676dd601aa2a480a2b1276bce75fe.png" alt="- - \cdot \cdot \cdot"></td>
</tr>
<tr>
<td>8</td>

<td><img class="tex" src="Morsecode-Dateien/11c79925b64bb6737b553a065e83982e.png" alt="- - - \cdot \cdot"></td>
</tr>
<tr>
<td>9</td>
<td><img class="tex" src="Morsecode-Dateien/6cc7803975448c6405ada04e2141d112.png" alt="- - - - \cdot"></td>
</tr>
<tr>
<td>0</td>
<td><img class="tex" src="Morsecode-Dateien/19e8c5a324a8709f9c4ecd5b513c50ec.png" alt="\begin{matrix} - - - - - \end{matrix}"></td>
</tr>
</tbody></table>
</th>
<td></td>
<th valign="top">
<table class="hintergrundfarbe1 rahmenfarbe1" style="border-style: solid; border-width: 1px; margin: 1em 1em 1em 0pt; border-collapse: collapse; empty-cells: show;" border="2" cellpadding="4" cellspacing="0" rules="all">

<tbody><tr class="hintergrundfarbe6">
<td colspan="2">Sonder- und Satzzeichen</td>
</tr>
<tr class="hintergrundfarbe6">
<td><b>Zeichen</b></td>
<td><b>Code</b></td>
</tr>
<tr>
<td>À</td>
<td><img class="tex" src="Morsecode-Dateien/299dc30c202c24dded861820e0372d74.png" alt="\cdot - - \cdot -"></td>
<td>Å</td>
<td><img class="tex" src="Morsecode-Dateien/299dc30c202c24dded861820e0372d74.png" alt="\cdot - - \cdot -"></td>
</tr>
<tr>
<td>Ä</td>

<td><img class="tex" src="Morsecode-Dateien/161bcc06eddbfe1fe4b79c43e463c467.png" alt="\cdot - \cdot -"></td>
</tr>
<tr>
<td>È</td>
<td><img class="tex" src="Morsecode-Dateien/ed682275c8144344c03f24dbdf8b0b3e.png" alt="\cdot - \cdot \cdot -"></td>
</tr>
<tr>
<td>É</td>
<td><img class="tex" src="Morsecode-Dateien/4aade46f6dd01b804a53a986eff358bb.png" alt="\cdot \cdot - \cdot \cdot"></td>
</tr>
<tr>
<td>Ö</td>
<td><img class="tex" src="Morsecode-Dateien/255678242f529fc970de6899fc6acf33.png" alt="- - - \cdot"></td>
</tr>

<tr>
<td>Ü</td>
<td><img class="tex" src="Morsecode-Dateien/c94807f44228079265b4bd4a573bdc53.png" alt="\cdot \cdot - -"></td>
</tr>
<tr>
<td>ß</td>
<td><img class="tex" src="Morsecode-Dateien/a85c314a4feb1cbde0c42c9d7a63ec4d.png" alt="\cdot \cdot \cdot - - \cdot \cdot"></td>
</tr>
<tr>
<td>CH</td>
<td><img class="tex" src="Morsecode-Dateien/f1c769f3642b7326431913d636e238c1.png" alt="\begin{matrix} - - - - \end{matrix}"></td>
</tr>
<tr>
<td>Ñ</td>

<td><img class="tex" src="Morsecode-Dateien/fe184679d65fd3da9201669fca5f6eff.png" alt="- - \cdot - -"></td>
</tr>
<tr>
<td>.<i>(AAA)</i></td>
<td><img class="tex" src="Morsecode-Dateien/48ee9ab63e301f868221f9627aca2ba1.png" alt="\cdot - \cdot - \cdot -"></td>
</tr>
<tr>
<td>,<i>(MIM)</i></td>
<td><img class="tex" src="Morsecode-Dateien/0992aafba153b7d03558fd453fceb1df.png" alt="- - \cdot \cdot - -"></td>
</tr>
<tr>
<td>:</td>
<td><img class="tex" src="Morsecode-Dateien/8c167ee4f0d498b1f11d82c4e376ba68.png" alt="- - - \cdot \cdot \cdot"></td>
</tr>
<tr>
<td>;</td>
<td><img class="tex" src="Morsecode-Dateien/7d771e9de6fa7376dd43c6801b06d3d1.png" alt="- \cdot - \cdot - \cdot"></td>
</tr>
<tr>
<td>?<i>(IMI)</i></td>
<td><img class="tex" src="Morsecode-Dateien/596ce746598345936b48aec61901642d.png" alt="\cdot \cdot - - \cdot \cdot"></td>
</tr>
<tr>
<td>-</td>
<td><img class="tex" src="Morsecode-Dateien/981034dff6d21c0bb6ff2622f5fdb004.png" alt="- \cdot \cdot \cdot \cdot -"></td>

</tr>
<tr>
<td>(</td>
<td><img class="tex" src="Morsecode-Dateien/312b127dc8b2b9b88aefd4a850137aef.png" alt="- \cdot - - \cdot"></td>
</tr>
<tr>
<td>)</td>
<td><img class="tex" src="Morsecode-Dateien/598a97d1d6c6fde690f445a4f0e36107.png" alt="- \cdot - - \cdot -"></td>
</tr>
<tr>
<td>'</td>
<td><img class="tex" src="Morsecode-Dateien/09c94896f2f35f26f4c3fcf4891813d2.png" alt="\cdot - - - - \cdot"></td>
</tr>
<tr>

<td>=</td>
<td><img class="tex" src="Morsecode-Dateien/b39c919f379f4f93e717e6eb6e88e9b5.png" alt="- \cdot \cdot \cdot -"></td>
</tr>
<tr>
<td>+</td>
<td><img class="tex" src="Morsecode-Dateien/b0ffcdf0dbd9244f0f26cb152a982fbd.png" alt="\cdot - \cdot - \cdot"></td>
</tr>
<tr>
<td>/</td>
<td><img class="tex" src="Morsecode-Dateien/9f01b0a4dfea38fecc88a5be052fc297.png" alt="- \cdot \cdot - \cdot"></td>
</tr>
<tr>
<td>@<i>(AC)</i></td>
<td><img class="tex" src="Morsecode-Dateien/f4edd62c29286e1c0fd8f8c6fa4c2fcf.png" alt="\cdot - - \cdot - \cdot"></td>
</tr>
</tbody></table>
</th>
<td></td>
<th valign="top">
<table class="hintergrundfarbe1 rahmenfarbe1" style="border-style: solid; border-width: 1px; margin: 1em 1em 1em 0pt; border-collapse: collapse; empty-cells: show;" border="2" cellpadding="4" cellspacing="0" rules="all">
<tbody><tr class="hintergrundfarbe6">
<td colspan="2">Signale</td>
</tr>
<tr class="hintergrundfarbe6">
<td><b>Zeichen</b></td>
<td><b>Code</b></td>
</tr>

<tr>
<td>KA<br><i>(Spruchanfang)</i></td>
<td><img class="tex" src="Morsecode-Dateien/70b0690da7fed2bbc3fb48177b2052f2.png" alt="- \cdot - \cdot -"></td>
</tr>
<tr>
<td>BT<br><i>(Pause)</i></td>
<td><img class="tex" src="Morsecode-Dateien/b39c919f379f4f93e717e6eb6e88e9b5.png" alt="- \cdot \cdot \cdot -"></td>
</tr>
<tr>
<td>AR<br><i>(Spruchende)</i></td>
<td><img class="tex" src="Morsecode-Dateien/b0ffcdf0dbd9244f0f26cb152a982fbd.png" alt="\cdot - \cdot - \cdot"></td>
</tr>
<tr>
<td>VE<br><i>(verstanden)</i></td>
<td><img class="tex" src="Morsecode-Dateien/3f3c08ce7b5060790f75514ced928813.png" alt="\cdot \cdot \cdot - \cdot"></td>
</tr>
<tr>
<td>SK<br><i>(Verkehrsende)</i></td>
<td><img class="tex" src="Morsecode-Dateien/6689850d31f1877ed48f25e4f7552292.png" alt="\cdot \cdot \cdot - \cdot -"></td>

</tr>
<tr>
<td>SOS<br><i>(internationaler<br>
Seenotruf)</i></td>
<td><img class="tex" src="Morsecode-Dateien/9ab83d984f7ebb12a86783ae8ae985ad.png" alt="\cdot \cdot \cdot - - - \cdot \cdot \cdot"></td>
</tr>
<tr>
<td>IRRUNG<br><i>(Wiederholung<br>ab letztem<br>richtigem Wort)</i></td>
<td><img class="tex" src="Morsecode-Dateien/4d39e9355da1ed0d032c175eaafc0a64.png" alt="\cdot \cdot \cdot \cdot \cdot \cdot \cdot \cdot"></td>
</tr>
</tbody></table>
</html>
