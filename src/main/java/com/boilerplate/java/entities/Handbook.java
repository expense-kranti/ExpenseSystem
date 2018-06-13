package com.boilerplate.java.entities;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class holds and maintains HandBook related data of user
 * 
 * @author urvij
 *
 */
public class Handbook extends BaseEntity implements Serializable, ICRMPublishDynamicURl, ICRMPublishEntity {

	/**
	 * This is the userId of the user against whom handbook data is in context
	 */
	private String userId;
	/**
	 * This is the category
	 */
	private String category;
	/**
	 * This is categoryType
	 */
	private String categoryType;
	/**
	 * This is employment type selected by user
	 */
	private String employmentType;

	/**
	 * Gets the user id
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id
	 * 
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the category
	 * 
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category
	 * 
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Gets the employment type
	 * 
	 * @return the employmentType
	 */
	public String getEmploymentType() {
		return employmentType;
	}

	/**
	 * Sets the employment type
	 * 
	 * @param employmentType
	 *            the employmentType to set
	 */
	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	/**
	 * Gets the category type
	 * 
	 * @return the categoryType
	 */
	public String getCategoryType() {
		return categoryType;
	}

	/**
	 * Sets the category type
	 * 
	 * @param categoryType
	 *            the categoryType to set
	 */
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	/**
	 * This method checks if required input values should not be null or empty
	 * 
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		if (this.isNullOrEmpty(this.getCategory()))
			throw new ValidationFailedException("MyHandbook", "Category is null or empty", null);
		if (this.isNullOrEmpty(this.getCategoryType()))
			throw new ValidationFailedException("MyHandbook", "CategoryType is null or empty", null);
		return true;
	}

	/**
	 * BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see ICRMPublishEntity.createPublishJSON
	 */
	@Override
	public String createPublishJSON(String template) throws UnauthorizedException {

		String returnValue = template;
		returnValue = returnValue.replace("@userId", this.getUserId());
		returnValue = returnValue.replace("@category", this.getCategory());
		returnValue = returnValue.replace("@categoryType", this.getCategoryType());
		returnValue = returnValue.replace("@employmentType",
				this.getEmploymentType() == null ? "" : this.getEmploymentType());
		return returnValue;
	}

	/**
	 * @see ICRMPublishDynamicURl.createPublishUrl
	 */
	@Override
	public String createPublishUrl(String url) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

}
