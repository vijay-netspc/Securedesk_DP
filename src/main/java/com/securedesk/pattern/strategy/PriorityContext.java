package com.securedesk.pattern.strategy;

import com.securedesk.entity.Ticket;
import com.securedesk.enums.PriorityLevel;
import com.securedesk.enums.TicketCategory;
import org.springframework.stereotype.Component;

/**
 * Context class for the Strategy pattern. Chooses which PriorityStrategy
 * to run based on the ticket's category, then delegates to it. Swapping
 * the mapping below is the only change needed to alter business rules,
 * no code that calls this class needs to change.
 */
@Component
public class PriorityContext {

    public PriorityLevel calculate(Ticket ticket) {
        PriorityStrategy strategy = resolveStrategy(ticket.getCategory());
        return strategy.calculatePriority(ticket);
    }

    private PriorityStrategy resolveStrategy(TicketCategory category) {
        return switch (category) {
            case SECURITY -> new CriticalPriorityStrategy();
            case NETWORK -> new UrgentPriorityStrategy();
            case HARDWARE, SOFTWARE, ACCESS -> new NormalPriorityStrategy();
        };
    }
}
