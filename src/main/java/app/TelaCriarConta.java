package app;

import classes.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import database.JpaUtil;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.Optional;

public class TelaCriarConta extends Tela {

    public TelaCriarConta(Stage stage) {
        super(stage);
    }

    public WebView criarWebview(String svgPath){
        WebView webView = new WebView();
        webView.setMinSize(25, 25);
        webView.setPrefSize(25, 25);
        webView.setMaxSize(25, 25);
        webView.setMouseTransparent(true);
        webView.setStyle("-fx-background-color: transparent;");

        String svgUrl = getClass().getResource(svgPath).toExternalForm();
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center; background-color: transparent;'>" +
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain;' />" +
                "</body></html>";
        webView.getEngine().loadContent(html);
        return webView;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarDialogoResetSenha() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Redefinir Senha");
        dialog.setHeaderText("Por favor, insira seu e-mail e a nova senha desejada.");

        ButtonType confirmarButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField emailField = new TextField();
        emailField.setPromptText("seu.email@exemplo.com");
        PasswordField novaSenhaField = new PasswordField();
        novaSenhaField.setPromptText("Digite a nova senha");
        PasswordField confirmarSenhaField = new PasswordField();
        confirmarSenhaField.setPromptText("Confirme a nova senha");

        grid.add(new Label("E-mail:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Nova Senha:"), 0, 1);
        grid.add(novaSenhaField, 1, 1);
        grid.add(new Label("Confirmar Senha:"), 0, 2);
        grid.add(confirmarSenhaField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Node confirmarButton = dialog.getDialogPane().lookupButton(confirmarButtonType);
        confirmarButton.setDisable(true);

        Runnable validacao = () -> {
            boolean emailValido = !emailField.getText().trim().isEmpty() && emailField.getText().contains("@");
            boolean senhasValidas = !novaSenhaField.getText().isEmpty()
                    && novaSenhaField.getText().equals(confirmarSenhaField.getText());
            confirmarButton.setDisable(!(emailValido && senhasValidas));
        };

        emailField.textProperty().addListener((observable, oldValue, newValue) -> validacao.run());
        novaSenhaField.textProperty().addListener((observable, oldValue, newValue) -> validacao.run());
        confirmarSenhaField.textProperty().addListener((observable, oldValue, newValue) -> validacao.run());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmarButtonType) {
                return new Pair<>(emailField.getText(), novaSenhaField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(dados -> {
            String email = dados.getKey();
            String novaSenha = dados.getValue();

            EntityManager em = JpaUtil.getFactory().createEntityManager();
            try {
                em.getTransaction().begin();

                TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c WHERE c.email = :email", Cliente.class);
                query.setParameter("email", email);
                Cliente cliente = query.getSingleResult();
                cliente.setSenha(novaSenha);
                em.merge(cliente);
                em.getTransaction().commit();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Sua senha foi alterada com sucesso!");

            } catch (NoResultException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Nenhum cliente encontrado com o e-mail: " + email);
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
            }
            catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Atualização", "Ocorreu um erro inesperado.");
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
            } finally {
                if (em != null) em.close();
            }
        });
    }

    @Override
    public Scene criarScene(){
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 40);
        Font interfont1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 13);

        String inputStyle = "-fx-background-color: white; -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px; -fx-font-size: 14px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);";
        String estiloFundoVinho = " -fx-background-color: linear-gradient(to right, #30000C, #800020)";
        String estiloPainelBranco = "-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 30;";

        // --- Painel de Criar Conta ---
        Label title = new Label("Criar Conta");
        title.setFont(playfairFont);
        title.setStyle("-fx-text-fill: #FFC300");

        Label subtitle = new Label("Preencha seus dados para se cadastrar");
        subtitle.setFont(interfont1);
        VBox titulos = new VBox(5, title, subtitle);
        titulos.setAlignment(Pos.CENTER);

