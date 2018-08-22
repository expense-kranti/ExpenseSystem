package com.boilerplate.database.mysql.implementations;

import java.util.HashMap;
import java.util.Map;

import com.boilerplate.database.interfaces.IPing;

public class MySQLPing extends MySQLBaseDataAccessLayer implements IPing {
	
	private static String defaultCheckQuery = "SELECT 1";
	/**
	 * @see IPing.checkDatabaseConnection
	 */
	@Override
	public void checkDatabaseConnection() {
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		super.executeSelectNative(defaultCheckQuery, queryParameterMap);
		
	}

}
