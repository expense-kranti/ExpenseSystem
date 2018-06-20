package com.boilerplate.service.implemetations;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IMySQLReport;
import com.boilerplate.database.interfaces.IReport;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
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
	 * @see IReportService.getReport
	 */
	@Override
	public Report getReport() throws NotFoundException {

		// Get the current logged in user
		String userId = RequestThreadLocal.getSession().getUserId();
		// Getting the report by calling database layer
		List<Map<String, Object>> reportMapList = mysqlReport.getReport(userId);
		Report report = null;
		// Check the list having report or not
		if (reportMapList.size() > 0) {
			Map<String, Object> reportMap = reportMapList.get(0);
			// converting the report map into the ReportEntity
			report = Base.fromMap(reportMap, Report.class);
		} else
			throw new NotFoundException("Report", "No such report found for the given report id", null);
		return report;
	}

	/**
	 * @see IReportService.getReportByReportId
	 */
	@Override
	public Report getReportByReportId(String reportId) throws NotFoundException, Exception {

		// getting the report from the database
		List<Report> reportList = mysqlReport.getReportByReportId(reportId);
		// Initializing the Report entity
		Report report = null;
		// check list of report is not empty
		if (reportList.size() > 0) {
			report = reportList.get(0);
		} else
			throw new NotFoundException("Report", "No such report found for the given report id", null);
		return report;

	}

}
