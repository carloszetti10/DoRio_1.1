package com.projeto.domRio1.doRio.controller.elementos.telaPessoa;

import com.projeto.domRio1.doRio.controller.elementos.telaInfPessoa.InfPessoaController;
import com.projeto.domRio1.doRio.model.Pessoa;
import com.projeto.domRio1.doRio.service.EmprestimoService;
import com.projeto.domRio1.doRio.service.pessoa.PessoaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Controller;

@Controller
public class TabelaPessoa {

    PessoaService pessoaService;

    @FXML
    private Label departamento;

    @FXML
    private Label id;

    @FXML
    private Label nome;

    @FXML
    private Label telefone;

    @FXML
    void editar(ActionEvent event) {

    }


    @FXML
    void informacao(ActionEvent event) {
        InfPessoaController.abrirInfoPessoa(this.id.getText(), this.pessoaService);
    }

    public void setData(Pessoa p) {
        id.setText(String.valueOf(p.getId()));
        nome.setText(p.getNome());
        departamento.setText(p.getSetor().getNomeDepartamento());
        telefone.setText(p.getTelefone());
    }
    public void meuService(PessoaService pessoaService){
        this.pessoaService = pessoaService;
    }
}
