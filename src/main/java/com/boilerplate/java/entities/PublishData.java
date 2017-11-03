package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Map;

import com.boilerplate.java.Base;
/**
 * This is publish data Entity for publishing data to third-party CRM 
 * @author mohit
 *
 */
public class PublishData extends Base implements Serializable{
	
	/**
	 * This is the publish method.
	 */
	private String method;
	
	/**
	 * This is the publish subject.
	 */
	private String subject;
	
	/**
	 * This is the dictionary of publish data.
	 */
	Map<String,Object> data;
	
	/**
	 * This method gets the publish method signature
	 * @return method The publish method signature
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * This method sets the publish method signature 
	 * @param method The method signature 
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * This method gets the publish subject
	 * @return subject The publish subject
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * This method sets the publish subject
	 * @param subject The subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
	 * This method gets the data to be publish
	 * @return data The publish data
	 */
	public Map<String,Object> getData() {
		return data;
	}
	
	/**
	 * This method sets the publish data
	 * @param data The publish data
	 */
	public void setData(Map<String,Object> data) {
		this.data = data;
	}

}
