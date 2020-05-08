package com.pedroalmir.ssnetwork.controller.handler;
import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.pedroalmir.ssnetwork.dao.UserDAO;
import com.pedroalmir.ssnetwork.dao.core.MyEntityManager;
import com.pedroalmir.ssnetwork.model.User;

/**
 * 
 */

/**
 * @author Pedro Almir
 *
 */
public class SSNetworkStartup extends HttpServlet {

	/** Serial version UID */
	private static final long serialVersionUID = -6606636086933445697L;
	
	@Override
	public void init() throws ServletException {
		String defaultPostText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus iaculis augue nisi, vitae feugiat sem mattis luctus. Ut sodales est eu diam scelerisque consequat.";
		/* Create base data for testing */
		MyEntityManager.changeToProduction();
		UserDAO userDAO = new UserDAO();
		if(userDAO.countAll() <= 0) {
			System.out.println("Initializing data for testing...");
			userDAO.save("Pedro Almir", "pedroalmir", "pedroalmir@gmail.com", "pedroalmir", getImageFile("pedroalmir.jpg"));
			userDAO.save("Carlos Junior", "carlosjunior", "carlosjunior@gmail.com", "carlosjunior", getImageFile("userprofile_1.jpg"));
			userDAO.save("Carolina Abreu", "carolinaabreu", "carolinaabreu@gmail.com", "carolinaabreu", getImageFile("userprofile_2.jpg"));
			userDAO.save("Vitor Hugo", "vitorhugo", "vitorhugo@gmail.com", "vitorhugo", getImageFile("userprofile_3.jpg"));
			userDAO.save("Vitoria Lopes", "vitorialopes", "vitorialopes@gmail.com", "vitorialopes", getImageFile("userprofile_4.jpg"));
			
			User user = userDAO.findByEmail("pedroalmir@gmail.com");
			user = userDAO.includePost(user.getId(), getImageFile("pedroalmir_post_1.jpg"), defaultPostText);
			user = userDAO.includePost(user.getId(), getImageFile("pedroalmir_post_2.jpg"), defaultPostText);
			user = userDAO.includePost(user.getId(), getImageFile("pedroalmir_post_3.jpg"), defaultPostText);
			user = userDAO.includePost(user.getId(), getImageFile("pedroalmir_post_4.jpg"), defaultPostText);
			
			user = userDAO.findByEmail("carolinaabreu@gmail.com");
			user = userDAO.includePost(user.getId(), getImageFile("userprofile_2_post_1.jpg"), defaultPostText);
			user = userDAO.includePost(user.getId(), getImageFile("userprofile_2_post_2.jpg"), defaultPostText);
			user = userDAO.includePost(user.getId(), getImageFile("userprofile_2_post_3.jpg"), defaultPostText);
			user = userDAO.includePost(user.getId(), getImageFile("userprofile_2_post_4.jpg"), defaultPostText);
			user = userDAO.includePost(user.getId(), getImageFile("userprofile_2_post_5.jpg"), defaultPostText);
			
			user = userDAO.findByEmail("vitorhugo@gmail.com");
			user = userDAO.includePost(user.getId(), getImageFile("userprofile_3_post_1.jpg"), defaultPostText);
			user = userDAO.includePost(user.getId(), getImageFile("userprofile_3_post_2.jpg"), defaultPostText);
			user = userDAO.includePost(user.getId(), getImageFile("userprofile_3_post_3.jpg"), defaultPostText);
			
			user = userDAO.findByEmail("vitorialopes@gmail.com");
			user = userDAO.includePost(user.getId(), getImageFile("userprofile_4_post_1.jpg"), defaultPostText);
			user = userDAO.includePost(user.getId(), getImageFile("userprofile_4_post_2.jpg"), defaultPostText);
			user = userDAO.includePost(user.getId(), getImageFile("userprofile_4_post_3.jpg"), defaultPostText);
			System.out.println("Done!");
		}
	}
	
	private static File getImageFile(String name) {
		return new File(SSNetworkStartup.class.getClassLoader().getResource("data-for-test/" + name).getFile());
	}
}
