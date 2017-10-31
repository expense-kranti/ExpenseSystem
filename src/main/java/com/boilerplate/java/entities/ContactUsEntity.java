package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author mohit
 *
 */
@ApiModel(value="This is the API for Contact us", description="ContactUsEntity", parent=BaseEntity.class)
public class ContactUsEntity extends BaseEntity implements Serializable {
	
	/**
	 * This is the Contact Person Name
	 */
	@ApiModelProperty(value="This is the Contact Person Name")
	private String contactPersonName;
	
	/**
	 * This is the Contact Person Email
	 */
	@ApiModelProperty(value="This is the Contact Person Email")
	private String contactPersonEmail;
	
	/**
	 * This is the Contact Person Mobile Number
	 */
	@ApiModelProperty(value="This is the Contact Person Mobile Number")
	private String contactPersonMobileNumber;
	
	/**
	 * This is the Contact Person Message
	 */
	@ApiModelProperty(value="This is the Contact Person Message")
	private String contactPersonMessage;
	
	/**
	 * This method gets the Contact Person Name
	 * @return The Contact Person Name
	 */
	public String getContactPersonName() {
		return contactPersonName;
	}
	
	/**
	 * This method sets the Contact Person Name
	 * @param contactPersonName The Contact Person Name
	 */
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	
	/**
	 * This method gets the Contact Person Email
	 * @return The Contact Person Email
	 */
	public String getContactPersonEmail() {
		return contactPersonEmail;
	}
	
	/**
	 * This method sets the Contact Person Email
	 * @param contactPersonEmail
	 */
	public void setContactPersonEmail(String contactPersonEmail) {
		this.contactPersonEmail = contactPersonEmail;
	}
	
	/**
	 * This method gets the Person Mobile Number
	 * @return The Person Mobile Number
	 */
	public String getContactPersonMobileNumber() {
		return contactPersonMobileNumber;
	}
	
	/**
	 * This method sets the Person Mobile Number
	 * @param contactPersonMobileNumber The Person Mobile Number
	 */
	public void setContactPersonMobileNumber(String contactPersonMobileNumber) {
		this.contactPersonMobileNumber = contactPersonMobileNumber;
	}
	
	/**
	 * This method gets the Contact Person Message
	 * @return
	 */
	public String getContactPersonMessage() {
		return contactPersonMessage;
	}
	
	/**
	 * This method sets the Contact Person Message
	 * @param contactPersonMessage The Contact Person Message
	 */
	public void setContactPersonMessage(String contactPersonMessage) {
		this.contactPersonMessage = contactPersonMessage;
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
