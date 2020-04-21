/**
 * 
 */
package com.pedroalmir.ssnetwork.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pedroalmir.ssnetwork.controller.base.GenericServlet;
import com.pedroalmir.ssnetwork.controller.result.MessageResult;
import com.pedroalmir.ssnetwork.dao.UserDAO;
import com.pedroalmir.ssnetwork.dao.core.MyEntityManager;
import com.pedroalmir.ssnetwork.model.User;

/**
 * @author Pedro Almir
 */
@WebServlet(name = "FindUserController", urlPatterns = { "/find/user" })
public class FindUserController extends GenericServlet {

	/** Serial Version UID */
	private static final long serialVersionUID = -2608338114748189594L;
	
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	
    	try {
	    	String nickname = request.getParameter("nickname").trim();
	    	if(nickname != null && !nickname.isEmpty()) {
	    		MyEntityManager.getManager().clear();
	    		UserDAO userDAO = new UserDAO();
	    		List<User> users = userDAO.findLikeNickname(nickname);
	    		if(users != null) {
	    			sendResponse(response, MessageResult.createSuccessMessage("user.list.success", users));
	    			return;
	    		}
	    	}
    	}catch(Exception ex) {
    		sendResponse(response, MessageResult.createErrorMessage("internal.error", null));
    		return;
    	}
    	
    	sendResponse(response, MessageResult.createErrorMessage("user.list.error", null));
    }
	
}
