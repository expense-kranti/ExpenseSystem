package com.boilerplate.asyncWork;

import com.boilerplate.database.interfaces.IRedisScript;

/**
 * This class run in back ground to publish the user data
 * 
 * @author shiva
 *
 */
public class PublishUserDataObserver implements IAsyncWorkObserver {

	/**
	 * This is the new instance of redis script
	 */
	IRedisScript redisScript;

	/**
	 * This method set the redis Script
	 * 
	 * @param redisScript
	 *            the redisScript to set
	 */
	public void setRedisScript(IRedisScript redisScript) {
		this.redisScript = redisScript;
	}

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		redisScript.getAllUserKeys();
	}
}
