package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Integer> {
}