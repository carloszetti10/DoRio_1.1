package com.projeto.domRio1.doRio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "equipamento_emprestimo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipamentoEmprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_equipamento_emp")
    private Equipamento equipamentoEmp;

    private Boolean disponivel = true;

    @OneToMany(mappedBy = "equipamento")
    private List<Emprestimo> emprestimos;

    //@OneToOne
    //@JoinColumn(name = "id_tipo_equi")
    //private EquiBase tipoModelo;

}
