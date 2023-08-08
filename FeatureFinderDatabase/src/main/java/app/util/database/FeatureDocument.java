package app.util.database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.util.StreamUtils;

public class FeatureDocument {
    Integer id;
    String contents;
    String description;
    String type;
    String name;

    public FeatureDocument(Integer id, String name, String type, String contents, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.contents = contents;
    }

    public FeatureDocument(Integer id, String name, String type, InputStream contentsStream, String description) {
        ByteArrayOutputStream arrayOutputStream;
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        arrayOutputStream = new ByteArrayOutputStream();
        try {
            StreamUtils.copy(contentsStream, arrayOutputStream);
            this.contents = arrayOutputStream.toString(StandardCharsets.UTF_8.toString());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getName() {
        return this.name;
    }

    public void setType(String type) {
        this.type=type;
    }

    public String getType() {
        return this.type;
    }

    public void setContents(String contents) {
        this.contents=contents;
    }

    public String getContents() {
        return this.contents;
    }

    public String toString() {
        return "{\"id\":\""+id+"\",\"name\":\""+name+"\",\"type\":\""+type+"\",\"description\":\""+description+"\",\"contents\":\""+contents+"\"}";
    }
}