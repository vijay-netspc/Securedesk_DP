package com.securedesk.pattern.chain;

import com.securedesk.entity.Ticket;
import com.securedesk.enums.TicketCategory;
import com.securedesk.enums.SupportLevel;
import com.securedesk.repository.EscalationHistoryRepository;

/** Handles all Network category tickets, including full outages. */
public class NetworkSpecialistHandler extends SupportHandler {

    public NetworkSpecialistHandler(EscalationHistoryRepository repo) { super(repo); }

    @Override
    protected boolean canHandle(Ticket ticket) {
        return ticket.getCategory() == TicketCategory.NETWORK;
    }

    @Override
    protected SupportLevel getLevel() { return SupportLevel.NETWORK_SPECIALIST; }
}
