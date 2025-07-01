package app;

import classes.*;

import database.JpaUtil;
import jakarta.persistence.TypedQuery;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TelaDelivery extends Tela {

    private ObservableList<Prato> pratosDisponiveisNoMenu;
    private EntityManager em = JpaUtil.getFactory().createEntityManager();
    private ObservableList<ItemCarrinhoUI> carrinhoDaUI;
    private ListView<ItemCarrinhoUI> carrinhoListViewUI;
    private Label totalLabelUI;
    private Label statusLabelUI;
    private String userEmail;
    private ComboBox<String> tipoPagamentoComboBox;

    // --- Constantes de Estilo ---
    private static final String DARK_BACKGROUND_COLOR = "linear-gradient(to right, #30000C, #800020)";
    private static final String PANEL_BACKGROUND_COLOR = "#FAF0E6";
    private static final String ACCENT_COLOR_GOLD = "#DAA520";

    // --- LINHA ADICIONADA PARA CORRIGIR O ERRO ---
    private static final String BORDER_COLOR_PANEL = "#DAA520";

    private static final String ACCENT_COLOR_DARK_GOLD = "#B8860B";
    private static final String TEXT_COLOR_ON_PANEL = "#3D2B1F";
    private static final String BUTTON_TEXT_COLOR = "#FFFFFF";

    private static final Font FONT_TITLE = Font.font("Arial", FontWeight.BOLD, 24);
    private static final Font FONT_LABEL_BOLD = Font.font("Arial", FontWeight.BOLD, 16);
    private static final Font FONT_TEXT_NORMAL = Font.font("Arial", FontWeight.NORMAL, 14);
    private static final Font FONT_BUTTON = Font.font("Arial", FontWeight.BOLD, 14);
    private static final Font FONT_ITEM_NAME = Font.font("Arial", FontWeight.BOLD, 16);
    private static final Font FONT_ITEM_DETAILS = Font.font("Arial", FontWeight.NORMAL, 13);
    private static final Font FONT_ITEM_PRICE = Font.font("Arial", FontWeight.BOLD, 15);


    public TelaDelivery(Stage stage,String userEmail) {
        super(stage);
        this.userEmail = userEmail;
        this.pratosDisponiveisNoMenu = FXCollections.observableArrayList();
        this.carrinhoDaUI = FXCollections.observableArrayList(item ->
                new Observable[]{
                        item.quantidadeNoCarrinhoProperty(),
                        item.pratoProperty()
                });
    }

    private void carregarPratosDoBancoDeDados() {
        if (em == null || !em.isOpen()) {
            Platform.runLater(() -> statusLabelUI.setText(Tela.emFrances ? "Erreur : EntityManager n'est pas actif." : "Erro: EntityManager não está ativo."));
            return;
        }
        try {
            em.getTransaction().begin();
            List<Prato> pratosDoBanco = em.createQuery("SELECT p FROM Prato p", Prato.class).getResultList();
            pratosDisponiveisNoMenu.clear();
            pratosDisponiveisNoMenu.addAll(pratosDoBanco);
            em.getTransaction().commit();

            Platform.runLater(() -> {
                if(statusLabelUI != null) {
                    if (pratosDisponiveisNoMenu.isEmpty()) {
                        statusLabelUI.setText(Tela.emFrances ? "Aucun plat disponible au menu." : "Nenhum prato disponível no cardápio.");
                    } else {
                        statusLabelUI.setText((Tela.emFrances ? "Menu chargé avec " : "Cardápio carregado com ") + pratosDisponiveisNoMenu.size() + (Tela.emFrances ? " plats." : " pratos."));
                    }
                }
            });

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            Platform.runLater(() -> {
                if(statusLabelUI != null) {
                    statusLabelUI.setText((Tela.emFrances ? "Erreur lors du chargement du menu: " : "Erro ao carregar cardápio: ") + e.getMessage());
                }
            });
        }
    }

    @Override
    public Scene criarScene() {
        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setPadding(new Insets(20));

        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62);
        Label tituloPrincipal = new Label(Tela.emFrances ? "Livraison" : "Delivery");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");
        Rectangle sublinhado = new Rectangle(230, 4);
        sublinhado.setFill(Color.web("#FFC300"));
        sublinhado.widthProperty().bind(tituloPrincipal.widthProperty());
        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        blocoTitulo.setPadding(new Insets(0, 0, 20, 0));
        layoutPrincipal.setTop(blocoTitulo);

        VBox painelMenu = criarPainelMenu();
        layoutPrincipal.setLeft(painelMenu);
        BorderPane.setMargin(painelMenu, new Insets(0, 15, 0, 0));

        VBox painelCarrinho = criarPainelCarrinhoDaUI();
        layoutPrincipal.setCenter(painelCarrinho);

        statusLabelUI = new Label(Tela.emFrances ? "Bienvenue ! Choisissez vos plats et ajoutez-les au panier." : "Bem-vindo! Escolha seus pratos e adicione ao carrinho.");
        statusLabelUI.setPadding(new Insets(8, 10, 8, 10));
        statusLabelUI.setMaxWidth(Double.MAX_VALUE);
        statusLabelUI.setAlignment(Pos.CENTER_LEFT);
        statusLabelUI.setFont(FONT_TEXT_NORMAL);
        statusLabelUI.setStyle(
                "-fx-background-color: #5D0017;" +
                        "-fx-text-fill: #FFC300; " +
                        "-fx-border-color: " + ACCENT_COLOR_GOLD + "; " +
                        "-fx-border-width: 1 0 0 0;"
        );
        layoutPrincipal.setBottom(statusLabelUI);

        carregarPratosDoBancoDeDados(); // Carregar os pratos após a UI ser inicializada

        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: " + DARK_BACKGROUND_COLOR + ";");
        layoutPrincipal.setStyle("-fx-background-color: transparent;");
        stackPane.getChildren().add(layoutPrincipal);

        BotaoVoltar.criarEPosicionar(stackPane, () -> new TelaInicial(super.getStage()).mostrarTela());

        Scene scene = new Scene(stackPane, 1024, 768);
        return scene;
    }

    // ... (O resto do código permanece o mesmo) ...

    public void fecharRecursosJPA() {
        if (em != null && em.isOpen()) {
            em.close();
            System.out.println("EntityManager da TelaDelivery fechado.");
        }
        JpaUtil.close();
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


    private VBox criarPainelMenu() {
        VBox painel = new VBox(15);
        painel.setPadding(new Insets(15));
        painel.setStyle(
                "-fx-background-color: " + PANEL_BACKGROUND_COLOR + "; " +
                        "-fx-border-color: " + BORDER_COLOR_PANEL + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8;"
        );
        painel.setPrefWidth(480);

        Label tituloMenu = new Label(Tela.emFrances ? "Menu" : "Cardápio");
        tituloMenu.setFont(FONT_TITLE);
        tituloMenu.setStyle("-fx-text-fill: " + TEXT_COLOR_ON_PANEL);
        tituloMenu.setMaxWidth(Double.MAX_VALUE);
        tituloMenu.setAlignment(Pos.CENTER);


        ListView<Prato> menuListView = new ListView<>(pratosDisponiveisNoMenu);
        menuListView.setCellFactory(param -> new PratoListCell());
        VBox.setVgrow(menuListView, Priority.ALWAYS);
        menuListView.setStyle("-fx-background-color: transparent; -fx-control-inner-background: " + PANEL_BACKGROUND_COLOR + ";");


        painel.getChildren().addAll(tituloMenu, menuListView);
        return painel;
    }

    private VBox criarPainelCarrinhoDaUI() {
        VBox painel = new VBox(15);
        painel.setPadding(new Insets(15));
        painel.setStyle(
                "-fx-background-color: " + PANEL_BACKGROUND_COLOR + "; " +
                        "-fx-border-color: " + BORDER_COLOR_PANEL + "; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8;"
        );

        Label tituloCarrinho = new Label(Tela.emFrances ? "Votre Commande" : "Seu Pedido");
        tituloCarrinho.setFont(FONT_TITLE);
        tituloCarrinho.setStyle("-fx-text-fill: " + TEXT_COLOR_ON_PANEL);
        tituloCarrinho.setMaxWidth(Double.MAX_VALUE);
        tituloCarrinho.setAlignment(Pos.CENTER);

        carrinhoListViewUI = new ListView<>(carrinhoDaUI);
        carrinhoListViewUI.setCellFactory(param -> new ItemCarrinhoUIListCell());
        carrinhoListViewUI.setPrefHeight(400);
        carrinhoListViewUI.setStyle("-fx-background-color: transparent; -fx-control-inner-background: " + PANEL_BACKGROUND_COLOR + ";");


        carrinhoDaUI.addListener((ListChangeListener<ItemCarrinhoUI>) c -> atualizarTotalDaUI());

        HBox totalBox = new HBox(10);
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        totalBox.setPadding(new Insets(10, 0, 10, 0));
        Label totalTextoLabel = new Label(Tela.emFrances ? "Total :" : "Total:");
        totalTextoLabel.setFont(FONT_LABEL_BOLD);
        totalTextoLabel.setTextFill(Color.web(TEXT_COLOR_ON_PANEL));
        totalLabelUI = new Label("R$ 0,00");
        totalLabelUI.setFont(FONT_LABEL_BOLD);
        totalLabelUI.setStyle("-fx-text-fill: " + ACCENT_COLOR_DARK_GOLD);
        totalBox.getChildren().addAll(totalTextoLabel, totalLabelUI);

        Label labelTipoPagamento = new Label(Tela.emFrances ? "Type de Paiement :" : "Tipo de Pagamento:");
        labelTipoPagamento.setFont(FONT_LABEL_BOLD);
        labelTipoPagamento.setTextFill(Color.web(TEXT_COLOR_ON_PANEL));

        tipoPagamentoComboBox = new ComboBox<>();
        tipoPagamentoComboBox.setPrefWidth(200);
        tipoPagamentoComboBox.getItems().addAll(
                Tela.emFrances ? "Pix (10% de réduction)" : "Pix (10% Off)",
                Tela.emFrances ? "Carte de Crédit" : "Cartão de Crédito",
                Tela.emFrances ? "Carte de Débit" : "Cartão de Débito",
                Tela.emFrances ? "Chèque" : "Talão de Cheque",
                Tela.emFrances ? "Espèces" : "Dinheiro Físico",
                Tela.emFrances ? "Payer à crédit" : "Pagar Fiado"
        );
        tipoPagamentoComboBox.setValue(Tela.emFrances ? "Pix (10% de réduction)" : "Pix (10% Off)");
        tipoPagamentoComboBox.setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-border-color: " + BORDER_COLOR_PANEL + "; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5; " +
                        "-fx-font-size: 14px;"
        );

        VBox tipoPagamentoBox = new VBox(5, labelTipoPagamento, tipoPagamentoComboBox);
        tipoPagamentoBox.setAlignment(Pos.CENTER_RIGHT);
        tipoPagamentoBox.setPadding(new Insets(10, 0, 0, 0));

        Button btnFinalizarPedido = new Button(Tela.emFrances ? "Finaliser la Commande" : "Finalizar Pedido");
        btnFinalizarPedido.setFont(FONT_BUTTON);
        btnFinalizarPedido.setStyle(
                "-fx-background-color: " + ACCENT_COLOR_GOLD + "; " +
                        "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                        "-fx-background-radius: 5; " +
                        "-fx-padding: 10 20 10 20;"
        );
        btnFinalizarPedido.setOnMouseEntered(e -> btnFinalizarPedido.setStyle(
                "-fx-background-color: " + ACCENT_COLOR_DARK_GOLD + "; " +
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
        btnFinalizarPedido.setPrefWidth(200);
        btnFinalizarPedido.setOnAction(e -> handleFinalizarPedido());

        VBox finalizarBox = new VBox(5,tipoPagamentoBox,btnFinalizarPedido);
        finalizarBox.setAlignment(Pos.CENTER_RIGHT);
        finalizarBox.setPadding(new Insets(10,0,0,0));

        painel.getChildren().addAll(tituloCarrinho, carrinhoListViewUI, totalBox, finalizarBox);
        atualizarTotalDaUI();
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
            descricaoText.setFill(Color.web(darkenSlightly(TEXT_COLOR_ON_PANEL, 0.2)));
            descricaoText.setWrappingWidth(380);

            precoLabel = new Label();
            precoLabel.setFont(FONT_ITEM_PRICE);
            precoLabel.setTextFill(Color.web(ACCENT_COLOR_DARK_GOLD));

            quantidadeSpinner = new Spinner<>(1, 10, 1);
            quantidadeSpinner.setPrefWidth(70);
            quantidadeSpinner.getEditor().setStyle("-fx-background-color: #FFFFFF;" + "-fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-border-color: " + BORDER_COLOR_PANEL + "; -fx-border-width: 1;");


            addButton = new Button(Tela.emFrances ? "Ajouter" : "Adicionar");
            addButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
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
                    quantidadeSpinner.getValueFactory().setValue(1);
                }
            });
            HBox addControls = new HBox(10, quantidadeSpinner, addButton);
            addControls.setAlignment(Pos.CENTER_LEFT);
            addControls.setPadding(new Insets(8,0,5,0));

            content = new VBox(8, nomeLabel, descricaoText, precoLabel, addControls);
            content.setPadding(new Insets(10, 10, 10, 10));
            content.setStyle(defaultContentStyle);

            setStyle("-fx-padding: 0; -fx-background-color: transparent;");

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
            } else {
                nomeLabel.setText(prato.getNome());
                descricaoText.setText(prato.getDescricao());
                precoLabel.setText(String.format("R$ %.2f", prato.getPreco()));
                setGraphic(content);

                int quantidadeDisponivel = prato.getQuantidade();
                SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                        (SpinnerValueFactory.IntegerSpinnerValueFactory) quantidadeSpinner.getValueFactory();

                if (quantidadeDisponivel > 0) {
                    valueFactory.setMax(quantidadeDisponivel);
                    if (valueFactory.getValue() > quantidadeDisponivel) {
                        valueFactory.setValue(1);
                    }
                    quantidadeSpinner.setDisable(false);
                    addButton.setDisable(false);
                    addButton.setText(Tela.emFrances ? "Ajouter" : "Adicionar");

                } else {
                    valueFactory.setMax(0);
                    valueFactory.setValue(0);
                    quantidadeSpinner.setDisable(true);
                    addButton.setDisable(true);
                    addButton.setText(Tela.emFrances ? "Indisponible" : "Indisponível");
                }
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
        statusLabelUI.setText(quantidade + "x " + prato.getNome() + (Tela.emFrances ? " ajouté(s) au panier." : " adicionado(s) ao carrinho."));
        Platform.runLater(() -> {
            if (!carrinhoDaUI.isEmpty()) {
                carrinhoListViewUI.scrollTo(carrinhoDaUI.size() -1);
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
            itemLabel.setFont(FONT_ITEM_DETAILS);
            itemLabel.setTextFill(Color.web(TEXT_COLOR_ON_PANEL));
            itemLabel.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(itemLabel, Priority.ALWAYS);

            quantidadeItemSpinner = new Spinner<>(0, 99, 1);
            quantidadeItemSpinner.setPrefWidth(70);
            quantidadeItemSpinner.getEditor().setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-border-color: " + BORDER_COLOR_PANEL + "; -fx-border-width: 1;");


            quantidadeItemSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                ItemCarrinhoUI item = getItem();
                if (item != null && newValue != null && newValue != item.getQuantidadeNoCarrinho()) {
                    Platform.runLater(() -> {
                        ItemCarrinhoUI currentItem = getItem();
                        if (currentItem != null) {
                            if (newValue <= 0) {
                                carrinhoDaUI.remove(currentItem);
                                statusLabelUI.setText(currentItem.getPrato().getNome() + (Tela.emFrances ? " retiré du panier." : " removido do carrinho."));
                            } else {
                                currentItem.setQuantidadeNoCarrinho(newValue);
                                statusLabelUI.setText((Tela.emFrances ? "Quantité de " : "Quantidade de ") + currentItem.getPrato().getNome() + (Tela.emFrances ? " mise à jour à " : " atualizada para ") + newValue + ".");
                            }
                        }
                    });
                } else if (item != null && newValue != null && newValue == 0 && newValue == item.getQuantidadeNoCarrinho()){
                    Platform.runLater(() -> {
                        ItemCarrinhoUI currentItem = getItem();
                        if(currentItem != null) {
                            carrinhoDaUI.remove(currentItem);
                            statusLabelUI.setText(currentItem.getPrato().getNome() + (Tela.emFrances ? " retiré du panier." : " removido do carrinho."));
                        }
                    });
                }
            });

            removeButton = new Button("X");
            removeButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            removeButton.setStyle(
                    "-fx-background-color: #D32F2F; " +
                            "-fx-text-fill: " + BUTTON_TEXT_COLOR + "; " +
                            "-fx-background-radius: 4; " +
                            "-fx-padding: 3 8 3 8;"
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
                    statusLabelUI.setText(item.getPrato().getNome() + (Tela.emFrances ? " retiré du panier." : " removido do carrinho."));
                }
            });

            VBox spinnerContainer = new VBox(quantidadeItemSpinner);
            spinnerContainer.setAlignment(Pos.CENTER);

            content = new HBox(10, itemLabel, spinnerContainer, removeButton);
            content.setAlignment(Pos.CENTER_LEFT);
            content.setPadding(new Insets(8));
            content.setStyle(defaultContentStyle);

            setStyle("-fx-padding: 0; -fx-background-color: transparent;");

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
            Alert alert = new Alert(Alert.AlertType.WARNING, Tela.emFrances ? "Votre panier est vide !\nVeuillez ajouter des articles avant de finaliser la commande." : "Seu carrinho está vazio!\nPor favor, adicione itens antes de finalizar o pedido.", ButtonType.OK);
            alert.setHeaderText(Tela.emFrances ? "Panier Vide" : "Carrinho Vazio");
            alert.setTitle(Tela.emFrances ? "Attention" : "Atenção");
            styleAlertDialog(alert);
            alert.showAndWait();
            statusLabelUI.setText(Tela.emFrances ? "Tentative de finaliser une commande avec un panier vide." : "Tentativa de finalizar pedido com carrinho vazio.");
            return;
        }

        String tipoPagamentoSelecionado = tipoPagamentoComboBox.getValue();
        if (tipoPagamentoSelecionado == null || tipoPagamentoSelecionado.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, Tela.emFrances ? "Veuillez sélectionner un type de paiement avant de finaliser la commande." : "Por favor, selecione um tipo de pagamento antes de finalizar o pedido.", ButtonType.OK);
            alert.setHeaderText(Tela.emFrances ? "Type de Paiement Non Sélectionné" : "Tipo de Pagamento Não Selecionado");
            alert.setTitle(Tela.emFrances ? "Attention" : "Atenção");
            styleAlertDialog(alert);
            alert.showAndWait();
            statusLabelUI.setText(Tela.emFrances ? "Type de paiement non sélectionné." : "Tipo de pagamento não selecionado.");
            return;
        }


        Pedido pedidoFinal = new Pedido();
        ArrayList<Prato> pratosParaPedidoFinal = new ArrayList<>();
        ArrayList<Integer> quantidadesParaPedidoFinal = new ArrayList<>();

        for (ItemCarrinhoUI itemUI : carrinhoDaUI) {
            pratosParaPedidoFinal.add(itemUI.getPrato());
            quantidadesParaPedidoFinal.add(itemUI.getQuantidadeNoCarrinho());
        }

        List<PedidoItem> pedidoItems = new ArrayList<>();
        for(int i =0;i < pratosParaPedidoFinal.size();i++) {
            PedidoItem item = new PedidoItem(pedidoFinal,pratosParaPedidoFinal.get(i),quantidadesParaPedidoFinal.get(i));
            pedidoItems.add(item);
        }

        StringBuilder sb = new StringBuilder(Tela.emFrances ? "Votre commande a été préparée pour l'envoi :\n\n" : "Seu pedido foi preparado para envio:\n\n");
        sb.append(Tela.emFrances ? "Articles de la Commande :\n" : "Itens do Pedido:\n");
        for( int j = 0;j < pedidoItems.size();j++) {
            if (pedidoItems.get(j).getPrato() != null) {
                Prato p = pedidoItems.get(j).getPrato();
                int q = pedidoItems.get(j).getQuantidade();
                if (p != null) {
                    sb.append(String.format("  - %dx %s (R$ %.2f cada)\n", q, p.getNome(), p.getPreco()));
                }
            }
        }

        TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c WHERE c.email = :email", Cliente.class);
        query.setParameter("email", this.userEmail);
        Cliente cliente = query.getSingleResult();
        pedidoFinal.setConsumidor(cliente);
        pedidoFinal.setItensPedido(pedidoItems);
        Float subtotalSimplesFinal = pedidoFinal.calcularPrecoTotal();

        Pagamento pagamentoFinal = new Pagamento(subtotalSimplesFinal,cliente.getNome(),tipoPagamentoSelecionado,1);
        pedidoFinal.setPagamento(pagamentoFinal);
        pedidoFinal.getPagamento().getTipo().split(" ");
        if(pedidoFinal.getPagamento().getTipo().split(" ")[0].equalsIgnoreCase("Pix")){
            pedidoFinal.getPagamento().calcular();}
        if(cliente.ehAniversario()){pedidoFinal.getPagamento().setPreco(pedidoFinal.getPagamento().getPreco()-5);}
        Float desconto = cliente.descontoIdade(pedidoFinal.getPagamento().getPreco());
        pedidoFinal.getPagamento().setPreco(desconto);
        pedidoFinal.fidelidade(cliente);
        Integer num = cliente.getFidelidade();
        cliente.setFidelidade(num+1);


        EntityManager tempEm = JpaUtil.getFactory().createEntityManager();
        try {
            tempEm.getTransaction().begin();
            tempEm.persist(pedidoFinal);
            tempEm.getTransaction().commit();
            statusLabelUI.setText(Tela.emFrances ? "Commande finalisée et enregistrée avec succès ! Le panier a été vidé." : "Pedido finalizado e salvo com sucesso! O carrinho foi limpo.");
            carrinhoDaUI.clear();
        } catch (Exception e) {
            if (tempEm.getTransaction().isActive()) {
                tempEm.getTransaction().rollback();
            }
            System.err.println("Erro ao salvar o pedido: " + e.getMessage());
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, Tela.emFrances ? "Erreur lors de l'enregistrement de la commande dans la base de données. Veuillez réessayer." : "Erro ao salvar o pedido no banco de dados. Por favor, tente novamente.", ButtonType.OK);
            errorAlert.setHeaderText(Tela.emFrances ? "Erreur de Persistance" : "Erro de Persistência");
            styleAlertDialog(errorAlert);
            errorAlert.showAndWait();
            statusLabelUI.setText(Tela.emFrances ? "Erreur lors de la finalisation de la commande." : "Erro ao finalizar o pedido.");
        } finally {
            if (tempEm != null && tempEm.isOpen()) {
                tempEm.close();
            }
        }

        sb.append(String.format((Tela.emFrances ? "  - 1x Livraison : R$ %d\n" : "  - 1x Frete: R$ %d\n"),pedidoFinal.getFrete()));
        sb.append(String.format((Tela.emFrances ? "\nSous-total des Plats : R$ %.2f" : "\nSubtotal dos Pratos: R$ %.2f"), pedidoFinal.getPagamento().getPreco()));
        sb.append(String.format((Tela.emFrances ? "\nType de Paiement : %s" : "\nTipo de Pagamento: %s"), tipoPagamentoSelecionado));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Tela.emFrances ? "Commande Finalisée" : "Pedido Finalizado");
        alert.setHeaderText(Tela.emFrances ? "Confirmation de la Commande" : "Confirmação do Pedido");

        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: Arial; -fx-font-size: 13px; -fx-text-fill: " + TEXT_COLOR_ON_PANEL + "; -fx-background-color: " + PANEL_BACKGROUND_COLOR + "; -fx-control-inner-background: " + PANEL_BACKGROUND_COLOR + ";");

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle("-fx-background: " + PANEL_BACKGROUND_COLOR + "; -fx-border-color: " + BORDER_COLOR_PANEL + ";");

        alert.getDialogPane().setContent(scrollPane);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(500);
        styleAlertDialog(alert);
        alert.showAndWait();

        carrinhoDaUI.clear();

        new TelaPagamento(super.getStage(), this.userEmail, OrigemDaTela.TELA_DELIVERY).mostrarTela();
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