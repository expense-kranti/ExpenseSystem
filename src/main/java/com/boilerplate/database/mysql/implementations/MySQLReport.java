package com.boilerplate.database.mysql.implementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IMySQLReport;
import com.boilerplate.database.interfaces.IReport;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Address;
import com.boilerplate.java.entities.Configuration;
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
	 * creating the instance of logger
	 */
	private Logger logger = Logger.getInstance(MySQLReport.class);

	/**
	 * This is the instance of configuration manager
	 */
	ConfigurationManager configurationManager;

	/**
	 * Setter for configuration
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

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

	/**
	 * @see IMySQLReport.getReport
	 */
	@Override

	public Report getLatestReport(String userId) {
		logger.logInfo("MySQLReport", "getLatestReport", "StartgetLatestReport", "Method-Start");
		// Initializing the report
		Report report = null;
		try {
			Map<String, Object> queryParameterMap = new HashMap<String, Object>();
			// Get the sql Query from the Configuration
			String sqlQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_THE_REPORT");
			sqlQuery = sqlQuery.replaceAll("@userId", userId);
			// Get the list of report map
			List<Map<String, Object>> reportMapList = super.executeSelectNative(sqlQuery, queryParameterMap);

			// Check the list having reports or not
			if (reportMapList.size() > 0) {
				Map<String, Object> reportMap = reportMapList.get(0);
				// Converting the report map into the ReportEntity
				report = Base.fromMap(reportMap, Report.class);
				logger.logInfo("MySQLReport", "getLatestReport", "EndgetLatestReport", "Method-End");

			}

		} catch (Exception ex) {
			logger.logException("MySQLReport", "getLatestReport", "Try-Catch",
					"exception in getting the report for logged in user from mysql", ex);
			throw ex;
		}
		return report;
	}

	/**
	 * @see IMySQLReport.getReportByReportId
	 */
	@Override
	public Report getReportById(String reportId) throws Exception {
		logger.logInfo("MySQLReport", "getReportById", "StartgetReportById", "Method-Start");
		try {
			// Creating the query parameter map
			Map<String, Object> queryParameterMap = new HashMap<String, Object>();
			// putting the value of the report
			queryParameterMap.put("reportId", reportId);
			// hsql Query for getting report
			String query = "from Report where reportId = :reportId";
			// getting the report from the database
			List<Report> reportList = super.executeSelect(query, queryParameterMap);
			// Initializing the Report
			Report report = null;
			// Check list of reports is not empty
			if (reportList.size() > 0) {
				report = reportList.get(0);
			}
			logger.logInfo("MySQLReport", "getReportById", "EndgetReportById", "Method-End");
			return report;
		} catch (Exception ex) {
			logger.logException("MySQLReport", "getReportById", "Try-Catch",
					"exception in getting the report by id in mysql", ex);
			throw ex;
		}
	}

	/**
	 * @see IMySQLReport.getTradeLine
	 */
	@Override
	public List<ReportTradeline> getTradeLine(String reportId) throws Exception {
		logger.logInfo("MySQLReport", "getTradeLine", "StartgetTradeLine", "Method Start");
		List<ReportTradeline> reportTradeLineList = null;
		try {
			// Creating the query parameter map
			Map<String, Object> queryParameterMap = new HashMap<String, Object>();
			// putting the value of the report
			queryParameterMap.put("reportId", reportId);
			// hql for getting reportTrade lines
			String queryforTradeLine = "from ReportTradeline where reportId = :reportId";
			// getting Report TradeLine
			reportTradeLineList = super.executeSelect(queryforTradeLine, queryParameterMap);
			logger.logInfo("MySQLReport", "getTradeLine", "EndgetTradeLine", "Method End");
		} catch (Exception ex) {
			logger.logException("MySQLReport", "getTradeLine", "Try-Catch",
					"exception in gtting the tradeLines from mysql", ex);
			throw ex;
		}
		return reportTradeLineList;

	}
}
