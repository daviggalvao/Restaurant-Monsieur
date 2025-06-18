
package app;

import database.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import classes.Cliente;
import classes.Pagamento;
import classes.Reserva;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TelaReserva extends Tela {

    public TelaReserva(Stage stage) {
        super(stage);
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @Override
    public void mostrarTela() {
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 52);
        Font playfairFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 26);
        Font interfont1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 20);
        Font interfont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 14);

        Label Nome = new Label(Tela.emFrances ? "Réservez votre table" : "Reserve Sua Mesa");
        Nome.setFont(playfairFont);
        Nome.setStyle("-fx-text-fill: #FFC300;");
        Label desc = new Label(Tela.emFrances ? "Vivez une expérience unique dans notre restaurant. \n" +
                "Réservez votre table et laissez - nous nous occuper de tous les détails." : "Desfrute de uma experiência única em nosso restaurante. \n" +
                "Reserve sua mesa e deixe-nos cuidar de todo os detalhes");
        desc.setFont(interfont2);
        desc.setStyle("-fx-text-fill: #FFC300;");
        Label infores = new Label(Tela.emFrances ? "Informations de réservation" : "Informações da Reserva");
        infores.setFont(playfairFont2);
        infores.setStyle("-fx-text-fill: #FFC300;");
        Label inforesdesc = new Label(Tela.emFrances ? "Remplissez les détails ci-dessous pour garantir votre table" : "Preencha os Dados abaixo para garantir sua mesa");
        inforesdesc.setFont(interfont2);
        inforesdesc.setStyle("-fx-text-fill: #FFC300;");

        TextField nome = new TextField(Tela.emFrances ? "Votre nom complet" : "Seu Nome Completo");
        nome.setStyle("-fx-text-fill: #FFC300;");
        VBox titulo = new VBox(Nome, desc);
        VBox informacoes = new VBox(infores, inforesdesc);
        titulo.setAlignment(Pos.CENTER);
        informacoes.setAlignment(Pos.CENTER);

        GridPane gridtitulo = new GridPane();
        gridtitulo.add(titulo, 0, 0);
        gridtitulo.add(informacoes, 0, 1);
        gridtitulo.setVgap(35);
        gridtitulo.setAlignment(Pos.TOP_CENTER); // Centraliza o GridPane na cena
        gridtitulo.getColumnConstraints().add(new ColumnConstraints(1000));;
        String estiloFundoVinho = "-fx-background-color: linear-gradient(to right, #30000C, #800020)";
        gridtitulo.setStyle(estiloFundoVinho);

        GridPane inputs = new GridPane();
        inputs.setHgap(20);
        inputs.setVgap(15);
        inputs.setPadding(new Insets(20, 20, 20, 20));
        inputs.setAlignment(Pos.CENTER);
       inputs.setStyle(estiloFundoVinho);


        String inputStyle = "-fx-background-color: white;\n" +
                "    -fx-background-radius: 5px;\n" +
                "    -fx-border-color: #ddd;\n" +
                "    -fx-border-radius: 5px;\n" +
                "    -fx-border-width: 1px;\n" +
                "    -fx-font-size: 14px;\n" +
                "    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);";

        String checkBoxStyle =
                "-fx-font-size: 14px; " +
                        "-fx-text-fill: #FFC300; " +
                        "-fx-padding: 5px 0; " +

                        // Estilo da caixa não selecionada
                        "-box-background-color: white; " +
                        "-box-border-color: #999999; " +
                        "-box-border-radius: 3px; " +
                        "-box-border-width: 1px; " +
                        "-box-background-radius: 3px; " +
                        "-box-padding: 3px; " +

                        // Estilo do check mark
                        "-mark-color: #4CAF50; " +
                        "-mark-shape: 'M 5 10 L 8 13 L 13 5'; ";

        Label lblNome = new Label(Tela.emFrances ? "\uD83D\uDC64 Nom Complet *" : "\uD83D\uDC64 Nome Completo *");
        lblNome.setStyle("-fx-text-fill: #FFC300;");
        TextField tfNome = new TextField();
        tfNome.setPrefHeight(40);
        tfNome.setPromptText(Tela.emFrances ? "Votre nom complet" : "Seu nome completo");
        tfNome.setStyle(inputStyle);

        Label lblemail = new Label("\uD83D\uDC64 Email *");
        lblemail.setStyle("-fx-text-fill: #FFC300;");
        TextField tfEmail = new TextField();
        tfEmail.setPrefHeight(40);
        tfEmail.setPromptText(Tela.emFrances ? "Votre e-mail complet" : "Seu e-mail completo");
        tfEmail.setStyle(inputStyle);

        Label lblData = new Label(Tela.emFrances ? "\uD83D\uDCC5 Date *" : "\uD83D\uDCC5 Data *");
        lblData.setStyle("-fx-text-fill: #FFC300;");
        DatePicker dpData = new DatePicker();
        dpData.setPrefHeight(40);

        Label lblHorario = new Label(Tela.emFrances ? "\uD83D\uDCC5 Temps *" : "\u23F0 Horário *");
        lblHorario.setStyle("-fx-text-fill: #FFC300;");
        ComboBox<String> cbHorario = new ComboBox<>();
        cbHorario.getItems().addAll("12:00", "12:30", "13:00", "13:30", "14:00","14:30","15:00",
                "19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30");
        cbHorario.setPromptText(Tela.emFrances ? "Sélectionnez l'heure" : "Selecione o horário");
        cbHorario.setPrefHeight(40);
        cbHorario.setStyle(inputStyle);

        Label lblChofer = new Label(Tela.emFrances ?"\uD83D\uDC64 Chauffeur *" : "\uD83D\uDC64 Chofer *");
        lblChofer.setStyle("-fx-text-fill: #FFC300;");
        CheckBox checkSim = new CheckBox(Tela.emFrances ? "Je veux devenir chauffeur" : "Quer Chofer?");
        checkSim.setPrefHeight(40);
        checkSim.setStyle(checkBoxStyle);

        Label lblPessoas = new Label(Tela.emFrances ? "\uD83D\uDC65 Personnes *" : "\uD83D\uDC65 Pessoas *");
        lblPessoas.setStyle("-fx-text-fill: #FFC300");
        ComboBox<Integer> cbPessoas = new ComboBox<>();
        cbPessoas.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);
        cbPessoas.setPromptText(Tela.emFrances ? "Combien de personnes?" : "Quantas pessoas?");
        cbPessoas.setPrefHeight(40);
        cbPessoas.setStyle(inputStyle);

        Label lblPagamento = new Label(Tela.emFrances ? "\uD83D\uDC64 Type de paiwment *" : "\uD83D\uDC64 Tipo de Pagamento *");
        lblPagamento.setStyle("-fx-text-fill: #FFC300;");
        ComboBox<String> cbPagamento = new ComboBox<>();
        cbPagamento.getItems().addAll("Pix",Tela.emFrances ? "Carte de crédit" : "Cartão De Crédito",Tela.emFrances ? "Carte de débit" : "Cartão De Débito",
                Tela.emFrances ? "Chéquier" : "Talão de Cheque",Tela.emFrances ? "Argent physique": "Dinheiro Físico",Tela.emFrances ? "Payer à crédit" : "Pagar Fiado");
        cbPagamento.setPromptText(Tela.emFrances ? "Sélectionnez le mode de paiement" : "Selecione o Método de Pagamento");
        cbPagamento.setPrefHeight(40);
        cbPagamento.setStyle(inputStyle);

        inputs.add(lblNome, 0, 0);
        inputs.add(tfNome, 0, 1, 3, 1);

        inputs.add(lblemail,3,0);
        inputs.add(tfEmail,3,1,2,1);

        inputs.add(lblHorario, 0, 2, 2, 1);
        inputs.add(cbHorario, 0, 3, 2, 1);

        inputs.add(lblData, 2, 2);
        inputs.add(dpData, 2, 3);

        inputs.add(lblChofer, 3, 2);  // Antes estava na coluna 6
        inputs.add(checkSim, 3, 3);

        inputs.add(lblPessoas, 0, 4);  // Alinhado com Horário acima
        inputs.add(cbPessoas, 0, 5);

        inputs.add(lblPagamento, 2, 4);  // Alinhado com Data acima
        inputs.add(cbPagamento, 2, 5, 2, 1);  // Reduzi o span para 2

        Button confirmar = new Button(Tela.emFrances ? "Confirmer la réservation" : "Confirmar reserva");
        confirmar.getStyleClass().add("button");

        confirmar.setOnAction(event -> {
            String name = tfNome.getText();
            String hora = cbHorario.getValue();
            LocalDate data = dpData.getValue();
            String pagamento = cbPagamento.getValue();
            Integer qtdpessoas = cbPessoas.getValue();
            Boolean chofer = checkSim.isSelected();
            String email = tfEmail.getText();


            if (name.trim().isEmpty() || email.trim().isEmpty() || hora.trim().isEmpty()
                    || data == null || qtdpessoas == null || pagamento == null) {

                mostrarAlerta(Alert.AlertType.WARNING,
                        Tela.emFrances ? "Champs obligatoires" : "Campos Obrigatórios",
                        Tela.emFrances ? "Veuillez remplir tous les champs pour continuer." : "Por favor, preencha todos os campos para continuar.");
                return;
            }

            EntityManager em = JpaUtil.getFactory().createEntityManager();

            try {
                TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Pessoa c WHERE c.email = :email", Cliente.class);
                query.setParameter("email", email);

                Cliente cliente = query.getSingleResult();

                Pagamento pagamentoReserva = new Pagamento(20.0f, name, pagamento, 1);
                Reserva reservation = new Reserva(data, hora, cliente, qtdpessoas, chofer, pagamentoReserva);
                em.getTransaction().begin();
                em.persist(reservation);
                em.getTransaction().commit();
                mostrarAlerta(Alert.AlertType.INFORMATION, Tela.emFrances ? "Réservation effectuée" : "Reserva Realizada", Tela.emFrances ? "Réservation effectuée pour le client "+ name +" avec succès !" : "Reserva feita para o cliente" + name + " com sucesso!");

            }catch (NoResultException e) {
                mostrarAlerta(Alert.AlertType.WARNING, Tela.emFrances ? "La recherche a renvoyé une erreur" : "Busca retornou erro", Tela.emFrances ? "Aucun compte avec" + email + " n'a été trouvé" : "Nenhuma conta com o" + email + " foi encontrada");
            }catch (Exception e) {
                if (em.getTransaction().isActive()) {em.getTransaction().rollback();}
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.WARNING, Tela.emFrances ? "Erreur" : "Erro", Tela.emFrances ? "Erreur lors de l'enregistrement du compte" : "Erro ao cadastrar a conta");
            } finally {
                if(em != null){em.close();}
            }

        });


        VBox root = new VBox(10, gridtitulo, inputs,confirmar);
        root.setAlignment(Pos.CENTER);
        root.setStyle(estiloFundoVinho);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/button.css").toExternalForm());
        super.getStage().setScene(scene);
        super.getStage().setMaximized(true);
        super.getStage().show();
    }
}

