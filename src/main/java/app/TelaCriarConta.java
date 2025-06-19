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

// 1. Herdar da classe Tela
public class TelaCriarConta extends Tela {

    public TelaCriarConta(Stage stage) {
        super(stage);
    }

    public WebView criarWebview(String svgPath){
        WebView webView = new WebView();
        webView.setMinSize(10, 10);
        webView.setPrefSize(20, 20);
        webView.setMaxSize(30, 30);

        String svgUrl = getClass().getResource(svgPath).toExternalForm();
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" +
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" +
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

    /**
     * Mostra um diálogo modal para o usuário redefinir sua senha.
     * Este ainda abre uma nova Stage, pois é um diálogo modal.
     */
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
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Atualização", "Ocorreu um erro inesperado ao tentar alterar a senha.");
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            } finally {
                em.close();
            }
        });
    }

    @Override
    public Scene criarScene(){ // Já era Scene criarScene()
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 40);
        Font interfont1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 13);

        String inputStyle = "-fx-background-color: white;\n" +
                "    -fx-background-radius: 5px;\n" +
                "    -fx-border-color: #ddd;\n" +
                "    -fx-border-radius: 5px;\n" +
                "    -fx-border-width: 1px;\n" +
                "    -fx-font-size: 14px;\n" +
                "    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);";

        String estiloFundoVinho = " -fx-background-color: linear-gradient(to right, #30000C, #800020)";
        String estiloPainelBranco = "-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 30;";

        Label title = new Label("Criar Conta");
        title.setFont(playfairFont);
        title.setStyle("-fx-text-fill: #FFC300");

        Label subtitle = new Label("Preencha seus dados para se cadastrar");
        subtitle.setFont(interfont1);
        VBox titulos = new VBox(5);
        titulos.setAlignment(Pos.CENTER);
        titulos.getChildren().addAll(title, subtitle);


        Label nomecaixa = new Label("Nome Completo");
        TextField nomeTF = new TextField();
        nomeTF.setPromptText("Digite seu nome completo");
        nomeTF.setStyle(inputStyle);
        nomeTF.setPrefHeight(40);
        nomeTF.setPrefWidth(60);
        WebView nomeWebView = criarWebview("/svg/profile-1335-svgrepo-com.svg");
        HBox nomehbox= new HBox(5);
        nomehbox.getChildren().addAll(nomeWebView,nomecaixa);
        VBox nomes = new VBox(5,nomehbox,nomeTF);
        nomes.setAlignment(Pos.CENTER);

        Label dataAniversario = new Label("Data de Aniversario");
        DatePicker dataAniversarioDP = new DatePicker();
        dataAniversarioDP.setPromptText("Selecione sua data de nascimento");
        dataAniversarioDP.setStyle(inputStyle);
        dataAniversarioDP.setPrefHeight(40);
        dataAniversarioDP.setPrefWidth(460);
        WebView dataWebView = criarWebview("/svg/calendar-time-svgrepo-com.svg");
        HBox datahbox = new HBox(5);
        datahbox.getChildren().addAll(dataWebView,dataAniversario);
        VBox datas = new VBox(5,datahbox,dataAniversarioDP);

        Label enderecocaixa = new Label("Endereco");
        TextField enderecoTF = new TextField();
        enderecoTF.setPromptText("Digite seu nome completo");
        enderecoTF.setStyle(inputStyle);
        WebView enderecoWebView = criarWebview("/svg/location-2-svgrepo-com.svg");
        HBox enderecohbox = new HBox(5);
        enderecohbox.getChildren().addAll(enderecoWebView,enderecocaixa);
        VBox enderecos = new VBox(5);
        enderecos.getChildren().addAll(enderecohbox,enderecoTF);
        enderecos.setAlignment(Pos.CENTER);

        Label emailcaixa = new Label("E-mail");
        TextField emailTF = new TextField();
        emailTF.setPromptText("Digite seu e-mail");
        emailTF.setStyle(inputStyle);
        WebView emailWebView = criarWebview("/svg/email-svgrepo-com.svg");
        HBox emailhbox = new HBox(5);
        emailhbox.getChildren().addAll(emailWebView,emailcaixa);
        VBox emails = new VBox(5);
        emails.getChildren().addAll(emailhbox,emailTF);
        emails.setAlignment(Pos.CENTER);

        Label senhacaixa = new Label("Senha");
        PasswordField senhaTF = new PasswordField();
        senhaTF.setPromptText("Digite sua senha");
        senhaTF.setStyle(inputStyle);
        WebView senhaWebView = criarWebview("/svg/padlock-svgrepo-com.svg");
        HBox senhahbox = new HBox(5);
        senhahbox.getChildren().addAll(senhaWebView,senhacaixa);
        VBox senhas = new VBox(5);
        senhas.getChildren().addAll(senhahbox,senhaTF);
        senhas.setAlignment(Pos.CENTER);

        Button confirmar = new Button("Criar Conta!");
        confirmar.getStyleClass().add("button");

        confirmar.setOnAction(event -> {
            String nome = nomeTF.getText();
            String email = emailTF.getText();
            String senha = senhaTF.getText();
            String endereco = enderecoTF.getText();
            LocalDate dateAniversario = dataAniversarioDP.getValue();

            if (nome.trim().isEmpty() ||
                    email.trim().isEmpty() ||
                    senha.trim().isEmpty() ||
                    dateAniversario == null) {

                mostrarAlerta(Alert.AlertType.WARNING,
                        "Campos Obrigatórios",
                        "Por favor, preencha todos os campos para continuar.");

                return;
            }

            EntityManager em = JpaUtil.getFactory().createEntityManager();

            try {
                TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Cliente c WHERE c.email = :email", Long.class);
                query.setParameter("email", email);

                Long count = query.getSingleResult();

                if (count > 0) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Aviso de Cadastro", "O email '" + email + "' já está em uso. Por favor, tente outro.");
                } else {
                    Cliente novoCliente = new Cliente(nome,dateAniversario,endereco,email,senha);

                    em.getTransaction().begin();
                    em.persist(novoCliente);
                    em.getTransaction().commit();

                    mostrarAlerta(Alert.AlertType.INFORMATION, "Cadastro Realizado", "Cliente '" + nome + "' cadastrado com sucesso!");

                    // *** Redirecionamento após criação de conta ***
                    if ("Reserva".equals(Tela.proximaTelaAposLogin)) {
                        new TelaReserva(super.getStage()).mostrarTela();
                    } else if ("Delivery".equals(Tela.proximaTelaAposLogin)) {
                        new TelaDelivery(super.getStage()).mostrarTela();
                    } else {
                        new TelaInicial(super.getStage()).mostrarTela(); // Default para Tela Inicial
                    }
                    Tela.proximaTelaAposLogin = null; // Limpa o estado após o redirecionamento
                }

            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
                System.out.println("Erro ao cadastrar o cliente.");
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        });

        VBox Main = new VBox(20,titulos,nomes,datas,enderecos,emails,senhas,confirmar);
        Main.setAlignment(Pos.TOP_CENTER);
        Main.setMaxWidth(400);
        Main.setMaxHeight(625);
        Main.setStyle(estiloPainelBranco);

        VBox vboxLogin = new VBox(20);
        vboxLogin.setAlignment(Pos.TOP_CENTER);
        vboxLogin.setMaxWidth(400);
        vboxLogin.setMaxHeight(625);
        vboxLogin.setStyle(estiloPainelBranco);

        Label loginTitle = new Label("Entrar");
        loginTitle.setFont(playfairFont);
        loginTitle.setStyle("-fx-text-fill: #FFC300");
        Label loginSubtitle = new Label("Acesse sua conta");
        loginSubtitle.setFont(interfont1);
        VBox loginTitulos = new VBox(5, loginTitle, loginSubtitle);
        loginTitulos.setAlignment(Pos.CENTER);

        Label emailLoginLabel = new Label("E-mail");
        TextField emailTFLogin = new TextField();
        emailTFLogin.setPromptText("Digite seu e-mail");
        emailTFLogin.setStyle(inputStyle);
        emailTFLogin.setPrefHeight(40);
        WebView emailLoginWebView = criarWebview("/svg/email-svgrepo-com.svg");
        HBox emailLoginHbox = new HBox(5, emailLoginWebView, emailLoginLabel);
        VBox emailLoginVbox = new VBox(5, emailLoginHbox, emailTFLogin);

        Label senhaLoginLabel = new Label("Senha");
        PasswordField senhaTFLogin = new PasswordField();
        senhaTFLogin.setPromptText("Digite sua senha");
        senhaTFLogin.setStyle(inputStyle);
        senhaTFLogin.setPrefHeight(40);
        WebView senhaLoginWebView = criarWebview("/svg/padlock-svgrepo-com.svg");
        HBox senhaLoginHbox = new HBox(5, senhaLoginWebView, senhaLoginLabel);
        VBox senhaLoginVbox = new VBox(5, senhaLoginHbox, senhaTFLogin);

        CheckBox lembrarCb = new CheckBox("Lembrar de mim");
        Hyperlink esqueceuLink = new Hyperlink("Esqueceu a senha?");
        esqueceuLink.setOnAction(event -> mostrarDialogoResetSenha());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox lembrarHbox = new HBox(lembrarCb, spacer, esqueceuLink);
        lembrarHbox.setAlignment(Pos.CENTER_LEFT);

        Button btnEntrar = new Button("Entrar");
        btnEntrar.getStyleClass().add("button");
        btnEntrar.setPrefWidth(460);
        btnEntrar.setOnAction(event -> {
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

                // *** Redirecionamento após login bem-sucedido ***
                if ("Reserva".equals(Tela.proximaTelaAposLogin)) {
                    new TelaReserva(super.getStage()).mostrarTela();
                } else if ("Delivery".equals(Tela.proximaTelaAposLogin)) {
                    new TelaDelivery(super.getStage()).mostrarTela();
                } else {
                    new TelaInicial(super.getStage()).mostrarTela(); // Default para Tela Inicial
                }
                Tela.proximaTelaAposLogin = null; // Limpa o estado após o redirecionamento

            } catch (NoResultException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Login", "E-mail ou senha incorretos. Tente novamente.");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado ao tentar fazer login.");
            } finally {
                em.close();
            }
        });

        Region linha1 = new Region();
        linha1.setStyle("-fx-background-color: #ddd; -fx-pref-height: 1;");
        HBox.setHgrow(linha1, Priority.ALWAYS);
        Label ouLabel = new Label("ou");
        Region linha2 = new Region();
        linha2.setStyle("-fx-background-color: #ddd; -fx-pref-height: 1;");
        HBox.setHgrow(linha2, Priority.ALWAYS);
        HBox separador = new HBox(linha1, ouLabel, linha2);
        separador.setAlignment(Pos.CENTER);
        separador.setSpacing(10);

        Region separadorCustomizado = new Region();
        separadorCustomizado.setPrefWidth(3);
        separadorCustomizado.setMaxHeight(550);
        separadorCustomizado.setStyle("-fx-background-color: #FFC300;");
        vboxLogin.getChildren().addAll(loginTitulos, emailLoginVbox, senhaLoginVbox, lembrarHbox, btnEntrar);
        HBox painelPrincipal = new HBox(40);
        painelPrincipal.setAlignment(Pos.CENTER);
        painelPrincipal.getChildren().addAll(Main,separadorCustomizado,vboxLogin);

        StackPane root = new StackPane(painelPrincipal);
        root.setAlignment(Pos.CENTER);
        root.setStyle(estiloFundoVinho);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

        // Removidos os comandos de Stage.setScene, Stage.setTitle, Stage.setMinWidth, etc.
        // Isso será feito pelo método mostrarTela() da classe base Tela.

        return scene;
    }
}