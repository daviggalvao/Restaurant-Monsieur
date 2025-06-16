package app;

import classes.Funcionario;
import classes.FuncionarioCargo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
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

    /**
     * Construtor da TelaFuncionarios.
     * @param stage O palco principal da aplicação.
     */
    public TelaFuncionarios(Stage stage) {
        super(stage);
    }

    @Override
    public void mostrarTela() {
        Font playfairFontTitulo = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 50);
        Font playfairFontSubs = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 45);
        Font interfontRodape1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 15);
        Font interfontRodape2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 17);

        // --- Título Principal ---
        Label tituloPrincipal = new Label("Funcionários");
        tituloPrincipal.setFont(playfairFontTitulo);
        tituloPrincipal.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhado = new Rectangle(230, 3);
        sublinhado.setFill(Color.web("#FFC300"));
        // ALTERAÇÃO: Vincula a largura do sublinhado à largura do título para responsividade automática.
        sublinhado.widthProperty().bind(tituloPrincipal.widthProperty());

        VBox blocoTitulo = new VBox(5, tituloPrincipal, sublinhado);
        blocoTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(blocoTitulo, new Insets(20, 0, 30, 0));

        // --- Divisor Vertical ---
        Rectangle divide = new Rectangle(5, 450);
        divide.setFill(Color.web("#FFC300"));

        // --- Seção de Promoção (Esquerda) ---
        Label promotion = new Label("Promoção");
        promotion.setFont(playfairFontSubs);
        promotion.setStyle("-fx-text-fill: #FFC300;");

        TableView<Funcionario> tabela = new TableView<>();
        ObservableList<Funcionario> funcionarioList = FXCollections.observableArrayList();
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Funcionario, String> nomeColuna = new TableColumn<>("Nome");
        nomeColuna.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Funcionario, FuncionarioCargo> cargoColuna = new TableColumn<>("Cargo");
        cargoColuna.setCellValueFactory(celldata ->
            celldata.getValue().getCargo()
        );

        TableColumn<Funcionario, String> contratoColuna = new TableColumn<>("Contrato");
        contratoColuna.setCellValueFactory(new PropertyValueFactory<>("dataContrato"));

        TableColumn<Funcionario, Void> promoverColuna = new TableColumn<>("Promover");
        promoverColuna.setCellFactory(coluna -> new TableCell<>() {
            private final Button botao = new Button("Promover");
            {
                // Estilo do botão pode ser adicionado aqui, se desejado
                botao.setOnAction(event -> {
                    Funcionario func = funcionarioList.get(getIndex());
                    // Adicione a lógica de promoção aqui
                    func.promocao();
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

        LocalDate data = LocalDate.of(2003,2,24);    // Dados de exemplo
        Funcionario test = new Funcionario("Carlos", data, "Rua pinheiros 12", FuncionarioCargo.ZELADOR, 500, "3/5/2000", "carlinhosmaia", "carlinhosmaia@orkut.com");
        funcionarioList.add(test);
        tabela.setItems(funcionarioList);

        VBox promocao = new VBox(20, promotion, tabela); // Aumentado espaçamento para melhor visual
        promocao.setAlignment(Pos.TOP_CENTER);
        promocao.setPadding(new Insets(0, 10, 0, 10)); // Adicionado padding


        // --- Seção de Contratação (Direita) ---
        Label register = new Label("Contratar");
        register.setFont(playfairFontSubs);
        register.setStyle("-fx-text-fill: #FFC300;");

        GridPane inputs = new GridPane();
        inputs.setHgap(20);
        inputs.setVgap(15);
        inputs.setAlignment(Pos.CENTER);

        String inputStyle = "-fx-background-color: white; -fx-background-radius: 5; -fx-border-color: #ddd; -fx-border-radius: 5; -fx-border-width: 1; -fx-font-size: 14px;";

        Label lblNome = new Label("\uD83D\uDC64 Nome Completo *");
        lblNome.setStyle("-fx-text-fill: #FFC300;");
        TextField tfNome = new TextField();
        tfNome.setPrefHeight(40);
        tfNome.setPromptText("Nome completo do funcionário");
        tfNome.setStyle(inputStyle);

        Label lblEmail = new Label("\uD83D\uDCE7 Email *");
        lblEmail.setStyle("-fx-text-fill: #FFC300;");
        TextField tfEmail = new TextField();
        tfEmail.setPrefHeight(40);
        tfEmail.setPromptText("Email para login");
        tfEmail.setStyle(inputStyle);

        Label lblSenha = new Label("\uD83D\uDD11 Senha *");
        lblSenha.setStyle("-fx-text-fill: #FFC300;");
        PasswordField tfSenha = new PasswordField(); // ALTERAÇÃO: Usar PasswordField para senhas
        tfSenha.setPrefHeight(40);
        tfSenha.setPromptText("Senha de acesso");
        tfSenha.setStyle(inputStyle);

        Label lblData = new Label("\uD83D\uDCC5 Data de Nascimento *");
        lblData.setStyle("-fx-text-fill: #FFC300;");
        DatePicker dpData = new DatePicker();
        dpData.setPrefHeight(40);
        dpData.setMinWidth(200); // Garante que o DatePicker tenha um tamanho mínimo

        Label lblCargo = new Label("\uD83D\uDCBC Cargo *");
        lblCargo.setStyle("-fx-text-fill: #FFC300;");
        ComboBox<FuncionarioCargo> cbCargo = new ComboBox<>();
        cbCargo.getItems().addAll(FuncionarioCargo.GERENTE, FuncionarioCargo.VENDEDOR, FuncionarioCargo.CHEF, FuncionarioCargo.GARCOM, FuncionarioCargo.SUPERVISOR);
        cbCargo.setPromptText("Selecione o cargo");
        cbCargo.setPrefHeight(40);
        cbCargo.setMinWidth(200); // Garante que o ComboBox tenha um tamanho mínimo
        cbCargo.setStyle(inputStyle);

        // ALTERAÇÃO: Usar ColumnConstraints para alinhar o formulário de forma mais limpa
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT); // Alinha os labels à direita
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS); // Permite que os campos de texto cresçam
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

        Button confirm = new Button("Confirmar Contratação");
        confirm.getStyleClass().add("button");
        confirm.setOnMouseClicked(mouseEvent -> {
            String nome = tfNome.getText();
            LocalDate dataNascimento = dpData.getValue();
            LocalDate hoje = LocalDate.now();
            String[] dataContrato = (hoje.toString()).split("-");
            String date = dataContrato[2] + "/" + dataContrato[1] + "/" + dataContrato[0];
            String cargo = cbCargo.getValue().toString();
            String email = tfEmail.getText();
            String senha = tfSenha.getText();
            float salario;
            switch (cargo){
                case "Gerente":
                    salario = 1400;
                    break;
                case "Vendedor":
                    salario = 1100;
                    break;
                case "Chef":
                    salario = 1300;
                    break;
                case "Garçom":
                    salario = 1100;
                    break;
                case "Supervisor":
                    salario = 1250;
                default:
                    salario = 1100;
                    break;
            }
            Funcionario hired = new Funcionario(nome, dataNascimento, " ", cbCargo.getValue(), salario, date, senha, email);
            funcionarioList.add(hired);
            tabela.setItems(funcionarioList);
            tfNome.clear();
            dpData.setValue(null);
            cbCargo.setValue(null);
            tfEmail.clear();
            tfSenha.clear();
        });
        confirm.setPadding(new Insets(10, 20, 10, 20)); // Adiciona um padding melhor

        VBox contrato = new VBox(20, register, inputs, confirm); // Aumentado espaçamento
        contrato.setAlignment(Pos.TOP_CENTER);
        contrato.setPadding(new Insets(0, 10, 0, 10)); // Adicionado padding
        VBox.setVgrow(inputs, Priority.ALWAYS); // Permite que a área de inputs cresça


        // --- Layout Principal (Horizontal) ---
        HBox total = new HBox(20, promocao, divide, contrato); // Espaçamento controla a distância
        total.setAlignment(Pos.CENTER);
        // ALTERAÇÃO: Removidos todos os tamanhos fixos para permitir que o HBox se ajuste.
        // total.setPrefHeight(600); // REMOVIDO
        // total.setPrefWidth(2000); // REMOVIDO
        // total.setMaxWidth(2000); // REMOVIDO
        // total.setMinWidth(1900); // REMOVIDO

        // ALTERAÇÃO: Vincula a altura do divisor à altura do contêiner pai
        divide.heightProperty().bind(total.heightProperty().subtract(100)); // Subtrai para criar uma margem visual

        // ALTERAÇÃO: Diz aos painéis esquerdo e direito para crescerem igualmente e preencherem o espaço
        HBox.setHgrow(promocao, Priority.ALWAYS);
        HBox.setHgrow(contrato, Priority.ALWAYS);

        // --- Rodapé ---
        Label desc1 = new Label("© 2025 Restaurant Monsieur-José - Sistema de Gestão de Restaurante");
        desc1.setFont(interfontRodape1);
        Label desc2 = new Label("Projetado para a excelência culinária francesa");
        desc2.setFont(interfontRodape2);
        String corTextoRodape = "white";
        desc1.setStyle("-fx-text-fill: " + corTextoRodape + ";");
        desc2.setStyle("-fx-text-fill: " + corTextoRodape + ";");

        VBox descricaoRodape = new VBox(5, desc1, desc2);
        descricaoRodape.setAlignment(Pos.BOTTOM_CENTER);
        descricaoRodape.setPadding(new Insets(20, 0, 20, 0)); // Padding em vez de margem

        // --- Layout Raiz e Cena ---
        VBox root = new VBox(blocoTitulo, total, descricaoRodape);
        root.setAlignment(Pos.CENTER);
        // root.setPadding(new Insets(20)); // Padding já está no scrollpane e rodapé

        // ALTERAÇÃO: Diz ao contêiner 'total' para ocupar todo o espaço vertical disponível
        VBox.setVgrow(total, Priority.ALWAYS);

        // ALTERAÇÃO: O GridPane não precisa mais de constraints fixas.
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.add(root, 0, 0);
        // Garante que o root preencha o GridPane
        GridPane.setHgrow(root, Priority.ALWAYS);
        GridPane.setVgrow(root, Priority.ALWAYS);

        String estiloFundoVinho = "-fx-background-color: linear-gradient(to right, #30000C, #800020);";
        grid.setStyle(estiloFundoVinho);

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle(estiloFundoVinho); // Garante que o fundo do scrollpane seja igual

        Scene scene = new Scene(scrollPane);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

        // ALTERAÇÃO: O listener de largura não é mais necessário, pois o layout agora é fluido.
        // scene.widthProperty().addListener((obs, oldVal, newVal) -> { ... }); // REMOVIDO

        super.getStage().setTitle("Funcionários");
        super.getStage().setMaximized(true);
        super.getStage().setScene(scene);
        super.getStage().setMinWidth(1000); // Define um tamanho mínimo razoável para a janela
        super.getStage().setMinHeight(700);
        super.getStage().show();
    }
}