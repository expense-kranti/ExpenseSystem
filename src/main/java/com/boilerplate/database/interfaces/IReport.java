package com.boilerplate.database.interfaces;

import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Report;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.java.entities.ReportTradeline;

public interface IReport {

	public BoilerplateMap<String, Report> getReports(String userId);

	public Object saveReport(Report report) throws Exception;

	

}
