/**
 * 
 */
package com.pedroalmir.ssnetwork.util;

import java.io.File;
import java.util.List;

import com.pedroalmir.ssnetwork.dao.UserDAO;
import com.pedroalmir.ssnetwork.dao.core.MyEntityManager;
import com.pedroalmir.ssnetwork.model.Post;
import com.pedroalmir.ssnetwork.model.User;
import com.pedroalmir.ssnetwork.service.MyDynamoService;

/**
 * @author Pedro Almir
 *
 */
public class MainApp {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyEntityManager.changeToProduction();
		
		UserDAO userDAO = new UserDAO();
		
		File image = new File("D:\\dev\\github\\pedroalmir\\cloud_study\\aws\\ssnetwork\\src\\main\\resources\\pedroalmir.jpg");
		userDAO.save("Pedro Almir Martins de Oliveira", "pedroalmir", "p.pedroalmir8477@gmail.com", "password", image);
		List<User> users = userDAO.listAll();
		for (User user : users) {
			System.out.println(user);
		}
		
		File postImage = new File("D:\\dev\\github\\pedroalmir\\cloud_study\\aws\\ssnetwork\\src\\main\\resources\\post.png");
		User user = userDAO.findByEmail("p.pedroalmir8477@gmail.com");
		user = userDAO.includePost(user.getId(), postImage, "Hi!");
		
		for(Post p : user.getPosts()) {
			System.out.println(p);
		}
		
		MyDynamoService dynamoService = new MyDynamoService();
		dynamoService.likePost(user.getPosts().get(0), "p.pedroalmir8477@gmail.com");
		dynamoService.unlikePost(user.getPosts().get(0), "p.pedroalmir8477@gmail.com");
		dynamoService.likePost(user.getPosts().get(0), "p.pedroalmir8477@gmail.com");
	}

}
