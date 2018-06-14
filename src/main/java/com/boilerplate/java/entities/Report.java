package com.boilerplate.java.entities;

import java.io.Serializable;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * This class is aReportEntity which contains all the data of the report
 * 
 * @author amit
 *
 */

@ApiModel(value = "A report", description = "A report", parent = BaseEntity.class)
public class Report extends BaseEntity implements Serializable {

	/**
	 * This is the userId
	 */
	private String userId;
	/**
	 * This is the file id
	 */
	private String fileId;

	/**
	 * this is the report status enum
	 */

	private ReportStatus reportStatusEnum;

	/**
	 * This is the bureauScore
	 */
	private int bureauScore;

	/**
	 * This is the report date
	 */
	private String reportDateTime;

	/**
	 * This is the report number
	 */
	private String reportNumber;

	/**
	 * This is the number of question Count
	 */
	private int questionCount;

	// This is the file entity
	FileEntity fileEntity;

	/**
	 * This is the report trade line
	 */
	BoilerplateList<ReportTradeline> reportTradelines = new BoilerplateList<>();

	/**
	 * This method is used to get the reportTradelines
	 * 
	 * @return reportTradelines
	 */

	public BoilerplateList<ReportTradeline> getReportTradelines() {
		return reportTradelines;
	}

	/**
	 * This method is used to set the reportTradelines
	 * 
	 * @param reportTradelines
	 */
	public void setReportTradelines(BoilerplateList<ReportTradeline> reportTradelines) {
		this.reportTradelines = reportTradelines;
	}

	/**
	 * This method is used to get the user id
	 * 
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * This method is used to set the u
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This method is used to get the fileId
	 * 
	 * @return fileId
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * This method is used to set the fileId
	 * 
	 * @param fileId
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * This method is used to get the reportStatusEnum
	 * 
	 * @return reportStatusEnum
	 */
	public ReportStatus getReportStatusEnum() {
		return reportStatusEnum;
	}

	/**
	 * This method is used to set the reportStatusEnum
	 * 
	 * @param reportStatusEnum
	 */
	public void setReportStatusEnum(ReportStatus reportStatusEnum) {
		this.reportStatusEnum = reportStatusEnum;
	}

	/**
	 * This method is used to get the bureauScore
	 * 
	 * @return bureauScore
	 */
	public int getBureauScore() {
		return bureauScore;
	}

	/**
	 * This method is used to set the bureauScore
	 * 
	 * @param bureauScore
	 */
	public void setBureauScore(int bureauScore) {
		this.bureauScore = bureauScore;
	}

	/**
	 * This method is used to get the reportDateTime
	 * 
	 * @return reportDateTime
	 */
	public String getReportDateTime() {
		return reportDateTime;
	}

	/**
	 * This method is used to set the reportDateTime
	 * 
	 * @param reportDateTime
	 */
	public void setReportDateTime(String reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

	/**
	 * This method is used to get the reportNumber
	 * 
	 * @return reportNumber
	 */
	public String getReportNumber() {
		return reportNumber;
	}

	/**
	 * This method is used to set the reportNumber
	 * 
	 * @param reportNumber
	 */
	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}

	/**
	 * This method is used to get the questionCount
	 * 
	 * @return questionCount
	 */
	public int getQuestionCount() {
		return questionCount;
	}

	/**
	 * This method is used to set the questionCount
	 * 
	 * @param questionCount
	 */
	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	/**
	 * This method is used to get the fileEntity
	 * 
	 * @return fileEntity
	 */
	public FileEntity getFileEntity() {
		return fileEntity;
	}

