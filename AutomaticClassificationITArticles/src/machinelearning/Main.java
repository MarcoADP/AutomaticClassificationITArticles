package machinelearning;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    public static final String TITULO = "Article Classification";
    public static final int LARGURA = 800;
    public static final int ALTURA = 600;

    private Stage stage;

    public static Main INSTANCE;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        INSTANCE = this;

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/main.fxml"));

            Scene scene = new Scene(root);

            stage.setMinHeight(ALTURA);
            stage.setMinWidth(LARGURA);
            stage.setTitle(TITULO);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
