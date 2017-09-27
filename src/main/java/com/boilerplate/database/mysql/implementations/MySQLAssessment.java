package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.java.Base;
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

	private static String sqlQueryGetAssessment = "SQL_QUERY_GET_ASSESSMENT";
	
	private static String sqlQueryGetAssessmenList = "SQL_QUERY_GET_ASSESSMENT_LIST";

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

	@Override
	public List<AssessmentEntity> getAssessment() {
		String sqlQuery = configurationManager.get(sqlQueryGetAssessmenList);
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		List<Map<String, Object>> requestedDataList = new ArrayList<>();
		List<AssessmentEntity> assessmentEntityList = new ArrayList<>();
		try {
			// Declare the new list to hold the result of SQl query
			requestedDataList = super.executeSelectNative(sqlQuery, queryParameterMap);
			int i = 0;
			while (i < requestedDataList.size()) {
				AssessmentEntity assessmentEntity = Base.fromMap(requestedDataList.get(i), AssessmentEntity.class);
				assessmentEntityList.add(assessmentEntity);
				i++;
			}
			
		} catch (Exception ex) {
			throw ex;
		}
		return assessmentEntityList;
	}

}
