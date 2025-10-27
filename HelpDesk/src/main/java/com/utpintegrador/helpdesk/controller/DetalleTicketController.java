package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.DetalleTicket;
import com.utpintegrador.helpdesk.service.DetalleTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-tickets") // URL base
public class DetalleTicketController {

    private final DetalleTicketService detalleTicketService;

    // 1. Inyectamos el servicio
    @Autowired
    public DetalleTicketController(DetalleTicketService detalleTicketService) {
        this.detalleTicketService = detalleTicketService;
    }

    // ENDPOINT: Obtener todos los detalles (para admin, quizás)
    // GET http://localhost:8080/api/detalle-tickets
    @GetMapping
    public List<DetalleTicket> obtenerTodos() {
        return detalleTicketService.obtenerTodosLosDetalles();
    }

    // ENDPOINT: Crear un nuevo detalle de ticket (la bitácora)
    // Petición: POST http://localhost:8080/api/detalle-tickets
    // URL Params: ?descripcion=Se asigna técnico...&ticketId=101&usuarioId=102&estadoId=103
    @PostMapping
    public ResponseEntity<DetalleTicket> crearDetalleTicket(
            @RequestParam String descripcion,  // 1. Descripción de la acción
            @RequestParam Integer ticketId,   // 2. ID del ticket afectado
            @RequestParam Integer usuarioId,  // 3. ID del usuario (técnico)
            @RequestParam Integer estadoId    // 4. ID del nuevo estado
    ) {
        try {
            // 5. Pasamos todo al servicio
            DetalleTicket nuevoDetalle = detalleTicketService.crearDetalleTicket(
                    descripcion, ticketId, usuarioId, estadoId
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDetalle);
        } catch (RuntimeException e) {
            // Error si el ticket, usuario o estado no existen
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    // NOTA: Generalmente los detalles de bitácora no se actualizan (PUT)
    // ni se eliminan (DELETE), ya que son un registro histórico.
}