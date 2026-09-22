package com.securedesk.dto;

import com.securedesk.entity.EscalationHistory;
import com.securedesk.enums.SupportLevel;

import java.time.LocalDateTime;

public class EscalationHistoryDto {
    private Long id;
    private SupportLevel fromLevel;
    private SupportLevel toLevel;
    private String reason;
    private LocalDateTime timestamp;

    public static EscalationHistoryDto from(EscalationHistory h) {
        EscalationHistoryDto dto = new EscalationHistoryDto();
        dto.id = h.getId();
        dto.fromLevel = h.getFromLevel();
        dto.toLevel = h.getToLevel();
        dto.reason = h.getReason();
        dto.timestamp = h.getTimestamp();
        return dto;
    }

    public Long getId() { return id; }
    public SupportLevel getFromLevel() { return fromLevel; }
    public SupportLevel getToLevel() { return toLevel; }
    public String getReason() { return reason; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
