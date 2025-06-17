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
        TelaPagamento telaPagamento = new TelaPagamento(primaryStage);
        TelaEstoque estoque = new TelaEstoque(primaryStage);
        TelaCriarConta telacriar = new TelaCriarConta(primaryStage);
        //estoque.mostrarTelaEstoque();
        //tela.mostrarTela();
       telaaa.mostrarTela();
        //telaa.mostrarTelaServicos();
       //telaaaa.mostrarTelaGerente();
        //cardapio.mostrarTelaCardapio();
        //delivery.mostrar();
        //conta.mostrarTelaConta();
        //telaPagamento.mostrarPagamento();
        //telacriar.mostrarTelaCriarConta();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
