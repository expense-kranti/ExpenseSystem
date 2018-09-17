package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;

/**
 * This method is a base of data access layer
 * 
 * @author shiva
 *
 */
public class MySQLBaseDataAccessLayer {

	/**
	 * This is the logger for MySQLBaseDataAccessLayer class
	 */
	private Logger logger = Logger.getInstance(MySQLBaseDataAccessLayer.class);

	/**
	 * This is the session
	 */
	private Session session = null;

	/**
	 * This method creates an object in the database
	 * 
	 * @param t
	 *            The object to be created
	 * @return The object as it stands after creation
	 * @throws Exception
	 *             throws exception in case of any error while creating object
	 *             in the database
	 */
	public <T> T create(T t) throws Exception {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(t);
			// commit the transaction
			transaction.commit();
			return t;
		} catch (Exception ex) {
			logger.logException("MySQLBaseDataAccessLayer", "create", "try-catch block", ex.getMessage(), ex);
			session.getTransaction().rollback();
			throw ex;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**
	 * This method creates a list of objects in the database
	 * 
	 * @param ts
	 *            The list of object's to be created
	 * @return The object as it stands after creation
	 * @throws Exception
	 *             throws exception in case of any error while creating a list
	 *             of objects in the database
	 */
	public <T> List<T> create(List<T> ts) throws Exception {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			for (T t : ts) {
				session.saveOrUpdate(t);
			}
			// commit the transaction
			transaction.commit();
			return ts;
		} catch (Exception ex) {
			logger.logException("MySQLBaseDataAccessLayer", "create with list of objects as parameters",
					"try-catch block", ex.getMessage(), ex);
			throw ex;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**
	 * This method is used to update an item in the database
	 * 
	 * @param t
	 *            The item to be updated
	 * @return The item after update
	 */
	public <T> T update(T t) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(t);
			// commit the transaction
			transaction.commit();
			return t;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**
	 * This api deletes the object from database
	 * 
	 * @param t
	 *            The item to be deleted
	 */
	public <T> void delete(T t) {
		List<Object> objects = new ArrayList<Object>();
		objects.add(t);
		this.delete(objects);
	}

	/**
	 * This method deletes a list of objects
	 * 
	 * @param objects
	 *            The objects to be deleted.
	 */
	public void delete(List<Object> objects) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			// delete the object
			for (Object object : objects) {
				session.delete(object);
			}
			// commit
			transaction.commit();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}

	/**
	 * This method execute the select native query and return the list of object
	 * 
	 * @param sqlQuery
	 *            The query to get the data from the database.
	 * @sqlQuery example: "Select MyBankStatusClick as publishData from
	 *           PublishData where PrimaryKey = :PRIMARYKEY"
	 * @param parameters
	 *            The parameter map parameter: {"PRIMARYKEY":"5425"}
	 * @return The list of object
	 * @response: [{publishData=[[{"result": "Hello world"}, {"result": "Hello
	 *            india"}], [{"result": "Hello world"}, {"result": "Hello
	 *            india"}]]}]
	 * 
	 */
	public List<Map<String, Object>> executeSelectNative(String sqlQuery, Map<String, Object> parameters) {
		Session session = null;
		if (this.session != null) {
			session = this.session;
		}
		try {

			// open a session
			session = HibernateUtility.getSessionFactory().openSession();

			// begin a transaction
			Transaction transaction = session.beginTransaction();
			// create query
			SQLQuery query = session.createSQLQuery(sqlQuery);
			for (String key : parameters.keySet()) {
				query.setParameter(key, (parameters.get(key)));
			}
			// this function convert the data to column name and value pair
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

			List<Map<String, Object>> aliasToValueMapList = query.list();

			transaction.commit();

			return aliasToValueMapList;
		} catch (Exception ex) {
			logger.logException("MySQLBaseDataAccessLayer", "executeSelectNative", "try-catch block",
					ex.getMessage() + "~~~" + ex.fillInStackTrace(), ex);
			session.getTransaction().rollback();
			throw ex;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}// end method

	/**
	 * This method execute the scalor native query and return the number of rows
	 * affected.
	 * 
	 * @param sqlQuery
	 *            The sql query string to update or create the data in the
	 *            database.
	 * @sqlQuery example Update PublishData set MyBankStatusClick =
	 *           '[[{"result": "Hello world"}, {"result": "Hello india"}],
	 *           [{"result": "Hello world"}, {"result": "Hello
	 *           india"}],[{"result": "Hello world"}, {"result": "Hello US"}]]'
	 *           where PrimaryKey = :PRIMARYKEY
	 * @param parameters
	 *            The parameters map .
	 * @parameters map {"PRIMARYKEY":"5425"}
	 * @return number of rows affected
	 * @throws Exception
	 *             This exception is thrown if the mechanism to query the
	 *             database is invalid in the context of the mysql database.
	 */
	public int executeScalorNative(String sqlQuery, Map<String, Object> parameters) throws Exception {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// begin a transaction
			Transaction transaction = session.beginTransaction();
			// get the user using a hsql query
			SQLQuery query = session.createSQLQuery(sqlQuery);
			for (String key : parameters.keySet()) {
				query.setParameter(key, parameters.get(key));
			}
			int rowsEffected = query.executeUpdate();
			transaction.commit();
			return rowsEffected;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}// end method

	/**
	 * This method executes a given query
	 * 
	 * @param hSQLQuery
	 *            The query to be executed
	 * @param queryParameters
	 *            The query parameters
	 * @return result of query execution
	 */
	public <T> List<T> executeSelect(String hSQLQuery, Map<String, Object> queryParameters) {
		Session session = null;
		try {

			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			session.enableFilter("activeFilter").setParameter("activeStatus", new Boolean(true));

			// get the user using a hsql query
			Query query = session.createQuery(hSQLQuery);
			for (String key : queryParameters.keySet()) {
				query.setParameter(key, queryParameters.get(key));
			}
			List<T> ts = query.list();
			return ts;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}// end method

	/**
	 * This method execute the update query
	 * 
	 * @param hSQLQuery
	 *            The hsql query
	 * @param queryParameters
	 *            The query parameters
	 * @return The number of rows affected.
	 */
	public int executeUpdate(String hSQLQuery, Map<String, Object> queryParameters) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// begin a transaction
			Transaction transaction = session.beginTransaction();
			// get the user using a hsql query
			Query query = session.createQuery(hSQLQuery);

			for (String key : queryParameters.keySet()) {
				query.setParameter(key, queryParameters.get(key));
			}
			int rowsEffected = query.executeUpdate();
			transaction.commit();
			return rowsEffected;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}// end method

	/**
	 * This method get the list of object
	 * 
	 * @param claz
	 *            The class
	 * @return The object list
	 */
	public List get(Class claz) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			session.enableFilter("activeFilter").setParameter("activeStatus", new Boolean(true));

			// begin a transaction
			Transaction transaction = session.beginTransaction();
			return session.createCriteria(claz).list();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}

	/**
	 * This method execute the select native query and return the list of
	 * object. This method doesn't open the session if we want to execute the
	 * query in loop then this method will use.
	 * 
	 * @param sqlQuery
	 *            The query to get the data from the database.
	 * @sqlQuery example: "Select MyBankStatusClick as publishData from
	 *           PublishData where PrimaryKey = :PRIMARYKEY"
	 * @param parameters
	 *            The parameter map parameter: {"PRIMARYKEY":"5425"}
	 * @param session
	 *            This is the hibernate session
	 * @return The list of object
	 * @response: [{publishData=[[{"result": "Hello world"}, {"result": "Hello
	 *            india"}], [{"result": "Hello world"}, {"result": "Hello
	 *            india"}]]}]
	 * 
	 */
	public List<Map<String, Object>> executeSelectNative(String sqlQuery, Map<String, Object> parameters,
			Session session) {
		try {
			// begin a transaction
			session.getTransaction().begin();
			// create query
			SQLQuery query = session.createSQLQuery(sqlQuery);
			for (String key : parameters.keySet()) {
				query.setParameter(key, (parameters.get(key)));
			}
			// this function convert the data to column name and value pair
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			List<Map<String, Object>> aliasToValueMapList = query.list();
			session.getTransaction().commit();
			return aliasToValueMapList;
		} catch (JDBCConnectionException jce) {
			this.reinitializeSessionFactory();
			throw jce;
		} catch (Exception ex) {
			logger.logException("MySQLBaseDataAccessLayer", "executeSelectNative returning list of map",
					"try-catch block", ex.getMessage(), ex);
			session.getTransaction().rollback();
			throw ex;
		}

	}// end method

	/**
	 * This method execute the scalor native query and return the number of rows
	 * affected. This method doesn't open the session if we want to execute the
	 * query in loop then this method will use.
	 * 
	 * @param sqlQuery
	 *            The sql query string to update or create the data in the
	 *            database.
	 * @sqlQuery example Update PublishData set MyBankStatusClick =
	 *           '[[{"result": "Hello world"}, {"result": "Hello india"}],
	 *           [{"result": "Hello world"}, {"result": "Hello
	 *           india"}],[{"result": "Hello world"}, {"result": "Hello US"}]]'
	 *           where PrimaryKey = :PRIMARYKEY
	 * @param parameters
	 *            The parameters map .
	 * @param session
	 *            The hibernate session
	 * @parameters map {"PRIMARYKEY":"5425"}
	 * @return number of rows affected
	 * @throws Exception
	 *             This exception is thrown if the mechanism to query the
	 *             database is invalid in the context of the mysql database.
	 */
	public int executeScalorNative(String sqlQuery, Map<String, Object> parameters, Session session) throws Exception {
		// begin a transaction
		try {
			session.getTransaction().begin();
			// get the user using a hsql query
			SQLQuery query = session.createSQLQuery(sqlQuery);
			for (String key : parameters.keySet()) {
				query.setParameter(key, parameters.get(key));
			}
			int rowsEffected = query.executeUpdate();
			session.getTransaction().commit();
			return rowsEffected;
		} catch (Exception ex) {
			logger.logException("MySQLBaseDataAccessLayer", "executeScalorNative", "try-catch block", ex.getMessage(),
					ex);
			session.getTransaction().rollback();
			throw ex;
		}
	}// end method

	/**
	 * This method executes a given query
	 * 
	 * @param hSQLQuery
	 *            The query to be executed
	 * @param initialLimit
	 *            This is the lower limit for getting the data
	 * @param finalLimit
	 *            This is the upper limit for getting the data
	 * @param queryParameters
	 *            The query parameters
	 * @return result of query execution
	 */
	public <T> List<T> executeSelectWilthFirstAndMaxResult(String hSQLQuery, Map<String, Object> queryParameters,
			int initialLimit, int finalLimit) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get the user using a hsql query
			Query query = session.createQuery(hSQLQuery);
			for (String key : queryParameters.keySet()) {
				query.setParameter(key, queryParameters.get(key));
			}
			query.setFirstResult(initialLimit);
			query.setMaxResults(finalLimit);

			List<T> ts = query.list();
			return ts;
		} catch (Exception ex) {
			logger.logException("MySQLBaseDataAccessLayer", "executeScalorNative", "try-catch block", ex.getMessage(),
					ex);
			throw ex;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}// end method

	/**
	 * This method reinitialize the session factory when the jdbc exception
	 * occurs so that invoking between the java and mysql not affect.
	 */
	private void reinitializeSessionFactory() {
		logger.logInfo("MySQLBaseDataAccessLayer", "reinitializeSessionFactory", "jdbc connection occurs", "");
		// rebuild main session factory
		HibernateUtility.reBuildSessionFactory();

	}

	/**
	 * This method is used to get create the session
	 * 
	 * @return the created session
	 */
	public Session openSession() {
		return HibernateUtility.getSessionFactory().openSession();
	}

}