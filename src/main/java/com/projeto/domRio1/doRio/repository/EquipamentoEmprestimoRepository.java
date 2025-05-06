package com.projeto.domRio1.doRio.repository;


import com.projeto.domRio1.doRio.model.EquiBase;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EquipamentoEmprestimoRepository extends JpaRepository<EquipamentoEmprestimo, Long> {

    @Query("SELECT e FROM EquipamentoEmprestimo e WHERE e.equipamentoEmp.id = :equipamentoId")
    List<EquipamentoEmprestimo> findByEquipamentoId(@Param("equipamentoId") Long equipamentoId);


    List<EquipamentoEmprestimo> findAllByEquipamentoEmpApagadoFalse();
}

