package com.boilerplate.service.implemetations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseHistoryEntity;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.FileDetailsEntity;
import com.boilerplate.java.entities.FileMappingEntity;
import com.boilerplate.java.entities.UserRoleType;
import com.boilerplate.service.interfaces.IFileService;

/**
 * this class implements IFileService
 * 
 * @author ruchi
 *
 */
public class FileService implements IFileService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(FileService.class);
	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is the instance of filePointer
	 */
	@Autowired
	IFilePointer filePointer;

	/**
	 * Sets the file pointer
	 * 
	 * @param filePointer
	 *            to set
	 */
	public void setFilePointer(IFilePointer filePointer) {
		this.filePointer = filePointer;
	}

	/**
	 * This is the instance of IUser
	 */
	IUser mySqlUser;

	/**
	 * This method set the mysqluser
	 * 
	 * @param mySqlUser
	 *            the mySqlUser to set
	 */
	public void setMySqlUser(IUser mySqlUser) {
		this.mySqlUser = mySqlUser;
	}

	/**
	 * @see IFileService.saveFileOnLocal
	 */
	@Override
	public FileDetailsEntity saveFileOnLocal(String fileMasterTag, MultipartFile file)
			throws UpdateFailedException, Exception {
		// get content type
		String contentType = file.getContentType();
		if (contentType == null)
			throw new ValidationFailedException("AttachmentEntity", "exceptionSaveFileOnLocal", null);
		// generate unique file name for saving on local disk
		String fileNameOnDisk = UUID.randomUUID().toString();
		fileNameOnDisk = fileNameOnDisk.replace(".", "_");
		// get path for saving file from configuration
		String rootFileUploadLocation = configurationManager.get("DESTINATION_FOR_SAVING_FILE_ON_DISK");
		// generate path with file name
		String filePath = rootFileUploadLocation + "/" + fileNameOnDisk;
		// save file name onto disk
		file.transferTo(new java.io.File(filePath));
		// create new attachment entity
		FileDetailsEntity fileDetailsEntity = new FileDetailsEntity(file.getOriginalFilename(), fileNameOnDisk,
				RequestThreadLocal.getSession().getExternalFacingUser().getId(), contentType);
		// save file details in MySQL
		filePointer.saveFileDetails(fileDetailsEntity);
		return fileDetailsEntity;
	}

	/**
	 * @see IFileService.saveFileMapping
	 */
	@Override
	public boolean saveFileMapping(ExpenseEntity expenseEntity) throws Exception {
		List<FileMappingEntity> fileMappings = new ArrayList<>();
		// for each attachment in expense entity
		for (String attachmentId : expenseEntity.getAttachmentIds()) {
			// fetch file details from database for the given attachment id
			// existence of attachment in database has already been checked
			FileDetailsEntity fileDetailsEntity = filePointer.getFileDetailsByAttachmentId(attachmentId);
			// create a new file mapping entity
			FileMappingEntity fileMappingEntity = new FileMappingEntity(fileDetailsEntity.getId(),
					RequestThreadLocal.getSession().getExternalFacingUser().getId(), expenseEntity.getId(), true, null,
					fileDetailsEntity.getAttachmentId());
			fileMappings.add(fileMappingEntity);
		}
		try {
			// save mapping in MySQL
			filePointer.saveFileMapping(fileMappings);
		} catch (Exception ex) {
			return false;
		}
		// return true, if all file mapping were saved successfully
		return true;

	}

	/**
	 * @see IFileService.updateFileMapping
	 */
	@Override
	public void updateFileMapping(ExpenseEntity expenseEntity, ExpenseHistoryEntity expenseHistoryEntity)
			throws Exception {
		List<String> newAttachments = new ArrayList<>();
		List<String> allAttachments = expenseEntity.getAttachmentIds();
		// for each attachment in expense entity
		for (String eachAttachmentId : allAttachments)
			// check if this attachment already exists for the given user and
			// expense
			if (filePointer.getFileMapping(filePointer.getFileDetailsByAttachmentId(eachAttachmentId).getId()) == null)
				// add it in new attachment list
				newAttachments.add(eachAttachmentId);
		// set new attachments in expense entity and save it
		if (newAttachments.size() != 0) {
			expenseEntity.setAttachmentIds(newAttachments);
			saveFileMapping(expenseEntity);
		}
		// fetch all attachments for given expense and user id
		List<FileMappingEntity> fileMappings = filePointer.getFileMappingByExpenseId(expenseEntity.getId());
		// if file mappings found
		if (fileMappings.size() != 0) {
			// get all file ids from file mappings
			List<String> fileIdsFromMapping = new ArrayList<>();
			List<String> fileIdsFromAttachments = new ArrayList<>();
			for (FileMappingEntity fileMapping : fileMappings)
				fileIdsFromMapping.add(fileMapping.getFileId());
			for (String attachmentId : allAttachments) {
				// fetch file details for this attachment id
				FileDetailsEntity fileDetailsEntity = filePointer.getFileDetailsByAttachmentId(attachmentId);
				fileIdsFromAttachments.add(fileDetailsEntity.getId());
			}
			// for each file mapping
			for (String fileIdFromMapping : fileIdsFromMapping) {
				if (!fileIdsFromAttachments.contains(fileIdFromMapping)) {
					FileMappingEntity fileMappingEntity = filePointer.getFileMapping(fileIdFromMapping);
					// set is active to false
					fileMappingEntity.setIsActive(false);
					// set expense history id
					fileMappingEntity.setExpenseHistoryId(expenseHistoryEntity.getId());
					// update file mapping
					filePointer.updateFileMapping(fileMappingEntity);
				}
			}
		}
	}

	/**
	 * @see IFileService.downloadFile
	 */
	@Override
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName)
			throws BadRequestException, NotFoundException, UnauthorizedException, IOException {
		// Fetch the file details for the given file
		FileDetailsEntity fileDetailsEntity = filePointer.getFileDetailsByAttachmentId(fileName);
		// check if fileDetailsEntity exists for the given file
		if (fileDetailsEntity == null)
			throw new NotFoundException("FileDetailsEntity", "File not found for the given file name", null);
		// fetch the currently logged in user
		ExternalFacingUser currentUser = RequestThreadLocal.getSession().getExternalFacingUser();
		// check if current user is finance/super-approver
		if (!currentUser.getRoleTypes().contains(UserRoleType.SUPER_APPROVER)
				&& !currentUser.getRoleTypes().contains(UserRoleType.FINANCE)) {
			// fetch the file owner
			ExternalFacingUser fileOwner = mySqlUser.getUser(fileDetailsEntity.getUserId());
			// check if current user is approver for the file owner
			if (fileOwner.getApproverId() != null && !fileOwner.getApproverId().equals(currentUser.getId())
					&& !fileOwner.getId().equals(currentUser.getId())) {
				// return error code
				response.sendError(401, "User is not authorized to download this file");
				return;
			}
		}
		// get the root file saving location
		String fileLocation = configurationManager.get("DESTINATION_FOR_SAVING_FILE_ON_DISK");
		Path file = Paths.get(fileLocation, fileName);
		if (Files.exists(file)) {
			response.addHeader("Content-Disposition",
					"attachment; filename=" + fileDetailsEntity.getOriginalFileName());
			response.setContentType(fileDetailsEntity.getContentType());
			try {
				Files.copy(file, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				ex.printStackTrace();
				response.sendError(500, "Some exception occurred while downloading file");
			}
		}
	}

	/**
	 * @see IFileService.checkFileExistence
	 */
	@Override
	public void checkFileExistence(List<String> attachmentIds) throws NotFoundException, BadRequestException {
		// for each attachment check if files have been uploaded
		for (String attachmentId : attachmentIds) {
			// get the file location
			File file = new File(configurationManager.get("DESTINATION_FOR_SAVING_FILE_ON_DISK") + "/" + attachmentId);
			// check if file exists in system
			if (!file.exists())
				// throw exception
				throw new NotFoundException("File", "File not saved in system", null);
			// fetch the file details entity
			FileDetailsEntity detailsEntity = filePointer.getFileDetailsByAttachmentId(attachmentId);
			// check if entry exists in file details for attachment id
			if (detailsEntity == null)
				throw new NotFoundException("File",
						"No record found for the attachment id :" + attachmentId + " in database", null);
			// check if file uploaded is being used for creating expense by same
			// user
			if (!detailsEntity.getUserId().equals(RequestThreadLocal.getSession().getExternalFacingUser().getId()))
				throw new BadRequestException("File",
						"This attachment id :" + attachmentId
								+ " does not belong to the current user, hence cannot be used by him for creating an expense",
						null);
		}
	}
}
