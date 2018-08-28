package com.boilerplate.java.entities;

import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity defines the attributes for expense
 * 
 * @author ruchi
 *
 */
public class ExpenseEntity extends BaseEntity {

	/**
	 * This is the default constructor
	 */
	public ExpenseEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @param description
	 * @param status
	 * @param attachments
	 * @param userId
	 * @param userName
	 */
	public ExpenseEntity(String id, String title, String description, ExpenseStatusType status,
			List<AttachmentEntity> attachments, String userId, String userName) {
		super();
		this.setId(id);
		this.title = title;
		this.description = description;
		this.status = status;
		this.attachments = attachments;
		this.userId = userId;
		this.userName = userName;
	}

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
	 * this is the list of attachment ids of the bill uploaded with the expense
	 */
	private List<AttachmentEntity> attachments;

	/**
	 * this is the id of the user by whom expense was filed
	 */
	private String userId;

	/**
	 * This is the complete name of the user
	 */
	private String userName;

	/**
	 * This is the reason/comment given by approver on rejection/approval of any
	 * expense
	 */
	private String approverComments;

	/**
	 * This method is used to get list of attachments *
	 * 
	 * @return
	 */
	public List<AttachmentEntity> getAttachments() {
		return attachments;
	}

	/**
	 * This method is used to set list of attchments ids
	 * 
	 * @param attachmentIds
	 */
	public void setAttachments(List<AttachmentEntity> attachments) {
		this.attachments = attachments;
	}

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
	 * This method is used to get user name
	 * 
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * This method is used to set user name
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * This method is used to get approver comments
	 * 
	 * @return
	 */
	public String getApproverComments() {
		return approverComments;
	}

	/**
	 * This method is used to set approver comments
	 * 
	 * @param approverComments
	 */
	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// check if expense entity has all the mandatory fields
		if (isNullOrEmpty(this.getTitle()) || isNullOrEmpty(this.getDescription()) || this.attachments.size() == 0
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
