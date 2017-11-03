package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.UnauthorizedException;

/**
 * This interface creates publish json
 * @author love
 *
 */
public interface ICRMPublishEntity {
	/**
	 * This method creates the data to publish to CRM
	 * @param template The publish template
	 * @return the publish data string
	 * @throws UnauthorizedException 
	 */
	public String createPublishJSON(String template) throws UnauthorizedException;
	
	
}
