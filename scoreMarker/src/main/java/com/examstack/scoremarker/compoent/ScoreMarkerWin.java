package com.examstack.scoremarker.compoent;

import com.examstack.common.domain.exam.AnswerSheet;
import com.examstack.scoremarker.config.ScoreMarkConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;


public class ScoreMarkerWin {

    private static boolean stop = false;

    @Autowired
    private QueueingConsumer consumer;
    @Autowired
    private ScoreCalcuService scoreCalcuService;
    @Autowired
    private ObjectMapper mapper;
    private static boolean waitingForMessage = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreMarkerWin.class);

    @PostConstruct
    public void init() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ScoreMarkConfig.class);
        context.refresh();
        scoreCalcuService = context.getBean(ScoreCalcuService.class);
        consumer = context.getBean(QueueingConsumer.class);
        mapper = context.getBean(ObjectMapper.class);
        LOGGER.info("ScoreMarker daemon init done.");
        run();

    }

    public ScoreMarkerWin() {

    }

    public static void start(String[] args) {


        ScoreMarkerWin win = new ScoreMarkerWin();
        win.init();
        System.out.println("start");
        Thread thread = new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                win.run();
            }
        };
        thread.start();
    }

    public static void stop(String[] args) {
        System.out.println("stop");
        stop = true;
        System.exit(0);
    }

/*    public static void main(String[] args) {
        ScoreMarkerWin scoreMarkerWin = new ScoreMarkerWin();
        scoreMarkerWin.init();
        scoreMarkerWin.run();
    }*/


    protected void run() {
        while (!stop) {
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
