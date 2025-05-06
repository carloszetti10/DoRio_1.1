package com.projeto.domRio1.doRio.service.pessoa;

import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.Departamento;
import com.projeto.domRio1.doRio.model.Emprestimo;
import com.projeto.domRio1.doRio.model.Pessoa;
import com.projeto.domRio1.doRio.repository.PessoaRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository repository;

    public void salva(Pessoa p) {
        try {
            Optional<Pessoa> byPessoa = repository.findByNomeAndSetor(p.getNome(), p.getSetor());
            // Se já existe, lança a exceção AvisoException
            if (byPessoa.isPresent()) {
                throw new AvisoException("Pessoa já existe!");
            }
            // Se não existir, salva o equipamento
            repository.save(p);
        } catch (AvisoException e) {
            throw e;
        } catch (Exception ex) {
            throw new ErroException("Não foi possível cadastrar o Solicitante!!");
        }
    }

    public List<Pessoa> listarTodos(){
        return repository.findAll();
    }



    @Transactional
    public List<Emprestimo> todosEmprestimoNaoDevolvido(Long idPessoa) {
        Pessoa pessoa = repository.findById(idPessoa).orElseThrow();

        // Força o carregamento da lista antes de fechar a sessão
        Hibernate.initialize(pessoa.getEmprestimos());

        return pessoa.getEmprestimos().stream()
                .filter(e -> !e.getEntregue()) // Filtra os não devolvidos
                .collect(Collectors.toList());
    }

    public Optional<Pessoa> findById(Long id) {
        return repository.findById(id);
    }
}
