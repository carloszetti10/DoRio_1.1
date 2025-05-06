package com.projeto.domRio1.doRio.repository;

import com.projeto.domRio1.doRio.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Optional<Departamento> findByNomeDepartamento(String nomeDepartamento);
}
