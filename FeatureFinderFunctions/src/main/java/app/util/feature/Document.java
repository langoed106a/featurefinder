package app.util.feature;

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
}