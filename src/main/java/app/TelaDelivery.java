package app;

import classes.Ingrediente; // Apenas para o App.java, não diretamente aqui
import classes.Prato;
// Removido: import classes.Pedido; // Não usaremos o Pedido do usuário para o estado interno da UI do carrinho
// As classes Pagamento e Cliente não são usadas diretamente na construção desta tela de menu/carrinho
// mas seriam necessárias para popular completamente o 'classes.Pedido' no final.

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TelaDelivery {

    private Stage stage;
    private ObservableList<Prato> pratosDisponiveisNoMenu; // Pratos carregados para exibir no menu

    // Carrinho da Interface Gráfica: lista de ItemCarrinhoUI
    private ObservableList<ItemCarrinhoUI> carrinhoDaUI;

    private ListView<ItemCarrinhoUI> carrinhoListViewUI;
    private Label totalLabelUI;
    private Label statusLabelUI;

    // --- BEGIN STYLE CONSTANTS ---
    private static final String DARK_BACKGROUND_COLOR = "#4B3832"; // Deep Brownish-Red (inspired by "Reserve Sua Mesa")
    private static final String PANEL_BACKGROUND_COLOR = "#FAF0E6"; // Linen (warm off-white for panels)
    private static final String ACCENT_COLOR_GOLD = "#DAA520";     // Goldenrod (for titles, buttons)
    private static final String ACCENT_COLOR_DARK_GOLD = "#B8860B"; // DarkGoldenrod (for more contrast on buttons/important text)
    private static final String TEXT_COLOR_ON_PANEL = "#3D2B1F";   // Dark Brown (for text on light panels)
    private static final String TEXT_COLOR_LIGHT = "#F5F5F5";      // Light Cream/Off-white (for text on dark backgrounds)
    private static final String BORDER_COLOR_PANEL = "#C8A67B";    // A muted gold/brown for panel borders
    private static final String BUTTON_TEXT_COLOR = "#FFFFFF";

    private static final Font FONT_TITLE = Font.font("Arial", FontWeight.BOLD, 24);
    private static final Font FONT_SUBTITLE = Font.font("Arial", FontWeight.BOLD, 20);
    private static final Font FONT_LABEL_BOLD = Font.font("Arial", FontWeight.BOLD, 16);
    private static final Font FONT_TEXT_NORMAL = Font.font("Arial", FontWeight.NORMAL, 14);
    private static final Font FONT_BUTTON = Font.font("Arial", FontWeight.BOLD, 14);
    private static final Font FONT_ITEM_NAME = Font.font("Arial", FontWeight.BOLD, 16);
    private static final Font FONT_ITEM_DETAILS = Font.font("Arial", FontWeight.NORMAL, 13);
    private static final Font FONT_ITEM_PRICE = Font.font("Arial", FontWeight.BOLD, 15);
    // --- END STYLE CONSTANTS ---

    public TelaDelivery(Stage stage, List<Prato> pratosDoMenu) {
        this.stage = stage;
        this.pratosDisponiveisNoMenu = FXCollections.observableArrayList(pratosDoMenu);
        this.carrinhoDaUI = FXCollections.observableArrayList(item ->
                new javafx.beans.Observable[]{ // Para que a ListView observe mudanças
                        item.quantidadeNoCarrinhoProperty(),
                        item.pratoProperty()
                });
    }

    public void mostrar() {
        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setPadding(new Insets(20)); // Increased padding
        layoutPrincipal.setStyle("-fx-background-color: " + DARK_BACKGROUND_COLOR + ";");

        // Main Title for the Delivery Screen
        Label screenTitle = new Label("Restaurante Monsieur-José - Delivery");
        screenTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        screenTitle.setTextFill(Color.web(ACCENT_COLOR_GOLD));
        screenTitle.setPadding(new Insets(0, 0, 20, 5)); // Add some bottom padding
        layoutPrincipal.setTop(screenTitle);
        BorderPane.setAlignment(screenTitle, Pos.CENTER_LEFT);


        VBox painelMenu = criarPainelMenu();
        layoutPrincipal.setLeft(painelMenu);
        BorderPane.setMargin(painelMenu, new Insets(0, 15, 0, 0)); // Increased margin

        VBox painelCarrinho = criarPainelCarrinhoDaUI();
        layoutPrincipal.setCenter(painelCarrinho);

        statusLabelUI = new Label("Bem-vindo! Escolha seus pratos e adicione ao carrinho.");
        statusLabelUI.setPadding(new Insets(8, 10, 8, 10));
        statusLabelUI.setMaxWidth(Double.MAX_VALUE);
        statusLabelUI.setAlignment(Pos.CENTER_LEFT);
        statusLabelUI.setFont(FONT_TEXT_NORMAL);
        statusLabelUI.setStyle(
                "-fx-background-color: " + darkenSlightly(DARK_BACKGROUND_COLOR, 0.1) + "; " +
                        "-fx-text-fill: " + TEXT_COLOR_LIGHT + "; " +
                        "-fx-border-color: " + ACCENT_COLOR_GOLD + "; " +
                        "-fx-border-width: 1 0 0 0;"
        );
        layoutPrincipal.setBottom(statusLabelUI);

        Scene scene = new Scene(layoutPrincipal, 1024, 768); // Slightly larger default size
        stage.setScene(scene);
        stage.setTitle("Sistema de Delivery - Cardápio Monsieur-José");
        stage.setMinWidth(900); // Increased min width
        stage.setMinHeight(700); // Increased min height
        stage.show();
    }

    // Helper to slightly darken a hex color
    private String darkenSlightly(String hexColor, double factor) {
        Color color = Color.web(hexColor);
        // Factor > 0 darkens, Factor < 0 lightens
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


    private VBox criarPainelMenu() {
        VBox painel = new VBox(15); // Increased spacing
        painel.setPadding(new Insets(15));
        painel.setStyle(
                "-fx-background-color: " + PANEL_BACKGROUND_COLOR + "; " +
                        "-fx-border-color: " + BORDER_COLOR_PANEL + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8;"
        );
        painel.setPrefWidth(480); // Adjusted preference

        Label tituloMenu = new Label("Cardápio");
        tituloMenu.setFont(FONT_TITLE);
        tituloMenu.setTextFill(Color.web(ACCENT_COLOR_DARK_GOLD));
        tituloMenu.setMaxWidth(Double.MAX_VALUE);
        tituloMenu.setAlignment(Pos.CENTER);


        ListView<Prato> menuListView = new ListView<>(pratosDisponiveisNoMenu);
        menuListView.setCellFactory(param -> new PratoListCell());
        menuListView.setPrefHeight(600); // Adjusted preference
        menuListView.setStyle("-fx-background-color: transparent; -fx-control-inner-background: " + PANEL_BACKGROUND_COLOR + ";");


        painel.getChildren().addAll(tituloMenu, menuListView);
        return painel;
    }

    private VBox criarPainelCarrinhoDaUI() {
        VBox painel = new VBox(15); // Increased spacing
        painel.setPadding(new Insets(15));
        painel.setStyle(
                "-fx-background-color: " + PANEL_BACKGROUND_COLOR + "; " +
                        "-fx-border-color: " + BORDER_COLOR_PANEL + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8;"
        );

        Label tituloCarrinho = new Label("Seu Pedido");
        tituloCarrinho.setFont(FONT_TITLE);
        tituloCarrinho.setTextFill(Color.web(ACCENT_COLOR_DARK_GOLD));
        tituloCarrinho.setMaxWidth(Double.MAX_VALUE);
        tituloCarrinho.setAlignment(Pos.CENTER);

        carrinhoListViewUI = new ListView<>(carrinhoDaUI); // Usa a lista de ItemCarrinhoUI
        carrinhoListViewUI.setCellFactory(param -> new ItemCarrinhoUIListCell());
        carrinhoListViewUI.setPrefHeight(400); // Adjusted preference
        carrinhoListViewUI.setStyle("-fx-background-color: transparent; -fx-control-inner-background: " + PANEL_BACKGROUND_COLOR + ";");


        carrinhoDaUI.addListener((ListChangeListener<ItemCarrinhoUI>) c -> atualizarTotalDaUI());

        HBox totalBox = new HBox(10);
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        totalBox.setPadding(new Insets(10, 0, 10, 0));
        Label totalTextoLabel = new Label("Total:");
        totalTextoLabel.setFont(FONT_LABEL_BOLD);
        totalTextoLabel.setTextFill(Color.web(TEXT_COLOR_ON_PANEL));
        totalLabelUI = new Label("R$ 0,00");
        totalLabelUI.setFont(FONT_LABEL_BOLD);
        totalLabelUI.setTextFill(Color.web(ACCENT_COLOR_DARK_GOLD)); // Use accent for total value
        totalBox.getChildren().addAll(totalTextoLabel, totalLabelUI);

        Button btnFinalizarPedido = new Button("Finalizar Pedido");
        btnFinalizarPedido.setFont(FONT_BUTTON);
        btnFinalizarPedido.setStyle(
                "-fx-background-color: " + ACCENT_COLOR_GOLD + "; " +
                        "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 10 20 10 20;" // Added padding
        );
        btnFinalizarPedido.setOnMouseEntered(e -> btnFinalizarPedido.setStyle(
                "-fx-background-color: " + ACCENT_COLOR_DARK_GOLD + "; " + // Darken on hover
                        "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 10 20 10 20;"
        ));
        btnFinalizarPedido.setOnMouseExited(e -> btnFinalizarPedido.setStyle(
                "-fx-background-color: " + ACCENT_COLOR_GOLD + "; " +
                        "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 10 20 10 20;"
        ));

        btnFinalizarPedido.setPrefWidth(200); // Increased preferred width
        btnFinalizarPedido.setOnAction(e -> handleFinalizarPedido());

        HBox finalizarBox = new HBox(btnFinalizarPedido);
        finalizarBox.setAlignment(Pos.CENTER_RIGHT);
        finalizarBox.setPadding(new Insets(10,0,0,0));

        painel.getChildren().addAll(tituloCarrinho, carrinhoListViewUI, totalBox, finalizarBox);
        atualizarTotalDaUI(); // Calcula o total inicial
        return painel;
    }

    private class PratoListCell extends ListCell<Prato> {
        private VBox content;
        private Label nomeLabel;
        private Text descricaoText;
        private Label precoLabel;
        private Spinner<Integer> quantidadeSpinner;
        private Button addButton;
        private final String defaultContentStyle = "-fx-border-color: " + BORDER_COLOR_PANEL + "; -fx-border-width: 0 0 1 0; -fx-background-color: transparent;";
        private final String hoverContentStyle = "-fx-background-color: " + darkenSlightly(PANEL_BACKGROUND_COLOR, -0.05) + ";" +
                "-fx-border-color: " + BORDER_COLOR_PANEL + "; -fx-border-width: 0 0 1 0;";


        public PratoListCell() {
            super();
            nomeLabel = new Label();
            nomeLabel.setFont(FONT_ITEM_NAME);
            nomeLabel.setTextFill(Color.web(TEXT_COLOR_ON_PANEL));

            descricaoText = new Text();
            descricaoText.setFont(FONT_ITEM_DETAILS);
            descricaoText.setFill(Color.web(darkenSlightly(TEXT_COLOR_ON_PANEL, 0.2))); // Slightly darker for description
            descricaoText.setWrappingWidth(380); // Adjust if panel width changes significantly

            precoLabel = new Label();
            precoLabel.setFont(FONT_ITEM_PRICE);
            precoLabel.setTextFill(Color.web(ACCENT_COLOR_DARK_GOLD));

            quantidadeSpinner = new Spinner<>(1, 10, 1);
            quantidadeSpinner.setPrefWidth(70);
            // Basic styling for spinner to blend in
            quantidadeSpinner.getEditor().setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-border-color: " + BORDER_COLOR_PANEL + "; -fx-border-width: 1;");


            addButton = new Button("Adicionar");
            addButton.setFont(Font.font("Arial", FontWeight.BOLD, 12)); // Keep this button a bit smaller
            addButton.setStyle(
                    "-fx-background-color: " + ACCENT_COLOR_GOLD + "; " +
                            "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                            "-fx-background-radius: 4;"
            );
            addButton.setOnMouseEntered(e -> addButton.setStyle(
                    "-fx-background-color: " + ACCENT_COLOR_DARK_GOLD + "; " +
                            "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                            "-fx-background-radius: 4;"
            ));
            addButton.setOnMouseExited(e -> addButton.setStyle(
                    "-fx-background-color: " + ACCENT_COLOR_GOLD + "; " +
                            "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                            "-fx-background-radius: 4;"
            ));

            addButton.setOnAction(event -> {
                Prato pratoSelecionado = getItem();
                if (pratoSelecionado != null) {
                    int quantidade = quantidadeSpinner.getValue();
                    adicionarPratoAoCarrinhoDaUI(pratoSelecionado, quantidade);
                    quantidadeSpinner.getValueFactory().setValue(1); // Reset spinner
                }
            });
            HBox addControls = new HBox(10, quantidadeSpinner, addButton);
            addControls.setAlignment(Pos.CENTER_LEFT);
            addControls.setPadding(new Insets(8,0,5,0)); // Adjusted padding

            content = new VBox(8, nomeLabel, descricaoText, precoLabel, addControls); // Increased spacing
            content.setPadding(new Insets(10, 10, 10, 10)); // Uniform padding
            content.setStyle(defaultContentStyle);


            // Remove default cell padding and background to let content VBox handle it
            setStyle("-fx-padding: 0; -fx-background-color: transparent;");

            // Hover effect for the cell content
            setOnMouseEntered(e -> {
                if (!isEmpty() && getItem() != null) {
                    content.setStyle(hoverContentStyle);
                }
            });
            setOnMouseExited(e -> {
                if (!isEmpty() && getItem() != null) {
                    content.setStyle(defaultContentStyle);
                }
            });
        }
        @Override
        protected void updateItem(Prato prato, boolean empty) {
            super.updateItem(prato, empty);
            if (empty || prato == null) {
                setGraphic(null);
                // Ensure empty cells also have transparent background if ListView bg is set
                // and reset content style if it was hovered
                if (content != null) content.setStyle(defaultContentStyle);
            } else {
                nomeLabel.setText(prato.getNome());
                descricaoText.setText(prato.getDescricao());
                precoLabel.setText(String.format("R$ %.2f", prato.getPreco()));
                setGraphic(content);
                // Ensure correct style is applied (in case of recycling)
                content.setStyle(defaultContentStyle);
            }
        }
    }

    private void adicionarPratoAoCarrinhoDaUI(Prato prato, int quantidade) {
        if (prato == null || quantidade <= 0) return;

        Optional<ItemCarrinhoUI> itemExistenteOpt = carrinhoDaUI.stream()
                .filter(itemUI -> itemUI.getPrato().getNome().equals(prato.getNome()))
                .findFirst();

        if (itemExistenteOpt.isPresent()) {
            ItemCarrinhoUI itemExistente = itemExistenteOpt.get();
            itemExistente.setQuantidadeNoCarrinho(itemExistente.getQuantidadeNoCarrinho() + quantidade);
        } else {
            carrinhoDaUI.add(new ItemCarrinhoUI(prato, quantidade));
        }
        statusLabelUI.setText(quantidade + "x " + prato.getNome() + " adicionado(s) ao carrinho.");
        Platform.runLater(() -> {
            if (!carrinhoDaUI.isEmpty()) {
                carrinhoListViewUI.scrollTo(carrinhoDaUI.size() -1);
                // Optionally select the last item, can be commented out if not desired
                // carrinhoListViewUI.getSelectionModel().select(carrinhoDaUI.size() - 1);
            }
        });
    }


    private class ItemCarrinhoUIListCell extends ListCell<ItemCarrinhoUI> {
        private HBox content;
        private Label itemLabel;
        private Button removeButton;
        private Spinner<Integer> quantidadeItemSpinner;
        private final String defaultContentStyle = "-fx-border-color: " + BORDER_COLOR_PANEL + "; -fx-border-width: 0 0 1 0; -fx-background-color: transparent;";
        private final String hoverContentStyle = "-fx-background-color: " + darkenSlightly(PANEL_BACKGROUND_COLOR, -0.05) + ";" +
                "-fx-border-color: " + BORDER_COLOR_PANEL + "; -fx-border-width: 0 0 1 0;";


        public ItemCarrinhoUIListCell() {
            super();
            itemLabel = new Label();
            itemLabel.setFont(FONT_ITEM_DETAILS); // Slightly smaller for cart items
            itemLabel.setTextFill(Color.web(TEXT_COLOR_ON_PANEL));
            itemLabel.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(itemLabel, Priority.ALWAYS);

            quantidadeItemSpinner = new Spinner<>(0, 99, 1); // Permite 0 para remover
            quantidadeItemSpinner.setPrefWidth(70);
            quantidadeItemSpinner.getEditor().setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-border-color: " + BORDER_COLOR_PANEL + "; -fx-border-width: 1;");


            quantidadeItemSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                ItemCarrinhoUI item = getItem();
                // Check if item is not null and newValue is different from current quantity
                if (item != null && newValue != null && newValue != item.getQuantidadeNoCarrinho()) {
                    Platform.runLater(() -> { // Defer modification
                        // Re-fetch item inside Platform.runLater to ensure it's still valid
                        ItemCarrinhoUI currentItem = getItem();
                        if (currentItem != null) { // Check again as cell might have been recycled
                            if (newValue <= 0) {
                                carrinhoDaUI.remove(currentItem);
                                statusLabelUI.setText(currentItem.getPrato().getNome() + " removido do carrinho.");
                            } else {
                                currentItem.setQuantidadeNoCarrinho(newValue);
                                statusLabelUI.setText("Quantidade de " + currentItem.getPrato().getNome() + " atualizada para " + newValue + ".");
                            }
                        }
                    });
                } else if (item != null && newValue != null && newValue == 0 && newValue == item.getQuantidadeNoCarrinho()){
                    // Handles the case where quantity is already 0 and spinner is set to 0 again (to remove)
                    Platform.runLater(() -> {
                        ItemCarrinhoUI currentItem = getItem();
                        if(currentItem != null) {
                            carrinhoDaUI.remove(currentItem);
                            statusLabelUI.setText(currentItem.getPrato().getNome() + " removido do carrinho.");
                        }
                    });
                }
            });

            removeButton = new Button("X"); // Smaller remove button
            removeButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            removeButton.setStyle(
                    "-fx-background-color: #D32F2F; " + // A subtle red for removal
                            "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                            "-fx-background-radius: 4; " +
                            "-fx-padding: 3 8 3 8;" // Smaller padding
            );
            removeButton.setOnMouseEntered(e -> removeButton.setStyle(
                    "-fx-background-color: #B71C1C; " +
                            "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                            "-fx-background-radius: 4; " +
                            "-fx-padding: 3 8 3 8;"
            ));
            removeButton.setOnMouseExited(e -> removeButton.setStyle(
                    "-fx-background-color: #D32F2F; " +
                            "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                            "-fx-background-radius: 4; " +
                            "-fx-padding: 3 8 3 8;"
            ));


            removeButton.setOnAction(event -> {
                ItemCarrinhoUI item = getItem();
                if (item != null) {
                    carrinhoDaUI.remove(item);
                    statusLabelUI.setText(item.getPrato().getNome() + " removido do carrinho.");
                }
            });

            VBox spinnerContainer = new VBox(quantidadeItemSpinner);
            spinnerContainer.setAlignment(Pos.CENTER);

            content = new HBox(10, itemLabel, spinnerContainer, removeButton);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setPadding(new Insets(8));
            content.setStyle(defaultContentStyle);

            // Remove default cell padding and background
            setStyle("-fx-padding: 0; -fx-background-color: transparent;");

            // Hover effect
            setOnMouseEntered(e -> {
                if (!isEmpty() && getItem() != null) {
                    content.setStyle(hoverContentStyle);
                }
            });
            setOnMouseExited(e -> {
                if (!isEmpty() && getItem() != null) {
                    content.setStyle(defaultContentStyle);
                }
            });
        }
        @Override
        protected void updateItem(ItemCarrinhoUI item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null || item.getPrato() == null) {
                setText(null);
                setGraphic(null);
                if (content != null) content.setStyle(defaultContentStyle);
            } else {
                itemLabel.setText(String.format("%s (Qtd: %d) - Sub: R$ %.2f",
                        item.getPrato().getNome(), item.getQuantidadeNoCarrinho(), item.getSubtotal()));

                // Update spinner value without triggering listener if it's the same
                if (quantidadeItemSpinner.getValueFactory().getValue() != item.getQuantidadeNoCarrinho()) {
                    quantidadeItemSpinner.getValueFactory().setValue(item.getQuantidadeNoCarrinho());
                }
                setGraphic(content);
                content.setStyle(defaultContentStyle);
            }
        }
    }

    private void atualizarTotalDaUI() {
        double total = 0;
        for (ItemCarrinhoUI item : carrinhoDaUI) {
            total += item.getSubtotal();
        }
        totalLabelUI.setText(String.format("R$ %.2f", total));
    }

    private void handleFinalizarPedido() {
        if (carrinhoDaUI.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Seu carrinho está vazio!\nPor favor, adicione itens antes de finalizar o pedido.", ButtonType.OK);
            alert.setHeaderText("Carrinho Vazio");
            alert.setTitle("Atenção");
            styleAlertDialog(alert);
            alert.showAndWait();
            statusLabelUI.setText("Tentativa de finalizar pedido com carrinho vazio.");
            return;
        }

        classes.Pedido pedidoFinal = new classes.Pedido(); // Assuming classes.Pedido exists
        ArrayList<Prato> pratosParaPedidoFinal = new ArrayList<>();
        ArrayList<Integer> quantidadesParaPedidoFinal = new ArrayList<>();

        for (ItemCarrinhoUI itemUI : carrinhoDaUI) {
            pratosParaPedidoFinal.add(itemUI.getPrato());
            quantidadesParaPedidoFinal.add(itemUI.getQuantidadeNoCarrinho());
        }
        pedidoFinal.setPratos(pratosParaPedidoFinal); // Assuming setPratos method exists
        pedidoFinal.setQuantidades(quantidadesParaPedidoFinal); // Assuming setQuantidades method exists

        StringBuilder sb = new StringBuilder("Seu pedido foi preparado para envio:\n\n");
        sb.append("Itens do Pedido:\n");
        if (pedidoFinal.getPratos() != null && pedidoFinal.getQuantidades() != null &&
                pedidoFinal.getPratos().size() == pedidoFinal.getQuantidades().size()) {
            for (int i = 0; i < pedidoFinal.getPratos().size(); i++) {
                Prato p = pedidoFinal.getPratos().get(i);
                int q = pedidoFinal.getQuantidades().get(i);
                if (p != null) {
                    sb.append(String.format("  - %dx %s (R$ %.2f cada)\n", q, p.getNome(), p.getPreco()));
                }
            }
        }


        double subtotalSimples = 0;
        for (ItemCarrinhoUI itemUI : carrinhoDaUI) {
            subtotalSimples += itemUI.getSubtotal();
        }
        sb.append(String.format("\nSubtotal dos Pratos: R$ %.2f", subtotalSimples));
        // sb.append(String.format("\nTotal Completo (calculado por classes.Pedido): R$ %.2f", totalCalculadoPeloSeuPedido)); // If implemented

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pedido Finalizado");
        alert.setHeaderText("Confirmação do Pedido");

        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: Arial; -fx-font-size: 13px; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-background-color: " + PANEL_BACKGROUND_COLOR + "; -fx-control-inner-background: " + PANEL_BACKGROUND_COLOR + ";");

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200); // Adjust height as needed
        scrollPane.setStyle("-fx-background: " + PANEL_BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR_PANEL + ";");

        alert.getDialogPane().setContent(scrollPane); // Use setContent for complex content
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(500); // Wider dialog
        styleAlertDialog(alert);
        alert.showAndWait();

        statusLabelUI.setText("Pedido finalizado com sucesso! O carrinho foi limpo.");
        carrinhoDaUI.clear();
    }

    // Helper method to style Alert dialogs consistently
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
        // If content is the default label, style it. Otherwise, assume it's custom (like our TextArea)
        if (content instanceof Label) {
            ((Label)content).setStyle("-fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-font-size: 14px; -fx-wrap-text: true;");
            ((Label)content).setPadding(new Insets(10));
        }

        dialogPane.getButtonTypes().stream().map(dialogPane::lookupButton).forEach(buttonNode -> {
            if (buttonNode instanceof Button) {
                Button button = (Button) buttonNode;
                button.setFont(FONT_BUTTON);
                button.setStyle(
                        "-fx-background-color: " + ACCENT_COLOR_GOLD + "; " +
                                "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                                "-fx-background-radius: 4; -fx-padding: 8 15 8 15;"
                );
                button.setOnMouseEntered(e -> button.setStyle(
                        "-fx-background-color: " + ACCENT_COLOR_DARK_GOLD + "; " +
                                "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 4; -fx-padding: 8 15 8 15;"
                ));
                button.setOnMouseExited(e -> button.setStyle(
                        "-fx-background-color: " + ACCENT_COLOR_GOLD + "; " +
                                "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-radius: 4; -fx-padding: 8 15 8 15;"
                ));
            }
        });
    }
}
