package com.securedesk.pattern.observer;

import com.securedesk.entity.Ticket;

/**
 * OBSERVER PATTERN
 * ----------------
 * Any object that needs to react to ticket lifecycle events implements
 * this interface and registers itself with a TicketNotifier. The
 * service layer that changes the ticket never needs to know who is
 * listening, or how many listeners there are.
 */
public interface TicketObserver {
    void update(Ticket ticket, String eventType, String message);
}
