package machinelearning.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import machinelearning.readers.PDFReader;
import machinelearning.util.ColorStyleTransition;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DragDropController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label labelDrag;

    private ColorStyleTransition cstBorder;
    private ColorStyleTransition cstLabel;

    @FXML
    private void initialize() {
        initAnimations();
    }

    private void initAnimations() {
        cstBorder = new ColorStyleTransition(0.3);
        cstBorder.setNode(rootPane);
        cstBorder.setStyleName("drag-pane-border-color");
        cstBorder.setStart(Color.rgb(220, 220, 220));
        cstBorder.setEnd(Color.rgb(77, 64, 185));

        cstLabel = new ColorStyleTransition(0.3);
        cstLabel.setNode(labelDrag);
        cstLabel.setStyleName("drag-label-color");
        cstLabel.setStart(Color.rgb(204, 204, 204));
        cstLabel.setEnd(Color.rgb(220, 220, 220));
    }

    @FXML
    private void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
        dragEvent.consume();
    }

    @FXML
    private void handleDragDropped(DragEvent dragEvent) {
        List<File> files = dragEvent.getDragboard().getFiles();

        if (files.size() == 1 && files.get(0).isDirectory()) {
            sendFiles(Arrays.asList(files.get(0).listFiles()));
        } else {
            sendFiles(files);
        }

        dragEvent.consume();
    }

    @FXML
    private void handleDragEntered() {
        cstBorder.play();
        cstLabel.play();
    }

    @FXML
    private void handleDragExited() {
        cstBorder.playInverse();
        cstLabel.playInverse();
    }

    @FXML
    private void handleMouseClick(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        List<File> files = fileChooser.showOpenMultipleDialog(null);

        if (files != null) {
            sendFiles(files);
        }
    }

    private void sendFiles(List<File> files) {
        List<File> pdfFiles = files.stream().filter(PDFReader::isPDF).collect(Collectors.toList());

        Platform.runLater(() -> MainController.INSTANCE.addFilesToTable(pdfFiles));
    }

}
