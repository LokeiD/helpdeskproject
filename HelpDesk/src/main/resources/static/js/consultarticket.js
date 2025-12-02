var tabla;
// Obtenemos el ID y Rol del usuario logueado desde los inputs ocultos en el layout/header
var usu_id = $('#user_idx').val();
var rol_id = $('#rol_idx').val();

function init(){
    $("#ticket_form").on("submit",function(e){
        guardar(e);
    });
}

$(document).ready(function(){

    // Llenar Combos de Filtros (usando los endpoints que ya creamos)
    $.get("/api/categorias", function(data) {
        $('#cat_id').empty().append('<option label="Seleccionar"></option>');
        $.each(data, function(index, value) {
            $('#cat_id').append('<option value="' + value.codigoCategoria + '">' + value.nombreCategoria + '</option>');
        });
    });

    $.get("/api/prioridades", function(data) {
        $('#prio_id').empty().append('<option label="Seleccionar"></option>');
        $.each(data, function(index, value) {
            $('#prio_id').append('<option value="' + value.codigoPrioridad + '">' + value.nombrePrioridad + '</option>');
        });
    });

    // Llenar Combo de Usuarios de Soporte (necesitaremos un endpoint filtrado, por ahora todos)
    // TODO: Crear endpoint /api/usuarios/soporte
    $.get("/api/usuarios", function(data) {
         // data es { data: [...] } por el cambio de DataTables
         var usuarios = data.data ? data.data : data;
         $('#usu_asig').empty().append('<option label="Seleccionar"></option>');
         $.each(usuarios, function(index, value) {
            // Aquí podrías filtrar por rol si tuvieras el ID del rol soporte en el JS
            $('#usu_asig').append('<option value="' + value.codigoUsuario + '">' + value.nombres + ' ' + value.apellidoPaterno + '</option>');
         });
    });

    // Lógica para mostrar tabla según rol
    if (rol_id == 1){ // Rol Usuario
        $('#viewuser').hide(); // Ocultar filtros avanzados

        tabla = $('#ticket_data').DataTable({
            "aProcessing": true,
            "aServerSide": false, // Cambiado a false para simplificar inicio
            dom: 'Bfrtip',
            "searching": true,
            lengthChange: false,
            colReorder: true,
            buttons: [ 'copyHtml5', 'excelHtml5', 'csvHtml5', 'pdfHtml5' ],
            "ajax":{
                url: '/api/tickets/listar-por-usuario/' + usu_id, // ¡NUEVO ENDPOINT!
                type : "get",
                dataType : "json",
                dataSrc: "", // Si devuelve array directo, o "data" si envuelves
                error: function(e){
                    console.log(e.responseText);
                }
            },
            "columns": [
                { "data": "codigoTicket" },
                { "data": "subCategoria.categoria.nombreCategoria" },
                { "data": "titulo" },
                { "data": "prioridad.nombrePrioridad" },
                {
                    "data": "estado",
                    "render": function(data) {
                        if (data) return '<span class="label label-success">Abierto</span>';
                        else return '<span class="label label-danger">Cerrado</span>';
                    }
                },
                {
                    "data": "fechaCreacion",
                     "render": function(data) { return new Date(data).toLocaleDateString() + ' ' + new Date(data).toLocaleTimeString(); }
                },
                {
                    "data": null,
                    "render": function(data, type, row){
                        return '<button type="button" class="btn btn-inline btn-primary btn-sm ladda-button" onClick="ver('+row.codigoTicket+')"><i class="fa fa-eye"></i></button>';
                    }
                }
            ],
            "language": { /* ... tus textos ... */ }
        });

    } else {
        // Rol Soporte (Admin)
        // Implementaremos listar_filtro después. Por ahora cargamos todos.
        tabla = $('#ticket_data').DataTable({
             // ... configuración similar ...
             "ajax":{
                url: '/api/tickets', // Trae TODOS los tickets
                type : "get",
                dataType : "json",
                dataSrc: "", // array directo
                error: function(e){ console.log(e.responseText); }
            },
            "columns": [ /* Mismas columnas */ ]
        });
    }
});

function ver(ticket_id){
    // Redirigir a detalle
    window.open('http://localhost:8080/detalle-ticket/' + ticket_id);
}

init();