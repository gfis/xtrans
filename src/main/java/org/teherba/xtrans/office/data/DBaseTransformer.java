/*  Transforms dBase II, III, IV and compatible database files.
    @(#) $Id: DBaseTransformer.java 967 2012-08-29 18:22:10Z gfis $
    2017-05-28: javadoc 1.8
    2008-07-31, Dr. Georg Fischer: copied from JSONTransformer
    1992-05-05, Georg Fischer: in C, copied from 'pdsdf'

    Caution, this file is UTF-8 encoded: äöüÄÖÜß
    dBase II is untested
*/
/*
 * Copyright 2008 Dr. Georg Fischer <punctum at punctum dot kom>
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

package org.teherba.xtrans.office.data;
import  org.teherba.xtrans.ByteRecord;
import  org.teherba.xtrans.ByteTransformer;
import  org.teherba.dbat.TableColumn;
import  java.util.ArrayList;
import  org.xml.sax.Attributes;
import  org.apache.logging.log4j.Logger;
import  org.apache.logging.log4j.LogManager;

/** Transforms dBase II, III, IV and compatible database files to/from XML.
 *  <p>
 *  Example of a dBase III file header:
 *  <pre>
+   0
     0: 83 5d  9 1e  5            2  1 3d                 .]........=.....
    10:                                                   ................
    20: 43 48 5f 32 30    ad  4  ec 24    43 80 71  b     CH_20.-.l$.C.q..
    30: 14    a4 6d ee 65 aa 21  f8 59  b    a4 6d fe 65  ..$mne*!xY..$m~e
    40: 4e 55 5f 36       ad  4  ec 24    4e 80 71  b     NU_6..-.l$.N.q..
    50:  6    a4 6d ee 65 aa 21  f8 59  b    a4 6d fe 65  ..$mne*!xY..$m~e
    60: 42 4f 4f 4c       ad  4  ec 24    4c 80 71  b     BOOL..-.l$.L.q..
    70:  1    a4 6d ee 65 aa 21  f8 59  b    a4 6d fe 65  ..$mne*!xY..$m~e
    80: 44 41 54 45       ad  4  ec 24    44 80 71  b     DATE..-.l$.D.q..
    90:  8    a4 6d ee 65 aa 21  f8 59  b    a4 6d fe 65  ..$mne*!xY..$m~e
    a0: 4d 45 4d 4f       ad  4  ec 24    4d 80 71  b     MEMO..-.l$.M.q..
    b0:  a    a4 6d ee 65 aa 21  f8 59  b    a4 6d fe 65  ..$mne*!xY..$m~e
    c0: 4e 55 5f 35 5f 32     4  ec 24    4e 80 71  b     NU_5_2..l$.N.q..
    d0:  5  2 a4 6d ee 65 aa 21  f8 59  b    a4 6d fe 65  ..$mne*!xY..$m~e
    e0: 43 48 5f 31 30        4  ec 24    43 80 71  b     CH_10...l$.C.q..
    f0:  a    a4 6d ee 65 aa 21  f8 59  b    a4 6d fe 65  ..$mne*!xY..$m~e

   100:  d    20 43 68 61 72 61  63 74 65 72 20 20 32 30  .. Character  20
   110: 20 20 20 46 65 6c 64 31  32 33 34 35 36 54 31 39     Feld123456T19
   120: 39 33 30 39 33 30 20 20  20 20 20 20 20 20 20 32  930930         2
   130: 31 32 2e 33 34 43 68 61  72 31 30 46 65 6c 64 20  12.34Char10Feld
   140: 43 68 61 72 61 63 74 65  72 20 20 32 30 20 20 20  Character  20
   150: 46 65 6c 64 31 32 33 34  35 36 54 31 39 39 33 30  Feld123456T19930
   160: 39 33 30 20 20 20 20 20  20 20 20 20 33 31 32 2e  930         312.
   170: 33 34 43 68 61 72 31 30  46 65 6c 64 20 43 68 61  34Char10Feld Cha
   180: 72 61 63 74 65 72 20 20  32 30 20 20 20 46 65 6c  racter  20   Fel
   190: 64 31 32 33 34 35 36 54  31 39 39 33 30 39 33 30  d123456T19930930
   1a0: 20 20 20 20 20 20 20 20  20 34 31 32 2e 33 34 43           412.34C
   1b0: 68 61 72 31 30 46 65 6c  64 20 43 68 61 72 61 63  har10Feld Charac
   1c0: 74 65 72 20 20 32 30 20  20 20 46 65 6c 64 31 32  ter  20   Feld12
   1d0: 33 34 35 36 54 31 39 39  33 30 39 33 30 20 20 20  3456T19930930
   1e0: 20 20 20 20 20 20 35 31  32 2e 33 34 43 68 61 72        512.34Char
   1f0: 31 30 46 65 6c 64 20 43  68 61 72 61 63 74 65 72  10Feld Character
+   1
   200: 20 20 32 30 20 20 20 46  65 6c 64 31 32 33 34 35    20   Feld12345
   210: 36 54 31 39 39 33 30 39  33 30 20 20 20 20 20 20  6T19930930
   220: 20 20 20 36 31 32 2e 33  34 43 68 61 72 31 30 46     612.34Char10F
   230: 65 6c 64 1a                                       eld.............
   240:                                                   ................
...
+   0
     0:  3 58  1  1 8c  1        c1  7 51  2              .X......A.Q.....
    10:                                                   ................
    20: 41 4e 52 45 44 45                 43              ANREDE.....C....
    30: 1c                                                ................
    40: 4b 44 5f 4e 52 5f                 43              KD_NR_.....C....
    50:  3                                                ................
    60: 4e 41 4d 45                       43              NAME.......C....
    70: 17                                                ................
    80: 44 41 54 55 4d                    44              DATUM......D....
    90:  8                                                ................
    a0: 53 54 52 41 5f 45                 43              STRA_E.....C....
    b0: 1c                                                ................
    c0: 50 4c 5a                          4e              PLZ........N....
    d0:  5                                                ................
</pre>
 *  @author Dr. Georg Fischer
 */
