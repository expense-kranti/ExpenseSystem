package com.boilerplate.database.redis.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.GenericListEncapsulationEntity;
import com.boilerplate.java.entities.MethodPermissions;
import com.boilerplate.java.entities.Role;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class BaseRedisDataAccessLayer {

	/**
	 * Redis database logger
	 */
	private static Logger logger = Logger.getInstance(BaseRedisDataAccessLayer.class);

	/**
	 * Creates a base DB layer
	 */
	public BaseRedisDataAccessLayer() {

	}

	/**
	 * The properties
	 */
	Properties properties = null;

	private static JedisPool pool = null;

	/**
	 * Gets a connection as a singleton
	 * 
	 * @return A Jedis command
	 */
	protected Jedis getConnection() {

		if (BaseRedisDataAccessLayer.pool != null) {
			return BaseRedisDataAccessLayer.pool.getResource();
		}
		// First check if this is just one local machine or a cluster

		// This is the connections to redis it is expected in format
		// host:port;host:port;...
		String redisConnections = this.getDatabaseConnectionFormPropertiesFile();

		String[] hostPort;
		String host;
		int port;
		// split the connections with ;
		String[] connections = redisConnections.split(";");
		// if there are more than one connections we have a cluster
		hostPort = connections[0].split(":");
		host = hostPort[0];
		port = Integer.parseInt(hostPort[1]);
		// and create a non clustered command

		BaseRedisDataAccessLayer.pool = new JedisPool(new JedisPoolConfig(), host, port);
		logger.logInfo("RedisDatabase", "getConnection", "Creating Single", host + ":" + port);

		createSeedData();
		return BaseRedisDataAccessLayer.pool.getResource();
	}

	// THIS IS A VERY BAD WAY TO DO THIS, need to find a better mechanism
	private void createSeedData() {
		BoilerplateMap<String, String> v1E1 = new BoilerplateMap<String, String>();
		BoilerplateMap<String, String> vAllEQA = new BoilerplateMap<String, String>();
		BoilerplateMap<String, String> v2EQA = new BoilerplateMap<String, String>();
		BoilerplateMap<String, String> vAllETest = new BoilerplateMap<String, String>();

		v1E1.put("V1_All", "V1_All");
		v1E1.put("V1_Dev", "V1_Dev");

		vAllEQA.put("V_All_QA", "V_All_QA");
		v2EQA.put("V_2_Dev", "V_2_Dev");
		v2EQA.put("V_2_QA", "V_2_QA");

		v1E1.put("V1_All_B", "V1_All_B");
		// load server specific configuration
		this.set("CONFIGURATION:V_ALL_E_TEST", Base.toXML(createSeedTestData()));

		this.set("CONFIGURATION:V_ALL_E_DEVELOPMENT", Base.toXML(createSeedDevelopmentData()));

		this.set("CONFIGURATION:V_ALL_E_PRODUCTION", Base.toXML(createSeedProductionData()));

		// load common configuration
		this.set("CONFIGURATION:V_ALL_E_ALL", Base.toXML(createSeedCommonData()));
		this.set("CONFIGURATION:V_1_E_1", Base.toXML(v1E1));

		this.set("CONFIGURATION:V_All_E_QA", Base.toXML(vAllEQA));

		// Storing seed content
		createSeedContent();
		// create seed user and role
		createSeedUserAndRole();
		// create method permission
		createMethodPermission();
	}

	/**
	 * Gets the connection string from the properties file
	 * 
	 * @return The connection string
	 */
	private String getDatabaseConnectionFormPropertiesFile() {
		InputStream inputStream = null;
		try {
			properties = new Properties();
			// Using the .properties file in the class path load the file
			// into the properties class
			inputStream = this.getClass().getClassLoader().getResourceAsStream("boilerplate.properties");
			properties.load(inputStream);
			return properties.getProperty("RedisDatabaseServer");
		} catch (IOException ioException) {
			// we do not generally expect an exception here
			// and because we are start of the code even before loggers
			// have been enabled if something goes wrong we will have to print
			// it to
			// console. We do not throw this exception because we do not expect
			// it
			// and if we do throw it then it would have to be handeled in all
			// code
			// making it bloated, it is hence a safe assumption this exception
			// ideally will not
			// happen unless the file access has issues
			System.out.println(ioException.toString());
		} finally {
			// close the input stream if it is not null
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception ex) {
					// if there is an issue closing it we just print it
					// and move forward as there is not a good way to inform
					// user.
					System.out.println(ex.toString());
				}
			}
		} // end finally
		return properties.getProperty("RedisDatabaseServer");
	}// end method

	/**
	 * Gets a key
	 * 
	 * @param key
	 *            The key
	 * @param typeOfClass
	 * @return
	 */
	public <T extends Base> T get(String key, Class<T> typeOfClass) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			Object o = jedis.get(key);
			if (o == null) {
				return null;
			}
			T t = Base.fromXML(o.toString(), typeOfClass);
			return t;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	/**
	 * Sets the value
	 * 
	 * @param key
	 *            The key
	 * @param value
	 *            The value
	 */
	public <T extends Base> void set(String key, T value) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.set(key, value.toXML());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	/**
	 * Sets the value
	 * 
	 * @param key
	 *            The key
	 * @param value
	 *            The value
	 */
	public <T extends Base> void set(String key, T value, int timeoutInSeconds) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.setex(key, timeoutInSeconds, value.toXML());
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	/**
	 * Deletes a key
	 * 
	 * @param key
	 *            The key
	 */
	public void delete(String key) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.del(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	/**
	 * Gets a key
	 * 
	 * @param key
	 *            The key
	 * @return
	 */
	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			return jedis.get(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

	}

	/**
	 * Sets the value
	 * 
	 * @param key
	 *            The key
	 * @param value
	 *            The value
	 */
	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.set(key, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * This Method insert the object at left head of Queue
	 * 
	 * @param queueName
	 *            The Queue name in which we want to insert object
	 * @param object
	 *            The object which we want to insert in to queue
	 */
	public void insert(String queueName, Object object) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.lpush(queueName, Base.toXML(object));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * This method remove last element of Queue
	 * 
	 * @param queueName
	 *            The queue name from which we want to remove the last element
	 * @param typeOfClass
	 *            The type of class in which we want to type cast
	 * @return t The last element remove from the queue
	 */
	public <T> T remove(String queueName, Class<T> typeOfClass) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			String elementRemove = jedis.rpop(queueName);
			if (elementRemove == null) {
				return null;
			}
			T t = Base.fromXML(elementRemove.toString(), typeOfClass);
			return t;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * This method remove last element of Queue
	 * 
	 * @param queueName
	 *            The queue name from which we want to remove the last element
	 * @return t The last element remove from the queue
	 */
	public String remove(String queueName) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			return jedis.rpop(queueName);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * This method get the queue size
	 * 
	 * @param queueName
	 *            The queue name from which we want to get the count
	 * @return the count of queue data present
	 */
	public Long getQueueSize(String queueName) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			return jedis.llen(queueName);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * This method creates method permission.
	 */
	private void createMethodPermission() {
		// This attribute tells us about server name
		String salesForceBaseurl = getSalesForceBaseUrl();
		// method permissions
		BoilerplateMap<String, MethodPermissions> methodPermissionMap = new BoilerplateMap<>();

		MethodPermissions methodPermission = null;

		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.Ping com.boilerplate.java.controllers.HealthController.ping()");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.Ping com.boilerplate.java.controllers.HealthController.ping()");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setUrlToPublish("http://localhost");
		methodPermission.setPublishMethod("POST");
		methodPermission.setPublishRequired(false);
		methodPermission.setDynamicPublishURl(false);
		methodPermission.setPublishBusinessSubject("CHECK_SERVER_STATUS");
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.sessions.Session com.boilerplate.java.controllers.UserController.authenticate(com.boilerplate.java.entities.AuthenticationRequest)");
		methodPermission.setMethodName(
				"public com.boilerplate.sessions.Session com.boilerplate.java.controllers.UserController.authenticate(com.boilerplate.java.entities.AuthenticationRequest)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setUrlToPublish("http://localhost");
		methodPermission.setPublishMethod("POST");
		methodPermission.setPublishRequired(false);
		methodPermission.setDynamicPublishURl(false);
		methodPermission.setPublishBusinessSubject("USER_LOGIN");

		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ExternalFacingReturnedUser com.boilerplate.java.controllers.UserController.getCurrentUser()");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ExternalFacingReturnedUser com.boilerplate.java.controllers.UserController.getCurrentUser()");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setUrlToPublish("http://localhost");
		methodPermission.setPublishMethod("POST");
		methodPermission.setPublishRequired(false);
		methodPermission.setDynamicPublishURl(false);
		methodPermission.setPublishBusinessSubject("GET_CURRENTLY_LOGGED_IN_USER");
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ExternalFacingUser com.boilerplate.java.controllers.UserController.createUser(com.boilerplate.java.entities.ExternalFacingUser)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ExternalFacingUser com.boilerplate.java.controllers.UserController.createUser(com.boilerplate.java.entities.ExternalFacingUser)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setPublishMethod("POST");
		methodPermission.setPublishRequired(true);
		methodPermission.setUrlToPublish(salesForceBaseurl + "/services/apexrest/Account");
		methodPermission.setDynamicPublishURl(false);
		methodPermission.setPublishTemplate(
				"{\"id\": \"@Id\",\"userId\": \"@userId\",\"authenticationProvider\": \"@authenticationProvider\",\"email\": \"@email\",\"firstName\": \"@firstName\",\"lastName\": \"@lastName\",\"middleName\": \"@middleName\",\"phoneNumber\": \"@phoneNumber\",\"ownerId\": \"@ownerId\",\"referalSource\": \"@referalSource\",\"campaignType\": \"@campaignType\",\"campaignSource\": \"@campaignSource\",\"campaignUUID\": \"@campaignUUID\"}");
		methodPermission.setPublishBusinessSubject("CREATE_USER_AKS");
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for assessment attempt
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.AssessmentEntity com.boilerplate.java.controllers.AssessmentController.attemptAssessment(com.boilerplate.java.entities.AssessmentEntity)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.AssessmentEntity com.boilerplate.java.controllers.AssessmentController.attemptAssessment(com.boilerplate.java.entities.AssessmentEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);
		methodPermission = new MethodPermissions();
		methodPermission
				.setId("public java.util.List com.boilerplate.java.controllers.AssessmentController.getAssesments()");
		methodPermission.setMethodName(
				"public java.util.List com.boilerplate.java.controllers.AssessmentController.getAssesments()");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.AttemptAssessmentListEntity com.boilerplate.java.controllers.AssessmentController.getAssessmentAttempt()");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.AttemptAssessmentListEntity com.boilerplate.java.controllers.AssessmentController.getAssessmentAttempt()");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);

		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.AssessmentController.saveAssesment(com.boilerplate.java.entities.AssessmentEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.AssessmentController.saveAssesment(com.boilerplate.java.entities.AssessmentEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);

		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.AssessmentController.submitAssesment(com.boilerplate.java.entities.AssessmentEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.AssessmentController.submitAssesment(com.boilerplate.java.entities.AssessmentEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);
		// logout api

		methodPermission = new MethodPermissions();
		methodPermission.setId("public void com.boilerplate.java.controllers.UserController.logout()");
		methodPermission.setMethodName("public void com.boilerplate.java.controllers.UserController.logout()");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);
		// password reset api
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ExternalFacingReturnedUser com.boilerplate.java.controllers.UserController.automaticPasswordReset(com.boilerplate.java.entities.ExternalFacingUser)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ExternalFacingReturnedUser com.boilerplate.java.controllers.UserController.automaticPasswordReset(com.boilerplate.java.entities.ExternalFacingUser)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);

		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);
		// change password
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ExternalFacingReturnedUser com.boilerplate.java.controllers.UserController.update(com.boilerplate.java.entities.UpdateUserPasswordEntity)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ExternalFacingReturnedUser com.boilerplate.java.controllers.UserController.update(com.boilerplate.java.entities.UpdateUserPasswordEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);
		// Validate answer
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.AssessmentQuestionSectionEntity com.boilerplate.java.controllers.AssessmentController.validateAnswer(com.boilerplate.java.entities.AssessmentQuestionSectionEntity)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.AssessmentQuestionSectionEntity com.boilerplate.java.controllers.AssessmentController.validateAnswer(com.boilerplate.java.entities.AssessmentQuestionSectionEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// upload file api
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.FileEntity com.boilerplate.java.controllers.FileController.upload(java.lang.String,org.springframework.web.multipart.MultipartFile)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.FileEntity com.boilerplate.java.controllers.FileController.upload(java.lang.String,org.springframework.web.multipart.MultipartFile)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);
		// get files on master tag basis
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.collections.BoilerplateList com.boilerplate.java.controllers.FileController.getFileOnMasterTag(java.lang.String)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.collections.BoilerplateList com.boilerplate.java.controllers.FileController.getFileOnMasterTag(java.lang.String)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// get files on master tag basis
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.FileController.download(java.lang.String,javax.servlet.http.HttpServletResponse)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.FileController.download(java.lang.String,javax.servlet.http.HttpServletResponse)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// user update self api

		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ExternalFacingReturnedUser com.boilerplate.java.controllers.UserController.updateLoggedInUser(com.boilerplate.java.entities.UpdateUserEntity)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ExternalFacingReturnedUser com.boilerplate.java.controllers.UserController.updateLoggedInUser(com.boilerplate.java.entities.UpdateUserEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setPublishMethod("POST");
		methodPermission.setPublishRequired(true);
		methodPermission.setUrlToPublish(salesForceBaseurl + "/services/apexrest/Account");
		methodPermission.setDynamicPublishURl(false);
		methodPermission.setPublishTemplate("");
		methodPermission.setPublishBusinessSubject("UPDATE_LOGGED_IN_USER_AKS");

		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permissions for ContactController starts here

		// method permission for send email method
		methodPermission = new MethodPermissions();
		methodPermission.setId("public void com.boilerplate.java.controllers.ContactController.contactUsEmail()");
		methodPermission
				.setMethodName("public void com.boilerplate.java.controllers.ContactController.contactUsEmail()");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);
		// method permission for contactUs method
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.ContactController.contactUs(com.boilerplate.java.entities.ContactUsEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.ContactController.contactUs(com.boilerplate.java.entities.ContactUsEntity)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permissions for ScriptController starts here
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.ScriptController.publishUserAndAssessmentReport()");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.ScriptController.publishUserAndAssessmentReport()");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permissions for ReferralController methods starts here

		// method permission for sendReferralLink method
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.ReferralController.sendReferralLink(com.boilerplate.java.entities.ReferalEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.ReferralController.sendReferralLink(com.boilerplate.java.entities.ReferalEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for getUserReferralContacts method
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ReferalEntity com.boilerplate.java.controllers.ReferralController.getUserReferredContacts()");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ReferalEntity com.boilerplate.java.controllers.ReferralController.getUserReferredContacts()");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for validateReferContact method
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.ReferralController.validateReferContact(com.boilerplate.java.entities.ReferalEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.ReferralController.validateReferContact(com.boilerplate.java.entities.ReferalEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for facebook refer link
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ReferalEntity com.boilerplate.java.controllers.ReferralController.getFaceBookReferralLink()");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ReferalEntity com.boilerplate.java.controllers.ReferralController.getFaceBookReferralLink()");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setPublishMethod("POST");
		methodPermission.setPublishRequired(true);
		methodPermission.setUrlToPublish(salesForceBaseurl + "/services/apexrest/ReferReport");
		methodPermission.setDynamicPublishURl(false);
		methodPermission.setPublishTemplate(
				"{\"userId\": \"@userId\",\"referralUUID\": \"@referralUUID\",\"type\": \"@type\",\"referralContacts\": @referralContacts}");
		methodPermission.setPublishBusinessSubject("REFER_REPORT_CREATED_AKS");
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permissions for ContactController starts here

		// method permission for send email method
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.ContactController.contactUsEmail(com.boilerplate.java.entities.EmailEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.ContactController.contactUsEmail(com.boilerplate.java.entities.EmailEntity)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);
		// method permissions for EMICalculatorController methods starts here

		// method permission for calculateEmi method
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.EmiDataEntity com.boilerplate.java.controllers.EmiCalculatorController.calculateEmi(com.boilerplate.java.entities.EmiDataEntity)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.EmiDataEntity com.boilerplate.java.controllers.EmiCalculatorController.calculateEmi(com.boilerplate.java.entities.EmiDataEntity)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permissions for blogactivitycontroller starts here

		// method permission for saveActivity method
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.BlogActivityController.saveActivity(com.boilerplate.java.entities.BlogActivityEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.BlogActivityController.saveActivity(com.boilerplate.java.entities.BlogActivityEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for feedback controller starts here

		// method permission for sendEmailOnFeedbackSubmit method
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllersFeedbackController.sendEmailOnFeedbackSubmit(com.boilerplate.java.entities.FeedBackEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllersFeedbackController.sendEmailOnFeedbackSubmit(com.boilerplate.java.entities.FeedBackEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		this.set("METHOD_PERMISSIONS", Base.toXML(methodPermissionMap));
	}

	/**
	 * This method creates seed user and role.
	 */
	private void createSeedUserAndRole() {
		GenericListEncapsulationEntity<Role> roles = new GenericListEncapsulationEntity<Role>();
		roles.setEntityList(new BoilerplateList<Role>());
		Role role = null;
		role = new Role("Admin", "Admin", "Admin of the system", true, false);
		roles.getEntityList().add(role);
		Role roleAdmin = role;
		role = new Role("RoleAssigner", "RoleAssigner", "This role can assign roles to other users", true, false);
		roles.getEntityList().add(role);
		Role roleAssigner = role;
		role = new Role("SelfAssign1", "SelfAssign1", "UT role", false, true);
		roles.getEntityList().add(role);
		role = new Role("SelfAssign2", "SelfAssign2", "UT role", false, true);
		roles.getEntityList().add(role);
		role = new Role("NonSelfAssign1", "NonSelfAssign1", "UT role", false, false);
		roles.getEntityList().add(role);
		role = new Role("NonSelfAssign2", "NonSelfAssign2", "UT role", false, false);
		roles.getEntityList().add(role);
		role = new Role("BackOfficeUser", "BackOfficeUser", "The back office user of the system", true, false);
		roles.getEntityList().add(role);
		role = new Role("BankAdmin", "BankAdmin", "The admin of the bank", true, false);
		roles.getEntityList().add(role);
		role = new Role("BankUser", "BankUser", "The user in the bank", true, false);
		roles.getEntityList().add(role);
		role = new Role("Impersinator", "Impersinator", "Is allowed impersination", true, false);
		roles.getEntityList().add(role);

		this.set("ROLES", Base.toXML(roles));

		// create annonnymous user
		ExternalFacingReturnedUser user = new ExternalFacingReturnedUser();
		user.setId("AKS:ANNONYMOUS");
		user.setUserId("AKS:ANNONYMOUS");
		user.setPassword("0");
		user.setAuthenticationProvider("AKS");
		user.setExternalSystemId("AKS:ANNONYMOUS");
		user.setUserStatus(1);
		user.setRoles(new BoilerplateList<Role>());
		this.set("USER:" + user.getUserId(), user);

		// create admin
		user = new ExternalFacingReturnedUser();
		user.setId("AKS:ADMIN");
		user.setUserId("AKS:ADMIN");
		user.setPassword("password");
		user.setFirstName("Admin");
		user.hashPassword();
		user.setAuthenticationProvider("AKS");
		user.setExternalSystemId("AKS:ADMIN");
		user.setUserStatus(1);
		user.setRoles(new BoilerplateList<Role>());
		user.getRoles().add(roleAdmin);
		this.set("USER:" + user.getUserId(), user);

		// create background
		user = new ExternalFacingReturnedUser();
		user.setId("AKS:BACKGROUND");
		user.setUserId("AKS:BACKGROUND");
		user.setPassword("0");
		user.setAuthenticationProvider("AKS");
		user.setExternalSystemId("AKS:BACKGROUND");
		user.setUserStatus(1);
		user.setRoles(new BoilerplateList<Role>());
		user.getRoles().add(roleAdmin);
		this.set("USER:" + user.getUserId(), user);

		// create role assigner
		user = new ExternalFacingReturnedUser();
		user.setId("AKS:ROLEASSIGNER");
		user.setUserId("AKS:ROLEASSIGNER");
		user.setPassword("0");
		user.setAuthenticationProvider("AKS");
		user.setExternalSystemId("AKS:ROLEASSIGNER");
		user.setUserStatus(1);
		user.setRoles(new BoilerplateList<Role>());
		user.getRoles().add(roleAssigner);
		this.set("USER:" + user.getUserId(), user);

	}

	/**
	 * SalesForce Production Base Url
	 */
	public static final String SalesForceProductionBaseUrl = "https://clearmydues.my.salesforce.com";

	/**
	 * SalesForce Development Base Url
	 */
	public static final String SalesForceDevelopmentBaseUrl = "https://clearmydues--Developmen.cs31.my.salesforce.com";

	/**
	 * SalesForce UAT Base Url
	 */
	public static final String SalesForceTestBaseUrl = "https://clearmydues--Developmen.cs31.my.salesforce.com";

	/**
	 * This method tells us about SalesForce Base url
	 * 
	 * @return serverName The salesForceBaseurl
	 */
	private String getSalesForceBaseUrl() {
		String salesForceBaseurl = null;
		switch (properties.getProperty("Enviornment").toUpperCase()) {
		case "PRODUCTION":
			salesForceBaseurl = SalesForceProductionBaseUrl;
			break;
		case "DEVELOPMENT":
			salesForceBaseurl = SalesForceDevelopmentBaseUrl;
			break;
		case "TEST":
			salesForceBaseurl = SalesForceTestBaseUrl;
			break;
		}
		return salesForceBaseurl;
	}

	/**
	 * This method create seed test data configuration.
	 * 
	 * @return configuration map
	 */
	private BoilerplateMap<String, String> createSeedTestData() {
		// This attribute tells us about server name
		String salesForceBaseurl = getSalesForceBaseUrl();
		BoilerplateMap<String, String> vAllETest = new BoilerplateMap<String, String>();
		vAllETest.put("SMS_ROOT_URL", "http://alerts.solutionsinfini.com/api/v3/index.php");
		vAllETest.put("SMS_API_KEY", "A0f52a89f0ab2bf7d755ab9dada057eab");
		vAllETest.put("SMS_SENDER", "CDSSMS");
		vAllETest.put("SMS_URL", "?method=sms&api_key=@apiKey&to=@to&sender=@sender&message=@message");
		vAllETest.put("S3_Bucket_Name", "csrdata-files");
		vAllETest.put("Secret_Access_Key", "VE7YpkMBrwCLbHgUz/8j8j0i2rdmhhnguv3LmJZz");
		vAllETest.put("Access_Key", "AKIAISJEWKJJ77Z4G5MQ");
		vAllETest.put("S3_Files_Path", "https://s3-ap-southeast-1.amazonaws.com/csrdata-files/");
		vAllETest.put("Contact_Person_Email", "love.kranti@clearmydues.com");
		vAllETest.put("Post_Article_Contact_Person_Email", "shiva.gupta@krantitechservices.in");
		vAllETest.put("Salesforce_Authtoken_URL", salesForceBaseurl
				+ "/services/oauth2/token?grant_type=password&client_id=3MVG9Se4BnchkASk.FTlViI7LYUGoKUIgrSoEssN2rGYY6dc99Ijwl6saXGnFU54MHNmFK32Bltn2rble187S&client_secret=5717367022576838052&username=aman.bindal@clearmydues.com.developmen&password=Jan@2016AZEDXfLWSBLW8T3s9EtWzsJq");
		vAllETest.put("tosEmailListForPublishBulkFailure", "love.kranti@clearmydues.com");
		vAllETest.put("ccsEmailListForPublishBulkFailure", "shiva.gupta@krantitechservices.in");
		vAllETest.put("RootFileDownloadLocation", "/downloads/");

		vAllETest.put("SF_Update_Account_Publish_Method", "POST");
		vAllETest.put("SF_Update_Account_Publish_Subject", "Publish_Bulk_HashData");
		vAllETest.put("SF_Update_Account_Publish_URL", salesForceBaseurl + "/services/apexrest/UpdateAccount");
		vAllETest.put("AKS_Assessment_Publish_URL", salesForceBaseurl + "/services/apexrest/AKSReport");
		vAllETest.put("AKS_USER_Publish_URL", salesForceBaseurl + "/services/apexrest/Account");
		vAllETest.put("Is_Publish_Report", "true"); // false for not publish
		vAllETest.put("Is_Script_Publish_User_To_CRM", "false"); // false for
																	// not
																	// publish
		vAllETest.put("Is_REFERRAL_REPORT_PUBLISH_ENABLED", "true");
		vAllETest.put("AKS_REFER_PUBLISH_URL", salesForceBaseurl + "/services/apexrest/AKSReport");
		vAllETest.put("AKS_REFER_PUBLISH_URL", salesForceBaseurl + "/services/apexrest/ReferReport");
		vAllETest.put("BASE_REFERRAL_LINK",
				"http://javacsr-120082491.ap-southeast-1.elb.amazonaws.com/#/?utm_medium=@utm_medium&&utm_source=@utm_campaign");
		vAllETest.put("URL_SHORTENER_API_URL",
				"https://zetl5ogaq4.execute-api.ap-southeast-1.amazonaws.com/test/urlshortener");
		vAllETest.put("REGISTERATION_REFER_EMAIL_CONTENT", "eae0c74f-4b0b-4a7e-8a97-4488404d6dac_referFriendhtml");
		return vAllETest;
	}

	/**
	 * This method create seed development data configuration.
	 * 
	 * @return configuration map
	 */
	private BoilerplateMap<String, String> createSeedDevelopmentData() {
		// This attribute tells us about server name
		String salesForceBaseurl = getSalesForceBaseUrl();
		BoilerplateMap<String, String> vAllEDev = new BoilerplateMap<String, String>();
		// put all configuration
		vAllEDev.put("SMS_ROOT_URL", "http://alerts.solutionsinfini.com/api/v3/index.php");
		vAllEDev.put("SMS_API_KEY", "A0f52a89f0ab2bf7d755ab9dada057eab");
		vAllEDev.put("SMS_SENDER", "CDSSMS");
		vAllEDev.put("SMS_URL", "?method=sms&api_key=@apiKey&to=@to&sender=@sender&message=@message");
		vAllEDev.put("RootFileUploadLocation", "/downloads/");
		// new config
		vAllEDev.put("S3_Bucket_Name", "csrdata-files");
		vAllEDev.put("Secret_Access_Key", "VE7YpkMBrwCLbHgUz/8j8j0i2rdmhhnguv3LmJZz");
		vAllEDev.put("Access_Key", "AKIAISJEWKJJ77Z4G5MQ");
		vAllEDev.put("S3_Files_Path", "https://s3-ap-southeast-1.amazonaws.com/csrdata-files/");
		vAllEDev.put("Contact_Person_Email", "love.kranti@clearmydues.com");
		vAllEDev.put("Post_Article_Contact_Person_Email", "shiva.gupta@krantitechservices.in");
		vAllEDev.put("Salesforce_Authtoken_URL", salesForceBaseurl
				+ "/services/oauth2/token?grant_type=password&client_id=3MVG9Se4BnchkASk.FTlViI7LYUGoKUIgrSoEssN2rGYY6dc99Ijwl6saXGnFU54MHNmFK32Bltn2rble187S&client_secret=5717367022576838052&username=aman.bindal@clearmydues.com.developmen&password=Jan@2016AZEDXfLWSBLW8T3s9EtWzsJq");
		vAllEDev.put("tosEmailListForPublishBulkFailure", "love.kranti@clearmydues.com");
		vAllEDev.put("ccsEmailListForPublishBulkFailure", "shiva.gupta@krantitechservices.in");
		vAllEDev.put("RootFileDownloadLocation", "/downloads/");

		vAllEDev.put("SF_Update_Account_Publish_Method", "POST");
		vAllEDev.put("SF_Update_Account_Publish_Subject", "Publish_Bulk_HashData");
		vAllEDev.put("SF_Update_Account_Publish_URL", salesForceBaseurl + "/services/apexrest/UpdateAccount");
		vAllEDev.put("AKS_Assessment_Publish_URL", salesForceBaseurl + "/services/apexrest/AKSReport");
		vAllEDev.put("AKS_USER_Publish_URL", salesForceBaseurl + "/services/apexrest/Account");
		vAllEDev.put("Is_Publish_Report", "true"); // false for not publish
		vAllEDev.put("Is_Script_Publish_User_To_CRM", "false"); // false for not
																// publish
		vAllEDev.put("Is_REFERRAL_REPORT_PUBLISH_ENABLED", "true");
		vAllEDev.put("AKS_REFER_PUBLISH_URL", salesForceBaseurl + "/services/apexrest/AKSReport");
		vAllEDev.put("AKS_REFER_PUBLISH_URL", salesForceBaseurl + "/services/apexrest/ReferReport");
		vAllEDev.put("BASE_REFERRAL_LINK",
				"http://localhost:8080/CSRFrontend/#/?utm_medium=@utm_medium&&utm_source=@utm_campaign");
		vAllEDev.put("URL_SHORTENER_API_URL",
				"https://zetl5ogaq4.execute-api.ap-southeast-1.amazonaws.com/test/urlshortener");
		vAllEDev.put("REGISTERATION_REFER_EMAIL_CONTENT", "d5ac3307-9360-476a-8cd0-6aa464381d56_referFriendhtml");

		// emailId to whom to send user selected feature got in feedback
		vAllEDev.put("EMAILIDTO_SEND_SELECTED_FEATURE", "urvij.singh@krantitechservices.in");
		
		return vAllEDev;

	}

	/**
	 * This method create seed prod data configuration.
	 * 
	 * @return configuration map
	 */
	private BoilerplateMap<String, String> createSeedProductionData() {
		// This attribute tells us about server name
		// This attribute tells us about server name
		String salesForceBaseurl = getSalesForceBaseUrl();
		BoilerplateMap<String, String> vAllEProduction = new BoilerplateMap<String, String>();

		vAllEProduction.put("SMS_ROOT_URL", "http://alerts.solutionsinfini.com/api/v3/index.php");
		vAllEProduction.put("SMS_API_KEY", "A0f52a89f0ab2bf7d755ab9dada057eab");
		vAllEProduction.put("SMS_SENDER", "CDSSMS");
		vAllEProduction.put("SMS_URL", "?method=sms&api_key=@apiKey&to=@to&sender=@sender&message=@message");

		// new config
		vAllEProduction.put("S3_Bucket_Name", "csrdata-filesprod");
		vAllEProduction.put("Secret_Access_Key", "u5jCd7AL6Gl3VK+hQvCoIqgJiT7FINjZAcze8MOQ");
		vAllEProduction.put("Access_Key", "AKIAI4CMTKY46FFRDFFQ");
		vAllEProduction.put("S3_Files_Path", "https://s3-ap-southeast-1.amazonaws.com/csrdata-filesprod/");
		vAllEProduction.put("Contact_Person_Email", "madhurima.bhadury@projectakshar.com");
		vAllEProduction.put("Post_Article_Contact_Person_Email", "madhurima.bhadury@projectakshar.com");
		vAllEProduction.put("Salesforce_Authtoken_URL", salesForceBaseurl
				+ "/services/oauth2/token?grant_type=password&client_id=3MVG9ZL0ppGP5UrDP91Iy_g04TU6IXDdftERV1nrcjs_Waw4TT6OMzxsZTLsECa.yAiavsFuRwST.QiotldN7&client_secret=1742947365004389153&username=aman.bindal@clearmydues.com&password=Jan@2017oUa14f1cSUuJtZtxPVKyC8Og");
		vAllEProduction.put("tosEmailListForPublishBulkFailure", "aman.bindal@clearmydues.com");
		vAllEProduction.put("ccsEmailListForPublishBulkFailure", "love.kranti@clearmydues.com");
		vAllEProduction.put("RootFileDownloadLocation", "/downloads/");

		vAllEProduction.put("SF_Update_Account_Publish_Method", "POST");
		vAllEProduction.put("SF_Update_Account_Publish_Subject", "Publish_Bulk_HashData");
		vAllEProduction.put("SF_Update_Account_Publish_URL", salesForceBaseurl + "/services/apexrest/UpdateAccount");
		vAllEProduction.put("AKS_Assessment_Publish_URL", salesForceBaseurl + "/services/apexrest/AKSReport");
		vAllEProduction.put("AKS_USER_Publish_URL", salesForceBaseurl + "/services/apexrest/Account");
		vAllEProduction.put("Is_Publish_Report", "true"); // false for not
		vAllEProduction.put("Is_Script_Publish_User_To_CRM", "true"); // false

		vAllEProduction.put("Is_REFERRAL_REPORT_PUBLISH_ENABLED", "true");
		vAllEProduction.put("AKS_REFER_PUBLISH_URL", salesForceBaseurl + "/services/apexrest/ReferReport");

		vAllEProduction.put("BASE_REFERRAL_LINK",
				"https://www.projectakshar.com/#/?utm_medium=@utm_medium&&utm_source=@utm_campaign");
		vAllEProduction.put("URL_SHORTENER_API_URL",
				"https://exzxzxudn4.execute-api.ap-southeast-1.amazonaws.com/aks/urlshortener");

		vAllEProduction.put("REGISTERATION_REFER_EMAIL_CONTENT",
				"d6085bc0-9827-45e2-b72a-2afe565e7c84_referFriendhtml");
		return vAllEProduction;
	}

	/**
	 * This method create seed common data configuration.
	 * 
	 * @return configuration map
	 */
	private BoilerplateMap<String, String> createSeedCommonData() {
		BoilerplateMap<String, String> vAllEAll = new BoilerplateMap<String, String>();
		// put all configuration
		vAllEAll.put("V_All_All", "V_All_All");
		vAllEAll.put("V_All_Dev", "V_All_Dev");
		vAllEAll.put("DefaultAuthenticationProvider", "AKS");
		vAllEAll.put("DSAAuthenticationProvider", "DSA");
		vAllEAll.put("SessionTimeOutInMinutes", "43200");
		vAllEAll.put("CMD_Organization_Id", "CMD001");
		vAllEAll.put("OrganizationTimeOutInSeconds", "2147483647");

		vAllEAll.put("CrossDomainHeaders",
				"Access-Control-Allow-Headers:*;Access-Control-Allow-Methods:*;Access-Control-Allow-Origin:*;Access-Control-Allow-Credentials: true;Access-Control-Expose-Headers:*");
		vAllEAll.put("DefaultUserStatus", "1");
		vAllEAll.put("Offer_Initial_Month_Size", "1");
		// Admin credentials
		vAllEAll.put("AdminId", "admin");
		vAllEAll.put("AdminPassword", "password");
		vAllEAll.put("SQL_QUERY_GET_ASSESSMENT",
				"From AssessmentEntity assessment where assessment.id = :Id and assessment.active = :Active ");
		vAllEAll.put("SQL_QUERY_GET_MULTIPLE_CHOICE_QUESTION",
				"From MultipleChoiceQuestionEntity mcq where mcq.questionId = :QuestionId ");
		vAllEAll.put("SQL_QUERY_GET_ASSESSMENT_LIST",
				"Select Name as name, Id as id,MaxScore as maxScore From Assessment Where IsActive = 1 And (IsSurvey = 0 or IsSurvey is null)");
		vAllEAll.put("SQL_QUERY_GET_SURVEY_LIST",
				"Select Name as name, Id as id,MaxScore as maxScore From Assessment Where IsActive = 1 And IsSurvey = 1");
		vAllEAll.put("SQL_QUERY_GET_QUESTION_EXPLANATION",
				"Select Explanation as QuestionExplanation From Question Where Id = :QuestionId");
		vAllEAll.put("Maximum_File_Upload_Size", "5");
		// Owner Allocation QUEUE NAme
		vAllEAll.put("S3_Signed_Url_Time_In_Hour", "5");
		vAllEAll.put("Default_Score", "0");

		vAllEAll.put("Rank1", "First Steps");
		vAllEAll.put("Rank2", "Stepping Up");
		vAllEAll.put("Rank3", "Walker");
		vAllEAll.put("Rank4", "Jogger");
		vAllEAll.put("Rank5", "Runner");
		vAllEAll.put("Rank6", "Sprinter");
		vAllEAll.put("Rank7", "Pace Setter");
		vAllEAll.put("Rank8", "Cross Country Racer");
		vAllEAll.put("Rank9", "Marathon Racer");
		vAllEAll.put("PublishDispatcherSleepTime", "120000");
		// process bulk process
		vAllEAll.put("Process_Bulk_Count", "10");
		vAllEAll.put("SF_Update_Hash_Name", "SFUpdateHash");
		vAllEAll.put("AKS_PUBLISH_QUEUE", "_PUBLISH_QUEUE_AKS_");
		vAllEAll.put("AKS_PUBLISH_SUBJECT", "Publish");

		// assessment publish config
		vAllEAll.put("AKS_Assessment_Publish_Method", "POST");
		vAllEAll.put("AKS_Assessment_Publish_Subject", "REPORT_CREATED_AKS");
		vAllEAll.put("AKS_Assessment_Publish_Template",
				"{\"totalScore\": \"@totalScore\",\"userId\": \"@userId\",\"rank\": \"@rank\",\"aksAssessments\": @aksAssessments}");
		vAllEAll.put("AKS_Assessment_Dynamic_Publish_Url", "false");
		// vAllEAll.put("Is_Publish_Report", "true"); // false for not publish
		vAllEAll.put("PublishDispatcherSleepTimeBeforeUser", "120000");
		vAllEAll.put("SQL_QUERY_GET_USER_ARTICLE", "From ArticleEntity WHERE userId = :UserId");
		// assessment user publish script

		vAllEAll.put("AKS_USER_Publish_Method", "POST");
		vAllEAll.put("AKS_USER_Publish_Subject", "CREATE_USER_AKS");
		vAllEAll.put("AKS_USER_Publish_Template",
				"{\"id\": \"@Id\",\"userId\": \"@userId\",\"authenticationProvider\": \"@authenticationProvider\",\"email\": \"@email\",\"firstName\": \"@firstName\",\"lastName\": \"@lastName\",\"middleName\": \"@middleName\",\"phoneNumber\": \"@phoneNumber\",\"ownerId\": \"@ownerId\",\"referalSource\": \"@referalSource\"}");
		vAllEAll.put("AKS_USER_Dynamic_Publish_Url", "false");
		vAllEAll.put("AKS_USER_EMAIL_HASH_BASE_TAG", "AKS_EMAIL_LIST_HASH");
		vAllEAll.put("MAX_SIZE_OF_REFERRAL_CONTACTS_PER_DAY", "10");
		vAllEAll.put("REFERRAL_LINK_UUID_LENGTH", "8");
		vAllEAll.put("GET_SHORT_URL_REQUEST_BODY_TEMPLATE", "{\"longUrl\":\"@longUrl\"}");
		vAllEAll.put("REFERRED_CONTACT_EXPIRATION_TIME_IN_MINUTE", "10080");
		vAllEAll.put("REFER_SCORE_FOR_EMAIL", "10");
		vAllEAll.put("REFER_SCORE_FOR_PHONE", "10");
		vAllEAll.put("REFER_SCORE_FOR_FACEBOOK", "10");
		vAllEAll.put("SIGNUP_USER_REFER_SCORE_FOR_EMAIL", "10");
		vAllEAll.put("SIGNUP_USER_REFER_SCORE_FOR_PHONE", "10");
		vAllEAll.put("SIGNUP_USER_REFER_SCORE_FOR_FACEBOOK", "10");
		vAllEAll.put("AKS_USER_UUID_HASH_BASE_TAG", "AKS_USER_UUID_HASH_MAP");
		vAllEAll.put("AKS_UUID_USER_HASH_BASE_TAG", "AKS_UUID_USER_HASH_MAP");
		vAllEAll.put("IS_SIGN_UP_USER_GET_REFER_SCORE", "true");
		// REFER publish configuration
		vAllEAll.put("AKS_REFER_PUBLISH_METHOD", "POST");
		vAllEAll.put("AKS_REFER_PUBLISH_SUBJECT", "REFER_REPORT_CREATED_AKS");
		vAllEAll.put("AKS_REFER_PUBLISH_TEMPLATE",
				"{\"userId\": \"@userId\",\"referralUUID\": \"@referralUUID\",\"type\": \"@type\",\"referralContacts\": @referralContacts,\"referralLink\": \"@referralLink\"}");
		vAllEAll.put("AKS_REFER_DYNAMIC_PUBLISH_URL", "false");
		vAllEAll.put("REFERRED_CONTACT_EXPIRATION_TIME_IN_MINUTE_FOR_ONE_DAY", "1440");
		vAllEAll.put("MAX_ALLOW_USER_SCORE", "40");
		vAllEAll.put("UPDATE_AKS_USER_SUBJECT", "UPDATE_LOGGED_IN_USER_AKS");
		
		return vAllEAll;

	}

	/**
	 * This method creates seed content
	 */
	private void createSeedContent() {
		// Storing seed content
		BoilerplateMap<String, String> contentMap = new BoilerplateMap<>();
		contentMap.put("WELCOME_MESSAGE_EMAIL_SUBJECT", "Welcome @FirstName");
		contentMap.put("WELCOME_MESSAGE_SMS",
				"Welcome @FirstName, @Password is your password. Thanks for registering on Akshar!");
		contentMap.put("RESET_PASSWORD_SMS",
				"Your new password is @Password. Please change your password after logging in. Akshar.");
		contentMap.put("PASSWORD_CHANGE_SMS", "Dear @FirstName, your password has been changed successfully. Akshar.");
		contentMap.put("WELCOME_MESSAGE_EMAIL_SUBJECT", "Contact Us");
		contentMap.put("CONTACT_US_EMAIL_BODY",
				"<b><h2>Contact Person Details:<h2></b> <b>Name:</b> @ContactPersonName <br> <b>Email:</b> @ContactPersonEmail <br> <b>Contact Number:</b> @ContactPersonMobileNumber <br> <b>Message:</b> @ContactPersonMessage");
		contentMap.put("BULK_PUBLISH_FAIL_EMAIL_SUBJECT", "AKS Bulk Publish Failure ");
		contentMap.put("POST_ARTICLE_EMAIL_SUBJECT", "Akshar Article : @Subject");
		contentMap.put("POST_ARTICLE_EMAIL_BODY",
				"<b><h2>Article Details:<h2></b> <b>Name:</b> @UserName <br> <b>Email:</b> @UserEmail <br> <b>Contact Number:</b> @UserMobileNumber <br> <b>Title:</b> @Title <br> <b>Content:</b> @Content <br> <b>KeyWords:</b> @KeyWords");
		// sms message for sending invitation to referred user
		// contentMap.put("JOIN_INVITATION_SMS",
		// "Hi, @UserFirstName referred you to join AKSHAR! Play exciting
		// quizzes to boost your financial knowledge and win exciting
		// rewards!@link");

		contentMap.put("JOIN_INVITATION_SMS",
				"Hi, @UserFirstName has referred you to join Akshar! Play exciting quizzes to boost your financial knowledge & win exciting rewards. Join now! @link");
		// email message for sending invitation to referred user related
		contentMap.put("JOIN_INVITATION_MESSAGE_EMAIL_SUBJECT", "You have just been referred!");
		contentMap.put("JOIN_INVITATION_MESSAGE_EMAIL_BODY",
				"<b><Hi, @UserFirstName referred you to join AKSHAR! Play exciting quizzes to boost your financial knowledge and win exciting rewards! @link/b>");

		//subject of email to be sent on getting user's selected feature(in feedback) 
		contentMap.put("FEATURE_SELECTED_INFO_EMAIL_SUBJECT", "User's selected feature");
		
		this.set("CONTENT:CMD001:VERSION_ALL:LOCALE_ALL", Base.toXML(contentMap));
	}

	/**
	 * Sets a key field value
	 * 
	 * @param key
	 *            The key
	 * @param field
	 *            The field
	 * @param value
	 *            The value
	 */
	public void hset(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.hset(key, field, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * Sets a key field value
	 * 
	 * @param key
	 *            The key
	 * @param field
	 *            The field
	 * @param value
	 *            The value
	 * @param timeoutInSeconds
	 *            The expire time
	 */
	public void hset(String key, String field, String value, int timeoutInSeconds) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.hset(key, field, value);
			jedis.expire(key, timeoutInSeconds);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * Gets a key field value
	 * 
	 * @param key
	 *            The key
	 * @param field
	 *            The field
	 * @return
	 */
	public String hget(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			return jedis.hget(key, field);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * Sets the key hash
	 * 
	 * @param key
	 *            The key
	 * @param hash
	 *            The hash
	 */
	public void hmset(String key, Map<String, String> hash) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.hmset(key, hash);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * Gets a key all fields value
	 * 
	 * @param key
	 *            The key
	 * @return
	 */
	public Map<String, String> hgetAll(String key) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			return jedis.hgetAll(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * Dels the key
	 * 
	 * @param key
	 *            The key
	 */
	public void del(String key) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.del(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * Getting the key/keys satisfying pattern
	 * 
	 * @param pattern
	 *            The pattern
	 * @return The set of keys
	 */
	public Set<String> keys(String pattern) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			return jedis.keys(pattern);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * This method adds the members to set access by defined key
	 * 
	 * @param key
	 *            The set key
	 * @param members
	 *            The members
	 */
	public void sadd(String key, String members) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.sadd(key, members);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * This method gets the set members
	 * 
	 * @param key
	 *            The set key
	 * @return The set of members
	 */
	public Set<String> smembers(String key) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			return jedis.smembers(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * This method tells whether key exists or not in our Redis
	 * 
	 * @param key
	 *            The key
	 * @return true/false
	 */
	public boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			return jedis.exists(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * This method delete the field from map.
	 * 
	 * @param key
	 *            The key
	 * @param field
	 *            The field name
	 * @return true/false
	 */
	public boolean delMapfield(String key, String field) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			return jedis.hdel(key, field) != null;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * Sets the value
	 * 
	 * @param key
	 *            The key
	 * @param value
	 *            The value
	 */
	public void set(String key, String value, int timeoutInSeconds) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.setex(key, timeoutInSeconds, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * This method increase the key counter in redis
	 * 
	 * @param key
	 *            The redis key
	 * @return true/false
	 */
	public boolean increaseCounter(String key) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			return jedis.incr(key) != null;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
}
