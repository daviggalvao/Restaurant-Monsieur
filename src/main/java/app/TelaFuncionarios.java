package app;

import classes.Funcionario;
import classes.FuncionarioCargo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory; // Certifique-se deste import
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.*;

import java.time.LocalDate;

public class TelaFuncionarios extends Tela {

    public TelaFuncionarios(Stage stage) {
        super(stage);
    }

    @Override
    public Scene criarScene() { // Removi o 'void mostrarTela()' pois já está '@Override Scene criarScene()'
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Font playfairFontSubs = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 45);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        Label tituloPrincipal = new Label(Tela.emFrances ? "Employés" : "Funcionários");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(230, 3);
        sublinhado.setFill(Color.web("#FFC300"));
        sublinhado.widthProperty().bind(tituloPrincipal.widthProperty());

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0));

        Rectangle divide = new Rectangle(5, 450);
        divide.setFill(Color.web("#FFC300"));

        Label promotion = new Label(Tela.emFrances ? "Promotion " : "Promoção");
        promotion.setFont(playfairFontSubs);
        promotion.setStyle("-fx-text-fill: #FFC300;");

        TableView<Funcionario> tabela = new TableView<>();
        ObservableList<Funcionario> funcionarioList = FXCollections.observableArrayList();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Funcionario, String> nomeColuna = new TableColumn<>(Tela.emFrances ? "Nom" : "Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Funcionario, FuncionarioCargo> cargoColuna = new TableColumn<>(Tela.emFrances ? "Position" : "Cargo");
        // *** SOLUÇÃO MAIS ROBUSTA E COMUMENTE USADA: PropertyValueFactory ***
        // Assume que a classe Funcionario tem um método getCargo() que retorna FuncionarioCargo
        cargoColuna.setCellValueFactory(new PropertyValueFactory<>("cargo"));


        TableColumn<Funcionario, String> contratoColuna = new TableColumn<>(Tela.emFrances ? "Contracter" : "Contrato");
        contratoColuna.setCellValueFactory(new PropertyValueFactory<>("dataContrato"));

        TableColumn<Funcionario, Void> promoverColuna = new TableColumn<>(Tela.emFrances ? "Promouvoir" : "Promover");
        promoverColuna.setCellFactory(coluna -> new TableCell<>() {
            private final Button botao = new Button(Tela.emFrances ? "Promouvoir" : "Promover");
            {
                botao.setOnAction(event -> {
                    Funcionario func = funcionarioList.get(getIndex());
                    func.promocao();
                    tabela.refresh();
                });
            }

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

        LocalDate data = LocalDate.of(2003,2,24);
        Funcionario test = new Funcionario("Carlos", data, "Rua pinheiros 12", FuncionarioCargo.ZELADOR, 500, "3/5/2000", "carlinhosmaia", "carlinhosmaia@orkut.com");
        funcionarioList.add(test);
        tabela.setItems(funcionarioList);

        VBox promocao = new VBox(20, promotion, tabela);
        promocao.setAlignment(Pos.TOP_CENTER);
        promocao.setPadding(new Insets(0, 10, 0, 10));


        Label register = new Label(Tela.emFrances ? "Embaucher" : "Contratar");
        register.setFont(playfairFontSubs);
        register.setStyle("-fx-text-fill: #FFC300;");

        GridPane inputs = new GridPane();
        inputs.setHgap(20);
        inputs.setVgap(15);
        inputs.setAlignment(Pos.CENTER);

        String inputStyle = "-fx-background-color: white; -fx-background-radius: 5; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-border-width: 1; -fx-font-size: 14px;";

        Label lblNome = new Label(Tela.emFrances ? "\uD83D\uDC64 Nom et prénom *" : "\uD83D\uDC64 Nome Completo *");
        lblNome.setStyle("-fx-text-fill: #FFC300;");
        TextField tfNome = new TextField();
        tfNome.setPrefHeight(40);
        tfNome.setPromptText(Tela.emFrances ? "Nom complet de l'employé" : "Nome completo do funcionário");
        tfNome.setStyle(inputStyle);

        Label lblEmail = new Label("\uD83D\uDCE7 Email *");
        lblEmail.setStyle("-fx-text-fill: #FFC300;");
        TextField tfEmail = new TextField();
        tfEmail.setPrefHeight(40);
        tfEmail.setPromptText(Tela.emFrances ? "E-mail pour la connexion" : "Email para login");
        tfEmail.setStyle(inputStyle);

        Label lblSenha = new Label(Tela.emFrances ? "\uD83D\uDD11 Mot de passe *" : "\uD83D\uDD11 Senha *");
        lblSenha.setStyle("-fx-text-fill: #FFC300;");
        PasswordField tfSenha = new PasswordField();
        tfSenha.setPrefHeight(40);
        tfSenha.setPromptText(Tela.emFrances ? "Mot de passe d'accès" : "Senha de acesso");
        tfSenha.setStyle(inputStyle);

        Label lblData = new Label(Tela.emFrances ? "\uD83D\uDCC5 Date de naissance *" : "\uD83D\uDCC5 Data de Nascimento *");
        lblData.setStyle("-fx-text-fill: #FFC300;");
        DatePicker dpData = new DatePicker();
        dpData.setPrefHeight(40);
        dpData.setMinWidth(200);

        Label lblCargo = new Label(Tela.emFrances ? "\uD83D\uDCBC Position *" : "\uD83D\uDCBC Cargo *");
        lblCargo.setStyle("-fx-text-fill: #FFC300;");
        ComboBox<FuncionarioCargo> cbCargo = new ComboBox<>();
        cbCargo.getItems().addAll(FuncionarioCargo.GERENTE, FuncionarioCargo.VENDEDOR, FuncionarioCargo.CHEF, FuncionarioCargo.GARCOM, FuncionarioCargo.SUPERVISOR, FuncionarioCargo.ZELADOR);
        cbCargo.setPromptText(Tela.emFrances ? "Sélectionnez le poste" : "Selecione o cargo");
        cbCargo.setPrefHeight(40);
        cbCargo.setMinWidth(200);
        cbCargo.setStyle(inputStyle);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        inputs.getColumnConstraints().addAll(col1, col2);

        inputs.add(lblNome, 0, 0);
        inputs.add(tfNome, 1, 0);
        inputs.add(lblData, 0, 1);
        inputs.add(dpData, 1, 1);
        inputs.add(lblCargo, 0, 2);
        inputs.add(cbCargo, 1, 2);
        inputs.add(lblEmail, 0, 3);
        inputs.add(tfEmail, 1, 3);
        inputs.add(lblSenha, 0, 4);
        inputs.add(tfSenha, 1, 4);

        Button confirm = new Button(Tela.emFrances ? "Confirmer l'embauche" : "Confirmar Contratação");
        confirm.getStyleClass().add("button");
        confirm.setOnAction(mouseEvent -> {
            String nome = tfNome.getText();
            LocalDate dataNascimento = dpData.getValue();
            LocalDate hoje = LocalDate.now();
            String dataContrato = (hoje.toString());
            FuncionarioCargo cargo = cbCargo.getValue();
            String email = tfEmail.getText();
            String senha = tfSenha.getText();
            float salario;

            if (nome.isEmpty() || dataNascimento == null || cargo == null || email.isEmpty() || senha.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(Tela.emFrances ? "Champs obligatoires" : "Campos Obrigatórios");
                alert.setHeaderText(null);
                alert.setContentText(Tela.emFrances ? "Veuillez remplir tous les champs." : "Por favor, preencha todos os campos.");
                alert.showAndWait();
                return;
            }

            switch (cargo){
                case GERENTE:
                    salario = 1400;
                    break;
                case VENDEDOR:
                    salario = 1100;
                    break;
                case CHEF:
                    salario = 1300;
                    break;
                case GARCOM:
                    salario = 1100;
                    break;
                case SUPERVISOR:
                    salario = 1250;
                    break;
                case ZELADOR:
                    salario = 1000;
                    break;
                default:
                    salario = 1100;
                    break;
            }
            Funcionario hired = new Funcionario(nome, dataNascimento, "", cargo, salario, dataContrato, senha, email);
            funcionarioList.add(hired);
            tabela.setItems(funcionarioList);
            tabela.refresh();

            tfNome.clear();
            dpData.setValue(null);
            cbCargo.setValue(null);
            tfEmail.clear();
            tfSenha.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(Tela.emFrances ? "Succès" : "Sucesso");
            alert.setHeaderText(null);
            alert.setContentText(Tela.emFrances ? "Employé " + nome + " embauché avec succès!" : "Funcionário " + nome + " contratado com sucesso!");
            alert.showAndWait();
        });
        confirm.setPadding(new Insets(10, 20, 10, 20));

        VBox contrato = new VBox(20, register, inputs, confirm);
        contrato.setAlignment(Pos.TOP_CENTER);
        contrato.setPadding(new Insets(0, 10, 0, 10));
        VBox.setVgrow(inputs, Priority.ALWAYS);

        HBox total = new HBox(20, promocao, divide, contrato);
        total.setAlignment(Pos.CENTER);

        divide.heightProperty().bind(total.heightProperty().subtract(100));

        HBox.setHgrow(promocao, Priority.ALWAYS);
        HBox.setHgrow(contrato, Priority.ALWAYS);

        Label desc1 = new Label(Tela.emFrances ? "© 2025 Restaurant Monsieur-José - Système de gestion de restaurant" : "© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfontRodape1);
        Label desc2 = new Label(Tela.emFrances ? "Conçu pour l'excellence culinaire française" : "Projetado para a excelência culinária francesa");
        desc2.setFont(interfontRodape2);
        String corTextoRodape = "white";
        desc1.setStyle("-fx-text-fill: " + corTextoRodape + ";");
        desc2.setStyle("-fx-text-fill: " + corTextoRodape + ";");

        VBox descricaoRodape = new VBox(5, desc1, desc2);
        descricaoRodape.setAlignment(Pos.BOTTOM_CENTER);
        descricaoRodape.setPadding(new Insets(20, 0, 20, 0));

        VBox root = new VBox(blocoTitulo, total, descricaoRodape);
        root.setAlignment(Pos.CENTER);

        VBox.setVgrow(total, Priority.ALWAYS);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.add(root, 0, 0);
        GridPane.setHgrow(root, Priority.ALWAYS);
        GridPane.setVgrow(root, Priority.ALWAYS);

        String estiloFundoVinho = "-fx-background-color: linear-gradient(to right, #30000C, #800020);";
        grid.setStyle(estiloFundoVinho);

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle(estiloFundoVinho);

        Scene scene = new Scene(scrollPane);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

        return scene;
    }
}