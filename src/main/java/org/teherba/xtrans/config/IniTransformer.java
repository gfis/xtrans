/*  Transforms Windows .ini files to and from XML
    @(#) $Id: IniTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2010-07-28, Dr. Georg Fischer: based on ConfigTransformer
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

/** Transformer for Windows .ini files used for system and application configuration.
 *  The file is structured in [sections] with 
 *	key=value pairs on single lines. 
 *  Comments start with a semicolon. 
 *	Blank lines are ignored.
 *  See also <a href="http://en.wikipedia.org/wiki/INI_file">http://en.wikipedia.org/wiki/INI_file</a>.
 *  Example:
<pre>
[bcmath]
; Number of decimal digits for all bcmath functions.
bcmath.scale = 0

[browscap]
browscap = "D:\xampp\php\browscap\browscap.ini"

[Informix]
; Default host for ifx_connect() (doesn't apply in safe mode).
ifx.default_host =
</pre>
 *  @author Dr. Georg Fischer
 */
public class IniTransformer extends ConfigTransformer { 
    public final static String CVSID = "@(#) $Id: IniTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** Constructor.
     */
    public IniTransformer() {
        super();
        setFormatCodes("ini");
        setDescription("Windows .ini file");
        setFileExtensions("ini");
    } // Constructor()
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
        hardFoldLength = 0; // no hard folding
        pairSeparator  = "=";
        nextContinue   = "\\";
        lineEndComment = new String[] { ";" };
	} // initialize

    /** Determines whether the line is a section heading.
     *	@param line trimmed current line
     */
    protected boolean isSectionHeading(String line) {
    	boolean result = false;
        if (line.startsWith("[") && line.endsWith("]")) {
        	result = true;
        	sectionName = line.substring(1, line.length() - 1);
		} // [name]
		return result;
	} // isSectionHeading
    
    /** Determines whether the line is empty
     *	and if so, terminates the current row.
     *	@param line current trimmed line
     */
    protected boolean isEmptyLine(String line) {
    	boolean result = false;
        if (line.length() == 0) {
        	result = true;
           	terminateRow(false); // empty lines will not cause a section break
           	initializeRow(line, -1);
		} // length = 0
		return result; 
	} // isEmptyLine

} // IniTransformer
