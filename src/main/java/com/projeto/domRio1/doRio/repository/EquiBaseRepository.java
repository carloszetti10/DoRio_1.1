package com.projeto.domRio1.doRio.repository;


import com.projeto.domRio1.doRio.model.EquiBase;
import com.projeto.domRio1.doRio.model.TipoEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EquiBaseRepository extends JpaRepository<EquiBase, Long> {
    Optional<EquiBase> findByNomeAndModelo(String nome, String modelo);

    Optional<EquiBase> findByNomeIgnoreCaseAndModeloIgnoreCase(String nome, String modelo);

    List<EquiBase> findAllByTipo(TipoEquipamento tipo);

    Optional<EquiBase> findByNomeContainingIgnoreCaseAndModeloContainingIgnoreCaseAndTipo(String nome, String modelo, TipoEquipamento tipo);


    //@Query("SELECT e FROM EquiBase e WHERE LOWER(e.nome) = LOWER(:nome) AND LOWER(e.modelo) = LOWER(:modelo)")
    //Optional<EquiBase> findByNomeAndModelo(@Param("nome") String nome, @Param("modelo") String modelo);
}
