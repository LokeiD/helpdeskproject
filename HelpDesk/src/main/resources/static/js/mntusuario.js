var tabla;

// Función que se ejecuta al inicio
function init(){
    // Prevenir envío normal del formulario modal
    $("#usuario_form").on("submit",function(e){
        guardaryeditar(e);
    });

    // Ocultar modal al inicio (Asegúrate que el HTML del modal esté en mntusuario.html o fragments/)
    //$('#modalmantenimiento').modal('hide');

    // Cargar combos de Rol y Area al inicio
    cargarCombos();
}

// Función para cargar los combos del modal
function cargarCombos(){
    // Combo Roles (llamando a la API JSON)
    $.get("/api/roles", function(data) {
        // Filtra solo los roles activos si es necesario
        var rolesActivos = data.filter(function(rol){ return rol.estado === true; });
        $('#rolId').empty().append('<option value="">Seleccionar Rol</option>');
        $.each(rolesActivos, function(index, value) {
            $('#rolId').append('<option value="' + value.codigoRol + '">' + value.nombreRol + '</option>');
        });
        // Inicializar Select2 DENTRO del modal
        $('#rolId').select2({dropdownParent: $('#modalmantenimiento')});
    });

    // Combo Areas (llamando a la API JSON)
    $.get("/api/areas", function(data) {
        // No hay 'estado' en Area según tu BD, así que usamos todos
        $('#areaId').empty().append('<option value="">Seleccionar Área</option>');
        $.each(data, function(index, value) {
            $('#areaId').append('<option value="' + value.codigoArea + '">' + value.nombreArea + '</option>');
        });
        // Inicializar Select2 DENTRO del modal
        $('#areaId').select2({dropdownParent: $('#modalmantenimiento')});
    });
}


// Función para limpiar el formulario modal
function limpiar(){
    $('#usu_id').val('');
    $('#nombres').val('');
    $('#apellidoPaterno').val('');
    $('#apellidoMaterno').val('');
    $('#correo').val('');
    $('#passwoord').val('').attr("placeholder", "Ingrese Contraseña"); // Placeholder al limpiar
    $('#rolId').val('').trigger('change'); // Resetea Select2
    $('#areaId').val('').trigger('change'); // Resetea Select2
    // Habilitar campo contraseña por defecto al limpiar (para crear)
    $('#passwoord').prop('required', true);
    // Asegurarse que el campo estado (si existiera en el modal) se resetee
    //$('#estado').val('true').trigger('change');
}

// Función para mostrar el modal (Nuevo o Editar)
function mostrarModal(flag){ // flag=true significa Nuevo, flag=false significa Editar
    limpiar();
    if (flag){
        $('#mdltitulo').html('Nuevo Usuario');
        $('#passwoord').prop('required', true).attr("placeholder", "Ingrese Contraseña");
        $('#modalmantenimiento').modal('show');
    } else {
        $('#mdltitulo').html('Editar Usuario');
        $('#passwoord').prop('required', false).attr("placeholder", "Dejar vacío para no cambiar");
        // No mostramos el estado en el modal según tu última petición
        $('#modalmantenimiento').modal('show');
    }
}

// Evento click del botón "Nuevo Registro"
$('#btnnuevo').click(function(){
    mostrarModal(true);
});

