package app;

import classes.Funcionario;
import classes.FuncionarioCargo;
import database.FirebaseCliente;
import database.FirebaseFuncionario;
import database.FirebaseReserva;
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
import javafx.animation.*;
import javafx.util.Callback;

public class TelaFuncionarios{
        private Stage stage;

        /**
         * Construtor da TelaFuncionarios.
         * @param stage O palco principal da aplicação.
         */
        public TelaFuncionarios(Stage stage) {this.stage = stage;}

        public void mostrarTelaFuncionarios() { //
            Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50); //
            Font playfairFontSubs = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 45); //
            Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15); //
            Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17); //

            // --- Título Principal ---
            Label tituloPrincipal = new Label("Funcionários"); //
            tituloPrincipal.setFont(playfairFontTitulo); //
            tituloPrincipal.setStyle("-fx-text-fill: #FFC300;"); // Cor do título: amarelo

            Rectangle sublinhado = new Rectangle(230, 3); //
            sublinhado.setFill(Color.web("#FFC300")); // Cor do sublinhado: amarelo

            VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado); //
            blocoTitulo.setAlignment(Pos.CENTER); //
            VBox.setMargin(tituloPrincipal, new Insets(0, 0, 0, 0)); //
            VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0)); //

            Rectangle divide = new Rectangle(5, 450); //
            divide.setFill(Color.web("#FFC300")); // Cor do sublinhado: amarelo

            Label promotion = new Label("Promoção");
            promotion.setFont(playfairFontSubs);
            promotion.setStyle("-fx-text-fill: #FFC300;");
            Label register = new Label("Contratar");
            register.setFont(playfairFontSubs);
            register.setStyle("-fx-text-fill: #FFC300;");

            TableView<Funcionario> tabela= new TableView<Funcionario>();

            tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            TableColumn<Funcionario, String> nomeColuna = new TableColumn<>("Nome");
            nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

            TableColumn<Funcionario, FuncionarioCargo>cargoColuna = new TableColumn<>("Cargo");
            cargoColuna.setCellValueFactory(new PropertyValueFactory<>("cargo"));

            TableColumn<Funcionario, String> contratoColuna = new TableColumn<>("Contrato");
            contratoColuna.setCellValueFactory(new PropertyValueFactory<>("dataContrato"));

            TableColumn<Funcionario, Void> promoverColuna = new TableColumn<>("Promover");
            promoverColuna.setCellFactory(coluna -> new TableCell<Funcionario, Void>() {
                private final Button botao = new Button("Promover");
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

            tabela.getColumns().addAll(nomeColuna, cargoColuna, contratoColuna, promoverColuna);
            tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

            Funcionario test = new Funcionario("Carlos", "24/2/1980", "Rua pinheiros 12", 45, FuncionarioCargo.CHEF, 500, "3/5/2000");

            ObservableList<Funcionario> FuncionarioList = FXCollections.observableArrayList(test);
            tabela.setItems(FuncionarioList);

            GridPane inputs = new GridPane();
            inputs.setHgap(20);
            inputs.setVgap(15);
            inputs.setPadding(new Insets(20, 20, 20, 20));
            inputs.setAlignment(Pos.CENTER);
            inputs.setStyle("-fx-background-color: transparent");


            String inputStyle = "-fx-background-color: white;\n" +
                    "    -fx-background-radius: 5px;\n" +
                    "    -fx-border-color: #ddd;\n" +
                    "    -fx-border-radius: 5px;\n" +
                    "    -fx-border-width: 1px;\n" +
                    "    -fx-font-size: 14px;\n" +
                    "    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);";


            Label lblNome = new Label("\uD83D\uDC64 Nome Completo *");
            lblNome.setStyle("-fx-text-fill: #FFC300;");
            TextField tfNome = new TextField();
            tfNome.setPrefHeight(40);
            tfNome.setMinWidth(300);
            tfNome.setPromptText("Seu nome completo");
            tfNome.setStyle(inputStyle);

            Label lblData = new Label("\uD83D\uDCC5 Data *");
            lblData.setStyle("-fx-text-fill: #FFC300;");
            DatePicker dpData = new DatePicker();
            dpData.setPrefHeight(40);

            Label lblCargo = new Label("\u23F0 Cargo *");
            lblCargo.setStyle("-fx-text-fill: #FFC300;");
            ComboBox<FuncionarioCargo> cbHorario = new ComboBox<>();
            cbHorario.getItems().addAll(FuncionarioCargo.GERENTE, FuncionarioCargo.VENDEDOR, FuncionarioCargo.CHEF, FuncionarioCargo.GARCOM, FuncionarioCargo.SUPERVISOR    );
            cbHorario.setPromptText("Selecione o cargo");
            cbHorario.setPrefHeight(40);
            cbHorario.setStyle(inputStyle);

            inputs.add(lblNome, 0, 0);
            inputs.add(tfNome, 1, 0);
            inputs.add(lblData, 0, 1);
            inputs.add(dpData, 1, 1);
            inputs.add(lblCargo, 0, 2);
            inputs.add(cbHorario, 1, 2);
            inputs.setMinWidth(300);


            Button confirm = new Button("Confirmar");
            confirm.getStyleClass().add("button");

            VBox promocao = new VBox(5, promotion, tabela);
            promocao.setAlignment(Pos.CENTER);
            promocao.setStyle("-fx-background-color: transparent");
            promocao.setPrefWidth(1000);
            VBox.setVgrow(promocao, Priority.ALWAYS);
            VBox contrato = new VBox(5, register, inputs, confirm);
            contrato.setStyle("-fx-background-color: transparent");
            contrato.setAlignment(Pos.CENTER);
            contrato.setPrefWidth(1000);
            VBox.setVgrow(contrato, Priority.ALWAYS);
            contrato.setSpacing(80);

            HBox total = new HBox(5, promocao, divide, contrato);
            total.setStyle("-fx-background-color: transparent");
            total.setPrefHeight(600);
            total.setPrefWidth(2000);
            total.setMaxWidth(2000);
            total.setMinWidth(1900);
            total.setAlignment(Pos.CENTER);
            HBox.setMargin(divide, new Insets(100, 100, 100, 100));
            promocao.setMaxWidth(Double.MAX_VALUE);
            contrato.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(promocao, Priority.ALWAYS);
            HBox.setHgrow(contrato, Priority.ALWAYS);

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
            descricaoRodape.setAlignment(Pos.BOTTOM_CENTER); //
            VBox.setMargin(descricaoRodape, new Insets(20, 0, 20, 0)); //

            // --- Layout Principal com VBox root (rodapé rolável) ---
            VBox root = new VBox(10, blocoTitulo, total, descricaoRodape); //
            root.setAlignment(Pos.CENTER); //
            root.setPadding(new Insets(20)); //


            VBox.setVgrow(blocoTitulo, Priority.NEVER); //
            VBox.setVgrow(descricaoRodape, Priority.NEVER); //

            GridPane grid = new GridPane(); //
            grid.setAlignment(Pos.CENTER_LEFT); //
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
            scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

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

            stage.setTitle("Funcionários"); //
            stage.setMaximized(true); //
            stage.setScene(scene); //
            stage.setMinWidth(800); //
            stage.setMinHeight(600); //
            stage.show(); //
        }
    }
