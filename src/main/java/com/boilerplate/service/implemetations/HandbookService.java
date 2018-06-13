package com.boilerplate.service.implemetations;

import com.boilerplate.database.interfaces.IHandbook;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.Handbook;
import com.boilerplate.service.interfaces.IHandbookService;

/**
 * This class implements IHandbookService
 * 
 * @author urvij
 *
 */
public class HandbookService implements IHandbookService {

	/**
	 * This is the instance of mySqlHandbook
	 */
	IHandbook mySqlHandbook;

	/**
	 * Sets the mySqlHandbook
	 * 
	 * @param mySqlHandbook
	 *            the mySqlHandbook to set
	 */
	public void setMySqlHandbook(IHandbook mySqlHandbook) {
		this.mySqlHandbook = mySqlHandbook;
	}

	/** 
	 * @see IHandbookService.publishHandBook
	 */
	@Override
	public Handbook publishHandBook(Handbook handbook) throws Exception {
		// validating input
		handbook.validate();
		// getting userId of currently logged in user and setting it in handbook
		// entity
		handbook.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
		// publish handbook in mysql
		mySqlHandbook.saveHandbook(handbook);
		return handbook;
	}
}
