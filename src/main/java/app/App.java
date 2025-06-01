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
        TelaReserva telaaa = new TelaReserva(primaryStage);
        TelaGerente telaaaa = new TelaGerente(primaryStage);
        TelaCardapio cardapio = new TelaCardapio(primaryStage);
         tela.mostrar();
        // telaaa.mostrarReserva();
        // telaa.mostrarTelaServicos();
        //telaaaa.mostrarTelaGerente();
        //cardapio.mostrarTelaCardapio();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
