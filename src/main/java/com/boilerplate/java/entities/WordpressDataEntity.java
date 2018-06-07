package com.boilerplate.java.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity contains the wordpress data from db like list of articles ,
 * searches etc
 * 
 * @author urvij
 *
 */
public class WordpressDataEntity extends BaseEntity implements Serializable {

	/**
	 * This is the list of map of articles details posted present in wordpress
	 * db
	 */
	private List<Map<String, Object>> topNewArticles;

	/**
	 * This is the total number of count of articles
	 */
	private BigInteger totalArticles;
	/**
	 * This is the total articles like count
	 */
	private int totalArticlesLiked;

	/**
	 * Gets the total articles liked
	 * 
	 * @return the totalArticlesLiked
	 */
	public int getTotalArticlesLiked() {
		return totalArticlesLiked;
	}

	/**
	 * Sets the total articles liked
	 * 
	 * @param totalArticlesLiked
	 *            the totalArticlesLiked to set
	 */
	public void setTotalArticlesLiked(int totalArticlesLiked) {
		this.totalArticlesLiked = totalArticlesLiked;
	}

	/**
	 * Gets the totalArticles
	 * 
	 * @return the totalArticles
	 */
	public BigInteger getTotalArticles() {
		return totalArticles;
	}

	/**
	 * Sets the totalArticles
	 * 
	 * @param totalArticles
	 *            the totalArticles to set
	 */
	public void setTotalArticles(BigInteger totalArticles) {
		this.totalArticles = totalArticles;
	}

	/**
	 * This is the list of map of top searched articles details wordpress db
	 */
	private List<Map<String, Object>> topSearchedArticles;

	/**
	 * Gets top new articles
	 * 
	 * @return the topNewArticles
	 */
	public List<Map<String, Object>> getTopNewArticles() {
		return topNewArticles;
	}

	/**
	 * Sets top new articles
	 * 
	 * @param topNewArticles
	 *            the topNewArticles to set
	 */
	public void setTopNewArticles(List<Map<String, Object>> topNewArticles) {
		this.topNewArticles = topNewArticles;
	}

	/**
	 * Gets the top searched articles
	 * 
	 * @return the topSearchedArticles
	 */
	public List<Map<String, Object>> getTopSearchedArticles() {
		return topSearchedArticles;
	}

	/**
	 * Sets the top searched articles
	 * 
	 * @param topSearchedArticles
	 *            the topSearchedArticles to set
	 */
	public void setTopSearchedArticles(List<Map<String, Object>> topSearchedArticles) {
		this.topSearchedArticles = topSearchedArticles;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
