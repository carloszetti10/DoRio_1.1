package com.projeto.domRio1.doRio.service;

import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.repository.EmprestimoRepository;
import com.projeto.domRio1.doRio.utils.PatrimonioCadastroEmprestimo;
import com.projeto.domRio1.doRio.utils.SessaoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmprestimoService {
    @Autowired
    EmprestimoRepository repository;
    @Autowired
    EquipamentoEmprestimoService equipamentoEmprestimoService;

    public List<Emprestimo> todosEmprestimo(){
        return repository.findAll();
    }

    public Emprestimo emprestimoAberto(EquipamentoEmprestimo e){
        return repository.findByEquipamentoAndEntregueFalse(e);
    }

    public List<Emprestimo> emprestimosPendentes(){
        return repository.findAllByEntregueFalse();
    }

    @Transactional
    public void salvarEmprestimo(Pessoa pessoa, Set<PatrimonioCadastroEmprestimo> listaPatrimonio) {
        try {
            for (PatrimonioCadastroEmprestimo p : listaPatrimonio) {
                EquipamentoEmprestimo equiEmpre = equipamentoEmprestimoService.buscarEquipamentoEmprestimo(p.getIdEquipamento(), p.getPatrimonioEquipamento());
                if (equiEmpre != null) {
                    if (!equiEmpre.getDisponivel()) {
                        throw new AvisoException("Verifique se todos os equipamentos estão disponíveis.");
                    }
                    Emprestimo emp = new Emprestimo();
                    emp.setPessoa(pessoa);
                    emp.setEquipamento(equiEmpre);
                    emp.setDataEmprestimo(LocalDate.now());
                    emp.setQuemEmprestou(SessaoUsuario.getUsuario());
                    repository.save(emp);
                    equipamentoEmprestimoService.atualizarStatusEqui(false, equiEmpre);
                } else {
                    // Se o equipamento não for encontrado, algo que não vai acontecer, mas decidir colocar por preucação
                    throw new ErroException("Não foi possivel realizar o emprestimo!");
                }
            }

        } catch (AvisoException e) {
            throw e;
        } catch (ErroException e) {
            throw e;
        } catch (Exception ex) {
            // Para outras exceções gerais
            throw new ErroException("Não foi possível realizar o empréstimo devido a um erro inesperado!");
        }
    }

    public Optional<Emprestimo> findById(Long id) {
        return repository.findById(id);
    }

    public List<Emprestimo> listarEmpPorData(LocalDate now) {
        return repository.findByDataEmprestimo(now);
    }

    public List<Emprestimo> listarEmpPorSolicitante(String text) {
        return repository.findAllByPessoaNomeContainingIgnoreCase(text);
    }

    public List<Emprestimo> listarPorPatEqui(String text) {
        return repository.findAllByEquipamentoEquipamentoEmpCodigoContainingIgnoreCase(text);
    }
}
