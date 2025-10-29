package com.utpintegrador.helpdesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // ¡OJO! @Controller, NO @RestController
public class HomeController {

    /**
     * Este método manejará la página principal después del login.
     * Corresponde a la URL "/" que pusimos en SecurityConfig.
     *
     * @return El nombre del archivo HTML en /templates
     */
    @GetMapping("/home")
    public String mostrarHome() {
        // Esto le dice a Spring que busque el archivo:
        // /src/main/resources/templates/home.html
        return "home";
    }

    @GetMapping("/nuevo-ticket")
    public String mostrarNuevoTicket() {
        // Esto le dice a Spring/Thymeleaf que busque y muestre
        // el archivo src/main/resources/templates/nuevoticket.html
        return "nuevoticket";
    }

    @GetMapping("/mantenimiento/usuario")
    public String mostrarMntUsuario() {
        // Esto le dice a Spring que busque el archivo:
        // /src/main/resources/templates/mntusuario.html
        return "mntusuario";
    }

    @GetMapping("/mantenimiento/prioridad")
    public String mostrarMntPrioridad() {
        // Esto le dice a Spring que busque el archivo:
        // /src/main/resources/templates/mntusuario.html
        return "mntprioridad";
    }

    @GetMapping("/mantenimiento/categoria")
    public String mostrarMntCategoria() {
        // Esto le dice a Spring que busque el archivo:
        // /src/main/resources/templates/mntusuario.html
        return "mntcategoria";
    }

    @GetMapping("/mantenimiento/subcategoria")
    public String mostrarMntSubcategoria() {
        // Esto le dice a Spring que busque el archivo:
        // /src/main/resources/templates/mntusuario.html
        return "mntsubcategoria";
    }

    @GetMapping("/mantenimiento/area")
    public String mostrarMntArea() {
        // Esto le dice a Spring que busque el archivo:
        // /src/main/resources/templates/mntusuario.html
        return "mntarea";
    }
}