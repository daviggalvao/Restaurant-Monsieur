package app;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.animation.*;
import javafx.util.Duration;

public class TelaGerente {
    private Stage stage;

    /**
     * Construtor da TelaGerente.
     * @param stage O palco principal da aplicação.
     */
    public TelaGerente(Stage stage) {this.stage = stage;}

    /**
     * Abre uma nova janela maximizada com um título e um conteúdo simples.
     * Este método é chamado quando um card é clicado.
     * @param titulo O título da nova janela e parte do conteúdo exibido.
     */
    private void abrirNovaJanela(String titulo) {
        Stage novaJanela = new Stage();
        novaJanela.setTitle(titulo);

        Label label = new Label("Conteúdo da janela: " + titulo);
        label.setStyle("-fx-font-size: 20px; -fx-padding: 20px;");

        StackPane pane = new StackPane(label);
        pane.setPadding(new Insets(20));
        Scene scene = new Scene(pane, 400, 300);

        novaJanela.setMaximized(true);
        novaJanela.setScene(scene);
        novaJanela.show();
    }

    /**
     * Cria um VBox estilizado como um card.
     * @param svgPath Caminho para o arquivo SVG do ícone.
     * @param titleText Texto do título do card.
     * @param descText Texto da descrição do card.
     * @param borderColor Cor da borda do card (ex: "#E4E9F0").
     * @param textColor Cor do texto do card.
     * @return Um VBox configurado como um card.
     */

    private VBox createCard(String svgPath, String titleText, String descText, String borderColor, String textColor) {
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 30); //
        Font interfont = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"),12); //

        WebView webView = new WebView(); //
        webView.setMinSize(40, 40); //
        webView.setPrefSize(50, 50); //
        webView.setMaxSize(40, 40); //

        String svgUrl = getClass().getResource(svgPath).toExternalForm(); //
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" + //
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" + //
                "</body></html>"; //
        webView.getEngine().loadContent(html); //

        Circle circle = new Circle(40); //
        circle.setFill(Color.WHITE); // Círculo de hover permanece BRANCO
        circle.setVisible(false); //
        StackPane iconStack = new StackPane(circle, webView); //
        iconStack.setAlignment(Pos.CENTER); //

        Label title = new Label(titleText); //
        title.setTextFill(Color.web(textColor)); // Texto do card PRETO
        title.setFont(playfairFont); //
        title.setAlignment(Pos.CENTER); //

        Label desc = new Label(descText); //
        desc.setTextFill(Color.web(textColor)); // Texto do card PRETO
        desc.setWrapText(true); //
        desc.setMaxWidth(200); //
        desc.setFont(interfont); //
        desc.setAlignment(Pos.CENTER); //

        VBox vbox = new VBox(10, iconStack, title, desc); //
        vbox.setAlignment(Pos.CENTER); //
        vbox.setPadding(new Insets(40)); //
        vbox.setPrefSize(300, 250); //

        String cardBackgroundColor = "#F0F0F0"; // Fundo do card: Cinza Claro
        String normalStyle = "-fx-border-color: " + borderColor + ";" + //
                "-fx-border-radius: 10;" + //
                "-fx-border-width: 2.0;" + //
                "-fx-background-radius: 10;" + //
                "-fx-background-color: " + cardBackgroundColor + ";"; //
        vbox.setStyle(normalStyle); //

