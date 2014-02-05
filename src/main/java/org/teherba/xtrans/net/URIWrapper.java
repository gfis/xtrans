/*  Wrapper around java.net.URI which parses more schemes
    @(#) $Id: URIWrapper.java 305 2009-12-10 07:24:20Z gfis $
    2009-12-09: pctEncode (replaces ' ' with "%20" instead of '+')
    2008-02-13: Java 1.5 types
    2006-11-24, Dr. Georg Fischer
*/
/*
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
 */
package org.teherba.xtrans.net;
import  java.io.UnsupportedEncodingException;
import  java.net.URI;
import  java.net.URISyntaxException;
import  java.net.URLDecoder;
import  java.net.URLEncoder;
import  java.util.ArrayList;
import  java.util.HashMap;
import  java.util.Iterator;
import  java.util.regex.Matcher;
import  java.util.regex.Pattern;
import  org.apache.log4j.Logger;

/**	Wrapper around java.net.URI which parses more (IANA registered) schemes.
 *  Unlike java.net.URI, this class can be subclassed and has public getters and setters
 *  for all properties of the URI. 
 *  The <em>query</em> is divided into the indivdual key=value pairs for 
 *  the parameters which are stored in a hashmap.
 *  <em>authority</em> and <em>schemeSpecificPart</em> are also not stored
 *  directly, but in their component properties.
 *  <p />
 *  The class currently implements the schemes marked with 
 *  "s" (directly via java.net.URI) or "w" (by decorating/wrapping java.net.URI) 
 *  in the following list:
<pre>
  URI Scheme       Description                                                Reference
  
  acap             application configuration access protocol                  [RFC2244]
  cid              content identifier                                         [RFC2392]
  crid             TV-Anytime Content Reference Identifier                    [RFC4078]
w data             data                                                       [RFC2397]
  dav              dav                                                        [RFC2518]
  dict             dictionary service protocol                                [RFC2229]
  dns              Domain Name System                                         [RFC4501]
w fax              fax                                                        [RFC3966]
s file             Host-specific file names                                   [RFC1738]
s ftp              File Transfer Protocol                                     [RFC1738]
w go               go                                                         [RFC3368]
s gopher           The Gopher Protocol                                        [RFC4266]
  h323             H.323                                                      [RFC3508]
s http             Hypertext Transfer Protocol                                [RFC2616]
s https            Hypertext Transfer Protocol Secure                         [RFC2818]
  im               Instant Messaging                                          [RFC3860]
  imap             internet message access protocol                           [RFC2192]
  info             Information Assets with Identifiers in Public Namespaces   [RFC4452]
  ipp              Internet Printing Protocol                                 [RFC3510]
  iris.beep        iris.beep                                                  [RFC3983]
  ldap             Lightweight Directory Access Protocol                      [RFC4516]
w mailto           Electronic mail address                                    [RFC2368]
  mid              message identifier                                         [RFC2392]
w modem            modem                                                      [RFC3966]
  mtqp             Message Tracking Query Protocol                            [RFC3887]
  mupdate          Mailbox Update (MUPDATE) Protocol                          [RFC3656]
  news             USENET news                                                [RFC1738]
  nfs              network file system protocol                               [RFC2224]
  nntp             USENET news using NNTP access                              [RFC1738]
  opaquelocktoken  opaquelocktokent                                           [RFC2518]
  pop              Post Office Protocol v3                                    [RFC2384]
  pres             Presence                                                   [RFC3859]
  rtsp             real time streaming protocol                               [RFC2326]
  service          service location                                           [RFC2609]
  sip              session initiation protocol                                [RFC3261]
  sips             secure session initiation protocol                         [RFC3261]
  snmp             Simple Network Management Protocol                         [RFC4088]
  soap.beep        soap.beep                                                  [RFC3288]
  soap.beeps       soap.beeps                                                 [RFC3288]
  tag              tag                                                        [RFC4151]
w tel              telephone                                                  [RFC3966]
  telnet           Reference to interactive sessions                          [RFC4248]
  tftp             Trivial File Transfer Protocol                             [RFC3617]
  tip              Transaction Internet Protocol                              [RFC2371]
w urn              Uniform Resource Names (click for registry)                [RFC2141]
  vemmi            versatile multimedia interface                             [RFC2122]
  xmlrpc.beep      xmlrpc.beep                                                [RFC3529]
  xmlrpc.beeps     xmlrpc.beeps                                               [RFC3529]
  xmpp             Extensible Messaging and Presence Protocol                 [RFC4622]
  z39.50r          Z39.50 Retrieval                                           [RFC2056]
  z39.50s          Z39.50 Session                                             [RFC2056]
  
  Provisional URI Schemes
  afs              Andrew File System global file names                       [RFC1738]
  dtn              DTNRG research and development                             [draft-irtf-dtnrg-arch]
                                                                              [RFC3987]
                                                                              [http://www.dtnrg.org/wiki/Code]
                                                                              [draft-irtf-dtnrg-bundle-security]
                                                                              [draft-irtf-dtnrg-sec-overview]
  mailserver       Access to data available from mail servers                 [RFC1738]
  tn3270           Interactive 3270 emulation sessions                        [RFC1738]
  
  Historical URI Schemes
  prospero         Prospero Directory Service                                 [RFC4157]
  wais             Wide Area Information Servers                              [RFC4156]
</pre>
 *  @author Dr. Georg Fischer
 */
