package com.boilerplate.service.implemetations;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.BlogActivityEntity;
import com.boilerplate.service.interfaces.IBlogActivityService;

/**
 * This class implements IBlogActivityService class
 * 
 * @author urvij
 *
 */
public class BlogActivityService implements IBlogActivityService {

	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
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
	 * @see IBlogActivityService.saveActivity
	 */
	@Override
	public BlogActivityEntity saveActivity(BlogActivityEntity blogActivityEntity) throws Exception {

		blogActivityEntity.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());

		// call redis database layer
		blogActivityDataAccess.saveActivity(blogActivityEntity);

		// if configuration value is true then add redis key
		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			// add blog key in redis.sadd
			blogActivityDataAccess.addInRedisSet(blogActivityEntity);
		}

		return blogActivityEntity;
	}

	/**
	 * @see IBlogActivityService.deleteUserBlogActivityService
	 */
	@Override
	public void deleteUserBlogActivityService(String userId) {
		blogActivityDataAccess.deleteActivity(userId);
	}
}
