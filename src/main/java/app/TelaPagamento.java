package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class TelaPagamento extends Tela {
    private String tituloDesc;
    private String tituloDesc2;
    private String p1 = "1x de R$ 117.30(à vista)";

    private String p2 = "2x de R$ 58.65";
    private String p3 = "3x de R$ 39.10";
    private String p4 = "4x de R$ 29.32";
    private String p5 = "5x de R$ 23.46";


    public TelaPagamento(Stage stage) {
        super(stage);
    }

    private void updateContent(String selectedItem, VBox contentBox) {

        contentBox.getChildren().clear();

        if (selectedItem.equals("Pedidos")) {
            Label pedido = new Label("Pedido #PED001");
            pedido.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            Label pizza = new Label("Pizza Margherita x2                                  R$ 91.80");
            pizza.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");
            Label refrigerante = new Label("Refrigerante 2L x1                                       R$ 8.50");
            refrigerante.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");
            Label sobremesa = new Label("Sobremesa x1                                            R$ 12.00");
            sobremesa.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");
            Label taxaEntrega = new Label("Taxa de entrega                                           R$ 5.00");
            taxaEntrega.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");

            Rectangle sublinhado = new Rectangle(325, 2);
            sublinhado.setFill(Color.web("#ddd"));

            Rectangle sublinhado2 = new Rectangle(325, 2);
            sublinhado2.setFill(Color.web("#ddd"));


            Label subtotal = new Label("Subtotal:");
            subtotal.setStyle("-fx-font-weight: bold; -fx-fill: black; -fx-font-size: 17px; ");
            subtotal.setAlignment(Pos.CENTER_LEFT);
            Label preco = new Label("R$ 117.30");
            preco.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 17px; ");
            preco.setAlignment(Pos.CENTER_RIGHT);

            HBox subtotalPrecoBox = new HBox(170, subtotal, preco);
            subtotalPrecoBox.setAlignment(Pos.CENTER_LEFT); // Alinha os itens no HBox à esquerda
            HBox.setHgrow(preco, Priority.ALWAYS); // Garante que o preço ficará alinhado à direita


            contentBox.getChildren().addAll(pedido, pizza, refrigerante, sobremesa, sublinhado, taxaEntrega, sublinhado2, subtotalPrecoBox);
            contentBox.setAlignment(Pos.BASELINE_LEFT);
            VBox.setMargin(contentBox,  new Insets(0, 0, 0, 3));


        } else if (selectedItem.equals("Reservas")) {

            Label reserva = new Label("Reserva #RES001");
            reserva.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            Label data = new Label("15/12/2024 às 19:30");
            data.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");
            Label local = new Label("Local: Salão Principal");
            local.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");
            Label preco = new Label("Mesa para 4 pessoas                                R$ 120.00");
            preco.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");
            Label taxaServico = new Label("Taxa de serviço                                           R$ 15.00");
            taxaServico.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");

            Rectangle sublinhado = new Rectangle(325, 2);
            sublinhado.setFill(Color.web("#ddd"));

            Rectangle sublinhado2 = new Rectangle(325, 2);
            sublinhado2.setFill(Color.web("#ddd"));

            Label subtotal = new Label("Subtotal:");
            subtotal.setStyle("-fx-font-weight: bold; -fx-fill: black; -fx-font-size: 17px; ");
            subtotal.setAlignment(Pos.CENTER_LEFT);
            Label precoTotal = new Label("R$ 145.00");
            precoTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 17px; ");
            precoTotal.setAlignment(Pos.CENTER_RIGHT);

            HBox subtotalPrecoBox = new HBox(162, subtotal, precoTotal);
            subtotalPrecoBox.setAlignment(Pos.CENTER_LEFT); // Alinha os itens no HBox à esquerda
            HBox.setHgrow(precoTotal, Priority.ALWAYS); // Garante que o preço ficará alinhado à direita

            contentBox.getChildren().addAll(reserva, data, local, preco , sublinhado, taxaServico, sublinhado2, subtotalPrecoBox);
            contentBox.setAlignment(Pos.BASELINE_LEFT);
            VBox.setMargin(contentBox,  new Insets(0, 0, 0, 3));
        }
    }

    private void updateContent2(String selectedItem, VBox contentBox) {
        contentBox.getChildren().clear();

        String[] Partes = selectedItem.split(" ");
        if(Partes[0].equals("1x")){
             contentBox.setPadding(new Insets(20));
             contentBox.setStyle("-fx-background-color: #FAFAFA; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");

            Label titulo = new Label("Resumo do Pagamento:");
            titulo.setFont(Font.font("System", FontWeight.BOLD, 16));

            Label valorTotal = new Label("Valor total:       R$ 117,30");
            valorTotal.setFont(Font.font("System", 14));

            Label parcelas = new Label("Parcelas:           1x");
            parcelas.setFont(Font.font("System", 14));

            Label valorParcela = new Label("Valor por parcela:   R$ 117,30");
            valorParcela.setFont(Font.font("System", FontWeight.BOLD, 14));

            Label pagamentoAVista = new Label("✓ Pagamento à vista");
            pagamentoAVista.setTextFill(Color.GREEN);
            pagamentoAVista.setFont(Font.font("System", 14));

            contentBox.getChildren().addAll(titulo, valorTotal, parcelas, valorParcela, pagamentoAVista);
            contentBox.setAlignment(Pos.BASELINE_LEFT);
        } else if (Partes[0].equals("2x")) {
            contentBox.setPadding(new Insets(20));
            contentBox.setStyle("-fx-background-color: #FAFAFA; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");

            Label titulo = new Label("Resumo do Pagamento:");
            titulo.setFont(Font.font("System", FontWeight.BOLD, 16));

            Label valorTotal = new Label("Valor total:       R$ 117,30");
            valorTotal.setFont(Font.font("System", 14));

            Label parcelas = new Label("Parcelas:           2x");
            parcelas.setFont(Font.font("System", 14));

            Label valorParcela = new Label("Valor por parcela:   R$ 58.65");
            valorParcela.setFont(Font.font("System", FontWeight.BOLD, 14));

            contentBox.getChildren().addAll(titulo, valorTotal, parcelas, valorParcela);
            contentBox.setAlignment(Pos.BASELINE_LEFT);
        }else if (Partes[0].equals("3x")) {
            contentBox.setPadding(new Insets(20));
            contentBox.setStyle("-fx-background-color: #FAFAFA; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");

            Label titulo = new Label("Resumo do Pagamento:");
            titulo.setFont(Font.font("System", FontWeight.BOLD, 16));

            Label valorTotal = new Label("Valor total:       R$ 117,30");
            valorTotal.setFont(Font.font("System", 14));

            Label parcelas = new Label("Parcelas:           3x");
            parcelas.setFont(Font.font("System", 14));

            Label valorParcela = new Label("Valor por parcela:   R$ 39.10");
            valorParcela.setFont(Font.font("System", FontWeight.BOLD, 14));

            contentBox.getChildren().addAll(titulo, valorTotal, parcelas, valorParcela);
            contentBox.setAlignment(Pos.BASELINE_LEFT);
        }else if (Partes[0].equals("4x")) {
            contentBox.setPadding(new Insets(20));
            contentBox.setStyle("-fx-background-color: #FAFAFA; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");

            Label titulo = new Label("Resumo do Pagamento:");
            titulo.setFont(Font.font("System", FontWeight.BOLD, 16));

            Label valorTotal = new Label("Valor total:       R$ 117,30");
            valorTotal.setFont(Font.font("System", 14));

            Label parcelas = new Label("Parcelas:           4x");
            parcelas.setFont(Font.font("System", 14));

            Label valorParcela = new Label("Valor por parcela:   R$ 29.32");
            valorParcela.setFont(Font.font("System", FontWeight.BOLD, 14));

            contentBox.getChildren().addAll(titulo, valorTotal, parcelas, valorParcela);
            contentBox.setAlignment(Pos.BASELINE_LEFT);
        }else if (Partes[0].equals("5x")) {
            contentBox.setPadding(new Insets(20));
            contentBox.setStyle("-fx-background-color: #FAFAFA; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");

            Label titulo = new Label("Resumo do Pagamento:");
            titulo.setFont(Font.font("System", FontWeight.BOLD, 16));

            Label valorTotal = new Label("Valor total:       R$ 117,30");
            valorTotal.setFont(Font.font("System", 14));

            Label parcelas = new Label("Parcelas:           5x");
            parcelas.setFont(Font.font("System", 14));

            Label valorParcela = new Label("Valor por parcela:   R$ 23.46");
            valorParcela.setFont(Font.font("System", FontWeight.BOLD, 14));

            contentBox.getChildren().addAll(titulo, valorTotal, parcelas, valorParcela);
            contentBox.setAlignment(Pos.BASELINE_LEFT);
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


        //---------------Criacao card do resumo pagamento ---------------//

        if (TipoCard.equals("Informacoes")) {
            String inputStyle = "-fx-background-color: white;\n" +
                    "    -fx-background-radius: 5px;\n" +
                    "    -fx-border-color: #ddd;\n" +
                    "    -fx-border-radius: 5px;\n" +
                    "    -fx-border-width: 1px;\n" +
                    "    -fx-font-size: 14px;\n";

            Label tipo = new Label("Tipo:");
            tipo.setStyle("-fx-font-size: 14px; -fx-font-weight: 500;");
            ComboBox<String> types = new ComboBox<String>();
            types.getItems().addAll("Pedidos", "Reservas");
            types.setValue("Pedidos");
            types.setPrefHeight(40);
            types.setStyle(inputStyle);
            types.setPrefWidth(400);


            final String[] selectedItem = {"Pedidos"};
            VBox contentBox = new VBox(15);

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

            //-----------------------Criacao Card Pagamento ----------------------//

        }else if(TipoCard.equals("Pagamento")){
            String inputStyle = "-fx-background-color: white;\n" +
                    "    -fx-background-radius: 5px;\n" +
                    "    -fx-border-color: #ddd;\n" +
                    "    -fx-border-radius: 5px;\n" +
                    "    -fx-border-width: 1px;\n" +
                    "    -fx-font-size: 14px;\n";

            Label tipo = new Label("Parcelamento:");
            tipo.setStyle("-fx-font-size: 14px; -fx-font-weight: 500;");
            ComboBox<String> types = new ComboBox<String>();
            types.getItems().addAll(p1,p2,p3,p4,p5);
            types.setValue("1x de R$ 117.30(à vista)");
            types.setPrefHeight(40);
            types.setStyle(inputStyle);
            types.setPrefWidth(400);


            final String[] selectedItem = {p1};
            VBox contentBox = new VBox(10);

            types.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Use o valor selecionado
                    selectedItem[0] = newValue;
                    updateContent2(selectedItem[0],contentBox);
                }
            });

            VBox tipos = new VBox(10, tipo, types);
            updateContent2(selectedItem[0], contentBox);

            Button finalizarPagamento = new Button("Finalizar Pagamento  💳");
            finalizarPagamento.setPrefWidth(400);
            finalizarPagamento.setFont(Font.font("System", FontWeight.BOLD, 16));
            finalizarPagamento.setStyle(
                    "-fx-background-color: #0C1120;" +  // cor escura
                            "-fx-text-fill: white;" +
                            "-fx-padding: 15 50 15 50;" +  // padding grande
                            "-fx-background-radius: 10;" + // canto arredondado
                            "-fx-cursor: hand;"  // cursor de mão
            );

            Label mensagemSeguranca = new Label("Pagamento seguro • Seus dados estão protegidos");
            mensagemSeguranca.setFont(Font.font("System", 13));
            mensagemSeguranca.setStyle("-fx-text-fill: #5E6D82;");
            mensagemSeguranca.setAlignment(Pos.CENTER);

            VBox botao = new VBox(10);
            botao.getChildren().addAll(finalizarPagamento, mensagemSeguranca);
            botao.setAlignment(Pos.CENTER);

            VBox vbox = new VBox(20, titulos,tipos,contentBox,botao);
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

    @Override
    public void mostrarTela() {
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Label titulo = new Label("Finalizar Pagamento");
        titulo.setFont(playfairFont);
        titulo.setStyle("-fx-text-fill: #FFC300;");


        String corBordaCard = "#E4E9F0";
        String corTextoCard = "black";

        tituloDesc = "Detalhes do Pedido";
        tituloDesc2 = "Forma de Pagamento";

        VBox cardInfo = createCard("/svg/package-svgrepo-com.svg", tituloDesc, "Informacoes", corBordaCard, corTextoCard);
        VBox cardPagamento = createCard("/svg/credit-card-svgrepo-com.svg", tituloDesc2, "Pagamento", corBordaCard, corTextoCard);

        HBox cardsContainer = new HBox(20); // 10 é o espaçamento entre os cartões
        cardsContainer.getChildren().addAll(cardInfo, cardPagamento);
        cardsContainer.setAlignment(Pos.CENTER);

        VBox contenedor = new VBox(25); // Espaçamento entre o título e os cards
        contenedor.getChildren().addAll(titulo, cardsContainer);
        contenedor.setAlignment(Pos.CENTER);

        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)";
        contenedor.setStyle("-fx-background-color:"  + estiloFundoVinho + ";");

        ScrollPane pane = new ScrollPane();
        pane.setContent(contenedor);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        pane.setStyle("-fx-background-color:"  + estiloFundoVinho + ";");

        Scene scene = new Scene(pane);

        super.getStage().setTitle("Pagamento");
        super.getStage().setScene(scene);
        super.getStage().setMinWidth(800); // Largura mínima
        super.getStage().setMinHeight(600); // Altura mínima
        super.getStage().setMaximized(true); // Maximizar a janela
        super.getStage().show(); // Exibir a tela
    }
}
