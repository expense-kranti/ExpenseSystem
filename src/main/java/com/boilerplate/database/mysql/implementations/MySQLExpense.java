package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IExpense;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RDBMSUtility;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.AttachmentEntity;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseHistoryEntity;
import com.boilerplate.java.entities.ExpenseStatusType;
import com.boilerplate.java.entities.FetchExpenseEntity;
import com.boilerplate.java.entities.FileMappingEntity;
import com.boilerplate.java.entities.UserRoleType;

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
	public ExpenseHistoryEntity saveExpenseHistory(ExpenseHistoryEntity expenseHistoryEntity) throws Exception {
		try {
			// create a new expense history entity
			return super.create(expenseHistoryEntity);
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
		queryParameterMap.put("UserId", RequestThreadLocal.getSession().getExternalFacingUser().getId());
		// This variable is used to hold the query response
		List<ExpenseEntity> expenses = new ArrayList<>();
		try {
			// Execute query
			expenses = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLExpense", "getExpenses", "exceptionGetExpenses",
					"While trying to get expense data, This is the user id~ "
							+ RequestThreadLocal.getSession().getExternalFacingUser().getId() + "This is the query"
							+ hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLExpense", "While trying to get expense data ~ " + ex.toString(), ex);
		}
		if (expenses.size() == 0)
			return null;
		return expenses;
	}

	/**
	 * @see IExpense.getExpensesForApprover
	 */
	@Override
	public List<Map<String, Object>> getExpensesForApprover(String approverId, UserRoleType role)
			throws BadRequestException {
		// Get the SQL query from configurations to get expense
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_EXPENSE_BY_APPROVER_OR_SUPER_APPROVER_ID");
		// Modify query
		hSQLQuery = RDBMSUtility.queryConstructionOfExpensesForApprovers(hSQLQuery, role, approverId);
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();

		// This variable is used to hold the query response
		List<Map<String, Object>> expenseMap = new ArrayList<>();
		try {
			// Execute query
			expenseMap = super.executeSelectNative(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLExpense", "getExpensesForApprover", "exceptionGetExpensesForApprover",
					"While trying to get expense data, This is the approverId~ " + approverId + "This is the query"
							+ hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLExpense",
					"While trying to get expense data for approver~ " + ex.toString(), ex);
		}
		return expenseMap;
	}

	/**
	 * @see IExpense.getFileMappingsForExpenses
	 */
	@Override
	public List<FileMappingEntity> getFileMappingsForExpenses(String expenseIds) throws BadRequestException {
		// Get the SQL query from configurations to get expense
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_FILE_MAPPING_BY_LIST_OF_EXPENSE_IDS");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// put expense ids in query parameter map
		// queryParameterMap.put("ExpenseIds", expenseIds);
		hSQLQuery = hSQLQuery.replaceAll(":ExpenseIds", expenseIds);
		// This variable is used to hold the query response
		List<FileMappingEntity> fileMappings = new ArrayList<>();
		try {
			// Execute query
			fileMappings = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLExpense", "getAttachemntsForExpenses", "exceptionGetAttachemntsForExpenses",
					"While trying to get file mappings, This is the list fo expense ids~ " + expenseIds
							+ "This is the query" + hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLExpense",
					"While trying to get expense data for approver~ " + ex.toString(), ex);
		}
		return fileMappings;
	}

	/**
	 * @see IExpense.getExpensesForFinance
	 */
	@Override
	public List<Map<String, Object>> getExpensesForFinance(String financeId, ExpenseStatusType expenseStatus)
			throws BadRequestException {
		// Get the SQL query from configurations to get expense
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_EXPENSE_BY_FINANCE_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// put finance id in query parameter map
		queryParameterMap.put("FinanceId", financeId);
		hSQLQuery = hSQLQuery.replace(":Status", expenseStatus.toString());

		// This variable is used to hold the query response
		List<Map<String, Object>> expenseMap = new ArrayList<>();
		try {
			// Execute query
			expenseMap = super.executeSelectNative(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLExpense", "getExpensesForFinance", "exceptionGetExpensesForFinance",
					"While trying to get expense data, This is the financeId~ " + financeId + "This is the query"
							+ hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLExpense",
					"While trying to get expense data for finance~ " + ex.toString(), ex);
		}
		return expenseMap;
	}

	/**
	 * @see IExpense.saveExpenseList
	 */
	@Override
	public void saveExpenseList(List<ExpenseEntity> expenses) throws Exception {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			// for each roles
			for (ExpenseEntity expense : expenses)
				// save expense in MySQL
				session.saveOrUpdate(expense);
			// commit the transaction
			transaction.commit();
		} catch (Exception ex) {
			logger.logException("MySQLExpense", "update/create expenses", "try-catch block", ex.getMessage(), ex);
			session.getTransaction().rollback();
			throw ex;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

}
