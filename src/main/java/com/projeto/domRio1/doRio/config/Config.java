package com.projeto.domRio1.doRio.config;


import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.repository.EquiBaseRepository;
import com.projeto.domRio1.doRio.repository.EquipamentoEmprestimoRepository;
import com.projeto.domRio1.doRio.repository.EquipamentoRepository;
import com.projeto.domRio1.doRio.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class Config {


    @Autowired
    private UsuarioService service;
    @Bean
    public CommandLineRunner getCommadLine(){
        return args -> {
            Usuario u = new Usuario();
            u.setNome("Admin");
            u.setUsuarioLogar("adm");
            u.setSenha("adm");
            u.setTipo(TipoUsuario.Administrador);
            cadastrarEquipamentosEmprestimo();

            Optional<Usuario> usuario = service.getUsuario(u.getUsuarioLogar());
            if (!usuario.isPresent()){
                service.salvarUsuario(u);
            }


        };
    }

    @Autowired
    private EquipamentoEmprestimoRepository equipamentoEmprestimoRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    EquiBaseRepository equiBaseRepository;

    public void cadastrarEquipamentosEmprestimo() {
        List<EquiBase> equipamentosBase = equiBaseRepository.findAll();

        if (equipamentosBase.isEmpty()) {
            System.out.println("Nenhum equipamento base encontrado.");
            return;
        }

        List<EquipamentoEmprestimo> lista = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            EquiBase equipamento = equipamentosBase.getFirst(); // reutiliza os existentes
            Equipamento equipamento1 = new Equipamento();
            equipamento1.setTipo(TipoEquipamento.EMPRESTIMO);
            equipamento1.setEquipamentoBase(equipamento);
            equipamento1.setCodigo("00"+i);
            equipamento1.setObs("Cadastrado automático.");
            EquipamentoEmprestimo emp = new EquipamentoEmprestimo();
            emp.setEquipamentoEmp(equipamento1);
            emp.setDisponivel(true);
            equipamentoRepository.save(equipamento1);

            lista.add(emp);
        }

        equipamentoEmprestimoRepository.saveAll(lista);
        System.out.println("500 equipamentos de empréstimo cadastrados com sucesso.");
    }

}