public class URIWrapper { 
    public final static String CVSID = "@(#) $Id: URIWrapper.java 305 2009-12-10 07:24:20Z gfis $";

    /** log4j logger (category) */
    private Logger log;
    
    /** Pattern for the initial scheme and ":" */
    private static final Pattern SCHEME_PATTERN = Pattern.compile("([\\w\\.]+)\\:");

    /** Standard encoding */
    private static final String UTF8 = "UTF-8";
    
    /** No-args Constructor.
     */
    public URIWrapper() {
        log = Logger.getLogger(URIWrapper.class.getName());
	} // constructor()
	
    /** Constructor from string.
     *  @param str - The string to be parsed into a URI.
     *  @throws URISyntaxException
     */
    public URIWrapper(String str) throws URISyntaxException {
    	this();
        String ssp = "";
        Matcher schemeMatcher = SCHEME_PATTERN.matcher(str);
        if (schemeMatcher.lookingAt()) {
            scheme = schemeMatcher.group(1).toLowerCase();  
            ssp = str.substring(scheme.length() + 1); // scheme specific part
        } else { // relative path
            scheme = null;
            ssp = str;
        }
        // System.out.println("URIWrapper(" + str + "):\nscheme=\"" + scheme + "\", ssp=\"" + ssp + "\"");
        try {
            uri = new URI(str);
            setURI(uri);
        } catch (URISyntaxException exc) {
            log.warn("constructor: " + exc.getMessage());
            setScheme(scheme);
            setSchemeSpecificPart(ssp);
        }
        if (scheme == null                   )  { parseGeneric  (ssp); setScheme("relative");
        } else if (scheme.equals("data"     ))  { parseData     (ssp);
        } else if (scheme.equals("fax"      ))  { parseTel      (ssp);
        } else if (scheme.equals("file"     ))  { parseGeneric  (ssp);
        } else if (scheme.equals("ftp"      ))  { parseGeneric  (ssp);
        } else if (scheme.equals("go"       ))  { parseGo       (ssp);
        } else if (scheme.equals("gopher"   ))  { parseGeneric  (ssp);
        } else if (scheme.equals("http"     ))  { parseGeneric  (ssp);
        } else if (scheme.equals("https"    ))  { parseGeneric  (ssp);
    //  } else if (scheme.equals("jndi"     ))  { parseURN      (ssp);
        } else if (scheme.equals("mailto"   ))  { parseMailto   (ssp);
        } else if (scheme.equals("modem"    ))  { parseTel      (ssp);
        } else if (scheme.equals("rmi"      ))  { parseURN      (ssp);
        } else if (scheme.equals("tag"      ))  { parseTag      (ssp);
        } else if (scheme.equals("tel"      ))  { parseTel      (ssp);
        } else if (scheme.equals("urn"      ))  { parseURN      (ssp);
        } else {            
            log.warn("URIWrapper: unknown scheme \"" + scheme + "\"");
        }
        // log.debug("URIWrapper - end");
    } // Constructor(String)
    
