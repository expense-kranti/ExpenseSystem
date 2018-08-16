package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ExpenseStatusType getStatus() {
		return status;
	}

	public void setStatus(ExpenseStatusType status) {
		this.status = status;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
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
