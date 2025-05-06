package com.projeto.domRio1.doRio.utils;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

@Component
public class MensagemPane extends HBox {

    private Label labelMensagem;
    private SVGPath icone;

    public MensagemPane() {
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-padding: 10px; -fx-background-radius: 5px; -fx-border-radius: 5px;");

        icone = new SVGPath();
        labelMensagem = new Label();
        labelMensagem.setStyle("-fx-font-size: 14px;");

        this.getChildren().addAll(icone, labelMensagem);
        this.setVisible(false); // Começa invisível
    }

    private void mostrarMensagem(String mensagem, String corFundo, String corTexto, String iconeSVG) {
        this.setStyle("-fx-background-color: " + corFundo + ";");
        labelMensagem.setText(mensagem);
        labelMensagem.setTextFill(Color.web(corTexto));
        icone.setContent(iconeSVG);
        icone.setFill(Color.web(corTexto));

        this.setVisible(true);

        // Animação para desaparecer após 3 segundos
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(5), this);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> this.setVisible(false));
        fadeOut.play();
    }

    public void mostrarErro(String mensagem) {
        String iconeErro = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM13 16h-2v-2h2v2zm0-4h-2V8h2v4z";
        mostrarMensagem(mensagem, "#ffcccc", "#d32f2f", iconeErro);
    }

    public void mostrarSucesso(String mensagem) {
        String iconeSucesso = "M10 20.5L2.5 13 4.91 10.59 10 15.67 19.09 5.5 21.5 7.91 10 20.5Z";
        mostrarMensagem(mensagem, "#c8e6c9", "#388e3c", iconeSucesso);
    }

    public void mostrarAviso(String mensagem) {
        String iconeAviso = "M12 2L1 21h22L12 2zm1 16h-2v-2h2v2zm0-4h-2v-4h2v4z";
        mostrarMensagem(mensagem, "#fff3cd", "#856404", iconeAviso);
    }
}

