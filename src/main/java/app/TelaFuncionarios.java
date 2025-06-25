package app;

import classes.Funcionario;
import classes.FuncionarioCargo;
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

public class TelaFuncionarios extends Tela {

    public TelaFuncionarios(Stage stage) {
        super(stage);
    }

    @Override
    public Scene criarScene() {
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Font playfairFontSubs = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 45);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        // --- Títulos ---
        Label tituloPrincipal = new Label(Tela.emFrances ? "Employés" : "Funcionários");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");
        Rectangle sublinhado = new Rectangle(230, 3);
        sublinhado.setFill(Color.web("#FFC300"));
        sublinhado.widthProperty().bind(tituloPrincipal.widthProperty());
        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0));

        // --- Painel de Promoção ---
        Label promotion = new Label(Tela.emFrances ? "Promotion" : "Promoção");
        promotion.setFont(playfairFontSubs);
        promotion.setStyle("-fx-text-fill: #FFC300;");

        TableView<Funcionario> tabela = new TableView<>();
        ObservableList<Funcionario> funcionarioList = FXCollections.observableArrayList();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Funcionario, String> nomeColuna = new TableColumn<>(Tela.emFrances ? "Nom" : "Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Funcionario, FuncionarioCargo> cargoColuna = new TableColumn<>(Tela.emFrances ? "Position" : "Cargo");
        cargoColuna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        TableColumn<Funcionario, String> contratoColuna = new TableColumn<>(Tela.emFrances ? "Contracter" : "Contrato");
        contratoColuna.setCellValueFactory(new PropertyValueFactory<>("dataContrato"));
        TableColumn<Funcionario, Void> promoverColuna = new TableColumn<>(Tela.emFrances ? "Promouvoir" : "Promover");
        promoverColuna.setCellFactory(coluna -> new TableCell<>() {
            private final Button botao = new Button(Tela.emFrances ? "Promouvoir" : "Promover");
            {
                botao.setOnAction(event -> {
                    Funcionario func = getTableView().getItems().get(getIndex());
                    func.promocao();
                    getTableView().refresh();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : botao);
            }
        });

        tabela.getColumns().addAll(nomeColuna, cargoColuna, contratoColuna, promoverColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        Funcionario test = new Funcionario("Carlos", LocalDate.of(2003, 2, 24), "Rua pinheiros 12", FuncionarioCargo.ZELADOR, 500, "03/05/2000", "carlinhosmaia", "carlinhosmaia@orkut.com");
        funcionarioList.add(test);
        tabela.setItems(funcionarioList);

        VBox promocaoBox = new VBox(20, promotion, tabela);
        promocaoBox.setAlignment(Pos.TOP_CENTER);
        promocaoBox.setPadding(new Insets(0, 10, 0, 10));

        // --- Painel de Contratação ---
        Label register = new Label(Tela.emFrances ? "Embaucher" : "Contratar");
        register.setFont(playfairFontSubs);
        register.setStyle("-fx-text-fill: #FFC300;");

        GridPane inputs = new GridPane();
        // ... Configuração do GridPane de inputs (existente) ...
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
        cbCargo.getItems().addAll(FuncionarioCargo.values());
        cbCargo.setPromptText(Tela.emFrances ? "Sélectionnez le poste" : "Selecione o cargo");
        cbCargo.setPrefHeight(40);
        cbCargo.setMinWidth(200);
        cbCargo.setStyle(inputStyle);
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
        confirm.setOnAction(event -> {
            // Lógica de contratação (existente)
        });

        VBox contratoBox = new VBox(20, register, inputs, confirm);
        contratoBox.setAlignment(Pos.TOP_CENTER);
        contratoBox.setPadding(new Insets(0, 10, 0, 10));
        VBox.setVgrow(inputs, Priority.ALWAYS);

        // --- Layout Total ---
        Rectangle divide = new Rectangle(5, 450);
        divide.setFill(Color.web("#FFC300"));
        HBox total = new HBox(20, promocaoBox, divide, contratoBox);
        total.setAlignment(Pos.CENTER);
        HBox.setHgrow(promocaoBox, Priority.ALWAYS);
        HBox.setHgrow(contratoBox, Priority.ALWAYS);

        // --- Rodapé ---
        Label desc1 = new Label("© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfontRodape1);
        Label desc2 = new Label("Projetado para a excelência culinária francesa");
        desc2.setFont(interfontRodape2);
        desc1.setStyle("-fx-text-fill: white;");
        desc2.setStyle("-fx-text-fill: white;");

        VBox descricaoRodape = new VBox(5, desc1, desc2);
        descricaoRodape.setAlignment(Pos.BOTTOM_CENTER);
        descricaoRodape.setPadding(new Insets(20, 0, 20, 0));

        // --- Layout Raiz ---
        VBox root = new VBox(blocoTitulo, total, descricaoRodape);
        root.setAlignment(Pos.CENTER);
        VBox.setVgrow(total, Priority.ALWAYS);

        String estiloFundoVinho = "-fx-background-color: linear-gradient(to right, #30000C, #800020);";
        root.setStyle(estiloFundoVinho);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle(estiloFundoVinho);

        // --- MUDANÇAS PARA ADICIONAR O BOTÃO VOLTAR ---
        // 1. Envolver o layout principal em um StackPane
        StackPane stackPane = new StackPane(scrollPane);
        stackPane.setStyle(estiloFundoVinho);

        // 2. Definir a ação de voltar para a TelaGerente
        Runnable acaoVoltar = () -> new TelaGerente(super.getStage()).mostrarTela();

        // 3. Adicionar o botão ao StackPane
        BotaoVoltar.criarEPosicionar(stackPane, acaoVoltar);

        // 4. Criar a cena com o StackPane como raiz
        Scene scene = new Scene(stackPane);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

        return scene;
    }
}