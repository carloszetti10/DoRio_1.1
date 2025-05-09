package com.projeto.domRio1.doRio.controller;

import com.projeto.domRio1.doRio.controller.elementos.emprestimo.FormEmprestimo;
import com.projeto.domRio1.doRio.controller.elementos.telaInit.CadastroEquipamentoController;
import com.projeto.domRio1.doRio.controller.elementos.telaInit.Tabelainit;
import com.projeto.domRio1.doRio.controller.elementos.usuario.UsuarioController;
import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.service.*;
import com.projeto.domRio1.doRio.service.pessoa.PessoaService;
import com.projeto.domRio1.doRio.utils.Menu;
import com.projeto.domRio1.doRio.utils.PatrimonioCadastroEmprestimo;
import com.projeto.domRio1.doRio.utils.SessaoUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class TelaInitController {
    @FXML
    private Label totalDevolucao;
    @FXML
    private VBox equipamentoTabela;
    @FXML
    private ListView<EquipamentoEmprestimo> equipamentoListView;
    @Getter
    @Autowired
    private EquipamentoService equipamentoService;
    @Autowired
    EquipamentoEmprestimoService equiEmpretimoService;
    @Autowired
    private EquiBaseService equiBaseService;
    @Autowired
    EmprestimoService emprestimoService;
    @Autowired
    PessoaService pessoaService;
    @Autowired
    MainController mainController;
    @Autowired
    EmprestimoController emprestimoController;
    @Autowired
    EquipamentoController equipamentoController;
    @Autowired
    private UsuarioService usuarioService;

    @FXML
    private Label nomeUsuario;

    @FXML
    public void initialize() {
        configurarTabela();
        nomeUsuario.setText(SessaoUsuario.getUsuario().getNome());
    }

    @FXML
    void painelAbrirDevolucao(MouseEvent event) {
        mainController.loadView(Menu.Devolucao);
    }

    @FXML
    void cadastrarEmprestimo(ActionEvent event) {
        Set<PatrimonioCadastroEmprestimo> pats = Tabelainit.lista;
        FormEmprestimo.addNew(emprestimoService, equiEmpretimoService, pessoaService, equipamentoService, equiBaseService, this, pats, emprestimoController);
        for (PatrimonioCadastroEmprestimo p : pats ){
            System.out.println(p.toString());
        }
    }

    @FXML
    void abrirCadastro(ActionEvent event) {
        CadastroEquipamentoController.addNew(
                this::salveEquipamento,
                this::saveBaseEqui,
                TipoEquipamento.values(),
                equiBaseService::buscarTodos,
                this
        );
    }


    @FXML
    void abrirPainelUsuario(MouseEvent event) {
        UsuarioController.abrir(usuarioService);
    }

    public EquiBaseService returnBaseService(){
        return this.equiBaseService;
    }
    public void configurarTabela() {
        List<EquipamentoEmprestimo> equipamentos = equiEmpretimoService.listarTodos();
        ObservableList<EquipamentoEmprestimo> observableList = FXCollections.observableArrayList(equipamentos);
        equipamentoListView.setItems(observableList);

        equipamentoListView.setCellFactory(list -> new ListCell<>() {
            private FXMLLoader loader;

            @Override
            protected void updateItem(EquipamentoEmprestimo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/templates/views/tabelas/TabelaInit.fxml"));
                        try {
                            setGraphic(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                            setGraphic(new Label("Erro ao carregar"));
                            return;
                        }
                    }

                    Tabelainit controller = loader.getController();
                    controller.setData(item);
                    controller.meuService(equiEmpretimoService, equipamentoService, equipamentoController);
                    controller.setVisibleBotao();

                    // Alterna cor de fundo se quiser
                    //if (getIndex() % 2 == 0) {
                       // getGraphic().setStyle("-fx-background-color: white;");
                    //} else {
                        //getGraphic().setStyle("-fx-background-color: lightgray;");
                   // }
                }
            }
        });

        preecherTotalDevolucao();
    }

    private void saveBaseEqui(EquiBase equi) {
        equiBaseService.salva(equi);
       // category.setValue(product.getCategory());
       //search();
    }
    private void salveEquipamento(NumeroComEqui equi){
        equipamentoService.salvar(equi.getEquipamento(), equi.getNumero());
    }

    public void preecherTotalDevolucao() {
        List<Emprestimo> emprestimos = emprestimoService.emprestimosPendentes();
        totalDevolucao.setText(String.valueOf(emprestimos.size()));
    }
}
