package com.projeto.domRio1.doRio.controller;

import com.projeto.domRio1.doRio.controller.elementos.emprestimo.FormEmprestimo;
import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.service.EmprestimoService;
import com.projeto.domRio1.doRio.service.EquiBaseService;
import com.projeto.domRio1.doRio.service.EquipamentoEmprestimoService;
import com.projeto.domRio1.doRio.service.EquipamentoService;
import com.projeto.domRio1.doRio.service.pessoa.PessoaService;
import com.projeto.domRio1.doRio.utils.CaixaDialogo;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@Controller
public class EmprestimoController implements Initializable {

    @Autowired
    private EquipamentoEmprestimoService equiEmprestimoService;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private EquipamentoService equipamentoService;

    @Autowired
    private EquiBaseService equiBase;

    @Autowired
    @Lazy
    TelaInitController telaInitController;


    @FXML
    private TableColumn<Emprestimo, LocalDate> dataEmprestimo;

    @FXML
    private TableColumn<Emprestimo, LocalDate> dataEntrega;

    @FXML
    private TableColumn<Emprestimo, String> equipamento;

    @FXML
    private TableColumn<Emprestimo, Long> id;

    @FXML
    private TableColumn<Emprestimo, String> quemEmprestou;

    @FXML
    private TableColumn<Emprestimo, String> quemRecebeu;

    @FXML
    private TableColumn<Emprestimo, String> solicitante;

    @FXML
    private TableView<Emprestimo> tableView;

    @FXML
    void novoEmprestimo(ActionEvent event) {
        FormEmprestimo.addNew(this.emprestimoService, this.equiEmprestimoService, this.pessoaService, this.equipamentoService, this.equiBase, telaInitController, null, this);
    }

    @FXML
    private TextField campoPesquisa;

    @FXML
    private ComboBox<String> comboPesquisa;



   public void configurarTabela(List<Emprestimo> emp) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataEmprestimo.setCellValueFactory(new PropertyValueFactory<>("dataEmprestimo"));
        dataEntrega.setCellValueFactory(new PropertyValueFactory<>("dataEntrega"));

        quemEmprestou.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue().getQuemEmprestou();
            return new SimpleStringProperty(usuario != null ? usuario.getNome() : "");
        });

        quemRecebeu.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue().getQuemRecebeu();
            return new SimpleStringProperty(usuario != null ? usuario.getNome() :  "Não entregue");
        });

        quemRecebeu.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Não entregue".equals(item)) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle(""); // padrão
                    }
                }
            }
        });

        solicitante.setCellValueFactory(cellData -> {
            Pessoa pessoa = cellData.getValue().getPessoa();
            return new SimpleStringProperty(pessoa != null ? pessoa.getNome() : "");
        });

        equipamento.setCellValueFactory(cellData -> {
            EquipamentoEmprestimo equipEmprestimo = cellData.getValue().getEquipamento();
            if (equipEmprestimo != null && equipEmprestimo.getEquipamentoEmp() != null &&
                    equipEmprestimo.getEquipamentoEmp().getEquipamentoBase() != null) {
                return new SimpleStringProperty(equipEmprestimo.getEquipamentoEmp().getEquipamentoBase().getNome()+" "+equipEmprestimo.getEquipamentoEmp().getCodigo());
            }
            return new SimpleStringProperty("");
        });

       equipamento.setCellFactory(column -> new TableCell<>() {
           @Override
           protected void updateItem(String item, boolean empty) {
               super.updateItem(item, empty);
               if (empty || item == null) {
                   setText(null);
                   setStyle("");
               } else {
                   setText(item);

                   // Pega o objeto da linha atual
                   Emprestimo detalhes = getTableView().getItems().get(getIndex());
                   EquipamentoEmprestimo equipEmprestimo = detalhes.getEquipamento();

                   if (equipEmprestimo != null && equipEmprestimo.getEquipamentoEmp() != null &&
                           equipEmprestimo.getEquipamentoEmp().isApagado()) {

                       setStyle("-fx-text-fill: red;");
                   } else {
                       setStyle("");
                   }
               }
           }
       });

        // Carregar dados na tabela
        ObservableList<Emprestimo> emprestimos = FXCollections.observableArrayList(emp);
        tableView.setItems(emprestimos);
   }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela(emprestimoService.todosEmprestimo());
        preencherComboPesquisa();
        campoPesquisa.setVisible(false);
    }

    @FXML
    void campoPesquisaCombo(KeyEvent event) {
       String campo = comboPesquisa.getValue();
       if (campo.equals("SOLICITANTE")){
           configurarTabela(emprestimoService.listarEmpPorSolicitante(campoPesquisa.getText()));
       }else if (campo.equals("PAT EQUIPAMENTO")) {
           configurarTabela(emprestimoService.listarPorPatEqui(campoPesquisa.getText()));
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
           configurarTabela(emprestimoService.todosEmprestimo());
           campoPesquisa.setVisible(false);
       }
    }



    public void abrirEmprestimoTelaInicial(List<Emprestimo> emprestimos) {
       configurarTabela(emprestimos);
       comboPesquisa.setValue("");
    }
}
