package com.projeto.domRio1.doRio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EquipamentoRetirada{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_equipamento_ret")
    private Equipamento equipamentoRet;

    private Integer quantidade;
}
