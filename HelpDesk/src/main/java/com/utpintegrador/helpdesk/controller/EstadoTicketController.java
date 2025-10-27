package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.EstadoTicket;
import com.utpintegrador.helpdesk.service.EstadoTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estados-ticket") // URL base para estados de ticket
public class EstadoTicketController {

    private final EstadoTicketService estadoTicketService;

    // 1. Inyectamos el servicio
    @Autowired
    public EstadoTicketController(EstadoTicketService estadoTicketService) {
        this.estadoTicketService = estadoTicketService;
    }

    // ENDPOINT: Obtener todos los estados
    // GET http://localhost:8080/api/estados-ticket
    @GetMapping
    public List<EstadoTicket> obtenerTodos() {
        return estadoTicketService.obtenerTodosLosEstados();
    }

    // ENDPOINT: Obtener un estado por ID
    // GET http://localhost:8080/api/estados-ticket/101
    @GetMapping("/{id}")
    public ResponseEntity<EstadoTicket> obtenerPorId(@PathVariable Integer id) {
        Optional<EstadoTicket> estado = estadoTicketService.obtenerEstadoPorId(id);

        return estado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT: Crear un nuevo estado
    // POST http://localhost:8080/api/estados-ticket
    // Body (JSON): { "nombreEstado": "En Proceso" }
    @PostMapping
    public ResponseEntity<EstadoTicket> crearEstado(@RequestBody EstadoTicket estadoTicket) {
        EstadoTicket nuevoEstado = estadoTicketService.guardarEstado(estadoTicket);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstado);
    }

    // ENDPOINT: Actualizar un estado
    // PUT http://localhost:8080/api/estados-ticket/101
    // Body (JSON): { "nombreEstado": "En Espera" }
    @PutMapping("/{id}")
    public ResponseEntity<EstadoTicket> actualizarEstado(@PathVariable Integer id, @RequestBody EstadoTicket estadoTicket) {
        // Verificamos si existe
        if (!estadoTicketService.obtenerEstadoPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        estadoTicket.setCodigoEstadoDeTicket(id); // Asignamos el ID
        EstadoTicket estadoActualizado = estadoTicketService.guardarEstado(estadoTicket);

        return ResponseEntity.ok(estadoActualizado);
    }

    // ENDPOINT: Eliminar un estado
    // DELETE http://localhost:8080/api/estados-ticket/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstado(@PathVariable Integer id) {
        estadoTicketService.eliminarEstado(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}