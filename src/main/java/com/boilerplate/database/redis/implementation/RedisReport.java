package com.boilerplate.database.redis.implementation;

import java.util.List;
import java.util.Set;

import com.boilerplate.database.interfaces.IReport;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExperianQuestionAnswer;
import com.boilerplate.java.entities.Report;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.java.entities.ReportTradeline;

public class RedisReport extends BaseRedisDataAccessLayer implements IReport {

	private String Report = "Report:";

	/**
	 * This is the variable to hold the key for akshar experian question answer
	 */
	private static final String AKS_ExperianQuestionAnswer = "AKS_ExperianQuestionAnswer:";

	/**
	 * @see IReport.getReports
	 */
	@Override
	public BoilerplateMap<String, Report> getReports(String userId) {

		Set<String> userReportIds = super.keys(Report + userId.toUpperCase() + ":*");
		BoilerplateMap<String, Report> reports = new BoilerplateMap();
		for (String userReportId : userReportIds) {
			reports.put(userReportId, super.get(userReportId, Report.class));
		}
		return reports;

	}

	/**
	 * @see IReport.save
	 */
	@Override
	public Object saveReport(com.boilerplate.java.entities.Report report) {
		report.setId((report.getUserId().toUpperCase() + ":" + report.getReportNumber()).toUpperCase());
		for (Object object : report.getReportTradelines()) {
			ReportTradeline reportTradeLine = (ReportTradeline) object;
			reportTradeLine.setId((report.getId() + ":" + reportTradeLine.getOrganizationName() + ":"
					+ reportTradeLine.getProductName() + ":" + reportTradeLine.getAccountNumber()).toUpperCase());
		}
		super.set(Report + report.getId(), report);
		return report;
	}

	/**
	 * @see IReport.getExperianQuestionAnswer
	 */
	@Override
	public ExperianQuestionAnswer getExperianQuestionAnswer(String userId, String questionId) {
		return super.get(AKS_ExperianQuestionAnswer + userId + ":" + questionId, ExperianQuestionAnswer.class);
	}

	/**
	 * @see IReport.checkQuestionAnswerExists
	 */
	@Override
	public boolean checkQuestionAnswerExists(String userId) {
		return super.exists(AKS_ExperianQuestionAnswer + userId);
	}

	@Override
	public void saveExperianQuestionAnswer(String userId, String questionId,
			ExperianQuestionAnswer experianQuestionAnswer) {
		super.set(AKS_ExperianQuestionAnswer + userId + ":" + questionId, experianQuestionAnswer);
	}

}
