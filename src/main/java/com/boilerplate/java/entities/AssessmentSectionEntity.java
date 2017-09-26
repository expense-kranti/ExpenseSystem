package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class is provide the information reading the assessment section
 * @author shiva
 *
 */
public class AssessmentSectionEntity extends BaseEntity {

	/**
	 * This is the assessment id
	 */
	private AssessmentEntity assessmentId;

	/**
	 * This is the section name
	 */
	private String sectionName;

	/**
	 * This is the order id
	 */
	private String orderId;

	/**
	 * This method is used to get the assessment id
	 * 
	 * @return the assessmentId
	 */
	public AssessmentEntity getAssessmentId() {
		return assessmentId;
	}

	/**
	 * This method is used to set the assessment id
	 * 
	 * @param assessmentId
	 *            the assessmentId to set
	 */
	public void setAssessmentId(AssessmentEntity assessmentId) {
		this.assessmentId = assessmentId;
	}

	/**
	 * This method is used to get the section name
	 * 
	 * @return the sectionName
	 */
	public String getSectionName() {
		return sectionName;
	}

	/**
	 * This method is used to set the section name
	 * 
	 * @param sectionName
	 *            the sectionName to set
	 */
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	/**
	 * This method is used to get the order id
	 * 
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * This method is used to set the order id
	 * 
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @see BaseEntity.ValidationFailedException
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
