package com.securedesk.pattern.chain;

import com.securedesk.entity.EscalationHistory;
import com.securedesk.entity.Ticket;
import com.securedesk.enums.SupportLevel;
import com.securedesk.repository.EscalationHistoryRepository;

/**
 * CHAIN OF RESPONSIBILITY PATTERN
 * -------------------------------
 * Abstract handler in the escalation chain: Level1 -> Level2 ->
 * NetworkSpecialist -> SecuritySpecialist. Each concrete handler decides
 * whether it can resolve the ticket at its level. If it cannot, it
 * records an escalation entry and forwards the ticket to the next
 * handler in the chain instead of the caller needing to know the order
 * of support levels.
 */
public abstract class SupportHandler {

    protected SupportHandler nextHandler;
    protected final EscalationHistoryRepository escalationHistoryRepository;

    protected SupportHandler(EscalationHistoryRepository escalationHistoryRepository) {
        this.escalationHistoryRepository = escalationHistoryRepository;
    }

    public void setNext(SupportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public final void handle(Ticket ticket, String reason) {
        if (canHandle(ticket)) {
            ticket.setCurrentSupportLevel(getLevel());
            return;
        }
        if (nextHandler != null) {
            SupportLevel from = getLevel();
            SupportLevel to = nextHandler.getLevel();
            escalationHistoryRepository.save(new EscalationHistory(ticket, from, to, reason));
            ticket.setCurrentSupportLevel(to);
            nextHandler.handle(ticket, reason);
        } else {
            ticket.setCurrentSupportLevel(SupportLevel.UNRESOLVED);
        }
    }

    protected abstract boolean canHandle(Ticket ticket);

    protected abstract SupportLevel getLevel();

    /** Public accessor so calling code (outside this package) can identify this handler's tier. */
    public SupportLevel level() {
        return getLevel();
    }

    /** Public accessor so calling code can walk to the next handler in the chain. */
    public SupportHandler nextInChain() {
        return nextHandler;
    }
}
