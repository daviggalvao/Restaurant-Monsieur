package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Restaurant Monsieur-José - Sistema de Gestão");
        // TelaInicial tela = new TelaInicial(primaryStage);
        // tela.mostrarTela();
        //TelaGerente tela = new TelaGerente(primaryStage);
        //tela.mostrarTela();
        //primaryStage.show();
        TelaPagamento telaPagamento = new TelaPagamento(primaryStage,"rbcorreaneto@gmail.com");
        telaPagamento.mostrarTela();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}