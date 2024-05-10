package com.scdlc.kfe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration // Anotacion para declarar que es configuracion de inicio
@EnableWebSecurity // Anotacion para que en la solicitud de user and password va a utilizar por defecto
public class SecurityConfig {
    //Medoto parte del Security Filter Chain ⬇️
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.csrf( csrf -> csrf.disable() ).authorizeRequests( auth -> {
            auth.antMatchers( "/" ).permitAll();
        } ).httpBasic( withDefaults() ).build();
    }//configure

    //Metodo que permite cifra y desifrar
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}// class SecurityConfig ⬆
