package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import java.util.List;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentQuestionSectionEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.java.entities.QuestionEntity;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.java.entities.TopScorerEntity;

/**
 * This class has the services for assessment related operations
 * 
 * @author shiva
 *
 */
public interface IAssessmentService {

	/**
	 * This method is used to get the assessment data regarding the assessment
	 * id from the data store
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment details like assessment
	 *            id
	 * @return the assessment data like assessment id, assessment
	 *         section,assessment questions etc.
	 * @throws NotFoundException
	 *             throw this exception in case of no assessment found for given
	 *             assessment id
	 * @throws BadRequestException
	 *             throw this exception in case of any error while trying to get
	 *             the assessment data
	 * @throws ValidationFailedException
	 *             throw this exception in case of any error while trying to get
	 *             any assessment which is already submitted by user
	 */
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity)
			throws BadRequestException, NotFoundException, ValidationFailedException;

	/**
	 * This method is used to get the all assessment which is exist in our
	 * system.
	 * 
	 * @return the list of all assessment exist in system and available for user
	 */
	public List<AssessmentEntity> getAssessments();

	/**
	 * This method is used to get the user attempted assessment details means
	 * all those assessment which is attempted by user in past
	 * 
	 * @return the user attempted assessment details means all those assessment
	 *         which was attempt by user in past
	 * @throws NotFoundException
	 *             throw this exception if user has never attempt any assessment
	 *             before
	 */
	public AttemptAssessmentListEntity getAssessmentAttempt() throws NotFoundException;

	/**
	 * This method is used to save the assessment data to data store ,assessment
	 * data like assessment id, assessment section,assessment questions etc.
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which we need to
	 *            save to data store ,assessment data like assessment id,
	 *            assessment section,assessment questions etc.
	 * @throws ValidationFailedException
	 *             throw this exception in case of any error in the input data
	 *             or in assessment entity like if attemptId is null
	 */
	public void saveAssesment(AssessmentEntity assessmentEntity) throws ValidationFailedException;

	/**
	 * This method is used to save the assessment data to data store after
	 * change the assessment status to submit, assessment data like assessment
	 * id, assessment section,assessment questions etc.
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which we need to
	 *            save to data store and has information like assessment id,
	 *            assessment section,assessment questions etc.
	 * 
	 * @throws ValidationFailedException
	 *             throw this exception in case of any error in the input data
	 *             or in assessment entity like if attemptId is null
	 * @throws Exception
	 *             throw this exception in case of any error while trying to
	 *             save the assessment data to data store
	 * 
	 * @return the assessment data which is now contains the user total score
	 *         for this assessment and count of question count of correct
	 *         question
	 */
	public AssessmentEntity submitAssesment(AssessmentEntity assessmentEntity)
			throws ValidationFailedException, Exception;

	/**
	 * This method is used to get the all survey which is exist in our system.
	 * 
	 * @return the list of all survey exist in system and available for user
	 */
	public List<AssessmentEntity> getSurveys();

	/**
	 * This method is used to get the user total score
	 * 
	 * @return the user total score
	 */
	public ScoreEntity getTotalScore();

	/**
	 * This method is used to validate the answer is the answer is correct or
	 * not
	 * 
	 * @param assessmentQuestionSectionEntity
	 *            this parameter contains the question data like section id,
	 *            question id,question type ,answer etc.
	 * @return the assessment question section entity which is now also contain
	 *         the explanation and answer status is correct or not
	 */
	public AssessmentQuestionSectionEntity validateAnswer(
			AssessmentQuestionSectionEntity assessmentQuestionSectionEntity) throws Exception;

	/**
	 * This method is used to get the user assessments status, what is the
	 * status user regarding each assessments in our data store, status means is
	 * it submitted or in progress.
	 * 
	 * @param userId
	 *            this is the user id
	 * @return the list of assessments and each element of list contain the
	 *         assessment name and its status
	 */
	public BoilerplateList<AssessmentEntity> getUserAssessmentStatus(String userId);

	/**
	 * 
	 * @return
	 */
	public BoilerplateList<TopScorerEntity> getTopScorrer();
}
