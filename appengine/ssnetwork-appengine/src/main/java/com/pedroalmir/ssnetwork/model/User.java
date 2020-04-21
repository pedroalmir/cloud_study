/**
 * 
 */
package com.pedroalmir.ssnetwork.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

/**
 * @author Pedro Almir
 */
@Entity
@Table(name = "user")
@NamedQuery(name = "user.countAll", 		query = "SELECT COUNT(u) FROM User u")
@NamedQuery(name = "user.listAll", 			query = "SELECT u FROM User u")
@NamedQuery(name = "user.findByID", 		query = "SELECT u FROM User u WHERE u.id =: userID")
@NamedQuery(name = "user.findByEmail", 		query = "SELECT u FROM User u WHERE u.email =: email")
@NamedQuery(name = "user.findByNickname", 	query = "SELECT u FROM User u WHERE u.nickname =: nickname")
@NamedQuery(name = "user.findLikeNickname", query = "SELECT u FROM User u WHERE u.nickname like : nickname")
public class User implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 2834590068483564411L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose (serialize = true, deserialize = true)
	private Long id;
	
	@Expose (serialize = true, deserialize = true)
	private String name;
	
	@Expose (serialize = true, deserialize = true)
	private String nickname;
	
	@Expose (serialize = true, deserialize = true)
	private String email;
	
	@Expose (serialize = false, deserialize = true)
	private String password;
	
	@Expose (serialize = true, deserialize = true)
	private String profileImg;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Expose (serialize = true, deserialize = true)
	private Date registrationDate;
	
	@Transient
	@Expose (serialize = true, deserialize = true)
	private Integer totalUsers;
	
	@Expose (serialize = true, deserialize = true)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts;
	
	/** Default constructor */
	public User() {
		this.posts = new ArrayList<>();
		this.registrationDate = new Date();
	}
	
	/**
	 * @param id
	 * @param name
	 * @param nickname
	 * @param email
	 * @param password
	 * @param profileImg
	 * @param posts
	 */
	public User(Long id, String name, String nickname, String email, String password, String profileImg,
			ArrayList<Post> posts) {
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.profileImg = profileImg;
		this.posts = posts;
		this.registrationDate = new Date();
	}

	/**
	 * @param name
	 * @param nickname
	 * @param email
	 * @param password
	 */
	public User(String name, String nickname, String email, String password) {
		super();
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.posts = new ArrayList<>();
		this.registrationDate = new Date();
	}
	
	/**
	 * @param name
	 * @param nickname
	 * @param email
	 * @param password
	 * @param profileImg
	 */
	public User(String name, String nickname, String email, String password, String profileImg) {
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.profileImg = profileImg;
		this.posts = new ArrayList<>();
		this.registrationDate = new Date();
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the profileImg
	 */
	public String getProfileImg() {
		return profileImg;
	}

	/**
	 * @param profileImg the profileImg to set
	 */
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	/**
	 * @return the posts
	 */
	public List<Post> getPosts() {
		return posts;
	}

	/**
	 * @param posts the posts to set
	 */
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	/**
	 * @return the totalUsers
	 */
	public Integer getTotalUsers() {
		return totalUsers;
	}

	/**
	 * @param totalUsers the totalUsers to set
	 */
	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
	}
	
	

	/**
	 * @return the registrationDate
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate the registrationDate to set
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((posts == null) ? 0 : posts.hashCode());
		result = prime * result + ((profileImg == null) ? 0 : profileImg.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (posts == null) {
			if (other.posts != null)
				return false;
		} else if (!posts.equals(other.posts))
			return false;
		if (profileImg == null) {
			if (other.profileImg != null)
				return false;
		} else if (!profileImg.equals(other.profileImg))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", nickname=" + nickname + ", email=" + email + ", password="
				+ password + ", profileImg=" + profileImg + ", registrationDate=" + registrationDate + ", totalUsers="
				+ totalUsers + ", posts=" + posts + "]";
	}
	
}
