/**
 * 
 */
package com.pedroalmir.ssnetwork.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.pedroalmir.ssnetwork.dao.core.AbstractDAO;
import com.pedroalmir.ssnetwork.model.Post;

/**
 * @author Pedro Almir
 *
 */
public class PostDAO extends AbstractDAO {
	
	/**
	 * @param postID
	 * @return post
	 */
	public Post findByID(Long postID) throws NoResultException {
		Query namedQuery = this.manager.createNamedQuery("post.findByID");
	    namedQuery.setParameter("pid", postID);
	    return (Post) namedQuery.getSingleResult();
	}
}
