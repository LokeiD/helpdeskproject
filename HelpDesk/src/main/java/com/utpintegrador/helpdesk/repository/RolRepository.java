package com.utpintegrador.helpdesk.repository;

import com.utpintegrador.helpdesk.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    // JpaRepository<Rol, Integer> significa:
    // "Gestiona la entidad 'Rol', cuya llave primaria es de tipo 'Integer'"
}