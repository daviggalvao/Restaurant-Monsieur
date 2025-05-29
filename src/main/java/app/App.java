package app;
import classes.*;
import database.*;
import javafx.application.Application;
import javafx.stage.Stage;
import app.*;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) {
        Reserva tela = new Reserva(primaryStage);
        tela.mostrarReserva();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

