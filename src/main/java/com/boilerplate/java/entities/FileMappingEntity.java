package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity is used for handling mapping of file against its user id and
 * expense id
 * 
 * @author ruchi
 *
 */
public class FileMappingEntity extends BaseEntity {

	/**
	 * this is the default constructor
	 */
	public FileMappingEntity() {
		// do nothing constructor
	}

	/**
	 * this is the parameterized constructor
	 */
	public FileMappingEntity(String attachmentId, String userId, String expenseId, boolean isActive,
			String expenseHistoryId, String fileName) {
		super();
		this.attachmentId = attachmentId;
		this.userId = userId;
		this.expenseId = expenseId;
		this.isActive = isActive;
		this.expenseHistoryId = expenseHistoryId;
		this.originalFileName = fileName;
	}

	/**
	 * This is the original file name given by user
	 */
	private String originalFileName;
	/**
	 * This is the id of the foe generated after uploading it
	 */
	private String attachmentId;

	/**
	 * This is the id of user who filed the expense/uploaded file
	 */
	private String userId;

	/**
	 * This is the id of the expense for which file has been uploaded
	 */
	private String expenseId;

	/**
	 * This indicates whether this is the latest bill attached with the expense
	 */
	private boolean isActive;

	/**
	 * This is the id of expense history which is generated if expense is
	 * updated along with file
	 */
	private String expenseHistoryId;

	/**
	 * This method is used to get attachment id
	 * 
	 * @return
	 */
	public String getAttachmentId() {
		return attachmentId;
	}

	/**
	 * This method is used to set attachment id
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
	 * This method is used to get original file name
	 * 
	 * @return
	 */
	public String getOriginalFileName() {
		return originalFileName;
	}

	/**
	 * This method is used to set original file name
	 * 
	 * @param originalFileName
	 */
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
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
