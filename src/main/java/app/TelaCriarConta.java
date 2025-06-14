package app;

import classes.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import database.JpaUtil;

import java.time.LocalDate;

public class TelaCriarConta {
    private Stage stage;

    public TelaCriarConta(Stage stage) {
        this.stage = stage;
    }

    public WebView criarWebview(String svgPath){
        WebView webView = new WebView(); //
        webView.setMinSize(10, 10); //
        webView.setPrefSize(20, 20); //
        webView.setMaxSize(30, 30); //

        String svgUrl = getClass().getResource(svgPath).toExternalForm(); //
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center;'>" + //
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain; background-color: transparent;' />" + //
                "</body></html>"; //
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

    public void mostrarTelaCriarConta(){
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
                // Verifique se o cliente já existe usando o CPF
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

                }

            } catch (Exception e) {
                // Se a transação estiver ativa, desfaça (rollback)
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

        Main.setMaxWidth(500);
        Main.setMaxHeight(625);
        Main.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10; " + // Adiciona cantos arredondados
                "-fx-padding: 30;");
        StackPane root = new StackPane(Main);
        root.setAlignment(Pos.CENTER);
        root.setStyle(estiloFundoVinho);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());
        stage.setTitle("Criar Conta");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setMaximized(true);
        stage.show();
    }

}