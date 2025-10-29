package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Categoria;
import com.utpintegrador.helpdesk.model.SubCategoria;
import com.utpintegrador.helpdesk.repository.CategoriaRepository;
import com.utpintegrador.helpdesk.repository.SubCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoriaService {

    private final SubCategoriaRepository subCategoriaRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public SubCategoriaService(SubCategoriaRepository subCategoriaRepository, CategoriaRepository categoriaRepository) {
        this.subCategoriaRepository = subCategoriaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<SubCategoria> obtenerPorCategoria(Integer catId) {
        return subCategoriaRepository.findByCategoria_CodigoCategoria(catId);
    }


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
