package com.securedesk.pattern.observer;

import com.securedesk.entity.Ticket;
import com.securedesk.repository.NotificationRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The Subject in the Observer pattern. The service layer calls
 * notifyObservers(...) exactly once per event; every registered observer
 * is invoked in turn.
 */
@Component
public class TicketNotifier {

    private final List<TicketObserver> observers = new ArrayList<>();

    public TicketNotifier(NotificationRepository notificationRepository) {
        observers.add(new EmployeeNotificationObserver(notificationRepository));
        observers.add(new SupportStaffNotificationObserver(notificationRepository));
    }

    public void registerObserver(TicketObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TicketObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Ticket ticket, String eventType, String message) {
        for (TicketObserver observer : observers) {
            observer.update(ticket, eventType, message);
        }
    }
}
