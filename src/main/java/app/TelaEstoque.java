package app;

import classes.Ingrediente;
import classes.Prato;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class TelaEstoque extends Tela {

    // Listas observáveis para as duas tabelas
    private ObservableList<Ingrediente> masterDataIngredientes;
    private ObservableList<Prato> masterDataPratos;

    public TelaEstoque(Stage stage) {
        super(stage);
    }

    @Override
    public Scene criarScene() {
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);

        Label tituloPrincipal = new Label(Tela.emFrances ? "Stock et Cuisine" : "Estoque e Cozinha");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");
        Rectangle sublinhado = new Rectangle(500, 3);
        sublinhado.setFill(Color.web("#FFC300"));
        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 20, 0));

        // --- CRIAÇÃO DOS DOIS PAINÉIS ---
        VBox painelIngredientes = criarPainelIngredientes();
        VBox painelPratos = criarPainelPratos();

        Rectangle divide = new Rectangle(5, 550);
        divide.setFill(Color.web("#FFC300"));

        HBox layoutDuplo = new HBox(30, painelIngredientes, divide, painelPratos);
        layoutDuplo.setAlignment(Pos.CENTER);
        HBox.setHgrow(painelIngredientes, Priority.ALWAYS);
        HBox.setHgrow(painelPratos, Priority.ALWAYS);

        // Layout principal que organiza tudo
        VBox layoutPrincipal = new VBox(20, blocoTitulo, layoutDuplo);
        layoutPrincipal.setPadding(new Insets(20));
        VBox.setVgrow(layoutDuplo, Priority.ALWAYS);

        StackPane stackPane = new StackPane(layoutPrincipal);
        stackPane.setStyle("-fx-background-color: linear-gradient(to right, #30000C, #800020);");

        Runnable acaoVoltar = () -> new TelaServicos(super.getStage()).mostrarTela();
        BotaoVoltar.criarEPosicionar(stackPane, acaoVoltar);

        Scene scene = new Scene(stackPane, 1400, 800);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

        return scene;
    }

    /**
     * Cria o painel esquerdo com a tabela de ingredientes.
     */
    private VBox criarPainelIngredientes() {
        // --- MUDANÇA: Definição da fonte do subtítulo ---
        Font playfairFontSubtitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 30);

        Label tituloIngredientes = new Label("Ingredientes");
        tituloIngredientes.setFont(playfairFontSubtitulo);
        tituloIngredientes.setStyle("-fx-text-fill: #FFC300;");

        // --- MUDANÇA: Criação do sublinhado para o subtítulo ---
        Rectangle sublinhado = new Rectangle(180, 2);
        sublinhado.setFill(Color.web("#FFC300"));
        sublinhado.widthProperty().bind(tituloIngredientes.widthProperty()); // O sublinhado adapta-se ao tamanho do texto

        VBox blocoSubtitulo = new VBox(5, tituloIngredientes, sublinhado);
        blocoSubtitulo.setAlignment(Pos.CENTER);

        TableView<Ingrediente> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        TableColumn<Ingrediente, String> nomeColuna = new TableColumn<>("Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        // --- MUDANÇA: Alteração do texto da coluna de "Qtd." para "Quantidade" ---
        TableColumn<Ingrediente, Integer> qtdColuna = new TableColumn<>("Quantidade");
        qtdColuna.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        TableColumn<Ingrediente, Void> abastecerColuna = new TableColumn<>("Abastecer");

        abastecerColuna.setCellFactory(col -> new TableCell<>() {
            private final Button botao = new Button("Pedir");
            {
                botao.getStyleClass().add("button");
                botao.setOnAction(event -> {
                    Ingrediente ing = getTableView().getItems().get(getIndex());
                    if (ing.encomendaIngrediente(10)) {
                        getTableView().refresh();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Falha ao encomendar o ingrediente.").show();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : botao);
            }
        });

        tabela.getColumns().addAll(nomeColuna, qtdColuna, abastecerColuna);

        List<Ingrediente> ingredientesDoBanco = Ingrediente.listarTodos();
        masterDataIngredientes = FXCollections.observableArrayList(ingredientesDoBanco);
        tabela.setItems(masterDataIngredientes);

        VBox painel = new VBox(10, blocoSubtitulo, tabela);
        painel.setAlignment(Pos.TOP_CENTER);

        return painel;
    }

    /**
     * Cria o painel direito com a tabela de pratos.
     */
    private VBox criarPainelPratos() {
        // --- MUDANÇA: Definição da fonte do subtítulo ---
        Font playfairFontSubtitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 30);

        Label tituloPratos = new Label("Pratos");
        tituloPratos.setFont(playfairFontSubtitulo);
        tituloPratos.setStyle("-fx-text-fill: #FFC300;");

        // --- MUDANÇA: Criação do sublinhado para o subtítulo ---
        Rectangle sublinhado = new Rectangle(180, 2);
        sublinhado.setFill(Color.web("#FFC300"));
        sublinhado.widthProperty().bind(tituloPratos.widthProperty());

        VBox blocoSubtitulo = new VBox(5, tituloPratos, sublinhado);
        blocoSubtitulo.setAlignment(Pos.CENTER);

        TableView<Prato> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        TableColumn<Prato, String> nomeColuna = new TableColumn<>("Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Prato, Integer> qtdColuna = new TableColumn<>("Estoque");
        qtdColuna.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        TableColumn<Prato, Void> cozinharColuna = new TableColumn<>("Ação");

        cozinharColuna.setCellFactory(col -> new TableCell<>() {
            private final Button botao = new Button("Cozinhar");
            {
                botao.getStyleClass().add("button");
                botao.setOnAction(event -> {
                    Prato prato = getTableView().getItems().get(getIndex());
                    if (prato.fazPrato()) {
                        getTableView().refresh();
                        masterDataIngredientes.setAll(Ingrediente.listarTodos());
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Não foi possível cozinhar o prato. Verifique o estoque de ingredientes.").show();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : botao);
            }
        });

        tabela.getColumns().addAll(nomeColuna, qtdColuna, cozinharColuna);

        List<Prato> pratosDoBanco = Prato.listarTodosComIngredientes();
        masterDataPratos = FXCollections.observableArrayList(pratosDoBanco);
        tabela.setItems(masterDataPratos);

        VBox painel = new VBox(10, blocoSubtitulo, tabela);
        painel.setAlignment(Pos.TOP_CENTER);

        return painel;
    }
}