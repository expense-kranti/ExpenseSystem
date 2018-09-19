package com.boilerplate.java.entities;

import java.util.Date;
import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModelProperty;

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
		// call the super constructor
		super();
	}

	/**
	 * This is the parameterized constructor
	 * 
	 * @param title
	 *            This is the title of the expense
	 * @param description
	 *            This is teh description of the expense
	 * @param status
	 *            This is the status of the expense
	 * @param fileMappings
	 *            This is the list of file mappings
	 * @param userId
	 *            This is the id of user filing the expense
	 * @param userName
	 *            This is the user name
	 */
	public ExpenseEntity(String id, String title, String description, ExpenseStatusType status,
			List<String> attachmentIds, String userId, String userName, String approverComments, float amount,
			Date creationDate, Date updatedDate) {
		super();
		this.setId(id);
		this.title = title;
		this.description = description;
		this.status = status;
		this.attachmentIds = attachmentIds;
		this.userId = userId;
		this.userName = userName;
		this.approverComments = approverComments;
		this.amount = amount;
		this.setCreationDate(creationDate);
		this.setUpdationDate(updatedDate);
	}

	/**
	 * this is the title of the expense
	 */
	@ApiModelProperty(value = "This is the title of the expense", required = true, notes = "This is the title of the expense")
	private String title;

	/**
	 * This is the description of the expense
	 */
	@ApiModelProperty(value = "This is the description of the expense", required = true, notes = "This is the description of the expense")
	private String description;

	/**
	 * This is the status of the expense
	 */
	@ApiModelProperty(value = "This is the status of the expense", required = true, notes = "This is the status of the expense")
	private ExpenseStatusType status;

	/**
	 * this is the list of attachment ids of the bill uploaded with the expense
	 */
	@ApiModelProperty(value = "This is the list of attachments of the expense", required = true, notes = "This is the list of attachments of the expense")
	private List<String> attachmentIds;

	/**
	 * this is the id of the user by whom expense was filed
	 */
	@ApiModelProperty(value = "This is the id of the user who filed this expense", required = true, notes = "This is the id of the user who filed this expense")
	private String userId;

	/**
	 * This is the complete name of the user
	 */
	@ApiModelProperty(value = "This is the name of the user who filed this expense", required = true, notes = "This is the name of the user who filed this expense")
	private String userName;

	/**
	 * This is the reason/comment given by approver on rejection/approval of any
	 * expense
	 */
	@ApiModelProperty(value = "This is the approverComments of the expense", required = true, notes = "This is the approverComments of the expense")
	private String approverComments;

	/**
	 * This is the amount of the expense
	 */
	@ApiModelProperty(value = "This is the amount of the expense", required = true, notes = "This is the amount of the expense")
	private float amount;

	/**
	 * This is the string for enum ExpenseStatusType
	 */
	@ApiModelProperty(value = "This is the string equivalent of the expense status type", required = true, notes = "This is the string equivalent of the expense status type")
	private String statusString;

	/**
	 * This method is used to get amount
	 * 
	 * @return
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * This method is used o set amount
	 * 
	 * @param amount
	 */
	public void setAmount(float amount) {
		this.amount = amount;
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
	 * This method is used to get list of attachment ids
	 * 
	 * @return
	 */
	public List<String> getAttachmentIds() {
		return attachmentIds;
	}

	/**
	 * This method is used to set attachment ids
	 * 
	 * @param attachmentIds
	 */
	public void setAttachmentIds(List<String> attachmentIds) {
		this.attachmentIds = attachmentIds;
	}

	/**
	 * This method is used to get status string
	 * 
	 * @return
	 */
	public String getStatusString() {
		return statusString;
	}

	/**
	 * This method is used to set status string
	 * 
	 * @param statusString
	 */
	public void setStatusString(String statusString) {
		this.statusString = statusString;
		for (ExpenseStatusType status : ExpenseStatusType.values())
			if (statusString.equalsIgnoreCase(String.valueOf(status)))
				this.setStatus(status);
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// check if expense entity has all the mandatory fields
		if (isNullOrEmpty(this.getTitle()) || isNullOrEmpty(this.getDescription()) || this.attachmentIds == null
				|| this.attachmentIds.size() == 0 || this.getAmount() == 0)
			throw new ValidationFailedException("ExpenseEntity", "One of the mandatory fields is missing", null);

		return true;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

}
