package app;

import classes.Cliente;
import classes.Pagamento;
import classes.Reserva;
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
import java.util.function.Predicate;

public class TelaGerenciarReserva extends Tela {

    public TelaGerenciarReserva(Stage stage) {
        super(stage);
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

        ObservableList<Reserva> masterData = FXCollections.observableArrayList();
        Cliente cliente1 = new Cliente("Maria Silva", LocalDate.of(2003,2,24), "Samambaia Norte", "maria", "maria@gmail.com");
        Pagamento pagamento1 = new Pagamento(300, "pizzas", "Dinheiro", 100);
        Reserva reserva1 = new Reserva(LocalDate.of(2025,7,12), "19:30", cliente1, 5, false, pagamento1);

        Cliente cliente2 = new Cliente("João Santos", LocalDate.of(1995,5,10), "Asa Sul", "joao", "joao@example.com");
        Pagamento pagamento2 = new Pagamento(450, "jantar completo", "Cartão", 0);
        Reserva reserva2 = new Reserva(LocalDate.of(2025,7,12), "20:00", cliente2, 2, true, pagamento2);

        Cliente cliente3 = new Cliente("Ana Pereira", LocalDate.of(1988,9,30), "Lago Norte", "ana", "ana@example.com");
        Pagamento pagamento3 = new Pagamento(200, "almoço", "PIX", 50);
        Reserva reserva3 = new Reserva(LocalDate.of(2025,8,15), "12:30", cliente3, 4, false, pagamento3);
        masterData.addAll(reserva1, reserva2, reserva3);

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

        // --- MUDANÇA ---
        // Adicionando a folha de estilo dos botões à cena inteira.
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

        return scene;
    }

    private void definirPredicadoFiltro(FilteredList<Reserva> filteredData, String nome, LocalDate data) {
        filteredData.setPredicate(reserva -> {
            if ((nome == null || nome.isEmpty()) && data == null) {
                return true;
            }
            String lowerCaseFilter = nome.toLowerCase();
            boolean nomeCorresponde = nome == null || nome.isEmpty() || reserva.getCliente().getNome().toLowerCase().contains(lowerCaseFilter);
            boolean dataCorresponde = data == null || reserva.getData().equals(data);
            return nomeCorresponde && dataCorresponde;
        });
    }
}