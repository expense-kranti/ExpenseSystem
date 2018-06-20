package com.boilerplate.asyncWork;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.boilerplate.database.interfaces.IMySQLBankData;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Address;
import com.boilerplate.java.entities.BankData;
import com.boilerplate.java.entities.CreditEnquiry;
import com.boilerplate.java.entities.ExperianReportCustomerPersonalDetails;
import com.boilerplate.java.entities.ExperianTradelineStatus;
import com.boilerplate.java.entities.FileEntity;
import com.boilerplate.java.entities.Report;
import com.boilerplate.java.entities.ReportTradeline;
import com.boilerplate.java.entities.ReportTradelineStatus;
import com.boilerplate.java.entities.TradelineFlow;
import com.boilerplate.service.interfaces.IFileService;
import com.boilerplate.service.interfaces.IReportService;
import com.opencsv.CSVReader;

import me.xdrop.fuzzywuzzy.FuzzySearch;

/**
 * This observer parses experian reports for fetching tradelines and other
 * required informations like all emailids, phonenumbers, addresses, full name,
 * credit inquiry data, non credit inquiry data
 * 
 * @author urvij
 *
 */
public class ParseExperianReportsForExtraDataObserver implements IAsyncWorkObserver {

	/**
	 * ParseExperianReportsForExtraDataObserver logger
	 */
	private static Logger logger = Logger.getInstance(ParseExperianReportsForExtraDataObserver.class);

	/**
	 * The dates will be stored in dd-MM-yyyy, hh:mm:ss format
	 */
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy,HH:mm:ss");

	/**
	 * These are the set for uniqueness
	 */
	private Set<String> emailIdSet = new HashSet<>();
	private List<Address> addressSet = new ArrayList<>();
	private Set<String> pincodeSet = new HashSet<>();
	private Set<String> mobileNumberSet = new HashSet<>();

	private static final String writeOff = "Write Off";
	
	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 *            The configuration manager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is the instance of S3File Entity
	 */
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
	 * This is an instance of the file service
	 */
	private IFileService fileService;

	/**
	 * Sets the file service
	 * 
	 * @param fileService
	 *            The file service
	 */
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * This is an instance of IMySQLBankData
	 */
	IMySQLBankData mySQLBankDataAccess;

	/**
	 * Sets the mySQLBankDataAccess
	 * 
	 * @param mySQLBankDataAccess
	 *            the mySQLBankDataAccess to set
	 */
	public void setMySQLBankDataAccess(IMySQLBankData mySQLBankDataAccess) {
		this.mySQLBankDataAccess = mySQLBankDataAccess;
	}

