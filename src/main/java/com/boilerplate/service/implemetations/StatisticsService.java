package com.boilerplate.service.implemetations;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.database.interfaces.IAksharArticles;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.StatisticsDataEntity;
import com.boilerplate.service.interfaces.IStatisticsService;

/**
 * This service implements IWordpressService
 * 
 * @author urvij
 *
 */
public class StatisticsService implements IStatisticsService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(StatisticsService.class);

	/**
	 * This is the like key
	 */
	private static final String LIKE_KEY = "LIKE:";

	/**
	 * This is the instance of IWordPress
	 */
	IAksharArticles mySQLAksharArticles;

	/**
	 * Sets the mySQLAksharArticles
	 * 
	 * @param mySQLAksharArticles
	 *            the mySQLAksharArticles to set
	 */
	public void setMySQLAksharArticles(IAksharArticles mySQLAksharArticles) {
		this.mySQLAksharArticles = mySQLAksharArticles;
	}

	/**
	 * This is the instance of blog activity data access
	 */
	IBlogActivity blogActivityDataAccess;

	/**
	 * Sets the blog activity data access
	 * 
	 * @param blogActivityDataAccess
	 */
	public void setBlogActivityDataAccess(IBlogActivity blogActivityDataAccess) {
		this.blogActivityDataAccess = blogActivityDataAccess;
	}

	/**
	 * This is the new instance of assessment class of data layer
	 */
	@Autowired
	IAssessment assessment;

	/**
	 * This method is used to set the assessment
	 * 
	 * @param assessment
	 */
	public void setAssessment(IAssessment assessment) {
		this.assessment = assessment;
	}

	/**
	 * IStatisticalService.getStatistics
	 */
	@Override
	public StatisticsDataEntity getStatistics() throws BadRequestException {

		// create wordpressDataEntity
		StatisticsDataEntity statisticsDataEntity = new StatisticsDataEntity();
		// initialize the value to 0
		statisticsDataEntity.setTotalArticles(BigInteger.ZERO);
		try {
			// get top 5 upload article limit is 5 which is hard
			// coded in query
			statisticsDataEntity.setTopNewArticles(mySQLAksharArticles.getTopNewArticles());
			// get total article count
			List<Map<String, Object>> result = mySQLAksharArticles.getArticleCounts();
			// check if there is any result
			if (result.size() > 0) {
				// cast the value to integer
				statisticsDataEntity.setTotalArticles((BigInteger) result.get(0).get("count"));
			} else {
				// if not data found then set it to 0
				statisticsDataEntity.setTotalArticles(BigInteger.ZERO);
			}
			// get the top recent searches currently top 10 which is hardcoded
			// in query
			statisticsDataEntity.setTopSearchedArticles(mySQLAksharArticles.getTopSearchedArticles());
			// get logged in users articles likes from redis
			Map<String, String> userArticlesLiked = blogActivityDataAccess
					.getBlogActivityMap(LIKE_KEY + RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
			// get liked articles of logged in user
			statisticsDataEntity.setTotalArticlesLiked(userArticlesLiked.size());
			// get top most played surveys in akshar
			statisticsDataEntity.setTopMostPlayedSurveys(assessment.getTopPlayedSurveys());
			// get top most played quizzes in akshar
			statisticsDataEntity.setTopMostPlayedQuizzes(assessment.getTopPlayedQuizzes());
		} catch (Exception ex) {
			logger.logException("", "getWordPressStatistics", "ExceptionGetWordPressStatistics",
					"Exception is : " + ex.toString(), ex);
		}
		// return entity
		return statisticsDataEntity;
	}

}
