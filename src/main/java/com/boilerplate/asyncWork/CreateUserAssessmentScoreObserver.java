package com.boilerplate.asyncWork;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentQuestionSectionEntity;
import com.boilerplate.java.entities.AssessmentSectionEntity;
import com.boilerplate.java.entities.MultipleChoiceQuestionEntity;
import com.boilerplate.java.entities.MultipleChoiceQuestionOptionEntity;
import com.boilerplate.java.entities.QuestionEntity;
import com.boilerplate.java.entities.QuestionType;

/**
 * This class create the user assessment score
 * 
 * @author shiva
 *
 */
public class CreateUserAssessmentScoreObserver implements IAsyncWorkObserver {

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

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		// Convert dataPayload to entity
		AssessmentEntity assessmentData = (AssessmentEntity) asyncWorkItem.getPayload();
		this.getAssessmentScore(assessmentData);
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
	private void getAssessmentScore(AssessmentEntity assessment) throws Exception {
		Float userSectionScore = 0f;
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
					questionData.setUserScore(questionData.getQuestionScore());
					// Add question score with section score
					userSectionScore = userSectionScore + Float.valueOf(questionData.getUserScore());
				}
			}
			// Set the section score
			section.setUserSectionScore(String.valueOf(userSectionScore));
		}
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
