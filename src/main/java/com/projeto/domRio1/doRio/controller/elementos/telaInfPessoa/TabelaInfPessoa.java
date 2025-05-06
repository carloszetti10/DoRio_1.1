package com.projeto.domRio1.doRio.controller.elementos.telaInfPessoa;

import com.projeto.domRio1.doRio.model.Emprestimo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;

@Controller
public class TabelaInfPessoa {

    @FXML
    private CheckBox caixaMarcacao;

    @FXML
    private Label codEqui;

    @FXML
    private Label dataEmprestimo;

    @FXML
    private Label id;

    @FXML
    private Label usuarioQueRealizou;

    @FXML
    private Label status;


    @FXML
    void EventoCaixa(ActionEvent event) {

    }

    public void setDadosEmprestimo(Emprestimo e) {
        id.setText(String.valueOf(e.getId()));
        String equi = e.getEquipamento().getEquipamentoEmp().getEquipamentoBase().getNome()+" "+e.getEquipamento().getEquipamentoEmp().getCodigo();
        codEqui.setText(equi);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataEmprestimo.setText(e.getDataEmprestimo().format(formatter));
        status.setText("Pendente");
        usuarioQueRealizou.setText(e.getQuemEmprestou().getNome());
    }
}
