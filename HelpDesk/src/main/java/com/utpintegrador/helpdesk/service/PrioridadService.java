package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Prioridad;
import com.utpintegrador.helpdesk.repository.PrioridadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un Servicio de Spring
public class PrioridadService {

    private final PrioridadRepository prioridadRepository;

    // 1. Inyectamos el repositorio
    @Autowired
    public PrioridadService(PrioridadRepository prioridadRepository) {
        this.prioridadRepository = prioridadRepository;
    }

    // --- Métodos CRUD básicos ---

    // Obtener todas las prioridades
    public List<Prioridad> obtenerTodasLasPrioridades() {
        return prioridadRepository.findAll();
    }

    // Obtener una prioridad por su ID
    public Optional<Prioridad> obtenerPrioridadPorId(Integer id) {
        return prioridadRepository.findById(id);
    }

    // Guardar una prioridad (nueva o actualizada)
    public Prioridad guardarPrioridad(Prioridad prioridad) {
        return prioridadRepository.save(prioridad);
    }

    // Eliminar una prioridad por su ID
    public void eliminarPrioridad(Integer id) {
        // Al igual que con las categorías, deberíamos validar
        // que no esté en uso por un Ticket antes de borrar.
        prioridadRepository.deleteById(id);
    }
}