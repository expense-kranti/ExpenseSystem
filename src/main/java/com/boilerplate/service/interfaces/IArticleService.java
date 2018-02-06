package com.boilerplate.service.interfaces;

import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ArticleEntity;

/**
 * This class has the services for article related operations
 * 
 * @author shiva
 *
 */
public interface IArticleService {

	/**
	 * This method is used to save the user article to the data store.
	 * 
	 * @param articleEntity
	 *            this parameter contains the articles details, details
	 *            basically contain the article title and article content
	 * @throws ValidationFailedException
	 *             throw this exception if articleEntity is not valid
	 * @throws Exception
	 */
	public void saveUserArticle(ArticleEntity articleEntity) throws ValidationFailedException, Exception;

	/**
	 * This method is used to get all the user articles which is saved by user
	 * in our data store.
	 * 
	 * @return the list of all the user articles which is saved by user in our
	 *         data store.
	 */
	public List<ArticleEntity> getUserArticle();
	
	/**
	 * This method is used to change article approved status to approved.
	 * 
	 * @param articleEntity
	 *            this parameter contains the articles details, details
	 *            basically contain the userId and article Id
	 * @throws Exception
	 */
	public void approveArticle(ArticleEntity articleEntity) throws Exception;
}
