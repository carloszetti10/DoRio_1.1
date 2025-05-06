package com.projeto.domRio1.doRio.controller.elementos.emprestimo;

import com.projeto.domRio1.doRio.controller.elementos.retirada.FormRetirada;
import com.projeto.domRio1.doRio.model.EquipamentoRetirada;
import com.projeto.domRio1.doRio.utils.PatrimonioCadastroEmprestimo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PatrimonioCadastro {

    @FXML
    private VBox elemento;
    @FXML
    private Label idEquipamento;
    @FXML
    private Label patrimonio;
    @FXML
    private Label nome;
    @FXML
    private Label quantidade;

    private static boolean status = false;

    public void setQuantidade(int quantidade){
        this.quantidade.setText(String.valueOf(quantidade));
    }


    public void setData(PatrimonioCadastroEmprestimo pat) {
        this.quantidade.setVisible(false);
        this.patrimonio.setText(pat.getPatrimonioEquipamento());
        this.idEquipamento.setText(String.valueOf(pat.getIdEquipamento()));
        this.nome.setText(pat.getNome());
        this.status = true;
    }

    public void setDataRetirada(PatrimonioCadastroEmprestimo ret) {
        this.patrimonio.setText(ret.getPatrimonioEquipamento());
        this.idEquipamento.setText(String.valueOf(ret.getIdEquipamento()));
        this.nome.setText(ret.getNome());
        this.quantidade.setText(String.valueOf(ret.getQuantidade()));
    }

    @FXML
    void fecharJanel(ActionEvent event) {
        if (this.status) {
            Long id = Long.parseLong(idEquipamento.getText());
            PatrimonioCadastroEmprestimo p = new PatrimonioCadastroEmprestimo(id, patrimonio.getText(), nome.getText());
            FormEmprestimo.removerListBy(p);
        }else {
            Long id = Long.parseLong(idEquipamento.getText());
            PatrimonioCadastroEmprestimo p = new PatrimonioCadastroEmprestimo(id, patrimonio.getText(), nome.getText(), Integer.valueOf(quantidade.getText()));
            FormRetirada.removerListBy(p);
        }
        // Obtém o botão que acionou o evento
        Node source = (Node) event.getSource();

        // Obtém o elemento pai até encontrar o container correto
        Parent parent = source.getParent();
        while (parent != null && !(parent instanceof VBox)) {
            parent = parent.getParent();
        }

        if (parent instanceof VBox elementoParaRemover) {
            Parent container = elementoParaRemover.getParent();

            if (container instanceof Pane paneContainer) {
                paneContainer.getChildren().remove(elementoParaRemover);
            }
        }
    }
}
