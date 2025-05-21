package com.projeto.domRio1.doRio.repository;

import com.projeto.domRio1.doRio.model.Emprestimo;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    Emprestimo findByEquipamentoAndEntregueFalse(EquipamentoEmprestimo equipamento);

    List<Emprestimo> findAllByEntregueFalse();

    List<Emprestimo> findByDataEmprestimo(LocalDate now);

    List<Emprestimo> findAllByPessoaNomeContainingIgnoreCase(String text);

    List<Emprestimo> findAllByEquipamentoEquipamentoEmpCodigoContainingIgnoreCase(String text);


    List<Emprestimo> findAllByEquipamentoEquipamentoEmpCodigoContaining(String text);
}
