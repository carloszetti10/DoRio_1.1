package com.projeto.domRio1.doRio.controller.elementos.retirada;


import com.projeto.domRio1.doRio.controller.RetiradaController;
import com.projeto.domRio1.doRio.controller.TelaInitController;
import com.projeto.domRio1.doRio.controller.elementos.emprestimo.PatrimonioCadastro;
import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.service.*;
import com.projeto.domRio1.doRio.service.pessoa.PessoaService;
import com.projeto.domRio1.doRio.utils.MensagemPane;
import com.projeto.domRio1.doRio.utils.PatrimonioCadastroEmprestimo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class FormRetirada {

    RetiradaService retiradaService;
    EquipamentoEmprestimoService equiEmprestimoService;
    PessoaService pessoaService;
    EquipamentoService equipamentoService;
    EquiBaseService equiBase;
    EquipamentoRetiradaService equipamentoRetiradaService;
    RetiradaController retiradaController;

    public static Set<PatrimonioCadastroEmprestimo> listaPatrimonio;



    @FXML
    private HBox painelEquis;

    @FXML
    private MensagemPane mensagemPane;

    @FXML
    private ComboBox<Pessoa> solicitante;

    @FXML
    private ComboBox<EquiBase> equipamento;

    @FXML
    private ComboBox<String> patrimonio;

    @FXML
    private Spinner<Integer> quantidade;

    public static void addNew(RetiradaService retiradaService, EquipamentoEmprestimoService equiEmprestimoService, PessoaService pessoaService, EquipamentoService equipamentoService, EquiBaseService equiBase, Set<PatrimonioCadastroEmprestimo> listaPatrimonio, EquipamentoRetiradaService equipamentoRetiradaService, RetiradaController retiradaController) {
        abrir(retiradaService,equiEmprestimoService,pessoaService, equipamentoService, equiBase,  listaPatrimonio, equipamentoRetiradaService, retiradaController);
    }

    public static void abrir(RetiradaService retiradaService, EquipamentoEmprestimoService equiEmprestimoService, PessoaService pessoaService,EquipamentoService equipamentoService, EquiBaseService equiBase, Set<PatrimonioCadastroEmprestimo> listaPatrimonio, EquipamentoRetiradaService equipamentoRetiradaService, RetiradaController retiradaController) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            FXMLLoader loader = new FXMLLoader(FormRetirada.class.getResource("/templates/views/caixa/RetiradaForm.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            FormRetirada controller = loader.getController();
            controller.init(retiradaService,equiEmprestimoService,pessoaService, equipamentoService, equiBase, listaPatrimonio, equipamentoRetiradaService, retiradaController);

            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void cadastrarEmprestimo(ActionEvent event) {
        try {
            // Verificando se os campos obrigatórios estão preenchidos antes de continuar
            Pessoa pessoa = solicitante.getValue();

            if ((pessoa == null) || listaPatrimonio.isEmpty()) {
                throw new AvisoException("Preencha todos os campos obrigatórios *");
            }

            retiradaService.salvarRetirada(pessoa, listaPatrimonio);
            mensagemPane.mostrarSucesso("Retirada cadastrado com sucesso!");
            listaPatrimonio.removeAll(listaPatrimonio);
            preencherEquipamento();
            configurarTabela();
            retiradaController.configurarTabela();

        } catch (ErroException erroException) {
            mensagemPane.mostrarErro(erroException.getMessage());
        } catch (AvisoException e) {
            // Exibe um aviso se os campos estiverem inválidos
            mensagemPane.mostrarAviso(e.getMessage());
        }
    }

    @FXML
    void pegarEquiSelecionadoCombo(ActionEvent event) {
        EquiBase value = equipamento.getValue();
        preencherPatrimonio(value);
    }

    @FXML
    void fecharJanela(ActionEvent event) {
        listaPatrimonio.removeAll(listaPatrimonio);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void addPatrimonioEmprestar(ActionEvent event) {
        EquiBase equipamentoValue = equipamento.getValue();
        String patrimonioValue = patrimonio.getValue();
        int quantidadeValue = quantidade.getValue();

        //fazer logica da quantidade
        Equipamento equiBanco = equipamentoService.findByCodigoAndEquipamentoBase(patrimonioValue, equipamentoValue, TipoEquipamento.RETIRADA);

        EquipamentoRetirada equipamentoRetirada = equipamentoRetiradaService.findbyEquipamentoRet(equiBanco);

        if((equipamentoValue == null) || (patrimonioValue == null)){
            mensagemPane.mostrarAviso("Selecione um equipamento!");
            return;
        }

        if(equipamentoRetirada.getQuantidade() >= quantidadeValue){
            converterParaPatCadEmp(equiBanco);
        } else{
            mensagemPane.mostrarAviso("Quantidade indisponivel!");
        }

    }

    public static void removerListBy(PatrimonioCadastroEmprestimo pat){
        listaPatrimonio.remove(pat);
    }

    public void converterParaPatCadEmp(Equipamento equi) {

        for (PatrimonioCadastroEmprestimo p : listaPatrimonio) {
            if (p.getIdEquipamento() == equi.getId()) {

                p.setQuantidade(quantidade.getValue());
                configurarTabela();
                return;
            }
        }


        PatrimonioCadastroEmprestimo patCadEmprestimo = new PatrimonioCadastroEmprestimo();
        patCadEmprestimo.setIdEquipamento(equi.getId());
        patCadEmprestimo.setPatrimonioEquipamento(equi.getCodigo());
        patCadEmprestimo.setNome(equi.getEquipamentoBase().getNome());
        patCadEmprestimo.setQuantidade(quantidade.getValue());

        listaPatrimonio.add(patCadEmprestimo);
        configurarTabela();
    }

    public void configurarTabela() {
        painelEquis.getChildren().clear();
        if (listaPatrimonio == null){
            listaPatrimonio = new HashSet<>();
        }

        boolean corColuna = false;
        for (PatrimonioCadastroEmprestimo pat : listaPatrimonio) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/templates/views/tabelas/PatrimoniosEmprestimo.fxml"));
            try {
                // Carregar o layout correto baseado no tipo do FXML
                Node node = fxmlLoader.load();  // Usando Node genérico
                // Obtendo o controller para setar os dados
                PatrimonioCadastro p = fxmlLoader.getController();
                p.setDataRetirada(pat);

                // Alternando cores para cada linha

                if (corColuna) {
                    node.setStyle("-fx-background-color: #107fbc;"); // Cor para linhas ímpares
                } else {
                    node.setStyle("-fx-background-color: #0b699d;"); // Cor para linhas pares
                }

                // Alternar o valor de isEvenRow para a próxima iteração
                corColuna = !corColuna;
                painelEquis.getChildren().add(node);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void preencherEquipamento(){
        List<EquiBase> equiBases = equiBase.buscarTodos();
        equipamento.getItems().clear();
        equipamento.getItems().addAll(equiBases);
        equipamento.setConverter(new StringConverter<EquiBase>() {
            @Override
            public String toString(EquiBase e) {
                return (e != null) ? e.getNome() + " " + e.getModelo() : ""; // Exibe apenas o nome
            }
            @Override
            public EquiBase fromString(String string) {
                return null; // Não necessário neste caso
            }
        });
    }

    void preencherPessoa(){
        List<Pessoa> pessoas = pessoaService.listarTodos();
        solicitante.getItems().clear();
        solicitante.getItems().addAll(pessoas);
        solicitante.setConverter(new StringConverter<Pessoa>() {
            @Override
            public String toString(Pessoa p) {
                return (p != null) ? p.getNome() + " " + p.getSetor().getNomeDepartamento() : "";
            }
            @Override
            public Pessoa fromString(String string) {
                return null; // Não necessário neste caso
            }
        });
    }

    void preencherPatrimonio(EquiBase equiBase) {
        List<Equipamento> listarEquipamento = equipamentoService.listarPorEquiBase(equiBase);

        if (listarEquipamento == null) return; // Evita NullPointerException

        Set<String> patrimonios = new HashSet<>(); // Evita duplicação

        for (Equipamento equi : listarEquipamento) {
            if (equi.getEquipamentosRetirada() != null) { // Evita NullPointerException
                for (EquipamentoRetirada equiReti : equi.getEquipamentosRetirada()) {
                    if (equiReti.getQuantidade() != 0) {
                        patrimonios.add(equi.getCodigo());
                    }
                }
            }
        }

        patrimonio.getItems().clear();
        patrimonio.getItems().addAll(patrimonios);
    }

    public static void configurarSpinner(Spinner<Integer> spinner) {
        int minValue = 1;
        int maxValue = 100_000;

        // Configura a fábrica de valores do Spinner com valor inicial 1
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, minValue);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);

        // Adicionando um filtro para permitir apenas números dentro do intervalo
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), minValue, change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change; // Permite apagar o valor
            }
            if (newText.matches("\\d*")) {
                try {
                    int value = Integer.parseInt(newText);
                    if (value >= minValue && value <= maxValue) {
                        return change; // Aceita a mudança se estiver dentro dos limites
                    }
                } catch (NumberFormatException e) {
                    return null; // Evita exceção se o número for muito grande
                }
            }
            return null; // Bloqueia entrada inválida
        });

        spinner.getEditor().setTextFormatter(textFormatter);

        // Sincroniza o editor do Spinner com o valor inicial correto
        spinner.getEditor().setText(String.valueOf(minValue));

        // Ao perder o foco, se estiver vazio, volta para 1
        spinner.getEditor().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && spinner.getEditor().getText().isEmpty()) {
                spinner.getValueFactory().setValue(Integer.valueOf(minValue));
                spinner.getEditor().setText(String.valueOf(minValue));
            }
        });
    }

    private void init(RetiradaService retiradaService, EquipamentoEmprestimoService equiEmprestimoService, PessoaService pessoaService, EquipamentoService equipamentoService, EquiBaseService equiBase,  Set<PatrimonioCadastroEmprestimo> listaPatrimonio, EquipamentoRetiradaService equipamentoRetiradaService, RetiradaController retiradaController) {
        this.retiradaService = retiradaService;
        this.equiEmprestimoService = equiEmprestimoService;
        this.pessoaService = pessoaService;
        this.equipamentoService = equipamentoService;
        this.equiBase = equiBase;
        this.equipamentoRetiradaService = equipamentoRetiradaService;
        this.retiradaController = retiradaController;
        FormRetirada.listaPatrimonio = listaPatrimonio;
        configurarTabela();
        preencherPessoa();
        preencherEquipamento();
        configurarSpinner(quantidade);
    }

}
