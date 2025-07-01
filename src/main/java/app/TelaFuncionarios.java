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
import java.util.List;
import java.util.Optional;

public class TelaFuncionarios extends Tela {

    // Usaremos esta lista para que a tabela se atualize dinamicamente
    private ObservableList<Funcionario> masterData;

    public TelaFuncionarios(Stage stage) {
        super(stage);
    }

    @Override
    public Scene criarScene() {
        // ... (código da UI inicial sem alterações)
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
        // MUDANÇA: Simplificado para usar PropertyValueFactory, já que o campo 'cargo' não é mais uma Property do JavaFX
        cargoColuna.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        TableColumn<Funcionario, LocalDate> contratoColuna = new TableColumn<>(Tela.emFrances ? "Contracter" : "Contrato");
        contratoColuna.setCellValueFactory(new PropertyValueFactory<>("dataContrato"));

        // --- LÓGICA DO BOTÃO "PROMOVER" ---
        TableColumn<Funcionario, Void> promoverColuna = new TableColumn<>(Tela.emFrances ? "Promouvoir" : "Promover");
        promoverColuna.setCellFactory(coluna -> new TableCell<>() {
            private final Button botao = new Button(Tela.emFrances ? "Promouvoir" : "Promover");
            {
                botao.setOnAction(event -> {
                    Funcionario func = getTableView().getItems().get(getIndex());
                    func.promocao(); // Modifica o objeto em memória
                    func.salvar();   // Persiste a modificação no banco de dados
                    getTableView().refresh(); // Atualiza a tabela na UI
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : botao);
            }
        });

        // --- LÓGICA DO BOTÃO "DEMITIR" ---
        TableColumn<Funcionario, Void> demitirColuna = new TableColumn<>(Tela.emFrances ? "Licencier" : "Demitir");
        demitirColuna.setCellFactory(coluna -> new TableCell<>() {
            private final Button botaoDemitir = new Button(Tela.emFrances ? "Licencier" : "Demitir");
            {
                botaoDemitir.setOnAction(event -> {
                    Funcionario func = getTableView().getItems().get(getIndex());
                    // Adiciona uma confirmação para evitar demissões acidentais
                    Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmacao.setTitle("Confirmar Demissão");
                    confirmacao.setHeaderText("Tem a certeza que deseja demitir " + func.getNome() + "?");
                    confirmacao.setContentText("Esta ação irá mover o funcionário para a lista de demitidos.");

                    Optional<ButtonType> resultado = confirmacao.showAndWait();
                    if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                        func.demitirFuncionario(); // Altera o status para DEMITIDO
                        func.salvar();             // Salva essa alteração no banco
                        masterData.remove(func); // Remove da lista visível na tabela
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : botaoDemitir);
            }
        });

        tabela.getColumns().addAll(nomeColuna, cargoColuna, contratoColuna, promoverColuna, demitirColuna);
        tabela.getStylesheets().add(getClass().getResource("/css/table.css").toExternalForm());

        // --- CARREGAMENTO INICIAL DOS DADOS ---
        List<Funcionario> funcionariosDoBanco = Funcionario.listarTodos();
        this.masterData = FXCollections.observableArrayList(funcionariosDoBanco);

        FilteredList<Funcionario> filteredData = new FilteredList<>(masterData, p -> true);
        tabela.setItems(filteredData);

        // ... (código da barra de pesquisa sem alterações)
        pesquisaNome.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredData.setPredicate(funcionario -> {
                if (newValue == null || newValue.isEmpty()) return true;
                return funcionario.getNome().toLowerCase().contains(newValue.toLowerCase());
            });
        });
        limparPesquisa.setOnAction(event -> {
            pesquisaNome.clear();
            filteredData.setPredicate(p -> true);
        });

        // --- LÓGICA DO FORMULÁRIO DE CONTRATAÇÃO ---
        // ... (código de criação dos componentes do formulário sem alterações)
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
        cbCargo.getItems().remove(FuncionarioCargo.DEMITIDO); // Não se pode contratar alguém já demitido
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
            // Validar campos
            if (tfNome.getText().isEmpty() || dpData.getValue() == null || cbCargo.getValue() == null || tfEmail.getText().isEmpty() || tfSenha.getText().isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Campos Obrigatórios");
                alerta.setHeaderText("Por favor, preencha todos os campos marcados com *");
                alerta.showAndWait();
                return;
            }

            // Criar novo funcionário com os dados do formulário
            Funcionario novoFuncionario = new Funcionario(
                    tfNome.getText(),
                    dpData.getValue(),
                    null, // Endereço pode ser opcional ou adicionado depois
                    cbCargo.getValue(),
                    0, // Salário inicial pode ser 0 ou um valor base
                    LocalDate.now(), // Data do contrato é a data atual
                    tfSenha.getText(),
                    tfEmail.getText()
            );

            // Salvar no banco de dados
            novoFuncionario.salvar();

            // Adicionar à lista da tabela para atualização automática da UI
            masterData.add(novoFuncionario);

            // Limpar formulário
            tfNome.clear();
            dpData.setValue(null);
            cbCargo.setValue(null);
            tfEmail.clear();
            tfSenha.clear();

            // Mensagem de sucesso
            Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
            sucesso.setTitle("Contratação Realizada");
            sucesso.setHeaderText("Novo funcionário contratado com sucesso!");
            sucesso.showAndWait();
        });

        // ... (resto do código de layout da UI sem alterações)
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
        Label desc1 = new Label(Tela.emFrances ? "© 2025 Restaurant Monsieur-José - Système de gestion de restaurant" : "© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfontRodape1);
        Label desc2 = new Label(Tela.emFrances ? "Conçu pour l'excellence culinaire française" : "Projetado para a excelência culinária francesa");
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