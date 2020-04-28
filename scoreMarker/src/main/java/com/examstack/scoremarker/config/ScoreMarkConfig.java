package com.examstack.scoremarker.config;

import com.examstack.common.Constants;
import com.examstack.common.domain.exam.ExamPaper;
import com.examstack.scoremarker.compoent.ScoreMarkerMain;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;

/**
 * 
 * @author Ocelot
 *
 */
@Configuration
@ComponentScan("com.examstack.scoremarker")
public class ScoreMarkConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScoreMarkerMain.class);

	@Value("${rabbitmq.host}")
	private String messageQueueHostname;
	
	@Value("${examstack.answersheet.posturi}")
	private String answerSheetPostUri;
	
	@Value("${examstack.exampaper.geturi}")
	private String examPaperGetUri;
	
//	private HashMap<String,ExamPaper> examPapersMap = new HashMap<String,ExamPaper>();;

	@Bean
	QueueingConsumer queueingConsumer() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(messageQueueHostname);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(Constants.ANSWERSHEET_DATA_QUEUE, true, false, false, null);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(Constants.ANSWERSHEET_DATA_QUEUE, true, consumer);
		return consumer;

	}
/*
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		String propertyFilePath = Constants.CONFIG_PATH + File.separator 	+ "config" + File.separator + "scoremaker.properties";
		File f = new File(propertyFilePath);
		if (!f.exists()){
			propertyFilePath = "config" + File.separator + "scoremaker.properties";
		}
		PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
		org.springframework.core.io.Resource[] properties = new FileSystemResource[] { new FileSystemResource(propertyFilePath) };
		propertySourcesPlaceholderConfigurer.setLocations(properties);
		return propertySourcesPlaceholderConfigurer;
	}*/

	@Bean
	ObjectMapper objectMapper()
	{
		return new ObjectMapper();
	}
	
	@Bean
	HashMap<String, ExamPaper> examPapersMap()
	{
		return new HashMap<String, ExamPaper>();
	}
	
	@Bean
	String answerSheetPostUri()
	{
		return answerSheetPostUri;
	}
	
	@Bean
	String examPaperGetUri()
	{
		return examPaperGetUri;
	}
	
	@Bean
	RestTemplate restTemplate()
	{
		return new RestTemplate();
	}
}
