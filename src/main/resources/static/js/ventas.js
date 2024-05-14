// #region DOM
const selProducto = document.querySelector('#selProducto');
const selUsuario = document.querySelector('#selUsuario');
const inputCantidad = document.querySelector('#inputCantidad');
const inputTotal = document.querySelector('#inputTotal');
const btnGuardar = document.querySelector('#btnGuardar');
const selProductoNew = document.querySelector('#selProductoNew');
const selUsuarioNew = document.querySelector('#selUsuarioNew');
const inputCantidadNew = document.querySelector('#inputCantidadNew');
const inputTotalNew = document.querySelector('#inputTotalNew');
const btnGuardarVenta = document.querySelector('#btn-guardarVenta');
const btnAgregarNuevaVenta = document.querySelector('#btnAgregarNuevaVenta');
const rangePicker = document.querySelector('#RangePicker');
const btnrangePicker = document.querySelector('#btn-rangePicker');

let isRange;
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
    await listVentas(isRange);
    // Inicializar una nueva instancia de DataTable con los datos actualizados
    dataTable = $("#datatable").DataTable(dataTableOptions).buttons().container().appendTo('#datatable_wrapper .col-md-6:eq(0)');
    // Establecer la bandera de inicialización en true
    dataTableIsInitialized = true;
}

