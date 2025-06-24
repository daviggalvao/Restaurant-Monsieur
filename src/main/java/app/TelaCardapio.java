package app;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TelaCardapio extends Tela{

    public TelaCardapio(Stage stage) {
        super(stage);
    }

    // ... (método createPratoCard permanece o mesmo)
    private VBox createPratoCard(String nomePrato, String preco, String descricaoPrato, String borderColor, String textColor) {
        Font playfairFontPrato = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 26);
        Font interFontPreco = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);
        Font interFontDescricao = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);

        Label lblNomePrato = new Label(nomePrato);
        lblNomePrato.setFont(playfairFontPrato);
        lblNomePrato.setTextFill(Color.web(textColor));
        lblNomePrato.setWrapText(true);
        lblNomePrato.setTextAlignment(TextAlignment.CENTER);
        StackPane nomePane = new StackPane(lblNomePrato);

        Label lblDescricaoPrato = new Label(descricaoPrato);
        lblDescricaoPrato.setFont(interFontDescricao);
        lblDescricaoPrato.setTextFill(Color.web(textColor));
        lblDescricaoPrato.setWrapText(true);
        lblDescricaoPrato.setLineSpacing(2.0);
        lblDescricaoPrato.setTextAlignment(TextAlignment.CENTER);
        StackPane descricaoPane = new StackPane(lblDescricaoPrato);

        Label lblPreco = new Label(preco + " €");
        lblPreco.setFont(interFontPreco);
        lblPreco.setTextFill(Color.web("#333333"));
        lblPreco.setTextAlignment(TextAlignment.CENTER);
        StackPane precoPane = new StackPane(lblPreco);

        VBox pratoCard = new VBox(18, nomePane, descricaoPane, precoPane);
        pratoCard.setAlignment(Pos.TOP_CENTER);
        pratoCard.setPadding(new Insets(30, 25, 30, 25));
        pratoCard.setMaxWidth(740);
        pratoCard.setMinHeight(Region.USE_PREF_SIZE);

        String cardBackgroundColor = "#FFFFFF";
        String baseBorderStyle = "-fx-border-radius: 6;" +
                "-fx-border-width: 1.0;";
        String normalBorderColorStyle = "-fx-border-color: " + borderColor + ";";

        String normalStyle = "-fx-background-color: " + cardBackgroundColor + ";" +
                normalBorderColorStyle +
                baseBorderStyle +
                "-fx-background-radius: 6;";
        pratoCard.setStyle(normalStyle);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(1.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, 0.12));
        pratoCard.setEffect(dropShadow);

        pratoCard.setOnMouseEntered(e -> {
            pratoCard.setCursor(Cursor.HAND);

            TranslateTransition translate = new TranslateTransition(Duration.millis(150), pratoCard);
            translate.setToY(-4);
            translate.play();

            ScaleTransition scale = new ScaleTransition(Duration.millis(150), pratoCard);
            scale.setToX(1.008);
            scale.setToY(1.008);
            scale.play();
        });

        pratoCard.setOnMouseExited(e ->{
            pratoCard.setCursor(Cursor.DEFAULT);

            TranslateTransition translate = new TranslateTransition(Duration.millis(150), pratoCard);
            translate.setToY(0);
            translate.play();

            ScaleTransition scale = new ScaleTransition(Duration.millis(150), pratoCard);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        return pratoCard;
    }

    @Override
    public Scene criarScene() {
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 12);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 10);

        Label tituloPrincipal = new Label("Cardápio");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(270, 3);
        sublinhado.setFill(Color.web("#FFC300"));

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0));

        String corBordaCardRequintado = "#E0E0E0";
        String corTextoCard = "black";

        VBox pratosContainer = new VBox(25);
        pratosContainer.setAlignment(Pos.TOP_CENTER);
        pratosContainer.setPadding(new Insets(10, 120, 20, 120));

        pratosContainer.getChildren().addAll(
                createPratoCard("Kylian Mbappé", "95,79", "Uma explosão de sabor e velocidade no paladar.", corBordaCardRequintado, corTextoCard),
                createPratoCard("Antoine Griezmann", "88,59", "Elegância e visão de jogo em cada garfada.", corBordaCardRequintado, corTextoCard)
        );

        Label desc1 = new Label("© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfontRodape1);
        Label desc2 = new Label("Projetado para a excelência culinária francesa");
        desc2.setFont(interfontRodape2);
        String corTextoRodape = "white";
        desc1.setStyle("-fx-text-fill: " + corTextoRodape + ";");
        desc2.setStyle("-fx-text-fill: " + corTextoRodape + ";");

        VBox descricaoRodape = new VBox(5, desc1, desc2);
        descricaoRodape.setAlignment(Pos.CENTER);
        VBox.setMargin(descricaoRodape, new Insets(20, 0, 20, 0));

        VBox root = new VBox(10, blocoTitulo, pratosContainer, descricaoRodape);
        root.setAlignment(Pos.CENTER);
        VBox.setVgrow(pratosContainer, Priority.ALWAYS);

        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)";
        root.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        // --- MUDANÇAS PARA ADICIONAR O BOTÃO VOLTAR ---
        StackPane stackPane = new StackPane(scrollPane);
        stackPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        Runnable acaoVoltar = () -> new TelaServicos(super.getStage()).mostrarTela();
        BotaoVoltar.criarEPosicionar(stackPane, acaoVoltar);

        Scene scene = new Scene(stackPane);
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            // ... (lógica de responsividade existente)
        });

        return scene;
    }
}