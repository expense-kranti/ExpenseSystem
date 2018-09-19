package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.Base;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This entity is used for handling mapping of file against its user id and
 * expense id
 * 
 * @author ruchi
 *
 */
public class FileMappingEntity extends BaseEntity {

	/**
	 * This is the parameterized constructor
	 * 
	 * @param fileId
	 * @param userId
	 * @param expenseId
	 * @param isActive
	 * @param expenseHistoryId
	 */
	public FileMappingEntity(String fileId, String userId, String expenseId, boolean isActive, String expenseHistoryId,
			String attachmentId) {
		super();
		this.fileId = fileId;
		this.userId = userId;
		this.expenseId = expenseId;
		this.isActive = isActive;
		this.expenseHistoryId = expenseHistoryId;
		this.attachmentId = attachmentId;
	}

	/**
	 * this is the default constructor
	 */
	public FileMappingEntity() {
		// do nothing constructor
	}

	/**
	 * This is the id of the file generated after saving it in file details
	 * table
	 */
	@ApiModelProperty(value = "This is the file id of the file details", notes = "This is the file id of the file details")
	private String fileId;

	/**
	 * This is the attachment id
	 */
	@ApiModelProperty(value = "This is the attachment id of the file details", notes = "This is the attachment id of the file details")
	private String attachmentId;

	/**
	 * This is the id of user who filed the expense/uploaded file
	 */
	@ApiModelProperty(value = "This is the id of user who filed the expense/uploaded file", notes = "This is the id of user who filed the expense/uploaded file")
	private String userId;

	/**
	 * This is the id of the expense for which file has been uploaded
	 */
	@ApiModelProperty(value = "This is the id of the expense for which file has been uploaded", notes = "This is the id of the expense for which file has been uploaded")
	private String expenseId;

	/**
	 * This indicates whether this is the latest bill attached with the expense
	 */
	@ApiModelProperty(value = "This indicates whether this is the latest bill attached with the expense", notes = "This indicates whether this is the latest bill attached with the expense")
	private boolean isActive;

	/**
	 * This is the id of expense history which is generated if expense is
	 * updated along with file
	 */
	@ApiModelProperty(value = "This is the id of expense history which is generated if expense is updated along with file", notes = "This is the id of expense history which is generated if expense is updated along with file")
	private String expenseHistoryId;

	/**
	 * This method is used to get user id
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * This method is used to set user id
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This method is used to get expense id
	 * 
	 * @return
	 */
	public String getExpenseId() {
		return expenseId;
	}

	/**
	 * This method is used to set expense id
	 * 
	 * @param expenseId
	 */
	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
	}

	/**
	 * This method is used to get expense history id
	 * 
	 * @return
	 */
	public String getExpenseHistoryId() {
		return expenseHistoryId;
	}

	/**
	 * This method is used to set expense history id
	 * 
	 * @param expenseHistoryId
	 */
	public void setExpenseHistoryId(String expenseHistoryId) {
		this.expenseHistoryId = expenseHistoryId;
	}

	/**
	 * This method is used to get is active status
	 * 
	 * @return
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * This method is used to set is active status
	 * 
	 * @param isActive
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * This method is used to get file id
	 * 
	 * @return
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * This method is used to et file id
	 * 
	 * @param fileId
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * This method is usedt to get attachment id
	 * 
	 * @return
	 */
	public String getAttachmentId() {
		return attachmentId;
	}

	/**
	 * This method is used to set attachment id
	 * 
	 * @param attachemntId
	 */
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	/**
	 * @see Base.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see Base.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see Base.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

}
