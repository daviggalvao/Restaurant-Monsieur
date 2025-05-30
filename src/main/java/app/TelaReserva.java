
package app;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import classes.Cliente;
import classes.Pagamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import database.FirebaseCliente;
import database.FirebaseReserva;
import classes.Reserva;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TelaReserva {
    private Stage stage;

    public TelaReserva(Stage stage) {
        this.stage = stage;
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public void mostrarReserva() {
        Font playfairFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 52);
        Font playfairFont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 26);
        Font interfont1 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 20);
        Font interfont2 = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 14);

        Label Nome = new Label("Reserve Sua Mesa");
        Nome.setFont(playfairFont);
        Nome.setStyle("-fx-text-fill: #FFC300;");
        Label desc = new Label("Desfrute de uma experiência única em nosso restaurante. \n" +
                "Reserve sua mesa e deixe-nos cuidar de todo os detalhes");
        desc.setFont(interfont2);
        desc.setStyle("-fx-text-fill: #FFC300;");
        Label infores = new Label("Informações da Reserva");
        infores.setFont(playfairFont2);
        infores.setStyle("-fx-text-fill: #FFC300;");
        Label inforesdesc = new Label("Preencha os Dados abaixo para garantir sua mesa");
        inforesdesc.setFont(interfont2);
        inforesdesc.setStyle("-fx-text-fill: #FFC300;");

        TextField nome = new TextField("Seu Nome Completo");
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

        Label lblNome = new Label("\uD83D\uDC64 Nome Completo *");
        lblNome.setStyle("-fx-text-fill: #FFC300;");
        TextField tfNome = new TextField();
        tfNome.setPrefHeight(40);
        tfNome.setPromptText("Seu nome completo");
        tfNome.setStyle(inputStyle);

        Label lblData = new Label("\uD83D\uDCC5 Data *");
        lblData.setStyle("-fx-text-fill: #FFC300;");
        DatePicker dpData = new DatePicker();
        dpData.setPrefHeight(40);

        Label lblHorario = new Label("\u23F0 Horário *");
        lblHorario.setStyle("-fx-text-fill: #FFC300;");
        ComboBox<String> cbHorario = new ComboBox<>();
        cbHorario.getItems().addAll("12:00", "12:30", "13:00", "13:30", "14:00","14:30","15:00",
                "19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30");
        cbHorario.setPromptText("Selecione o horário");
        cbHorario.setPrefHeight(40);
        cbHorario.setStyle(inputStyle);

        Label lblChofer = new Label("\uD83D\uDC64 Chofer *");
        lblChofer.setStyle("-fx-text-fill: #FFC300;");
        CheckBox checkSim = new CheckBox("Quer Chofer?");
        checkSim.setPrefHeight(40);
        checkSim.setStyle(checkBoxStyle);

        Label lblPessoas = new Label("\uD83D\uDC65 Pessoas *");
        lblPessoas.setStyle("-fx-text-fill: #FFC300");
        ComboBox<Integer> cbPessoas = new ComboBox<>();
        cbPessoas.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8);
        cbPessoas.setPromptText("Quantas pessoas?");
        cbPessoas.setPrefHeight(40);
        cbPessoas.setStyle(inputStyle);

        Label lblPagamento = new Label("\uD83D\uDC64 Tipo de Pagamento *");
        lblPagamento.setStyle("-fx-text-fill: #FFC300;");
        ComboBox<String> cbPagamento = new ComboBox<>();
        cbPagamento.getItems().addAll("Pix","Cartão De Crédito","Cartão De Débito",
                "Talão de Cheque","Dinheiro Físico","Pagar Fiado");
        cbPagamento.setPromptText("Selecione o Método de Pagamento");
        cbPagamento.setPrefHeight(40);
        cbPagamento.setStyle(inputStyle);

        inputs.add(lblNome, 0, 0);
        inputs.add(tfNome, 0, 1, 5, 1); // Ajuste o span conforme necessário

        inputs.add(lblHorario, 0, 2, 2, 1);
        inputs.add(cbHorario, 0, 3, 2, 1);

        inputs.add(lblData, 2, 2);
        inputs.add(dpData, 2, 3);

        inputs.add(lblChofer, 4, 2);  // Antes estava na coluna 6
        inputs.add(checkSim, 4, 3);

        inputs.add(lblPessoas, 0, 4);  // Alinhado com Horário acima
        inputs.add(cbPessoas, 0, 5);

        inputs.add(lblPagamento, 2, 4);  // Alinhado com Data acima
        inputs.add(cbPagamento, 2, 5, 2, 1);  // Reduzi o span para 2

        Button confirmar = new Button("Confirmar reserva");
        confirmar.setStyle(
                "-fx-background-color: #500020;" +          // Vinho escuro
                        "-fx-text-fill: #ffcc00;" +                  // Amarelo
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10 20 10 20;" +                 // Espaçamento interno
                        "-fx-background-radius: 10;" +                // Bordas arredondadas
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #ffcc00;" +                // Borda amarela
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;"                            // Cursor de mãozinha no hover
        );

        confirmar.setOnMouseEntered(e -> confirmar.setStyle(
                "-fx-background-color: #ffcc00;" +            // Fundo amarelo ao passar o mouse
                        "-fx-text-fill: #500020;" +                    // Texto vinho escuro
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #ffcc00;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;"
        ));

        confirmar.setOnMouseExited(e -> confirmar.setStyle(
                "-fx-background-color: #500020;" +
                        "-fx-text-fill: #ffcc00;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #ffcc00;" +
                        "-fx-border-width: 2;" +
                        "-fx-cursor: hand;"
        ));

        confirmar.setOnMouseClicked(e -> {
            FirebaseReserva refdatabase = new FirebaseReserva();
            FirebaseCliente refcliente = new FirebaseCliente();

            // Pega os valores dos inputs do usuário
            String nomeCliente = tfNome.getText().trim();
            LocalDate dataReserva = dpData.getValue();
            String horario = cbHorario.getValue();
            boolean chofer = checkSim.isSelected();
            Integer qtdPessoas = cbPessoas.getValue();
            String tipoPagamento = cbPagamento.getValue();

            Pagamento pagamento = new Pagamento(20.0f,nomeCliente,tipoPagamento,0);
            pagamento.ehPix();

            if (nomeCliente.isEmpty() || dataReserva == null || horario == null || qtdPessoas == null || pagamento == null) {
                System.out.println("Por favor, preencha todos os campos obrigatórios.");
                return;
            }

            // Busca o cliente pelo nome de forma assíncrona
            refcliente.lerClienteNome(nomeCliente, new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Pega o primeiro cliente encontrado com o nome informado
                        DataSnapshot clienteSnap = snapshot.getChildren().iterator().next();
                        Cliente cliente = clienteSnap.getValue(Cliente.class);

                        if (cliente == null) {
                            System.err.println("Erro ao converter dados do cliente.");
                            return;
                        }

                        // Cria a nova reserva com os dados coletados
                        Reserva novaReserva = new Reserva(
                                dataReserva.toString(),
                                horario,
                                cliente,
                                qtdPessoas,
                                chofer,
                                pagamento
                        );

                        // Salva a reserva no Firebase
                        refdatabase.criarReserva(novaReserva, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError error, DatabaseReference ref) {
                                Platform.runLater(() -> {
                                    if (error != null) {
                                        mostrarAlerta(AlertType.ERROR, "Erro", "Erro ao criar reserva: " + error.getMessage());
                                    } else {
                                        mostrarAlerta(AlertType.INFORMATION, "Sucesso", "Reserva criada com sucesso!");
                                    }
                                });
                            }
                        });

                    } else {

                        Platform.runLater(() -> {
                            mostrarAlerta(AlertType.WARNING, "Cliente não encontrado", "Não foi possível encontrar cliente com esse nome.");
                            System.out.println("Nenhum cliente encontrado com o nome: " + nomeCliente);
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                    Platform.runLater(() -> {
                        mostrarAlerta(AlertType.ERROR, "Erro", "Erro na consulta: " + error.getMessage());
                        System.err.println("Erro ao consultar cliente: " + error.getMessage());
                    });
                }
            });
        });


        VBox root = new VBox(10, gridtitulo, inputs,confirmar);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle(estiloFundoVinho);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }


}

