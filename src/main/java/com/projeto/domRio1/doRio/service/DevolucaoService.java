package com.projeto.domRio1.doRio.service;

import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.Emprestimo;
import com.projeto.domRio1.doRio.model.Usuario;
import com.projeto.domRio1.doRio.repository.EmprestimoRepository;
import com.projeto.domRio1.doRio.utils.SessaoUsuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class DevolucaoService {

    @Autowired
    EmprestimoRepository repository;

    @Autowired
    EquipamentoEmprestimoService equiService;

    @Transactional
    public void devolver(Set<Emprestimo>emprestimos){
        try {
            Usuario usuario = SessaoUsuario.getUsuario();
            for(Emprestimo e : emprestimos){
                e.setEntregue(true);
                e.setDataEntrega(LocalDate.now());
                e.setQuemRecebeu(usuario);
                repository.save(e);
                equiService.atualizarEquiDevolvido(e.getEquipamento());
            }
        }catch (Exception ex){
            throw new ErroException("Não foi possível registrar devolução!");
        }
    }


}
