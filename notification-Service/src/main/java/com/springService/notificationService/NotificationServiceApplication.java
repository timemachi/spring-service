package com.springService.notificationService;

import com.springService.notificationService.event.CustomEvent;
import com.springService.notificationService.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class NotificationServiceApplication {

	@Autowired
	NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

	@KafkaListener(topics = "notificationTopic")
	public void handleNotification(CustomEvent event){
		log.info("Received Notification for {} -{} -{}", event.getServiceName(), event.getActionName(), event.getEventCode());
		notificationService.saveEvent(event);
	}
}
