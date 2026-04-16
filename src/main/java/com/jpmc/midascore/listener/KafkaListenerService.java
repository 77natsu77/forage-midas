package com.jpmc.midascore;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import com.jpmc.midascore.foundation.Transaction;

@Component
public class KafkaListenerService {

    private List<Float> firstFourAmounts =
        Collections.synchronizedList(new ArrayList<>());

    @KafkaListener(topics = "trader-updates", groupId = "midas-group")
    public void listen(String message) {

        String[] parts = message.split(", ");
        float amount = Float.parseFloat(parts[2]);

        if (firstFourAmounts.size() < 4) {
            firstFourAmounts.add(amount);
            
        }
         System.out.println("-------------Received: " + amount);
    }

    public List<Float> getFirstFourAmounts() {
        return firstFourAmounts;
    }
}