package com.boilerplate.databases.s3FileSystem.implementations;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.boilerplate.database.interfaces.IFile;
import com.boilerplate.framework.Logger;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * This is the class for file saving on to AWS S3 and downloading by sftp
 * @author mohit
 *
 */
public class S3File implements IFile {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(S3File.class);
	
	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * @param configurationManager
	 */
	public void setConfigurationManager(
			com.boilerplate.configurations.ConfigurationManager 
			configurationManager){
		this.configurationManager = configurationManager;
	}
	

	/**
	 * Saves the file to disk with the new generated name and then uploads the same to S3
	 */
	@Override
	public String saveFile(MultipartFile file) throws IOException {
		String fileNameOnDisk = UUID.randomUUID().toString()+"_"+(file.getOriginalFilename()).replace(".", "");
		String rootFileUploadLocation = configurationManager.get("RootFileUploadLocation");
		String filePath = rootFileUploadLocation + fileNameOnDisk;
		file.transferTo(new java.io.File(filePath));
		//Getting S3 Client
		AmazonS3 s3Client = this.getS3Client();
		try{
            File fileToUpload = new File(filePath);
            //bytesFileToUpload will be the placeholder of file bytes
            byte[] bytesFileToUpload = new byte[(int) fileToUpload.length()];
            FileInputStream fileInputStream=null;
            //convert file into array of bytes
            fileInputStream = new FileInputStream(fileToUpload);
            fileInputStream.read(bytesFileToUpload);
            fileInputStream.close();
            // ObjectMetadata instance use to set meta data for file byte array input stream.
            ObjectMetadata objectMetadata = new ObjectMetadata();
        	objectMetadata.setContentLength(file.getSize());
        	objectMetadata.setContentType(java.nio.file.Files.probeContentType(Paths.get(configurationManager.get("RootFileUploadLocation")+fileNameOnDisk)));
            // file byte array input stream.
        	InputStream myInputStream = new ByteArrayInputStream(bytesFileToUpload);
        	// Putting file stream along with its meta data information to AWS S3.
        	s3Client.putObject((new PutObjectRequest(configurationManager.get("S3_Bucket_Name"), fileNameOnDisk, myInputStream, objectMetadata)));
        }
		catch(AmazonServiceException amazonServiceException){
			logger.logException("S3File", "saveFile",
					amazonServiceException.getErrorCode() + "inside try block and its description is:" + amazonServiceException.getErrorMessage(),
					"Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.",
					amazonServiceException);
			throw amazonServiceException;
		}
		catch(AmazonClientException amazonClientException){
			logger.logException("S3File", "saveFile",
					"inside try block" + amazonClientException.getMessage(),
    				"Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.",
    				amazonClientException);
			throw amazonClientException;
        }
		return fileNameOnDisk;
	}
	
	
	
	/**
	 * This method make AWS Credentials by using AWS Access Key 
	 * and AWS Secret Access Key from Configuration file
	 * @return awsCredentials The AWSCredentials
	 */
	private AmazonS3 getS3Client(){
		AWSCredentials awsCredentials = new BasicAWSCredentials(
				configurationManager.get("Access_Key"),
						configurationManager.get("Secret_Access_Key")
    			);	
		return new AmazonS3Client(awsCredentials);
	}
	
	/**
	 * This method takes http Url string as a paramater and then download file from
	 * this url to our local 
	 * @param urlString The http url
	 * @return fileName The file name
	 * @throws IOException
	 */
	public String downloadFileFromS3ToLocal(String id) throws IOException{
		InputStream inputStream = null;
        String contentType = null;
        FileOutputStream outputStream = null;
        //replace unwanted whitespac
        // Convert string to url
    	URL url = new URL(this.getPreSignedS3URL(id));
    	// Make http connection with the url
    	HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
    	// get response code of http url connection
    	int responseCode = httpUrlConnection.getResponseCode();
    	try {
        	// always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                contentType = httpUrlConnection.getContentType();
                // get inputstream from connection
                inputStream = httpUrlConnection.getInputStream();
             // opens an output stream to save into file
                outputStream = new FileOutputStream(configurationManager.get("RootFileDownloadLocation")+id);
     
                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            else{
            	logger.logInfo("S3File", "downloadFileFromS3ToLocal", "Inside try block while getting response on http url connection", "No file to download. Server replied HTTP code: " + responseCode);
            }
    	}
        finally {
        	if (inputStream != null) {
            	inputStream.close();
            }
        	if (outputStream != null) {
        		outputStream.close();
            }
        	httpUrlConnection.disconnect();
        }
    	return id;
	}


	@Override
	public String getPreSignedS3URL(String id) {
		AmazonS3 s3Client = this.getS3Client();
		URL url;
		try {
			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			milliSeconds += 1000 * 60 * Integer.parseInt(configurationManager.get("S3_Signed_Url_Time_In_Hour"))*60; // Add  hour.
			expiration.setTime(milliSeconds);

			GeneratePresignedUrlRequest generatePresignedUrlRequest = 
				    new GeneratePresignedUrlRequest(configurationManager.get("S3_Bucket_Name"),
				    		id);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET); 
			generatePresignedUrlRequest.setExpiration(expiration);

			url = s3Client.generatePresignedUrl(generatePresignedUrlRequest); 

		} catch(AmazonServiceException amazonServiceException){
			logger.logException("S3File", "get pre signed url",
					amazonServiceException.getErrorCode() + "inside try block and its description is:" + amazonServiceException.getErrorMessage(),
					"Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.",
					amazonServiceException);
			throw amazonServiceException;
		}
		catch(AmazonClientException amazonClientException){
			logger.logException("S3File", "getPreSignedS3URL",
					"inside try block" + amazonClientException.getMessage(),
    				"Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.",
    				amazonClientException);
			throw amazonClientException;
        }
		return url.toString();
	}


}
