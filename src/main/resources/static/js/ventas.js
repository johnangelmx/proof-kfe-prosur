// #region DOM

// #region Verificacion de inicio
const verifySession = () => {
    const data = JSON.parse(localStorage.getItem('sessionId'));
    if (!data) {
        redirectLogin();
    }
    return data;
};

// Funcion para redireccionar al login
const redirectLogin = () => {
    toastr.remove();
    toastr["error"]("Autenticación de perfil inválido, serás redireccionado al inicio de sesión en segundos");
    setTimeout(() => {
        window.location.href = "../index.html"
    }, 3000);
}

// Variables para PLUGIN DataTable
let dataTable;
let dataTableIsInitialized = false;
const dataTableOptions = {
    "pageLength": 15,
    "destroy": true,
    "language": {
        "decimal": "",
        "emptyTable": "No hay información",
        "info": "Mostrando _START_ a _END_ de _TOTAL_ Entradas",
        "infoEmpty": "Mostrando 0 to 0 of 0 Entradas",
        "infoFiltered": "(Filtrado de _MAX_ total entradas)",
        "infoPostFix": "",
        "thousands": ",",
        "lengthMenu": "Mostrar _MENU_ Entradas",
        "loadingRecords": "Cargando...",
        "processing": "Procesando...",
        "search": "Buscar:",
        "zeroRecords": "Sin resultados encontrados",
        "paginate": {
            "first": "Primero", "last": "Ultimo", "next": "Siguiente", "previous": "Anterior"
        }
    },
    "responsive": true,
    "lengthChange": false,
    "autoWidth": false,
    "buttons": ["copy", "csv", "excel", "pdf", "print", "colvis"]
}

// Función para recrear la DataTable
async function recreateDataTable() {
    if ($.fn.DataTable.isDataTable("#datatable")) {
        $("#datatable").DataTable().destroy();
    }
    await listProducts();
    // Inicializar una nueva instancia de DataTable con los datos actualizados
    dataTable = $("#datatable").DataTable(dataTableOptions).buttons().container().appendTo('#datatable_wrapper .col-md-6:eq(0)');
    // Establecer la bandera de inicialización en true
    dataTableIsInitialized = true;
}

// Inicialización inicial de la DataTable
const initDataTable = async () => {
    await recreateDataTable();
};

// Obteniendo Usuario
const obtenerUsuario = async () => {
    const data = verifySession();
    const resp = await fetch(`/api/usuarios/${data.idUsuario}`, {
        headers: {"Authorization": "Bearer: " + data.token}
    })
    if (!resp.ok) {
        window.location.href = "../index.html"
    }
    return await resp.json();
}
// Agregando la información del usuario en la barra de la izquierda
const addInfoSidebar = async () => {
    let userdata = await obtenerUsuario()
    document.querySelector('#usuarioPanelDerecho').innerText = `${userdata.nombre}`
}

