package app;

import classes.Pedido;
import classes.Prato;
import classes.Cliente;
import classes.Pagamento;
import classes.Ingrediente;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaGerenciarDelivery extends Tela {

    private List<Pedido> listaDePedidos;
    private TableView<Pedido> tabelaPedidos;
    private ListView<String> detalhesListView;
    private Label clienteLabel, totalLabel, statusAtualLabel;
    private ComboBox<String> statusComboBox;
    private Button salvarStatusButton;
    private Map<Pedido, String> statusDosPedidos = new HashMap<>();

    private static final String DARK_BACKGROUND_COLOR = "#30000C"; // Alterado para combinar
    private static final String PANEL_BACKGROUND_COLOR = "#FAF0E6";
    private static final String ACCENT_COLOR_GOLD = "#DAA520";
    private static final String ACCENT_COLOR_DARK_GOLD = "#B8860B";
    private static final String TEXT_COLOR_ON_PANEL = "#3D2B1F";
    private static final String BORDER_COLOR_PANEL = "#C8A67B";
    private static final String BUTTON_TEXT_COLOR = "#FFFFFF";

    private static final Font FONT_TITLE = Font.font("Arial", FontWeight.BOLD, 24);
    private static final Font FONT_LABEL = Font.font("Arial", FontWeight.NORMAL, 14);
    private static final Font FONT_LABEL_BOLD = Font.font("Arial", FontWeight.BOLD, 14);
    private static final Font FONT_BUTTON = Font.font("Arial", FontWeight.BOLD, 14);

    public TelaGerenciarDelivery(Stage stage) {
        super(stage);
        this.listaDePedidos = criarPedidosFicticios();
        this.listaDePedidos.forEach(p -> statusDosPedidos.putIfAbsent(p, "Recebido"));
    }

    private List<Pedido> criarPedidosFicticios() {
        // ... (código existente sem alterações)
        List<Pedido> pedidos = new ArrayList<>();

        List<Ingrediente> ingredientesVazios = new ArrayList<>();

        Cliente cliente1 = new Cliente("João Silva", LocalDate.of(1990, 5, 15), "Rua A, 123", "senha123", "joao@email.com");
        Prato prato1 = new Prato(45.00f, ingredientesVazios, "Pizza de Calabresa", "Pizza com calabresa e queijo", 10);
        Prato prato2 = new Prato(10.00f, ingredientesVazios, "Coca-Cola 2L", "Refrigerante", 20);
        Pagamento pag1 = new Pagamento(65.00f, "Dinheiro", "Dinheiro", 1);
        Pedido pedido1 = new Pedido(pag1, new ArrayList<>(Arrays.asList(prato1, prato2)), new ArrayList<>(Arrays.asList(1, 2)), cliente1);
        pedidos.add(pedido1);

        Cliente cliente2 = new Cliente("Maria Oliveira", LocalDate.of(1988, 10, 20), "Av. B, 456", "senha456", "maria@email.com");
        Prato prato3 = new Prato(35.00f, ingredientesVazios, "Salada Caesar", "Salada com frango grelhado", 15);
        Pagamento pag2 = new Pagamento(35.00f, "Cartão de Crédito", "Crédito", 1);
        Pedido pedido2 = new Pedido(pag2, new ArrayList<>(Arrays.asList(prato3)), new ArrayList<>(Arrays.asList(1)), cliente2);
        pedidos.add(pedido2);

        return pedidos;
    }

    @Override
    public Scene criarScene() {
        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setPadding(new Insets(20));
        // O fundo será definido pelo StackPane

        Label titulo = new Label("Gerenciamento de Deliveries");
        titulo.setFont(FONT_TITLE);
        titulo.setTextFill(Color.web(ACCENT_COLOR_GOLD));
        titulo.setPadding(new Insets(0, 0, 20, 0));
        layoutPrincipal.setTop(titulo);

        tabelaPedidos = criarTabelaPedidos();
        layoutPrincipal.setCenter(tabelaPedidos);

        VBox painelDetalhes = criarPainelDetalhes();
        layoutPrincipal.setRight(painelDetalhes);
        BorderPane.setMargin(painelDetalhes, new Insets(0, 0, 0, 20));

        tabelaPedidos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) popularPainelDetalhes(newSelection);
                    else limparPainelDetalhes();
                });

        carregarPedidosNaTabela();

        // --- MUDANÇAS PARA ADICIONAR O BOTÃO VOLTAR ---
        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)";
        StackPane stackPane = new StackPane(layoutPrincipal);
        stackPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");
        // Ajusta o preenchimento no StackPane para dar espaço ao botão
        stackPane.setPadding(new Insets(20));

        Runnable acaoVoltar = () -> new TelaServicos(super.getStage()).mostrarTela();
        BotaoVoltar.criarEPosicionar(stackPane, acaoVoltar);
        // Remove o alinhamento do botão que está no StackPane, pois o criarEPosicionar já faz isso
        StackPane.setAlignment(layoutPrincipal, Pos.CENTER);


        Scene scene = new Scene(stackPane, 1280, 720);
        try {
            String css = getTableViewStylesheet();
            String dataUri = "data:text/css," + URLEncoder.encode(css, StandardCharsets.UTF_8.name());
            scene.getStylesheets().add(dataUri);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return scene;
    }

    // ... (Restante do código da classe TelaGerenciarDelivery permanece o mesmo)
    private TableView<Pedido> criarTabelaPedidos() {
        TableView<Pedido> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabela.setPlaceholder(new Label("Nenhum pedido para exibir."));

        TableColumn<Pedido, String> colunaCliente = new TableColumn<>("Cliente");
        colunaCliente.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getConsumidor() != null ?
                        cellData.getValue().getConsumidor().getNome() : "N/A")
        );
        colunaCliente.setPrefWidth(250);

        TableColumn<Pedido, Float> colunaTotal = new TableColumn<>("Valor Total");
        colunaTotal.setCellValueFactory(cellData ->
                new SimpleFloatProperty(cellData.getValue().getPagamento() != null ?
                        cellData.getValue().getPagamento().getPreco() : 0.0f).asObject()
        );
        colunaTotal.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("R$ %.2f", item));
                setAlignment(Pos.CENTER_LEFT);
            }
        });
        colunaTotal.setPrefWidth(150);

        TableColumn<Pedido, String> colunaStatus = new TableColumn<>("Status");
        colunaStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(statusDosPedidos.getOrDefault(cellData.getValue(), "N/A"))
        );
        colunaStatus.setPrefWidth(150);

        tabela.getColumns().addAll(colunaCliente, colunaTotal, colunaStatus);
        return tabela;
    }

    private VBox criarPainelDetalhes() {
        VBox painel = new VBox(15);
        painel.setPadding(new Insets(15));
        painel.setPrefWidth(400);
        painel.setStyle(
                "-fx-background-color: " + PANEL_BACKGROUND_COLOR + "; " +
                        "-fx-border-color: " + BORDER_COLOR_PANEL + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8;"
        );

        Label tituloDetalhes = new Label("Detalhes do Pedido");
        tituloDetalhes.setFont(FONT_TITLE);
        tituloDetalhes.setTextFill(Color.web(ACCENT_COLOR_DARK_GOLD));
        tituloDetalhes.setMaxWidth(Double.MAX_VALUE);
        tituloDetalhes.setAlignment(Pos.CENTER);

        VBox infoBox = new VBox(5);
        clienteLabel = new Label("Cliente: -");
        clienteLabel.setFont(FONT_LABEL);
        clienteLabel.setTextFill(Color.web(TEXT_COLOR_ON_PANEL));
        totalLabel = new Label("Valor Total: -");
        totalLabel.setFont(FONT_LABEL);
        totalLabel.setTextFill(Color.web(TEXT_COLOR_ON_PANEL));
        statusAtualLabel = new Label("Status Atual: -");
        statusAtualLabel.setFont(FONT_LABEL_BOLD);
        statusAtualLabel.setTextFill(Color.web(ACCENT_COLOR_DARK_GOLD));
        infoBox.getChildren().addAll(clienteLabel, totalLabel, statusAtualLabel);

        Label itensLabel = new Label("Itens do Pedido:");
        itensLabel.setFont(FONT_LABEL_BOLD);
        itensLabel.setTextFill(Color.web(TEXT_COLOR_ON_PANEL));

        detalhesListView = new ListView<>();
        detalhesListView.setStyle("-fx-background-color: transparent; -fx-control-inner-background: " + PANEL_BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR_PANEL + ";");
        VBox.setVgrow(detalhesListView, Priority.ALWAYS);


        Label mudarStatusLabel = new Label("Alterar Status para:");
        mudarStatusLabel.setFont(FONT_LABEL_BOLD);
        mudarStatusLabel.setTextFill(Color.web(TEXT_COLOR_ON_PANEL));

        statusComboBox = new ComboBox<>(FXCollections.observableArrayList("Recebido", "Em Preparo", "Saiu para Entrega", "Concluído", "Cancelado"));
        statusComboBox.setDisable(true);
        statusComboBox.setMaxWidth(Double.MAX_VALUE);

        salvarStatusButton = new Button("Salvar Status");
        salvarStatusButton.setDisable(true);
        salvarStatusButton.setMaxWidth(Double.MAX_VALUE);
        salvarStatusButton.setFont(FONT_BUTTON);
        salvarStatusButton.setStyle(
                "-fx-background-color: " + ACCENT_COLOR_GOLD + "; " +
                        "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 8 15;"
        );
        salvarStatusButton.setOnMouseEntered(e -> salvarStatusButton.setStyle("-fx-background-color: " + ACCENT_COLOR_DARK_GOLD + "; -fx-text-fill: " + BUTTON_TEXT_COLOR + "; -fx-background-radius: 5; -fx-padding: 8 15;"));
        salvarStatusButton.setOnMouseExited(e -> salvarStatusButton.setStyle("-fx-background-color: " + ACCENT_COLOR_GOLD + "; -fx-text-fill: " + BUTTON_TEXT_COLOR + "; -fx-background-radius: 5; -fx-padding: 8 15;"));
        salvarStatusButton.setOnAction(e -> salvarNovoStatus());

        painel.getChildren().addAll(tituloDetalhes, infoBox, new Separator(), itensLabel, detalhesListView, new Separator(), mudarStatusLabel, statusComboBox, salvarStatusButton);

        return painel;
    }

    private void popularPainelDetalhes(Pedido pedido) {
        clienteLabel.setText("Cliente: " + (pedido.getConsumidor() != null ? pedido.getConsumidor().getNome() : "N/A"));
        totalLabel.setText("Valor Total: " + (pedido.getPagamento() != null ? String.format("R$ %.2f", pedido.getPagamento().getPreco()) : "R$ 0,00"));

        String statusAtual = statusDosPedidos.get(pedido);
        statusAtualLabel.setText("Status Atual: " + statusAtual);
        statusComboBox.setValue(statusAtual);

        detalhesListView.getItems().clear();
        if (pedido.getPratos() != null && pedido.getQuantidades() != null) {
            for (int i = 0; i < pedido.getPratos().size(); i++) {
                Prato prato = pedido.getPratos().get(i);
                Integer quantidade = pedido.getQuantidades().get(i);
                detalhesListView.getItems().add(String.format("%dx %s (R$ %.2f)", quantidade, prato.getNome(), prato.getPreco()));
            }
        }

        statusComboBox.setDisable(false);
        salvarStatusButton.setDisable(false);
    }

    private void limparPainelDetalhes() {
        clienteLabel.setText("Cliente: -");
        totalLabel.setText("Valor Total: -");
        statusAtualLabel.setText("Status Atual: -");
        detalhesListView.getItems().clear();
        statusComboBox.setValue(null);
        statusComboBox.setDisable(true);
        salvarStatusButton.setDisable(true);
    }

    private void carregarPedidosNaTabela() {
        ObservableList<Pedido> pedidosObservable = FXCollections.observableArrayList(this.listaDePedidos);
        tabelaPedidos.setItems(pedidosObservable);
    }

    private void salvarNovoStatus() {
        Pedido pedidoSelecionado = tabelaPedidos.getSelectionModel().getSelectedItem();
        String novoStatus = statusComboBox.getValue();

        if (pedidoSelecionado != null && novoStatus != null && !novoStatus.isEmpty()) {
            statusDosPedidos.put(pedidoSelecionado, novoStatus);
            statusAtualLabel.setText("Status Atual: " + novoStatus);
            tabelaPedidos.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Status do pedido de " + pedidoSelecionado.getConsumidor().getNome() + " atualizado para '" + novoStatus + "'!");
            alert.setHeaderText("Status Atualizado");
            styleAlertDialog(alert);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um pedido e um novo status para salvar.");
            alert.setHeaderText("Nenhuma Alteração Detectada");
            styleAlertDialog(alert);
            alert.showAndWait();
        }
    }

    private String getTableViewStylesheet() {
        return " .table-view { " +
                "    -fx-background-color: " + PANEL_BACKGROUND_COLOR + "; " +
                "    -fx-control-inner-background: " + PANEL_BACKGROUND_COLOR + "; " +
                "    -fx-border-color: " + BORDER_COLOR_PANEL + "; " +
                "    -fx-border-width: 1px; " +
                "} " +
                " .table-view .column-header-background { " +
                "    -fx-background-color: " + darkenSlightly(PANEL_BACKGROUND_COLOR, 0.1) + "; " +
                "    -fx-background-insets: 0, 0 1 0 1; " +
                "} " +
                " .table-view .column-header { " +
                "    -fx-background-color: transparent; " +
                "    -fx-padding: 12px; " +
                "} " +
                " .table-view .column-header .label { " +
                "    -fx-text-fill: " + ACCENT_COLOR_DARK_GOLD + "; " +
                "    -fx-font-family: 'Arial'; " +
                "    -fx-font-weight: bold; " +
                "    -fx-font-size: 14px; " +
                "} " +
                " .table-view .table-row-cell { " +
                "    -fx-background-color: -fx-control-inner-background; " +
                "    -fx-border-width: 0 0 1px 0; " +
                "    -fx-border-color: " + darkenSlightly(BORDER_COLOR_PANEL, 0.2) + "; " +
                "    -fx-padding: 0; " +
                "} " +
                " .table-view .table-row-cell:odd { " +
                "    -fx-background-color: " + darkenSlightly(PANEL_BACKGROUND_COLOR, -0.02) + "; " +
                "} " +
                " .table-view .table-row-cell:hover { " +
                "    -fx-background-color: " + darkenSlightly(PANEL_BACKGROUND_COLOR, -0.05) + "; " +
                "} " +
                " .table-view .table-row-cell:selected { " +
                "    -fx-background-color: " + ACCENT_COLOR_DARK_GOLD + "; " +
                "} " +
                " .table-view .table-row-cell:selected .text { " +
                "    -fx-fill: white; " +
                "} " +
                " .table-view .table-cell { " +
                "    -fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; " +
                "    -fx-font-family: 'Arial'; " +
                "    -fx-font-size: 13px; " +
                "    -fx-padding: 14px 8px; " +
                "}";
    }

    private String darkenSlightly(String hexColor, double factor) {
        Color color = Color.web(hexColor);
        if (factor > 0) {
            for (int i = 0; i < (int)(factor * 10); i++) color = color.darker();
        } else {
            for (int i = 0; i < (int)(-factor * 10); i++) color = color.brighter();
        }
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    private void styleAlertDialog(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: " + PANEL_BACKGROUND_COLOR + ";" +
                        "-fx-border-color: " + BORDER_COLOR_PANEL + "; -fx-border-width: 1;"
        );
        Node headerPanel = dialogPane.lookup(".header-panel");
        if (headerPanel != null) {
            headerPanel.setStyle("-fx-background-color: " + darkenSlightly(PANEL_BACKGROUND_COLOR, 0.05) + "; -fx-padding: 10px;");
            Node headerLabel = headerPanel.lookup(".label");
            if (headerLabel instanceof Label) {
                ((Label)headerLabel).setStyle("-fx-text-fill: " + ACCENT_COLOR_DARK_GOLD + "; -fx-font-size: 16px; -fx-font-weight: bold;");
            }
        }
        Node content = dialogPane.getContent();
        if (content instanceof Label) {
            ((Label)content).setStyle("-fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-font-size: 14px; -fx-wrap-text: true;");
            ((Label)content).setPadding(new Insets(10));
        }
        dialogPane.getButtonTypes().stream().map(dialogPane::lookupButton).forEach(buttonNode -> {
            if (buttonNode instanceof Button) {
                Button button = (Button) buttonNode;
                button.setFont(FONT_BUTTON);
                button.setStyle("-fx-background-color: " + ACCENT_COLOR_GOLD + "; -fx-text-fill: " + BUTTON_TEXT_COLOR + "; -fx-background-radius: 4; -fx-padding: 8 15;");
                button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + ACCENT_COLOR_DARK_GOLD + "; -fx-text-fill: " + BUTTON_TEXT_COLOR + "; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 8 15;"));
                button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + ACCENT_COLOR_GOLD + "; -fx-text-fill: " + BUTTON_TEXT_COLOR + "; -fx-font-weight: bold; -fx-background-radius: 4; -fx-padding: 8 15;"));
            }
        });
    }
}