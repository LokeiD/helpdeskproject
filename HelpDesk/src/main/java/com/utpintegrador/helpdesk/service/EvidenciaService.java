package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Evidencia;
import com.utpintegrador.helpdesk.model.Ticket;
import com.utpintegrador.helpdesk.repository.EvidenciaRepository;
import com.utpintegrador.helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // <-- Añadir import
import org.springframework.stereotype.Service;

import java.nio.file.Path; // <-- Añadir import
import java.nio.file.Paths; // <-- Añadir import
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EvidenciaService {

    private final EvidenciaRepository evidenciaRepository;
    private final TicketRepository ticketRepository;

    // --- ¡NUEVO! Inyectar la ruta base ---
    @Value("${app.upload.dir}")
    private String uploadRootDir;

    @Autowired
    public EvidenciaService(EvidenciaRepository evidenciaRepository, TicketRepository ticketRepository) {
        this.evidenciaRepository = evidenciaRepository;
        this.ticketRepository = ticketRepository;
    }

    // --- Métodos CRUD básicos (sin cambios) ---
    public List<Evidencia> obtenerTodasLasEvidencias() {
        return evidenciaRepository.findAll();
    }
    public Optional<Evidencia> obtenerEvidenciaPorId(Integer id) {
        return evidenciaRepository.findById(id);
    }


    // --- MÉTODO MODIFICADO ---
    /**
     * Crea el registro de Evidencia (la "carpeta" lógica) en la BD,
     * guardando la RUTA COMPLETA en Ruta_Evidencia.
     * @param nombreCarpeta El nombre de la subcarpeta (ej: "Ticket_101")
     * @param ticketId El ID del ticket asociado
     * @return La entidad Evidencia guardada
     */
    public Evidencia guardarEvidencia(String nombreCarpeta, Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con id: " + ticketId));

        // Construir la ruta completa
        // Usamos Paths.get().resolve().toString() para manejar correctamente las barras '/' o '\'
        Path rutaCompletaPath = Paths.get(uploadRootDir).resolve(nombreCarpeta);
        String rutaCompletaString = rutaCompletaPath.toString() + System.getProperty("file.separator"); // Asegura barra final

        Evidencia evidencia = new Evidencia();
        // Guardamos la RUTA COMPLETA
        evidencia.setRutaEvidencia(rutaCompletaString);
        evidencia.setTicket(ticket);
        evidencia.setFechaSubida(LocalDate.now());

        System.out.println("DEBUG (EvidenciaService): Guardando Evidencia con Ruta_Evidencia: " + rutaCompletaString); // Log para verificar
        return evidenciaRepository.save(evidencia);
    }


    public void eliminarEvidencia(Integer id) {
        // TODO: Borrar carpeta física y Detalles asociados
        evidenciaRepository.deleteById(id);
    }
}