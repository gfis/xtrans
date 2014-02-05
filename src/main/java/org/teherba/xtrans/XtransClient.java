/*  SOAP Client which calls XtransService
    @(#) $Id: XtransClient.java 81 2009-02-12 19:18:51Z gfis $
	2008-12-10: use service.properties
    2005-08-26, Dr. Georg Fischer: copied from Axis 1.3
*/
/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
 
/** SOAP client which transforms various file formats to and from XML
 *	with the aid of the xtrans service.
 *  This class is the service client interface to <em>BaseTransformer</em>,
 *  and ressembles the functionality of the commandline interface
 *  in @see MainTransformer.
 *  @author Dr. Georg Fischer
 */
package org.teherba.xtrans;
import  org.apache.axis.client.Call;
import  org.apache.axis.client.Service;
import  java.io.File;
import  java.io.FileInputStream;
import  java.util.Properties;
import  javax.xml.namespace.QName;

public class XtransClient {
    public final static String CVSID = "@(#) $Id: XtransClient.java 81 2009-02-12 19:18:51Z gfis $";

    /** Activates the <em>XtransService</em>
     *  @param args arguments on the commandline:
     *  <ul>
     *  <li>language, ISO 639 code, for example "de"</li>
     *  <li>function to be activated: month, weekday, season, parse ...</li>
     *  <li>digits (optional): sequence of digits, or number word for function <em>parse</em></li>
     *  </ul>
     */
    public static void main(String [] args) {
        try {
	        Properties props = new Properties();
    	    String propsName = "service.properties";
    	    props.load(XtransClient.class.getClassLoader().getResourceAsStream(propsName)); // (1) load from classpath (jar)
    	    File propsFile = new File(propsName);
    	    if (propsFile.exists()) {
    	        props.load(new FileInputStream(propsFile)); // (2) add any properties from a file in the current directory
    	    }
        	String axisURL = props.getProperty("axis_url", "http://localhost:8180/axis");

            String   endpoint = axisURL + "/services/XtransService";
            Service  service  = new Service();
            Call     call     = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            call.setOperationName(new QName(axisURL, "getResponse"));
            call.addParameter("function", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            call.addParameter("parm1"   , org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            call.addParameter("parm2"   , org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            call.setReturnType(           org.apache.axis.Constants.XSD_STRING);
            
            String language = "de";
            String function = "spell";
            String digits   = "";
            int iargs = 0;
            if (iargs < args.length) {
                language = args[iargs ++];
                if (iargs < args.length) {
                    function = args[iargs ++];
                    while (iargs < args.length) {
                        digits += args[iargs ++] + " ";
                    }
                }
            }
            String ret = (String) call.invoke( new Object[] { function, language, digits } );
            System.out.println(ret);
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        }
    } // main
    
} // XtransClient
