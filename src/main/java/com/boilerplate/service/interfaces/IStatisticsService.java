package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.WordpressDataEntity;

/**
 * This interface contains methods to operate on wordpress data
 * 
 * @author urvij
 *
 */
public interface IStatisticsService {

	/**
	 * This method is used to get the articles details present in wordpress
	 * 
	 * @return the WordpressDataEntity containing the articlesdetails
	 * @throws BadRequestException
	 *             thrown when wrong query mechanismm is used to mysql database
	 */
	public WordpressDataEntity getWordPressStatistics() throws BadRequestException;
}
