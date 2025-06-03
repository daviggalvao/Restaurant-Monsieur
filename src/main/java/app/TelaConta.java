package app; // Assumindo que está no mesmo pacote 'app'

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox; // Para um layout simples, se necessário
import javafx.stage.Stage;

public class TelaConta {

    private Stage stage;

    /**
     * Construtor da TelaConta.
     * @param stage O Stage principal onde a cena desta tela será exibida.
     */
    public TelaConta(Stage stage) {
        this.stage = stage;
    }

    /**
     * Configura e exibe a tela com o gráfico de lucros e custos.
     * @param lucrosArray Array com os valores de lucro.
     * @param custosArray Array com os valores de custo (valores absolutos, serão negativados para o gráfico).
     */
    public void mostrarTelaConta(double[] lucrosArray, double[] custosArray) {
        // --- 1. Definição dos Eixos ---
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Período/Item");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Valor (R$)");

        // --- 2. Criação do Gráfico de Barras ---
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Análise de Lucros e Custos");

        // --- 3. Criação da Série de Lucros ---
        XYChart.Series<String, Number> seriesLucro = new XYChart.Series<>();
        seriesLucro.setName("PROFIT"); // Nome para a legenda

        for (int i = 0; i < lucrosArray.length; i++) {
            String categoria = "Ponto " + (i + 1);
            seriesLucro.getData().add(new XYChart.Data<>(categoria, lucrosArray[i]));
        }

        // --- 4. Criação da Série de Custos ---
        XYChart.Series<String, Number> seriesCusto = new XYChart.Series<>();
        seriesCusto.setName("COST"); // Nome para a legenda

        for (int i = 0; i < custosArray.length; i++) {
            String categoria = "Ponto " + (i + 1); // As categorias devem ser as mesmas
            seriesCusto.getData().add(new XYChart.Data<>(categoria, -custosArray[i])); // Adiciona como negativo
        }

        // --- 5. Adicionar Séries ao Gráfico ---
        barChart.getData().addAll(seriesLucro, seriesCusto);

        // --- 6. Configuração da Cena e Exibição ---
        // VBox root = new VBox(barChart); // Opcional: para adicionar padding ou outros elementos
        // Scene scene = new Scene(root, 800, 600);
        Scene scene = new Scene(barChart, 800, 600); // Usar barChart diretamente se for o único nó

        // Carregar a folha de estilos CSS.
        // Certifique-se que o arquivo 'styles.css' está acessível (mesmo pacote ou classpath)
        // e contém as definições para as cores das barras do gráfico.
        // Exemplo de como era na TelaInicial:
        // scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        // Se o "styles.css" for compartilhado e estiver no caminho correto, a linha abaixo deve funcionar.
        // Caso contrário, ajuste o caminho ou o nome do arquivo.
        String cssPath = "styles.css"; // Nome do arquivo CSS
        if (getClass().getResource(cssPath) != null) {
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        } else {
            System.out.println("Aviso: Arquivo CSS '" + cssPath + "' não encontrado. O gráfico usará estilos padrão.");
        }


        this.stage.setTitle("Análise de Contas - Lucros e Custos");
        this.stage.setScene(scene);
        // this.stage.setMaximized(true); // Opcional, como na TelaInicial
        this.stage.show();
    }
}