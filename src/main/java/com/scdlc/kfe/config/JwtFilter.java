package com.scdlc.kfe.config;

import io.jsonwebtoken.*;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {
    public static String secret = "CH25#La_Cohorte_Actual_123456789%%%";

    // Los parámetros del método son:
    //
    //ServletRequest request: Representa la solicitud HTTP entrante.
    //ServletResponse response: Representa la respuesta HTTP saliente.
    //FilterChain chain: Es un objeto que permite invocar el siguiente filtro en la cadena o,
    // si este filtro es el último en la cadena, invocar al servlet o controlador correspondiente.  ⬇️
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authHeader = httpServletRequest.getHeader( "authorization" );
        if (("POST".equals( httpServletRequest.getMethod()  )) && !httpServletRequest.getRequestURI().contains("/api/usuarios/") ||
                (("GET".equals( httpServletRequest.getMethod() )) && (!httpServletRequest.getRequestURI().contains( "/api/productos/" )))
                || ("PUT".equals( httpServletRequest.getMethod() )) || ("DELETE".equals( httpServletRequest.getMethod() ))) {
            if (authHeader == null || !authHeader.startsWith( "Bearer: " )) {
                throw new ServletException( "1. Invalid Token" );
            }// if authHedaer
            String token = authHeader.substring( 7 );
            try {
                Claims claims = Jwts.parser().setSigningKey( secret ).parseClaimsJws( token ).getBody();
                claims.forEach( (key, value) -> {
                    System.out.println( "key: " + key + " value: " + value );
                } );
            } catch (SignatureException | MalformedJwtException | ExpiredJwtException e) {
                throw new ServletException( "2. Invalid Token." );
            }
        }
        chain.doFilter( request, response );

    }
}
