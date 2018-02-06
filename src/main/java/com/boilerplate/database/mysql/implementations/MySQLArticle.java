package com.boilerplate.database.mysql.implementations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IArticle;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ArticleEntity;

/**
 * This class implements IAssessment and perform management related operations
 * regarding the article
 * 
 * @author shiva
 *
 */
public class MySQLArticle extends MySQLBaseDataAccessLayer implements IArticle {

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
	 * This variable provide the key for SQL query for configurations this SQL
	 * query is used to get the user article
	 */
	private static String sqlQueryGetUserArticle = "SQL_QUERY_GET_USER_ARTICLE";

	/**
	 * @throws Exception
	 * @see IArticle.saveUserArticle
	 */
	@Override
	public void saveUserArticle(ArticleEntity articleEntity) throws Exception {
		// Set creation time to current time
		articleEntity.setCreationDate(Date.valueOf(LocalDate.now()));
		// Save the user article
		super.create(articleEntity);
	}

	/**
	 * @see IArticle.getUserArticle
	 */
	@Override
	public List<ArticleEntity> getUserArticle(String userId) {
		// Get the SQL query from configurations for get the list of assessment
		String sqlQuery = configurationManager.get(sqlQueryGetUserArticle);
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put user id in query parameter
		queryParameterMap.put("UserId", userId);
		// Get the user articles from the data base
		List<ArticleEntity> requestedDataList = super.executeSelect(sqlQuery, queryParameterMap);
		return requestedDataList;
	}

	/**
	 * @throws Exception
	 * @see IArticle.approveArticle
	 */
	@Override
	public void approveArticle(ArticleEntity articleEntity) throws Exception {
		// Set current date for approved date
		articleEntity.setApprovedDate(Date.valueOf(LocalDate.now()));
		// Update article approve status to approved
		super.create(articleEntity);
	}

}
