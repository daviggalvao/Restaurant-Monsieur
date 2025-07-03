package app;

import classes.Cliente;
import database.JpaUtil;
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
        dialog.setTitle(Tela.emFrances ? "Réinitialiser le mot de passe" : "Redefinir Senha");
        dialog.setHeaderText(Tela.emFrances ? "Veuillez saisir votre e-mail et le nouveau mot de passe souhaité." : "Por favor, insira seu e-mail e a nova senha desejada.");

        ButtonType confirmarButtonType = new ButtonType(Tela.emFrances ? "Confirmer" : "Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField emailField = new TextField();
        emailField.setPromptText("seu.email@exemplo.com");
        PasswordField novaSenhaField = new PasswordField();
        novaSenhaField.setPromptText(Tela.emFrances ? "Saisissez le nouveau mot de passe" : "Digite a nova senha");
        PasswordField confirmarSenhaField = new PasswordField();
        confirmarSenhaField.setPromptText(Tela.emFrances ? "Confirmez le nouveau mot de passe" : "Confirme a nova senha");

        grid.add(new Label("E-mail:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label(Tela.emFrances ? "Nouveau Mot de passe :" : "Nova Senha:"), 0, 1);
        grid.add(novaSenhaField, 1, 1);
        grid.add(new Label(Tela.emFrances ? "Confirmer le Mot de passe :" : "Confirmar Senha:"), 0, 2);
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
                mostrarAlerta(Alert.AlertType.INFORMATION, Tela.emFrances ? "Succès" : "Sucesso", Tela.emFrances ? "Votre mot de passe a été modifié avec succès !" : "Sua senha foi alterada com sucesso!");

            } catch (NoResultException e) {
                mostrarAlerta(Alert.AlertType.ERROR, Tela.emFrances ? "Erreur" : "Erro", (Tela.emFrances ? "Aucun client trouvé avec l'e-mail : " : "Nenhum cliente encontrado com o e-mail: ") + email);
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
            }
            catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, Tela.emFrances ? "Erreur de Mise à Jour" : "Erro de Atualização", Tela.emFrances ? "Une erreur inattendue est survenue." : "Ocorreu um erro inesperado.");
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

        Label title = new Label(Tela.emFrances ? "Créer un Compte" : "Criar Conta");
        title.setFont(playfairFont);
        title.setStyle("-fx-text-fill: #FFC300");

        Label subtitle = new Label(Tela.emFrances ? "Remplissez vos données pour vous inscrire" : "Preencha seus dados para se cadastrar");
        subtitle.setFont(interfont1);
        VBox titulos = new VBox(5, title, subtitle);
        titulos.setAlignment(Pos.CENTER);

        TextField nomeTF = new TextField();
        nomeTF.setPromptText(Tela.emFrances ? "Saisissez votre nom complet" : "Digite seu nome completo");
        VBox nomes = new VBox(5, new HBox(5, criarWebview("/svg/profile-1335-svgrepo-com.svg"), new Label(Tela.emFrances ? "Nom Complet" : "Nome Completo")), nomeTF);

        DatePicker dataAniversarioDP = new DatePicker();
        dataAniversarioDP.setPromptText(Tela.emFrances ? "Sélectionnez votre date de naissance" : "Selecione sua data de nascimento");
        dataAniversarioDP.setMaxWidth(Double.MAX_VALUE);
        VBox datas = new VBox(5, new HBox(5, criarWebview("/svg/calendar-time-svgrepo-com.svg"), new Label(Tela.emFrances ? "Date de Naissance" : "Data de Aniversário")), dataAniversarioDP);

        TextField enderecoTF = new TextField();
        enderecoTF.setPromptText(Tela.emFrances ? "Saisissez votre adresse" : "Digite seu endereço");
        VBox enderecos = new VBox(5, new HBox(5, criarWebview("/svg/location-2-svgrepo-com.svg"), new Label(Tela.emFrances ? "Adresse" : "Endereço")), enderecoTF);

        TextField emailTF = new TextField();
        emailTF.setPromptText(Tela.emFrances ? "Saisissez votre e-mail" : "Digite seu e-mail");
        VBox emails = new VBox(5, new HBox(5, criarWebview("/svg/email-svgrepo-com.svg"), new Label("E-mail")), emailTF);

        PasswordField senhaTF = new PasswordField();
        senhaTF.setPromptText(Tela.emFrances ? "Saisissez votre mot de passe" : "Digite sua senha");
        VBox senhas = new VBox(5, new HBox(5, criarWebview("/svg/padlock-svgrepo-com.svg"), new Label(Tela.emFrances ? "Mot de passe" : "Senha")), senhaTF);

        nomeTF.setStyle(inputStyle);
        dataAniversarioDP.setStyle(inputStyle);
        enderecoTF.setStyle(inputStyle);
        emailTF.setStyle(inputStyle);
        senhaTF.setStyle(inputStyle);

        Button confirmar = new Button(Tela.emFrances ? "Créer un Compte" : "Criar Conta");
        confirmar.getStyleClass().add("button");
        confirmar.setMaxWidth(Double.MAX_VALUE);

        confirmar.setOnAction(event -> {
            String nome = nomeTF.getText();
            String email = emailTF.getText();
            String senha = senhaTF.getText();
            String endereco = enderecoTF.getText();
            LocalDate dateAniversario = dataAniversarioDP.getValue();

            if (nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty() || dateAniversario == null) {
                mostrarAlerta(Alert.AlertType.WARNING, Tela.emFrances ? "Champs Obligatoires" : "Campos Obrigatórios", Tela.emFrances ? "Veuillez remplir tous les champs." : "Por favor, preencha todos os campos.");
                return;
            }

            EntityManager em = JpaUtil.getFactory().createEntityManager();
            try {
                TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Cliente c WHERE c.email = :email", Long.class);
                query.setParameter("email", email);
                if (query.getSingleResult() > 0) {
                    mostrarAlerta(Alert.AlertType.WARNING, Tela.emFrances ? "Avertissement" : "Aviso", (Tela.emFrances ? "L'e-mail '" : "O email '") + email + (Tela.emFrances ? "' est déjà utilisé." : "' já está em uso."));
                } else {
                    Cliente novoCliente = new Cliente(nome, dateAniversario, endereco, email, senha);
                    em.getTransaction().begin();
                    em.persist(novoCliente);
                    em.getTransaction().commit();
                    mostrarAlerta(Alert.AlertType.INFORMATION, Tela.emFrances ? "Succès" : "Sucesso", (Tela.emFrances ? "Client '" : "Cliente '") + nome + (Tela.emFrances ? "' enregistré avec succès !" : "' cadastrado com sucesso!"));

                    if ("Reserva".equals(Tela.proximaTelaAposLogin)) {
                        new TelaReserva(super.getStage()).mostrarTela();
                    } else if ("Delivery".equals(Tela.proximaTelaAposLogin)) {
                        new TelaDelivery(super.getStage(),novoCliente.getEmail()).mostrarTela();
                    } else {
                        new TelaInicial(super.getStage()).mostrarTela();
                    }
                    Tela.proximaTelaAposLogin = null;
                }
            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, Tela.emFrances ? "Erreur" : "Erro", Tela.emFrances ? "Erreur lors de l'enregistrement du client." : "Erro ao cadastrar o cliente.");
            } finally {
                if (em != null) em.close();
            }
        });

        VBox painelCriarConta = new VBox(20, titulos, nomes, datas, enderecos, emails, senhas, confirmar);
        painelCriarConta.setAlignment(Pos.TOP_CENTER);
        painelCriarConta.setMaxWidth(400);
        painelCriarConta.setStyle(estiloPainelBranco);

        Label loginTitle = new Label(Tela.emFrances ? "Se Connecter" : "Entrar");
        loginTitle.setFont(playfairFont);
        loginTitle.setStyle("-fx-text-fill: #FFC300");
        Label loginSubtitle = new Label(Tela.emFrances ? "Accédez à votre compte" : "Acesse sua conta");
        loginSubtitle.setFont(interfont1);
        VBox loginTitulos = new VBox(5, loginTitle, loginSubtitle);
        loginTitulos.setAlignment(Pos.CENTER);

        TextField emailTFLogin = new TextField();
        emailTFLogin.setPromptText(Tela.emFrances ? "Saisissez votre e-mail" : "Digite seu e-mail");
        VBox emailLoginVbox = new VBox(5, new HBox(5, criarWebview("/svg/email-svgrepo-com.svg"), new Label("E-mail")), emailTFLogin);

        PasswordField senhaTFLogin = new PasswordField();
        senhaTFLogin.setPromptText(Tela.emFrances ? "Saisissez votre mot de passe" : "Digite sua senha");
        VBox senhaLoginVbox = new VBox(5, new HBox(5, criarWebview("/svg/padlock-svgrepo-com.svg"), new Label(Tela.emFrances ? "Mot de passe" : "Senha")), senhaTFLogin);

        emailTFLogin.setStyle(inputStyle);
        senhaTFLogin.setStyle(inputStyle);

        CheckBox lembrarCb = new CheckBox(Tela.emFrances ? "Se souvenir de moi" : "Lembrar de mim");
        Hyperlink esqueceuLink = new Hyperlink(Tela.emFrances ? "Mot de passe oublié ?" : "Esqueceu a senha?");
        esqueceuLink.setOnAction(event -> mostrarDialogoResetSenha());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox lembrarHbox = new HBox(lembrarCb, spacer, esqueceuLink);
        lembrarHbox.setAlignment(Pos.CENTER_LEFT);

        Button btnEntrar = new Button(Tela.emFrances ? "Entrer" : "Entrar");
        btnEntrar.getStyleClass().add("button");
        btnEntrar.setMaxWidth(Double.MAX_VALUE);
        btnEntrar.setOnAction(event -> {
            String email = emailTFLogin.getText();
            String senha = senhaTFLogin.getText();

            if (email.trim().isEmpty() || senha.trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, Tela.emFrances ? "Champs Vides" : "Campos Vazios", Tela.emFrances ? "Veuillez saisir votre e-mail et votre mot de passe." : "Por favor, digite seu e-mail e senha.");
                return;
            }

            EntityManager em = JpaUtil.getFactory().createEntityManager();
            try {
                TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c WHERE c.email = :email AND c.senha = :senha", Cliente.class);
                query.setParameter("email", email);
                query.setParameter("senha", senha);

                Cliente cliente = query.getSingleResult();
                mostrarAlerta(Alert.AlertType.INFORMATION, Tela.emFrances ? "Connexion Réussie" : "Login Bem-sucedido", (Tela.emFrances ? "Bienvenue, " : "Bem-vindo(a) de volta, ") + cliente.getNome() + "!");

                if ("Reserva".equals(Tela.proximaTelaAposLogin)) {
                    new TelaReserva(super.getStage()).mostrarTela();
                } else if ("Delivery".equals(Tela.proximaTelaAposLogin)) {
                    new TelaDelivery(super.getStage(), cliente.getEmail()).mostrarTela();
                } else {
                    new TelaInicial(super.getStage()).mostrarTela();
                }
                Tela.proximaTelaAposLogin = null;

            } catch (NoResultException e) {
                mostrarAlerta(Alert.AlertType.ERROR, Tela.emFrances ? "Erreur de Connexion" : "Erro de Login", Tela.emFrances ? "E-mail ou mot de passe incorrect." : "E-mail ou senha incorretos.");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, Tela.emFrances ? "Erreur" : "Erro", Tela.emFrances ? "Une erreur inattendue est survenue." : "Ocorreu um erro inesperado.");
            } finally {
                if (em != null) em.close();
            }
        });

        VBox painelLogin = new VBox(20);
        painelLogin.setAlignment(Pos.TOP_CENTER);
        painelLogin.setMaxWidth(400);
        painelLogin.setStyle(estiloPainelBranco);
        painelLogin.getChildren().addAll(loginTitulos, emailLoginVbox, senhaLoginVbox, lembrarHbox, btnEntrar);

        Region separadorCustomizado = new Region();
        separadorCustomizado.setPrefWidth(3);
        separadorCustomizado.setMaxHeight(550);
        separadorCustomizado.setStyle("-fx-background-color: #FFC300;");

        HBox painelPrincipal = new HBox(40, painelCriarConta, separadorCustomizado, painelLogin);
        painelPrincipal.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(painelPrincipal);

        root.setPadding(new Insets(50, 0, 50, 0));

        root.setAlignment(Pos.CENTER);
        root.setStyle(estiloFundoVinho);

        Runnable acaoVoltar = () -> new TelaInicial(super.getStage()).mostrarTela();
        BotaoVoltar.criarEPosicionar(root, acaoVoltar);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());

        return scene;
    }
}