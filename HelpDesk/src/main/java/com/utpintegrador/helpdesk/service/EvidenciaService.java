package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Evidencia;
import com.utpintegrador.helpdesk.model.Ticket;
import com.utpintegrador.helpdesk.repository.EvidenciaRepository;
import com.utpintegrador.helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un Servicio de Spring
public class EvidenciaService {

    // 1. Necesitamos ambos repositorios
    private final EvidenciaRepository evidenciaRepository;
    private final TicketRepository ticketRepository;

    // 2. Inyectamos los repositorios
    @Autowired
    public EvidenciaService(EvidenciaRepository evidenciaRepository, TicketRepository ticketRepository) {
        this.evidenciaRepository = evidenciaRepository;
        this.ticketRepository = ticketRepository;
    }

    // --- Métodos CRUD básicos ---

    public List<Evidencia> obtenerTodasLasEvidencias() {
        return evidenciaRepository.findAll();
    }

    public Optional<Evidencia> obtenerEvidenciaPorId(Integer id) {
        return evidenciaRepository.findById(id);
    }

    // --- Método con Lógica de Negocio ---

    /**
     * Guarda una nueva evidencia, asociándola a un ticket existente.
     * Aquí 'rutaEvidencia' sería el path del archivo guardado en el servidor.
     */
    public Evidencia guardarEvidencia(String rutaEvidencia, Integer ticketId) {

        // 1. Lógica de Validación: Buscar el Ticket padre
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con id: " + ticketId));

        // 2. Lógica de Negocio: Crear la nueva entidad Evidencia
        Evidencia evidencia = new Evidencia();
        evidencia.setRutaEvidencia(rutaEvidencia);
        evidencia.setTicket(ticket);
        evidencia.setFechaSubida(LocalDate.now()); // Establece la fecha actual

        // 3. Guardar en la BD
        return evidenciaRepository.save(evidencia);
    }

    public void eliminarEvidencia(Integer id) {
        // (Aquí también iría la lógica para borrar el archivo físico del servidor)
        evidenciaRepository.deleteById(id);
    }
}