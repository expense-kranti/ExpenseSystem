package com.boilerplate.service.interfaces;

import com.boilerplate.java.entities.IncomeTaxEntity;

/**
 * This class has methods that provide service for Income tax calculation
 * 
 * @author urvij
 *
 */
public interface IIncomeTaxService {

	/**
	 * This method calculates tax with general predefined deductions and not
	 * with other sophisticated deductions invested
	 * 
	 * @return IncomeTaxEntity that contains the close to estimated tax and
	 *         monthly take home salary
	 */
	public IncomeTaxEntity calculateSimpleTax(IncomeTaxEntity incomeTaxEntity);

}
