package app;
import classes.*;
import database.*;
import javafx.application.Application;
import javafx.stage.Stage;
import app.*;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) {
        TelaInicial tela = new TelaInicial(primaryStage);
        TelaServicos telaa = new TelaServicos(primaryStage);
        tela.mostrar();
        //telaa.mostrarTelaServicos();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
