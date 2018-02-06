package com.boilerplate.service.implemetations;


import java.sql.Date;
import java.time.LocalDate;

import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.BlogActivityEntity;
import com.boilerplate.service.interfaces.IBlogActivityService;

/**
 * This class implements IBlogActivityService class
 * @author urvij
 *
 */
public class BlogActivityService implements IBlogActivityService {

	/**
	 * This is the instance of blog activity data access
	 */
	IBlogActivity blogActivityDataAccess;
	
	/**
	 * Sets the blog activity data access
	 * @param blogActivityDataAccess
	 */
	public void setBlogActivityDataAccess(IBlogActivity blogActivityDataAccess) {
		this.blogActivityDataAccess = blogActivityDataAccess;
	}

	/** 
	 * @throws Exception 
	 * @see IBlogActivityService.saveActivity
	 */
	@Override
	public BlogActivityEntity saveActivity(BlogActivityEntity blogActivityEntity) throws Exception{
		
		blogActivityEntity.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
		
		//call redis database layer
		blogActivityDataAccess.saveActivity(blogActivityEntity);
		
		// add blog key in redis.sadd 
		blogActivityDataAccess.addInRedisSet(blogActivityEntity);
		
		// save blog activity in MySql
		//blogActivityDataAccess.mySqlSaveBlogActivity(blogActivityEntity);
		return blogActivityEntity;
	}
	/**
	 * @see IBlogActivityService.deleteUserBlogActivityService
	 */
	@Override
	public void deleteUserBlogActivityService(String userId){
		blogActivityDataAccess.deleteActivity(userId);
	}
}
