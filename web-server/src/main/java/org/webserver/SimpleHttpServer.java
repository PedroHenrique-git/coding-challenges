package org.webserver;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SimpleHttpServer {
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void start() {
        int SERVER_PORT = 5000;

        try(ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(SERVER_PORT));

            while(true) {
                Socket client = server.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                List<String> headers = readInput(in);
                String[] httpConfig = readeBaseHttpConfig(headers);

                String path = httpConfig[1];

                List<String> indexMap = List.of("/", "/index", "/index.html");

                if(indexMap.contains(path)) {
                    out.write("HTTP/1.1 200 OK\r\n");
                    out.write("Content-type: text/html\r\n");
                    out.write("\r\n");

                    out.write(readFileContent("index.html"));
                } else {
                    String content = readFileContent(path);

                    if(content.isEmpty()) {
                        out.write("HTTP/1.1 404\r\n");
                        out.write("Content-type: text/plain\r\n");
                        out.write("\r\n");
                        out.write("Page not found");
                    } else {
                        out.write("HTTP/1.1 200 OK\r\n");
                        out.write("Content-type: text/html\r\n");
                        out.write("\r\n");
                        out.write(content);
                    }
                }

                out.close();
                in.close();
                client.close();
            }
        } catch (IOException e) {
            logger.info("Something went wrong during the server initialization");
            logger.info("Exception error: " + e.getMessage());
        }
    }

    private List<String> readInput(BufferedReader reader) throws IOException {
        List<String> headers = new ArrayList<>();

        String header;

        while ((header = reader.readLine()) != null) {
            headers.add(header);

            if(header.isEmpty()) {
                break;
            }
        }

        return headers;
    }

    private String[] readeBaseHttpConfig(List<String> headers) {
        return headers.get(0).split(" ");
    }

    private String readFileContent(String fileName) {
        String STATIC_FOLDER = "www";

        try {
            return Files.readString(Path.of(String.format(STATIC_FOLDER + "/" + fileName)));
        } catch (IOException e) {
            logger.info("something went wrong when trying to read the file " + e.getMessage());

            return "";
        }
    }
}
