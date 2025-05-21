package com.projeto.domRio1.doRio.service;


import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.repository.EquipamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipamentoService {
    @Autowired
    EquipamentoRepository equipamentoRepository;

    @Autowired
    EquipamentoEmprestimoService equiEmpestimo;

    @Autowired
    EquipamentoRetiradaService equiRetirada;

    @Autowired
    EmprestimoService emprestimoService;

    @Autowired
    RetiradaService retiradaService;

    @Transactional
    public void salvar(Equipamento e, int quantidade) {
        try {
            if (e.getTipo() == TipoEquipamento.EMPRESTIMO) {
                salvarEquipamentoEmprestimo(e, quantidade);
            } else {
                salvarEquipamentoRetirada(e, quantidade);
            }
        } catch (Exception ex) {
            throw new ErroException("Não foi possível cadastrar o Equipamento!");
        }
    }

    private void salvarEquipamentoEmprestimo(Equipamento e, int quantidade) {
        Equipamento existente = findByCodigoAndEquipamentoBase(e.getCodigo(), e.getEquipamentoBase(), TipoEquipamento.EMPRESTIMO);
        if (existente != null && existente.isApagado()) {
            existente.setApagado(false);
            equipamentoRepository.save(existente);
        } else {
            Equipamento novo = equipamentoRepository.save(e);
            cadastrarPorTipo(novo, quantidade);
        }
    }

    private void salvarEquipamentoRetirada(Equipamento e, int quantidade) {
        Equipamento existente = findByCodigoAndEquipamentoBase(e.getCodigo(), e.getEquipamentoBase(), TipoEquipamento.RETIRADA);
        if (existente != null && existente.isApagado()) {
            EquipamentoRetirada retirada = equiRetirada.buscarEquipamentoRetirada(existente.getId(), e.getCodigo());
            retirada.setQuantidade(quantidade);
            equiRetirada.salva(retirada);
            existente.setApagado(false);
            equipamentoRepository.save(existente);
        } else {
            Equipamento novo = equipamentoRepository.save(e);
            cadastrarPorTipo(novo, quantidade);
        }
    }

    @Transactional
    void cadastrarPorTipo(Equipamento e, int quant){
        if(e.getTipo().equals(TipoEquipamento.EMPRESTIMO)){
            EquipamentoEmprestimo emprestimo = new EquipamentoEmprestimo();
            emprestimo.setEquipamentoEmp(e);
            emprestimo.setDisponivel(true);
            equiEmpestimo.salvar(emprestimo);
        }else{
            EquipamentoRetirada retirada = new EquipamentoRetirada();
            retirada.setEquipamentoRet(e);
            retirada.setQuantidade(quant);
            equiRetirada.salva(retirada);
        }
    }


    public List<Equipamento> buscarTodos() {
        return equipamentoRepository.findAll();
    }


    public Equipamento findByCodigoAndEquipamentoBase(String patrimonioValue, EquiBase equipamentoValue, TipoEquipamento tipoEquipamento) {
        return equipamentoRepository.findByCodigoAndEquipamentoBaseAndTipo(patrimonioValue, equipamentoValue, tipoEquipamento);
    }


    @Transactional
    public List<Equipamento> listarPorEquiBase(EquiBase equiBase) {
        return equipamentoRepository.findAllByEquipamentoBase(equiBase);
    }

    public void setarApagado(Equipamento equipamento){
        try {
            if(equipamento.getTipo() == TipoEquipamento.RETIRADA){
                equipamento.setApagado(true);
                equipamentoRepository.save(equipamento);
            } else{
                EquipamentoEmprestimo equipamentoEmprestimo = equiEmpestimo
                        .buscarEquipamentoEmprestimo(equipamento.getId(), equipamento.getCodigo());
                Emprestimo emprestimo = emprestimoService.emprestimoAberto(equipamentoEmprestimo);
                if(emprestimo != null){
                    throw new AvisoException("Não é possível excluir: empréstimo ativo.");
                }else{
                    equipamento.setApagado(true);
                    equipamentoRepository.save(equipamento);
                }
            }
        }catch (AvisoException aviso){
            throw aviso;
        }catch (Exception e){
            throw new RuntimeException("Falha ao excluir equipamento!", e);
        }

    }

    public Optional<Equipamento> findById(Long id) {
        return equipamentoRepository.findById(id);
    }


    @Transactional
    public boolean empRetAberto(Equipamento e){
        boolean existe = false;
        if(e.getTipo() == TipoEquipamento.EMPRESTIMO){
            EquipamentoEmprestimo equipamentoEmprestimo = equiEmpestimo.buscarEquipamentoEmprestimo(e.getId(), e.getCodigo());
            Emprestimo emprestimo = emprestimoService.emprestimoAberto(equipamentoEmprestimo);
            if (emprestimo != null){
                existe = true;
            }else {
                    existe = false;
            }
        }
        return existe;
    }

    public List<Equipamento> listaEquiTipoEmprestimo(TipoEquipamento tipo) {
        return equipamentoRepository.findAllByTipo(tipo);
    }

    @Transactional
    public void editar(Equipamento equi, String tObs, int quant) {
        try {
            Equipamento ed = new Equipamento();
            ed.setId(equi.getId());
            ed.setApagado(equi.isApagado());
            ed.setCodigo(equi.getCodigo());
            ed.setObs(tObs);
            ed.setEquipamentoBase(equi.getEquipamentoBase());
            ed.setTipo(equi.getTipo());
            equipamentoRepository.save(ed);
            if(equi.getTipo() == TipoEquipamento.RETIRADA){
                EquipamentoRetirada retirada = equiRetirada.buscarEquipamentoRetirada(equi.getId(), equi.getCodigo());
                int quantAt = retirada.getQuantidade() + quant;

                retirada.setEquipamentoRet(ed);
                retirada.setQuantidade(quantAt);
                equiRetirada.editar(retirada);
            }
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível editar o equipamento.");
        }
    }
}
