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

public class TelaServicos{
    private Stage stage;

    /**
     * Construtor da TelaServicos.
     * @param stage O palco principal da aplicação.
     */
    public TelaServicos(Stage stage) {this.stage = stage;}

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
     * @param textColor Cor do texto e do círculo de hover do ícone.
     * @return Um VBox configurado como um card.
     */
    private VBox createCard(String svgPath, String titleText, String descText, String borderColor, String textColor) {
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 30);
        Font interfont = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"),12);

        WebView webView = new WebView();
        webView.setMinSize(40, 40);
        webView.setPrefSize(50, 50);
        webView.setMaxSize(40, 40);

        String svgUrl = getClass().getResource(svgPath).toExternalForm();
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" +
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" +
                "</body></html>";
        webView.getEngine().loadContent(html);

        // O círculo de hover usará a textColor (que será preta)
        Circle circle = new Circle(40);
        circle.setFill(Color.TRANSPARENT);
        circle.setVisible(false);
        StackPane iconStack = new StackPane(circle, webView);
        iconStack.setAlignment(Pos.CENTER);

        // Título do card (agora sempre com textColor definida no mostrarTelaServicos)
        Label title = new Label(titleText);
        title.setTextFill(Color.web(textColor));
        title.setFont(playfairFont);
        title.setAlignment(Pos.CENTER);

