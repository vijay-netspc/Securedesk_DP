package com.securedesk.pattern.chain;

import com.securedesk.repository.EscalationHistoryRepository;
import org.springframework.stereotype.Component;

/**
 * Builds the fixed escalation chain:
 * Level1 -> Level2 -> NetworkSpecialist -> SecuritySpecialist
 */
@Component
public class EscalationChainBuilder {

    private final EscalationHistoryRepository escalationHistoryRepository;

    public EscalationChainBuilder(EscalationHistoryRepository escalationHistoryRepository) {
        this.escalationHistoryRepository = escalationHistoryRepository;
    }

    public SupportHandler buildChain() {
        SupportHandler level1 = new Level1SupportHandler(escalationHistoryRepository);
        SupportHandler level2 = new Level2SupportHandler(escalationHistoryRepository);
        SupportHandler networkSpecialist = new NetworkSpecialistHandler(escalationHistoryRepository);
        SupportHandler securitySpecialist = new SecuritySpecialistHandler(escalationHistoryRepository);

        level1.setNext(level2);
        level2.setNext(networkSpecialist);
        networkSpecialist.setNext(securitySpecialist);

        return level1;
    }
}
