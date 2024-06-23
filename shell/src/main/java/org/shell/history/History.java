package org.shell.history;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class History {
    private final List<String> history = new ArrayList<>();

    public History() {
        List<String> storedHistory = null;

        try {
            storedHistory = Files.readAllLines(getFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        history.addAll(storedHistory);
    }

    private File getFile() throws IOException {
       String userHome = System.getProperty("user.home");

       File file = new File(userHome + "/.ccsh_history");

       if(!file.exists()) {
           boolean _success = file.createNewFile();
       }

       return file;
    }

    public List<String> getHistory() {
        return history;
    }

    public void addEntry(String entry) throws IOException {
        history.add(entry);

        StringBuilder historyStr = new StringBuilder();

        new HashSet<>(history).forEach(e -> historyStr.append(e).append("\n"));

        Files.write(this.getFile().toPath(), historyStr.toString().getBytes());
    }

}
