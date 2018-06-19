package com.boilerplate.asyncWork;

import java.io.File;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.boilerplate.database.interfaces.IMySQLReport;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Address;
import com.boilerplate.java.entities.ElectronicContact;
import com.boilerplate.java.entities.ExperianTradelineStatus;
import com.boilerplate.java.entities.FileEntity;
import com.boilerplate.java.entities.Report;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.java.entities.ReportSource;
import com.boilerplate.java.entities.ReportStatus;
import com.boilerplate.java.entities.ReportTradeline;
import com.boilerplate.java.entities.ReportTradelineStatus;
import com.boilerplate.service.interfaces.IFileService;
import com.boilerplate.service.interfaces.IReportService;

/**
 * This class is used to parse the experian report to get required data
 * 
 * @author
 *
 */
public class ParseExperianReportObserver implements IAsyncWorkObserver {
	/**
	 * ParseExperianReportObserver logger
	 */
	private static Logger logger = Logger.getInstance(ParseExperianReportObserver.class);

	/**
	 * The dates will be stored in dd-MM-yyyy, hh:mm:ss format
	 */
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy,HH:mm:ss");

	/**
	 * The RedisSFUpdateHash
	 */
	@Autowired
	com.boilerplate.database.redis.implementation.RedisSFUpdateHash redisSFUpdateHashAccess;

	/**
	 * This method sets RedisSFUpdateHash
	 * 
	 * @return
	 */
	public void setRedisSFUpdateHashAccess(
			com.boilerplate.database.redis.implementation.RedisSFUpdateHash redisSFUpdateHashAccess) {
		this.redisSFUpdateHashAccess = redisSFUpdateHashAccess;
	}

	/**
	 * The configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * Sets the configuration manager
	 * 
	 * @param configurationManager
	 *            The configuration manager
	 */

	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is the report service which is used to process and save report
	 */
	@Autowired
	IReportService reportService;

	/**
	 * Sets the report service
	 * 
	 * @param reportService
	 *            The report service
	 */
	public void setReportService(IReportService reportService) {
		this.reportService = reportService;
	}

	/**
	 * This is an instance of mysqlReport
	 */
	IMySQLReport mysqlReport;

	/**
	 * @param mysqlReport
	 *            the mysqlReport to set
	 */
	public void setMysqlReport(IMySQLReport mysqlReport) {
		this.mysqlReport = mysqlReport;
	}

	/**
	 * This is the instance of fileservice
	 */
	@Autowired
	private IFileService fileService;

	/**
	 * This method set the file service
	 * 
	 * @param fileService
	 */
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	@Autowired
	com.boilerplate.databases.s3FileSystem.implementations.S3File file;

