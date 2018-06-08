package com.boilerplate.service.implemetations;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.database.interfaces.IWordPress;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.WordpressDataEntity;
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
	IWordPress mySQLWordPress;

	/**
	 * @param mySQLWordPress
	 *            the mySQLWordPress to set
	 */
	public void setMySQLWordPress(IWordPress mySQLWordPress) {
		this.mySQLWordPress = mySQLWordPress;
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
	 * IStatisticalService.getArticlesDetails
	 */
	@Override
	public WordpressDataEntity getWordPressStatistics() throws BadRequestException {

		// create wordpressDataEntity
		WordpressDataEntity wordpressDataEntity = new WordpressDataEntity();
		// initialize the value to 0
		wordpressDataEntity.setTotalArticles(BigInteger.ZERO);
		try {
			// get top 5 upload article limit is 5 which is hard
			// coded in query
			wordpressDataEntity.setTopNewArticles(mySQLWordPress.getTopNewArticles());
			// get total article count
			List<Map<String, Object>> result = mySQLWordPress.getArticleCounts();
			// check if there is any result
			if (result.size() > 0) {
				// cast the value to integer
				wordpressDataEntity.setTotalArticles((BigInteger) result.get(0).get("count"));
			} else {
				// if not data found then set it to 0
				wordpressDataEntity.setTotalArticles(BigInteger.ZERO);
			}
			// get the top recent searches currently top 10 which is hardcoded
			// in query
			wordpressDataEntity.setTopSearchedArticles(mySQLWordPress.getTopSearchedArticles());
			// get logged in users articles likes from redis
			Map<String, String> userArticlesLiked = blogActivityDataAccess
					.getBlogActivityMap(LIKE_KEY + RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
			// get liked articles of logged in user 
			wordpressDataEntity.setTotalArticlesLiked(userArticlesLiked.size());
		} catch (Exception ex) {
			logger.logException("", "getWordPressStatistics", "ExceptionGetWordPressStatistics",
					"Exception is : " + ex.toString(), ex);
		}
		// return entity
		return wordpressDataEntity;
	}

}
