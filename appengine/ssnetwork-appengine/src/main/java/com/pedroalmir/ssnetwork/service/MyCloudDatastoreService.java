/**
 * 
 */
package com.pedroalmir.ssnetwork.service;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.pedroalmir.ssnetwork.model.Post;

/**
 * @author Pedro Almir
 *
 */
public class MyCloudDatastoreService {
	
	private final String KIND = "ssnet-likes";
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private final KeyFactory keyFactory = datastore.newKeyFactory().setKind(KIND);
	
	public void unlikePost(Post post, String userEmail) {
		Entity entity = findPostLike(post.getId().toString(), userEmail);
		if(entity != null) {
			datastore.delete(entity.getKey());
		}
	}
	
	/**
	 * @param post
	 */
	public void likePost(Post post, String userEmail) {
		Entity entity = findPostLike(post.getId().toString(), userEmail);
		if(entity == null) {
			Key key = datastore.allocateId(keyFactory.newKey());
			Entity like = Entity.newBuilder(key)
					.set("postID", 	  StringValue.newBuilder(post.getId().toString()).build())
					.set("userEmail", StringValue.newBuilder(userEmail).build())
					.build();
			datastore.put(like);
		}
	}
	
	/**
	 * @param postID
	 * @return
	 */
	public Entity findPostLike(String postID, String userEmail) {
		Query<Entity> query = Query.newEntityQueryBuilder()
		    .setKind(KIND)
		    .setFilter(CompositeFilter.and(PropertyFilter.eq("postID", postID), PropertyFilter.eq("userEmail", userEmail)))
			.build();
		
		QueryResults<Entity> results = datastore.run(query);
		if(results != null && results.hasNext()) {
			return results.next();
		}
		
		return null;
	}
	
	/**
	 * @param postID
	 * @return
	 */
	public Integer findPostLikes(Long postID) {
		Query<Entity> query = Query.newEntityQueryBuilder()
		    .setKind(KIND)
		    .setFilter(PropertyFilter.eq("postID", postID.toString()))
			.build();
		
		QueryResults<Entity> results = datastore.run(query);
		if(results != null && results.hasNext()) {
			int rating = 0;
			while (results.hasNext()) {
				results.next();
				rating++;
			}
			
			return rating;
		}
		return null;
	}
	
}
