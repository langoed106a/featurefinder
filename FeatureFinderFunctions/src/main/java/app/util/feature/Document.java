package app.util.feature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Document {
    String id;
    String description;
    String label;
    String name;
    String origin;
    String type;
    String contents;

    public Document() {
       id="";
       description="";
       label="";
       name="";
       origin="";
       type="";
       contents="";
    }

    public Document(String id, String name, String type, String contents, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.contents = contents;
        this.description = description;
        this.origin = "";
        this.label = "";
    }

    public Document(Integer id, String name, String type, InputStream contentsStream, String description) {
		this.id=String.valueOf(id);
		this.name=name;
		this.type=type;
		this.description=description;
		ByteArrayOutputStream arrayOutputStream;
		arrayOutputStream = new ByteArrayOutputStream();
		try {
		      StreamUtils.copy(contentsStream, arrayOutputStream);
		      this.contents = arrayOutputStream.toString(StandardCharsets.UTF_8.toString());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOrigin(String origin) {
        this.origin=origin;
    }

    public String getOrigin() {
        return origin;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public String toJson() {
        String jsonStr = "";
        try {
            jsonStr = new ObjectMapper().writeValueAsString(this);
        } catch (Exception exception) {
                exception.printStackTrace();
        }
        return jsonStr;
    }

    public void fromJson(String jsonStr) {
         Document document = null;
         try {
              document = new ObjectMapper().readValue(jsonStr, Document.class);
              this.setId(document.getId());
              this.setType(document.getType());
              this.setLabel(document.getLabel());
              this.setOrigin(document.getOrigin());
              this.setName(document.getName());
              this.setContents(document.getContents());
              this.setDescription(document.getDescription());
         } catch (Exception exception) {
             exception.printStackTrace();
         }
    }
}