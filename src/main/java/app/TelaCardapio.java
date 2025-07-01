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

public class TelaCardapio extends Tela {

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
        String normalStyle = "-fx-background-color: " + cardBackgroundColor + ";" +
                "-fx-border-color: " + borderColor + ";" +
                "-fx-border-radius: 6;" +
                "-fx-border-width: 1.0;" +
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

        Label tituloPrincipal = new Label(Tela.emFrances ? "Menu" : "Cardápio");
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
                createPratoCard("Kylian Mbappé", "95,79",
                        (Tela.emFrances) ? "Une explosion de saveur et de vitesse en bouche, une finition impeccable. Accompagné de purée de pommes de terre truffée." : "Uma explosão de sabor e velocidade no paladar, finalização impecável. Acompanha purê de batatas trufado.",
                        corBordaCardRequintado, corTextoCard),

                createPratoCard("Antoine Griezmann", "88,59",
                        (Tela.emFrances) ? "Élégance et vision de jeu à chaque bouchée. Un plat polyvalent avec sauce hollandaise et asperges fraîches." : "Elegância e visão de jogo em cada garfada. Um prato versátil com molho hollandaise e aspargos frescos.",
                        corBordaCardRequintado, corTextoCard),

                createPratoCard("Aurélien Tchouaméni", "82,99",
                        (Tela.emFrances) ? "Solidité et force qui protègent le milieu de terrain de votre appétit. Médaillon de filet mignon à la sauce au porto." : "Solidez e força que protegem o meio-campo do seu apetite. Medalhão de filé mignon ao molho de vinho do Porto.",
                        corBordaCardRequintado, corTextoCard),

                createPratoCard("Thierry Henry", "99,99",
                        (Tela.emFrances) ? "Un classique intemporel avec une touche de génie. Homard grillé au beurre d'herbes fines." : "Um clássico atemporal com um toque de genialidade. Lagosta grelhada com manteiga de ervas finas.",
                        corBordaCardRequintado, corTextoCard),

                createPratoCard("Ousmane Dembélé", "78,79",
                        (Tela.emFrances) ? "Des notes déconcertantes d'agrumes et de saveurs épicées. Ceviche de bar aux fruits de la passion et au piment." : "Dribles desconcertantes de sabores cítricos e picantes. Ceviche de robalo com maracujá e pimenta dedo-de-moça.",
                        corBordaCardRequintado, corTextoCard),

                createPratoCard("Dimitri Payet", "85,49",
                        (Tela.emFrances) ? "Précision et créativité dans un plat qui enchante. Risotto au safran, Saint-Jacques sautées et une touche de magie." : "Precisão e criatividade em um prato que encanta. Risoto de açafrão com vieiras salteadas e um toque de magia.",
                        corBordaCardRequintado, corTextoCard)
        );

        Label desc1 = new Label(Tela.emFrances ? "© 2025 Restaurant Monsieur-José - Système de gestion de restaurant" : "© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfontRodape1);
        Label desc2 = new Label(Tela.emFrances ? "Conçu pour l'excellence culinaire française" : "Projetado para a excelência culinária francesa");
        desc2.setFont(interfontRodape2);
        desc1.setStyle("-fx-text-fill: white;");
        desc2.setStyle("-fx-text-fill: white;");

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

        // --- INÍCIO DA INTEGRAÇÃO ---

        StackPane layoutFinal = new StackPane();
        layoutFinal.getChildren().add(scrollPane);

        // --- CORREÇÃO AQUI ---
        // Define o fundo do StackPane raiz para evitar o retângulo branco.
        layoutFinal.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        Runnable acaoVoltar = () -> new TelaServicos(super.getStage()).mostrarTela();

        BotaoVoltar.criarEPosicionar(layoutFinal, acaoVoltar);

        Scene scene = new Scene(layoutFinal);

        // --- FIM DA INTEGRAÇÃO ---

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

        return scene;
    }
}