package com.boilerplate.database.interfaces;

import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Report;

public interface IReport {

	public BoilerplateMap<String, Report> getReports(String userId);

	public Object save(Report report);

}
