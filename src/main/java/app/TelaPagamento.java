package app;

import classes.Pedido;
import classes.Reserva;
import database.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region; // Importe Region
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.List;

public class TelaPagamento extends Tela { // A classe já estende Tela
    private String userEmail;
    private String tituloDesc;
    private String tituloDesc2;
    private String p1 = "1x de R$ 117.30(à vista)";
    private String p2 = "2x de R$ 58.65";
    private String p3 = "3x de R$ 39.10";
    private String p4 = "4x de R$ 29.32";
    private String p5 = "5x de R$ 23.46";


    public TelaPagamento(Stage stage, String email) {

        super(stage);
        this.userEmail = email;
    }

    public List<Reserva> buscarReservasPorEmailHQL(EntityManager em) {
        TypedQuery<Reserva> query = em.createQuery("FROM Reserva r WHERE r.cliente.email = :email ", Reserva.class);
        query.setParameter("email", this.userEmail);
        return query.getResultList();
    }

    public List<Pedido> buscarPedidosPorEmailHQL(EntityManager em) {
        // Assume que a classe Pedido tem um campo 'consumidor' que é um Cliente
        // e que Cliente tem um campo 'email'.
        TypedQuery<Pedido> query = em.createQuery("FROM Pedido p WHERE p.consumidor.email = :email ", Pedido.class);
        query.setParameter("email", this.userEmail);
        return query.getResultList();
    }

