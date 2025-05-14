package com.projeto.domRio1.doRio.controller.elementos.telaInit;

import com.projeto.domRio1.doRio.controller.TelaInitController;
import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.EquiBase;
import com.projeto.domRio1.doRio.model.Equipamento;
import com.projeto.domRio1.doRio.model.NumeroComEqui;
import com.projeto.domRio1.doRio.model.TipoEquipamento;
import com.projeto.domRio1.doRio.service.EquiBaseService;
import com.projeto.domRio1.doRio.utils.MensagemPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;


@Controller
public class CadastroEquipamentoController {

    private Consumer<EquiBase> saveHandler;
    private Consumer<NumeroComEqui> equipamentoConsumer;
    private TelaInitController telaInitController;

    @FXML
    private Pane painelCadEquipamento;
    @FXML
    private ComboBox<EquiBase> comboEqui;
    @FXML
    private ComboBox<TipoEquipamento> comboTipoEquiBase;
    @FXML
    private ComboBox<TipoEquipamento> comboTipo;
    @FXML
    private TextArea especificacaoEqui;
    @FXML
    private TextField modeloEqui;
    @FXML
    private TextField nomeEqui;
    @FXML
    private MensagemPane mensagemPane;
    @FXML
    private TextField patrimonio;
    @FXML
    private Pane painelQuantidade;
    @FXML
    private Spinner<Integer> quantidade;


