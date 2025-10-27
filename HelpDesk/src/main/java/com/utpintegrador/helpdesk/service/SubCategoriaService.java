package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Categoria;
import com.utpintegrador.helpdesk.model.SubCategoria;
import com.utpintegrador.helpdesk.repository.CategoriaRepository;
import com.utpintegrador.helpdesk.repository.SubCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un Servicio de Spring
public class SubCategoriaService {

    // 1. Necesitamos ambos repositorios
    private final SubCategoriaRepository subCategoriaRepository;
    private final CategoriaRepository categoriaRepository;

    // 2. Inyectamos los repositorios
    @Autowired
    public SubCategoriaService(SubCategoriaRepository subCategoriaRepository, CategoriaRepository categoriaRepository) {
        this.subCategoriaRepository = subCategoriaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // --- Métodos CRUD ---

    public List<SubCategoria> obtenerTodasLasSubCategorias() {
        return subCategoriaRepository.findAll();
    }

    public Optional<SubCategoria> obtenerSubCategoriaPorId(Integer id) {
        return subCategoriaRepository.findById(id);
    }

    // --- Método con Lógica de Negocio ---

    /**
     * Crea o actualiza una SubCategoria, asegurándose de que su Categoria padre exista.
     */
    public SubCategoria guardarSubCategoria(SubCategoria subCategoria, Integer categoriaId) {

        // 1. Lógica de Validación: Buscar la Categoría padre
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + categoriaId));

        // 2. Lógica de Negocio: Asignar la entidad encontrada
        subCategoria.setCategoria(categoria);

        // 3. Guardar en la BD
        return subCategoriaRepository.save(subCategoria);
    }

    public void eliminarSubCategoria(Integer id) {
        // OJO: Deberíamos validar que esta subcategoría no esté
        // en uso por ningún Ticket antes de borrarla.
        subCategoriaRepository.deleteById(id);
    }
}