package com.scdlc.kfe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/login/")
public class loginController {
//    private final UsuarioService usuarioService;
//
//    @Autowired
//    public loginController(UsuarioService usuarioService) {
//        this.usuarioService = usuarioService;
//    }
//
//    @PostMapping
//    public Token loginUsuario(@RequestBody Usuario usuario) throws ServletException {
//        Usuario tmpUsuario = null;
//        if (usuarioService.validateUsuario(usuario)) {
//            return new Token(generateToken(usuario.getEmail()));
//        }
//        throw new ServletException(" nombre usuario o contrasena incorretos");
//    }
//
//    //? Metodo para generar token ⬇
//    private String generateToken(String username) {
//        // Creando calendario para establecer tiempo de caducidad de JWT ⬇️
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.HOUR, 10);
//        // Creando instancia JWT ⬇️
//        return Jwts.builder() // Retorna y crea los parametros para generar el token
//                .setSubject(username) // Subjet nombre usuario
//                .claim("role", "user") //  asignacion nombre valor
//                .setIssuedAt(new Date()) // datos actuales
//                .setExpiration(calendar.getTime()) // datos fecha de expiracion
//                .signWith(SignatureAlgorithm.HS256, JwtFilter.secret) // tipo de encriptacion más frase de seguridad
//                .compact();
//    }// ⬆ Method generateToken
}// class logincontroller
