package com.securedesk.pattern.strategy;

import com.securedesk.entity.Ticket;
import com.securedesk.enums.PriorityLevel;

/**
 * Default algorithm for low-impact categories (Hardware, Software, Access).
 * Only escalates to URGENT if the description clearly signals a blocking
 * issue; otherwise the ticket stays NORMAL.
 */
public class NormalPriorityStrategy implements PriorityStrategy {

    private static final String[] URGENT_KEYWORDS = {"not working", "urgent", "asap", "down", "stuck"};

    @Override
    public PriorityLevel calculatePriority(Ticket ticket) {
        String desc = safe(ticket.getDescription());
        for (String keyword : URGENT_KEYWORDS) {
            if (desc.contains(keyword)) {
                return PriorityLevel.URGENT;
            }
        }
        return PriorityLevel.NORMAL;
    }

    private String safe(String s) {
        return s == null ? "" : s.toLowerCase();
    }
}
