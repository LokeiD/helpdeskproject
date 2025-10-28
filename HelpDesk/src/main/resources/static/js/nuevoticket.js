function init(){
    $("#ticket_form").on("submit",function(e){
        guardaryeditar(e);
    });
}

$(document).ready(function() {
    /* 1. Inicializar SummerNote */
    $('#tick_descrip').summernote({
        height: 150,
        lang: "es-ES",
        toolbar: [
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['font', ['strikethrough', 'superscript', 'subscript']],
            ['fontsize', ['fontsize']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']]
        ]
    });

    /* 2. Llenar Combo categoria */
    $.get("/api/categorias", function(data) {
        $('#cat_id').empty().append('<option value="">Seleccionar</option>');
        $.each(data, function(index, value) {
            $('#cat_id').append('<option value="' + value.codigoCategoria + '">' + value.nombreCategoria + '</option>');
        });
        $('#cat_id').select2();
    });

    /* 3. Evento change de Categoria */
    $("#cat_id").change(function(){
        var cat_id = $(this).val();
        if (cat_id) {
            $.get("/api/subcategorias/por-categoria", { cat_id : cat_id }, function(data) {
                $('#cats_id').empty().append('<option value="">Seleccionar</option>');
                $.each(data, function(index, value) {
                    $('#cats_id').append('<option value="' + value.codigoSubCategoria + '">' + value.nombre + '</option>');
                });
                $('#cats_id').select2();
            });
        } else {
            $('#cats_id').empty().append('<option value="">Seleccionar</option>').select2();
        }
    });

    /* 4. Llenar combo Prioridad */
    $.get("/api/prioridades", function(data) {
        $('#prio_id').empty().append('<option value="">Seleccionar</option>');
        $.each(data, function(index, value) {
            $('#prio_id').append('<option value="' + value.codigoPrioridad + '">' + value.nombrePrioridad + '</option>');
        });
        $('#prio_id').select2();
    });

    // Inicializar los selects vacíos con Select2
    $('#cats_id').select2();

}); // Fin de $(document).ready

function guardaryeditar(e){
    e.preventDefault();

    $('#btnguardar').prop("disabled",true);
    $('#btnguardar').html('<i class="fa fa-spinner fa-spin"></i> Espere..');

    // Esta línea YA incluye todos los campos, incluido el input type="file"
    var formData = new FormData($("#ticket_form")[0]);

    /* Validar campos */
    if ($('#tick_descrip').summernote('isEmpty') || $('#tick_titulo').val()==='' || $('#cats_id').val() === '' || $('#cat_id').val() === '' || $('#prio_id').val() === ''){
        swal("Advertencia!", "Campos Obligatorios Vacios", "warning");
        $('#btnguardar').prop("disabled",false);
        $('#btnguardar').html('Guardar');
    } else {

        // ========== ELIMINAR O COMENTAR ESTE BUCLE ==========
        /*
        var totalfiles = $('#fileElem')[0].files.length;
        console.log("Número de archivos seleccionados:", totalfiles);
        console.log("Archivos:", $('#fileElem')[0].files);
        for (var i = 0; i < totalfiles; i++) {
            console.log("Añadiendo archivo al FormData:", $('#fileElem')[0].files[i].name);
            formData.append("files[]", $('#fileElem')[0].files[i]); // <-- ESTO CAUSABA LA DUPLICACIÓN
        }
        */
        // =======================================================


        // ========== LOG ANTES DE ENVIAR (Opcional, para verificar) ==========
        console.log("Contenido de FormData antes de enviar:");
        for (var pair of formData.entries()) {
            // Si es un archivo, muestra su nombre, si no, el valor
            console.log(pair[0]+ ': ' + (pair[1] instanceof File ? pair[1].name : pair[1]));
        }
        // ================================================================


        /* Guardar Ticket */
        $.ajax({
            url: "/api/tickets/insertar",
            type: "POST",
            data: formData, // formData ya contiene los archivos correctamente
            contentType: false,
            processData: false,
            success: function(data){
                console.log(data);
                $('#tick_titulo').val('');
                $('#tick_descrip').summernote('reset');
                $('#cat_id').val('').trigger('change');
                $('#prio_id').val('').trigger('change');
                $('#fileElem').val('');
                swal("Correcto!", "Ticket Registrado Correctamente: Nro-" + data.tick_id, "success");
                $('#btnguardar').prop("disabled",false);
                $('#btnguardar').html('Guardar');
            },
            error: function(jqXHR, textStatus, errorThrown){
                console.error("Error al guardar ticket:", textStatus, errorThrown, jqXHR.responseText); // Añadido jqXHR.responseText
                swal("Error!", "No se pudo registrar el ticket. Verifique los datos o contacte al administrador.", "error");
                $('#btnguardar').prop("disabled",false);
                $('#btnguardar').html('Guardar');
            }
        });
    } // Fin del else (validación)
} // Fin de guardaryeditar

init();
