package com.boilerplate.database.redis.implementation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.database.mysql.implementations.MySQLBlogActivity;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.BlogActivityEntity;

/**
 * This class is a redis implementation of blogactivity
 * 
 * @author urvij
 *
 */
public class RedisBlogActivity extends BaseRedisDataAccessLayer implements IBlogActivity {

	/**
	 * This is the blog user mapping for storing user's blog activity
	 */
	private static final String BlogUser = "BLOGUSER:";
	
	/**
	 * This is the blogUser key used to migrate redis data to mySql
	 */
	public static final String BlogUserKeyForSet = "BLOGUSER";

	/**
	 * @see IBlogActivity.saveActivity
	 */
	@Override
	public void saveActivity(BlogActivityEntity blogActivityEntity) {
		
		
		// create new map of input activity and save
		Map<String, String> blogActivityMap = new HashMap<>();
		blogActivityMap.put(blogActivityEntity.getActivity(), blogActivityEntity.getAction());
		// save blog activity
		super.hmset(BlogUser + blogActivityEntity.getActivityType() + ":"
				+ blogActivityEntity.getUserId(), blogActivityMap);
		
	}

	/**
	 * @see IBlogActivity.deleteActivity
	 */
	@Override
	public void deleteActivity(String userId) {
		Set<String> keys = getAllBlogUserKeys(userId);
		for (String key : keys) {
			super.del(key);
		}
	}

	/**
	 * @see IBlogActivity.getAllBlogUserKeys
	 */
	@Override
	public Set<String> getAllBlogUserKeys(String userId) {
		return super.keys(BlogUser + "*" + userId);
	}

	@Override
	public void mySqlSaveBlogActivity(BlogActivityEntity blogActivityEntity) throws Exception {
		// TODO Auto-generated method stub
		MySQLBlogActivity obj = new MySQLBlogActivity();
		obj.mySqlSaveBlogActivity(blogActivityEntity);
	}

	/**
	 * @see IBlogActivity.addInRedisSet
	 */
	@Override
	public void addInRedisSet(BlogActivityEntity blogActivity) {
		super.sadd(BlogUserKeyForSet,
				blogActivity.getUserId() + ":" + blogActivity.getActivityType() + ":" + blogActivity.getActivity());
	}

}
