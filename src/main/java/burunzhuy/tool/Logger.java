package burunzhuy.tool;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Logger {
    static String PATH_DIR = "logs/";

    public static void logToFile(String filename, String data) {
        Path dir = Paths.get("").toAbsolutePath();
        Path file = dir.resolve(PATH_DIR + filename);

        try {
            if (Files.notExists(file)) {
                Files.createDirectories(file.getParent());
                Files.createFile(file);
            }
        } catch (IOException error) {
            System.out.println(error.getMessage());
            return;
        }

        try (FileWriter writer = new FileWriter(file.toFile(), true)) {
            writer.append(LocalDateTime.now() + " - message: " + data).append('\n');
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