public class DBaseTransformer extends ByteTransformer {
    public final static String CVSID = "@(#) $Id: DBaseTransformer.java 967 2012-08-29 18:22:10Z gfis $";

    /** log4j logger (category) */
    private Logger log;

    /** No-args Constructor.
     */
    public DBaseTransformer() {
        super();
        setFormatCodes("dbase");
        setDescription("dBase Database File");
        setFileExtensions("dbf");
        setMimeType("application/octet-stream");
    } // Constructor()

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        setBinaryFormat(true);
        log = LogManager.getLogger(DBaseTransformer.class.getName());
        // mustAmpEscape = false;
        putEntityReplacements();
    } // initialize

    private static final String BOOLEAN_TAG     = "bool";
    private static final String COLUMN_TAG      = "col";
    private static final String COLUMNS_TAG     = "columns";
    private static final String DATA_TAG        = "td";
    private static final String DECIMAL_ATTR    = "decimal";
    private static final String HEADSIZE_ATTR   = "headsize";
    private static final String MARK_ATTR       = "mark";
    private static final String NAME_ATTR       = "name";
    private static final String NROWS_ATTR      = "nrows";
    private static final String ROOT_TAG        = "dbase";
    private static final String ROWSIZE_ATTR    = "rowsize";
    private static final String ROW_TAG         = "tr";
    private static final String TABLE_TAG       = "table";
    private static final String TYPE_ATTR       = "type";
    private static final String VERSION_ATTR    = "version";
    private static final String UPDATE_ATTR     = "last-update";
    private static final String WIDTH_ATTR      = "width";
    private static final String WITH_MEMO_ATTR  = "with-memo";

    /** state of finite automaton */
    private int state;
    /* values for <em>state</em> */
    private static final int    IN_FILE_INIT    = 0;
    private static final int    IN_DESC_INIT    = 1;
    private static final int    IN_DESC         = 2;
    private static final int    IN_DESC_TERM    = 3;
    private static final int    IN_SKIP_HEADER  = 4;
    private static final int    IN_ROW_INIT     = 5;
    private static final int    IN_ROW          = 6;
    private static final int    IN_COL          = 7;
    private static final int    IN_FINISH       = 8;

    /** dBase 2, 3, 4 ... */
    private  int genVersion;
    /** how many bytes are to be stored in 'content' */
    private  int storeLen;
    /** current number of bytes processed so far */
    private  int bytesRead;
    /** data start at this offset */
    private  long dataStart;
    /** length of file header */
    private  int headerSize;
    /** length of column descriptor */
    private  int colDescSize;
    /** total number of rows (records) */
    private  int rowCount;
    /** current row number, 0 based */
    private  int irow;
    /** current column number, 0 based */
    private  int rowSize;
    /** maximum length of a column name (nil padded) */
    private final int MAX_NAME = 11;

    /** length of a row (record) */
    protected  int igenCol;
    /** description of all columns */
    protected ArrayList<TableColumn> genColumnList;

    /** record for the specific format */
    protected ByteRecord genRecord;

    /** Maximum length of buffer for fields */
    private static final int MAX_BUF = 296;
    /** Buffer for string content and descriptors */
    protected byte[] genContent;
    /** index of 1st free byte in <em>genContent</em> */
    protected int gffContent;
    /** source encoding */
    protected String genEncoding;

    /** Gets a number from 2 bytes in a buffer (least significant byte order)
     *  @param buffer buffer containing the number
     *  @param start position of least significant byte = character
     *  @return short int
     */
    private int getLSB2(byte [] buffer, int start) {
        return     (buffer[start    ] & 0xff)
                | ((buffer[start + 1] & 0xff) << 8);
    } // getLSB2

    /** Evaluates the first of the file containing lengths and pointers.
     *  As a side effect, the dBase <em>genVersion</em> is set to 2, 3, 4 ...
     *  @param buffer buffer containing the file descriptor
     *  @return byte size of the file descriptor (8 or 32)
     *  Example:
<pre>
        0:  3 5c  5  5 10           a2    12                 .\......".......
       10:                                                   ................
</pre>
     */
    private int evalFileHeader(byte [] buffer) {
        int result = 32;
        int year   = 0;
        int month  = 0;
        int day    = 0;
        rowCount   = 0;
        headerSize = 0;
        rowSize    = 0;
        int  ind   = 0; // current index in 'genContent'
        genVersion    = buffer[ind ++]; // MGe; high bit = Memo file (.DBT) present
        boolean withMemo = (genVersion & 0x80) > 0;
        genVersion &= 0x07;
        switch (genVersion) { // which dBase is it?
            case 0x02: // dBase II
                result     =  8;
                colDescSize = 16;
                rowCount   =  getLSB2(buffer, ind);        ind += 2; // max 65535 rows
                month      =  buffer[ind ++] & 0xff;
                day        =  buffer[ind ++] & 0xff;
                year       =  buffer[ind ++] & 0xff;
                headerSize =  0;
                rowSize    =  getLSB2(buffer, ind);        ind += 2; // max. 1000
                break;
            default: // dBase III and later
                result     = 32;
                colDescSize = 32;
                year       =  buffer[ind ++] & 0xff;
                month      =  buffer[ind ++] & 0xff;
                day        =  buffer[ind ++] & 0xff;
                rowCount   =  getLSB2(buffer, ind)       ; ind += 2;
                rowCount  |= (getLSB2(buffer, ind) << 16); ind += 2;
                headerSize =  getLSB2(buffer, ind);        ind += 2;
                rowSize    =  getLSB2(buffer, ind);        ind += 2;
                break;
        } // switch genVersion
        year += (year <= 79) ? 2000 : 1900;

        pushXML(TABLE_TAG, toAttributes(new String[]
                { NAME_ATTR     , ""
                , VERSION_ATTR  , String.valueOf(genVersion)
                , ROWSIZE_ATTR  , String.valueOf(rowSize)
                , NROWS_ATTR    , String.valueOf(rowCount)
                , HEADSIZE_ATTR , String.valueOf(headerSize)
                , UPDATE_ATTR   , String.valueOf(year       )
                        + '-'   + String.valueOf(month + 100).substring(1) // with leading zero
                        + '-'   + String.valueOf(day   + 100).substring(1)
                , WITH_MEMO_ATTR, (withMemo ? "true" : "false")
                }));
        fireLineBreak();
        return result;
    } // evalFileHeader

    /** Evaluates a column (field) description.
     *  @param genContent buffer containing the column description, 16 or 32 bytes.
     *  Properties of the column are stored in a new element of <em>genColumnList</em>.
     *  Example:
<pre>
    20: 41 4e 52 45 44 45                 43              ANREDE.....C....
    30: 1c                                                ................
    40: 4b 44 5f 4e 52 5f                 43              KD_NR_.....C....
    50:  3                                                ................
    c0: 4e 55 5f 35 5f 32     4  ec 24    4e 80 71  b     NU_5_2..l$.N.q..
    d0:  5  2 a4 6d ee 65 aa 21  f8 59  b    a4 6d fe 65  ..$mne*!xY..$m~e
</pre>
     */
    private void evalColumnDesc(byte [] genContent) {
        int ind = 0;
        boolean busy = true;
        while (busy && ind < MAX_NAME) {
            if (genContent[ind] == '\u0000') {
                busy = false;
            } else {
                ind ++;
            }
        } // while busy
        String name = null;
        try {
            name = new String(genContent, 0, ind, genEncoding);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
            name = new String(genContent, 0, ind);
        }
        ind = MAX_NAME;
        char colType = (char) (genContent[ind ++] & 0xff);
        int width   = 0;
        int decimal = 0;
        switch (genVersion) { // which dBase is it?
            case 0x02: // dBase II
                width   = genContent[ind ++] & 0xff;
                ind += 2; // internal field data address
                decimal = genContent[ind ++] & 0xff;
                break;
            default: // dBase III and later
                ind += 4; // internal field data address
                width   = genContent[ind ++] & 0xff;
                decimal = genContent[ind ++] & 0xff;
                break;
        } // switch genVersion

        TableColumn genColumn = new TableColumn();
        genColumnList.add(genColumn);
        genColumn.setWidth(width);
        genColumn.setName (name);
        genColumn.setDataType(colType);
        String typeName = "VARCHAR";
        switch (colType) {
            default:
                typeName = "UNDEFINED";
                break;
            case 'C':
                // already set
                break;
            case 'N': // -.0123456789
                typeName = "DECIMAL";
                break;
            case 'L': // logical
                typeName = "BOOLEAN";
                break;
            case 'M': // memo: 10 digits for .DBT block number
                typeName = "DATALINK";
                break;
            case 'D': // YYYYMMDD
                typeName = "DATE";
                break;
        } // switch colType
        genColumn.setTypeName(typeName);

        pushXML(COLUMN_TAG, toAttributes(new String[]
                { NAME_ATTR     , name
                , TYPE_ATTR     , typeName
                , WIDTH_ATTR    , String.valueOf(width  )
                , DECIMAL_ATTR  , String.valueOf(decimal)
                }));
            fireCharacters(name);
        popXML(); // COLUMN_TAG
        fireLineBreak();
    } // evalColumnDesc

    /** Evaluates the content of column <em>igenCol</em> in the current row.
     *  @param genContent buffer containing the value of the data cell
     *  @param igenCol number of the column, 0 based
     */
    private void evalDataCell(byte [] genContent, int igenCol) {
        pushXML(DATA_TAG);
            switch (genColumnList.get(igenCol).getDataType()) {
                case 'L':
                    // interpretation of character for logical value
                    switch (genContent[0]) {
                        case 'y':
                        case 't':
                        case 'Y':
                        case 'T':
                            fireCharacters("true");
                            break;
                        default:
                        case 'f':
                        case 'n':
                        case 'F':
                        case 'N':
                            fireCharacters("false");
                            break;
                    } // switch
                    break;
                default:
                case 'C':
                case 'N':
                    int len = gffContent - 1;
                    while (len >= 0 && genContent[len] == ' ') { // rtrim
                        len --;
                    } // while
                    // System.out.println(new String(genContent, 0, len + 1));
                    try {
                        fireCharacters(new String (genContent, 0, len + 1, genEncoding));
                    } catch (Exception exc) {
                        log.error(exc.getMessage(), exc);
                        fireCharacters(new String (genContent, 0, len + 1));
                    }
                    break;
                case 'F':
                    fireCharacters(new String (genContent, 0, gffContent));
                    break;
                case 'M':
                    fireCharacters(new String (genContent, 0, gffContent));
                    break;
                case 'D': // YYYYMMDD
                    //       012345678
                    String date = (new String(genContent, 0, gffContent)).trim();
                    if (date.length() >= 8) {
                        fireCharacters(date.substring (0, 4)
                                + "-" + date.substring(4, 6)
                                + "-" + date.substring(6, 8));
                    }
                    break;
            } // switch dtype
        popXML(); // DATA_TAG
    } // evalDataCell

    /** Transforms from the specified format to XML
     *  @return whether the transformation was successful
     */
    public boolean generate() {
        boolean result = true;
        try {
            genRecord = new ByteRecord(2906);
            genEncoding = getSourceEncoding();
            genRecord.setEncoding(genEncoding);
            genColumnList = new ArrayList<TableColumn>(16); // empty so far
            igenCol = 0;

            bytesRead = 0;
            fireStartDocument();
            fireStartRoot(ROOT_TAG);
            fireLineBreak();
            String line;
            state = IN_FILE_INIT;
            genContent = new byte[MAX_BUF];
            gffContent = 0;
            byte [] buffer = genRecord.getBuffer();
            boolean readOff = true;
            byte ch; // current character read

            int len = 0;
            while ((len = genRecord.read(byteReader)) >= 0) {
                int ind = 0;
                while (ind < len) {
                    readOff = true;
                    ch = buffer[ind];
                    bytesRead ++;
                    // System.out.println("State " + state + ", char " + ch);

                    switch (state) {

                        case IN_FILE_INIT: // at the very start of the file; assume that entire header was read
                            bytesRead = evalFileHeader(buffer); // sets genVersion et al.
                            fireStartElement(COLUMNS_TAG);
                            fireLineBreak();
                            ind = bytesRead - 1;
                            state = IN_DESC_INIT;
                            break; // IN_FILE_INIT

                        case IN_DESC_INIT:
                            if (false) {
                            } else if (ch == '\r') { // end of column descriptors in dBase III: (0d 00)
                                dataStart = headerSize;
                                if (bytesRead >= dataStart) {
                                    state = IN_ROW_INIT;
                                    irow = 0;
                                    gffContent = 0;
                                    fireEndElement(COLUMNS_TAG);
                                    fireLineBreak();
                                } else {
                                    state = IN_SKIP_HEADER;
                                }
                            } else if (ch == '\u0000') { // end of column descriptors in dBase II
                                dataStart = 520;
                                state = IN_SKIP_HEADER;
                            } else {
                                gffContent = 0;
                                genContent[gffContent ++] = ch;
                                state = IN_DESC;
                            }
                            break; // IN_DESC_INIT;

                        case IN_DESC:
                            genContent[gffContent ++] = ch;
                            if (gffContent >= colDescSize) {
                                evalColumnDesc(genContent);
                                state = IN_DESC_INIT;
                            }
                            break; // IN_DESC;

                        case IN_ROW_INIT:
                            if (ch == '\u001a') { // behind last row
                                state = IN_FINISH;
                            } else if (irow >= rowCount) {
                                state = IN_FINISH;
                            } else {
                                if (ch == ' ') {
                                    pushXML(ROW_TAG);
                                } else {
                                    pushXML(ROW_TAG, toAttribute(MARK_ATTR, String.valueOf(ch)));
                                }
                                gffContent = 0;
                                igenCol = 0;
                                storeLen = genColumnList.get(igenCol).getWidth();
                                state = IN_COL;
                            }
                            break; // IN_ROW_INIT;

                        case IN_COL:
                            genContent[gffContent ++] = ch;
                            if (gffContent >= storeLen) { // this column is finished
                                evalDataCell(genContent, igenCol);
                                igenCol ++;
                                if (igenCol < genColumnList.size()) {
                                    gffContent = 0;
                                    storeLen = genColumnList.get(igenCol).getWidth();
                                    state = IN_COL;
                                } else {
                                    popXML(); // (ROW_TAG);
                                    fireLineBreak();
                                    irow ++;
                                    if (irow < rowCount) {
                                        state = IN_ROW_INIT;
                                    } else {
                                        state = IN_FINISH;
                                    }
                                }
                            } // column finished
                            break;

                        case IN_SKIP_HEADER:
                            if (bytesRead >= dataStart) {
                                state = IN_ROW_INIT;
                                irow = 0;
                                gffContent = 0;
                                fireEndElement(COLUMNS_TAG);
                                fireLineBreak();
                            }
                            break; // IN_SKIP_HEADER

                        case IN_FINISH: // ignore rest of file
                            popXML(); // (TABLE_TAG);
                            fireLineBreak();
                            break; // IN_FINISH

                        default:
                            fireComment("invalid state " + state);
                            break;

                    } // switch state

                    if (readOff) {
                        ind ++;
                    }
                } // while ind < len
            } // while not EOF

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

    /** column descriptors */
    private  ArrayList<TableColumn> serColumnList;
    /** one column descriptor */
    private TableColumn serColumn;
    /** current column number, 0 based */
    private int iserCol;
    /** buffer for values accumulated by successive activations of {@link #characters} */
    private byte [] serContent;
    /** first free byte position in <em>serContent</em> */
    private int sffContent;
    /** byte buffer to be written to the output file */
    private byte [] serBuffer;
    /** byte record for file header */
    private ByteRecord serRecord;
    /** current topmost element */
    private String serElement;
    /** version of the output dBase file */
    private int serVersion;
    /** result encoding */
    protected String serEncoding;

    /** Writes the attributes to the output file header
     *  @param attrs attributes of the table tag
     */
    public void writeFileHeader(Attributes attrs) {
        try {
            String
            value = attrs.getValue(VERSION_ATTR);
            int serVersion = 3;
            if (value != null) {
                try {
                    serVersion = Integer.parseInt(value);
                } catch (Exception exc) { }
            }

            value = attrs.getValue(UPDATE_ATTR);
            int year  = 80;
            int month = 1;
            int day   = 1;
            if (value != null) {
                try {
                    year  = Integer.parseInt(value.substring(0, 4)) % 100;
                    month = Integer.parseInt(value.substring(5, 7)) % 100;
                    day   = Integer.parseInt(value.substring(8,10)) % 100;
                } catch (Exception exc) { }
            }

            value = attrs.getValue(NROWS_ATTR);
            long rowCount = 1;
            if (value != null) {
                try {
                    rowCount = Long.parseLong(value);
                } catch (Exception exc) { }
            }

            value = attrs.getValue(ROWSIZE_ATTR);
            int rowSize = 29;
            if (value != null) {
                try {
                    rowSize = Integer.parseInt(value);
                } catch (Exception exc) { }
            }

            value = attrs.getValue(HEADSIZE_ATTR);
            int headerSize = 520;
            if (value != null) {
                try {
                    headerSize = Integer.parseInt(value);
                } catch (Exception exc) { }
            }

            value = attrs.getValue(WITH_MEMO_ATTR);
            boolean withMemo = false;
            if (value != null) {
                withMemo = value.startsWith("t");
            }

            switch (serVersion) {
                case 2:
                    serRecord = new ByteRecord(8);
                    serRecord.setPosition(0);
                    serRecord.setLSB(1, serVersion);
                    serRecord.setLSB(2, rowCount);
                    serRecord.setLSB(1, month);
                    serRecord.setLSB(1, day  );
                    serRecord.setLSB(1, year );
                    serRecord.setLSB(2, rowSize);
                    break;
                case 3:
                default:
                    serRecord = new ByteRecord(32);
                    serRecord.setPosition(0);
                    serRecord.setLSB(1, serVersion + (withMemo ? 0x80 : 0));
                    serRecord.setLSB(1, year );
                    serRecord.setLSB(1, month);
                    serRecord.setLSB(1, day  );
                    serRecord.setLSB(4, rowCount);
                    serRecord.setLSB(2, headerSize);
                    serRecord.setLSB(2, rowSize);
                    serRecord.fill('\u0000'); // pad with nil bytes
                    break;
            } // switch version
            serRecord.write(byteWriter);
        } catch (Exception exc) {
            log.error(exc.getMessage());
        }
    } // writeFileHeader

    /** Writes the attributes to the output file header
     *  @param serVersion dBase version of the output file: 2, 3, 0x83
     *  @param attrs attributes of the table tag
     */
    public void writeColumnDesc(int serVersion, Attributes attrs) {
        serColumn = new TableColumn();
        serColumnList.add(serColumn);
        String
        value = attrs.getValue(NAME_ATTR);
        String name = value;
        serColumn.setName(name);

        value = attrs.getValue(WIDTH_ATTR);
        int width = 10;
        if (value != null) {
            try {
                width = Integer.parseInt(value);
            } catch (Exception exc) { }
        }
        serColumn.setWidth(width);

        value = attrs.getValue(TYPE_ATTR);
        String typeName = value;
        serColumn.setTypeName(typeName);
        char typeLetter = 'C';
        if (false) {
        } else if (typeName.endsWith  ("CHAR"    )) {
            typeLetter = 'C';
        } else if (typeName.startsWith("BOOL"    )) {
            typeLetter = 'L';
        } else if (typeName.startsWith("DEC"     ) || typeName.startsWith("NUM")) {
            typeLetter = 'N';
        } else if (typeName.startsWith("DATALINK")) {
            typeLetter = 'M';
        } else if (typeName.startsWith("DATE"    )) {
            typeLetter = 'D';
        } else {
            typeLetter = 'C';
        }

        value = attrs.getValue(DECIMAL_ATTR);
        int decimal = 0;
        if (value != null) {
            try {
                decimal = Integer.parseInt(value);
            } catch (Exception exc) { }
        }
        serColumn.setDecimal(decimal);

        try {
            switch (serVersion) {
                case 2:
                    serRecord = new ByteRecord(16);
                    serRecord.setEncoding(serEncoding);
                    serRecord.setPosition(0);
                    serRecord.setPadChar('\u0000'); // pad with nil bytes
                    serRecord.setString(MAX_NAME, name);
                    serRecord.setLSB(MAX_NAME, 1, typeLetter);
                    serRecord.setLSB(1, width);
                    serRecord.setLSB(2, 0);
                    serRecord.setLSB(1, decimal);
                    break;
                case 3:
                default:
                    serRecord = new ByteRecord(32);
                    serRecord.setEncoding(serEncoding);
                    serRecord.setPosition(0);
                    serRecord.setPadChar('\u0000'); // pad with nil bytes
                    serRecord.setString(MAX_NAME, name);
                    serRecord.setLSB(1, typeLetter);
                    serRecord.setLSB(4, 0L);
                    serRecord.setLSB(1, width);
                    serRecord.setLSB(1, decimal);
                    break;
            } // switch version
            serRecord.write(byteWriter);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // writeColumnDesc

    /** Preprocesses the value in <em>serContent</em> for dBase field types,
     *  and writes the resulting byte stream to the output file
     *  @param serColumn current column descriptor
     */
    public void writeDataCell(TableColumn serColumn) {
        String typeName = serColumn.getTypeName();
        if (false) {
        } else if (typeName.equals("DATE"    )) {
            // 2008-08-02
            // 01234567890
            serContent[4] = serContent[5];
            serContent[5] = serContent[6];
            serContent[6] = serContent[8];
            serContent[7] = serContent[9];
            sffContent = 8;
        } else if (typeName.equals("BOOLEAN" )) {
            sffContent = 0;
            if (Character.toUpperCase(serContent[0]) == 'T') {
                serContent[sffContent ++] = (byte) 'T';
            } else {
                serContent[sffContent ++] = (byte) 'F';
            }
        } else if (typeName.equals("VARCHAR" )) {
        } else if (typeName.equals("DATALINK")) {
        }

        // now fill or truncate to column's width
        int width = serColumn.getWidth();
        if (sffContent >= width) { // truncate
            sffContent = width;
        } else { // pad
            while (sffContent < width) {
                serContent[sffContent ++] = (byte) ' ';
            } // while padding
        } // pad

        try {
            // now we can write a data cell from 'serContent'
            byteWriter.write(serContent, 0, width);
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // writeDataCell

    /** Receive notification of the beginning of the document.
     */
    public void startDocument() {
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
        serElement = qName;
        String value = null;
        try {
            if (false) {
            } else if (qName.equals(ROOT_TAG   )) {
                serContent = new byte[MAX_BUF];
                sffContent = 0;
                serColumnList = new ArrayList<TableColumn>(16); // empty so far
                iserCol = 0;
                serEncoding = getResultEncoding();
            } else if (qName.equals(COLUMN_TAG )) {
                writeColumnDesc(serVersion, attrs);
            } else if (qName.equals(COLUMNS_TAG)) {
                iserCol = 0;
            } else if (qName.equals(DATA_TAG   )) {
                sffContent = 0;
            } else if (qName.equals(ROW_TAG    )) {
                // start of row, insert the deletion marker byte
                String marker = attrs.getValue(MARK_ATTR);
                if (marker == null) {
                    marker = " ";
                }
                byteWriter.write(marker.charAt(0));
                iserCol = 0;
            } else if (qName.equals(TABLE_TAG  )) {
                writeFileHeader(attrs);
            } else {
            }
        } catch (Exception exc) {
            log.error(exc.getMessage());
        }
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
        serElement = qName;
        try {
            if (false) {
            } else if (qName.equals(ROOT_TAG   )) {
            } else if (qName.equals(COLUMN_TAG )) {
                iserCol ++;
            } else if (qName.equals(COLUMNS_TAG)) {
                serRecord.setLSB(0, 2, 0x0d);
                serRecord.write(byteWriter, 2);
            } else if (qName.equals(DATA_TAG   )) {
                serColumn = serColumnList.get(iserCol);
                writeDataCell(serColumn); // process and write according to column data type
                iserCol ++; // this column was processed
            } else if (qName.equals(ROW_TAG    )) {
                iserCol = 0;
            } else if (qName.equals(TABLE_TAG  )) {
                sffContent = 0;
                serContent[sffContent ++] = (byte) 0x1a;
                byteWriter.write(serContent, 0, sffContent);
            } else {
            }
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // endElement

    /** Receive notification of character data inside an element.
     *  @param ch the characters.
     *  @param start the start position in the character array.
     *  @param length the number of characters to use from the character array.
     */
    public void characters(char[] ch, int start, int length) {
        if (serElement.equals(DATA_TAG)) {
            String chars = replaceInResult(new String(ch, start, length));
            try {
                System.arraycopy(chars.getBytes(serEncoding), 0, serContent, sffContent, chars.length());
                sffContent += chars.length();
            } catch (Exception exc) {
                log.error(exc.getMessage(), exc);
            }
        }
        // else ignore all whitespace
    } // characters

} // DBaseTransformer
