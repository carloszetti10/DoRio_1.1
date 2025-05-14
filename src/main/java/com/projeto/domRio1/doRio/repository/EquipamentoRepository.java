package com.projeto.domRio1.doRio.repository;


import com.projeto.domRio1.doRio.model.EquiBase;
import com.projeto.domRio1.doRio.model.Equipamento;
import com.projeto.domRio1.doRio.model.TipoEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    List<Equipamento> findAllByEquipamentoBase(EquiBase equiBase);

    Equipamento findByCodigoAndEquipamentoBaseAndTipo(String patrimonioValue, EquiBase equipamentoValue, TipoEquipamento tipoEquipamento);

    List<Equipamento> findAllByTipo(TipoEquipamento tipo);
}
