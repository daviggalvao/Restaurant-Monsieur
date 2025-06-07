package app;

import classes.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.*;

public class TelaClientes {
    private Stage stage;

    /**
     * Construtor da TelaCliente.
     * @param stage O palco principal da aplicação.
     */
    public TelaClientes(Stage stage) {this.stage = stage;}


    public void mostrarTelaCliente() { //
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50); //
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15); //
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17); //

        // --- Título Principal ---
        Label tituloPrincipal = new Label("Clientes"); //
        tituloPrincipal.setFont(playfairFontTitulo); //
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;"); // Cor do título: amarelo

        Rectangle sublinhado = new Rectangle(230, 3); //
        sublinhado.setFill(Color.web("#FFC300")); // Cor do sublinhado: amarelo

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado); //
        blocoTitulo.setAlignment(Pos.CENTER); //
        VBox.setMargin(tituloPrincipal, new Insets(0, 0, 0, 0)); //
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0)); //

        TableView<Cliente> tabela= new TableView<Cliente>();

        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Cliente, String> nomeColuna = new TableColumn<>("Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Cliente, String>idColuna = new TableColumn<>("ID");
        idColuna.setCellValueFactory(new PropertyValueFactory<>("Id"));

        TableColumn<Cliente, String> fidelidadeColuna = new TableColumn<>("Fidelidade");
        fidelidadeColuna.setCellValueFactory(new PropertyValueFactory<>("fidelidade"));

        TableColumn<Cliente, String> aniversarioColuna = new TableColumn<>("Aniversário");
        aniversarioColuna.setCellValueFactory(new PropertyValueFactory<>("dataAniversario"));

        TableColumn<Cliente, String> enderecoColuna = new TableColumn<>("Endereço");
        enderecoColuna.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        

        tabela.getColumns().addAll(nomeColuna, idColuna, fidelidadeColuna, aniversarioColuna, enderecoColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        Cliente test = new Cliente("Maria", "24/2/2003", "Samambaia Norte q.2");

        ObservableList<Cliente> ClienteList = FXCollections.observableArrayList(test);

        tabela.setItems(ClienteList);

        // --- Rodapé ---
        Label desc1 = new Label("© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante"); //
        desc1.setFont(interfontRodape1); //
        Label desc2 = new Label("Projetado para a excelência culinária francesa"); //
        desc2.setFont(interfontRodape2); //
        // MODIFICAÇÃO: Cor do texto do rodapé alterada para BRANCO
        String corTextoRodape = "white";
        desc1.setStyle("-fx-text-fill: " + corTextoRodape + ";"); //
        desc2.setStyle("-fx-text-fill: " + corTextoRodape + ";"); //

        VBox descricaoRodape = new VBox(5, desc1, desc2); //
        descricaoRodape.setAlignment(Pos.CENTER); //
        VBox.setMargin(descricaoRodape, new Insets(20, 0, 20, 0)); //

        // --- Layout Principal com VBox root (rodapé rolável) ---
        VBox root = new VBox(10, blocoTitulo, tabela, descricaoRodape); //
        root.setAlignment(Pos.CENTER); //
        root.setPadding(new Insets(20)); //

        VBox.setVgrow(blocoTitulo, Priority.NEVER); //
        VBox.setVgrow(descricaoRodape, Priority.NEVER); //

        GridPane grid = new GridPane(); //
        grid.setAlignment(Pos.CENTER); //
        grid.getColumnConstraints().add(new ColumnConstraints(1000)); //
        grid.add(root, 0, 0); //

        // Fundo da tela: vinho
        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)"; //
        grid.setStyle("-fx-background-color: " + estiloFundoVinho + ";"); //

        ScrollPane scrollPane = new ScrollPane(grid); //
        scrollPane.setFitToWidth(true); //
        scrollPane.setFitToHeight(true); //
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); //
        scrollPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";"); // Fundo do ScrollPane vinho

        Scene scene = new Scene(scrollPane); //

        // Lógica de responsividade
        scene.widthProperty().addListener((obs, oldVal, newVal) -> { //
            if (newVal.doubleValue() < 1200) { //
                tituloPrincipal.setFont(Font.font(playfairFontTitulo.getFamily(), 52)); //
                sublinhado.setWidth(190); //
            } else { //
                tituloPrincipal.setFont(playfairFontTitulo); //
                sublinhado.setWidth(230); //
            }
        });

        stage.setTitle("Clientes"); //
        stage.setMaximized(true); //
        stage.setScene(scene); //
        stage.setMinWidth(800); //
        stage.setMinHeight(600); //
        stage.show(); //
    }
}
