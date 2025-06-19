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

import java.util.ArrayList; // Import para ArrayList
import classes.Pedido;     // Import para a classe Pedido

public class TelaServicos extends Tela { // 1. Herdar da classe Tela

    public TelaServicos(Stage stage) {
        super(stage);
    }

    // REMOVIDO: O método abrirNovaJanela, pois não será mais usado para mudar de tela.
    /*
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
    */

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
        circle.setFill(Color.WHITE); //
        circle.setVisible(false); //
        StackPane iconStack = new StackPane(circle, webView); //
        iconStack.setAlignment(Pos.CENTER); //

        Label title = new Label(titleText); //
        title.setTextFill(Color.web(textColor)); //
        title.setFont(playfairFont); //
        title.setAlignment(Pos.CENTER); //

        Label desc = new Label(descText); //
        desc.setTextFill(Color.web(textColor)); //
        desc.setWrapText(true); //
        desc.setMaxWidth(200); //
        desc.setFont(interfont); //
        desc.setAlignment(Pos.CENTER); //

        VBox vbox = new VBox(10, iconStack, title, desc); //
        vbox.setAlignment(Pos.CENTER); //
        vbox.setPadding(new Insets(40)); //
        vbox.setPrefSize(300, 250); //

        String cardBackgroundColor = "#F0F0F0"; //
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

