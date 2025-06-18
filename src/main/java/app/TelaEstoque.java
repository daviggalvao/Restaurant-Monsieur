package app;

import classes.Ingrediente; // Importa a classe de modelo fornecida
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
import javafx.stage.Stage;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Tela para exibir e gerenciar o estoque de ingredientes,
 * com um layout e estilo baseados na TelaClientes.
 */
public class TelaEstoque extends Tela {

    public TelaEstoque(Stage stage) {
        super(stage);
    }

    @Override
    public void mostrarTela() {
        // Carregamento de fontes, igual à TelaClientes
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        // --- Título Principal ---
        Label tituloPrincipal = new Label("Estoque"); // Título da nova tela
        tituloPrincipal.setFont(playfairFontTitulo); //
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;"); //

        Rectangle sublinhado = new Rectangle(190, 3); // Largura ajustada para o texto "Estoque"
        sublinhado.setFill(Color.web("#FFC300")); //

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado); //
        blocoTitulo.setAlignment(Pos.CENTER); //
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0)); //

        // --- Tabela de Estoque ---
        TableView<Ingrediente> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //

        // --- Definição das Colunas baseadas na classe Ingrediente ---
        // As strings "id", "nome", etc., devem corresponder exatamente aos nomes dos métodos getter na classe Ingrediente (ex: getId(), getNome())
        TableColumn<Ingrediente, String> idColuna = new TableColumn<>("ID");
        idColuna.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Ingrediente, String> nomeColuna = new TableColumn<>("Ingrediente");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Ingrediente, Float> precoColuna = new TableColumn<>("Preço (R$)");
        precoColuna.setCellValueFactory(new PropertyValueFactory<>("preco"));

        TableColumn<Ingrediente, Integer> quantidadeColuna = new TableColumn<>("Quantidade");
        quantidadeColuna.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        TableColumn<Ingrediente, String> validadeColuna = new TableColumn<>("Validade");
        validadeColuna.setCellValueFactory(new PropertyValueFactory<>("validade"));

        tabela.getColumns().addAll(idColuna, nomeColuna, precoColuna, quantidadeColuna, validadeColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        // --- Dados de Exemplo para a Tabela ---
        ObservableList<Ingrediente> dados = FXCollections.observableArrayList(
                new Ingrediente(1L, "Tomate Italiano", 8.50f, 20, "15/06/2025"),
                new Ingrediente(2L, "Queijo Mussarela", 45.00f, 15, "30/07/2025"),
                new Ingrediente(3L, "Farinha de Trigo", 5.20f, 50, "01/12/2026"),
                new Ingrediente(4L, "Filé Mignon (kg)", 95.80f, 30, "10/06/2025"),
                new Ingrediente(5L, "Vinho Tinto Seco", 75.00f, 40, "N/A"),
                new Ingrediente(6L, "Manjericão Fresco", 15.00f, 5, "09/06/2025")
        );
        tabela.setItems(dados);

        // --- Rodapé (igual ao da TelaClientes) ---
        Label desc1 = new Label("© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante"); //
        desc1.setFont(interfontRodape1); //
        Label desc2 = new Label("Projetado para a excelência culinária francesa"); //
        desc2.setFont(interfontRodape2); //
        String corTextoRodape = "white"; //
        desc1.setStyle("-fx-text-fill: " + corTextoRodape + ";"); //
        desc2.setStyle("-fx-text-fill: " + corTextoRodape + ";"); //

        VBox descricaoRodape = new VBox(5, desc1, desc2); //
        descricaoRodape.setAlignment(Pos.CENTER); //
        VBox.setMargin(descricaoRodape, new Insets(20, 0, 20, 0)); //

        // --- Layout Principal (igual ao da TelaClientes) ---
        VBox root = new VBox(10, blocoTitulo, tabela, descricaoRodape); //
        root.setAlignment(Pos.CENTER); //
        root.setPadding(new Insets(20)); //
        VBox.setVgrow(tabela, Priority.ALWAYS);

        GridPane grid = new GridPane(); //
        grid.setAlignment(Pos.CENTER); //
        grid.getColumnConstraints().add(new ColumnConstraints(1000)); //
        grid.add(root, 0, 0); //

        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)"; //
        grid.setStyle("-fx-background-color: " + estiloFundoVinho + ";"); //

        ScrollPane scrollPane = new ScrollPane(grid); //
        scrollPane.setFitToWidth(true); //
        scrollPane.setFitToHeight(true); //
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //
        scrollPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";"); //

        Scene scene = new Scene(scrollPane); //

        super.getStage().setTitle("Estoque"); //
        super.getStage().setMaximized(true); //
        super.getStage().setScene(scene); //
        super.getStage().setMinWidth(800); //
        super.getStage().setMinHeight(600); //
        super.getStage().show(); //
    }
}