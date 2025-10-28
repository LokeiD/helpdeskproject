package com.utpintegrador.helpdesk.controller;

import com.utpintegrador.helpdesk.model.Evidencia; // <-- NUEVO: Necesitamos el objeto Evidencia
import com.utpintegrador.helpdesk.model.Ticket;
import com.utpintegrador.helpdesk.service.DetalleEvidenciaService; // <-- NUEVO
import com.utpintegrador.helpdesk.service.DetalleTicketService;
import com.utpintegrador.helpdesk.service.EvidenciaService;
import com.utpintegrador.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import java.util.List;


@RestController
@RequestMapping("/api/tickets") // URL base para tickets
public class TicketController {

    private final TicketService ticketService;
    private final DetalleTicketService detalleTicketService;
    private final EvidenciaService evidenciaService;
    private final DetalleEvidenciaService detalleEvidenciaService; // <-- NUEVO

    @Value("${app.upload.dir}")
    private String uploadRootDir; // Renombrado para claridad (Directorio Raíz)

    // --- ¡CONSTRUCTOR MODIFICADO! ---
    @Autowired
    public TicketController(TicketService ticketService,
                            DetalleTicketService detalleTicketService,
                            EvidenciaService evidenciaService,
                            DetalleEvidenciaService detalleEvidenciaService) { // <-- NUEVO
        this.ticketService = ticketService;
        this.detalleTicketService = detalleTicketService;
        this.evidenciaService = evidenciaService;
        this.detalleEvidenciaService = detalleEvidenciaService; // <-- NUEVO
    }

    // ENDPOINT: Obtener todos los tickets
    // GET http://localhost:8080/api/tickets
    @GetMapping
    public List<Ticket> obtenerTodos() {
        return ticketService.obtenerTodosLosTickets();
    }

    // ENDPOINT: Obtener un ticket por ID
    // GET http://localhost:8080/api/tickets/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> obtenerPorId(@PathVariable Integer id) {
        try {
            Ticket ticket = ticketService.obtenerTicketPorId(id);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/insertar")
    public ResponseEntity<?> guardarTicket(
            @RequestParam("usu_id") Integer usuId,
            @RequestParam("cats_id") Integer subCatId,
            @RequestParam("prio_id") Integer prioId,
            @RequestParam("tick_titulo") String titulo,
            @RequestParam("tick_descrip") String descripcion,
            @RequestParam(value = "files[]", required = false) MultipartFile[] files
    ) {
        Evidencia evidenciaCreada = null; // <-- NUEVO: Variable para guardar la evidencia creada

        try {
            // 1. Crear el Ticket principal
            Ticket nuevoTicket = ticketService.crearTicket(usuId, subCatId, prioId, titulo);

            // 2. Estado "Sin asignar"
            final Integer ESTADO_SIN_ASIGNAR_ID = 100; // <-- Verifica este ID

            // 3. Crear el primer DetalleTicket (guarda la descripción)
            detalleTicketService.crearDetalleTicket(
                    descripcion,
                    nuevoTicket.getCodigoTicket(),
                    null,
                    ESTADO_SIN_ASIGNAR_ID
            );

            // --- 4. LÓGICA (MODIFICADA) PARA GUARDAR ARCHIVOS EN CARPETA ---
            if (files != null && files.length > 0 && !files[0].isEmpty()) { // Verifica si hay archivos reales

                // 4.A. Crear el registro de EVIDENCIA (la "carpeta" en la BD)
                // Usaremos una subcarpeta nombrada con el ID del ticket para organizar
                String nombreCarpetaTicket = "Ticket_" + nuevoTicket.getCodigoTicket();
                Path ticketUploadPath = Paths.get(uploadRootDir).resolve(nombreCarpetaTicket);

                // Creamos el registro Evidencia ANTES de guardar archivos
                // Necesitamos un método en EvidenciaService que cree y devuelva la Evidencia
                evidenciaCreada = evidenciaService.guardarEvidencia(
                        nombreCarpetaTicket, // Guardamos el nombre de la carpeta como "Ruta_Evidencia"
                        nuevoTicket.getCodigoTicket()
                );

                // 4.B. Crear el directorio físico en el disco
                if (!Files.exists(ticketUploadPath)) {
                    Files.createDirectories(ticketUploadPath);
                }

                // 4.C. Iterar y guardar cada archivo DENTRO de la carpeta del ticket
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        try {
                            // Generar nombre único para el archivo
                            String originalFileName = file.getOriginalFilename();
                            String fileExtension = "";
                            if (originalFileName != null && originalFileName.contains(".")) {
                                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                            }
                            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

                            // Ruta completa del archivo DENTRO de la carpeta del ticket
                            Path filePath = ticketUploadPath.resolve(uniqueFileName);

                            // Guardar archivo físico
                            Files.copy(file.getInputStream(), filePath);

                            // Guardar detalle en DETALLE_DE_EVIDENCIAS
                            // Necesitamos un método en DetalleEvidenciaService
                            detalleEvidenciaService.guardarDetalleArchivo(
                                    uniqueFileName, // Guardamos el nombre único del archivo
                                    evidenciaCreada.getCodigoEvidencia() // Vinculado al ID de la Evidencia creada
                            );

                            System.out.println("Archivo guardado: " + filePath.toString());

                        } catch (IOException e) {
                            System.err.println("Error al guardar el archivo " + file.getOriginalFilename() + ": " + e.getMessage());
                            // Considerar cómo manejar errores aquí (¿borrar lo ya subido?)
                        }
                    }
                }
            }
            // --- FIN LÓGICA MODIFICADA ---

            // 5. Devolver el ID del ticket al JS
            Map<String, Integer> response = Map.of("tick_id", nuevoTicket.getCodigoTicket());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            e.printStackTrace();
            // Si hubo un error creando el ticket o buscando IDs, devolver 400
            return ResponseEntity.badRequest().body("Error al crear el ticket (datos inválidos): " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            // Cualquier otro error (ej: I/O al crear carpeta/archivo) devolver 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor al procesar la solicitud.");
        }

    }

}