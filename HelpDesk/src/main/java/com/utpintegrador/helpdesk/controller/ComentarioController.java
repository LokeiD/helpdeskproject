package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Comentario;
import com.utpintegrador.helpdesk.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comentarios") // URL base
public class ComentarioController {

    private final ComentarioService comentarioService;

    // 1. Inyectamos el servicio
    @Autowired
    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    // ENDPOINT: Crear un nuevo comentario
    // Petición: POST http://localhost:8080/api/comentarios
    // URL Params: ?contenido=Gracias, funcionó.&usuarioId=101&detalleTicketId=104
    @PostMapping
    public ResponseEntity<Comentario> crearComentario(
            @RequestParam String contenido,        // 1. El texto del comentario
            @RequestParam Integer usuarioId,      // 2. ID del usuario que comenta
            @RequestParam Integer detalleTicketId // 3. ID del detalle que se está comentando
    ) {
        try {
            // 4. Pasamos todo al servicio
            Comentario nuevoComentario = comentarioService.crearComentario(
                    contenido, usuarioId, detalleTicketId
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoComentario);
        } catch (RuntimeException e) {
            // Error si el usuario o el detalle no existen
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    // ENDPOINT: Eliminar un comentario
    // DELETE http://localhost:8080/api/comentarios/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Integer id) {
        comentarioService.eliminarComentario(id);
        return ResponseEntity.noContent().build();
    }

    // NOTA: Al igual que los detalles, los comentarios rara vez se actualizan (PUT).
    // Y para leerlos (GET), lo más común es tener un endpoint en el
    // DetalleTicketController que devuelva el detalle *con* sus comentarios.
}