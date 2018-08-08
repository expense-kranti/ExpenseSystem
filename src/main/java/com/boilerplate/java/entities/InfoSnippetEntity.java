package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This is the information snippet entity, a sub module will contain a list of
 * information snippet
 * 
 * @author ruchi
 *
 */
public class InfoSnippetEntity extends BaseEntity {

	/**
	 * This is the id of the sub module to which this info snippet belongs
	 */
	private String subModuleId;

	/**
	 * This is the heading of the information snippet
	 */
	private String heading;

	/**
	 * This is the text of the information snippet
	 */
	private String text;

	/**
	 * This is the order id
	 */
	private int orderId;

	/**
	 * This indicates whether this snippet is active or not
	 */
	private boolean isActive;

	/**
	 * This method is used to get the sub module id
	 * 
	 * @return
	 */
	public String getSubModuleId() {
		return subModuleId;
	}

	/**
	 * This is used to set the sub module id
	 * 
	 * @param subModuleId
	 */
	public void setSubModuleId(String subModuleId) {
		this.subModuleId = subModuleId;
	}

	/**
	 * This method is used to get the heading
	 * 
	 * @return
	 */
	public String getHeading() {
		return heading;
	}

	/**
	 * This method is used to set the heading
	 * 
	 * @return
	 */
	public void setHeading(String heading) {
		this.heading = heading;
	}

	/**
	 * This method is used to get the text
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * This method is used to set the text
	 * 
	 * @return
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * this method is used to get the order id
	 * 
	 * @return
	 */
	public int getOrderId() {
		return orderId;
	}

	/**
	 * this method is used to set the order id
	 * 
	 * @return
	 */
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	/**
	 * This method is used to get isActive
	 * 
	 * @return
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * This method is used to set isActive
	 * 
	 * @return
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
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
