package com.projeto.domRio1.doRio.utils;

import com.projeto.domRio1.doRio.model.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class SessaoUsuario {

    @Getter
    @Setter
    private static Usuario usuario;

    public static boolean isLogado() {
        return usuario != null;
    }

    public static void logout() {
        usuario = null;
    }
}
