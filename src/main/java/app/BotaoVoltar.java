package app;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;

public class BotaoVoltar extends Button {

    /**
     * Construtor para o botão de voltar genérico.
     * @param acaoDeVoltar Uma ação (Runnable) a ser executada quando o botão for clicado.
     */
    public BotaoVoltar(Runnable acaoDeVoltar) {
        super(); // Chama o construtor da classe Button

        // Configurações do WebView para o ícone SVG
        WebView backIcon = criarWebview("/svg/back-svgrepo-com.svg"); // Caminho do SVG da seta
        this.setGraphic(backIcon); // Define o SVG como gráfico do botão

        // Ajustando o tamanho do botão
        this.setPrefSize(35, 35);
        this.setMinSize(35, 35);
        this.setMaxSize(35, 35);

        // Estilo e cursor
        this.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        this.setCursor(Cursor.HAND);

        // Efeitos de hover
        this.setOnMouseEntered(e -> this.setCursor(Cursor.HAND));
        this.setOnMouseExited(e -> this.setCursor(Cursor.DEFAULT));

        // Ação de voltar agora é flexível
        this.setOnAction(e -> acaoDeVoltar.run());
    }

    /**
     * Método auxiliar para criar um WebView com um SVG.
     */
    private WebView criarWebview(String svgPath){
        WebView webView = new WebView();
        webView.setStyle("-fx-background-color: transparent;"); // Garante fundo transparente
        webView.setMinSize(25, 25);
        webView.setPrefSize(25, 25);
        webView.setMaxSize(25, 25);
        webView.setMouseTransparent(true);

        String svgUrl = getClass().getResource(svgPath).toExternalForm();
        String html = "<html><body style='margin:0; overflow:hidden; display:flex; justify-content:center; align-items:center; background-color: transparent;'>" +
                "<img src='" + svgUrl + "' style='width:100%; height:100%; object-fit:contain;' />" +
                "</body></html>";
        webView.getEngine().loadContent(html);
        return webView;
    }

    /**
     * Método auxiliar para criar e posicionar o botão de voltar em um StackPane.
     * @param stackPane O StackPane onde o botão será adicionado.
     * @param acaoDeVoltar A ação a ser executada ao clicar no botão.
     * @return O BotaoVoltar criado e posicionado.
     */
    public static BotaoVoltar criarEPosicionar(StackPane stackPane, Runnable acaoDeVoltar) {
        BotaoVoltar backButton = new BotaoVoltar(acaoDeVoltar);
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(20));
        stackPane.getChildren().add(backButton);
        return backButton;
    }
}