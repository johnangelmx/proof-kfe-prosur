// #region Verificacion de inicio
const verifySession = () => {
    const data = JSON.parse(localStorage.getItem('sessionId'));
    if (!data) {
        redirectLogin();
    }
    return data;
};
// agregar nombre del usuario en el panel izquierdo
const addInfoSidebar = async (data) => {
    try {
        const response = await fetch(`/api/usuarios/${data.idUsuario}`, {
            headers: {
                "Authorization": `Bearer: ${data.token}`
            }
        });
        const userData = await response.json();
        const usuarioPanelDerecho = document.getElementById("usuarioPanelDerecho");
        usuarioPanelDerecho.innerText = userData.nombre;
    } catch (error) {
        redirectLogin();
    }
    return true;
};
// ----------------------------------------------------------------
// #region Funciones
const obtenerFechaActual = () => {
    const fechaLocal = new Date(Date.now() - (new Date().getTimezoneOffset() * 60000));
    return fechaLocal.toISOString().slice(0, 10);
};

// Ingresar informacion a las cards
const ventasHoy = async () => {
    console.log(obtenerFechaActual().toString())
    try {
        const response = await fetch(`/api/ventas/hoy?hoy=${obtenerFechaActual().toString()}`, {
            headers: {
                "Authorization": `Bearer: ${verifySession().token}`
            }
        })
        const data = await response.json();
        let ventas = await data.length;
        const elemento = document.getElementById("ventas-hoy-overlay");
        if (elemento) elemento.remove();

        document.getElementById("ventas-hoy").innerHTML = `<h3>${ventas}</h3><p>Ventas Totales Hoy</p>`;
    } catch (e) {
        redirectLogin();
    }
    return true;
}
const usuariosRegistrados = async () => {
    try {
        const response = await fetch(`/api/usuarios/`, {
            headers: {
                "Authorization": `Bearer: ${verifySession().token}`
            }
        })
        const data = await response.json();
        let usuariosRegistrados = 0;
        data.forEach(usuario => {
            usuariosRegistrados++;
        });
        const elemento = document.getElementById("usuarios-registrados-overlay");
        if (elemento) {
            elemento.remove();
        }
        document.getElementById("usuarios-registrados").innerHTML = `<h3>${usuariosRegistrados}</h3><p>Usuarios Registrados</p>`;
    } catch (e) {
        redirectLogin();
    }
    return true;
}
const ventasMes = async () => {
    try {
        const response = await fetch(`/api/ventas/mes?mes=${obtenerFechaActual().toString()}`, {
            headers: {
                "Authorization": `Bearer: ${verifySession().token}`
            }
        })
        const data = await response.json();
        let ventas = await data.length;
        const elemento = document.getElementById("ventas-mes-overlay");
        if (elemento) elemento.remove();

        document.getElementById("ventas-mes").innerHTML = `<h3>${ventas}</h3><p>Ventas Totales del Mes</p>`;
    } catch (e) {
        redirectLogin();
    }
    return true;
}
const productosRegistrados = async () => {
    try {
        const response = await fetch(`/api/productos/`)
        const data = await response.json();
        let productosRegistrados = await data.length;
        const elemento = document.getElementById("productos-registrados-overlay");
        if (elemento) {
            elemento.remove();
        }
        document.getElementById("productos-registrados").innerHTML = `<h3>${productosRegistrados}</h3><p>Productos Registrados</p>`;
    } catch (e) {
        redirectLogin();
    }
    return true;
}
const productosTabla = async () => {
    try {
        const response = await fetch(`/api/ventas/mas_vendidos`, {
            headers: {
                "Authorization": `Bearer: ${verifySession().token}`
            }
        })
        if (response.ok) {
            const tbody = document.querySelector('#tbody-masVendidos');
            let data = await response.json();
            await data.forEach(producto => {
                let tr = `<tr>
                                    <td>${producto.id}</td>
                                    <td>${producto.nombre}</td>
                                    <td>${producto.descripcion}</td>
                                    <td>$ ${producto.precio}</td>
                                    <td>${producto.cantidadStock}</td>
                                </tr>`
                tbody.innerHTML += tr;
            })
        }
    } catch (e) {
        redirectLogin();
    }
    return true;
}


// redireccionar al inicio de sesion
const redirectLogin = () => {
    toastr.remove();
    toastr["error"]("Autenticación de perfil inválido, serás redireccionado al inicio de sesión en segundos");
    localStorage.clear();
    setTimeout(() => {
        window.location.href = "../index.html"
    }, 3000);
}
// ----------------------------------------------------------------
// EVENTOS
// Cerrar Sesion
cerrarSesion.addEventListener("click", (e) => {
    e.preventDefault()
    localStorage.clear();
    window.location.href = "../index.html"
})

// EVENTO AL INICIAR EL PANEL
window.addEventListener("load", () => {
    addInfoSidebar(verifySession());
    ventasHoy();
    usuariosRegistrados();
    ventasMes();
    productosRegistrados();
    productosTabla();
})
btnUsuarios.addEventListener("click", () => {
    event.preventDefault();
    window.location.href = "../pages/usuarios.html"
})
btnProductos.addEventListener("click", () => {
    event.preventDefault();
    window.location.href = "../pages/productos.html"
})
btnVentas.addEventListener("click", () => {
    event.preventDefault();
    window.location.href = "../pages/ventas.html"
})