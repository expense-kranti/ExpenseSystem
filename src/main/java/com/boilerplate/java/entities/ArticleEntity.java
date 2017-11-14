package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Date;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is provide the information regarding the assessment
 * 
 * @author shiva
 *
 */
public class ArticleEntity extends BaseEntity implements Serializable {

	/**
	 * This is user id.
	 */
	@JsonIgnore
	private String userId;

	/**
	 * This is the article title.
	 */
	private String title;

	/**
	 * This is the article content.
	 */
	private String content;

	/**
	 * This is the status of article is it approved or not.
	 */
	@JsonIgnore
	private Boolean isApproved;

	/**
	 * This is the approved date.
	 */
	@JsonIgnore
	private Date approvedDate;

	/**
	 * This method is used to get the user id.
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * This method is used to set the user id.
	 * 
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This method is used to get the article title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This method is used to set the article title.
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * This method is used to get the article content.
	 * 
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * This method is used to set the article content.
	 * 
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * This method is used to get the approved status of article.
	 * 
	 * @return the isApproved
	 */
	public Boolean getIsApproved() {
		return isApproved;
	}

	/**
	 * This method is used to set the approved status of article.
	 * 
	 * @param isApproved
	 *            the isApproved to set
	 */
	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	/**
	 * This method is used to get the approved Date time.
	 * 
	 * @return the approvedDateTime
	 */
	public Date getApprovedDate() {
		return approvedDate;
	}

	/**
	 * This method is used to set the approved Date time.
	 * 
	 * @param approvedDateTime
	 *            the approvedDateTime to set
	 */
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// Check the title is null if it is null then throw validation failed
		// exception
		if (this.title.equals(null)) {
			throw new ValidationFailedException("ArticleEntity", "Title should not be null", null);
		}
		// Check the content is null if it is null then throw validation failed
		// exception
		else if (this.content.equals(null)) {
			throw new ValidationFailedException("ArticleEntity", "content should not be null", null);
		}
		return true;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}
}
