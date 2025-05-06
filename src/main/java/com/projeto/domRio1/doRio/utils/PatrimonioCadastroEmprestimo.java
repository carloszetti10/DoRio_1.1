package com.projeto.domRio1.doRio.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatrimonioCadastroEmprestimo {
    private Long idEquipamento;
    private String patrimonioEquipamento;
    private String nome;
    private int quantidade;
    private boolean status; //TRUE E EMPRESTIMO

    public PatrimonioCadastroEmprestimo(Long idEquipamento, String patrimonioEquipamento, String nome) {
        this.idEquipamento = idEquipamento;
        this.patrimonioEquipamento = patrimonioEquipamento;
        this.nome = nome;
    }

    public PatrimonioCadastroEmprestimo(Long idEquipamento, String patrimonioEquipamento, String nome, int quantidade) {
        this.idEquipamento = idEquipamento;
        this.patrimonioEquipamento = patrimonioEquipamento;
        this.nome = nome;
        this.quantidade = quantidade;
    }
}
