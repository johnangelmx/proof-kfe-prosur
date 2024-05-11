package com.scdlc.kfe.controller;

import com.scdlc.kfe.DTO.ChangePassword;
import com.scdlc.kfe.model.Usuario;
import com.scdlc.kfe.service.UsuarioService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/usuarios/")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> getAllUsuarios() {

        return usuarioService.getAllUsuarios();
    }

    @GetMapping(path = "{userId}")
    public Usuario getUsuario(@PathVariable("userId") Long id) {

        return usuarioService.getUsuario( id );
    }

    @DeleteMapping(path = "{userId}")
    public Usuario deleteUsuario(@PathVariable("userId") Long id) {

        return usuarioService.deleteUsuario( id );
    }

    //    @PostMapping
    //    public Usuario addUsuario(@RequestBody Usuario usuario) {
    //        return usuarioService.addUsuario( usuario );
    //    }

    @PostMapping
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario user = usuarioService.addUsuario( usuario );
            return new ResponseEntity<>( "Usuario creado exitosamente" + user, HttpStatus.CREATED );
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>( "El usuario ya existe", HttpStatus.CONFLICT );
        }
    }

    @PutMapping(path = "{userId}")
    public Usuario updateUsuario(@PathVariable("userId") Long id, @RequestParam(required = false) String nombre, @RequestParam(required = false) String rol) {

        return usuarioService.updateUsuario( id, nombre, rol );
    }

    @PutMapping(path = "changePassword/{userId}")
    public Usuario updateUsuario(@PathVariable("userId") Long id, @RequestBody ChangePassword changePassword) {

        return usuarioService.updatePasswordUsuario( id, changePassword );
    }

}
