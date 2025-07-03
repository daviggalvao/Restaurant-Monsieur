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

public class TelaGerente extends Tela {

    public TelaGerente(Stage stage) {
        super(stage);
    }

    private VBox createCard(String svgPath, String titleText, String descText, String borderColor, String textColor) {
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 30);
        Font interfont = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"),12);

        WebView webView = new WebView();
        webView.setMinSize(40, 40);
        webView.setPrefSize(50, 50);
        webView.setMaxSize(40, 40);
        webView.setMouseTransparent(true);

        String svgUrl = getClass().getResource(svgPath).toExternalForm();
        String html = "<html><body style='margin:0; overflow:hidden; background-color: transparent;'>" +
                "<img src='" + svgUrl + "' style='width:100%; height:100%;' />" +
                "</body></html>";
        webView.getEngine().loadContent(html);
        webView.setStyle("-fx-background-color: transparent;");

        Circle circle = new Circle(40);
        circle.setFill(Color.WHITE);
        circle.setVisible(false);
        StackPane iconStack = new StackPane(circle, webView);
        iconStack.setAlignment(Pos.CENTER);

        Label title = new Label(titleText);
        title.setTextFill(Color.web(textColor));
        title.setFont(playfairFont);

        Label desc = new Label(descText);
        desc.setTextFill(Color.web(textColor));
        desc.setWrapText(true);
        desc.setMaxWidth(200);
        desc.setFont(interfont);

        VBox vbox = new VBox(10, iconStack, title, desc);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(40));
        vbox.setPrefSize(300, 250);

        String normalStyle = "-fx-border-color: " + borderColor + "; -fx-border-radius: 10; -fx-background-radius: 10; -fx-background-color: #F0F0F0;";
        vbox.setStyle(normalStyle);

        vbox.setOnMouseEntered(e -> {
            vbox.setCursor(javafx.scene.Cursor.HAND);
            circle.setVisible(true);
        });

        vbox.setOnMouseExited(e ->{
            vbox.setCursor(javafx.scene.Cursor.DEFAULT);
            circle.setVisible(false);
        });

        return vbox;
    }

    @Override
    public Scene criarScene() {
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        Label tituloPrincipal = new Label(Tela.emFrances ? "Comptes/Inscriptions" : "Contas/Cadastros");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(230, 3);
        sublinhado.setFill(Color.web("#FFC300"));

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0));

        String corBordaCard = "#E4E9F0";
        String corTextoCard = "black";

        VBox cardCliente = createCard("/svg/client-profile-svgrepo-com.svg", Tela.emFrances ? "Clients" : "Clientes", Tela.emFrances ? "Gérer les comptes clients" : "Gerenciar contas dos clientes", corBordaCard, corTextoCard);
        VBox cardPromocao = createCard("/svg/promotion-svgrepo-com.svg", Tela.emFrances ? "Employés" : "Funcionários", Tela.emFrances ? "Promouvoir/embaucher des employés" : "Promover/Contratar funcionários", corBordaCard, corTextoCard);

        HBox linha1Cards = new HBox(20, cardCliente, cardPromocao);
        linha1Cards.setAlignment(Pos.CENTER);

        cardCliente.setOnMouseClicked(mouseEvent -> new TelaClientes(super.getStage()).mostrarTela());
        cardPromocao.setOnMouseClicked(mouseEvent -> new TelaFuncionarios(super.getStage()).mostrarTela());

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

        VBox root = new VBox(10, blocoTitulo, linha1Cards, descricaoRodape);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        VBox.setVgrow(linha1Cards, Priority.ALWAYS);

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

        StackPane stackPane = new StackPane(scrollPane);
        stackPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        Runnable acaoVoltar = () -> new TelaServicos(super.getStage()).mostrarTela();
        BotaoVoltar.criarEPosicionar(stackPane, acaoVoltar);

        Scene scene = new Scene(stackPane);
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
        });

        return scene;
    }
}