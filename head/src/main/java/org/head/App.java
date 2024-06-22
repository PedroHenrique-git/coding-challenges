package org.head;


import org.lib.Head;
import java.util.List;

public class App
{
    public static void main( String[] args ) {
        try {
            Head head = null;

            if(args.length > 0) {
                List<String> listArgs = List.of(args);

                String linesToShowParameter = listArgs.stream().filter(arg -> arg.matches("-n\\d*")).findFirst().orElse("");
                int linesToShow = linesToShowParameter.isEmpty() ? 10 : Integer.parseInt(linesToShowParameter.split("-n")[1]);

                String file = listArgs.get(listArgs.size() - 1);

                head = new Head(file, linesToShow);

                System.out.println(head.processInput());

                return;
            }

            head = new Head();

            head.processNoInput();
        } catch (Exception error) {
            System.out.println("Something went wrong, verify the list of parameters please!");
        }
    }
}
