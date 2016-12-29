package machinelearning.readers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PDFReader {

    public static PDDocument openPDF(File file) throws IOException {
        return PDDocument.load(file);
    }

    public static String getText(PDDocument pd) throws IOException {
        return new PDFTextStripper().getText(pd);
    }

    public static String[] splitText(String text) {
        String subText[];
        subText = text.split("\\s+");
        return subText;
    }

    public static List<String> getPDFWords(File file) throws IOException {
        PDDocument pdDocument = openPDF(file);
        String text = getText(pdDocument);
        pdDocument.close();
        return Arrays.asList(splitText(text));
    }

    public static int compareTexts(List<String> textKeyWords, List<String> pdfWords, String filePath) throws IOException {
        //PrintWriter gravarArq = new PrintWriter(new FileWriter(filePath));

        int count = 0;
        for (String tkeyWord : textKeyWords) {
            for (String pdfWord : pdfWords) {
                if (tkeyWord.contains(pdfWord) && !pdfWord.equals("") && pdfWord.length() > 3) {
                    count++;
                    //gravarArq.printf("%s\n", pdfWord);
                }
            }
        }

        //gravarArq.printf("TOTAL => %d\n", count);
        //gravarArq.close();

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