        vbox.setOnMouseEntered(e -> { //
            TranslateTransition translate = new TranslateTransition(Duration.millis(200), vbox); //
            translate.setToY(-5); //
            translate.play(); //
            vbox.setCursor(javafx.scene.Cursor.HAND); //
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), vbox); //
            scale.setToX(1.05); //
            scale.setToY(1.05); //
            scale.play(); //
            circle.setVisible(true); //
            vbox.setStyle( //
                    "-fx-border-color: " + borderColor + ";" + //
                            "-fx-border-radius: 10;" + //
                            "-fx-border-width: 2;" + //
                            "-fx-background-radius: 10;" + //
                            "-fx-background-color: " + cardBackgroundColor + ";" //
            );
        });

        vbox.setOnMouseExited(e ->{ //
            TranslateTransition translate = new TranslateTransition(Duration.millis(200), vbox); //
            translate.setToY(0); //
            translate.play(); //
            vbox.setCursor(javafx.scene.Cursor.DEFAULT); //
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), vbox); //
            scale.setToX(1); //
            scale.setToY(1); //
            scale.play(); //
            circle.setVisible(false); //
            vbox.setStyle(normalStyle); //
        });

        vbox.setOnMouseClicked(e -> { //
            abrirNovaJanela(titleText); //
        });

        return vbox; //
    }

    public void mostrarTelaGerente() { //
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62); //
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15); //
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17); //

        // --- Título Principal ---
        Label tituloPrincipal = new Label("Área do Gerente"); //
        tituloPrincipal.setFont(playfairFontTitulo); //
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;"); // Cor do título: amarelo

        Rectangle sublinhado = new Rectangle(230, 3); //
        sublinhado.setFill(Color.web("#FFC300")); // Cor do sublinhado: amarelo

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado); //
        blocoTitulo.setAlignment(Pos.CENTER); //
        VBox.setMargin(tituloPrincipal, new Insets(0, 0, 0, 0)); //
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0)); //

        // --- Cards ---
        String corBordaCard = "#E4E9F0"; //
        String corTextoCard = "black"; // Texto dos cards: PRETO

        // Nomes e SVGs dos cards conforme o arquivo fornecido
        VBox cardExtrato = createCard("/svg/bills-svgrepo-com.svg", "Extrato", "Verificar saldo do restaurante", corBordaCard, corTextoCard); //
        VBox cardPromocao = createCard("/svg/promotion-svgrepo-com.svg", "Promoções", "Promover funcionários", corBordaCard, corTextoCard); //
        VBox cardPagarFunc = createCard("/svg/payment-business-and-finance-svgrepo-com.svg", "Pagamentos", "Pagar os funcionários", corBordaCard, corTextoCard); //

        HBox linha1Cards = new HBox(20, cardExtrato, cardPromocao, cardPagarFunc); //
        linha1Cards.setAlignment(Pos.CENTER); //

        // --- Rodapé ---
        Label desc1 = new Label("© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante"); //
        desc1.setFont(interfontRodape1); //
        Label desc2 = new Label("Projetado para a excelência culinária francesa"); //
        desc2.setFont(interfontRodape2); //
        // MODIFICAÇÃO: Cor do texto do rodapé alterada para BRANCO
        String corTextoRodape = "white";
        desc1.setStyle("-fx-text-fill: " + corTextoRodape + ";"); //
        desc2.setStyle("-fx-text-fill: " + corTextoRodape + ";"); //

        VBox descricaoRodape = new VBox(5, desc1, desc2); //
        descricaoRodape.setAlignment(Pos.CENTER); //
        VBox.setMargin(descricaoRodape, new Insets(20, 0, 20, 0)); //

        // --- Layout Principal com VBox root (rodapé rolável) ---
        VBox root = new VBox(10, blocoTitulo, linha1Cards, descricaoRodape); //
        root.setAlignment(Pos.CENTER); //
        root.setPadding(new Insets(20)); //

        VBox.setVgrow(blocoTitulo, Priority.NEVER); //
        VBox.setVgrow(linha1Cards, Priority.ALWAYS); //
        VBox.setVgrow(descricaoRodape, Priority.NEVER); //

        GridPane grid = new GridPane(); //
        grid.setAlignment(Pos.CENTER); //
        grid.getColumnConstraints().add(new ColumnConstraints(1000)); //
        grid.add(root, 0, 0); //

        // Fundo da tela: vinho
        String corFundoVinho = "#660018"; //
        grid.setStyle("-fx-background-color: " + corFundoVinho + ";"); //
        root.setStyle("-fx-background-color: " + corFundoVinho + ";"); // Fundo do root também vinho

        ScrollPane scrollPane = new ScrollPane(grid); //
        scrollPane.setFitToWidth(true); //
        scrollPane.setFitToHeight(true); //
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //
        scrollPane.setStyle("-fx-background-color: " + corFundoVinho + ";"); // Fundo do ScrollPane vinho

        Scene scene = new Scene(scrollPane); //

        // Lógica de responsividade
        scene.widthProperty().addListener((obs, oldVal, newVal) -> { //
            if (newVal.doubleValue() < 1200) { //
                tituloPrincipal.setFont(Font.font(playfairFontTitulo.getFamily(), 52)); //
                sublinhado.setWidth(190); //
                double cardWidthSmall = 260; //
                double cardHeightSmall = 220; //
                cardExtrato.setPrefSize(cardWidthSmall, cardHeightSmall); //
                cardPromocao.setPrefSize(cardWidthSmall, cardHeightSmall); //
                cardPagarFunc.setPrefSize(cardWidthSmall, cardHeightSmall); //
                linha1Cards.setSpacing(15); //
            } else { //
                tituloPrincipal.setFont(playfairFontTitulo); //
                sublinhado.setWidth(230); //
                double cardWidthLarge = 300; //
                double cardHeightLarge = 250; //
                cardPromocao.setPrefSize(cardWidthLarge, cardHeightLarge); //
                cardExtrato.setPrefSize(cardWidthLarge, cardHeightLarge); //
                cardPagarFunc.setPrefSize(cardWidthLarge, cardHeightLarge); //
                linha1Cards.setSpacing(20); //
            }
        });

        stage.setTitle("Serviços"); //
        stage.setScene(scene); //
        stage.setMinWidth(800); //
        stage.setMinHeight(600); //
        stage.setMaximized(true); //
        stage.show(); //
    }
}
