package com.scdlc.kfe.config;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

//? GenericFilterBean es una clase abstracta que proporciona una base conveniente para crear filtros personalizados en una aplicación web. ⬇️
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
        String authHeader = httpServletRequest.getHeader("authorization");
        if (("POST".equals(httpServletRequest.getMethod())) || (("GET".equals(httpServletRequest.getMethod())) && (!httpServletRequest.getRequestURI().contains("/api/productos/"))) || ("PUT".equals(httpServletRequest.getMethod())) || ("DELETE".equals(httpServletRequest.getMethod()))) {
            if (authHeader == null || !authHeader.startsWith("Bearer: ")) {
                throw new ServletException("1. Invalid Token");
            }// if authHedaer
            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
                claims.forEach((key, value) -> {
                    System.out.println("key: " + key + " value: " + value);
                });
            } catch (SignatureException | MalformedJwtException | ExpiredJwtException e) {
                throw new ServletException("2. Invalid Token.");
            }//catch
        }// if methods
        chain.doFilter(request, response);

    }
}
