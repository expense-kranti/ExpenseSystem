package com.boilerplate.service.implemetations;

import java.io.IOException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IIncomeTax;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.IncomeTaxEntity;
import com.boilerplate.service.interfaces.IIncomeTaxService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This class implements IIncomeTaxService
 * 
 * @author urvij
 *
 */
public class IncomeTaxService implements IIncomeTaxService {

	/**
	 * These are the user contact details fields to be used to save contact
	 * details
	 */
	public static final String emailIdField = "emailId";
	public static final String phoneNumberField = "phoneNumber";
	public static final String firstNameField = "firstName";

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
	public IncomeTaxEntity calculateSimpleTax(IncomeTaxEntity incomeTaxEntity)
			throws ValidationFailedException, JsonParseException, JsonMappingException, IOException {

		incomeTaxEntity.validate();
		long taxableIncome = 0;
		// getting pre-assumed deductions allowed for income tax calculation
		long maxAllowedDeductions = Integer.parseInt(configurationManager.get("Max_Travel_Allowance_Deduction"))
				+ Integer.parseInt(configurationManager.get("Max_Medical_Allowance_Deduction"))
				+ Integer.parseInt(configurationManager.get("Max_80C_Allowed_Deduction"))
				+ Integer.parseInt(configurationManager.get("Max_80D_Allowed_Deduction"))
				+ Integer.parseInt(configurationManager.get("Max_80CCD_Allowed_Deduction"));

		taxableIncome = (incomeTaxEntity.getCtc() - maxAllowedDeductions) < 0 ? 0
				: (incomeTaxEntity.getCtc() - maxAllowedDeductions);

		// added for chatbot handling
		if (taxableIncome == 0) {
			incomeTaxEntity.setEstimatedTax((long) 0);
			incomeTaxEntity.setTakeHomeSalaryMonthly(
					(long) getTakeHomeSalaryPerMonth(incomeTaxEntity.getCtc(), incomeTaxEntity.getEstimatedTax()));
		} else {
			incomeTaxEntity.setEstimatedTax((long) ((getEstimatedTaxFromSlab(incomeTaxEntity.getAge(), taxableIncome))
					* Double.parseDouble(configurationManager.get("Education_Cess"))));
			incomeTaxEntity.setTakeHomeSalaryMonthly(
					(long) getTakeHomeSalaryPerMonth(incomeTaxEntity.getCtc(), incomeTaxEntity.getEstimatedTax()));
		}

		// // CALCULATE ESTIMATED TAX AND SET IT
		// incomeTaxEntity.setEstimatedTax((long)
		// ((getEstimatedTaxFromSlab(incomeTaxEntity.getAge(), taxableIncome))
		// * Double.parseDouble(configurationManager.get("Education_Cess"))));

		// incomeTaxEntity.setTakeHomeSalaryMonthly(
		// (long) getTakeHomeSalaryPerMonth(incomeTaxEntity.getCtc(),
		// incomeTaxEntity.getEstimatedTax()));
		// maintaining uuid is crucial for maintaining session
		// COMMENTED FOR CHATBOT ONLY DEPLOYMENT
		// if (incomeTaxEntity.getUuid() == null ||
		// incomeTaxEntity.getUuid().equals("")) {
		// incomeTaxEntity.setUuid(getUUID(Integer.valueOf(configurationManager.get("INCOMETAX_UUID_LENGTH"))));
		// }

		// COMMENTED FOR CHATBOTONLY DEPLOYMENT
		// incomeTaxDataAccess.saveIncomeTaxData(incomeTaxEntity);

		return incomeTaxEntity;
	}