        // Campos de texto para criar conta...
        TextField nomeTF = new TextField();
        nomeTF.setPromptText("Digite seu nome completo");
        VBox nomes = new VBox(5, new HBox(5, criarWebview("/svg/profile-1335-svgrepo-com.svg"), new Label("Nome Completo")), nomeTF);

        DatePicker dataAniversarioDP = new DatePicker();
        dataAniversarioDP.setPromptText("Selecione sua data de nascimento");
        dataAniversarioDP.setMaxWidth(Double.MAX_VALUE);
        VBox datas = new VBox(5, new HBox(5, criarWebview("/svg/calendar-time-svgrepo-com.svg"), new Label("Data de Aniversário")), dataAniversarioDP);

        TextField enderecoTF = new TextField();
        enderecoTF.setPromptText("Digite seu endereço");
        VBox enderecos = new VBox(5, new HBox(5, criarWebview("/svg/location-2-svgrepo-com.svg"), new Label("Endereço")), enderecoTF);

        TextField emailTF = new TextField();
        emailTF.setPromptText("Digite seu e-mail");
        VBox emails = new VBox(5, new HBox(5, criarWebview("/svg/email-svgrepo-com.svg"), new Label("E-mail")), emailTF);

        PasswordField senhaTF = new PasswordField();
        senhaTF.setPromptText("Digite sua senha");
        VBox senhas = new VBox(5, new HBox(5, criarWebview("/svg/padlock-svgrepo-com.svg"), new Label("Senha")), senhaTF);

        // Aplicando estilos
        nomeTF.setStyle(inputStyle);
        dataAniversarioDP.setStyle(inputStyle);
        enderecoTF.setStyle(inputStyle);
        emailTF.setStyle(inputStyle);
        senhaTF.setStyle(inputStyle);

        Button confirmar = new Button("Criar Conta");
        confirmar.getStyleClass().add("button");
        confirmar.setMaxWidth(Double.MAX_VALUE);

