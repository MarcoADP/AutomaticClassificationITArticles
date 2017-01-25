package machinelearning.controller;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import machinelearning.neuralnet.NeuralNetwork;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileTableController {

    @FXML
    private VBox rootPane;
    @FXML
    private TableView<ClassificationTask> tableFiles;
    @FXML
    private TableColumn<ClassificationTask, Boolean> tableColumnCheckBox;

    @FXML
    private TableColumn<ClassificationTask, File> tableColumnFile;
    @FXML
    private TableColumn<ClassificationTask, String> tableColumnClassificacao;
    @FXML
    private TableColumn<ClassificationTask, Boolean> tableColumnAction;
    //@FXML
    //private TableColumn<ClassificationTask, Double> tableColumnProgresso;
    @FXML
    private TableColumn<ClassificationTask, Status> tableColumnStatus;
    @FXML
    private Label labelNumFiles;

    @FXML
    private Label labelAnalisando;
    @FXML
    private Label labelTotalAnalisados;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button btnAnalisar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnRemover;

    private ObservableList<ClassificationTask> files = FXCollections.observableArrayList();

    private ObservableList<ClassificationTask> selectedFiles = FXCollections.observableArrayList();
    private IntegerProperty numTasksCompleted = new SimpleIntegerProperty(0);

    private IntegerProperty numTotalAnalisados = new SimpleIntegerProperty(0);
    private BooleanProperty tasksRunning = new SimpleBooleanProperty(false);

    private ClassificationAllTask cTask;
    private static final int DEFAULT_THREAD_NUM = 1;

    private int nThreads;

    @FXML
    private void initialize() {
        tableFiles.setItems(files);
        configBindings();
        configTable();
        setNThreads(DEFAULT_THREAD_NUM);
    }

    private void configBindings() {
        IntegerBinding filesSizeBinding = Bindings.size(files);
        IntegerBinding selectedFilesSizeBinding = Bindings.size(selectedFiles);

        labelNumFiles.textProperty().bind(Bindings.format("Número de arquivos: %d", filesSizeBinding));
        labelAnalisando.textProperty().bind(Bindings.format("Analisando: %d/%d", numTasksCompleted, selectedFilesSizeBinding));
        labelTotalAnalisados.textProperty().bind(Bindings.format("Total analisados: %d", numTotalAnalisados));

        btnAnalisar.disableProperty().bind(tasksRunning.or(selectedFilesSizeBinding.isEqualTo(0)));
        btnCancelar.disableProperty().bind(tasksRunning.not());
        btnRemover.disableProperty().bind(tasksRunning.or(selectedFilesSizeBinding.isEqualTo(0)));
    }

    private void configTable() {
        tableColumnCheckBox.setCellValueFactory(new PropertyValueFactory<>("selecionado"));
        tableColumnCheckBox.setCellFactory(CheckBoxTableCell.forTableColumn(tableColumnCheckBox));

        tableColumnFile.setCellValueFactory(new PropertyValueFactory<>("file"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableColumnAction.setCellValueFactory(features -> new SimpleBooleanProperty(features.getValue() != null));
        tableColumnAction.setCellFactory(tableColumn -> new ButtonCell(tableFiles));

        //tableColumnProgresso.setCellValueFactory(new PropertyValueFactory<>("progress"));
        //tableColumnProgresso.setCellFactory(ProgressBarTableCell.forTableColumn());

        tableColumnClassificacao.setCellValueFactory(new PropertyValueFactory<>("classification"));
        tableColumnClassificacao.setCellFactory(param -> {
            return new TableCell<ClassificationTask, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    getStyleClass().remove("black");
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                        if (item.toUpperCase().contains("NÃO")) {
                            getStyleClass().add("black");
                        }
                    }
                }
            };
        });

        tableFiles.setRowFactory(tv -> {
            TableRow<ClassificationTask> row = new TableRow<>();
            row.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    //row.disableProperty().bind(newValue.statusProperty().isEqualTo(Status.CONCLUIDO).or(tasksRunning));
                    row.disableProperty().bind(tasksRunning);
                }
            });
            return row;
        });
    }

    public int addFiles(List<File> files) {
        int tamanhoAnterior = this.files.size();
        files.stream()
                .filter(file -> !this.files.contains(new ClassificationTask(file)))
                .forEach(this::addClassificationTask);
        return this.files.size() - tamanhoAnterior;
    }

    private void addClassificationTask(File file) {
        ClassificationTask task = new ClassificationTask(file, Status.NAO_INICIADO, "-");
        files.add(task);
    }

    private void addClassificationTask(ClassificationTask oldTask) {
        ClassificationTask newTask = new ClassificationTask(oldTask.file, oldTask.getStatus(), "-");
        files.add(newTask);
    }

    @FXML
    private void handleBtnAnalisar() {
        cTask = new ClassificationAllTask(selectedFiles, nThreads);

        ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

        numTasksCompleted.setValue(0);
        tasksRunning.set(true);

        executor.execute(cTask);
    }

    @FXML
    private void handleBtnCancelar() {
        for (ClassificationTask task : selectedFiles) {
            if (task.getStatus() != Status.CONCLUIDO) {
                task.cancel();
            }
        }
        cTask.cancel();
    }

    @FXML
    private void handleBtnRemover() {
        for (int i = files.size() - 1; i >= 0; i--) {
            final ClassificationTask task = files.get(i);
            if (task.getStatus() != Status.CONCLUIDO && task.isSelecionado()) {
                files.remove(task);
                selectedFiles.remove(task);
            }
        }
    }

    private void onExecutorServiceFinished() {
        tasksRunning.set(false);

        for (int i = selectedFiles.size() - 1; i >= 0; i--) {
            final ClassificationTask task = selectedFiles.get(i);
            if (task.getStatus() == Status.CONCLUIDO) {
                selectedFiles.remove(task);
            } else if (task.getStatus() == Status.CANCELADO) {
                selectedFiles.remove(task);
                files.remove(task);
                addClassificationTask(task);
            }
        }

        numTasksCompleted.set(0);
    }

    public VBox getRoot() {
        return rootPane;
    }

    public int getNThreads() {
        return nThreads;
    }

    public void setNThreads(int nThreads) {
        this.nThreads = nThreads;
    }

    public enum Status {
        ANALISANDO("Analisando..."),
        CANCELADO("Cancelado"),
        CONCLUIDO("Concluído"),
        NA_FILA("Na fila"),
        NAO_INICIADO("Não iniciado");


        private String text;

        Status(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public class ClassificationTask extends Task<Void> {

        private File file;
        private StringProperty classification = new SimpleStringProperty();
        private ObjectProperty<Status> status = new SimpleObjectProperty<>();
        private BooleanProperty selecionado = new SimpleBooleanProperty();

        public ClassificationTask(File file, Status status, String classification) {
            this.file = file;
            setStatus(status);
            setClassification(classification);
            updateProgress(0, 0);

            selecionadoProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    if (!selectedFiles.contains(this) && this.status.get() != Status.CONCLUIDO) {
                        selectedFiles.add(this);
                    }
                } else {
                    selectedFiles.remove(this);
                }
            });
            setSelecionado(true);
        }

        public ClassificationTask(File file) {
            this.file = file;
        }

        @Override
        protected Void call() throws Exception {
            updateProgress(0, 1);

            NeuralNetwork neuralNetwork = new NeuralNetwork(new File("ArtificialIntelligenceAreas.txt"));
            String classification = neuralNetwork.classificarTexto(file);

            setClassification(classification);

            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            this.setStatus(Status.CONCLUIDO);
            this.updateProgress(1, 1);

            numTasksCompleted.setValue(numTasksCompleted.get() + 1);
            numTotalAnalisados.set(numTotalAnalisados.get() + 1);
        }

        @Override
        protected void cancelled() {
            super.cancelled();
            setStatus(Status.CANCELADO);
            updateProgress(0, 0);
        }

        @Override
        protected void running() {
            super.running();
            this.setStatus(Status.ANALISANDO);
        }

        @Override
        protected void scheduled() {
            super.scheduled();
            this.setStatus(Status.NA_FILA);
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

        public boolean isSelecionado() {
            return selecionado.get();
        }

        public BooleanProperty selecionadoProperty() {
            return selecionado;
        }

        public void setSelecionado(boolean selecionado) {
            this.selecionado.set(selecionado);
        }
    }

    public class ClassificationAllTask extends Task<Void> {

        private List<ClassificationTask> tasks;

        private DoubleProperty pProperty = new SimpleDoubleProperty(0.0);

        private ExecutorService executor;

        public ClassificationAllTask(List<ClassificationTask> tasks, int nThreads) {
            this.tasks = tasks;
            progressBar.progressProperty().bind(pProperty.divide(tasks.size()));

            executor = Executors.newFixedThreadPool(nThreads, r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });
        }

        @Override
        protected Void call() throws Exception {
            this.updateProgress(0.0, tasks.size());
            pProperty.set(0.0);

            for (ClassificationTask task : tasks) {
                task.setStatus(Status.NA_FILA);
                task.progressProperty().addListener((observable, oldValue, newValue) -> {
                    double progress = newValue.doubleValue() - oldValue.doubleValue();
                    if (Double.isNaN(progress)) progress = 0.0;
                    pProperty.setValue(pProperty.getValue() + progress);
                });

                executor.execute(task);
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            return null;
        }

        @Override
        protected void cancelled() {
            super.cancelled();
            updateProgress(0, 0);
            onExecutorServiceFinished();
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            updateProgress(1, 1);
            onExecutorServiceFinished();
        }
    }
    
    private class ButtonCell extends TableCell<ClassificationTask, Boolean> {
        final Button btnShow = new Button("Visualizar");
        final StackPane paddedButton = new StackPane();

        public ButtonCell(final TableView table) {
            paddedButton.setPadding(new Insets(1));
            paddedButton.getChildren().add(btnShow);
            btnShow.setOnAction(event -> {
                showClassificationInfo(getTableView().getItems().get(getIndex()));
            });
            btnShow.getStyleClass().add("btn-action");
        }

        @Override protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            btnShow.disableProperty().unbind();

            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(paddedButton);
                btnShow.disableProperty().bind(getTableView().getItems().get(getIndex()).statusProperty().isEqualTo(Status.CONCLUIDO).not());
            } else {
                setGraphic(null);
            }
        }
    }

    private void showClassificationInfo(ClassificationTask classificationTask) {
        System.out.println(classificationTask.file);
    }
}
