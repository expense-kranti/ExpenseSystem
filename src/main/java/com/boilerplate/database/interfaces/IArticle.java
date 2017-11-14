package com.boilerplate.database.interfaces;

import java.util.List;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ArticleEntity;

/**
 * This class provide the method for article related operations regarding data
 * base
 * 
 * @author shiva
 *
 */
public interface IArticle {

	/**
	 * This method is used to save the user article to the data base.
	 * 
	 * @param articleEntity
	 *            this parameter contains the articles details, details
	 *            basically contain the article title and article content
	 */
	public void saveUserArticle(ArticleEntity articleEntity);

	/**
	 * This method is used to get all the user articles which is saved by user
	 * in our data base.
	 * 
	 * @return the list of all the user articles which is saved by user in our
	 *         data base.
	 */
	public List<ArticleEntity> getUserArticle(String userId);

	/**
	 * This method is used to change article approved status to approved.
	 * 
	 * @param articleEntity
	 *            this parameter contains the articles details, details
	 *            basically contain the userId and article Id
	 */
	public void approveArticle(ArticleEntity articleEntity);
}
