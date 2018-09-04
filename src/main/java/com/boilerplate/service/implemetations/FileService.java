package com.boilerplate.service.implemetations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.database.interfaces.IFile;
import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.AttachmentEntity;
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
	public AttachmentEntity saveFileOnLocal(String fileMasterTag, MultipartFile file)
			throws UpdateFailedException, Exception {
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
		AttachmentEntity attachmentEntity = new AttachmentEntity(fileMasterTag, fileNameOnDisk);
		return attachmentEntity;
	}

	/**
	 * @see IFileService.saveFileMapping
	 */
	@Override
	public void saveFileMapping(ExpenseEntity expenseEntity) throws Exception {
		// for each attachment in expense enitity
		for (AttachmentEntity attachment : expenseEntity.getAttachments()) {
			// create a new file mapping entity
			FileMappingEntity fileMappingEntity = new FileMappingEntity(attachment.getAttachmentId(),
					expenseEntity.getUserId(), expenseEntity.getId(), true, null, attachment.getOriginalFileName());
			// save mapping in MySQL
			filePointer.saveFileMapping(fileMappingEntity);
		}
	}

	/**
	 * @see IFileService.updateFileMapping
	 */
	@Override
	public List<AttachmentEntity> updateFileMapping(ExpenseEntity expenseEntity,
			ExpenseHistoryEntity expenseHistoryEntity) throws Exception {
		List<AttachmentEntity> newAttachments = new ArrayList<>();
		List<AttachmentEntity> allAttachments = expenseEntity.getAttachments();
		// for each attachment in expense entity
		for (AttachmentEntity attachment : allAttachments) {
			// check if this attachment already exists for the given user and
			// expense
			if (filePointer.getFileMapping(attachment.getAttachmentId()) == null)
				// add it in new attachment list
				newAttachments.add(attachment);
		}
		// set new attachments in expense entity and save it
		expenseEntity.setAttachments(newAttachments);
		saveFileMapping(expenseEntity);
		// fetch all attachments for given expense and user id
		List<FileMappingEntity> fileMappings = filePointer.getFileMappingByExpenseId(expenseEntity.getId());
		// if file mappings found
		if (fileMappings.size() != 0 && newAttachments.size() != 0) {
			// for each file mapping
			for (FileMappingEntity fileMappingEntity : fileMappings) {
				// if new attachment list does not contain the attachment id
				if (!newAttachments.contains(fileMappingEntity.getAttachmentId())) {
					// set is active to false
					fileMappingEntity.setIsActive(false);
					// set expense history id
					fileMappingEntity.setExpenseHistoryId(expenseHistoryEntity.getId());
					// update file mapping
					filePointer.updateFileMapping(fileMappingEntity);
				}
			}
		}
		return allAttachments;
	}

	@Override
	public void getFile(HttpServletResponse response) throws MalformedURLException, IOException {
		// get path for getting file from configuration
		String rootFileSavedLocation = configurationManager.get("DESTINATION_FOR_SAVING_FILE_ON_DISK");
		// generate path with file name
		String filePath = rootFileSavedLocation + "/"
				+ "52596d96-e4dd-435c-b022-383d4bdbeb2f_Dump20180827_sql_finance1@krantitech_com";

		File file = new File(filePath);
		InputStream in = new FileInputStream(file);
		response.setContentType("pdf");
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		response.setHeader("Content-Length", String.valueOf(file.length()));
		FileCopyUtils.copy(in, response.getOutputStream());

	}
}
