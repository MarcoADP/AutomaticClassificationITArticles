package machinelearning.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;
import java.util.List;

public class ResultController {

    @FXML
    private VBox rootPane;
    @FXML
    private GridPane gridPane;

    @FXML
    private Label labelTitle;
    @FXML
    private Label labelCategory;
    @FXML
    private Label labelResult;
    @FXML
    private Separator separator;

    @FXML
    private void initialize() {

    }

    public void setData(String filename, List<String> categories, double[] values) {
        gridPane.getChildren().clear();
        labelTitle.setText("Classificação para: " + filename);

        gridPane.add(labelTitle, 0, 0);
        gridPane.add(separator, 0, 1);
        gridPane.add(labelCategory, 0, 2);
        gridPane.add(labelResult, 1, 2);

        RowConstraints rowConstraints = new RowConstraints(20, 20, 30);

        DecimalFormat df = new DecimalFormat("#.###### %");

        for (int i = 0; i < values.length; i++) {
            Label labelCategory = new Label(categories.get(i) + ": ");
            labelCategory.getStyleClass().add("h3");

            String strValue = df.format(values[i]);
            Label labelValue = new Label(strValue);

            gridPane.add(labelCategory, 0, i + 3);
            gridPane.add(labelValue, 1, i + 3);
            if (gridPane.getRowConstraints().size() < values.length + 3) {
                gridPane.getRowConstraints().add(rowConstraints);
            }
        }
    }

    public VBox getRoot() {
        return rootPane;
    }
}
