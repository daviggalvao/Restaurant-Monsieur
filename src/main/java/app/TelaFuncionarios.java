package app;

import classes.Funcionario;
import classes.FuncionarioCargo;
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

        Label tituloPrincipal = new Label(Tela.emFrances ? "Employés" : "Funcionários");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");
        Rectangle sublinhado = new Rectangle(230, 3);
        sublinhado.setFill(Color.web("#FFC300"));
        sublinhado.widthProperty().bind(tituloPrincipal.widthProperty());
        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0));

        Label promotion = new Label(Tela.emFrances ? "Promotion" : "Promoção");
        promotion.setFont(playfairFontSubs);
        promotion.setStyle("-fx-text-fill: #FFC300;");

        TextField pesquisaNome = new TextField();
        pesquisaNome.setPromptText(Tela.emFrances ? "Rechercher par nom" : "Pesquisar por nome");
        pesquisaNome.setMinWidth(300);
        Button limparPesquisa = new Button(Tela.emFrances ? "Nettoyer" : "Limpar");
        HBox barraPesquisa = new HBox(10, pesquisaNome, limparPesquisa);
        barraPesquisa.setAlignment(Pos.CENTER);

        TableView<Funcionario> tabela = new TableView<>();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Funcionario, String> nomeColuna = new TableColumn<>(Tela.emFrances ? "Nom" : "Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Funcionario, FuncionarioCargo> cargoColuna = new TableColumn<>(Tela.emFrances ? "Position" : "Cargo");
        cargoColuna.setCellValueFactory(cellData -> cellData.getValue().getCargo());

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

        // --- NOVO: Coluna para demitir funcionário ---
        TableColumn<Funcionario, Void> demitirColuna = new TableColumn<>(Tela.emFrances ? "Licencier" : "Demitir");
        demitirColuna.setCellFactory(coluna -> new TableCell<>() {
            private final Button botaoDemitir = new Button(Tela.emFrances ? "Licencier" : "Demitir");
            {
                botaoDemitir.setOnAction(event -> {
                    Funcionario func = getTableView().getItems().get(getIndex());
                    func.demitirFuncionario();
                    getTableView().refresh();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : botaoDemitir);
            }
        });
        // --- FIM DO NOVO ---

        // --- MUDANÇA: Adicionando a nova coluna à tabela ---
        tabela.getColumns().addAll(nomeColuna, cargoColuna, contratoColuna, promoverColuna, demitirColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        ObservableList<Funcionario> masterData = FXCollections.observableArrayList();
        masterData.add(new Funcionario("Carlos Silva", LocalDate.of(2003, 2, 24), "Rua pinheiros 12", FuncionarioCargo.ZELADOR, 500, "03/05/2020", "carlinhosmaia", "carlinhosmaia@orkut.com"));
        masterData.add(new Funcionario("Beatriz Costa", LocalDate.of(1995, 8, 15), "Avenida Central 33", FuncionarioCargo.GARCOM, 2500, "10/01/2022", "bia.costa", "beatriz@email.com"));
        masterData.add(new Funcionario("Juliana Alves", LocalDate.of(1998, 11, 5), "Praça da Sé 4", FuncionarioCargo.CHEF, 3200, "25/07/2021", "juju.alves", "juliana@email.com"));

        FilteredList<Funcionario> filteredData = new FilteredList<>(masterData, p -> true);
        tabela.setItems(filteredData);

        pesquisaNome.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredData.setPredicate(funcionario -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return funcionario.getNome().toLowerCase().contains(lowerCaseFilter);
            });
        });
        limparPesquisa.setOnAction(event -> {
            pesquisaNome.clear();
            filteredData.setPredicate(p -> true);
        });

        VBox promocaoBox = new VBox(20, promotion, barraPesquisa, tabela);
        promocaoBox.setAlignment(Pos.TOP_CENTER);
        promocaoBox.setPadding(new Insets(0, 10, 0, 10));

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
            // Lógica de contratação
        });

        VBox contratoBox = new VBox(20, register, inputs, confirm);
        contratoBox.setAlignment(Pos.TOP_CENTER);
        contratoBox.setPadding(new Insets(0, 10, 0, 10));
        VBox.setVgrow(inputs, Priority.ALWAYS);

        Rectangle divide = new Rectangle(5, 450);
        divide.setFill(Color.web("#FFC300"));
        HBox total = new HBox(20, promocaoBox, divide, contratoBox);
        total.setAlignment(Pos.CENTER);
        HBox.setHgrow(promocaoBox, Priority.ALWAYS);
        HBox.setHgrow(contratoBox, Priority.ALWAYS);

        Label desc1 = new Label("© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfontRodape1);
        Label desc2 = new Label("Projetado para a excelência culinária francesa");
        desc2.setFont(interfontRodape2);
        desc1.setStyle("-fx-text-fill: white;");
        desc2.setStyle("-fx-text-fill: white;");

        VBox descricaoRodape = new VBox(5, desc1, desc2);
        descricaoRodape.setAlignment(Pos.BOTTOM_CENTER);
        descricaoRodape.setPadding(new Insets(20, 0, 20, 0));

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

        StackPane stackPane = new StackPane(scrollPane);
        stackPane.setStyle(estiloFundoVinho);

        Runnable acaoVoltar = () -> new TelaGerente(super.getStage()).mostrarTela();
        BotaoVoltar.criarEPosicionar(stackPane, acaoVoltar);

        Scene scene = new Scene(stackPane);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

        return scene;
    }
}