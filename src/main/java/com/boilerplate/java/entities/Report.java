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
