package com.scdlc.kfe.service;

import com.scdlc.kfe.DTO.ChangePassword;
import com.scdlc.kfe.model.Usuario;
import com.scdlc.kfe.model.Venta;
import com.scdlc.kfe.repository.UsuarioRepository;
import com.scdlc.kfe.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final VentaRepository ventaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, VentaRepository ventaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.ventaRepository = ventaRepository;
    }


    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuario(Long id) {
        return usuarioRepository.findById( id ).orElseThrow( () -> new IllegalArgumentException( "El usuario " + id + " no existe" ) );
    }

    public Usuario deleteUsuario(Long id) {
        if (usuarioRepository.existsById( id )) {
            Usuario tmpUser = usuarioRepository.findById( id ).get();
//            Recursividad en la DB
            List<Venta> ventas = ventaRepository.findByUsuario( tmpUser );
            ventaRepository.deleteAll( ventas );
            usuarioRepository.deleteById( id );
            return tmpUser;
        } else {
            throw new IllegalArgumentException( "El usuario " + id + " no existe" );
        }
    }

    public Usuario addUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail( usuario.getEmail() ).isEmpty()) {
            usuario.setContrasena( passwordEncoder.encode( usuario.getContrasena() ) );
            return usuarioRepository.save( usuario );
        } else {
            throw new IllegalArgumentException( "El usuario con correo" + usuario.getEmail() + " ya esta registrado" );
        }
    }

    public Usuario updateUsuario(Long id, String nombre, String rol) {
        if (usuarioRepository.existsById( id )) {
            Usuario tmp = usuarioRepository.findById( id ).get();
            if (nombre != null) tmp.setNombre( nombre );
            if (rol != null) tmp.setRol( rol );
            usuarioRepository.save( tmp );
            return tmp;
        } else {
            throw new IllegalArgumentException( "El ID: " + id + " No existe" );
        }
    }

    public Usuario updatePasswordUsuario(Long id, ChangePassword changePassword) {
        if (!usuarioRepository.existsById( id )) throw new IllegalArgumentException( "El ID: " + id + " No existe" );
        if (changePassword.getOldPassword() == null || changePassword.getNewPassword() == null)
            throw new IllegalArgumentException( "oldpassword o newpassword vienen nulo" );

        Usuario tmpUser = usuarioRepository.findById( id ).get();

        if (passwordEncoder.matches( changePassword.getOldPassword(), tmpUser.getContrasena() )) {
            tmpUser.setContrasena( passwordEncoder.encode( changePassword.getNewPassword() ) );
            usuarioRepository.save( tmpUser );
            return tmpUser;
        } else {
            throw new IllegalArgumentException( " La oldpassword no coincide con la constrasena" );
        }
    }

//    public boolean validateUsuario(Usuario usuario) {
//        Optional<Usuario> userByEmail = usuarioRepository.findByEmail(usuario.getEmail());
//        if (userByEmail.isPresent()) {
//            Usuario user = userByEmail.get();
////            if (user.getPassword().equals(usuario.getPassword())) {
//            if (passwordEncoder.matches(usuario.getPassword(), user.getPassword())) { //nuevo encoder
//                return true;
//            }
//
//        }
//        return false;
//    }


}
