package com.jpmc.midascore.listener;

import com.jpmc.midascore.TransactionProcessingService;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final TransactionProcessingService service;

    public KafkaConsumer(TransactionProcessingService service) {
        this.service = service;
    }
    
    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core")
    public void listen(Transaction transaction) {
        service.processTransaction(transaction);
        //System.out.println("Received transaction with amount: " + transaction.getAmount());
    }
}