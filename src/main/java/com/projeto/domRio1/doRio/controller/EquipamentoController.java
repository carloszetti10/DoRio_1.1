package com.projeto.domRio1.doRio.controller;

import com.projeto.domRio1.doRio.controller.elementos.telaInit.Tabelainit;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import com.projeto.domRio1.doRio.model.EquipamentoRetirada;
import com.projeto.domRio1.doRio.model.TipoEquipamento;
import com.projeto.domRio1.doRio.service.EquipamentoEmprestimoService;
import com.projeto.domRio1.doRio.service.EquipamentoRetiradaService;
import com.projeto.domRio1.doRio.service.EquipamentoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class EquipamentoController {
    @Autowired
    EquipamentoEmprestimoService equiEmpretimoService;
    @Autowired
    EquipamentoRetiradaService equipamentoRetiradaService;
    @Autowired
    EquipamentoService equipamentoService;
    EquipamentoController controller = EquipamentoController.this;


    //private static List<?> listaGeral = new ArrayList<>();



    @FXML
    private ListView<EquipamentoEmprestimo> equipamentoListView;
    @FXML
    private ListView<EquipamentoRetirada> equipamentoListViewRet;

    @FXML
    private ComboBox<TipoEquipamento> tipoEquiCombo;
    @FXML
    private Label statuQuant;
    @FXML
    private Label quantStatus;

    @FXML
    void abrirCadastro(ActionEvent event) {

    }
    @FXML
    void tipoEquiEventoCombo(ActionEvent event) {
        if(tipoEquiCombo.getValue() == TipoEquipamento.RETIRADA){
            statuQuant.setText("QUANTIDADE");
            configurarTabela(equipamentoRetiradaService.listarTodos());
            equipamentoListViewRet.setVisible(true);
            equipamentoListView.setVisible(false);
        } else {
            equiEmpretimoService.listarTodos();
            statuQuant.setText("STATUS");
            configurarTabela(equiEmpretimoService.listarTodos());
            equipamentoListViewRet.setVisible(false);
            equipamentoListView.setVisible(true);
        }
    }
    @FXML
    public void initialize() {
        tipoEquiCombo.setItems(FXCollections.observableArrayList(TipoEquipamento.values()));
        tipoEquiCombo.setValue(TipoEquipamento.RETIRADA);
        tipoEquiEventoCombo(new ActionEvent());
    }



    public void configurarTabela(List<?> lista) {
        List<Object> equipamentos = new ArrayList<>(lista);
        Object primeiro = null;

        if (!equipamentos.isEmpty()) {
            primeiro = equipamentos.get(0);
            if (primeiro.getClass().getName().equals(EquipamentoEmprestimo.class.getName())) {
                configurarTabEquiEmprestimo(equipamentos);
            } else if (primeiro.getClass().getName().equals(EquipamentoRetirada.class.getName())) {
                configurarTabEquiRetirada(equipamentos);
            }
        }
    }






    /*public void configurarTabela1() {
        equipamentoTabela.getChildren().clear();
        List<Object> equipamentos = new ArrayList<>(listaGeral);
        Object primeiro = null;

        if (!equipamentos.isEmpty()) {
            primeiro = equipamentos.get(0);
            if (primeiro.getClass().getName().equals(EquipamentoEmprestimo.class.getName())) {

                List<EquipamentoEmprestimo> lista = listaGeral.stream()
                        .filter(e -> e instanceof EquipamentoEmprestimo)
                        .map(e -> (EquipamentoEmprestimo) e)
                        .collect(Collectors.toList());

                boolean corColuna = false;
                for (EquipamentoEmprestimo ep : lista) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/templates/views/tabelas/TabelaInit.fxml"));
                    try {
                        // Carregar o layout correto baseado no tipo do FXML
                        Node node = fxmlLoader.load();  // Usando Node genérico
                        // Obtendo o controller para setar os dados
                        Tabelainit ps = fxmlLoader.getController();
                        ps.setData(ep);
                        ps.meuService(this.equiEmpretimoService, this.equipamentoService, this);
                        //iniciar(ps);
                        // Alternando cores para cada linha

                        if (corColuna) {
                            node.setStyle("-fx-background-color: lightgray;"); // Cor para linhas ímpares
                        } else {
                            node.setStyle("-fx-background-color: white;"); // Cor para linhas pares
                        }

                        // Alternar o valor de isEvenRow para a próxima iteração
                        corColuna = !corColuna;
                        // Adicionando o node carregado ao container (ex: VBox ou HBox)
                        equipamentoTabela.getChildren().add(node);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (primeiro.getClass().getName().equals(EquipamentoRetirada.class.getName())) {
                List<EquipamentoRetirada> lista = listaGeral.stream()
                        .filter(e -> e instanceof EquipamentoRetirada)
                        .map(e -> (EquipamentoRetirada) e)
                        .collect(Collectors.toList());

                boolean corColuna = false;
                for (EquipamentoRetirada ep : lista) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/templates/views/tabelas/TabelaInit.fxml"));
                    try {
                        // Carregar o layout correto baseado no tipo do FXML
                        Node node = fxmlLoader.load();  // Usando Node genérico
                        // Obtendo o controller para setar os dados
                        Tabelainit ps = fxmlLoader.getController();
                        ps.setDataReirada(ep);
                        ps.meuServiceRetirada(this.equipamentoRetiradaService);
                        //iniciar(ps);
                        // Alternando cores para cada linha

                        if (corColuna) {
                            node.setStyle("-fx-background-color: lightgray;"); // Cor para linhas ímpares
                        } else {
                            node.setStyle("-fx-background-color: white;"); // Cor para linhas pares
                        }

                        // Alternar o valor de isEvenRow para a próxima iteração
                        corColuna = !corColuna;
                        // Adicionando o node carregado ao container (ex: VBox ou HBox)
                        equipamentoTabela.getChildren().add(node);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            System.out.println("Tipo: " + primeiro.getClass().getName());
        }

    }*/




    public void configurarTabEquiEmprestimo(List<?> listaEqui) {
        List<EquipamentoEmprestimo> equipamentos = (List<EquipamentoEmprestimo>) listaEqui;
        ObservableList<EquipamentoEmprestimo> observableList = FXCollections.observableArrayList(equipamentos);

        equipamentoListView.setItems(observableList);

        equipamentoListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(EquipamentoEmprestimo item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/views/tabelas/TabelaInit.fxml"));
                        Node graphic = loader.load();

                        Tabelainit ps = loader.getController();
                        ps.setData(item);
                        ps.meuService(equiEmpretimoService, equipamentoService, controller, equipamentoRetiradaService);
                        //ps.setVisibleBotao();

                        setGraphic(graphic);
                    } catch (IOException e) {
                        e.printStackTrace();
                        setGraphic(new Label("Erro ao carregar"));
                    }
                }
            }
        });
    }

    public void configurarTabEquiRetirada(List<?> listaEqui) {
        equipamentoListViewRet.setVisible(false);
        List<EquipamentoRetirada> equipamentos = (List<EquipamentoRetirada>) listaEqui;
        ObservableList<EquipamentoRetirada> observableList = FXCollections.observableArrayList(equipamentos);

        equipamentoListViewRet.setItems(observableList);

        equipamentoListViewRet.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(EquipamentoRetirada item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/views/tabelas/TabelaInit.fxml"));
                        Node graphic = loader.load();

                        Tabelainit ps = loader.getController();
                        ps.setDataReirada(item);
                        ps.meuService(equiEmpretimoService, equipamentoService, controller, equipamentoRetiradaService);
                        //ps.setVisibleBotao();

                        setGraphic(graphic);
                    } catch (IOException e) {
                        e.printStackTrace();
                        setGraphic(new Label("Erro ao carregar"));
                    }
                }
            }
        });
    }

}
