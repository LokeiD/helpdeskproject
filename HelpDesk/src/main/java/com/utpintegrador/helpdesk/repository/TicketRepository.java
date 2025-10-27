package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    // Más adelante:
    // List<Ticket> findByUsuario(Usuario usuario);
    // List<Ticket> findByEstado(boolean estado);
}