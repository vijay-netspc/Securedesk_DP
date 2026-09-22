package com.securedesk.pattern.chain;

import com.securedesk.entity.Ticket;
import com.securedesk.enums.TicketCategory;
import com.securedesk.enums.SupportLevel;
import com.securedesk.repository.EscalationHistoryRepository;

/**
 * Handles routine Hardware / Software / Access issues. Anything already
 * flagged CRITICAL is considered beyond Level 1's scope and is passed on
 * immediately.
 */
public class Level1SupportHandler extends SupportHandler {

    public Level1SupportHandler(EscalationHistoryRepository repo) { super(repo); }

    @Override
    protected boolean canHandle(Ticket ticket) {
        boolean routineCategory = ticket.getCategory() == TicketCategory.HARDWARE
                || ticket.getCategory() == TicketCategory.SOFTWARE
                || ticket.getCategory() == TicketCategory.ACCESS;
        boolean notCritical = ticket.getPriority() == null
                || !ticket.getPriority().name().equals("CRITICAL");
        return routineCategory && notCritical;
    }

    @Override
    protected SupportLevel getLevel() { return SupportLevel.LEVEL1; }
}
