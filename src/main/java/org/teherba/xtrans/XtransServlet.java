/*  Servlet which
    transforms various file formats to and from XML.
    @(#) $Id: XtransServlet.java 796 2011-09-10 13:58:28Z gfis $
	2011-04-06: XtransFactory -> MultiFormatFactory
	2010-12-13: sqlPretty
    2008-06-25: with view="xslTrans"; textarea input
    2007-11-05: also with LexicalHandler
    2005-07-27, Dr. Georg Fischer
    For the discussion of "enctype=multipart/form-data" c.f.
    http://forum.java.sun.com/thread.jspa?threadID=329408&messageID=1340610

    Content-Disposition: attachment; filename="gfis.gif"
    Content-Type: image/gif
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
import  org.teherba.xtrans.BaseTransformer;
import  org.teherba.xtrans.MainTransformer; // for Javadoc only
import  org.teherba.xtrans.MultiFormatFactory;
import  org.teherba.xtrans.XMLTransformer;
import  org.teherba.xtrans.XtransFactory;
import  java.io.IOException;
import  java.io.StringReader;
import  java.util.Iterator;
import  java.util.List;
import  javax.servlet.RequestDispatcher;
import  javax.servlet.ServletConfig;
import  javax.servlet.ServletException;
import  javax.servlet.http.HttpServlet;
import  javax.servlet.http.HttpServletRequest;
import  javax.servlet.http.HttpServletResponse;
import  javax.servlet.http.HttpSession;
import	org.apache.log4j.Logger;
import  org.apache.commons.fileupload.FileItem;
import  org.apache.commons.fileupload.FileItemFactory;
import  org.apache.commons.fileupload.disk.DiskFileItemFactory;
import  org.apache.commons.fileupload.servlet.ServletFileUpload;

/** Servlet which
 *  transforms various file formats to and from XML.
 *  This class is the servlet interface to <em>BaseTransformer</em>,
 *  and ressembles the functionality of the commandline interface
 *  in {@link MainTransformer}.
 *  @author Dr. Georg Fischer
 */
public class XtransServlet extends HttpServlet {
    public final static String CVSID = "@(#) $Id: XtransServlet.java 796 2011-09-10 13:58:28Z gfis $";
    // public final static long serialVersionUID = 19470629006L;

	/** log4j logger (category) */
	private Logger log;

    /** Delivers some {@link BaseTransformer} */
    private XtransFactory factory;

