package machinelearning.readers;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextReader {

    private TextReader() { }

    public static List<String> getKeyWords(String filepath) {
        List<String> words = new ArrayList<>();

        Path path = Paths.get(filepath);

        try (Stream<String> lines = Files.lines(path)) {
            words = lines
                    //.map(s -> s.substring(s.indexOf(" ") + 1))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            //System.out.println("");
            e.printStackTrace();
        }
        
        /*for(String s : words){
            System.out.println(s);
        }*/
        
        return words;
    }

}

