package com.securedesk.pattern.observer;

import com.securedesk.entity.Notification;
import com.securedesk.entity.Ticket;
import com.securedesk.repository.NotificationRepository;

/**
 * Notifies the support staff member currently assigned to the ticket
 * (e.g. when it is assigned or escalated to them).
 */
public class SupportStaffNotificationObserver implements TicketObserver {

    private final NotificationRepository notificationRepository;

    public SupportStaffNotificationObserver(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void update(Ticket ticket, String eventType, String message) {
        if (ticket.getAssignedTo() == null) return;
        Notification notification = new Notification(ticket, ticket.getAssignedTo(),
                "[" + eventType + "] " + message);
        notificationRepository.save(notification);
    }
}