	/**
	 * @see IIncomeTaxService.calculateTaxWithInvestments
	 */
	@Override
	public IncomeTaxEntity calculateTaxWithInvestments(IncomeTaxEntity incomeTaxEntity)
			throws NotFoundException, JsonParseException, JsonMappingException, IOException, ValidationFailedException {

		incomeTaxEntity.validate();
		long taxableIncome = 0;
		// check user input investments if larger than max allowed then assign
		// max allowed otherwise input investment
		long exempted80C = (incomeTaxEntity.getInvestmentIn80C() < Integer
				.parseInt(configurationManager.get("Max_80C_Allowed_Deduction")))
						? (incomeTaxEntity.getInvestmentIn80C() < 0 ? 0 : incomeTaxEntity.getInvestmentIn80C())
						: Integer.parseInt(configurationManager.get("Max_80C_Allowed_Deduction"));
		long exempted80D = (incomeTaxEntity.getInvestmentIn80D() < Integer
				.parseInt(configurationManager.get("Max_80D_Allowed_Deduction_ON_INVESTMENT")))
						? (incomeTaxEntity.getInvestmentIn80D() < 0 ? 0 : incomeTaxEntity.getInvestmentIn80D())
						: Integer.parseInt(configurationManager.get("Max_80D_Allowed_Deduction_ON_INVESTMENT"));
		long exempted80E = (incomeTaxEntity.getInvestmentIn80E() < Integer
				.parseInt(configurationManager.get("Max_80E_Allowed_Deduction_ON_INVESTMENT")))
						? (incomeTaxEntity.getInvestmentIn80E() < 0 ? 0 : incomeTaxEntity.getInvestmentIn80E())
						: Integer.parseInt(configurationManager.get("Max_80E_Allowed_Deduction_ON_INVESTMENT"));
		long exemptedSection24 = (incomeTaxEntity.getInvestmentInSection24() < Integer
				.parseInt(configurationManager.get("Max_SECTION24_Allowed_Deduction_ON_INVESTMENT")))
						? (incomeTaxEntity.getInvestmentInSection24() < 0 ? 0
								: incomeTaxEntity.getInvestmentInSection24())
						: Integer.parseInt(configurationManager.get("Max_SECTION24_Allowed_Deduction_ON_INVESTMENT"));
		long exempted80CCD1B = (incomeTaxEntity.getInvestmentIn80CCD1B() < Integer
				.parseInt(configurationManager.get("Max_80CCD1B_Allowed_Deduction_ON_INVESTMENT")))
						? (incomeTaxEntity.getInvestmentIn80CCD1B() < 0 ? 0 : incomeTaxEntity.getInvestmentIn80CCD1B())
						: Integer.parseInt(configurationManager.get("Max_80CCD1B_Allowed_Deduction_ON_INVESTMENT"));

		double ctc = incomeTaxEntity.getCtc();
		incomeTaxEntity
				.setMedicalAllowance(Long.parseLong(configurationManager.get("Max_Medical_Allowance_Deduction")));
		incomeTaxEntity.setTravelAllowance(Long.parseLong(configurationManager.get("Max_Travel_Allowance_Deduction")));
		// calculate total deduction based on investments done
		long totalDeduction = exempted80C + exempted80D + exempted80E + exemptedSection24 + exempted80CCD1B
				+ Integer.parseInt(configurationManager.get("Max_Travel_Allowance_Deduction"))
				+ Integer.parseInt(configurationManager.get("Max_Medical_Allowance_Deduction"));

		double basicSalary = ctc * 0.5;
		double hra = 0;
		double hraFromRentPaid = 0;

		incomeTaxEntity.setBasicSalary((long) basicSalary);
		int age = incomeTaxEntity.getAge();
		// check if user having rented house in metropolitan city then calculate
		// hra accordingly
		// here is the rebate given and hra exempted which is less hra or hra
		// calculated from house rent if paid
		if (incomeTaxEntity.isFromMetropolitanCity()) {
			hra = basicSalary * 0.5;
		} else {
			hra = basicSalary * 0.4;
		}
		incomeTaxEntity.setHouseRentPaidMonthly(
				incomeTaxEntity.getHouseRentPaidMonthly() < 0 ? 0 : incomeTaxEntity.getHouseRentPaidMonthly());
		if (incomeTaxEntity.getHouseRentPaidMonthly() != 0) {
			hraFromRentPaid = ((incomeTaxEntity.getHouseRentPaidMonthly() * 12) - (basicSalary * 0.1));
			hraFromRentPaid = hraFromRentPaid < 0 ? 0 : hraFromRentPaid;
			incomeTaxEntity.setHraExempted((long) (hra < hraFromRentPaid ? hra : hraFromRentPaid));
		} else {
			incomeTaxEntity.setHraExempted(0);
		}
		// add hra in total deduction
		totalDeduction += incomeTaxEntity.getHraExempted();

		taxableIncome = (long) ((ctc - totalDeduction) < 0 ? 0 : (ctc - totalDeduction));

		if (taxableIncome == 0) {
			incomeTaxEntity.setEstimatedTax((long) 0);
		} else {
			// calculate estimated tax by calculating estimated tax from tax
			// slab
			// and then adding education cess on it
			incomeTaxEntity.setEstimatedTax((long) (getEstimatedTaxFromSlab(age, taxableIncome)
					* Double.parseDouble(configurationManager.get("Education_Cess"))));
		}

		// COMMENTED FOR MAKING CHATBOT WORKING
		// // save income tax details in datastore
		// incomeTaxDataAccess.saveIncomeTaxData(incomeTaxEntity);

		return incomeTaxEntity;

	}

