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
        tela.mostrar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
//da um bizu no q ta errado no bgl do javafx
