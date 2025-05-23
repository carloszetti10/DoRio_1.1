package com.projeto.domRio1.doRio.service;

import com.projeto.domRio1.doRio.model.EquiBase;
import com.projeto.domRio1.doRio.model.Equipamento;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import com.projeto.domRio1.doRio.repository.EquipamentoEmprestimoRepository;
import com.projeto.domRio1.doRio.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class EquipamentoEmprestimoService {
    @Autowired
    EquipamentoEmprestimoRepository repository;

    @Autowired
    EquipamentoRepository equipamentoRepository;

    @Autowired
    EquiBaseService equiBaseService;

    @Autowired
    @Lazy
    EmprestimoService emprestimoService;

    public EmprestimoService returnEmprestimoService(){
        return emprestimoService;
    }

    public EquipamentoEmprestimo salvar(EquipamentoEmprestimo e){
        return repository.save(e);
    }

    public List<EquipamentoEmprestimo> listarTodos() {
        return repository.findAllByEquipamentoEmpApagadoFalse();
    }

    public Optional<EquipamentoEmprestimo> findById(Long id){
        return repository.findById(id);
    }


    public List<EquipamentoEmprestimo> listarEquipamento(EquipamentoEmprestimo equiEmprestimo) {
       // return repository.findAllById(equiEmprestimo)
        return null;
    }

    @Transactional
    public EquipamentoEmprestimo buscarEquipamentoEmprestimo(Long idEquipamento, String patrimonioEquipamento) {

        List<EquipamentoEmprestimo> listaEquipamentos = repository.findByEquipamentoId(idEquipamento);
        for (EquipamentoEmprestimo e : listaEquipamentos){
           if (e.getEquipamentoEmp().getCodigo().equals(patrimonioEquipamento)){
               return e;
           }
        }
        return null;
    }

    public void atualizarStatusEqui(boolean b, EquipamentoEmprestimo equiEmpre) {
        equiEmpre.setDisponivel(b);
        repository.save(equiEmpre);
    }

    public void atualizarEquiDevolvido(EquipamentoEmprestimo equipamento) {
        equipamento.setDisponivel(true);
        repository.save(equipamento);
    }


    public List<EquipamentoEmprestimo> listarEquiEmpPorEquiBase(EquiBase value) {
        return repository.findByEquipamentoEmpEquipamentoBaseAndEquipamentoEmpApagadoFalse(value);
    }


    public List<EquipamentoEmprestimo> pesquisar(String text, EquiBase value) {
        List<EquipamentoEmprestimo> list = List.of();
        if (!text.trim().isEmpty() && value != null) {
            list = repository.findByEquipamentoEmpEquipamentoBaseAndEquipamentoEmpCodigoAndEquipamentoEmpApagadoFalse(value, text);
        } else if (text.trim().isEmpty()) {
            list = repository.findByEquipamentoEmpEquipamentoBaseAndEquipamentoEmpApagadoFalse(value);
        } else if (value == null) {
            list = repository.findByEquipamentoEmpCodigoContainingIgnoreCaseAndEquipamentoEmpApagadoFalse(text);
        }
        return list;

    }

    public List<EquipamentoEmprestimo> buscarPorPat(String text) {
        return repository.findAllByEquipamentoEmpApagadoFalseAndEquipamentoEmpCodigoContainingIgnoreCase(text);
    }
}