    /** Decodes a string with embedded sequences of "%" followed by 2 hex digits    
     *  into the specified character set. 
     *	The method is the opposite of {@link #pctEncode} and is similiar
     *  to URLDecoder.decode, but it does not convert "+" signs to spaces.
     *	This is sometimes better for "data" URIs.
	 *  The modified specification from java.net.URLEncoder follows:
	 *	When encoding a String, the following rules apply:
	 *	<ul>
     *	<li>The alphanumeric characters "a" through "z", "A" through "Z" and "0" through "9" remain the same.</li>
	 *  <li>The special characters ".", "-", "*", and "_" remain the same.</li>
     *	<li>The space character " " is converted into "%20".</li>
     * 	<li>All other characters are unsafe and are first converted into one or more bytes using some encoding scheme.
     *	Then each byte is represented by the 3-character string "%xy", 
     *	where xy is the two-digit hexadecimal representation of the byte.
     *	The recommended encoding scheme to use is UTF-8. However, for compatibility reasons, 
     *	if an encoding is not specified, then the default encoding of the platform is used. 
     *	</li>
     *	</ul>
	 *	For example using UTF-8 as the encoding scheme the string "The string ü@foo-bar" 
	 *	would get converted to "The%20string%20%C3%BC%40foo-bar" because in UTF-8 the 
	 *	character ü is encoded as two bytes C3 (hex) and BC (hex), 
	 *	and the character @ is encoded as one byte 40 (hex). 
     *  @param str string to be decoded
     *  @param charset character set to be used, normally "UTF-8"
     *  @return string with decoded characters
     */
    public String pctDecode(String str, String charset) {
        String result = "";
        byte byteBuffer[] = new byte[str.length()]; // will never be longer than 'str'
        int bytePos = 0;
        int strPos = 0;
        while (strPos < str.length()) {
            char ch = str.charAt(strPos ++);
            if (ch == '%') { // escaped
                int code = 0;
                if (strPos + 2 <= str.length()) {
                    try {
                        code = Integer.parseInt(str.substring(strPos, strPos + 2), 16);
                    } catch (Exception exc) {
                        log.error("invalid =xx escape sequence in " + str);
                    }
                    strPos += 2;
                } else {
                    log.error("incomplete =xx escape sequence, strPos=" + strPos + "\n" + str);
                }
                byteBuffer[bytePos ++] = (byte) code;
            } else { // not excaped
                byteBuffer[bytePos ++] = (byte) ch;
            }
        } // while strPos
        try {
            result = new String(byteBuffer, 0, bytePos, charset);
        } catch (Exception exc) {
            log.error("invalid encoding " + charset + ": " + exc.getMessage());
        }
        return result;
    } // pctDecode

    /** Encodes a string by replacing any non-URI character by embedded sequences of "%" followed by 2 hex digits.
     *  The method is the opposite of {@link #pctDecode} similiar
     *  to URLDecoder.encode, but it does not convert spaces to "+" signs.
     *	This is sometimes better for "data" URIs.
     *  @param str string to be decoded
     *  @param charset character set to be used, normally "UTF-8"
     *  @return string with decoded characters
     */
    public String pctEncode(String str, String charset) {
        StringBuffer result = new StringBuffer(str.length() * 2);
        try {
	        byte byteBuffer[] = str.getBytes(charset); // will never be longer than 'str'
    	    int bytePos = 0;
        	int strPos = 0;
	        while (bytePos < byteBuffer.length) {
    	        char ch = (char) (byteBuffer[bytePos ++] & 0xff);
        	    if (false) {
            	} else if (ch < 128 && Character.isLetterOrDigit(ch)) {
            		result.append(ch);
	            } else if (".-*_".indexOf(ch) >= 0) {
    	        	result.append(ch);
        	    } else {
            		result.append('%');
            		String escape = String.valueOf(Integer.toHexString(ch));
	            	if (escape.length() < 2) {
    	        		escape = "0" + escape;
        	    	}
            		result.append(escape);
	            }
    	    } // while bytePos
        } catch (Exception exc) {
            log.error("invalid encoding " + charset + ": " + exc.getMessage());
        }
        return result.toString();
    } // pctEncode

