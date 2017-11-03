package com.boilerplate.java.entities;

import java.io.UnsupportedEncodingException;
/**
 * This interface creates Publish Url
 * @author mohit
 *
 */
public interface ICRMPublishDynamicURl {
	/**
	 * This method creates the URL to publish
	 * @param url the publish url
	 * @return the publish url string
	 * @throws UnsupportedEncodingException 
	 */
	public String createPublishUrl(String url) throws UnsupportedEncodingException;

}
