/**
 * 
 */
package com.pedroalmir.ssnetwork.dao.core;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.google.appengine.api.utils.SystemProperty;

/**
 * @author Pedro Almir
 *
 */
public class MyEntityManager {
	
	private static EntityManager manager;
	private static final Logger log = Logger.getLogger(MyEntityManager.class.getName());
	
	/**
	 * @return the manager
	 */
	public static EntityManager getManager() {
		if(manager == null) {
			log.info("Initializing get manager...");
			
			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
				Map<String, String> properties = new HashMap<>();
				properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.GoogleDriver");
				properties.put("javax.persistence.jdbc.url", "jdbc:google:mysql://ssnetwork:southamerica-east1:ssnetwork-csql/ssnetworkdb?user=ssnet_dbuser");
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("test", properties);
				manager = emf.createEntityManager();
			}else {
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("development");
				manager = emf.createEntityManager();
			}
			
			log.info("Manager created...");
		}
		
		return manager;
	}
	
}
