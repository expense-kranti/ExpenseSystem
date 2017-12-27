package com.boilerplate.java.entities;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.service.interfaces.IReportService;
import com.google.gson.Gson;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="A report", description="A report", parent=BaseEntity.class)
public class Report extends BaseEntity implements Serializable, ICRMPublishEntity{
	
	public FileEntity getFileEntity() {
		return fileEntity;
	}

	public void setFileEntity(FileEntity fileEntity) {
		this.fileEntity = fileEntity;
	}

	public BoilerplateList<ReportTradeline> getReportTradelines() {
		return reportTradelines;
	}

	public void setReportTradelines(BoilerplateList<ReportTradeline> reportTradelines) {
		this.reportTradelines = reportTradelines;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}
	
	
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public ReportSource getReportSourceEnum() {
		return reportSourceEnum;
	}

	public void setReportSourceEnum(ReportSource reportSourceEnum) {
		this.reportSourceEnum = reportSourceEnum;
	}

	public ReportStatus getReportStatusEnum() {
		return reportStatusEnum;
	}

	public void setReportStatusEnum(ReportStatus reportStatusEnum) {
		this.reportStatusEnum = reportStatusEnum;
	}

	public int getBureauScore() {
		return bureauScore;
	}

	public void setBureauScore(int bureauScore) {
		this.bureauScore = bureauScore;
	}

	public String getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(String reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

	public String getReportNumber() {
		return reportNumber;
	}

	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}

	public String getCreditRating() {
		return creditRating;
	}

	public void setCreditRating(String creditRating) {
		this.creditRating = creditRating;
	}

	public ReportVersion getReportVersionEnum() {
		return reportVersionEnum;
	}

	public void setReportVersionEnum(ReportVersion reportVersionEnum) {
		this.reportVersionEnum = reportVersionEnum;
	}

	@Override
	public BaseEntity transformToExternal() {
		return this;
	}
	
	@ApiModelProperty(value="The id of the user for whom the report is generated")
	private String userId;
	
	@ApiModelProperty(value="The id of the file for the report")
	private String fileId;
	
	@ApiModelProperty(value="This is the source of report")
	private ReportSource reportSourceEnum;

	@ApiModelProperty(value="This is the status of report")
	private ReportStatus reportStatusEnum;

	@ApiModelProperty(value="This is score")
	private int bureauScore;
	

	@ApiModelProperty(value="When report was created on the bureau")
	private String reportDateTime;
	

	@ApiModelProperty(value="This is the report number")
	private String reportNumber;
	

	@ApiModelProperty(value="This is the credit rating")
	private String creditRating;
	

	@ApiModelProperty(value="This is the version of report")
	private ReportVersion reportVersionEnum;
	
	public int getReportSource(){
		return this.reportSourceEnum.ordinal();
	}
	
	public int getReportStatus(){
		return this.reportStatusEnum.ordinal();
	}
	
	public int getReportVersion(){
		return this.reportVersionEnum.ordinal();
	}
	
	public void setReportSource(int reportSource){
		this.reportSourceEnum = ReportSource.values()[reportSource];
	}
	
	public void setReportStatus(int reportStatus){
		this.reportStatusEnum = ReportStatus.values()[reportStatus];
	}
	
	public void setReportVersion(int reportVersion){
		this.reportVersionEnum = ReportVersion.values()[reportVersion];
	} 
	
	FileEntity fileEntity;
	
	BoilerplateList<ReportTradeline> reportTradelines = new BoilerplateList<>();
	
	/**
	 * This method get the question count
	 * @return the questionCount
	 */
	
