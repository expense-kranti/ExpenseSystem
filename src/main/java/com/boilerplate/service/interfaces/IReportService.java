package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExperianQuestionAnswer;
import com.boilerplate.java.entities.Report;

/**
 * This class is used to do operations on reports like saving and getting
 * reports
 * 
 * @author
 *
 */
public interface IReportService {

	/**
	 * This method is used to save the report
	 * 
	 * @param report
	 *            the report to save
	 * @throws Exception
	 */
	public void save(Report report) throws Exception;

	/**
	 * This method is used to get the reports against a user with given userId
	 * 
	 * @param userId
	 *            the userId against whom reports are to be found
	 * @return the list of reports against a user
	 * @throws UnauthorizedException
	 *             if the user trying to get the reports is not authorized
	 */
	public BoilerplateMap<String, Report> getReports(String userId) throws UnauthorizedException;

	/**
	 * This method is used to get the experian question answer entity for the given
	 * question id for userid
	 * 
	 * @param userId
	 *            the user id
	 * @param questionId
	 *            the question id
	 * @return the experian question answer entity
	 */
	public ExperianQuestionAnswer getQuestionAnswers(String userId, String questionId);

	/**
	 * This method is used to check if question answers key exists for user id
	 * 
	 * @param userId
	 *            the user id
	 * @return true if key exists, false if not
	 */
	public boolean checkQuestionAnswerExists(String userId);

	/**
	 * This method is used to save the experian question answer entity against the
	 * given userid
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

	/**
	 * This method is used to get the latest report of the current logged_in user
	 * 
	 * @return the report of the user
	 * @throws NotFoundException
	 *             thrown when no express attempt found for logged in user
	 * @throws UnauthorizedException
	 *             This exception occurred if user is not logged in
	 * @throws Exception
	 *             This exception throw if any exception occurred
	 */
	public Report getLatestReport() throws NotFoundException, UnauthorizedException, Exception;

	/**
	 * This method is used to get the product name for the given product code
	 * 
	 * @param accountType
	 *            account type is the product code
	 * @return the productname
	 */
	public String getProductName(int accountType);

	/**
	 * This method is used to get the report for the given report Id
	 * 
	 * @param reportId
	 *            This is the report id for which we have to find the report
	 * @return the report for the given report id
	 * @throws NotFoundException
	 *             thrown when no express attempt found for logged in user
	 * @throws Exception
	 *             This exception occurred if any exception occurred
	 */
	public Report getReportByReportId(String reportId) throws NotFoundException, Exception;

}
