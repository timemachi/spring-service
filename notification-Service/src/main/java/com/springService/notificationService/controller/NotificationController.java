package com.springService.notificationService.controller;

import com.springService.notificationService.event.CustomEvent;
import com.springService.notificationService.service.NotificationService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.adapter.BatchToRecordAdapter;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/all")
    public List<CustomEvent> getAllEvent() {
        return notificationService.getAllEvents();
    }

    @GetMapping("/{serviceName}")
    public List<CustomEvent> getEventByService(@PathVariable String service) {
        return notificationService.getEventsByServiceName(service);
    }
}

class DemoProducerCallBack implements Callback {

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        
    }
}