	/**
	 * This method sets the instance of S3File Entity
	 * 
	 * @param file
	 *            The file
	 */
	public void setFile(com.boilerplate.databases.s3FileSystem.implementations.S3File file) {
		this.file = file;
	}

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		this.parse((ReportInputEntity) asyncWorkItem.getPayload());

	}

	/**
	 * Parses a standard data from experian without time component
	 * 
	 * @param date
	 *            The date
	 * @return A java date
	 * @throws ParseException
	 *             If there is an error parsing the date
	 */
	private java.util.Date experianStringToDate(String date) throws ParseException {
		// if the date is null then return todays date
		if (date == null)
			return new Date();
		if (date.trim().equals(""))
			return new Date();
		if (date.contains("null"))
			return new Date();
		// return the date based on the odd format
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		int dt = Integer.parseInt(date.substring(6, 8));
		int hour = 0;
		int minute = 0;
		int second = 0;

		java.util.Date reportDateTime = formatter
				.parse(dt + "-" + month + "-" + year + "," + hour + ":" + minute + ":" + second);
		return reportDateTime;
	}

	/**
	 * Converts an expeirna string sent back as date month and year to a date
	 * string
	 * 
	 * @param year
	 *            The year of date
	 * @param month
	 *            The month of date
	 * @param date
	 *            The date
	 * @return A java date
	 * @throws ParseException
	 *             if there is an error parsing the date
	 */
	private java.util.Date experianStringToDate(String year, String month, String date) throws ParseException {

		int yr = Integer.parseInt(year);
		int mnth = Integer.parseInt(month);
		int dt = 1;
		int hour = 0;
		int minute = 0;
		int second = 0;

		java.util.Date reportDateTime = formatter
				.parse(dt + "-" + mnth + "-" + yr + "," + hour + ":" + minute + ":" + second);
		return reportDateTime;
	}

	/**
	 * This method converts the account holder type magic number from XML into a
	 * propert String
	 * 
	 * @param accountHolderType
	 *            The integer for account holder type
	 * @return The account holder type code's corresponding matched value
	 */
	private String accountHolderType(String accountHolderType) {
		// The magic number mapping is based on inputs from experian
		// This is not driven by a DB based logic as it is not expected to
		// change
		if (accountHolderType.equals("1"))
			return "Individual";
		if (accountHolderType.equals("2"))
			return "Joint";
		if (accountHolderType.equals("3"))
			return "Authorized User";
		if (accountHolderType.equals("7"))
			return "Deceased";
		return accountHolderType;
	}

	/**
	 * This method converts an experian status code and DPD into a tradeline
	 * status to group a tradeline as good bad or ugly. A good tradeline is
	 * anything with DPD <90, while anything >90 is a case for concern in CMD
	 * even if it is not for the bureau. Other than that standard bureau codes
	 * are used. Which are magic numbers
	 * 
	 * @param experinStatusCode
	 *            The code from experian
	 * @param dpd
	 *            The dpd
	 * @return The status of the tradeline
	 */
	private ExperianTradelineStatus getStatus(int experinStatusCode, int dpd) {
		// in other cases we use the default experian status.
		switch (experinStatusCode) {
		case 22:
		case 23:
		case 24:
		case 25:
		case 80:
		case 82:
		case 83:
		case 84:
		case 35:
		case 36:
		case 37:
		case 41:
		case 43:
		case 47:
		case 48:
		case 49:
		case 53:
		case 54:
		case 57:
		case 58:
		case 59:
		case 60:
		case 62:
		case 64:
		case 65:
		case 68:
		case 69:
		case 70:
		case 72:
		case 74:
		case 76:
		case 77:
		case 85:
		case 86:
		case 87:
		case 88:
		case 89:
		case 90:
		case 93:
		case 97:
			return ExperianTradelineStatus.WrittenOff;
		// Ignoring the Active account status code managed with DPD
		// case 11: case 21: case 71: case 40: case 78: case 0: return
		// ExperianTradelineStatus.Active;

		case 30:
		case 31:
		case 34:
		case 39:
		case 40:
		case 42:
		case 46:
		case 51:
		case 52:
		case 63:
		case 75:
		case 91:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 12:
			return ExperianTradelineStatus.Closed;

		case 32:
		case 33:
		case 38:
		case 44:
		case 45:
		case 50:
		case 55:
		case 56:
		case 61:
		case 66:
		case 67:
		case 73:
		case 79:
		case 81:
		case 94:
			return ExperianTradelineStatus.Settled;
		}
		// if DPD> 90 we are not so good
		if (dpd > 90) {
			return ExperianTradelineStatus.Bad;
		}
		// if DPD< 90 we are good
		else if (experinStatusCode == 0) {
			return ExperianTradelineStatus.Closed;
		} else {
			return ExperianTradelineStatus.Active;
		}

	}

	/**
	 * This method conversts a string from experian into double
	 * 
	 * @param s
	 *            The string
	 * @return A double
	 */
	private double parseExperinaStringToDouble(String s) {
		if (s == null)
			return 0.0;
		if (s.equals(""))
			return 0.0;
		if (s.contains("null"))
			return 0.0;
		return Double.parseDouble(s);
	}

	/**
	 * This method conversts a string from experian into an integer
	 * 
	 * @param s
	 *            The string
	 * @return A integer
	 */
	private int parseExperinaStringToInteger(String s) {
		if (s == null)
			return 0;
		if (s.equals(""))
			return 0;
		if (s.contains("null"))
			return 0;
		return Integer.parseInt(s);
	}

	/**
	 * This method is used to parse the report to fetch required data from its
	 * xmlResponse getting concerned node values
	 * 
	 * @param reportInputEntity
	 *            which contains the report to be parsed
	 * @throws Exception
	 */
	public void parse(ReportInputEntity reportInputEntity) throws Exception {
		try {

			// Get file from local if not found then downloads
			String fileNameInURL = null;
			FileEntity fileEntity = fileService.getFile(reportInputEntity.getReportFileNameOnDisk());
			if (!new File(configurationManager.get("RootFileDownloadLocation"),
					reportInputEntity.getReportFileNameOnDisk()).exists()) {
				fileNameInURL = this.file.downloadFileFromS3ToLocal(fileEntity.getFullFileNameOnDisk());
			} else {
				fileNameInURL = fileEntity.getFileName();
			}

			// load the html file as a string
			String htmlFile = FileUtils
					.readFileToString(new File(configurationManager.get("RootFileDownloadLocation") + fileNameInURL));

			// cut out the xml part from it, again due to issues this can only
			// be
			// done as a magic number
			// ideally we would have liked to be able to parse html and extract
			// it
			// based on a query
			int startingPOsition = htmlFile.indexOf("xmlResponse") + 21;
			// the resulting file as the trailing tags which need to be removed
			String xmlFile = htmlFile.substring(startingPOsition, htmlFile.length());
			xmlFile = xmlFile.replace("\"/>", "");
			xmlFile = xmlFile.replace("</body>", "");
			xmlFile = xmlFile.replace("</html>", "");
			// the xml we get is html encoded for example < is represented as
			// &lt;
			// hence to make it usable
			// we parse
			xmlFile = StringEscapeUtils.unescapeHtml(xmlFile);
			// for each tradeline save in the DB

			BoilerplateList<ReportTradeline> tradelines = new BoilerplateList<>();
			ReportTradeline tradeline = null;

			// parse xml file from report

			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(xmlFile));

			Document doc = documentBuilder.parse(inputSource);

			Report report = new Report();
			// Normalize the XML Structure; It's just too important !!
			NodeList root = doc.getChildNodes();
			Node rootNode = getNode("InProfileResponse", root);
			Node creditProfileHeader = getNode("CreditProfileHeader", rootNode.getChildNodes());
			Node score = getNode("SCORE", rootNode.getChildNodes());
			NodeList accountDetails = doc.getElementsByTagName("CAIS_Account_DETAILS");
			// get creditprofileheader nodes
			NodeList creditProfileHeaderNodes = creditProfileHeader.getChildNodes();

			// get bureau score nodes
			NodeList scoreNodes = score.getChildNodes();

			// for the report extract the tradelines
			for (int i = 0; i < accountDetails.getLength(); i++) {
				try {
					NodeList cAISAccountDETAILS = accountDetails.item(i).getChildNodes();
					tradeline = new ReportTradeline();
					String accountNumber = getNodeValue("Account_Number", cAISAccountDETAILS);
					tradeline.setReportId(getNodeValue("ReportNumber", creditProfileHeaderNodes));
					String organizationName = getNodeValue("Subscriber_Name", cAISAccountDETAILS);
					tradeline.setOrganizationName(organizationName);
					tradeline.setProductName(
							getProductName(Integer.parseInt((getNodeValue("Account_Type", cAISAccountDETAILS)) == ""
									? "0" : getNodeValue("Account_Type", cAISAccountDETAILS))));

					tradeline.setAccountNumber(accountNumber);
					tradeline.setUserId(reportInputEntity.getUserId());
					String tradelineId = tradeline.getUserId().toUpperCase() + ":" + tradeline.getReportId().toUpperCase() + ":"
							+ tradeline.getOrganizationName().toUpperCase() + ":" + tradeline.getProductName().toUpperCase() + ":"
							+ tradeline.getAccountNumber().toUpperCase();

					tradeline.setDateOpened(this.experianStringToDate(getNodeValue("Open_Date", cAISAccountDETAILS)));
					if (getNodeValue("Date_Closed", cAISAccountDETAILS) != "") {
						tradeline.setDateClosed(
								this.experianStringToDate(getNodeValue("Date_Closed", cAISAccountDETAILS).toString()));
					}
					if (getNodeValue("Open_Date", cAISAccountDETAILS) != "") {
						tradeline.setDateOpened(
								this.experianStringToDate(getNodeValue("Open_Date", cAISAccountDETAILS).toString()));
					}
					if (getNodeValue("Date_of_Last_Payment", cAISAccountDETAILS) != "") {
						tradeline.setDateOfLastPayment(this.experianStringToDate(
								getNodeValue("Date_of_Last_Payment", cAISAccountDETAILS).toString()));
					}

					tradeline.setAccountHolderType(getNodeValue("AccountHoldertypeCode", cAISAccountDETAILS));

					String year = "1900";
					String month = "1";
					Element accountHistoryElement = (Element) cAISAccountDETAILS;
					NodeList cAISAccountHistoryList = accountHistoryElement
							.getElementsByTagName("CAIS_Account_History");
					NodeList cAISAccountHistory = cAISAccountHistoryList.item(0).getChildNodes();
					// String daysPastDue = getNodeValue("Days_Past_Due",
					// cAISAccountHistory);

					if (cAISAccountHistory.getLength() > 0) {
						year = getNodeValue("Year", cAISAccountHistory);
						month = getNodeValue("Month", cAISAccountHistory);
						tradeline.setDPD(
								parseExperinaStringToInteger(getNodeValue("Days_Past_Due", cAISAccountHistory)));
					}
					if (year == "" || month == "") {
						year = "1900";
						month = "1";
					}

					tradeline.setCurrentBalance(
							checkDoubleorAssign(getNodeValue("Current_Balance", cAISAccountDETAILS), -1.0));
					if (getNodeValue("Date_Reported", cAISAccountDETAILS) != "") {
						tradeline.setDateReported(this
								.experianStringToDate(getNodeValue("Date_Reported", cAISAccountDETAILS).toString()));
					}
					tradeline.setAmountDue(
							checkDoubleorAssign(getNodeValue("Amount_Past_Due", cAISAccountDETAILS), -1.0));
					tradeline.setOccupation(getNodeValue("Occupation_Code", cAISAccountDETAILS));
					tradeline.setExperianTradelineStatusEnum(this.getStatus(
							parseExperinaStringToInteger(getNodeValue("Account_Status", cAISAccountDETAILS)),
							tradeline.getDPD()));

					tradeline.setUserId(reportInputEntity.getUserId());
					tradeline.setReportTradelineStatus(ReportTradelineStatus.WaitingBalance);
					tradeline.setId(tradelineId);

					// SAVE REPORT TRADELINES
					mysqlReport.saveReportTradeline(tradeline);

					// get all address nodes
					Element addressElement = (Element) cAISAccountDETAILS;
					NodeList addressNodeList = addressElement.getElementsByTagName("CAIS_Holder_Address_Details");
					Address address = null;

					for (int j = 0; j < addressNodeList.getLength(); j++) {
						NodeList cAISHolderAddressDetais = addressNodeList.item(j).getChildNodes();
						address = new Address();
						address.setId(Integer.toString(j));
						address.setCity(getNodeValue("City_non_normalized", cAISHolderAddressDetais));
						address.setFirstLineOfAddress(
								getNodeValue("First_Line_Of_Address_non_normalized", cAISHolderAddressDetais)
										.replace("\"", ""));
						address.setFifthLineOfAddress(
								getNodeValue("Fifth_Line_Of_Address_non_normalized", cAISHolderAddressDetais)
										.replace("\"", ""));
						address.setSecondLineOfAddress(
								getNodeValue("Second_Line_Of_Address_non_normalized", cAISHolderAddressDetais)
										.replace("\"", ""));
						address.setState(getNodeValue("State_non_normalized", cAISHolderAddressDetais));
						address.setThirdLineOfAddress(
								getNodeValue("Third_Line_Of_Address_non_normalized", cAISHolderAddressDetais)
										.replace("\"", ""));
						address.setCountryCode(getNodeValue("CountryCode_non_normalized", cAISHolderAddressDetais));
						address.setZipCode(getNodeValue("ZIP_Postal_Code_non_normalized", cAISHolderAddressDetais));
						address.setTradelineId(tradelineId);

						// tradeline.getAddresses().add(address);
						// save address of tradeline of user report
						mysqlReport.saveAddress(address);

					}
					// get all phone numbers and emails
					Element holderPhoneElement = (Element) cAISAccountDETAILS;
					NodeList phoneList = holderPhoneElement.getElementsByTagName("CAIS_Holder_Phone_Details");
					ElectronicContact electronicContact = null;
					for (int k = 0; k < phoneList.getLength(); k++) {
						NodeList cAISHolderPhoneDetails = phoneList.item(k).getChildNodes();
						electronicContact = new ElectronicContact();
						electronicContact.setId(Integer.toString(k));
						electronicContact.setEmail(getNodeValue("EMailId", cAISHolderPhoneDetails));
						electronicContact.setTelephoneNumber(getNodeValue("Telephone_Number", cAISHolderPhoneDetails));
						electronicContact
								.setMobileNumber(getNodeValue("Mobile_Telephone_Number", cAISHolderPhoneDetails));
						electronicContact.setTradelineId(tradelineId);
						// save electronic contacts
						mysqlReport.saveElectronicContact(electronicContact);
					}
				} catch (Exception ex) {
					logger.logInfo("ParseExperianReportObserver", "parse", "tradeline parse exception", ex.toString());
				}
			}
			report.setUserId(reportInputEntity.getUserId());
			report.setFileId(fileEntity.getFileName());
			report.setId(getNodeValue("ReportNumber", creditProfileHeaderNodes));
			report.setBureauScore(Integer.parseInt(
					getNodeValue("BureauScore", scoreNodes) == "" ? "0" : getNodeValue("BureauScore", scoreNodes)));
			report.setReportNumber(getNodeValue("ReportNumber", creditProfileHeaderNodes));
			report.setReportStatusEnum(ReportStatus.Complete);
			report.setReportDateTime(getNodeValue("ReportDate", creditProfileHeaderNodes));
			report.setReportSourceEnum(ReportSource.Experian);
			// save report's remaining items.
			mysqlReport.saveReport(report);
			// sets the pan number in pan number hash
			setPanNumberInHash(reportInputEntity);
		} catch (Exception ex) {
			logger.logException("ParseExperianReportObserver", "parse", "ExceptionParse", ex.toString(), ex);
		}

	}

	/**
	 * This method sets the pan number inside the pan number list hash
	 * 
	 * @param reportInputEntiity
	 *            The reportInputEntiity contains required input data here pan
	 *            number is required
	 */
	private void setPanNumberInHash(ReportInputEntity reportInputEntiity) {
		if (reportInputEntiity.getReportVersion() == 1) {
			if (reportInputEntiity.getPanNumber() != null) {
				this.redisSFUpdateHashAccess.hset(configurationManager.get("PanNumberHash_Base_Tag"),
						reportInputEntiity.getPanNumber().toUpperCase(), reportInputEntiity.getUserId());
			}
		}

	}

	/**
	 * 
	 * This method give the node from root child nodes on the basis of tag name.
	 * 
	 * @param tagName
	 *            node name
	 * @param nodes
	 *            root child nodes
	 * @return the found node in nodelist of xml document
	 */
	private Node getNode(String tagName, NodeList nodes) {
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				return node;
			}
		}

		return null;
	}

	/**
	 * This method finds the value of node on the basis of tag name.
	 * 
	 * @param tagName
	 *            node name
	 * @param nodes
	 *            nodes list
	 * @return node value
	 */
	private String getNodeValue(String tagName, NodeList nodes) {
		for (int j = 0; j < nodes.getLength(); j++) {
			Node node = nodes.item(j);
			if (node.getNodeName().equalsIgnoreCase(tagName)) {
				NodeList childNodes = node.getChildNodes();
				for (int k = 0; k < childNodes.getLength(); k++) {
					Node data = childNodes.item(k);
					if (data.getNodeType() == Node.TEXT_NODE)
						return data.getNodeValue();
				}
			}

		}
		return "";
	}

	/**
	 * This method converts a string from experian into a double
	 * 
	 * @param str
	 *            The String
	 * @param defaulValue
	 *            The default value
	 * @return double value
	 */
	private Double checkDoubleorAssign(String str, double defaulValue) {
		if (str == "") {
			return defaulValue;
		} else {
			return Double.parseDouble(str);
		}

	}

	/**
	 * This method is used to get the productname on the basis of account type
	 * number
	 * 
	 * @param accountType
	 *            the value for which mapped value to get
	 * @return the product name
	 */
	private String getProductName(int accountType) {
		String productName = "";
		switch (accountType) {

		case 0:
			productName = "Other";
			break;
		case 1:
			productName = "AUTO LOAN";
			break;
		case 2:
			productName = "HOUSING LOAN";
			break;
		case 3:
			productName = "PROPERTY LOAN";
			break;
		case 4:
			productName = "LOAN AGAINST SHARES SECURITIES";
			break;
		case 5:
			productName = "PERSONAL LOAN";
			break;
		case 6:
			productName = "CONSUMER LOAN";
			break;
		case 7:
			productName = "GOLD LOAN";
			break;
		case 8:
			productName = "EDUCATIONAL LOAN";
			break;
		case 9:
			productName = "LOAN TO PROFESSIONAL";
			break;
		case 10:
			productName = "CREDIT CARD";
			break;
		case 11:
			productName = "LEASING";
			break;
		case 12:
			productName = "OVERDRAFT LOAN";
			break;
		case 13:
			productName = "TWO WHEELER LOAN";
			break;
		case 14:
			productName = "NON FUNDED CREDIT FACILITY";
			break;
		case 15:
			productName = "LOAN AGAINST BANK DEPOSITS";
			break;
		case 16:
			productName = "FLEET CARD";
			break;
		case 17:
			productName = "Commercial Vehicle LOAN";
			break;
		case 18:
			productName = "Telco Wireless";
			break;
		case 19:
			productName = "Telco Broadband";
			break;
		case 20:
			productName = "Telco Landline";
			break;
		case 31:
			productName = "Secured Credit Card";
			break;
		case 32:
			productName = "Used Car Loan";
			break;
		case 33:
			productName = "Construction Equipment Loan";
			break;
		case 34:
			productName = "Tractor Loan";
			break;
		case 35:
			productName = "CORPORATE CREDIT CARD";
			break;
		case 43:
			productName = "Microfinance Others";
			break;
		case 51:
			productName = "BUSINESS LOAN GENERAL";
			break;
		case 52:
			productName = "BUSINESS LOAN PRIORITY SECTOR SMALL BUSINESS";
			break;
		case 53:
			productName = "BUSINESS LOAN PRIORITY SECTOR AGRICULTURE";
			break;
		case 54:
			productName = "BUSINESS LOAN PRIORITY SECTOR OTHERS";
			break;
		case 55:
			productName = "BUSINESS NON FUNDED CREDIT FACILITY GENERAL";
			break;
		case 56:
			productName = "BUSINESS NON FUNDED CREDIT FACILITY PRIORITY SECTOR SMALL BUSINESS";
			break;
		case 57:
			productName = "BUSINESS NON FUNDED CREDIT FACILITY PRIORITY SECTOR AGRICULTURE";
			break;
		case 58:
			productName = "BUSINESS NON FUNDED CREDIT FACILITY PRIORITY SECTOR OTHERS";
			break;
		case 59:
			productName = "BUSINESS LOANS AGAINST BANK DEPOSITS";
			break;
		case 60:
			productName = "Staff Loan";
			break;

		default:
			productName = "Other";
			break;
		}

		return productName;
	}

}
