package com.boilerplate.service.implemetations;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IIncomeTax;
import com.boilerplate.java.entities.IncomeTaxEntity;
import com.boilerplate.service.interfaces.IIncomeTaxService;

/**
 * This class implements IIncomeTaxService
 * 
 * @author urvij
 *
 */
public class IncomeTaxService implements IIncomeTaxService {

	// ADD DEPENDENCY IN ROOT CONTEXT
	/**
	 * This is an instance of IIncomeTax
	 */
	IIncomeTax incomeTaxDataAccess;

	/**
	 * Sets the incomeTaxDataAccess
	 * 
	 * @param incomeTaxDataAccess
	 *            the incomeTaxDataAccess to set
	 */
	public void setIncomeTaxDataAccess(IIncomeTax incomeTaxDataAccess) {
		this.incomeTaxDataAccess = incomeTaxDataAccess;
	}

	/**
	 * This is the instance of configuration manager
	 */
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @see IIncomeTaxService.calculateSimpleTax
	 */
	@Override
	public IncomeTaxEntity calculateSimpleTax(IncomeTaxEntity incomeTaxEntity) {

		long maxAllowedDeductions = Integer.parseInt(configurationManager.get("Max_Travel_Allowance_Deduction"))
				+ Integer.parseInt(configurationManager.get("Max_Medical_Allowance_Deduction"))
				+ Integer.parseInt(configurationManager.get("Max_80C_Allowed_Deduction"))
				+ Integer.parseInt(configurationManager.get("Max_80D_Allowed_Deduction"))
				+ Integer.parseInt(configurationManager.get("Max_80CCD_Allowed_Deduction"));

		incomeTaxEntity.setEstimatedTax((incomeTaxEntity.getCtc() - maxAllowedDeductions) < 0 ? 0
				: (incomeTaxEntity.getCtc() - maxAllowedDeductions));

		incomeTaxEntity.setTakeHomeSalaryMonthly(
				getTakeHomeSalaryPerMonth(incomeTaxEntity.getCtc(), incomeTaxEntity.getEstimatedTax()));
		incomeTaxEntity.setUuid(getUUID(Integer.valueOf(configurationManager.get("INCOMETAX_UUID_LENGTH"))));

		incomeTaxDataAccess.saveIncomeTaxData(incomeTaxEntity);

		return incomeTaxEntity;
	}

	/**
	 * This method generates a uuid
	 * 
	 * @param uuidLength
	 *            the length of the uuid to generate
	 * @return the generated uuid
	 */
	private String getUUID(Integer uuidLength) {
		Random rand = new Random();
		String uuid = "";
		for (int i = 0; i < uuidLength; i++) {
			int randNum = rand.nextInt(26 - 0);
			uuid += String.valueOf((char) randNum + 97);
		}
		return uuid;
	}

	private double getTakeHomeSalaryPerMonth(double ctc, double tax) {
		return ((ctc - tax) / 12);
	}
}
