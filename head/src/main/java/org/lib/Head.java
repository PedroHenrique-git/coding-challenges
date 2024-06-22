package org.lib;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Head {
    private String file;
    private int linesToShow = 10;

    public Head() {}

    public Head(String file, int linesToShow) {
        this.file = file;
        this.linesToShow = linesToShow;
    }

    public String processInput() throws IOException {
        List<String> fileData = readFile();

        StringBuilder b = new StringBuilder();

        fileData.subList(0, Math.min(linesToShow, fileData.size())).forEach(line -> b.append(String.format("%s\n", line)));

        return b.toString();
    }

    public void processNoInput() {
        Scanner scanner = new Scanner(System.in);

        for(int i = 0; i < linesToShow; i++) {
            String line = scanner.nextLine();
            System.out.println(line);
        }
    }

    public List<String> readFile() throws IOException {
        return  Files.readAllLines(new File(file).toPath(), StandardCharsets.UTF_8);
    }
}
