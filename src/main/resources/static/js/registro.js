// #region DOM
let inNombre = document.querySelector('#inNombre');
let inCorreo = document.querySelector('#inCorreo');
let inContrasena = document.querySelector('#inContrasena');
let confirmar = document.querySelector('#confirmar');
let btnRegistrarse = document.querySelector('#btnRegistrarse');

// #region Variables

// #region Funciones
const validarCampos = () => {
    const nombreRegex = /^[a-zA-Z\s]{1,30}$/;
    const correoRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const contrasenaRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+|~=`{}\[\]:;"'<>?,./\\-])[^\s]{8,20}$/;
    const confirmarContrasenaRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+|~=`{}\[\]:;"'<>?,./\\-])[^\s]{8,20}$/;

    if (!nombreRegex.test(inNombre.value)) {
        toastr["error"]("El nombre debe contener solo letras y espacios, de 2 a 30 caracteres.");
        return false;
    }
    if (!correoRegex.test(inCorreo.value)) {
        toastr["error"]("El correo electrónico no es válido.");
        return false;
    }
    if (!contrasenaRegex.test(inContrasena.value)) {
        toastr["info"]("Debe contener al menos una letra minúscula, una letra mayúscula, un dígito, un carácter especial y ser de longitud entre 8 y 20 caracteres.");
        toastr["error"]("La contraseña no cumple con los requisitos.");
        return false;
    }
    if (!confirmarContrasenaRegex.test(confirmar.value)) {
        toastr["error"]("La confirmación de contraseña no coincide o no cumple con los requisitos.");
        return false;
    }
    return true;
}
const verificarContrasenas = () => {
    if (inContrasena.value !== confirmar.value) {
        toastr["error"]("La contraseña y Confirmar la contraseña no coinciden. Verificar Campos");
        return false;
    }
    return true;
}

const registrarUsuario = async () => {
    let body = {
        nombre: `${inNombre.value}`, email: `${inCorreo.value}`, contrasena: `${inContrasena.value}`, rol: 'user'
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

// #region Listeners
btnRegistrarse.addEventListener('click', async function (event) {
    event.preventDefault();
    if (validarCampos() && verificarContrasenas()) {
        if (await registrarUsuario()) {
            toastr["success"]("Todos los campos son válidos. ¡Registrado exitosamente!");
            toastr["info"]("Sera redirigido al incio de sesion en unos segundos");
            setTimeout(function () {
                window.location.href = "./../index.html";
            }, 3000)
        }
    }
});





