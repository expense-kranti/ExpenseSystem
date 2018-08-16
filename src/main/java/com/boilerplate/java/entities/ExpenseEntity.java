package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity defines the attributes for expense
 * 
 * @author ruchi
 *
 */
public class ExpenseEntity extends BaseEntity {

	/**
	 * this is the title of the expense
	 */
	private String title;

	/**
	 * This is the description of the expense
	 */
	private String description;

	/**
	 * This is the status of the expense
	 */
	private ExpenseStatusType status;

	/**
	 * this is the attachment id of the bill uploaded with the expense
	 */
	private String attachmentId;

	/**
	 * this is the id of the user by whom expense was filed
	 */
	private String userId;

	/**
	 * This method is used to get title of the expense
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * this method is used to set title of the expense
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * This method is used to get descripion of the expense
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This method is used to set description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * This method is used to get status
	 * 
	 * @return
	 */
	public ExpenseStatusType getStatus() {
		return status;
	}

	/**
	 * This method is used to set status
	 * 
	 * @param status
	 */
	public void setStatus(ExpenseStatusType status) {
		this.status = status;
	}

	/**
	 * This method is used to get attcahment id of the bill/file
	 * 
	 * @return
	 */
	public String getAttachmentId() {
		return attachmentId;
	}

	/**
	 * This method is used to set attchment id of the file
	 * 
	 * @param attachmentId
	 */
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	/**
	 * This method is used to get user id
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * this method is used to set user id
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// check if expense entity has all the mandatory fields
		if (isNullOrEmpty(this.getTitle()) || isNullOrEmpty(this.getDescription()) || isNullOrEmpty(this.attachmentId)
				|| isNullOrEmpty(this.getUserId()))
			throw new ValidationFailedException("ExpenseEntity", "One of the mandatory fields is missing", null);

		return false;
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
