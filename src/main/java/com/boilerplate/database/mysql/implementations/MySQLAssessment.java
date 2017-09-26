package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.java.entities.AssessmentEntity;

public class MySQLAssessment extends MySQLBaseDataAccessLayer implements IAssessment {

	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This method is used to set the configurationManager
	 * 
	 * @param configurationManager
	 *            the configurationManager to set
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	private String sqlQueryGetAssessment = "SQL_QUERY_GET_ASSESSMENT";

	/**
	 * @see IAssessment.getAssessment
	 */
	@Override
	public List<AssessmentEntity> getAssessment(AssessmentEntity assessmentEntity) {
		// Get the SQL query to process the request
		String sqlQuery = configurationManager.get(sqlQueryGetAssessment);
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		queryParameterMap.put("Id", assessmentEntity.getId());
		List<AssessmentEntity> requestedDataList = new ArrayList<>();
		try {
			// Declare the new list to hold the result of SQl query
			requestedDataList = super.executeSelect(sqlQuery, queryParameterMap);
		} catch (Exception ex) {
			throw ex;
		}
		return requestedDataList;
	}

}
