package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.*;
import com.utpintegrador.helpdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service // Marca esta clase como un Servicio de Spring
public class DetalleTicketService {

    // 1. Necesitamos 4 repositorios
    private final DetalleTicketRepository detalleTicketRepository;
    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoTicketRepository estadoTicketRepository;

    // 2. Inyectamos todos
    @Autowired
    public DetalleTicketService(DetalleTicketRepository detalleTicketRepository,
                                TicketRepository ticketRepository,
                                UsuarioRepository usuarioRepository,
                                EstadoTicketRepository estadoTicketRepository) {
        this.detalleTicketRepository = detalleTicketRepository;
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadoTicketRepository = estadoTicketRepository;
    }

    // --- Métodos CRUD básicos ---

    public List<DetalleTicket> obtenerTodosLosDetalles() {
        return detalleTicketRepository.findAll();
    }

    // (Podríamos necesitar un método para ver todos los detalles DE UN ticket)
    // public List<DetalleTicket> obtenerDetallesPorTicket(Integer ticketId) {
    //    ...
    // }

    // --- Método con Lógica de Negocio ---

    /**
     * Crea una nueva entrada en la bitácora del ticket (asigna un técnico,
     * registra un avance, cambia el estado, etc.)
     */
    public DetalleTicket crearDetalleTicket(String descripcion, Integer ticketId, Integer usuarioId, Integer estadoId) {

        // 1. Lógica de Validación: Validar que todas las partes existan
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con id: " + ticketId));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario (técnico) no encontrado con id: " + usuarioId));

        EstadoTicket estado = estadoTicketRepository.findById(estadoId)
                .orElseThrow(() -> new RuntimeException("Estado de Ticket no encontrado con id: " + estadoId));

        // 2. Lógica de Negocio: Crear la nueva entrada de bitácora
        DetalleTicket detalle = new DetalleTicket();
        detalle.setDescripcion(descripcion);
        detalle.setFechaAsignacion(LocalDate.now()); // Fecha en que se hace el registro

        // 3. Asignar las entidades relacionadas
        detalle.setTicket(ticket);
        detalle.setUsuario(usuario);
        detalle.setEstadoTicket(estado);

        // 4. TODO: Lógica de Negocio Adicional
        // Aquí es donde también deberíamos actualizar el estado principal del TICKET.
        // Por ejemplo, si el 'estado.getNombreEstado()' es "Resuelto" o "Cerrado",
        // deberíamos actualizar 'ticket.setEstado(false)' y 'ticket.setFechaCierre(LocalDateTime.now())'
        // y luego guardar el 'ticket' con ticketRepository.save(ticket).

        // 5. Guardar el nuevo detalle
        return detalleTicketRepository.save(detalle);
    }
}