	/**
	 * This is the email regex expression
	 */
	public static Pattern emailResxPattern = Pattern
			.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		logger.logInfo("ParseExperianReportsForExtraDataObserver", "observe", "StartObserve",
				"about to get file id from queue for parsing reports");
		// get report ids of reports to be parsed, from CSVfile and parse
		// reports
		parseReport((String) asyncWorkItem.getPayload());
		// getReportIdsFromCSVAndParseReports((String)
		// asyncWorkItem.getPayload());
		logger.logInfo("ParseExperianReportsForExtraDataObserver", "observe", "EndObserve",
				"parsing reports observer end");
	}

	/**
	 * This method is used to get report from data base and parse them
	 * 
	 * @param reportId
	 *            the id of report to be parsed
	 * @throws NotFoundException
	 *             thrown when report is not found
	 * @throws IOException
	 *             thrown when exception occurs in getting file
	 * @throws FactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws ParseException
	 */
	private void parseReport(String reportId) throws NotFoundException, IOException, ParserConfigurationException,
			FactoryConfigurationError, SAXException, ParseException {
		logger.logInfo("ParseExperianReportsForExtraDataObserver", "parseReport", "StartParseReport",
				"Start of report Parsing with reportId is : " + reportId + " about to get report from redis daatabase");
		// get report from database with given reportId

		try {
			Report report = reportService.getReportById(reportId);
			if (report != null) {
				// reset them for new report parsing
				emailIdSet = new HashSet<>();
				addressSet = new ArrayList<>();
				pincodeSet = new HashSet<>();
				mobileNumberSet = new HashSet<>();

				// to be used in setting usermobile number in bankdata tradeline
				String[] ids = null;
				String userPhoneNumber = null;

				if (report.getUserId() != null) {
					ids = report.getUserId().split(":");
				}
				if (ids.length == 2) {
					userPhoneNumber = ids[1];
				}

				// get file
				FileEntity fileEntity = fileService.getFile(report.getFileId());
				// Get file from local if not found then downloads
				String fileNameInURL = null;
				if (!new File(configurationManager.get("RootFileDownloadLocation"), report.getFileId()).exists()) {
					fileNameInURL = this.file.downloadFileFromS3ToLocal(fileEntity.getFullFileNameOnDisk());
				} else {
					fileNameInURL = report.getFileId();
				}
				// load the html file as a string
				String htmlFile = FileUtils.readFileToString(
						new File(configurationManager.get("RootFileDownloadLocation") + fileNameInURL));

				// parse xml file from report
				int startingPOsition = htmlFile.indexOf("xmlResponse") + 21;
				// the resulting file as the trailing tags which need to be
				// removed
				String xmlFile = htmlFile.substring(startingPOsition, htmlFile.length());
				xmlFile = xmlFile.replace("\"/>", "");
				xmlFile = xmlFile.replace("</body>", "");
				xmlFile = xmlFile.replace("</html>", "");
				// the xml we get is html encoded for example < is represented
				// as
				// &lt;
				// hence to make it usable
				// we parse
				xmlFile = StringEscapeUtils.unescapeHtml(xmlFile);
				System.out.println(xmlFile);

				BoilerplateList<ReportTradeline> tradelines = new BoilerplateList<>();
				ReportTradeline tradeline = null;

				DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource inputSource = new InputSource();
				inputSource.setCharacterStream(new StringReader(xmlFile));

				Document doc = documentBuilder.parse(inputSource);

				// Normalize the XML Structure; It's just too important !!
				NodeList root = doc.getChildNodes();
				Node rootNode = getNode("InProfileResponse", root);
				Node creditProfileHeader = getNode("CreditProfileHeader", rootNode.getChildNodes());
				Node score = getNode("SCORE", rootNode.getChildNodes());

				NodeList accountDetails = doc.getElementsByTagName("CAIS_Account_DETAILS");

				List<CreditEnquiry> creditEnquiryList = new ArrayList<>();
				ExperianReportCustomerPersonalDetails customerDetails = new ExperianReportCustomerPersonalDetails();

				// for the report extract the tradelines
				for (int i = 0; i < accountDetails.getLength(); i++) {
					try {
						NodeList cAISAccountDETAILS = accountDetails.item(i).getChildNodes();

						try {
							tradeline = new ReportTradeline();
							tradeline.setReportId(report.getId());
							String accountNumber = getNodeValue("Account_Number", cAISAccountDETAILS);
							tradeline.setAccountNumber(accountNumber);
							tradeline.setHighCreditLoanAmount(checkDoubleorAssign(
									getNodeValue("Highest_Credit_or_Original_Loan_Amount", cAISAccountDETAILS), -1.0));
							String rePaymentTenure = getNodeValue("Repayment_Tenure", cAISAccountDETAILS);
							tradeline.setRepaymentTenure(
									checkDoubleorAssign(getNodeValue("Repayment_Tenure", cAISAccountDETAILS), -1.0));
							tradeline.setDateOpened(
									this.experianStringToDate(getNodeValue("Open_Date", cAISAccountDETAILS)));
							if (getNodeValue("Date_Closed", cAISAccountDETAILS) != "") {
								tradeline.setDateClosed(this.experianStringToDate(
										getNodeValue("Date_Closed", cAISAccountDETAILS).toString()));
							}
							if (getNodeValue("Open_Date", cAISAccountDETAILS) != "") {
								tradeline.setDateOpened(this.experianStringToDate(
										getNodeValue("Open_Date", cAISAccountDETAILS).toString()));
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
							String daysPastDue = getNodeValue("Days_Past_Due", cAISAccountHistory);

							if (cAISAccountHistory.getLength() > 0) {
								year = getNodeValue("Year", cAISAccountHistory);
								month = getNodeValue("Month", cAISAccountHistory);

								tradeline.setDaysPastDue(parseExperinaStringToInteger(
										getNodeValue("Days_Past_Due", cAISAccountHistory)));

							}
							if (year == "" || month == "") {
								year = "1900";
								month = "1";
							} else {
								tradeline.setLastHistoryDate(this.experianStringToDate(year, month, "1"));
							}
							tradeline.setSettlementAmount(
									checkDoubleorAssign(getNodeValue("Settlement_Amount", cAISAccountDETAILS), -1.0));
							tradeline.setCurrentBalance(
									checkDoubleorAssign(getNodeValue("Current_Balance", cAISAccountDETAILS), -1.0));
							if (getNodeValue("Date_Reported", cAISAccountDETAILS) != "") {
								tradeline.setDateReported(this.experianStringToDate(
										getNodeValue("Date_Reported", cAISAccountDETAILS).toString()));
							}
							tradeline.setAmountDue(
									checkDoubleorAssign(getNodeValue("Amount_Past_Due", cAISAccountDETAILS), -1.0));
							tradeline.setValueCollateral(getNodeValue("Value_of_Collateral", cAISAccountDETAILS));
							tradeline.setTypeCollateral(getNodeValue("Type_of_Collateral", cAISAccountDETAILS));
							tradeline.setOccupation(getNodeValue("Occupation_Code", cAISAccountDETAILS));
							tradeline.setRateOfIntererst(
									checkDoubleorAssign(getNodeValue("Rate_of_Interest", cAISAccountDETAILS), -1.0));
							tradeline.setIncome(checkDoubleorAssign(getNodeValue("Income", cAISAccountDETAILS), -1.0));

							Element holderDetailElement = (Element) cAISAccountDETAILS;
							NodeList cAISHolderDetailsList = holderDetailElement
									.getElementsByTagName("CAIS_Holder_Details");
							NodeList cAISHolderDetails = cAISHolderDetailsList.item(0).getChildNodes();
							// check if customer full name is null or empty then
							// set
							// if
							// it
							// is
							if (isNullOrEmpty(customerDetails.getFirstName())) {
								makeAndSetCustomerFullName(customerDetails, cAISHolderDetails);
							}
							if (isNullOrEmpty(customerDetails.getVoterId())) {
								customerDetails.setVoterId(getNodeValue("Voter_ID_Number", cAISHolderDetails));
							}
							if (isNullOrEmpty(customerDetails.getPassport())) {
								customerDetails.setPassport(getNodeValue("Passport_Number", cAISHolderDetails));
							}

							// check if customer dob is null then extract and
							// set if
							// it
							// is
							if (customerDetails.getDob() == null) {
								customerDetails.setDob(
										this.experianStringToDate(getNodeValue("Date_of_birth", cAISHolderDetails)));

							}

							// get all phone numbers and emails
							Element holderPhoneElement = (Element) cAISAccountDETAILS;
							NodeList phoneList = holderPhoneElement.getElementsByTagName("CAIS_Holder_Phone_Details");
							for (int k = 0; k < phoneList.getLength(); k++) {
								NodeList cAISHolderPhoneDetails = phoneList.item(k).getChildNodes();
								// check email validity and add in to emailid
								// set
								addIntoEmailSet(cAISHolderPhoneDetails);
								// check mobile number validity and add in to
								// mobile
								// number
								// set
								addIntoMobileNumberSet(cAISHolderPhoneDetails);

							}

							// get customer's all ids
							Element holderIdElement = (Element) cAISAccountDETAILS;
							NodeList idsList = holderIdElement.getElementsByTagName("CAIS_Holder_ID_Details");
							for (int l = 0; l < idsList.getLength(); l++) {
								NodeList cAISHolderIdsDetails = idsList.item(l).getChildNodes();

								if (isNullOrEmpty(customerDetails.getPanNumber())) {
									customerDetails.setPanNumber(getNodeValue("Income_TAX_PAN", cAISHolderIdsDetails));
								}
								if (isNullOrEmpty(customerDetails.getDrivingLicense())) {
									customerDetails.setDrivingLicense(
											getNodeValue("Driver_License_Number", cAISHolderIdsDetails));
								}
								if (isNullOrEmpty(customerDetails.getAadhaarNumber())) {
									customerDetails.setAadhaarNumber(
											getNodeValue("Universal_ID_Number", cAISHolderIdsDetails));
								}

								// check email validity and add in to emailid
								// set
								addIntoEmailSet(cAISHolderIdsDetails);
							}

							String organizationName = getNodeValue("Subscriber_Name", cAISAccountDETAILS);

							int accountType = Integer.parseInt((getNodeValue("Account_Type", cAISAccountDETAILS)) == ""
									? "0" : getNodeValue("Account_Type", cAISAccountDETAILS));
							// BoilerplateMap<String, Product> productMap =
							// productService.getProductExperianTagMap();
							// Product product = productMap.get(accountType +
							// "");
							// String productId = product.getId();
							String productId = reportService.getProductName(accountType);
							tradeline.setOrganizationName(organizationName);
							tradeline.setProductName(productId);
							tradeline.setExperianTradelineStatusEnum(this.getStatus(
									parseExperinaStringToInteger(getNodeValue("Account_Status", cAISAccountDETAILS)),
									tradeline.getDaysPastDue()));

							// get all address nodes
							Element addressElement = (Element) cAISAccountDETAILS;
							NodeList addressNodeList = addressElement
									.getElementsByTagName("CAIS_Holder_Address_Details");

							// add address entity into address entity set
							makeAddressAndAddIntoList(addressNodeList);

							tradeline.setUserId(report.getUserId());
							tradeline.setReportTradelineStatus(ReportTradelineStatus.WaitingBalance);
							tradeline.setTradelineFlow(TradelineFlow.Start);

						} catch (Exception ex) {
							logger.logException("ParseExperianReportObserver", "parse", "tradeline parse exception", "",
									ex);
						}
					} catch (Exception ex) {
						logger.logException("ParseExperianReportsForExtraDataObserver", "parseReport",
								"ExceptionInParsingTradeline", "", null);
					}
					// add tradeline in tradeline list
					if (tradeline != null) {
						tradelines.add(tradeline);
					}
				}

				// get credit enquiry details, make entity and add in
				// list
				// extract non credit enquiry details
				NodeList creditEnquiryDetails = doc.getElementsByTagName("CAPS");
				creditEnquiryList = new ArrayList<>();
				for (int ci = 0; ci < creditEnquiryDetails.getLength(); ci++) {

					NodeList creditEnquiryDetailsChildNodes = creditEnquiryDetails.item(ci).getChildNodes();
					Element creditEnquiryDetailsChildNodesElement = (Element) creditEnquiryDetailsChildNodes;
					NodeList creditEnquiryApplicationDetails = creditEnquiryDetailsChildNodesElement
							.getElementsByTagName("CAPS_Application_Details");

					for (int j = 0; j < creditEnquiryApplicationDetails.getLength(); j++) {
						NodeList creditEnquiryApplicationDetailsChildNodes = creditEnquiryApplicationDetails.item(j)
								.getChildNodes();
						CreditEnquiry creditEnquiry = new CreditEnquiry();
						creditEnquiry.setAmount(checkDoubleorAssign(
								getNodeValue("Amount_Financed", creditEnquiryApplicationDetailsChildNodes), 0));
						creditEnquiry.setCreditInstitutionName(
								getNodeValue("Subscriber_Name", creditEnquiryApplicationDetailsChildNodes));
						creditEnquiry.setApplicationDate(this.experianStringToDate(
								getNodeValue("Date_of_Request", creditEnquiryApplicationDetailsChildNodes)));
						Element creditEnquiryApplicationElement = (Element) creditEnquiryApplicationDetailsChildNodes;
						NodeList creditEnquiryApplicantNodeList = creditEnquiryApplicationElement
								.getElementsByTagName("CAPS_Applicant_Details");

						// get credit enquiry applicant details
						for (int l = 0; l < creditEnquiryApplicantNodeList.getLength(); l++) {
							NodeList creditEnquiryApplicantDetails = creditEnquiryApplicantNodeList.item(l)
									.getChildNodes();
							creditEnquiry.setPanNumber(getNodeValue("IncomeTaxPan", creditEnquiryApplicantDetails));
							creditEnquiry.setPassport(getNodeValue("Passport_number", creditEnquiryApplicantDetails));
							creditEnquiry
									.setVoterId(getNodeValue("Voter_s_Identity_Card", creditEnquiryApplicantDetails));
							creditEnquiry.setDrivingLicense(
									getNodeValue("Driver_License_Number", creditEnquiryApplicantDetails));

							creditEnquiry.setDob(experianStringToDate(
									getNodeValue("Date_Of_Birth_Applicant", creditEnquiryApplicantDetails)));

							if (!(isNullOrEmpty(getNodeValue("MobilePhoneNumber", creditEnquiryApplicantDetails)))) {
								creditEnquiry.setMobileNumber(validatedMobileNumber(
										getNodeValue("MobilePhoneNumber", creditEnquiryApplicantDetails)));
							}
							if (isValidEmailId(getNodeValue("EMailId", creditEnquiryApplicantDetails))) {
								creditEnquiry.setEmailId(getNodeValue("EMailId", creditEnquiryApplicantDetails));
							}

							creditEnquiry.setSearchType(this.getSearchTypeFromReasonCode(
									getNodeValue("Enquiry_Reason", creditEnquiryApplicantDetails)));

							// check email validity and add in to emailid
							// set
							addIntoEmailSet(creditEnquiryApplicantDetails);
							// check mobile number validity and add in to
							// mobile
							// number
							// set
							addIntoMobileNumberSet(creditEnquiryApplicantDetails);
							// fill customer details, if null or empty,
							// found in
							// the nodelist
							fillCustomerDetails(customerDetails, creditEnquiryApplicantDetails);

							// get address details for credit enquiry
							NodeList addressDetailsList = creditEnquiryApplicationElement
									.getElementsByTagName("CAPS_Applicant_Address_Details");
							for (int k = 0; k < addressDetailsList.getLength(); k++) {
								NodeList childNodesOfAddress = addressDetailsList.item(0).getChildNodes();

								// set credit enquiry address
								creditEnquiry.setAddress(makeAddressData(childNodesOfAddress));
							}

						}
						creditEnquiry.setUserMobileNumber(userPhoneNumber);
						creditEnquiryList.add(creditEnquiry);
					}
				}

				// extract non credit enquiry details from document and
				// set
				// in customer details
				extractNonCreditEnquiryDetails(doc, customerDetails);

				// and save them
				try {

					// save or update bankdata creditenquiries, emailId,
					// mobilenumber, address in database
					saveOrUpdateBankDataAndOtherData(tradelines, customerDetails, creditEnquiryList, emailIdSet,
							mobileNumberSet, addressSet);

				} catch (Exception ex) {
					logger.logException("ParseExperianReportObserver", "parseReport",
							"tradeline add database exception", "", ex);
				}
			} else {
				logger.logException("ParseExperianReportsForExtraDataObserver", "parseReport",
						"ElseBlockReportNullCheck", "ReportNotFound for reportid : " + reportId, null);
			}

		} catch (Exception ex) {
			logger.logException("ParseExperianReportsForExtraDataObserver", "parseReport", "ExceptionInReportParsing",
					" for reportid : " + reportId, null);
		}
		logger.logInfo("ParseExperianReportsForExtraDataObserver", "parseReport", "EndparseReport",
				"End of report Parsing with reportId : " + reportId);

	}

	/**
	 * 
	 * This method give the node from root child nodes on the basis of tag name.
	 * 
	 * @param tagName
	 *            node name
	 * @param nodes
	 *            root child nodes
	 * @return
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
	 * This method converts xml node to string.
	 * 
	 * @param node
	 *            xml node
	 * @return string value
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	private String converNodeToXML(Node node) throws TransformerFactoryConfigurationError, TransformerException {
		StringWriter stringWriter = new StringWriter();
		Transformer xform = TransformerFactory.newInstance().newTransformer();
		xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		xform.setOutputProperty(OutputKeys.INDENT, "yes");
		xform.transform(new DOMSource(node), new StreamResult(stringWriter));
		return stringWriter.toString();

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
		try {
			// if the date is null then return todays date
			if (date == null)
				return null;

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
			// current year and should not less than 1920
			if ((reportDateTime.getYear() + 1900) > Calendar.getInstance().get(Calendar.YEAR)
					|| (reportDateTime.getYear() + 1900) < 1920) {
				return null;
			}
			return reportDateTime;
		} catch (Exception ex) {
			return null;
		}
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
	 * @return
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

	private boolean isValidEmailId(String emailId) {
		if (emailId != null && emailId.isEmpty() == false) {
			Matcher matcher = emailResxPattern.matcher(emailId);
			if (matcher.matches() == true) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is used to get filtered mobile number which is of 10 digits
	 * 
	 * @param mobileNumber
	 *            the mobile number to be validated
	 * @return the validated mobile number else null
	 */
	private String validatedMobileNumber(String mobileNumber) {
		if (mobileNumber.length() > 10) {
			mobileNumber = mobileNumber.substring(mobileNumber.length() - 10);
			if (mobileNumber.startsWith("9") || mobileNumber.startsWith("8") || mobileNumber.startsWith("7")) {
				return mobileNumber;
			}
		}
		if (mobileNumber.length() == 10) {
			if (mobileNumber.startsWith("9") || mobileNumber.startsWith("8") || mobileNumber.startsWith("7")) {
				return mobileNumber;
			}
		}
		return null;

	}

	/**
	 * This method is used to check for email validation and add in to emailset
	 * 
	 * @param cAISHolderPhoneDetails
	 *            the nodelist from which email id to get
	 */
	private void addIntoEmailSet(NodeList nodeList) {
		// check emailId for null or empty value and for email
		// pattern
		if (isValidEmailId(getNodeValue("EMailId", nodeList))) {
			if (emailIdSet.contains(getNodeValue("EMailId", nodeList)) == false) {
				emailIdSet.add(getNodeValue("EMailId", nodeList));
			}
		}
	}

	/**
	 * This method is used to check for mobile number validation and add in to
	 * mobile number set
	 * 
	 * @param cAISHolderPhoneDetails
	 *            the nodelist from which mobile number to get
	 */
	private void addIntoMobileNumberSet(NodeList nodeList) {
		// get mobile number
		String mobileNumber = getNodeValue("Mobile_Telephone_Number", nodeList);
		// check for mobileNumber null or empty
		if (mobileNumber != null && mobileNumber.isEmpty() == false) {
			// validate mobile number
			mobileNumber = validatedMobileNumber(mobileNumber);
			if (mobileNumber != null) {
				if (mobileNumberSet.contains(mobileNumber) == false) {
					mobileNumberSet.add(mobileNumber);
				}
			}

		}
	}

	/**
	 * This method is used to add address entity into address entity set
	 * 
	 * @param addressNodeList
	 *            the address node list to get addresses and add into address
	 *            set
	 */
	private void makeAddressAndAddIntoList(NodeList addressNodeList) {

		Address address = null;
		for (int j = 0; j < addressNodeList.getLength(); j++) {
			NodeList cAISHolderAddressDetais = addressNodeList.item(j).getChildNodes();
			String pincode = getNodeValue("ZIP_Postal_Code_non_normalized", cAISHolderAddressDetais);
			/// check for pincode existence so that we have unique
			/// addresses on basis of pincode uniqueness
			if (pincode.isEmpty() == true || (pincodeSet.contains(pincode) == false)) {
				if (isNullOrEmpty(pincode) == false) {
					// add into pincode set if not empty
					pincodeSet.add(pincode);
				}
				address = new Address();
				address.setId(Integer.toString(j));
				address.setCity(validateAddress(getNodeValue("City_non_normalized", cAISHolderAddressDetais)));
				address.setFirstLineOfAddress(
						validateAddress(getNodeValue("First_Line_Of_Address_non_normalized", cAISHolderAddressDetais)
								.replace("\"", "")));
				address.setFifthLineOfAddress(
						validateAddress(getNodeValue("Fifth_Line_Of_Address_non_normalized", cAISHolderAddressDetais)
								.replace("\"", "")));
				address.setSecondLineOfAddress(
						validateAddress(getNodeValue("Second_Line_Of_Address_non_normalized", cAISHolderAddressDetais)
								.replace("\"", "")));
				address.setState(validateAddress(
						this.getStateNameFromStateCode(getNodeValue("State_non_normalized", cAISHolderAddressDetais))));
				address.setThirdLineOfAddress(
						validateAddress(getNodeValue("Third_Line_Of_Address_non_normalized", cAISHolderAddressDetais)
								.replace("\"", "")));
				address.setCountryCode(
						validateAddress(getNodeValue("CountryCode_non_normalized", cAISHolderAddressDetais)));
				address.setZipCode(validateAddress(pincode));

				address.setAddressType(this.getAddressTypeNameFromCode(
						validateAddress(getNodeValue("Address_indicator_non_normalized", cAISHolderAddressDetais))));
				// address.setAccountNumber(tradeline.getAccountNumber());
				// address.setBankName(tradeline.getOrganizationId());
				// data source manually set
				address.setDataSource("Experian");

				addressSet.add(address);

			}
		}
	}

	/**
	 * This method checks if string value provided is null or empty if yes then
	 * return true
	 * 
	 * @param stringValue
	 *            the value to be checked
	 * @return true if stringvalue is null or empty otherwise false
	 */
	private boolean isNullOrEmpty(String stringValue) {
		if (stringValue == null || stringValue.isEmpty())
			return true;
		return false;
	}

	/**
	 * This method is used to set customer full name
	 * 
	 * @param customerDetails
	 *            the entity whose full name is to set
	 * @param cAISHolderDetails
	 *            the nodelist which contains the tags to get full name
	 */
	private void makeAndSetCustomerFullName(ExperianReportCustomerPersonalDetails customerDetails,
			NodeList cAISHolderDetails) {
		// set firstname, middle name and last name seperatly
		customerDetails.setFirstName(getNodeValue("First_Name_Non_Normalized", cAISHolderDetails));
		customerDetails
				.setMiddleName((isNullOrEmpty(getNodeValue("Middle_Name_1_Non_Normalized", cAISHolderDetails)) == true
						? "" : getNodeValue("Middle_Name_1_Non_Normalized", cAISHolderDetails) + " ")
						+ (isNullOrEmpty(getNodeValue("Middle_Name_2_Non_Normalized", cAISHolderDetails)) == true ? ""
								: getNodeValue("Middle_Name_2_Non_Normalized", cAISHolderDetails) + " ")
						+ (isNullOrEmpty(getNodeValue("Middle_Name_3_Non_Normalized", cAISHolderDetails)) == true ? ""
								: getNodeValue("Middle_Name_3_Non_Normalized", cAISHolderDetails) + " "));
		customerDetails.setLastName(getNodeValue("Surname_Non_Normalized", cAISHolderDetails));

		customerDetails.setFullName(
				customerDetails.getFirstName() + " " + customerDetails.getMiddleName() + customerDetails.getLastName());

	}

	private void fillCustomerDetails(ExperianReportCustomerPersonalDetails customerDetails, NodeList nodelist)
			throws ParseException {
		// check for customer personal details null or empty
		// values and try to fill them here
		if (isNullOrEmpty(customerDetails.getPanNumber())) {
			customerDetails.setPanNumber(getNodeValue("IncomeTaxPan", nodelist));
		}
		if (isNullOrEmpty(customerDetails.getDrivingLicense())) {
			customerDetails.setDrivingLicense(getNodeValue("Driver_License_Number", nodelist));
		}

		if (isNullOrEmpty(customerDetails.getVoterId())) {
			customerDetails.setVoterId(getNodeValue("Voter_s_Identity_Card", nodelist));
		}
		if (isNullOrEmpty(customerDetails.getPassport())) {
			customerDetails.setPassport(getNodeValue("Passport_number", nodelist));
		}
		// dob can only be null not empty
		if (customerDetails.getDob() == null) {
			customerDetails.setDob(this.experianStringToDate(getNodeValue("Date_Of_Birth_Applicant", nodelist)));
		}
		if (isNullOrEmpty(customerDetails.getAadhaarNumber())) {
			customerDetails.setAadhaarNumber(getNodeValue("Universal_ID_Number", nodelist));
		}
	}

	/**
	 * This method is used to make address
	 * 
	 * @param childNodesOfAddress
	 *            the node that contains addressdetails
	 * @return the prepared address
	 */
	private String makeAddressData(NodeList childNodesOfAddress) {

		// populate address entity
		makeAddressAndAddInListEnquiry(childNodesOfAddress);

		return getNodeValue("FlatNoPlotNoHouseNo", childNodesOfAddress) + " "
				+ getNodeValue("BldgNoSocietyName", childNodesOfAddress) + " "
				+ getNodeValue("RoadNoNameAreaLocality", childNodesOfAddress) + " "
				+ getNodeValue("City", childNodesOfAddress) + " " + getNodeValue("Landmark", childNodesOfAddress) + " "
				+ getStateNameFromStateCode(getNodeValue("State", childNodesOfAddress)) + " "
				+ getNodeValue("PINCode", childNodesOfAddress) + " "
				+ getNodeValue("Country_Code", childNodesOfAddress);

	}

	/**
	 * This method is used to populate address entity and add in address list
	 * 
	 * @param childNodesOfAddress
	 *            the nodelist containing address details
	 */
	private void makeAddressAndAddInListEnquiry(NodeList childNodesOfAddress) {
		// make address entity and then add in address set
		Address address = new Address();

		String pincode = getNodeValue("PINCode", childNodesOfAddress);
		String ft = getNodeValue("FlatNoPlotNoHouseNo", childNodesOfAddress);

		/// check for pincode existence so that we have unique
		/// addresses on basis of pincode uniqueness
		if (((pincode.isEmpty() == true) || !(pincodeSet.contains(pincode))) && !ft.equals("")) {
			if (isNullOrEmpty(pincode) == false) {
				// add into pincode set if not empty
				pincodeSet.add(pincode);
			}
			address = new Address();
			// address.setId(Integer.toString(j));
			address.setCity(validateAddress(getNodeValue("City", childNodesOfAddress)));
			address.setFirstLineOfAddress(validateAddress(getNodeValue("FlatNoPlotNoHouseNo", childNodesOfAddress)));
			address.setFifthLineOfAddress(isNullOrEmpty(getNodeValue("Landmark", childNodesOfAddress)) == true ? ""
					: getNodeValue("Landmark", childNodesOfAddress) + " ");
			address.setSecondLineOfAddress(validateAddress(getNodeValue("BldgNoSocietyName", childNodesOfAddress)));
			address.setState(
					validateAddress(this.getStateNameFromStateCode(getNodeValue("State", childNodesOfAddress))));
			address.setThirdLineOfAddress(validateAddress(getNodeValue("RoadNoNameAreaLocality", childNodesOfAddress)));
			address.setDataSource("Experian");

			address.setZipCode(validateAddress(pincode));
			// manually set
			address.setAddressType("Personal");

			// account number and bank name is set at time of saving
			addressSet.add(address);

		}
	}

	/**
	 * This method is used to extract non credit enquiry details
	 * 
	 * @throws ParseException
	 */
	private void extractNonCreditEnquiryDetails(Document doc, ExperianReportCustomerPersonalDetails customerDetails)
			throws ParseException {
		// extract non credit enquiry details
		NodeList nonCreditEnquiryDetails = doc.getElementsByTagName("NonCreditCAPS");
		for (int jj = 0; jj < nonCreditEnquiryDetails.getLength(); jj++) {
			NodeList nonCreditEnquiryDetailsChildNodes = nonCreditEnquiryDetails.item(jj).getChildNodes();
			Element nonCreditEnquiryDetailsChildNodesElement = (Element) nonCreditEnquiryDetailsChildNodes;
			NodeList nonCreditEnquiryApplicationDetails = nonCreditEnquiryDetailsChildNodesElement
					.getElementsByTagName("CAPS_Application_Details");
			for (int jk = 0; jk < nonCreditEnquiryApplicationDetails.getLength(); jk++) {
				NodeList nonCreditEnquiryApplicationDetailsChildNodes = nonCreditEnquiryApplicationDetails.item(jk)
						.getChildNodes();
				Element nonCreditEnquiryApplicationDetailsElement = (Element) nonCreditEnquiryApplicationDetailsChildNodes;
				NodeList nonCreditEnquiryApplicantNodeList = nonCreditEnquiryApplicationDetailsElement
						.getElementsByTagName("CAPS_Applicant_Details");
				for (int m = 0; m < nonCreditEnquiryApplicantNodeList.getLength(); m++) {
					NodeList nonCreditEnquiryApplicantDetails = nonCreditEnquiryApplicantNodeList.item(m)
							.getChildNodes();

					// fill customer details found here in this
					// nodelist
					fillCustomerDetails(customerDetails, nonCreditEnquiryApplicantDetails);

					// check email validity and add in to emailid set
					addIntoEmailSet(nonCreditEnquiryApplicantDetails);
					// check mobile number validity and add in to mobile
					// number
					// set
					addIntoMobileNumberSet(nonCreditEnquiryApplicantDetails);

					// get address details for non credit enquiry
					NodeList addressDetailsList = nonCreditEnquiryApplicationDetailsElement
							.getElementsByTagName("CAPS_Applicant_Address_Details");
					for (int k = 0; k < addressDetailsList.getLength(); k++) {
						NodeList childNodesOfAddress = addressDetailsList.item(0).getChildNodes();

						// populate address entity
						makeAddressAndAddInListEnquiry(childNodesOfAddress);
					}
				}

			}

		}
	}

	private void saveOrUpdateBankDataAndOtherData(BoilerplateList<ReportTradeline> tradelines,
			ExperianReportCustomerPersonalDetails customerDetails, List<CreditEnquiry> creditEnquiryList,
			Set<String> emailIdSet, Set<String> mobileNumberSet, List<Address> addressList) {

		Address addressForBankData = addressList.get(0);
		logger.logInfo("ParseExperianReportsForExtraDataObserver", "saveOrUpdateBankDataAndOtherData",
				"StartsaveOrUpdateBankDataAndOtherData", "about to process tradelines");
		for (int i = 0; i < tradelines.size(); i++) {
			System.out.println("tradeline size : " + tradelines.size());
			// process each tradeline for account number and bank name
			ReportTradeline reportTradeline = (ReportTradeline) tradelines.get(i);
			// TODO need to check for reporttradeline null or empty here
			try {
				if (!(isNullOrEmpty(reportTradeline.getAccountNumber())
						&& isNullOrEmpty(reportTradeline.getOrganizationName()))) {
					List<BankData> requestedData = mySQLBankDataAccess.getBankDataForAccountNumberAndBankName(
							reportTradeline.getAccountNumber(), reportTradeline.getOrganizationName());
					if (isNullOrEmpty(customerDetails.getFirstName())
							&& !isNullOrEmpty(customerDetails.getLastName())) {
						Map<String, String> nameMap = getValidName(customerDetails.getLastName());
						if (nameMap.containsKey("FirstName")) {
							customerDetails.setFirstName(nameMap.get("FirstName"));
						}
						if (nameMap.containsKey("MiddleName")) {
							customerDetails.setMiddleName(nameMap.get("MiddleName"));
						}
						if (nameMap.containsKey("LastName")) {
							customerDetails.setLastName(nameMap.get("LastName"));
						}
					}
					if (requestedData.size() > 0) {
						for (int j = 0; j < requestedData.size(); j++) {
							BankData bankData = requestedData.get(0);
							// name is indented with spaces between name words
							String fullNameInBankData = bankData.getFirstName()
									+ (((isNullOrEmpty(bankData.getMiddleName()) == true) ? " "
											: (bankData.getMiddleName() + " ")) + bankData.getLastName());
							if (getStringMatchingPercentage(customerDetails.getFullName(), fullNameInBankData) > 45) {

								// update bankdata for accountnumber and bank
								// name
								if (isNullOrEmpty(bankData.getFirstName())) {
									bankData.setFirstName(removeSpecialAndTrim(customerDetails.getFirstName()));
								}
								if (isNullOrEmpty(bankData.getLastName())) {
									bankData.setFirstName(removeSpecialAndTrim(customerDetails.getLastName()));
								}
								if (isNullOrEmpty(bankData.getProduct())) {
									bankData.setProduct(removeSpecialAndTrim(reportTradeline.getProductName()));
								}
								if (isNullOrEmpty(bankData.getBankName())) {
									bankData.setBankName(removeSpecialAndTrim(reportTradeline.getOrganizationName()));
								}
								if (bankData.getDateOpened() == null) {
									bankData.setDateOpened(reportTradeline.getDateOpened());
								}
								if (bankData.getStatus() == null) {
									bankData.setStatus(reportTradeline.getExperianTradelineStatusEnum()
											.equals(ExperianTradelineStatus.WrittenOff) ? writeOff
													: String.valueOf(reportTradeline.getExperianTradelineStatusEnum()));
								}
								if (bankData.getLastPaymentDate() == null) {
									bankData.setLastPaymentDate(reportTradeline.getDateOfLastPayment());
								}
								if (bankData.getSanctionedAmount() == null) {
									bankData.setSanctionedAmount(reportTradeline.getHighCreditLoanAmount());
								}
								// being saved in database in yyyy/mm/dd format
								if (bankData.getDateOfBirth() == null) {
									bankData.setDateOfBirth(customerDetails.getDob());
								}
								if (isNullOrEmpty(bankData.getPanNumber())) {
									bankData.setPanNumber(removeSpecialAndTrim(customerDetails.getPanNumber()));
								}
								if (isNullOrEmpty(bankData.getAadhar())) {
									bankData.setAadhar(removeSpecialAndTrim(customerDetails.getAadhaarNumber()));
								}
								if (isNullOrEmpty(bankData.getPassport())) {
									bankData.setPassport(removeSpecialAndTrim(customerDetails.getPassport()));
								}
								if (isNullOrEmpty(bankData.getDrivingLicense())) {
									bankData.setDrivingLicense(
											removeSpecialAndTrim(customerDetails.getDrivingLicense()));
								}
								if (isNullOrEmpty(bankData.getVoterId())) {
									bankData.setVoterId(removeSpecialAndTrim(customerDetails.getVoterId()));
								}
								if (isNullOrEmpty(bankData.getAddressLine1())) {
									bankData.setAddressLine1(
											validateAddress(addressForBankData.getFirstLineOfAddress()));
								}
								if (isNullOrEmpty(bankData.getAddressLine2())) {
									bankData.setAddressLine2(
											validateAddress(addressForBankData.getSecondLineOfAddress()));
								}
								if (isNullOrEmpty(bankData.getAddressLine3())) {
									bankData.setAddressLine3(
											validateAddress(addressForBankData.getThirdLineOfAddress()));
								}
								if (isNullOrEmpty(bankData.getAddressLine4())) {
									bankData.setAddressLine4(
											validateAddress(addressForBankData.getFifthLineOfAddress()));
								}
								if (isNullOrEmpty(bankData.getCity())) {
									bankData.setCity(validateAddress(addressForBankData.getCity()));
								}
								if (isNullOrEmpty(bankData.getState())) {
									bankData.setState(validateAddress(addressForBankData.getState()));
								}
								if (isNullOrEmpty(bankData.getPinCode())) {
									bankData.setPinCode(validateAddress(addressForBankData.getZipCode()));
								}

								try {

									// save or update bankdata
									mySQLBankDataAccess.saveOrUpdateBankData(bankData);

								} catch (Exception ex) {
									logger.logException("ParseExperianReportsForExtraDataObserver",
											"saveOrUpdateBankDataAndOtherData", "catchblock",
											"Exception cause is : " + ex.getCause().toString(), ex);
								}

							}
							// do nothing when name not matched but account
							// number and bank name entry present in database
						}
					} else {
						// insert bank data into database
						BankData bankData = new BankData();
						bankData.setBankName(removeSpecialAndTrim(reportTradeline.getOrganizationName()));
						bankData.setProduct(removeSpecialAndTrim(reportTradeline.getProductName()));
						bankData.setAccountNumber(removeSpecialAndTrim(reportTradeline.getAccountNumber()));

						bankData.setFirstName(removeSpecialAndTrim(customerDetails.getFirstName()));
						bankData.setMiddleName(removeSpecialAndTrim(customerDetails.getMiddleName()));
						bankData.setLastName(removeSpecialAndTrim(customerDetails.getLastName()));
						bankData.setDateOpened(reportTradeline.getDateOpened());
						bankData.setStatus(reportTradeline.getExperianTradelineStatusEnum()
								.equals(ExperianTradelineStatus.WrittenOff) ? writeOff
										: String.valueOf(reportTradeline.getExperianTradelineStatusEnum()));
						bankData.setLastPaymentDate(reportTradeline.getDateOfLastPayment());
						bankData.setSanctionedAmount(reportTradeline.getHighCreditLoanAmount());
						// being saved in database in yyyy/mm/dd format
						bankData.setDateOfBirth(customerDetails.getDob());
						bankData.setPanNumber(removeSpecialAndTrim(customerDetails.getPanNumber()));
						bankData.setAadhar(removeSpecialAndTrim(customerDetails.getAadhaarNumber()));
						bankData.setPassport(removeSpecialAndTrim(customerDetails.getPassport()));
						bankData.setDrivingLicense(removeSpecialAndTrim(customerDetails.getDrivingLicense()));
						bankData.setVoterId(removeSpecialAndTrim(customerDetails.getVoterId()));
						bankData.setDataSource("Experian");

						bankData.setAddressLine1(validateAddress(addressForBankData.getFirstLineOfAddress()));
						bankData.setAddressLine2(validateAddress(addressForBankData.getSecondLineOfAddress()));
						bankData.setAddressLine3(validateAddress(addressForBankData.getThirdLineOfAddress()));
						bankData.setAddressLine4(validateAddress(addressForBankData.getFifthLineOfAddress()));
						bankData.setCity(validateAddress(addressForBankData.getCity()));
						bankData.setState(validateAddress(addressForBankData.getState()));
						bankData.setPinCode(validateAddress(addressForBankData.getZipCode()));

						try {
							// save new bank data entry
							mySQLBankDataAccess.saveOrUpdateBankData(bankData);
						} catch (Exception ex) {
							logger.logException("ParseExperianReportsForExtraDataObserver",
									"saveOrUpdateBankDataAndOtherData", "catchblock",
									"Exception cause is : " + ex.getCause().toString(), ex);
						}

					}

					// save emailIds for tradeline id
					Iterator emails = emailIdSet.iterator();
					while (emails.hasNext()) {
						try {
							mySQLBankDataAccess.saveEmail((String) emails.next(), reportTradeline.getOrganizationName(),
									reportTradeline.getAccountNumber());
						} catch (Exception ex) {
							logger.logException("ParseExperianReportsForExtraDataObserver",
									"saveOrUpdateBankDataAndOtherData", "CatchBlock SavingEmail",
									"Exception saving email : " + ex.getCause().toString(), ex);
						}
					}

					// save mobilenumbers for tradeline id
					Iterator mobileNumbers = mobileNumberSet.iterator();
					while (mobileNumbers.hasNext()) {
						try {
							mySQLBankDataAccess.saveMobileNumber((String) mobileNumbers.next(),
									reportTradeline.getOrganizationName(), reportTradeline.getAccountNumber());
						} catch (Exception ex) {
							logger.logException("ParseExperianReportsForExtraDataObserver",
									"saveOrUpdateBankDataAndOtherData", "CatchBlock SavingMobileNumber",
									"Exception saving mobileNumber cause : " + ex.getCause().toString(), ex);
						}
					}

					for (int addressIndex = 0; addressIndex < addressList.size(); addressIndex++) {
						try {
							Address address = addressList.get(addressIndex);
							address.setAccountNumber(reportTradeline.getAccountNumber());
							address.setBankName(reportTradeline.getOrganizationName());
							mySQLBankDataAccess.saveAddress(address);
						} catch (Exception ex) {
							logger.logException("ParseExperianReportsForExtraDataObserver",
									"saveOrUpdateBankDataAndOtherData", "CatchBlock SavingAddress",
									"Exception saving address , cause : " + ex.getCause().toString(), ex);
						}
					}

				} else {
					logger.logException("ParseExperianReportsForExtraDataObserver", "saveOrUpdateBankDataAndOtherData",
							"SaveTradelineException",
							"tradeline donot have account number and bank name tradelineId is : "
									+ reportTradeline.getId(),
							null);
				}

			} catch (Exception ex) {
				logger.logException("ParseExperianReportsForExtraDataObserver", "saveOrUpdateBankDataAndOtherData",
						"SaveUpdateTradeline",
						"Exception in tradeline SaveUpdate, tradelineId : " + reportTradeline.getId(), ex);
			}
		}
		logger.logInfo("ParseExperianReportsForExtraDataObserver", "saveOrUpdateBankDataAndOtherData",
				"EndSaveOfTradeline", "");

		// save credit enquiry
		for (CreditEnquiry creditEnquiry : creditEnquiryList) {
			logger.logInfo("ParseExperianReportsForExtraDataObserver", "saveOrUpdateBankDataAndOtherData",
					"Insert-CreditEnquiry-START", " about to insert credit enquiry");
			try {
				// save credit enquiry
				mySQLBankDataAccess.saveCreditEnquiry(creditEnquiry);
			} catch (Exception ex) {
				logger.logException("ParseExperianReportsForExtraDataObserver", "saveOrUpdateBankDataAndOtherData",
						"Exception", "Exception in saving credit enquiry, cause : " + ex.getCause().toString(), ex);
			}

			logger.logInfo("ParseExperianReportsForExtraDataObserver", "saveOrUpdateBankDataAndOtherData",
					"Insert-CreditEnquiry-END", " Inserted credit enquiry");
		}
		logger.logInfo("ParseExperianReportsForExtraDataObserver", "saveOrUpdateBankDataAndOtherData", "End", "");

	}

	/**
	 * This method is used to compare two string values to calculate and give
	 * matching percentage
	 * 
	 * @param compareString1
	 *            the firstString to compare
	 * @param compareString2
	 *            the second String to compare
	 * @return the percentageof match
	 */
	public int getStringMatchingPercentage(String compareString1, String compareString2) {

		int percentage = 0;
		percentage = FuzzySearch.ratio(compareString1, compareString2);
		logger.logInfo("ParseExperianReportsForExtraDataObserver", "getStringMatchingPercentage",
				"EndGetStringMatchingPercentage", "about match strings String 1 is :" + compareString1
						+ " and second string is : " + compareString2 + " and percentage is: " + percentage);
		return percentage;

	}

	/**
	 * This method gives the address type name for address type code
	 * 
	 * @param addressTypeCode
	 *            the addresstypeCode
	 * @return the address type name
	 */
	private String getAddressTypeNameFromCode(String addressTypeCode) {
		switch (addressTypeCode) {
		case "01":
			return "Permanent address";
		case "02":
			return "Residence Address";
		case "03":
			return "Office Address";
		case "04":
			return "Not Categorised";
		default:
			return "Personal";
		}

	}

	/**
	 * This method is used to get search type from enquiry reason code
	 * 
	 * @param reasonCode
	 *            against which search type is to get
	 * @return the search type mapped against reason code
	 */
	private String getSearchTypeFromReasonCode(String reasonCode) {
		switch (reasonCode) {

		case "1":
			return "Agriculture Loan";
		case "2":
			return "Auto Loan";
		case "3":
			return "Business Loan";
		case "4":
			return "Commercial Vehicle Loans";
		case "5":
			return "Construction Equipment loan";
		case "6":
			return "Consumer Loan";
		case "7":
			return "Credit Card";
		case "8":
			return "Education Loan";
		case "9":
			return "Leasing";
		case "10":
			return "Loan against collateral";
		case "11":
			return "Microfinance";
		case "12":
			return "Non-funded Credit Facility";
		case "13":
			return "Personal Loan";
		case "14":
			return "Property Loan";
		case "15":
			return "Telecom";
		case "16":
			return "Two/Three Wheeler Loan";
		case "17":
			return "Working Capital Loan";
		case "99":
			return "Others";

		default:
			return null;
		}
	}

	/**
	 * This method is used to get state name from state code
	 * 
	 * @param stateCode
	 *            the state code for which state names to get
	 * @return the state name
	 */
	private String getStateNameFromStateCode(String stateCode) {
		switch (stateCode) {
		case "01":
			return "JAMMU and KASHMIR";
		case "02":
			return "HIMACHAL PRADESH";
		case "03":
			return "PUNJAB";
		case "04":
			return "CHANDIGARH";
		case "05":
			return "UTTRANCHAL";
		case "06":
			return "HARAYANA";
		case "07":
			return "DELHI";
		case "08":
			return "RAJASTHAN";
		case "09":
			return "UTTAR PRADESH";
		case "10":
			return "BIHAR";
		case "11":
			return "SIKKIM";
		case "12":
			return "ARUNACHAL PRADESH";
		case "13":
			return "NAGALAND";
		case "14":
			return "MANIPUR";
		case "15":
			return "MIZORAM";
		case "16":
			return "TRIPURA";
		case "17":
			return "MEGHALAYA";
		case "18":
			return "ASSAM";
		case "19":
			return "WEST BENGAL";
		case "20":
			return "JHARKHAND";
		case "21":
			return "ORRISA";
		case "22":
			return "CHHATTISGARH";
		case "23":
			return "MADHYA PRADESH";
		case "24":
			return "GUJRAT";
		case "25":
			return "DAMAN and DIU";
		case "26":
			return "DADARA and NAGAR HAVELI";
		case "27":
			return "MAHARASHTRA";
		case "28":
			return "ANDHRA PRADESH";
		case "29":
			return "KARNATAKA";
		case "30":
			return "GOA";
		case "31":
			return "LAKSHADWEEP";
		case "32":
			return "KERALA";
		case "33":
			return "TAMIL NADU";
		case "34":
			return "PONDICHERRY";
		case "35":
			return "ANDAMAN and NICOBAR ISLANDS";
		case "36":
			return "TELANGANA";
		default:
			return "";
		}

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

	private String removeSpecialAndTrim(String data) {
		data = data.replaceAll("[^\\w- .]+", "");
		// Remove starting and ending spaces
		data = data.trim();
		return data;
	}

	/**
	 * This method is used to validate the mobile number related data
	 * 
	 * @param data
	 *            this is the mobile number data
	 * @return true if data is valid and false if not valid
	 */
	public static String validateAddress(String address) {
		// validate data
		return address.replaceAll("[^\\w- / ,]+", "").trim();
	}

	/**
	 * This method is used to get the valid the name
	 * 
	 * @param data
	 *            this is the data
	 * @return the valid name
	 */
	private Map<String, String> getValidName(String data) {
		// This is used to generate a correct format name
		Map<String, String> validName = new HashMap<>();
		// Validate the string remove all the special character
		data = data.replaceAll("[^\\w- .]+", "");
		// Remove starting and ending spaces
		data = data.trim();
		// Split the name from space
		String[] nameData = data.split("\\s+");
		// Initialize the count
		int count = 0;
		// Middle name
		String middleName = "";
		// For loop to format the name
		for (String name : nameData) {
			// If count equal to zero means it is first name
			if (count == 0) {
				validName.put("FirstName", name.substring(0, 1).toUpperCase() + name.substring(1));

			} else if (count == nameData.length - 1) {

				// Check is the name contains the title then replace the title
				// with first name
				if (this.checkIsNameContainsTitle(validName.get("FirstName")) && count == 1) {
					// Update the first name
					validName.put("FirstName", name.substring(0, 1).toUpperCase() + name.substring(1));
				} else {// Last name
					validName.put("LastName", name);
				}
			} else {
				// Check is the name contains the title then replace the title
				// with first name
				if (this.checkIsNameContainsTitle(validName.get("FirstName")) && count == 1) {
					// Update the first name
					validName.put("FirstName", name.substring(0, 1).toUpperCase() + name.substring(1));
				} else {
					// Add name in middle name
					middleName = middleName + " " + name;
				}
			}
			count++;
		}
		if (middleName != null && !middleName.isEmpty()) {
			// Put middle name in map if the middle name is not null
			validName.put("MiddleName", middleName);
		}
		return validName;
	}

	/**
	 * This method is used to check is the name contains the title
	 * 
	 * @param fullName
	 *            this is the full name
	 * @return true if name contains the title else return false
	 */
	private boolean checkIsNameContainsTitle(String fullName) {
		// Check is debug level log is enabled or not
		// Get name title list which is need to ignore from name
		String nameTitles = "Mr.,Mrs.,Dr.,Mr,Mrs,Dr";
		// If name titles is not null or blank then check existence
		if (nameTitles != null && !nameTitles.isEmpty() && !nameTitles.equals("")) {
			// Get the list of titles from the configurations
			List<String> titleList = new ArrayList<String>(Arrays.asList(nameTitles.split(",")));
			// If size is greater than zero
			if (titleList.size() > 0) {
				// Check for each title
				for (String title : titleList) {
					// Check is the
					// if (fullName.toLowerCase().contains(title.toLowerCase() +
					// " ")) {
					// return true;
					// }
					if (fullName.toLowerCase().equals(title.toLowerCase())) {
						return true;
					}
				}
			}
		}

		return false;
	}

}
