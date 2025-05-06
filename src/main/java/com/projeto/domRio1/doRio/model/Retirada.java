package com.projeto.domRio1.doRio.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Retirada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "equipamento_id", nullable = false)
    private EquipamentoRetirada equipamento;

    private LocalDate dataRetirada;

    private Integer quantEqui;
    @ManyToOne
    @JoinColumn(name = "realizou_retirada_id", nullable = false)
    private Usuario usuarioQRealizou;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa solicitante;

}
