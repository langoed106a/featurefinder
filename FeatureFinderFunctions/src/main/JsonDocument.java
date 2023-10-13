package app.util.feature;

public class JsonDocument {
    String type;
    Document document;

    public JsonDocument() {

    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setContent(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
}