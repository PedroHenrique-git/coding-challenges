package org.lib;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.IntStream;

public class Head {
    private List<String> files;
    private int linesToShow = 10;
    private int bytesToShow = 31;

    public Head() {}

    public Head(List<String> files) {
        this.files = files;
    }

    public Head(List<String> files, int linesToShow) {
        this.files = files;
        this.linesToShow = linesToShow;
    }

    public Head(List<String> files, int linesToShow, int bytesToShow) {
        this.files = files;
        this.linesToShow = linesToShow;
        this.bytesToShow = bytesToShow;
    }

    public String processInput() throws IOException {
        List<List<String>> filesData = readFile();

        StringBuilder b = new StringBuilder();

        filesData.forEach(fileData -> {
            StringBuilder fileInfoResult = new StringBuilder();

            fileInfoResult.append(String.format("%s\n", fileData.get(0)));
            fileData.subList(1, Math.min(linesToShow, fileData.size())).forEach(line -> fileInfoResult.append(String.format("%s\n", line)));

            fileInfoResult.append("\n\n");

            b.append(fileInfoResult);
        });

        return b.toString();
    }

    public String processInputByNumberOfBytes() throws IOException {
        List<List<String>> filesData = readFile();

        StringBuilder b = new StringBuilder();

        filesData.forEach(fileData -> {
            StringBuilder textBuilder = new StringBuilder();

            String fileName = fileData.get(0);

            IntStream.range(0, fileData.size()).forEach(index -> {
                String line = fileData.get(index);

                if(index > 0)
                    textBuilder.append(String.format("%s\n", line));
            });

            textBuilder.append("\n\n");

            String textByBytes = new String(
                    Arrays.copyOfRange(textBuilder.toString().getBytes(StandardCharsets.UTF_8), 0, bytesToShow),
                    StandardCharsets.UTF_8
            );

            b.append(fileName.trim()).append("\n").append(textByBytes);
        });

        return b.toString();
    }

    public void processNoInput() {
        Scanner scanner = new Scanner(System.in);

        for(int i = 0; i < linesToShow; i++) {
            String line = scanner.nextLine();
            System.out.println(line);
        }
    }

    public List<List<String>> readFile() {
        List<List<String>> filesData = new ArrayList<>();

        files.forEach(file -> {
            try {
                File f = new File(file);
                List<String> fileData = new ArrayList<>();

                fileData.add("==> " + f.getName()  + " <==");
                fileData.addAll(Files.readAllLines(f.toPath(), StandardCharsets.UTF_8));

                filesData.add(fileData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return filesData;
    }
}
