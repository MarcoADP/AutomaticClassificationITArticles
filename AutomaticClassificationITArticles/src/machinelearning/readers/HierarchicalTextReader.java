package machinelearning.readers;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HierarchicalTextReader {

    public static BufferedReader openTXT(String dir) throws FileNotFoundException {
        FileReader arq = new FileReader("lista.txt");
        return (new BufferedReader(arq));
    }

    public static ArrayList<String> readTXT(BufferedReader arq) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        String word = arq.readLine();
        while (word != null) {
            list.add(word);
            word = arq.readLine();
        }
        return list;
    }
}