	private int questionCount;
	/**
	 * This method creates the organization data for publishing
	 * @return retrunValue The publish data string
	 */
	@Override
	public String createPublishJSON(String template) {
		String retrunValue = template;	
		retrunValue = retrunValue.replace("@reportId", this.getId());
		retrunValue = retrunValue.replace("@uniqueTransactionId", this.getUniqueTransitionId()==null?"":this.getUniqueTransitionId());
		retrunValue = retrunValue.replace("@userId", this.getUserId());
		retrunValue = retrunValue.replace("@fileId", this.getFileId()== null ? "" :this.getFileId());
		retrunValue = retrunValue.replace("@reportSourceEnum", this.getReportSourceEnum().toString());
		retrunValue = retrunValue.replace("@reportStatusEnum", this.getReportStatusEnum().toString());
		retrunValue = retrunValue.replace("@bureauScore", Integer.toString(this.getBureauScore()));
		retrunValue = retrunValue.replace("@reportDateTime", this.getReportDateTime()==null?"":this.getReportDateTime());
		retrunValue = retrunValue.replace("@reportNumber", this.getReportNumber());
		retrunValue = retrunValue.replace("@creditRating", this.getCreditRating()==null?"":this.getCreditRating());
		retrunValue = retrunValue.replace("@reportVersionEnum", this.getReportVersionEnum().toString());
		retrunValue = retrunValue.replace("@fileEntity", this.getFileEntity()==null ? "{}" :this.getFileEntity().toString());
		Gson gson1 = new com.google.gson.GsonBuilder().disableHtmlEscaping().create();
		
		BoilerplateList<Map> tradelineList = new BoilerplateList<>();
		for(Object o: this.getReportTradelines()){
			ReportTradeline reportTradeline = (ReportTradeline)o;
			Map tradelineMap = Base.toMap(reportTradeline);
			BoilerplateList<Address> tradeLinesAddresses = reportTradeline.getAddresses();
			BoilerplateList<ElectronicContact> tradelineElectronicsContact = reportTradeline.getElectronicContacts();
			// override some value for publishing 
			tradelineMap.put("electronicContacts", tradelineElectronicsContact);
			tradelineMap.put("addresses", tradeLinesAddresses);
			tradelineMap.put("dateOpened", reportTradeline.getDateOpened());
			tradelineMap.put("lastHistoryDate", reportTradeline.getLastHistoryDate());
			tradelineMap.put("dateOfLastPayment", reportTradeline.getDateOfLastPayment());
			tradelineMap.put("dateClosed", reportTradeline.getDateClosed());
			tradelineMap.put("dateReported", reportTradeline.getDateReported());
			tradelineList.add(tradelineMap);
		}
		retrunValue = retrunValue.replace("@reportTradelines",gson1.toJson(tradelineList));
		retrunValue = retrunValue.replace("@reportSource", Integer.toString(this.getReportSource()));
		
		retrunValue = retrunValue.replace("@questionCount", Integer.toString(this.getQuestionCount()));
		retrunValue = retrunValue.replace("@reportStatus", Integer.toString(this.getReportStatus()));
		retrunValue = retrunValue.replace("@reportVersion", Integer.toString(this.getReportVersion()));
		return retrunValue;
		
	}

	/**
	 * This method get the question count
	 * @return the questionCount
	 */
	public int getQuestionCount() {
		return questionCount;
	}

	/**
	 * This method set the question count
	 * @param questionCount the questionCount to set
	 */
	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}
	
	private String crmReportId;
	/**
	 * This method get the crm report id
	 * @return the crmReportId
	 */
	public String getCrmReportId() {
		return crmReportId;
	}

	/**
	 * This method set the crm report id
	 * @param crmReportId the crmReportId to set
	 */
	public void setCrmReportId(String crmReportId) {
		this.crmReportId = crmReportId;
	}
	
	/**
	 * This method get the unique transaction id
	 * @return the uniqueTransitionId
	 */
	public String getUniqueTransitionId() {
		return uniqueTransitionId;
	}

	/**
	 * This method set the unique transaction id
	 * @param uniqueTransitionId the uniqueTransitionId to set
	 */
	public void setUniqueTransitionId(String uniqueTransitionId) {
		this.uniqueTransitionId = uniqueTransitionId;
	}

	private String uniqueTransitionId;
}
