package com.pedroalmir.ssnetwork.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;
import com.pedroalmir.ssnetwork.util.DateUtil;

/**
 * @author Pedro Almir
 *
 */
@Entity
@Table(name = "post")
@NamedQuery(name = "post.findByID", query = "SELECT p FROM Post p WHERE p.id =:pid")
public class Post implements Serializable{

	/** Serial Version UID */
	private static final long serialVersionUID = 1432759484571039033L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose (serialize = true, deserialize = true)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Expose (serialize = true, deserialize = true)
	private Date date;
	
	@Lob 
	@Column(name="message", length = 512, columnDefinition = "text")
	@Expose (serialize = true, deserialize = true)
	private String message;
	
	@Expose (serialize = true, deserialize = true)
	private String imagePath;
	
	@Transient
	@Expose (serialize = true, deserialize = true)
	private Integer likes;
	
	@Transient
	@Expose (serialize = true, deserialize = true)
	private Boolean likedByLoggedUser;
	
	
	/** Default Constructor */
	public Post() {
		
	}

	/**
	 * @param id
	 * @param date
	 * @param message
	 * @param imagePath
	 */
	public Post(Long id, Date date, String message, String imagePath) {
		super();
		this.id = id;
		this.date = date;
		this.message = message;
		this.imagePath = imagePath;
	}
	

	/**
	 * @param message
	 * @param imagePath
	 */
	public Post(String message, String imagePath) {
		this.date = DateUtil.newDateInSaoPaulo();
		this.message = message;
		this.imagePath = imagePath;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
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
	 * @return the likes
	 */
	public Integer getLikes() {
		return likes;
	}

	/**
	 * @param likes the likes to set
	 */
	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	
	/**
	 * @return the likedByLoggedUser
	 */
	public Boolean getLikedByLoggedUser() {
		return likedByLoggedUser;
	}

	/**
	 * @param likedByLoggedUser the likedByLoggedUser to set
	 */
	public void setLikedByLoggedUser(Boolean likedByLoggedUser) {
		this.likedByLoggedUser = likedByLoggedUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imagePath == null) ? 0 : imagePath.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imagePath == null) {
			if (other.imagePath != null)
				return false;
		} else if (!imagePath.equals(other.imagePath))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", date=" + date + ", message=" + message + ", imagePath=" + imagePath + "]";
	}
}
