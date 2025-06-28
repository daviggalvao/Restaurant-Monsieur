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

import java.util.List; // Import necessário

public class TelaEstoque extends Tela {

    public TelaEstoque(Stage stage) {
        super(stage);
    }

    @Override
    public Scene criarScene() {
        // ... (Seu código inicial da UI permanece o mesmo) ...
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
        TextField pesquisaNome = new TextField();
        pesquisaNome.setPromptText(Tela.emFrances ? "Rechercher par nom" : "Pesquisar por nome");
        pesquisaNome.setMinWidth(300);
        Button limparPesquisa = new Button(Tela.emFrances ? "Nettoyer" : "Limpar");
        HBox barraPesquisa = new HBox(10, pesquisaNome, limparPesquisa);
        barraPesquisa.setAlignment(Pos.CENTER);
        VBox.setMargin(barraPesquisa, new Insets(0, 0, 20, 0));
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
                // --- MUDANÇA 1: LÓGICA DO BOTÃO ATUALIZADA ---
                botao.setOnAction(event -> {
                    Ingrediente inc = getTableView().getItems().get(getIndex());
                    if (inc.precisaRepor()) {
                        // Chama o método e verifica se a encomenda foi bem-sucedida
                        boolean sucesso = inc.encomendaIngrediente(10);
                        if (sucesso) {
                            // Se teve sucesso, apenas atualiza a tabela para mostrar a nova quantidade
                            getTableView().refresh();
                        } else {
                            // Se falhou (ex: saldo insuficiente), mostra um alerta ao utilizador
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Falha na Encomenda");
                            alert.setHeaderText("Não foi possível realizar a encomenda.");
                            alert.setContentText("Verifique o saldo da conta ou contacte o suporte.");
                            alert.showAndWait();
                        }
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : botao);
            }
        });

        tabela.getColumns().addAll(nomeColuna, precoColuna, quantidadeColuna, pedirColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        // --- MUDANÇA 2: CARREGAR DADOS DO BANCO DE DADOS ---
        // A lista de dados fixos foi removida e substituída por uma chamada ao método estático.
        List<Ingrediente> ingredientesDoBanco = Ingrediente.listarTodos();
        ObservableList<Ingrediente> masterData = FXCollections.observableArrayList(ingredientesDoBanco);

        FilteredList<Ingrediente> filteredData = new FilteredList<>(masterData, p -> true);
        tabela.setItems(filteredData);

        // ... (O resto do seu código da UI permanece o mesmo) ...
        pesquisaNome.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredData.setPredicate(ingrediente -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return ingrediente.getNome().toLowerCase().contains(lowerCaseFilter);
            });
        });
        limparPesquisa.setOnAction(event -> {
            pesquisaNome.clear();
            filteredData.setPredicate(p -> true);
        });
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