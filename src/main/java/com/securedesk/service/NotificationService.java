package com.securedesk.service;

import com.securedesk.dto.NotificationDto;
import com.securedesk.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationDto> getForUser(Long userId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationDto::from)
                .collect(Collectors.toList());
    }
}
