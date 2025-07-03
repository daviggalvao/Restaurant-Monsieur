package app;

import classes.Cliente;
import classes.Pagamento;
import classes.Reserva;
import database.JpaUtil;
import jakarta.persistence.EntityManager;
import javafx.beans.property.SimpleStringProperty;
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

import java.time.LocalDate;
import java.util.List;

public class TelaGerenciarReserva extends Tela {

    public TelaGerenciarReserva(Stage stage) {
        super(stage);
    }

    private ObservableList<Reserva> carregarReservasDoBanco() {
        EntityManager em = JpaUtil.getFactory().createEntityManager();
        try {
            List<Reserva> reservas = em.createQuery("SELECT r FROM Reserva r JOIN FETCH r.cliente", Reserva.class).getResultList();
            return FXCollections.observableArrayList(reservas);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Carregamento");
            alert.setHeaderText("Falha ao carregar dados do banco.");
            alert.setContentText("Não foi possível carregar as reservas. Verifique a conexão com o banco de dados e tente novamente.");
            alert.showAndWait();
            return FXCollections.observableArrayList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Scene criarScene() {
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        Label tituloPrincipal = new Label(Tela.emFrances ? "Réservations" : "Reservas");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(230, 3);
        sublinhado.setFill(Color.web("#FFC300"));

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 20, 0));

        TextField pesquisaNome = new TextField();
        pesquisaNome.setPromptText(Tela.emFrances ? "Rechercher par nom" : "Pesquisar por nome");
        pesquisaNome.setMinWidth(250);

        DatePicker pesquisaData = new DatePicker();
        pesquisaData.setPromptText(Tela.emFrances ? "Rechercher par date" : "Pesquisar por data");

        Button limparPesquisa = new Button(Tela.emFrances ? "Nettoyer" : "Limpar");

        HBox barraPesquisa = new HBox(15, pesquisaNome, pesquisaData, limparPesquisa);
        barraPesquisa.setAlignment(Pos.CENTER);
        VBox.setMargin(barraPesquisa, new Insets(0, 0, 20, 0));

        TableView<Reserva> tabela= new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setPlaceholder(new Label("Nenhuma reserva encontrada no banco de dados."));

        TableColumn<Reserva, String> nomeColuna = new TableColumn<>(Tela.emFrances ? "Client" : "Cliente");
        nomeColuna.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getCliente().getNome()));
        TableColumn<Reserva, String> dataColuna = new TableColumn<>(Tela.emFrances ? "Date" : "Data");
        dataColuna.setCellValueFactory(new PropertyValueFactory<>("data"));
        TableColumn<Reserva, String> timeColuna = new TableColumn<>(Tela.emFrances ? "Temps" : "Horário");
        timeColuna.setCellValueFactory(new PropertyValueFactory<>("horario"));
        TableColumn<Reserva, String> qtdColuna = new TableColumn<>(Tela.emFrances ? "N° Personnes" : "N°Pessoas");
        qtdColuna.setCellValueFactory(cellNum -> new SimpleStringProperty(String.valueOf(cellNum.getValue().getQuantidadePessoas())));
        TableColumn<Reserva, String> choferColuna = new TableColumn<>(Tela.emFrances ? "Chauffeur" : "Chofer");
        choferColuna.setCellValueFactory(value-> new SimpleStringProperty(value.getValue().getChofer() ? (Tela.emFrances ? "Oui" : "Sim") : (Tela.emFrances ? "Non" : "Não")));

        tabela.getColumns().addAll(nomeColuna, dataColuna, timeColuna, qtdColuna, choferColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        ObservableList<Reserva> masterData = carregarReservasDoBanco();

        FilteredList<Reserva> filteredData = new FilteredList<>(masterData, p -> true);
        tabela.setItems(filteredData);

        pesquisaNome.textProperty().addListener((obs, oldValue, newValue) -> {
            definirPredicadoFiltro(filteredData, newValue, pesquisaData.getValue());
        });

        pesquisaData.valueProperty().addListener((obs, oldValue, newValue) -> {
            definirPredicadoFiltro(filteredData, pesquisaNome.getText(), newValue);
        });

        limparPesquisa.setOnAction(event -> {
            pesquisaNome.clear();
            pesquisaData.setValue(null);
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
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < 1200) {
                tituloPrincipal.setFont(Font.font(playfairFontTitulo.getFamily(), 42));
                sublinhado.setWidth(190);
            } else {
                tituloPrincipal.setFont(playfairFontTitulo);
                sublinhado.setWidth(230);
            }
        });

        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

        return scene;
    }

    private void definirPredicadoFiltro(FilteredList<Reserva> filteredData, String nome, LocalDate data) {
        filteredData.setPredicate(reserva -> {
            boolean nomeOk = (nome == null || nome.isEmpty());
            boolean dataOk = (data == null);

            if (nomeOk && dataOk) {
                return true;
            }

            boolean nomeCorresponde = nomeOk || reserva.getCliente().getNome().toLowerCase().contains(nome.toLowerCase());
            boolean dataCorresponde = dataOk || reserva.getData().equals(data);

            if (!nomeOk && !dataOk) {
                return nomeCorresponde && dataCorresponde;
            } else if (!nomeOk) {
                return nomeCorresponde;
            } else {
                return dataCorresponde;
            }
        });
    }
}