package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.java.collections.BoilerplateMap;
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
	 * This method is used to get the report of the current logged_in user
	 * 
	 * @return the report of the user
	 * @throws NotFoundException
	 *             thrown when no express attempt found for logged in user
	 * @throws UnauthorizedException
	 *             This exception occurred if user is not logged in
	 */
	public Report getReport() throws NotFoundException, UnauthorizedException;

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
