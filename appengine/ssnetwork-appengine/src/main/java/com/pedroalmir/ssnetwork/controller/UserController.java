/**
 * 
 */
package com.pedroalmir.ssnetwork.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.pedroalmir.ssnetwork.controller.base.GenericServlet;
import com.pedroalmir.ssnetwork.controller.result.MessageResult;
import com.pedroalmir.ssnetwork.dao.UserDAO;
import com.pedroalmir.ssnetwork.dao.core.MyEntityManager;
import com.pedroalmir.ssnetwork.model.Post;
import com.pedroalmir.ssnetwork.model.User;
import com.pedroalmir.ssnetwork.service.MyCloudDatastoreService;

/**
 * @author Pedro Almir
 */
@WebServlet(name = "UserController", urlPatterns = { "/user" })
public class UserController extends GenericServlet {

	/** Serial Version UID */
	private static final long serialVersionUID = -2608338114748189594L;
	
	/** Upload settings */
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	
    	try {
	    	String nickname 	= request.getParameter("nickname");
	    	String loggedUser 	= request.getParameter("loggedUser");
	    	String begin 		= request.getParameter("begin");
	    	String end 			= request.getParameter("end");
	    	if(nickname != null && !nickname.isEmpty() & loggedUser != null && !loggedUser.isEmpty()) {
	    		MyEntityManager.getManager().clear();
	    		
	    		MyCloudDatastoreService cloudDatastoreService = new MyCloudDatastoreService();
	    		UserDAO userDAO = new UserDAO();
	    		User user = userDAO.findByNickname(nickname);
	    		User lUser = userDAO.findByNickname(loggedUser);
	    		
	    		Iterator<Post> iterator = user.getPosts().iterator();
	    		while (iterator.hasNext()) {
					Post p = (Post) iterator.next();
					
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(begin != null && !begin.isEmpty() && end != null && !end.isEmpty()) {
						Date startDate = formatter.parse(begin + " 00:00:00");
						Date endDate = formatter.parse(end + " 23:59:00");
						
						boolean isInsideInterval = (p.getDate().after(startDate) && p.getDate().before(endDate));
						
						/* To debug */
						System.out.println("Filter [from: " + formatter.format(startDate) 
							+ ", to: " + formatter.format(endDate) 
							+ "] - Post Date: " + formatter.format(p.getDate())
							+ " - Result: " + isInsideInterval);
						
						if(!isInsideInterval){
							iterator.remove();
							continue;
						}
					}
					
					Integer likes = cloudDatastoreService.findPostLikes(p.getId());
	    			if(likes != null) {
	    				p.setLikes(likes);
	    				if(lUser != null && !nickname.equals(loggedUser)) {
	    					Item item = cloudDatastoreService.findPostLike(p.getId().toString(), lUser.getEmail());
	    					if(item != null) {
	    						p.setLikedByLoggedUser(true);
	    					}else {
	    						p.setLikedByLoggedUser(false);
	    					}
	    	    		}
	    			}else {
	    				p.setLikes(0);
	    				p.setLikedByLoggedUser(false);
	    			}
				}
	    		
	    		Long count = userDAO.countAll();
	    		if(count != null) user.setTotalUsers(count.intValue());
	    		
	    		sendResponse(response, MessageResult.createSuccessMessage("user.get.success", user));
	    		return;
	    	}
    	}catch(Exception ex) {
    		sendResponse(response, MessageResult.createErrorMessage("internal.error", null));
    		return;
    	}
    	sendResponse(response, MessageResult.createErrorMessage("user.get.error", null));
    }
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        sendResponse(response, MessageResult.createErrorMessage("user.register.error", null));
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        sendResponse(response, MessageResult.createErrorMessage("user.update.error", null));
	}
	
	/**
	 * @param userDAO
	 * @param userMap
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private boolean validadeFormData(UserDAO userDAO, Map<String, Object> userMap) throws UnsupportedEncodingException {
		String[] fieldsRequired = new String[] {"name", "nickname", "email", "password"};
		for (String field : fieldsRequired) {
			if(userMap.get(field) != null && !((String) userMap.get(field)).isEmpty()) {
				
				if(field.equals("nickname")) {
					Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
					Matcher m = p.matcher((String) userMap.get(field));
					if (m.find()) {
						return false;
					}
				}
			}else {
				return false;
			}
		}
		
		try {
			User user = userDAO.findByEmail((String) userMap.get("email"));
			if(user != null) return false;
		} catch(NoResultException nRE) {/* it's ok! */}
		
		try {
			User user = userDAO.findByNickname((String) userMap.get("nickname"));
			if(user != null) return false;
		} catch(NoResultException nRE) {/* it's ok! */}
		
		return true;
	}
	
}
