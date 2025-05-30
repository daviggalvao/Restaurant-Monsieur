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
        Reserva telaaa = new Reserva((primaryStage));
        TelaGerente telaaaa = new TelaGerente(primaryStage);
        tela.mostrar();
        //telaaa.mostrarReserva();
        //telaa.mostrarTelaServicos();
        //telaaaa.mostrarTelaGerente();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
