package com.projeto.domRio1.doRio.controller;

import com.projeto.domRio1.doRio.controller.elementos.devolucao.TabelaDevolucao;
import com.projeto.domRio1.doRio.model.Emprestimo;
import com.projeto.domRio1.doRio.service.DevolucaoService;
import com.projeto.domRio1.doRio.service.EmprestimoService;
import com.projeto.domRio1.doRio.utils.CaixaDialogo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class DevolucaoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private DevolucaoService devolucaoService;

    @Autowired
    @Lazy
    private DevolucaoController thisController;

    @FXML
    private VBox devolucaoTabela;

    @FXML
    void abrirDialogoDevolucao(ActionEvent event) {
        Set<Emprestimo> lista = TabelaDevolucao.emprestimoLista;

        for (Emprestimo l : lista){
            System.out.println(l.toString());
        }
        if(lista != null && !lista.isEmpty()){
            boolean confirmação = CaixaDialogo.mostrarDialogoOpcao("Confirmação", "", "");

            if(confirmação){
                devolucaoService.devolver(lista);
            }

        }else {
            CaixaDialogo.mostrarDialogoAviso("Aviso", "","Selecione uma devolução para cadastrar!");
        }

        configurarTabela();
        TabelaDevolucao.emprestimoLista.removeAll(lista);


    }

    @FXML
    public void initialize() {
        configurarTabela();
    }
    public void configurarTabela() {
        devolucaoTabela.getChildren().clear();
        List<Emprestimo> emprestimos = new ArrayList<>(emprestimoService.emprestimosPendentes());
        boolean corColuna = false;
        for (Emprestimo ep : emprestimos) {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/templates/views/tabelas/TabelaDevolucao.fxml"));
            try {
                // Carregar o layout correto baseado no tipo do FXML
                Node node = fxmlLoader.load();  // Usando Node genérico
                // Obtendo o controller para setar os dados
                TabelaDevolucao ps = fxmlLoader.getController();
                ps.setData(ep);
                ps.meusServices(devolucaoService, emprestimoService,thisController);
                // Alternando cores para cada linha

                if (corColuna) {
                    node.setStyle("-fx-background-color: lightgray;"); // Cor para linhas ímpares
                } else {
                    node.setStyle("-fx-background-color: white;"); // Cor para linhas pares
                }

                // Alternar o valor de isEvenRow para a próxima iteração
                corColuna = !corColuna;
                // Adicionando o node carregado ao container (ex: VBox ou HBox)
                devolucaoTabela.getChildren().add(node);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void devolverEvent() {
    }
}
