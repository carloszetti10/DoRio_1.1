package com.projeto.domRio1.doRio.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "id_equi_base")
    private EquiBase equipamentoBase;
    
    @Enumerated(EnumType.STRING)
    private TipoEquipamento tipo;

    private String obs;

    private boolean apagado = false;

    @OneToMany(mappedBy = "equipamentoEmp", fetch = FetchType.EAGER)
    private List<EquipamentoEmprestimo> equipamentosEmprestimo = new ArrayList<>();

    @OneToMany(mappedBy = "equipamentoRet", fetch = FetchType.EAGER)
    private List<EquipamentoRetirada> equipamentosRetirada = new ArrayList<>();

}
