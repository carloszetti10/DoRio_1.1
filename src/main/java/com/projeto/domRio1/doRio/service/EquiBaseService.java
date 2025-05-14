package com.projeto.domRio1.doRio.service;


import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.EquiBase;
import com.projeto.domRio1.doRio.model.TipoEquipamento;
import com.projeto.domRio1.doRio.repository.EquiBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EquiBaseService {


    @Autowired
    EquiBaseRepository repository;

    public void salva(EquiBase equi) {
        try {
            Optional<EquiBase> byNomeAndModelo = repository.findByNomeContainingIgnoreCaseAndModeloContainingIgnoreCaseAndTipo(equi.getNome(), equi.getModelo(), equi.getTipo());

            if (byNomeAndModelo.isPresent()) {
                throw new AvisoException("Equipamento já existe!");
            }

            repository.save(equi);

        } catch (AvisoException e) {
            throw e; // ← Esse bloco pode ser removido
        } catch (Exception ex) {
            throw new ErroException("Não foi possível cadastrar o Equipamento!");
        }
    }

    public List<TipoEquipamento> findAll() {
        List<EquiBase> all = repository.findAll();
        return new ArrayList<>();
    }

    public List<EquiBase> buscarTodos() {
        return repository.findAll();
    }

    public Optional<EquiBase> buscarPorId(Long id){
        return repository.findById(id);
    }

    public List<EquiBase> buscarTodosEquiTipo(TipoEquipamento tipo) {
        return repository.findAllByTipo(tipo);
    }
}
