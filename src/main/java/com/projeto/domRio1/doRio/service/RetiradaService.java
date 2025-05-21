package com.projeto.domRio1.doRio.service;

import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.repository.RetiradaRepository;
import com.projeto.domRio1.doRio.utils.PatrimonioCadastroEmprestimo;
import com.projeto.domRio1.doRio.utils.SessaoUsuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class RetiradaService {
    @Autowired
    RetiradaRepository repository;
    @Autowired
    EquipamentoRetiradaService equipamentoRetiradaService;


    @Transactional
    public void salvarRetirada(Pessoa pessoa, Set<PatrimonioCadastroEmprestimo> listaPatrimonio) {
        try {
            for (PatrimonioCadastroEmprestimo p : listaPatrimonio) {
                EquipamentoRetirada equiRet = equipamentoRetiradaService.buscarEquipamentoRetirada(p.getIdEquipamento(), p.getPatrimonioEquipamento());
                if (equiRet != null) {
                    if (p.getQuantidade() > equiRet.getQuantidade()) {
                        //Não ira acontercer, mas decidir colocar
                        throw new AvisoException("Quantidade indisponível!");
                    }
                    Retirada ret = new Retirada();
                    ret.setSolicitante(pessoa);
                    ret.setEquipamento(equiRet);;
                    ret.setDataRetirada(LocalDate.now());
                    ret.setUsuarioQRealizou(SessaoUsuario.getUsuario());
                    ret.setQuantEqui(p.getQuantidade());
                    repository.save(ret);
                    int quantAtualizar = equiRet.getQuantidade() - p.getQuantidade();
                    equipamentoRetiradaService.atualizarQuantidade(quantAtualizar, equiRet);
                } else {
                    // Se o equipamento não for encontrado, algo que não vai acontecer, mas decidir colocar por preucação
                    throw new ErroException("Erro ao realizar a retirada!");
                }
            }

        } catch (AvisoException e) {
            throw e;
        } catch (ErroException e) {
            throw e;
        } catch (Exception ex) {
            // Para outras exceções gerais
            throw new ErroException("Não foi possível realizar a retirada devido a um erro inesperado!");
        }
    }

    public List<Retirada> todosEmprestimo() {
        return repository.findAll();
    }

    public List<Retirada> retiradaHoje(LocalDate hoje){
        return repository.findAllByDataRetirada(hoje);
    }

    public List<Retirada> listarEmpPorSolicitante(String text) {
        return repository.findAllBySolicitanteNomeContainingIgnoreCase(text);
    }

    public List<Retirada> listarPorPatEqui(String text) {
        return  repository.findAllByEquipamentoEquipamentoRetCodigoContainingIgnoreCase(text);
    }
}
