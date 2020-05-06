/**
 * 
 */
package com.pedroalmir.ssnetwork.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pedroalmir.ssnetwork.controller.base.GenericServlet;
import com.pedroalmir.ssnetwork.controller.result.MessageResult;
import com.pedroalmir.ssnetwork.dao.PostDAO;
import com.pedroalmir.ssnetwork.dao.core.MyEntityManager;
import com.pedroalmir.ssnetwork.model.Post;
import com.pedroalmir.ssnetwork.service.MyDynamoService;

/**
 * @author Pedro Almir
 */
public class LikePostController extends GenericServlet {

	/** Serial Version UID */
	private static final long serialVersionUID = -2608338114748189594L;
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
	    	String email 	= request.getParameter("email").trim();
	    	Long postID 	= Long.valueOf(request.getParameter("post").trim());
	    	Boolean isLike 	= Boolean.valueOf(request.getParameter("isLike").trim());
	    	
	    	if(email != null && !email.isEmpty() && postID != null && isLike != null) {
	    		MyEntityManager.getManager().clear();
	    		PostDAO postDAO = new PostDAO();
	    		Post post = postDAO.findByID(postID);
	    		if(post != null) {
	    			MyDynamoService dynamoService = new MyDynamoService();
	    			if(isLike) {
	    				dynamoService.likePost(post, email);
	    			}else {
	    				dynamoService.unlikePost(post, email);
	    			}
	    			sendResponse(response, MessageResult.createSuccessMessage("post.like.success", null));
	    			return;
	    		}
	    	}
    	}catch(Exception ex) {
    		sendResponse(response, MessageResult.createErrorMessage("internal.error", null));
    		return;
    	}
    	
    	sendResponse(response, MessageResult.createErrorMessage("post.like.error", null));
	}
}
