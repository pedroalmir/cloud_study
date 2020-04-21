/**
 * 
 */
package com.pedroalmir.ssnetwork.service;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.pedroalmir.ssnetwork.model.Post;

/**
 * @author Pedro Almir
 *
 */
public class MyCloudDatastoreService {
	
	private final String TABLE_NAME = "ssnetwork_rlike";
	
	/** Constructor */
	public MyCloudDatastoreService() {
		
	}
	
	public void unlikePost(Post post, String userEmail) {
	}
	
	/**
	 * @param post
	 */
	public void likePost(Post post, String userEmail) {
		
	}
	
	/**
	 * @param postID
	 * @return
	 */
	public Item findPostLike(String postID, String userEmail) {
        return null;
	}
	
	/**
	 * @param postID
	 * @return
	 */
	public Integer findPostLikes(Long postID) {
		return null;
	}
	
}
