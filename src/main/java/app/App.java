package app;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application{

    @Override
    public void start(Stage primaryStage) {
        TelaInicial tela = new TelaInicial(primaryStage);
        TelaServicos telaa = new TelaServicos(primaryStage);
        TelaReserva telaaa = new TelaReserva((primaryStage));
        telaaa.mostrarReserva();
        //telaa.mostrarTelaServicos();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
