/**
 * 
 */
package com.pedroalmir.ssnetwork.controller;

import java.io.IOException;

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
@WebServlet(name = "LoginController", urlPatterns = { "/login" })
public class LoginController extends GenericServlet {
    
    /** Serial Version UID */
	private static final long serialVersionUID = -3878303834130506026L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		try {
	    	String email = request.getParameter("email").trim();
	    	String password = request.getParameter("password").trim();
	    	if(email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
	    		MyEntityManager.getManager().clear();
	    		UserDAO userDAO = new UserDAO();
	    		User user = userDAO.findByEmail(email);
	    		
	    		if(user != null && user.getPassword().equals(password)) {
	    			sendResponse(response, MessageResult.createSuccessMessage("user.login.success", user));
	    			return;
	    		}
	    	}
    	}catch(Exception ex) {
    		sendResponse(response, MessageResult.createErrorMessage("internal.error", null));
    		return;
    	}
		sendResponse(response, MessageResult.createErrorMessage("user.login.error", null));
	}
	
}
