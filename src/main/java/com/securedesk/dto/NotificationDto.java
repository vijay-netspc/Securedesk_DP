package com.securedesk.dto;

import com.securedesk.entity.Notification;

import java.time.LocalDateTime;

public class NotificationDto {
    private Long id;
    private Long ticketId;
    private String ticketTitle;
    private String recipientName;
    private String message;
    private LocalDateTime createdAt;

    public static NotificationDto from(Notification n) {
        NotificationDto dto = new NotificationDto();
        dto.id = n.getId();
        dto.ticketId = n.getTicket() != null ? n.getTicket().getId() : null;
        dto.ticketTitle = n.getTicket() != null ? n.getTicket().getTitle() : null;
        dto.recipientName = n.getRecipient() != null ? n.getRecipient().getName() : null;
        dto.message = n.getMessage();
        dto.createdAt = n.getCreatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public Long getTicketId() { return ticketId; }
    public String getTicketTitle() { return ticketTitle; }
    public String getRecipientName() { return recipientName; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
