package com.projeto.domRio1.doRio.controller;

import com.projeto.domRio1.doRio.controller.elementos.retirada.FormRetirada;
import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.service.*;
import com.projeto.domRio1.doRio.service.pessoa.PessoaService;
import com.projeto.domRio1.doRio.utils.PatrimonioCadastroEmprestimo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

@Controller
public class RetiradaController implements Initializable {

    @FXML
    private TableColumn<Retirada, ?> dataRetirada;

    @FXML
    private TableColumn<Retirada, String> equipamento;

    @FXML
    private TableColumn<Retirada, Long> id;

    @FXML
    private TableColumn<Retirada, Integer> quantidade;

    @FXML
    private TableColumn<Retirada, String> solicitante;

    @FXML
    private TableView<Retirada> tableView;

    @FXML
    private TableColumn<Retirada, String> usuarioRealizou;


    @Autowired
    RetiradaService retiradaService;
    @Autowired
    EquipamentoEmprestimoService equiEmprestimoService;
    @Autowired
    PessoaService pessoaService;
    @Autowired
    EquipamentoService equipamentoService;
    @Autowired
    EquiBaseService equiBase;
    @Autowired
    EquipamentoRetiradaService equipamentoRetiradaService;
    Set<PatrimonioCadastroEmprestimo> listaPatrimonio;
    @FXML
    void novaRetirada(ActionEvent event) {
        FormRetirada.addNew(retiradaService, equiEmprestimoService, pessoaService, equipamentoService, equiBase, listaPatrimonio, equipamentoRetiradaService, this);
    }
    public void configurarTabela(List<Retirada> lista) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataRetirada.setCellValueFactory(new PropertyValueFactory<>("dataRetirada"));

        quantidade.setCellValueFactory(new PropertyValueFactory<>("quantEqui"));

        usuarioRealizou.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue().getUsuarioQRealizou();
            return new SimpleStringProperty(usuario != null ? usuario.getNome() : "");
        });


        solicitante.setCellValueFactory(cellData -> {
            Pessoa pessoa = cellData.getValue().getSolicitante();
            return new SimpleStringProperty(pessoa != null ? pessoa.getNome() : "");
        });

        equipamento.setCellValueFactory(cellData -> {
            EquipamentoRetirada equipamentoRetirada = cellData.getValue().getEquipamento();
            if (equipamentoRetirada != null && equipamentoRetirada.getEquipamentoRet() != null &&
                    equipamentoRetirada.getEquipamentoRet().getEquipamentoBase() != null) {
                return new SimpleStringProperty(equipamentoRetirada.getEquipamentoRet().getEquipamentoBase().getNome()+" "+equipamentoRetirada.getEquipamentoRet().getCodigo());
            }
            return new SimpleStringProperty("");
        });

        // Carregar dados na tabela
        ObservableList<Retirada> retiradas = FXCollections.observableArrayList(
                lista
        );
        tableView.setItems(retiradas);
    }
    @FXML
    private TextField campoPesquisa;
    @FXML
    private ComboBox<String> comboPesquisa;



    @FXML
    void campoPesquisaCombo(KeyEvent event) {
        String campo = comboPesquisa.getValue();
        if (campo.equals("SOLICITANTE")){
            configurarTabela(retiradaService.listarEmpPorSolicitante(campoPesquisa.getText()));
        }else if (campo.equals("PAT EQUIPAMENTO")) {
            configurarTabela(retiradaService.listarPorPatEqui(campoPesquisa.getText()));
        }
    }
    void preencherComboPesquisa(){
        List<String> stringValor = List.of("TODOS", "SOLICITANTE", "PAT EQUIPAMENTO");
        comboPesquisa.getItems().clear();
        comboPesquisa.setValue("TODOS");
        comboPesquisa.getItems().addAll(stringValor);
    }
    @FXML
    void comboEvent(ActionEvent event) {
        if (!Objects.equals(comboPesquisa.getValue(), "TODOS")){
            campoPesquisa.setVisible(true);
            campoPesquisa.setText("");
        } else {
            configurarTabela(retiradaService.todosEmprestimo());
            campoPesquisa.setVisible(false);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela(retiradaService.todosEmprestimo());
        preencherComboPesquisa();
        campoPesquisa.setVisible(false);
    }

    public void abrirRetiradaTelaInicial(List<Retirada> ret) {
        configurarTabela(ret);
        comboPesquisa.setValue("");
    }


}
