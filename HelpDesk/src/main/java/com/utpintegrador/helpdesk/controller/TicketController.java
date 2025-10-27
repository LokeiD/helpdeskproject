package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Ticket;
import com.utpintegrador.helpdesk.service.DetalleTicketService; // <-- AÑADIDO
import com.utpintegrador.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // <-- AÑADIDO para archivos

import java.util.List;
import java.util.Map; // <-- AÑADIDO para la respuesta JSON

@RestController
@RequestMapping("/api/tickets") // URL base para tickets
public class TicketController {

    private final TicketService ticketService;
    // --- ¡AÑADE ESTAS INYECCIONES! ---
    private final DetalleTicketService detalleTicketService;

    // --- ¡MODIFICA EL CONSTRUCTOR! ---
    @Autowired
    public TicketController(TicketService ticketService,
                            DetalleTicketService detalleTicketService) { // <-- AÑADIDO
        this.ticketService = ticketService;
        this.detalleTicketService = detalleTicketService; // <-- AÑADIDO
    }

    // ENDPOINT: Obtener todos los tickets
    // GET http://localhost:8080/api/tickets
    @GetMapping
    public List<Ticket> obtenerTodos() {
        return ticketService.obtenerTodosLosTickets();
    }

    // ENDPOINT: Obtener un ticket por ID
    // GET http://localhost:8080/api/tickets/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> obtenerPorId(@PathVariable Integer id) {
        try {
            Ticket ticket = ticketService.obtenerTicketPorId(id);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ENDPOINT ANTIGUO PARA CREAR (lo comentamos o borramos si ya no se usa)
    /*
    @PostMapping
    public ResponseEntity<Ticket> crearTicketSimple(
            @RequestParam String titulo,
            @RequestParam Integer usuarioId,
            @RequestParam Integer subCategoriaId
    ) {
        try {
            Ticket nuevoTicket = ticketService.crearTicket(usuarioId, subCategoriaId, null, titulo); // Asume que prioId es null
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    */

    // --- ¡NUEVO ENDPOINT PARA EL FORMULARIO! ---
    /**
     * Reemplaza a: ticket.php?op=insert
     * Recibe los datos del formulario (multipart/form-data) para crear un nuevo ticket.
     * URL: POST http://localhost:8080/api/tickets/insertar
     */
    @PostMapping("/insertar")
    public ResponseEntity<?> guardarTicket( // Cambiado a ResponseEntity<?> para mejor manejo de errores
                                            @RequestParam("usu_id") Integer usuId,
                                            @RequestParam("cats_id") Integer subCatId,
                                            @RequestParam("prio_id") Integer prioId,
                                            @RequestParam("tick_titulo") String titulo,
                                            @RequestParam("tick_descrip") String descripcion,
                                            @RequestParam(value = "files[]", required = false) MultipartFile[] files // Para los archivos adjuntos
    ) {
        try {
            // 1. Crear el Ticket principal
            // (Asegúrate que tu TicketService tenga este método y acepte prioId)
            Ticket nuevoTicket = ticketService.crearTicket(usuId, subCatId, prioId, titulo);

            // 2. Encontrar el estado "ABIERTO"
            // ¡¡DEBES VERIFICAR ESTE ID EN TU BD!! Asumiré que es 100.
            final Integer ESTADO_ABIERTO_ID = 100;

            // 3. Crear el primer DetalleTicket (que guarda la descripción inicial)
            detalleTicketService.crearDetalleTicket(
                    descripcion,
                    nuevoTicket.getCodigoTicket(), // El ID del ticket recién creado
                    usuId,                         // El usuario que lo crea
                    ESTADO_ABIERTO_ID              // El estado "Abierto"
            );

            // 4. TODO: Lógica para guardar los archivos (files)
            if (files != null && files.length > 0) {
                System.out.println("Archivos recibidos para el ticket " + nuevoTicket.getCodigoTicket() + ": " + files.length);
                // Aquí iría el bucle para guardar cada 'file' en el servidor
                // y luego llamar a EvidenciaService y DetalleEvidenciaService
                // para crear los registros en la base de datos.
            }

            // 5. Devolver el ID del ticket al JS, que espera un JSON: {"tick_id": NNN}
            Map<String, Integer> response = Map.of("tick_id", nuevoTicket.getCodigoTicket());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Captura errores si usuId, subCatId, prioId no existen
            // o si hubo otro problema. Devuelve un error 400.
            e.printStackTrace(); // Imprime el error en la consola del servidor para depurar
            return ResponseEntity.badRequest().body("Error al crear el ticket: " + e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otro error inesperado. Devuelve un error 500.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

}