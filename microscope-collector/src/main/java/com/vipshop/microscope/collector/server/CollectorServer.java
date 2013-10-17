package com.vipshop.microscope.collector.server;

import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vipshop.microscope.collector.consumer.MessageConsumerExecutor;
import com.vipshop.microscope.collector.thrift.ThriftServerExecutor;
import com.vipshop.microscope.hbase.repository.Repositorys;

public class CollectorServer implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(CollectorServer.class);
	
	public static void main(String[] args) throws TTransportException {
		
		Repositorys.init();
		
		logger.info("start message consumer thread pool, size " + CollectorConstant.CONSUMER_POOL_SIZE);
		MessageConsumerExecutor.startMessageConsumer();
		
		logger.info("start collector server on port: " + CollectorConstant.COLLECTOR_PORT);
		ThriftServerExecutor.startNonBlockingServer();

	}
	
	@Override
	public void run() {
		
		logger.info("start message consumer thread pool");
		MessageConsumerExecutor.startMessageConsumer();
		
		logger.info("start collector server on port: " + CollectorConstant.COLLECTOR_PORT);
		try {
			ThriftServerExecutor.startNonBlockingServer();
		} catch (TTransportException e) {
		}
	}
	
}
