package app;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Tela {
    protected Stage stage;
    public static boolean emFrances = false;
    public static String proximaTelaAposLogin = null;

    public Tela(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public abstract Scene criarScene();

    public void mostrarTela() {
        Scene scene = criarScene();
        stage.setScene(scene);
        stage.setMaximized(true);
    }
}