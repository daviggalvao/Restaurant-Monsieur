package app;

import classes.*;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TelaGerenciarDelivery extends Tela {

    private ObservableList<Pedido> masterData;
    private FilteredList<Pedido> filteredData;
    private TableView<Pedido> tabelaPedidos;

    private ListView<String> detalhesListView;
    private Label clienteLabel, totalLabel, statusAtualLabel;
    private ComboBox<PedidoStatus> statusComboBox;
    private Button salvarStatusButton;

    private static final String DARK_BACKGROUND_COLOR = "linear-gradient(to right, #30000C, #800020)";
    private static final String PANEL_BACKGROUND_COLOR = "#FAF0E6";
    private static final String ACCENT_COLOR_GOLD = "#FFC300";

    public TelaGerenciarDelivery(Stage stage) {
        super(stage);
        List<Pedido> listaDePedidos = Pedido.listarTodos();
        this.masterData = FXCollections.observableArrayList(listaDePedidos);
    }

    @Override
    public Scene criarScene() {
        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setPadding(new Insets(20));

        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62);
        Label tituloPrincipal = new Label(Tela.emFrances ? "Commandes" : "Pedidos");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");
        Rectangle sublinhado = new Rectangle(230, 4);
        sublinhado.setFill(Color.web("#FFC300"));
        sublinhado.widthProperty().bind(tituloPrincipal.widthProperty());
        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        blocoTitulo.setPadding(new Insets(0, 0, 20, 0));
        layoutPrincipal.setTop(blocoTitulo);

        TextField pesquisaNome = new TextField();
        // --- TRADUÇÃO ---
        pesquisaNome.setPromptText(Tela.emFrances ? "Rechercher par nom" : "Pesquisar por nome");
        pesquisaNome.setMinWidth(300);

        // --- TRADUÇÃO ---
        Button limparPesquisa = new Button(Tela.emFrances ? "Nettoyer" : "Limpar");

        HBox barraPesquisa = new HBox(10, pesquisaNome, limparPesquisa);
        barraPesquisa.setAlignment(Pos.CENTER_LEFT);

        tabelaPedidos = criarTabelaPedidos();

        VBox centroContainer = new VBox(15, barraPesquisa, tabelaPedidos);
        VBox.setVgrow(tabelaPedidos, Priority.ALWAYS);
        layoutPrincipal.setCenter(centroContainer);

        VBox painelDetalhes = criarPainelDetalhes();
        layoutPrincipal.setRight(painelDetalhes);
        BorderPane.setMargin(painelDetalhes, new Insets(0, 0, 0, 20));

        this.filteredData = new FilteredList<>(masterData, p -> true);
        tabelaPedidos.setItems(filteredData);

        pesquisaNome.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(pedido -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newVal.toLowerCase();
                if (pedido.getConsumidor() != null && pedido.getConsumidor().getNome() != null) {
                    return pedido.getConsumidor().getNome().toLowerCase().contains(lowerCaseFilter);
                }
                return false;
            });
        });

        limparPesquisa.setOnAction(e -> {
            pesquisaNome.clear();
            filteredData.setPredicate(p -> true);
        });

        tabelaPedidos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        popularPainelDetalhes(newSelection);
                    } else {
                        limparPainelDetalhes();
                    }
                }
        );

        StackPane stackPane = new StackPane(layoutPrincipal);
        stackPane.setStyle("-fx-background-color: " + DARK_BACKGROUND_COLOR + ";");

        Runnable acaoVoltar = () -> new TelaServicos(super.getStage()).mostrarTela();
        BotaoVoltar.criarEPosicionar(stackPane, acaoVoltar);

        Scene scene = new Scene(stackPane, 1200, 700);
        return scene;
    }

    private TableView<Pedido> criarTabelaPedidos() {
        TableView<Pedido> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        // --- TRADUÇÃO ---
        tabela.setPlaceholder(new Label(Tela.emFrances ? "Aucune commande à afficher." : "Nenhum pedido para exibir."));

        // --- TRADUÇÃO ---
        TableColumn<Pedido, String> colunaCliente = new TableColumn<>(Tela.emFrances ? "Client" : "Cliente");
        colunaCliente.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getConsumidor() != null ?
                        cellData.getValue().getConsumidor().getNome() : "N/A")
        );
        colunaCliente.setPrefWidth(250);

        // --- TRADUÇÃO ---
        TableColumn<Pedido, Float> colunaTotal = new TableColumn<>(Tela.emFrances ? "Valeur Totale" : "Valor Total");
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

        // --- TRADUÇÃO ---
        TableColumn<Pedido, String> colunaStatus = new TableColumn<>(Tela.emFrances ? "Statut" : "Status");
        colunaStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus() != null ?
                        cellData.getValue().getStatus().toString() : "N/A")
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
                        "-fx-border-color: #C8A67B; -fx-border-width: 1; " +
                        "-fx-border-radius: 8; -fx-background-radius: 8;"
        );

        // --- TRADUÇÃO ---
        Label tituloDetalhes = new Label(Tela.emFrances ? "Détails de la Commande" : "Detalhes do Pedido");
        tituloDetalhes.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tituloDetalhes.setTextFill(Color.web("#B8860B"));
        tituloDetalhes.setMaxWidth(Double.MAX_VALUE);
        tituloDetalhes.setAlignment(Pos.CENTER);

        VBox infoBox = new VBox(5);
        // --- TRADUÇÃO ---
        clienteLabel = new Label(Tela.emFrances ? "Client : -" : "Cliente: -");
        totalLabel = new Label(Tela.emFrances ? "Valeur Totale : -" : "Valor Total: -");
        statusAtualLabel = new Label(Tela.emFrances ? "Statut Actuel : -" : "Status Atual: -");
        infoBox.getChildren().addAll(clienteLabel, totalLabel, statusAtualLabel);

        // --- TRADUÇÃO ---
        Label itensLabel = new Label(Tela.emFrances ? "Articles de la Commande :" : "Itens do Pedido:");
        detalhesListView = new ListView<>();
        VBox.setVgrow(detalhesListView, Priority.ALWAYS);

        // --- TRADUÇÃO ---
        Label mudarStatusLabel = new Label(Tela.emFrances ? "Changer le Statut pour :" : "Alterar Status para:");
        statusComboBox = new ComboBox<>(FXCollections.observableArrayList(PedidoStatus.values()));
        statusComboBox.setDisable(true);
        statusComboBox.setMaxWidth(Double.MAX_VALUE);

        // --- TRADUÇÃO ---
        salvarStatusButton = new Button(Tela.emFrances ? "Enregistrer le Statut" : "Salvar Status");
        salvarStatusButton.setDisable(true);
        salvarStatusButton.setMaxWidth(Double.MAX_VALUE);
        salvarStatusButton.setOnAction(e -> salvarNovoStatus());

        painel.getChildren().addAll(tituloDetalhes, infoBox, new Separator(), itensLabel, detalhesListView, new Separator(), mudarStatusLabel, statusComboBox, salvarStatusButton);
        return painel;
    }

    private void popularPainelDetalhes(Pedido pedido) {
        // --- TRADUÇÃO ---
        clienteLabel.setText((Tela.emFrances ? "Client : " : "Cliente: ") + (pedido.getConsumidor() != null ? pedido.getConsumidor().getNome() : "N/A"));
        totalLabel.setText((Tela.emFrances ? "Valeur Totale : " : "Valor Total: ") + (pedido.getPagamento() != null ? String.format("R$ %.2f", pedido.getPagamento().getPreco()) : "R$ 0,00"));

        PedidoStatus statusAtual = pedido.getStatus();
        // --- TRADUÇÃO ---
        statusAtualLabel.setText((Tela.emFrances ? "Statut Actuel : " : "Status Atual: ") + (statusAtual != null ? statusAtual.toString() : "N/A"));
        statusComboBox.setValue(statusAtual);

        detalhesListView.getItems().clear();
        if (pedido.getItensPedido() != null && !pedido.getItensPedido().isEmpty()) {
            for (PedidoItem item : pedido.getItensPedido()) {
                Prato prato = item.getPrato();
                Integer quantidade = item.getQuantidade();
                if (prato != null) {
                    detalhesListView.getItems().add(String.format("%dx %s", quantidade, prato.getNome()));
                }
            }
        }

        statusComboBox.setDisable(false);
        salvarStatusButton.setDisable(false);
    }

    private void limparPainelDetalhes() {
        // --- TRADUÇÃO ---
        clienteLabel.setText(Tela.emFrances ? "Client : -" : "Cliente: -");
        totalLabel.setText(Tela.emFrances ? "Valeur Totale : -" : "Valor Total: -");
        statusAtualLabel.setText(Tela.emFrances ? "Statut Actuel : -" : "Status Atual: -");
        detalhesListView.getItems().clear();
        statusComboBox.setValue(null);
        statusComboBox.setDisable(true);
        salvarStatusButton.setDisable(true);
    }

    private void salvarNovoStatus() {
        Pedido pedidoSelecionado = tabelaPedidos.getSelectionModel().getSelectedItem();
        PedidoStatus novoStatus = statusComboBox.getValue();

        if (pedidoSelecionado != null && novoStatus != null) {
            pedidoSelecionado.setStatus(novoStatus);
            pedidoSelecionado.salvar();

            statusAtualLabel.setText((Tela.emFrances ? "Statut Actuel : " : "Status Atual: ") + novoStatus.toString());
            tabelaPedidos.refresh();

            // --- TRADUÇÃO ---
            Alert alert = new Alert(Alert.AlertType.INFORMATION, (Tela.emFrances ? "Statut mis à jour à '" : "Status atualizado para '") + novoStatus + "'!");
            alert.setHeaderText(Tela.emFrances ? "Statut Mis à Jour" : "Status Atualizado");
            alert.showAndWait();
        } else {
            // --- TRADUÇÃO ---
            Alert alert = new Alert(Alert.AlertType.WARNING, Tela.emFrances ? "Veuillez sélectionner une commande et un nouveau statut à enregistrer." : "Selecione um pedido e um novo status para salvar.");
            alert.setHeaderText(Tela.emFrances ? "Aucune Modification Détectée" : "Nenhuma Alteração Detectada");
            alert.showAndWait();
        }
    }
}