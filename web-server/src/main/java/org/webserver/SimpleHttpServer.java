package org.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

public class SimpleHttpServer {
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void start() {
        try(ServerSocket server = new ServerSocket()) {
            server.bind(new InetSocketAddress(5000));

            while(true) {
                Socket client = server.accept();

                InputStream stream = client.getInputStream();

                List<String> requestHeaders = readIncomingStream(stream);

                System.out.println(requestHeaders);
            }
        } catch (IOException e) {
            logger.info("Something went wrong during the server initialization");
            logger.info("Exception error: " + e.getMessage());
        }
    }

    private List<String> readIncomingStream(InputStream stream) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream)
        );

        return reader.lines().toList();
    }
}