// Inicialización inicial de la DataTable
const initDataTable = async (isRange) => {
    await recreateDataTable(isRange);
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
const obtenerDatosVentas = async () => {
    const data = verifySession();
    const response = await fetch("/api/ventas/", {
        headers: {"Authorization": "Bearer: " + data.token}
    });
    return await response.json();
};
const obtenerNuevasVentas = async () => {
    const data = verifySession();
    const inputValue = rangePicker.value;
    const fechas = inputValue.split(" - ");

    function formatDate(dateString) {
        const [month, day, year] = dateString.split("/");
        return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
    }

    const fechaInicio = formatDate(fechas[0]);
    const fechaFin = formatDate(fechas[1]);
    console.log(`/api/ventas/periodo?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`);
    const response = await fetch(`/api/ventas/periodo?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`, {
        method: 'GET', headers: {
            'Authorization': `Bearer: ${data.token}`
        }
    });

    if (!response.ok) {
        throw new Error("No se pudieron obtener las ventas");
    }

    return await response.json();
};

const listVentas = async (isRange) => {
    let ventas;
    if (isRange) {
        ventas = await obtenerNuevasVentas();
    } else {
        ventas = await obtenerDatosVentas();
    }
    let content = ``;
    ventas.forEach((venta, index) => {
        content += `
            <tr>
                <td>${venta.id}</td>
                <td>${venta.producto.nombre}</td>
                <td>${venta.producto.precio}</td>
                <td>${venta.usuario.nombre}</td>
                <td>${venta.cantidad}</td>
                <td>${venta.fecha}</td>
                <td>${venta.total}</td>
                <td>
                    <button data-identifier="${venta.id}" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#modal-editar" id="editar"><i class="fas fa-pen"></i></button>
                    <button data-identifier="${venta.id}" class="btn btn-sm btn-danger" data-toggle="modal" data-target="#modal-danger" id="eliminar"><i class="fas fa-trash"></i></button>
                </td>
                
            </tr>
            `
    });

    // EDITAR
    $('#datatable').off('click', '#editar').on('click', '#editar', async function (event) {
        desplegarProductos();
        desplegarUsuarios();

        const response = await fetch(`/api/ventas/${$(this).data('identifier')}`, {
            headers: {
                "Authorization": "Bearer: " + data.token
            }
        });
        const venta = await response.json();
        if (response.ok) {
            selProducto.value = venta.producto.nombre;
            selUsuario.value = venta.usuario.nombre;
            inputCantidad.value = venta.cantidad;
            inputTotal.value = (venta.producto.precio * venta.cantidad);
        } else {
            redirectLogin();
        }

        btnGuardar.addEventListener("click", async () => {
            const opcionProducto = selProducto.options[selProducto.selectedIndex];
            const opcionUsuario = selUsuario.options[selUsuario.selectedIndex];
            const idProducto = opcionProducto.getAttribute('data-target');
            const params = {
                idProducto: `${idProducto}`,
                idUsuario: `${opcionUsuario.getAttribute('data-target')}`,
                cantidad: `${inputCantidad.value}`,
                fecha: `${obtenerFechaActual()}`,
                total: await nuevoTotal(idProducto, inputCantidad.value),
            };

            const queryParams = new URLSearchParams(params).toString();
            const urlWithParams = `/api/ventas/${$(this).data('identifier')}?${queryParams}`;

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

        })
    });

    // ELIMINAR
    $('#datatable').off('click', '#eliminar').on('click', '#eliminar', async function (event) {
        const data = verifySession();
        btnModalEliminar.addEventListener("click", async () => {
            const response = await fetch(`/api/ventas/${$(this).data('identifier')}`, {
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
                redirectLogin();
            }
        })
    });


    tableBody.innerHTML = content;
}
// Registrar una nueva venta
const registrarVenta = async () => {
    const data = verifySession();
    const opcionProducto = selProductoNew.options[selProductoNew.selectedIndex];
    const opcionUsuario = selUsuarioNew.options[selUsuarioNew.selectedIndex];
    const idProducto = opcionProducto.getAttribute('data-target');
    let body = {
        idProducto: `${idProducto}`,
        idUsuario: `${opcionUsuario.getAttribute('data-target')}`,
        cantidad: `${inputCantidadNew.value}`,
        fecha: `${obtenerFechaActual()}`,
        total: await nuevoTotal(idProducto, inputCantidadNew.value)
    }
    toastr.remove();
    toastr["info"](`TOTAL: $ ${await nuevoTotal(idProducto, inputCantidadNew.value)} PESOS`);
    try {
        const resp = await fetch(`/api/ventas/crear`, {
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

// Funcion para cerrar el Modal
const closeModal = () => {
    $('#modal-danger').modal('hide');
    $('#modal-new-user').modal('hide')
    $('#modal-editar').modal('hide')
}
// Funcion para desplegar productos select
const desplegarProductos = async () => {
    const data = verifySession();
    const response = await fetch(`/api/productos/`, {
        headers: {
            "Authorization": "Bearer: " + data.token
        }
    });
    if (response.ok) {
        const productos = await response.json();
        productos.forEach(producto => {
            selProducto.innerHTML += `
            <option value="${producto.nombre}" data-target="${producto.id}">${producto.nombre}</option>
            `
        });
    } else {
        redirectLogin();
    }
}
// funcion para desplegar usuarios select
const desplegarUsuarios = async () => {
    const data = verifySession();
    const response = await fetch(`/api/usuarios/`, {
        headers: {
            "Authorization": "Bearer: " + data.token
        }
    });
    if (response.ok) {
        const usuarios = await response.json();
        usuarios.forEach(usuario => {
            selUsuario.innerHTML += `
            <option value="${usuario.nombre}" data-target="${usuario.id}">${usuario.nombre}</option>
            `
        });
    } else {
        redirectLogin();
    }
}// Funcion para desplegar productos select
const desplegarProductosNew = async () => {
    console.log('Desplegar productos new');
    const data = verifySession();
    const response = await fetch(`/api/productos/`, {
        headers: {
            "Authorization": "Bearer: " + data.token
        }
    });
    if (response.ok) {
        const productos = await response.json();
        productos.forEach(producto => {
            selProductoNew.innerHTML += `
            <option value="${producto.nombre}" data-target="${producto.id}">${producto.nombre}</option>
            `
        });
    } else {
        redirectLogin();
    }
}
// funcion para desplegar usuarios select
const desplegarUsuariosNew = async () => {
    console.log('desplegarUsuarios new')
    const data = verifySession();
    const response = await fetch(`/api/usuarios/`, {
        headers: {
            "Authorization": "Bearer: " + data.token
        }
    });
    if (response.ok) {
        const usuarios = await response.json();
        usuarios.forEach(usuario => {
            selUsuarioNew.innerHTML += `
            <option value="${usuario.nombre}" data-target="${usuario.id}">${usuario.nombre}</option>
            `
        });
    } else {
        redirectLogin();
    }
}
const obtenerFechaActual = () => {
    const fechaLocal = new Date(Date.now() - (new Date().getTimezoneOffset() * 60000));
    return fechaLocal.toISOString().slice(0, 10);
};
const nuevoTotal = async (idProducto, cantidad) => {
    let nuevoTotal = 0
    const data = verifySession();
    const response = await fetch(`/api/productos/${idProducto}`, {
        headers: {
            "Authorization": "Bearer: " + data.token
        }
    });
    const producto = await response.json();
    return producto.precio * cantidad;
}
const limpiarInputs = () => {
    inputCantidadNew.value = '';
    inputTotalNew.value = '';
    inputCantidad.value = '';
    inputTotal.value = '';
}
// funcion para validar datos
const validarCamposNew = () => {
    const cantidadStockRegex = /^[0-9]+$/;
    if (!cantidadStockRegex.test(inputCantidadNew.value.trim())) {
        toastr.remove();
        toastr["error"]("La cantidad debe ser un número válido.");
        return false;
    }
    return true;
}

// #region Listeners
// central
window.addEventListener("load", async () => {
    verifySession();
    await addInfoSidebar();
    isRange = false;
    await initDataTable(isRange);
})
btnAgregarNuevaVenta.addEventListener("click", async () => {
    await desplegarProductosNew();
    await desplegarUsuariosNew();
})
btnGuardarVenta.addEventListener("click", async () => {
    event.preventDefault();
    if (validarCamposNew()) {
        if (await registrarVenta()) {
            toastr["success"]("Todos los campos son válidos. ¡Registrado exitosamente!");
            await recreateDataTable();
            closeModal();
            limpiarInputs();
        }
    }
})

btnrangePicker.addEventListener("click", async () => {
    isRange = true;
    await initDataTable(isRange);
})
