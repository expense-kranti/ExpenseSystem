package com.boilerplate.database.mysql.implementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boilerplate.database.interfaces.IMySQLReport;
import com.boilerplate.database.interfaces.IReport;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Address;
import com.boilerplate.java.entities.ElectronicContact;
import com.boilerplate.java.entities.Report;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.java.entities.ReportTradeline;

/**
 * This method implements methods of IReport for mysql crud operation
 * 
 * @author urvij
 *
 */
public class MySQLReport extends MySQLBaseDataAccessLayer implements IMySQLReport {

	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(MySQLReport.class);

	/**
	 * @see IMySQLReport.saveReport
	 */
	@Override
	public void saveReport(Report latestReport) throws Exception {
		try {
			// Save the user data into the data base
			super.create(latestReport);
		} catch (Exception ex) {
			// Log the exception
			logger.logException("MySQLReport", "saveReport", "Try-Catch", " Exception in saving report data in my sql"
					+ latestReport.getUserId() + " report is : " + Base.toJSON(latestReport), ex);
			throw ex;
		}
	}

	/**
	 * @see IMySQLReport.saveReportInputEntity
	 */
	@Override
	public void saveReportInputEntity(ReportInputEntity reportInputEntitty) throws Exception {
		try {
			super.create(reportInputEntitty);
		} catch (Exception ex) {
			// Log the exception
			logger.logException("MySQLReport", "saveReportInputEntity", "Try-Catch",
					" Exception in saving report input enitity in my sql", ex);
			throw ex;
		}
	}

	/**
	 * @see IMySQLReport.saveReportTradeline
	 */
	@Override
	public void saveReportTradeline(ReportTradeline reportTradeLine) throws Exception {
		try {
			super.create(reportTradeLine);
		} catch (Exception ex) {
			// Log the exception
			logger.logException("MySQLReport", "saveReportTradeline", "Try-Catch",
					" Exception in saving report tradeline in my sql", ex);
			throw ex;
		}

	}

	/**
	 * @see IMySQLReport.saveElectronicContact
	 */
	@Override
	public void saveElectronicContact(ElectronicContact electronicContact) throws Exception {
		try {
			super.create(electronicContact);
		} catch (Exception ex) {
			// Log the exception
			logger.logException("MySQLReport", "saveElectronicContact", "Try-Catch",
					" Exception in saving Electronic Contact in my sql", ex);
			throw ex;
		}
	}

	@Override
	public void saveAddress(Address address) throws Exception {
		try {
			super.create(address);
		} catch (Exception ex) {
			// Log the exception
			logger.logException("MySQLReport", "saveAddress", "Try-Catch", " Exception in saving Address in my sql",
					ex);
			throw ex;
		}
	}

	@Override
	public List<ReportInputEntity> getReportInputEntity(String userId) {
		String hSQLQuery = "from ReportInputEntity where userId = :userId";
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("userId", userId);
		return super.executeSelect(hSQLQuery, queryParameters);
	}

}
