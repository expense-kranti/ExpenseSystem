package com.boilerplate.database.mysql.implementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boilerplate.database.interfaces.IMySQLBankData;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.Address;
import com.boilerplate.java.entities.BankData;
import com.boilerplate.java.entities.CreditEnquiry;

/**
 * This class implements methods of IMySQLBankData
 * 
 * @author urvij
 *
 */
public class MySQLBankData extends MySQLBaseDataAccessLayer implements IMySQLBankData {

	/**
	 * ParseExperianReportsForExtraDataObserver logger
	 */
	private static Logger logger = Logger.getInstance(MySQLBankData.class);

	/**
	 * @see IMySQLBankData.getBankDataForAccountNumberAndBankName
	 */
	@Override
	public List<BankData> getBankDataForAccountNumberAndBankName(String accountNumber, String bankName) {
		String hql = "from BankData where accountNumber = :accountNumber and bankName = :bankName";
		Map<String, Object> queryParameterMap = new HashMap<>();
		queryParameterMap.put("accountNumber", accountNumber);
		queryParameterMap.put("bankName", bankName);
		try {
			return super.executeSelect(hql, queryParameterMap);
		} catch (Exception ex) {
			logger.logException("MySQLBankData", "getBankDataForAccountNumberAndBankName", "CatchBlock",
					"Exception is : " + ex.getMessage() + " Exception cause is : " + ex.getCause().toString(), ex);
			throw ex;
		}
	}

	@Override
	public void saveOrUpdateBankData(BankData bankData) throws Exception {
		try {
			// this method saves and updates entity
			super.create(bankData);
		} catch (Exception ex) {
			logger.logException("MySQLBankData", "saveOrUpdateBankData", "CatchBlock",
					"Exception is : " + ex.getMessage() + " Exception cause is : " + ex.getCause().toString(), ex);
			throw ex;
		}

	}

	@Override
	public void saveCreditEnquiry(CreditEnquiry creditEnquiry) throws Exception {
		try {
			super.create(creditEnquiry);
		} catch (Exception ex) {
			logger.logException("MySQLBankData", "saveCreditEnquiries", "catch block",
					"Exception is : " + ex.getMessage() + " Exception cause is : " + ex.getCause().toString(), ex);
			throw ex;
		}
	}

	@Override
	public void saveEmail(String email, String bankName, String accountNumber) throws Exception {
		try {
			String query = "insert into EmailData (BankName, AccountNumber, DataSource, Email) values (:bankName, :accountNumber, 'Experian', :email)";
			Map<String, Object> queryParameterMap = new HashMap<>();
			queryParameterMap.put("accountNumber", accountNumber);
			queryParameterMap.put("bankName", bankName);
			queryParameterMap.put("email", email);

			super.executeScalorNative(query, queryParameterMap);

		} catch (Exception ex) {
			logger.logException("MySQLBankData", "saveEmail", "CatchBlock",
					"Exception is : " + ex.getMessage() + " Exception cause is : " + ex.getCause().toString(), ex);
			throw ex;
		}
	}

	@Override
	public void saveMobileNumber(String mobileNumber, String bankName, String accountNumber) throws Exception {
		try {
			String query = "insert into PhoneData (Number, Type, BankName, AccountNumber, DataSource) values (:number, 'Mobile', :bankName, :accountNumber, 'Experian')";
			Map<String, Object> queryParameterMap = new HashMap<>();
			queryParameterMap.put("number", mobileNumber);
			queryParameterMap.put("accountNumber", accountNumber);
			queryParameterMap.put("bankName", bankName);

			super.executeScalorNative(query, queryParameterMap);

		} catch (Exception ex) {
			logger.logException("MySQLBankData", "saveMobileNumber", "CatchBlock",
					"Exception is : " + ex.getMessage() + " Exception cause is : " + ex.getCause().toString(), ex);
			throw ex;
		}
	}

	@Override
	public void saveAddress(Address address) throws Exception {
		try {
			String query = "insert into AddressData (BankName, AccountNumber, DataSource, Address_1, Address_2, Address_3, Address_4, City, State, Pincode, Type) values (:bankName, :accountNumber, :dataSource, :address_1, :address_2, :address_3, :address_4, :city, :state, :pincode, :type)";
			Map<String, Object> queryParameterMap = new HashMap<>();
			
			queryParameterMap.put("bankName", address.getBankName());
			queryParameterMap.put("accountNumber", address.getAccountNumber());
			queryParameterMap.put("dataSource", address.getDataSource());
			queryParameterMap.put("address_1", address.getFirstLineOfAddress());
			queryParameterMap.put("address_2", address.getSecondLineOfAddress());
			queryParameterMap.put("address_3", address.getThirdLineOfAddress());
			queryParameterMap.put("address_4", address.getFifthLineOfAddress());
			queryParameterMap.put("city", address.getCity());
			queryParameterMap.put("state", address.getState());
			queryParameterMap.put("pincode", address.getZipCode());
			queryParameterMap.put("type", address.getAddressType());

			super.executeScalorNative(query, queryParameterMap);

		} catch (Exception ex) {
			logger.logException("MySQLBankData", "saveAddress", "CatchBlock",
					"Exception is : " + ex.getMessage() + " Exception cause is : " + ex.getCause().toString(), ex);
			throw ex;
		}
	}

}
