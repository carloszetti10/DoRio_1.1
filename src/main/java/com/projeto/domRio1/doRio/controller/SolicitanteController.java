package com.projeto.domRio1.doRio.controller;

import com.projeto.domRio1.doRio.controller.elementos.telaInit.Tabelainit;
import com.projeto.domRio1.doRio.controller.elementos.telaPessoa.CadastroPessoaController;
import com.projeto.domRio1.doRio.controller.elementos.telaPessoa.TabelaPessoa;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import com.projeto.domRio1.doRio.model.Pessoa;
import com.projeto.domRio1.doRio.service.DepartamentoService;
import com.projeto.domRio1.doRio.service.pessoa.PessoaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SolicitanteController {

    @FXML
    private ListView<Pessoa> pessoaListView;

    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private DepartamentoService departamentoService;


    @Autowired
    @Lazy
    private SolicitanteController thisController;



    @FXML
    void abrirCadastro(ActionEvent event){
        CadastroPessoaController.addNew(this.pessoaService, this.departamentoService, this.thisController);
    }

    @FXML
    public void initialize() {
        configurarTabela();
    }



    public void configurarTabela() {
        ObservableList<Pessoa> observableList = FXCollections.observableArrayList(pessoaService.listarTodos());
        pessoaListView.setItems(observableList);

        pessoaListView.setCellFactory(list -> new ListCell<>() {
            private FXMLLoader loader;

            @Override
            protected void updateItem(Pessoa item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/templates/views/tabelas/TabelaPessoa.fxml"));
                        try {
                            setGraphic(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                            setGraphic(new Label("Erro ao carregar"));
                            return;
                        }
                    }


                    TabelaPessoa ps = loader.getController();
                    ps.setData(item);
                    //ps.meuService(this.equiEmpretimoService);
                    ps.meuService(pessoaService);

                }
            }
        });
    }
}