    // O método updateContent agora recebe o ScrollPane como argumento
    // O ScrollPane não é usado para alinhamento de preço, mas o seu uso foi mantido da versão anterior
    private void updateContent(String selectedItem, VBox contentBox) {
        contentBox.getChildren().clear(); // Limpa o conteúdo anterior

        if (selectedItem.equals("Pedidos")) {
            // O ScrollPane que contém contentBox é passado para este método na versão anterior.
            // Para controle da barra de rolagem, parentScrollPane.setVbarPolicy() deve ser chamado
            // dentro de createCard no listener ou em uma chamada inicial.
            // parentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            // parentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            EntityManager tempEm = JpaUtil.getFactory().createEntityManager();
            List<Pedido> pedidos = buscarPedidosPorEmailHQL(tempEm);

            double totalPedidosCalculado = 0.0;

            if (pedidos.isEmpty()) {
                Label noPedidosLabel = new Label("Nenhum pedido encontrado para este usuário.");
                noPedidosLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
                contentBox.getChildren().add(noPedidosLabel);
            } else {
                for (Pedido p : pedidos) {
                    Label pedidoHeader = new Label("Pedido #" + p.getId());
                    pedidoHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                    contentBox.getChildren().add(pedidoHeader);

                    // Adicionar os itens do pedido
                    if (p.getItensPedido() != null && !p.getItensPedido().isEmpty()) {
                        for (classes.PedidoItem item : p.getItensPedido()) {
                            if (item.getPrato() != null) {
                                double precoTotalItem = item.getQuantidade() * item.getPrato().getPreco();
                                String itemText = String.format("%s x%d", item.getPrato().getNome(), item.getQuantidade());
                                String precoItemText = String.format("R$ %.2f", precoTotalItem);

                                HBox itemRow = new HBox(); // <--- SEM ESPAÇAMENTO FIXO AQUI
                                Label itemLabel = new Label(itemText);
                                itemLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");
                                itemLabel.setWrapText(true); // Manter quebra de linha

                                Region spacer = new Region(); // <--- NOVO: Espaçador flexível
                                HBox.setHgrow(spacer, Priority.ALWAYS); // <--- NOVO: Espaçador ocupará o espaço restante

                                Label precoLabel = new Label(precoItemText);
                                precoLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");
                                // precoLabel.setWrapText(true); // Geralmente não é necessário para preços, mas mantido se desejar

                                // Adiciona o itemLabel, o espaçador e o precoLabel
                                itemRow.getChildren().addAll(itemLabel, spacer, precoLabel); // <--- MODIFICADO AQUI
                                itemRow.setAlignment(Pos.BASELINE_LEFT); // Alinhar itens na linha (left)
                                contentBox.getChildren().add(itemRow);
                            }
                        }
                    } else {
                        Label noItemsLabel = new Label("    (Sem itens no pedido)");
                        noItemsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
                        contentBox.getChildren().add(noItemsLabel);
                    }

                    // Adicionar taxa de entrega específica do pedido
                    // Também aplicando a mesma lógica para alinhar "R$ X.XX" à direita
                    HBox taxaEntregaRow = new HBox();
                    Label taxaEntregaTextLabel = new Label("Taxa de entrega");
                    taxaEntregaTextLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");
                    taxaEntregaTextLabel.setWrapText(true);

                    Region spacerTaxa = new Region();
                    HBox.setHgrow(spacerTaxa, Priority.ALWAYS);

                    Label taxaEntregaPrecoLabel = new Label(String.format("R$ %.2f", (double)p.getFrete()));
                    taxaEntregaPrecoLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");

                    taxaEntregaRow.getChildren().addAll(taxaEntregaTextLabel, spacerTaxa, taxaEntregaPrecoLabel);
                    taxaEntregaRow.setAlignment(Pos.BASELINE_LEFT);
                    contentBox.getChildren().add(taxaEntregaRow);


                    Rectangle separator = new Rectangle(325, 1); // Separador entre pedidos
                    separator.setFill(Color.web("#eee"));
                    VBox.setMargin(separator, new Insets(10, 0, 10, 0));
                    contentBox.getChildren().add(separator);

                    totalPedidosCalculado += p.calcularPrecoTotal(); // Soma o total de cada pedido
                }

                // Linhas e subtotal final para todos os pedidos
                Rectangle sublinhado = new Rectangle(325, 2); // Ajustado para 325 para consistência, se o card for 400 de largura
                sublinhado.setFill(Color.web("#ddd"));
                contentBox.getChildren().add(sublinhado);

                Label subtotalGeralLabel = new Label("Subtotal Geral:");
                subtotalGeralLabel.setStyle("-fx-font-weight: bold; -fx-fill: black; -fx-font-size: 17px; ");
                subtotalGeralLabel.setAlignment(Pos.CENTER_LEFT);

                Region spacerTotalGeral = new Region(); // <--- NOVO: Espaçador flexível para o subtotal geral
                HBox.setHgrow(spacerTotalGeral, Priority.ALWAYS); // <--- NOVO: Ocupa o espaço restante

                Label precoTotalGeral = new Label(String.format("R$ %.2f", totalPedidosCalculado));
                precoTotalGeral.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 17px; ");
                precoTotalGeral.setAlignment(Pos.CENTER_RIGHT);

                HBox subtotalPrecoBox = new HBox(); // <--- SEM ESPAÇAMENTO FIXO AQUI
                subtotalPrecoBox.setAlignment(Pos.CENTER_LEFT);
                // HBox.setHgrow(precoTotalGeral, Priority.ALWAYS); // Removido, o spacerTotalGeral faz o trabalho
                subtotalPrecoBox.getChildren().addAll(subtotalGeralLabel, spacerTotalGeral, precoTotalGeral); // <--- NOVO

                // Afasta o HBox do subtotal da borda esquerda
                VBox.setMargin(subtotalPrecoBox, new Insets(0, 0, 0, 15)); // <--- NOVO: Adiciona margem esquerda de 15px
                contentBox.getChildren().add(subtotalPrecoBox);
            }

            contentBox.setAlignment(Pos.BASELINE_LEFT);
            VBox.setMargin(contentBox,  new Insets(0, 0, 0, 0)); // Mantendo a margem interna do VBox
            tempEm.close(); // Fecha o EntityManager
        } else if (selectedItem.equals("Reservas")) {

            EntityManager tempEm = JpaUtil.getFactory().createEntityManager();
            List<Reserva> reservations = buscarReservasPorEmailHQL(tempEm);

            double totalReservas = 0.0;

            if (reservations.isEmpty()) {
                Label noReservasLabel = new Label("Nenhuma reserva encontrada para este usuário.");
                noReservasLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
                contentBox.getChildren().add(noReservasLabel);
            } else {
                for (Reserva r : reservations) {
                    String dia = "N/A";
                    String mes = "N/A";
                    if (r.getData() != null) {
                        String[] dias = r.getData().toString().split("-");
                        if (dias.length >= 3) {
                            dia = dias[2];
                            mes = dias[1];
                        }
                    }

                    String nomeCliente = "Cliente Desconhecido";
                    if (r.getCliente() != null && r.getCliente().getNome() != null) {
                        String[] nomes = r.getCliente().getNome().split(" ");
                        if (nomes.length > 0) {
                            nomeCliente = nomes[0];
                        }
                    }

                    double valorPagamento = 0.0;
                    String nomePagamento = "N/A";

                    // Verificação de nulo para Pagamento
                    if (r.getPagamento() != null) {
                        valorPagamento = r.getPagamento().getPreco();
                        nomePagamento = r.getPagamento().getNome(); // Assumindo que Pagamento tem getNome()
                    }

                    HBox reservationRow = new HBox(); // <--- NOVO: HBox para a linha da reserva
                    Label reservationTextLabel = new Label(
                            String.format("  - Reserva para %s às %s do dia %s-%s",
                                    nomeCliente, r.getHorario(), dia, mes)
                    );
                    reservationTextLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: mediumbold;");
                    reservationTextLabel.setWrapText(true);

                    Region spacerReserva = new Region(); // <--- NOVO: Espaçador flexível para a reserva
                    HBox.setHgrow(spacerReserva, Priority.ALWAYS); // <--- NOVO: Ocupa o espaço restante

                    Label reservationPriceLabel = new Label(String.format("(R$ %.2f)", valorPagamento)); // Preço entre parênteses
                    reservationPriceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");

                    reservationRow.getChildren().addAll(reservationTextLabel, spacerReserva, reservationPriceLabel);
                    reservationRow.setAlignment(Pos.BASELINE_LEFT);
                    contentBox.getChildren().add(reservationRow);

                    totalReservas += valorPagamento; // Usa o valor do pagamento para o total
                }

                Rectangle sublinhado = new Rectangle(325, 2);
                sublinhado.setFill(Color.web("#ddd"));

                // Removendo a Taxa de Serviço fixa, pois não estava sendo usada dinamicamente
                // Label taxaServico = new Label("Taxa de Serviço                                          R$ 0.00");
                // taxaServico.setStyle("-fx-font-size: 14px; -fx-font-weight: mediumbold;");
                // contentBox.getChildren().add(taxaServico);

                // Removido sublinhado2, conforme solicitado anteriormente.
                // Rectangle sublinhado2 = new Rectangle(325, 2);
                // sublinhado2.setFill(Color.web("#ddd"));


                Label subtotal = new Label("Subtotal:");
                subtotal.setStyle("-fx-font-weight: bold; -fx-fill: black; -fx-font-size: 17px; ");
                subtotal.setAlignment(Pos.CENTER_LEFT);

                Region spacerReservaSubtotal = new Region(); // <--- NOVO: Espaçador flexível para o subtotal da reserva
                HBox.setHgrow(spacerReservaSubtotal, Priority.ALWAYS); // <--- NOVO: Ocupa o espaço restante

                Label precoTotal = new Label(String.format("R$ %.2f", totalReservas));
                precoTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 17px; ");
                precoTotal.setAlignment(Pos.CENTER_RIGHT);

                HBox subtotalPrecoBox = new HBox(); // <--- SEM ESPAÇAMENTO FIXO AQUI
                subtotalPrecoBox.setAlignment(Pos.CENTER_LEFT);
                // HBox.setHgrow(precoTotal, Priority.ALWAYS); // Removido
                subtotalPrecoBox.getChildren().addAll(subtotal, spacerReservaSubtotal, precoTotal); // <--- NOVO

                // Afasta o HBox do subtotal da borda esquerda
                VBox.setMargin(subtotalPrecoBox, new Insets(0, 0, 0, 15)); // <--- NOVO: Adiciona margem esquerda de 15px
                contentBox.getChildren().addAll(sublinhado, subtotalPrecoBox); // Adiciona apenas o que é relevante
            }

            contentBox.setAlignment(Pos.BASELINE_LEFT);
            VBox.setMargin(contentBox,  new Insets(0, 0, 0, 0));
            tempEm.close(); // Fecha o EntityManager
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
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 25);
        Font interfont = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 12);

