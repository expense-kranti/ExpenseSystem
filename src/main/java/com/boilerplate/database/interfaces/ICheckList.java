package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.CheckListEntity;

/**
 * This interfaces has methods for checklist database management
 * 
 * @author urvij
 *
 */
public interface ICheckList {

	/**
	 * This method is used to save checklistMap for given userId
	 * 
	 * @param checkListEntity
	 *            the checklist entity that contains the userId of the user and
	 *            the checkList of the user
	 * @return the checkList that contains the userId and the checklist
	 */
	public CheckListEntity save(CheckListEntity checkListEntity);

	/**
	 * This method is used to get the CheckListMap for the given userId
	 * 
	 * @param checkListEntity
	 *            containing the userId against whom CheckListMap is to get
	 * @return CheckListEntity containing the checkListMap
	 * @throws NotFoundException
	 *             thrown when no checklist map is found against the logged in
	 *             user
	 */
	public CheckListEntity getCheckList(CheckListEntity checkListEntity) throws NotFoundException;

}
