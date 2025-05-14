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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class TelaInitController {
    @FXML
    private Label totalDevolucao;
    @FXML
    private Label totalEmpHoje;
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
        iniciarTela();
    }

    public void iniciarTela(){
        configurarTabela(equiEmpretimoService.listarTodos());
        nomeUsuario.setText(SessaoUsuario.getUsuario().getNome());
        preencherComboPesquisa();
        setarQuantTotalEmpHoje();
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
    public void preecherTabela(){
        configurarTabela(equiEmpretimoService.listarTodos());
        preencherComboPesquisa();
    }
    public void configurarTabela(List<EquipamentoEmprestimo> equipamentos) {
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

                    // Alterna cor de fundo
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
    }
    private void salveEquipamento(NumeroComEqui equi){
        equipamentoService.salvar(equi.getEquipamento(), equi.getNumero());
    }

    public void preecherTotalDevolucao() {
        List<Emprestimo> emprestimos = emprestimoService.emprestimosPendentes();
        totalDevolucao.setText(String.valueOf(emprestimos.size()));
    }

    @FXML
    private TextField campoPesquisa;
    @FXML
    void pesquisar(KeyEvent event) {
       metodoPesquisar();
    }
    @FXML
    private ComboBox<EquiBase> comboPequisa;
    @FXML
    void pesquisarPorCombo(ActionEvent event) {
        metodoPesquisar();
    }
    void preencherComboPesquisa(){
        List<EquiBase> equis = equiBaseService.buscarTodos();
        comboPequisa.getItems().clear();
        comboPequisa.getItems().addAll(equis);
        comboPequisa.setConverter(new StringConverter<EquiBase>() {
            @Override
            public String toString(EquiBase e) {
                return (e != null) ? e.getNome() + " " + e.getModelo() : "";
            }
            @Override
            public EquiBase fromString(String string) {
                return null;
            }
        });
    }
    void metodoPesquisar(){
        if(campoPesquisa.getText() == null && campoPesquisa.getText().trim().isEmpty()){
            configurarTabela(equiEmpretimoService.listarTodos());
        }else{
            List<EquipamentoEmprestimo> pesquisar = equiEmpretimoService.pesquisar(campoPesquisa.getText(), comboPequisa.getValue());
            configurarTabela(pesquisar);
        }
    }
    void setarQuantTotalEmpHoje(){
        List<Emprestimo> emprestimos = emprestimoService.listarEmpPorData(LocalDate.now());
        totalEmpHoje.setText(String.valueOf(emprestimos.size()));
    }

    @FXML
    void painelAbrirEmprestimoData(MouseEvent event) {
        mainController.loadView(Menu.Emprestimo);
        emprestimoController.abrirEmprestimoTelaInicial(emprestimoService.listarEmpPorData(LocalDate.now()));
    }

}
