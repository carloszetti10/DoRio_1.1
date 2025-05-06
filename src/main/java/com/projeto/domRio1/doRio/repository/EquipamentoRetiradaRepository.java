package com.projeto.domRio1.doRio.repository;


import com.projeto.domRio1.doRio.model.Equipamento;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import com.projeto.domRio1.doRio.model.EquipamentoRetirada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipamentoRetiradaRepository extends JpaRepository<EquipamentoRetirada, Long> {
    EquipamentoRetirada findByEquipamentoRet(Equipamento equipamento);

    @Query("SELECT e FROM EquipamentoRetirada e WHERE e.equipamentoRet.id = :equipamentoId")
    List<EquipamentoRetirada> findByEquipamentoId(@Param("equipamentoId") Long equipamentoId);


    List<EquipamentoRetirada> findAllByEquipamentoRetApagadoFalse();
}