// Función Principal
$(document).ready(function(){
    // Inicializar DataTable
    tabla = $('#usuario_data').DataTable({ // Cambiado de .dataTable() a .DataTable() (API moderna)
        "processing": true, // Cambiado de aProcessing a processing
        "serverSide": false, // Cambiado a false si cargas todos los datos al inicio
        "ajax":{
            url: '/api/usuarios', // Llama al endpoint GET de UsuarioController
            type : "get",
            dataType : "json",
            dataSrc: "data", // ¡Importante! Indica que los datos están en la propiedad 'data' del JSON
            error: function(e){
                console.error("Error cargando datos:", e.responseText);
                // Mostrar mensaje de error al usuario
                swal("Error!", "No se pudieron cargar los datos de usuarios.", "error");
            }
        },
        // --- Opciones de apariencia y lenguaje (sin cambios) ---
        dom: 'Bfrtip',
        buttons: [
                    'copyHtml5',
                    'excelHtml5',
                    'csvHtml5',
                    'pdf' // 'pdfHtml5' también es común
                ],
        "responsive": true,
        "bInfo":true,
        "iDisplayLength": 10,
        "order": [[ 0, "asc" ]],
        "language": { /* ... */ },
        // ============ Definir las columnas (ORDEN CORREGIDO) ============
        "columns": [
            { "data": "nombres" },
            { "data": "apellidoPaterno" },
            { "data": "apellidoMaterno" },
            { "data": "correo", "className": "d-none d-sm-table-cell" },
            { "data": "rol.nombreRol", "className": "d-none d-sm-table-cell" },
            { "data": "area.nombreArea", "className": "d-none d-sm-table-cell" },
            { // Fecha Modificacion
                "data": "fechaModificacion",
                "render": function(data, type, row) {
                    if (data) {
                         try { return new Date(data).toLocaleDateString() + ' ' + new Date(data).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }); }
                         catch (e) { return "Fecha inválida"; }
                    } else { return '<span class="label label-default">Sin Fecha</span>'; }
                },
                "className": "d-none d-sm-table-cell"
            },
            { // Fecha Eliminacion
                "data": "fechaEliminacion",
                "render": function(data, type, row) {
                    if (data) {
                         try { return new Date(data).toLocaleDateString() + ' ' + new Date(data).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }); }
                         catch (e) { return "Fecha inválida"; }
                    } else { return '<span class="label label-default">Sin Fecha</span>'; }
                },
                "className": "d-none d-sm-table-cell"
            },
            { // Estado (AHORA SOLO LABEL)            // Col 8
                "data": "estado",
                "className": "d-none d-sm-table-cell text-center",
                "render": function(data, type, row) {
                    if (data === true) {
                        return '<span class="label label-success">Activo</span>';
                    } else {
                        return '<span class="label label-danger">Inactivo</span>';
                    }
                }
            },
            { // Botón Editar
                "data": null,
                "orderable": false,
                "className": "text-center",
                "render": function (data, type, row) {
                    return '<button type="button" class="btn btn-inline btn-warning btn-sm" onClick="editar('+row.codigoUsuario+')"><i class="fa fa-pencil"></i></button>';
            }},
            { // Botón Eliminar
                "data": null,
                "orderable": false,
                "className": "text-center",
                "render": function (data, type, row) {
                    return '<button type="button" class="btn btn-inline btn-danger btn-sm" onClick="eliminar('+row.codigoUsuario+')"><i class="fa fa-trash"></i></button>';
            }},
            { // ¡NUEVA COLUMNA BOTÓN ACTIVAR!     // Col 9
                "data": null,
                "orderable": false,
                "className": "text-center",
                "render": function(data, type, row) {
                    if (row.estado === false) { // Muestra botón solo si está inactivo
                        return '<button type="button" onClick="activarUsuario(' + row.codigoUsuario + ');" class="btn btn-inline btn-success btn-sm" title="Activar Usuario"><i class="fa fa-check"></i></button>';
                    } else {
                        return ''; // Vacío si está activo
                    }
                }
            }
        ]
        // Ya no se necesita .DataTable() al final si usas la API moderna DataTable({})
    });
}); // Fin $(document).ready


// --- Función Guardar y Editar (Sin cambios relevantes aquí) ---
function guardaryeditar(e){
    e.preventDefault();
    var usuId = $('#usu_id').val();
    var url = (usuId) ? '/api/usuarios/' + usuId : '/api/usuarios';
    var method = (usuId) ? 'PUT' : 'POST';

    // Construir el objeto JSON (YA NO INCLUYE estado explícitamente)
    var usuarioJson = {
        nombres: $('#nombres').val(),
        apellidoPaterno: $('#apellidoPaterno').val(),
        apellidoMaterno: $('#apellidoMaterno').val(),
        correo: $('#correo').val(),
        passwoord: $('#passwoord').val() || null, // Enviar null si está vacío al editar
        rol: { codigoRol: $('#rolId').val() ? parseInt($('#rolId').val()) : null },
        area: { codigoArea: $('#areaId').val() ? parseInt($('#areaId').val()) : null }
    };

    var token = $("input[name='_csrf']").val();
    //var header = $("input[name='_csrf_header']").attr('name'); // Mejor obtener el nombre real si Thymeleaf lo cambia
    var header = "X-CSRF-TOKEN"; // Usar el nombre estándar

    $.ajax({
        url: url,
        type: method,
        data: JSON.stringify(usuarioJson),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
        success: function(data){
            $('#usuario_form')[0].reset();
            $("#modalmantenimiento").modal('hide');
            tabla.ajax.reload(); // Recargar datos de la tabla

            swal("Correcto!", (usuId ? "Actualizado" : "Registrado") + " Correctamente", "success");
        },
        error: function(jqXHR, textStatus, errorThrown){
             console.error("Error:", textStatus, errorThrown);
             console.error("Response:", jqXHR.responseText);
             var errorMsg = "Hubo un problema al guardar.";
             if (jqXHR.responseJSON && jqXHR.responseJSON.error) {
                 errorMsg = jqXHR.responseJSON.error;
             } else if (jqXHR.responseText) {
                 // Intentar extraer mensaje si no es JSON
                 try { errorMsg = $(jqXHR.responseText).text() || jqXHR.responseText; } catch(e){}
             }
             swal("Error!", errorMsg, "error");
        }
    });
}

