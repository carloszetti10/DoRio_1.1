package com.projeto.domRio1.doRio.config;

import com.projeto.domRio1.doRio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IniciarBanco {

    @Autowired
    private UsuarioRepository repository;

    @Bean
    public CommandLineRunner getCommadLine(){
        return args -> {

        };
    }
}
