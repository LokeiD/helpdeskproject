package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Area;
import com.utpintegrador.helpdesk.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    private final AreaService areaService;

    @Autowired
    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping
    public List<Area> obtenerTodas() {
        return areaService.obtenerAreas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Area> obtenerPorId(@PathVariable Integer id) {
        Optional<Area> area = areaService.obtenerAreaPorId(id);

        return area.map(ResponseEntity::ok) // Forma corta de if/else
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Area> crearArea(@RequestBody Area area) {
        Area nuevaArea = areaService.guardarArea(area);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaArea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Area> actualizarArea(@PathVariable Integer id, @RequestBody Area area) {
        if (!areaService.obtenerAreaPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        area.setCodigoArea(id);
        Area areaActualizada = areaService.guardarArea(area);

        return ResponseEntity.ok(areaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArea(@PathVariable Integer id) {
        areaService.eliminarArea(id);
        return ResponseEntity.noContent().build();
    }
}