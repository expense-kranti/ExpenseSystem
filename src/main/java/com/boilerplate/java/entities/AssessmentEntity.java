package com.boilerplate.java.entities;

import java.util.List;

import javax.persistence.JoinColumn;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class is provide the information reading the assessment
 * 
 * @author shiva
 *
 */
public class AssessmentEntity extends BaseEntity {

	/**
	 * This is the assessment name
	 */
	private String name;

	/**
	 * This is the max score of assessment
	 */
	private String maxScore;

	/**
	 * This is the section for the assessment
	 */
	@JoinColumn(name = "id", insertable=false, updatable=false)
	private List<AssessmentSectionEntity> sections;

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

}
