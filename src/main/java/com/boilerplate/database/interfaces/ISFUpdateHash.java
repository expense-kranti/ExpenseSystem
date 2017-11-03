package com.boilerplate.database.interfaces;

import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

public interface ISFUpdateHash {

	/**
	 * This method Sets the key hash
	 * @param key The key
	 * @param hash The hash
	 */
	public void hmset(String key, Map<String, String> hash);
	
	/**
	 * This method Gets a key all fields value
	 * @param key The key
	 * @return
	 */
	public Map<String, String> hgetAll(String key);
	
	/**
	 * This method Gets a key field value
	 * @param key The key
	 * @param field The field
	 * @return
	 */
	public String hget(String key, String field);
	
	/**
	 * This method Sets a key field value
	 * @param key The key
	 * @param field The field
	 * @param value The value
	 */
	public void hset(String key, String field, String value);
	
	/**
	 * This method Dels the key
	 * @param key The key
	 */
	public void del(String key);
	
	/**
	 * Getting the key/keys satisfying pattern
	 * @param pattern The pattern
	 * @return The set of keys
	 */
	public Set<String> keys(String pattern);
	
	/**
	 * This method gets the set members
	 * @param key The set key
	 * @return The set of members
	 */
	public Set<String> smembers(String key);
	
	/**
	 * This method adds the members to set access by defined key
	 * @param key The set key
	 * @param members The members
	 */
	public void sadd(String key, String members);
	/**
	 * This method delete the field from the hash map.
	 * @param key The hash map key
	 * @param field The field
	 */
	public void hdel(String key, String field);
}
