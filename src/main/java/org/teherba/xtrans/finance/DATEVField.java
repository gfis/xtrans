/*  Transformer for DATEV accounting data files
    @(#) $Id: DATEVTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2016-10-13, Georg Fischer: extracted from DATEVTransformer
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
package org.teherba.xtrans.finance;

/** Inner class for the definition of a DATEV field
 *  @author Dr. Georg Fischer
 */
public class DATEVField {
    public final static String CVSID = "@(#) $Id: DATEVTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** delimiting character (at the end of the field) */
    char delim;
    /** attribute name */
    String attrName;
    /** field width in bytes */
    int width;
    /** whether to remove leading zeroes */
    boolean trimZeroes;

    /** Constructor
     *  @param delim delimiting character (at the end of the field)
     *  @param attrName attribute name
     *  @param width field width in bytes
     *  @param trimZeroes whether to remove leading zeroes
     */
    public DATEVField(char delim, String attrName, int width, boolean trimZeroes) {
        // log.debug("DATEVField(" + delim + "," + attrName + "," + width + "," + trimZeroes + ")");
        this.delim      = delim;
        this.attrName   = attrName;
        this.width      = width;
        this.trimZeroes = trimZeroes;
    }
} // DATEVField
