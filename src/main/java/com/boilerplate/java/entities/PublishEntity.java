package com.boilerplate.java.entities;

import java.io.Serializable;

public class PublishEntity implements Serializable{

	/**
	 * This is the mehtod of publish entity.
	 */
	private String method;
	
	/**
	 * This is the input of publish entity
	 */
	private Object[] input;
	
	/**
	 * This is the return value of the publish entity.
	 */
	private Object returnValue;

	/**
	 * This method gets the method of publish entity.
	 * @return method The method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * This method sets the method of publish entity. 
	 * @param method The method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * This method gets the input of the publish entity
	 * @return input The input
	 */
	public Object[] getInput() {
		return input;
	}

	/**
	 * This method sets the input of the publish entity.
	 * @param input The input
	 */
	public void setInput(Object[] input) {
		this.input = input;
	}

	/**
	 * This method gets the return value of publish entity.
	 * @return returnValue The returnValue
	 */
	public Object getReturnValue() {
		return returnValue;
	}

	/**
	 * This method sets the return value of the publish entity.
	 * @param returnValue The returnValue
	 */
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	
	/**
	 * This is the url on which we want to publish
	 */
	private String url;
	
	/**
	 * This method gets the url on which we want to post the publish entity.
	 * @return url The url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * This method sets the url on which we want to post the publish entity
	 * @param url The url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * This method gets the publish method
	 * @return publishMethod
	 */
	public String getPublishMethod() {
		return publishMethod;
	}

	/**
	 * This method sets the publish method.
	 * @param publishMethod The publish method
	 */
	public void setPublishMethod(String publishMethod) {
		this.publishMethod = publishMethod;
	}

	/**
	 * This method get the publish subject.
	 * @return publishSubject The publishSubject
	 */
	public String getPublishSubject() {
		return publishSubject;
	}

	/**
	 * This method sets the publish subject
	 * @param publishSubject The publish subject
	 */
	public void setPublishSubject(String publishSubject) {
		this.publishSubject = publishSubject;
	}
	/**
	 * This method get the publish template.
	 * @return publishTemplate The publishTemplate
	 */
	public String getPublishTemplate() {
		return publishTemplate;
	}
	/**
	 * This method sets the publish template
	 * @param publishTemplate The publish template
	 */
	public void setPublishTemplate(String publishTemplate) {
		this.publishTemplate = publishTemplate;
	}

	
	/**
	 * This is the publish method
	 */
	private String publishMethod;
	
	/**
	 * This is the publish subject
	 */
	private String publishSubject;
	/**
	 * This is the publish template
	 */
	private String publishTemplate;
	/**
	 * This tells if dynamic url is required for publish
	 */
	private boolean isDynamicPublishURl;
	/**
	 * This method get the dynamic publish URL.
	 * @return the dynamic url
	 */
	public boolean isDynamicPublishURl() {
		return isDynamicPublishURl;
	}
	/**
	 * This method set the dynamic publish URL.
	 * @return isDynamicPublishURl the dynamic url
	 */
	public void setDynamicPublishURl(boolean isDynamicPublishURl) {
		this.isDynamicPublishURl = isDynamicPublishURl;
	}
	
}
