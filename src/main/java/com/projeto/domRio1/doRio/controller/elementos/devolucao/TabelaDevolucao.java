package com.projeto.domRio1.doRio.controller.elementos.devolucao;

import com.projeto.domRio1.doRio.controller.DevolucaoController;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.Emprestimo;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import com.projeto.domRio1.doRio.service.DevolucaoService;
import com.projeto.domRio1.doRio.service.EmprestimoService;
import com.projeto.domRio1.doRio.utils.CaixaDialogo;
import com.projeto.domRio1.doRio.utils.PatrimonioCadastroEmprestimo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class TabelaDevolucao {


    EmprestimoService emprestimoService;
    DevolucaoService devolucaoService;
    DevolucaoController devolucaoController;

    @FXML
    private CheckBox caixaMarcacao;
    @FXML
    private Label id;
    @FXML
    private Label dataEmprestimo;
    @FXML
    private Label equipamento;
    @FXML
    private Label solicitante;
    @FXML
    private Label status;
    @FXML
    private Label usuarioEmprestou;

    public static Set<Emprestimo> emprestimoLista = new HashSet<>();
    @FXML
    void EventoCaixa(ActionEvent event) {
        Long idConvet = Long.valueOf(String.valueOf(this.id.getText()));
        Optional<Emprestimo> emp = emprestimoService.findById(idConvet);
        if(caixaMarcacao.isSelected()){
            if (!emp.get().getEntregue()){
                emprestimoLista.add(emp.get());
                System.out.println("Adicionando: " + emp.get());
            }
        }else {
             emprestimoLista.remove(emp.get());
            System.out.println("Tentando remover: " + emp.get());
        }
    }

    @FXML
    void bntDevolver(ActionEvent event) {

        Long id = Long.parseLong(this.id.getText());
        Optional<Emprestimo> e = emprestimoService.findById(id);
        String equi = e.get().getEquipamento().getEquipamentoEmp().getEquipamentoBase().getNome()+" "+ e.get().getEquipamento().getEquipamentoEmp().getCodigo();

        boolean botao = CaixaDialogo.mostrarDialogoOpcao("Confirmação", "", "Deseja realmente Realizar devolução do Equipamento: " + equi);

        try {
            if(botao){
                Set<Emprestimo> lista = new HashSet<>();
                lista.add(e.get());
                devolucaoService.devolver(lista);
            }
        } catch (ErroException ex) {
            CaixaDialogo.mostrarDialogoErro("Erro", "", "Não foi possível registrar a devolução!");
        }

        devolucaoController.configurarTabela();

    }

    public void setData(Emprestimo e){
        id.setText(String.valueOf(e.getId()));
        solicitante.setText(e.getPessoa().getNome());
        equipamento.setText(e.getEquipamento().getEquipamentoEmp().getEquipamentoBase().getNome()+ " "+e.getEquipamento().getEquipamentoEmp().getCodigo());
        status.setText("Pendente");
        usuarioEmprestou.setText(e.getQuemEmprestou().getNome());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dataEmprestimo.setText(e.getDataEmprestimo().format(formatter));
    }

    public void meusServices(DevolucaoService devolucaoService, EmprestimoService emprestimoService, DevolucaoController devolucaoController) {
        this.devolucaoService = devolucaoService;
        this.emprestimoService = emprestimoService;
        this.devolucaoController = devolucaoController;
    }
}
