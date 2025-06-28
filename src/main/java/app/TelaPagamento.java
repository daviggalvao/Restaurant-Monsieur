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
import javafx.scene.text.FontWeight;
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
        this.origem = origem; // Armazena a origem recebida
    }

    /**
     * Busca todas as reservas associadas a um email de cliente específico.
     * @param em O EntityManager para interagir com o banco de dados.
     * @return Uma lista de objetos Reserva.
     */
    public List<Reserva> buscarReservasPorEmailHQL(EntityManager em) {
        TypedQuery<Reserva> query = em.createQuery("FROM Reserva r WHERE r.cliente.email = :email ", Reserva.class);
        query.setParameter("email", this.userEmail);
        return query.getResultList();
    }

    /**
     * Busca todos os pedidos associados a um email de consumidor específico.
     * @param em O EntityManager para interagir com o banco de dados.
     * @return Uma lista de objetos Pedido.
     */
    public List<Pedido> buscarPedidosPorEmailHQL(EntityManager em) {
        // Assume que a classe Pedido tem um campo 'consumidor' que é um Cliente
        // e que Cliente tem um campo 'email'.
        TypedQuery<Pedido> query = em.createQuery("FROM Pedido p WHERE p.consumidor.email = :email ", Pedido.class);
        query.setParameter("email", this.userEmail);
        return query.getResultList();
    }

    /**
     * Calcula o subtotal de todas as reservas do usuário.
     * @return O valor total das reservas.
     */
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
                total += p.getPagamento().getPreco();
            }
            return total;
        } finally {
            em.close();
        }
    }

    /**
     * Atualiza o conteúdo da caixa de informações (Pedidos ou Reservas).
     * @param selectedItem O tipo de item selecionado (Pedidos ou Reservas).
     * @param contentBox A VBox onde o conteúdo será exibido.
     */
    private void updateContent(String selectedItem, VBox contentBox) {
        contentBox.getChildren().clear(); // Limpa o conteúdo anterior
        String styleText = "-fx-font-size: 13px; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + ";";
        String styleTextBold = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + ";";

        if (selectedItem.equals("Pedidos")) {
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
                    pedidoHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + ";");
                    contentBox.getChildren().add(pedidoHeader);

                    // Adicionar os itens do pedido
                    if (p.getItensPedido() != null && !p.getItensPedido().isEmpty()) {
                        for (classes.PedidoItem item : p.getItensPedido()) {
                            if (item.getPrato() != null) {
                                double precoTotalItem = item.getQuantidade() * item.getPrato().getPreco();
                                String itemText = String.format("%s x%d", item.getPrato().getNome(), item.getQuantidade());
                                String precoItemText = String.format("R$ %.2f", precoTotalItem);

                                HBox itemRow = new HBox();
                                Label itemLabel = new Label(itemText);
                                itemLabel.setStyle(styleText);
                                itemLabel.setWrapText(true);

                                Region spacer = new Region();
                                HBox.setHgrow(spacer, Priority.ALWAYS);

                                Label precoLabel = new Label(precoItemText);
                                precoLabel.setStyle(styleText);

                                itemRow.getChildren().addAll(itemLabel, spacer, precoLabel);
                                itemRow.setAlignment(Pos.BASELINE_LEFT);
                                contentBox.getChildren().add(itemRow);
                            }
                        }
                    } else {
                        Label noItemsLabel = new Label(" (Sem itens no pedido)");
                        noItemsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
                        contentBox.getChildren().add(noItemsLabel);
                    }

                    // Adicionar taxa de entrega específica do pedido
                    HBox taxaEntregaRow = new HBox();
                    Label taxaEntregaTextLabel = new Label("Taxa de entrega");
                    taxaEntregaTextLabel.setStyle(styleText);
                    taxaEntregaTextLabel.setWrapText(true);

                    Region spacerTaxa = new Region();
                    HBox.setHgrow(spacerTaxa, Priority.ALWAYS);

                    Label taxaEntregaPrecoLabel = new Label(String.format("R$ %.2f", (double) p.getFrete()));
                    taxaEntregaPrecoLabel.setStyle(styleText);

                    taxaEntregaRow.getChildren().addAll(taxaEntregaTextLabel, spacerTaxa, taxaEntregaPrecoLabel);
                    taxaEntregaRow.setAlignment(Pos.BASELINE_LEFT);
                    contentBox.getChildren().add(taxaEntregaRow);

                    Rectangle separator = new Rectangle(325, 1);
                    separator.setFill(Color.web("#eee"));
                    VBox.setMargin(separator, new Insets(10, 0, 10, 0));
                    contentBox.getChildren().add(separator);

                    if(p.getPagamento().getTipo().split(" ")[0].equalsIgnoreCase("Pix")){
                        totalPedidosCalculado += (p.calcularPrecoTotal())*0.9f;
;                    }else{totalPedidosCalculado += p.calcularPrecoTotal();}
                }

                Rectangle sublinhado = new Rectangle(325, 2);
                sublinhado.setFill(Color.web("#ddd"));
                contentBox.getChildren().add(sublinhado);

                Label subtotalGeralLabel = new Label("Subtotal Geral:");
                subtotalGeralLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-font-size: 17px;");
                subtotalGeralLabel.setAlignment(Pos.CENTER_LEFT);

                Region spacerTotalGeral = new Region();
                HBox.setHgrow(spacerTotalGeral, Priority.ALWAYS);

                Label precoTotalGeral = new Label(String.format("R$ %.2f", totalPedidosCalculado));
                precoTotalGeral.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 17px; ");
                precoTotalGeral.setAlignment(Pos.CENTER_RIGHT);

                HBox subtotalPrecoBox = new HBox();
                subtotalPrecoBox.setAlignment(Pos.CENTER_LEFT);
                subtotalPrecoBox.getChildren().addAll(subtotalGeralLabel, spacerTotalGeral, precoTotalGeral);
                VBox.setMargin(subtotalPrecoBox, new Insets(0, 0, 0, 15));
                contentBox.getChildren().add(subtotalPrecoBox);
            }

            contentBox.setAlignment(Pos.BASELINE_LEFT);
            VBox.setMargin(contentBox, new Insets(0, 0, 0, 0));
            tempEm.close();
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
                    // String nomePagamento = "N/A"; // Variável não utilizada

                    // Verificação de nulo para Pagamento
                    if (r.getPagamento() != null) {
                        valorPagamento = r.getPagamento().getPreco();
                        // nomePagamento = r.getPagamento().getNome(); // Assumindo que Pagamento tem getNome(), mas não é usado
                    }

                    HBox reservationRow = new HBox();
                    Label reservationTextLabel = new Label(
                            String.format(" - Reserva para %s às %s do dia %s-%s",
                                    nomeCliente, r.getHorario(), dia, mes)
                    );
                    reservationTextLabel.setStyle(styleText);
                    reservationTextLabel.setWrapText(true);

                    Region spacerReserva = new Region();
                    HBox.setHgrow(spacerReserva, Priority.ALWAYS);

                    Label reservationPriceLabel = new Label(String.format("(R$ %.2f)", valorPagamento));
                    reservationPriceLabel.setStyle(styleText);

                    reservationRow.getChildren().addAll(reservationTextLabel, spacerReserva, reservationPriceLabel);
                    reservationRow.setAlignment(Pos.BASELINE_LEFT);
                    contentBox.getChildren().add(reservationRow);

                    totalReservas += valorPagamento;
                }

                Rectangle sublinhado = new Rectangle(325, 2);
                sublinhado.setFill(Color.web("#ddd"));


                Label subtotal = new Label("Subtotal:");
                subtotal.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-font-size: 17px;");
                subtotal.setAlignment(Pos.CENTER_LEFT);

                Region spacerReservaSubtotal = new Region();
                HBox.setHgrow(spacerReservaSubtotal, Priority.ALWAYS);

                Label precoTotal = new Label(String.format("R$ %.2f", totalReservas));
                precoTotal.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-font-size: 17px; ");
                precoTotal.setAlignment(Pos.CENTER_RIGHT);

                HBox subtotalPrecoBox = new HBox();
                subtotalPrecoBox.setAlignment(Pos.CENTER_LEFT);
                subtotalPrecoBox.getChildren().addAll(subtotal, spacerReservaSubtotal, precoTotal);
                VBox.setMargin(subtotalPrecoBox, new Insets(0, 0, 0, 15));
                contentBox.getChildren().addAll(sublinhado, subtotalPrecoBox);
            }

            contentBox.setAlignment(Pos.BASELINE_LEFT);
            VBox.setMargin(contentBox, new Insets(0, 0, 0, 0));
            tempEm.close();
        }
    }

    /**
     * Atualiza o conteúdo do resumo de pagamento com base no item selecionado da ComboBox
     * e nos valores totais de reserva e pedido.
     * @param selectedItem A opção de parcelamento selecionada.
     * @param contentBox A VBox onde o resumo de pagamento será exibido.
     * @param subtotalReservas O subtotal das reservas.
     * @param subtotalPedidos O subtotal dos pedidos.
     * @param totalGeral O valor total a ser pago (reservas + pedidos).
     */
    private void updateContent2(String selectedItem, VBox contentBox, double subtotalReservas, double subtotalPedidos, double totalGeral) {
        contentBox.getChildren().clear();
        contentBox.setPadding(new Insets(20));
        contentBox.setSpacing(8); // Adiciona um espaçamento para ficar mais legível
        contentBox.setStyle("-fx-background-color: #FAFAFA; -fx-border-color: #E0E0E0; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Estilos para os textos
        String styleBold = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + ";";
        String styleNormal = "-fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + ";";

        Label titulo = new Label("Resumo do Pagamento:");
        titulo.setStyle(styleBold);

        Label labelSubtotalReserva = new Label(String.format("Subtotal da Reserva: R$ %.2f", subtotalReservas));
        labelSubtotalReserva.setStyle(styleNormal);

        Label labelSubtotalPedido = new Label(String.format("Subtotal dos Pedidos: R$ %.2f", subtotalPedidos));
        labelSubtotalPedido.setStyle(styleNormal);

        Label valorTotal = new Label(String.format("Valor total: R$ %.2f", totalGeral));
        valorTotal.setStyle(styleBold); // Mantido como negrito para destaque

        String[] partesParcelamento = selectedItem.split("x de ");
        int numParcelas = Integer.parseInt(partesParcelamento[0].trim());
        double valorPorParcela = (totalGeral > 0 && numParcelas > 0) ? totalGeral / numParcelas : 0;

        Label parcelas = new Label(String.format("Parcelas: %dx", numParcelas));
        parcelas.setStyle(styleNormal);

        Label valorParcela = new Label(String.format("Valor por parcela: R$ %.2f", valorPorParcela));
        valorParcela.setStyle(styleBold);

        contentBox.getChildren().addAll(titulo, labelSubtotalReserva, labelSubtotalPedido, valorTotal, parcelas, valorParcela);

        if (numParcelas == 1) {
            Label pagamentoAVista = new Label("✓ Pagamento à vista");
            pagamentoAVista.setTextFill(Color.GREEN); // Cor verde já está boa
            pagamentoAVista.setFont(Font.font("System", 14));
            contentBox.getChildren().add(pagamentoAVista);
        }
    }


    /**
     * Cria um card visual para exibição de informações ou pagamento.
     * @param svgPath O caminho para o arquivo SVG do ícone.
     * @param titleText O texto do título do card.
     * @param TipoCard O tipo de card ("Informacoes" ou "Pagamento").
     * @param borderColor A cor da borda do card.
     * @param textColor A cor do texto do título.
     * @param subtotalReservas O subtotal das reservas (usado apenas para o card de Pagamento).
     * @param subtotalPedidos O subtotal dos pedidos (usado apenas para o card de Pagamento).
     * @param totalGeral O valor total (usado apenas para o card de Pagamento).
     * @return Uma VBox representando o card.
     */
    private VBox createCard(String svgPath, String titleText, String TipoCard, String borderColor, String textColor,
                            double subtotalReservas, double subtotalPedidos, double totalGeral) {
        // Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 25); // Não está sendo usado
        // Font interfont = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 12); // Não está sendo usado

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
                    " -fx-background-radius: 5px;\n" +
                    " -fx-border-color: #ddd;\n" +
                    " -fx-border-radius: 5px;\n" +
                    " -fx-border-width: 1px;\n" +
                    " -fx-font-size: 14px;\n";

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
            updateContent(selectedItem[0], dynamicContentVBox); // Chamada inicial

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
            String normalStyle = "-fx-border-color: " + borderColor + ";" +
                    "-fx-border-radius: 10;" +
                    "-fx-border-width: 2.0;" +
                    "-fx-background-radius: 10;" +
                    "-fx-background-color: " + cardBackgroundColor + ";";
            vbox.setStyle(normalStyle);

            return vbox;

        } else if (TipoCard.equals("Pagamento")) {
            String inputStyle = "-fx-background-color: white;\n" +
                    " -fx-background-radius: 5px;\n" +
                    " -fx-border-color: #ddd;\n" +
                    " -fx-border-radius: 5px;\n" +
                    " -fx-border-width: 1px;\n" +
                    " -fx-font-size: 14px;\n";

            Label tipo = new Label("Parcelamento:");
            tipo.setStyle("-fx-font-size: 14px; -fx-font-weight: 500;");
            ComboBox<String> types = new ComboBox<String>();

            // Adiciona as opções de parcelamento dinamicamente com base no totalGeral
            types.getItems().addAll(
                    String.format("1x de R$ %.2f (à vista)", totalGeral),
                    String.format("2x de R$ %.2f", totalGeral / 2),
                    String.format("3x de R$ %.2f", totalGeral / 3),
                    String.format("4x de R$ %.2f", totalGeral / 4),
                    String.format("5x de R$ %.2f", totalGeral / 5)
            );
            types.setValue(String.format("1x de R$ %.2f (à vista)", totalGeral)); // Define o valor inicial

            types.setPrefHeight(40);
            types.setStyle(inputStyle);
            types.setPrefWidth(400);

            final String[] selectedItem = {types.getValue()}; // Inicializa com o valor atual da ComboBox
            VBox contentBox = new VBox(10);

            types.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectedItem[0] = newValue;
                    // Passa os valores de subtotal e total geral para updateContent2
                    updateContent2(selectedItem[0], contentBox, subtotalReservas, subtotalPedidos, totalGeral);
                }
            });

            VBox tipos = new VBox(10, tipo, types);
            // Chamada inicial para preencher o resumo de pagamento
            updateContent2(selectedItem[0], contentBox, subtotalReservas, subtotalPedidos, totalGeral);

            Button finalizarPagamento = new Button("Finalizar Pagamento 💳");
            finalizarPagamento.setPrefWidth(400);
            finalizarPagamento.setFont(Font.font("System", FontWeight.BOLD, 16));
            finalizarPagamento.setStyle(
                    "-fx-background-color: #0C1120;" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 15 50 15 50;" +
                            "-fx-background-radius: 10;" +
                            "-fx-cursor: hand;"
            );

            // --- Lógica para o botão Finalizar Pagamento ---
            finalizarPagamento.setOnAction(e -> {
                // Confirmação com o usuário
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmação de Pagamento");
                confirmAlert.setHeaderText("Confirmar finalização do pagamento?");
                confirmAlert.setContentText(String.format("O valor total de R$ %.2f será processado.", totalGeral));

                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    EntityManager emTransacao = JpaUtil.getFactory().createEntityManager();
                    EntityTransaction transaction = null;
                    try {
                        transaction = emTransacao.getTransaction();
                        transaction.begin();

                        // 1. Adicionar o valor total à conta do Sr. José
                        ContaBancariaJose contaJose;
                        try {
                            // Tenta buscar a única instância de ContaBancariaJose (assumindo que existe apenas uma)
                            TypedQuery<ContaBancariaJose> queryConta = emTransacao.createQuery("SELECT c FROM ContaBancariaJose c", ContaBancariaJose.class);
                            queryConta.setMaxResults(1); // Limita a 1 resultado
                            contaJose = queryConta.getSingleResult();
                        } catch (NoResultException ex) {
                            // Se não houver resultado, cria uma nova instância
                            contaJose = new ContaBancariaJose();
                            emTransacao.persist(contaJose);
                            emTransacao.flush();
                        }

                        // Atualiza os valores estáticos (que a JPA pode ou não persistir diretamente
                        // dependendo do setup, mas faremos para manter a lógica do código existente)
                        contaJose.setEntrada(contaJose.getEntrada() + (float) totalGeral);
                        // Supondo que o saldo é entrada - saida
                        contaJose.setSaldo(contaJose.getEntrada() - contaJose.getSaida());

                        // Para garantir que a entidade seja sincronizada com o banco, mesmo com campos estáticos
                        // se a JPA estiver monitorando o objeto 'contaJose'
                        emTransacao.merge(contaJose);


                        // 2. Processar Pedidos: entregar pratos e remover pedidos
                        List<Pedido> pedidosDoUsuario = buscarPedidosPorEmailHQL(emTransacao);
                        for (Pedido pedido : pedidosDoUsuario) {
                            if (pedido.getItensPedido() != null) {
                                for (PedidoItem item : pedido.getItensPedido()) {
                                    // Pega o Prato do PedidoItem. Precisa ser um Prato gerenciado pelo EntityManager atual.
                                    Prato pratoGerenciado = emTransacao.find(Prato.class, item.getPrato().getId());
                                    if (pratoGerenciado != null) {
                                        // Chama entregaPrato para cada prato na quantidade do pedido
                                        // O loop deve ser para cada unidade do prato
                                        for (int i = 0; i < item.getQuantidade(); i++) {
                                            pratoGerenciado.entregaPrato(); // Atualiza entrada e decrementa quantidade no prato
                                        }
                                        emTransacao.merge(pratoGerenciado); // Sincroniza o prato atualizado com o banco
                                    }
                                }
                            }
                            // Remove o pedido do banco de dados (e itens, se houver cascade)
                            emTransacao.remove(pedido);
                        }

                        // 3. Remover Reservas pagas
                        List<Reserva> reservasDoUsuario = buscarReservasPorEmailHQL(emTransacao);
                        for (Reserva reserva : reservasDoUsuario) {
                            // Assume que todas as reservas buscadas aqui são consideradas "pagas" e devem ser removidas
                            emTransacao.remove(reserva);
                        }

                        transaction.commit();

                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Pagamento Concluído");
                        successAlert.setHeaderText("Operação de Pagamento Finalizada com Sucesso!");
                        successAlert.setContentText("Reservas e pedidos foram processados e os valores adicionados à conta.");
                        successAlert.showAndWait();

                        // Após a finalização, você pode querer voltar para uma tela inicial
                        new TelaInicial(getStage()).mostrarTela();

                    } catch (Exception ex) {
                        if (transaction != null && transaction.isActive()) {
                            transaction.rollback();
                        }
                        System.err.println("Erro ao finalizar pagamento: " + ex.getMessage());
                        ex.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Erro");
                        errorAlert.setHeaderText("Falha ao Finalizar Pagamento");
                        errorAlert.setContentText("Ocorreu um erro ao processar o pagamento. Por favor, tente novamente.\nDetalhes: " + ex.getMessage());
                        errorAlert.showAndWait();
                    } finally {
                        if (emTransacao != null && emTransacao.isOpen()) {
                            emTransacao.close();
                        }
                    }
                }
            });

            Label mensagemSeguranca = new Label("Pagamento seguro • Seus dados estão protegidos");
            mensagemSeguranca.setFont(Font.font("System", 13));
            mensagemSeguranca.setStyle("-fx-text-fill: #5E6D82;");
            mensagemSeguranca.setAlignment(Pos.CENTER);

            VBox botao = new VBox(10);
            botao.getChildren().addAll(finalizarPagamento, mensagemSeguranca);
            botao.setAlignment(Pos.CENTER);

            VBox vbox = new VBox(20, titulos, tipos, contentBox, botao);
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

    @Override
    public Scene criarScene() {
        // --- 1. CONFIGURAÇÃO DA ESTRUTURA PRINCIPAL E ESTILO DE FUNDO ---
        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)";

        // O StackPane será o container raiz para aplicar o fundo e o botão de voltar
        StackPane rootPane = new StackPane();
        rootPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        // Usaremos um BorderPane para organizar o conteúdo principal e a barra de status
        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setStyle("-fx-background-color: transparent;"); // Fundo transparente para ver o StackPane

        // --- 2. CRIAÇÃO DO TÍTULO, CARDS E CONTEÚDO CENTRAL ---
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Label titulo = new Label("Finalizar Pagamento");
        titulo.setFont(playfairFont);
        titulo.setStyle("-fx-text-fill: #FFC300;");

        double subtotalReservas = calcularSubtotalReservas();
        double subtotalPedidos = calcularSubtotalPedidos();
        double totalGeral = subtotalReservas + subtotalPedidos;

        String corBordaCard = "#E4E9F0";
        String corTextoCard = "black";
        tituloDesc = "Detalhes do Pedido";
        tituloDesc2 = "Forma de Pagamento";

        VBox cardInfo = createCard("/svg/package-svgrepo-com.svg", tituloDesc, "Informacoes", corBordaCard, corTextoCard, 0.0, 0.0, 0.0);
        VBox cardPagamento = createCard("/svg/credit-card-svgrepo-com.svg", tituloDesc2, "Pagamento", corBordaCard, corTextoCard, subtotalReservas, subtotalPedidos, totalGeral);

        HBox cardsContainer = new HBox(20);
        cardsContainer.getChildren().addAll(cardInfo, cardPagamento);
        cardsContainer.setAlignment(Pos.CENTER);

        VBox contenedor = new VBox(25);
        contenedor.getChildren().addAll(titulo, cardsContainer);
        contenedor.setAlignment(Pos.CENTER);
        // O contenedor principal agora é transparente
        contenedor.setStyle("-fx-background-color: transparent;");
        // Adicionamos um padding para não colar nas bordas
        contenedor.setPadding(new Insets(20));

        ScrollPane pane = new ScrollPane();
        pane.setContent(contenedor);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        // O ScrollPane também precisa ser transparente
        pane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // O ScrollPane com o conteúdo vai no centro do BorderPane
        layoutPrincipal.setCenter(pane);


        // --- 3. CRIAÇÃO DA BARRA DE STATUS INFERIOR (SEEMLHANTE À TELADELIVERY) ---
        Label statusBar = new Label("Pronto para finalizar sua compra. Verifique os detalhes e a forma de pagamento.");
        statusBar.setPadding(new Insets(8, 10, 8, 10));
        statusBar.setMaxWidth(Double.MAX_VALUE);
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setFont(Font.font("Arial", FontWeight.NORMAL, 14)); // Usando fonte padrão, similar à FONT_TEXT_NORMAL
        statusBar.setStyle(
                "-fx-background-color: #5D0017;" + // Cor sólida que combina com o gradiente
                        "-fx-text-fill: #FFC300;" +
                        "-fx-border-color: #DAA520;" + // Cor de destaque dourada
                        "-fx-border-width: 1 0 0 0;" // Borda apenas no topo
        );

        // A barra de status vai na parte de baixo do BorderPane
        layoutPrincipal.setBottom(statusBar);

        rootPane.getChildren().add(layoutPrincipal); // Adiciona o layout principal ao root

        // 3. LÓGICA DINÂMICA PARA A AÇÃO DE VOLTAR
        Runnable acaoVoltar;
        switch (this.origem) {
            case TELA_DELIVERY:
                acaoVoltar = () -> new TelaDelivery(getStage(), this.userEmail).mostrarTela();
                break;
            case TELA_RESERVA:
                acaoVoltar = () -> new TelaReserva(getStage()).mostrarTela();
                break;
            default:
                // Fallback para a tela inicial, caso a origem seja desconhecida
                acaoVoltar = () -> new TelaInicial(getStage()).mostrarTela();
                break;
        }

        // Cria e posiciona o botão de voltar com a ação dinâmica
        BotaoVoltar.criarEPosicionar(rootPane, acaoVoltar);

        Scene scene = new Scene(rootPane, 1024, 768); // Ajustado o tamanho da cena que estava faltando
        return scene;
    }

}