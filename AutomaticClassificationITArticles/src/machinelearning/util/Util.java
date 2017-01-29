package machinelearning.util;

import java.io.File;

public class Util {

    private Util() {

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
