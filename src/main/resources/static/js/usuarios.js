// #region Variables DOM
let modal = document.getElementById('modal-lg'), modalDanger = document.getElementById('modal-danger'),
    inputNombres = document.getElementById("inputNombres"), inNombre = document.querySelector('#inputNewUser-nombre'),
    inEmail = document.querySelector('#inputNewUser-email'),
    inContrasena = document.querySelector('#inputNewUser-contrasena'),
    inConfirmarContrasena = document.querySelector('#inputNewUser-confirmarContrasena'),
    selectedValue = document.getElementById("select-rol").value;

// #region Verificacion de inicio
const verifySession = () => {
    const data = JSON.parse(localStorage.getItem('sessionId'));
    if (!data) {
        redirectLogin();
    }
    return data;
};

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

    if ($.fn.DataTable.isDataTable("#datatable_users")) {
        $("#datatable_users").DataTable().destroy();
    }

    await listUsers();

    // Inicializar una nueva instancia de DataTable con los datos actualizados
    dataTable = $("#datatable_users").DataTable(dataTableOptions).buttons().container().appendTo('#datatable_users_wrapper .col-md-6:eq(0)');

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
// Ingresando Información a la tabla
const listUsers = async () => {
    const data = verifySession();
    const response = await fetch("/api/usuarios/", {headers: {"Authorization": "Bearer: " + data.token}})
    const users = await response.json()

    let content = ``;
    users.forEach((user, index) => {
        let btnRol;
        if (user.rol === "user") {
            btnRol = `<button data-identifier="${user.id}" class='btn btn-sm btn-success' id="user"><i class='fas fa-user-alt'></i></button>`
        } else {
            btnRol = `<button data-identifier="${user.id}" class='btn btn-sm btn-warning' id="admin"><i class='fas fa-hammer'></i></button>`
        }
        content += `
            <tr>
                <td>${user.id}</td>
                <td>${user.nombre}</td>
                <td>${user.email}</td>
                <td>${user.rol}</td>
                <td>${btnRol}</td>
                <td>
                    <button data-identifier="${user.id}" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#modal-lg" id="editar"><i class="fas fa-pen"></i></button>
                    <button data-identifier="${user.id}" class="btn btn-sm btn-danger" data-toggle="modal" data-target="#modal-danger" id="eliminar"><i class="fas fa-trash"></i></button>
                </td>
                
            </tr>
            `
    });

    // EDITAR USUARIO
    $('#datatable_users').off('click', '#editar').on('click', '#editar', async function (event) {

        const response = await fetch(`/api/usuarios/${$(this).data('identifier')}`, {
            headers: {
                "Authorization": "Bearer: " + data.token
            }
        });
        if (response.ok) {
            const user = await response.json();
            inputNombres.value = user.nombre;
        } else {
            // redirectLogin();
        }

        btnGuardar.addEventListener("click", async () => {
            const nombreRegex = /^[a-zA-Z\s]{1,30}$/;
            let isOk = true;

            if (!nombreRegex.test(inputNombres.value.trim())) {
                toastr.remove();
                toastr["error"]("Nombre invalido ,verificarlo");
                isOk = false;
            }


            if (isOk) {
                const params = {
                    nombre: `${inputNombres.value}`,
                };

                const queryParams = new URLSearchParams(params).toString();
                const urlWithParams = `/api/usuarios/${$(this).data('identifier')}?${queryParams}`;

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
                    // redirectLogin();
                }

            } else {
            }
        })
    });


    // ELIMINAR
    $('#datatable_users').off('click', '#eliminar').on('click', '#eliminar', async function (event) {
        const data = verifySession();
        btnModalEliminar.addEventListener("click", async () => {
            const response = await fetch(`/api/usuarios/${$(this).data('identifier')}`, {
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


    // USER TO ADMIN
    $('#datatable_users').off('click', '#user').on('click', '#user', async function (event) {
        const button = $(this);
        const identifier = button.data('identifier');
        const queryParams = new URLSearchParams({rol: 'admin'});
        const urlWithParams = `/api/usuarios/${identifier}?${queryParams}`;
        const data = verifySession();

        const response = await fetch(urlWithParams, {
            method: 'PUT', headers: {
                'Authorization': `Bearer: ${data.token}`
            }
        });

        if (response.ok) {
            toastr.remove();
            toastr["success"]("Cambio de rol completado");

            // Actualizar el botón con la clase y el contenido correspondientes
            button
                .removeClass('btn-success')
                .addClass('btn-warning')
                .attr('id', 'admin')
                .html(`<i class='fas fa-hammer'></i>`);
            await recreateDataTable();
        } else {

            redirectLogin();
        }

    });
    // ADMIN TO USER
    $('#datatable_users').off('click', '#admin').on('click', '#admin', async function (event) {
        const button = $(this);
        const identifier = button.data('identifier');
        const queryParams = new URLSearchParams({rol: 'user'});
        const urlWithParams = `/api/usuarios/${identifier}?${queryParams}`;
        const data = verifySession();

        const response = await fetch(urlWithParams, {
            method: 'PUT', headers: {
                'Authorization': `Bearer: ${data.token}`
            }
        });

        if (response.ok) {
            toastr.remove();
            toastr["success"]("Cambio de rol completado");

            // Actualizar el botón con la clase y el contenido correspondientes
            button
                .removeClass('btn-warning')
                .addClass('btn-success')
                .attr('id', 'cliente')
                .html(`<i class='fas fa-shopping-bag'></i>`);

            await recreateDataTable();
        } else {
            redirectLogin();
        }
    });


    tableBody_users.innerHTML = content;

}

// Funcion para cerrar el Modal
const closeModal = () => {
    $(modal).modal('hide');
    $(modalDanger).modal('hide');
    $('#modal-new-user').modal('hide')
}

// Funcion para redireccionar al login
const redirectLogin = () => {
    toastr.remove();
    toastr["error"]("Autenticación de perfil inválido, serás redireccionado al inicio de sesión en segundos");
    setTimeout(() => {
        window.location.href = "../index.html"
    }, 3000);
}
//  Funcion para validar campos newuser
const validarCampos = () => {
    const nombreRegex = /^[a-zA-Z\s]{1,30}$/;
    const correoRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const contrasenaRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+|~=`{}\[\]:;"'<>?,./\\-])[^\s]{8,20}$/;
    const confirmarContrasenaRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+|~=`{}\[\]:;"'<>?,./\\-])[^\s]{8,20}$/;

    if (!nombreRegex.test(inNombre.value)) {
        toastr["error"]("El nombre debe contener solo letras y espacios, de 2 a 30 caracteres.");
        return false;
    }
    if (!correoRegex.test(inEmail.value)) {
        toastr["error"]("El correo electrónico no es válido.");
        return false;
    }
    if (!contrasenaRegex.test(inContrasena.value)) {
        toastr["info"]("Debe contener al menos una letra minúscula, una letra mayúscula, un dígito, un carácter especial y ser de longitud entre 8 y 20 caracteres.");
        toastr["error"]("La contraseña no cumple con los requisitos.");
        return false;
    }
    if (!confirmarContrasenaRegex.test(inConfirmarContrasena.value)) {
        toastr["error"]("La confirmación de contraseña no coincide o no cumple con los requisitos.");
        return false;
    }

    return true;
}

const registrarUsuario = async () => {
    selectedValue = (selectedValue === 'Administrador') ? 'admin' : 'user';

    let body = {
        nombre: `${inNombre.value}`, email: `${inEmail.value}`, contrasena: `${inContrasena.value}`, rol: selectedValue
    };
    try {
        const resp = await fetch(`/api/usuarios/`, {
            method: 'POST',
            headers: new Headers({'Content-type': 'application/json'}),
            mode: 'cors',
            body: JSON.stringify(body)
        });
        if (resp.status === 201) {
            return true;
        } else if (resp.status === 409) {
            toastr["error"]("El usuario ya esta registrado, por favor inicie sesion con sus credenciales");
            return false;
        }

    } catch (e) {
        toastr["error"]("Error de red");
    }
    return false;
}
const limpiarInputs = () => {
    inputNombres.value = '';
    inNombre.value = '';
    inEmail.value = '';
    inContrasena.value = '';
    inConfirmarContrasena.value = '';
}
// #region Listeners
// Crear Usuario
document.querySelector('#btn-guardarNewUser').addEventListener("click", async () => {
    event.preventDefault();
    if (validarCampos()) {
        if (await registrarUsuario()) {
            toastr["success"]("Todos los campos son válidos. ¡Registrado exitosamente!");
            await recreateDataTable();
            closeModal();
            limpiarInputs();
        }
    }
})
// Cerrar Sesion
document.querySelector("#cerrarSesion").addEventListener("click", (e) => {
    e.preventDefault()
    localStorage.clear();
    window.location.href = "../index.html"
})

// Evento de carga de la página
window.addEventListener("load", async () => {
    verifySession();
    await addInfoSidebar();
    await initDataTable();
})

