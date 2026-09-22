package com.securedesk.pattern.chain;

import com.securedesk.entity.Ticket;
import com.securedesk.enums.TicketCategory;
import com.securedesk.enums.SupportLevel;
import com.securedesk.repository.EscalationHistoryRepository;

/** Final link in the chain - handles all Security incidents. */
public class SecuritySpecialistHandler extends SupportHandler {

    public SecuritySpecialistHandler(EscalationHistoryRepository repo) { super(repo); }

    @Override
    protected boolean canHandle(Ticket ticket) {
        return ticket.getCategory() == TicketCategory.SECURITY;
    }

    @Override
    protected SupportLevel getLevel() { return SupportLevel.SECURITY_SPECIALIST; }
}
