package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Usuario;
import com.utpintegrador.helpdesk.repository.TicketRepository;
import com.utpintegrador.helpdesk.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard") // URL Base para todas las llamadas del dashboard
public class DashboardApiController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // --- ENDPOINTS DE ROL "ADMIN" ---
    // (Llamados cuando rol_id != 1)

    @PostMapping("/admin/total")
    public Map<String, Long> getAdminTotal() {
        long total = ticketRepository.count();
        return Collections.singletonMap("TOTAL", total);
    }

    @PostMapping("/admin/totalabierto")
    public Map<String, Long> getAdminTotalAbierto() {
        long total = ticketRepository.countByEstado(true); // true = Abierto
        return Collections.singletonMap("TOTAL", total);
    }

    @PostMapping("/admin/totalcerrado")
    public Map<String, Long> getAdminTotalCerrado() {
        long total = ticketRepository.countByEstado(false); // false = Cerrado
        return Collections.singletonMap("TOTAL", total);
    }

    // --- ENDPOINTS DE ROL "USUARIO" ---
    // (Llamados cuando rol_id == 1)

    @PostMapping("/usuario/total")
    public Map<String, Long> getUsuarioTotal(@RequestParam("usu_id") Integer usuId) {
        Usuario usuario = usuarioRepository.findById(usuId).orElseThrow();
        long total = ticketRepository.countByUsuario(usuario);
        return Collections.singletonMap("TOTAL", total);
    }

    @PostMapping("/usuario/totalabierto")
    public Map<String, Long> getUsuarioTotalAbierto(@RequestParam("usu_id") Integer usuId) {
        Usuario usuario = usuarioRepository.findById(usuId).orElseThrow();
        long total = ticketRepository.countByUsuarioAndEstado(usuario, true);
        return Collections.singletonMap("TOTAL", total);
    }

    @PostMapping("/usuario/totalcerrado")
    public Map<String, Long> getUsuarioTotalCerrado(@RequestParam("usu_id") Integer usuId) {
        Usuario usuario = usuarioRepository.findById(usuId).orElseThrow();
        long total = ticketRepository.countByUsuarioAndEstado(usuario, false);
        return Collections.singletonMap("TOTAL", total);
    }

    // --- ENDPOINTS PARA GRÁFICOS Y CALENDARIO (PENDIENTES) ---
    // Estos son más complejos y los haremos después. Por ahora, devolvemos datos vacíos.

    @PostMapping({"/admin/grafico", "/usuario/grafico"})
    public Map[] getGrafico() {
        // Devolvemos un array vacío para que Morris.js no falle
        return new Map[0];
    }

    @PostMapping({"/admin/calendar/admin", "/admin/calendar/usuario"})
    public Map[] getCalendar() {
        // Devolvemos un array vacío para que FullCalendar no falle
        return new Map[0];
    }
}