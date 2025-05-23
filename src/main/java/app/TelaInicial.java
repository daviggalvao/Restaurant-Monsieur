package app;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TelaInicial {
    private Stage stage;

    public TelaInicial(Stage stage) {
        this.stage = stage;
    }

    private VBox createCard(String iconText, String titleText, String descText, String color,String cortexto) {
        // Ícone como Label ou pode usar ImageView
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 30);
        Font interfont = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"),12);

        Circle circle = new Circle(40);
        circle.setFill(Color.web(cortexto));
        circle.setVisible(false);

        Label icon = new Label(iconText);
        icon.setFont(Font.font(35));
        icon.setTextFill(Color.web(cortexto));
        StackPane iconStack = new StackPane(circle, icon);

        // Título
        Label title = new Label(titleText);
        title.setTextFill(Color.web(cortexto));
        title.setFont(playfairFont);

        // Descrição
        Label desc = new Label(descText);
        desc.setTextFill(Color.web(cortexto));
        desc.setWrapText(true);
        desc.setMaxWidth(200);
        desc.setFont(interfont);

        VBox vbox = new VBox(10, iconStack, title, desc);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(40));
        vbox.setPrefSize(300, 250);

        // Borda estilo card
        String normalStyle = "-fx-border-color: " + color + ";" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 2.0;" +
                "-fx-background-radius: 10;" +
                "-fx-background-color: white;";

        vbox.setStyle(normalStyle);


        // Efeito hover
        vbox.setOnMouseEntered(e -> {
            TranslateTransition translate = new TranslateTransition(Duration.millis(200), vbox);
            translate.setToY(-5);
            translate.play();

            ScaleTransition scale = new ScaleTransition(Duration.millis(200), vbox);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();

            circle.setVisible(true);
            icon.setTextFill(Color.web("white"));

            vbox.setStyle(
                    "-fx-border-color: " + color + ";" +
                            "-fx-border-radius: 10;" +
                            "-fx-border-width: 2;" +
                            "-fx-background-radius: 10;" +
                            "-fx-background-color: white;"  // ou qualquer outra cor
            );
        });

        vbox.setOnMouseExited(e ->{
            TranslateTransition translate = new TranslateTransition(Duration.millis(200), vbox);
            translate.setToY(0);
            translate.play();

            ScaleTransition scale = new ScaleTransition(Duration.millis(200), vbox);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();

            circle.setVisible(false);
            icon.setTextFill(Color.web(cortexto));

            vbox.setStyle(normalStyle);
        });

        return vbox;
    }

    public void mostrar() {
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 82);
        Font playfairFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 56);
        Font interfont1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"),30);
        Font interfont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"),20);
        Font interfont3 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"),15);
        Font interfont4 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"),12);

        Label nomeRest = new Label("Restaurant");
        nomeRest.setFont(playfairFont);
        nomeRest.setStyle("-fx-text-fill: #660018;");

        Label nomeRest2 = new Label("Monsieur-José");
        nomeRest2.setFont(playfairFont2);
        nomeRest2.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(260, 3);
        sublinhado.setFill(Color.web("#FFC300"));

        Label inforest1 = new Label("Sistema de Gestão Profissional");
        Label inforest2 = new Label("Gerencie suas reservas e entregas com elegância e simplicidade" );
        inforest1.setStyle("-fx-text-fill: black;");
        inforest1.setFont(interfont1);
        inforest2.setStyle("-fx-text-fill: black;");
        inforest2.setFont(interfont2);

        VBox infos = new VBox();
        infos.setAlignment(Pos.CENTER);
        infos.setSpacing(20);
        infos.getChildren().addAll(inforest1, inforest2);
        VBox.setMargin(infos,  new Insets(50,0,0,0));

        VBox blocoMonsieur = new VBox();
        blocoMonsieur.setAlignment(Pos.CENTER);
        blocoMonsieur.getChildren().addAll(nomeRest2, sublinhado);

        VBox vbox1 = new VBox(-15, nomeRest, blocoMonsieur,infos);
        vbox1.setAlignment(Pos.CENTER);
        vbox1.setMargin(nomeRest, new Insets(-100,0,0,0));

        VBox card1 = createCard("📅", "Reservas", "Gerenciar Pedidos de Reserva", "#E4E9F0","#660018");
        VBox card2 = createCard("🚚", "Delivery", "Gerenciar Pedidos de Delivery", "#E4E9F0","black");
        VBox card3 = createCard("\uD83D\uDDC2", "Serviços", "Gerenciar Serviços de Caixa", "#E4E9F0","#FFC300");
        HBox cardBox = new HBox(20, card1, card2, card3);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setPadding(new Insets(50));

        Label desc1 = new Label("© 2024 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfont3);
        Label desc2 = new Label("Projetado para a excelência culinária francesa");
        desc2.setFont(interfont4);
        VBox descricao = new VBox(10, desc1, desc2);
        descricao.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, vbox1, cardBox,descricao);
        root.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); // Centraliza o GridPane na cena
        grid.getColumnConstraints().add(new ColumnConstraints(1000));
        grid.add(root, 0, 0);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
