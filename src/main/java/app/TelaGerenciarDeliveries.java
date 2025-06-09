package app;

import classes.Pedido;
import classes.Prato;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaGerenciarDeliveries {

    private Stage stage;
    private List<Pedido> listaDePedidos; // Recebe os pedidos prontos
    private TableView<Pedido> tabelaPedidos;
    private ListView<String> detalhesListView;
    private Label clienteLabel, totalLabel, statusAtualLabel;
    private ComboBox<String> statusComboBox;
    private Button salvarStatusButton;

    // Mapa para armazenar o status de cada pedido, já que a classe Pedido original não tem esse campo.
    private Map<Pedido, String> statusDosPedidos = new HashMap<>();

    // --- BEGIN STYLE CONSTANTS (Mesma paleta da tela de Delivery) ---
    private static final String DARK_BACKGROUND_COLOR = "#4B3832";
    private static final String PANEL_BACKGROUND_COLOR = "#FAF0E6";
    private static final String ACCENT_COLOR_GOLD = "#DAA520";
    private static final String ACCENT_COLOR_DARK_GOLD = "#B8860B";
    private static final String TEXT_COLOR_ON_PANEL = "#3D2B1F";
    private static final String TEXT_COLOR_LIGHT = "#F5F5F5";
    private static final String BORDER_COLOR_PANEL = "#C8A67B";
    private static final String BUTTON_TEXT_COLOR = "#FFFFFF";

    private static final Font FONT_TITLE = Font.font("Arial", FontWeight.BOLD, 24);
    private static final Font FONT_SUBTITLE = Font.font("Arial", FontWeight.BOLD, 18);
    private static final Font FONT_LABEL = Font.font("Arial", FontWeight.NORMAL, 14);
    private static final Font FONT_LABEL_BOLD = Font.font("Arial", FontWeight.BOLD, 14);
    private static final Font FONT_BUTTON = Font.font("Arial", FontWeight.BOLD, 14);
    // --- END STYLE CONSTANTS ---


    public TelaGerenciarDeliveries(Stage stage, List<Pedido> pedidosConcluidos) {
        this.stage = stage;
        this.listaDePedidos = pedidosConcluidos;
        // Inicializa todos os pedidos com um status padrão "Recebido".
        this.listaDePedidos.forEach(p -> statusDosPedidos.putIfAbsent(p, "Recebido"));
    }

    public void mostrar() {
        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setPadding(new Insets(20));
        layoutPrincipal.setStyle("-fx-background-color: " + DARK_BACKGROUND_COLOR + ";");

        Label titulo = new Label("Gerenciamento de Deliveries");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
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
                    if (newSelection != null) {
                        popularPainelDetalhes(newSelection);
                    } else {
                        limparPainelDetalhes();
                    }
                }
        );

        carregarPedidosNaTabela();

        Scene scene = new Scene(layoutPrincipal, 1200, 700);
        // Aplica o stylesheet da TableView na cena
        try {
            String css = getTableViewStylesheet();
            String dataUri = "data:text/css," + URLEncoder.encode(css, StandardCharsets.UTF_8.name());
            scene.getStylesheets().add(dataUri);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        stage.setScene(scene);
        stage.setTitle("Gerenciamento de Deliveries - Monsieur-José");
        stage.show();
    }

    private TableView<Pedido> criarTabelaPedidos() {
        TableView<Pedido> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN); // Política de redimensionamento
        tabela.setPlaceholder(new Label("Nenhum pedido para exibir."));

        // Coluna Cliente
        TableColumn<Pedido, String> colunaCliente = new TableColumn<>("Cliente");
        colunaCliente.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getConsumidor() != null ?
                        cellData.getValue().getConsumidor().getNome() : "N/A")
        );
        colunaCliente.setPrefWidth(250);

        // Coluna Total
        TableColumn<Pedido, Float> colunaTotal = new TableColumn<>("Valor Total");
        colunaTotal.setCellValueFactory(cellData ->
                new SimpleFloatProperty(cellData.getValue().getPagamento() != null ?
                        cellData.getValue().getPagamento().getPreco() : 0.0f).asObject()
        );
        // A formatação da célula será feita via CSS/TableCell, mas um fallback pode ser útil.
        colunaTotal.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("R$ %.2f", item));
                setAlignment(Pos.CENTER_LEFT);
            }
        });
        colunaTotal.setPrefWidth(150);

        // Coluna Status
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
        statusAtualLabel.setFont(FONT_LABEL_BOLD); // Destaque para o status
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

    // --- BEGIN HELPER METHODS ---

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

    // --- END HELPER METHODS ---
}
