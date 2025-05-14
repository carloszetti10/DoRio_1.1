package com.projeto.domRio1.doRio.controller.elementos.emprestimo;


import com.projeto.domRio1.doRio.controller.EmprestimoController;
import com.projeto.domRio1.doRio.controller.TelaInitController;
import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.service.EmprestimoService;
import com.projeto.domRio1.doRio.service.EquiBaseService;
import com.projeto.domRio1.doRio.service.EquipamentoEmprestimoService;
import com.projeto.domRio1.doRio.service.EquipamentoService;
import com.projeto.domRio1.doRio.service.pessoa.PessoaService;
import com.projeto.domRio1.doRio.utils.MensagemPane;
import com.projeto.domRio1.doRio.utils.PatrimonioCadastroEmprestimo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;

@Controller
public class FormEmprestimo {

    EmprestimoService emprestimoService;
    EquipamentoEmprestimoService equiEmprestimoService;
    PessoaService pessoaService;
    EquipamentoService equipamentoService;
    EquiBaseService equiBase;
    TelaInitController telaInitController;
    EmprestimoController emprestimoController;
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

    public static void addNew(EmprestimoService emprestimoService, EquipamentoEmprestimoService equiEmprestimoService, PessoaService pessoaService, EquipamentoService equipamentoService, EquiBaseService equiBase, TelaInitController telaInitController, Set<PatrimonioCadastroEmprestimo> listaPatrimonio, EmprestimoController emprestimoController) {
        abrir(emprestimoService,equiEmprestimoService,pessoaService, equipamentoService, equiBase, telaInitController, listaPatrimonio, emprestimoController);
    }

    public static void abrir(EmprestimoService emprestimoService, EquipamentoEmprestimoService equiEmprestimoService, PessoaService pessoaService,EquipamentoService equipamentoService, EquiBaseService equiBase, TelaInitController telaInitController, Set<PatrimonioCadastroEmprestimo> listaPatrimonio, EmprestimoController emprestimoController) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            FXMLLoader loader = new FXMLLoader(FormEmprestimo.class.getResource("/templates/views/caixa/EmprestimoForm.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            FormEmprestimo controller = loader.getController();
            controller.init(emprestimoService,equiEmprestimoService,pessoaService, equipamentoService, equiBase, telaInitController, listaPatrimonio, emprestimoController);

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

            emprestimoService.salvarEmprestimo(pessoa, listaPatrimonio);
            //telaInitController.configurarTabela();
            //limpaCampoInicial();
            mensagemPane.mostrarSucesso("Emprestimo cadastrado com sucesso!");
            listaPatrimonio.removeAll(listaPatrimonio);
            preencherEquipamento();
            configurarTabela();
            //telaInitController.preecherTabela();
            telaInitController.iniciarTela();
            emprestimoController.configurarTabela(emprestimoService.todosEmprestimo());
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
        telaInitController.preecherTabela();
        listaPatrimonio.removeAll(listaPatrimonio);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void addPatrimonioEmprestar(ActionEvent event) {
        EquiBase equipamentoValue = equipamento.getValue();
        String patrimonioValue = patrimonio.getValue();
        if((equipamentoValue == null) || (patrimonioValue == null)){
            mensagemPane.mostrarAviso("Selecione um equipamento!");
            return;
        }
        //System.out.println(equipamentoValue + " "+ patrimonioValue + " EQUI TELA-----------");
        Equipamento equiBanco = equipamentoService.findByCodigoAndEquipamentoBase(patrimonioValue, equipamentoValue, TipoEquipamento.EMPRESTIMO);

        //System.out.println(equiBanco.getId() + equiBanco.getCodigo() + "EQUI VINDO DO BANCO-----------");
        if (equiBanco.getTipo() != TipoEquipamento.EMPRESTIMO){
            System.out.println("não pode ser emprestado");
        }
        converterParaPatCadEmp(equiBanco);
    }

    public static void removerListBy(PatrimonioCadastroEmprestimo pat){
        listaPatrimonio.remove(pat);
    }

    public void converterParaPatCadEmp(Equipamento equi) {
        // Criando um novo objeto de patrimônio com os dados do equipamento
        PatrimonioCadastroEmprestimo patCadEmprestimo = new PatrimonioCadastroEmprestimo();
        patCadEmprestimo.setIdEquipamento(equi.getId());
        patCadEmprestimo.setPatrimonioEquipamento(equi.getCodigo());
        patCadEmprestimo.setNome(equi.getEquipamentoBase().getNome());

        // Adiciona à lista
        listaPatrimonio.add(patCadEmprestimo);
        // Configura a tabela com a lista criada
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
                p.setData(pat);

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
                return null;
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
            if (equi.getEquipamentosEmprestimo() != null) { // Evita NullPointerException
                for (EquipamentoEmprestimo equiEmpre : equi.getEquipamentosEmprestimo()) {
                    if (Boolean.TRUE.equals(equiEmpre.getDisponivel())) { // Evita NullPointerException e compara corretamente
                        patrimonios.add(equi.getCodigo()); // Se precisar do código do empréstimo, troque por equiEmpre.getCodigo()
                    }
                }
            }
        }

        patrimonio.getItems().clear();
        patrimonio.getItems().addAll(patrimonios);
    }

    private void init(EmprestimoService emprestimoService, EquipamentoEmprestimoService equiEmprestimoService, PessoaService pessoaService, EquipamentoService equipamentoService, EquiBaseService equiBase, TelaInitController telaInitController, Set<PatrimonioCadastroEmprestimo> listaPatrimonio, EmprestimoController emprestimoController) {
        this.emprestimoService = emprestimoService;
        this.equiEmprestimoService = equiEmprestimoService;
        this.pessoaService = pessoaService;
        this.equipamentoService = equipamentoService;
        this.equiBase = equiBase;
        this.telaInitController = telaInitController;
        this.emprestimoController = emprestimoController;
        FormEmprestimo.listaPatrimonio = listaPatrimonio;
        configurarTabela();
        preencherPessoa();
        preencherEquipamento();
    }
}
