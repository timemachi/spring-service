package com.springService.notificationService.controller;

import com.springService.notificationService.event.CustomEvent;
import com.springService.notificationService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/controller")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/all")
    public List<CustomEvent> getAllEvent() {
        return notificationService.getAllEvents();
    }
}
