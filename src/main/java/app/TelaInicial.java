package app;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.text.html.ImageView;
import java.awt.*;

public class TelaInicial {
    private Stage stage;

    public TelaInicial(Stage stage) {
        this.stage = stage;
    }

    protected VBox createCard(String svgPath, String titleText, String descText, String color,String cortexto) {
        // Ícone como Label ou pode usar ImageView
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 30);
        Font interfont = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"),12);

        WebView webView = new WebView();
        webView.setPrefSize(40, 40); // Tamanho do ícone

        // Carrega o SVG diretamente do arquivo
        String svgContent = getClass().getResource(svgPath).toExternalForm();
        webView.getEngine().load(svgContent);

        // 2. Ajuste do StackPane (remova o SVGPath antigo)
        Circle circle = new Circle(40);
        circle.setFill(Color.web(cortexto));
        circle.setVisible(false);
        StackPane iconStack = new StackPane(circle, webView);

        // Título
        Label title = new Label(titleText);
        title.setTextFill(Color.web(cortexto));
        title.setFont(playfairFont);
        title.setAlignment(Pos.CENTER);

        // Descrição
        Label desc = new Label(descText);
        desc.setTextFill(Color.web(cortexto));
        desc.setWrapText(true);
        desc.setMaxWidth(200);
        desc.setFont(interfont);
        desc.setAlignment(Pos.CENTER);

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
            svgIcon.setFill(Color.web(cortexto));

            vbox.setStyle(normalStyle);
        });

        return vbox;
    }

    public void mostrar() {
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62);
        Font playfairFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 46);
        Font interfont1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 24);
        Font interfont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 16);
        Font interfont3 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 12);
        Font interfont4 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 10);

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

        VBox infos = new VBox(5);
        infos.setAlignment(Pos.CENTER);
        infos.getChildren().addAll(inforest1, inforest2);
        VBox.setMargin(infos, new Insets(15, 0, 10, 0));


        VBox blocoMonsieur = new VBox(5);
        blocoMonsieur.setAlignment(Pos.CENTER);
        blocoMonsieur.getChildren().addAll(nomeRest2, sublinhado);

        VBox vbox1 = new VBox(0, nomeRest, blocoMonsieur,infos);
        vbox1.setAlignment(Pos.CENTER);
        VBox.setMargin(nomeRest, new Insets(0, 0, -10, 0)); // Ajuste fino para aproximar "Restaurant" e "Monsieur-José"
        VBox.setMargin(blocoMonsieur, new Insets(-10, 0, 0, 0)); // A

        String svgContent =
                "M36 27a4 4 0 0 1-4 4h-8V5h8a4 4 0 0 1 4 4v18z " +  // Path vermelho
                "M4 5a4 4 0 0 0-4 4v18a4 4 0 0 0 4 4h8V5H4z " +     // Path azul
                "M12 5h12v26H12z";
        VBox card1 = createCard(svgContent, "Reservas", "Gerenciar Pedidos de Reserva", "#E4E9F0","#660018");
        VBox card2 = createCard("svg/flag-for-flag-france-svgrepo-com.svg", "Delivery", "Gerenciar Pedidos de Delivery", "#E4E9F0","black");
        VBox card3 = createCard("svg/flag-for-flag-france-svgrepo-com.svg", "Serviços", "Gerenciar Serviços de Caixa", "#E4E9F0","#FFC300");
        HBox cardBox = new HBox(20, card1, card2, card3);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setPadding(new Insets(20,50,50,50));

        Label desc1 = new Label("© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfont3);
        Label desc2 = new Label("Projetado para a excelência culinária francesa");
        desc2.setFont(interfont4);
        VBox descricao = new VBox(5, desc1, desc2);
        descricao.setAlignment(Pos.CENTER);
        VBox.setMargin(descricao, new Insets(20, 0, 20, 0));

        VBox root = new VBox(10, vbox1, cardBox,descricao);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        VBox.setVgrow(vbox1, Priority.ALWAYS);
        VBox.setVgrow(cardBox, Priority.ALWAYS);
        VBox.setVgrow(descricao, Priority.ALWAYS);


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); // Centraliza o GridPane na cena
        grid.getColumnConstraints().add(new ColumnConstraints(1000));
        grid.add(root, 0, 0);   
        grid.setBackground(new Background(new BackgroundFill(Color.web("white"), new CornerRadii(5), null)));

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        Scene scene = new Scene(scrollPane);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal.doubleValue() < 1000) {
                        nomeRest.setFont(Font.font(playfairFont.getFamily(), 52));
                        nomeRest2.setFont(Font.font(playfairFont2.getFamily(), 36));
                        inforest1.setFont(Font.font(interfont1.getFamily(), 18));
                        inforest2.setFont(Font.font(interfont2.getFamily(), 14));

                        card1.setPrefSize(250, 200);
                        card2.setPrefSize(250, 200);
                        card3.setPrefSize(250, 200);
                        cardBox.setSpacing(10);

                    } else {
                        nomeRest.setFont(playfairFont);
                        nomeRest2.setFont(playfairFont2);
                        inforest1.setFont(interfont1);
                        inforest2.setFont(interfont2);

                        card1.setPrefSize(300, 250);
                        card2.setPrefSize(300, 250);
                        card3.setPrefSize(300, 250);
                        cardBox.setSpacing(20);
                    }
        });

            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.setMaximized(true);
            stage.show();
    }
}
