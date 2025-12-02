package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.Ticket;
import com.utpintegrador.helpdesk.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    long count();

    long countByEstado(boolean estado);

    long countByUsuario(Usuario usuario);

    long countByUsuarioAndEstado(Usuario usuario, boolean estado);

    List<Ticket> findByUsuario_CodigoUsuario(Integer usuarioId);

}