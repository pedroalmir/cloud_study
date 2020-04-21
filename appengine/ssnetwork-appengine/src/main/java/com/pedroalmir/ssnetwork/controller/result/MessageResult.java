/**
 * 
 */
package com.pedroalmir.ssnetwork.controller.result;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.annotations.Expose;

/**
 * @author Pedro Almir
 *
 */
public class MessageResult {
	
	@Expose (serialize = true, deserialize = true)
	private int statusCode;
	
	@Expose (serialize = true, deserialize = true)
	private String message;
	
	@Expose (serialize = true, deserialize = true)
	private Object data;
	
	@Expose (serialize = false, deserialize = false)
	private static Map<String, String> msgMap;
	static {
		msgMap = Stream.of(new String[][] {
			{ "internal.error", "Internal Error!" },
			{ "user.get.success", "Data recovered successfully" },
			{ "user.get.error", "Error getting user data. Try again." },
			{ "user.register.success", "User successfully registered!" }, 
			{ "user.register.error", "Please, you must inform unique e-mail and nickname, in addition to following the other restrictions of each field." },
			{ "user.login.success", "User authenticated successfully!" },
			{ "user.login.error", "Incorrect username or password." },
			{ "post.save.success", "Post saved successfully." },
			{ "post.save.error", "Enter the required fields and try again." },
			{ "user.list.success", "User listed successfully!" },
			{ "user.list.error", "Problems to search user! Please, try again." },
			{ "post.like.success", "Post updated successfully." },
			{ "post.like.error", "Problems to like/unlike the post! Please, try again." },
			{ "user.update.success", "User updated successfully." },
			{ "user.update.error", "Problems to update user! Please, try again." },
		}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
	}
	
	/**
	 * @param key
	 * @return
	 */
	public static String getMessageByKey(String key) {
		return msgMap.get(key);
	}
	
	/**
	 * @param key
	 * @param redirectTo
	 * @return
	 */
	public static MessageResult createErrorMessage(String key, Object data) {
		return new MessageResult(500, MessageResult.getMessageByKey(key), data);
	}
	
	/**
	 * @param key
	 * @param redirectTo
	 * @return
	 */
	public static MessageResult createSuccessMessage(String key, Object data) {
		return new MessageResult(200, MessageResult.getMessageByKey(key), data);
	}
	
	/**
	 * @param statusCode
	 * @param message
	 * @param data
	 */
	public MessageResult(int statusCode, String message, Object data) {
		this.statusCode = statusCode;
		this.message = message;
		this.data = data;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "MessageResult [statusCode=" + statusCode + ", message=" + message + ", data=" + data + "]";
	}
}
