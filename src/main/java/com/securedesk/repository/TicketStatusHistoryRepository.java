package com.securedesk.repository;

import com.securedesk.entity.TicketStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketStatusHistoryRepository extends JpaRepository<TicketStatusHistory, Long> {
    List<TicketStatusHistory> findByTicketIdOrderByTimestampAsc(Long ticketId);
}
