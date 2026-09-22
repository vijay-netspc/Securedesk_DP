package com.securedesk.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Column(length = 1000)
    private String message;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Notification() {}

    public Notification(Ticket ticket, User recipient, String message) {
        this.ticket = ticket;
        this.recipient = recipient;
        this.message = message;
    }

    public Long getId() { return id; }
    public Ticket getTicket() { return ticket; }
    public User getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
