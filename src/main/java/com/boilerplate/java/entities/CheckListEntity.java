package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Map;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;

/**
 * This class maintains checklist
 * 
 * @author urvij
 *
 */
public class CheckListEntity extends BaseEntity implements Serializable {

	/**
	 * This is the userId
	 */
	private String userId;
	/**
	 * This is the checklist hash map that contains some elements and their
	 * values
	 */
	private Map<String, String> checkListMap;

	/**
	 * Gets the userId
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the userId
	 * 
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the checklist map
	 * 
	 * @return the checkListMap
	 */
	public Map<String, String> getCheckListMap() {
		return checkListMap;
	}

	/**
	 * Sets the checklist map
	 * 
	 * @param checkListMap
	 *            the checkListMap to set
	 */
	public void setCheckListMap(Map<String, String> checkListMap) {
		this.checkListMap = checkListMap;
	}

	/**
	 * BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// check if checkList map is null or empty
		if (this.getCheckListMap() == null || this.getCheckListMap().isEmpty() )
			throw new ValidationFailedException("CheckListEntity", "checkListMap cannot be null or empty", null);
		return true;
	}

	/**
	 * BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