        confirmar.setOnAction(event -> {
            // ... (Lógica de confirmação existente)
            String nome = nomeTF.getText();
            String email = emailTF.getText();
            String senha = senhaTF.getText();
            String endereco = enderecoTF.getText();
            LocalDate dateAniversario = dataAniversarioDP.getValue();

            if (nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty() || dateAniversario == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Por favor, preencha todos os campos.");
                return;
            }

            EntityManager em = JpaUtil.getFactory().createEntityManager();
            try {
                TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Cliente c WHERE c.email = :email", Long.class);
                query.setParameter("email", email);
                if (query.getSingleResult() > 0) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "O email '" + email + "' já está em uso.");
                } else {
                    Cliente novoCliente = new Cliente(nome, dateAniversario, endereco, email, senha);
                    em.getTransaction().begin();
                    em.persist(novoCliente);
                    em.getTransaction().commit();
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Cliente '" + nome + "' cadastrado com sucesso!");

                    if ("Reserva".equals(Tela.proximaTelaAposLogin)) {
                        new TelaReserva(super.getStage()).mostrarTela();
                    } else if ("Delivery".equals(Tela.proximaTelaAposLogin)) {
                        new TelaDelivery(super.getStage()).mostrarTela();
                    } else {
                        new TelaInicial(super.getStage()).mostrarTela();
                    }
                    Tela.proximaTelaAposLogin = null;
                }
            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar o cliente.");
            } finally {
                if (em != null) em.close();
            }
        });

        VBox painelCriarConta = new VBox(20, titulos, nomes, datas, enderecos, emails, senhas, confirmar);
        painelCriarConta.setAlignment(Pos.TOP_CENTER);
        painelCriarConta.setMaxWidth(400);
        painelCriarConta.setStyle(estiloPainelBranco);

        // --- Painel de Login ---
        Label loginTitle = new Label("Entrar");
        loginTitle.setFont(playfairFont);
        loginTitle.setStyle("-fx-text-fill: #FFC300");
        Label loginSubtitle = new Label("Acesse sua conta");
        loginSubtitle.setFont(interfont1);
        VBox loginTitulos = new VBox(5, loginTitle, loginSubtitle);
        loginTitulos.setAlignment(Pos.CENTER);

        // Campos de login...
        TextField emailTFLogin = new TextField();
        emailTFLogin.setPromptText("Digite seu e-mail");
        VBox emailLoginVbox = new VBox(5, new HBox(5, criarWebview("/svg/email-svgrepo-com.svg"), new Label("E-mail")), emailTFLogin);

        PasswordField senhaTFLogin = new PasswordField();
        senhaTFLogin.setPromptText("Digite sua senha");
        VBox senhaLoginVbox = new VBox(5, new HBox(5, criarWebview("/svg/padlock-svgrepo-com.svg"), new Label("Senha")), senhaTFLogin);

        emailTFLogin.setStyle(inputStyle);
        senhaTFLogin.setStyle(inputStyle);

        CheckBox lembrarCb = new CheckBox("Lembrar de mim");
        Hyperlink esqueceuLink = new Hyperlink("Esqueceu a senha?");
        esqueceuLink.setOnAction(event -> mostrarDialogoResetSenha());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox lembrarHbox = new HBox(lembrarCb, spacer, esqueceuLink);
        lembrarHbox.setAlignment(Pos.CENTER_LEFT);

        Button btnEntrar = new Button("Entrar");
        btnEntrar.getStyleClass().add("button");
        btnEntrar.setMaxWidth(Double.MAX_VALUE);
        btnEntrar.setOnAction(event -> {
            // ... (Lógica de login existente)
            String email = emailTFLogin.getText();
            String senha = senhaTFLogin.getText();

            if (email.trim().isEmpty() || senha.trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos Vazios", "Por favor, digite seu e-mail e senha.");
                return;
            }

            EntityManager em = JpaUtil.getFactory().createEntityManager();
            try {
                TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c WHERE c.email = :email AND c.senha = :senha", Cliente.class);
                query.setParameter("email", email);
                query.setParameter("senha", senha);

                Cliente cliente = query.getSingleResult();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Login Bem-sucedido", "Bem-vindo(a) de volta, " + cliente.getNome() + "!");

                if ("Reserva".equals(Tela.proximaTelaAposLogin)) {
                    new TelaReserva(super.getStage()).mostrarTela();
                } else if ("Delivery".equals(Tela.proximaTelaAposLogin)) {
                    new TelaDelivery(super.getStage()).mostrarTela();
                } else {
                    new TelaInicial(super.getStage()).mostrarTela();
                }
                Tela.proximaTelaAposLogin = null;

            } catch (NoResultException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Login", "E-mail ou senha incorretos.");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado.");
            } finally {
                if (em != null) em.close();
            }
        });

        VBox painelLogin = new VBox(20);
        painelLogin.setAlignment(Pos.TOP_CENTER);
        painelLogin.setMaxWidth(400);
        painelLogin.setStyle(estiloPainelBranco);
        painelLogin.getChildren().addAll(loginTitulos, emailLoginVbox, senhaLoginVbox, lembrarHbox, btnEntrar);

        // --- Layout Principal com Separador ---
        Region separadorCustomizado = new Region();
        separadorCustomizado.setPrefWidth(3);
        separadorCustomizado.setMaxHeight(550);
        separadorCustomizado.setStyle("-fx-background-color: #FFC300;");

        HBox painelPrincipal = new HBox(40, painelCriarConta, separadorCustomizado, painelLogin);
        painelPrincipal.setAlignment(Pos.CENTER);

        // --- Container Raiz com Botão Voltar ---
        StackPane root = new StackPane(painelPrincipal);
        root.setAlignment(Pos.CENTER);
        root.setStyle(estiloFundoVinho);

        // Ação de voltar para a Tela Inicial
        Runnable acaoVoltar = () -> new TelaInicial(super.getStage()).mostrarTela();
        BotaoVoltar.criarEPosicionar(root, acaoVoltar);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

        return scene;
    }
}