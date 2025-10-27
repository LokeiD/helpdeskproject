package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.*;
import com.utpintegrador.helpdesk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un Servicio de Spring
public class DiccionarioSolucionService {

    // 1. Necesitamos 3 repositorios para esta lógica
    private final DiccionarioSolucionRepository diccionarioSolucionRepository;
    private final UsuarioRepository usuarioRepository;
    private final SubCategoriaRepository subCategoriaRepository;

    // 2. Inyectamos los repositorios
    @Autowired
    public DiccionarioSolucionService(DiccionarioSolucionRepository diccionarioSolucionRepository,
                                      UsuarioRepository usuarioRepository,
                                      SubCategoriaRepository subCategoriaRepository) {
        this.diccionarioSolucionRepository = diccionarioSolucionRepository;
        this.usuarioRepository = usuarioRepository;
        this.subCategoriaRepository = subCategoriaRepository;
    }

    // --- Métodos CRUD básicos ---

    public List<DiccionarioSolucion> obtenerTodasLasSoluciones() {
        return diccionarioSolucionRepository.findAll();
    }

    public Optional<DiccionarioSolucion> obtenerSolucionPorId(Integer id) {
        return diccionarioSolucionRepository.findById(id);
    }

    // --- Método con Lógica de Negocio ---

    /**
     * Crea una nueva entrada en el diccionario.
     * Recibe el objeto y los IDs de sus relaciones.
     */
    public DiccionarioSolucion crearSolucion(DiccionarioSolucion solucion, Integer usuarioId, Integer subCategoriaId) {

        // 1. Lógica de Validación: Buscar las entidades relacionadas
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        SubCategoria subCategoria = subCategoriaRepository.findById(subCategoriaId)
                .orElseThrow(() -> new RuntimeException("SubCategoría no encontrada con id: " + subCategoriaId));

        // 2. Lógica de Negocio: Asignar las entidades
        solucion.setUsuario(usuario);
        solucion.setSubCategoria(subCategoria);

        // 3. Lógica de Negocio: Establecer valores por defecto
        solucion.setFecha(LocalDate.now()); // Establece la fecha de creación

        // 4. Guardar en la BD
        return diccionarioSolucionRepository.save(solucion);
    }

    public void eliminarSolucion(Integer id) {
        diccionarioSolucionRepository.deleteById(id);
    }
}