	/**
	 * @see IIncomeTaxService.getIncomeTaxData
	 */
	@Override
	public IncomeTaxEntity getIncomeTaxData(IncomeTaxEntity incomeTaxEntity) throws NotFoundException {
		return incomeTaxDataAccess.getIncomeTaxData(incomeTaxEntity.getUuid());
	}

	/**
	 * This method is used to calculate estimated tax according to available tax
	 * slab
	 * 
	 * @param age
	 *            of the user against whom income tax is calculated
	 * @param taxableIncome
	 *            the income on which tax has to be calculated
	 * @return the estimated tax
	 */
	private double getEstimatedTaxFromSlab(int age, long taxableIncome) {
		if (age < 60) {
			if (taxableIncome < 250000) {
				return (0);
			} else if (taxableIncome <= 500000) {
				return ((long) ((taxableIncome - 250000) * 0.05));
			} else if (taxableIncome <= 1000000) {
				return (12500 + (long) ((taxableIncome - 500000) * 0.2));
			} else if (taxableIncome <= 5000000) {
				return (112500 + (long) ((taxableIncome - 1000000) * 0.3));
			} else if (taxableIncome <= 10000000) {
				return (112500 + (long) (((taxableIncome - 1000000) * 0.3) * 1.1));
			} else if (taxableIncome > 10000000) {
				return (112500 + (long) (((taxableIncome - 1000000) * 0.3) * 1.15));
			}
		} else if (age <= 79) {
			if (taxableIncome < 300000) {
				return (0);
			} else if (taxableIncome <= 500000) {
				return ((long) ((taxableIncome - 300000) * 0.05));
			} else if (taxableIncome <= 1000000) {
				return (10000 + (long) ((taxableIncome - 500000) * 0.2));
			} else if (taxableIncome <= 5000000) {
				return (110000 + (long) ((taxableIncome - 1000000) * 0.3));
			} else if (taxableIncome <= 10000000) {
				return (110000 + (long) (((taxableIncome - 1000000) * 0.3) * 1.1));
			} else if (taxableIncome > 10000000) {
				return (110000 + (long) (((taxableIncome - 1000000) * 0.3) * 1.15));
			}
		} // when age >= 80
		else {
			if (taxableIncome < 500000) {
				return (0);
			} else if (taxableIncome <= 1000000) {
				return ((long) ((taxableIncome - 500000) * 0.2));
			} else if (taxableIncome <= 5000000) {
				return (100000 + (long) ((taxableIncome - 1000000) * 0.3));
			} else if (taxableIncome <= 10000000) {
				return (100000 + (long) (((taxableIncome - 1000000) * 0.3) * 1.1));
			} else if (taxableIncome >= 10000000) {
				return (100000 + (long) (((taxableIncome - 1000000) * 0.3) * 1.15));
			}
		}
		return 0;
	}

	/**
	 * @see IIncomeTaxService.saveIncomeTaxUserDetails
	 */
	@Override
	public void saveIncomeTaxUserDetails(IncomeTaxEntity incomeTaxEntity)
			throws JsonParseException, JsonMappingException, IOException {
		incomeTaxDataAccess.saveUserContacts(incomeTaxEntity.getUuid(), emailIdField, incomeTaxEntity.getEmailId());
		incomeTaxDataAccess.saveUserContacts(incomeTaxEntity.getUuid(), phoneNumberField,
				incomeTaxEntity.getPhoneNumber());
		incomeTaxDataAccess.saveUserContacts(incomeTaxEntity.getUuid(), firstNameField, incomeTaxEntity.getFirstName());
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
			uuid = uuid + String.valueOf((char) (randNum + 97));
		}
		return uuid;
	}

	/**
	 * This method is used to calculate take home salary per month
	 * 
	 * @param ctc
	 *            the ctc
	 * @param tax
	 *            the estimated tax calculated
	 * @return the take home salary per month
	 */
	private double getTakeHomeSalaryPerMonth(double ctc, double tax) {
		if (ctc > tax && ctc - tax != 0) {
			return ((ctc - tax) / 12);
		}
		return ctc;
	}
}
