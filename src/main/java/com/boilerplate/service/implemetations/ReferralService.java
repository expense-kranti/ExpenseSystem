package com.boilerplate.service.implemetations;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.SendEmailToReferredUserObserver;
import com.boilerplate.asyncWork.SendSmsToReferredUserObserver;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.UserReferalMediumType;
import com.boilerplate.service.interfaces.IReferralService;

public class ReferralService implements IReferralService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(ReferralService.class);
	
	/**
	 * This is a new instance of Referral
	 */
	IReferral referral;

	/**
	 * This method is used to set the referral
	 * 
	 * @param referral
	 *            the referral to set
	 */
	public void setReferral(IReferral referral) {
		this.referral = referral;
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
	
	/**
	 * This is the generic publish subject list that can be sms or email subject list
	 * @author kranti123
	 */
	BoilerplateList<String> subjectsForInvitingReferredUser = new BoilerplateList<>();
	
	
	/**
	 * This is the instance of SendSmsToReferredUserObserver
	 */
	@Autowired
	SendSmsToReferredUserObserver sendSmsToReferredUserObserver;
	
	
	/**
	 * This sets the SendSmsToReferredUserObserver
	 * @param sendSmsToReferredUserObserver
	 */
	public void setSendSmsToReferredUserObserver(SendSmsToReferredUserObserver sendSmsToReferredUserObserver) {
		this.sendSmsToReferredUserObserver = sendSmsToReferredUserObserver;
	}
	
	/**
	 * This is the instance of SendEmailToReferredUserObserver
	 */
	@Autowired
	SendEmailToReferredUserObserver sendEmailToReferredUserObserver;
	
	/**
	 * This is the instance of SendEmailToReferredUserObserver
	 */
	public void setSendEmailToReferredUserObserver(SendEmailToReferredUserObserver sendEmailToReferredUserObserver) {
		this.sendEmailToReferredUserObserver = sendEmailToReferredUserObserver;
	}

	/**
	 * This is the publish subject list.
	 */
	BoilerplateList<String> subjectsForSendSMS = new BoilerplateList<>();
	BoilerplateList<String> subjectsForSendEmail = new BoilerplateList<>();
	
	/**
	 * Initializes the bean
	 */
	public void initialize() {
		subjectsForSendSMS.add("SendSMSToReferredUser");
		subjectsForSendEmail.add("SendEmailToReferredUser");
		
	}

	/**
	 * @see IReferralService.getUserReferredContacts
	 */
	@Override
	public ReferalEntity getUserReferredContacts() {
		return referral.getUserReferredContacts();
	}

	/**
	 * @see IReferralService.sendReferralLink
	 */
	@Override
	public void sendReferralLink(ReferalEntity referalEntity) {
		referral.setUserReferralContacts(referalEntity);
		this.sendSmsOrEmail(referalEntity);
	}
	
	
	/**
	 * This method notifies referred user according to user referral medium
	 * @author kranti123
	 * @param referalEntity The ReferalEntity
	 */
	public void sendSmsOrEmail(ReferalEntity referalEntity){
		UserReferalMediumType userReferralMediumType = referalEntity.getReferralMediumType();
		
		ExternalFacingUser currentUser = RequestThreadLocal.getSession().getExternalFacingUser();
		String currentUserName = currentUser.getFirstName() + " " +currentUser.getMiddleName() + " " +currentUser.getLastName();
		
		    switch (userReferralMediumType) {
		      case Email:
		    	  try{
					    queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForSendEmail, "ReferalEntity", "sendSmsOrEmail");
		    	  }
		    	  catch(Exception ex){
		    		    //if queue is not working we send email on the thread
					    this.sendEmailToReferredUsersWithoutUsingQueue(referalEntity);
					    logger.logException("referralService", "sendSmsOrEmail", "try-Queue Reader",
								ex.toString() + " ReferalEntity inserting in queue is: " + Base.toJSON(referalEntity)
										+ " Queue Down",
								ex);
		    	  }
                 break;
			
              case Phone_Number:
            	  try{
     		            queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForSendSMS, "ReferalEntity", "sendSmsOrEmail");
            	  }catch(Exception ex){
		    		    //if queue is not working we send sms on the thread
          			   this.sendSmsToReferredUsersWithoutUsingQueue(referalEntity);
          			 logger.logException("referralService", "sendSmsOrEmail", "try-Queue Reader",
     						ex.toString() + " ReferalEntity inserting in queue is: " + Base.toJSON(referalEntity)
     								+ " Queue Down",
     						ex);
            	  }
		         break;	
			
              case Facebook:
		         break;
		    }
					
		
	}
	

	
	/**
	 * This method sends sms to referred users without using background queue job
	 * @param referalEntity the ReferalEntity
	 */
    public void sendSmsToReferredUsersWithoutUsingQueue(ReferalEntity referalEntity){
    	//way of getting user name needs to be changed according to shiva
    	ExternalFacingUser currentUser = RequestThreadLocal.getSession().getExternalFacingUser();
		String currentUserName = currentUser.getFirstName() + " " +currentUser.getMiddleName() + " " +currentUser.getLastName();
		
    	BoilerplateMap<String, String> phoneNumberReferrals = referalEntity.getPhoneNumberReferrals();
		for(Map.Entry<String, String> phoneNumberMap : phoneNumberReferrals.entrySet()){
			String phoneNumber = phoneNumberMap.getValue();	
			try{
            sendSmsToReferredUserObserver.sendSMS(currentUserName, phoneNumber);
			}catch (Exception exSendPassword) {
				// if an exception takes place here we cant do much hence just
				// log it and move
				// forward
				logger.logException("ReferralService", "sendSmsToReferredUsersWithoutUsingQueue", "inside catch: try-Queue Reader",
						exSendPassword.toString() + "" + "Gateway down", exSendPassword);
			}
			
			
		}
    }
    
    /**
     * This method sends email to referred users without using background queue job
     * @param referalEntity The ReferalEntity
     */
    public void sendEmailToReferredUsersWithoutUsingQueue(ReferalEntity referalEntity){
    	
    	//way of getting user name needs to be changed according to shiva
    	ExternalFacingUser currentUser = RequestThreadLocal.getSession().getExternalFacingUser();
		String currentUserName = currentUser.getFirstName() + " " +currentUser.getMiddleName() + " " +currentUser.getLastName();
		
    	BoilerplateList<String> tosEmailList = new BoilerplateList<>(); 
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
		
		for(Map.Entry<String, String> emailReferralMap : referalEntity.getEmailReferrals().entrySet()){
			tosEmailList.add(emailReferralMap.getValue());
			try{
			 sendEmailToReferredUserObserver.sendEmail(currentUserName, tosEmailList, ccsEmailList, bccsEmailList, currentUser.getPhoneNumber(), currentUser.getUserKey());
			}
			catch(Exception exEmail){
				// if an exception takes place here we cant do much hence just
				// log it and move
				// forward
				logger.logException("ReferralService", "sendEmailToReferredUsersWithoutUsingQueue", "try-Queue Reader - Send Email", exEmail.toString(),
						exEmail);
			}
			tosEmailList.remove(0);
            
		}
    }

}
