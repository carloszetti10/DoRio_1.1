package com.projeto.domRio1.doRio.controller.elementos.usuario;

import com.projeto.domRio1.doRio.controller.elementos.emprestimo.FormEmprestimo;
import com.projeto.domRio1.doRio.exception.AvisoException;
import com.projeto.domRio1.doRio.exception.ErroException;
import com.projeto.domRio1.doRio.model.TipoUsuario;
import com.projeto.domRio1.doRio.model.Usuario;
import com.projeto.domRio1.doRio.service.UsuarioService;
import com.projeto.domRio1.doRio.utils.CaixaDialogo;
import com.projeto.domRio1.doRio.utils.MensagemPane;
import com.projeto.domRio1.doRio.utils.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;

@Controller
public class UsuarioController {

    UsuarioService usuarioService;

    @FXML
    public void initialize() {
        painelCadUsuario.setVisible(false);
        nomeUsuario.setText(SessaoUsuario.getUsuario().getNome());
        tipoUsuario.setText(SessaoUsuario.getUsuario().getTipo().name());
        usuario.setText(SessaoUsuario.getUsuario().getUsuarioLogar());
    }

    @FXML
    private TextField cadNome;

    @FXML
    private Button botaoAbriCad;

    @FXML
    private MensagemPane mensagemPane;

    @FXML
    private TextField cadUsuario;

    @FXML
    private ComboBox<TipoUsuario> comboTipoUsuario;

    @FXML
    private PasswordField confirmarSenha;

    @FXML
    private Label nomeUsuario;

    @FXML
    private AnchorPane painelCadUsuario;

    @FXML
    private PasswordField senhaa;

    @FXML
    private Label tipoUsuario;


    @FXML
    private Label usuario;


    @FXML
    void abrirpainelUsuario(ActionEvent event) {
        if(SessaoUsuario.getUsuario().getTipo() == TipoUsuario.Administrador){
            painelCadUsuario.setVisible(true);
        } else{
            CaixaDialogo.mostrarDialogoAviso("Aviso", "", "Você não tem permissão para cadastrar usuário.");
        }
    }

    @FXML
    void cadastrarUsuario(ActionEvent event) {
        try {
            String nomee = cadNome.getText();
            String usuarioo = cadUsuario.getText();
            TipoUsuario tipoo = comboTipoUsuario.getValue();
            String senhaaText = senhaa.getText();
            String confirmaSenhaa = confirmarSenha.getText();

            if (nomee == null || nomee.trim().isEmpty() ||
                    usuarioo == null || usuarioo.trim().isEmpty() ||
                    tipoo == null || senhaaText == null || senhaaText.trim().isEmpty() ||
                    confirmaSenhaa == null || confirmaSenhaa.trim().isEmpty()) {
                mensagemPane.mostrarAviso("Preencha todos os campos *");
            } else {
                boolean b = confirmarSenhaMetodo(senhaaText, confirmaSenhaa);
                if(!b){
                    mensagemPane.mostrarAviso("Senhas não coincidem!");
                }else {
                    Usuario u = new Usuario();
                    u.setNome(nomee);
                    u.setUsuarioLogar(usuarioo);
                    u.setTipo(tipoo);
                    u.setSenha(senhaaText);
                    usuarioService.salvarUsuario(u);
                    mensagemPane.mostrarSucesso("Usuário cadastrado!");
                    limparCampo();
                }
            }
        } catch (ErroException erroException) {
            mensagemPane.mostrarErro(erroException.getMessage());
        } catch (AvisoException e) {
            mensagemPane.mostrarAviso(e.getMessage());
        }


    }

    @FXML
    void fechaPainelcadUsuario(ActionEvent event) {
        painelCadUsuario.setVisible(false);
    }


    @FXML
    void fecharJanela(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    public static void abrir(UsuarioService usuarioService){
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            FXMLLoader loader = new FXMLLoader(FormEmprestimo.class.getResource("/templates/views/caixa/InformacaoUsuario.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);

            UsuarioController controller = loader.getController();
            controller.init(usuarioService);

            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void init(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        comboTipoUsuario.getItems().addAll(TipoUsuario.values());
        if(SessaoUsuario.getUsuario().getTipo() == TipoUsuario.Estoquista){
            botaoAbriCad.setVisible(false);
        }
    }

    public boolean confirmarSenhaMetodo( String senha, String confirmar){
        if(senha.equals(confirmar)){
            return true;
        }else{
            return false;
        }
    }

    public void limparCampo(){
        // Limpar os campos de texto
        cadNome.clear();
        cadUsuario.clear();
        senhaa.clear();
        confirmarSenha.clear();

        comboTipoUsuario.setValue(null); // Se você quiser deixar o combo box sem seleção
    }
}


