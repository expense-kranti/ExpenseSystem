package com.boilerplate.database.interfaces;

import com.boilerplate.java.entities.Report;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.java.entities.ReportTradeline;

/**
 * This interface contains the methods used to do crud operations for report and
 * its generation related data like report, reporttradeline, report input enity
 * saving, getting and updating
 * 
 * @author urvij
 *
 */
public interface IMySQLReport {

	/**
	 * This method is used to save report in database
	 * 
	 * @param report
	 *            the report entity to be saved
	 * @throws Exception
	 *             thrown when any exception occurs in saving report entity
	 */
	public void saveReport(Report report) throws Exception;

	/**
	 * This method is used to save report input entity in database
	 * 
	 * @param reportInputEntitty
	 *            the report input entity
	 * @throws Exception
	 *             thrown when any exception occurs in saving report input
	 *             entity
	 */
	public void saveReportInputEntity(ReportInputEntity reportInputEntitty) throws Exception;

	/**
	 * This method is used to save report tradeline
	 * 
	 * @param reportTradeLine
	 *            contains details about report tradeline
	 * @throws Exception
	 *             thrown when exception occurs in saving tradeline entity
	 * 
	 */
	void saveReportTradeline(ReportTradeline reportTradeLine) throws Exception;
}
