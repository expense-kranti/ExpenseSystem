package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;

import java.util.List;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.MultipleChoiceQuestionEntity;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.java.entities.UserAssessmentDetailEntity;
import com.boilerplate.java.entities.UserMonthlyScoreEntity;

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
	 * This method is used to get the all survey which is exist in our system.
	 * 
	 * @return the list of all assessment exist in system and available for user
	 */
	public List<AssessmentEntity> getSurveys();

	/**
	 * This method is used to get the question explanation
	 * 
	 * @param questionId
	 *            this parameter contains the question Id used to get the
	 *            explanation for this question
	 * @return the explanation regarding the question id
	 */
	public String getQuestionExpanation(String questionId);

	/**
	 * This method is used to save the user assessment data
	 * 
	 * @param userAssessmentDetail
	 *            contains the user assessment details to be saved
	 * @throws Exception
	 *             thrown when exception occurs while saving or updating entity
	 *             in MySQL
	 */
	public void saveUserAssessmentData(UserAssessmentDetailEntity userAssessmentDetail) throws Exception;

	/**
	 * This method is used to save user assessment score into MySQL
	 * 
	 * @param scoreEntity
	 *            it contains userId of the user whose assessment score is to
	 *            save, the assessmentId, the Obtained Score of the attempted
	 *            assessment
	 * @throws Exception
	 *             thrown when exception occurs while saving the entity
	 */
	public void saveUserAssessmentScore(ScoreEntity scoreEntity) throws Exception;

	/**
	 * This method is used to save user monthly score to MYSQL
	 * 
	 * @throws Exception
	 *             thrown when exception occurs in saving entity in MYSQL
	 */
	public void saveUserMonthlyScore(UserMonthlyScoreEntity userMonthlyScoreEntity) throws Exception;
}
