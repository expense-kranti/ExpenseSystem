package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IReport;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.RequestThreadLocal;
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

	/**
	 * This method set the file service
	 * @param fileService the fileService to set
	 */
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	public void setReportDataAccess(IReport reportDataAccess) {
		this.reportDataAccess = reportDataAccess;
	}
	
	@Override
	public void save(Report report) {
		reportDataAccess.save(report);
	}
	
	@Override
	public BoilerplateMap<String, Report> getReports(String userId)
			throws UnauthorizedException {
		boolean canExecute = false;
		for (Role role : RequestThreadLocal.getSession().getExternalFacingUser()
				.getRoles()) {
			if (role.getRoleName().toUpperCase().equals("ADMIN") || role
					.getRoleName().toUpperCase().equals("BACKOFFICEUSER")) {
				canExecute = true;
			}
		}

		if (RequestThreadLocal.getSession().getExternalFacingUser().getId()
				.equals(userId)) {
			canExecute = true;
		}

		if (!canExecute)
			throw new UnauthorizedException("Report",
					"User doesnt have permissions to get Reports", null);

		BoilerplateMap<String, Report> reportMap = reportDataAccess
				.getReports(userId);
		BoilerplateMap<String, FileEntity> fileEntityMap = fileService
				.getAllFileList(userId);
		for (String key : reportMap.keySet()) {
			Report report = reportMap.get(key);
			report.setFileEntity(fileEntityMap.get(report.getFileId()));
		}

		return reportMap;
	}

}
