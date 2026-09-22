package com.securedesk.pattern.chain;

import com.securedesk.entity.Ticket;
import com.securedesk.enums.TicketCategory;
import com.securedesk.enums.SupportLevel;
import com.securedesk.repository.EscalationHistoryRepository;

/**
 * Handles harder Hardware/Software/Access issues that Level 1 could not
 * resolve, plus general Network issues that are not yet full outages.
 */
public class Level2SupportHandler extends SupportHandler {

    public Level2SupportHandler(EscalationHistoryRepository repo) { super(repo); }

    @Override
    protected boolean canHandle(Ticket ticket) {
        boolean escalatedGeneral = ticket.getCategory() == TicketCategory.HARDWARE
                || ticket.getCategory() == TicketCategory.SOFTWARE
                || ticket.getCategory() == TicketCategory.ACCESS;
        boolean minorNetwork = ticket.getCategory() == TicketCategory.NETWORK
                && ticket.getPriority() != null
                && ticket.getPriority().name().equals("URGENT");
        return escalatedGeneral || minorNetwork;
    }

    @Override
    protected SupportLevel getLevel() { return SupportLevel.LEVEL2; }
}
