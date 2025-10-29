package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Prioridad;
import com.utpintegrador.helpdesk.service.PrioridadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prioridades")
public class PrioridadController {

    private final PrioridadService prioridadService;

    @Autowired
    public PrioridadController(PrioridadService prioridadService) {
        this.prioridadService = prioridadService;
    }

    @GetMapping
    public List<Prioridad> obtenerPrioridades() {
        List<Prioridad> todas = prioridadService.obtenerPrioridades();
        //* Filtrar y listar prioridades activas
        return todas.stream()
                .filter(Prioridad::isEstado)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prioridad> obtenerPorId(@PathVariable Integer id) {
        Optional<Prioridad> prioridad = prioridadService.obtenerPrioridadPorId(id);

        return prioridad.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Prioridad> crearPrioridad(@RequestBody Prioridad prioridad) {
        Prioridad nuevaPrioridad = prioridadService.guardarPrioridad(prioridad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPrioridad);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Prioridad> actualizarPrioridad(@PathVariable Integer id, @RequestBody Prioridad prioridad) {
        if (!prioridadService.obtenerPrioridadPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        prioridad.setCodigoPrioridad(id);
        Prioridad prioridadActualizada = prioridadService.guardarPrioridad(prioridad);

        return ResponseEntity.ok(prioridadActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrioridad(@PathVariable Integer id) {
        prioridadService.eliminarPrioridad(id);
        return ResponseEntity.noContent().build();
    }
}
