package app.util.feature;

import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import app.util.feature.TextDocument;

public class JsonLineReader {
    private ObjectMapper objectMapper;
    private StringBuilder id_builder=new StringBuilder(),
                          name_builder=new StringBuilder(),
                          contents_builder=new StringBuilder();
    private String language, origin;
    private Map<String, StringBuilder> field_mappings=Map.of("Message",contents_builder, "id", id_builder, "From ID", name_builder);

    public JsonLineReader(String language) {
        this.objectMapper = new ObjectMapper();
        this.language = language;
    }

    public void read(LineReader lineReader) {
        Stream<String> inputLines = null;
        try {
             this.origin = lineReader.getOrigin();
             inputLines = lineReader.lines();
             inputLines.forEach(line->{processLine(line);});
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void processLine(String line) {
        Map<String, Object> jsonMap = null;
        String jsonValue="";
        TextDocument textDocument = null;
        StringBuilder value_builder = null;
        try {
             jsonMap = objectMapper.readValue(line, new TypeReference<Map<String, Object>>(){});
             for (String key:field_mappings.keySet()) {
                 jsonValue = (String) jsonMap.get(key);
                 value_builder = (StringBuilder)field_mappings.get(key);
                 value_builder.append(jsonValue);
                 textDocument = new TextDocument(id_builder.toString(), name_builder.toString(), "text", this.language, origin, contents_builder.toString());
                 id_builder.setLength(0);
                 name_builder.setLength(0);
                 contents_builder.setLength(0);
             }
        } catch (Exception exception) {
                exception.printStackTrace();
        }
    }
}