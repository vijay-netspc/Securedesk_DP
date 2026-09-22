package com.securedesk.entity;

import com.securedesk.enums.TicketStatusEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_status_history")
public class TicketStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    private TicketStatusEnum fromStatus;

    @Enumerated(EnumType.STRING)
    private TicketStatusEnum toStatus;

    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    public TicketStatusHistory() {}

    public TicketStatusHistory(Ticket ticket, TicketStatusEnum fromStatus, TicketStatusEnum toStatus) {
        this.ticket = ticket;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }

    public Long getId() { return id; }
    public Ticket getTicket() { return ticket; }
    public TicketStatusEnum getFromStatus() { return fromStatus; }
    public TicketStatusEnum getToStatus() { return toStatus; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
