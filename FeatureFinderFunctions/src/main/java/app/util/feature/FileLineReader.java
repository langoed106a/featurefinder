package app.util.feature;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class FileLineReader implements LineReader {
    private String filename;

    public FileLineReader(String filename) {
        this.filename = filename;
    }

    @Override
    public String getOrigin() {
        return filename;
    }

    @Override
    public Stream<String> lines() {
        Stream<String> fileLines=null;
        try {
             fileLines = Files.lines(Paths.get(this.filename));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return fileLines;
    }
}