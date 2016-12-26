package machinelearning.controller;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import machinelearning.Main;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private DragDropController dragDropController;
    @FXML
    private FileTableController fileTableController;
    @FXML
    private SobreController sobreController;

    public static MainController INSTANCE;

    @FXML
    private void initialize() {
        INSTANCE = this;

        sobreController = inicializarController("../view/sobre.fxml");
    }

    public void addFilesToTable(List<File> files) {
        int addedFiles = fileTableController.addFiles(files);

        String message;
        if (addedFiles == 0) {
            message = "Nenhum arquivo novo adicionado.";
        } else if (addedFiles == 1) {
            message = "1 arquivo novo adicionado.";
        } else {
            message = addedFiles + " arquivos novos adicionados.";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Arquivos adicionados");
        alert.setHeaderText(message);
        alert.setContentText(null);
        alert.showAndWait();
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
