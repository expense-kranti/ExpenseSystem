package com.boilerplate.java.entities;

import java.io.Serializable;
import java.net.HttpCookie;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;

public class HttpEntity extends BaseEntity implements Serializable {
	
	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}
	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}
	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}
	
	private String url;
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the requestBody
	 */
	public String getRequestBody() {
		return requestBody;
	}
	/**
	 * @param requestBody the requestBody to set
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the requestHeaders
	 */
	public BoilerplateMap<String, BoilerplateList<String>> getRequestHeaders() {
		return requestHeaders;
	}
	/**
	 * @param requestHeaders the requestHeaders to set
	 */
	public void setRequestHeaders(
			BoilerplateMap<String, BoilerplateList<String>> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}
	/**
	 * @return the requestCookies
	 */
	public BoilerplateList<HttpCookie> getRequestCookies() {
		return requestCookies;
	}
	/**
	 * @param requestCookies the requestCookies to set
	 */
	public void setRequestCookies(BoilerplateList<HttpCookie> requestCookies) {
		this.requestCookies = requestCookies;
	}
	/**
	 * @return the httpResponse
	 */
	public HttpResponse getHttpResponse() {
		return httpResponse;
	}
	/**
	 * @param httpResponse the httpResponse to set
	 */
	public void setHttpResponse(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}

	private String requestBody;
	
	private String method;
	
	private BoilerplateMap<String,BoilerplateList<String>> requestHeaders;
	
	private BoilerplateList<HttpCookie> requestCookies;
	
	private HttpResponse httpResponse;
	/**
	 * This is the constructor of http entity.
	 * @param requestBody The request body
	 * @param method The request method
	 * @param requestHeaders The request headers
	 * @param requestCookies The request cookies.
	 */
	public HttpEntity(String requestBody,String method,
			BoilerplateMap<String,BoilerplateList<String>> requestHeaders,
			BoilerplateList<HttpCookie> requestCookies,String url){
		this.setRequestBody(requestBody);
		this.setRequestHeaders(requestHeaders);
		this.setMethod(method);
		this.setRequestCookies(requestCookies);
		this.setUrl(url);
	}

}
