package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.CheckListEntity;

public interface ICheckListService {

	/**
	 * This method is used to save the checklistmap against logged in user
	 * 
	 * @param the
	 *            checklistentity containing the checklistmap to save
	 * @return the CheckListEntity with userId of logged in user against whom
	 *         checklist is saved
	 * @throws ValidationFailedException
	 *             thrown if input checklistmap is null or empty
	 */
	public CheckListEntity save(CheckListEntity checkListEntity) throws ValidationFailedException;

	/**
	 * This method is used to get the checklist of logged in user
	 * 
	 * @return CheckListEntity that contains the checklist map
	 * @throws NotFoundException
	 *             thrown when no checklist map is found against the logged in
	 *             user
	 */
	public CheckListEntity getCheckList() throws NotFoundException;

}
