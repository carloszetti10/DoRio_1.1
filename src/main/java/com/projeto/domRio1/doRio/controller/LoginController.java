package com.projeto.domRio1.doRio.controller;


import com.projeto.domRio1.doRio.DoRioApplication;
import com.projeto.domRio1.doRio.service.UsuarioService;
import com.projeto.domRio1.doRio.utils.MensagemPane;
import com.projeto.domRio1.doRio.utils.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class LoginController {

    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField compoSenha;

    @FXML
    private MensagemPane mensagemPane;

    @Autowired
    private UsuarioService usuarioService;

    @FXML
    void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    void fazerLogin(ActionEvent event) {
        String usuarioText = campoUsuario.getText();
        String senhaText = compoSenha.getText();

        if(usuarioText.isEmpty() || senhaText.isEmpty()){
            mensagemPane.mostrarAviso("Preencha usuário e senha!");
        }else{
            if (usuarioService.autenticar(usuarioText, senhaText)) {
                SessaoUsuario.setUsuario(usuarioService.getUsuario(usuarioText).get());
                MainController.abrirMain(stage);
            } else {
                mensagemPane.mostrarErro("Usuário ou senha incorreta!");
            }
        }


    }


    public static void loadView(Stage stage) {

        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/templates/views/LoginView.fxml"));
            loader.setControllerFactory(DoRioApplication.getApplicationContext()::getBean);

            stage.initStyle(StageStyle.UNDECORATED);
            Parent view = loader.load();
            stage.setScene(new Scene(view));

            LoginController controller = loader.getController();
            controller.passarStage(stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage stage;
    private void passarStage(Stage stage) {
        this.stage = stage;
    }
}
