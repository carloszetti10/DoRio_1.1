package com.projeto.domRio1.doRio.controller.elementos.telaInit;

import com.projeto.domRio1.doRio.controller.EquipamentoController;
import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.model.*;
import com.projeto.domRio1.doRio.service.EmprestimoService;
import com.projeto.domRio1.doRio.service.EquipamentoEmprestimoService;
import com.projeto.domRio1.doRio.service.EquipamentoRetiradaService;
import com.projeto.domRio1.doRio.service.EquipamentoService;
import com.projeto.domRio1.doRio.utils.CaixaDialogo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;

import java.util.Objects;
import java.util.Optional;

@Controller
public class InformacaoEquiController {

    @FXML
    private Label emprestadoPara;

    @FXML
    private TextArea especificacao;

    @FXML
    private Label idTela;

    @FXML
    private Label modelo;

    @FXML
    private Label nome;

    @FXML
    private TextArea obs;

    @FXML
    private TextField pat;

    @FXML
    private Spinner<Integer> quantidade;

    @FXML
    private Label textQuant;

    @FXML
    private Label tipo;

    @FXML
    private Button botaoAtualizar;

    @FXML
    private Label textoQuantEditar;

    @FXML
    void btnAtualizar(ActionEvent event) {
        try {
            boolean confirmacao = CaixaDialogo.mostrarDialogoOpcao("Confirmação", "", "Deseja editar o equipamento?");
            if (confirmacao){
                atualizar();
                CaixaDialogo.mostrarDialogoSucesso("Sucesso", "", "Editado com sucesso!");
            }
        } catch (AvisoException av){
            CaixaDialogo.mostrarDialogoAviso("Aviso", "", av.getMessage());
        } catch (Exception e) {
            CaixaDialogo.mostrarDialogoErro("Erro", "",e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    private EquipamentoEmprestimoService service;
    private EmprestimoService emprestimoService;
    EquipamentoRetiradaService equipamentoRetiradaService;
    EquipamentoService equipamentoService;
    private Long id;
    boolean editar;
    EquipamentoController equiController;

    public static void addNew(String text, EquipamentoEmprestimoService equiEmpretimoService, EquipamentoRetiradaService equipamentoRetiradaService, EquipamentoService equipamentoService, boolean editar, EquipamentoController equiController) {
        Long id = converterId(text);
        abrir(id, equiEmpretimoService, equipamentoRetiradaService, equipamentoService, editar, equiController);
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
    public static void abrir(Long id, EquipamentoEmprestimoService equiEmpretimoService, EquipamentoRetiradaService equipamentoRetiradaService, EquipamentoService equipamentoService, boolean editar, EquipamentoController equiController) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            FXMLLoader loader = new FXMLLoader(CadastroEquipamentoController.class.getResource("/templates/views/caixa/InformacaoEqui.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            InformacaoEquiController controller = loader.getController();
            controller.init(id, equiEmpretimoService, equipamentoRetiradaService, equipamentoService, editar, equiController);


            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void init(Long id, EquipamentoEmprestimoService equiEmpretimoService, EquipamentoRetiradaService equipamentoRetiradaService, EquipamentoService equipamentoService, boolean editar, EquipamentoController equiController) {
        this.id = id;
        this.service = equiEmpretimoService;
        this.emprestimoService = service.returnEmprestimoService();
        this.equipamentoRetiradaService = equipamentoRetiradaService;
        this.equipamentoService = equipamentoService;
        this.editar = editar;
        this.equiController = equiController;
        especificacao.setEditable(false);
        especificacao.setWrapText(true);
        pat.setEditable(false);
        organiarInfo();
        CadastroEquipamentoController.configurarSpinner(quantidade);
        setVisibleElementos();
    }
    void organiarInfo(){
        Optional<Equipamento> equiBase = equipamentoService.findById(id);

        if(equiBase.get().getTipo() == TipoEquipamento.EMPRESTIMO){
            organizarInfoEquiEmp(equiBase.get());
        }else if (equiBase.get().getTipo() == TipoEquipamento.RETIRADA){
            organizarInfoEquiRet(equiBase.get());
        }

    }
    private void organizarInfoEquiRet(Equipamento equi) {
        EquipamentoRetirada e = equipamentoRetiradaService.buscarEquipamentoRetirada(equi.getId(), equi.getCodigo());
        idTela.setText(String.valueOf(id));
        pat.setText(e.getEquipamentoRet().getCodigo());
        nome.setText(e.getEquipamentoRet().getEquipamentoBase().getNome());
        modelo.setText(e.getEquipamentoRet().getEquipamentoBase().getModelo());
        obs.setText(e.getEquipamentoRet().getObs());
        tipo.setText(e.getEquipamentoRet().getTipo().toString());
        especificacao.setText(e.getEquipamentoRet().getEquipamentoBase().getEspecificacao());
        textQuant.setText("Quantidade: ");
        emprestadoPara.setText(String.valueOf(e.getQuantidade()));
    }
    private void organizarInfoEquiEmp(Equipamento equipamento) {
        EquipamentoEmprestimo e = service.buscarEquipamentoEmprestimo(equipamento.getId(), equipamento.getCodigo());
        //Optional<EquipamentoEmprestimo> e = service.findById(this.id);
        idTela.setText(String.valueOf(id));
        pat.setText(e.getEquipamentoEmp().getCodigo());
        nome.setText(e.getEquipamentoEmp().getEquipamentoBase().getNome());
        modelo.setText(e.getEquipamentoEmp().getEquipamentoBase().getModelo());
        obs.setText(e.getEquipamentoEmp().getObs());
        especificacao.setText(e.getEquipamentoEmp().getEquipamentoBase().getEspecificacao());
        tipo.setText(e.getEquipamentoEmp().getTipo().toString());
        Emprestimo emprestimo = emprestimoService.emprestimoAberto(e);
        if(emprestimo != null){
            emprestadoPara.setText(emprestimo.getPessoa().getNome());
        }else{
            emprestadoPara.setText("N/A");
        }
        quantidade.setVisible(false);
        textoQuantEditar.setVisible(false);
    }
    public void setVisibleElementos(){
        if(!editar){
            obs.setEditable(false);
            obs.setWrapText(true);
            pat.setEditable(false);
            botaoAtualizar.setVisible(false);
            textoQuantEditar.setVisible(false);
            quantidade.setVisible(false);
        }
    }

    public void atualizar() {
        try {
            String tObs = obs.getText();
            Integer quant = quantidade.getValue();

            Equipamento equipamento = equipamentoService.findById(id)
                    .orElseThrow(() -> new AvisoException("Erro!"));

            String obsAtual = equipamento.getObs();
            boolean obsAlterada = (obsAtual != null) ? !obsAtual.equals(tObs) : (tObs != null && !tObs.isEmpty());

            if (equipamento.getTipo() == TipoEquipamento.RETIRADA) {
                if (quant == 0 && !obsAlterada) {
                    throw new AvisoException("Altere os campos para fazer edição.");
                }
            } else if (equipamento.getTipo() == TipoEquipamento.EMPRESTIMO) {
                if (!obsAlterada) {
                    throw new AvisoException("Altere os campos para fazer edição.");
                }
            }

            equipamentoService.editar(equipamento, tObs, quant);
            organiarInfo();
            equiController.setarTipoCombo(TipoEquipamento.RETIRADA);

        } catch (AvisoException e) {
            throw e;
        }
    }



}