        // Descrição do card (agora sempre com textColor definida no mostrarTelaServicos)
        Label desc = new Label(descText);
        desc.setTextFill(Color.web(textColor));
        desc.setWrapText(true);
        desc.setMaxWidth(200);
        desc.setFont(interfont);
        desc.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10, iconStack, title, desc);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(40));
        vbox.setPrefSize(300, 250);

        // MODIFICAÇÃO 2: Cor de fundo dos cards alterada para BRANCO
        String cardBackgroundColor = "white";
        String normalStyle = "-fx-border-color: " + borderColor + ";" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 2.0;" +
                "-fx-background-radius: 10;" +
                "-fx-background-color: " + cardBackgroundColor + ";"; // Fundo do card para BRANCO
        vbox.setStyle(normalStyle);

        vbox.setOnMouseEntered(e -> {
            TranslateTransition translate = new TranslateTransition(Duration.millis(200), vbox);
            translate.setToY(-5);
            translate.play();
            vbox.setCursor(javafx.scene.Cursor.HAND);
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), vbox);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
            circle.setVisible(true);
            // Estilo de hover também com fundo BRANCO para o card
            vbox.setStyle(
                    "-fx-border-color: " + borderColor + ";" +
                            "-fx-border-radius: 10;" +
                            "-fx-border-width: 2;" +
                            "-fx-background-radius: 10;" +
                            "-fx-background-color: " + cardBackgroundColor + ";" // Fundo do card para BRANCO no hover
            );
        });

        vbox.setOnMouseExited(e ->{
            TranslateTransition translate = new TranslateTransition(Duration.millis(200), vbox);
            translate.setToY(0);
            translate.play();
            vbox.setCursor(javafx.scene.Cursor.DEFAULT);
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), vbox);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
            circle.setVisible(false);
            vbox.setStyle(normalStyle);
        });

        vbox.setOnMouseClicked(e -> {
            abrirNovaJanela(titleText);
        });

        return vbox;
    }

    /**
     * Configura e exibe a tela de Serviços.
     */
    public void mostrarTelaServicos() {
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        Label tituloPrincipal = new Label("Serviços");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;"); // Cor do título mantida como amarelo

        Rectangle sublinhado = new Rectangle(230, 3);
        sublinhado.setFill(Color.web("#FFC300")); // Cor do sublinhado mantida como amarelo

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(tituloPrincipal, new Insets(0, 0, 0, 0));
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0));

        String corBordaCard = "#E4E9F0"; // Cor da borda dos cards mantida
        String svgPlaceholder = "/svg/calendar-time-svgrepo-com.svg";

        // MODIFICAÇÃO 1: Cor de TODOS os textos dos cards alterada para PRETO
        String corTextoCard = "black";

        // Criação dos 6 cards, todos com texto preto
        VBox cardCadastro = createCard("/svg/contacts-svgrepo-com.svg", "Cadastros", "Gerenciar Cadastros", corBordaCard, corTextoCard);
        VBox cardPedido = createCard("/svg/shopping-cart-svgrepo-com.svg", "Pedidos", "Gerenciar Pedidos", corBordaCard, corTextoCard);
        VBox cardReserva = createCard("/svg/calendar-big-svgrepo-com.svg", "Reservas", "Gerenciar Reservas", corBordaCard, corTextoCard);
        VBox cardEstoque = createCard("/svg/box-svgrepo-com.svg", "Estoque", "Gerenciar Estoque", corBordaCard, corTextoCard);
        VBox cardConta = createCard("/svg/diary-svgrepo-com.svg", "Cardápio", "Gerenciar Cardápio", corBordaCard, corTextoCard);
        VBox cardMenu = createCard("/svg/bank-svgrepo-com.svg", "Conta", "Gerenciar Conta", corBordaCard, corTextoCard);

        HBox linha1Cards = new HBox(20, cardCadastro, cardPedido, cardReserva);
        linha1Cards.setAlignment(Pos.CENTER);
        HBox linha2Cards = new HBox(20, cardEstoque, cardConta, cardMenu);
        linha2Cards.setAlignment(Pos.CENTER);
        VBox cardBoxContainer = new VBox(20, linha1Cards, linha2Cards);
        cardBoxContainer.setAlignment(Pos.CENTER);
        cardBoxContainer.setPadding(new Insets(20, 0, 0, 50));

        // Rodapé (texto branco sobre fundo vinho)
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

        VBox root = new VBox(10, blocoTitulo, cardBoxContainer, descricaoRodape);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        VBox.setVgrow(blocoTitulo, Priority.NEVER);
        VBox.setVgrow(cardBoxContainer, Priority.ALWAYS);
        VBox.setVgrow(descricaoRodape, Priority.NEVER);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.getColumnConstraints().add(new ColumnConstraints(1000));
        grid.add(root, 0, 0);

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Scene scene = new Scene(scrollPane);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < 1200) {
                tituloPrincipal.setFont(Font.font(playfairFontTitulo.getFamily(), 52));
                sublinhado.setWidth(190);
                double cardWidthSmall = 260;
                double cardHeightSmall = 220;
                cardCadastro.setPrefSize(cardWidthSmall, cardHeightSmall);
                cardPedido.setPrefSize(cardWidthSmall, cardHeightSmall);
                cardReserva.setPrefSize(cardWidthSmall, cardHeightSmall);
                cardEstoque.setPrefSize(cardWidthSmall, cardHeightSmall);
                cardConta.setPrefSize(cardWidthSmall, cardHeightSmall);
                cardMenu.setPrefSize(cardWidthSmall, cardHeightSmall);
                linha1Cards.setSpacing(15);
                linha2Cards.setSpacing(15);
                cardBoxContainer.setSpacing(15);
            } else {
                tituloPrincipal.setFont(playfairFontTitulo);
                sublinhado.setWidth(230);
                double cardWidthLarge = 300;
                double cardHeightLarge = 250;
                cardCadastro.setPrefSize(cardWidthLarge, cardHeightLarge);
                cardPedido.setPrefSize(cardWidthLarge, cardHeightLarge);
                cardReserva.setPrefSize(cardWidthLarge, cardHeightLarge);
                cardEstoque.setPrefSize(cardWidthLarge, cardHeightLarge);
                cardConta.setPrefSize(cardWidthLarge, cardHeightLarge);
                cardMenu.setPrefSize(cardWidthLarge, cardHeightLarge);
                linha1Cards.setSpacing(20);
                linha2Cards.setSpacing(20);
                cardBoxContainer.setSpacing(20);
            }
        });

        stage.setTitle("Serviços");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setMaximized(true);
        stage.show();
    }
}