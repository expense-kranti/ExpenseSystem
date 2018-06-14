package com.boilerplate.database.mysql.implementations;

import com.boilerplate.database.interfaces.IHandbook;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.Handbook;

/**
 * This class implements the methods of IHandbook
 * 
 * @author urvij
 *
 */
public class MySQLHandbook extends MySQLBaseDataAccessLayer implements IHandbook {

	/**
	 * This is the logger instance
	 */
	private Logger logger = Logger.getInstance(MySQLHandbook.class);

	/**
	 * @see IHandbook.saveHandbook
	 */
	@Override
	public void saveHandbook(Handbook handbook) throws Exception {
		try {
			// save handbook
			super.create(handbook);
		} catch (Exception ex) {
			logger.logException("MySQLHandbook", "saveHandbook", "ExceptionSaveHandbook",
					"Exception is : " + ex.toString(), ex);
			throw ex;
		}

	}

}
