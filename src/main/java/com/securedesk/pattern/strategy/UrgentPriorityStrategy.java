package com.securedesk.pattern.strategy;

import com.securedesk.entity.Ticket;
import com.securedesk.enums.PriorityLevel;

/**
 * Algorithm used for categories that affect multiple users or ongoing
 * business operations (e.g. Network). Escalates to CRITICAL when outage /
 * breach style keywords are present, otherwise stays URGENT.
 */
public class UrgentPriorityStrategy implements PriorityStrategy {

    private static final String[] CRITICAL_KEYWORDS = {"outage", "breach", "attack", "all users", "production down"};

    @Override
    public PriorityLevel calculatePriority(Ticket ticket) {
        String desc = safe(ticket.getDescription());
        for (String keyword : CRITICAL_KEYWORDS) {
            if (desc.contains(keyword)) {
                return PriorityLevel.CRITICAL;
            }
        }
        return PriorityLevel.URGENT;
    }

    private String safe(String s) {
        return s == null ? "" : s.toLowerCase();
    }
}
