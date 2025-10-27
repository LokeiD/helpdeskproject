package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Prioridad;
import com.utpintegrador.helpdesk.service.PrioridadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // <-- AÑADIR import

@RestController
@RequestMapping("/api/prioridades") // URL base para prioridades
public class PrioridadController {

    private final PrioridadService prioridadService;

    // 1. Inyectamos el servicio de Prioridad
    @Autowired
    public PrioridadController(PrioridadService prioridadService) {
        this.prioridadService = prioridadService;
    }

    /**
     * ENDPOINT MODIFICADO: Obtener SOLO las prioridades ACTIVAS.
     * GET http://localhost:8080/api/prioridades
     * Devuelve una lista JSON de prioridades activas para los combos.
     */
    @GetMapping
    public List<Prioridad> obtenerTodasActivas() {
        List<Prioridad> todas = prioridadService.obtenerTodasLasPrioridades();
        // Usamos Streams de Java para filtrar la lista
        return todas.stream()
                .filter(Prioridad::isEstado) // Filtra solo las que tienen estado = true
                .collect(Collectors.toList()); // Devuelve la nueva lista filtrada
    }

    // ENDPOINT: Obtener una prioridad por ID (Sin cambios)
    // GET http://localhost:8080/api/prioridades/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Prioridad> obtenerPorId(@PathVariable Integer id) {
        Optional<Prioridad> prioridad = prioridadService.obtenerPrioridadPorId(id);

        return prioridad.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT: Crear una nueva prioridad (Sin cambios)
    // POST http://localhost:8080/api/prioridades
    // Body (JSON): { "nombrePrioridad": "Alta", "estado": true }
    @PostMapping
    public ResponseEntity<Prioridad> crearPrioridad(@RequestBody Prioridad prioridad) {
        Prioridad nuevaPrioridad = prioridadService.guardarPrioridad(prioridad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPrioridad);
    }

    // ENDPOINT: Actualizar una prioridad (Sin cambios)
    // PUT http://localhost:8080/api/prioridades/{id}
    // Body (JSON): { "nombrePrioridad": "MUY ALTA", "estado": true }
    @PutMapping("/{id}")
    public ResponseEntity<Prioridad> actualizarPrioridad(@PathVariable Integer id, @RequestBody Prioridad prioridad) {
        // Verificamos si existe
        if (!prioridadService.obtenerPrioridadPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        prioridad.setCodigoPrioridad(id); // Asignamos el ID
        Prioridad prioridadActualizada = prioridadService.guardarPrioridad(prioridad);

        return ResponseEntity.ok(prioridadActualizada);
    }

    // ENDPOINT: Eliminar una prioridad (Sin cambios)
    // DELETE http://localhost:8080/api/prioridades/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrioridad(@PathVariable Integer id) {
        prioridadService.eliminarPrioridad(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