        // Removida a chamada a abrirNovaJanela. A navegação será feita no criarScene.
        /*
        vbox.setOnMouseClicked(e -> {
            abrirNovaJanela(titleText);
        });
        */
        return vbox; //
    }

    /**
     * Configura e retorna a cena da tela de Serviços.
     */
    @Override // Indica que este método está implementando o método abstrato da classe base
    public Scene criarScene() { // MUDANÇA AQUI: de void mostrarTelaServicos() para Scene criarScene()
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62); //
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15); //
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17); //

        // --- Título Principal ---
        Label tituloPrincipal = new Label(Tela.emFrances ? "Services" : "Serviços"); //
        tituloPrincipal.setFont(playfairFontTitulo); //
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;"); //

        Rectangle sublinhado = new Rectangle(230, 3); //
        sublinhado.setFill(Color.web("#FFC300")); //

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado); //
        blocoTitulo.setAlignment(Pos.CENTER); //
        VBox.setMargin(tituloPrincipal, new Insets(0, 0, 0, 0)); //
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0)); //

        // --- Cards ---
        String corBordaCard = "#E4E9F0"; //
        String corTextoCard = "black"; //

        VBox cardCadastro = createCard("/svg/contacts-svgrepo-com.svg", Tela.emFrances ? "Inscriptions" : "Cadastros", Tela.emFrances ? "Gérer les inscriptions" : "Gerenciar Cadastros", corBordaCard, corTextoCard); //
        VBox cardPedido = createCard("/svg/shopping-cart-svgrepo-com.svg", Tela.emFrances ? "Ordres" : "Pedidos", Tela.emFrances ? "Gérer les commandes" : "Gerenciar Pedidos", corBordaCard, corTextoCard); //
        VBox cardReserva = createCard("/svg/calendar-big-svgrepo-com.svg", Tela.emFrances ? "Réservations" : "Reservas", Tela.emFrances ? "Gérer les réservations" : "Gerenciar Reservas", corBordaCard, corTextoCard); //
        VBox cardEstoque = createCard("/svg/box-svgrepo-com.svg", Tela.emFrances ? "Action" : "Estoque", Tela.emFrances ? "Gérer l'inventaire" : "Gerenciar Estoque", corBordaCard, corTextoCard); //
        VBox cardMenu = createCard("/svg/diary-svgrepo-com.svg", Tela.emFrances ? "Menu" : "Cardápio", Tela.emFrances ? "Gérer le menu" : "Gerenciar Cardápio", corBordaCard, corTextoCard); //
        VBox cardConta = createCard("/svg/bank-svgrepo-com.svg", Tela.emFrances ? "Compte" : "Conta", Tela.emFrances ? "Gérer le compte" : "Gerenciar Conta", corBordaCard, corTextoCard); //

        HBox linha1Cards = new HBox(20, cardCadastro, cardPedido, cardReserva); //
        linha1Cards.setAlignment(Pos.CENTER); //
        HBox linha2Cards = new HBox(20, cardEstoque, cardConta, cardMenu); //
        linha2Cards.setAlignment(Pos.CENTER); //
        VBox cardBoxContainer = new VBox(20, linha1Cards, linha2Cards); //
        cardBoxContainer.setAlignment(Pos.CENTER); //
        cardBoxContainer.setPadding(new Insets(20, 0, 0, 50)); //

        cardCadastro.setOnMouseClicked(e->{ //
            new TelaGerente(super.getStage()).mostrarTela(); //
        });

        cardConta.setOnMouseClicked(e->{ //
            new TelaConta(super.getStage()).mostrarTela(); //
        });

        cardReserva.setOnMouseClicked(mouseEvent -> { //
            // Chamada para a nova TelaGerenciarReserva
            new TelaGerenciarReserva(super.getStage()).mostrarTela(); //
        });

        cardEstoque.setOnMouseClicked(mouseEvent -> { //
            new TelaEstoque(super.getStage()).mostrarTela(); //
        });

        // *** ADICIONANDO NAVEGABILIDADE PARA TelaGerenciarDelivery ***
        cardPedido.setOnMouseClicked(mouseEvent->{ //
            // O construtor de TelaGerenciarDelivery agora aceita apenas a Stage.
            // A lista de pedidos será gerada internamente ou carregada pela própria tela.
            new TelaGerenciarDelivery(super.getStage()).mostrarTela(); //
        });

        cardMenu.setOnMouseClicked(mouseEvent -> { //
            new TelaCardapio(super.getStage()).mostrarTela(); //
        });

        // --- Rodapé ---
        Label desc1 = new Label(Tela.emFrances ? "© 2025 Restaurant Monsieur-José - Système de gestion de restaurant" : "© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante"); //
        desc1.setFont(interfontRodape1); //
        Label desc2 = new Label(Tela.emFrances ? "Projetado para a excelência culinária francesa" : "Projetado para a excelência culinária francesa"); //
        desc2.setFont(interfontRodape2); //
        String corTextoRodape = "white"; //
        desc1.setStyle("-fx-text-fill: " + corTextoRodape + ";"); //
        desc2.setStyle("-fx-text-fill: " + corTextoRodape + ";"); //

        VBox descricaoRodape = new VBox(5, desc1, desc2); //
        descricaoRodape.setAlignment(Pos.CENTER); //
        VBox.setMargin(descricaoRodape, new Insets(20, 0, 20, 0)); //

        // --- Layout Principal com VBox root (rodapé rolável) ---
        VBox root = new VBox(10, blocoTitulo, cardBoxContainer, descricaoRodape); //
        root.setAlignment(Pos.CENTER); //
        root.setPadding(new Insets(20)); //

        VBox.setVgrow(blocoTitulo, Priority.NEVER); //
        VBox.setVgrow(cardBoxContainer, Priority.ALWAYS); //
        VBox.setVgrow(descricaoRodape, Priority.NEVER); //

        GridPane grid = new GridPane(); //
        grid.setAlignment(Pos.CENTER); //
        grid.getColumnConstraints().add(new ColumnConstraints(1000)); //
        grid.add(root, 0, 0); //

        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)"; //
        grid.setStyle("-fx-background-color: " + estiloFundoVinho + ";"); //

        ScrollPane scrollPane = new ScrollPane(grid); //
        scrollPane.setFitToWidth(true); //
        scrollPane.setFitToHeight(true); //
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //
        scrollPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";"); //

        Scene scene = new Scene(scrollPane); //

        // Lógica de responsividade
        scene.widthProperty().addListener((obs, oldVal, newVal) -> { //
            if (newVal.doubleValue() < 1200) { //
                tituloPrincipal.setFont(Font.font(playfairFontTitulo.getFamily(), 52)); //
                sublinhado.setWidth(190); //
                double cardWidthSmall = 260; //
                double cardHeightSmall = 220; //
                cardCadastro.setPrefSize(cardWidthSmall, cardHeightSmall); //
                cardPedido.setPrefSize(cardWidthSmall, cardHeightSmall); //
                cardReserva.setPrefSize(cardWidthSmall, cardHeightSmall); //
                cardEstoque.setPrefSize(cardWidthSmall, cardHeightSmall); //
                cardConta.setPrefSize(cardWidthSmall, cardHeightSmall); //
                cardMenu.setPrefSize(cardWidthSmall, cardHeightSmall); //
                linha1Cards.setSpacing(15); //
                linha2Cards.setSpacing(15); //
                cardBoxContainer.setSpacing(15); //
            } else { //
                tituloPrincipal.setFont(playfairFontTitulo); //
                sublinhado.setWidth(230); //
                double cardWidthLarge = 300; //
                double cardHeightLarge = 250; //
                cardCadastro.setPrefSize(cardWidthLarge, cardHeightLarge); //
                cardPedido.setPrefSize(cardWidthLarge, cardHeightLarge); //
                cardReserva.setPrefSize(cardWidthLarge, cardHeightLarge); //
                cardEstoque.setPrefSize(cardWidthLarge, cardHeightLarge); //
                cardConta.setPrefSize(cardWidthLarge, cardHeightLarge); //
                cardMenu.setPrefSize(cardWidthLarge, cardHeightLarge); //
                linha1Cards.setSpacing(20); //
                linha2Cards.setSpacing(20); //
                cardBoxContainer.setSpacing(20); //
            }
        });

        // Removidos os comandos de Stage.setScene, Stage.setTitle, Stage.setMinWidth, etc.
        // Eles já estão sendo chamados pelo método mostrarTela() da classe base Tela.
        /*
        super.getStage().setTitle("Serviços");
        super.getStage().setScene(scene);
        super.getStage().setMinWidth(800);
        super.getStage().setMinHeight(600);
        super.getStage().setMaximized(true);
        super.getStage().show();
        */

        return scene; // Retorna a Scene construída
    }

    private void mostrarAlerta(Alert.AlertType type, String title, String content) { //
        Alert alert = new Alert(type); //
        alert.setTitle(title); //
        alert.setHeaderText(null); //
        alert.setContentText(content); //
        alert.showAndWait(); //
    }
}