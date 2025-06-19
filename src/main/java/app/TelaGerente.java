package app;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image; // Manter se usado
import javafx.scene.image.ImageView; // Manter se usado
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage; // Necessário para o construtor, mas não para o criarScene
import javafx.geometry.*;
import javafx.animation.*;
import javafx.util.Duration;

public class TelaGerente extends Tela { // 1. HERDAR DE TELA
    // Removido: private Stage stage; (já está na classe base Tela)

    /**
     * Construtor da TelaGerente.
     * @param stage O palco principal da aplicação.
     */
    public TelaGerente(Stage stage) {
        super(stage); // Chama o construtor da classe base
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

        Circle circle = new Circle(40);
        circle.setFill(Color.WHITE);
        circle.setVisible(false);
        StackPane iconStack = new StackPane(circle, webView);
        iconStack.setAlignment(Pos.CENTER);

        Label title = new Label(titleText);
        title.setTextFill(Color.web(textColor));
        title.setFont(playfairFont);
        title.setAlignment(Pos.CENTER);

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

        String cardBackgroundColor = "#F0F0F0";
        String normalStyle = "-fx-border-color: " + borderColor + ";" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 2.0;" +
                "-fx-background-radius: 10;" +
                "-fx-background-color: " + cardBackgroundColor + ";";
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
            vbox.setStyle(
                    "-fx-border-color: " + borderColor + ";" +
                            "-fx-border-radius: 10;" +
                            "-fx-border-width: 2;" +
                            "-fx-background-radius: 10;" +
                            "-fx-background-color: " + cardBackgroundColor + ";"
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

        return vbox;
    }

    @Override // 2. IMPLEMENTAR criarScene()
    public Scene criarScene() { // MUDANÇA AQUI: de void mostrarTelaGerente() para Scene criarScene()
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        // --- Título Principal ---
        Label tituloPrincipal = new Label(Tela.emFrances ? "Comptes/Inscriptions" : "Contas/Cadastros"); // Usa Tela.emFrances
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(230, 3);
        sublinhado.setFill(Color.web("#FFC300"));

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0));

        // --- Cards ---
        String corBordaCard = "#E4E9F0";
        String corTextoCard = "black";

        VBox cardCliente = createCard("/svg/client-profile-svgrepo-com.svg", Tela.emFrances ? "Clients" : "Clientes", Tela.emFrances ? "Gérer les comptes clients" : "Gerenciar contas dos clientes", corBordaCard, corTextoCard);
        VBox cardPromocao = createCard("/svg/promotion-svgrepo-com.svg", Tela.emFrances ? "Employés" : "Funcionários", Tela.emFrances ? "Promouvoir/embaucher des employés" : "Promover/Contratar funcionários", corBordaCard, corTextoCard);

        HBox linha1Cards = new HBox(20, cardCliente, cardPromocao);
        linha1Cards.setAlignment(Pos.CENTER);

        // 3. ATUALIZAR CHAMADAS DE NAVEGAÇÃO
        cardCliente.setOnMouseClicked(mouseEvent -> {
            // Assumindo que TelaClientes também será refatorada para usar mostrarTela()
            new TelaClientes(super.getStage()).mostrarTela();
        });

        cardPromocao.setOnMouseClicked(mouseEvent -> {
            // Assumindo que TelaFuncionarios também será refatorada para usar mostrarTela()
            new TelaFuncionarios(super.getStage()).mostrarTela();
        });

        // Image img = new Image(getClass().getResource("/svg/bank-svgrepo-com.svg").toExternalForm()); // Pode ser removido se não for usado
        // ImageView imageView = new ImageView(img); // Pode ser removido se não for usado

        // --- Rodapé ---
        Label desc1 = new Label(Tela.emFrances ? "© 2025 Restaurant Monsieur-José - Système de gestion de restaurant" : "© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfontRodape1);
        Label desc2 = new Label(Tela.emFrances ? "Conçu pour l'excellence culinaire française" : "Projetado para a excelência culinária francesa");
        desc2.setFont(interfontRodape2);
        String corTextoRodape = "white";
        desc1.setStyle("-fx-text-fill: " + corTextoRodape + ";");
        desc2.setStyle("-fx-text-fill: " + corTextoRodape + ";");

        VBox descricaoRodape = new VBox(5, desc1, desc2);
        descricaoRodape.setAlignment(Pos.CENTER);
        VBox.setMargin(descricaoRodape, new Insets(20, 0, 20, 0));

        // --- Layout Principal ---
        VBox root = new VBox(10, blocoTitulo, linha1Cards, descricaoRodape);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        VBox.setVgrow(blocoTitulo, Priority.NEVER);
        VBox.setVgrow(linha1Cards, Priority.ALWAYS);
        VBox.setVgrow(descricaoRodape, Priority.NEVER);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.getColumnConstraints().add(new ColumnConstraints(1000));
        grid.add(root, 0, 0);

        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)";
        grid.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        Scene scene = new Scene(scrollPane);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < 1200) {
                tituloPrincipal.setFont(Font.font(playfairFontTitulo.getFamily(), 52));
                sublinhado.setWidth(190);
                double cardWidthSmall = 260;
                double cardHeightSmall = 220;
                cardCliente.setPrefSize(cardWidthSmall, cardHeightSmall);
                cardPromocao.setPrefSize(cardWidthSmall, cardHeightSmall);
                linha1Cards.setSpacing(15);
            } else {
                tituloPrincipal.setFont(playfairFontTitulo);
                sublinhado.setWidth(230);
                double cardWidthLarge = 300;
                double cardHeightLarge = 250;
                cardPromocao.setPrefSize(cardWidthLarge, cardHeightLarge);
                cardCliente.setPrefSize(cardWidthLarge, cardHeightLarge);
                linha1Cards.setSpacing(20);
            }
        });

        // 4. REMOVIDO: stage.setTitle, stage.setMaximized, stage.setScene, stage.setMinWidth, etc.
        // super.getStage().setTitle("Contas");
        // super.getStage().setMaximized(true);
        // super.getStage().setScene(scene);
        // super.getStage().setMinWidth(800);
        // super.getStage().setMinHeight(600);
        // super.getStage().show();

        return scene; // RETORNA A SCENE
    }
}