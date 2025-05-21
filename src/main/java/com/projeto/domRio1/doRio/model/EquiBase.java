package com.projeto.domRio1.doRio.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nome", "modelo"}))
public class EquiBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String modelo;
    private String especificacao;
    private Integer quantidade;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoEquipamento tipo;

    @OneToMany(mappedBy = "equipamentoBase")
    private List<Equipamento> equipamentos = new ArrayList<>();

}
