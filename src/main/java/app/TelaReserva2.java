package app;

import classes.Cliente;
import classes.Pagamento;
import classes.Reserva;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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

public class TelaReserva2 extends Tela {

    /**
     * Construtor da TelaCliente.
     * @param stage O palco principal da aplicação.
     */
    public TelaReserva2(Stage stage) {super(stage);}

    @Override
    public void mostrarTela() { //
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50); //
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15); //
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17); //

        // --- Título Principal ---
        Label tituloPrincipal = new Label("Reservas"); //
        tituloPrincipal.setFont(playfairFontTitulo); //
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;"); // Cor do título: amarelo

        Rectangle sublinhado = new Rectangle(230, 3); //
        sublinhado.setFill(Color.web("#FFC300")); // Cor do sublinhado: amarelo

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado); //
        blocoTitulo.setAlignment(Pos.CENTER); //
        VBox.setMargin(tituloPrincipal, new Insets(0, 0, 0, 0)); //
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0)); //

        TableView<Reserva> tabela= new TableView<Reserva>();

        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Reserva, String> nomeColuna = new TableColumn<>("Cliente");
        nomeColuna.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getCliente().getNome()));

        TableColumn<Reserva, String>dataColuna = new TableColumn<>("Data");
        dataColuna.setCellValueFactory(new PropertyValueFactory<>("data"));

        TableColumn<Reserva, String> timeColuna = new TableColumn<>("Horário");
        timeColuna.setCellValueFactory(new PropertyValueFactory<>("horario"));

        TableColumn<Reserva, String> qtdColuna = new TableColumn<>("N°Pessoas");
        qtdColuna.setCellValueFactory(cellNum -> new SimpleStringProperty(String.valueOf(cellNum.getValue().getQuantidadePessoas())));

        TableColumn<Reserva, String> choferColuna = new TableColumn<>("Chofer");
        choferColuna.setCellValueFactory(value-> new SimpleStringProperty(value.getValue().getChofer() ? "Sim" : "Não"));


        tabela.getColumns().addAll(nomeColuna, dataColuna, timeColuna, qtdColuna, choferColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        LocalDate data = LocalDate.of(2003,2,24);
        Cliente test1 = new Cliente("Maria", data, "Samambaia Norte q.2", "maria", "maria@gmail.com");
        Pagamento test2 = new Pagamento(300, "pizzas", "Dinheiro", 100);
        LocalDate datas= LocalDate.of(2025,7,12);
        Reserva test = new Reserva(datas, "19:30", test1, 5, false, test2);

        ObservableList<Reserva> ReservaList = FXCollections.observableArrayList(test);

        tabela.setItems(ReservaList);

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

        super.getStage().setTitle("Reservas"); //
        super.getStage().setMaximized(true); //
        super.getStage().setScene(scene); //
        super.getStage().setMinWidth(800); //
        super.getStage().setMinHeight(600); //
        super.getStage().show(); //
    }
}