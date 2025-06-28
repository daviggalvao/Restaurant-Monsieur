package app;

import classes.OrigemDaTela;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Restaurant Monsieur-José - Sistema de Gestão");
        //TelaGerente tela = new TelaGerente(primaryStage);
        //tela.mostrarTela();
        //primaryStage.show();
        TelaInicial tela = new TelaInicial(primaryStage);
        tela.mostrarTela();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}