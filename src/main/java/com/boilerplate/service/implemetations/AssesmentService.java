package com.boilerplate.service.implemetations;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.asyncWork.CreateUserAssessmentScoreObserver;
import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.exceptions.rest.BadRequestException;

import java.util.List;
import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentQuestionSectionEntity;
import com.boilerplate.java.entities.AssessmentSectionEntity;
import com.boilerplate.java.entities.AssessmentStatus;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.java.entities.MultipleChoiceQuestionEntity;
import com.boilerplate.java.entities.MultipleChoiceQuestionOptionEntity;
import com.boilerplate.java.entities.QuestionEntity;
import com.boilerplate.java.entities.QuestionType;
import com.boilerplate.service.interfaces.IAssessmentService;

/**
 * This class implements the IAssessment service class
 * 
 * @author shiva
 *
 */
public class AssesmentService implements IAssessmentService {

	private String createScoreBackgroundJobSubject = "CreateScore";
	/**
	 * this is the new instance of assessment class of data layer
	 */
	@Autowired
	IAssessment assessment;

	/**
	 * This method is used to set the assessment
	 * 
	 * @param assessment
	 */
	public void setAssessment(IAssessment assessment) {
		this.assessment = assessment;
	}

	@Autowired
	IRedisAssessment redisAssessment;

	/**
	 * This method set the redisAssessment
	 * 
	 * @param redisAssessment
	 *            the redisAssessment to set
	 */
	public void setRedisAssessment(IRedisAssessment redisAssessment) {
		this.redisAssessment = redisAssessment;
	}

