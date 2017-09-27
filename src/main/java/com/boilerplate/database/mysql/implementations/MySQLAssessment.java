package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentQuestionSectionEntity;
import com.boilerplate.java.entities.AssessmentSectionEntity;
import com.boilerplate.java.entities.MultipleChoiceQuestionEntity;
import com.boilerplate.java.entities.QuestionType;

/**
 * This class implements IAssessment
 * 
 * @author shiva
 *
 */
public class MySQLAssessment extends MySQLBaseDataAccessLayer implements IAssessment {

	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This method is used to set the configurationManager
	 * 
	 * @param configurationManager
	 *            the configurationManager to set
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This variable provide the key for SQL query for get the assessment from
	 * configurations
	 */
	private static String sqlQueryGetAssessment = "SQL_QUERY_GET_ASSESSMENT";

	/**
	 * This variable provide the key for SQL query for get the multiple choice
	 * question from configurations
	 */
	private static String sqlQueryGetMultipleChoiceQuestion = "SQL_QUERY_GET_MULTIPLE_CHOICE_QUESTION";

	/**
	 * @see IAssessment.getAssessment
	 */
	@Override
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity) throws BadRequestException {
		// Get the SQL query to process the request
		String sqlQuery = configurationManager.get(sqlQueryGetAssessment);
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put the id into query parameter
		queryParameterMap.put("Id", assessmentEntity.getId());
		// This variable is used to hold the query response data
		List<AssessmentEntity> requestedDataList = new ArrayList<>();
		try {
			//Execute query
			requestedDataList = super.executeSelect(sqlQuery, queryParameterMap);
		} catch (Exception ex) {
			// Throw this exception in case of any error while trying to get the
			// assessment data regarding the assessment id
			throw new BadRequestException("AssessmentEntity", "While trying to get the assessment ~ "
					+ "This is the SQL query ~ " + sqlQuery + "~" + ex.toString(), null);
		}
		// Get the questions for assessment data
		AssessmentEntity assessment = this.getQuestion(requestedDataList.get(0));
		return assessment;
	}

	/**
	 * This method is used to get the question first check the type of question
	 * and then according to type get the questions from the data base.
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment details
	 * @return the assessment entity which is now contain the questions also
	 * @throws BadRequestException
	 *             throw this exception in case of any error while trying to get
	 *             the question
	 */
	private AssessmentEntity getQuestion(AssessmentEntity assessmentEntity) throws BadRequestException {
		// Run for loop to get the question regarding each section
		for (AssessmentSectionEntity section : assessmentEntity.getSections()) {
			// Run for loop to get the question text
			for (AssessmentQuestionSectionEntity question : section.getQuestions()) {
				// According to type get the question
				switch (QuestionType.valueOf(question.getQuestion().getQuestionType().getName())) {
				case MultipleChoice:
					// Get the multiple choice question and options
					(question.getQuestion())
							.setQuestionText(this.getMultipleChoiceQuestionAndOptions(question.getQuestion().getId()));
					break;
				default:
					// If no case match then throw an exception
					throw new NotImplementedException(
							"No quetsion type found" + question.getQuestion().getQuestionType().getName());
				}
			}
		}
		return assessmentEntity;

	}

	/**
	 * This method is used to get the multiple choice question and options
	 * regarding the questionId
	 * 
	 * @param id
	 *            this parameter contains the question id
	 * @throws BadRequestException
	 *             throw this exception in case of any error while trying to get
	 *             the multiple choice question regarding question id
	 */
	private MultipleChoiceQuestionEntity getMultipleChoiceQuestionAndOptions(String questionId)
			throws BadRequestException {
		// Get the SQL query to process the request
		String sqlQuery = configurationManager.get(sqlQueryGetMultipleChoiceQuestion);
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put question in query parameter
		queryParameterMap.put("QuestionId", questionId);
		// This variable is used to hold the query response
		List<MultipleChoiceQuestionEntity> requestedDataList = new ArrayList<>();
		try {
			//Execute query
			requestedDataList = super.executeSelect(sqlQuery, queryParameterMap);
		} catch (Exception ex) {
			// Throw exception
			throw new BadRequestException("AssessmentEntity", "While trying to get multiple choice question ~ "
					+ "This is the SQL query ~ " + sqlQuery + "~" + ex.toString(), null);
		}
		return requestedDataList.get(0);
	}

}
