/*  Transforms VCard address book entries
    @(#) $Id: VCardTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2017-05-28: javadoc 1.8
    2007-10-12: Dr. Georg Fischer
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

package org.teherba.xtrans.organizer;
import  org.teherba.xtrans.organizer.OrganizerTransformer;

/** Transformer for VCard address book entries.
 *  VCard is an ASCII-based format for the description of entries in
 *  an address or phone book, and is described in
 *  <a href="http://www.ietf.org/rfc/rfc2426.txt">RFC 2426</a>.
 *  Each line starts with a key, a colon, and the value.
 *  Examples:
 * <pre>
   BEGIN:vCard
   VERSION:3.0
   FN:Frank Dawson
   ORG:Lotus Development Corporation
   ADR;TYPE=WORK,POSTAL,PARCEL:;;6544 Battleford Drive
    ;Raleigh;NC;27613-3502;U.S.A.
   TEL;TYPE=VOICE,MSG,WORK:+1-919-676-9515
   TEL;TYPE=FAX,WORK:+1-919-676-9564
   EMAIL;TYPE=INTERNET,PREF:Frank_Dawson@Lotus.com
   EMAIL;TYPE=INTERNET:fdawson@earthlink.net
   URL:http://home.earthlink.net/~fdawson
   END:vCard

   BEGIN:vCard
   VERSION:3.0
   FN:Tim Howes
   ORG:Netscape Communications Corp.
   ADR;TYPE=WORK:;;501 E. Middlefield Rd.;Mountain View;
    CA; 94043;U.S.A.
   TEL;TYPE=VOICE,MSG,WORK:+1-415-937-3419
   TEL;TYPE=FAX,WORK:+1-415-528-4164
   EMAIL;TYPE=INTERNET:howes@netscape.com
   END:vCard
 * </pre>
 *  @author Dr. Georg Fischer
 */
public class VCardTransformer extends OrganizerTransformer {
    public final static String CVSID = "@(#) $Id: VCardTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** No-args Constructor.
     */
    public VCardTransformer() {
        super();
        setFormatCodes      ("vcard");
        setDescription      ("VCard address/phone book entries");
        setFileExtensions   ("vcf");
        setMimeType         ("text/x-vcard");
    } // Constructor()

    /** Initializes the (quasi-constant) global structures and variables.
     *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
     *  selected generator and serializer.
     */
    public void initialize() {
        super.initialize();
    } // initialize

} // VCardTransformer
