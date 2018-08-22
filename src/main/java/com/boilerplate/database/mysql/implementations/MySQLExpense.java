package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IExpense;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RDBMSUtility;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseHistoryEntity;
import com.boilerplate.java.entities.FetchExpenseEntity;

/**
 * this class implements IExpense
 * 
 * @author ruchi
 *
 */
public class MySQLExpense extends MySQLBaseDataAccessLayer implements IExpense {

	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(MySQLExpense.class);

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

	/**
	 * @see IExpense.createExpense
	 */
	@Override
	public ExpenseEntity createExpense(ExpenseEntity expenseEntity) throws Exception {
		try {
			// create a new expense entity
			return super.create(expenseEntity);
		} catch (Exception ex) {
			logger.logException("MySQLExpense", "createExpense", "exceptionCreateExpense",
					"Exception occurred while creating a new expense", ex);
			throw ex;
		}
	}

	/**
	 * @see IExpense.updateExpense
	 */
	@Override
	public ExpenseEntity updateExpense(ExpenseEntity expenseEntity) {
		try {
			// update expense entity
			return super.update(expenseEntity);
		} catch (Exception ex) {
			logger.logException("MySQLExpense", "updateExpense", "exceptionUpdateExpense",
					"Exception occurred while updating an expense", ex);
			throw ex;
		}
	}

	/**
	 * @see IExpense.getExpense
	 */
	@Override
	public ExpenseEntity getExpense(String id) throws BadRequestException {
		// Get the SQL query from configurations to get expense
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_EXPENSE_BY_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("ExpenseId", id);
		// This variable is used to hold the query response
		List<ExpenseEntity> expenses = new ArrayList<>();
		try {
			// Execute query
			expenses = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLExpense", "getExpense", "exceptionGetExpense",
					"While trying to get expense data, This is the id~ " + id + "This is the query" + hSQLQuery, ex);
			// Throw exception
			throw new BadRequestException("MySQLExpense", "While trying to get expense data ~ " + ex.toString(), ex);
		}
		if (expenses.size() == 0)
			return null;
		return expenses.get(0);
	}

	/**
	 * @see IExpense.saveExpenseHistory
	 */
	@Override
	public void saveExpenseHistory(ExpenseHistoryEntity expenseHistoryEntity) throws Exception {
		try {
			// create a new expense history entity
			super.create(expenseHistoryEntity);
		} catch (Exception ex) {
			logger.logException("MySQLExpense", "saveExpenseHistory", "exceptionSaveExpenseHistory",
					"Exception occurred while creating a new expense history", ex);
			throw ex;
		}
	}

	/**
	 * @see IExpense.getExpenses
	 */
	@Override
	public List<ExpenseEntity> getExpenses(FetchExpenseEntity fetchExpenseEntity) throws BadRequestException {
		// Get the SQL query from configurations to get expense
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_EXPENSE_BY_USER_ID");
		// construct query for filtering expenses
		hSQLQuery = RDBMSUtility.queryConstructionForGetExpenses(hSQLQuery, fetchExpenseEntity);
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("UserId", fetchExpenseEntity.getUserId());
		// This variable is used to hold the query response
		List<ExpenseEntity> expenses = new ArrayList<>();
		try {
			// Execute query
			expenses = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLExpense", "getExpenses", "exceptionGetExpenses",
					"While trying to get expense data, This is the user id~ " + fetchExpenseEntity.getUserId()
							+ "This is the query" + hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLExpense", "While trying to get expense data ~ " + ex.toString(), ex);
		}
		if (expenses.size() == 0)
			return null;
		return expenses;
	}

}
