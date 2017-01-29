package machinelearning.controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

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

    @FXML
    private RadioMenuItem radioMenuItemT1;
    @FXML
    private RadioMenuItem radioMenuItemT2;
    @FXML
    private RadioMenuItem radioMenuItemT4;
    @FXML
    private RadioMenuItem radioMenuItemT8;
    @FXML
    private RadioMenuItem radioMenuItemT16;

    private ToggleGroup toggleGroup;

    public static MainController INSTANCE;

    @FXML
    private void initialize() {
        INSTANCE = this;

        sobreController = initController("../view/sobre.fxml");

        configToggleGroup();
    }

    private void configToggleGroup() {
        toggleGroup = new ToggleGroup();
        radioMenuItemT1.setSelected(true);
        radioMenuItemT1.setToggleGroup(toggleGroup);
        radioMenuItemT2.setToggleGroup(toggleGroup);
        radioMenuItemT4.setToggleGroup(toggleGroup);
        radioMenuItemT8.setToggleGroup(toggleGroup);
        radioMenuItemT16.setToggleGroup(toggleGroup);

        radioMenuItemT1.setOnAction(event -> fileTableController.setNThreads(1));
        radioMenuItemT2.setOnAction(event -> fileTableController.setNThreads(2));
        radioMenuItemT4.setOnAction(event -> fileTableController.setNThreads(4));
        radioMenuItemT8.setOnAction(event -> fileTableController.setNThreads(8));
        radioMenuItemT16.setOnAction(event -> fileTableController.setNThreads(16));
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

    public void showErrorMessage(String title, String msg) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(title);
        error.setContentText(msg);
        error.showAndWait();
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

    public static <T> T initController(String fxml_path) {
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource(fxml_path));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader.getController();
    }
}
