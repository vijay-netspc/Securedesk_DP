package com.securedesk.pattern.strategy;

import com.securedesk.entity.Ticket;
import com.securedesk.enums.PriorityLevel;

/**
 * STRATEGY PATTERN
 * ----------------
 * Each implementation encapsulates a distinct algorithm for deciding a
 * ticket's priority. The algorithm can be swapped at runtime by the
 * PriorityContext without changing the calling code.
 */
public interface PriorityStrategy {
    PriorityLevel calculatePriority(Ticket ticket);
}
