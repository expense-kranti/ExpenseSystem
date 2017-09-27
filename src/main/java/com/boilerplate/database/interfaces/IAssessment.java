package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.AssessmentEntity;

/**
 * This class provide the method for assessment related operations regarding
 * data base
 * 
 * @author shiva
 *
 */
public interface IAssessment {

	/**
	 * This method is used to get the assessment data regarding the assessment
	 * id
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment id
	 * @return the assessment data
	 * @throws BadRequestException
	 *             throw this exception in case of any error while trying to get
	 *             the assessment data regarding the assessment id
	 */
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity) throws BadRequestException;
}
