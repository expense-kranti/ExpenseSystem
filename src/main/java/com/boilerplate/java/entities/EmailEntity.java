package com.boilerplate.java.entities;

public class EmailEntity {
	
	private String toemail;
	
	private String smtpFrom;
	
	private String smtpFromName;
	
	private String username;
	private String password;
	/**
	 * @return the smtpFromName
	 */
	public String getSmtpFromName() {
		return smtpFromName;
	}
	/**
	 * @param smtpFromName the smtpFromName to set
	 */
	public void setSmtpFromName(String smtpFromName) {
		this.smtpFromName = smtpFromName;
	}
	/**
	 * @return the toemail
	 */
	public String getToemail() {
		return toemail;
	}
	/**
	 * @param toemail the toemail to set
	 */
	public void setToemail(String toemail) {
		this.toemail = toemail;
	}
	/**
	 * @return the smtpFrom
	 */
	public String getSmtpFrom() {
		return smtpFrom;
	}
	/**
	 * @param smtpFrom the smtpFrom to set
	 */
	public void setSmtpFrom(String smtpFrom) {
		this.smtpFrom = smtpFrom;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the smtpHost
	 */
	public String getSmtpHost() {
		return smtpHost;
	}
	/**
	 * @param smtpHost the smtpHost to set
	 */
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	/**
	 * @return the smtpPort
	 */
	public String getSmtpPort() {
		return smtpPort;
	}
	/**
	 * @param smtpPort the smtpPort to set
	 */
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	private String smtpHost;
	
	private String smtpPort;
}
