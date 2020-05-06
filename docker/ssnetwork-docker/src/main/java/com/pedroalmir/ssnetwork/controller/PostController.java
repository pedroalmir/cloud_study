/**
 * 
 */
package com.pedroalmir.ssnetwork.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import com.pedroalmir.ssnetwork.controller.base.GenericServlet;
import com.pedroalmir.ssnetwork.controller.result.MessageResult;
import com.pedroalmir.ssnetwork.dao.UserDAO;
import com.pedroalmir.ssnetwork.dao.core.MyEntityManager;
import com.pedroalmir.ssnetwork.model.User;

/**
 * @author Pedro Almir
 */
public class PostController extends GenericServlet {

	/** Serial Version UID */
	private static final long serialVersionUID = -2608338114748189594L;
	
	/** Upload settings */
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("ISO-8859-15");
		if (!ServletFileUpload.isMultipartContent(request)) {
			sendResponse(response, MessageResult.createErrorMessage("internal.error", null));
			return;
		}
		
		// configures upload settings
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        
        ServletFileUpload upload = new ServletFileUpload(factory);
        // sets maximum size of upload file
        upload.setFileSizeMax(MAX_FILE_SIZE);
        // sets maximum size of request (include file + form data)
        upload.setSizeMax(MAX_REQUEST_SIZE);
        
        try {
        	// Parse the request
        	List<FileItem> items = upload.parseRequest(new ServletRequestContext(request));
        	// Process the uploaded items
        	Iterator<FileItem> iter = items.iterator();
        	// Map values to create user
        	Map<String, Object> postMap = new LinkedHashMap<>();
        	while (iter.hasNext()) {
        	    FileItem item = iter.next();
        	    if (item.isFormField()) {
        	    	postMap.put(item.getFieldName(), new String(item.getString().getBytes(), "UTF-8"));
        	    } else {
                    String name = item.getName();
                    File tmpFile = new File(name);
                    item.write(tmpFile);
                    postMap.put(item.getFieldName(), tmpFile);
        	    }
        	}
        	
        	MyEntityManager.getManager().clear();
        	UserDAO userDAO = new UserDAO();
        	
        	// Validate form data
        	if(validadeFormData(postMap)) {
        		User user = userDAO.findByEmail((String) postMap.get("email"));
        		if(user != null) {
        			user = userDAO.includePost(user.getId(), (File) postMap.get("photo"), (String) postMap.get("message"));
        			sendResponse(response, MessageResult.createSuccessMessage("post.save.success", user));
        			return;
        		}        		
        	}
        } catch (Exception ex) {
        	sendResponse(response, MessageResult.createErrorMessage("internal.error", null));
        	return;
        }
        sendResponse(response, MessageResult.createErrorMessage("post.save.error", null));
	}
	
	/**
	 * @param userDAO
	 * @param postMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private boolean validadeFormData(Map<String, Object> postMap) throws UnsupportedEncodingException {
		String[] fieldsRequired = new String[] {"message", "email"};
		for (String field : fieldsRequired) {
			if(postMap.get(field) != null && !((String) postMap.get(field)).isEmpty()) {
				
			}else {
				return false;
			}
		}
		
		return true;
	}
	
}
