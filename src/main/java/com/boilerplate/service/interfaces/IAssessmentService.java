package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import java.util.List;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;

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

	/**
	 * This method is used to get the assessments
	 * 
	 * @return the list of assessment
	 */
	public List<AssessmentEntity> getAssessments();

	/**
	 * This method is used to get the details about the user attempt assessments
	 * 
	 * @return the attempt assessment details
	 * @throws NotFoundException
	 *             throw this exception in case of there is no attempt
	 *             assessment found for the current user
	 */
	public AttemptAssessmentListEntity getAssessmentAttempt() throws NotFoundException;

	/**
	 * This method is used to save the assessment data
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which is we need
	 *            to save to data store
	 * @throws ValidationFailedException
	 *             throw this exception in case of any validation fail regarding
	 *             the assessment data
	 */
	public void saveAssesment(AssessmentEntity assessmentEntity) throws ValidationFailedException;

	/**
	 * This method is used to save the assessment data to data store after
	 * change the assessment status to submit.
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which is we need
	 *            to save to data store
	 * @throws ValidationFailedException
	 *             throw this exception in case of any validation fail regarding
	 *             the assessment data
	 */
	public void submitAssesment(AssessmentEntity assessmentEntity) throws ValidationFailedException;
}
