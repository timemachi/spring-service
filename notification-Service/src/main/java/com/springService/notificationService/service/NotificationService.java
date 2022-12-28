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

    public List<CustomEvent> getEventsByServiceName(String serviceName) {
        return notificationRepository.findDistinctByServiceName(serviceName);
    }

    public void saveEvent(CustomEvent event) {
        notificationRepository.save(event);
    }
}
