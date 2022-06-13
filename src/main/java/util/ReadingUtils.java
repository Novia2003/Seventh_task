package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadingUtils {

    public static List<String> readLinesFromFile(String fileName) throws FileNotFoundException {
        List<String> lines;
        try (Scanner scanner = new Scanner(new File(fileName))) {
            lines = new ArrayList<>();
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
        }
        return lines;
    }

    public static String readNumbersFromFile(String fileName) throws FileNotFoundException {
        String string;
        try (Scanner scanner = new Scanner(new File(fileName))) {
            string = scanner.useDelimiter("\\Z").next();
        }
        return string;
    }
}
