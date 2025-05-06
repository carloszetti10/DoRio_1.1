package com.projeto.domRio1.doRio.service;

import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.Departamento;
import com.projeto.domRio1.doRio.model.EquiBase;
import com.projeto.domRio1.doRio.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {
    @Autowired
    DepartamentoRepository repository;

    public void salva(Departamento departamento) {
        try {
            Optional<Departamento> bynomeDepartamento = repository.findByNomeDepartamento(departamento.getNomeDepartamento());
            // Se já existe, lança a exceção AvisoException
            if (bynomeDepartamento.isPresent()) {
                throw new AvisoException("Departamento já existe!");
            }
            // Se não existir, salva o equipamento
            repository.save(departamento);
        } catch (AvisoException e) {
            throw e;
        } catch (Exception ex) {
            throw new ErroException("Não foi possível cadastrar o Departamento!");
        }
    }

    public List<Departamento> listarTodos() {
        return repository.findAll();
    }
}
