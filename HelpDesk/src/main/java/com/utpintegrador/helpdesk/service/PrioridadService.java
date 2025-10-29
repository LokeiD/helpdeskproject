package com.utpintegrador.helpdesk.service;
import com.utpintegrador.helpdesk.model.Prioridad;
import com.utpintegrador.helpdesk.repository.PrioridadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PrioridadService {

    private final PrioridadRepository prioridadRepository;

    @Autowired
    public PrioridadService(PrioridadRepository prioridadRepository) {
        this.prioridadRepository = prioridadRepository;
    }

    public List<Prioridad> obtenerPrioridades() {
        return prioridadRepository.findAll();
    }

    public Optional<Prioridad> obtenerPrioridadPorId(Integer id) {
        return prioridadRepository.findById(id);
    }

    public Prioridad guardarPrioridad(Prioridad prioridad) {
        return prioridadRepository.save(prioridad);
    }

    public void eliminarPrioridad(Integer id) {
        prioridadRepository.deleteById(id);
    }
}