package app;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TelaInicial {
    private Stage stage;
    private boolean emFrances = false;
    private WebView webView;
    private String txtNomeRest = "Restaurant";
    private String txtNomeRest2 = "Monsieur-José";
    private String txtInfo1 = "Sistema de Gestão Profissional";
    private String txtInfo2 = "Gerencie suas reservas e entregas com elegância e simplicidade";
    private String txtDesc1 = "© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante";
    private String txtDesc2 = "Projetado para a excelência culinária francesa";

    private String txtCard1Title = "Reservas";
    private String txtCard1Desc = "Gerenciar Pedidos de Reserva";
    private String txtCard2Title = "Delivery";
    private String txtCard2Desc = "Gerenciar Pedidos de Delivery";

    private Label nomeRest, nomeRest2, inforest1, inforest2, desc1, desc2;
    private VBox card1, card2, parent;

    public TelaInicial(Stage stage) {
        this.stage = stage;
    }

    private void traduzirParaFrances(Button botaoTraduzir){
        if (!emFrances) {
            // Traduz para francês
            txtNomeRest = "Restaurant";
            txtNomeRest2 = "Monsieur-José";
            txtInfo1 = "Système de gestion professionnelle";
            txtInfo2 = "Gérez vos réservations et livraisons avec élégance et simplicité";
            txtDesc1 = "© 2025 Restaurant Monsieur-José - Système de gestion de restaurant";
            txtDesc2 = "Conçu pour l'excellence culinaire française";

            txtCard1Title = "Réservations";
            txtCard1Desc = "Gérer les demandes de réservation";
            txtCard2Title = "Livraison";
            txtCard2Desc = "Gérer les demandes de livraison";

            emFrances = true;

            WebView brasil = new WebView();
            brasil.setPrefSize(15,15);
            brasil.setMouseTransparent(true);

            String svgFrance = "/svg/flag-for-flag-brazil-svgrepo-com.svg";
            String svgUrl = getClass().getResource(svgFrance).toExternalForm();
            String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" +
                    "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" +
                    "</body></html>";

            brasil.getEngine().loadContent(html);
            botaoTraduzir.setGraphic(brasil);
        } else {
            // Traduz para português
            txtNomeRest = "Restaurant";
            txtNomeRest2 = "Monsieur-José";
            txtInfo1 = "Sistema de Gestão Profissional";
            txtInfo2 = "Gerencie suas reservas e entregas com elegância e simplicidade";
            txtDesc1 = "© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante";
            txtDesc2 = "Projetado para a excelência culinária francesa";

            txtCard1Title = "Reservas";
            txtCard1Desc = "Gerenciar Pedidos de Reserva";
            txtCard2Title = "Delivery";
            txtCard2Desc = "Gerenciar Pedidos de Delivery";

            emFrances = false;

            WebView france = new WebView();
            france.setPrefSize(15,15);
            france.setMouseTransparent(true);

            String svgFrance = "/svg/flag-for-flag-france-svgrepo-com.svg";
            String svgUrl = getClass().getResource(svgFrance).toExternalForm();
            String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" +
                    "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" +
                    "</body></html>";

            france.getEngine().loadContent(html);
            botaoTraduzir.setGraphic(france);
        }
        nomeRest.setText(txtNomeRest);
        nomeRest2.setText(txtNomeRest2);
        inforest1.setText(txtInfo1);
        inforest2.setText(txtInfo2);
        desc1.setText(txtDesc1);
        desc2.setText(txtDesc2);

        atualizarCard(card1, txtCard1Title, txtCard1Desc);
        atualizarCard(card2, txtCard2Title, txtCard2Desc);
    }

    private void atualizarCard(VBox card, String novoTitulo, String novaDescricao) {
        Label titulo = (Label) card.getChildren().get(1);
        Label descricao = (Label) card.getChildren().get(2);
        titulo.setText(novoTitulo);
        descricao.setText(novaDescricao);
    }

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

    protected VBox createCard(String svgPath, String titleText, String descText, String color, String cortexto) {
        // Ícone como Label ou pode usar ImageView
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 30);
        Font interfont = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"),12);


        // Carrega o SVG diretamente do arquivo
        WebView webView = new WebView();
        webView.setMinSize(40, 40);  // Tamanho mínimo
        webView.setPrefSize(50, 50); // Tamanho preferencial
        webView.setMaxSize(40, 40);

        // Tamanho máximo
        String svgUrl = getClass().getResource(svgPath).toExternalForm();
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" +
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" +
                "</body></html>";
        webView.getEngine().loadContent(html);


        // efeito hover
        Circle circle = new Circle(40);
        circle.setFill(Color.WHITE);
        circle.setVisible(false);
        StackPane iconStack = new StackPane(circle, webView);
        iconStack.setAlignment(Pos.CENTER);

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
                "-fx-background-color: #F0F0F0;";

        vbox.setStyle(normalStyle);


        // Efeito hover
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
            circle.setFill(Color.WHITE);

            vbox.setStyle(
                    "-fx-border-color: " + color + ";" +
                            "-fx-border-radius: 10;" +
                            "-fx-border-width: 2;" +
                            "-fx-background-radius: 10;" +
                            "-fx-background-color: #F0F0F0;"  // ou qualquer outra cor
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


        // Evento de clique para abrir nova janela
        vbox.setOnMouseClicked(e -> {
            abrirNovaJanela(titleText);  // chama método que cria e mostra a nova janela
        });


        return vbox;
    }

    public void mostrar() {
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62);
        Font playfairFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 46);
        Font playfairFont3 = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 24);
        Font interfont1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 24);
        Font interfont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 16);
        Font interfont3 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 12);
        Font interfont4 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 10);

        nomeRest = new Label(txtNomeRest);
        nomeRest.setFont(playfairFont);
        nomeRest.setStyle("-fx-text-fill: #FFC300;");

        nomeRest2 = new Label(txtNomeRest2);
        nomeRest2.setFont(playfairFont2);
        nomeRest2.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(260, 3);
        sublinhado.setFill(Color.web("#FFC300"));

        inforest1 = new Label(txtInfo1);
        inforest2 = new Label(txtInfo2);
        inforest1.setStyle("-fx-text-fill: #FFC300;");
        inforest1.setFont(interfont1);
        inforest2.setStyle("-fx-text-fill: #FFC300;");
        inforest2.setFont(interfont2);

        WebView france = new WebView();
        france.setPrefSize(15,15);
        france.setMouseTransparent(true);

        String svgFrance = "/svg/flag-for-flag-france-svgrepo-com.svg";
        String svgUrl = getClass().getResource(svgFrance).toExternalForm();
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" +
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" +
                "</body></html>";

        france.getEngine().loadContent(html);
        Button botaoTraduzir = new Button();
        botaoTraduzir.setGraphic(france);
        botaoTraduzir.setOnAction(e -> traduzirParaFrances(botaoTraduzir));

        WebView gerente = new WebView();
        gerente.setPrefSize(15,15);
        gerente.setMouseTransparent(true);

        String svgGerente = "/svg/manager-avatar-svgrepo-com.svg";
        String svgUrl2 = getClass().getResource(svgGerente).toExternalForm();
        String html2 = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" +
                "<img src='" + svgUrl2 + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" +
                "</body></html>";

        gerente.getEngine().loadContent(html2);
        Button botaoGerente = new Button();
        botaoGerente.setGraphic(gerente);
        botaoGerente.setOnMouseClicked(e->{
            VBox vbox = new VBox(5);
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(40));
            vbox.setPrefSize(300, 250);

            vbox.setBackground(new Background(new BackgroundFill(Color.web("#F0F0F0"), CornerRadii.EMPTY, Insets.EMPTY)));
            TextField senha = new TextField();
            senha.setPromptText("Senha do gerente");
            String msg = "Senha do Gerente: ";
            Label label = new Label(msg);
            label.setFont(playfairFont3);
            label.setTextFill(Color.web("#30000C"));
            label.setWrapText(true);

            String msg2 = "Senha incorreta!";
            Label error = new Label(msg2);
            error.setFont(interfont2);
            error.setTextFill(Color.web("#30000C"));
            error.setVisible(false);

            Rectangle under = new Rectangle(150, 2);
            under.setFill(Color.web("#30000C"));

            Button confirm = new Button("Confirmar");
            VBox.setMargin(under, new Insets(0, 0, 23, 0));
            VBox.setMargin(senha, new Insets(0, 0, 15, 0));

            vbox.getChildren().addAll(label,under,senha,confirm,error);
            Scene scene = new Scene(vbox, 300, 250);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Gerente");
            stage.show();

            senha.setOnMouseClicked(event->{error.setVisible(false);});

            confirm.setOnAction(event -> {
                String password = senha.getText();
                if (password.equals("PSG5-0")){
                    stage.close();
                    new TelaServicos(new Stage()).mostrarTelaServicos();}
                else{error.setVisible(true);}});
        });

        VBox infos = new VBox(5);
        infos.setAlignment(Pos.CENTER);
        infos.getChildren().addAll(inforest1, inforest2,botaoTraduzir,botaoGerente);

        VBox.setMargin(infos, new Insets(15, 0, 10, 0));
        infos.setMargin(botaoTraduzir, new Insets(-55, 600, 0, 0));
        infos.setMargin(botaoGerente, new Insets(-30, 0, 0, 575));

        VBox blocoMonsieur = new VBox(5);
        blocoMonsieur.setAlignment(Pos.CENTER);
        blocoMonsieur.getChildren().addAll(nomeRest2, sublinhado);

        VBox vbox1 = new VBox(0, nomeRest, blocoMonsieur,infos);
        vbox1.setAlignment(Pos.CENTER);
        VBox.setMargin(nomeRest, new Insets(0, 0, -10, 0)); // Ajuste fino para aproximar "Restaurant" e "Monsieur-José"
        VBox.setMargin(blocoMonsieur, new Insets(-10, 0, 0, 0)); // A

         card1 = createCard( "/svg/calendar-time-svgrepo-com.svg", txtCard1Title, txtCard1Desc, "#F0F0F0","#000000");
         card2 = createCard("/svg/delivery-svgrepo-com.svg", txtCard2Title,txtCard2Desc , "#F0F0F0","#000000");

        card1.setOnMouseClicked(mouseEvent->{ new TelaReserva(new Stage()).mostrarReserva();});
        card2.setOnMouseClicked(mouseEvent->{ new TelaCardapio(new Stage()).mostrarTelaCardapio();});

        HBox cardBox = new HBox(20, card1, card2);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setPadding(new Insets(35,0,50,0));

        desc1 = new Label(txtDesc1);
        desc1.setFont(interfont3);
        desc1.setTextFill(Color.web("white"));
        desc2 =  new Label(txtDesc2);
        desc2.setFont(interfont4);
        desc2.setTextFill(Color.web("white"));
        VBox descricao = new VBox(5, desc1, desc2);
        descricao.setAlignment(Pos.CENTER);

        VBox.setMargin(descricao, new Insets(0, 0, 20, 0));

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
        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)";
        grid.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        VBox conteudoScroll = new VBox(20, grid);
        conteudoScroll.setAlignment(Pos.CENTER);
        conteudoScroll.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        ScrollPane scrollPane = new ScrollPane(conteudoScroll);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setStyle(
                "-fx-background-color: transparent;"      +  // remove o fundo branco da própria ScrollPane
                        "-fx-background: transparent;"            +  // garante que o fundo seja transparente
                        "-fx-border-color: transparent;"          +  // remove qualquer borda externa que viesse por padrão
                        "-fx-focus-color: transparent;"           +  // impede que apareça o contorno de seleção azul
                        "-fx-faint-focus-color: transparent;"
        );

        Scene scene = new Scene(scrollPane);
        scene.setFill(Color.web("#30000C"));

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < 1000) {
                nomeRest.setFont(Font.font(playfairFont.getFamily(), 52));
                nomeRest2.setFont(Font.font(playfairFont2.getFamily(), 36));
                inforest1.setFont(Font.font(interfont1.getFamily(), 22));
                inforest2.setFont(Font.font(interfont2.getFamily(), 18));

                card1.setPrefSize(250, 200);
                card2.setPrefSize(250, 200);
                cardBox.setSpacing(10);

            } else {
                nomeRest.setFont(playfairFont);
                nomeRest2.setFont(playfairFont2);
                inforest1.setFont(interfont1);
                inforest2.setFont(interfont2);

                card1.setPrefSize(300, 250);
                card2.setPrefSize(300, 250);
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