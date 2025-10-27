package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Integer> {
    /**
     * Metodo mágico de Spring Data JPA:
     * Busca subcategorías filtrando por la clave primaria (CodigoCategoria)
     * de la entidad Categoria a la que están relacionadas.
     */
    List<SubCategoria> findByCategoria_CodigoCategoria(Integer codigoCategoria);
}