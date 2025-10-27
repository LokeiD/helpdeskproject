package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Ticket;
import com.utpintegrador.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets") // URL base para tickets
public class TicketController {

    private final TicketService ticketService;

    // 1. Inyectamos el servicio
    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // ENDPOINT: Obtener todos los tickets
    // GET http://localhost:8080/api/tickets
    @GetMapping
    public List<Ticket> obtenerTodos() {
        return ticketService.obtenerTodosLosTickets();
    }

    // ENDPOINT: Obtener un ticket por ID
    // GET http://localhost:8080/api/tickets/101
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> obtenerPorId(@PathVariable Integer id) {
        try {
            Ticket ticket = ticketService.obtenerTicketPorId(id);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ENDPOINT: Crear un nuevo ticket (¡El más importante!)
    // Petición: POST http://localhost:8080/api/tickets
    // URL Params: ?titulo=Mi Impresora no funciona&usuarioId=101&subCategoriaId=105
    @PostMapping
    public ResponseEntity<Ticket> crearTicket(
            @RequestParam String titulo,         // 1. Título del ticket
            @RequestParam Integer usuarioId,      // 2. ID del usuario que reporta
            @RequestParam Integer subCategoriaId  // 3. ID de la subcategoría
    ) {
        try {
            // 4. Pasamos todo al servicio
            Ticket nuevoTicket = ticketService.crearNuevoTicket(titulo, usuarioId, subCategoriaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTicket);
        } catch (RuntimeException e) {
            // Esto captura el error si el Usuario o SubCategoria no existen
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        }
    }

    // NOTA SOBRE DELETE:
    // Generalmente, los tickets no se "eliminan" (DELETE),
    // se "cierran" (mediante un PUT o POST a un endpoint /cerrar)
    // que crearía un DetalleTicket con un estado "Cerrado".
    // Por eso, omitimos el @DeleteMapping por ahora.
}