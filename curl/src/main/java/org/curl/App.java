package org.curl;

import org.lib.Ccurl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App
{
    public static void main( String[] args ) throws IOException, InterruptedException {
        System.setProperty("jdk.httpclient.allowRestrictedHeaders", "Connection");

        final List<String> supportedMethods = List.of("POST", "PUT", "DELETE");
        final List<String> listOfArgs = List.of(args);

        String url = listOfArgs.stream().filter(arg -> arg.contains("https://") || arg.contains("http://")).findFirst().orElse("");

        if(url.isEmpty()) {
            System.out.println("Invalid url");

            return;
        }

        String method = "";

        if(listOfArgs.contains("-X")) {
            method = listOfArgs.stream().filter(supportedMethods::contains).findFirst().orElse("GET");
        }

        String body = "";

        if(listOfArgs.contains("-d")) {
            int bodyIndex = listOfArgs.indexOf("-d") + 1;
            body = listOfArgs.get(bodyIndex);
        }

        List<String> headersList = new ArrayList<>();

        if(listOfArgs.contains("-H")) {
            for(int i = 0; i < listOfArgs.size(); i++) {
                if(listOfArgs.get(i).equals("-H")) {
                    String[] header =  listOfArgs.get(i + 1).split(": ");

                    headersList.add(header[0].trim());
                    headersList.add(header[1].trim());
                }            }
        }

        var result = Ccurl.makeRequest(method, url, headersList.toArray(new String[]{}), body);

        StringBuilder builder = new StringBuilder();

        if(listOfArgs.contains("-v")) {
            builder.append(String.format("connecting to %s \n", result.get("requestHost")));
            builder.append(String.format("Sending request %s %s HTTP/%s \n", result.get("requestMethod"), result.get("requestPath"), result.get("httpVersion")));
            builder.append(String.format("Host: %s \n", result.get("requestHost")));
            builder.append(String.format("%s", result.get("requestHeaders")));

            builder.append("\n");

            builder.append(String.format("HTTP/%s %s %s\n", result.get("httpVersion"), result.get("responseStatus"), result.get("responseStatusMessage")));
            builder.append(String.format("Date: %s\n", result.get("responseDate")));
            builder.append(String.format("%s\n", result.get("responseHeaders")));
        }

        builder.append(String.format("%s", result.get("responseBody")));

        System.out.println(builder);
    }
}
