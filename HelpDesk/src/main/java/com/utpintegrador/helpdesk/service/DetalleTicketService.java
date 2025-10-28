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

        // 1. Validar Ticket y Estado (siguen siendo obligatorios)
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con id: " + ticketId));
        EstadoTicket estado = estadoTicketRepository.findById(estadoId)
                .orElseThrow(() -> new RuntimeException("Estado de Ticket no encontrado con id: " + estadoId));

        // 2. Validar Usuario SÓLO si usuarioId NO es null
        Usuario usuarioAsignado = null; // Por defecto es null
        if (usuarioId != null) {
            usuarioAsignado = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario (técnico) no encontrado con id: " + usuarioId));
        }

        // 3. Crear la nueva entrada de bitácora
        DetalleTicket detalle = new DetalleTicket();
        detalle.setDescripcion(descripcion);
        detalle.setTicket(ticket);
        detalle.setEstadoTicket(estado);
        detalle.setUsuario(usuarioAsignado); // Será null si usuarioId fue null

        // 4. Establecer Fecha_Asignacion SÓLO si hay un usuario asignado
        if (usuarioAsignado != null) {
            detalle.setFechaAsignacion(LocalDate.now()); // Fecha en que se asigna
        } else {
            detalle.setFechaAsignacion(null); // Aseguramos que sea null si no hay técnico
        }

        // 5. TODO: Lógica Adicional (actualizar estado del Ticket principal)
        // ... (Tu lógica para cerrar el ticket si el estado es "Solucionado", etc.) ...

        // 6. Guardar el nuevo detalle
        return detalleTicketRepository.save(detalle);
    }
}