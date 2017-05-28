/*  Transforms Java MANIFEST.MF files to and from XML
    @(#) $Id: ManifestTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2010-07-28, Dr. Georg Fischer: copied from IniTransformer
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
package org.teherba.xtrans.config;
import  org.teherba.xtrans.config.ConfigTransformer;

/** Transforms Java MANIFEST.MF files to and from XML.
 *  The format is described in
 *  <a href="http://java.sun.com/javase/6/docs/technotes/guides/jar/jar.html">http://java.sun.com/javase/6/docs/technotes/guides/jar/jar.html#JAR Manifest</a>.
 *  Example:
<pre>
Manifest-Version: 1.0
Ant-Version: Apache Ant 1.7.1
Created-By: 16.3-b01 (Sun Microsystems Inc.)
Built-By: gfis
Main-Class: org.teherba.xtrans.MainTransformer

Name: xtrans
Specification-Title: xtrans
Specification-Version: 1.7 for JDK 1.5
Specification-Vendor: Dr. Georg Fischer
Implementation-Title: xtrans
Implementation-Version: xtrans 566 2010-07-27-09-06-56
Implementation-Vendor: Dr. Georg Fischer

</pre>
/ *  @author Dr. Georg Fischer
 */
public class ManifestTransformer extends ConfigTransformer {
    public final static String CVSID = "@(#) $Id: ManifestTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** Constructor.
     */
    public ManifestTransformer() {
        super();
        setFormatCodes("manifest");
        setDescription("Java MANIFEST.MF file");
        setFileExtensions("MF");
    } // Constructor()

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
        pairSeparator  = ":";
        nextContinue   = "";
        lineEndComment = new String[] { ";" };
        hardFoldLength = 70;
    } // initialize

} // ManifestTransformer
