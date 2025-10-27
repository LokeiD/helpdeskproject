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
    public DetalleEvidencia guardarDetalleEvidencia(String nombreDetalle, Integer evidenciaId) {

        // 1. Lógica de Validación: Buscar la Evidencia padre
        Evidencia evidencia = evidenciaRepository.findById(evidenciaId)
                .orElseThrow(() -> new RuntimeException("Evidencia no encontrada con id: " + evidenciaId));

        // 2. Lógica de Negocio: Crear la nueva entidad
        DetalleEvidencia detalle = new DetalleEvidencia();
        detalle.setNombre(nombreDetalle);
        detalle.setEvidencia(evidencia);

        // 3. Guardar en la BD
        return detalleEvidenciaRepository.save(detalle);
    }

    public void eliminarDetalleEvidencia(Integer id) {
        detalleEvidenciaRepository.deleteById(id);
    }
}