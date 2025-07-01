package app;

import classes.ContaBancariaJose;
import classes.OrigemDaTela;
import classes.Pedido;
import classes.PedidoItem;
import classes.Prato;
import classes.Reserva;
import database.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class TelaPagamento extends Tela {
    private String userEmail;
    private OrigemDaTela origem;
    private String tituloDesc;
    private String tituloDesc2;
    private static final String TEXT_COLOR_ON_PANEL = "#3D2B1F";

    public TelaPagamento(Stage stage, String email, OrigemDaTela origem) {
        super(stage);
        this.userEmail = email;
        this.origem = origem;
    }

    public List<Reserva> buscarReservasPorEmailHQL(EntityManager em) {
        TypedQuery<Reserva> query = em.createQuery("FROM Reserva r WHERE r.cliente.email = :email ", Reserva.class);
        query.setParameter("email", this.userEmail);
        return query.getResultList();
    }

    public List<Pedido> buscarPedidosPorEmailHQL(EntityManager em) {
        TypedQuery<Pedido> query = em.createQuery("SELECT p FROM Pedido p LEFT JOIN FETCH p.itensPedido pi LEFT JOIN FETCH pi.prato WHERE p.consumidor.email = :email", Pedido.class);
        query.setParameter("email", this.userEmail);
        return query.getResultList();
    }

    private double calcularSubtotalReservas() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        try {
            List<Reserva> reservas = buscarReservasPorEmailHQL(em);
            double total = 0.0;
            for (Reserva r : reservas) {
                if (r.getPagamento() != null) {
                    total += r.getPagamento().getPreco();
                }
            }
            return total;
        } finally {
            em.close();
        }
    }

    private double calcularSubtotalPedidos() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        try {
            List<Pedido> pedidos = buscarPedidosPorEmailHQL(em);
            double total = 0.0;
            for (Pedido p : pedidos) {
                if(p.getPagamento().getTipo().split(" ")[0].equalsIgnoreCase("Pix")){
                    total += (p.calcularPrecoTotal()) * 0.9f;
                } else {
                    total += p.calcularPrecoTotal();
                }
            }
            return total;
        } finally {
            em.close();
        }
    }

    private void updateContent(String selectedItem, VBox contentBox) {
        contentBox.getChildren().clear();
        String styleText = "-fx-font-size: 13px; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + ";";

        if (selectedItem.equals(Tela.emFrances ? "Commandes" : "Pedidos")) {
            EntityManager tempEm = JpaUtil.getFactory().createEntityManager();
            List<Pedido> pedidos = buscarPedidosPorEmailHQL(tempEm);
            double totalPedidosCalculado = 0.0;

            if (pedidos.isEmpty()) {
                Label noPedidosLabel = new Label(Tela.emFrances ? "Aucune commande trouvée pour cet utilisateur." : "Nenhum pedido encontrado para este usuário.");
                noPedidosLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
                contentBox.getChildren().add(noPedidosLabel);
            } else {
                for (Pedido p : pedidos) {
                    Label pedidoHeader = new Label((Tela.emFrances ? "Commande #" : "Pedido #") + p.getId());
                    pedidoHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + ";");
                    contentBox.getChildren().add(pedidoHeader);

                    if (p.getItensPedido() != null && !p.getItensPedido().isEmpty()) {
                        for (PedidoItem item : p.getItensPedido()) {
                            if (item.getPrato() != null) {
                                double precoTotalItem = item.getQuantidade() * item.getPrato().getPreco();
                                String itemText = String.format("%s x%d", item.getPrato().getNome(), item.getQuantidade());
                                String precoItemText = String.format("R$ %.2f", precoTotalItem);

                                HBox itemRow = new HBox();
                                Label itemLabel = new Label(itemText);
                                itemLabel.setStyle(styleText);
                                Region spacer = new Region();
                                HBox.setHgrow(spacer, Priority.ALWAYS);
                                Label precoLabel = new Label(precoItemText);
                                precoLabel.setStyle(styleText);
                                itemRow.getChildren().addAll(itemLabel, spacer, precoLabel);
                                contentBox.getChildren().add(itemRow);
                            }
                        }
                    } else {
                        Label noItemsLabel = new Label(Tela.emFrances ? " (Aucun article dans la commande)" : " (Sem itens no pedido)");
                        noItemsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
                        contentBox.getChildren().add(noItemsLabel);
                    }

                    HBox taxaEntregaRow = new HBox();
                    Label taxaEntregaTextLabel = new Label(Tela.emFrances ? "Frais de livraison" : "Taxa de entrega");
                    taxaEntregaTextLabel.setStyle(styleText);
                    Region spacerTaxa = new Region();
                    HBox.setHgrow(spacerTaxa, Priority.ALWAYS);
                    Label taxaEntregaPrecoLabel = new Label(String.format("R$ %.2f", (double) p.getFrete()));
                    taxaEntregaPrecoLabel.setStyle(styleText);
                    taxaEntregaRow.getChildren().addAll(taxaEntregaTextLabel, spacerTaxa, taxaEntregaPrecoLabel);
                    contentBox.getChildren().add(taxaEntregaRow);

                    Rectangle separator = new Rectangle(325, 1);
                    separator.setFill(Color.web("#eee"));
                    VBox.setMargin(separator, new Insets(10, 0, 10, 0));
                    contentBox.getChildren().add(separator);

                    if(p.getPagamento().getTipo().split(" ")[0].equalsIgnoreCase("Pix")){
                        totalPedidosCalculado += (p.calcularPrecoTotal()) * 0.9f;
                    } else { totalPedidosCalculado += p.calcularPrecoTotal(); }
                }

                Rectangle sublinhado = new Rectangle(325, 2);
                sublinhado.setFill(Color.web("#ddd"));
                contentBox.getChildren().add(sublinhado);

                Label subtotalGeralLabel = new Label(Tela.emFrances ? "Sous-total Général :" : "Subtotal Geral:");
                subtotalGeralLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-font-size: 17px;");
                Region spacerTotalGeral = new Region();
                HBox.setHgrow(spacerTotalGeral, Priority.ALWAYS);
                Label precoTotalGeral = new Label(String.format("R$ %.2f", totalPedidosCalculado));
                precoTotalGeral.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 17px; ");
                HBox subtotalPrecoBox = new HBox(subtotalGeralLabel, spacerTotalGeral, precoTotalGeral);
                contentBox.getChildren().add(subtotalPrecoBox);
            }
            tempEm.close();
        } else if (selectedItem.equals(Tela.emFrances ? "Réservations" : "Reservas")) {
            EntityManager tempEm = JpaUtil.getFactory().createEntityManager();
            List<Reserva> reservations = buscarReservasPorEmailHQL(tempEm);
            double totalReservas = 0.0;

            if (reservations.isEmpty()) {
                Label noReservasLabel = new Label(Tela.emFrances ? "Aucune réservation trouvée pour cet utilisateur." : "Nenhuma reserva encontrada para este usuário.");
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

                    String nomeCliente = Tela.emFrances ? "Client Inconnu" : "Cliente Desconhecido";
                    if (r.getCliente() != null && r.getCliente().getNome() != null) {
                        String[] nomes = r.getCliente().getNome().split(" ");
                        if (nomes.length > 0) {
                            nomeCliente = nomes[0];
                        }
                    }

                    double valorPagamento = 0.0;
                    if (r.getPagamento() != null) {
                        valorPagamento = r.getPagamento().getPreco();
                    }

                    HBox reservationRow = new HBox();
                    Label reservationTextLabel = new Label(
                            String.format((Tela.emFrances ? " - Réservation pour %s à %s le %s-%s" : " - Reserva para %s às %s do dia %s-%s"),
                                    nomeCliente, r.getHorario(), dia, mes)
                    );
                    reservationTextLabel.setStyle(styleText);
                    reservationTextLabel.setWrapText(true);
                    Region spacerReserva = new Region();
                    HBox.setHgrow(spacerReserva, Priority.ALWAYS);
                    Label reservationPriceLabel = new Label(String.format("(R$ %.2f)", valorPagamento));
                    reservationPriceLabel.setStyle(styleText);
                    reservationRow.getChildren().addAll(reservationTextLabel, spacerReserva, reservationPriceLabel);
                    contentBox.getChildren().add(reservationRow);
                    totalReservas += valorPagamento;
                }

                Rectangle sublinhado = new Rectangle(325, 2);
                sublinhado.setFill(Color.web("#ddd"));

                Label subtotal = new Label(Tela.emFrances ? "Sous-total :" : "Subtotal:");
                subtotal.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-font-size: 17px;");
                Region spacerReservaSubtotal = new Region();
                HBox.setHgrow(spacerReservaSubtotal, Priority.ALWAYS);
                Label precoTotal = new Label(String.format("R$ %.2f", totalReservas));
                precoTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 17px; ");
                HBox subtotalPrecoBox = new HBox(subtotal, spacerReservaSubtotal, precoTotal);
                contentBox.getChildren().addAll(sublinhado, subtotalPrecoBox);
            }
            tempEm.close();
        }
    }

    private void updateContent2(String selectedItem, VBox contentBox, double subtotalReservas, double subtotalPedidos, double totalGeral) {
        contentBox.getChildren().clear();
        contentBox.setPadding(new Insets(20));
        contentBox.setSpacing(8);
        contentBox.setStyle("-fx-background-color: #FAFAFA; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");

        String styleBold = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + ";";
        String styleNormal = "-fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + ";";

        Label titulo = new Label(Tela.emFrances ? "Résumé du Paiement :" : "Resumo do Pagamento:");
        titulo.setStyle(styleBold);

        Label labelSubtotalReserva = new Label(String.format((Tela.emFrances ? "Sous-total de la Réservation : R$ %.2f" : "Subtotal da Reserva: R$ %.2f"), subtotalReservas));
        labelSubtotalReserva.setStyle(styleNormal);

        Label labelSubtotalPedido = new Label(String.format((Tela.emFrances ? "Sous-total des Commandes : R$ %.2f" : "Subtotal dos Pedidos: R$ %.2f"), subtotalPedidos));
        labelSubtotalPedido.setStyle(styleNormal);

        Label valorTotal = new Label(String.format((Tela.emFrances ? "Valeur totale : R$ %.2f" : "Valor total: R$ %.2f"), totalGeral));
        valorTotal.setStyle(styleBold);

        int numParcelas = Integer.parseInt(selectedItem.split("x")[0].trim());
        double valorPorParcela = (totalGeral > 0 && numParcelas > 0) ? totalGeral / numParcelas : 0;

        Label parcelas = new Label(String.format((Tela.emFrances ? "Échéances : %dx" : "Parcelas: %dx"), numParcelas));
        parcelas.setStyle(styleNormal);

        Label valorParcela = new Label(String.format((Tela.emFrances ? "Montant par échéance : R$ %.2f" : "Valor por parcela: R$ %.2f"), valorPorParcela));
        valorParcela.setStyle(styleBold);

        contentBox.getChildren().addAll(titulo, labelSubtotalReserva, labelSubtotalPedido, valorTotal, parcelas, valorParcela);

        if (numParcelas == 1) {
            Label pagamentoAVista = new Label(Tela.emFrances ? "✓ Paiement à vue" : "✓ Pagamento à vista");
            pagamentoAVista.setTextFill(Color.GREEN);
            pagamentoAVista.setFont(Font.font("System", 14));
            contentBox.getChildren().add(pagamentoAVista);
        }
    }

    private WebView criarWebview(String svgPath){
        WebView webView = new WebView();
        webView.setMinSize(10, 10);
        webView.setPrefSize(25, 25);
        webView.setMaxSize(30, 30);
        String svgUrl = getClass().getResource(svgPath).toExternalForm();
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" +
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" +
                "</body></html>";
        webView.getEngine().loadContent(html);
        return webView;
    }

    private VBox createCard(String svgPath, String titleText, String TipoCard, String borderColor, String textColor,
                            double subtotalReservas, double subtotalPedidos, double totalGeral) {

        WebView webView = criarWebview(svgPath);

        Label title = new Label(titleText);
        title.setTextFill(Color.web(textColor));
        title.setStyle("-fx-font-size: 25px; -fx-font-weight: 600;");
        HBox titulos = new HBox(7, webView, title);
        titulos.setAlignment(Pos.TOP_LEFT);
        HBox.setMargin(title, new Insets(-3, 0, 0, 0));


        if (TipoCard.equals("Informacoes")) {
            String inputStyle = "-fx-background-color: white;-fx-background-radius: 5px;-fx-border-color: #ddd;-fx-border-radius: 5px;-fx-border-width: 1px;-fx-font-size: 14px;";

            Label tipo = new Label(Tela.emFrances ? "Type :" : "Tipo:");
            tipo.setStyle("-fx-font-size: 14px; -fx-font-weight: 500;");

            ComboBox<String> types = new ComboBox<>();
            types.getItems().addAll(Tela.emFrances ? "Commandes" : "Pedidos", Tela.emFrances ? "Réservations" : "Reservas");
            types.setValue(Tela.emFrances ? "Commandes" : "Pedidos");
            types.setPrefHeight(40);
            types.setStyle(inputStyle);
            types.setPrefWidth(400);

            final String[] selectedItem = {types.getValue()};
            VBox dynamicContentVBox = new VBox(15);

            ScrollPane contentScrollPane = new ScrollPane(dynamicContentVBox);
            contentScrollPane.setFitToWidth(true);
            contentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            contentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            contentScrollPane.setFitToHeight(true);

            types.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectedItem[0] = newValue;
                    updateContent(selectedItem[0], dynamicContentVBox);
                }
            });

            VBox tipos = new VBox(10, tipo, types);
            updateContent(selectedItem[0], dynamicContentVBox);

            dynamicContentVBox.setStyle("-fx-background-color: white");
            contentScrollPane.setStyle("-fx-background-color: white");
            VBox vbox = new VBox(20, titulos, tipos, contentScrollPane);
            vbox.setAlignment(Pos.TOP_CENTER);
            vbox.setPadding(new Insets(40));
            vbox.setMinSize(420, 450);
            vbox.setPrefSize(420, 450);
            vbox.setMaxSize(420, 950);
            VBox.setVgrow(contentScrollPane, Priority.ALWAYS);

            String cardBackgroundColor = "White";
            String normalStyle = "-fx-border-color: " + borderColor + ";-fx-border-radius: 10;-fx-border-width: 2.0;-fx-background-radius: 10;-fx-background-color: " + cardBackgroundColor + ";";
            vbox.setStyle(normalStyle);

            return vbox;

        } else if (TipoCard.equals("Pagamento")) {
            String inputStyle = "-fx-background-color: white;-fx-background-radius: 5px;-fx-border-color: #ddd;-fx-border-radius: 5px;-fx-border-width: 1px;-fx-font-size: 14px;";

            Label tipo = new Label(Tela.emFrances ? "Paiement échelonné :" : "Parcelamento:");
            tipo.setStyle("-fx-font-size: 14px; -fx-font-weight: 500;");
            ComboBox<String> types = new ComboBox<>();

            types.getItems().addAll(
                    String.format("1x de R$ %.2f" + (Tela.emFrances ? " (paiement unique)" : " (à vista)"), totalGeral),
                    String.format("2x de R$ %.2f", totalGeral / 2),
                    String.format("3x de R$ %.2f", totalGeral / 3),
                    String.format("4x de R$ %.2f", totalGeral / 4),
                    String.format("5x de R$ %.2f", totalGeral / 5)
            );
            types.setValue(String.format("1x de R$ %.2f" + (Tela.emFrances ? " (paiement unique)" : " (à vista)"), totalGeral));

            types.setPrefHeight(40);
            types.setStyle(inputStyle);
            types.setPrefWidth(400);

            final String[] selectedItem = {types.getValue()};
            VBox contentBox = new VBox(10);

            types.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectedItem[0] = newValue;
                    updateContent2(selectedItem[0], contentBox, subtotalReservas, subtotalPedidos, totalGeral);
                }
            });

            VBox tipos = new VBox(10, tipo, types);
            updateContent2(selectedItem[0], contentBox, subtotalReservas, subtotalPedidos, totalGeral);

            Button finalizarPagamento = new Button((Tela.emFrances ? "Finaliser le Paiement" : "Finalizar Pagamento") + " 💳");
            finalizarPagamento.setPrefWidth(400);
            finalizarPagamento.setFont(Font.font("System", FontWeight.BOLD, 16));
            finalizarPagamento.setStyle(
                    "-fx-background-color: #0C1120;" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 15 50 15 50;" +
                            "-fx-background-radius: 10;" +
                            "-fx-cursor: hand;"
            );

            finalizarPagamento.setOnAction(e -> {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle(Tela.emFrances ? "Confirmation de Paiement" : "Confirmação de Pagamento");
                confirmAlert.setHeaderText(Tela.emFrances ? "Confirmer la finalisation du paiement ?" : "Confirmar finalização do pagamento?");
                confirmAlert.setContentText(String.format((Tela.emFrances ? "Le montant total de R$ %.2f sera traité." : "O valor total de R$ %.2f será processado."), totalGeral));

                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    EntityManager emTransacao = JpaUtil.getFactory().createEntityManager();
                    EntityTransaction transaction = null;
                    try {
                        transaction = emTransacao.getTransaction();
                        transaction.begin();
                        ContaBancariaJose contaJose;
                        try {
                            TypedQuery<ContaBancariaJose> queryConta = emTransacao.createQuery("SELECT c FROM ContaBancariaJose c", ContaBancariaJose.class);
                            queryConta.setMaxResults(1);
                            contaJose = queryConta.getSingleResult();
                        } catch (NoResultException ex) {
                            contaJose = new ContaBancariaJose();
                            emTransacao.persist(contaJose);
                        }

                        contaJose.setEntrada(contaJose.getEntrada() + (float) totalGeral);
                        contaJose.setSaldo(contaJose.getEntrada() - contaJose.getSaida());
                        emTransacao.merge(contaJose);

                        List<Pedido> pedidosDoUsuario = buscarPedidosPorEmailHQL(emTransacao);
                        for (Pedido pedido : pedidosDoUsuario) {
                            if (pedido.getItensPedido() != null) {
                                for (PedidoItem item : pedido.getItensPedido()) {
                                    Prato pratoGerenciado = emTransacao.find(Prato.class, item.getPrato().getId());
                                    if (pratoGerenciado != null) {
                                        for (int i = 0; i < item.getQuantidade(); i++) {
                                            pratoGerenciado.entregaPrato();
                                        }
                                        emTransacao.merge(pratoGerenciado);
                                    }
                                }
                            }
                            emTransacao.remove(emTransacao.contains(pedido) ? pedido : emTransacao.merge(pedido));
                        }

                        List<Reserva> reservasDoUsuario = buscarReservasPorEmailHQL(emTransacao);
                        for (Reserva reserva : reservasDoUsuario) {
                            emTransacao.remove(emTransacao.contains(reserva) ? reserva : emTransacao.merge(reserva));
                        }

                        transaction.commit();

                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle(Tela.emFrances ? "Paiement Terminé" : "Pagamento Concluído");
                        successAlert.setHeaderText(Tela.emFrances ? "Opération de Paiement Terminée avec Succès !" : "Operação de Pagamento Finalizada com Sucesso!");
                        successAlert.setContentText(Tela.emFrances ? "Les réservations et commandes ont été traitées et les montants ajoutés au compte." : "Reservas e pedidos foram processados e os valores adicionados à conta.");
                        successAlert.showAndWait();

                        new TelaInicial(getStage()).mostrarTela();

                    } catch (Exception ex) {
                        if (transaction != null && transaction.isActive()) transaction.rollback();
                        ex.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle(Tela.emFrances ? "Erreur" : "Erro");
                        errorAlert.setHeaderText(Tela.emFrances ? "Échec de la Finalisation du Paiement" : "Falha ao Finalizar Pagamento");
                        errorAlert.setContentText((Tela.emFrances ? "Une erreur est survenue lors du traitement du paiement. Veuillez réessayer.\nDétails: " : "Ocorreu um erro ao processar o pagamento. Por favor, tente novamente.\nDetalhes: ") + ex.getMessage());
                        errorAlert.showAndWait();
                    } finally {
                        if (emTransacao != null && emTransacao.isOpen()) emTransacao.close();
                    }
                }
            });

            Label mensagemSeguranca = new Label(Tela.emFrances ? "Paiement sécurisé • Vos données sont protégées" : "Pagamento seguro • Seus dados estão protegidos");
            mensagemSeguranca.setFont(Font.font("System", 13));
            mensagemSeguranca.setStyle("-fx-text-fill: #5E6D82;");

            VBox botao = new VBox(10, finalizarPagamento, mensagemSeguranca);
            botao.setAlignment(Pos.CENTER);

            VBox vbox = new VBox(20, titulos, tipos, contentBox, botao);
            vbox.setAlignment(Pos.TOP_CENTER);
            vbox.setPadding(new Insets(40));
            vbox.setPrefSize(420, 450);

            String cardBackgroundColor = "White";
            String normalStyle = "-fx-border-color: " + borderColor + ";-fx-border-radius: 10;-fx-border-width: 2.0;-fx-background-radius: 10;-fx-background-color: " + cardBackgroundColor + ";";
            vbox.setStyle(normalStyle);

            return vbox;
        }
        return new VBox();
    }

    @Override
    public Scene criarScene() {
        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)";
        StackPane rootPane = new StackPane();
        rootPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setStyle("-fx-background-color: transparent;");

        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Label titulo = new Label(Tela.emFrances ? "Finaliser le Paiement" : "Finalizar Pagamento");
        titulo.setFont(playfairFont);
        titulo.setStyle("-fx-text-fill: #FFC300;");

        double subtotalReservas = calcularSubtotalReservas();
        double subtotalPedidos = calcularSubtotalPedidos();
        double totalGeral = subtotalReservas + subtotalPedidos;

        String corBordaCard = "#E4E9F0";
        String corTextoCard = "black";

        tituloDesc = Tela.emFrances ? "Détails de la Commande" : "Detalhes do Pedido";
        tituloDesc2 = Tela.emFrances ? "Moyen de Paiement" : "Forma de Pagamento";

        VBox cardInfo = createCard("/svg/package-svgrepo-com.svg", tituloDesc, "Informacoes", corBordaCard, corTextoCard, 0.0, 0.0, 0.0);
        VBox cardPagamento = createCard("/svg/credit-card-svgrepo-com.svg", tituloDesc2, "Pagamento", corBordaCard, corTextoCard, subtotalReservas, subtotalPedidos, totalGeral);

        HBox cardsContainer = new HBox(20);
        cardsContainer.getChildren().addAll(cardInfo, cardPagamento);
        cardsContainer.setAlignment(Pos.CENTER);

        VBox contenedor = new VBox(25);
        contenedor.getChildren().addAll(titulo, cardsContainer);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setStyle("-fx-background-color: transparent;");
        contenedor.setPadding(new Insets(20));

        ScrollPane pane = new ScrollPane();
        pane.setContent(contenedor);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        pane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        layoutPrincipal.setCenter(pane);

        Label statusBar = new Label(Tela.emFrances ? "Prêt à finaliser votre achat. Vérifiez les détails et le moyen de paiement." : "Pronto para finalizar sua compra. Verifique os detalhes e a forma de pagamento.");
        statusBar.setPadding(new Insets(8, 10, 8, 10));
        statusBar.setMaxWidth(Double.MAX_VALUE);
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        statusBar.setStyle(
                "-fx-background-color: #5D0017;" +
                        "-fx-text-fill: #FFC300;" +
                        "-fx-border-color: #DAA520;" +
                        "-fx-border-width: 1 0 0 0;"
        );
        layoutPrincipal.setBottom(statusBar);

        rootPane.getChildren().add(layoutPrincipal);

        Runnable acaoVoltar;
        switch (this.origem) {
            case TELA_DELIVERY:
                acaoVoltar = () -> new TelaDelivery(getStage(), this.userEmail).mostrarTela();
                break;
            case TELA_RESERVA:
                acaoVoltar = () -> new TelaReserva(getStage()).mostrarTela();
                break;
            default:
                acaoVoltar = () -> new TelaInicial(getStage()).mostrarTela();
                break;
        }

        BotaoVoltar.criarEPosicionar(rootPane, acaoVoltar);
        return new Scene(rootPane, 1024, 768);
    }
}