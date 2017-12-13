package com.boilerplate.database.redis.implementation;

import java.util.Map;
import java.util.Set;

import com.boilerplate.database.interfaces.IRedisScript;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ReferalEntity;

public class RedisScript  extends BaseRedisDataAccessLayer implements IRedisScript {
	
	/**
	 * This is the main key of user in redis data base
	 */
	private static final String User = "USER:AKS";
	
	private static final String ReferredContact = "ReferredContact:";
	
	/**
	 * @see IRedisScript.getAllUserKeys
	 */
	@Override
	public Set<String> getAllUserKeys() {
		Set<String> listOfUserKey = super.keys(User + "*");
		return listOfUserKey;		
	}
	@Override
	public ReferalEntity getReferDetails(String userReferId, String referMedium){
		Map<String, String> referalEntityMap = super.hgetAll(ReferredContact + userReferId + ":" + referMedium);
		ReferalEntity referalEntity = Base.fromMap(referalEntityMap, ReferalEntity.class);
		return referalEntity;
	}
	

}
