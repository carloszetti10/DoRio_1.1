package com.projeto.domRio1.doRio.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.projeto.domRio1.doRio.controller.elementos.telaInit.CadastroEquipamentoController;
import com.projeto.domRio1.doRio.controller.elementos.telaInit.Tabelainit;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import com.projeto.domRio1.doRio.model.EquipamentoRetirada;
import com.projeto.domRio1.doRio.model.TipoEquipamento;
import com.projeto.domRio1.doRio.service.EquipamentoEmprestimoService;
import com.projeto.domRio1.doRio.service.EquipamentoRetiradaService;
import com.projeto.domRio1.doRio.service.EquipamentoService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    @Autowired
    @Lazy
    TelaInitController telaInitController;
    @FXML
    private ListView<EquipamentoEmprestimo> equipamentoListView;
    @FXML
    private ListView<EquipamentoRetirada> equipamentoListViewRet;
    @FXML
    private ComboBox<TipoEquipamento> tipoEquiCombo;
    @FXML
    private Label statuQuant;

    @FXML
    void abrirCadastro(ActionEvent event) {
        telaInitController.abrirCadastroEqupamento();
    }

    @FXML
    void tipoEquiEventoCombo(ActionEvent event) {
        if(tipoEquiCombo.getValue() == TipoEquipamento.RETIRADA){
            statuQuant.setText("QUANTIDADE");
            configurarTabela(equipamentoRetiradaService.listarTodos());
            equipamentoListViewRet.setVisible(true);
            equipamentoListView.setVisible(false);
            campoPesquisar.setText("");
        } else if (tipoEquiCombo.getValue() == TipoEquipamento.EMPRESTIMO) {
            equiEmpretimoService.listarTodos();
            statuQuant.setText("STATUS");
            configurarTabela(equiEmpretimoService.listarTodos());
            equipamentoListViewRet.setVisible(false);
            equipamentoListView.setVisible(true);
            campoPesquisar.setText("");
        }
    }
    public void iniciar(){
        tipoEquiCombo.setItems(FXCollections.observableArrayList(TipoEquipamento.values()));
        tipoEquiCombo.setValue(TipoEquipamento.RETIRADA);
        tipoEquiEventoCombo(new ActionEvent());
    }
    public void setarTipoCombo(TipoEquipamento e){
        tipoEquiCombo.setValue(null);
        tipoEquiCombo.setValue(e);
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
                        ps.setVisibleCaixa();

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
        //equipamentoListViewRet.setVisible(false);
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
                        ps.setVisibleCaixa();

                        setGraphic(graphic);
                    } catch (IOException e) {
                        e.printStackTrace();
                        setGraphic(new Label("Erro ao carregar"));
                    }
                }
            }
        });
    }

    @FXML
    private TextField campoPesquisar;

    @FXML
    void campoPesquisaKey(KeyEvent event) {
        Platform.runLater(() -> {
            String text = campoPesquisar.getText();

            if (tipoEquiCombo.getValue() == TipoEquipamento.RETIRADA) {
                List<EquipamentoRetirada> es = equipamentoRetiradaService.buscarPorPat(text);
                configurarTabela(es);

            } else if (tipoEquiCombo.getValue() == TipoEquipamento.EMPRESTIMO) {
                List<EquipamentoEmprestimo> es = equiEmpretimoService.buscarPorPat(text);
                configurarTabela(es);
            }
        });
    }


    @FXML
    public void initialize() {
        iniciar();
    }

}
