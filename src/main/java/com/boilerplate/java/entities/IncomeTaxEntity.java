/**
 * 
 */
package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class is used to maintain income tax related data
 * 
 * @author urvij
 *
 */
public class IncomeTaxEntity extends BaseEntity implements Serializable {

	/**
	 * This is the uuid to recognize a user
	 */
	private String uuid;
	/**
	 * This is the CTC (cost to company)
	 */
	private long ctc;
	/**
	 * This is the age
	 */
	private int age;
	/**
	 * This is used to represent if tax is being calculated against metropolitan
	 * city dweller
	 */
	private boolean isFromMetropolitanCity;
	/**
	 * This is the estimated tax
	 */
	private double estimatedTax;
	/**
	 * This is the take home salary pre month
	 */
	private double takeHomeSalaryMonthly;

	/**
	 * Gets the uuid
	 * 
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid
	 * 
	 * @param uuid
	 *            the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Gets the ctc
	 * 
	 * @return the ctc
	 */
	public long getCtc() {
		return ctc;
	}

	/**
	 * Sets the ctc
	 * 
	 * @param ctc
	 *            the ctc to set
	 */
	public void setCtc(long ctc) {
		this.ctc = ctc;
	}

	/**
	 * Gets the age
	 * 
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Sets the age
	 * 
	 * @param age
	 *            the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Gets isfromMetropolitanCity
	 * 
	 * @return the isFromMetropolitanCity
	 */
	public boolean isFromMetropolitanCity() {
		return isFromMetropolitanCity;
	}

	/**
	 * Sets isfromMetropolitanCity
	 * 
	 * @param isFromMetropolitanCity
	 *            the isFromMetropolitanCity to set
	 */
	public void setFromMetropolitanCity(boolean isFromMetropolitanCity) {
		this.isFromMetropolitanCity = isFromMetropolitanCity;
	}

	/**
	 * Gets the estimated tax
	 * 
	 * @return the estimatedTax
	 */
	public double getEstimatedTax() {
		return estimatedTax;
	}

	/**
	 * Sets the estimated tax
	 * 
	 * @param estimatedTax
	 *            the estimatedTax to set
	 */
	public void setEstimatedTax(double estimatedTax) {
		this.estimatedTax = estimatedTax;
	}

	/**
	 * Gets the take home salary monthly
	 * 
	 * @return the takeHomeSalaryMonthly
	 */
	public double getTakeHomeSalaryMonthly() {
		return takeHomeSalaryMonthly;
	}

	/**
	 * Sets the take home salary monthly
	 * 
	 * @param takeHomeSalaryMonthly
	 *            the takeHomeSalaryMonthly to set
	 */
	public void setTakeHomeSalaryMonthly(double takeHomeSalaryMonthly) {
		this.takeHomeSalaryMonthly = takeHomeSalaryMonthly;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {

		if (this.getCtc() <= 0)
			throw new ValidationFailedException("TaxEntity", "Ctc should not be zero", null);
		if (this.getAge() < 18)
			throw new ValidationFailedException("TaxEntity", "Age should be greater than or equal to 18 years", null);

		return true;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