        WebView webView = new WebView();
        webView.setMinSize(10, 10);
        webView.setPrefSize(25, 25);
        webView.setMaxSize(30, 30);

        String svgUrl = getClass().getResource(svgPath).toExternalForm();
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" +
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" +
                "</body></html>";
        webView.getEngine().loadContent(html);

        Label title = new Label(titleText);
        title.setTextFill(Color.web(textColor));
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
            VBox dynamicContentVBox = new VBox(15);
            dynamicContentVBox.setStyle("-fx-background-color: white");

            // Adiciona o dynamicContentVBox dentro de um ScrollPane
            ScrollPane contentScrollPane = new ScrollPane(dynamicContentVBox);
            contentScrollPane.setFitToWidth(true); // Faz o ScrollPane se ajustar à largura do VBox pai
            contentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            contentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            contentScrollPane.setFitToHeight(true);

            types.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectedItem[0] = newValue;
                    updateContent(selectedItem[0], dynamicContentVBox); // <--- ATENÇÃO: ScrollPane não está sendo passado para updateContent aqui.
                }
            });

            VBox tipos = new VBox(10, tipo, types);
            updateContent(selectedItem[0], dynamicContentVBox); // <--- ATENÇÃO: ScrollPane não está sendo passado para updateContent aqui.

            dynamicContentVBox.setStyle("-fx-background-color: white");
            contentScrollPane.setStyle("-fx-background-color: white");
            VBox vbox = new VBox(20, titulos, tipos, contentScrollPane);
            vbox.setAlignment(Pos.TOP_CENTER);
            vbox.setPadding(new Insets(40));
            vbox.setMinSize(420,450);
            vbox.setPrefSize(420, 450);
            vbox.setMaxSize(420, 950);
            VBox.setVgrow(contentScrollPane, Priority.ALWAYS);

            String cardBackgroundColor = "White";
            String normalStyle = "-fx-border-color: " + borderColor + ";" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 2.0;" +
                    "-fx-background-radius: 10;" +
                    "-fx-background-color: " + cardBackgroundColor + ";";
            vbox.setStyle(normalStyle);

            return vbox;

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
                    "-fx-background-color: #0C1120;" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 15 50 15 50;" +
                            "-fx-background-radius: 10;" +
                            "-fx-cursor: hand;"
            );

            Label mensagemSeguranca = new Label("Pagamento seguro • Seus dados estão protegidos");
            mensagemSeguranca.setFont(Font.font("System", 13));
            mensagemSeguranca.setStyle("-fx-text-fill: #5E6D82;");
            mensagemSeguranca.setAlignment(Pos.CENTER);

            VBox botao = new VBox(10);
            botao.getChildren().addAll(finalizarPagamento, mensagemSeguranca);
            botao.setAlignment(Pos.CENTER);

            VBox vbox = new VBox(20, titulos,tipos,contentBox,botao);
            vbox.setAlignment(Pos.TOP_CENTER);
            vbox.setPadding(new Insets(40));
            vbox.setPrefSize(420, 450);

            String cardBackgroundColor = "White";
            String normalStyle = "-fx-border-color: " + borderColor + ";" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 2.0;" +
                    "-fx-background-radius: 10;" +
                    "-fx-background-color: " + cardBackgroundColor + ";";
            vbox.setStyle(normalStyle);

            return vbox;
        }
        VBox vbox = new VBox(20, titulos);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(40));
        vbox.setPrefSize(420, 450);

        String cardBackgroundColor = "White";
        String normalStyle = "-fx-border-color: " + borderColor + ";" +
                "-fx-border-radius: 10;" +
                "-fx-border-width: 2.0;" +
                "-fx-background-radius: 10;" +
                "-fx-background-color: " + cardBackgroundColor + ";";
        vbox.setStyle(normalStyle);

        return vbox;
    }

    @Override // ADICIONAR @Override e mudar de 'void mostrarTela()' para 'public Scene criarScene()'
    public Scene criarScene() {
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

        HBox cardsContainer = new HBox(20);
        cardsContainer.getChildren().addAll(cardInfo, cardPagamento);
        cardsContainer.setAlignment(Pos.CENTER);

        VBox contenedor = new VBox(25);
        contenedor.getChildren().addAll(titulo, cardsContainer);
        contenedor.setAlignment(Pos.CENTER);

        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)";
        contenedor.setStyle("-fx-background-color:"  + estiloFundoVinho + ";");

        ScrollPane pane = new ScrollPane();
        pane.setContent(contenedor);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        pane.setStyle("-fx-background-color:"  + estiloFundoVinho + ";");

        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(pane); // Adiciona o ScrollPane original.
        Scene scene = new Scene(rootPane);
        return scene;
    }
}