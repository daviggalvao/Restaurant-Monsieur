package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage; // Necessário para o construtor, mas não para o criarScene

import java.net.MalformedURLException; // Manter se usado
import java.net.URL; // Manter se usado

public class TelaConta extends Tela { // Já estende Tela

    private final String[] NOMES_DOS_MESES = {
            "Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
            "Jul", "Ago", "Set", "Out", "Nov", "Dez"
    };

    public TelaConta(Stage stage) {
        super(stage);
    }

    @Override
    public Scene criarScene() { // MUDANÇA AQUI: de void mostrarTela() para Scene criarScene()
        double[] lucrosArray = {1500.0, 2200.0, 1800.0, 2500.0, 1950.0, 3000.0, 600.0, 1800.0, 1950.0, 1560.0, 1000.0, 1500.0};
        double[] custosArray = {800.0, 1200.0, 950.0, 1300.0, 1000.0, 1500.0, 800.0, 1200.0, 950.0, 1300.0, 1000.0, 1500.0};
        Font playfairFontTituloPagina = null;
        Font interFontParaEixos = null;
        try {
            playfairFontTituloPagina = Font.loadFont(getClass().getResourceAsStream("/fonts/PlayfairDisplay-Bold.ttf"), 62);
            interFontParaEixos = Font.loadFont(getClass().getResourceAsStream("/fonts/Inter-VariableFont_opsz,wght.ttf"), 14);
        } catch (Exception e) {
            System.err.println("Erro ao carregar fontes: " + e.getMessage());
            playfairFontTituloPagina = Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 62);
            interFontParaEixos = Font.font("Arial", 14);
        }

        Label tituloPagina = new Label(Tela.emFrances ? "Compte Bancaire" : "Conta Bancária"); // Usa Tela.emFrances
        tituloPagina.setFont(playfairFontTituloPagina);
        tituloPagina.setStyle("-fx-text-fill: #FFC300;");

        Rectangle sublinhadoTitulo = new Rectangle(400, 4);
        sublinhadoTitulo.setFill(Color.web("#FFC300"));

        VBox blocoTituloPagina = new VBox(5, tituloPagina, sublinhadoTitulo);
        blocoTituloPagina.setAlignment(Pos.CENTER);


        // --- 1. Definição dos Eixos ---
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(null);
        xAxis.setTickLabelFill(Color.WHITE);
        if (interFontParaEixos != null) {
            xAxis.setStyle("-fx-font-family: '" + interFontParaEixos.getFamily() + "'; -fx-font-size: " + interFontParaEixos.getSize() + "px;");
        } else {
            xAxis.setStyle("-fx-font-size: 14px;");
        }

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(null);
        yAxis.setTickUnit(50);
        yAxis.setTickLabelFill(Color.WHITE);
        if (interFontParaEixos != null) {
            yAxis.setStyle("-fx-font-family: '" + interFontParaEixos.getFamily() + "'; -fx-font-size: " + interFontParaEixos.getSize() + "px;");
        } else {
            yAxis.setStyle("-fx-font-size: 14px;");
        }

        // --- 2. Criação do Gráfico de Barras ---
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(null);
        barChart.setStyle("-fx-background-color: transparent;");
        barChart.setLegendVisible(false);
        VBox.setVgrow(barChart, javafx.scene.layout.Priority.ALWAYS);

        // --- 3. Criação da Série de Lucros ---
        XYChart.Series<String, Number> seriesLucro = new XYChart.Series<>();
        seriesLucro.setName("Lucros");

        for (int i = 0; i < lucrosArray.length; i++) {
            String categoria = NOMES_DOS_MESES[i % NOMES_DOS_MESES.length];
            seriesLucro.getData().add(new XYChart.Data<>(categoria, lucrosArray[i]));
        }

        // --- 4. Criação da Série de Custos ---
        XYChart.Series<String, Number> seriesCusto = new XYChart.Series<>();
        seriesCusto.setName("Custos");

        for (int i = 0; i < custosArray.length; i++) {
            String categoria = NOMES_DOS_MESES[i % NOMES_DOS_MESES.length];
            seriesCusto.getData().add(new XYChart.Data<>(categoria, custosArray[i]));
        }

        // --- 5. Adicionar Séries ao Gráfico ---
        barChart.getData().addAll(seriesLucro, seriesCusto);

        // --- 6. Configuração do Layout Principal e Cena ---
        VBox rootPane = new VBox();
        rootPane.setAlignment(Pos.TOP_CENTER);
        rootPane.setSpacing(20);
        rootPane.setPadding(new Insets(30, 20, 20, 20));
        rootPane.getChildren().addAll(blocoTituloPagina, barChart);

        String estiloFundoVinho = "linear-gradient(to right, #30000C, #800020)";
        rootPane.setStyle("-fx-background-color: " + estiloFundoVinho + ";");

        Scene scene = new Scene(rootPane, 800, 700);
        scene.setFill(Color.web("#30000C"));

        String cssInterno = """
            .default-color0.chart-bar { /* Lucros */
                -fx-bar-fill: green;
            }
            .default-color1.chart-bar { /* Custos */
                -fx-bar-fill: red;
            }
            .chart-vertical-grid-lines {
                -fx-stroke: #FFFFFF20;
            }
            .chart-horizontal-grid-lines {
                -fx-stroke: #FFFFFF20;
            }
            .chart-plot-background {
                -fx-background-color: transparent;
            }
            .axis {
                -fx-tick-length: 5;
                -fx-border-color: transparent;
            }
            """;
        try {
            String cssDataUri = "data:text/css," + cssInterno.replace("\n", "").replace(" ", "%20");
            scene.getStylesheets().add(cssDataUri);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar CSS interno à cena: " + e.getMessage());
        }

        // REMOVIDO: stage.setTitle, stage.setScene, stage.show.
        // super.getStage().setTitle("Conta Bancária");
        // super.getStage().setScene(scene);
        // super.getStage().show();

        return scene; // RETORNA A SCENE
    }
}