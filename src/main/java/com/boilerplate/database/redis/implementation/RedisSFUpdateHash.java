package com.boilerplate.database.redis.implementation;

import java.util.Map;
import java.util.Set;

import com.boilerplate.database.interfaces.ISFUpdateHash;

public class RedisSFUpdateHash extends BaseRedisDataAccessLayer implements ISFUpdateHash {

	/**
	 * @see ISFUpdateHash.hmset
	 */
	@Override
	public void hmset(String key, Map<String, String> hash) {
		super.hmset(key, hash);
	}

	/**
	 * @see ISFUpdateHash.hgetAll
	 */
	@Override
	public Map<String, String> hgetAll(String key) {
		return super.hgetAll(key);
	}

	/**
	 * @see ISFUpdateHash.hget
	 */
	@Override
	public String hget(String key, String field) {
		return super.hget(key, field);
	}

	/**
	 * @see ISFUpdateHash.hset
	 */
	@Override
	public void hset(String key, String field, String value) {
		super.hset(key, field, value);
	}
	
	/**
	 * @see ISFUpdateHash.del
	 */
	@Override
	public void del(String key){
		super.del(key);
	}

	/**
	 * @see ISFUpdateHash.keys
	 */
	public Set<String> keys(String pattern){
		return super.keys(pattern);
	}
	
	/**
	 * @see ISFUpdateHash.smembers
	 */
	@Override
	public Set<String> smembers(String key){
		return super.smembers(key);
	}
	
	/**
	 * @see ISFUpdateHash.sadd
	 */
	@Override
	public void sadd(String key, String members){
		super.sadd(key, members);
	}

	@Override
	public void hdel(String key, String field) {
		super.delMapfield(key, field);
		
	}
}
