package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.*;
import com.utpintegrador.helpdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    // ¡Necesitamos 3 repositorios para crear un ticket!
    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final SubCategoriaRepository subCategoriaRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         UsuarioRepository usuarioRepository,
                         SubCategoriaRepository subCategoriaRepository) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.subCategoriaRepository = subCategoriaRepository;
    }

    // --- MÉTODOS CON LÓGICA DE NEGOCIO ---

    /**
     * Crea un nuevo ticket.
     * Recibe los datos "sueltos" del controlador, aplica la lógica de negocio
     * y arma la entidad Ticket para guardarla.
     */
    public Ticket crearNuevoTicket(String titulo, Integer usuarioId, Integer subCategoriaId) {

        // 1. Lógica de Validación: Buscar las entidades relacionadas
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        SubCategoria subCategoria = subCategoriaRepository.findById(subCategoriaId)
                .orElseThrow(() -> new RuntimeException("SubCategoría no encontrada con id: " + subCategoriaId));

        // 2. Lógica de Negocio: Armar el nuevo ticket
        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setTitulo(titulo);
        nuevoTicket.setUsuario(usuario); // Asignamos el objeto completo
        nuevoTicket.setSubCategoria(subCategoria); // Asignamos el objeto completo

        // 3. Lógica de Negocio: Establecer valores por defecto
        nuevoTicket.setFechaCreacion(LocalDateTime.now()); // ¡Regla de negocio!
        nuevoTicket.setEstado(true); // ¡Regla de negocio! (true = Activo)

        // El resto de campos (Fecha_Cierre, Prioridad) quedan nulos, como debe ser.

        // 4. Guardar en la BD
        return ticketRepository.save(nuevoTicket);
    }

    // --- Métodos más simples ---

    public List<Ticket> obtenerTodosLosTickets() {
        return ticketRepository.findAll();
    }

    public Ticket obtenerTicketPorId(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con id: " + id));
    }

    // (Aquí irían más métodos: asignarTecnico, cerrarTicket, añadirComentario, etc.)
}