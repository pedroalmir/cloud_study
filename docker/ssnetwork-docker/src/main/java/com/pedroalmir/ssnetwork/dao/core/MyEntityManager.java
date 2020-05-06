/**
 * 
 */
package com.pedroalmir.ssnetwork.dao.core;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Pedro Almir
 *
 */
public class MyEntityManager {
	
	private static String context = "development";
	private static EntityManager manager;
	
	/**
	 * @return the manager
	 */
	public static EntityManager getManager() {
		if(manager == null) {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(context);
			manager = emf.createEntityManager();
		}
		return manager;
	}
	
	/**
	 * Change context to production
	 */
	public static void changeToProduction() {
		MyEntityManager.context = "production";
	}
}
