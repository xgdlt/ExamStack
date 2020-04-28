package com.examstack.scoremarker.compoent;

import java.io.IOException;

import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.examstack.common.daemon.AbstractDaemon;
import com.examstack.common.domain.exam.AnswerSheet;
import com.examstack.scoremarker.config.ScoreMarkConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import javax.annotation.PostConstruct;

/**
 * 
 * @author Ocelot
 *
 */
@Component
public class ScoreMarkerMain{
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreMarkerMain.class);

	@Autowired
	private QueueingConsumer consumer;


	private boolean waitingForMessage = false;

	@Autowired
	private ScoreCalcuService scoreCalcuService;
	
	@Autowired
	private ObjectMapper mapper;

	@PostConstruct
	public void init()  {
        LOGGER.info("ScoreMarker daemon begin to init.");

		new ConsumerThread().start();

		LOGGER.info("ScoreMarker daemon init done.");
	}

	private class ConsumerThread extends Thread{

        @Override
        public void run() {
            while (true) {
                try {
                    LOGGER.info("scoreMaker checking next delivery from message queue");
                    waitingForMessage = true;
                    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                    waitingForMessage = false;
                    AnswerSheet answerSheet = mapper.readValue(delivery.getBody(),
                            AnswerSheet.class);
                    scoreCalcuService.calcuScore(answerSheet);
                } catch (ShutdownSignalException e) {
                    LOGGER.error("scoreMaker received ShutdownSignalException: ", e);
                    try {
                        Thread.sleep(100);
                    } catch (Throwable ex) {
                        LOGGER.error("scoreMaker sleep exception: ", ex);
                    }
                } catch (IOException | ConsumerCancelledException | InterruptedException e) {
                    LOGGER.error("scoreMaker received exception", e);
                }
            }

        }
    }


}
