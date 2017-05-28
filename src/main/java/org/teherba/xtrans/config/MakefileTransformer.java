/*  Transforms Unix makefiles (input to the 'make' utility)
    @(#) $Id: MakefileTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2010-08-02, Dr. Georg Fischer: based on ConfigTransformer
*/
/*
 * Copyright 2010 Dr. Georg Fischer <punctum at punctum dot kom>
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
package org.teherba.xtrans.config;
import  org.teherba.xtrans.config.ConfigTransformer;
import  java.util.regex.Matcher;

/** Transforms Unix makefiles (input to the <em>make</em> utility).
 *  Example:
<pre>
# makesub - makefile for subdirectories of ./hahn
# set THIS outside

TOOL=../../../../../tool/
.SUFFIXES: .des .htm
.des.htm:
    perl $(TOOL)desnatl.pl en $*.des &gt; $*.htm
    perl $(TOOL)prenatl.pl es $*.htm | perl $(TOOL)delnatl.pl es &gt; $*.es.html

$(THIS): $(THIS).htm $(THIS).tab.des
    cp $(THIS).en.html index.html
#         in order to prevent directory browsing

$(THIS).tab.des: $(THIS).dat
    perl $(TOOL)datprep.pl $(THIS).dat | perl $(TOOL)datdes.pl table &gt; $@
$(THIS).lis.des: $(THIS).dat
    perl $(TOOL)datprep.pl $(THIS).dat | perl $(TOOL)datdes.pl list  &gt; $@
</pre>
 *  @author Dr. Georg Fischer
 */
public class MakefileTransformer extends ConfigTransformer {
    public final static String CVSID = "@(#) $Id: MakefileTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** Constructor.
     */
    public MakefileTransformer() {
        super();
        setFormatCodes("make");
        setDescription("Unix makefiles");
        setFileExtensions("make");
    } // Constructor()

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        hardFoldLength = 0; // no hard folding
        pairSeparator  = "\t"; // colon will be replaced by \t
        nextContinue   = "\\"; // but soft folding
        lineEndComment = new String[] { "#" };
    } // initialize

    /** Prepares the input line and find any separator.
     *  @param line line to be prepared
     *  @return position of a separator, or -1 if none was found
     */
    protected int analyzeLine(String line) {
        int sepos = -1; // no separator found
        if (false) {
        } else if (line.startsWith("\t")) {
            sepos = 0;
        } else { // no leading tab
            // Pattern wordPunct = Pattern.compile("([^=\\:]+)([=\\:])");
            Matcher matcher = wordPunct.matcher(line);
            if (matcher.lookingAt()) {
                String separator = matcher.group(2);
                sepos = matcher.group(1).length(); // position of "=" or ":"
                if (separator.equals(":")) {
                    terminateRow(true); // start a new table
                }
            } // if lookingAt
        } // no leading tab
        return sepos;
    } // analyzeLine

    /** Determines whether the line is a section heading.
     *  @param line trimmed current line
     */
    protected boolean isSectionHeading(String line) {
        boolean result = false;
        return result;
    } // isSectionHeading

    /** Determines whether the line is empty
     *  and if so, terminates the current row.
     *  @param line current trimmed line
     */
    protected boolean isEmptyLine(String line) {
        boolean result = false;
        return result;
    } // isEmptyLine

} // MakefileTransformer
