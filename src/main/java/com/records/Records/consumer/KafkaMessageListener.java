package com.records.Records.consumer;

import com.records.Records.model.KafkaUserData;
import com.records.Records.service.SendNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageListener {
    Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    @Autowired
    private SendNotification notificationService;

    @KafkaListener(topics = "test",groupId = "consumer-group", containerFactory = "consumerListener")
    public void consumeEvents(KafkaUserData userData) {
        log.info("consumer consume the events {} ", userData.getEmail());
        notificationService.sendNotification(userData);
    }
}
