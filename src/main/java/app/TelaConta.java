package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TelaConta extends Tela {

    private final String[] NOMES_DOS_MESES = Tela.emFrances ? { "Jan", "Fév", "Mar", "Avr", "Mai", "Jui", "Jui", "Aoû", "Sep", "Oct", "Nov", "Déc" } : { "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez" };

    public TelaConta(Stage stage) {
        super(stage);
    }

    @Override
    public Scene criarScene() {
        double[] lucrosArray = {1500.0, 2200.0, 1800.0, 2500.0, 1950.0, 3000.0, 600.0, 1800.0, 1950.0, 1560.0, 1000.0, 1500.0};
        double[] custosArray = {800.0, 1200.0, 950.0, 1300.0, 1000.0, 1500.0, 800.0, 1200.0, 950.0, 1300.0, 1000.0, 1500.0};
        Font playfairFontTituloPagina = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62);
        Font interFontParaEixos = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 14);

        Label tituloPagina = new Label(Tela.emFrances ? "Compte Bancaire" : "Conta Bancária");
        tituloPagina.setFont(playfairFontTituloPagina);
        tituloPagina.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhadoTitulo = new Rectangle(400, 4);
        sublinhadoTitulo.setFill(Color.web("#FFC300"));

        VBox blocoTituloPagina = new VBox(5, tituloPagina, sublinhadoTitulo);
        blocoTituloPagina.setAlignment(Pos.CENTER);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setTickLabelFill(Color.WHITE);
        xAxis.setStyle("-fx-font-family: '" + interFontParaEixos.getFamily() + "'; -fx-font-size: " + interFontParaEixos.getSize() + "px;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setTickLabelFill(Color.WHITE);
        yAxis.setStyle("-fx-font-family: '" + interFontParaEixos.getFamily() + "'; -fx-font-size: " + interFontParaEixos.getSize() + "px;");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(false);
        VBox.setVgrow(barChart, javafx.scene.layout.Priority.ALWAYS);

        XYChart.Series<String, Number> seriesLucro = new XYChart.Series<>();
        for (int i = 0; i < lucrosArray.length; i++) {
            seriesLucro.getData().add(new XYChart.Data<>(NOMES_DOS_MESES[i], lucrosArray[i]));
        }
        XYChart.Series<String, Number> seriesCusto = new XYChart.Series<>();
        for (int i = 0; i < custosArray.length; i++) {
            seriesCusto.getData().add(new XYChart.Data<>(NOMES_DOS_MESES[i], custosArray[i]));
        }
        barChart.getData().addAll(seriesLucro, seriesCusto);

        VBox rootPane = new VBox();
        rootPane.setAlignment(Pos.TOP_CENTER);
        rootPane.setSpacing(20);
        rootPane.setPadding(new Insets(30, 20, 20, 20));
        rootPane.getChildren().addAll(blocoTituloPagina, barChart);

        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)";
        rootPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        // --- MUDANÇAS PARA ADICIONAR O BOTÃO VOLTAR ---
        StackPane stackPane = new StackPane(rootPane);
        stackPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        Runnable acaoVoltar = () -> new TelaServicos(super.getStage()).mostrarTela();
        BotaoVoltar.criarEPosicionar(stackPane, acaoVoltar);

        Scene scene = new Scene(stackPane, 800, 700);

        String cssInterno = ".default-color0.chart-bar { -fx-bar-fill: green; } .default-color1.chart-bar { -fx-bar-fill: red; }";
        scene.getStylesheets().add("data:text/css," + cssInterno);

        return scene;
    }
}
