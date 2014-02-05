/*  Transformer SOAP Service
    @(#) $Id: XtransService.java 9 2008-09-05 05:21:15Z gfis $
    2005-07-27, Georg Fischer
    
    Service to be called via SOAP, offering the functions of Transformer
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

package org.teherba.xtrans;
import  org.teherba.xtrans.MainTransformer;

/** This class is the SOAP service interface to @see MainTransformer, 
 *  and ressembles the functionality of the commandline interface
 *  in that class.
 *  @author Dr. Georg Fischer
 */
public class XtransService { 
    public final static String CVSID = "@(#) $Id: XtransService.java 9 2008-09-05 05:21:15Z gfis $";

    /** Returns the results of an activation of <em>MainTransformer</em>
     *  to a SOAP client.
     *  @param function a code for the desired function: xml ...
     *  @param parm1 primary entity to be checked 
     *  @param parm2 additional parameter
     *  @return error message: "not yet implemented"
     */
    public String getResponse(String function, String parm1, String parm2)  {
        String response = "";
        try {
            if  (   true
            //  ||  function.equals("xml"     )        
                ) 
            {
                response = "XtransService not yet implemented";
                        // checker.process(new String[] { "-" + function, parm1, parm2 });
            }
            else { // invalid function
                response = "001 - invalid function \"" + function + "\"";
            }
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        }
        return response;
    } // getResponse

} // XtransService
