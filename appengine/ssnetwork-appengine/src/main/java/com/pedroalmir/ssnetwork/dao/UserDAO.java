/**
 * 
 */
package com.pedroalmir.ssnetwork.dao;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.pedroalmir.ssnetwork.dao.core.AbstractDAO;
import com.pedroalmir.ssnetwork.dao.core.MyEntityManager;
import com.pedroalmir.ssnetwork.model.Post;
import com.pedroalmir.ssnetwork.model.User;
import com.pedroalmir.ssnetwork.service.MyCloudStorageService;

/**
 * @author Pedro Almir
 *
 */
public class UserDAO extends AbstractDAO {
	
	public User includePost(Long userID, File image, String message) {
		String url = MyCloudStorageService.uploadImage(image);
		Post post = new Post(message, url);
		User user = this.findByID(userID);
		user.getPosts().add(post);
		return this.update(user);
	}
	
	/**
	 * @param name
	 * @param nickname
	 * @param email
	 * @param password
	 * @param profileImage
	 */
	public void save(String name, String nickname, String email, String password, File profileImage) {
		String url = ""; // default profile image
		if(profileImage != null) {
			url = MyCloudStorageService.uploadImage(profileImage);
		}
		this.create(name, nickname, email, password, url);
	}
	
	/**
	 * @param userMap
	 */
	public User save(Map<String, Object> userMap) {
		String url = MyCloudStorageService.uploadImage((File) userMap.get("profileImg"));
		return this.create((String) userMap.get("name"), (String) userMap.get("nickname"), 
				(String) userMap.get("email"), (String) userMap.get("password"), url);
	}
	
	/**
	 * @param user
	 */
	public User update(User user) {
		EntityTransaction transaction = this.manager.getTransaction();
		try {
			transaction.begin();
			MyEntityManager.getManager().merge(user);
			transaction.commit();
			return user;
		}catch (Exception e) {
			transaction.rollback();
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	/**
	 * @param name
	 * @param nickname
	 * @param email
	 * @param password
	 * @param profileImage
	 */
	private User create(String name, String nickname, String email, String password, String profileImage) {
		User user = new User(name, nickname, email, password, profileImage);
		EntityTransaction transaction = this.manager.getTransaction();
		try {
			transaction.begin();
			MyEntityManager.getManager().persist(user);
			transaction.commit();
			return user;
		}catch (Exception e) {
			transaction.rollback();
			System.err.println(e.getMessage());
		}
		return null;
	}
	
	/**
	 * @param email
	 * @return user
	 */
	public User findByEmail(String email) throws NoResultException {
		Query namedQuery = this.manager.createNamedQuery("user.findByEmail");
	    namedQuery.setParameter("email", email);
	    return (User) namedQuery.getSingleResult();
	}
	
	/**
	 * @param nickname
	 * @return
	 */
	public User findByNickname (String nickname) throws NoResultException {
		Query namedQuery = this.manager.createNamedQuery("user.findByNickname");
	    namedQuery.setParameter("nickname", nickname);
	    return (User) namedQuery.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findLikeNickname (String nickname) {
		Query namedQuery = this.manager.createNamedQuery("user.findLikeNickname");
	    namedQuery.setParameter("nickname", "%" + nickname  + "%");
	    return (List<User>) namedQuery.getResultList();
	}
	
	/**
	 * @param userID
	 * @return user
	 */
	public User findByID(Long userID) throws NoResultException {
		Query namedQuery = this.manager.createNamedQuery("user.findByID");
	    namedQuery.setParameter("userID", userID);
	    return (User) namedQuery.getSingleResult();
	}
	
	/**
	 * @return all users
	 */
	@SuppressWarnings("unchecked")
	public List<User> listAll(){
		Query namedQuery = this.manager.createNamedQuery("user.listAll");
	    return (List<User>) namedQuery.getResultList();
	}
	
	/**
	 * @return
	 */
	public Long countAll() {
		Query namedQuery = this.manager.createNamedQuery("user.countAll");
	    return (Long) namedQuery.getSingleResult();
	}
}
