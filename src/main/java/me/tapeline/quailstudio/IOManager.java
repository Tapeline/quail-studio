package me.tapeline.quailstudio;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class IOManager {

    /**
     * Predefined library locations list
     * */
    public static List<String> libFolders = new ArrayList<>(Arrays.asList(
            "",
            "/home/tapeline/", // Only for testing
            "lib/",
            "userlibs/",
            "custom/libs/",
            "addons/libs/"
    ));

    /**
     * Console Put Function W/o \n
     * */
    public void consolePut(String s) {
        System.out.print(s);
    }

    /**
     * Console Input Function W/o \n
     * */
    public String consoleInput(String s) {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    /**
     * Input from a file
     */
    private static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
		catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString().replace('\0', ' ');
    }
    public static String fileInput(String path) {
        /*try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            return "";
        }*/
        return readLineByLineJava8(path);
    }

    /**
     * Check if file exists
     */
    public static boolean fileExists(String path) {
        File f = new File(path);
        return f.exists();
    }

    /**
     * Create blank file
     */
    public static void fileCreate(String path) {
        fileSet(path, "");
    }

    /**
     * Set file contents to sth.
     */
    public static void fileSet(String path, String content) {
        try {
            FileWriter fw = new FileWriter(path);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Append sth. to existing file contents
     */
    public static void filePut(String path, String update) {
        String content = fileInput(path);
        fileSet(path, content + update);
    }

    /**
     * Convert path.file to path/file
     */

    /**
     * Load library contents
     */
    public static String loadLibrary(String path) {
        for (String folder : libFolders) {
            File f = new File(folder + path);
            if (f.exists()) {
                return fileInput(folder + path);
            }
        }
        return null;
    }

}
