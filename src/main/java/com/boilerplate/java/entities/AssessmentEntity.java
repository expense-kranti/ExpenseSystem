package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class is provide the information reading the assessment
 * 
 * @author shiva
 *
 */
public class AssessmentEntity extends BaseEntity implements Serializable {

	/**
	 * This is the assessment name
	 */
	private String name;

	/**
	 * This is the max score of assessment
	 */
	private String maxScore;

	/**
	 * This is the sections for the assessment
	 */
	private List<AssessmentSectionEntity> sections;

	/**
	 * This is the assessment status
	 */
	private AssessmentStatus status;

	/**
	 * This method is used to construct this class and set id and status
	 * 
	 * @param id
	 *            this parameter define the id of assessment
	 * @param status
	 *            this parameter define the assessment status
	 */
	public AssessmentEntity(String id, AssessmentStatus status) {
		// Set id
		super.setId(id);
		this.status = status;
	}

	/**
	 * This is a simple constructor
	 */
	public AssessmentEntity() {

	}

	/**
	 * This method is used to get the assessment name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method is used to set the assessment name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * This method is used to get the assessment max score
	 * 
	 * @return the maxScore
	 */
	public String getMaxScore() {
		return maxScore;
	}

	/**
	 * This method is used to set the assessment max scores
	 * 
	 * @param maxScore
	 *            the maxScore to set
	 */
	public void setMaxScore(String maxScore) {
		this.maxScore = maxScore;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		if(this.attemptId==null)
			throw new ValidationFailedException("AssessmentEntity", "Attempt id is null/Empty", null);
		return true;
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
	 * This method is used to get the sections
	 * 
	 * @return the sections
	 */
	public List<AssessmentSectionEntity> getSections() {
		return sections;
	}

	/**
	 * This method is used to set the sections
	 * 
	 * @param sections
	 *            the sections to set
	 */
	public void setSections(List<AssessmentSectionEntity> sections) {
		this.sections = sections;
	}

	private String attemptId;

	/**
	 * @return the attemptId
	 */
	public String getAttemptId() {
		return attemptId;
	}

	/**
	 * @param attemptId
	 *            the attemptId to set
	 */
	public void setAttemptId(String attemptId) {
		this.attemptId = attemptId;
	}

	/**
	 * This method is used to get the assessment status
	 * 
	 * @return the status
	 */
	public AssessmentStatus getStatus() {
		return status;
	}

	/**
	 * This method is used to set the assessment status
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(AssessmentStatus status) {
		this.status = status;
	}

}
