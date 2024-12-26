/*  Transforms Java Script Object Notation to/from XML.
    @(#) $Id: JSONTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2024-12-26: deprecations
    2016-10-16: </str> was missing
    2008-07-22: new version with nested nodes
    2007-10-19: tagStack in BaseTransformer
    2007-08-31, Dr. Georg Fischer: Narbonne-Plage
    Caution, this file is UTF-8 encoded: äöüÄÖÜß
*/
/*
 * Copyright 2006 Dr. Georg Fischer <dr dot georg dot fischer at gmail dot kom>
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

package org.teherba.xtrans.general;
import  org.teherba.xtrans.CharTransformer;
import  java.io.BufferedReader;
import  java.util.Stack;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transforms Java Script Object Notation to/from XML.
 *  JSON consists of objects, arrays, strings, numbers, true, false and null.
 *  Objects have one or more elements with a name and an object value,
 *  while arrays have unnamed objects only.
 *  JSON is mainly used in AJAX, since it is a lot more compact than XML.
 *  <p>
 *  Example:
 *  <pre>
{ "Kreditkarte"   : "Xema"
, "Nummer"        : "1234-5678-9012-3456"
, "Inhaber"       :
    { "Name"      : "Georg"
    , "Vorname"   : "Fischer"
    , "Geschlecht": "männlich"
    , "Vorlieben" :
        [ "Bücher\u00a0lesen"
        , { "Sammeln" :
          [ Insel-Bücher
          , Lochkarten
          , Briefmarken
          , Dateiformate
          ]
        , "Programmieren"
        ]
    , "Alter"     : null
    , "Kinder"    : 2
    , "Ruhestand" : false
    }
, "Deckung"       : 1e+4
, "Währung"       : "EUR"
}

json = object.
object = "{" (key ":" token) / "," "}"
key = string
token = string | number | "true" | "false" | "null" | object | array.
array = "[" token / "," "]".

</pre>
 *  @author Dr. Georg Fischer
 */
