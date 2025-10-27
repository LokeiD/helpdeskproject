package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.*;
import com.utpintegrador.helpdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; // Asegúrate de importar esto
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final SubCategoriaRepository subCategoriaRepository;

    // --- ¡AÑADE ESTE REPOSITORIO! ---
    private final PrioridadRepository prioridadRepository;

    // --- ¡MODIFICA EL CONSTRUCTOR! ---
    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         UsuarioRepository usuarioRepository,
                         SubCategoriaRepository subCategoriaRepository,
                         PrioridadRepository prioridadRepository) { // ¡Añádelo!
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.subCategoriaRepository = subCategoriaRepository;
        this.prioridadRepository = prioridadRepository; // ¡Añádelo!
    }

    // --- MÉTODO CON LÓGICA DE NEGOCIO MODIFICADO ---
    /**
     * Crea un nuevo ticket desde el formulario.
     * Busca las entidades relacionadas (Usuario, SubCategoria, Prioridad)
     * y guarda el ticket principal.
     */
    public Ticket crearTicket(Integer usuId, Integer subCatId, Integer prioId, String titulo) {

        // 1. Lógica de Validación: Buscar las entidades relacionadas
        Usuario usuario = usuarioRepository.findById(usuId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuId));

        SubCategoria subCategoria = subCategoriaRepository.findById(subCatId)
                .orElseThrow(() -> new RuntimeException("SubCategoría no encontrada con id: " + subCatId));

        Prioridad prioridad = prioridadRepository.findById(prioId)
                .orElseThrow(() -> new RuntimeException("Prioridad no encontrada con id: " + prioId));

        // 2. Lógica de Negocio: Armar el nuevo ticket
        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setTitulo(titulo);
        nuevoTicket.setUsuario(usuario);
        nuevoTicket.setSubCategoria(subCategoria);
        nuevoTicket.setPrioridad(prioridad); // ¡Asignamos la prioridad!

        // 3. Lógica de Negocio: Establecer valores por defecto para el Ticket
        nuevoTicket.setFechaCreacion(LocalDateTime.now());
        nuevoTicket.setEstado(true); // true = Abierto (Activo)
        // Fecha_Cierre queda nulo

        // 4. Guardar en la BD
        return ticketRepository.save(nuevoTicket);
    }

    // --- Métodos más simples (Sin cambios) ---

    public List<Ticket> obtenerTodosLosTickets() {
        return ticketRepository.findAll();
    }

    public Ticket obtenerTicketPorId(Integer id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con id: " + id));
    }

    // (Aquí irían más métodos: asignarTecnico, cerrarTicket, añadirComentario, etc.)
}
