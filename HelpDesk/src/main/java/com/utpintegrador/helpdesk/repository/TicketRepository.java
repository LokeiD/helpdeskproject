package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.Ticket;
import com.utpintegrador.helpdesk.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    // Más adelante:
    // List<Ticket> findByUsuario(Usuario usuario);
    // List<Ticket> findByEstado(boolean estado);
    // Contar todos los tickets (para el admin)
    long count();

    // Contar por estado (para el admin)
    long countByEstado(boolean estado); // true=Abierto, false=Cerrado

    // Contar todos los tickets de un usuario
    long countByUsuario(Usuario usuario);

    // Contar por estado Y usuario (para el usuario)
    long countByUsuarioAndEstado(Usuario usuario, boolean estado);

    // (Para los gráficos y calendario, necesitaremos consultas más complejas después)
}