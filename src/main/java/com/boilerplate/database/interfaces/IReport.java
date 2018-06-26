package com.boilerplate.database.interfaces;

import java.util.List;

import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExperianQuestionAnswer;
import com.boilerplate.java.entities.Report;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.java.entities.ReportTradeline;

public interface IReport {

	public BoilerplateMap<String, Report> getReports(String userId);

	public Object saveReport(Report report) throws Exception;

	/**
	 * This method is used to get the experian question answers for a user
	 * 
	 * @param userId
	 *            the user id of the user
	 */
	public ExperianQuestionAnswer getExperianQuestionAnswer(String userId, String questionId);

	/**
	 * This method is used to check if question answers key exists for user id
	 * 
	 * @param userId
	 *            the user id
	 * @return true if key exists, false if not
	 */
	public boolean checkQuestionAnswerExists(String userId);

	/**
	 * This method is used to save the experian question answer entity against
	 * the given userid
	 * 
	 * @param userId
	 *            the user id
	 * @param questionId
	 *            the question id
	 * @param experianQuestionAnswer
	 *            the entity to save
	 */
	public void saveExperianQuestionAnswer(String userId, String questionId,
			ExperianQuestionAnswer experianQuestionAnswer);

}
