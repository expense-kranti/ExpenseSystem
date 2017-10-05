package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;

import java.util.List;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.MultipleChoiceQuestionEntity;

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
	 * @return the assessment data like assessment id, assessment
	 *         section,assessment questions etc.
	 * @throws BadRequestException
	 *             throw this exception in case of any error while trying to get
	 *             the assessment data regarding the assessment id
	 * @throws NotFoundException
	 *             throw this exception in case of no assessment found for given
	 *             assessment id
	 */
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity)
			throws BadRequestException, NotFoundException;

	/**
	 * This method is used to get the all assessment which is exist in our
	 * system.
	 * 
	 * @return the list of all assessment exist in system and available for user
	 */
	public List<AssessmentEntity> getAssessments();

	/**
	 * This method is used to get the multiple choice question and options
	 * regarding the questionId from the data base
	 * 
	 * @param questionId
	 *            this parameter contains the question id
	 * @throws BadRequestException
	 *             throw this exception in case of any error while trying to get
	 *             the multiple choice question regarding question id
	 */
	public MultipleChoiceQuestionEntity getMultipleChoiceQuestionAndOptions(String questionId)
			throws BadRequestException;
	
	/**
	 * This method is used to get the all survey which is exist in our
	 * system.
	 * 
	 * @return the list of all assessment exist in system and available for user
	 */
	public List<AssessmentEntity> getSurveys();
}
