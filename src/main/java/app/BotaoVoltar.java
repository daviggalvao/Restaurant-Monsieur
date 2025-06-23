package app;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.stage.Stage; // Necessário para a navegação

public class BotaoVoltar extends Button {

    private Stage stage; // Referência para a Stage principal

    /**
     * Construtor para o botão de voltar.
     * Ao ser clicado, ele volta para a TelaInicial.
     * @param stage A Stage principal da aplicação, necessária para a navegação.
     */
    public BotaoVoltar(Stage stage) {
        super(); // Chama o construtor da classe Button
        this.stage = stage;

        // Configurações do WebView para o ícone SVG
        WebView backIcon = criarWebview("/svg/back-svgrepo-com.svg"); // Caminho do SVG da seta
        this.setGraphic(backIcon); // Define o SVG como gráfico do botão

        // Ajustando o tamanho do botão para ele se ajustar ao tamanho do SVG
        this.setPrefSize(35, 35);
        this.setMinSize(35, 35);
        this.setMaxSize(35, 35);

        // Estilo para o botão de voltar (totalmente transparente)
        this.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        this.setCursor(Cursor.HAND); // Cursor de mão

        // Efeitos de hover (manter transparente, mas mudar cursor)
        this.setOnMouseEntered(e -> {
            this.setCursor(Cursor.HAND);
            // Opcional: se quiser um feedback visual bem sutil no hover, pode adicionar:
            // this.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 5; -fx-border-width: 0;");
        });
        this.setOnMouseExited(e -> {
            this.setCursor(Cursor.DEFAULT);
            this.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        });

        // Ação de voltar para a TelaInicial
        this.setOnAction(e -> {
            new TelaInicial(this.stage).mostrarTela();
        });
    }

    /**
     * Método auxiliar para criar um WebView com um SVG.
     * Reutiliza a lógica existente, garantindo que o WebView seja transparente a cliques.
     * @param svgPath Caminho do arquivo SVG.
     * @return Um WebView configurado com o SVG.
     */
    private WebView criarWebview(String svgPath){
        WebView webView = new WebView();

        // --- LINHA ADICIONADA ---
        // Garante que o fundo do próprio componente WebView seja transparente.
        webView.setStyle("-fx-background-color: transparent;");

        webView.setMinSize(25, 25);
        webView.setPrefSize(25, 25);
        webView.setMaxSize(25, 25);
        webView.setMouseTransparent(true); // Permite cliques passarem para o botão pai

        String svgUrl = getClass().getResource(svgPath).toExternalForm();
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center; background-color: transparent;'>" +
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain;' />" +
                "</body></html>";
        webView.getEngine().loadContent(html);
        return webView;
    }

    /**
     * Método auxiliar para posicionar o botão de voltar em um StackPane.
     * @param stackPane O StackPane onde o botão será adicionado.
     * @param stage A Stage principal (necessária para obter o Stage da tela atual)
     * @return O BotaoVoltar criado e posicionado.
     */
    public static BotaoVoltar criarEPosicionar(StackPane stackPane, Stage stage) {
        BotaoVoltar backButton = new BotaoVoltar(stage);
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(20));
        stackPane.getChildren().add(backButton);
        return backButton;
    }
}