package machinelearning.controller;

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
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
    private void handleDragDropped(DragEvent dragEvent) {
        List<File> files = dragEvent.getDragboard().getFiles();
        System.out.println("Got " + files.size() + " files");
        for (File file : files) {
            System.out.println(file);
            if (file.isDirectory()) {
                System.out.println("DIRETÓRIO");
                for (String s : file.list()) {
                    System.out.println(s);
                }
            } else {
                try {
                    PDDocument pd = PDFReader.openPDF(file.getAbsolutePath());
                    System.out.println(PDFReader.getText(pd));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        dragEvent.consume();
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
    private void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
        dragEvent.consume();
    }

    private void fadeInBorderDrag() {
        cstBorder.play();
        cstLabel.play();
    }

    private void fadeOutBorderDrag() {
        cstBorder.playInverse();
        cstLabel.playInverse();
    }

    public static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    @FXML
    private void handleMouseClick(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        System.out.println(fileChooser.showOpenMultipleDialog(null));
        //if list != null
    }

}
