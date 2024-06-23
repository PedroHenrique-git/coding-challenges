package org.shell;

import org.shell.history.History;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App
{
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        History history = new History();

        boolean on = true;

        while(on) {
            System.out.print("ccsh > ");

            String command = scanner.nextLine().trim();

            if(command.equalsIgnoreCase("exit")) {
                break;
            }

            if(command.equalsIgnoreCase("history")) {
                history.getHistory().forEach(System.out::println);

                continue;
            }

            List<String> commandArgs = new ArrayList<>();

            commandArgs.add("sh");
            commandArgs.add("-c");
            commandArgs.add(command);

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(commandArgs);

            try {
                history.addEntry(command);
            } catch (IOException _e) {
                // do nothing
            }

            try {
                Process process = processBuilder.start();

                StringBuilder output = new StringBuilder();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream())
                );

                String line;

                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

                int exitVal = process.waitFor();

                if (exitVal == 0) {
                    System.out.println(output);
                } else {
                    System.out.println("something went wrong");
                }
            } catch (Exception _e) {
                System.out.println("something went wrong");
            }
        }
    }
}
