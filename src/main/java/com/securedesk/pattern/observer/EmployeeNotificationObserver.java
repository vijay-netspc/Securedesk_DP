package com.securedesk.pattern.observer;

import com.securedesk.entity.Notification;
import com.securedesk.entity.Ticket;
import com.securedesk.repository.NotificationRepository;

/**
 * Notifies the employee who raised the ticket (e.g. when it is resolved
 * or closed).
 */
public class EmployeeNotificationObserver implements TicketObserver {

    private final NotificationRepository notificationRepository;

    public EmployeeNotificationObserver(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void update(Ticket ticket, String eventType, String message) {
        if (ticket.getCreatedBy() == null) return;
        Notification notification = new Notification(ticket, ticket.getCreatedBy(),
                "[" + eventType + "] " + message);
        notificationRepository.save(notification);
    }
}
