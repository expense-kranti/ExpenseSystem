package com.boilerplate.service.implemetations;

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
	 * @see IHandbookService.publishHandBook
	 */
	@Override
	public Handbook publishHandBook(Handbook handbook) throws ValidationFailedException {
		// validating input
		handbook.validate();
		// getting userId of currently logged in user and setting it in handbook
		// entity
		handbook.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
		return handbook;
	}
}
