package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.AssessmentEntity;

/**
 * This class has the services for assessment related operations
 * 
 * @author shiva
 *
 */
public interface IAssessmentService {

	/**
	 * This method is used to get the assessment data regarding the assessment
	 * id
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment id
	 * @return the assessment data
	 * @throws BadRequestException
	 *             throw this exception in case of any error while trying to get
	 *             the assessment data
	 */
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity) throws BadRequestException;
}
