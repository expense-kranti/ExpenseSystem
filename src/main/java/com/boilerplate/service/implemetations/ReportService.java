package com.boilerplate.service.implemetations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IMySQLReport;
import com.boilerplate.database.interfaces.IReport;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExperianQuestionAnswer;
import com.boilerplate.java.entities.FileEntity;
import com.boilerplate.java.entities.Report;
import com.boilerplate.java.entities.Role;
import com.boilerplate.service.interfaces.IFileService;
import com.boilerplate.service.interfaces.IReportService;

public class ReportService implements IReportService {

	@Autowired
	IReport reportDataAccess;

	@Autowired
	IFileService fileService;

	@Autowired
	IMySQLReport mysqlReport;

	/**
	 * This is the setter for mysqlReport
	 * 
	 * @param mysqlReport
	 */

	public void setMysqlReport(IMySQLReport mysqlReport) {
		this.mysqlReport = mysqlReport;
	}

	/**
	 * This method set the file service
	 * 
	 * @param fileService
	 *            the fileService to set
	 */
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	public void setReportDataAccess(IReport reportDataAccess) {
		this.reportDataAccess = reportDataAccess;
	}

	@Override
	public void save(Report report) throws Exception {
		reportDataAccess.saveReport(report);
	}

	@Override
	public BoilerplateMap<String, Report> getReports(String userId) throws UnauthorizedException {
		boolean canExecute = false;
		for (Role role : RequestThreadLocal.getSession().getExternalFacingUser().getRoles()) {
			if (role.getRoleName().toUpperCase().equals("ADMIN")
					|| role.getRoleName().toUpperCase().equals("BACKOFFICEUSER")) {
				canExecute = true;
			}
		}

		if (RequestThreadLocal.getSession().getExternalFacingUser().getId().equals(userId)) {
			canExecute = true;
		}

		if (!canExecute)
			throw new UnauthorizedException("Report", "User doesnt have permissions to get Reports", null);

		BoilerplateMap<String, Report> reportMap = reportDataAccess.getReports(userId);
		BoilerplateMap<String, FileEntity> fileEntityMap = fileService.getAllFileList(userId);
		for (String key : reportMap.keySet()) {
			Report report = reportMap.get(key);
			report.setFileEntity(fileEntityMap.get(report.getFileId()));
		}

		return reportMap;
	}

	/**
	 * @see IReportService.getQuestionAnswers
	 */
	@Override
	public ExperianQuestionAnswer getQuestionAnswers(String userId, String questionId) {
		return reportDataAccess.getExperianQuestionAnswer(userId, questionId);
	}

	/**
	 * @see IReport.checkQuestionAnswerExists
	 */
	@Override
	public boolean checkQuestionAnswerExists(String userId) {
		return reportDataAccess.checkQuestionAnswerExists(userId);
	}

	/**
	 * @see IReport.saveExperianQuestionAnswer
	 */
	@Override
	public void saveExperianQuestionAnswer(String userId, String questionId,
			ExperianQuestionAnswer experianQuestionAnswer) {
		reportDataAccess.saveExperianQuestionAnswer(userId, questionId, experianQuestionAnswer);
	}

	/**
	 * @see IReportService.getReport
	 */
	@Override
	public Report getLatestReport() {

		// Get the current logged in user
		String userId = RequestThreadLocal.getSession().getUserId();
		// Getting the report by calling database layer
		return mysqlReport.getLatestReport(userId);
	}
	
	
	/**
	 * This method is used to get the productname on the basis of account type
	 * number
	 * 
	 * @param accountType
	 *            the value for which mapped value to get
	 * @return the product name
	 */
	@Override
	public String getProductName(int accountType) {
		String productName = "";
		switch (accountType) {

		case 0:
			productName = "Other";
			break;
		case 1:
			productName = "AUTO LOAN";
			break;
		case 2:
			productName = "HOUSING LOAN";
			break;
		case 3:
			productName = "PROPERTY LOAN";
			break;
		case 4:
			productName = "LOAN AGAINST SHARES SECURITIES";
			break;
		case 5:
			productName = "PERSONAL LOAN";
			break;
		case 6:
			productName = "CONSUMER LOAN";
			break;
		case 7:
			productName = "GOLD LOAN";
			break;
		case 8:
			productName = "EDUCATIONAL LOAN";
			break;
		case 9:
			productName = "LOAN TO PROFESSIONAL";
			break;
		case 10:
			productName = "CREDIT CARD";
			break;
		case 11:
			productName = "LEASING";
			break;
		case 12:
			productName = "OVERDRAFT LOAN";
			break;
		case 13:
			productName = "TWO WHEELER LOAN";
			break;
		case 14:
			productName = "NON FUNDED CREDIT FACILITY";
			break;
		case 15:
			productName = "LOAN AGAINST BANK DEPOSITS";
			break;
		case 16:
			productName = "FLEET CARD";
			break;
		case 17:
			productName = "Commercial Vehicle LOAN";
			break;
		case 18:
			productName = "Telco Wireless";
			break;
		case 19:
			productName = "Telco Broadband";
			break;
		case 20:
			productName = "Telco Landline";
			break;
		case 31:
			productName = "Secured Credit Card";
			break;
		case 32:
			productName = "Used Car Loan";
			break;
		case 33:
			productName = "Construction Equipment Loan";
			break;
		case 34:
			productName = "Tractor Loan";
			break;
		case 35:
			productName = "CORPORATE CREDIT CARD";
			break;
		case 43:
			productName = "Microfinance Others";
			break;
		case 51:
			productName = "BUSINESS LOAN GENERAL";
			break;
		case 52:
			productName = "BUSINESS LOAN PRIORITY SECTOR SMALL BUSINESS";
			break;
		case 53:
			productName = "BUSINESS LOAN PRIORITY SECTOR AGRICULTURE";
			break;
		case 54:
			productName = "BUSINESS LOAN PRIORITY SECTOR OTHERS";
			break;
		case 55:
			productName = "BUSINESS NON FUNDED CREDIT FACILITY GENERAL";
			break;
		case 56:
			productName = "BUSINESS NON FUNDED CREDIT FACILITY PRIORITY SECTOR SMALL BUSINESS";
			break;
		case 57:
			productName = "BUSINESS NON FUNDED CREDIT FACILITY PRIORITY SECTOR AGRICULTURE";
			break;
		case 58:
			productName = "BUSINESS NON FUNDED CREDIT FACILITY PRIORITY SECTOR OTHERS";
			break;
		case 59:
			productName = "BUSINESS LOANS AGAINST BANK DEPOSITS";
			break;
		case 60:
			productName = "Staff Loan";
			break;

		default:
			productName = "Other";
			break;
		}

		return productName;
	}

}
