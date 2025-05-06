package com.projeto.domRio1.doRio.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class CaixaDialogo {
        public static void mostrarDialogoInformacao(String titulo, String cabecalho, String conteudo) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(cabecalho);
            alert.setContentText(conteudo);
            alert.showAndWait();
        }

        public static void mostrarDialogoAviso(String titulo, String cabecalho, String conteudo) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(titulo);
            alert.setHeaderText(cabecalho);
            alert.setContentText(conteudo);
            alert.showAndWait();
        }

        public static void mostrarDialogoErro(String titulo, String cabecalho, String conteudo) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(titulo);
            alert.setHeaderText(cabecalho);
            alert.setContentText(conteudo);
            alert.showAndWait();
        }

    public static boolean mostrarDialogoOpcao(String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);

        // Agora com ButtonData definidos corretamente
        ButtonType butConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(butConfirmar, buttonTypeCancel);

        // Adiciona o CSS
        alert.getDialogPane().getStylesheets().add(
                CaixaDialogo.class.getResource("/templates/views/style/application.css").toExternalForm()
        );

        // Aplica as classes CSS
        Button btnConfirmar = (Button) alert.getDialogPane().lookupButton(butConfirmar);
        btnConfirmar.getStyleClass().add("botao-login");

        Button btnCancelar = (Button) alert.getDialogPane().lookupButton(buttonTypeCancel);
        btnCancelar.getStyleClass().add("botao-cancelar");

        // Mostra o alerta
        Optional<ButtonType> result = alert.showAndWait();

        // Trata o resultado
        if (result.isEmpty()) {
            // Usuário fechou no X
            return false;
        } else if (result.get() == butConfirmar) {
            return true;
        } else {
            return false;
        }
    }

        public static void mostrarDialogoSucesso(String titulo, String cabecalho, String conteudo) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(cabecalho);
            alert.setContentText(conteudo);
            alert.showAndWait();
        }
}
