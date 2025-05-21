package com.projeto.domRio1.doRio.controller.elementos.telaInfPessoa;

import com.projeto.domRio1.doRio.controller.elementos.telaInit.CadastroEquipamentoController;
import com.projeto.domRio1.doRio.controller.elementos.telaInit.Tabelainit;
import com.projeto.domRio1.doRio.model.Emprestimo;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import com.projeto.domRio1.doRio.model.Pessoa;
import com.projeto.domRio1.doRio.service.pessoa.PessoaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class InfPessoaController {

    @FXML
    private ListView<Emprestimo> listView;
    @FXML
    private Label nomeSolicitante;

    private Long id;
    PessoaService pessoaService;

    public static void abrirInfoPessoa(String text, PessoaService pessoaService) {
        Long id = converterId(text);
        abrir(id, pessoaService);
    }

    @FXML
    void fecharJanela(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public static Long converterId(String text) {
        try {
            Long id = Long.parseLong(text);
            return id;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("");
        }
    }

    public static void abrir(Long id, PessoaService pessoaService){
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            FXMLLoader loader = new FXMLLoader(CadastroEquipamentoController.class.getResource("/templates/views/caixa/InfoPessoa.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            InfPessoaController controller = loader.getController();
            controller.init(id, pessoaService);

            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void init(Long id, PessoaService pessoaService) {
        this.id = id;
        this.pessoaService = pessoaService;
        configurarTabela();
        pegarNomePessoa();
    }

    public void pegarNomePessoa(){
        Optional<Pessoa> pessoa = pessoaService.findById(this.id);
        nomeSolicitante.setText(pessoa.get().getNome());
    }


    public void configurarTabela() {
        ObservableList<Emprestimo> observableList = FXCollections.observableArrayList(pessoaService.todosEmprestimoNaoDevolvido(this.id));
        listView.setItems(observableList);

        listView.setCellFactory(list -> new ListCell<>() {
            private FXMLLoader loader;

            @Override
            protected void updateItem(Emprestimo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/templates/views/tabelas/TabelaInfPessoa.fxml"));
                        try {
                            setGraphic(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                            setGraphic(new Label("Erro ao carregar"));
                            return;
                        }
                    }

                    TabelaInfPessoa ps = loader.getController();
                    ps.setDadosEmprestimo(item);
                    // Alterna cor de fundo
                    //if (getIndex() % 2 == 0) {
                    // getGraphic().setStyle("-fx-background-color: white;");
                    //} else {
                    //getGraphic().setStyle("-fx-background-color: lightgray;");
                    // }
                }
            }
        });
    }
}
