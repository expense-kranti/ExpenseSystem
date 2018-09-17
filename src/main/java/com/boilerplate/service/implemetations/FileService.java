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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.database.interfaces.IFile;
import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseHistoryEntity;
import com.boilerplate.java.entities.FileMappingEntity;
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
	 *            to set
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is the file service provider on data layer
	 */
	@Autowired
	IFile file;

	/**
	 * Sets the file provider
	 * 
	 * @param file
	 *            The file provider
	 */
	public void setFile(IFile file) {
		this.file = file;
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
	 * @see IFileService.saveFileOnLocal
	 */
	@Override
	public FileMappingEntity saveFileOnLocal(String fileMasterTag, MultipartFile file)
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
		FileMappingEntity fileMappingEntity = new FileMappingEntity(fileNameOnDisk,
				RequestThreadLocal.getSession().getExternalFacingUser().getId(), null, true, null,
				file.getOriginalFilename(), contentType);
		return fileMappingEntity;
	}

	/**
	 * @see IFileService.saveFileMapping
	 */
	@Override
	public boolean saveFileMapping(ExpenseEntity expenseEntity) throws Exception {
		List<FileMappingEntity> fileMappings = new ArrayList<>();
		// for each attachment in expense enitity
		for (FileMappingEntity eachMapping : expenseEntity.getFileMappings()) {
			// create a new file mapping entity
			FileMappingEntity fileMappingEntity = new FileMappingEntity(eachMapping.getAttachmentId(),
					expenseEntity.getUserId(), expenseEntity.getId(), true, null, eachMapping.getOriginalFileName(),
					eachMapping.getContentType());
			fileMappings.add(fileMappingEntity);
		}
		try {
			// save mapping in MySQL
			filePointer.saveFileMapping(fileMappings);
		} catch (Exception ex) {
			return false;
		}
		// return true, if all file mapping were saved succesfully
		return true;

	}

	/**
	 * @see IFileService.updateFileMapping
	 */
	@Override
	public List<FileMappingEntity> updateFileMapping(ExpenseEntity expenseEntity,
			ExpenseHistoryEntity expenseHistoryEntity) throws Exception {
		List<FileMappingEntity> newMappings = new ArrayList<>();
		List<FileMappingEntity> allMappings = expenseEntity.getFileMappings();
		// for each attachment in expense entity
		for (FileMappingEntity eachMapping : allMappings) {
			// check if this attachment already exists for the given user and
			// expense
			if (filePointer.getFileMapping(eachMapping.getAttachmentId()) == null)
				// add it in new attachment list
				newMappings.add(eachMapping);
		}
		// set new attachments in expense entity and save it
		expenseEntity.setFileMappings(newMappings);
		saveFileMapping(expenseEntity);
		// fetch all attachments for given expense and user id
		List<FileMappingEntity> fileMappings = filePointer.getFileMappingByExpenseId(expenseEntity.getId());
		// if file mappings found
		if (fileMappings.size() != 0 && newMappings.size() != 0) {
			// for each file mapping
			for (FileMappingEntity fileMappingEntity : fileMappings) {
				for (FileMappingEntity newMapping : newMappings) {
					// if new attachment list does not contain the attachment id
					if (!newMapping.getAttachmentId().equals(fileMappingEntity.getAttachmentId())) {
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
		return allMappings;
	}

	/**
	 * @see IFileService.downloadFile
	 */
	@Override
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) throws BadRequestException, NotFoundException {
		// Fetch the file mapping for the given file
		FileMappingEntity fileMappingEntity = filePointer.getFileMapping(fileName);
		// check if file mapping exists for the given file
		if (fileMappingEntity == null)
			throw new NotFoundException("FileMappingEntity", "File mapping not found for the given file name", null);
		// get the root file saving location
		String fileLocation = configurationManager.get("DESTINATION_FOR_SAVING_FILE_ON_DISK");
		Path file = Paths.get(fileLocation, fileName);
		if (Files.exists(file)) {
			response.addHeader("Content-Disposition",
					"attachment; filename=" + fileMappingEntity.getOriginalFileName());
			response.setContentType(fileMappingEntity.getContentType());
			try {
				Files.copy(file, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @see IFileService.checkFileExistence
	 */
	@Override
	public boolean checkFileExistence(List<FileMappingEntity> mappings) {
		// for each file mapping check if files have been uploaded
		for (FileMappingEntity fileMappingEntity : mappings) {
			// get the file location
			File file = new File(configurationManager.get("DESTINATION_FOR_SAVING_FILE_ON_DISK") + "/"
					+ fileMappingEntity.getAttachmentId());
			// check if file exists
			if (!file.exists())
				// return false if file doesn't exist
				return false;
		}
		// if all files exist, return true
		return true;
	}
}
