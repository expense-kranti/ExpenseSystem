package com.boilerplate.service.implemetations;

import java.io.IOException;

import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.BlogActivityEntity;
import com.boilerplate.service.interfaces.IBlogActivityService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
	 * @see IBlogActivityService.saveActivity
	 */
	@Override
	public void saveActivity(BlogActivityEntity blogActivityEntity){
		//call redis database layer
		blogActivityDataAccess.saveActivity(blogActivityEntity);
	}
}
