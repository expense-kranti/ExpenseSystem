package com.boilerplate.database.redis.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.MethodPermissions;

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

		// method permission for create user
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ExternalFacingUser com.boilerplate.java.controllers.UserController.createUser(com.boilerplate.java.entities.ExternalFacingUser)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ExternalFacingUser com.boilerplate.java.controllers.UserController.createUser(com.boilerplate.java.entities.ExternalFacingUser)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for update user
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ExternalFacingUser com.boilerplate.java.controllers.UserController.updateUser(com.boilerplate.java.entities.ExternalFacingUser)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ExternalFacingUser com.boilerplate.java.controllers.UserController.updateUser(com.boilerplate.java.entities.ExternalFacingUser)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for disable user
		methodPermission = new MethodPermissions();
		methodPermission
				.setId("public void com.boilerplate.java.controllers.UserController.disableUser(java.lang.String)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.UserController.disableUser(java.lang.String)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for enable user
		methodPermission = new MethodPermissions();
		methodPermission
				.setId("public void com.boilerplate.java.controllers.UserController.enableUser(java.lang.String)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.UserController.enableUser(java.lang.String)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for assigning approvers
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.UserController.assignApprovers(com.boilerplate.java.entities.AssignApproverEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.UserController.assignApprovers(com.boilerplate.java.entities.AssignApproverEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for assignRoles
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.UserController.assignRoles(com.boilerplate.java.entities.SaveRoleEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.UserController.assignRoles(com.boilerplate.java.entities.SaveRoleEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for delete roles
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.UserController.deleteRoles(com.boilerplate.java.entities.SaveRoleEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.UserController.deleteRoles(com.boilerplate.java.entities.SaveRoleEntity)");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for get all users
		methodPermission = new MethodPermissions();
		methodPermission.setId("public java.util.List com.boilerplate.java.controllers.UserController.getAllUsers()");
		methodPermission
				.setMethodName("public java.util.List com.boilerplate.java.controllers.UserController.getAllUsers()");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for creating expense
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ExpenseEntity com.boilerplate.java.controllers.ExpenseController.createExpense(com.boilerplate.java.entities.ExpenseEntity)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ExpenseEntity com.boilerplate.java.controllers.ExpenseController.createExpense(com.boilerplate.java.entities.ExpenseEntity)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for updating expense
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ExpenseEntity com.boilerplate.java.controllers.ExpenseController.updateExpense(com.boilerplate.java.entities.ExpenseEntity)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ExpenseEntity com.boilerplate.java.controllers.ExpenseController.updateExpense(com.boilerplate.java.entities.ExpenseEntity)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for authenticating user
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.sessions.Session com.boilerplate.java.controllers.UserController.ssoLogin(java.lang.String)");
		methodPermission.setMethodName(
				"public com.boilerplate.sessions.Session com.boilerplate.java.controllers.UserController.ssoLogin(java.lang.String)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(false);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for get user(currently logged in user)
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ExternalFacingUser com.boilerplate.java.controllers.UserController.getCurrentUser()");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ExternalFacingUser com.boilerplate.java.controllers.UserController.getCurrentUser()");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(false);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for getExpenses
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public java.util.List com.boilerplate.java.controllers.ExpenseController.getExpensesForEmployee(com.boilerplate.java.entities.FetchExpenseEntity)");
		methodPermission.setMethodName(
				"public java.util.List com.boilerplate.java.controllers.ExpenseController.getExpensesForEmployee(com.boilerplate.java.entities.FetchExpenseEntity)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for file upload
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.FileDetailsEntity com.boilerplate.java.controllers.FileController.upload(java.lang.String,org.springframework.web.multipart.MultipartFile)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.FileDetailsEntity com.boilerplate.java.controllers.FileController.upload(java.lang.String,org.springframework.web.multipart.MultipartFile)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for getExpenses for approvers/super approvers
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public java.util.List com.boilerplate.java.controllers.ExpenseController.getExpensesForApprover()");
		methodPermission.setMethodName(
				"public java.util.List com.boilerplate.java.controllers.ExpenseController.getExpensesForApprover()");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(true);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for approverExpense for approvers/super approvers
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.java.entities.ExpenseEntity com.boilerplate.java.controllers.ExpenseController.approveExpenseForApprover(com.boilerplate.java.entities.ExpenseReviewEntity)");
		methodPermission.setMethodName(
				"public com.boilerplate.java.entities.ExpenseEntity com.boilerplate.java.controllers.ExpenseController.approveExpenseForApprover(com.boilerplate.java.entities.ExpenseReviewEntity)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(true);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for get expenses for finance
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public java.util.List com.boilerplate.java.controllers.ExpenseController.getExpensesForFinance()");
		methodPermission.setMethodName(
				"public java.util.List com.boilerplate.java.controllers.ExpenseController.getExpensesForFinance()");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for get expense reports for finance
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public java.util.List com.boilerplate.java.controllers.ExpenseController.getReportsForFinance(java.lang.String)");
		methodPermission.setMethodName(
				"public java.util.List com.boilerplate.java.controllers.ExpenseController.getReportsForFinance(java.lang.String)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for approve/reject individual expense by finance
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.ExpenseController.expenseReviewByFinance(com.boilerplate.java.entities.ExpenseReviewEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.ExpenseController.expenseReviewByFinance(com.boilerplate.java.entities.ExpenseReviewEntity)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for file download
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.FileController.downloadFile(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse,java.lang.String)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.FileController.downloadFile(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse,java.lang.String)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for approve for finance
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public void com.boilerplate.java.controllers.ExpenseController.approveExpenseForFinance(com.boilerplate.java.entities.ExpenseReportEntity)");
		methodPermission.setMethodName(
				"public void com.boilerplate.java.controllers.ExpenseController.approveExpenseForFinance(com.boilerplate.java.entities.ExpenseReportEntity)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(true);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// method permission for approve for finance
		methodPermission = new MethodPermissions();
		methodPermission.setId(
				"public com.boilerplate.sessions.Session com.boilerplate.java.controllers.UserController.sSOauthenticate(java.lang.String)");
		methodPermission.setMethodName(
				"public com.boilerplate.sessions.Session com.boilerplate.java.controllers.UserController.sSOauthenticate(java.lang.String)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(false);
		methodPermission.setIsApproverRoleRequired(false);
		methodPermission.setIsFinanceRoleRequired(false);
		methodPermissionMap.put(methodPermission.getMethodName(), methodPermission);

		// save the method permission map in configuration
		// in database
		this.set("METHOD_PERMISSIONS", Base.toXML(methodPermissionMap));

	}

	/**
	 * This method creates seed user and role.
	 */
	private void createSeedUserAndRole() {
	}

	/**
	 * This method create seed test data configuration.
	 * 
	 * @return configuration map
	 */
	private BoilerplateMap<String, String> createSeedTestData() {
		// This attribute tells us about server name
		BoilerplateMap<String, String> vAllETest = new BoilerplateMap<String, String>();
		return vAllETest;
	}

	/**
	 * This method create seed development data configuration.
	 * 
	 * @return configuration map
	 */
	private BoilerplateMap<String, String> createSeedDevelopmentData() {
		// This attribute tells us about server name
		BoilerplateMap<String, String> vAllEDev = new BoilerplateMap<String, String>();
		// put all configuration
		vAllEDev.put("DESTINATION_FOR_SAVING_FILE_ON_DISK", "/home/ruchi/ExpenseBills");

		return vAllEDev;

	}

	/**
	 * This method create seed prod data configuration.
	 * 
	 * @return configuration map
	 */
	private BoilerplateMap<String, String> createSeedProductionData() {
		// This attribute tells us about server name
		BoilerplateMap<String, String> vAllEProduction = new BoilerplateMap<String, String>();

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
		vAllEAll.put("SessionTimeOutInMinutes", "129600");

		vAllEAll.put("CrossDomainHeaders",
				"Access-Control-Allow-Headers:*;Access-Control-Allow-Methods:*;Access-Control-Allow-Origin:*;Access-Control-Allow-Credentials: true;Access-Control-Expose-Headers:*");
		vAllEAll.put("DefaultUserStatus", "1");
		vAllEAll.put("DefaultAuthenticationProvider", "DEFAULT");
		vAllEAll.put("HD_CLAIM", "krantitechservices.in");

		vAllEAll.put("Offer_Initial_Month_Size", "1");
		// Admin credentials
		vAllEAll.put("AdminId", "admin");
		vAllEAll.put("AdminPassword", "password");
		vAllEAll.put("Maximum_File_Upload_Size", "5");
		// Email configuration data
		vAllEAll.put("SUBJECT_FOR_EXPENSE_SUBMISSION", "Expense Submitted");
		vAllEAll.put("SUBJECT_FOR_EXPENSE_RE_SUBMISSION", "Expense Re-submitted");
		vAllEAll.put("SUBJECT_FOR_EXPENSE_APPROVED", "Expense Approved");
		vAllEAll.put("SUBJECT_FOR_EXPENSE_REJECTION", "Expense Rejected");
		vAllEAll.put("CONTENT_FOR_EXPENSE_SUBMISSION",
				"An expense is filed by @employeeName, please take necessary action");
		vAllEAll.put("CONTENT_FOR_EXPENSE_APPROVED",
				"An expense filed by @employeeName has been approved by respective Approver, please have a look and take necessary action");
		vAllEAll.put("CONTENT_FOR_EXPENSE_REJECTION",
				"An expense filed by you has been rejected by your approver, please have a look and take necessary action");

		// SQL queries
		vAllEAll.put("SQL_QUERY_FOR_GETTING_USERS_BY_MOBILE_OR_EMAIL_ID",
				"FROM ExternalFacingUser user where user.phoneNumber = :Mobile or user.email = :Email");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_USERS_BY_USER_ID",
				"FROM ExternalFacingUser user where user.userId = :UserId");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_EXPENSE_BY_ID", "FROM ExpenseEntity expense where expense.id = :ExpenseId");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_EXPENSE_BY_STATUS",
				"FROM ExpenseEntity expense where expense.status = :Status");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_USERS_BY_ID", "FROM ExternalFacingUser user where user.id = :Id");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_USER_ROLES_BY_ID",
				"FROM UserRoleEntity userRoles where userRoles.userId = :UserId");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_EXPENSE_BY_USER_ID",
				"FROM ExpenseEntity expense where expense.userId = :UserId and Date(expense.creationDate) >='@StartDate' and Date(expense.creationDate) <= '@EndDate' and expense.status = '@Status'");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_EXPENSE_BY_APPROVER",
				"FROM ExpenseEntity expense where expense.status in ('Submitted','Re_Submitted') and expense.userId in (select user.id from ExternalFacingUser user where user.approverId = @ApproverId)");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_EXPENSE_FOR_SUPER_APPROVER",
				"FROM ExpenseEntity expense where expense.status in ('Submitted','Re_Submitted')");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_FILE_MAPPING",
				"FROM FileMappingEntity mapping where mapping.fileId = :FileId");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_FILE_MAPPING_BY_EXPENSE_ID",
				"FROM FileMappingEntity mapping where mapping.expenseId = :ExpenseId and mapping.isActive = true");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_FILE_MAPPING_BY_LIST_OF_EXPENSE_IDS",
				"FROM FileMappingEntity mapping where mapping.expenseId in (:ExpenseIds) and mapping.isActive = true");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_USER_AMOUNTS",
				"SELECT ex.UserId , sum(ex.Amount) as TotalAmount, CONCAT(user.FirstName,' ',user.LastName) as Name FROM Expenses ex join User user on user.Id = ex.UserId where ex.Status = '@Status' group by ex.UserId");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_FILE_MAPPING_BY_ATTACHMENT_ID",
				"FROM FileMappingEntity mapping where mapping.attachmentId = :AttachmentId and mapping.isActive = true");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_FILE_DETAILS_BY_ATTACHMENT_ID",
				"FROM FileDetailsEntity details where details.attachmentId = :AttachmentId");
		vAllEAll.put("SQL_QUERY_FOR_ALL_ROLE_TYPES", "FROM RoleEntity role");
		vAllEAll.put("GET_ALL_ACTIVE_EXPENSES", "FROM ExpenseEntity expense");
		vAllEAll.put("SQL_QUERY_FOR_GETTING_FINANCE_USERS",
				"select user.Id as id, user.UserId as userId, user.EmailId as email, user.FirstName as firstName, user.LastName as lastName FROM User user join UserRoleMapping mapping on user.Id = mapping.UserId join Roles role on mapping.RoleId = role.Id where role.RoleName = '@Role'");

		return vAllEAll;

	}

	/**
	 * This method creates seed content
	 */
	private void createSeedContent() {
		// Storing seed content
		BoilerplateMap<String, String> contentMap = new BoilerplateMap<>();

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
	 * This method removes the members from set with the given key
	 * 
	 * @param key
	 *            The set key
	 * @param members
	 *            The members to delete
	 */
	public void srem(String key, String members) {
		Jedis jedis = null;
		try {
			jedis = this.getConnection();
			jedis.srem(key, members);
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
