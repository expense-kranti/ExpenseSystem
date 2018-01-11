package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.service.interfaces.IReferralService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on user referral.
 * 
 * @author shiva
 *
 */
@Api(description = "This controller has api for operate referral", value = "Referral API's", basePath = "/referral")
@Controller
public class ReferralController extends BaseController {

	/**
	 * This is a new instance of referral service
	 */
	@Autowired
	IReferralService referralService;

	/**
	 * This API is used to get the contact referred by user in current date
	 * 
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             the contact referred by user
	 */
	@ApiOperation(value = "Get the contact referred by user in current date")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/userReferral", method = RequestMethod.GET)
	public @ResponseBody ReferalEntity getUserReferredContacts() throws Exception {
		// Get the all user articles
		return referralService.getUserReferredContacts();
	}

	/**
	 * This API is used to send referral link to those contact referred by user.
	 * 
	 * @param ReferalEntity
	 *            this parameter contains the details of referred contact by
	 *            user and the type of referred medium
	 * 
	 * @throws Exception
	 *             throw this exception in case of any error while trying to
	 *             send referral link to those contact referred by user
	 */
	@ApiOperation(value = "This api is used to send referral link to those contact referred by user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/userReferral", method = RequestMethod.POST)
	public @ResponseBody void sendReferralLink(@RequestBody ReferalEntity referalEntity) throws Exception {
		// Save the user article
		referralService.sendReferralLink(referalEntity);
	}

	/**
	 * This API is used to send referral link to those contact referred by user.
	 * 
	 * @param ReferalEntity
	 *            this parameter contains the details of referred contact by
	 *            user and the type of referred medium
	 * 
	 * @throws Exception
	 *             throw this exception in case of any error while trying to
	 *             send referral link to those contact referred by user
	 */
	@ApiOperation(value = "This api is used to send referral link to those contact referred by user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 409, message = "The user already exists in the system for the provider") })
	@RequestMapping(value = "/validateReferContact", method = RequestMethod.POST)
	public @ResponseBody void validateReferContact(@RequestBody ReferalEntity referalEntity)
			throws Exception, ConflictException {
		// Save the user article
		referralService.validateReferContact(referalEntity);
	}

	/**
	 * This API is used to get the contact referred by user in current date
	 * 
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             the contact referred by user
	 */
	@ApiOperation(value = "Get the contact referred by user in current date")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/userReferralLink", method = RequestMethod.GET)
	public @ResponseBody ReferalEntity getFaceBookReferralLink() throws Exception {
		// Get the all user articles
		return referralService.getFaceBookReferralLink();
	}

	/**
	 * This API is used to get the LinkedIn referral link for the user referred
	 * in current date
	 * 
	 * @return referralEntity that contains the referral link
	 */
	@ApiOperation(value = "Get the LinkedIn referral link for the referred user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/userLinkedInReferralLink", method = RequestMethod.GET)
	public @ResponseBody ReferalEntity getLinkedInReferralLink() throws Exception {
		return referralService.getLinkedInReferralLink();
	}

}
