package com.boilerplate.queue;

import javax.jms.Connection;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ApacheMQ implements IQueue{

	private static ApacheMQ apacheMQ = null;
	Connection connection = null;
	
	private ApacheMQ(){

		ActiveMQConnectionFactory activeMQConnectionFactory =
				new ActiveMQConnectionFactory("vm://localhost");
	}
	
	public static ApacheMQ getInstance(){
		return ApacheMQ.apacheMQ;
	}
	
	@Override
	public <T> void insert(String subject, T item) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T remove(String subject, int timeoutInMilliSeconds)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T remove(String subject) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isQueueEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetQueueErrorCount() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getQueueSize(String string) {
		// TODO Auto-generated method stub
		return 0;
	}

}
