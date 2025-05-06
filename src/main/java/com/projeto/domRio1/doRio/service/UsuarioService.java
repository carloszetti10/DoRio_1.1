package com.projeto.domRio1.doRio.service;


import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.model.Usuario;
import com.projeto.domRio1.doRio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();



    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }


    public boolean autenticar(String usuario, String senha) {
        Optional<Usuario> opt = usuarioRepository.findByUsuarioLogar(usuario);
        return opt.isPresent() && encoder.matches(senha, opt.get().getSenha());
    }

    public void salvarUsuario(Usuario u) {
        Optional<Usuario> by = usuarioRepository.findByUsuarioLogar(u.getUsuarioLogar());
        if (by.isPresent()) {
            throw new AvisoException("Nome de usuário já existente!");
        }else {
            u.setSenha(encoder.encode(u.getSenha()));
            usuarioRepository.save(u);
        }

    }

    public Optional<Usuario> getUsuario(String usuario) {
        return usuarioRepository.findByUsuarioLogar(usuario);
    }


    public void apagar(Usuario u){
        usuarioRepository.delete(u);
        System.out.println("Apagado -----------------");
    }


}
