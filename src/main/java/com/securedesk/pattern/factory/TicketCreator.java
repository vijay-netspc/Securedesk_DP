package com.securedesk.pattern.factory;

import com.securedesk.entity.Ticket;
import com.securedesk.entity.User;
import com.securedesk.enums.SupportLevel;
import com.securedesk.enums.TicketCategory;
import com.securedesk.enums.TicketStatusEnum;

/**
 * FACTORY METHOD PATTERN
 * ----------------------
 * Abstract creator. Each ticket category has its own concrete creator that
 * knows how to build a correctly-initialized Ticket object: the category,
 * the default first-line support level it should be routed to, and any
 * category-specific defaults. This keeps ticket-creation rules out of the
 * service/controller layer and makes adding a new ticket type a matter of
 * adding one new class, not editing existing switch/if-else logic.
 */
public abstract class TicketCreator {

    public Ticket createTicket(String title, String description, User createdBy) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setCreatedBy(createdBy);
        ticket.setCategory(getCategory());
        ticket.setStatus(TicketStatusEnum.OPEN);
        ticket.setCurrentSupportLevel(getInitialSupportLevel());
        return ticket;
    }

    protected abstract TicketCategory getCategory();

    protected abstract SupportLevel getInitialSupportLevel();
}
