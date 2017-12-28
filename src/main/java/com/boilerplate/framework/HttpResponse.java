package com.boilerplate.framework;

import java.net.HttpCookie;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;

/**
 * This class holds the data related to http response
 * 
 * @author
 *
 */
public class HttpResponse {
	/**
	 * This is the url
	 */
	String url;
	/**
	 * This is the requestHeaders
	 */
	BoilerplateMap<String, BoilerplateList<String>> requestHeaders;
	/**
	 * This is the requestCookies
	 */
	BoilerplateList<HttpCookie> requestCookies;
	/**
	 * This is the requestBody
	 */
	String requestBody;
	/**
	 * This is the responseHeaders
	 */
	java.util.Map<String, java.util.List<String>> responseHeaders;
	/**
	 * This is the responseCookies
	 */
	java.util.List<HttpCookie> responseCookies;
	/**
	 * This is the responseBody
	 */
	String responseBody;
	/**
	 * This is the method
	 */
	String method;

	/**
	 * Gets the url
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url
	 * 
	 * @param url
	 *            to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the requestHeaders
	 * 
	 * @return the requestHeaders
	 */
	public BoilerplateMap<String, BoilerplateList<String>> getRequestHeaders() {
		return requestHeaders;
	}

	/**
	 * Sets the requestHeaders
	 * 
	 * @param requestHeaders
	 *            to set
	 */
	public void setRequestHeaders(BoilerplateMap<String, BoilerplateList<String>> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	/**
	 * Gets the requestCookies
	 * 
	 * @return the requestCookies
	 */
	public BoilerplateList<HttpCookie> getRequestCookies() {
		return requestCookies;
	}

	public void setRequestCookies(BoilerplateList<HttpCookie> requestCookies) {
		this.requestCookies = requestCookies;
	}

	/**
	 * Gets the requestBody
	 * 
	 * @return the requestBody
	 */
	public String getRequestBody() {
		return requestBody;
	}

	/**
	 * Sets the requestBody
	 * 
	 * @param requestBody
	 *            to set
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	/**
	 * Gets the responseHeaders
	 * 
	 * @return the responseHeaders
	 */
	public java.util.Map<String, java.util.List<String>> getResponseHeaders() {
		return responseHeaders;
	}

	/**
	 * Sets the responseHeaders
	 * 
	 * @param responseHeaders
	 *            to set
	 */
	public void setResponseHeaders(java.util.Map<String, java.util.List<String>> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	/**
	 * Gets the responseCookies
	 * 
	 * @return the responseCookies
	 */
	public java.util.List<HttpCookie> getResponseCookies() {
		return responseCookies;
	}

	/**
	 * Sets the responseCookies
	 * 
	 * @param responseCookies
	 *            to set
	 */
	public void setResponseCookies(java.util.List<HttpCookie> responseCookies) {
		this.responseCookies = responseCookies;
	}

	/**
	 * Gets the responseBody
	 * 
	 * @return the responseBody
	 */
	public String getResponseBody() {
		return responseBody;
	}

	/**
	 * Sets the responseBody
	 * 
	 * @param responseBody
	 *            to set
	 */
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	/**
	 * Gets the method
	 * 
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * This is the method
	 * 
	 * @param method
	 *            to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * This is the httpStatus
	 */
	public int httpStatus;

	/**
	 * Gets the httpStatus
	 * 
	 * @return
	 */
	public int getHttpStatus() {
		return httpStatus;
	}

	/**
	 * Sets the httpStatus
	 * 
	 * @param httpStatus
	 *            to set
	 */
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

}
