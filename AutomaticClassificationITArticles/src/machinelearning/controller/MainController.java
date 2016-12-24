package machinelearning.controller;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import machinelearning.Main;
import machinelearning.util.ColorStyleTransition;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private AnchorPane dragPane;

    @FXML
    private SobreController sobreController;

    public static MainController INSTANCE;


    private ColorStyleTransition cst;


    @FXML
    private void initialize() {
        INSTANCE = this;

        sobreController = inicializarController("../view/sobre.fxml");

        initBorderAnimation();
    }

    private void initBorderAnimation() {
        cst = new ColorStyleTransition(0.3);
        cst.setPane(dragPane);
        cst.setStyleName("drag-pane-border-color");
        cst.setStart(Color.rgb(220, 220, 220));
        cst.setEnd(Color.rgb(12, 112, 223));

    }

    @FXML
    public void handleMenuItemSair() {
        Platform.exit();
    }

    @FXML
    private void handleMenuItemSobre() {
        Alert sobre = new Alert(Alert.AlertType.INFORMATION);
        sobre.setTitle("Sobre");
        sobre.setHeaderText(null);
        sobre.setGraphic(null);
        sobre.getDialogPane().setContent(sobreController.getRoot());
        sobre.showAndWait();
    }

    @FXML
    private void handleDragDone() {
        System.out.println("done");
    }

    @FXML
    private void handleDragDropped() {
        System.out.println("dropped");
    }

    @FXML
    private void handleDragEntered() {
        fadeInBorderDrag();
    }

    @FXML
    private void handleDragExited() {
        fadeOutBorderDrag();
    }

    @FXML
    private void handleDragOver() {

    }

    private void fadeInBorderDrag() {
        cst.setStart(Color.rgb(220, 220, 220));
        cst.setEnd(Color.rgb(77, 64, 185));
        cst.play();
    }

    private void fadeOutBorderDrag() {
        cst.setStart(Color.rgb(77, 64, 185));
        cst.setEnd(Color.rgb(220, 220, 220));
        cst.play();
    }

    public void proximaTela(Pane novo) {
        mostrarTela(novo, 1);
    }

    public void voltarTela(Pane novo) {
        mostrarTela(novo, -1);
    }

    private void mostrarTela(Pane novo, int fator) {
        Node antigo = borderPane.getCenter();

        borderPane.setCenter(novo);
        Main.INSTANCE.getStage().sizeToScene();

        double width = borderPane.getWidth();

        KeyFrame start = new KeyFrame(Duration.ZERO,
                new KeyValue(novo.translateXProperty(), width * fator),
                new KeyValue(antigo.translateXProperty(), 0));

        KeyFrame end = new KeyFrame(Duration.seconds(0.2),
                new KeyValue(novo.translateXProperty(), 0),
                new KeyValue(antigo.translateXProperty(), -width * fator));

        Timeline slide = new Timeline(start, end);
        slide.play();
    }

    private <T> T inicializarController(String fxml_path) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml_path));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }
}
