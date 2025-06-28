package app;

import classes.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.*;
import jakarta.persistence.EntityManager; // <-- MUDANÇA
import database.JpaUtil;             // <-- MUDANÇA

import java.time.LocalDate;
import java.util.List;

public class TelaClientes extends Tela {

    public TelaClientes(Stage stage) {
        super(stage);
    }

    @Override
    public Scene criarScene() {
        // ... (seu código de configuração de fontes, títulos, etc. permanece o mesmo)
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        Label tituloPrincipal = new Label(Tela.emFrances ? "Clients" : "Clientes");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(230, 3);
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

        TableView<Cliente> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Cliente, String> nomeColuna = new TableColumn<>(Tela.emFrances ? "Nom" : "Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Cliente, String> fidelidadeColuna = new TableColumn<>("Email");
        fidelidadeColuna.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Cliente, String> aniversarioColuna = new TableColumn<>(Tela.emFrances ? "Anniversaire" : "Aniversário");
        aniversarioColuna.setCellValueFactory(new PropertyValueFactory<>("dataAniversario"));
        TableColumn<Cliente, String> enderecoColuna = new TableColumn<>(Tela.emFrances ? "Adresse" : "Endereço");
        enderecoColuna.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        tabela.getColumns().addAll(nomeColuna, fidelidadeColuna, aniversarioColuna, enderecoColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        ObservableList<Cliente> masterData = FXCollections.observableArrayList();
        EntityManager em = JpaUtil.getFactory().createEntityManager();

        try {
            // Bloco para popular o banco de dados com dados iniciais se estiver vazio
            em.getTransaction().begin();
            long totalClientes = em.createQuery("SELECT COUNT(c) FROM Cliente c", Long.class).getSingleResult();
            if (totalClientes == 0) {
                System.out.println("Banco de dados vazio. Populando com dados iniciais...");
                em.persist(new Cliente("Maria Silva", LocalDate.of(2003, 2, 24), "Samambaia Norte Q.2", "Ouro", "mariazinha@outlook.com"));
                em.persist(new Cliente("João Santos", LocalDate.of(1995, 5, 10), "Asa Sul", "Prata", "joao.santos@email.com"));
                em.persist(new Cliente("Ana Pereira", LocalDate.of(1988, 9, 30), "Lago Norte", "Bronze", "ana.pereira@email.com"));
                em.persist(new Cliente("Carlos Souza", LocalDate.of(1999, 7, 15), "Taguatinga Centro", "Ouro", "carlos@email.com"));
            }
            em.getTransaction().commit();
            List<Cliente> clientesDoBanco = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();

            masterData.addAll(clientesDoBanco);

        } finally {
            if (em != null) {
                em.close();
            }
        }

        FilteredList<Cliente> filteredData = new FilteredList<>(masterData, p -> true);
        tabela.setItems(filteredData);

        pesquisaNome.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredData.setPredicate(cliente -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return cliente.getNome().toLowerCase().contains(lowerCaseFilter);
            });
        });

        limparPesquisa.setOnAction(event -> {
            pesquisaNome.clear();
            filteredData.setPredicate(p -> true);
        });

        // ... (o restante do seu código para rodapé, layout, scene, etc., permanece o mesmo)
        Label desc1 = new Label("© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfontRodape1);
        Label desc2 = new Label("Projetado para a excelência culinária francesa");
        desc2.setFont(interfontRodape2);
        desc1.setStyle("-fx-text-fill: white;");
        desc2.setStyle("-fx-text-fill: white;");

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
        stackPane.setStyle("-fx-background-color: " + estiloFundoVinho);

        Runnable acaoVoltar = () -> new TelaGerente(super.getStage()).mostrarTela();
        BotaoVoltar.criarEPosicionar(stackPane, acaoVoltar);

        Scene scene = new Scene(stackPane);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

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