package app;

import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.animation.*;
import javafx.util.Duration;

public class TelaServicos extends TelaInicial{
    private Stage stage;

    public TelaServicos(Stage stage) {
        super();
        this.stage = stage;
    }

    public void mostrarTelaServicos() {
        VBox card1 = createCard("A", "Reservas", "Gerenciar Pedidos de Reserva", "#E4E9F0","#660018");
    }
}
