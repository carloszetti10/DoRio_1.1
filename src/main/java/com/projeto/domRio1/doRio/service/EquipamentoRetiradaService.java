package com.projeto.domRio1.doRio.service;


import com.projeto.domRio1.doRio.model.Equipamento;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import com.projeto.domRio1.doRio.model.EquipamentoRetirada;
import com.projeto.domRio1.doRio.model.Retirada;
import com.projeto.domRio1.doRio.repository.EquipamentoRetiradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EquipamentoRetiradaService {

    @Autowired
    EquipamentoRetiradaRepository repository;

    public EquipamentoRetirada salva(EquipamentoRetirada e){
        return repository.save(e);
    }

    public List<EquipamentoRetirada> listarTodos() {
        return repository.findAllByEquipamentoRetApagadoFalse();
    }

    public EquipamentoRetirada findbyEquipamentoRet(Equipamento equiBanco) {
        return repository.findByEquipamentoRet(equiBanco);
    }

    public EquipamentoRetirada buscarEquipamentoRetirada(Long idEquipamento, String patrimonioEquipamento) {
        List<EquipamentoRetirada> listaEquipamentos = repository.findByEquipamentoId(idEquipamento);
        for (EquipamentoRetirada e : listaEquipamentos){
            if (e.getEquipamentoRet().getCodigo().equals(patrimonioEquipamento)){
                return e;
            }
        }
        return null;
    }

    public void atualizarQuantidade(int quantAtualizar, EquipamentoRetirada equiRet) {
        equiRet.setQuantidade(quantAtualizar);
        repository.save(equiRet);
    }

    public void editar(EquipamentoRetirada equi) {
        repository.save(equi);
    }

    public List<EquipamentoRetirada> buscarPorPat(String text) {
        return repository.findAllByEquipamentoRetApagadoFalseAndEquipamentoRetCodigoContainingIgnoreCase(text);
    }
}
