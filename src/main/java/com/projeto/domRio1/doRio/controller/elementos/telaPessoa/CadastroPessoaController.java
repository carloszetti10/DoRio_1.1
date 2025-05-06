package com.projeto.domRio1.doRio.controller.elementos.telaPessoa;

import com.projeto.domRio1.doRio.controller.SolicitanteController;
import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.service.DepartamentoService;
import com.projeto.domRio1.doRio.service.pessoa.PessoaService;
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
import org.springframework.stereotype.Controller;
import java.util.List;


@Controller
public class CadastroPessoaController {

    @FXML
    private Pane painelCadDepartamento;

    @FXML
    private TextField nome;

    @FXML
    private ComboBox<Departamento> comboDepartamento;

    @FXML
    private TextField local;

    @FXML
    private TextField telefone;

    @FXML
    private TextField nomeDepartamento;

    @FXML
    private MensagemPane mensagemPane;

    void limpaCampoInicial(){
        nome.setText("");
        telefone.setText("");
        preencherComboEqui();
    }

    void limparCadDepartamento(){
         nomeDepartamento.setText("");
         local.setText("");
    }

    @FXML
    void abrirCad(ActionEvent event){
        painelCadDepartamento.setVisible(true);
    }

    @FXML
    void fecharPaneCad(ActionEvent event){
        painelCadDepartamento.setVisible(false);
    }

    private PessoaService pessoaService;
    private DepartamentoService departamentoService;
    private SolicitanteController solicitanteController;


    @FXML
    void cadastrarPessoa(ActionEvent event) {
        try {

            String nomePessoa = this.nome.getText();
            Departamento departamento = this.comboDepartamento.getValue();
            String telefone = this.telefone.getText();

            if (nomePessoa == null || nomePessoa.isEmpty() || departamento == null) {
                throw new AvisoException("Preencha todos os campos obrigatórios *");
            }

            // Criando o objeto
            Pessoa p = new Pessoa();
            p.setNome(nomePessoa);
            p.setTelefone(telefone);
            p.setSetor(departamento);
            pessoaService.salva(p);
            limpaCampoInicial();
            solicitanteController.configurarTabela();
            mensagemPane.mostrarSucesso("Pessoa cadastrada com sucesso!");
        } catch (ErroException erroException) {
            mensagemPane.mostrarErro(erroException.getMessage());
        } catch (AvisoException e) {
            // Exibe um aviso se os campos estiverem inválidos
            mensagemPane.mostrarAviso(e.getMessage());
        }
    }

    @FXML
    void cadastrarDepartamento(ActionEvent event) {
        try {
            String nome = this.nomeDepartamento.getText();
            String local = this.local.getText();

            if (nome == null || nome.trim().isEmpty()) {
                throw new AvisoException("Campo setor e obrigatório!");
            }
            // Criando o objeto e definindo os valores
            Departamento d = new Departamento();
            d.setNomeDepartamento(nome);
            d.setLocal(local);

            departamentoService.salva(d);
            // Se tudo ocorrer bem, exibe a mensagem de sucesso
            mensagemPane.mostrarSucesso("Departamento cadastrado com sucesso!");
            preencherComboEqui();
            limparCadDepartamento();
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
    public static void addNew(PessoaService pessoaService, DepartamentoService departamentoService, SolicitanteController solicitanteController) {
        abrir(pessoaService, departamentoService, solicitanteController);
    }

    public static void abrir(PessoaService pessoaService, DepartamentoService departamentoService, SolicitanteController solicitanteController) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            FXMLLoader loader = new FXMLLoader(CadastroPessoaController.class.getResource("/templates/views/caixa/FormPessoa.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            CadastroPessoaController controller = loader.getController();
            controller.init(pessoaService, departamentoService, solicitanteController);


            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void preencherComboEqui(){
        comboDepartamento.getItems().clear();
        List<Departamento> departamentos =  departamentoService.listarTodos();
        if (departamentos != null) {
            comboDepartamento.getItems().addAll(departamentos);
            comboDepartamento.setConverter(new StringConverter<Departamento>() {
                @Override
                public String toString(Departamento d) {
                    return (d != null) ? d.getNomeDepartamento() : ""; // Exibe apenas o nome
                }
                @Override
                public Departamento fromString(String string) {
                    return null;
                }
            });
        }
    }

    //List<EquiBase> equipamentos;
    private void init(PessoaService pessoaService, DepartamentoService departamentoService, SolicitanteController solicitanteController) {

        this.departamentoService = departamentoService;
        this.pessoaService = pessoaService;
        this.solicitanteController = solicitanteController;
        preencherComboEqui();
        painelCadDepartamento.setVisible(false);
    }



}
