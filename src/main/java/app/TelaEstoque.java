package app;

import classes.Ingrediente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

public class TelaEstoque extends Tela {

    public TelaEstoque(Stage stage) {
        super(stage);
    }

    @Override
    public Scene criarScene() {
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        Label tituloPrincipal = new Label(Tela.emFrances ? "Stock" : "Estoque");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(190, 3);
        sublinhado.setFill(Color.web("#FFC300"));

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 20, 0));

        // --- MUDANÇA: Ajuste no texto e adição do botão limpar ---
        TextField pesquisaNome = new TextField();
        // 1. Texto do campo de pesquisa alterado para ser mais simples
        pesquisaNome.setPromptText(Tela.emFrances ? "Rechercher par nom" : "Pesquisar por nome");
        pesquisaNome.setMinWidth(300); // Ajustei a largura mínima

        // 2. Criação do botão Limpar
        Button limparPesquisa = new Button(Tela.emFrances ? "Nettoyer" : "Limpar");

        // 3. Adição do botão ao HBox, com espaçamento
        HBox barraPesquisa = new HBox(10, pesquisaNome, limparPesquisa);
        barraPesquisa.setAlignment(Pos.CENTER);
        VBox.setMargin(barraPesquisa, new Insets(0, 0, 20, 0));
        // --- FIM DA MUDANÇA ---

        TableView<Ingrediente> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Ingrediente, String> nomeColuna = new TableColumn<>(Tela.emFrances ? "Ingredient" : "Ingrediente");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Ingrediente, Float> precoColuna = new TableColumn<>(Tela.emFrances ? "Prix (R$)" : "Preço (R$)");
        precoColuna.setCellValueFactory(new PropertyValueFactory<>("preco"));
        TableColumn<Ingrediente, Integer> quantidadeColuna = new TableColumn<>(Tela.emFrances ? "Montant" : "Quantidade");
        quantidadeColuna.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        TableColumn<Ingrediente, Void> pedirColuna = new TableColumn<>(Tela.emFrances ? "Carburant" : "Abastecer");
        pedirColuna.setCellFactory(coluna -> new TableCell<>() {
            private final Button botao = new Button(Tela.emFrances ? "Carburant" : "Abastecer");
            {
                botao.setOnAction(event -> {
                    Ingrediente inc = getTableView().getItems().get(getIndex());
                    if(inc.precisaRepor())
                        inc.encomendaIngrediente(10);
                    getTableView().refresh();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(botao);
                }
            }
        });

        tabela.getColumns().addAll(nomeColuna, precoColuna, quantidadeColuna, pedirColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        ObservableList<Ingrediente> masterData = FXCollections.observableArrayList(
                new Ingrediente(1L, "Tomate Italiano", 8.50f, 0, "15/06/2025"),
                new Ingrediente(2L, "Queijo Mussarela", 45.00f, 15, "30/07/2025"),
                new Ingrediente(3L, "Farinha de Trigo", 5.20f, 50, "01/12/2026"),
                new Ingrediente(4L, "Azeite de Oliva", 25.50f, 30, "01/01/2027")
        );

        FilteredList<Ingrediente> filteredData = new FilteredList<>(masterData, p -> true);
        tabela.setItems(filteredData);

        pesquisaNome.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredData.setPredicate(ingrediente -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return ingrediente.getNome().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // --- MUDANÇA: Adicionando a ação para o botão de limpar ---
        limparPesquisa.setOnAction(event -> {
            pesquisaNome.clear(); // Limpa o campo de texto
            filteredData.setPredicate(p -> true); // Reseta o filtro para mostrar tudo
        });
        // --- FIM DA MUDANÇA ---

        Label desc1 = new Label(Tela.emFrances ? "© 2025 Restaurant Monsieur-José - Système de gestion de restaurant" : "© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfontRodape1);
        Label desc2 = new Label(Tela.emFrances ? "Conçu pour l'excellence culinaire française" : "Projetado para a excelência culinária francesa");
        desc2.setFont(interfontRodape2);
        String corTextoRodape = "white";
        desc1.setStyle("-fx-text-fill: " + corTextoRodape + ";");
        desc2.setStyle("-fx-text-fill: " + corTextoRodape + ";");

        VBox descricaoRodape = new VBox(5, desc1, desc2);
        descricaoRodape.setAlignment(Pos.CENTER);
        VBox.setMargin(descricaoRodape, new Insets(20, 0, 20, 0));

        VBox root = new VBox(10, blocoTitulo, barraPesquisa, tabela, descricaoRodape);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        VBox.setVgrow(tabela, Priority.ALWAYS);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.getColumnConstraints().add(new ColumnConstraints(1000));
        grid.add(root, 0, 0);

        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)";
        grid.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        StackPane stackPane = new StackPane(scrollPane);
        stackPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        Runnable acaoVoltar = () -> new TelaServicos(super.getStage()).mostrarTela();
        BotaoVoltar.criarEPosicionar(stackPane, acaoVoltar);

        Scene scene = new Scene(stackPane);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

        return scene;
    }
}