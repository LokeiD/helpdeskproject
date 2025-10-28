package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.DetalleEvidencia;
import com.utpintegrador.helpdesk.model.Evidencia;
import com.utpintegrador.helpdesk.repository.DetalleEvidenciaRepository;
import com.utpintegrador.helpdesk.repository.EvidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un Servicio de Spring
public class DetalleEvidenciaService {

    // 1. Necesitamos ambos repositorios
    private final DetalleEvidenciaRepository detalleEvidenciaRepository;
    private final EvidenciaRepository evidenciaRepository;

    // 2. Inyectamos los repositorios
    @Autowired
    public DetalleEvidenciaService(DetalleEvidenciaRepository detalleEvidenciaRepository,
                                   EvidenciaRepository evidenciaRepository) {
        this.detalleEvidenciaRepository = detalleEvidenciaRepository;
        this.evidenciaRepository = evidenciaRepository;
    }

    // --- Métodos CRUD básicos ---

    public List<DetalleEvidencia> obtenerTodosLosDetalles() {
        return detalleEvidenciaRepository.findAll();
    }

    public Optional<DetalleEvidencia> obtenerDetallePorId(Integer id) {
        return detalleEvidenciaRepository.findById(id);
    }

    // --- Método con Lógica de Negocio ---

    /**
     * Guarda un nuevo detalle de evidencia, asociándolo a una evidencia existente.
     */
    public DetalleEvidencia guardarDetalleArchivo(String nombreArchivoUnico, Integer evidenciaId) {
        Evidencia evidencia = evidenciaRepository.findById(evidenciaId)
                .orElseThrow(() -> new RuntimeException("Evidencia no encontrada con id: " + evidenciaId));

        DetalleEvidencia detalle = new DetalleEvidencia();
        // Guardamos el nombre único del archivo (ej: uuid-aleatorio.jpg) como Nombre
        detalle.setNombre(nombreArchivoUnico);
        detalle.setEvidencia(evidencia);

        return detalleEvidenciaRepository.save(detalle);
    }

    public void eliminarDetalleEvidencia(Integer id) {
        // TODO: Al eliminar un detalle, también deberíamos borrar el archivo físico asociado.
        detalleEvidenciaRepository.deleteById(id);
    }
}