package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class FileUtil {
    private static Path webPagePath = Path.of("src/main/resources/html/index.html");
    private static Path scriptPath = Path.of("src/main/resources/html/script.js");
    private static Path adminPagePath = Path.of("src/main/resources/html/adminPage.html");
    private static Path stylesPath = Path.of("src/main/resources/html/styles.css");

    public enum FileType {
        htmlPage,
        script,
        adminPage,
        styles
    }
    public static String readFromFile(FileType fileType) {
        Path filePath = Path.of("");
        switch (fileType){
            case htmlPage:
                filePath = webPagePath;
                break;

            case script:
                filePath = scriptPath;
                break;
            case adminPage:
                filePath = adminPagePath;
                break;
            case styles:
                filePath = stylesPath;
                break;
        }

        String fileContent = "";
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader brd = new BufferedReader(new FileReader(String.valueOf(filePath)))) {

            String sCurrentLine;
            while ((sCurrentLine = brd.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileContent = contentBuilder.toString();
        return fileContent;
    }
}
