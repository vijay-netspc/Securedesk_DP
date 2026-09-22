package com.securedesk.dto;

import com.securedesk.entity.Ticket;
import com.securedesk.enums.PriorityLevel;
import com.securedesk.enums.SupportLevel;
import com.securedesk.enums.TicketCategory;
import com.securedesk.enums.TicketStatusEnum;

import java.time.LocalDateTime;

public class TicketResponseDto {
    private Long id;
    private String title;
    private String description;
    private TicketCategory category;
    private PriorityLevel priority;
    private TicketStatusEnum status;
    private SupportLevel currentSupportLevel;
    private String createdByName;
    private String assignedToName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TicketResponseDto from(Ticket t) {
        TicketResponseDto dto = new TicketResponseDto();
        dto.id = t.getId();
        dto.title = t.getTitle();
        dto.description = t.getDescription();
        dto.category = t.getCategory();
        dto.priority = t.getPriority();
        dto.status = t.getStatus();
        dto.currentSupportLevel = t.getCurrentSupportLevel();
        dto.createdByName = t.getCreatedBy() != null ? t.getCreatedBy().getName() : null;
        dto.assignedToName = t.getAssignedTo() != null ? t.getAssignedTo().getName() : null;
        dto.createdAt = t.getCreatedAt();
        dto.updatedAt = t.getUpdatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TicketCategory getCategory() { return category; }
    public PriorityLevel getPriority() { return priority; }
    public TicketStatusEnum getStatus() { return status; }
    public SupportLevel getCurrentSupportLevel() { return currentSupportLevel; }
    public String getCreatedByName() { return createdByName; }
    public String getAssignedToName() { return assignedToName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
