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
	private boolean fromMetropolitanCity;
	/**
	 * This is the basic Salary calculated on ctc
	 */
	private long basicSalary;
	/**
	 * This is the 80C investment done
	 */
	private long investmentIn80C;
	/**
	 * This is the 80D investment done
	 */
	private long investmentIn80D;
	/**
	 * This is the 80E investment done
	 */
	private long investmentIn80E;
	/**
	 * This is the section24 investment done
	 */
	private long investmentInSection24;
	/**
	 * This is the 80CCD1B investment done
	 */
	private long investmentIn80CCD1B;
	/**
	 * This is the medical allowance
	 */
	private long medicalAllowance;
	/**
	 * This is the travel allowance
	 */
	private long travelAllowance;
	/**
	 * This is the house rent paid by user per year
	 */
	private long houseRentPaidMonthly;
	/**
	 * This is the hra exempted
	 */
	private long hraExempted;
	/**
	 * This is the estimated tax
	 */
	private long estimatedTax;
	/**
	 * This is the take home salary pre month
	 */
	private long takeHomeSalaryMonthly;
	/**
	 * This is the emailId
	 */
	private String emailId;
	/**
	 * This is the phoneNumber
	 */
	private String phoneNumber;
	/**
	 * This is the firstName
	 */
	private String firstName;

	/**
	 * This is the ctc input
	 */
	private String ctcInput;
	/**
	 * This is the ageInString
	 */
	private String ageInput;
	/**
	 * This is the home rent paid
	 */
	private String homeRentPaidMonthlyInput;
	/**
	 * This is the 80C investment done
	 */
	private String investmentIn80CInput;
	/**
	 * This is the 80D investment done
	 */
	private String investmentIn80DInput;
	/**
	 * @return the ageInput
	 */
	public String getAgeInput() {
		return ageInput;
	}

	/**
	 * @param ageInput the ageInput to set
	 */
	public void setAgeInput(String ageInput) {
		this.ageInput = ageInput;
	}

	/**
	 * @return the homeRentPaidMonthlyInput
	 */
	public String getHomeRentPaidMonthlyInput() {
		return homeRentPaidMonthlyInput;
	}

	/**
	 * @param homeRentPaidMonthlyInput the homeRentPaidMonthlyInput to set
	 */
	public void setHomeRentPaidMonthlyInput(String homeRentPaidMonthlyInput) {
		this.homeRentPaidMonthlyInput = homeRentPaidMonthlyInput;
	}

	/**
	 * @return the investmentIn80CInput
	 */
	public String getInvestmentIn80CInput() {
		return investmentIn80CInput;
	}

	/**
	 * @param investmentIn80CInput the investmentIn80CInput to set
	 */
	public void setInvestmentIn80CInput(String investmentIn80CInput) {
		this.investmentIn80CInput = investmentIn80CInput;
	}

	/**
	 * @return the investmentIn80DInput
	 */
	public String getInvestmentIn80DInput() {
		return investmentIn80DInput;
	}

	/**
	 * @param investmentIn80DInput the investmentIn80DInput to set
	 */
	public void setInvestmentIn80DInput(String investmentIn80DInput) {
		this.investmentIn80DInput = investmentIn80DInput;
	}

	/**
	 * @return the investmentIn80EInput
	 */
	public String getInvestmentIn80EInput() {
		return investmentIn80EInput;
	}

	/**
	 * @param investmentIn80EInput the investmentIn80EInput to set
	 */
	public void setInvestmentIn80EInput(String investmentIn80EInput) {
		this.investmentIn80EInput = investmentIn80EInput;
	}

	/**
	 * @return the investmentInSection24Input
	 */
	public String getInvestmentInSection24Input() {
		return investmentInSection24Input;
	}

	/**
	 * @param investmentInSection24Input the investmentInSection24Input to set
	 */
	public void setInvestmentInSection24Input(String investmentInSection24Input) {
		this.investmentInSection24Input = investmentInSection24Input;
	}

	/**
	 * This is the 80E investment done
	 */
	private String investmentIn80EInput;
	/**
	 * This is the section24 investment done
	 */
	private String investmentInSection24Input;
	/**
	 * This is the ctc for lac abbreviation
	 */
	private double ctcForLacAbreviation;
	/**
	 * This is the uuidOrUserId
	 */
	private String uuidOrUserId;
	/**
	 * This is the userId of the who is calculating the tax
	 */
	private String userId;

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
	 * Gets from metropolitancity
	 * 
	 * @return the fromMetropolitanCity
	 */
	public boolean isFromMetropolitanCity() {
		return fromMetropolitanCity;
	}

	/**
	 * Sets frommetropolitoncity
	 * 
	 * @param fromMetropolitanCity
	 *            the fromMetropolitanCity to set
	 */
	public void setFromMetropolitanCity(boolean fromMetropolitanCity) {
		this.fromMetropolitanCity = fromMetropolitanCity;
	}

	/**
	 * Gets the basic salary
	 * 
	 * @return the basicSalary
	 */
	public long getBasicSalary() {
		return basicSalary;
	}

	/**
	 * Sets the basic salary
	 * 
	 * @param basicSalary
	 *            the basicSalary to set
	 */
	public void setBasicSalary(long basicSalary) {
		this.basicSalary = basicSalary;
	}

	/**
	 * Gets the investment in 80C
	 * 
	 * @return the investmentIn80C
	 */
	public long getInvestmentIn80C() {
		return investmentIn80C;
	}

	/**
	 * Sets the investment in 80C
	 * 
	 * @param investmentIn80C
	 *            the investmentIn80C to set
	 */
	public void setInvestmentIn80C(long investmentIn80C) {
		this.investmentIn80C = investmentIn80C;
	}

	/**
	 * Gets the investment in 80D
	 * 
	 * @return the investmentIn80D
	 */
	public long getInvestmentIn80D() {
		return investmentIn80D;
	}

	/**
	 * Sets the investment in 80D
	 * 
	 * @param investmentIn80D
	 *            the investmentIn80D to set
	 */
	public void setInvestmentIn80D(long investmentIn80D) {
		this.investmentIn80D = investmentIn80D;
	}

	/**
	 * Gets the investment in 80E
	 * 
	 * @return the investmentIn80E
	 */
	public long getInvestmentIn80E() {
		return investmentIn80E;
	}

	/**
	 * Sets the investment in 80E
	 * 
	 * @param investmentIn80E
	 *            the investmentIn80E to set
	 */
	public void setInvestmentIn80E(long investmentIn80E) {
		this.investmentIn80E = investmentIn80E;
	}

	/**
	 * Gets the investment in section 24
	 * 
	 * @return the investmentInSection24
	 */
	public long getInvestmentInSection24() {
		return investmentInSection24;
	}

	/**
	 * Sets the investment in section 24
	 * 
	 * @param investmentInSection24
	 *            the investmentInSection24 to set
	 */
	public void setInvestmentInSection24(long investmentInSection24) {
		this.investmentInSection24 = investmentInSection24;
	}

	/**
	 * Gets the medical allowance
	 * 
	 * @return the medicalAllowance
	 */
	public long getMedicalAllowance() {
		return medicalAllowance;
	}

	/**
	 * Sets the medical allowance
	 * 
	 * @param medicalAllowance
	 *            the medicalAllowance to set
	 */
	public void setMedicalAllowance(long medicalAllowance) {
		this.medicalAllowance = medicalAllowance;
	}

	/**
	 * Gets the travel allowance
	 * 
	 * @return the travelAllowance
	 */
	public long getTravelAllowance() {
		return travelAllowance;
	}

	/**
	 * Sets the travel allowance
	 * 
	 * @param travelAllowance
	 *            the travelAllowance to set
	 */
	public void setTravelAllowance(long travelAllowance) {
		this.travelAllowance = travelAllowance;
	}

	/**
	 * Gets the investment in 80CCD1B
	 * 
	 * @return the investmentIn80CCD1B
	 */
	public long getInvestmentIn80CCD1B() {
		return investmentIn80CCD1B;
	}

	/**
	 * Sets the investment in 80CCD1B
	 * 
	 * @param investmentIn80CCD1B
	 *            the investmentIn80CCD1B to set
	 */
	public void setInvestmentIn80CCD1B(long investmentIn80CCD1B) {
		this.investmentIn80CCD1B = investmentIn80CCD1B;
	}

	/**
	 * Gets the houserent paid monthly
	 * 
	 * @return the houseRentPaidMonthly
	 */
	public long getHouseRentPaidMonthly() {
		return houseRentPaidMonthly;
	}

	/**
	 * Sets the house rent paid monthly
	 * 
	 * @param houseRentPaidMonthly
	 *            the houseRentPaidMonthly to set
	 */
	public void setHouseRentPaidMonthly(long houseRentPaidMonthly) {
		this.houseRentPaidMonthly = houseRentPaidMonthly;
	}

	/**
	 * Gets the hra exempted
	 * 
	 * @return the hraExempted
	 */
	public long getHraExempted() {
		return hraExempted;
	}

	/**
	 * Sets the hra exempted
	 * 
	 * @param hraExempted
	 *            the hraExempted to set
	 */
	public void setHraExempted(long hraExempted) {
		this.hraExempted = hraExempted;
	}

	/**
	 * Gets the estimated tax
	 * 
	 * @return the estimatedTax
	 */
	public long getEstimatedTax() {
		return estimatedTax;
	}

	/**
	 * Sets the estimated tax
	 * 
	 * @param estimatedTax
	 *            the estimatedTax to set
	 */
	public void setEstimatedTax(long estimatedTax) {
		this.estimatedTax = estimatedTax;
	}

	/**
	 * Gets the take home salary monthly
	 * 
	 * @return the takeHomeSalaryMonthly
	 */
	public long getTakeHomeSalaryMonthly() {
		return takeHomeSalaryMonthly;
	}

	/**
	 * Sets the take home salary monthly
	 * 
	 * @param takeHomeSalaryMonthly
	 *            the takeHomeSalaryMonthly to set
	 */
	public void setTakeHomeSalaryMonthly(long takeHomeSalaryMonthly) {
		this.takeHomeSalaryMonthly = takeHomeSalaryMonthly;
	}

	/**
	 * Gets the emaildId
	 * 
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * Sets the emailId
	 * 
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * Gets the phoneNumber
	 * 
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets teh phoneNumber
	 * 
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Gets the firstname
	 * 
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the firstName
	 * 
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * Gets the ctc for abreviation
	 * 
	 * @return the ctcForLacAbreviation
	 */
	public double getCtcForLacAbreviation() {
		return ctcForLacAbreviation;
	}

	/**
	 * Sets the ctc for abreviation
	 * 
	 * @param ctcForLacAbreviation
	 *            the ctcForLacAbreviation to set
	 */
	public void setCtcForLacAbreviation(double ctcForLacAbreviation) {
		this.ctcForLacAbreviation = ctcForLacAbreviation;
	}

	/**
	 * Gets the uuidOrUserId
	 * 
	 * @return the uuidOrUserId
	 */
	public String getUuidOrUserId() {
		return uuidOrUserId;
	}

	/**
	 * Sets the uuidOrUserId
	 * 
	 * @param uuidOrUserId
	 *            the uuidOrUserId to set
	 */
	public void setUuidOrUserId(String uuidOrUserId) {
		this.uuidOrUserId = uuidOrUserId;
	}

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
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// here cause message is made with requirements
		// if (this.getCtc() <= 0)
		// throw new ValidationFailedException("TaxEntity", "Ctc shouold not be
		// zero", null);
		// if (this.getCtc() > 100000000)
		// throw new ValidationFailedException("TaxEntity", "Ctc should not be
		// greater than 10Crore Rupees", null);
		if (this.getCtcForLacAbreviation() <= 0)
			throw new ValidationFailedException("TaxEntity", "Ctc should not be less than zero", null);
		if (this.getCtcForLacAbreviation() > 100000000)
			throw new ValidationFailedException("TaxEntity", "Ctc should not be greater than 10Crore ", null);
		if (this.getAge() < 18)
			throw new ValidationFailedException("TaxEntity", "Age should be atleast 18 years", null);

		return true;
	}

	/**
	 * This method converts input string values to primitive type values
	 */
	public void convertEntityPropertiesStringValuesToPrimitiveTypes() {

		if (this.isNullOrEmpty(this.getCtcInput())) {
			this.setCtcInput("0");
		}
		if (this.isNullOrEmpty(this.getInvestmentIn80CInput())) {
			this.setInvestmentIn80C(0);
		} else {
			this.setInvestmentIn80C(Double.valueOf(this.getInvestmentIn80CInput()).longValue());
		}
		if (this.isNullOrEmpty(this.getInvestmentIn80DInput())) {
			this.setInvestmentIn80D(0);
		} else {
			this.setInvestmentIn80D(Double.valueOf(this.getInvestmentIn80DInput()).longValue());
		}
		if (this.isNullOrEmpty(this.getInvestmentInSection24Input())) {
			this.setInvestmentInSection24(0);
		} else {
			this.setInvestmentInSection24(Double.valueOf(this.getInvestmentInSection24Input()).longValue());
		}
		if (this.isNullOrEmpty(this.getInvestmentIn80EInput())) {
			this.setInvestmentIn80E(0);
		} else {
			this.setInvestmentIn80E(Double.valueOf(this.getInvestmentIn80EInput()).longValue());
		}

		if (this.isNullOrEmpty(this.getAgeInput())) {
			this.setAge(18);
		} else {
			this.setAge((Double.valueOf(this.getAgeInput())).intValue());
		}
		if (this.isNullOrEmpty(this.getHomeRentPaidMonthlyInput())) {
			this.setHouseRentPaidMonthly(0);
		} else {
			this.setHouseRentPaidMonthly(Double.valueOf(this.getHomeRentPaidMonthlyInput()).intValue());
		}

	}

	/**
	 * This method gets and converts ctc value form string to double and then
	 * make it to lac value
	 */
	public void convertCTCTolacValueFromAbbreviatedInput() {
		// //this while loop is to remove all the leading
		// while (this.getCtcInString().charAt(0) == 0) {
		// this.setCtcInString(this.getCtcInString().replaceFirst("0", ""));
		// }
		// if (this.getCtcInString().equals("")) {
		// this.setCtcInString("0");
		// }
		this.setCtcForLacAbreviation((Double.valueOf(this.getCtcInput())) * 100000);
	}

	// this method is used to convert negative inputs to zeros as we are not
	// handling negative input values in chatbot
	public void makeNegativeValuesToZero() {
		if (this.getCtcForLacAbreviation() < 0) {
			this.setCtcForLacAbreviation(0);
		}
		if (this.getInvestmentIn80C() < 0) {
			this.setInvestmentIn80C(0);
		}
		if (this.getInvestmentIn80D() < 0) {
			this.setInvestmentIn80D(0);
		}
		if (this.getInvestmentInSection24() < 0) {
			this.setInvestmentInSection24(0);
		}
		if (this.getInvestmentIn80E() < 0) {
			this.setInvestmentIn80E(0);
		}
		if (this.getInvestmentIn80CCD1B() < 0) {
			this.setInvestmentIn80CCD1B(0);
		}
		if (this.getAge() < 0) {
			this.setAge(18);
		}
		if (this.getHouseRentPaidMonthly() < 0) {
			this.setHouseRentPaidMonthly(0);
		}
	}

	/**
	 * @return the ctcInput
	 */
	public String getCtcInput() {
		return ctcInput;
	}

	/**
	 * @param ctcInput the ctcInput to set
	 */
	public void setCtcInput(String ctcInput) {
		this.ctcInput = ctcInput;
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
