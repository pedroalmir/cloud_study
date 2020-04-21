/**
 * 
 */
package com.pedroalmir.ssnetwork.dao.core;

import javax.persistence.EntityManager;

/**
 * @author Pedro Almir
 *
 */
public abstract class AbstractDAO {
	
	protected EntityManager manager;
	
	/**
	 * Default constructor
	 */
	public AbstractDAO() {
		this.manager = MyEntityManager.getManager();
	}
}
