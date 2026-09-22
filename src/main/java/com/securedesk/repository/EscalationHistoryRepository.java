package com.securedesk.repository;

import com.securedesk.entity.EscalationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EscalationHistoryRepository extends JpaRepository<EscalationHistory, Long> {
    List<EscalationHistory> findByTicketIdOrderByTimestampAsc(Long ticketId);
}
