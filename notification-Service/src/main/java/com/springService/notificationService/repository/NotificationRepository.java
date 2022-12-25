package com.springService.notificationService.repository;

import com.springService.notificationService.event.CustomEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<CustomEvent, Long> {
}