	/**
	 * @see IAssessmentService.getAssessmentData
	 */
	@Override
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity)
			throws BadRequestException, NotFoundException {
		// Set assessment active status to true because we want to get only
		// active assessment
		assessmentEntity.setActive(true);
		AssessmentEntity assessmentData = new AssessmentEntity();
		try {
			AttemptAssessmentListEntity attemptAssessmentList = this.getAssessmentAttempt();
			// Set attempt id
			assessmentEntity.setAttemptId(RequestThreadLocal.getSession().getUserId() + assessmentEntity.getId());
			if (this.isAssessmentExist(attemptAssessmentList, assessmentEntity)) {
				// Get assessment data from redis
				assessmentData = redisAssessment.getAssessment(assessmentEntity);
			} else {
				// Get the assessment data regarding assessment id
				assessmentData = assessment.getAssessment(assessmentEntity);
				// Set attempt id
				assessmentData.setAttemptId(RequestThreadLocal.getSession().getUserId() + assessmentData.getId());
				// Save the assessment
				this.saveAssessment(assessmentData, AssessmentStatus.Inprogress);
				// Save the attempt assessment data to redis
				this.saveAssessmentAttemptWithAppendAssessmentDetail(assessmentEntity, attemptAssessmentList);
			}
		} catch (NotFoundException ex) {
			// Declare new instance of attempt assessment list
			AttemptAssessmentListEntity attemptAssessment = new AttemptAssessmentListEntity();
			// Set the user id to list
			attemptAssessment.setUserId(RequestThreadLocal.getSession().getUserId());
			// Get the assessment data regarding assessment id
			assessmentData = assessment.getAssessment(assessmentEntity);
			// Set attempt id
			assessmentData.setAttemptId(RequestThreadLocal.getSession().getUserId() + assessmentData.getId());
			// Save the assessment
			this.saveAssessment(assessmentData, AssessmentStatus.Inprogress);
			// Save the attempt assessment data to redis
			this.saveAssessmentAttemptWithAppendAssessmentDetail(assessmentEntity, attemptAssessment);
		}
		// Get the assessment data from data base
		return assessmentData;
	}

	/**
	 * This method is used to check the list of assessment contains the new
	 * assessment id or not if exist then return true else false.
	 * 
	 * @param attemptAssessmentList
	 *            this parameter contains the list of assessment
	 * @param assessmentEntity
	 *            this parameter define the new assessment
	 * @return true if assessment exist else return false
	 */
	private boolean isAssessmentExist(AttemptAssessmentListEntity attemptAssessmentList,
			AssessmentEntity newAssessment) {
		// Get the assessment list
		List<AssessmentEntity> assessmentList = attemptAssessmentList.getAssessmentList();
		// Run for loop to check is exist or not
		for (AssessmentEntity assessment : assessmentList) {
			// Check is assessment id equal to or not
			if ((assessment.getId()).equals(newAssessment.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is used to save the attempt assessment data to data store
	 * first append the new assessment details to it to existing list of
	 * assessment, assessment details like assessment id, assessment
	 * section,assessment questions etc.
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which we need to
	 *            save to data store ,assessment data like assessment id,
	 *            assessment section,assessment questions etc.
	 * @param attemptAssessment
	 *            this parameter contains the information about the assessment
	 *            attempt by user
	 */
	private void saveAssessmentAttemptWithAppendAssessmentDetail(AssessmentEntity assessmentEntity,
			AttemptAssessmentListEntity attemptAssessment) {
		// Declare new list of assessment
		BoilerplateList<AssessmentEntity> assessmentList = new BoilerplateList<>();
		// Check is size of assessment listk
		if (attemptAssessment.getAssessmentList().size() > 0) {
			// Declare new list of assessment
			assessmentList = attemptAssessment.getAssessmentList();
		}
		// Add new assessment to list
		assessmentList.add(new AssessmentEntity(assessmentEntity.getId(), AssessmentStatus.Inprogress));
		// Set the assessment list
		attemptAssessment.setAssessmentList(assessmentList);
		// Save the assessment details
		redisAssessment.saveAssessmentAttempt(attemptAssessment);
	}

	/**
	 * @see IAssessmentService.getAssessments
	 */
	@Override
	public List<AssessmentEntity> getAssessments() {
		List<AssessmentEntity> assessmentList = assessment.getAssessments();
		return assessmentList;
	}

	/**
	 * @see IAssessmentService.getAssessmentAttempt
	 */
	@Override
	public AttemptAssessmentListEntity getAssessmentAttempt() throws NotFoundException {
		AttemptAssessmentListEntity attemptAssessmentListEntity = redisAssessment.getAssessmentAttempt();
		if (attemptAssessmentListEntity == null) {
			throw new NotFoundException("AttemptAssessmentListEntity", "No attempt was found for this user.", null);
		}
		return attemptAssessmentListEntity;
	}

	/**
	 * @see IAssessmentService.saveAssesment
	 */
	@Override
	public void saveAssesment(AssessmentEntity assessmentEntity) throws ValidationFailedException {
		// Validate the assessment data
		assessmentEntity.validate();
		// Save the assessment to data store
		redisAssessment.saveAssessment(assessmentEntity);
	}

	/**
	 * @see IAssessmentService.submitAssesment
	 */
	@Override
	public void submitAssesment(AssessmentEntity assessmentEntity) throws ValidationFailedException, Exception {
		// Validate the assessment data
		assessmentEntity.validate();
		// Save the assessment
		this.saveAssessment(assessmentEntity, AssessmentStatus.Submit);
		// Get the assessment user score
		AssessmentEntity assessmentScore = this.getAssessmentScore(assessmentEntity);
	}

	/**
	 * This method is used to save the assessment into data store with set the
	 * assessment status
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which we need to
	 *            save to data store ,assessment data like assessment id,
	 *            assessment section,assessment questions etc.
	 * @param assessmentStatus
	 *            this is the assessment status like in-progress,submit
	 */
	private void saveAssessment(AssessmentEntity assessmentEntity, AssessmentStatus assessmentStatus) {
		// Set the assessment status to submit
		assessmentEntity.setStatus(assessmentStatus);
		// Save the assessment to data store
		redisAssessment.saveAssessment(assessmentEntity);
	}

	/**
	 * This method is used to get the user attempted assessment score
	 * 
	 * @param assessment
	 *            this parameter contains the user attempted assessment data
	 *            like assessment sections each section contains some question
	 *            and each question also contains user answer
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             the assessment score
	 */
	private AssessmentEntity getAssessmentScore(AssessmentEntity assessment) throws Exception {
		// This is the user section score obtain
		Float userSectionScore = 0f;
		// This is the user assessment score
		Float userAssessmentScore = 0f;
		// This is the user correct answer count
		Integer totalCorrectAnswer = 0;
		// This is total number of question in assessment
		Integer totalQuestion = 0;
		// Run a for loop to check each section question score
		for (AssessmentSectionEntity section : assessment.getSections()) {
			// Set the user score to zero
			userSectionScore = 0f;
			// Get each question score
			for (AssessmentQuestionSectionEntity questionData : section.getQuestions()) {
				// Check is the question's answer is correct or not
				if (this.getAnswerStatus(questionData.getQuestion())) {
					// If answer is correct set the user score equal to question
					// score
					questionData.setIsAnswerCorrect(true);
					// Increment correct answer count
					totalCorrectAnswer = totalCorrectAnswer + 1;
					// Add question score with section score
					userSectionScore = userSectionScore + Float.valueOf(questionData.getQuestionScore());
				}
				// Increment total questions by 1
				totalQuestion = totalQuestion + 1;
			}
			// Add the user section score with assessment score
			userAssessmentScore = userAssessmentScore + userSectionScore;
		}
		// Set user obtained score
		assessment.setObtainedScore(String.valueOf(userAssessmentScore));
		// Set correct answer count
		assessment.setTotalCorrectAnswer(totalCorrectAnswer);
		// Set total question count
		assessment.setTotalQuestions(totalQuestion);
		return assessment;
	}

	/**
	 * This method is used to get the question's answer status is the answer is
	 * correct or not if correct then return true else false
	 * 
	 * @param question
	 *            this parameter contains the question details like question id
	 *            ,question type and question's answer
	 * @return true if answer is correct else return false
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             the answer status
	 */
	private boolean getAnswerStatus(QuestionEntity question) throws Exception {
		// Set id by default zero
		boolean questionCorrect;
		// According to type get the question
		switch (QuestionType.valueOf(question.getQuestionType().getName())) {
		case MultipleChoice:
			// Get the multiple choice question and options
			questionCorrect = getMultipleChoiceQuestionAnswerStatus(question);
			break;
		default:
			// If no case match then throw an exception
			throw new NotImplementedException("No quetsion type found" + question.getQuestionType().getName());
		}
		return questionCorrect;
	}
	
	/**
	 * This method is used to get the multiple choice question's answer status
	 * is the answer is correct or not if correct then return true else false
	 * 
	 * @param question
	 *            this parameter contains the question details like question id
	 *            ,question type and user selected option
	 * @return true if option is correct else return false
	 * @throws BadRequestException
	 *             throw this exception in case of any error while trying to get
	 *             the multiple choice question option status
	 */
	private boolean getMultipleChoiceQuestionAnswerStatus(QuestionEntity question) throws BadRequestException {
		// Get the options status
		MultipleChoiceQuestionEntity multipleChoiceQuestionEntity = assessment
				.getMultipleChoiceQuestionAndOptions(question.getId());
		// Run a for to check the user selected option is correct or not
		for (MultipleChoiceQuestionOptionEntity option : multipleChoiceQuestionEntity.getOptions()) {
			// Match the option id
			if (option.getId().equals(question.getAnswer())) {
				// Check is option is correct or not
				if (option.getIsCorrect()) {
					// If option is correct return true
					return true;
				}
			}
		}
		return false;
	}
}
