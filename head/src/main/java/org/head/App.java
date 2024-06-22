package org.head;


import org.lib.Head;
import java.util.List;
import java.util.stream.Collectors;

public class App
{
    public static void main( String[] args ) {
        try {
            Head head = null;

            if(args.length > 0) {
                List<String> listArgs = List.of(args);

                String linesToShowParameter = listArgs.stream().filter(arg -> arg.matches("-n\\d*")).findFirst().orElse("");
                String bytesToShowParameter = listArgs.stream().filter(arg -> arg.equals("-c")).findFirst().orElse("");

                int linesToShow = linesToShowParameter.isEmpty() ? 10 : Integer.parseInt(linesToShowParameter.split("-n")[1]);
                int bytesToShow = bytesToShowParameter.isEmpty() ? -1 : Integer.parseInt(listArgs.get(listArgs.indexOf("-c") + 1));

                List<String> files = listArgs.stream().filter(arg -> arg.matches("(.*)\\.(.*)")).collect(Collectors.toList());

                head = bytesToShow > 0 ? new Head(files, 0, bytesToShow) : new Head(files, linesToShow);
                String output = bytesToShow > 0 ? head.processInputByNumberOfBytes() : head.processInput();

                System.out.println(output);

                return;
            }

            head = new Head();

            head.processNoInput();
        } catch (Exception error) {
            System.out.println("Something went wrong, verify the list of parameters please!");
        }
    }
}
