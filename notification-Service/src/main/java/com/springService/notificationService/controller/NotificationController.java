package com.springService.notificationService.controller;

import com.springService.notificationService.event.CustomEvent;
import com.springService.notificationService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
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
