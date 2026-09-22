package com.securedesk.dto;

import com.securedesk.entity.TicketStatusHistory;
import com.securedesk.enums.TicketStatusEnum;

import java.time.LocalDateTime;

public class TicketStatusHistoryDto {
    private Long id;
    private TicketStatusEnum fromStatus;
    private TicketStatusEnum toStatus;
    private LocalDateTime timestamp;

    public static TicketStatusHistoryDto from(TicketStatusHistory h) {
        TicketStatusHistoryDto dto = new TicketStatusHistoryDto();
        dto.id = h.getId();
        dto.fromStatus = h.getFromStatus();
        dto.toStatus = h.getToStatus();
        dto.timestamp = h.getTimestamp();
        return dto;
    }

    public Long getId() { return id; }
    public TicketStatusEnum getFromStatus() { return fromStatus; }
    public TicketStatusEnum getToStatus() { return toStatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
