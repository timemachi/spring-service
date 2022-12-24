package com.springService.productservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomEvent {
    private String serviceName;
    private String actionName;
    private String eventCode;
    private final LocalDateTime date = LocalDateTime.now();
}