// --- Función Editar (Sin cambios relevantes aquí) ---
function editar(usu_id){
    $.get("/api/usuarios/" + usu_id, function(data) {
        $('#mdltitulo').html('Editar Usuario');
        $('#usu_id').val(data.codigoUsuario);
        $('#nombres').val(data.nombres);
        $('#apellidoPaterno').val(data.apellidoPaterno);
        $('#apellidoMaterno').val(data.apellidoMaterno);
        $('#correo').val(data.correo);
        $('#passwoord').val('').attr("placeholder", "Dejar vacío para no cambiar").prop('required', false);
        $('#rolId').val(data.rol ? data.rol.codigoRol : '').trigger('change');
        $('#areaId').val(data.area ? data.area.codigoArea : '').trigger('change');
        // Ya no cargamos ni mostramos el campo estado aquí
        $('#modalmantenimiento').modal('show');
    }).fail(function(jqXHR) {
         console.error("Error al cargar usuario:", jqXHR.responseText);
         swal("Error!", "No se pudo cargar la información del usuario.", "error");
    });
}

// --- Función Eliminar (sin cambios) ---
function eliminar(usu_id){
    swal({
        title: "Confirmar Eliminación",
        text: "¿Está seguro de Eliminar (desactivar) este usuario?", // Texto aclarado
        type: "warning", // Tipo warning para eliminación
        showCancelButton: true,
        confirmButtonClass: "btn-danger",
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "No, cancelar",
        closeOnConfirm: false
    },
    function(isConfirm) {
        if (isConfirm) {
             var token = $("input[name='_csrf']").val();
             var header = "X-CSRF-TOKEN";

            $.ajax({
                url: "/api/usuarios/" + usu_id,
                type: "DELETE",
                beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
                success: function (datos) {
                    swal("Eliminado!", datos.message || "El usuario ha sido desactivado.", "success");
                    tabla.ajax.reload();
                },
                error: function(jqXHR, textStatus, errorThrown){
                     console.error("Error al eliminar:", jqXHR.responseJSON || textStatus);
                     var errorMsg = "No se pudo eliminar el usuario.";
                     if(jqXHR.responseJSON && jqXHR.responseJSON.error) {
                         errorMsg = jqXHR.responseJSON.error;
                     }
                     swal("Error!", errorMsg, "error");
                }
            });
        }
    });
}


// --- Función Activar (sin cambios) ---
function activarUsuario(usu_id) {
    swal({
        title: "Confirmar Activación",
        text: "¿Está seguro de reactivar este usuario?",
        type: "info",
        showCancelButton: true,
        confirmButtonClass: "btn-success",
        confirmButtonText: "Sí, activar",
        cancelButtonText: "No, cancelar",
        closeOnConfirm: false
    },
    function(isConfirm) {
        if (isConfirm) {
            var token = $("input[name='_csrf']").val();
            var header = "X-CSRF-TOKEN";

            $.ajax({
                url: '/api/usuarios/' + usu_id + '/activar',
                type: 'PUT',
                beforeSend: function(xhr) { xhr.setRequestHeader(header, token); },
                success: function(data) {
                    tabla.ajax.reload();
                    swal("Activado!", data.message || "El usuario ha sido reactivado.", "success");
                },
                error: function(jqXHR, textStatus, errorThrown) {
                     console.error("Error al activar:", jqXHR.responseJSON || textStatus);
                     var errorMsg = "No se pudo activar el usuario.";
                     if(jqXHR.responseJSON && jqXHR.responseJSON.error) {
                         errorMsg = jqXHR.responseJSON.error;
                     }
                     swal("Error!", errorMsg, "error");
                }
            });
        }
    });
}

// Llamada inicial a init
init();
