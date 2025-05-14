package com.projeto.domRio1.doRio.controller.elementos.telaInit;

import com.projeto.domRio1.doRio.controller.EquipamentoController;
import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.model.Equipamento;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import com.projeto.domRio1.doRio.model.EquipamentoRetirada;
import com.projeto.domRio1.doRio.model.TipoUsuario;
import com.projeto.domRio1.doRio.service.EquipamentoEmprestimoService;
import com.projeto.domRio1.doRio.service.EquipamentoRetiradaService;
import com.projeto.domRio1.doRio.service.EquipamentoService;
import com.projeto.domRio1.doRio.utils.CaixaDialogo;
import com.projeto.domRio1.doRio.utils.MensagemPane;
import com.projeto.domRio1.doRio.utils.PatrimonioCadastroEmprestimo;
import com.projeto.domRio1.doRio.utils.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class Tabelainit {

    EquipamentoEmprestimoService equiEmpretimoService;
    EquipamentoRetiradaService equiRetiradaService;
    EquipamentoService equipamentoService;
    EquipamentoController equipamentoController;

    @FXML
    private Button botaoEditar;

    @FXML
    private Button botaoExcluir;

    @FXML
    private Label codigo;

    @FXML
    private Label status;

    @FXML
    private Label id;

    @FXML
    private Label idEquiBase;

    @FXML
    private Label modelo;

    @FXML
    private Label nomeEqui;

    @FXML
    private Label tipo;


    @FXML
    void editar(ActionEvent event) {
    }

    @FXML
    void clickNome(MouseEvent event) {
        abrirTelaInfo();
    }

    @FXML
    void excluir(ActionEvent event) {
        try {
            if(SessaoUsuario.getUsuario().getTipo() == TipoUsuario.Administrador ){
                boolean confirmacao = CaixaDialogo.mostrarDialogoOpcao("Confirmação", "", "Deseja realmente apagar esse equipamento?");
                if (confirmacao){
                    Optional<Equipamento> byId = equipamentoService.findById(Long.valueOf(this.idEquiBase.getText()));
                    equipamentoService.setarApagado(byId.get());
                    CaixaDialogo.mostrarDialogoSucesso("Sucesso", "", "Equipamento Apagado com sucesso!");
                    equipamentoController.initialize();
                }
            }
            else {
                CaixaDialogo.mostrarDialogoAviso("Aviso", "", "Você não tem permissão para apagar equipamento!");
            }
        } catch (AvisoException a){
            CaixaDialogo.mostrarDialogoAviso("Aviso", "", a.getMessage());
        } catch (RuntimeException e) {
            CaixaDialogo.mostrarDialogoErro("Erro", "", e.getMessage());
        }

    }

    @FXML
    void informacao(ActionEvent event) {
        abrirTelaInfo();
    }

    @FXML
    private CheckBox caixaButton;

    public static Set<PatrimonioCadastroEmprestimo>  lista = new HashSet<>();

    @FXML
    void preencheuACaixa(ActionEvent event) {
        if(caixaButton.isSelected()){
            Long idConvet = Long.valueOf(String.valueOf(this.id.getText()));
            Optional<EquipamentoEmprestimo> ep = equiEmpretimoService.findById(idConvet);
            if (ep.get().getDisponivel()){
                PatrimonioCadastroEmprestimo pat = new PatrimonioCadastroEmprestimo();
                pat.setNome(ep.get().getEquipamentoEmp().getEquipamentoBase().getNome());
                pat.setIdEquipamento(ep.get().getEquipamentoEmp().getId());
                pat.setPatrimonioEquipamento(ep.get().getEquipamentoEmp().getCodigo());
                lista.add(pat);
            }
        }else {
            Long idConvet = Long.valueOf(String.valueOf(this.id.getText()));
            Optional<EquipamentoEmprestimo> ep = equiEmpretimoService.findById(idConvet);

            PatrimonioCadastroEmprestimo pat = new PatrimonioCadastroEmprestimo();
            pat.setNome(ep.get().getEquipamentoEmp().getEquipamentoBase().getNome());
            pat.setIdEquipamento(ep.get().getEquipamentoEmp().getId());
            pat.setPatrimonioEquipamento(ep.get().getEquipamentoEmp().getCodigo());
            lista.remove(pat);
        }
    }

    void abrirTelaInfo(){
        InformacaoEquiController.addNew(this.idEquiBase.getText(), this.equiEmpretimoService, this.equiRetiradaService, this.equipamentoService);
    }

    public void setData(EquipamentoEmprestimo ep) {
        id.setText(String.valueOf(ep.getId()));
        idEquiBase.setText(String.valueOf(ep.getEquipamentoEmp().getId()));
        codigo.setText(ep.getEquipamentoEmp().getCodigo());
        modelo.setText(ep.getEquipamentoEmp().getEquipamentoBase().getModelo());
        nomeEqui.setText(ep.getEquipamentoEmp().getEquipamentoBase().getNome());
        tipo.setText(ep.getEquipamentoEmp().getTipo().toString());
        if (ep.getDisponivel()) {
            status.setStyle("-fx-background-color:  #00bf63; -fx-text-fill: white;");
            //status.setStyle("-fx-text-fill: #00bf63;");
            status.setText("Disponível");
        } else {
            status.setStyle("-fx-background-color:  #ff3131; -fx-text-fill: white;");
            //status.setStyle("-fx-text-fill:#ff3131;");
            status.setText("Indisponível");
        }
    }

    public void meuService(EquipamentoEmprestimoService equiEmpretimoService, EquipamentoService equipamentoService, EquipamentoController equipamentoController, EquipamentoRetiradaService equiRetiradaService) {
        this.equiEmpretimoService = equiEmpretimoService;
        this.equipamentoService = equipamentoService;
        this.equipamentoController = equipamentoController;
        this.equiRetiradaService = equiRetiradaService;
    }


    public void setDataReirada(EquipamentoRetirada ep) {
        id.setText(String.valueOf(ep.getId()));
        idEquiBase.setText(String.valueOf(ep.getEquipamentoRet().getId()));
        codigo.setText(ep.getEquipamentoRet().getCodigo());
        modelo.setText(ep.getEquipamentoRet().getEquipamentoBase().getModelo());
        nomeEqui.setText(ep.getEquipamentoRet().getEquipamentoBase().getNome());
        tipo.setText(ep.getEquipamentoRet().getTipo().toString());
        if (ep.getQuantidade() > 0) {
            status.setStyle("-fx-background-color:  #00bf63; -fx-text-fill: white;");
            status.setText(String.valueOf(ep.getQuantidade()));
        } else {
            status.setStyle("-fx-background-color:  #ff3131; -fx-text-fill: white;");
            //status.setStyle("-fx-text-fill:#ff3131;");
            status.setText(String.valueOf(ep.getQuantidade()));
        }
    }

    public void setVisibleBotao(){
        botaoEditar.setVisible(false);
        botaoExcluir.setVisible(false);
    }
}


