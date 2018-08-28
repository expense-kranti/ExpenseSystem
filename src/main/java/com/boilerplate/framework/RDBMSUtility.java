package com.boilerplate.framework;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.boilerplate.java.entities.FetchExpenseEntity;
import com.boilerplate.java.entities.UserRoleType;

/**
 * This class has methods for query replacing
 * 
 * @author ruchi
 *
 */
public class RDBMSUtility {

	/**
	 * This method is used to modify query for get expenses according to
	 * filtration required
	 * 
	 * @param sqlQuery
	 *            This is the sql query to be modified
	 * @param fetchExpenseEntity
	 *            this entity contains attributes required for filtration
	 * @return Modified sql query
	 */
	public static String queryConstructionForGetExpenses(String sqlQuery, FetchExpenseEntity fetchExpenseEntity) {
		// check if fetchExpenseEntity contains date
		if (fetchExpenseEntity.getStartDate() != null) {
			// replace end date and start date in sql query
			sqlQuery = sqlQuery.replace("@StartDate", fetchExpenseEntity.getStartDate());
			sqlQuery = sqlQuery.replace("@EndDate", fetchExpenseEntity.getEndDate());
		} else {
			// remove date filtering part from sql query
			sqlQuery = sqlQuery.replace(
					"and Date(expense.creationDate) >'@StartDate' and Date(expense.creationDate) <= '@EndDate'", "");
		}

		// check if fetchExpenseEntity contains status of expense
		if (fetchExpenseEntity.getExpenseType() != null)
			// replace status type in sql query
			sqlQuery = sqlQuery.replace("@Status", fetchExpenseEntity.getExpenseType().toString());
		else
			sqlQuery = sqlQuery.replace("and expense.status = '@Status'", "");
		// return modified sql query
		return sqlQuery;

	}

	/**
	 * This method is used to construct query for getting expenses filed under
	 * approvers/super approvers
	 * 
	 * @param query
	 *            this is the query to be modified
	 * @param role
	 *            this is the role for which expenses need to be fetched
	 * @param approverId
	 *            This is the approver's id
	 * @return Modified sql query
	 */
	public static String queryConstructionOfExpensesForApprovers(String query, UserRoleType role, String approverId) {
		// check if role is approver or super approver
		if (role.equals(UserRoleType.Approver))
			query = query.replaceAll("user.SuperApproverId = @ApproverId", "");
		else if (role.equals(UserRoleType.Super_Approver))
			query = query.replaceAll("user.ApproverId = @ApproverId", "");
		// replace approver id
		query = query.replaceAll("@ApproverId", approverId);
		return query;
	}

}
