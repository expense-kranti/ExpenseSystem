package com.boilerplate.service.implemetations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IArticle;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ArticleEntity;
import com.boilerplate.service.interfaces.IArticleService;

/**
 * This class implements the IArticleService and perform management related
 * operation for the articles
 * 
 * @author shiva
 *
 */
public class ArticleService implements IArticleService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(UserService.class);

	/**
	 * This is the new instance of article class of data layer
	 */
	IArticle article;

	/**
	 * This method is used to set the IArticle
	 * 
	 * @param article
	 *            the article to set
	 */
	public void setArticle(IArticle article) {
		this.article = article;
	}

	/**
	 * This is an instance of the queue job, to save the session back on to the
	 * database async
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader job
	 * 
	 * @param queueReaderJob
	 *            The queue reader job
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	/**
	 * This is the subjects List For ContactUs
	 */
	BoilerplateList<String> subjectsForArticle = new BoilerplateList();

	/**
	 * Initializes the Bean
	 */
	public void initialize() {
		subjectsForArticle.add("SendEmailArticle");
	}

	/**
	 * @see IArticleService.saveUserArticle
	 */
	@Override
	public void saveUserArticle(ArticleEntity articleEntity) throws ValidationFailedException {
		//Validate articleEntity
		articleEntity.validate();
		// Set the user id
		articleEntity.setUserId(RequestThreadLocal.getSession().getUserId());
		// Set the is approved to false
		articleEntity.setIsApproved(false);
		// Save the user article
		article.saveUserArticle(articleEntity);
		try {
			queueReaderJob.requestBackroundWorkItem(articleEntity, subjectsForArticle, "ArticleService",
					"saveUserArticle");
		} catch (Exception exEmail) {
			// if an exception takes place here we cant do much hence just log
			// it and move forward
			logger.logException("ArticleService", "saveUserArticle", "try-Queue Reader - Send Email",
					exEmail.toString(), exEmail);
		}
	}

	/**
	 * @see IArticleService.getUserArticle
	 */
	@Override
	public List<ArticleEntity> getUserArticle() {
		// Get the user articles
		return article.getUserArticle(RequestThreadLocal.getSession().getUserId());
	}

	/**
	 * @see IArticleService.approveArticle
	 */
	@Override
	public void approveArticle(ArticleEntity articleEntity) {
		// Set approved status to true
		articleEntity.setIsApproved(true);
		article.approveArticle(articleEntity);
	}

}
