package com.boilerplate.java.entities;

/**
 * This class is used to manage the top scorer related information
 * 
 * @author shiva
 *
 */
public class TopScorerEntity {

	/**
	 * This is the user first name
	 */
	private String firstName;

	/**
	 * This the user middle name
	 */
	private String middleName;

	/**
	 * This is the user last name
	 */
	private String lastName;

	/**
	 * This is the user total score
	 */
	private String totalScore;

	/**
	 * This is the user rank
	 */
	private String rank;

	/**
	 * This method is used to get the user first name
	 * 
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * This method is used to set the user first name
	 * 
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * This method is used to get the user middle name
	 * 
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * This method is used to set the user middle name
	 * 
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * This method is used to get the user last name
	 * 
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * This method is used to set the user last name
	 * 
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * This method is used to get the user total score
	 * 
	 * @return the totalScore
	 */
	public String getTotalScore() {
		return totalScore;
	}

	/**
	 * This method is used to set the user total score
	 * 
	 * @param totalScore
	 *            the totalScore to set
	 */
	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	/**
	 * This method is used to get the user rank
	 * 
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}

	/**
	 * This method is used to set the user rank
	 * 
	 * @param rank
	 *            the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}

	/**
	 * This constructor is used to construct this entity and set all the
	 * parameters
	 * 
	 * @param firstName
	 *            this is the first name
	 * @param middleName
	 *            this is the middle name
	 * @param lastName
	 *            this is the last name
	 * @param string
	 *            this the profilePicUrl
	 * @param totalScore
	 *            this is the user total score
	 * @param rank
	 *            this is the user rank
	 */
	public TopScorerEntity(String firstName, String middleName, String lastName, String totalScore, String rank) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.totalScore = totalScore;
		this.rank = rank;
	}

}
