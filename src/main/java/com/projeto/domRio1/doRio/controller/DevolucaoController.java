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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
    private ListView<Emprestimo> devolucaoTabela;

    @FXML
    void abrirDialogoDevolucao(ActionEvent event) {
        try{
            Set<Emprestimo> lista = TabelaDevolucao.emprestimoLista;
            for (Emprestimo l : lista){
                System.out.println(l.toString());
            }
            if(lista != null && !lista.isEmpty()){
                boolean confirmacao = CaixaDialogo.mostrarDialogoOpcao("Confirmação", "", "Deseja realizar a devolução?");
                if(confirmacao){
                    devolucaoService.devolver(lista);
                    CaixaDialogo.mostrarDialogoSucesso("Sucesso", "", "Devolução cadastrada!");
                }
            }else {
                CaixaDialogo.mostrarDialogoAviso("Aviso", "","Selecione uma devolução para cadastrar!");
            }
            configurarTabela(emprestimoService.emprestimosPendentes());
            TabelaDevolucao.emprestimoLista.removeAll(lista);
        }catch (Exception ex){
            CaixaDialogo.mostrarDialogoErro("Erro", "", "Não foi possível registrar a devolução!");
        }

    }

    @FXML
    public void initialize() {
        configurarTabela(emprestimoService.emprestimosPendentes());
    }
    public void configurarTabela(List<Emprestimo> lista) {

        devolucaoTabela.setCellFactory(list -> new ListCell<>() {
            private FXMLLoader loader;

            @Override
            protected void updateItem(Emprestimo emprestimo, boolean empty) {
                super.updateItem(emprestimo, empty);
                if (empty || emprestimo == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (loader == null) {
                        loader = new FXMLLoader(getClass().getResource("/templates/views/tabelas/TabelaDevolucao.fxml"));
                        try {
                            setGraphic(loader.load());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    TabelaDevolucao ps = loader.getController();
                    ps.setData(emprestimo);
                    ps.meusServices(devolucaoService, emprestimoService,thisController);
                }
            }
        });

        devolucaoTabela.getItems().clear();
        // Adicionar
        devolucaoTabela.getItems().addAll(
               lista
        );
    }

    @FXML
    private TextField campoPesquisa;

    @FXML
    void campoPesquisaKey(KeyEvent event) {
        String text = campoPesquisa.getText();
        configurarTabela(devolucaoService.listaPorPar(text));
    }

}
