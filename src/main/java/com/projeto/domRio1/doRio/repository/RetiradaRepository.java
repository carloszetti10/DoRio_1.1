package com.projeto.domRio1.doRio.repository;

import com.projeto.domRio1.doRio.model.Retirada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RetiradaRepository extends JpaRepository<Retirada, Long> {
    List<Retirada> findAllByDataRetirada(LocalDate hoje);
    
    List<Retirada> findAllBySolicitanteNomeContainingIgnoreCase(String text);

    List<Retirada> findAllByEquipamentoEquipamentoRetCodigoContainingIgnoreCase(String text);
}
