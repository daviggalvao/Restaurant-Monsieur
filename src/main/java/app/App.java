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
        TelaDelivery delivery = new TelaDelivery(primaryStage);
        TelaConta conta = new TelaConta(primaryStage);
        double[] lucrosTemporarios = {1500.0, 2200.0, 1800.0, 2500.0, 1950.0, 3000.0};
        double[] saidasTemporarias = {800.0, 1200.0, 950.0, 1300.0, 1000.0, 1500.0};
        tela.mostrar();
        // telaaa.mostrarReserva();
        // telaa.mostrarTelaServicos();
        //telaaaa.mostrarTelaGerente();
        //cardapio.mostrarTelaCardapio();
        //delivery.mostrar();
        //conta.mostrarTelaConta(lucrosTemporarios, saidasTemporarias);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
