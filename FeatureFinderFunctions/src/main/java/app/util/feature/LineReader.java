package app.util.feature;

import java.util.List;
import java.util.stream.Stream;

public interface LineReader {

    public String getOrigin();
    public Stream<String> lines();
}