package automaticclassificationitarticles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class Main {

    public static void main(String[] args) throws IOException {
        
        //LEITURA TXT
        BufferedReader lerArq = Leitura.openTXT("lista.txt");
        ArrayList<String> keywords = Leitura.readTXT(lerArq);
        
        for(int i = 1; i <= 341; i++){
            //Leitura PDF
            String dir = "d:/Projetos/AutomaticClassificationITArticles/AutomaticClassificationITArticles/jcr/";
            //int num_text = 1;
            dir = dir + Integer.toString(i) + ".pdf";
            PDDocument pd;
            try {
                pd = Leitura.openPDF(dir);
            } catch (Exception e){
                continue;
            }
            String text = Leitura.getText(pd);
            String sub[] = Leitura.splitText(text);
            /*for(String s : sub){
                System.out.println(s);
            }*/



            //COMPARAR 
            int cont = Leitura.compareTexts(keywords, sub);
            System.out.println("PDF NUMERO " + i + " => " + cont);
        }
       
    }
    
}
