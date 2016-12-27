package machinelearning;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;


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

        stage.setOnCloseRequest(event -> System.exit(0));

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


//        //LEITURA TXT
//        BufferedReader lerArq = PDFReader.openTXT("lista.txt");
//        ArrayList<String> keywords = PDFReader.readTXT(lerArq);
//
//        for(int i = 1; i <= 341; i++){
//            //PDFReader PDF
//            String dir = "C:/Users/Otávio/Desktop/jcr/";
//            //int num_text = 1;
//            dir = dir + Integer.toString(i) + ".readers";
//            PDDocument pd;
//            try {
//                pd = PDFReader.openPDF(dir);
//            } catch (Exception e){
//                continue;
//            }
//            String text = PDFReader.getText(pd);
//            String sub[] = PDFReader.splitText(text);
//            /*for(String s : sub){
//                System.out.println(s);
//            }*/
//
//
//
//            //COMPARAR
//            int cont = PDFReader.compareTexts(keywords, sub);
//            System.out.println("PDF NUMERO " + i + " => " + cont);
//        }

        launch(args);

    }

}
