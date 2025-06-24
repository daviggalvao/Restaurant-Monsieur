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

import java.time.LocalDate;

public class TelaClientes extends Tela {

    public TelaClientes(Stage stage) {
        super(stage);
    }

    @Override
    public Scene criarScene() {
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        // --- Título Principal ---
        Label tituloPrincipal = new Label(Tela.emFrances ? "Clients" : "Clientes");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(230, 3);
        sublinhado.setFill(Color.web("#FFC300"));

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0));

        // --- Tabela ---
        TableView<Cliente> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Cliente, String> nomeColuna = new TableColumn<>(Tela.emFrances ? "Nom" : "Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Cliente, String> idColuna = new TableColumn<>("ID");
        idColuna.setCellValueFactory(new PropertyValueFactory<>("Id"));
        TableColumn<Cliente, String> fidelidadeColuna = new TableColumn<>(Tela.emFrances ? "Fidelité" : "Fidelidade");
        fidelidadeColuna.setCellValueFactory(new PropertyValueFactory<>("fidelidade"));
        TableColumn<Cliente, String> aniversarioColuna = new TableColumn<>(Tela.emFrances ? "Anniversaire" : "Aniversário");
        aniversarioColuna.setCellValueFactory(new PropertyValueFactory<>("dataAniversario"));
        TableColumn<Cliente, String> enderecoColuna = new TableColumn<>(Tela.emFrances ? "Adresse" : "Endereço");
        enderecoColuna.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        tabela.getColumns().addAll(nomeColuna, idColuna, fidelidadeColuna, aniversarioColuna, enderecoColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        Cliente test = new Cliente("Maria", LocalDate.of(2003, 2, 24), "Samambaia Norte q.2", "interdelixao", "mariazinha@outlook.com");
        tabela.setItems(FXCollections.observableArrayList(test));

        // --- Rodapé ---
        Label desc1 = new Label("© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfontRodape1);
        Label desc2 = new Label("Projetado para a excelência culinária francesa");
        desc2.setFont(interfontRodape2);
        desc1.setStyle("-fx-text-fill: white;");
        desc2.setStyle("-fx-text-fill: white;");

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

        // --- MUDANÇAS PARA ADICIONAR O BOTÃO VOLTAR ---
        // 1. Envolver o layout principal em um StackPane
        StackPane stackPane = new StackPane(scrollPane);
        stackPane.setStyle("-fx-background-color: " + estiloFundoVinho); // Garante o fundo correto

        // 2. Definir a ação de voltar para a TelaGerente
        Runnable acaoVoltar = () -> new TelaGerente(super.getStage()).mostrarTela();

        // 3. Adicionar o botão ao StackPane
        BotaoVoltar.criarEPosicionar(stackPane, acaoVoltar);

        // 4. Criar a cena com o StackPane como raiz
        Scene scene = new Scene(stackPane);

        // Lógica de responsividade
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < 1200) {
                tituloPrincipal.setFont(Font.font(playfairFontTitulo.getFamily(), 42));
                sublinhado.setWidth(190);
            } else {
                tituloPrincipal.setFont(playfairFontTitulo);
                sublinhado.setWidth(230);
            }
        });

        return scene;
    }
}