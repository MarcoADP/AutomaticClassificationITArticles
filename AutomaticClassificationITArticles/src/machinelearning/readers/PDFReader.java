package machinelearning.readers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.util.ArrayList;

public class PDFReader {

    public static PDDocument openPDF(String dir) throws IOException {
        return PDDocument.load(new File(dir));
    }

    public static String getText(PDDocument pd) throws IOException {
        return new PDFTextStripper().getText(pd);
    }

    public static String[] splitText(String text) {
        String subText[];
        subText = text.split(" ");
        return subText;
    }

    public static int compareTexts(ArrayList<String> text1, String[] text2) {
        int count = 0;
        for (String s : text1) {
            for (String a : text2) {
                if (s.contains(a) && !a.equals("") && a.length() > 3) {
                    count++;
                    //System.out.println("S => " + s + " || A => " + a);
                }
            }
        }
        return count;
    }

    public static boolean isPDF(File file) {
        String extension = getFileExtension(file.getAbsolutePath());
        return extension.equalsIgnoreCase("PDF");
    }

    public static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
