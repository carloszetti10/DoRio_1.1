package com.projeto.domRio1.doRio.controller;

import com.projeto.domRio1.doRio.controller.elementos.telaPessoa.CadastroPessoaController;
import com.projeto.domRio1.doRio.controller.elementos.telaPessoa.TabelaPessoa;
import com.projeto.domRio1.doRio.model.Pessoa;
import com.projeto.domRio1.doRio.service.DepartamentoService;
import com.projeto.domRio1.doRio.service.pessoa.PessoaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SolicitanteController {

    @FXML
    private VBox pessoaTabela;

    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private DepartamentoService departamentoService;


    @Autowired
    @Lazy
    private SolicitanteController thisController;



    @FXML
    void abrirCadastro(ActionEvent event){
        CadastroPessoaController.addNew(this.pessoaService, this.departamentoService, this.thisController);
    }

    @FXML
    public void initialize() {
        configurarTabela();
    }

    public void configurarTabela() {
        pessoaTabela.getChildren().clear();
        List<Pessoa> pessoas = new ArrayList<>(pessoaService.listarTodos());
        boolean corColuna = false;
        for (Pessoa p : pessoas){

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/templates/views/tabelas/TabelaPessoa.fxml"));
            try {
                // Carregar o layout correto baseado no tipo do FXML
                Node node = fxmlLoader.load();  // Usando Node genérico
                // Obtendo o controller para setar os dados
                TabelaPessoa ps = fxmlLoader.getController();
                ps.setData(p);
                //ps.meuService(this.equiEmpretimoService);
                ps.meuService(this.pessoaService);

                // Alternando cores para cada linha

                if (corColuna) {
                    node.setStyle("-fx-background-color: lightgray;"); // Cor para linhas ímpares
                } else {
                    node.setStyle("-fx-background-color: white;"); // Cor para linhas pares
                }

                // Alternar o valor de isEvenRow para a próxima iteração
                corColuna = !corColuna;
                // Adicionando o node carregado ao container (ex: VBox ou HBox)
                pessoaTabela.getChildren().add(node);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
