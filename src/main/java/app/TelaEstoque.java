package app;

import classes.Ingrediente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage; // Necessário para o construtor, mas não para o criarScene
import java.io.UnsupportedEncodingException; // Manter se usado em outras partes não visíveis
import java.net.URLEncoder; // Manter se usado em outras partes não visíveis
import java.nio.charset.StandardCharsets; // Manter se usado em outras partes não visíveis

public class TelaEstoque extends Tela { // Já estende Tela

    public TelaEstoque(Stage stage) {
        super(stage);
    }

    @Override
    public Scene criarScene() { // MUDANÇA AQUI: de void mostrarTela() para Scene criarScene()
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        // --- Título Principal ---
        Label tituloPrincipal = new Label(Tela.emFrances ? "Stock" : "Estoque"); // Usa Tela.emFrances
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(190, 3);
        sublinhado.setFill(Color.web("#FFC300"));

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0));

        // --- Tabela de Estoque ---
        TableView<Ingrediente> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Ingrediente, String> idColuna = new TableColumn<>("ID");
        idColuna.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Ingrediente, String> nomeColuna = new TableColumn<>(Tela.emFrances ? "Ingredient" : "Ingrediente"); // Usa Tela.emFrances
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Ingrediente, Float> precoColuna = new TableColumn<>(Tela.emFrances ? "Prix (R$)" : "Preço (R$)"); // Usa Tela.emFrances
        precoColuna.setCellValueFactory(new PropertyValueFactory<>("preco"));

        TableColumn<Ingrediente, Integer> quantidadeColuna = new TableColumn<>(Tela.emFrances ? "Montant" : "Quantidade"); // Usa Tela.emFrances
        quantidadeColuna.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        TableColumn<Ingrediente, String> validadeColuna = new TableColumn<>(Tela.emFrances ? "Validité" : "Validade"); // Usa Tela.emFrances
        validadeColuna.setCellValueFactory(new PropertyValueFactory<>("validade"));

        tabela.getColumns().addAll(idColuna, nomeColuna, precoColuna, quantidadeColuna, validadeColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        ObservableList<Ingrediente> dados = FXCollections.observableArrayList(
                new Ingrediente(1L, "Tomate Italiano", 8.50f, 20, "15/06/2025"),
                new Ingrediente(2L, "Queijo Mussarela", 45.00f, 15, "30/07/2025"),
                new Ingrediente(3L, "Farinha de Trigo", 5.20f, 50, "01/12/2026"),
                new Ingrediente(4L, "Filé Mignon (kg)", 95.80f, 30, "10/06/2025"),
                new Ingrediente(5L, "Vinho Tinto Seco", 75.00f, 40, "N/A"),
                new Ingrediente(6L, "Manjericão Fresco", 15.00f, 5, "09/06/2025")
        );
        tabela.setItems(dados);

        // --- Rodapé ---
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

        // --- Layout Principal ---
        VBox root = new VBox(10, blocoTitulo, tabela, descricaoRodape);
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

        Scene scene = new Scene(scrollPane);

        // REMOVIDO: stage.setTitle, stage.setMaximized, stage.setScene, stage.setMinWidth, etc.
        // super.getStage().setTitle("Estoque");
        // super.getStage().setMaximized(true);
        // super.getStage().setScene(scene);
        // super.getStage().setMinWidth(800);
        // super.getStage().setMinHeight(600);
        // super.getStage().show();

        return scene; // RETORNA A SCENE
    }
}