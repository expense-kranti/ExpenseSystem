package com.boilerplate.database.redis.implementation;

import java.util.Map;

import com.boilerplate.database.interfaces.ICheckList;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.CheckListEntity;

/**
 * This class is the redis implementation for checklist
 * 
 * @author urvij
 *
 */
public class RedisCheckList extends BaseRedisDataAccessLayer implements ICheckList {

	/**
	 * This is the key for saving and getting checklist
	 */
	private static final String CheckList = "CHECKLIST:";

	/**
	 * ICheckList.save
	 */
	@Override
	public CheckListEntity save(CheckListEntity checkListEntity) {

		Map<String, String> checkListMapOfUser = super.hgetAll(CheckList + checkListEntity.getUserId());
		// check for checklist map is being saved for first time
		if (checkListMapOfUser == null || checkListMapOfUser.isEmpty()) {
			super.hmset(CheckList + checkListEntity.getUserId(), checkListEntity.getCheckListMap());
		} else {
			for (Map.Entry<String, String> entry : checkListEntity.getCheckListMap().entrySet())
				// do not set new value to existing key if present
				if (!checkListMapOfUser.containsKey(entry.getKey())) {
					super.hset(CheckList + checkListEntity.getUserId(), entry.getKey(), entry.getValue());
				}
		}
		return checkListEntity;
	}

	/**
	 * @see ICheckList.getCheckList
	 */
	@Override
	public CheckListEntity getCheckList(CheckListEntity checkListEntity) throws NotFoundException {

		checkListEntity.setCheckListMap(super.hgetAll(CheckList + checkListEntity.getUserId()));
		if (checkListEntity.getCheckListMap() == null || checkListEntity.getCheckListMap().isEmpty()) {
			throw new NotFoundException("CheckListEntity", "No CheckListMap Found for the user", null);
		}
		return checkListEntity;
	}

}
