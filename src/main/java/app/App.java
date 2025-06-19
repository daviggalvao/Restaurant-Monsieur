package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Restaurant Monsieur-José - Sistema de Gestão"); // Define o título uma vez

        // Cria a primeira tela (TelaInicial) e a exibe na Stage principal
        // Nenhuma outra tela é instanciada aqui
        TelaInicial tela = new TelaInicial(primaryStage);
        tela.mostrarTela(); // Chama o método mostrarTela() da base Tela
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}