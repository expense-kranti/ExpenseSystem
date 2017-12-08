package com.boilerplate.service.implemetations;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.CalculateTotalScoreObserver;
import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.exceptions.rest.BadRequestException;

import java.util.ArrayList;
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
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.java.entities.TopScorerEntity;
import com.boilerplate.service.interfaces.IAssessmentService;

/**
 * This class implements the IAssessment service class
 * 
 * @author shiva
 *
 */
public class AssesmentService implements IAssessmentService {

	/**
	 * This is new instance of Calculate Total Score Observer
	 */
	CalculateTotalScoreObserver calculateTotalScoreObserver;

	/**
	 * This method is used to set the Calculate Total Score Observer
	 * 
	 * @param calculateTotalScoreObserver
	 *            the calculateTotalScoreObserver to set
	 */
	public void setCalculateTotalScoreObserver(CalculateTotalScoreObserver calculateTotalScoreObserver) {
		this.calculateTotalScoreObserver = calculateTotalScoreObserver;
	}

	/**
	 * This is an instance of the queue job, to save the session back on to the
	 * database async
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader job
	 * 
	 * @param queueReaderJob
	 *            The queue reader job
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	/**
	 * This is the new instance of assessment class of data layer
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

	/**
	 * This is the new instance of redis assessment
	 */
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
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This variable is used to define the list of subjects ,subjects basically
	 * define the background operations need to be perform for calculate the
	 * user total score
	 */
	BoilerplateList<String> subjectsForCalculateTotalScore = new BoilerplateList();

	/**
	 * Initializes the bean
	 */
	public void initilize() {
		subjectsForCalculateTotalScore.add("CalculateTotalScore");
	}

