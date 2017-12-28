package com.boilerplate.service.interfaces;

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
	 */
	public void save(Report report);

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

}
