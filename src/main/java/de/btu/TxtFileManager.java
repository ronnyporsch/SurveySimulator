package de.btu;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class TxtFileManager {
    public static List<String> readQuestionsFromFile() throws IOException {
        File questionsFile = new File(Utility.getDirectoryOfExecutable(), "questions.txt");
        return Files.readLines(questionsFile, Charsets.UTF_8);
    }

    public static void createFileAndWriteString(String text, String outputFileName) throws IOException {
        File file = new File(Utility.getDirectoryOfExecutable()
                + File.separator + "output" + File.separator + outputFileName);
        file.getParentFile().mkdir();
        if (file.createNewFile())
            System.out.println("file " + file.getName() + " created!");
        java.nio.file.Files.write(Paths.get(file.getPath()), text.getBytes());
        System.out.println("your file can be found here: " + file.getPath());
    }
}
