package com.boilerplate.service.implemetations;


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
	 * @return 
	 * @see IBlogActivityService.saveActivity
	 */
	@Override
	public BlogActivityEntity saveActivity(BlogActivityEntity blogActivityEntity){
		//call redis database layer
		blogActivityDataAccess.saveActivity(blogActivityEntity);
		blogActivityEntity.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
		return blogActivityEntity;
	}
}
