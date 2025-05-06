package com.projeto.domRio1.doRio.repository;

import com.projeto.domRio1.doRio.model.Emprestimo;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    Emprestimo findByEquipamentoAndEntregueFalse(EquipamentoEmprestimo equipamento);

    List<Emprestimo> findAllByEntregueFalse();


}
