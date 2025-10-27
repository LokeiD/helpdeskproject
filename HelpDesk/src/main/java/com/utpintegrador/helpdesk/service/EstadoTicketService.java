package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.EstadoTicket;
import com.utpintegrador.helpdesk.repository.EstadoTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un Servicio de Spring
public class EstadoTicketService {

    private final EstadoTicketRepository estadoTicketRepository;

    // 1. Inyectamos el repositorio
    @Autowired
    public EstadoTicketService(EstadoTicketRepository estadoTicketRepository) {
        this.estadoTicketRepository = estadoTicketRepository;
    }

    // --- Métodos CRUD básicos ---

    // Obtener todos los estados de ticket
    public List<EstadoTicket> obtenerTodosLosEstados() {
        return estadoTicketRepository.findAll();
    }

    // Obtener un estado por su ID
    public Optional<EstadoTicket> obtenerEstadoPorId(Integer id) {
        return estadoTicketRepository.findById(id);
    }

    // Guardar un estado (nuevo o actualizado)
    public EstadoTicket guardarEstado(EstadoTicket estadoTicket) {
        return estadoTicketRepository.save(estadoTicket);
    }

    // Eliminar un estado por su ID
    public void eliminarEstado(Integer id) {
        // Esta es otra tabla crítica que no deberías borrar
        // si está siendo usada por un DetalleTicket.
        estadoTicketRepository.deleteById(id);
    }
}