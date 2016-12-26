package machinelearning.controller;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

public class FileTableController {

    public Label labelNumFiles;
    @FXML
    private VBox rootPane;

    @FXML
    private TableView<ClassificationTask> tableFiles;
    @FXML
    private TableColumn<ClassificationTask, File> tableColumnFile;
    @FXML
    private TableColumn<ClassificationTask, String> tableColumnClassificacao;
    @FXML
    private TableColumn<ClassificationTask, Double> tableColumnProgresso;
    @FXML
    private TableColumn<ClassificationTask, Status> tableColumnStatus;

    private ObservableList<ClassificationTask> filesToClassify = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        tableFiles.setItems(filesToClassify);

        initBindings();
        initTableColumns();
    }

    private void initBindings() {
        IntegerBinding filesSizeBinding = Bindings.size(filesToClassify);
        labelNumFiles.textProperty().bind(Bindings.format("Número de arquivos: %d", filesSizeBinding));
    }

    private void initTableColumns() {
        tableColumnFile.setCellValueFactory(new PropertyValueFactory<>("file"));
        tableColumnClassificacao.setCellValueFactory(new PropertyValueFactory<>("classification"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnProgresso.setCellValueFactory(new PropertyValueFactory<>("progress"));
        tableColumnProgresso.setCellFactory(ProgressBarTableCell.forTableColumn());
    }

    public int addFiles(List<File> files) {
        int tamanhoAnterior = this.filesToClassify.size();
        files.stream()
                .filter(file -> !this.filesToClassify.contains(new ClassificationTask(file)))
                .forEach(this::addClassificationTask);
        return this.filesToClassify.size() - tamanhoAnterior;
    }

    private void addClassificationTask(File file) {
        filesToClassify.add(new ClassificationTask(file));
        new Thread(filesToClassify.get(filesToClassify.size() - 1)).start();
    }

    public VBox getRoot() {
        return rootPane;
    }

    public enum Status {
        CONCLUIDO("Concluído"),
        NA_FILA("Na fila"),
        ANALISANDO("Analisando...");

        private String text;

        Status(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public static class ClassificationTask extends Task<Void> {

        private File file;
        private StringProperty classification = new SimpleStringProperty();
        private ObjectProperty<Status> status = new SimpleObjectProperty<>();

        public ClassificationTask(File file, Status status, String classification) {
            this.file = file;
            this.status.setValue(status);
            this.classification.setValue(classification);
            updateProgress(0, 0);
        }

        public ClassificationTask(File file) {
            this(file, Status.NA_FILA, " - ");
        }

        @Override
        protected Void call() throws Exception {
            this.updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, 1);
            this.setStatus(Status.NA_FILA);
            Thread.sleep((int)(Math.random()*1000));
            this.setStatus(Status.ANALISANDO);
            for (int i = 0; i < 100; i++) {
                updateProgress((1.0 * i) / 100, 1);
                Thread.sleep((int)(Math.random()*200));
            }
            this.setStatus(Status.CONCLUIDO);
            this.updateProgress(1, 1);
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClassificationTask that = (ClassificationTask) o;

            return file.equals(that.file);
        }

        @Override
        public int hashCode() {
            return file.hashCode();
        }

        public File getFile() {
            return file;
        }

        public String getClassification() {
            return classification.get();
        }

        public StringProperty classificationProperty() {
            return classification;
        }

        public void setClassification(String classification) {
            this.classification.set(classification);
        }

        public Status getStatus() {
            return status.get();
        }

        public ObjectProperty<Status> statusProperty() {
            return status;
        }

        public void setStatus(Status status) {
            this.status.set(status);
        }
    }
}