	/**
	 * @see IAssessmentService.getAssessmentData
	 */
	@Override
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity)
			throws BadRequestException, NotFoundException, ValidationFailedException {
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
	 * @throws ValidationFailedException
	 *             throw this exception in case of any error while trying to get
	 *             any assessment which is already submitted by user
	 */
	private boolean isAssessmentExist(AttemptAssessmentListEntity attemptAssessmentList, AssessmentEntity newAssessment)
			throws ValidationFailedException {
		// Get the assessment list
		List<AssessmentEntity> assessmentList = attemptAssessmentList.getAssessmentList();
		// Run for loop to check is exist or not
		for (AssessmentEntity assessment : assessmentList) {
			// Check is assessment id equal to or not
			if ((assessment.getId()).equals(newAssessment.getId())) {
				// Check is the assessment is submitted by user or not
				if (assessment.getStatus().equals(AssessmentStatus.Submit)) {
					throw new ValidationFailedException("AssessmentEntity",
							"This assessment was already submitted by user", null);
				}
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
	public AssessmentEntity submitAssesment(AssessmentEntity assessmentEntity)
			throws ValidationFailedException, Exception {
		// Validate the assessment data
		assessmentEntity.validate();
		// Get the assessment user score
		AssessmentEntity assessmentScore = this.getAssessmentScore(assessmentEntity);
		// Set the userId
		assessmentScore.setUserId(RequestThreadLocal.getSession().getUserId());
		// Get user attempt assessment detail
		AttemptAssessmentListEntity attemptAssessment = this.getAssessmentAttempt();
		for (int i = 0; i < attemptAssessment.getAssessmentList().size(); i++) {
			AssessmentEntity assessment = (AssessmentEntity) attemptAssessment.getAssessmentList().get(i);
			// Set assessment status to submit
			if (assessment.getId().equals(assessmentEntity.getId())) {
				assessment.setStatus(AssessmentStatus.Submit);
			}
		}
		// Save the assessment details
		redisAssessment.saveAssessmentAttempt(attemptAssessment);
		// Save the assessment
		this.saveAssessment(assessmentScore, AssessmentStatus.Submit);
		try {
			queueReaderJob.requestBackroundWorkItem(assessmentScore, subjectsForCalculateTotalScore, "AssesmentService",
					"submitAssesment");
		} catch (Exception ex) {
			// now if the queue is not working we calculate user total score on
			// the thread
			calculateTotalScoreObserver.calculateTotalScore(assessmentScore);
		}
		return assessmentScore;
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
		// Get saved copy of assessment
		AssessmentEntity assessmentSavedCopy = this.getAssessment(assessment);
		// Run a for loop to check each section question score
		for (AssessmentSectionEntity section : assessment.getSections()) {
			// Set the user score to zero
			userSectionScore = 0f;
			// Get each question score
			for (AssessmentQuestionSectionEntity questionData : section.getQuestions()) {
				// Check is the question's answer is correct or not
				if (this.getAnswerStatus(questionData.getQuestion())) {
					// Get saved section data from saved copy of this assessment
					for (AssessmentSectionEntity sectionSavedCopy : assessmentSavedCopy.getSections()) {
						// If section id match with saved copy of section then
						// check qustion
						if (sectionSavedCopy.getId().equals(section.getId())) {
							// Run for loop to check each question id if
							// question match with saved copy of this question
							// then update user section score
							for (AssessmentQuestionSectionEntity questionSavedData : sectionSavedCopy.getQuestions()) {
								// If question id match with saved copy of this
								// question
								if (questionSavedData.getId().equals(questionData.getId())) {
									// If answer is correct set the user score
									// equal to question
									// score
									questionData.setIsAnswerCorrect(true);
									// Increment correct answer count
									totalCorrectAnswer = totalCorrectAnswer + 1;
									// Add question score with section score
									userSectionScore = userSectionScore
											+ Float.valueOf(questionSavedData.getQuestionScore());
									break;
								}
							}
							break;
						}
					}
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

	/**
	 * This method is used to get the multiple choice question's answer status
	 * is the answer is correct or not if correct then set answer status to true
	 * else false and return the correct option
	 * 
	 * @param assessmentQuestionSectionEntity
	 *            this parameter contains the assessment question section
	 *            details like question details, question id ,question type and
	 *            user selected option
	 * @return the assessmentQuestionSectionEntity which is now also have the
	 *         answer status and correct option text
	 * @throws BadRequestException
	 *             throw this exception in case of any error while trying to get
	 *             the multiple choice question option status
	 */
	private AssessmentQuestionSectionEntity getMultipleChoiceQuestionAnswerStatusAndCorrectOption(
			AssessmentQuestionSectionEntity assessmentQuestionSectionEntity) throws BadRequestException {
		// Get the options status
		MultipleChoiceQuestionEntity multipleChoiceQuestionEntity = assessment
				.getMultipleChoiceQuestionAndOptions(assessmentQuestionSectionEntity.getQuestion().getId());
		// Run a for to check the user selected option is correct or not
		for (MultipleChoiceQuestionOptionEntity option : multipleChoiceQuestionEntity.getOptions()) {
			// Match the option id
			if (option.getId().equals(assessmentQuestionSectionEntity.getQuestion().getAnswer())) {
				// Check is option is correct or not
				if (option.getIsCorrect()) {
					// If option is correct return true
					assessmentQuestionSectionEntity.setIsAnswerCorrect(true);
					// Set correct option
					assessmentQuestionSectionEntity.setCorrectOption(option.getText());
					// Return assessment question entity which is now also
					// contain the correct option text and answer status
					return assessmentQuestionSectionEntity;
				}
			}
			if (option.getIsCorrect()) {
				// Set correct option
				assessmentQuestionSectionEntity.setCorrectOption(option.getText());
			}
		}
		// Set answer status to false
		assessmentQuestionSectionEntity.setIsAnswerCorrect(false);
		// Return assessment question entity which is now also
		// contain the correct option text and answer status
		return assessmentQuestionSectionEntity;
	}

	/**
	 * @see IAssessmentService.getSurveys
	 */
	@Override
	public List<AssessmentEntity> getSurveys() {
		return assessment.getSurveys();
	}

	/**
	 * @see IAssessmentService.getTotalScore
	 */
	@Override
	public ScoreEntity getTotalScore() {
		// Get the user total score
		ScoreEntity scoreEntity = redisAssessment.getTotalScore(RequestThreadLocal.getSession().getUserId());
		// check for null rank
		if (scoreEntity == null) {
			scoreEntity = new ScoreEntity();
			scoreEntity.setRank(configurationManager.get("Rank1"));
			scoreEntity.setObtainedScore(configurationManager.get("Default_Score"));
		}
		return scoreEntity;
	}

	/**
	 * @see IAssessmentService.validateAnswer
	 */
	@Override
	public AssessmentQuestionSectionEntity validateAnswer(
			AssessmentQuestionSectionEntity assessmentQuestionSectionEntity) throws Exception {
		// Get answer status and correct option
		AssessmentQuestionSectionEntity assessmentQuestionSection = this
				.getMultipleChoiceQuestionAnswerStatusAndCorrectOption(assessmentQuestionSectionEntity);
		// Get the explanation
		assessmentQuestionSection.setExplanation(
				assessment.getQuestionExpanation(assessmentQuestionSectionEntity.getQuestion().getId()));
		return assessmentQuestionSection;
	}

	/**
	 * @see IAssessmentService.getUserAssessmentStatus
	 */
	@Override
	public BoilerplateList<AssessmentEntity> getUserAssessmentStatus(String userId) {
		// Get all the assessments exist in data store
		List<AssessmentEntity> assessments = assessment.getAssessments();
		// Get all the surveys exist in data store
		List<AssessmentEntity> surveys = assessment.getSurveys();
		// Add all survey to assessment list
		assessments.addAll(surveys);
		// Declare a new list to collect the information regarding the user
		// assessments status
		BoilerplateList<AssessmentEntity> userAssessmentsStatus = new BoilerplateList<AssessmentEntity>();
		// Run a for loop to check each assessment status for the user
		for (AssessmentEntity assessmentData : assessments) {
			// Declare new instance of assessment to store information regarding
			// the assessment status
			AssessmentEntity assessmentsStatusEntity = new AssessmentEntity();
			// Get the assessment details of user means is this assessment is in
			// progress or submit
			AssessmentEntity userAssessment = redisAssessment.getAssessment(assessmentData, userId);
			// If user assessment is null means user never attempt this
			// assessment before
			if (userAssessment != null) {
				// Set the assessment status
				assessmentsStatusEntity.setStatus(userAssessment.getStatus());
			} else {
				// Set the assessment status
				assessmentsStatusEntity.setStatus(AssessmentStatus.NotStarted);
			}
			// Set assessment name
			assessmentsStatusEntity.setName(assessmentData.getName());
			// Check the assessment is a survey or quiz
			if (assessmentData.isSurvey()) {
				// If it is survey
				assessmentsStatusEntity
						.setId("Survey:" + assessmentData.getName() + ":" + assessmentData.getId() + ":" + userId);
			} else {
				// If is not survey it is quiz
				assessmentsStatusEntity
						.setId("Quiz:" + assessmentData.getName() + ":" + assessmentData.getId() + ":" + userId);
			}
			// Add each assessment status to list
			userAssessmentsStatus.add(assessmentsStatusEntity);
		}
		return userAssessmentsStatus;
	}

	@Override
	public BoilerplateList<TopScorerEntity> getTopScorrer() {
		// TODO Auto-generated method stub
		return null;
	}

}
