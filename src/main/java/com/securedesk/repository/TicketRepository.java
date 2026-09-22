package com.securedesk.repository;

import com.securedesk.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByAssignedToId(Long userId);
    List<Ticket> findByCreatedById(Long userId);
}
