package com.boilerplate.database.mysql.implementations;

import com.boilerplate.database.interfaces.IMySQLReport;
import com.boilerplate.database.interfaces.IReport;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
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

}
