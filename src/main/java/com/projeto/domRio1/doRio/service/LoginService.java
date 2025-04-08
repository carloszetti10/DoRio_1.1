package com.projeto.domRio1.doRio.service;

import com.projeto.domRio1.doRio.exception.ExceptionLogin;
import com.projeto.domRio1.doRio.model.Usuario;
import com.projeto.domRio1.doRio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UsuarioRepository repository;

    public Usuario login(String usuario, String senha){

        if(usuario.isEmpty()){
            throw new ExceptionLogin("preecha o usuario!");
        }else {
            Usuario salva = new Usuario();
            salva.setNome(usuario);
            repository.save(salva);
        }
        Usuario u = repository.findById(1L).orElseThrow(()-> new ExceptionLogin("Usuario não encontrado"));
        return u;
    }
}
