package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Categoria;
import com.utpintegrador.helpdesk.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un Servicio de Spring
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // 1. Inyectamos el repositorio
    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // --- Métodos CRUD básicos ---

    // Obtener todas las categorías
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    // Obtener una categoría por su ID
    public Optional<Categoria> obtenerCategoriaPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    // Guardar una categoría (nueva o actualizada)
    public Categoria guardarCategoria(Categoria categoria) {
        // Aquí podríamos añadir lógica, por ejemplo, para manejar el 'Estado'
        return categoriaRepository.save(categoria);
    }

    // Eliminar una categoría por su ID
    public void eliminarCategoria(Integer id) {
        // OJO: Probablemente no deberías eliminar una categoría si tiene
        // SubCategorías asociadas. Más adelante añadiremos esa validación.
        categoriaRepository.deleteById(id);
    }
}