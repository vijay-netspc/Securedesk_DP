package com.securedesk.entity;

import com.securedesk.enums.SupportLevel;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "escalation_history")
public class EscalationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    private SupportLevel fromLevel;

    @Enumerated(EnumType.STRING)
    private SupportLevel toLevel;

    private String reason;

    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    public EscalationHistory() {}

    public EscalationHistory(Ticket ticket, SupportLevel fromLevel, SupportLevel toLevel, String reason) {
        this.ticket = ticket;
        this.fromLevel = fromLevel;
        this.toLevel = toLevel;
        this.reason = reason;
    }

    public Long getId() { return id; }
    public Ticket getTicket() { return ticket; }
    public SupportLevel getFromLevel() { return fromLevel; }
    public SupportLevel getToLevel() { return toLevel; }
    public String getReason() { return reason; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
