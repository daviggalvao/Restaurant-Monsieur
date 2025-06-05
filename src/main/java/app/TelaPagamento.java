package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class TelaPagamento {
    private Stage stage;
    private String tituloDesc;

    public TelaPagamento(Stage stage) {
        this.stage = stage;
    }

    private void updateContent(String selectedItem, VBox contentBox) {
        // Limpar conteúdo existente
        contentBox.getChildren().clear();

        if (selectedItem.equals("Pedidos")) {
            // Conteúdo para "Pedidos"
            Text pedido = new Text("Pedido #PED001");
            Text pizza = new Text("Pizza Margherita x2 .............................. R$ 91.80");
            Text refrigerante = new Text("Refrigerante 2L x1 ............................. R$ 8.50");
            Text sobremesa = new Text("Sobremesa x1 ....................................... R$ 12.00");
            Text taxaEntrega = new Text("Taxa de entrega ................................. R$ 5.00");

            Text subtotal = new Text("Subtotal: ........................................... R$ 117.30");
            subtotal.setStyle("-fx-font-weight: bold; -fx-fill: green;");

            contentBox.getChildren().addAll(pedido, pizza, refrigerante, sobremesa, taxaEntrega, subtotal);
        } else if (selectedItem.equals("Reservas")) {
            // Conteúdo para "Reservas"
            Text reserva = new Text("Reserva #RES001");
            Text data = new Text("Data: 21/11/2025");
            Text local = new Text("Local: Restaurante Monsieur-José");

            contentBox.getChildren().addAll(reserva, data, local);
        }
    }


    private VBox createCard(String svgPath, String titleText, String TipoCard, String borderColor, String textColor) {
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 25); //
        Font interfont = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 12); //

        WebView webView = new WebView(); //
        webView.setMinSize(10, 10); //
        webView.setPrefSize(25, 25); //
        webView.setMaxSize(30, 30); //

        String svgUrl = getClass().getResource(svgPath).toExternalForm(); //
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" + //
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" + //
                "</body></html>"; //
        webView.getEngine().loadContent(html); //

        Label title = new Label(titleText); //
        title.setTextFill(Color.web(textColor)); // Texto do card PRETO
        title.setStyle("-fx-font-size: 25px; -fx-font-weight: 600;");
        HBox titulos = new HBox(7, webView, title);
        titulos.setAlignment(Pos.TOP_LEFT);
        HBox.setMargin(title, new Insets(-3, 0, 0, 0));

        if (TipoCard.equals("Informacoes")) {
            String inputStyle = "-fx-background-color: white;\n" +
                    "    -fx-background-radius: 5px;\n" +
                    "    -fx-border-color: #ddd;\n" +
                    "    -fx-border-radius: 5px;\n" +
                    "    -fx-border-width: 1px;\n" +
                    "    -fx-font-size: 14px;\n" +
                    "    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);";

            Label tipo = new Label("Tipo:");
            tipo.setStyle("-fx-font-size: 14px; -fx-font-weight: 500;");
            ComboBox<String> types = new ComboBox<String>();
            types.getItems().addAll("Pedidos", "Reservas");
            types.setValue("Pedidos");
            types.setPrefHeight(40);
            types.setStyle(inputStyle);
            types.setPrefWidth(400);


            final String[] selectedItem = {"Pedidos"};
            VBox contentBox = new VBox(10);

            types.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Use o valor selecionado
                    selectedItem[0] = newValue;
                    updateContent(selectedItem[0],contentBox);
                }
            });

            VBox tipos = new VBox(10, tipo, types);
            updateContent(selectedItem[0], contentBox);

            VBox vbox = new VBox(20, titulos, tipos, contentBox);
            vbox.setAlignment(Pos.TOP_CENTER);
            vbox.setPadding(new Insets(40));
            vbox.setMinSize(420,450);
            vbox.setPrefSize(420, 450);
            vbox.setMaxSize(420, 950);

            String cardBackgroundColor = "White"; // Fundo do card: Cinza Claro
            String normalStyle = "-fx-border-color: " + borderColor + ";" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 2.0;" +
                    "-fx-background-radius: 10;" +
                    "-fx-background-color: " + cardBackgroundColor + ";";
            vbox.setStyle(normalStyle);

            return vbox;
        }
        VBox vbox = new VBox(20, titulos);
        vbox.setAlignment(Pos.TOP_CENTER); //
        vbox.setPadding(new Insets(40)); //
        vbox.setPrefSize(420, 450); //

        String cardBackgroundColor = "White"; // Fundo do card: Cinza Claro
        String normalStyle = "-fx-border-color: " + borderColor + ";" + //
                "-fx-border-radius: 10;" + //
                "-fx-border-width: 2.0;" + //
                "-fx-background-radius: 10;" + //
                "-fx-background-color: " + cardBackgroundColor + ";"; //
        vbox.setStyle(normalStyle);

        return vbox;
    }

    public void mostrarPagamento() {
        Label titulo = new Label("Finalizar Pagamento");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #FFC300;");

        String corBordaCard = "#E4E9F0";
        String corTextoCard = "black";

        tituloDesc = "Detalhes do Pedido";

        VBox cardInfo = createCard("/svg/package-svgrepo-com.svg", tituloDesc, "Informacoes", corBordaCard, corTextoCard);
        VBox cardPagamento = createCard("/svg/credit-card-svgrepo-com.svg", "Pagamento", "Promover/Contratar funcionários", corBordaCard, corTextoCard);

        HBox cardsContainer = new HBox(20); // 10 é o espaçamento entre os cartões
        cardsContainer.getChildren().addAll(cardInfo, cardPagamento);
        cardsContainer.setAlignment(Pos.CENTER);

        VBox contenedor = new VBox(25); // Espaçamento entre o título e os cards
        contenedor.getChildren().addAll(titulo, cardsContainer);
        contenedor.setAlignment(Pos.CENTER);

        ScrollPane pane = new ScrollPane();
        pane.setContent(contenedor);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        Scene scene = new Scene(pane);
        scene.setFill(Color.RED);

        stage.setTitle("Pagamento");
        stage.setScene(scene);
        stage.setMinWidth(800); // Largura mínima
        stage.setMinHeight(600); // Altura mínima
        stage.setMaximized(true); // Maximizar a janela
        stage.show(); // Exibir a tela
    }
}