    @FXML
    void abrirCadEquipamento(ActionEvent event){
        painelCadEquipamento.setVisible(true);
    }
    @FXML
    void fecharPaneCadEqui(ActionEvent event){
        painelCadEquipamento.setVisible(false);
    }
    @FXML
    void verificarTipo(ActionEvent event) {
        capturarTipoAbrirQuantidade();
    }
    @FXML
    void cadastrarEquiPatrimonizado(ActionEvent event) {
        try {
            // Verificando se os campos obrigatórios estão preenchidos antes de continuar
            String codigoPatrimonio = patrimonio.getText();
            TipoEquipamento tipoEquipamento = comboTipo.getValue();
            EquiBase equipamentoBase = comboEqui.getValue();

            if (codigoPatrimonio == null || codigoPatrimonio.isEmpty() || tipoEquipamento == null || equipamentoBase == null) {
                throw new AvisoException("Preencha todos os campos obrigatórios *");
            }

            Equipamento by = telaInitController.getEquipamentoService().findByCodigoAndEquipamentoBase(codigoPatrimonio, equipamentoBase, tipoEquipamento);
            if(by != null && !by.isApagado()){
                    throw new AvisoException("Equipamento já cadastrado!");
            }

            // Criando o objeto Equipamento
            Equipamento eq = new Equipamento();
            eq.setCodigo(codigoPatrimonio);
            eq.setTipo(tipoEquipamento);
            eq.setEquipamentoBase(equipamentoBase);


            // Criando o objeto NumeroComEqui e aceitando
            NumeroComEqui numeroComEqui = new NumeroComEqui(eq, this.quantidade.getValue());
            equipamentoConsumer.accept(numeroComEqui);
            
            telaInitController.preecherTabela();
            limpaCampoInicial();
            mensagemPane.mostrarSucesso("Equipamento cadastrado com sucesso!");
            capturarTipoAbrirQuantidade();
        } catch (ErroException erroException) {
            mensagemPane.mostrarErro(erroException.getMessage());
        } catch (AvisoException e) {
            // Exibe um aviso se os campos estiverem inválidos
            mensagemPane.mostrarAviso(e.getMessage());
        }
    }
    @FXML
    void cadastrarEquipamento(ActionEvent event) {
        try {
            String nome = nomeEqui.getText();
            String modelo = modeloEqui.getText();
            String esp = especificacaoEqui.getText();
            TipoEquipamento tipo = comboTipoEquiBase.getValue();
            // Valida os campos antes de tentar salvar
            if (nome == null || nome.trim().isEmpty() || modelo == null || modelo.trim().isEmpty() || tipo == null) {
                throw new AvisoException("Nome, Modelo e Tipo são campos obrigatórios.");
            }
            // Criando o objeto e definindo os valores
            EquiBase e = new EquiBase();
            e.setNome(nome);
            e.setModelo(modelo);
            e.setTipo(tipo);
            e.setEspecificacao(esp);
            // Aceitando o objeto para o próximo processamento (como salvar)
            saveHandler.accept(e);
            // Se tudo ocorrer bem, exibe a mensagem de sucesso
            mensagemPane.mostrarSucesso("Equipamento cadastrado com sucesso!");
            capturarTipoAbrirQuantidade();
            limparCadEquiBase();
        } catch (AvisoException e) {
            // Exibe um aviso se os campos estiverem inválidos
            mensagemPane.mostrarAviso(e.getMessage());
        } catch (ErroException erroException) {
            // Exibe um erro genérico caso algum erro não específico aconteça
            mensagemPane.mostrarErro(erroException.getMessage());
        }
    }
    @FXML
    void fecharJanela(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public static void addNew(Consumer<NumeroComEqui> equipamentoConsumer, Consumer<EquiBase> saveHandler, TipoEquipamento[] tipoEqui, Supplier<List<EquiBase>> listaEquipamento,TelaInitController telaInit) {
        abrir(null, equipamentoConsumer, saveHandler, tipoEqui,listaEquipamento,telaInit);
    }

    public static void abrir(EquiBase e,Consumer<NumeroComEqui> equipamentoConsumer,  Consumer<EquiBase> saveHandler, TipoEquipamento[] tipoEqui,Supplier<List<EquiBase>> listaEquipamento, TelaInitController telaInit) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            FXMLLoader loader = new FXMLLoader(CadastroEquipamentoController.class.getResource("/templates/views/caixa/FormEquipamento.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            CadastroEquipamentoController controller = loader.getController();
            controller.init(e, equipamentoConsumer, saveHandler,tipoEqui,listaEquipamento,telaInit);


            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void preencherComboEqui(List<EquiBase> lista){
        comboEqui.getItems().clear();
        if (lista != null) {
            comboEqui.getItems().addAll(lista);
        }
    }

    void preencherComboTipoEqui(){
        comboTipoEquiBase.getItems().clear();
        comboTipoEquiBase.getItems().addAll(TipoEquipamento.values());
    }

    private void init( EquiBase e,Consumer<NumeroComEqui> equipamentoConsumer,Consumer<EquiBase> saveHandler, TipoEquipamento[] tipoEqui, Supplier<List<EquiBase>> listaEquipamento,TelaInitController telaInit) {

        this.telaInitController = telaInit;
        this.equipamentoConsumer = equipamentoConsumer;
        this.saveHandler = saveHandler;
        comboTipo.getItems().addAll(tipoEqui);
        preencherComboEqui(List.of());
        preencherComboTipoEqui();
        comboEqui.setConverter(new StringConverter<EquiBase>() {
            @Override
            public String toString(EquiBase equiBase) {
                return (equiBase != null) ? equiBase.getNome() + " " + equiBase.getModelo() : ""; // Exibe apenas o nome
            }
            @Override
            public EquiBase fromString(String string) {
                return null; // Não necessário neste caso
            }
        });
        configurarSpinner(quantidade);
        painelQuantidade.setVisible(false);
        painelCadEquipamento.setVisible(false);
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

    void capturarTipoAbrirQuantidade(){
        EquiBaseService equiBaseService = telaInitController.returnBaseService();
        List<EquiBase> equiBases = equiBaseService.buscarTodos();
        if(comboTipo.getValue() == TipoEquipamento.RETIRADA){
            painelQuantidade.setVisible(true);
            preencherComboEqui(equiBaseService.buscarTodosEquiTipo(TipoEquipamento.RETIRADA));
        }else if (comboTipo.getValue() == TipoEquipamento.EMPRESTIMO){
            painelQuantidade.setVisible(false);
            preencherComboEqui(equiBaseService.buscarTodosEquiTipo(TipoEquipamento.EMPRESTIMO));
        }
    }

    void limpaCampoInicial(){
        patrimonio.setText("");
        preencherComboEqui(List.of());
    }

    void limparCadEquiBase(){
        nomeEqui.setText("");
        modeloEqui.setText("");
        especificacaoEqui.setText("");
    }
}
