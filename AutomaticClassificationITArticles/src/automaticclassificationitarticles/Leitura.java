package automaticclassificationitarticles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Leitura {
    
    public static PDDocument openPDF(String dir) throws IOException{
        PDDocument pd = null;
        pd = PDDocument.load(new File(dir));
        return pd;
    } 
    
    public static String getText(PDDocument pd) throws IOException{
        return new PDFTextStripper().getText(pd);
    }
    
    public static String[] splitText(String text){
        String subText[];
        subText = text.split(" ");
        return subText;
    }
    
    public static BufferedReader openTXT(String dir) throws FileNotFoundException{
        FileReader arq = new FileReader("lista.txt");
        return (new BufferedReader(arq));
    }
    
    public static ArrayList<String> readTXT(BufferedReader arq) throws IOException{
        ArrayList<String> list = new ArrayList<>();
        String word = arq.readLine();
        while(word != null){
            list.add(word);
            word = arq.readLine();
        }
        return list;
    }
    
    public static int compareTexts(ArrayList<String> text1, String[] text2){
        int count = 0;
        for(String s : text1){
            for(String a : text2){
                if(s.contains(a) && !a.equals("") && a.length()>3){
                    count++;
                    //System.out.println("S => " + s + " || A => " + a);
                }
            }
        }
        
        
        return count;
    }
}
