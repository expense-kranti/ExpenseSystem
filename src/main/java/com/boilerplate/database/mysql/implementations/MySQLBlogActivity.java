package com.boilerplate.database.mysql.implementations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.BlogActivityEntity;

public class MySQLBlogActivity extends MySQLBaseDataAccessLayer implements IBlogActivity {

	@Override
	public void saveActivity(BlogActivityEntity blogActivityEntity) throws Exception {
		// Save the user blogActivity // update is not working
		super.create(blogActivityEntity);
	}

	@Override
	public void deleteActivity(String userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> getAllBlogUserKeys(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method is used to save data 
	 */
	@Override
	public void mySqlSaveBlogActivity(BlogActivityEntity blogActivityEntity) throws Exception {
		// Save the user blogActivity
		super.create(blogActivityEntity);

	}

	@Override
	public void addInRedisSet(BlogActivityEntity blogActivity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> fetchBlogActivityAndAddInQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteItemFromRedisBlogActivitySet(String blogActivity) {
		// TODO Auto-generated method stub

	}

	@Override
	public BlogActivityEntity getBlogActivity(String blogActivity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getBlogActivityMap(String payload) {
		// TODO Auto-generated method stub
		return null;
	}

}