    //-----------------------------------------------
    /** Parses the "data" URI scheme described in RFC 2397.
     *  The syntax is
     *  <pre>
       dataurl    := "data:" [ mediatype ] [ ";base64" ] "," data
       mediatype  := [ type "/" subtype ] *( ";" parameter )
       data       := *urlchar
       parameter  := attribute "=" value
     *  </pre>
     *  mediatype defaults to "text/plain;charset=US-ASCII", 
     *  where "text/plain" can be omitted. ";base64" has no
     *  following "=" sign.
     *  @param ssp scheme specific part
     */
    protected void parseData(String ssp) {
        // log.debug("parseData: " + ssp);
        try {
            int commaPos = ssp.indexOf(','); // data start behind
            if (commaPos >= 0) { // with data
            } else { // degenerate: parameters only
                commaPos = ssp.length();
            } 
            params = new HashMap/*<1.5*/<String, ArrayList<String>>/*1.5>*/(32);
            if (commaPos > 0) {
                String[] parameters = ssp.substring(0, commaPos).split(";");
                int iparm = 0;
                while (iparm < parameters.length) {
                    String pair = parameters[iparm ++];
                    int equalPos = pair.indexOf("=");
                    String key   = "";
                    String value = "true";
                    if (equalPos < 0) { // key only, no value
                        key = pair;
                        if (iparm == 1) {
                            key   = "mediatype";
                            value = pair;
                        }
                    } else {
                        key   = pair.substring(0, equalPos);
                        value = pair.substring(equalPos + 1);
                    }
                    addParam(key, pctDecode(value, UTF8));
                } // while iparm
            }
            String mediaType = "text/plain";
            ArrayList/*<1.5*/<String>/*1.5>*/ al = getParam("mediatype");
            if (al != null) {
                mediaType = (String) al.get(0);
            }
            String charSet = "US-ASCII";
            if (mediaType.startsWith("text/")) {
                al = getParam("charset");
                if (al != null) {
                    charSet = ((String) al.get(0)).toUpperCase();
                }
            }
            if (commaPos < ssp.length()) { // evaluate data part
                String data = ssp.substring(commaPos + 1);
                if (getParam("base64") != null) {
                    addParam("body", data);
                } else {
                    addParam("body", pctDecode(data, charSet));
                }
            } // with data part
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // parseData
    
    /** Parses the following URL schemes:
     *  <ul>
     *  <li>ftp</li>
     *  <li>file</li>
     *  <li>gopher</li>
     *  <li>http, https</li>
     *  </ul>
     *  The syntax is
     *  <pre>
     *  </pre>
     *  @param ssp scheme specific part
     */
    protected void parseGeneric(String ssp) {
        try {
            uri = new URI(((scheme != null) ? scheme + ":" : "") + ssp);
            setURI(uri);
        } catch (URISyntaxException exc) {
            log.debug("parseGeneric - warning, scheme=\"" + scheme + "\": " + exc.getMessage());
            setScheme(scheme);
            setSchemeSpecificPart(ssp);
        }
    } // parseGeneric
    
    /** Parses the "go" URI scheme described in RFC 3368.
     *  The syntax is
     *  <pre>
   		cnrp-uri      = "go:" (form1 / form2)
        form1         = "//" [server] ["?" ((common-name *avpair) / id-req) ]
        form2         = common-name *avpair
        id-req        = "id=" value
        avpair        = ";" attribute "=" [ type "," ] value
        server        = // as specified in RFC2396
        common-name     = *(unreserved | escaped)
        attribute       = *(unreserved | escaped)
        value           = *(unreserved | escaped)
        type            = *(unreserved | escaped)
        unreserved      = // as specified in RFC2396
        escaped       = "%" hex hex
     *  </pre>
     *  @param ssp scheme specific part
     */
    protected void parseGo(String ssp) {
        log.debug("parseGo 1: " + ssp);
        try {
            int qmPos = ssp.indexOf('?'); // position of the question mark, query starts behind
            if (qmPos >= 0) { // form 1
                if (ssp.startsWith("//")) {
                    String server = ssp.substring(0, qmPos);
                    if (server.equals("//")) {
                        server = "//localhost";
                    }
                    uri = new URI("go:" + server);
                    userInfo    = uri.getUserInfo();
                    host        = uri.getHost();
                    port        = uri.getPort();
                } else {
                    log.error("go: URI with \"?\" and no leading \"//\"");
                }
                query = ssp.substring(qmPos + 1);
            } else { // form 2
                query = ssp;
            } 
            int semiPos = query.indexOf(";");
            if (semiPos < 0) {
                semiPos = query.length();
            }
            String commonName = query.substring(0, semiPos);
            addParam("common-name", commonName);
            if (semiPos >= 0 && semiPos < query.length()) {
                query = query.substring(semiPos + 1);
                splitParams(query, ";");
            }
        } catch (URISyntaxException exc) {
            log.error("parseGo - error in ssp=\"" + ssp + "\": " + exc.getMessage());
        }
    } // parseGo
    
    /** Parses the "mailto"  
     *  URI scheme described in RFC 2368.
     *  The syntax is
     *  <pre>
     mailtoURL  =  "mailto:" [ to ] [ headers ]
     to         =  #mailbox 
     headers    =  "?" header *( "&amp;" header )
     header     =  hname "=" hvalue
     hname      =  *urlc
     hvalue     =  *urlc
     *  </pre>
     * "mailbox" consists of zero or more comma separated mail addresses, 
     *  possibly with "phrase" and "comment" components
     *  @param ssp scheme specific part
     */
    protected void parseMailto(String ssp) {
        // log.debug("parseMailto: " + ssp);
        try {
            int qmPos = ssp.indexOf('?'); // position of the question mark, headers start behind
            if (qmPos >= 0) { // with headers
            } else { // list of addresses only, no headers
                qmPos = ssp.length();
            } 
            params = new HashMap/*<1.5*/<String, ArrayList<String>>/*1.5>*/(32);
            if (qmPos > 0) {
                String[] addresses = ssp.substring(0, qmPos).split(",");
                int iaddr = 0;
                while (iaddr < addresses.length) {
                    addParam("to", pctDecode(addresses[iaddr ++], UTF8));
                } // while iaddr
            }
            if (qmPos < ssp.length()) { // evaluate headers
                String headers = ssp.substring(qmPos + 1);
                splitParams(headers);
            } // with headers
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // parseMailto
    
    /** Parses the "tag"  
     *  URI schemes described in RFC 4151 and 
     *  www.taguri.org.
     *  <pre>
	    The general syntax of a tag URI, in ABNF [2], is:
	 
	       tagURI = "tag:" taggingEntity ":" specific [ "#" fragment ]
	 
	    Where:
	 
	       taggingEntity = authorityName "," date
	       authorityName = DNSname / emailAddress
	       date = year ["-" month ["-" day]]
	       year = 4DIGIT
	       month = 2DIGIT
	       day = 2DIGIT
	       DNSname = DNScomp *( "."  DNScomp ) ; see RFC 1035 [3]
	       DNScomp = alphaNum [*(alphaNum /"-") alphaNum]
	       emailAddress = 1*(alphaNum /"-"/"."/"_") "@" DNSname
	       alphaNum = DIGIT / ALPHA
	       specific = *( pchar / "/" / "?" ) ; pchar from RFC 3986 [1]
	       fragment = *( pchar / "/" / "?" ) ; same as RFC 3986 [1]
     *  </pre>
     *  @param ssp scheme specific part
     */
    protected void parseTag(String ssp) {
        parseGeneric(ssp);
    } // parseTag
    
    /** Parses the "tel" 
     *  URI scheme described in RFC 3966. The obsolete "fax" and "modem"
     *  URI schemes from RFC 2806 are also mapped to this method.
     *  The syntax is
     *  <pre>
	    telephone-uri        = "tel:" telephone-subscriber
	    telephone-subscriber = global-number / local-number
	    global-number        = global-number-digits *par
	    local-number         = local-number-digits *par context *par
	    par                  = parameter / extension / isdn-subaddress
	    isdn-subaddress      = ";isub=" 1*uric
	    extension            = ";ext=" 1*phonedigit
	    context              = ";phone-context=" descriptor
	    descriptor           = domainname / global-number-digits
	    global-number-digits = "+" *phonedigit DIGIT *phonedigit
	    local-number-digits  =
	       *phonedigit-hex (HEXDIG / "*" / "#")*phonedigit-hex
	    domainname           = *( domainlabel "." ) toplabel [ "." ]
	    domainlabel          = alphanum
	                           / alphanum *( alphanum / "-" ) alphanum
	    toplabel             = ALPHA / ALPHA *( alphanum / "-" ) alphanum
	    parameter            = ";" pname ["=" pvalue ]
	    pname                = 1*( alphanum / "-" )
	    pvalue               = 1*paramchar
	    paramchar            = param-unreserved / unreserved / pct-encoded
	    unreserved           = alphanum / mark
	    mark                 = "-" / "_" / "." / "!" / "~" / "*" /
	                           "'" / "(" / ")"
	    pct-encoded          = "%" HEXDIG HEXDIG
	    param-unreserved     = "[" / "]" / "/" / ":" / "&" / "+" / "$"
	    phonedigit           = DIGIT / [ visual-separator ]
	    phonedigit-hex       = HEXDIG / "*" / "#" / [ visual-separator ]
	    visual-separator     = "-" / "." / "(" / ")"
	    alphanum             = ALPHA / DIGIT
	    reserved             = ";" / "/" / "?" / ":" / "@" / "&" /
	                           "=" / "+" / "$" / ","
	    uric                 = reserved / unreserved / pct-encoded
	 
	    Each parameter name ("pname"), the ISDN subaddress, the 'extension',
	    and the 'context' MUST NOT appear more than once.  The 'isdn-
	    subaddress' or 'extension' MUST appear first, if present, followed by
	    the 'context' parameter, if present, followed by any other parameters
	    in lexicographical order.
     *  </pre>
     *  @param ssp scheme specific part
     */
    protected void parseTel(String ssp) {
        try {
            String[] parameters = ssp.split(";"); 
            int iparm = 0;
            while (iparm < parameters.length) {
                String pair = parameters[iparm ++];
                int equalPos = pair.indexOf("=");
                String key   = "";
                String value = "true";
                if (equalPos < 0) { // key only, no value
                    key = pair;
                    if (iparm == 1) {
                        key   = "telno";
                        value = pair;
                    }
                } else {
                    key   = pair.substring(0, equalPos);
                    value = pair.substring(equalPos + 1);
                }
                addParam(key, pctDecode(value, UTF8)); // unshield
            } // while iparm
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // parseTel
    
    /** Parses the "urn"
     *  URI scheme described in RFC 2141.
     *  The syntax is
     *  <pre>
	    <URN> ::= "urn:" <NID> ":" <NSS>
	    <NID>         ::= <let-num> [ 1,31<let-num-hyp> ]
	    <let-num-hyp> ::= <upper> | <lower> | <number> | "-"
	    <let-num>     ::= <upper> | <lower> | <number>
	    <NSS>         ::= 1*<URN chars>
	    <URN chars>   ::= <trans> | "%" <hex> <hex>
	    <trans>       ::= <upper> | <lower> | <number> | <other> | <reserved>
	    <hex>         ::= <number> | "A" | "B" | "C" | "D" | "E" | "F" |
	                      "a" | "b" | "c" | "d" | "e" | "f"
	    <other>       ::= "(" | ")" | "+" | "," | "-" | "." |
	                      ":" | "=" | "@" | ";" | "$" |
	                      "_" | "!" | "*" | "'"
	    <reserved>    ::= '%" | "/" | "?" | "#"
	    <excluded> ::= octets 1-32 (1-20 hex) | "\" | """ | "&" | "<"
	                   | ">" | "[" | "]" | "^" | "`" | "{" | "|" | "}" | "~"
	                   | octets 127-255 (7F-FF hex)
     *  </pre>
     *  @param ssp scheme specific part
     */
    protected void parseURN(String ssp) {
        // log.debug("parseURN: " + ssp);
        try {
            params = new HashMap/*<1.5*/<String, ArrayList<String>>/*1.5>*/(32);
            int colonPos = ssp.indexOf(':'); // data start behind
            if (colonPos >= 0) { // with data
                setParam("nid", ssp.substring(0, colonPos).toLowerCase());
                setParam("nss", ssp.substring(colonPos + 1)); // no "%" decoding without further ns specific parsing
            } else { // degenerate: parameters only
                colonPos = ssp.length();
                setParam("nid", ssp.substring(0, colonPos).toLowerCase());
            } 
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // parseURN
    
    //-----------------------------------------------
    /** URI - the generalized URI built from the properties of this class */
    private URI uri;
    
    /** Sets the URI
     *  @param uri universal resource identifier (URL or URN)
     */
    public void setURI(URI uri) {
        this.uri    = uri;
        scheme      = uri.getScheme();
        if (scheme != null) {
            scheme  = scheme.toLowerCase();
        }
        userInfo    = uri.getUserInfo();
        host        = uri.getHost();
        port        = uri.getPort();
        path        = uri.getPath();
        query       = uri.getQuery();
        params      = new HashMap/*<1.5*/<String, ArrayList<String>>/*1.5>*/(32);
        if (query != null) {
            splitParams(query);
        }
        fragment    = uri.getFragment();
    } // setURI
    
    /** Gets the URI
     *  @return uri universal resource identifier (URL or URN)
     */
    public URI getURI() {
        return uri;
    } // getURI
    //-----------------------------------------------
    /** Returns the content of this URI as a ("URL encoded") US-ASCII string.
     *  @return the content of this URI as a US-ASCII string.
     */
    public String toASCIIString() {
        return uri.toASCIIString();
    } // toASCIIString

    /** Returns the content of this URI as a string (with no "URL encoding").
     *  @return the content of this URI as a string.
     */
    public String toString() {
        return uri.toString();
    } // toString
    //-----------------------------------------------
    /** scheme - scheme */
    private String scheme;
    
    /** Sets the scheme
     *  @param scheme "http:", "tel:" etc.
     */
    public void setScheme(String scheme) {
        this.scheme = scheme;
        try {
            uri = new URI(scheme, userInfo, host, port, path, query, fragment);
        } catch (URISyntaxException exc) {
            log.error(exc.getMessage(), exc);
        }
    } // setScheme
    
    /** Gets the scheme
     *  @return "http:", "tel:" etc. 
     */
    public String getScheme() {
        return scheme;
    } // getScheme
    //-----------------------------------------------
    /** scheme specific part */
    private String schemeSpecificPart;
    
    /** Sets the scheme specific part
     *  @param schemeSpecificPart scheme specific part
     */
    public void setSchemeSpecificPart(String schemeSpecificPart) {
        this.schemeSpecificPart = schemeSpecificPart;
        try {
            uri = new URI(scheme, schemeSpecificPart, fragment);
            setURI(uri);
        } catch (URISyntaxException exc) {
            log.error(exc.getMessage(), exc);
        }
    } // setSchemaSpecificPart
    /** Gets the scheme specific part
     *  @return scheme specific part
     */
    public String getSchemeSpecificPart() {
        return uri.getSchemeSpecificPart();
    } // getSchemaSpecificPart
    //-----------------------------------------------
    /** authority */
    private String authority;
    /** Sets the authority
     *  @param authority authority
     */
    public void setAuthority(String authority) {
        this.authority = authority;
        try {
            uri = new URI(scheme, authority, path, query, fragment);
            setURI(uri);
        } catch (URISyntaxException exc) {
            log.error(exc.getMessage(), exc);
        }
    } // setAuthority
    /** Gets the authority
     *  @return authority
     */
    public String getAuthority() {
        return uri.getAuthority();
    } // getAuthority
    //-----------------------------------------------
    /** user information (name, password) */
    private String userInfo;
    /** Sets user information (name, password) 
     *  @param userInfo user information (name, password) 
     */
    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
        try {
            uri = new URI(scheme, userInfo, host, port, path, query, fragment);
        } catch (URISyntaxException exc) {
            log.error(exc.getMessage(), exc);
        }
    } // setUserInfo
    /** Gets the user information (name, password) 
     *  @return user information (name, password) 
     */
    public String getUserInfo() {
        return uri.getUserInfo();
    } // getUserInfo
    //-----------------------------------------------
    /** host - DNS name or IP address */
    private String host;
    /** Sets the host - DNS name or IP address
     *  @param host DNS name or IP address
     */
    public void setHost(String host) {
        this.host = host;
        try {
            uri = new URI(scheme, userInfo, host, port, path, query, fragment);
        } catch (URISyntaxException exc) {
            log.error(exc.getMessage(), exc);
        }
    } // setHost
    /** Gets the host - DNS name or IP address
     *  @return DNS name or IP address
     */
    public String getHost() {
        return uri.getHost();
    } // getHost
    //-----------------------------------------------
    /** port number */
    private int port;
    /** Sets the port number
     *  @param port port number
     */
    public void setPort(int port) {
        this.port = port;
        try {
            uri = new URI(scheme, userInfo, host, port, path, query, fragment);
        } catch (URISyntaxException exc) {
            log.error(exc.getMessage(), exc);
        }
    } // setPort
    /** Gets the port number
     *  @return port number
     */
    public int getPort() {
        return uri.getPort();
    } // getPort
    //-----------------------------------------------
    /** path - path and filename */
    private String path;
    /** Sets the path - path and filename
     *  @param path path and filename
     */
    public void setPath(String path) {
        this.path = path;
        try {
            uri = new URI(scheme, userInfo, host, port, path, query, fragment);
        } catch (URISyntaxException exc) {
            log.error(exc.getMessage(), exc);
        }
    } // setPath
    /** Gets the path - path and filename
     *  @return path and filename
     */
    public String getPath() {
        return uri.getPath();
    } // getPath
    //-----------------------------------------------
    /** query - key=value pairs for parameters */
    private String query;
    
    /** Sets the query to a list of "URL encoded" key=value pairs for parameters
     *  @param query key=value pairs for parameters
     */
    public void setQuery(String query) {
        this.query = query;
        params = new HashMap/*<1.5*/<String, ArrayList<String>>/*1.5>*/(32);
        splitParams(query);
        try {
            uri = new URI(scheme, userInfo, host, port, path, query, fragment);
        } catch (URISyntaxException exc) {
            log.error(exc.getMessage(), exc);
        }
    } // setQuery
    
    /** Gets the query as a list of key=value pairs for parameters
     *  @return key=value pairs for parameters
     */
    public String getQuery() {
        query = joinParams();
        return query;
    } // getQuery
    //-----------------------------------------------
    /** params - parameter map of pairs (String key, ArrayList&lt;String&gt; value) */
    private HashMap/*<1.5*/<String, ArrayList<String>>/*1.5>*/ params;

    /** Sets the parameter map
     *  @param params parameter map which maps each key to one or more values
     */
    public void setParams(HashMap/*<1.5*/<String, ArrayList<String>>/*1.5>*/ params) {
        this.params = params;
        query = joinParams();
    } // setParams

    /** Gets the parameter map
     *  @return parameter map
     */
    public HashMap/*<1.5*/<String, ArrayList<String>>/*1.5>*/ getParams() {
        return params;
    } // getParams

    /** Sets the parameter to a single value 
     *  @param key parameter name
     *  @param value parameter value
     */
    public void setParam(String key, String value) {
        ArrayList/*<1.5*/<String>/*1.5>*/ al = new ArrayList/*<1.5*/<String>/*1.5>*/(8);
        al.add(value);
        params.put(key, al);
        query = joinParams();
    } // setPaaram

    /** Adds another value to a key
     *  @param key   parameter name
     *  @param value parameter value
     */
    public void addParam(String key, String value) {
        ArrayList/*<1.5*/<String>/*1.5>*/ al = params.get(key);
        if (al == null) { // new key
            al = new ArrayList/*<1.5*/<String>/*1.5>*/(8);
        }
        al.add(value);
        params.put(key, al);
        query = joinParams();
    } // addParam
 
    /** Gets a parameter's value(s)
     *  @param key parameter name
     *  @return parameter value(s)
     */
    public ArrayList/*<1.5*/<String>/*1.5>*/ getParam(String key) {
        return params.get(key);
    } // getParam
    
    /** Extracts the individual key=value pairs from a string,
     *  and returns a hashmap for them. The values are array lists,
     *  since keys may occur several times.
     *  @param str string to be splitted into key=value pairs
     *  @param separ separator for key=value pairs
     */
    public void splitParams(String str, String separ) {
        try {
            String pairs[] = str.split("\\" + separ); // discard trailing empty strings
            int ipair = 0;
            while (ipair < pairs.length) {
                String pair = pairs[ipair ++];
                if (pair.length() > 0) { // ignore empty pair resulting from a leading "&"
                    String parts[] = pair.split("=", 2);
                    if (parts.length > 1) {
                        addParam  ( URLDecoder.decode(parts[0], UTF8)
                                  , URLDecoder.decode(parts[1], UTF8));
                    } else {
                        addParam  ( URLDecoder.decode(pair    , UTF8)
                                  , ""                                  );
                    }
                }
            } // while
        } catch (UnsupportedEncodingException exc) {
            log.error(exc.getMessage(), exc);
        }
    } // splitParams(String, String)
    
    /** Extracts the individual key=value pairs from a string,
     *  and returns a hashmap for them. The values are array lists,
     *  since keys may occur several times. 
     *  The default URL parameter separator "&" is used.
     *  @param str string to be splitted into key=value pairs
     */
    public void splitParams(String str) {
        splitParams(str, "&");
    } // splitParams(String)
    
    /** Concatenates the individual key=value pairs from the hashmap 
     *  and returns a "URL encoded" string.
     *  @param separ separator for key=value pairs
     *  @return "URL encoded" list of key=value pairs for parameters
     */
    public String joinParams(String separ) {
        StringBuffer result = new StringBuffer(128);
        try {
            Iterator iter = params.keySet().iterator();
            boolean first = true;
            while (iter.hasNext()) {
                String key = (String) iter.next();
                ArrayList/*<1.5*/<String>/*1.5>*/ al = params.get(key);
                if (al != null) {
                    int ial = 0;
                    while (ial < al.size()) {
                        String value = (String) al.get(ial ++);
                        if (! first) {
                            result.append(separ);
                        }
                        first = false;
                        result.append(URLEncoder.encode(key  , UTF8));
                        result.append("=");
                        result.append(URLEncoder.encode(value, UTF8));
                    } // while ial
                } // parameter was stored
            } // while all parameters
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return result.toString();
    } // joinParams(String)
    
    /** Concatenates the individual key=value pairs from the hashmap 
     *  and returns a "URL encoded" string.
     *  The default URL parameter separator "&" is used.
     *  @return "URL encoded" list of key=value pairs for parameters
     */
    public String joinParams() {
        return joinParams("&");
    } // joinParams()
    //-----------------------------------------------
    /** fragment - specific fragment in the file */
    private String fragment;
    /** Sets the fragment - specific fragment in the file
     *  @param fragment specific fragment in the file
     */
    public void setFragment(String fragment) {
        this.fragment = fragment;
        try {
            uri = new URI(scheme, userInfo, host, port, path, query, fragment);
        } catch (URISyntaxException exc) {
            log.error(exc.getMessage(), exc);
        }
    } // setFragment
    /** Gets the fragment - specific fragment in the file
     *  @return specific fragment in the file
     */
    public String getFragment() {
        return uri.getFragment();
    } // getFragment
    //-----------------------------------------------

    /** Test program with commandline interface for URI en-/decoding.
     *  @param args elements of the commandline separated by whitespace
     */
    public static void main(String args[]) {
        Logger log = Logger.getLogger(URIWrapper.class.getName());
		String word = "http://www.punctum.com:80/cgi-bin/test.cgi?parm1=val1&parm2=val2"; // default URI
		if (args.length >= 1) { 
	        word = args[0];
		}
        URIWrapper wrapper = null;
		try {
	        if (word.matches("\\w+\\:.*")) { // starts with a scheme - assume URI
		        wrapper = new URIWrapper(word);
		        System.out.println("input: " + word);
	        } else { // some string to be URI encoded
		        wrapper = new URIWrapper();
		        String uri  =  wrapper.pctEncode(word, "UTF-8");
		        System.out.println("encoded: " + uri );
		        word 		=  wrapper.pctDecode(uri , "UTF-8");
		        System.out.println("decoded: " + word);
		        wrapper = new URIWrapper(uri);
		    }
	        System.out.println("authority:\t"		+ wrapper.getAuthority	());
	        System.out.println("fragment:\t"		+ wrapper.getFragment	());
	        System.out.println("host:\t"			+ wrapper.getHost		());
	        System.out.println("path:\t"			+ wrapper.getPath		());
	        System.out.println("port:\t"			+ wrapper.getPort		());
	        System.out.println("query:\t"			+ wrapper.getQuery		());
	        System.out.println("scheme:\t"			+ wrapper.getScheme		());
	        System.out.println("specific part:\t"	+ wrapper.getSchemeSpecificPart());
	        System.out.println("user info:\t"		+ wrapper.getUserInfo	());
        } catch (URISyntaxException exc) {
   	        log.error(exc.getMessage(), exc);
       	}
    } // main

} // URIWrapper
