package com.projeto.domRio1.doRio.model;

import lombok.Data;

@Data
public class NumeroComEqui {
    private Equipamento equipamento;
    private Integer numero;

    public NumeroComEqui(Equipamento equipamento, Integer numero) {
        this.equipamento = equipamento;
        this.numero = numero;
    }
}