public class JSONTransformer extends CharTransformer {
    public final static String CVSID = "@(#) $Id: JSONTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** Non-args Constructor.
     */
    public JSONTransformer() {
        super();
        setFormatCodes("json");
        setDescription("Java Script Object Notation");
        setFileExtensions("json");
    } // constructor

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        log = LogManager.getLogger(JSONTransformer.class.getName());
    } // initialize

    /** Root element tag */
    private static final String ROOT_TAG    = "json";
    /** Object element tag */
    private static final String OBJECT_TAG  = "obj";
    /** Array element tag */
    private static final String ARRAY_TAG   = "arr";
    /** (Array) element element tag */
    private static final String ELEM_TAG    = "elem";
    /** Key element tag */
    private static final String KEY_TAG     = "key";
    /** (Object) node element tag */
    private static final String NODE_TAG    = "node";
    /** String element tag */
    private static final String STRING_TAG  = "str";
    /** Number element tag */
    private static final String NUMBER_TAG  = "num";
    /** Null element tag */
    private static final String NULL_TAG    = "null";
    /** Boolean false element tag */
    private static final String FALSE_TAG   = "false";
    /** Boolean True element tag */
    private static final String TRUE_TAG    = "true";
    /** Value element tag */
    private static final String VALUE_TAG   = "val";

    /** Buffer for string content, number content, true, false, null */
    private StringBuffer buffer;
    /** Buffer for Unicode escapes */
    private StringBuffer unicode;

    /** Flushes the buffer, writes any whitespace or token contained in it.
     */
    private void flush() {
        if (buffer.length() > 0) {
            fireCharacters(buffer.toString());
            buffer.setLength(0);
        }
    } // flush

    /** Stack for parser states */
    protected Stack/*<1.5*/<Integer>/*1.5>*/ parseStack;
    /** state of finite automaton */
    private int parseState;
    /* values for <em>parseState</em> */
    private static final int IN_ARRAY           =  0;
    private static final int IN_ARRAY_ELEM      =  1;
    private static final int IN_BACKSLASH       =  2;
    private static final int IN_NUMBER          =  4;
    private static final int IN_OBJECT          =  5;
    private static final int IN_OBJECT_KEY      =  6;
    private static final int IN_OBJECT_NODE     =  7;
    private static final int IN_SPACE           =  8;
    private static final int IN_START           =  9;
    private static final int IN_STRING          = 10;
    private static final int IN_TOKEN           = 11;
    private static final int IN_UNICODE         = 12;
    private static final int IN_OBJECT_VALUE    = 13;

    /** Pushes the current parse state, and enters a new one.
     *  @param newState new state to be entered
     */
    protected void push(int newState) {
        parseStack.push(Integer.valueOf(parseState));
        parseState = newState;
    } // push

    /** Switches to some state, and pushes another.
     *  @param newState new state to be entered
     *  @param otherState state to be returned by next <em>pop</em>
     */
    protected void push(int newState, int otherState) {
        parseStack.push(Integer.valueOf(otherState));
        parseState = newState;
    } // push

    /** Takes the parse state from the top of the parse stack.
     */
    protected void pop() {
        if (parseStack.size() <= 0) {
            log.error("parseStack underflow");
        } else {
            parseState = ((Integer) parseStack.pop()).intValue();
        }
    } // pop

    /** Logs an error message, or fires a corresponding comment
     *  @param text text of error message
     */
    protected void logError(String text) {
        if (false) {
            log.error(text);
        } else {
            fireComment("state " + parseState + ": " + text);
            fireLineBreak();
        }
    } // logError

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     *  The syntax is as follows ("/" = "separated by",
     *  whitespace may be interspersed everywhere):
     *  <pre>
     *  json    = object | array | string | number | token.
     *  object  = "{" node/"," "}".
     *  node    = key ":" value.
     *  key     = string.
     *  value   = object | array | string | number | token.
     *  array   = "[" element/"," "]".
     *  element = object | array | string | number | token
     *  token   = "true" | "false" | "null".
     *  </pre>
     */
    public boolean generate() {
        boolean result = true;
        try {
            parseStack = new Stack/*<1.5*/<Integer>/*1.5>*/();
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            BufferedReader buffReader = new BufferedReader(charReader);
            String line;
            parseState = IN_START;
            buffer = new StringBuffer(296);
            int unicode = 0;
            int escapeLen = 0; // length of unicode escape hex string
            boolean readOff = true;
            char ch; // current character read
            int chint = buffReader.read();

            while (chint >= 0) {
                ch = (char) chint;
                readOff = true;
                // System.out.println("State " + parseState + ", char " + ch);

                switch (parseState) {

                    case IN_SPACE:
                        switch (ch) {
                            case '\b':
                            case '\f':
                            case '\n':
                            case '\r':
                            case ' ':
                                buffer.append(ch);
                                break;
                            default:
                                flush();
                                readOff = false;
                                pop();
                                break;
                        } // switch ch
                        break; // IN_SPACE

                    case IN_START: // before "{", "[", string, number, token
                        switch (ch) {
                            case '\b':
                            case '\f':
                            case '\n':
                            case '\r':
                            case '\t':
                            case ' ':
                                buffer.append(ch); // whitespace
                                push(IN_SPACE);
                                break;
                            case '{':
                                pushXML(OBJECT_TAG);
                                push(IN_OBJECT);
                                break;
                            case '[':
                                pushXML(ARRAY_TAG);
                                push(IN_ARRAY);
                                break;
                            case ',':
                            case ':':
                            case ']':
                            case '}':
                                logError("\"" + ch + "\" found instead of string, number, token, object, array");
                                pop();
                                break;
                            case '"':
                                pushXML(STRING_TAG);
                                push(IN_STRING);
                                break;
                            case '+':
                            case '-':
                                buffer.append(ch);
                                pushXML(NUMBER_TAG);
                                push(IN_NUMBER);
                                break;
                            default: // should be start of "true", "false" or "null", or of a number
                                buffer.append(ch);
                                if (Character.isDigit(ch)) {
                                    pushXML(NUMBER_TAG);
                                    push(IN_NUMBER);
                                } else {
                                    push(IN_TOKEN);
                                }
                                break;
                        } // switch ch
                        break; // IN_START

                    case IN_OBJECT: // behind "{"
                        switch (ch) {
                            case '\b':
                            case '\f':
                            case '\n':
                            case '\r':
                            case '\t':
                            case ' ':
                                buffer.append(ch); // whitespace
                                push(IN_SPACE);
                                break;
                            case '}': // strange, empty object
                                popXML(OBJECT_TAG);
                                pop();
                                break;
                            case '"':
                                pushXML(NODE_TAG);
                                pushXML(KEY_TAG);
                                push(IN_STRING, IN_OBJECT_KEY);
                                break;
                            case '{':
                            case '[':
                            case ']':
                            case ',':
                            case ':':
                            case '+':
                            case '-':
                            default: // digit or letter
                                logError("\"" + ch + "\" found instead of key");
                                pop();
                                break;
                        } // switch ch
                        break; // IN_OBJECT

                    case IN_OBJECT_KEY: // behind "{ key"
                        switch (ch) {
                            case '\b':
                            case '\f':
                            case '\n':
                            case '\r':
                            case '\t':
                            case ' ':
                                buffer.append(ch); // whitespace
                                push(IN_SPACE);
                                break;
                            case ':':
                                popXML(KEY_TAG);
                                pop();
                                push(IN_OBJECT_VALUE);
                                break;
                            case ',': // strange, no corresponding empty value
                                popXML(KEY_TAG);
                                popXML(VALUE_TAG);
                                popXML(NODE_TAG);
                                pop();
                                push(IN_OBJECT_NODE);
                                break;
                            default:
                                logError("\"" + ch + "\" found instead of \":\"");
                                break;
                        } // switch ch
                        break; // IN_OBJECT_KEY

                    case IN_OBJECT_VALUE: // behind "{ key :"
                        switch (ch) {
                            case '\b':
                            case '\f':
                            case '\n':
                            case '\r':
                            case '\t':
                            case ' ':
                                buffer.append(ch); // whitespace
                                push(IN_SPACE);
                                break;
                            case '{':
                                pushXML(OBJECT_TAG);
                                push(IN_OBJECT);
                                break;
                            case '[':
                                pushXML(ARRAY_TAG);
                                push(IN_ARRAY);
                                break;
                            case '}': // strange: no corresponding empty value
                                popXML(VALUE_TAG);
                                popXML(NODE_TAG);
                                popXML(OBJECT_TAG); // OBJECT_TAG
                                pop();
                                break;
                            case ']':
                                logError("\"" + ch + "\" found instead of \"}\", \",\", value");
                                pop();
                                break;
                            case ',': // strange, no corresponding empty value
                                popXML(VALUE_TAG);
                                popXML(NODE_TAG);
                                pop();
                                push(IN_OBJECT);
                                break;
                            case '"':
                                pushXML(VALUE_TAG);
                                pushXML(STRING_TAG);
                                push(IN_STRING, IN_OBJECT_NODE);
                                break;
                            case '+':
                            case '-':
                                buffer.append(ch);
                                pushXML(VALUE_TAG);
                                pushXML(NUMBER_TAG);
                                push(IN_NUMBER, IN_OBJECT_NODE);
                                break;
                            default:
                                pushXML(VALUE_TAG);
                                buffer.append(ch);
                                if (Character.isDigit(ch)) {
                                    pushXML(NUMBER_TAG);
                                    push(IN_NUMBER, IN_OBJECT_NODE);
                                } else {
                                    push(IN_TOKEN, IN_OBJECT_NODE);
                                }
                                break;
                        } // switch ch
                        break; // IN_OBJECT_VALUE

                    case IN_OBJECT_NODE: // behind "{ key : value"
                        switch (ch) {
                            case '\b':
                            case '\f':
                            case '\n':
                            case '\r':
                            case '\t':
                            case ' ':
                                buffer.append(ch); // whitespace
                                push(IN_SPACE);
                                break;
                            case ',':
                                popXML(VALUE_TAG);
                                popXML(NODE_TAG);
                                pop();
                                push(IN_OBJECT);
                                break;
                            case '}':
                                popXML(VALUE_TAG);
                                popXML(NODE_TAG);
                                popXML(OBJECT_TAG);
                                pop();
                                break;
                            default:
                                logError("\"" + ch + "\" found instead of \":\"");
                                break;
                        } // switch ch
                        break; // IN_OBJECT_NODE

                    case IN_ARRAY: // behind "["
                        switch (ch) {
                            case '\b':
                            case '\f':
                            case '\n':
                            case '\r':
                            case '\t':
                            case ' ':
                                buffer.append(ch); // whitespace
                                push(IN_SPACE);
                                break;
                            case '{':
                                pushXML(ELEM_TAG);
                                pushXML(OBJECT_TAG);
                                push(IN_OBJECT);
                                break;
                            case '[':
                                pushXML(ELEM_TAG);
                                pushXML(ARRAY_TAG);
                                push(IN_ARRAY);
                                break;
                            case ',': // strange, empty element
                                popXML(ELEM_TAG);
                                pop();
                                push(IN_ARRAY);
                                break;
                            case ']': // strange, empty element
                                popXML(ELEM_TAG);
                                popXML(ARRAY_TAG);
                                pop();
                                break;
                            case ':':
                            case '}':
                                logError("\"" + ch + "\" found instead of string, number, token, object, array");
                                pop();
                                break;
                            case '"':
                                pushXML(ELEM_TAG);
                                pushXML(STRING_TAG);
                                push(IN_STRING, IN_ARRAY_ELEM);
                                break;
                            case '+':
                            case '-':
                                buffer.append(ch);
                                pushXML(ELEM_TAG);
                                pushXML(NUMBER_TAG);
                                push(IN_NUMBER, IN_ARRAY_ELEM);
                                break;
                            default:
                                buffer.append(ch);
                                pushXML(ELEM_TAG);
                                if (Character.isDigit(ch)) {
                                    pushXML(NUMBER_TAG);
                                    push(IN_NUMBER, IN_ARRAY_ELEM);
                                } else {
                                    push(IN_TOKEN, IN_ARRAY_ELEM);
                                }
                                break;
                        } // switch ch
                        break; // IN_ARRAY

                    case IN_ARRAY_ELEM: // behind "[ elem"
                        switch (ch) {
                            case '\b':
                            case '\f':
                            case '\n':
                            case '\r':
                            case '\t':
                            case ' ':
                                buffer.append(ch); // whitespace
                                push(IN_SPACE);
                                break;
                            case ',':
                                popXML(ELEM_TAG);
                                pop();
                                push(IN_ARRAY);
                                break;
                            case ']':
                                popXML(ELEM_TAG);
                                popXML(ARRAY_TAG);
                                pop();
                                break;
                            case ':':
                            case '}':
                            default:
                                logError("\"" + ch + "\" found instead of \",\" or \"]\"");
                                break;
                        } // switch ch
                        break; // IN_ARRAY_ELEM

                    case IN_STRING:
                        switch (ch) {
                            case '"':
                                flush();
                                pop();
                                // any of the following 2 will succeed
                                popXML(STRING_TAG);
                                popXML(KEY_TAG);
                                break;
                            case '\\':
                                parseState = IN_BACKSLASH;
                                break;
                            case '<':
                                if (getMimeType().equals("text/xml")) {
                                    buffer.append("&lt;");
                                } else {
                                    buffer.append(ch);
                                }
                                break;
                            case '>':
                                if (getMimeType().equals("text/xml")) {
                                    buffer.append("&gt;");
                                } else {
                                    buffer.append(ch);
                                }
                                break;
                            case '&':
                                if (getMimeType().equals("text/xml")) {
                                    buffer.append("&amp;");
                                } else {
                                    buffer.append(ch);
                                }
                                break;
                            case '\'':
                                if (getMimeType().equals("text/xml")) {
                                    buffer.append("&apos;");
                                } else {
                                    buffer.append(ch);
                                }
                                break;
                            default: // any string character
                                buffer.append(ch);
                                break;
                        } // switch ch
                        break; // IN_STRING

                    case IN_BACKSLASH:
                        parseState = IN_STRING;
                        switch (ch) {
                            case '"':
                                buffer.append(ch);
                                break;
                            case '\\':
                                buffer.append(ch);
                                break;
                            case '/':
                                buffer.append(ch);
                                break;
                            case 'b':
                                buffer.append('\b');
                                break;
                            case 'f':
                                buffer.append('\f');
                                break;
                            case 'n':
                                buffer.append('\n');
                                break;
                            case 'r':
                                buffer.append('\r');
                                break;
                            case 't':
                                buffer.append('\t');
                                break;
                            case 'u':
                                parseState = IN_UNICODE;
                                unicode = 0;
                                escapeLen = 0;
                                break;
                            default: // any other character is invalid
                                // take it literally as if it were not escaped
                                buffer.append(ch);
                                break;
                        } // switch ch
                        break; // IN_BACKSLASH

                    case IN_UNICODE:
                        escapeLen ++;
                        if (escapeLen > 4) {
                            parseState = IN_STRING;
                            readOff = false;
                            buffer.append((char) (unicode & 0xffff));
                        } else if (ch >= '0' && ch <= '9') {
                            unicode = unicode * 16 + (ch - '0');
                        } else if (ch >= 'A' && ch <= 'F') {
                            unicode = unicode * 16 + (ch - 'A' + 10);
                        } else if (ch >= 'a' && ch <= 'f') {
                            unicode = unicode * 16 + (ch - 'a' + 10);
                        } else {
                            parseState = IN_STRING;
                            readOff = false;
                            buffer.append((char) (unicode & 0xffff));
                        }
                        break; // IN_UNICODE

                    case IN_NUMBER:
                        switch (ch) {
                            case '+':
                            case '-':
                            case 'E':
                            case 'e':
                                buffer.append(ch);
                                break;
                            default:
                                if (Character.isDigit(ch)) {
                                    buffer.append(ch);
                                } else {
                                    flush();
                                    popXML(NUMBER_TAG);
                                    readOff = false;
                                    pop();
                                }
                                break;
                        } // switch ch
                        break; // IN_NUMBER

                    case IN_TOKEN:
                        if (Character.isLetter(ch)) {
                            buffer.append(ch);
                        } else {
                            String value = buffer.toString();
                            if (false) {
                            } else if (value.equals("true")) {
                                fireEmptyElement(TRUE_TAG);
                            } else if (value.equals("false")) {
                                fireEmptyElement(FALSE_TAG);
                            } else if (value.equals("null")) {
                                fireEmptyElement(NULL_TAG);
                            } else { // NUMBER
                                logError("unknown token " + value);
                                flush();
                            }
                            buffer.setLength(0);
                            readOff = false;
                            pop();
                        } // no letter
                        break; // IN_TOKEN

                    default:
                        logError("invalid state " + parseState);
                        break;

                } // switch parseState

                if (readOff) {
                    chint = buffReader.read();
                }
            } // while not EOF
            buffReader.close();

            while (tagStack.size() > 0) { // flush XML stack
                popXML();
            } // flush

            fireEndElement(ROOT_TAG);
            fireLineBreak();
            fireEndDocument();
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return  result;
    } // generate

    /*===========================*/
    /* SAX handler for XML input */
    /*===========================*/

    /** Stack for elements of the language: objects, keys, values, arrays ... */
    private Stack/*<1.5*/<String>/*1.5>*/ serStack;
    /** Buffer for values */
    private StringBuffer serBuffer;
    /** current topmost element */
    private String elem;
    /** separator for nodes    of an object: "{" or "," */
    private String serObjectSep;
    /** separator for elements of an array:  "[" or "," */
    private String serArraySep;

    /** Writes the value buffer with proper character escape sequences
     *  @param serBuffer String to be escaped and written
     */
    public void writeEscapedString(String serBuffer) {
        int pos = 0;
        while (pos < serBuffer.length()) {
            char ch = serBuffer.charAt(pos ++);
            switch (ch) {
                case '\b':
                    charWriter.print("\\b");
                    break;
                case '\f':
                    charWriter.print("\\f");
                    break;
                case '\n':
                    charWriter.print("\\n");
                    break;
                case '\r':
                    charWriter.print("\\r");
                    break;
                case '\t':
                    charWriter.print("\\t");
                    break;
                case '\\':
                    charWriter.print("\\\\");
                    break;
                case '/':
                    charWriter.print("\\/");
                    break;
                case '"':
                    charWriter.print("\\\"");
                    break;
                case '\u00a0':
                    charWriter.print("\\u00a0");
                    break;
                default:
                    if (ch < 256) {
                        charWriter.print(ch);
                    } else {
                        String hex = Integer.toHexString(ch);
                        charWriter.print("\\u" + "0000".substring(0, 4 - hex.length()) + hex);
                    }
                    break;
            } // switch ch
        } // while pos
    } // writeEscapedString

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
        serStack = new Stack/*<1.5*/<String>/*1.5>*/();
        serBuffer = new StringBuffer(256);
        serObjectSep = "{";
        serArraySep  = "[";
    } // startDocument

    /** Receive notification of the start of an element.
     *  Looks for the element which contains raw lines.
     *  @param uri The Namespace URI, or the empty string if the element has no Namespace URI
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix),
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix),
     *  or the empty string if qualified names are not available.
     *  @param attrs the attributes attached to the element.
     *  If there are no attributes, it shall be an empty Attributes object.
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs) {
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        elem = qName;
        if (false) {
        } else if (elem.equals(OBJECT_TAG )) {
            serObjectSep = "{";
            charWriter.print(serObjectSep);
        } else if (elem.equals(ARRAY_TAG  )) {
            serArraySep = "[";
            charWriter.print(serArraySep);
        } else if (elem.equals(ELEM_TAG   )) {
            if (serArraySep.equals("[")) {
                serArraySep = ",";
            } else {
                charWriter.print(serArraySep);
            }
        } else if (elem.equals(KEY_TAG    )) {
            if (serObjectSep.equals("{")) {
                serObjectSep = ",";
            } else {
                charWriter.print(serObjectSep);
            }
            charWriter.print("\"");
        } else if (elem.equals(NULL_TAG   )) {
            charWriter.print(NULL_TAG);
        } else if (elem.equals(TRUE_TAG   )) {
            charWriter.print(TRUE_TAG);
        } else if (elem.equals(FALSE_TAG  )) {
            charWriter.print(FALSE_TAG);
        } else if (elem.equals(NUMBER_TAG )) {
        } else if (elem.equals(STRING_TAG )) {
            charWriter.print("\"");
        } else if (elem.equals(VALUE_TAG  )) {
        } else {
        }
        serStack.push(elem);
        serBuffer.setLength(0);
    } // startElement

    /** Receive notification of the end of an element.
     *  Looks for the element which contains raw lines.
     *  Terminates the line.
     *  @param uri the Namespace URI, or the empty string if the element has no Namespace URI
     *  or if Namespace processing is not being performed.
     *  @param localName the local name (without prefix),
     *  or the empty string if Namespace processing is not being performed.
     *  @param qName the qualified name (with prefix),
     *  or the empty string if qualified names are not available.
     */
    public void endElement(String uri, String localName, String qName) {
        if (namespace.length() > 0 && qName.startsWith(namespace)) {
            qName = qName.substring(namespace.length());
        }
        elem = qName;
        serStack.pop();
        if (false) {
        } else if (elem.equals(ROOT_TAG   )) {
            // ignore
        } else if (elem.equals(OBJECT_TAG )) {
            charWriter.print("}");
        } else if (elem.equals(ARRAY_TAG  )) {
            charWriter.print("]");
        } else if (elem.equals(KEY_TAG    )) {
            charWriter.print("\":");
        } else if (elem.equals(NULL_TAG   )) {
        } else if (elem.equals(TRUE_TAG   )) {
        } else if (elem.equals(FALSE_TAG  )) {
        } else if (elem.equals(NUMBER_TAG )) {
        } else if (elem.equals(STRING_TAG )) {
            charWriter.print("\"");
        } else {
        }
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int length) {
        elem = (String) serStack.peek();
        String chars = replaceInResult(new String(ch, start, length));
        if (    elem.equals(KEY_TAG) ||  elem.equals(STRING_TAG)) {
            writeEscapedString(chars);
        } else {
            charWriter.print(chars);
        }
    } // characters

} // JSONTransformer
