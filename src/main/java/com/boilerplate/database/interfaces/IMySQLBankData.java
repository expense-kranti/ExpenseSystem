package com.boilerplate.database.interfaces;

import java.util.List;

import com.boilerplate.java.entities.Address;
import com.boilerplate.java.entities.BankData;
import com.boilerplate.java.entities.CreditEnquiry;

/**
 * This interface has methods to perform crud operations to/from BankData
 * 
 * @author urvij
 *
 */
public interface IMySQLBankData {

	/**
	 * This method is used to get the bank data entry for given account number
	 * and bank name
	 * 
	 * @param accountNumber
	 *            the account number
	 * @param bankName
	 *            the bank name
	 * @return the list of map of columns and respective values
	 */
	public List<BankData> getBankDataForAccountNumberAndBankName(String accountNumber, String bankName);

	/**
	 * This method is used to save credit enquiry
	 * 
	 * @param creditEnquiry
	 *            the credit enquiry entity
	 * @throws Exception 
	 */
	public void saveCreditEnquiry(CreditEnquiry creditEnquiry) throws Exception;

	/**
	 * This method is used to save or update bank data
	 * 
	 * @param bankData
	 *            the bankdata to save or update
	 * @throws Exception
	 */
	public void saveOrUpdateBankData(BankData bankData) throws Exception;

	/**
	 * This method is used to save email
	 * 
	 * @param email
	 *            the email to save
	 * @param bankName
	 *            the bank name
	 * @param accountNumber
	 *            the account number
	 * @throws Exception 
	 */
	public void saveEmail(String email, String bankName, String accountNumber) throws Exception;

	/**
	 * This method is used to save mobile number
	 * 
	 * @param mobileNumber
	 *            the mobile number to save
	 * @param bankName
	 *            the bank name for which to save
	 * @param accountNumber
	 *            the account number for which to save
	 * @throws Exception 
	 */
	public void saveMobileNumber(String mobileNumber, String bankName, String accountNumber) throws Exception;

	/**
	 * This method is used to save address
	 * 
	 * @param address
	 *            the address to save
	 * @throws Exception 
	 */
	public void saveAddress(Address address) throws Exception;
}
