package com.projeto.domRio1.doRio.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "equipamento_id", nullable = false)
    private EquipamentoEmprestimo equipamento;

    private LocalDate dataEmprestimo;
    private LocalDate dataEntrega;

    @ManyToOne
    @JoinColumn(name = "quem_emprestou_id")
    private Usuario quemEmprestou; // Um empréstimo tem apenas um usuário que fez

    @ManyToOne
    @JoinColumn(name = "quem_recebeu_id")
    private Usuario quemRecebeu; // Um empréstimo tem apenas um usuário que recebeu

    private Boolean entregue = false;
}
