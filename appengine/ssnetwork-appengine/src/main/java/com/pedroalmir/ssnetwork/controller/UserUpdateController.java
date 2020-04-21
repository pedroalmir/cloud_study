/**
 * 
 */
package com.pedroalmir.ssnetwork.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import com.pedroalmir.ssnetwork.controller.base.GenericServlet;
import com.pedroalmir.ssnetwork.controller.result.MessageResult;
import com.pedroalmir.ssnetwork.dao.UserDAO;
import com.pedroalmir.ssnetwork.dao.core.MyEntityManager;
import com.pedroalmir.ssnetwork.model.User;
import com.pedroalmir.ssnetwork.service.MyCloudStorageService;

/**
 * @author Pedro Almir
 */
@WebServlet(name = "UserUpdateController", urlPatterns = { "/user/update" })
public class UserUpdateController extends GenericServlet {

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
			
			// Map values to create user
			Map<String, Object> userMap = new LinkedHashMap<>();
			
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = upload.getItemIterator(request);
			
			while (iter.hasNext()) {
			    FileItemStream item = iter.next();
			    String name = item.getFieldName();
			    InputStream stream = item.openStream();
	        	
			    if (item.isFormField()) {
			    	userMap.put(name, new String(Streams.asString(stream).getBytes(), "UTF-8"));
			    } else {
			    	// Image here.
			    	userMap.put("profileImgName", item.getName());
                    userMap.put(item.getFieldName(), IOUtils.toByteArray(stream));
			    }
			}
			
			if(userMap.get("email") != null && !((String) userMap.get("email")).isEmpty()) {
        		String email = (String) userMap.get("email");
        		
        		MyEntityManager.getManager().clear();
        		UserDAO userDAO = new UserDAO();
        		User user = userDAO.findByEmail(email);
        		if(user != null) {
        			if(userMap.get("name") != null && !((String) userMap.get("name")).isEmpty()) {
        				user.setName((String)userMap.get("name"));
        			}
        			if(userMap.get("password") != null && !((String) userMap.get("password")).isEmpty()) {
        				user.setPassword((String)userMap.get("password"));
        			}
        			if(userMap.get("profileImgName") != null && ((String) userMap.get("profileImgName")).length() > 0 
        					&& userMap.get("profileImg") != null && ((byte[]) userMap.get("profileImg")).length > 0) {
        				String url = MyCloudStorageService.uploadImage((String) userMap.get("profileImgName"), (byte[]) userMap.get("profileImg"));
        				user.setProfileImg(url);
        			}
        			userDAO.update(user);
        			sendResponse(response, MessageResult.createSuccessMessage("user.update.success", user));
        			return;
        		}
        	}
			
		} catch (FileUploadException | IOException e) {
			e.printStackTrace();
			sendResponse(response, MessageResult.createErrorMessage("internal.error", e.getMessage()));
        	return;
		}
		sendResponse(response, MessageResult.createErrorMessage("user.update.error", null));
	}
}
