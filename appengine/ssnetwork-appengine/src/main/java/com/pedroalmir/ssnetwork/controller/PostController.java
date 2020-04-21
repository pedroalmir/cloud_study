/**
 * 
 */
package com.pedroalmir.ssnetwork.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import com.pedroalmir.ssnetwork.controller.base.GenericServlet;
import com.pedroalmir.ssnetwork.controller.result.MessageResult;
import com.pedroalmir.ssnetwork.dao.UserDAO;
import com.pedroalmir.ssnetwork.dao.core.MyEntityManager;
import com.pedroalmir.ssnetwork.model.User;

/**
 * @author Pedro Almir
 */
@WebServlet(name = "PostController", urlPatterns = { "/post" })
public class PostController extends GenericServlet {

	/** Serial Version UID */
	private static final long serialVersionUID = -2608338114748189594L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			
			if (!ServletFileUpload.isMultipartContent(request)) {
				sendResponse(response, MessageResult.createErrorMessage("internal.error", null));
				return;
			}
        
        	// Map values to create post
        	Map<String, Object> postMap = new LinkedHashMap<>();
        	
        	ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = upload.getItemIterator(request);
			
			while (iter.hasNext()) {
			    FileItemStream item = iter.next();
			    String name = item.getFieldName();
			    InputStream stream = item.openStream();
	        	
			    if (item.isFormField()) {
			    	postMap.put(name, new String(Streams.asString(stream).getBytes(), "UTF-8"));
			    } else {
			        // Image here.
			    	postMap.put("postImgName", item.getName());
			    	postMap.put(item.getFieldName(), IOUtils.toByteArray(stream));
			    }
			}
        	
        	MyEntityManager.getManager().clear();
        	UserDAO userDAO = new UserDAO();
        	
        	// Validate form data
        	if(validadeFormData(postMap)) {
        		User user = userDAO.findByEmail((String) postMap.get("email"));
        		if(user != null) {
        			user = userDAO.includePost(user.getId(), (String) postMap.get("postImgName"), 
        					(byte[]) postMap.get("photo"), (String) postMap.get("message"));
        			sendResponse(response, MessageResult.createSuccessMessage("post.save.success", user));
        			return;
        		}        		
        	}
        } catch (Exception ex) {
        	ex.printStackTrace();
        	sendResponse(response, MessageResult.createErrorMessage("internal.error", ex.getMessage()));
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
