package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;

import com.boilerplate.database.interfaces.IIncomeTax;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.IncomeTaxEntity;

/**
 * This class is used to manage income tax data in MySQL database by
 * implementing methods from IIncomeTax
 * 
 * @author urvij
 *
 */
public class MySQLIncomeTax extends MySQLBaseDataAccessLayer implements IIncomeTax {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(MySQLIncomeTax.class);

	/**
	 * This is the instance of configuration manager
	 */
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @see IIncomeTax.saveIncomeTaxData
	 */
	@Override
	public IncomeTaxEntity saveIncomeTaxData(IncomeTaxEntity incomeTaxEntity) throws Exception {
		// save income tax data in MySQL
		try {
			super.create(incomeTaxEntity);
		} catch (Exception ex) {
			logger.logException("MySQLIncomeTax", "saveIncomeTaxData", "Catch block",
					" IncomeTax calculating UserId : " + incomeTaxEntity.getUuidOrUserId() + " Exception message is : "
							+ ex.getMessage() + " Exception Cause is : " + ex.getCause().toString(),
					ex);
			throw ex;
		}
		return incomeTaxEntity;
	}

	/**
	 * @see IIncomeTax.getIncomeTaxData
	 */
	@Override
	public IncomeTaxEntity getIncomeTaxData(String uuidOrUserId) throws NotFoundException {
		// get query template from configuration to get incometax entity if
		// exists
		String query = configurationManager.get("MYSQL_QUERY_TO_GET_INCOME_TAX_DATA");
		// set parameters to be replaced with values
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		queryParameterMap.put("uuidOrUserId", uuidOrUserId);
		List<IncomeTaxEntity> incomeTaxEntityList = new ArrayList<>();
		incomeTaxEntityList = super.executeSelect(query, queryParameterMap);
		if (incomeTaxEntityList.size() > 0)
			return (IncomeTaxEntity) incomeTaxEntityList.get(0);
		return null;
	}

	/**
	 * @see IIncomeTax.saveUserContacts
	 */
	@Override
	public void saveUserContacts(String uuid, String field, String value) {
		// TODO Auto-generated method stub

	}

}
