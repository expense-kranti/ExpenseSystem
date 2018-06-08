package com.boilerplate.database.mysql.implementations;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IAksharArticles;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.service.implemetations.StatisticsService;

/**
 * This class implements IAksharArticles interface means it has methods
 * operating on articles published on akshar 
 * 
 * @author urvij
 *
 */
public class MySQLAksharArticles extends MySQLBaseDataAccessLayer implements IAksharArticles {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(MySQLAksharArticles.class);

	/**
	 * This is the wordpress hibernate configuration
	 */
	private String wordPressDatabaseConnection = "WordPressDatabaseConnection";

	// session
	Session session = null;

	/**
	 * The instance of configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * Sets the configuration manager
	 * 
	 * @param configurationManager
	 *            The configuration manager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @see IAksharArticles.getArticlesDetails
	 */
	@Override
	public List<Map<String, Object>> getTopNewArticles() throws BadRequestException {
		// query for getting articles in descending order with top 5
		String sqlQuery = configurationManager.get("GET_NEW_ADDED_ARTICLES");
		// set parametes map
		BoilerplateMap<String, Object> queryParameters = new BoilerplateMap<>();

		List<Map<String, Object>> returndata = null;
		try {
			// load new configurations
			session = HibernateUtility.getSessionFactory(configurationManager.get(wordPressDatabaseConnection))
					.openSession();
			// execute query
			returndata = super.executeSelectNative(sqlQuery, queryParameters, session);
		} catch (Exception ex) {
			// log exception
			logger.logException("MySQLAksharArticles", "getTopNewArticles", "ExceptionGetTopNewArticles",
					"Exception is : " + ex.toString(), ex);
		} finally {
			session.flush();
			session.close();
		}
		return returndata;
	}

	/**
	 * @see IAksharArticles.getArticleCounts
	 */
	@Override
	public List<Map<String, Object>> getArticleCounts() {
		// query for getting articles count
		String sqlQuery = configurationManager.get("GET_TOTAL_ARTICLES_COUNT");
		// set parametes map
		BoilerplateMap<String, Object> queryParameters = new BoilerplateMap<>();
		// create session
		List<Map<String, Object>> returndata = null;
		try {
			// load new configurations
			session = HibernateUtility.getSessionFactory(configurationManager.get(wordPressDatabaseConnection))
					.openSession();
			// execute query
			returndata = super.executeSelectNative(sqlQuery, queryParameters, session);
		} catch (Exception ex) {
			// log exception
			logger.logException("MySQLAksharArticles", "getArticleCounts", "ExceptionGetArticleCounts",
					"Exception is : " + ex.toString(), ex);
		} finally {
			session.flush();
			session.close();
		}
		return returndata;
	}

	/**
	 * @see IAksharArticles.getTopSearchedArticles
	 */
	@Override
	public List<Map<String, Object>> getTopSearchedArticles() throws BadRequestException {
		// query for getting top searched articles
		String sqlQuery = configurationManager.get("GET_TOP_SEARCHED_ARTICLES");
		// set parametes map
		BoilerplateMap<String, Object> queryParameters = new BoilerplateMap<>();
		// create session
		List<Map<String, Object>> returndata = null;
		try {
			// load new configurations
			session = HibernateUtility.getSessionFactory(configurationManager.get(wordPressDatabaseConnection))
					.openSession();
			// execute query
			returndata = super.executeSelectNative(sqlQuery, queryParameters, session);
		} catch (Exception ex) {
			// log exception
			logger.logException("MySQLAksharArticles", "getTopSearchedArticles", "ExceptionGetTopSearchedArticles",
					"Exception is : " + ex.toString(), ex);
		} finally {
			session.flush();
			session.close();
		}
		return returndata;
	}

}
