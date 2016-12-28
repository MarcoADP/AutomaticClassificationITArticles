package machinelearning.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Util {
    
    public static String[] readPDF(String dir) throws IOException{
        PDDocument pd = null;
        pd = PDDocument.load(new File(dir));
        String text = new PDFTextStripper().getText(pd);
        String subText[];
        subText = text.split(" ");
        return subText;
    } 
    
    
    
    public static ArrayList<String> readTXT(String dir) throws IOException{
        FileReader arq = new FileReader("lista.txt");
        BufferedReader lerArq = new BufferedReader(arq);
        ArrayList<String> list = new ArrayList<>();
        String word = lerArq.readLine();
        while(word != null){
            list.add(word);
            word = lerArq.readLine();
        }
        return list;
    }
    
    public static int compareTexts(ArrayList<String> text1, String[] text2, int i) throws IOException{
        String end = "D:\\Projetos\\AutomaticClassificationITArticles\\AutomaticClassificationITArticles\\lista\\";
        end = end + Integer.toString(i) + ".txt";
        
        PrintWriter gravarArq = new PrintWriter(new FileWriter(end));
        
                
        int count = 0;
        for(String s : text1){
            for(String a : text2){
                if(s.contains(a) && !a.equals("") && a.length()>3){
                    count++;
                    gravarArq.printf("%s\n", a);
                    //System.out.println("S => " + s + " || A => " + a);
                }
            }
        }
        
        gravarArq.printf("TOTAL => %d\n", count);
        gravarArq.close();
        
        return count;
    }
    
    public static void begin() throws IOException{
        //LEITURA TXT
        ArrayList<String> keywords = Util.readTXT("lista.txt");
        /*for(String s : keywords){
            System.out.println(s);
        }*/
        
        FileWriter arq = new FileWriter("D:\\Projetos\\AutomaticClassificationITArticles\\AutomaticClassificationITArticles\\Classificacao.txt");
        PrintWriter gravarArq = new PrintWriter(arq);
        int ia = 0, nia = 0;
        
        for(int i = 1; i <= 341; i++){
            //Leitura PDF
            String dir = "d:/Projetos/AutomaticClassificationITArticles/AutomaticClassificationITArticles/jcr/";
            //int num_text = 1;
            dir = dir + Integer.toString(i) + ".pdf";
            PDDocument pd;
            String sub[];
            try {
                sub = Util.readPDF(dir);
            } catch (Exception e){
                continue;
            }
            
            

            
            //COMPARAR 
            int cont = Util.compareTexts(keywords, sub, i);
            String id;
            if(cont > 300){
                id = "Inteligência Artificial";
                ia++;
            } else {
                id = "NÃO É IA";
                nia++;
            }
            gravarArq.printf("%3d  =>  %4d  -  %s\n", i, cont,id);
            System.out.println("PDF NUMERO " + i + " => " + cont);
            
           
        }
        gravarArq.printf("IA => %d\nNIA => %d", ia, nia);
        arq.close();
        
    }

    
    
    
}
