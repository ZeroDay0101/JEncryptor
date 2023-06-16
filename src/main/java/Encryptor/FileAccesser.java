package Encryptor;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileAccesser {
    public ArrayList<String> read(File path) {
        ArrayList<String> result = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(path);
            while (myReader.hasNextLine()) {
                result.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return result;
    }

    public void write(File path, String text) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(path, true));
            out.append(text + System.getProperty("line.separator"));

            out.close();
            System.out.println("Successfully wrote line to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void clearTheFile(File path) throws IOException {
        new FileWriter(path, false).close();
    }


}
