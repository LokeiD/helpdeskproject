$(document).ready(function(){
    // Inicia la primera comprobación 5 segundos después de cargar la página
    setTimeout(mostrar_notificacion, 5000);
});

function mostrar_notificacion(){
    var usu_id = $('#user_idx').val();

    // 1. ¡URL TRADUCIDA!
    // Apunta a nuestro nuevo @RestController
    $.ajax({
        url: "/api/notificaciones/mostrar", // Reemplaza a: ../../controller/notificacion.php?op=mostrar
        type: "POST",
        data: { usu_id: usu_id }, // Spring Boot prefiere 'data' simple para @RequestParam
        success: function(data){
            // 2. ¡NO MÁS JSON.parse!
            // Spring Boot devuelve JSON automáticamente, 'data' ya es un objeto.
            if (data) {
                // 'data' es el objeto Notificacion (o null si no hay)

                // 3. ¡URL TRADUCIDA!
                // Construimos la URL de Spring, no la de PHP
                var urlTicket = "/consultar-ticket/" + data.tick_id;

                $.notify({
                    icon: 'glyphicon glyphicon-star',
                    message: data.not_mensaje,
                    url: urlTicket // URL de Spring
                });

                // 4. ¡URL TRADUCIDA!
                // Llama al endpoint para marcarla como leída
                $.post("/api/notificaciones/actualizar", { not_id: data.not_id }, function (response) {
                    // No hace nada al actualizar, igual que tu original
                });
            }
        },
        complete: function() {
            // 5. Vuelve a llamar a la función 5 segundos después de que esta termine
            setTimeout(mostrar_notificacion, 5000);
        }
    });
}

// 6. ¡MEJORA!
// Quitamos el 'setInterval'. Es mejor usar 'setTimeout' dentro del 'complete' de AJAX.
// Esto evita que se acumulen peticiones si el servidor tarda en responder.
// El 'setInterval' original de 5 segundos se movió a 'complete' y 'ready'.