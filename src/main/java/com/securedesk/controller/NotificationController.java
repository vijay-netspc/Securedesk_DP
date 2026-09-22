package com.securedesk.controller;

import com.securedesk.dto.NotificationDto;
import com.securedesk.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDto>> getForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getForUser(userId));
    }
}
