package com.boilerplate.java.entities;

import java.util.Date;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * this class extends the expense entity and defines some extra parameters which
 * are required for saving expense history
 * 
 * @author ruchi
 *
 */
public class ExpenseHistoryEntity extends BaseEntity {

	/**
	 * This is the default constructor
	 */
	public ExpenseHistoryEntity() {
		super();
	}

	/**
	 * this is the parameterized constructor
	 * 
	 * @param previousId
	 * @param previousCreationDate
	 * @param previousUpdationDate
	 * @param title
	 * @param description
	 * @param status
	 * @param attachmentIds
	 * @param userId
	 */
	public ExpenseHistoryEntity(String previousId, Date previousCreationDate, Date previousUpdationDate, String title,
			String description, ExpenseStatusType status, String userId, String approverComments, float amount) {
		super();
		this.previousId = previousId;
		this.previousCreationDate = previousCreationDate;
		this.previousUpdationDate = previousUpdationDate;
		this.title = title;
		this.description = description;
		this.status = status;
		this.userId = userId;
		this.approverComments = approverComments;
		this.amount = amount;
	}

	/**
	 * This is the id of the expense in expense table
	 */
	@ApiModelProperty(value = "This is the previousId of the expense", required = true, notes = "This is the id from the expense table")
	private String previousId;

	/**
	 * this is the creation date of the expense
	 */
	@ApiModelProperty(value = "This is the original creation date of the expense", required = true, notes = "This is the original creation date of the expense")
	private Date previousCreationDate;

	/**
	 * This is the last update date of expense prior to saving this history
	 */
	@ApiModelProperty(value = "This is the original update date of the expense", required = true, notes = "This is the original update date of the expense")
	private Date previousUpdationDate;

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
	 * this is the id of the user by whom expense was filed
	 */
	@ApiModelProperty(value = "This is the userId of the expense", required = true, notes = "This is the userId of the expense")
	private String userId;

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
	 * This method is used to get previous id
	 * 
	 * @return
	 */
	public String getPreviousId() {
		return previousId;
	}

	/**
	 * This method is used to set previous id
	 * 
	 * @param previousId
	 */
	public void setPreviousId(String previousId) {
		this.previousId = previousId;
	}

	/**
	 * this method is used to get previousCreationDate
	 * 
	 * @return
	 */
	public Date getPreviousCreationDate() {
		return previousCreationDate;
	}

	/**
	 * This method is used to set previousCreationDate
	 * 
	 * @param previousCreationDate
	 */
	public void setPreviousCreationDate(Date previousCreationDate) {
		this.previousCreationDate = previousCreationDate;
	}

	/**
	 * This method is used to get previousUpdationDate
	 * 
	 * @return
	 */
	public Date getPreviousUpdationDate() {
		return previousUpdationDate;
	}

	/**
	 * This method is used to set previousUpdationDate
	 * 
	 * @param previousUpdationDate
	 */
	public void setPreviousUpdationDate(Date previousUpdationDate) {
		this.previousUpdationDate = previousUpdationDate;
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
