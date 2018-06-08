package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.StatisticsDataEntity;

/**
 * This interface contains methods to get akshar statistical data like number of
 * articles, new articles added, top searched articles, most played articles
 * 
 * @author urvij
 *
 */
public interface IStatisticsService {

	/**
	 * This method is used to get the statistical details like new and top
	 * articles details
	 * 
	 * @return the StatisticsDataEntity containing the articles details
	 * @throws BadRequestException
	 *             thrown when wrong query mechanismm is used to mysql database
	 */
	public StatisticsDataEntity getStatistics() throws BadRequestException;
}
