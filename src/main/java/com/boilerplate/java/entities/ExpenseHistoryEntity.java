package com.boilerplate.java.entities;

import java.util.Date;
import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;

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
		// TODO Auto-generated constructor stub
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
			String description, ExpenseStatusType status, String userId) {
		super();
		this.previousId = previousId;
		this.previousCreationDate = previousCreationDate;
		this.previousUpdationDate = previousUpdationDate;
		this.title = title;
		this.description = description;
		this.status = status;
		this.userId = userId;
	}

	/**
	 * This is the id of the expense in expense table
	 */
	private String previousId;

	/**
	 * this is the creation date of the expense
	 */
	private Date previousCreationDate;

	/**
	 * This is the last update date of expense prior to saving this history
	 */
	private Date previousUpdationDate;

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

	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
