package com.boilerplate.service.implemetations;

import com.boilerplate.database.interfaces.ICheckList;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.CheckListEntity;
import com.boilerplate.service.interfaces.ICheckListService;

/**
 * This class provides services for saving and getting checklist
 * 
 * @author urvij
 *
 */
public class CheckListService implements ICheckListService {

	/**
	 * This is the instance of IcheckList
	 */
	ICheckList checkListDataAccess;

	/**
	 * Sets the checkListDataAccess
	 * 
	 * @param checkListDataAccess
	 *            the checkListDataAccess to set
	 */
	public void setCheckListDataAccess(ICheckList checkListDataAccess) {
		this.checkListDataAccess = checkListDataAccess;
	}

	/**
	 * @see ICheckListService.save
	 */
	@Override
	public CheckListEntity save(CheckListEntity checkListEntity) throws ValidationFailedException {
		checkListEntity.validate();
		checkListEntity.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
		return checkListDataAccess.save(checkListEntity);
	}

	/**
	 * @see ICheckList.getCheckList
	 */
	@Override
	public CheckListEntity getCheckList() throws NotFoundException {
		CheckListEntity checkListEntity = new CheckListEntity();
		checkListEntity.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
		return checkListDataAccess.getCheckList(checkListEntity);
	}
}
