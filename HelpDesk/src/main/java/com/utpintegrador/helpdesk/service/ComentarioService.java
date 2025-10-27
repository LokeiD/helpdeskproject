package com.utpintegrador.helpdesk.service;

import com.utpintegrador.helpdesk.model.Comentario;
import com.utpintegrador.helpdesk.model.DetalleTicket;
import com.utpintegrador.helpdesk.model.Usuario;
import com.utpintegrador.helpdesk.repository.ComentarioRepository;
import com.utpintegrador.helpdesk.repository.DetalleTicketRepository;
import com.utpintegrador.helpdesk.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service // Marca esta clase como un Servicio de Spring
public class ComentarioService {

    // 1. Necesitamos 3 repositorios
    private final ComentarioRepository comentarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final DetalleTicketRepository detalleTicketRepository;

    // 2. Inyectamos todos
    @Autowired
    public ComentarioService(ComentarioRepository comentarioRepository,
                             UsuarioRepository usuarioRepository,
                             DetalleTicketRepository detalleTicketRepository) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.detalleTicketRepository = detalleTicketRepository;
    }

    // --- Métodos CRUD (quizás necesites uno para ver todos los comentarios de un detalle) ---

    // public List<Comentario> obtenerComentariosPorDetalle(Integer detalleTicketId) {
    //    ... (requiere un método custom en el ComentarioRepository)
    // }

    // --- Método con Lógica de Negocio ---

    /**
     * Crea un nuevo comentario de un usuario sobre una entrada de bitácora (DetalleTicket).
     */
    public Comentario crearComentario(String contenido, Integer usuarioId, Integer detalleTicketId) {

        // 1. Lógica de Validación: Validar que el usuario y el detalle existan
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        DetalleTicket detalleTicket = detalleTicketRepository.findById(detalleTicketId)
                .orElseThrow(() -> new RuntimeException("Detalle de Ticket no encontrado con id: " + detalleTicketId));

        // 2. Lógica de Negocio: Crear el nuevo comentario
        Comentario comentario = new Comentario();
        comentario.setContenido(contenido);
        comentario.setFechaCreacion(LocalDateTime.now()); // Fecha y hora actual

        // 3. Asignar las entidades relacionadas
        comentario.setUsuario(usuario);
        comentario.setDetalleTicket(detalleTicket);

        // 4. Guardar en la BD
        return comentarioRepository.save(comentario);
    }

    public void eliminarComentario(Integer id) {
        comentarioRepository.deleteById(id);
    }
}