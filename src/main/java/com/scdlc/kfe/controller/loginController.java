package com.scdlc.kfe.controller;

import com.scdlc.kfe.DTO.Login;
import com.scdlc.kfe.DTO.LoginResponse;
import com.scdlc.kfe.config.JwtFilter;
import com.scdlc.kfe.service.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping(path = "/api/login/")
public class loginController {
    private final UsuarioService usuarioService;

    @Autowired
    public loginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public LoginResponse loginUsuario(@RequestBody Login login) throws ServletException {
        if (usuarioService.validateUsuario( login )) {
            Long id = usuarioService.getIdUsuario( login );
            String token = generateToken( login.getEmail());
            return new LoginResponse( token, id );
        }
        throw new ServletException( "Nombre de usuario o contraseña incorrectos" );
    }

    //? Metodo para generar token
    private String generateToken(String login) {

        Calendar calendar = Calendar.getInstance();
        calendar.add( Calendar.HOUR, 10 );

        return Jwts.builder()
                .setSubject( login )
                .claim( "role", "user" )
                .setIssuedAt( new Date() )
                .setExpiration( calendar.getTime() )
                .signWith( SignatureAlgorithm.HS256, JwtFilter.secret )
                .compact();
    }
}