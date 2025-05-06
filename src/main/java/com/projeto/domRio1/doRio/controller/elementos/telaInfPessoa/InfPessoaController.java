package com.projeto.domRio1.doRio.controller.elementos.telaInfPessoa;

import com.projeto.domRio1.doRio.controller.elementos.telaInit.CadastroEquipamentoController;
import com.projeto.domRio1.doRio.model.Emprestimo;
import com.projeto.domRio1.doRio.model.Pessoa;
import com.projeto.domRio1.doRio.service.pessoa.PessoaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class InfPessoaController {
    @FXML
    private VBox emprestimoInfoPessoas;
    @FXML
    private Label nomeSolicitante;

    private Long id;
    PessoaService pessoaService;

    public static void abrirInfoPessoa(String text, PessoaService pessoaService) {
        Long id = converterId(text);
        abrir(id, pessoaService);
    }

    @FXML
    void fecharJanela(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public static Long converterId(String text) {
        try {
            Long id = Long.parseLong(text);
            return id;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("");
        }
    }

    public static void abrir(Long id, PessoaService pessoaService){
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            FXMLLoader loader = new FXMLLoader(CadastroEquipamentoController.class.getResource("/templates/views/caixa/InfoPessoa.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            InfPessoaController controller = loader.getController();
            controller.init(id, pessoaService);

            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void init(Long id, PessoaService pessoaService) {
        this.id = id;
        this.pessoaService = pessoaService;
        configurarTabela();
        pegarNomePessoa();
    }

    public void pegarNomePessoa(){
        Optional<Pessoa> pessoa = pessoaService.findById(this.id);
        nomeSolicitante.setText(pessoa.get().getNome());
    }


    public void configurarTabela() {
        emprestimoInfoPessoas.getChildren().clear();
        List<Emprestimo> emprestimos = new ArrayList<>(pessoaService.todosEmprestimoNaoDevolvido(this.id));
        boolean corColuna = false;
        for (Emprestimo e : emprestimos){

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/templates/views/tabelas/TabelaInfPessoa.fxml"));
            try {
                // Carregar o layout correto baseado no tipo do FXML
                Node node = fxmlLoader.load();  // Usando Node genérico
                // Obtendo o controller para setar os dados
                TabelaInfPessoa ps = fxmlLoader.getController();
                ps.setDadosEmprestimo(e);
                //ps.meuService(this.equiEmpretimoService);

                // Alternando cores para cada linha

                if (corColuna) {
                    node.setStyle("-fx-background-color: lightgray;"); // Cor para linhas ímpares
                } else {
                    node.setStyle("-fx-background-color: white;"); // Cor para linhas pares
                }

                // Alternar o valor de isEvenRow para a próxima iteração
                corColuna = !corColuna;
                // Adicionando o node carregado ao container (ex: VBox ou HBox)
                emprestimoInfoPessoas.getChildren().add(node);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
