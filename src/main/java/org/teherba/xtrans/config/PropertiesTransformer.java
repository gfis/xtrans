/*  Transforms Java Properties files 
    @(#) $Id: PropertiesTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2010-08-02, Dr. Georg Fischer: copied from MakefileTransformer
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
import	java.util.regex.Matcher;
import	java.util.regex.Pattern;

/** Transforms Java <em>Properties</em> files as described in 
 *  the <em>load</em> method.
 *  Example:
<pre>
# log4j.rootCategory=INFO, CONSOLE, LOGFILE
log4j.rootCategory=INFO, CONSOLE
# CONSOLE is set to be a ConsoleAppender using a PatternLayout.
log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender
log4j.appender.CONSOLE.Threshold=INFO
log4j.appender.CONSOLE.layout=org.apache.log4j.PatternLayout
log4j.appender.CONSOLE.layout.ConversionPattern=%d{ISO8601} %-4r [%t] %-5p %c %x - %m%n 
log4j.appender.CONSOLE.Target=System.err
</pre>
 *  @author Dr. Georg Fischer
 */
public class PropertiesTransformer extends ConfigTransformer { 
    public final static String CVSID = "@(#) $Id: PropertiesTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** Constructor.
     */
    public PropertiesTransformer() {
        super();
        setFormatCodes("props,properties");
        setDescription("Java Properties");
        setFileExtensions("properties,props");
    } // Constructor()
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        hardFoldLength = 0; // no hard folding
        pairSeparator  = "="; // colon will be replaced by \t
        nextContinue   = "\\"; // but soft folding
        lineEndComment = new String[] { "#", "!" };
	} // initialize

    /** Prepares the input line and find any separator.
     *	@param line line to be prepared
     *	@return position of a separator, or -1 if none was found
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
     *	@param line trimmed current line
     */
    protected boolean isSectionHeading(String line) {
    	boolean result = false;
		return result;
	} // isSectionHeading
    
    /** Determines whether the line is empty
     *	and if so, terminates the current row.
     *	@param line current trimmed line
     */
    protected boolean isEmptyLine(String line) {
    	boolean result = false;
		return result; 
	} // isEmptyLine

} // PropertiesTransformer
