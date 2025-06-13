package app;

import javafx.stage.Stage;

public abstract class Tela {
    private Stage stage;
    public Tela(Stage stage) {this.stage = stage;}
    public Stage getStage() {return stage;}
    public abstract void mostrarTela();
}
