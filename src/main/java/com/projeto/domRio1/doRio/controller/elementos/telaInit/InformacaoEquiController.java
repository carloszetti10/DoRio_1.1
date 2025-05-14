package com.projeto.domRio1.doRio.controller.elementos.telaInit;

import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.service.EmprestimoService;
import com.projeto.domRio1.doRio.service.EquipamentoEmprestimoService;
import com.projeto.domRio1.doRio.service.EquipamentoRetiradaService;
import com.projeto.domRio1.doRio.service.EquipamentoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.Optional;

@Controller
public class InformacaoEquiController {

    @FXML
    private Label emprestadoPara;

    @FXML
    private Label especificacao;

    @FXML
    private Label modelo;

    @FXML
    private Label nome;

    @FXML
    private Label pat;

    @FXML
    private Label tipo;

    @FXML
    private Label obs;

    private EquipamentoEmprestimoService service;
    private EmprestimoService emprestimoService;
    EquipamentoRetiradaService equipamentoRetiradaService;
    EquipamentoService equipamentoService;
    private Long id;

    public static void addNew(String text, EquipamentoEmprestimoService equiEmpretimoService, EquipamentoRetiradaService equipamentoRetiradaService, EquipamentoService equipamentoService) {
        Long id = converterId(text);
        abrir(id, equiEmpretimoService, equipamentoRetiradaService, equipamentoService);
    }

    public static Long converterId(String text) {
        try {
            Long id = (Long) Long.parseLong(text);
            return id;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("");
        }
    }

    @FXML
    void fecharJanela(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    public static void abrir(Long id, EquipamentoEmprestimoService equiEmpretimoService, EquipamentoRetiradaService equipamentoRetiradaService, EquipamentoService equipamentoService) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            FXMLLoader loader = new FXMLLoader(CadastroEquipamentoController.class.getResource("/templates/views/caixa/InformacaoEqui.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            InformacaoEquiController controller = loader.getController();
            controller.init(id, equiEmpretimoService, equipamentoRetiradaService, equipamentoService);


            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init(Long id, EquipamentoEmprestimoService equiEmpretimoService, EquipamentoRetiradaService equipamentoRetiradaService, EquipamentoService equipamentoService) {
        this.id = id;
        this.service = equiEmpretimoService;
        this.emprestimoService = service.returnEmprestimoService();
        this.equipamentoRetiradaService = equipamentoRetiradaService;
        this.equipamentoService = equipamentoService;
        organiarInfo();
    }

    void organiarInfo(){
        Optional<Equipamento> equiBase = equipamentoService.findById(id);

        if(equiBase.get().getTipo() == TipoEquipamento.EMPRESTIMO){
            organizarInfoEquiEmp(equiBase.get());
        }else {
            organizarInfoEquiRet(equiBase.get());
        }

    }

    @FXML
    private Label textQuant;

    private void organizarInfoEquiRet(Equipamento equi) {
        EquipamentoRetirada e = equipamentoRetiradaService.buscarEquipamentoRetirada(equi.getId(), equi.getCodigo());
        pat.setText(e.getEquipamentoRet().getCodigo());
        nome.setText(e.getEquipamentoRet().getEquipamentoBase().getNome());
        modelo.setText(e.getEquipamentoRet().getEquipamentoBase().getModelo());
        if(obs.getText().isEmpty()){
            obs.setText(e.getEquipamentoRet().getObs());
        }
        tipo.setText(e.getEquipamentoRet().getTipo().toString());
        especificacao.setText(e.getEquipamentoRet().getEquipamentoBase().getEspecificacao());
        textQuant.setText("Quantidade: ");
        emprestadoPara.setText(String.valueOf(e.getQuantidade()));
    }

    private void organizarInfoEquiEmp(Equipamento equipamento) {
        EquipamentoEmprestimo e = service.buscarEquipamentoEmprestimo(equipamento.getId(), equipamento.getCodigo());
        //Optional<EquipamentoEmprestimo> e = service.findById(this.id);
        pat.setText(e.getEquipamentoEmp().getCodigo());
        nome.setText(e.getEquipamentoEmp().getEquipamentoBase().getNome());
        modelo.setText(e.getEquipamentoEmp().getEquipamentoBase().getModelo());
        if(obs.getText().isEmpty()){
            obs.setText(e.getEquipamentoEmp().getObs());
        }
        tipo.setText(e.getEquipamentoEmp().getTipo().toString());
        especificacao.setText(e.getEquipamentoEmp().getEquipamentoBase().getEspecificacao());
        Emprestimo emprestimo = emprestimoService.emprestimoAberto(e);
        if(emprestimo != null){
            emprestadoPara.setText(emprestimo.getPessoa().getNome());
        }else{
            emprestadoPara.setText("N/A");
        }
    }


}
