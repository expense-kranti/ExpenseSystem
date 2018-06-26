package com.boilerplate.asyncWork;

import java.io.File;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
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
import com.boilerplate.java.entities.ReportVersion;
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

	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader jon
	 * 
	 * @param queueReaderJob
	 *            The queue reader jon
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	BoilerplateList<String> subjectsForParsingExperianReports = new BoilerplateList<>();

	/**
	 * This code is called during initialization of the application, the
	 * unmarshler is an expensive object hence we want to keep only one instance
	 * of it and share the same.
	 * 
	 * @throws Exception
	 *             If there is an error while creating instance
	 */
	public void initialize() throws Exception {
		subjectsForParsingExperianReports.add("ParseExperianReportsForExtraData");
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
			FileEntity fileEntity = fileService.getFile(reportInputEntity.getReportFileId());
			if (!new File(configurationManager.get("RootFileDownloadLocation"), reportInputEntity.getReportFileId())
					.exists()) {
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
			// this report id is the colon seperated userid and reportnumber
			report.setId(reportInputEntity.getUserId() + ":" + reportInputEntity.getReportNumber());
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
					tradeline.setReportId(report.getId());
					String organizationName = getNodeValue("Subscriber_Name", cAISAccountDETAILS);
					tradeline.setOrganizationName(organizationName);
					tradeline.setProductName(reportService.getProductName(
							Integer.parseInt((getNodeValue("Account_Type", cAISAccountDETAILS)) == "" ? "0"
									: getNodeValue("Account_Type", cAISAccountDETAILS))));

					tradeline.setAccountNumber(accountNumber);
					tradeline.setUserId(reportInputEntity.getUserId());
					//create tradeline id
					String tradelineId = tradeline.getUserId().toUpperCase() + ":"
							+ reportInputEntity.getReportNumber().toUpperCase() + ":"
							+ tradeline.getOrganizationName().toUpperCase() + ":"
							+ tradeline.getProductName().toUpperCase() + ":"
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
					

					if (cAISAccountHistory.getLength() > 0) {
						year = getNodeValue("Year", cAISAccountHistory);
						month = getNodeValue("Month", cAISAccountHistory);
						tradeline.setDaysPastDue(
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
							tradeline.getDaysPastDue()));

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
			report.setReportNumber(getNodeValue("ReportNumber", creditProfileHeaderNodes));

			report.setBureauScore(Integer.parseInt(
					getNodeValue("BureauScore", scoreNodes) == "" ? "0" : getNodeValue("BureauScore", scoreNodes)));
			report.setReportStatusEnum(ReportStatus.Complete);
			report.setReportVersionEnum(ReportVersion.ExperianV1);
			report.setReportDateTime(getNodeValue("ReportDate", creditProfileHeaderNodes));
			// save report's remaining items.
			mysqlReport.saveReport(report);
			// sets the pan number in pan number hash
			setPanNumberInHash(reportInputEntity, report);

			try {
				// here the queue will be containing file id(which in turn
				// contains report ids to be parsed)
				queueReaderJob.requestBackroundWorkItem(report.getId(), subjectsForParsingExperianReports,
						"ParseExperianReportObserver", "parseExperianReportObserver",
						"_AKS_PARSE_EXPERIAN_REPORTS_QUEUE");
			} catch (Exception ex) {
				logger.logError("ParseExperianReportObserver", "parse",
						"Inside try-catch block pushing task for ParseExperianReportsForExtraDataObserver in _AKS_PARSE_EXPERIAN_REPORTS_QUEUE",
						ex.toString());
			}
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
	private void setPanNumberInHash(ReportInputEntity reportInputEntiity, Report report) {
		if (report.getReportVersionEnum() == ReportVersion.ExperianV1) {
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

}
