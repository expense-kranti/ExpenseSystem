package com.boilerplate.database.mysql.implementations;

import java.util.HashMap;
import java.util.Map;
import com.boilerplate.database.interfaces.IPing;
import com.boilerplate.framework.Logger;
import com.boilerplate.service.implemetations.PingService;

public class MySQLPing extends MySQLBaseDataAccessLayer implements IPing {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(MySQLPing.class);
	private static String defaultCheckQuery = "SELECT 1";

	/**
	 * @see IPing.checkDatabaseConnection
	 */
	@Override
	public void checkDatabaseConnection() {
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		try {
			super.executeSelectNative(defaultCheckQuery, queryParameterMap);
		} catch (Exception ex) {
			logger.logException("MySQLPing", "checkDatabaseConnection", "checkDatabaseConnection",
					ex.toString() + "~~~" + ex.fillInStackTrace(), ex);
			throw ex;
		}
	}

}
