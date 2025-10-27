package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Prioridad;
import com.utpintegrador.helpdesk.service.PrioridadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prioridades") // URL base para prioridades
public class PrioridadController {

    private final PrioridadService prioridadService;

    // 1. Inyectamos el servicio de Prioridad
    @Autowired
    public PrioridadController(PrioridadService prioridadService) {
        this.prioridadService = prioridadService;
    }

    // ENDPOINT: Obtener todas las prioridades
    // GET http://localhost:8080/api/prioridades
    @GetMapping
    public List<Prioridad> obtenerTodas() {
        return prioridadService.obtenerTodasLasPrioridades();
    }

    // ENDPOINT: Obtener una prioridad por ID
    // GET http://localhost:8080/api/prioridades/101
    @GetMapping("/{id}")
    public ResponseEntity<Prioridad> obtenerPorId(@PathVariable Integer id) {
        Optional<Prioridad> prioridad = prioridadService.obtenerPrioridadPorId(id);

        return prioridad.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ENDPOINT: Crear una nueva prioridad
    // POST http://localhost:8080/api/prioridades
    // Body (JSON): { "nombrePrioridad": "Alta", "estado": true }
    @PostMapping
    public ResponseEntity<Prioridad> crearPrioridad(@RequestBody Prioridad prioridad) {
        Prioridad nuevaPrioridad = prioridadService.guardarPrioridad(prioridad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPrioridad);
    }

    // ENDPOINT: Actualizar una prioridad
    // PUT http://localhost:8080/api/prioridades/101
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

    // ENDPOINT: Eliminar una prioridad
    // DELETE http://localhost:8080/api/prioridades/101
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrioridad(@PathVariable Integer id) {
        prioridadService.eliminarPrioridad(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}