    /** Initializes the servlet
     *  @param config configuration data
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
		log = Logger.getLogger(XtransServlet.class.getName());
        factory = new MultiFormatFactory();
    } // init

    /** Creates the response for a HTTP GET request.
     *  @param request fields from the client input form
     *  @param response data to be sent back the user's browser
     *  @throws IOException
     */
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
        generateResponse(request, response);
    } // doGet

    /** Creates the response for a HTTP POST request.
     *  @param request fields from the client input form
     *  @param response data to be sent back the user's browser
     *  @throws IOException
     */    
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        generateResponse(request, response);
    } // doPost

    /** Gets the value of an HTML input field, maybe as empty string
     *  @param request request for the HTML form
     *  @param name name of the input field
     *  @return non-null (but possibly empty) string value of the input field
     */
    private String getInputField(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value == null) {
            value = "";
        }
        return value;
    } // getInputField

    /** Creates the response for a HTTP GET or POST request.
     *  @param request fields from the client input form
     *  @param response data to be sent back the user's browser
     *  @throws IOException
     */
    public void generateResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String newPage      = ""; // no error so far
            HttpSession session = request.getSession();
            String sourceFormat = "line";
            String resultFormat = "xml";
            String dir          = "";
            String options      = "";
            String pipeLine     = "";
            String inputText	= "";
            String style        = "genRecord";
            String view			= "index";
            BaseTransformer generator   = null;
            BaseTransformer serializer  = null;

            FileItemFactory fuFactory = new DiskFileItemFactory(); // Create a factory for disk-based file items
            ServletFileUpload upload  = new ServletFileUpload(fuFactory); // Create a new file upload handler
            List /* FileItem */ items = upload.parseRequest(request); // Parse the request
            FileItem fileItem = null; // not seen so far
            Iterator iter = items.iterator();
            while (iter.hasNext()) { // Process the uploaded items
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                    String name  = item.getFieldName();
                    String value = item.getString();
                    session.setAttribute(name, value);
                    if (false) {
                    } else if (name.equals("view"   )) {
                        view		 = value;
                    } else if (name.equals("format" )) {
                        sourceFormat = value;
                    } else if (name.equals("target" )) {
                        resultFormat = value;
                    } else if (name.equals("pipeLine" )) {
                        pipeLine = value;
                    } else if (name.equals("dir"    )) {
                        dir = value;
                    } else if (name.equals("enc1"   )) {
                        if (value.length() > 0) {
                            options += "-enc1 " + value + " ";
                        }
                    } else if (name.equals("enc2"   )) {
                        if (value.length() > 0) {
                            options += "-enc2 " + value + " ";
                        }
                    } else if (name.equals("nsp"    )) {
                        if (value.length() > 0) {
                            options += "-nsp " + value + " ";
                        }
                    } else if (name.equals("opt"    )) {
                        options += value + " ";
                    } else if (name.equals("intext" )) {
                        inputText = value;
                    } else { // unknown field name
                    }
                } else { // no formField - uploaded fileItem
                    fileItem = item;
                }
            } // while uploaded items
            
            log.debug("view = " + view);
            
            if (newPage.length() > 0) { // already some error - fall through
            	
            } else if (! fileItem.getString().matches("\\s*") && ! inputText.equals("")
            		||   fileItem.getString().matches("\\s*") &&   inputText.equals("")
            		) { // either file or inputText must be specified
                newPage = "/message.jsp";
                session.setAttribute("messno"  , "007");
                
            } else if (view.equals("index")) { // index page: foreign -> XML or vice versa
	            generator  = factory.getTransformer(sourceFormat); // try whether the format is valid
        	    if (generator == null) {
            	    newPage = "/message.jsp";
                	session.setAttribute("messno"  , "003"); // invalid format code
            	} else {
		            if (dir.equals("from")) { // interchange source and result formats
    		            String temp  = resultFormat; // xml
        		        resultFormat = sourceFormat; // foreign
            		    sourceFormat = temp; // xml
            		    serializer   = generator; // foreign
			            generator    = factory.getTransformer(sourceFormat); // xml
           			} else {
    	        		serializer   = factory.getTransformer(resultFormat); // xml
    	        	}
                	generator .parseOptionString(options);
			        generator .setSourceEncoding(generator .getOption("enc1", "UTF-8")); // should be symmetrical for testing
                	serializer.parseOptionString(options);
        			serializer.setResultEncoding(serializer.getOption("enc2", "UTF-8"));
                	generator .setContentHandler(serializer);
                	generator .setLexicalHandler(serializer);
       	        	response.setCharacterEncoding(serializer.getResultEncoding());

                	if (false) {
                	} else if (dir.equals("to")) { // to XML
                	    if (resultFormat.equals("xml")) {
                	        response.setHeader("Content-Disposition", "inline; filename=\"" + fileItem.getName() + ".xml\"");
                	        response.setContentType("text/xml");
                	        generator.setMimeType("text/xml"); // is needed sometimes because of "&amp;" multiplication
                	        if (generator.isBinaryFormat()) {
                	            generator.setByteReader(fileItem.getInputStream ());
                	        } else {
                	            generator.setCharReader(new StringReader
                	            		( inputText.matches("\\s*")
                	            		? fileItem.getString(generator.getSourceEncoding())
                	            		: inputText
                	            		));
                	        }
                	        serializer.setCharWriter(response.getWriter());
                	        generator.generate();
                	    } else {
                	        newPage = "/message.jsp";
                	        session.setAttribute("messno"  , "004"); // invalid resultFormat format
                	    }

                	} else if (dir.equals("from")) { // from XML
                	    if (sourceFormat.equals("xml")) {
                	        String name = fileItem.getName();
                	        name = (name.endsWith(".xml"))
                	                ? name.substring(0, name.length() - 4) // remove ".xml"
                	                : name + "." + generator.getFileExtension(); // append default extension
                	        response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
                	        response.setContentType(generator.getMimeType());
                	        generator.setCharReader(new StringReader
                	                ( inputText.matches("\\s*")
                	                ? fileItem.getString(generator.getSourceEncoding())
                	                : inputText
                	                ));
                	        if (serializer.isBinaryFormat()) {
                	            serializer.setByteWriter(response.getOutputStream());
                	        } else {
                	            serializer.setCharWriter(response.getWriter      ());
                	        }
                	        generator.generate();
                	    } else {
                	        newPage = "/message.jsp";
                	        session.setAttribute("messno"  , "003"); // invalid sourceFormat format
                	    } 
                		// from XML
                	} else { // invalid direction
	            		log.error("invalid direction " + dir);
	            		newPage = "/message.jsp";
    	        	    session.setAttribute("messno"  , "003"); // invalid sourceFormat format
                	}
                	generator .closeAll();
                	serializer.closeAll();
            	} // index page: foreign -> XML or vice versa
                
            } else if (view.equals("xslTrans")) { // XSLT to text (Java or SQL)
				if (true) {
		            // log.debug("getRealPath(\"/docs\")=" + getServletContext().getRealPath("/docs") + ", dir=" + dir);
					// MainTransformer pipe = new MainTransformer(); // was XtransPipe
					log.debug("pipeLine = " + pipeLine);
					factory.setRealPath(getServletContext().getRealPath("/xslt"));
					factory.createPipeLine(pipeLine.split("\\s+"));
		            generator    = factory.getGenerator ();
		            serializer   = factory.getSerializer();
                	// generator .setContentHandler(serializer);
                	// generator .setLexicalHandler(serializer);
       	        	response.setCharacterEncoding(serializer.getResultEncoding());

                	if (true) {
						if (serializer instanceof XMLTransformer) {
                	        response.setHeader("Content-Disposition", "inline; filename=\"" 
                	        		+ fileItem.getName() + ".xml\"");
                	        response.setContentType("text/xml");
                	        generator.setMimeType("text/xml"); // is needed sometimes because of "&amp;" multiplication
                	        // generator.setCharWriter(response.getWriter());
                	        if (generator.isBinaryFormat()) {
                	            generator.setByteReader(fileItem.getInputStream ());
                	        } else {
                	            generator.setCharReader(new StringReader
                	            		( inputText.matches("\\s*")
                	            		? fileItem.getString(generator.getSourceEncoding())
                	            		: inputText
                	            		));
                	        }
                	        serializer.setCharWriter(response.getWriter());
                	        generator.generate();
                	    } else {
                	        String name = fileItem.getName();
                	        name = (name.endsWith(".xml"))
                	                ? name.substring(0, name.length() - 4) // remove ".xml"
                	                : name + "." + generator.getFileExtension(); // append default extension
                	        response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
                	        response.setContentType(generator.getMimeType());
                	        generator.setCharReader(new StringReader
                	                ( inputText.matches("\\s*")
                	                ? fileItem.getString(generator.getSourceEncoding())
                	                : inputText
                	                ));
                	        if (serializer.isBinaryFormat()) {
                	            serializer.setByteWriter(response.getOutputStream());
                	        } else {
                	            serializer.setCharWriter(response.getWriter      ());
                	        }
                	        generator.generate();
                	    } 
                		// from XML
                	}
                	generator .closeAll();
                	serializer.closeAll();
            	} // xslTrans: XML pipe
            
            } else if (view.equals("sqlPretty")) { // SQL pretty printer
				if (true) {
					// MainTransformer pipe = new MainTransformer(); // was XtransPipe
					log.debug("pipeLine = " + pipeLine);
					factory.setRealPath(getServletContext().getRealPath("/"));
					factory.createPipeLine(pipeLine.split("\\s+"));
		            generator    = factory.getGenerator ();
		            serializer   = factory.getSerializer();
       	        	response.setCharacterEncoding(serializer.getResultEncoding());

           	        String name = fileItem.getName();
           	        response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
           	        response.setContentType(generator.getMimeType());
           	        generator.setCharReader(new StringReader
           	                ( inputText.matches("\\s*")
           	                ? fileItem.getString(generator.getSourceEncoding())
           	                : inputText
           	                ));
       	            serializer.setCharWriter(response.getWriter      ());
           	        generator.generate();
                	generator .closeAll();
                	serializer.closeAll();
            	} // sqlPretty
            
            } else { // invalid view
            	log.error("invalid view " + view);
            	newPage = "/message.jsp";
                session.setAttribute("messno"  , "006"); // invalid view
                session.setAttribute("format"  , view); 
            }

            if (newPage.length() > 0) { // error message or XSD file
                System.out.println("newPage=\"" + newPage + "\", messno=" + session.getAttribute("messno"));
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(newPage);
                dispatcher.forward(request, response);
            }
        } catch (Exception exc) {
            response.getWriter().write(exc.getMessage());
            log.error(exc.getMessage(), exc);
        } // try
    } // generateResponse
    
} // XtransServlet
