package com.boilerplate.service.implemetations;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IExperian;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.Role;
import com.boilerplate.java.entities.Voucher;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.service.interfaces.IVoucherService;
import com.opencsv.CSVReader;

public class VoucherService implements IVoucherService {

	@Autowired
	com.boilerplate.service.implemetations.ExperianBureauService experianBureauService;

	public void setExperianBureauService(
			com.boilerplate.service.implemetations.ExperianBureauService experianBureauService) {
		this.experianBureauService = experianBureauService;
	}

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(VoucherService.class);

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
	 * This is the user service
	 */
	@Autowired
	IUserService userService;

	/**
	 * This method sets the user service
	 * 
	 * @param userService
	 *            The user service
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * This is the instance of IExperian
	 */
	@Autowired
	IExperian experianDataAccess;

	/**
	 * This method sets the instance of IExperian
	 * 
	 * @param experianDataAccess
	 *            The experianDataAccess
	 */
	public void setExperianDataAccess(IExperian experianDataAccess) {
		this.experianDataAccess = experianDataAccess;
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
	 * @throws IOException
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws ParseException
	 * @throws UnauthorizedException
	 * @see IVoucherService.voucherUploadFromCSV
	 */
	@Override
	public void voucherUploadFromCSV(String fileId)
			throws IOException, NotFoundException, BadRequestException, ParseException, UnauthorizedException {
		// checks for user role if not admin then throw unauthorized exception
		if (!checkIsAdmin()) {
			throw new UnauthorizedException("User", "Logged in User is not allowed to upload vouchers from CSV", null);
		}
		String csvFileName = null;
		String[] row = null;
		File file;
		// Get file from local if not found then downloads
		if (!(new File(configurationManager.get("RootFileDownloadLocation"), fileId)).exists()) {
			csvFileName = this.file.downloadFileFromS3ToLocal(configurationManager.get("S3_Files_Path") + fileId);
		} else {
			csvFileName = fileId;
		}
		// Reading CSv File and setting vouchersList from CSV Data
		CSVReader csvReader = new CSVReader(
				new FileReader(configurationManager.get("RootFileDownloadLocation") + csvFileName));
		BoilerplateList<Voucher> vouchersList = new BoilerplateList<>();
		Voucher voucherEntity = null;
		String[] headerLine = csvReader.readNext();
		while ((row = csvReader.readNext()) != null) {
			voucherEntity = new Voucher();
			voucherEntity.setVoucherCode(row[0]);
			voucherEntity.setExpiryDate(new SimpleDateFormat("yyyy-MM-dd").parse(row[1]));
			vouchersList.add(voucherEntity);
		}
		experianDataAccess.create(vouchersList);
	}

	/**
	 * This method checks the roles of the user and tells about whether that
	 * user have managerial role or not
	 * 
	 * @return isAdmin The isAdmin returns true/false
	 * @throws NotFoundException
	 *             The NotFoundException
	 * @throws BadRequestException
	 *             The BadRequestException
	 */
	private boolean checkIsAdmin() throws NotFoundException, BadRequestException {
		boolean isAdmin = false;
		if (RequestThreadLocal.getSession() != null) {
			ExternalFacingReturnedUser user = userService
					.get(RequestThreadLocal.getSession().getExternalFacingUser().getUserId(), false);
			// check user state exists in input states
			for (Role role : user.getRoles()) {
				if (role.getRoleName().toUpperCase().equals("ADMIN")
						|| role.getRoleName().toUpperCase().equals("BACKOFFICEUSER")
						|| role.getRoleName().toUpperCase().equals("BANKADMIN")
						|| role.getRoleName().toUpperCase().equals("BANKUSER")) {
					isAdmin = true;
					break;
				}
			}
		}
		return isAdmin;
	}

}
