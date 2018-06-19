package com.boilerplate.database.redis.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IExperian;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.Voucher;

/**
 * This is the class used to implements Redis Voucher
 * 
 * @author mohit
 *
 */
public class RedisVoucher extends BaseRedisDataAccessLayer implements IExperian {

	/**
	 * This is the key used to store voucher queue name
	 */
	private static final String AKS_VOUCHER_QUEUE_NAME = "AKS_VOUCHER";

	/**
	 * This is the key used to store used voucher queue name
	 */
	private static final String AKS_USED_VOUCHER_QUEUE_NAME = "AKS_USED_VOUCHER";
	
	/**
	 * This is the instance of the Logger
	 */
	Logger logger = Logger.getInstance(RedisVoucher.class);

	/**
	 * This is the configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This sets the configuration Manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is an instance of the queue job, to save the session back on to the
	 * database async
	 */
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

	BoilerplateList<String> subjectsForVoucherAlertToAdmin = new BoilerplateList();

	/**
	 * Initializes the bean
	 */
	public void initialize() {
		subjectsForVoucherAlertToAdmin.add("VoucherAlert");
	}

	/**
	 * @see IExperian.create
	 */
	@Override
	public void create(BoilerplateList<Voucher> vouchers) {
		List<Voucher> vouchersList = vouchers;
		for (Voucher voucher : vouchersList) {
			super.insert(AKS_VOUCHER_QUEUE_NAME, voucher);
		}
	}

	/**
	 * @see IExperian.getVoucherCode
	 */
	@Override
	public Voucher getVoucherCode(String userId, String sessionId) throws NotFoundException {
		if (super.getQueueSize(AKS_VOUCHER_QUEUE_NAME)
				% Integer.parseInt(configurationManager.get("Voucher_Count_Alert_Frequency")) == 0) {
			// This method will send voucher alert sms and email to the admin
			// this.sendVoucherAlertToAdmin();
		}
		Voucher voucher = super.remove(AKS_VOUCHER_QUEUE_NAME, Voucher.class);
		if (voucher == null) {
			logger.logWarning("RedisVoucher", "getVoucherCode", "Fetching Voucher from DB", "Voucher Not available");
			throw new NotFoundException("Voucher", "Voucher Not available", null);
		}
		voucher.setAssignedTo(userId);
		voucher.setSessionId(sessionId);
		super.insert(AKS_USED_VOUCHER_QUEUE_NAME, voucher);
		return voucher;
	}

	/**
	 * @see IExperian.logToExperianDatabase
	 */
	@Override
	public void logToExperianDatabase(String userId, String purpose, String httpContent) {
		logger.logInfo("RedisVoucher", "logToExperianDatabase", "Log", Base.toJSON(httpContent));
	}


}
