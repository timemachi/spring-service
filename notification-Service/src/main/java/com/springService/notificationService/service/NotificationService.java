package com.springService.notificationService.service;

import com.springService.notificationService.event.CustomEvent;
import com.springService.notificationService.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<CustomEvent> getAllEvents() {
        return notificationRepository.findAll();
    }
}
