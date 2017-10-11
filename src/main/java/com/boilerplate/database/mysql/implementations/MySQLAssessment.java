package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.Base;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentQuestionSectionEntity;
import com.boilerplate.java.entities.AssessmentSectionEntity;
import com.boilerplate.java.entities.MultipleChoiceQuestionEntity;
import com.boilerplate.java.entities.MultipleChoiceQuestionOptionEntity;
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
	 * This variable provide the key for SQL query for configurations this SQL
	 * query is used to get the assessment from configurations
	 */
	private static String sqlQueryGetAssessment = "SQL_QUERY_GET_ASSESSMENT";

	/**
	 * This variable provide the key for SQL query for configurations this SQL
	 * query is used to get the multiple choice question
	 */
	private static String sqlQueryGetMultipleChoiceQuestion = "SQL_QUERY_GET_MULTIPLE_CHOICE_QUESTION";

	/**
	 * This variable provide the key for SQL query for configurations this SQL
	 * query is used to get list of assessment
	 */
	private static String sqlQueryGetAssessmenList = "SQL_QUERY_GET_ASSESSMENT_LIST";

	/**
	 * This variable provide the key for SQL query for configurations this SQL
	 * query is used to get list of survey
	 */
	private static String sqlQueryGetSurveyList = "SQL_QUERY_GET_SURVEY_LIST";

	/**
	 * This variable provide the key for SQL query for configurations this SQL
	 * query is used to get the question explanation
	 */
	private static String sqlQueryGetQuestionExplanation = "SQL_QUERY_GET_QUESTION_EXPLANATION";

	/**
	 * This variable is used to define the key for get the question explanation
	 * from the map
	 */
	private static String questionExplanation = "QuestionExplanation";

	/**
	 * @see IAssessment.getAssessment
	 */
	@Override
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity)
			throws BadRequestException, NotFoundException {
		// Get the SQL query to process the request
		String sqlQuery = configurationManager.get(sqlQueryGetAssessment);
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put the id into query parameter
		queryParameterMap.put("Id", assessmentEntity.getId());
		// Put the active status into query parameter
		queryParameterMap.put("Active", assessmentEntity.getActive());
		// This variable is used to hold the query response data
		List<AssessmentEntity> requestedDataList = new ArrayList<>();
		try {
			// Execute query
			requestedDataList = super.executeSelect(sqlQuery, queryParameterMap);
		} catch (Exception ex) {
			// Throw this exception in case of any error while trying to get the
			// assessment data regarding the assessment id
			throw new BadRequestException("AssessmentEntity", "While trying to get the assessment ~ "
					+ "This is the SQL query ~ " + sqlQuery + "~" + ex.toString(), null);
		}
		// Check the size of response of SQL query
		if (requestedDataList.size() <= 0) {
			// Throw this exception if the size of list is equal to zero
			throw new NotFoundException("AssessmentEntity", "No assessment found for given id.", null);
		}
		// Get the questions for assessment data
		AssessmentEntity assessment = this.getQuestion(requestedDataList.get(0));
		return assessment;
	}

	/**
	 * This method is used to get the question, first check the type of question
	 * and then according to type get the questions from the data base.
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which we need to
	 *            save to data store ,assessment data like assessment id,
	 *            assessment section,assessment question,assessment question
	 *            type etc.
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
							.setQuestionData(this.getMultipleChoiceQuestionAndOptions(question.getQuestion().getId()));
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
	 * @see IAssessment.getMultipleChoiceQuestionAndOptions
	 */
	public MultipleChoiceQuestionEntity getMultipleChoiceQuestionAndOptions(String questionId)
			throws BadRequestException {
		// Get the SQL query from configurations for get the multiple choice
		// question
		String sqlQuery = configurationManager.get(sqlQueryGetMultipleChoiceQuestion);
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put question in query parameter
		queryParameterMap.put("QuestionId", questionId);
		// This variable is used to hold the query response
		List<MultipleChoiceQuestionEntity> requestedDataList = new ArrayList<>();
		try {
			// Execute query
			requestedDataList = super.executeSelect(sqlQuery, queryParameterMap);
		} catch (Exception ex) {
			// Throw exception
			throw new BadRequestException("AssessmentEntity", "While trying to get multiple choice question ~ "
					+ "This is the SQL query ~ " + sqlQuery + "~" + ex.toString(), null);
		}
		//shuffle the option list so that correct option is not in same order
		requestedDataList.get(0).setText(requestedDataList.get(0).getText().replace("\\", ""));
		Collections.shuffle(requestedDataList.get(0).getOptions());
		return requestedDataList.get(0);
	}

	/**
	 * @see IAssessment.getAssessments
	 */
	@Override
	public List<AssessmentEntity> getAssessments() {
		// Get the SQL query from configurations for get the list of assessment
		String sqlQuery = configurationManager.get(sqlQueryGetAssessmenList);
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// This variable is used to hold the query response
		List<Map<String, Object>> requestedDataList = new ArrayList<>();
		// This variable is used to hold the list of assessment entity converted
		// from map
		List<AssessmentEntity> assessmentEntityList = new ArrayList<>();
		try {
			// Execute query
			requestedDataList = super.executeSelectNative(sqlQuery, queryParameterMap);
			int i = 0;
			// While loop to convert each map into entity
			while (i < requestedDataList.size()) {
				// Convert map into entity
				AssessmentEntity assessmentEntity = Base.fromMap(requestedDataList.get(i), AssessmentEntity.class);
				// Add to list
				assessmentEntityList.add(assessmentEntity);
				i++;
			}

		} catch (Exception ex) {
			// Throw this exception in case of any error while trying to convert
			// the list of map to list of entity
			throw ex;
		}
		return assessmentEntityList;
	}

	/**
	 * @see IAssessment.getSurveys
	 */
	@Override
	public List<AssessmentEntity> getSurveys() {
		// Get the SQL query from configurations for get the list of assessment
		String sqlQuery = configurationManager.get(sqlQueryGetSurveyList);
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// This variable is used to hold the query response
		List<Map<String, Object>> requestedDataList = new ArrayList<>();
		// This variable is used to hold the list of assessment entity converted
		// from map
		List<AssessmentEntity> assessmentEntityList = new ArrayList<>();
		try {
			// Execute query
			requestedDataList = super.executeSelectNative(sqlQuery, queryParameterMap);
			int i = 0;
			// While loop to convert each map into entity
			while (i < requestedDataList.size()) {
				// Convert map into entity
				AssessmentEntity assessmentEntity = Base.fromMap(requestedDataList.get(i), AssessmentEntity.class);
				// Add to list
				assessmentEntityList.add(assessmentEntity);
				i++;
			}

		} catch (Exception ex) {
			// Throw this exception in case of any error while trying to convert
			// the list of map to list of entity
			throw ex;
		}
		return assessmentEntityList;
	}

	/**
	 * @see IAssessment.getQuestionExpanation
	 */
	@Override
	public String getQuestionExpanation(String questionId) {
		// Get the SQL query from configurations for get the list of assessment
		String sqlQuery = configurationManager.get(sqlQueryGetQuestionExplanation);
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put question in query parameter
		queryParameterMap.put("QuestionId", questionId);
		// This variable is used to hold the query response
		List<Map<String, Object>> requestedDataList = // Execute query
				requestedDataList = super.executeSelectNative(sqlQuery, queryParameterMap);
		return (String) requestedDataList.get(0).get(questionExplanation);
	}

}
