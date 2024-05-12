// DOM ⬇
let inEmail = document.getElementById("inEmail"), inPassword = document.getElementById("inPassword"),
    btnLogin = document.getElementById("btnLogin");
// Variables

// Regex ⬇
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
    passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])([^\s]){8,20}$/;

// Funciones ⬇
const regexPass = (email, password) => {
    if (email != null && emailRegex.test(email)) {
        if (password != null && passwordRegex.test(password)) {
            return true;
        }
        toastr["error"]("Por favor, verifique que la contraseña esté correctamente escrita", "Ingreso Invalido")
        return false
    }
    toastr["error"]("Por favor, verifique que el correo esté correctamente escrito", "Ingreso Invalido")
    return false;
}
const verifyLogin = async () => {
    let body = {
        email: `${inEmail.value}`, contrasena: `${inPassword.value}`
    };
    try {
        const resp = await fetch(`/api/login/`, {
            method: 'POST',
            headers: new Headers({'Content-type': 'application/json'}),
            mode: 'cors',
            body: JSON.stringify(body)
        });

        if (resp.ok) {
            const data = await resp.json();
            try {
                let response = await fetch(`/api/usuarios/${data.idUsuario}`, {
                    headers: {
                        "Authorization": "Bearer: " + data.token
                    }
                });
                response = await response.json();
                if (response.rol === "admin") {
                    localStorage.setItem('sessionId', JSON.stringify(data));
                    toastr.success('¡Operación exitosa!', 'Éxito');
                    window.location.href = "./pages/dashboard.html";
                } else {
                    toastr["error"]("Usted no tiene permiso para este ingreso", "Ingreso Invalido")
                }
            } catch {
                toastr["error"]("Por favor verifique sus credenciales", "Ingreso Invalido")
            }
        }else{
            toastr["error"]("Por favor verifique sus credenciales", "Ingreso Invalido")
        }
    } catch (error) {
        toastr["error"]("Por favor verifique sus credenciales", "Ingreso Invalido")
    }
}
// Listeners ⬇
btnLogin.addEventListener("click", (e) => {
    e.preventDefault();
    if (regexPass(inEmail.value.trim(), inPassword.value.trim())) {
        verifyLogin();
    }
})



