package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Report;

public interface IReportService {

	public void save(Report report);

	public BoilerplateMap<String, Report> getReports(String userId) throws UnauthorizedException;

}
