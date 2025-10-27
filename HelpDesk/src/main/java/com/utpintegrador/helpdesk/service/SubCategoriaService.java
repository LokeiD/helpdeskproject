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

    private final SubCategoriaRepository subCategoriaRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public SubCategoriaService(SubCategoriaRepository subCategoriaRepository, CategoriaRepository categoriaRepository) {
        this.subCategoriaRepository = subCategoriaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // --- ¡MÉTODO NUEVO! ---
    /**
     * Obtiene una lista de subcategorías filtradas por el código de la categoría padre.
     * @param catId El ID de la Categoría padre.
     * @return Lista de SubCategorias.
     */
    public List<SubCategoria> obtenerPorCategoria(Integer catId) {
        // Llama al método mágico que debemos crear en el Repositorio
        return subCategoriaRepository.findByCategoria_CodigoCategoria(catId);
    }

    // --- Métodos CRUD ---

    public List<SubCategoria> obtenerTodasLasSubCategorias() {
        return subCategoriaRepository.findAll();
    }

    public Optional<SubCategoria> obtenerSubCategoriaPorId(Integer id) {
        return subCategoriaRepository.findById(id);
    }

    public SubCategoria guardarSubCategoria(SubCategoria subCategoria, Integer categoriaId) {

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + categoriaId));

        subCategoria.setCategoria(categoria);

        return subCategoriaRepository.save(subCategoria);
    }

    public void eliminarSubCategoria(Integer id) {
        subCategoriaRepository.deleteById(id);
    }
}
