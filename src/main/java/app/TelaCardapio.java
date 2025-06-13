package app;

import javafx.animation.ScaleTransition;      // Import ADICIONADO
import javafx.animation.TranslateTransition;  // Import ADICIONADO
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;                  // Import ADICIONADO (ou usar nome qualificado)
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
import javafx.util.Duration;                 // Import ADICIONADO

public class TelaCardapio extends Tela{

    public TelaCardapio(Stage stage) {
        super(stage);
    }

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
        // String hoverBorderColorStyle = "-fx-border-color: #FFC300;"; // Não é mais usado para este hover

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
        pratoCard.setEffect(dropShadow); // Sombra base aplicada

        // DropShadow hoverShadow = new DropShadow(); // Não é mais usado para este hover
        // ... definições de hoverShadow removidas

        // MODIFICAÇÃO DO EFEITO DE HOVER
        pratoCard.setOnMouseEntered(e -> {
            pratoCard.setCursor(Cursor.HAND); // Mantém o cursor de mão

            // Efeito de translação sutil para cima
            TranslateTransition translate = new TranslateTransition(Duration.millis(150), pratoCard);
            translate.setToY(-4); // Ajuste o valor para mais ou menos sutil
            translate.play();

            // Efeito de escala sutil
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), pratoCard);
            scale.setToX(1.008); // Aumento de 0.8%
            scale.setToY(1.008);
            scale.play();

            // As linhas abaixo que mudavam a sombra e o estilo da borda foram removidas:
            // pratoCard.setEffect(hoverShadow);
            // pratoCard.setStyle(hoverStyle);
        });

        pratoCard.setOnMouseExited(e ->{
            pratoCard.setCursor(Cursor.DEFAULT); // Restaura o cursor padrão

            // Reverte a translação
            TranslateTransition translate = new TranslateTransition(Duration.millis(150), pratoCard);
            translate.setToY(0);
            translate.play();

            // Reverte a escala
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), pratoCard);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            // As linhas abaixo que restauravam a sombra e o estilo da borda foram removidas,
            // pois a sombra base não foi alterada e o estilo base também não.
            // pratoCard.setEffect(dropShadow);
            // pratoCard.setStyle(normalStyle);
        });

        return pratoCard;
    }

    @Override
    public void mostrarTela() {
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

        VBox pratoMbappe = createPratoCard("Kylian Mbappé", "95,79",
                "Uma explosão de sabor e velocidade no paladar, finalização impecável. Acompanha purê de batatas trufado.",
                corBordaCardRequintado, corTextoCard);

        VBox pratoGriezmann = createPratoCard("Antoine Griezmann", "88,59",
                "Elegância e visão de jogo em cada garfada. Um prato versátil com molho hollandaise e aspargos frescos.",
                corBordaCardRequintado, corTextoCard);

        VBox pratoTchouameni = createPratoCard("Aurélien Tchouaméni", "82,99",
                "Solidez e força que protegem o meio-campo do seu apetite. Medalhão de filé mignon ao molho de vinho do Porto.",
                corBordaCardRequintado, corTextoCard);

        VBox pratoHenry = createPratoCard("Thierry Henry", "99,99",
                "Um clássico atemporal com um toque de genialidade. Lagosta grelhada com manteiga de ervas finas.",
                corBordaCardRequintado, corTextoCard);

        VBox pratoDembele = createPratoCard("Ousmane Dembélé", "78,79",
                "Dribles desconcertantes de sabores cítricos e picantes. Ceviche de robalo com maracujá e pimenta dedo-de-moça.",
                corBordaCardRequintado, corTextoCard);

        VBox pratoPayet = createPratoCard("Dimitri Payet", "85,49",
                "Precisão e criatividade em um prato que encanta. Risoto de açafrão com vieiras salteadas e um toque de magia.",
                corBordaCardRequintado, corTextoCard);

        pratosContainer.getChildren().addAll(pratoMbappe, pratoGriezmann, pratoTchouameni, pratoHenry, pratoDembele, pratoPayet);

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
        scrollPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";"); // Mantém o fundo do ScrollPane

        Scene scene = new Scene(scrollPane);
        // Se a barra branca persistir, as soluções anteriores como scene.setFill(null);
        // e zerar padding/border/insets do scrollPane podem precisar ser reintroduzidas aqui.

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double paddingLateralBase = 120;
            if (newVal.doubleValue() < 900) {
                tituloPrincipal.setFont(Font.font(playfairFontTitulo.getFamily(), 42));
                sublinhado.setWidth(180);
                double novoPadding = Math.max(30, paddingLateralBase - (900 - newVal.doubleValue()) / 5);
                pratosContainer.setPadding(new Insets(10, novoPadding, 10, novoPadding));
            } else if (newVal.doubleValue() < 1200) {
                tituloPrincipal.setFont(Font.font(playfairFontTitulo.getFamily(), 52));
                sublinhado.setWidth(230);
                double novoPadding = Math.max(70, paddingLateralBase - (1200 - newVal.doubleValue()) / 6);
                pratosContainer.setPadding(new Insets(10, novoPadding, 10, novoPadding));
            } else {
                tituloPrincipal.setFont(playfairFontTitulo);
                sublinhado.setWidth(270);
                pratosContainer.setPadding(new Insets(10, paddingLateralBase, 20, paddingLateralBase));
            }
        });

        super.getStage().setTitle("Cardápio - Restaurant Monsieur-José");
        super.getStage().setScene(scene);
        super.getStage().setMinWidth(700);
        super.getStage().setMinHeight(600);
        super.getStage().setMaximized(true);
        super.getStage().show();
    }
}