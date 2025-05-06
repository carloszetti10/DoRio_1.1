package com.projeto.domRio1.doRio.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "pessoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @ManyToOne
    @JoinColumn(name = "id_departamento", nullable = false)
    private Departamento setor;
    private String telefone;


    @OneToMany(mappedBy = "pessoa")
    private List<Emprestimo> emprestimos;
}
