package com.securedesk.pattern.strategy;

import com.securedesk.entity.Ticket;
import com.securedesk.enums.PriorityLevel;

/**
 * Algorithm used for Security tickets. These are always treated as at
 * least CRITICAL because of compliance/data-risk implications, regardless
 * of wording in the description.
 */
public class CriticalPriorityStrategy implements PriorityStrategy {

    @Override
    public PriorityLevel calculatePriority(Ticket ticket) {
        return PriorityLevel.CRITICAL;
    }
}