const listProducts = async () => {
    const data = verifySession();
//     Peticion
    const response = await fetch("/api/productos/")
    const products = await response.json()

    let content = ``;
    products.forEach((prod, index) => {
        let btnRol;
        content += `
            <tr>
                <td>${prod.id}</td>
                <td>${prod.nombre}</td>
                <td>${prod.descripcion}</td>
                <td>${prod.precio}</td>
                <td>${prod.cantidadStock}</td>
                <td>
                    <button data-identifier="${prod.id}" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#modal-editar" id="editar"><i class="fas fa-pen"></i></button>
                    <button data-identifier="${prod.id}" class="btn btn-sm btn-danger" data-toggle="modal" data-target="#modal-danger" id="eliminar"><i class="fas fa-trash"></i></button>
                </td>
                
            </tr>
            `
    });
    // Variable global para almacenar el identificador
    let identifier;
    // EDITAR USUARIO
    $('#datatable').off('click', '#editar').on('click', '#editar', async function (event) {
        identifier = $(this).data('identifier');
        const response = await fetch(`/api/productos/${identifier}`, {
            headers: {
                "Authorization": "Bearer: " + data.token
            }
        });
        if (response.ok) {
            const producto = await response.json();
            inputNombre.value = producto.nombre;
            inputDescripcion.value = producto.descripcion;
            inputPrecio.value = producto.precio;
            inputcantidadStock.value = producto.cantidadStock;
        } else {
            redirectLogin();
        }

        btnGuardar.addEventListener("click", async () => {
            const nombreRegex = /^[\w\dáéíóúÁÉÍÓÚüÜñÑ\s\S]{1,250}$/;
            const descripcionRegex = /^[\w\dáéíóúÁÉÍÓÚüÜñÑ\s\S]{1,250}$/;
            const precioRegex = /^\d+(\.\d{1,2})?$/;
            const cantidadStockRegex = /^\d+$/;

            let isOk = true;

            if (!nombreRegex.test(inputNombre.value.trim())) {
                toastr.remove();
                toastr["error"]("El nombre debe contener solo letras y espacios.");
                isOk = false;
            }

            if (!descripcionRegex.test(inputDescripcion.value.trim())) {
                toastr.remove();
                toastr["error"]("La descripción debe contener letras, números, espacios en blanco y los caracteres -, .,.");
                isOk = false;
            }

            if (!precioRegex.test(inputPrecio.value.trim())) {
                toastr.remove();
                toastr["error"]("El precio debe ser un número válido (pueden ser decimales con hasta dos dígitos después del punto).");
                isOk = false;
            }

            if (!cantidadStockRegex.test(inputcantidadStock.value.trim())) {
                toastr.remove();
                toastr["error"]("La cantidad de stock debe ser un número entero positivo.");
                isOk = false;
            }


            if (isOk) {
                const params = {
                    nombre: `${inputNombre.value}`,
                    descripcion: `${inputDescripcion.value}`,
                    precio: `${inputPrecio.value}`,
                    cantidadStock: `${inputcantidadStock.value}`
                };

                const queryParams = new URLSearchParams(params).toString();
                const urlWithParams = `/api/productos/${identifier}?${queryParams}`;

                const response = await fetch(urlWithParams, {
                    method: 'PUT', headers: {
                        'Authorization': `Bearer: ${data.token}`
                    }
                });

                if (response.ok) {
                    const data = await response.json();
                    // Manejar la respuesta exitosa
                    closeModal();
                    toastr.remove(); // Eliminar todos los mensajes de Toastr visibles y ocultos
                    toastr["success"]("Actualización completada"); // Mostrar el nuevo mensaje de éxito
                    await recreateDataTable();
                } else {
                    redirectLogin();
                }
            }
        })
    });

    // ELIMINAR
    $('#datatable').off('click', '#eliminar').on('click', '#eliminar', async function (event) {
        const data = verifySession();
        btnModalEliminar.addEventListener("click", async () => {
            const response = await fetch(`/api/productos/${$(this).data('identifier')}`, {
                method: 'DELETE', headers: {
                    'Authorization': `Bearer: ${data.token}`
                }
            });

            if (response.ok) {
                closeModal();
                toastr.remove();
                toastr["success"]("Eliminación de usuario completada");
                await recreateDataTable();
            } else {
                // redirectLogin();
            }
        })
    });


    tableBody.innerHTML = content;
}
// Registrar una nueva venta
const registrarProducto = async () => {
    const data = verifySession();
    let body = {
        nombre: `${inNombre.value}`,
        descripcion: `${inDescripcion.value}`,
        precio: `${inPrecio.value}`,
        cantidadStock: `${incantidadStock.value}`
    }
    try {
        const resp = await fetch(`/api/productos/`, {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Authorization': `Bearer: ${data.token}`
            }, mode: 'cors', body: JSON.stringify(body)
        })
        if (resp.ok) {
            return true;

        }
    } catch (e) {
        return false;
    }

}
const validarCampos = () => {
    const nombreRegex = /^[\w\dáéíóúÁÉÍÓ��üÜñÑ\s\S]{1,250}$/;
    const descripcionRegex = /^[\w\dáéíóúÁÉÍÓÚüÜñÑ\s\S]{1,250}$/;
    const precioRegex = /^\d+(\.\d{1,2})?$/;
    const cantidadStockRegex = /^\d+$/;

    let isOk = true;

    if (!nombreRegex.test(inNombre.value.trim())) {
        toastr.remove();
        toastr["error"]("El nombre debe contener solo letras y espacios.");
        isOk = false;
    }
    if (!descripcionRegex.test(inDescripcion.value.trim())) {
        toastr.remove();
        toastr["error"]("La descripción debe contener letras, números, espacios en blanco y los caracteres -, .,.");
        isOk = false;
    }
    if (!precioRegex.test(inPrecio.value.trim())) {
        toastr.remove();
        toastr["error"]("El precio debe ser un número válido (pueden ser decimales con hasta dos dígitos después del punto).");
        isOk = false;
    }
    if (!cantidadStockRegex.test(incantidadStock.value.trim())) {
        toastr.remove();
        toastr["error"]("La cantidad de stock debe ser un número entero positivo.");
        isOk = false;
    }
    return isOk;
}

const limpiarInputs = () => {
    inNombre.value = "";
    inDescripcion.value = "";
    inPrecio.value = "";
    incantidadStock.value = "";
}

// Funcion para cerrar el Modal
const closeModal = () => {
    $('#modal-danger').modal('hide');
    $('#modal-new-user').modal('hide')
    $('#modal-editar').modal('hide')
}
// #region Listeners
// central
window.addEventListener("load", async () => {
    verifySession();
    await addInfoSidebar();
    await initDataTable();
})
btnGuardarNewUser.addEventListener("click", async () => {
    event.preventDefault();
    if (validarCampos()) {
        if (await registrarProducto()) {
            toastr["success"]("Todos los campos son válidos. ¡Registrado exitosamente!");
            await recreateDataTable();
            closeModal();
            limpiarInputs();
        }
    }
})


