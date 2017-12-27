package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="This is a voucher"
, description="A voucher", parent=BaseEntity.class)
public class Voucher extends BaseEntity implements Serializable{

	/**
	 * This is the voucher code
	 */
	@ApiModelProperty(value="The voucher code")
	private String voucherCode;
	
	/**
	 * This is the user id to which the voucher code was assigned
	 */
	@ApiModelProperty(value="The user id this voucher is assigned to")
	private String assignedTo;
	
	/**
	 * This is the session in which the code was assigned
	 */
	@ApiModelProperty(value="The session in which this voucher was assigned")
	private String sessionId;

	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return this;
	}

	/**
	 * Gets the voucher code
	 * @return The voucher code
	 */
	public String getVoucherCode() {
		return voucherCode;
	}

	/**
	 * Sets the voucher code
	 * @param voucherCode The voucher code
	 */
	public void setVoucherCode(String voucherCode) {
		this.voucherCode = voucherCode;
	}

	/**
	 * Gets assigned to
	 * @return The user id assigned to the voucher
	 */
	public String getAssignedTo() {
		return assignedTo;
	}

	/**
	 * Sets assigned to 
	 * @param assignedTo The user id to which the voucher was assigned
	 */
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	/**
	 * Gets the session id in which the voucher was used
	 * @return The session id
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Sets the session id in which the voucher was assigned
	 * @param sessionId The session id
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getHitId() {
		return hitId;
	}

	public void setHitId(String hitId) {
		this.hitId = hitId;
	}

	/**
	 * The hit id for the user
	 */
	private String hitId;
	
	/**
	 * The expiry date of voucher
	 */
	@ApiModelProperty(value="The voucher code")
	private java.util.Date expiryDate;

	public java.util.Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(java.util.Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
}
