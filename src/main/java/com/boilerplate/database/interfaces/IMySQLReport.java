package com.boilerplate.database.interfaces;

import java.util.List;
import java.util.Map;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.Address;
import com.boilerplate.java.entities.ElectronicContact;
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
	 *             thrown when any exception occurs in saving report input entity
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
	public void saveReportTradeline(ReportTradeline reportTradeLine) throws Exception;

	/**
	 * This method is used to save electronic contacts details found in report
	 * 
	 * @param electronicContact
	 *            contains details of electronicContacts (phone details, email)of
	 *            reportTradeline of user report
	 * @throws Exception
	 *             thrown when any exception occurs in saving electronic contacts
	 */
	public void saveElectronicContact(ElectronicContact electronicContact) throws Exception;

	/**
	 * This method is used to save address details found in report
	 * 
	 * @param address
	 *            contains details of address to be saved
	 * @throws Exception
	 *             thrown when any exception occurs in saving address
	 */
	public void saveAddress(Address address) throws Exception;

	/**
	 * This method is used to get the report input entity
	 * 
	 * @param userId
	 *            the user id of the user
	 * @throws NotFoundException
	 *             thrown when any data not fount
	 * @return the report input entity
	 */
	public List<ReportInputEntity> getReportInputEntity(String userId);

	/**
	 * This method is used to get the report for the current logged in users
	 * 
	 * @param userId
	 *            This is the current logged in user
	 * @throws NotFoundException
	 *             thrown when data not found in database
	 * @return the reports map list
	 */
	public List<Map<String, Object>> getReport(String userId) throws NotFoundException;

	/**
	 * This method is used to get the report for the given report id
	 * 
	 * @param reportId
	 *            This is the report id for which the report need to be fetched
	 * @throws Exception
	 *             This exception occurred when any exception arise
	 * @return The reports list
	 */
	public List<Report> getReportByReportId(String reportId) throws Exception;

}
