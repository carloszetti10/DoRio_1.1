package com.projeto.domRio1.doRio.utils;

public enum Menu {
    Inicial("Inicial"),
    Retirada("Retirada"),
    Solicitante("Solicitante"),
    Emprestimo("Emprestimo"),
    Equipamentoo("Equipamentoo"),
    Devolucao("Devolucao");


    private String title;

    Menu(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getFxml() {

        return String.format("/templates/views/%s.fxml", name());
    }
}
