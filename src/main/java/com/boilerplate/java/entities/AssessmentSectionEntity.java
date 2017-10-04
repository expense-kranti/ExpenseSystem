package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class is provide the information reading the assessment section
 * 
 * @author shiva
 *
 */
public class AssessmentSectionEntity extends BaseEntity implements Serializable {

	/**
	 * This is the assessment id
	 */
	@JsonIgnore
	private AssessmentEntity assessment;

	/**
	 * This is the section name
	 */
	private String sectionName;

	/**
	 * This is the order id
	 */
	private String orderId;

	/**
	 * This is the sections for the assessment
	 */
	private List<AssessmentQuestionSectionEntity> questions;

	/**
	 * This method is used to get the assessment
	 * 
	 * @return the assessment
	 */
	public AssessmentEntity getAssessment() {
		return assessment;
	}

	/**
	 * This method is used to set the assessment
	 * 
	 * @param assessment
	 *            the assessment to set
	 */
	public void setAssessment(AssessmentEntity assessment) {
		this.assessment = assessment;
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

	/**
	 * This is method is used to get the questions
	 * 
	 * @return the questions
	 */
	public List<AssessmentQuestionSectionEntity> getQuestions() {
		return questions;
	}

	/**
	 * This is method is used to set the questions
	 * 
	 * @param questions
	 *            the questions to set
	 */
	public void setQuestions(List<AssessmentQuestionSectionEntity> questions) {
		this.questions = questions;
	}
}
