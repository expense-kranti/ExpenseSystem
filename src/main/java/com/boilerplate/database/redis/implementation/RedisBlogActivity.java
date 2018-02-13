package com.boilerplate.database.redis.implementation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.mysql.implementations.MySQLBlogActivity;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
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
	public static final String BlogUserKeyForSet = "AKS_BLOGUSER_MYSQL";

	/**
	 * @see IBlogActivity.saveActivity
	 */
	@Override
	public void saveActivity(BlogActivityEntity blogActivityEntity) {
		// create new map of input activity and save
		Map<String, String> blogActivityMap = new HashMap<>();
		blogActivityMap.put(blogActivityEntity.getActivity(), blogActivityEntity.getAction());
		// save blog activity
		super.hmset(BlogUser + blogActivityEntity.getActivityType() + ":" + blogActivityEntity.getUserId(),
				blogActivityMap);

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

	/**
	 * @see IBlogActivity.addInRedisSet
	 */
	@Override
	public void addInRedisSet(BlogActivityEntity blogActivity) {
		super.sadd(BlogUserKeyForSet, blogActivity.getActivityType() + ":" + blogActivity.getUserId());
	}

	/**
	 * @see IBlogActivity.addInRedisSet
	 */
	@Override
	public void addInRedisSet(String blogActivityType, String userId) {
		super.sadd(BlogUserKeyForSet, blogActivityType + ":" + userId);
	}

	@Override
	public Set<String> fetchBlogActivityAndAddInQueue() {
		return super.smembers(BlogUserKeyForSet);
	}

	/**
	 * @see IReferral.deleteRedisUserIdSet
	 */
	@Override
	public void deleteItemFromRedisBlogActivitySet(String blogActivity) {
		super.srem(BlogUserKeyForSet, blogActivity);
	}

	@Override
	public Map<String, String> getBlogActivityMap(String BlogActivityKey) {
		Map<String, String> blogActivityMap = super.hgetAll(BlogUser + BlogActivityKey);

		return blogActivityMap;
	}

	@Override
	public Set<String> getAllBlogActivityKeys() {
		return super.keys(BlogUser + "*" + "AKS:" + "*");
	}

	

}
