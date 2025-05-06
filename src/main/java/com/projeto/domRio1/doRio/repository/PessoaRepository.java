package com.projeto.domRio1.doRio.repository;

import com.projeto.domRio1.doRio.model.Departamento;
import com.projeto.domRio1.doRio.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Optional<Pessoa> findByNomeAndSetor(String nome, Departamento setor);
}
