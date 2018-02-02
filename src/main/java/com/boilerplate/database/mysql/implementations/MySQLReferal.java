package com.boilerplate.database.mysql.implementations;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ReferredContactDetailEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class MySQLReferal extends MySQLBaseDataAccessLayer implements IReferral {

	@Override
	public void mySqlSaveReferalData(ReferalEntity referalEntity , ReferredContactDetailEntity referalContact) {
		
		// Save the reffered user
		super.create(referalEntity);
		referalEntity.getId();
		referalContact.setReferalId(referalEntity.getId());
		super.create(referalContact);
	}
	
	@Override
	public void saveUserReferUUID(ReferalEntity referalEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUserReferUUID(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserReferredExpireContacts(ReferalEntity referalEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void increaseDayCounter(ReferalEntity referalEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDayCount(ReferalEntity referalEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void increaseReferSignUpCounter(ReferalEntity referalEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDayCounter(ReferalEntity referalEntity, String initialValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUserReferredExpireContacts(ReferalEntity referalEntity, String contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUserReferContacts(ReferalEntity referalEntity,
			ReferredContactDetailEntity updateReferralContactDetailsEntity)
			throws JsonParseException, JsonMappingException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getReferUser(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createSignUpCounter(ReferalEntity referalEntity, String initialValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSignUpCount(ReferalEntity referalEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getUserReferredContacts(ReferalEntity referalEntity, String contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUserAllReferredContactsData(String userReferId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getUserAllReferredContactsKeys(String userReferId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUserAllReferSignUpCountData(String userReferId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getUserAllReferSignUpCountKeys(String userReferId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getUserAllReferCounterKeys(String userReferId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getUserAllReferredExpireContactKeys(String userReferId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUserAllReferCounterData(String userReferId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUserAllReferredExpireContactsData(String userReferId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addInRedisSet(ReferalEntity referalEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> fetchUserReferIdsFromRedisSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteItemFromRedisUserReferIdSet(String userReferId) {
		// TODO Auto-generated method stub
		
	}

	
}
