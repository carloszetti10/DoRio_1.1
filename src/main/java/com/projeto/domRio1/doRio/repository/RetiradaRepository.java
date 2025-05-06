package com.projeto.domRio1.doRio.repository;

import com.projeto.domRio1.doRio.model.Equipamento;
import com.projeto.domRio1.doRio.model.EquipamentoRetirada;
import com.projeto.domRio1.doRio.model.Retirada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetiradaRepository extends JpaRepository<Retirada, Long> {

}
