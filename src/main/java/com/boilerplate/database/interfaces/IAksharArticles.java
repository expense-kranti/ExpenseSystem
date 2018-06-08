package com.boilerplate.database.interfaces;

import java.util.List;
import java.util.Map;

import com.boilerplate.exceptions.rest.BadRequestException;

/**
 * This interface contains methods to operate on data base of wordpress for
 * getting details of articles published on akshar
 * 
 * @author urvij
 *
 */
public interface IAksharArticles {

	/**
	 * This method is used to get the article details from wordpress
	 * 
	 * @return the list of map of articles details from word press
	 * @throws BadRequestException
	 *             thrown when wrong query mechanism is used to mysql database
	 */
	public List<Map<String, Object>> getTopNewArticles() throws BadRequestException;

	/**
	 * This method is used to get the article counts
	 * 
	 * @return returns the article count
	 */
	public List<Map<String, Object>> getArticleCounts();

	/**
	 * This method is used to get the top searched articles with details in
	 * wordpress
	 * 
	 * @return the list of map of top searched articles with details from word
	 *         press
	 * @throws BadRequestException
	 *             thrown when wrong query mechanism is used to mysql database
	 */
	public List<Map<String, Object>> getTopSearchedArticles() throws BadRequestException;
}
