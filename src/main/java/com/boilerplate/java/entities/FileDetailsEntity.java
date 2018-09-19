package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.Base;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This class defines the attributes for files being uploaded
 * 
 * @author ruchi
 *
 */
public class FileDetailsEntity extends BaseEntity {
	/**
	 * this is the default constructor
	 */
	public FileDetailsEntity() {
		super();
		// do nothing constructor
	}

	/**
	 * This is the parameterized constructor
	 * 
	 * @param originalFileName
	 * @param attachmentId
	 * @param userId
	 * @param contentType
	 */
	public FileDetailsEntity(String originalFileName, String attachmentId, String userId, String contentType) {
		super();
		this.originalFileName = originalFileName;
		this.attachmentId = attachmentId;
		this.userId = userId;
		this.contentType = contentType;
	}

	/**
	 * This is the original file name given by user
	 */
	@ApiModelProperty(value = "This is the original file name given by user", notes = "This is the original file name given by user")
	private String originalFileName;
	/**
	 * This is the id of the foe generated after uploading it
	 */
	@ApiModelProperty(value = "his is the id of the foe generated after uploading it", notes = "his is the id of the foe generated after uploading it")
	private String attachmentId;

	/**
	 * This is the id of user who filed the expense/uploaded file
	 */
	@ApiModelProperty(value = "This is the id of user who filed the expense/uploaded file", notes = "This is the id of user who filed the expense/uploaded file")
	private String userId;

	/**
	 * This is the content type of the file
	 */
	@ApiModelProperty(value = "This is the content type of the file", notes = "This is the content type of the file")
	private String contentType;

	/**
	 * This method is used to get original file name
	 * 
	 * @return
	 */
	public String getOriginalFileName() {
		return originalFileName;
	}

	/**
	 * This method is used to set orginal file name
	 * 
	 * @param originalFileName
	 */
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

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
	 * @param originalFileName
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
	 * THis method is used to get content type
	 * 
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * THis method is used to set cobtet type
	 * 
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
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
