package com.springService.notificationService.repository;

import com.springService.notificationService.event.CustomEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<CustomEvent, Long> {

    List<CustomEvent> findDistinctByServiceName(String serviceName);


}
