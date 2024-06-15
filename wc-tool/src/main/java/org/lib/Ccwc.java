package org.lib;

import org.exception.AppException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Ccwc {
    private String operation;
    private String filePath;

    public Ccwc() {}

    public Ccwc(String operation, String filePath) {
        this.operation = operation;
        this.filePath = filePath;
    }

    public String processInput() throws AppException, IOException {
        Tuple<File, List<String>, List<String>, Long> result = readInput();

        File f = result.valueA;
        List<String> lines = result.valueB;
        List<String> words = result.valueC;
        long numbersOfCharacters = result.valueD;

        long numberOfBytes = f.length();

        switch (operation) {
            case "-c":
                return String.format("%d %s", numberOfBytes, f.getName());
            case "-l":
                return String.format("%d %s", lines.size(), f.getName());
            case "-w":
                return String.format("%d %s", words.size(), f.getName());
            case "-m":
                return String.format("%d %s", numbersOfCharacters, f.getName());
            default:
                return String.format("%d %d %d %s", lines.size(), words.size(), numberOfBytes, f.getName());
        }
    }


    private Tuple<File, List<String>, List<String>, Long> readInput() throws AppException, IOException {
        File file = new File(filePath);

        if(!file.isFile()) {
            file = File.createTempFile("input", ".txt");

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write(filePath);
            writer.close();
        }

        if(!file.exists() || !file.canRead()) {
            throw new AppException("This file can not be read");
        }

        List<String> lines = new ArrayList<>();
        List<String> words = new ArrayList<>();
        long numberOfCharacters = 0;

        Scanner scannerLines = new Scanner(file);
        Scanner scannerWords = new Scanner(file);

        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        while(bufferedReader.read() >= 0) {
            numberOfCharacters += 1;
        }

        while(scannerLines.hasNextLine()) {
            String line = scannerLines.nextLine();

            lines.add(line);
        }

        while(scannerWords.hasNext()) {
            String line = scannerWords.next();

            words.add(line);
        }

        return new Tuple<>(file, lines, words, numberOfCharacters);
    }
}
