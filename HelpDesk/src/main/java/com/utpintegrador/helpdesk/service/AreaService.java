package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Area;
import com.utpintegrador.helpdesk.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un Servicio de Spring
public class AreaService {

    private final AreaRepository areaRepository;

    // 1. Inyectamos el repositorio que necesitamos
    @Autowired
    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    // --- Métodos CRUD básicos ---

    // Obtener todas las áreas
    public List<Area> obtenerTodasLasAreas() {
        return areaRepository.findAll();
    }

    // Obtener un área por su ID
    public Optional<Area> obtenerAreaPorId(Integer id) {
        return areaRepository.findById(id);
    }

    // Guardar un área (ya sea nueva o actualizada)
    public Area guardarArea(Area area) {
        // En este servicio simple, no hay lógica de negocio extra.
        // Simplemente guardamos.
        return areaRepository.save(area);
    }

    // Eliminar un área por su ID
    public void eliminarArea(Integer id) {
        // Aquí podríamos validar si el área está en uso por algún usuario
        // antes de permitir borrarla. Por ahora, la borramos directamente.
        areaRepository.deleteById(id);
    }
}