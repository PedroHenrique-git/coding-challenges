package org.ccwc;

import org.lib.Ccwc;

import java.util.List;
import java.util.logging.Logger;

public class App
{
    static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main( String[] args ) {
        try {
            List<String> listArgs = List.of(args);

            if(listArgs.isEmpty()) {
                log.info("You must provide some parameter");

                return;
            }

            String operation = listArgs.size() <= 1 ? "" : listArgs.get(0);
            String filePath = listArgs.size() <= 1 ? listArgs.get(0) : listArgs.get(1);

            Ccwc ccwc = new Ccwc(operation, filePath);

            System.out.println(ccwc.processInput());
        } catch (Exception _e) {
            log.info("Something went wrong during the program execution");
        }
    }
}
