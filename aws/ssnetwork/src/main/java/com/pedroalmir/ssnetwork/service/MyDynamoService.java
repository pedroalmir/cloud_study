/**
 * 
 */
package com.pedroalmir.ssnetwork.service;

import java.io.IOException;
import java.util.Iterator;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.pedroalmir.ssnetwork.model.Post;
import com.pedroalmir.ssnetwork.util.AwsUtils;

/**
 * @author Pedro Almir
 *
 */
public class MyDynamoService {
	
	private DynamoDB dynamoDB;
	private final String TABLE_NAME = "ssnetwork_rlike";
	
	/** Constructor */
	public MyDynamoService() {
		BasicAWSCredentials awsCreds;
		try {
			awsCreds = AwsUtils.getCredentials();
			
			AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
					.withRegion(Regions.SA_EAST_1)
					.build();
			
			this.dynamoDB = new DynamoDB(client);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void unlikePost(Post post, String userEmail) {
		Item result = findPostLike(post.getId().toString(), userEmail);
		Table table = dynamoDB.getTable(TABLE_NAME);
		if(result != null) {
			DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
		            .withPrimaryKey(new PrimaryKey("postID", post.getId().toString(), "userEmail", userEmail));
			try {
	            table.deleteItem(deleteItemSpec);
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}
	
	/**
	 * @param post
	 */
	public void likePost(Post post, String userEmail) {
		Item result = findPostLike(post.getId().toString(), userEmail);
		Table table = dynamoDB.getTable(TABLE_NAME);
		if(result == null) {
			try {
				table.putItem(new Item().withPrimaryKey("postID", post.getId().toString(), "userEmail", userEmail));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param postID
	 * @return
	 */
	public Item findPostLike(String postID, String userEmail) {
		Table table = dynamoDB.getTable(TABLE_NAME);
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("postID", postID, "userEmail", userEmail);

        try {
            return table.getItem(spec);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return null;
	}
	
	/**
	 * @param postID
	 * @return
	 */
	public Integer findPostLikes(Long postID) {
		Table table = dynamoDB.getTable(TABLE_NAME);
		
        QuerySpec querySpec = new QuerySpec()
        		.withKeyConditionExpression("postID = :pid")
                .withValueMap(new ValueMap().withString(":pid", postID.toString()));
        
        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        
        try {
        	int rating = 0;
        	
            items = table.query(querySpec);
            iterator = items.iterator();
            
            while (iterator.hasNext()) {
                iterator.next();
                rating++;
            }
            
            return rating;
        }catch (Exception e) {
            e.printStackTrace();
        }
		
		return null;
	}
	
}
