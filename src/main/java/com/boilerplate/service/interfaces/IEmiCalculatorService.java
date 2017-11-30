package com.boilerplate.service.interfaces;

import javax.xml.bind.ValidationException;

import com.boilerplate.java.entities.EmiDataEntity;

/**
 * This interface declares the methods for services to calculate EMI
 * 
 * @author kranti123
 *
 */
public interface IEmiCalculatorService {

	/**
	 * This method calculates the emi for given loan details
	 * 
	 * @param emiDataEntity
	 *            It contains(as input) the loan amount borrowed, loan period
	 *            for loan, interest rate(per annum) for loan taken
	 * @return The emiDataEntity It contains(as output) the amortized schedule
	 *         data(like interest, principal paid per month in each year), the
	 *         total interest payable,total payment to be done
	 * @throws ValidationException Thrown when one or more of the required fields are empty
	 */
	public EmiDataEntity emiCalculator(EmiDataEntity emiDataEntity) throws ValidationException;

}