	/**
	 * This method is used to set the fileEntity
	 * 
	 * @param fileEntity
	 */
	public void setFileEntity(FileEntity fileEntity) {
		this.fileEntity = fileEntity;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

}

//
//
// @ApiModel(value = "A report", description = "A report", parent =
// BaseEntity.class)
// public class Report extends BaseEntity implements Serializable,
// ICRMPublishEntity {
//
//
//
//
//
//
//
//
//
// /**
// * Gets file entity
// *
// * @return file entity
// */
// public FileEntity getFileEntity() {
// return fileEntity;
// }
//
// /**
// * Sets file entity
// *
// * @param fileEntity
// * to set
// */
// public void setFileEntity(FileEntity fileEntity) {
// this.fileEntity = fileEntity;
// }
//
// /**
// * Gets report tradelines
// *
// * @return the list of report tradelines
// */
// public BoilerplateList<ReportTradeline> getReportTradelines() {
// return reportTradelines;
// }
//
// /**
// * Sets report tradelines
// *
// * @param reportTradelines
// * to set
// */
// public void setReportTradelines(BoilerplateList<ReportTradeline>
// reportTradelines) {
// this.reportTradelines = reportTradelines;
// }
//
// /**
// * @see BaseEntity.validate
// */
// @Override
// public boolean validate() throws ValidationFailedException {
// return true;
// }
//
// /**
// * @see BaseEntity.transformToInternal
// */
// @Override
// public BaseEntity transformToInternal() {
// return this;
// }
//
// /**
// * Gets the user id
// *
// * @return the user id
// */
// public String getUserId() {
// return userId;
// }
//
// /**
// * Sets user id
// *
// * @param userId
// * to set
// */
// public void setUserId(String userId) {
// this.userId = userId;
// }
//
// /**
// * Gets file id
// *
// * @return file id
// */
// public String getFileId() {
// return fileId;
// }
//
// /**
// * Sets file id
// *
// * @param fileId
// * to set
// */
// public void setFileId(String fileId) {
// this.fileId = fileId;
// }
//
// /**
// * Gets report source
// *
// * @return the report source
// */
// public ReportSource getReportSourceEnum() {
// return reportSourceEnum;
// }
//
// /**
// * Sets report source
// *
// * @param reportSource
// * to set
// */
// public void setReportSourceEnum(ReportSource reportSourceEnum) {
// this.reportSourceEnum = reportSourceEnum;
// }
//
// /**
// * Gets report status
// *
// * @return the report status
// */
// public ReportStatus getReportStatusEnum() {
// return reportStatusEnum;
// }
//
// /**
// * Sets report status
// *
// * @param reportStatus
// * to set
// */
// public void setReportStatusEnum(ReportStatus reportStatusEnum) {
// this.reportStatusEnum = reportStatusEnum;
// }
//
// /**
// * Gets bureau source
// *
// * @return the bureau source
// */
// public int getBureauScore() {
// return bureauScore;
// }
//
// /**
// * Sets bureau source
// *
// * @param bureauScore
// * to set
// */
// public void setBureauScore(int bureauScore) {
// this.bureauScore = bureauScore;
// }
//
// /**
// * Gets reportdatetime
// *
// * @return the reportdatetime
// */
// public String getReportDateTime() {
// return reportDateTime;
// }
//
// /**
// * Sets reportdatetime
// *
// * @param reportDateTime
// * to set
// */
// public void setReportDateTime(String reportDateTime) {
// this.reportDateTime = reportDateTime;
// }
//
// /**
// * Gets report number
// *
// * @return the report number
// */
// public String getReportNumber() {
// return reportNumber;
// }
//
// /**
// * Sets the report number
// *
// * @param reportNumber
// * to set
// */
// public void setReportNumber(String reportNumber) {
// this.reportNumber = reportNumber;
// }
//
// /**
// * Gets credit rating
// *
// * @return the credit rating
// */
// public String getCreditRating() {
// return creditRating;
// }
//
// /**
// * Sets credit rating
// *
// * @param creditRating
// * to set
// */
// public void setCreditRating(String creditRating) {
// this.creditRating = creditRating;
// }
//
// /**
// * Gets report version Enum
// *
// * @return the report version Enum
// */
// public ReportVersion getReportVersionEnum() {
// return reportVersionEnum;
// }
//
// /**
// * Sets report version
// *
// * @param reportVersion
// * to set
// */
// public void setReportVersionEnum(ReportVersion reportVersionEnum) {
// this.reportVersionEnum = reportVersionEnum;
// }
//
// /**
// * @see BaseEntity.transformToExrternal
// */
// @Override
// public BaseEntity transformToExternal() {
// return this;
// }
//
// /**
// * This is the id of the user for whom the report is generated
// */
// @ApiModelProperty(value = "The id of the user for whom the report is
// generated")
// private String userId;
// /**
// * This is id of the file for the report
// */
// @ApiModelProperty(value = "The id of the file for the report")
// private String fileId;
// /**
// * This is
// */
// @ApiModelProperty(value = "This is the source of report")
// private ReportSource reportSourceEnum;
// /**
// * This is status of report
// */
// @ApiModelProperty(value = "This is the status of report")
// private ReportStatus reportStatusEnum;
// /**
// * This is bureauScore
// */
// @ApiModelProperty(value = "This is score")
// private int bureauScore;
// /**
// * This is when report was created on the bureau
// */
// @ApiModelProperty(value = "When report was created on the bureau")
// private String reportDateTime;
// /**
// * This is the report number
// */
// @ApiModelProperty(value = "This is the report number")
// private String reportNumber;
// /**
// * This is the credit rating
// */
// @ApiModelProperty(value = "This is the credit rating")
// private String creditRating;
// /**
// * This is the version of report
// */
// @ApiModelProperty(value = "This is the version of report")
// private ReportVersion reportVersionEnum;
//
// /**
// * Gets the report source
// *
// * @return the report source
// */
// public int getReportSource() {
// return this.reportSourceEnum.ordinal();
// }
//
// /**
// * Gets report status
// *
// * @return the report status
// */
// public int getReportStatus() {
// return this.reportStatusEnum.ordinal();
// }
//
// /**
// * Gets report version
// *
// * @return the report version
// */
// public int getReportVersion() {
// return this.reportVersionEnum.ordinal();
// }
//
// /**
// * Sets report source
// *
// * @param reportSource
// * to set
// */
// public void setReportSource(int reportSource) {
// this.reportSourceEnum = ReportSource.values()[reportSource];
// }
//
// /**
// * Sets report status
// *
// * @param reportStatus
// * to set
// */
// public void setReportStatus(int reportStatus) {
// this.reportStatusEnum = ReportStatus.values()[reportStatus];
// }
//
// /**
// * Sets report version
// *
// * @param reportVersion
// * to set
// */
// public void setReportVersion(int reportVersion) {
// this.reportVersionEnum = ReportVersion.values()[reportVersion];
// }
//
// /**
// * This is the file entity
// */
// FileEntity fileEntity;
//
// /**
// * This is the report tradelines list
// */
// BoilerplateList<ReportTradeline> reportTradelines = new BoilerplateList<>();
//
// /**
// * This method get the question count
// *
// * @return the questionCount
// */
//
// private int questionCount;
//
// /**
// * This method creates the organization data for publishing
// *
// * @return retrunValue The publish data string
// */
// @Override
// public String createPublishJSON(String template) {
// String retrunValue = template;
// retrunValue = retrunValue.replace("@reportId", this.getId());
// retrunValue = retrunValue.replace("@uniqueTransactionId",
// this.getUniqueTransitionId() == null ? "" : this.getUniqueTransitionId());
// retrunValue = retrunValue.replace("@userId", this.getUserId());
// retrunValue = retrunValue.replace("@fileId", this.getFileId() == null ? "" :
// this.getFileId());
// retrunValue = retrunValue.replace("@reportSourceEnum",
// this.getReportSourceEnum().toString());
// retrunValue = retrunValue.replace("@reportStatusEnum",
// this.getReportStatusEnum().toString());
// retrunValue = retrunValue.replace("@bureauScore",
// Integer.toString(this.getBureauScore()));
// retrunValue = retrunValue.replace("@reportDateTime",
// this.getReportDateTime() == null ? "" : this.getReportDateTime());
// retrunValue = retrunValue.replace("@reportNumber", this.getReportNumber());
// retrunValue = retrunValue.replace("@creditRating",
// this.getCreditRating() == null ? "" : this.getCreditRating());
// retrunValue = retrunValue.replace("@reportVersionEnum",
// this.getReportVersionEnum().toString());
// retrunValue = retrunValue.replace("@fileEntity",
// this.getFileEntity() == null ? "{}" : this.getFileEntity().toString());
// Gson gson1 = new
// com.google.gson.GsonBuilder().disableHtmlEscaping().create();
//
// BoilerplateList<Map> tradelineList = new BoilerplateList<>();
// for (Object o : this.getReportTradelines()) {
// ReportTradeline reportTradeline = (ReportTradeline) o;
// Map tradelineMap = Base.toMap(reportTradeline);
// BoilerplateList<Address> tradeLinesAddresses =
// reportTradeline.getAddresses();
// BoilerplateList<ElectronicContact> tradelineElectronicsContact =
// reportTradeline.getElectronicContacts();
// // override some value for publishing
// tradelineMap.put("electronicContacts", tradelineElectronicsContact);
// tradelineMap.put("addresses", tradeLinesAddresses);
// tradelineMap.put("dateOpened", reportTradeline.getDateOpened());
// tradelineMap.put("lastHistoryDate", reportTradeline.getLastHistoryDate());
// tradelineMap.put("dateOfLastPayment",
// reportTradeline.getDateOfLastPayment());
// tradelineMap.put("dateClosed", reportTradeline.getDateClosed());
// tradelineMap.put("dateReported", reportTradeline.getDateReported());
// tradelineList.add(tradelineMap);
// }
// retrunValue = retrunValue.replace("@reportTradelines",
// gson1.toJson(tradelineList));
// retrunValue = retrunValue.replace("@reportSource",
// Integer.toString(this.getReportSource()));
//
// retrunValue = retrunValue.replace("@questionCount",
// Integer.toString(this.getQuestionCount()));
// retrunValue = retrunValue.replace("@reportStatus",
// Integer.toString(this.getReportStatus()));
// retrunValue = retrunValue.replace("@reportVersion",
// Integer.toString(this.getReportVersion()));
// return retrunValue;
//
// }
//
// /**
// * This method get the question count
// *
// * @return the questionCount
// */
// public int getQuestionCount() {
// return questionCount;
// }
//
// /**
// * This method set the question count
// *
// * @param questionCount
// * the questionCount to set
// */
// public void setQuestionCount(int questionCount) {
// this.questionCount = questionCount;
// }
//
// /**
// * This is the crm report id
// */
// private String crmReportId;
//
// /**
// * This method get the crm report id
// *
// * @return the crmReportId
// */
// public String getCrmReportId() {
// return crmReportId;
// }
//
// /**
// * This method set the crm report id
// *
// * @param crmReportId
// * the crmReportId to set
// */
// public void setCrmReportId(String crmReportId) {
// this.crmReportId = crmReportId;
// }
//
// /**
// * This method get the unique transaction id
// *
// * @return the uniqueTransitionId
// */
// public String getUniqueTransitionId() {
// return uniqueTransitionId;
// }
//
// /**
// * This method set the unique transaction id
// *
// * @param uniqueTransitionId
// * the uniqueTransitionId to set
// */
// public void setUniqueTransitionId(String uniqueTransitionId) {
// this.uniqueTransitionId = uniqueTransitionId;
// }
//
// /**
// * This is the unique transition id
// */
// private String uniqueTransitionId;
// }
