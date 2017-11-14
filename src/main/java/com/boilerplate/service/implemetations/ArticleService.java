package com.boilerplate.service.implemetations;

import java.util.List;

import com.boilerplate.database.interfaces.IArticle;
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
	 * @see IArticleService.saveUserArticle
	 */
	@Override
	public void saveUserArticle(ArticleEntity articleEntity) {
		// Set the user id
		articleEntity.setUserId(RequestThreadLocal.getSession().getUserId());
		// Set the is approved to false
		articleEntity.setIsApproved(false);
		// Save the user article
		article.saveUserArticle(articleEntity);
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
		article.approveArticle(articleEntity);
	}

}
