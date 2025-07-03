package app;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;

public class BotaoVoltar extends Button {

    public BotaoVoltar(Runnable acaoDeVoltar) {
        super();

        WebView backIcon = criarWebview("/svg/back-svgrepo-com.svg");
        this.setGraphic(backIcon);

        this.setPrefSize(35, 35);
        this.setMinSize(35, 35);
        this.setMaxSize(35, 35);

        this.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        this.setCursor(Cursor.HAND);

        this.setOnMouseEntered(e -> this.setCursor(Cursor.HAND));
        this.setOnMouseExited(e -> this.setCursor(Cursor.DEFAULT));

        this.setOnAction(e -> acaoDeVoltar.run());
    }

    private WebView criarWebview(String svgPath){
        WebView webView = new WebView();
        webView.setStyle("-fx-background-color: transparent;");
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

    public static BotaoVoltar criarEPosicionar(StackPane stackPane, Runnable acaoDeVoltar) {
        BotaoVoltar backButton = new BotaoVoltar(acaoDeVoltar);
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(20));
        stackPane.getChildren().add(backButton);
        return backButton;
    }
}