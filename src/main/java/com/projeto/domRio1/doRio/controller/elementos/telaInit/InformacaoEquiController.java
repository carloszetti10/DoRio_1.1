package com.projeto.domRio1.doRio.controller.elementos.telaInit;

import com.projeto.domRio1.doRio.model.Emprestimo;
import com.projeto.domRio1.doRio.model.EquipamentoEmprestimo;
import com.projeto.domRio1.doRio.service.EmprestimoService;
import com.projeto.domRio1.doRio.service.EquipamentoEmprestimoService;
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

    @Autowired
    private EquipamentoEmprestimoService service;
    @Autowired
    private EmprestimoService emprestimoService;
    private Long id;

    public static void addNew(String text, EquipamentoEmprestimoService equiEmpretimoService) {
        Long id = converterId(text);
        abrir(id, equiEmpretimoService);
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


    public static void abrir(Long id, EquipamentoEmprestimoService equiEmpretimoService ) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            FXMLLoader loader = new FXMLLoader(CadastroEquipamentoController.class.getResource("/templates/views/caixa/InformacaoEqui.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            InformacaoEquiController controller = loader.getController();
            controller.init(id, equiEmpretimoService);


            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init(Long id, EquipamentoEmprestimoService equiEmpretimoService) {
        this.id = id;
        this.service = equiEmpretimoService;
        this.emprestimoService = service.returnEmprestimoService();
        organiarInfo();
    }

    void organiarInfo(){
        Optional<EquipamentoEmprestimo> e = service.findById(this.id);
        pat.setText(e.get().getEquipamentoEmp().getCodigo());
        nome.setText(e.get().getEquipamentoEmp().getEquipamentoBase().getNome());
        modelo.setText(e.get().getEquipamentoEmp().getEquipamentoBase().getModelo());
        if(obs.getText().isEmpty()){
            obs.setText(e.get().getEquipamentoEmp().getObs());
        }
        tipo.setText(e.get().getEquipamentoEmp().getTipo().toString());
        especificacao.setText(e.get().getEquipamentoEmp().getEquipamentoBase().getEspecificacao());
        Emprestimo emprestimo = emprestimoService.emprestimoAberto(e.get());
        if(emprestimo != null){
            emprestadoPara.setText(emprestimo.getPessoa().getNome());
        }else{
            emprestadoPara.setText("N/A");
        }
    }



}
