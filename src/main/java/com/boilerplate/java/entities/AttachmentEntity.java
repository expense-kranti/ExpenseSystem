package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity is used for recognizing files uploaded by user
 * 
 * @author ruchi
 *
 */
public class AttachmentEntity extends BaseEntity {

	/**
	 * This is the default contructor
	 */
	public AttachmentEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AttachmentEntity(String originalFileName, String attachmentId, String contentType) {
		super();
		this.originalFileName = originalFileName;
		this.attachmentId = attachmentId;
		this.contentType = contentType;
	}

	/**
	 * This is the original file name given by user
	 */
	private String originalFileName;

	/**
	 * This is the system generated attachment id
	 */
	private String attachmentId;

	/**
	 * This is the content type of the file attached
	 */

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
	 * This method is used to set original file name
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
	 * This method is used to set attachent id
	 * 
	 * @param attachmentId
	 */
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	/**
	 * This method is used to get content type
	 * 
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * This method is used to set content type
	 * 
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		if (this.getAttachmentId() == null || this.getContentType() == null || this.getOriginalFileName() == null)
			throw new ValidationFailedException("AttachmentEntity", "exceptionValidate", null);
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
