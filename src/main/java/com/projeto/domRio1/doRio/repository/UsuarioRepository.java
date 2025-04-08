package com.projeto.domRio1.doRio.repository;

import com.projeto.domRio1.doRio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
}
